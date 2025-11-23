package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    // Método para buscar un estado por su nombre
    Optional<Estado> findByNombre(String nombre);
}