package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.*;
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.repository.*;
import com.tpi.microservicioseguimiento.service.GeoService;
import com.tpi.microservicioseguimiento.service.RutaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException; 

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    private static final Logger logger = LoggerFactory.getLogger(SeguimientoController.class);
    
    private final DepositoRepository depositoRepository;
    private final TarifaRepository tarifaRepository;
    private final EstadoRepository estadoRepository; 
    private final TipoTramoRepository tipoTramoRepository;
    private final GeoService geoService;
    private final RutaService rutaService;

    public SeguimientoController(DepositoRepository depositoRepository,
                                 TarifaRepository tarifaRepository,
                                 EstadoRepository estadoRepository, 
                                 TipoTramoRepository tipoTramoRepository,
                                 GeoService geoService,
                                 RutaService rutaService) { 
        this.depositoRepository = depositoRepository;
        this.tarifaRepository = tarifaRepository;
        this.estadoRepository = estadoRepository;
        this.tipoTramoRepository = tipoTramoRepository;
        this.geoService = geoService;
        this.rutaService = rutaService; 
    }

    // --- Endpoints de Datos Maestros ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/depositos")
    public ResponseEntity<Deposito> registrarDeposito(@RequestBody Deposito deposito) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositoRepository.save(deposito));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/depositos")
    public ResponseEntity<List<Deposito>> listarDepositos() {
        return ResponseEntity.ok(depositoRepository.findAll());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tarifas")
    public ResponseEntity<Tarifa> registrarTarifa(@RequestBody Tarifa nuevaTarifa) {
        Tarifa tarifa = tarifaRepository.findByNombre(nuevaTarifa.getNombre())
                .orElse(nuevaTarifa);
        tarifa.setCostoBase(nuevaTarifa.getCostoBase());
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaRepository.save(tarifa));
    }
    
    @PostMapping("/estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoRepository.save(estado));
    }

    @PostMapping("/tipos-tramo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTramo> crearTipoTramo(@RequestBody TipoTramo tipoTramo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoTramoRepository.save(tipoTramo));
    }

    // --- Endpoint de Asignar Ruta ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<?> asignarRuta(@RequestBody Ruta ruta) {
        try {
            Ruta rutaGuardada = rutaService.asignarRuta(ruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
        } catch (Exception e) {
            logger.error("Error al asignar ruta: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Error al asignar ruta");
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    // --- Endpoint de Google Maps (Solo Distancia) ---
    @GetMapping("/distancia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerDistancia(
            @RequestParam String origen,
            @RequestParam String destino) {
        try {
             GeoResponse dto = geoService.calcularDistancia(origen, destino);
             return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error al calcular distancia: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Error de API Externa");
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    // --- Endpoint de Sugerencia de Ruta (Cálculo de Costos) ---
    @GetMapping("/rutas/sugerir") // NOTA: El endpoint es 'sugerir', no 'sugeridas'
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENTE')")
    public ResponseEntity<?> sugerirRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam double peso,
            @RequestParam double volumen) {
        
        logger.info("Calculando ruta. Origen: {}, Destino: {}", origen, destino);
        
        try {
            // CORRECCIÓN: Llamamos a rutaService, que es quien tiene la lógica de costos
            RutaSugeridaDTO sugerencia = rutaService.sugerirRuta(origen, destino, peso, volumen);
            return ResponseEntity.ok(sugerencia);

        } catch (NoSuchElementException e) {
            logger.warn("Datos faltantes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error en sugerencia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}