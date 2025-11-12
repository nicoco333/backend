package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
}