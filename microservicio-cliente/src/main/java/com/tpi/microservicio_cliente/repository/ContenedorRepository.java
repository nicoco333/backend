package com.tpi.microservicio_cliente.repository;

import com.tpi.microservicio_cliente.entity.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
}