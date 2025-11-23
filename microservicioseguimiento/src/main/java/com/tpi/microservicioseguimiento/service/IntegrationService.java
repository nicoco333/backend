package com.tpi.microservicioseguimiento.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class IntegrationService {

    private final RestClient restClient;

    // URLs apuntan al Gateway o nombres de docker interno
    @Value("${app.ms-cliente.url:http://localhost:8085/api/cliente}")
    private String msClienteUrl;

    public IntegrationService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public Long obtenerIdClientePorEmail(String email) {
        try {
            // Llama a MS-Cliente: GET /api/cliente/clientes/buscar?email=...
            Map response = restClient.get()
                    .uri(msClienteUrl + "/clientes/buscar?email=" + email)
                    .retrieve()
                    .body(Map.class);
            
            if (response != null && response.containsKey("idCliente")) {
                return ((Number) response.get("idCliente")).longValue();
            }
        } catch (Exception e) {
            return null; // No encontrado o error
        }
        return null;
    }
}