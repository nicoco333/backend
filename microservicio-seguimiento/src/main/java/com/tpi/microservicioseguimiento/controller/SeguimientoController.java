package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.Deposito;
import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.enums.EstadoTramo;
import com.tpi.microservicioseguimiento.entity.enums.TipoTramo;
import com.tpi.microservicioseguimiento.repository.*;

// --- ¡NUEVOS IMPORTS! ---
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.service.GeoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento") // URL base para este controlador
public class SeguimientoController {

    // Repositorios
    private final DepositoRepository depositoRepository;
    private final RutaRepository rutaRepository;
    private final TarifaRepository tarifaRepository;
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;
    
    // --- ¡NUEVO SERVICIO INYECTADO! ---
    private final GeoService geoService;

    // --- ¡CONSTRUCTOR ACTUALIZADO! ---
    public SeguimientoController(DepositoRepository depositoRepository,
                                 RutaRepository rutaRepository,
                                 TarifaRepository tarifaRepository,
                                 TramoRepository tramoRepository,
                                 CamionRepository camionRepository,
                                 GeoService geoService) { // <-- Añadido
        this.depositoRepository = depositoRepository;
        this.rutaRepository = rutaRepository;
        this.tarifaRepository = tarifaRepository;
        this.tramoRepository = tramoRepository;
        this.camionRepository = camionRepository;
        this.geoService = geoService; // <-- Añadido
    }

    // --- Endpoints de Depósitos y Tarifas (los que ya tenías) ---

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/depositos")
    public ResponseEntity<Deposito> registrarDeposito(@RequestBody Deposito deposito) {
        Deposito nuevoDeposito = depositoRepository.save(deposito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDeposito);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/depositos")
    public ResponseEntity<List<Deposito>> listarDepositos() {
        List<Deposito> depositos = depositoRepository.findAll();
        return ResponseEntity.ok(depositos);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tarifas")
    public ResponseEntity<Tarifa> registrarTarifa(@RequestBody Tarifa nuevaTarifa) {
        Tarifa tarifaGuardada = tarifaRepository.save(nuevaTarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
    }

    // --- Endpoint de Asignar Ruta (el que ya tenías) ---

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<Ruta> asignarRuta(@RequestBody Ruta ruta) {
        // ... (lógica de asignar ruta que ya tenías) ...
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

    // --- ¡NUEVOS ENDPOINTS DE GOOGLE MAPS! ---

    /**
     * [ADMIN] Endpoint para calcular distancia entre dos puntos (de tu compañero).
     */
    @GetMapping("/distancia")
    @PreAuthorize("hasRole('ADMIN')")
    public GeoResponse obtenerDistancia(
            @RequestParam String origen,
            @RequestParam String destino) throws Exception {
        
        return geoService.calcularDistancia(origen, destino);
    }

    /**
     * [ADMIN] Endpoint para consultar rutas tentativas con costo.
     * (Requisito del TPI)
     */
    @GetMapping("/rutas/sugeridas")
    @PreAuthorize("hasRole('ADMIN')")
    public RutaSugeridaDTO sugerirRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam double peso,
            @RequestParam double volumen) throws Exception {
        
        return geoService.sugerirRuta(origen, destino, peso, volumen);
    }
}