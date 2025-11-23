package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
    // Esto sigue funcionando porque el ID (idTransportista) es Long.
}