package com.tpi.microservicioflota.repository;
import com.tpi.microservicioflota.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNombre(String nombre);
}