package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
    // Por ahora, solo con los métodos básicos de JpaRepository
    // (como findById y save) es suficiente.
}