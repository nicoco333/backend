package com.tpi.microservicio_cliente.repository;

import com.tpi.microservicio_cliente.entity.SolicitudTraslado;
// Añade estas dos importaciones:
import com.tpi.microservicio_cliente.entity.enums.EstadoSolicitud;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudTrasladoRepository extends JpaRepository<SolicitudTraslado, Long> {
    
    // AÑADE ESTE MÉTODO:
    /**
     * Busca todas las solicitudes que estén en cualquiera de los estados proporcionados.
     * @param estados Lista de estados (ej. [BORRADOR, PROGRAMADA])
     * @return Lista de solicitudes que coinciden.
     */
    List<SolicitudTraslado> findByEstadoIn(List<EstadoSolicitud> estados);
}