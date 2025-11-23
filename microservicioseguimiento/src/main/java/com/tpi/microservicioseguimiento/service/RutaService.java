package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Camion;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.Estado;
import com.tpi.microservicioseguimiento.model.GeoResponse;
import com.tpi.microservicioseguimiento.model.RutaSugeridaDTO;
import com.tpi.microservicioseguimiento.repository.CamionRepository;
import com.tpi.microservicioseguimiento.repository.TarifaRepository;
import com.tpi.microservicioseguimiento.repository.RutaRepository;
import com.tpi.microservicioseguimiento.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;
    private final EstadoRepository estadoRepository;
    private final GeoService geoService;
    private final CamionRepository camionRepository;
    private final TarifaRepository tarifaRepository;

    public RutaService(RutaRepository rutaRepository,
                       EstadoRepository estadoRepository,
                       GeoService geoService,
                       CamionRepository camionRepository,
                       TarifaRepository tarifaRepository) {
        this.rutaRepository = rutaRepository;
        this.estadoRepository = estadoRepository;
        this.geoService = geoService;
        this.camionRepository = camionRepository;
        this.tarifaRepository = tarifaRepository;
    }

    public RutaSugeridaDTO sugerirRuta(String origen, String destino, double peso, double volumen) throws Exception {
        
        // 1. Distancia REAL (Google Maps)
        GeoResponse geoData = this.geoService.calcularDistancia(origen, destino);
        double distanciaKm = geoData.getKilometros();

        // 2. Tarifas
        Tarifa tarifaComb = tarifaRepository.findByNombre("valor_litro_combustible")
                .orElseThrow(() -> new NoSuchElementException("Falta tarifa 'valor_litro_combustible'"));
        Tarifa tarifaGest = tarifaRepository.findByNombre("cargo_gestion")
                .orElseThrow(() -> new NoSuchElementException("Falta tarifa 'cargo_gestion'"));

        double precioLitro = tarifaComb.getCostoBase();
        double cargoGestion = tarifaGest.getCostoBase();

        // 3. Camiones Aptos
        List<Camion> camiones = camionRepository
                .findByDisponibilidadTrueAndCapacidadKgGreaterThanEqualAndCapacidadM3GreaterThanEqual(peso, volumen);

        // Valores por defecto para evitar error / cero si no hay flota cargada
        double consumoProm = 0.2;
        double costoKmProm = 300.0;

        if (!camiones.isEmpty()) {
            consumoProm = camiones.stream().mapToDouble(Camion::getConsumoGL).average().orElse(0.2);
            costoKmProm = camiones.stream().mapToDouble(Camion::getCosto).average().orElse(300.0);
        }

        // 4. Cálculo Final
        double costoTramos = distanciaKm * costoKmProm;
        double costoCombustible = distanciaKm * consumoProm * precioLitro;
        double costoTotal = cargoGestion + costoTramos + costoCombustible;

        RutaSugeridaDTO dto = new RutaSugeridaDTO();
        dto.setOrigen(origen);
        dto.setDestino(destino);
        dto.setKilometros(distanciaKm);
        dto.setDuracionTexto(geoData.getDuracionTexto());
        dto.setCostoEstimado(Math.round(costoTotal * 100.0) / 100.0);

        return dto;
    }

    public Ruta asignarRuta(Ruta ruta) {
        // ... (Lógica de asignación sin cambios) ...
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            throw new IllegalArgumentException("Ruta sin tramos");
        }
        Estado estado = estadoRepository.findByNombre("ASIGNADO").orElseThrow();
        ruta.getTramos().forEach(t -> { t.setRuta(ruta); t.setEstado(estado); });
        return rutaRepository.save(ruta);
    }
}