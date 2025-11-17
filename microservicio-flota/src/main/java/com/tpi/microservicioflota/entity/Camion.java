package com.tpi.microservicioflota.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "camiones")
public class Camion {

    @Id
    @Column(name = "patente") // Coincide con el DER (reemplaza a 'dominio')
    private String patente;

    @Column(name = "disponibilidad") // Coincide con el DER
    private boolean disponibilidad;

    @Column(name = "capacidadKg") // Coincide con el DER
    private double capacidadKg;

    @Column(name = "capacidadM3") // Coincide con el DER
    private double capacidadM3;

    @Column(name = "consumoGL") // Coincide con el DER (Gas/Litro?)
    private double consumoGL;

    @Column(name = "costo") // Coincide con el DER
    private double costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTransportista") // Coincide con el DER
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Transportista transportista;

    // Constructor vacío
    public Camion() {
    }

    // Getters y Setters
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public double getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(double capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public double getCapacidadM3() {
        return capacidadM3;
    }

    public void setCapacidadM3(double capacidadM3) {
        this.capacidadM3 = capacidadM3;
    }

    public double getConsumoGL() {
        return consumoGL;
    }

    public void setConsumoGL(double consumoGL) {
        this.consumoGL = consumoGL;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }
}