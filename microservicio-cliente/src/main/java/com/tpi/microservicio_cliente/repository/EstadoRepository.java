package com.tpi.microservicio_cliente.repository;

import com.tpi.microservicio_cliente.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ¡Añadir import!

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    // ¡AÑADIR ESTE MÉTODO!
    // Busca un estado por su nombre exacto.
    Optional<Estado> findByNombre(String nombre);
}