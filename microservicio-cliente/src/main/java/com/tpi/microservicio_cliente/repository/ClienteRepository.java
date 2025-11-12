package com.tpi.microservicio_cliente.repository;

import com.tpi.microservicio_cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
    // Por ahora no necesitamos métodos extra.
}