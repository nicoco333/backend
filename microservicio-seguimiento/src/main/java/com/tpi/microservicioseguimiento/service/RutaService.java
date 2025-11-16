package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.Estado; // ¡Cambiado! Importa la Entidad
import com.tpi.microservicioseguimiento.repository.RutaRepository;
import com.tpi.microservicioseguimiento.repository.EstadoRepository; // ¡Nuevo!
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;
    private final EstadoRepository estadoRepository; // ¡Nuevo!

    // ¡Constructor manual! (reemplaza a @RequiredArgsConstructor)
    public RutaService(RutaRepository rutaRepository, EstadoRepository estadoRepository) {
        this.rutaRepository = rutaRepository;
        this.estadoRepository = estadoRepository;
    }

    /**
     * Asigna y guarda una nueva ruta, alineado con el nuevo DER.
     */
    public Ruta asignarRuta(Ruta ruta) throws IllegalArgumentException {
        
        // [1] Validación de Negocio: La ruta debe tener tramos
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            throw new IllegalArgumentException("La ruta debe contener al menos un tramo para ser asignada.");
        }

        // [2] Busca el estado "ASIGNADO" en la base de datos
        Estado estadoAsignado = estadoRepository.findByNombre("ASIGNADO")
                .orElseThrow(() -> new RuntimeException("Error fatal: El estado 'ASIGNADO' no existe en la base de datos."));

        // [3] Establecer el estado y la relación padre-hijo (JPA)
        for (Tramo tramo : ruta.getTramos()) {
            tramo.setRuta(ruta); // ¡IMPORTANTE! Asigna la ruta padre a cada tramo
            tramo.setEstado(estadoAsignado); // ¡Usa la entidad Estado!
            
            // (Los campos 'cantidadTramos' y 'cantidadDepositos' se eliminaron
            // porque no están en la entidad Ruta del DER)
        }

        // [4] Persistencia: Guardar la ruta (la persistencia en cascada guardará los tramos)
        Ruta rutaGuardada = rutaRepository.save(ruta);
        
        return rutaGuardada;
    }
}