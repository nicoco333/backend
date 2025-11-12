package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {
    // JpaRepository<TipoDeEntidad, TipoDelID>

    /**
     * Busca camiones disponibles que cumplan con un peso y volumen mínimo.
     * Esto será vital para que este servicio pueda "asignar camiones".
     */
    List<Camion> findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
            double peso, double volumen);
}