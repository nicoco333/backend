package com.tpi.microserviciocliente.controller;

import com.tpi.microserviciocliente.entity.Cliente;
import com.tpi.microserviciocliente.service.ClienteService; // Usamos el Service
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService; // Inyectamos el Service

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // SOLO OPERADOR/ADMIN pueden registrar clientes
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.registrarCliente(cliente));
    }

    // Endpoint de comunicación entre microservicios
    @GetMapping("/clientes/buscar")
    public ResponseEntity<Cliente> buscarPorEmail(@RequestParam String email) {
        return clienteService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
}