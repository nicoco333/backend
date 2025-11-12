package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
}