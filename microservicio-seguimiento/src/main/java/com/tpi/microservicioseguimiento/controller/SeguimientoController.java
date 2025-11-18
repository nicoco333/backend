package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.*;
import com.tpi.microservicioseguimiento.repository.*;
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.service.GeoService;
import com.tpi.microservicioseguimiento.service.RutaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
// [CORRECCIÓN] Imports necesarios para el manejo de error
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

    // --- Endpoints de Datos Maestros (POST /estados, POST /tarifas, etc.) ---
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
        Tarifa tarifa = tarifaRepository.findByNombre(nuevaTarifa.getNombre())
                .orElse(nuevaTarifa);
        tarifa.setCostoBase(nuevaTarifa.getCostoBase());
        Tarifa tarifaGuardada = tarifaRepository.save(tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
    }
    
    @PostMapping("/estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado) {
        Estado nuevoEstado = estadoRepository.save(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    @PostMapping("/tipos-tramo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTramo> crearTipoTramo(@RequestBody TipoTramo tipoTramo) {
        TipoTramo nuevoTipoTramo = tipoTramoRepository.save(tipoTramo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoTramo);
    }


    // --- Endpoint de Asignar Ruta ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<?> asignarRuta(@RequestBody Ruta ruta) {
        // [CORRECCIÓN] try-catch para manejar si falta el estado "ASIGNADO"
        try {
            Ruta rutaGuardada = rutaService.asignarRuta(ruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
        } catch (NoSuchElementException e) {
            logger.error("Error al asignar ruta: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Datos Faltantes");
            body.put("message", e.getMessage()); // ej. "Estado 'ASIGNADO' no encontrado"
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    // --- Endpoints de Google Maps ---
    @GetMapping("/distancia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerDistancia(
            @RequestParam String origen,
            @RequestParam String destino) {
        try {
             GeoResponse dto = geoService.calcularDistancia(origen, destino);
             return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // [CORRECCIÓN] Atrapa el error si la API Key falla
            logger.error("Error al calcular distancia: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Error de API Externa");
            body.put("message", "Fallo al conectar con Google Maps: " + e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    // [CORRECCIÓN] Se agrega try-catch para manejar el 500 y devolver 404
    @GetMapping("/rutas/sugeridas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sugerirRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam double peso,
            @RequestParam double volumen) {
        
        logger.info("Calculando ruta sugerida y costos. Origen: {}, Destino: {}, Peso: {}, Volumen: {}", 
                     origen, destino, peso, volumen);
        
        try {
            RutaSugeridaDTO sugerencia = geoService.sugerirRuta(origen, destino, peso, volumen);
            return ResponseEntity.ok(sugerencia);

        } catch (NoSuchElementException e) {
            // ¡Atrapamos el error de "No hay camiones" o "Faltan tarifas"!
            logger.warn("No se pudo calcular la ruta: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "No Encontrado");
            body.put("message", e.getMessage()); // "No se encontraron camiones disponibles..."
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            // Atrapa cualquier otro error (ej. Google Maps explotó)
            logger.error("Error inesperado en sugerirRuta: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Error Interno");
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}