package com.tpi.microservicioseguimiento.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpi.microservicioseguimiento.entity.Camion;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.repository.CamionRepository;
import com.tpi.microservicioseguimiento.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeoService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestClient restClient;
    private final CamionRepository camionRepository;
    private final TarifaRepository tarifaRepository;
    private final ObjectMapper objectMapper;

    // --- ¡CONSTRUCTOR ACTUALIZADO! ---
    // Inyectamos todas las dependencias que necesitamos
    public GeoService(RestClient.Builder builder,
                      CamionRepository camionRepository,
                      TarifaRepository tarifaRepository,
                      ObjectMapper objectMapper) {
        
        this.restClient = builder.baseUrl("https://maps.googleapis.com/maps/api").build();
        this.camionRepository = camionRepository;
        this.tarifaRepository = tarifaRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Calcula la distancia y duración (simple) desde Google Maps.
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
        dto.setKilometros(leg.path("distance").path("value").asDouble() / 1000); // Convertir metros a km
        dto.setDuracionTexto(leg.path("duration").path("text").asText());
        
        return dto;
    }


    // --- ¡LÓGICA DE COSTOS TOTALMENTE REFACTORIZADA! ---
    // Este método ahora usa los nombres de tu DER.
    
    /**
     * Calcula una ruta sugerida con costo estimado, alineado con el DER.
     */
    public RutaSugeridaDTO sugerirRuta(String origen, String destino, double peso, double volumen) throws Exception {
        
        // 1. Obtener distancia y duración de Google
        GeoResponse geoData = this.calcularDistancia(origen, destino);
        double distanciaKm = geoData.getKilometros();

        // 2. Obtener la configuración de tarifas desde la BD (usando el nuevo DER)
        // Buscamos cada tarifa por su nombre.
        Tarifa tarifaComb = tarifaRepository.findByNombre("valor_litro_combustible")
                .orElseThrow(() -> new Exception("Tarifa 'valor_litro_combustible' no encontrada."));
        
        Tarifa tarifaGest = tarifaRepository.findByNombre("cargo_gestion")
                .orElseThrow(() -> new Exception("Tarifa 'cargo_gestion' no encontrada."));

        double precioLitroCombustible = tarifaComb.getCostoBase();
        double cargoGestion = tarifaGest.getCostoBase();

        // 3. Buscar camiones aptos (¡usando los nuevos nombres de campo!)
        List<Camion> camionesAptos = camionRepository
                .findByDisponibilidadTrueAndCapacidadKgGreaterThanEqualAndCapacidadM3GreaterThanEqual(peso, volumen);

        if (camionesAptos.isEmpty()) {
            throw new Exception("No se encontraron camiones disponibles para ese peso/volumen.");
        }

        // 4. Calcular promedios (¡usando los nuevos getters!)
        double consumoPromedioKm = camionesAptos.stream()
                .mapToDouble(Camion::getConsumoGL) // <-- CAMBIO AQUÍ
                .average()
                .orElse(0.0);
        
        double costoPromedioKm = camionesAptos.stream()
                .mapToDouble(Camion::getCosto) // <-- CAMBIO AQUÍ
                .average()
                .orElse(0.0);

        // 5. Calcular Costo Estimado (según reglas del TPI)
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