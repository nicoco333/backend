package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.*; // Importa todas las entidades
import com.tpi.microservicioseguimiento.repository.*; // Importa todos los repos
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.service.GeoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento")
public class SeguimientoController {

    // Todos los repositorios
    private final DepositoRepository depositoRepository;
    private final RutaRepository rutaRepository;
    private final TarifaRepository tarifaRepository;
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;
    private final EstadoRepository estadoRepository; // ¡Nuevo repo!
    private final TipoTramoRepository tipoTramoRepository; // ¡Nuevo repo!
    
    // El GeoService
    private final GeoService geoService;

    // --- ¡CONSTRUCTOR ACTUALIZADO! ---
    public SeguimientoController(DepositoRepository depositoRepository,
                                 RutaRepository rutaRepository,
                                 TarifaRepository tarifaRepository,
                                 TramoRepository tramoRepository,
                                 CamionRepository camionRepository,
                                 EstadoRepository estadoRepository, // ¡Inyectado!
                                 TipoTramoRepository tipoTramoRepository, // ¡Inyectado!
                                 GeoService geoService,
                                 ObjectMapper objectMapper) { // ObjectMapper también
        this.depositoRepository = depositoRepository;
        this.rutaRepository = rutaRepository;
        this.tarifaRepository = tarifaRepository;
        this.tramoRepository = tramoRepository;
        this.camionRepository = camionRepository;
        this.estadoRepository = estadoRepository; // ¡Asignado!
        this.tipoTramoRepository = tipoTramoRepository; // ¡Asignado!
        this.geoService = geoService;
    }

    // --- Endpoints de Depósitos (Sin cambios) ---

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
    
    // --- Endpoint de Tarifas (¡Lógica actualizada al DER!) ---
    
    /**
     * [ADMIN] Crea o actualiza una tarifa por su nombre.
     * El DER indica que las tarifas se guardan por nombre.
     * Ejemplo de JSON: {"nombre": "cargo_gestion", "costoBase": 75.0}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tarifas")
    public ResponseEntity<Tarifa> registrarTarifa(@RequestBody Tarifa nuevaTarifa) {
        // Busca si ya existe una tarifa con ese nombre
        Tarifa tarifa = tarifaRepository.findByNombre(nuevaTarifa.getNombre())
                .orElse(nuevaTarifa); // Si no existe, usa la nueva
        
        // Actualiza el costo
        tarifa.setCostoBase(nuevaTarifa.getCostoBase());
        
        Tarifa tarifaGuardada = tarifaRepository.save(tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
    }

    // --- Endpoint de Asignar Ruta (¡Lógica actualizada al DER!) ---

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<Ruta> asignarRuta(@RequestBody Ruta ruta) {
        
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            return ResponseEntity.badRequest().build(); 
        }

        // 1. Busca el estado "ASIGNADO" en la base de datos
        Estado estadoAsignado = estadoRepository.findByNombre("ASIGNADO")
                .orElseThrow(() -> new RuntimeException("Estado 'ASIGNADO' no encontrado."));

        // 2. Asigna las relaciones (JPA lo necesita)
        for (Tramo tramo : ruta.getTramos()) {
            tramo.setRuta(ruta); // Asigna la ruta padre a cada tramo
            tramo.setEstado(estadoAsignado); // ¡Usa la entidad Estado!
            
            // (La lógica para asignar TipoTramo, Camion y Depósitos
            // debería venir en el JSON de la petición)
        }
        
        Ruta rutaGuardada = rutaRepository.save(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
    }

    // --- Endpoints de Google Maps (Sin cambios, ya usan GeoService) ---

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
}