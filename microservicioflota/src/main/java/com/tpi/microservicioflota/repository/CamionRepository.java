package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// ¡CAMBIO IMPORTANTE! El ID del Camion (patente) ahora es String
public interface CamionRepository extends JpaRepository<Camion, String> {
    // JpaRepository<TipoDeEntidad, TipoDelID>

    // ¡MÉTODO ACTUALIZADO!
    // El campo 'disponible' ahora se llama 'disponibilidad'
    List<Camion> findByDisponibilidadTrue();
}