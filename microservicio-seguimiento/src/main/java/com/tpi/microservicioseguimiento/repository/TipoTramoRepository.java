package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.TipoTramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTramoRepository extends JpaRepository<TipoTramo, Long> {
}