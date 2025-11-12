package com.tpi.microservicioflota.controller;

import com.tpi.microservicioflota.entity.Camion;
import com.tpi.microservicioflota.entity.Tramo;
import com.tpi.microservicioflota.entity.Transportista;
import com.tpi.microservicioflota.entity.enums.EstadoTramo;
import com.tpi.microservicioflota.repository.CamionRepository;
import com.tpi.microservicioflota.repository.TramoRepository;
import com.tpi.microservicioflota.repository.TransportistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Esta es la importación que ya añadiste
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flota")
public class FlotaController {

    // ... (El constructor no cambia) ...
    private final CamionRepository camionRepository;
    private final TransportistaRepository transportistaRepository;
    private final TramoRepository tramoRepository;

    public FlotaController(CamionRepository camionRepository,
                           TransportistaRepository transportistaRepository,
                           TramoRepository tramoRepository) {
        this.camionRepository = camionRepository;
        this.transportistaRepository = transportistaRepository;
        this.tramoRepository = tramoRepository;
    }

    // --- Endpoints Protegidos ---

    /**
     * [ADMIN] Endpoint para registrar un nuevo transportista.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("/transportistas")
    public ResponseEntity<Transportista> registrarTransportista(@RequestBody Transportista transportista) {
        Transportista nuevoTransportista = transportistaRepository.save(transportista);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTransportista);
    }

    /**
     * [ADMIN] Endpoint para registrar un nuevo camión. 
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("/camiones")
    public ResponseEntity<Camion> registrarCamion(@RequestBody Camion camion) {
        Camion nuevoCamion = camionRepository.save(camion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCamion);
    }

    /**
     * [ADMIN] Endpoint para consultar camiones libres. 
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/camiones/disponibles")
    public ResponseEntity<List<Camion>> obtenerCamionesDisponibles() {
        List<Camion> camionesDisponibles = camionRepository.findByDisponibleTrue();
        return ResponseEntity.ok(camionesDisponibles);
    }

    /**
     * [TRANSPORTISTA] Endpoint para registrar el inicio de un tramo. 
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PutMapping("/tramos/{id}/inicio")
    public ResponseEntity<Tramo> registrarInicioTramo(@PathVariable Long id) {
        
        Optional<Tramo> tramoOptional = tramoRepository.findById(id);
        if (tramoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tramo tramo = tramoOptional.get();
        tramo.setEstado(EstadoTramo.INICIADO);
        tramo.setFechaHoraInicio(LocalDateTime.now());

        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponible(false);
            camionRepository.save(camionAsignado);
        }

        Tramo tramoActualizado = tramoRepository.save(tramo);
        return ResponseEntity.ok(tramoActualizado);
    }

    /**
     * [TRANSPORTISTA] Endpoint para registrar el fin de un tramo. 
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PutMapping("/tramos/{id}/fin")
    public ResponseEntity<Tramo> registrarFinTramo(@PathVariable Long id) {
        
        Optional<Tramo> tramoOptional = tramoRepository.findById(id);
        if (tramoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tramo tramo = tramoOptional.get();
        tramo.setEstado(EstadoTramo.FINALIZADO);
        tramo.setFechaHoraFin(LocalDateTime.now());

        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponible(true);
            camionRepository.save(camionAsignado);
        }

        Tramo tramoActualizado = tramoRepository.save(tramo);
        return ResponseEntity.ok(tramoActualizado);
    }
}