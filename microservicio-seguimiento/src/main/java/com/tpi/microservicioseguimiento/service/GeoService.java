package com.tpi.microservicioseguimiento.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// ¡Nuevos imports!
import com.tpi.microservicioseguimiento.entity.Camion;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.model.GeoResponse; // El DTO del PDF se llamaba DistanciaDTO, tú le pusiste GeoResponse
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO; // El DTO que acabamos de crear
import com.tpi.microservicioseguimiento.repository.CamionRepository;
import com.tpi.microservicioseguimiento.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List; // ¡Nuevo import!

@Service
public class GeoService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    // ¡Nuevos Repositorios!
    private final RestClient restClient;
    private final CamionRepository camionRepository;
    private final TarifaRepository tarifaRepository;
    private final ObjectMapper objectMapper; // Para procesar JSON

    // ¡Constructor MODIFICADO!
    public GeoService(RestClient.Builder builder, 
                      CamionRepository camionRepository, 
                      TarifaRepository tarifaRepository) {
        
        // El PDF creaba un RestClient nuevo; nosotros usamos el que Spring nos da (mejor práctica)
        this.restClient = builder.baseUrl("https://maps.googleapis.com/maps/api").build();
        this.camionRepository = camionRepository;
        this.tarifaRepository = tarifaRepository;
        this.objectMapper = new ObjectMapper(); // Instanciamos el procesador de JSON
    }

    /**
     * Este es el método que ya tenías (basado en el PDF).
     * Lo modificamos para que devuelva GeoResponse en lugar de DistanciaDTO
     * y use el objectMapper.
     */
    public GeoResponse calcularDistancia(String origen, String destino) throws Exception {
        
        String url = "/distancematrix/json?origins=" + origen +
                     "&destinations=" + destino +
                     "&units=metric&key=" + apiKey;

        ResponseEntity<String> response = restClient.get().uri(url).retrieve().toEntity(String.class);

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode leg = root.path("rows").get(0).path("elements").get(0);

        GeoResponse dto = new GeoResponse();
        dto.setOrigen(origen);
        dto.setDestino(destino);
        dto.setKilometros(leg.path("distance").path("value").asDouble() / 1000);
        dto.setDuracionTexto(leg.path("duration").path("text").asText());
        
        return dto;
    }

    // --- ¡MÉTODO NUEVO PARA LA LÓGICA DEL TPI! ---

    /**
     * Calcula una ruta sugerida con costo estimado.
     * Cumple con los requisitos del TPI[cite: 788, 847].
     */
    public RutaSugeridaDTO sugerirRuta(String origen, String destino, double peso, double volumen) throws Exception {
        
        // 1. Obtener distancia y duración de Google
        GeoResponse geoData = this.calcularDistancia(origen, destino);
        double distanciaKm = geoData.getKilometros();

        // 2. Obtener la configuración de tarifas (asumimos 1 sola fila de tarifas)
        // El TPI pide el precio del combustible y un cargo de gestión 
        Tarifa tarifa = tarifaRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("No hay tarifas configuradas en la base de datos."));
        
        double precioLitroCombustible = tarifa.getValorLitroCombustible();
        double cargoGestion = tarifa.getCargoGestion();

        // 3. Buscar camiones aptos
        // El TPI pide calcular en base a "camiones elegibles" 
        List<Camion> camionesAptos = camionRepository
                .findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso, volumen);

        if (camionesAptos.isEmpty()) {
            throw new Exception("No se encontraron camiones disponibles para ese peso/volumen.");
        }

        // 4. Calcular promedios (como pide el TPI [cite: 807, 847])
        double consumoPromedioKm = camionesAptos.stream()
                .mapToDouble(Camion::getConsumoCombustiblePromedio)
                .average()
                .orElse(0.0);
        
        double costoPromedioKm = camionesAptos.stream()
                .mapToDouble(Camion::getCostoBasePorKm)
                .average()
                .orElse(0.0);

        // 5. Calcular Costo Estimado (según reglas del TPI [cite: 845])
        double costoTramos = distanciaKm * costoPromedioKm;
        double costoCombustible = distanciaKm * consumoPromedioKm * precioLitroCombustible;
        
        // Costo Estimado = Gestión + Costo por KM + Costo Combustible
        double costoTotalEstimado = cargoGestion + costoTramos + costoCombustible;

        // 6. Armar la respuesta
        RutaSugeridaDTO sugerencia = new RutaSugeridaDTO();
        sugerencia.setOrigen(origen);
        sugerencia.setDestino(destino);
        sugerencia.setKilometros(distanciaKm);
        sugerencia.setDuracionTexto(geoData.getDuracionTexto());
        sugerencia.setCostoEstimado(costoTotalEstimado);

        return sugerencia;
    }
}