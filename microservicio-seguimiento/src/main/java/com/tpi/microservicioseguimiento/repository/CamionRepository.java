package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> { // ID es String (patente)
    
    // ¡MÉTODO ACTUALIZADO!
    // Usa los nuevos nombres de campo del DER:
    // findBy[Disponibilidad]TrueAnd[CapacidadKg]GreaterThanEqualAnd[CapacidadM3]GreaterThanEqual
    List<Camion> findByDisponibilidadTrueAndCapacidadKgGreaterThanEqualAndCapacidadM3GreaterThanEqual(
            double peso, double volumen);
}