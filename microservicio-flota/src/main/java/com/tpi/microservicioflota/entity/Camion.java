package com.tpi.microservicioflota.entity;

// [CORRECCIÓN] Importar la anotación para ignorar el proxy
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "camiones")
// [CORRECCIÓN] Esta anotación ignora los campos de proxy de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Camion {

    @Id
    @Column(name = "patente")
    private String patente;

    @Column(name = "disponibilidad")
    private boolean disponibilidad;

    @Column(name = "capacidadKg")
    private double capacidadKg;

    @Column(name = "capacidadM3")
    private double capacidadM3;

    @Column(name = "consumoGL")
    private double consumoGL;

    @Column(name = "costo")
    private double costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTransportista")
    // [CORRECCIÓN] Esta anotación también es necesaria en la relación
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Transportista transportista;

    // ... Getters y Setters (sin cambios) ...
    public Camion() {
    }
    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }
    public boolean isDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public double getCapacidadKg() { return capacidadKg; }
    public void setCapacidadKg(double capacidadKg) { this.capacidadKg = capacidadKg; }
    public double getCapacidadM3() { return capacidadM3; }
    public void setCapacidadM3(double capacidadM3) { this.capacidadM3 = capacidadM3; }
    public double getConsumoGL() { return consumoGL; }
    public void setConsumoGL(double consumoGL) { this.consumoGL = consumoGL; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public Transportista getTransportista() { return transportista; }
    public void setTransportista(Transportista transportista) { this.transportista = transportista; }
}