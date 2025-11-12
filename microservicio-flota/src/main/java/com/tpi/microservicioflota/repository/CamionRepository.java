package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {
    // JpaRepository<TipoDeEntidad, TipoDelID>

    // NUEVO MÉTODO AÑADIDO:
    // Spring Data JPA entiende que "findByDisponibleTrue" significa:
    // "SELECT * FROM camiones WHERE disponible = true"
    List<Camion> findByDisponibleTrue();
}