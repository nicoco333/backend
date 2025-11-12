package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "camiones")
public class Camion {

    @Id
    @Column(name = "dominio", length = 10)
    private String dominio;

    @Column(name = "capacidad_peso_kg", nullable = false)
    private double capacidadPeso;

    @Column(name = "capacidad_volumen_m3", nullable = false)
    private double capacidadVolumen;

    @Column(name = "consumo_combustible_km", nullable = false)
    private double consumoCombustiblePromedio;

    @Column(name = "costo_km", nullable = false)
    private double costoBasePorKm;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;


    // 1. Constructor vacío (requerido por JPA)
    public Camion() {
    }

    // 2. Getters y Setters (los que @Data hacía por ti)
    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public double getCapacidadPeso() {
        return capacidadPeso;
    }

    public void setCapacidadPeso(double capacidadPeso) {
        this.capacidadPeso = capacidadPeso;
    }

    public double getCapacidadVolumen() {
        return capacidadVolumen;
    }

    public void setCapacidadVolumen(double capacidadVolumen) {
        this.capacidadVolumen = capacidadVolumen;
    }

    public double getConsumoCombustiblePromedio() {
        return consumoCombustiblePromedio;
    }

    public void setConsumoCombustiblePromedio(double consumoCombustiblePromedio) {
        this.consumoCombustiblePromedio = consumoCombustiblePromedio;
    }

    public double getCostoBasePorKm() {
        return costoBasePorKm;
    }

    public void setCostoBasePorKm(double costoBasePorKm) {
        this.costoBasePorKm = costoBasePorKm;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

}