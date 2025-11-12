package com.tpi.microservicioflota.repository;

import com.tpi.microservicioflota.entity.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelID>
    
    // Con solo esto, ya tienes métodos como:
    // .save(transportista)
    // .findById(id)
    // .findAll()
    // .delete(transportista)
}