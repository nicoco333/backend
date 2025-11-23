package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tarifas")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarifa") // Coincide con el DER
    private Long idTarifa;

    @Column(name = "nombre", nullable = false, unique = true) // Coincide con el DER
    private String nombre;

    @Column(name = "costo_base", nullable = false) // Coincide con el DER
    private Double costoBase;

    // Constructor vacío
    public Tarifa() {
    }

    // Getters y Setters
    public Long getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Long idTarifa) { this.idTarifa = idTarifa; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getCostoBase() { return costoBase; }
    public void setCostoBase(Double costoBase) { this.costoBase = costoBase; }
}