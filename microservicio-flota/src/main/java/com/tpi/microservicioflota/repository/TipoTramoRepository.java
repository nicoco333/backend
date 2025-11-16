package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.TipoTramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTramoRepository extends JpaRepository<TipoTramo, Long> {
    // No necesitamos métodos extra por ahora
}