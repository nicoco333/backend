package com.tpi.microservicio_cliente.controller;

import com.tpi.microservicio_cliente.entity.Cliente;
import com.tpi.microservicio_cliente.entity.Contenedor;
import com.tpi.microservicio_cliente.entity.SolicitudTraslado;
import com.tpi.microservicio_cliente.entity.enums.EstadoSolicitud;
import com.tpi.microservicio_cliente.repository.ClienteRepository;
import com.tpi.microservicio_cliente.repository.ContenedorRepository;
import com.tpi.microservicio_cliente.repository.SolicitudTrasladoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Esta es la importación que ya añadiste
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente") 
public class ClienteController {

    // ... (El constructor no cambia) ...
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final SolicitudTrasladoRepository solicitudTrasladoRepository;

    public ClienteController(ClienteRepository clienteRepository,
                             ContenedorRepository contenedorRepository,
                             SolicitudTrasladoRepository solicitudTrasladoRepository) {
        this.clienteRepository = clienteRepository;
        this.contenedorRepository = contenedorRepository;
        this.solicitudTrasladoRepository = solicitudTrasladoRepository;
    }

    // --- Endpoints para CLIENTE ---

    /**
     * [CLIENTE] Endpoint para registrar un nuevo cliente.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    /**
     * [CLIENTE] Endpoint para crear una nueva solicitud de traslado.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/solicitudes")
    public ResponseEntity<SolicitudTraslado> crearSolicitud(@RequestBody SolicitudTraslado solicitud) {
        solicitud.setEstado(EstadoSolicitud.BORRADOR);
        SolicitudTraslado nuevaSolicitud = solicitudTrasladoRepository.save(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }

    /**
     * [CLIENTE] Endpoint para ver el estado de su solicitud.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<SolicitudTraslado> verEstadoSolicitud(@PathVariable Long id) {
        Optional<SolicitudTraslado> solicitudOptional = solicitudTrasladoRepository.findById(id);

        if (solicitudOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(solicitudOptional.get());
    }

    // --- Endpoints para ADMIN ---

    /**
     * [ADMIN] Endpoint para listar todas las solicitudes de traslado.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudTraslado>> listarTodasLasSolicitudes() {
        List<SolicitudTraslado> solicitudes = solicitudTrasladoRepository.findAll();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * [ADMIN] Endpoint para filtrar contenedores pendientes.
     */
    // <-- ¡NUEVO!
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/contenedores/pendientes")
    public ResponseEntity<List<Contenedor>> filtrarContenedoresPendientes() {
        
        List<EstadoSolicitud> estadosPendientes = Arrays.asList(
                EstadoSolicitud.BORRADOR, 
                EstadoSolicitud.PROGRAMADA
        );
        List<SolicitudTraslado> solicitudesPendientes = 
                solicitudTrasladoRepository.findByEstadoIn(estadosPendientes);
        List<Contenedor> contenedoresPendientes = solicitudesPendientes.stream()
                .map(SolicitudTraslado::getContenedor)
                .collect(Collectors.toList());

        return ResponseEntity.ok(contenedoresPendientes);
    }

} // <-- Llave de cierre final de la clase