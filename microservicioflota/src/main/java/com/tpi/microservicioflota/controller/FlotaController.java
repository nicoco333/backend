package com.tpi.microservicioflota.controller;

import com.tpi.microservicioflota.entity.Camion;
import com.tpi.microservicioflota.entity.Transportista;
import com.tpi.microservicioflota.service.FlotaService; // Usamos el Service
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flota")
public class FlotaController {

    private final FlotaService flotaService; // Inyectamos el Service

    public FlotaController(FlotaService flotaService) {
        this.flotaService = flotaService;
    }

    // --- Gestión de Inventario (ADMIN / OPERADOR) ---

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @PostMapping("/transportistas")
    public ResponseEntity<Transportista> registrarTransportista(@RequestBody Transportista t) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flotaService.registrarTransportista(t));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @PostMapping("/camiones")
    public ResponseEntity<Camion> registrarCamion(@RequestBody Camion camion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flotaService.registrarCamion(camion));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @GetMapping("/camiones/disponibles")
    public ResponseEntity<List<Camion>> obtenerCamionesDisponibles() {
        return ResponseEntity.ok(flotaService.obtenerCamionesDisponibles());
    }

    // --- Endpoint INTERNO para comunicación entre servicios ---
    @PutMapping("/camiones/{patente}/disponibilidad")
    public ResponseEntity<?> actualizarDisponibilidad(@PathVariable String patente, @RequestParam boolean disponible) {
        boolean actualizado = flotaService.actualizarDisponibilidad(patente, disponible);
        if (actualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}