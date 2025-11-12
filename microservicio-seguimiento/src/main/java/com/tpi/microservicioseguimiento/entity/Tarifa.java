package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tarifas")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Cargos de Gestión valor fijo"
    @Column(name = "cargo_gestion", nullable = false)
    private Double cargoGestion;

    // "valor del litro" de combustible
    @Column(name = "valor_litro_combustible", nullable = false)
    private Double valorLitroCombustible;
    
    // "costo por kilómetro base"
    @Column(name = "costo_km_base")
    private Double costoKmBase;

    // Constructor vacío
    public Tarifa() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getCargoGestion() { return cargoGestion; }
    public void setCargoGestion(Double cargoGestion) { this.cargoGestion = cargoGestion; }
    public Double getValorLitroCombustible() { return valorLitroCombustible; }
    public void setValorLitroCombustible(Double valorLitroCombustible) { this.valorLitroCombustible = valorLitroCombustible; }
    public Double getCostoKmBase() { return costoKmBase; }
    public void setCostoKmBase(Double costoKmBase) { this.costoKmBase = costoKmBase; }
}