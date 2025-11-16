package com.tpi.microservicio_cliente.repository;

import com.tpi.microservicio_cliente.entity.Estado; // ¡Añadir import!
import com.tpi.microservicio_cliente.entity.SolicitudTraslado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // ¡Añadir import!

@Repository
public interface SolicitudTrasladoRepository extends JpaRepository<SolicitudTraslado, Long> {
    
    // ¡AÑADIR ESTE MÉTODO!
    // Busca todas las solicitudes que estén en una lista de estados.
    List<SolicitudTraslado> findByEstadoIn(List<Estado> estados);
}