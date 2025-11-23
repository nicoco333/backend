package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.SolicitudTraslado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudTrasladoRepository extends JpaRepository<SolicitudTraslado, Long> {
    // Para que el cliente pueda ver "Mis Solicitudes"
    List<SolicitudTraslado> findByIdCliente(Long idCliente);
}