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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    // Repositorios
    private final DepositoRepository depositoRepository;
    private final TarifaRepository tarifaRepository;
    private final EstadoRepository estadoRepository; // ¡NECESARIO AHORA!
    private final TipoTramoRepository tipoTramoRepository; // ¡NECESARIO AHORA!
    
    // Servicios
    private final GeoService geoService;
    private final RutaService rutaService;

    // --- ¡CONSTRUCTOR ACTUALIZADO! ---
    public SeguimientoController(DepositoRepository depositoRepository,
                                 TarifaRepository tarifaRepository,
                                 EstadoRepository estadoRepository, // ¡Inyectado!
                                 TipoTramoRepository tipoTramoRepository, // ¡Inyectado!
                                 GeoService geoService,
                                 RutaService rutaService) { 
        this.depositoRepository = depositoRepository;
        this.tarifaRepository = tarifaRepository;
        this.estadoRepository = estadoRepository; // ¡Asignado!
        this.tipoTramoRepository = tipoTramoRepository; // ¡Asignado!
        this.geoService = geoService;
        this.rutaService = rutaService; 
    }

    // --- Endpoints de Depósitos y Tarifas (sin cambios) ---

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

    // --- Endpoint de Asignar Ruta (sin cambios) ---

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<Ruta> asignarRuta(@RequestBody Ruta ruta) {
        Ruta rutaGuardada = rutaService.asignarRuta(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
    }

    // --- Endpoints de Google Maps (sin cambios) ---

    @GetMapping("/distancia")
    @PreAuthorize("hasRole('ADMIN')")
    public GeoResponse obtenerDistancia(
            @RequestParam String origen,
            @RequestParam String destino) throws Exception {
        return geoService.calcularDistancia(origen, destino);
    }

    @GetMapping("/rutas/sugeridas")
    @PreAuthorize("hasRole('ADMIN')")
    public RutaSugeridaDTO sugerirRuta(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam double peso,
            @RequestParam double volumen) throws Exception {
        return geoService.sugerirRuta(origen, destino, peso, volumen);
    }

    // --- ¡NUEVOS ENDPOINTS DE DATOS MAESTROS! ---

    /**
     * [ADMIN] Crea un nuevo Estado en el sistema.
     * Ejemplo JSON: {"nombre": "BORRADOR", "ambito": "SOLICITUD"}
     */
    @PostMapping("/estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado) {
        Estado nuevoEstado = estadoRepository.save(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    /**
     * [ADMIN] Crea un nuevo Tipo de Tramo en el sistema.
     * Ejemplo JSON: {"nombre": "ORIGEN-DESTINO", "descripcion": "..."}
     */
    @PostMapping("/tipos-tramo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTramo> crearTipoTramo(@RequestBody TipoTramo tipoTramo) {
        TipoTramo nuevoTipoTramo = tipoTramoRepository.save(tipoTramo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoTramo);
    }
}