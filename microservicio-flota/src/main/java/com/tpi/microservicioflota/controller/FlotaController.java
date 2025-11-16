package com.tpi.microservicioflota.controller;

import com.tpi.microservicioflota.entity.Camion;
import com.tpi.microservicioflota.entity.Tramo;
import com.tpi.microservicioflota.entity.Transportista;
import com.tpi.microservicioflota.entity.Estado; // ¡Cambiado!
import com.tpi.microservicioflota.repository.CamionRepository;
import com.tpi.microservicioflota.repository.TramoRepository;
import com.tpi.microservicioflota.repository.TransportistaRepository;
import com.tpi.microservicioflota.repository.EstadoRepository; // ¡Nuevo!
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flota")
public class FlotaController {

    private final CamionRepository camionRepository;
    private final TransportistaRepository transportistaRepository;
    private final TramoRepository tramoRepository;
    private final EstadoRepository estadoRepository; // ¡Inyectado!

    // ¡Constructor actualizado!
    public FlotaController(CamionRepository camionRepository,
                           TransportistaRepository transportistaRepository,
                           TramoRepository tramoRepository,
                           EstadoRepository estadoRepository) { // ¡Inyectado!
        this.camionRepository = camionRepository;
        this.transportistaRepository = transportistaRepository;
        this.tramoRepository = tramoRepository;
        this.estadoRepository = estadoRepository; // ¡Asignado!
    }

    // ... (Endpoints /transportistas y /camiones no cambian) ...

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transportistas")
    public ResponseEntity<Transportista> registrarTransportista(@RequestBody Transportista t) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transportistaRepository.save(t));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/camiones")
    public ResponseEntity<Camion> registrarCamion(@RequestBody Camion camion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camionRepository.save(camion));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/camiones/disponibles")
    public ResponseEntity<List<Camion>> obtenerCamionesDisponibles() {
        return ResponseEntity.ok(camionRepository.findByDisponibilidadTrue());
    }

    // --- ¡LÓGICA DE ESTADO ACTUALIZADA! ---

    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PutMapping("/tramos/{id}/inicio")
    public ResponseEntity<Tramo> registrarInicioTramo(@PathVariable Long id) {
        
        // 1. Busca el estado "INICIADO" en la base de datos
        Estado estadoIniciado = estadoRepository.findByNombre("INICIADO")
                .orElseThrow(() -> new RuntimeException("Estado 'INICIADO' no encontrado."));

        // 2. Busca el tramo
        Tramo tramo = tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado."));

        // 3. Asigna los nuevos valores
        tramo.setEstado(estadoIniciado); // ¡Usa la entidad!
        tramo.setFechaHoraInicio(LocalDateTime.now());

        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponibilidad(false);
            camionRepository.save(camionAsignado);
        }

        return ResponseEntity.ok(tramoRepository.save(tramo));
    }

    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PutMapping("/tramos/{id}/fin")
    public ResponseEntity<Tramo> registrarFinTramo(@PathVariable Long id) {
        
        // 1. Busca el estado "FINALIZADO" en la base de datos
        Estado estadoFinalizado = estadoRepository.findByNombre("FINALIZADO")
                .orElseThrow(() -> new RuntimeException("Estado 'FINALIZADO' no encontrado."));

        // 2. Busca el tramo
        Tramo tramo = tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado."));

        // 3. Asigna los nuevos valores
        tramo.setEstado(estadoFinalizado); // ¡Usa la entidad!
        tramo.setFechaHoraFin(LocalDateTime.now());

        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponibilidad(true);
            camionRepository.save(camionAsignado);
        }

        return ResponseEntity.ok(tramoRepository.save(tramo));
    }
}