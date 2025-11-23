package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    // ¡MÉTODO NUEVO!
    // Para buscar tarifas específicas como "valor_litro_combustible"
    Optional<Tarifa> findByNombre(String nombre);
}