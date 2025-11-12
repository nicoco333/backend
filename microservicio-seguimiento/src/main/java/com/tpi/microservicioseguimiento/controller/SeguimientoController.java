package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.Deposito;
import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.enums.EstadoTramo;
import com.tpi.microservicioseguimiento.entity.enums.TipoTramo;
import com.tpi.microservicioseguimiento.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Esta es la importación que ya añadiste
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento") // URL base para este controlador
public class SeguimientoController {

    // ... (El constructor no cambia) ...
    private final DepositoRepository depositoRepository;
    private final RutaRepository rutaRepository;
    private final TarifaRepository tarifaRepository;
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;

    public SeguimientoController(DepositoRepository depositoRepository,
                                 RutaRepository rutaRepository,
                                 TarifaRepository tarifaRepository,
                                 TramoRepository tramoRepository,
                                 CamionRepository camionRepository) {
        this.depositoRepository = depositoRepository;
        this.rutaRepository = rutaRepository;
        this.tarifaRepository = tarifaRepository;
        this.tramoRepository = tramoRepository;
        this.camionRepository = camionRepository;
    }

    /**
     * [ADMIN] Endpoint para registrar un nuevo depósito.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/depositos")
    public ResponseEntity<Deposito> registrarDeposito(@RequestBody Deposito deposito) {
        Deposito nuevoDeposito = depositoRepository.save(deposito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDeposito);
    }

    /**
     * [ADMIN] Endpoint para listar todos los depósitos disponibles.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/depositos")
    public ResponseEntity<List<Deposito>> listarDepositos() {
        List<Deposito> depositos = depositoRepository.findAll();
        return ResponseEntity.ok(depositos);
    }
    
    /**
     * [ADMIN] Endpoint para registrar una nueva tarifa.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tarifas")
    public ResponseEntity<Tarifa> registrarTarifa(@RequestBody Tarifa nuevaTarifa) {
        Tarifa tarifaGuardada = tarifaRepository.save(nuevaTarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
    }

    /**
     * [ADMIN] Endpoint para asignar una ruta (con sus tramos) a una solicitud.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<Ruta> asignarRuta(@RequestBody Ruta ruta) {
        
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            return ResponseEntity.badRequest().build(); 
        }

        ruta.setCantidadTramos(ruta.getTramos().size());
        
        long depositosIntermedios = ruta.getTramos().stream()
                .filter(t -> t.getTipo() == TipoTramo.ORIGEN_DEPOSITO || 
                             t.getTipo() == TipoTramo.DEPOSITO_DEPOSITO)
                .count();
        ruta.setCantidadDepositos((int) depositosIntermedios);

        for (Tramo tramo : ruta.getTramos()) {
            tramo.setEstado(EstadoTramo.ASIGNADO);
        }

        Ruta rutaGuardada = rutaRepository.save(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
    }
}