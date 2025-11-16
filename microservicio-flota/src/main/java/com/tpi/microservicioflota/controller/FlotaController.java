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

    // El constructor sigue igual
    public FlotaController(CamionRepository camionRepository,
                           TransportistaRepository transportistaRepository,
                           TramoRepository tramoRepository) {
        this.camionRepository = camionRepository;
        this.transportistaRepository = transportistaRepository;
        this.tramoRepository = tramoRepository;
    }

    /**
     * [ADMIN] Endpoint para registrar un nuevo transportista.
     * La lógica no cambia.
     */
    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("/transportistas")
    public ResponseEntity<Transportista> registrarTransportista(@RequestBody Transportista transportista) {
        Transportista nuevoTransportista = transportistaRepository.save(transportista);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTransportista);
    }

    /**
     * [ADMIN] Endpoint para registrar un nuevo camión.
     * La lógica no cambia, pero el JSON que se envía debe coincidir
     * con la nueva entidad Camion (ej. "patente", "capacidadKg", etc.).
     */
    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("/camiones")
    public ResponseEntity<Camion> registrarCamion(@RequestBody Camion camion) {
        // Ejemplo de JSON esperado ahora:
        // {
        //   "patente": "ABC123Z",
        //   "capacidadKg": 20000.0,
        //   "capacidadM3": 80.0,
        //   "consumoGL": 0.3,
        //   "costo": 1.5,
        //   "disponibilidad": true,
        //   "transportista": {
        //     "idTransportista": 1 
        //   }
        // }
        Camion nuevoCamion = camionRepository.save(camion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCamion);
    }

    /**
     * [ADMIN] Endpoint para consultar camiones libres.
     * ¡MÉTODO ACTUALIZADO!
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/camiones/disponibles")
    public ResponseEntity<List<Camion>> obtenerCamionesDisponibles() {
        // Usamos el nuevo método del repositorio (disponibilidad en lugar de disponible)
        List<Camion> camionesDisponibles = camionRepository.findByDisponibilidadTrue();
        return ResponseEntity.ok(camionesDisponibles);
    }

    /**
     * [TRANSPORTISTA] Endpoint para registrar el inicio de un tramo.
     * ¡MÉTODO ACTUALIZADO!
     */
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

        // Lógica actualizada: setDisponibilidad
        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponibilidad(false); // <-- CAMBIO AQUÍ
            camionRepository.save(camionAsignado);
        }

        Tramo tramoActualizado = tramoRepository.save(tramo);
        return ResponseEntity.ok(tramoActualizado);
    }

    /**
     * [TRANSPORTISTA] Endpoint para registrar el fin de un tramo.
     * ¡MÉTODO ACTUALIZADO!
     */
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

        // Lógica actualizada: setDisponibilidad
        Camion camionAsignado = tramo.getCamion();
        if (camionAsignado != null) {
            camionAsignado.setDisponibilidad(true); // <-- CAMBIO AQUÍ
            camionRepository.save(camionAsignado);
        }

        Tramo tramoActualizado = tramoRepository.save(tramo);
        return ResponseEntity.ok(tramoActualizado);
    }
}