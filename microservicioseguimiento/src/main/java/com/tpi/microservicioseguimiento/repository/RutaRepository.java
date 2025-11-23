package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
}