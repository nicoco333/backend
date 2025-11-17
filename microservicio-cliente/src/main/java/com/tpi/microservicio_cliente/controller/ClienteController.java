package com.tpi.microservicio_cliente.controller;

import com.tpi.microservicio_cliente.entity.Cliente;
import com.tpi.microservicio_cliente.entity.Contenedor;
import com.tpi.microservicio_cliente.entity.SolicitudTraslado;
import com.tpi.microservicio_cliente.entity.Estado; // ¡Import cambiado!
import com.tpi.microservicio_cliente.repository.ClienteRepository;
import com.tpi.microservicio_cliente.repository.ContenedorRepository;
import com.tpi.microservicio_cliente.repository.SolicitudTrasladoRepository;
import com.tpi.microservicio_cliente.repository.EstadoRepository; // ¡Import nuevo!

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate; // ¡Import nuevo!
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    // ¡Repositorios actualizados!
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final SolicitudTrasladoRepository solicitudTrasladoRepository;
    private final EstadoRepository estadoRepository; // ¡Repo nuevo!

    // ¡Constructor actualizado!
    public ClienteController(ClienteRepository clienteRepository,
                             ContenedorRepository contenedorRepository,
                             SolicitudTrasladoRepository solicitudTrasladoRepository,
                             EstadoRepository estadoRepository) {
        this.clienteRepository = clienteRepository;
        this.contenedorRepository = contenedorRepository;
        this.solicitudTrasladoRepository = solicitudTrasladoRepository;
        this.estadoRepository = estadoRepository;
    }

    /**
     * [CLIENTE] Endpoint para registrar un nuevo cliente.
     */
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        logger.info("Registrando nuevo cliente: {}", cliente.getNombre());
        Cliente nuevoCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    /**
     * [CLIENTE] Endpoint para registrar un nuevo contenedor.
     * (Este endpoint es nuevo, pero necesario según el DER)
     */
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/contenedores")
    public ResponseEntity<Contenedor> registrarContenedor(@RequestBody Contenedor contenedor) {
        Long idCliente = (contenedor.getCliente() != null) ? contenedor.getCliente().getIdCliente() : null;
        logger.info("Registrando nuevo contenedor. Peso: {}, Cliente ID: {}", contenedor.getPeso(), idCliente);
        // Asumimos que el JSON viene con el idCliente
        // { "peso": 5000, "volumen": 30, "cliente": { "idCliente": 1 } }
        Contenedor nuevoContenedor = contenedorRepository.save(contenedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContenedor);
    }

    /**
     * [CLIENTE] Endpoint para crear una nueva solicitud de traslado.
     * ¡Lógica MUY actualizada!
     */
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudTraslado solicitud) {
        // Log de operación importante
        Long idCliente = (solicitud.getCliente() != null) ? solicitud.getCliente().getIdCliente() : null;
        logger.info("Iniciando nueva solicitud de traslado para Cliente ID: {}", idCliente);

        // 1. Buscar el estado inicial "BORRADOR"
        Estado estadoBorrador = estadoRepository.findByNombre("BORRADOR")
                .orElseThrow(() -> new RuntimeException("Error: Estado 'BORRADOR' no encontrado."));

        // 2. Asignar el estado y la fecha
        solicitud.setEstado(estadoBorrador);
        solicitud.setFecha(LocalDate.now());
        
        // (Asumimos que el cliente y contenedor vienen seteados en el JSON)
        
        SolicitudTraslado nuevaSolicitud = solicitudTrasladoRepository.save(solicitud);
        logger.info("Solicitud creada con éxito. ID: {}", nuevaSolicitud.getIdSolicitud());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }

    /**
     * [CLIENTE] Endpoint para ver el estado de su solicitud.
     */
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<SolicitudTraslado> verEstadoSolicitud(@PathVariable Long id) {
        Optional<SolicitudTraslado> solicitudOptional = solicitudTrasladoRepository.findById(id);
        return solicitudOptional.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoints para ADMIN ---

    /**
     * [ADMIN] Endpoint para listar todas las solicitudes de traslado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudTraslado>> listarTodasLasSolicitudes() {
        logger.info("Administrador consultando todas las solicitudes");
        List<SolicitudTraslado> solicitudes = solicitudTrasladoRepository.findAll();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * [ADMIN] Endpoint para filtrar contenedores de solicitudes pendientes.
     * ¡Lógica ACTUALIZADA!
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/contenedores/pendientes")
    public ResponseEntity<List<Contenedor>> filtrarContenedoresPendientes() {

        logger.info("Filtrando contenedores pendientes de retiro/entrega");
        
        // 1. Buscar los estados "pendientes" en la BD
        Estado borrador = estadoRepository.findByNombre("BORRADOR").orElse(null);
        Estado programada = estadoRepository.findByNombre("PROGRAMADA").orElse(null);

        if (borrador == null || programada == null) {
            logger.error("Estados requeridos no encontrados para el filtro");
            // No podemos filtrar si los estados no existen
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 2. Buscamos las SOLICITUDES que están pendientes
        List<SolicitudTraslado> solicitudesPendientes = 
                solicitudTrasladoRepository.findByEstadoIn(Arrays.asList(borrador, programada));

        // 3. Extraemos los CONTENEDORES de esas solicitudes
        List<Contenedor> contenedoresPendientes = solicitudesPendientes.stream()
                .map(SolicitudTraslado::getContenedor)
                .collect(Collectors.toList());

        return ResponseEntity.ok(contenedoresPendientes);
    }
}