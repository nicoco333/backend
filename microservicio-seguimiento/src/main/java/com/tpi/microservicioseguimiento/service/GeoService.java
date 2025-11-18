package com.tpi.microservicioseguimiento.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpi.microservicioseguimiento.model.GeoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeoService {

    private static final Logger logger = LoggerFactory.getLogger(GeoService.class);

    // Inyectamos tu clave real. Si falla la inyección, usa la hardcodeada por seguridad.
    @Value("${google.maps.api.key:AIzaSyCVUJTGkUt0iaXKXTOREr80OQZTdUnsxEM}")
    private String apiKey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GeoService(RestClient.Builder builder, ObjectMapper objectMapper) {
        this.restClient = builder.baseUrl("https://maps.googleapis.com/maps/api").build();
        this.objectMapper = objectMapper;
    }

    /**
     * Calcula la distancia usando la API Real de Google Maps.
     */
    public GeoResponse calcularDistancia(String origen, String destino) throws Exception {
        
        logger.info("Consultando Google Maps API para ruta: {} -> {}", origen, destino);

        String url = "/distancematrix/json?origins=" + origen +
                     "&destinations=" + destino +
                     "&units=metric&key=" + apiKey;

        try {
            ResponseEntity<String> response = restClient.get().uri(url).retrieve().toEntity(String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            // 1. Validar estado general de la petición
            if (!"OK".equals(root.path("status").asText())) {
                throw new Exception("Error Google Maps API: " + root.path("status").asText() + 
                                    " - " + root.path("error_message").asText());
            }

            // 2. Validar si encontró ruta
            JsonNode element = root.path("rows").get(0).path("elements").get(0);
            if (!"OK".equals(element.path("status").asText())) {
                throw new Exception("Ruta no encontrada: " + element.path("status").asText());
            }

            // 3. Extraer datos
            GeoResponse dto = new GeoResponse();
            dto.setOrigen(origen);
            dto.setDestino(destino);
            // Distancia viene en metros -> pasamos a KM
            dto.setKilometros(element.path("distance").path("value").asDouble() / 1000.0);
            dto.setDuracionTexto(element.path("duration").path("text").asText());
            
            return dto;

        } catch (Exception e) {
            logger.error("Fallo al conectar con Google Maps: {}", e.getMessage());
            throw e; // Relanzamos para que el controller sepa que falló
        }
    }
    
    // Alias para compatibilidad con RutaService (ambos llaman a lo mismo)
    public GeoResponse calcularDistanciaEstimada(String origen, String destino) throws Exception {
        return calcularDistancia(origen, destino);
    }
    
    public GeoResponse calcularDistanciaFinal(String origen, String destino) throws Exception {
        return calcularDistancia(origen, destino);
    }
}