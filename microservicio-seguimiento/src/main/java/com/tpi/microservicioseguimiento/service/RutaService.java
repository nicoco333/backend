package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.Estado;
import com.tpi.microservicioseguimiento.repository.RutaRepository;
import com.tpi.microservicioseguimiento.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // [CORRECCIÓN] Importar

@Service
public class RutaService {

    private final RutaRepository rutaRepository;
    private final EstadoRepository estadoRepository;

    public RutaService(RutaRepository rutaRepository, EstadoRepository estadoRepository) {
        this.rutaRepository = rutaRepository;
        this.estadoRepository = estadoRepository;
    }

    public Ruta asignarRuta(Ruta ruta) throws IllegalArgumentException {
        
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            throw new IllegalArgumentException("La ruta debe contener al menos un tramo para ser asignada.");
        }

        // [CORRECCIÓN] Usamos NoSuchElementException
        Estado estadoAsignado = estadoRepository.findByNombre("ASIGNADO")
                .orElseThrow(() -> new NoSuchElementException("Error fatal: El estado 'ASIGNADO' no existe en la base de datos."));

        for (Tramo tramo : ruta.getTramos()) {
            tramo.setRuta(ruta); 
            tramo.setEstado(estadoAsignado);
        }

        Ruta rutaGuardada = rutaRepository.save(ruta);
        
        return rutaGuardada;
    }
}