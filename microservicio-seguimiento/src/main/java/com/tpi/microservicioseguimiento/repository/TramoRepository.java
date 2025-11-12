package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
}