package com.tpi.microservicioseguimiento.entity;

// [CORRECCIÓN] Importar para ignorar proxies
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "camiones")
// [CORRECCIÓN] Ignorar proxies para evitar errores 500 al serializar
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

    // [CORRECCIÓN] El servicio de Seguimiento no necesita saber quién es el Transportista.
    // Se elimina el campo 'private Transportista transportista;'

    // Constructor vacío
    public Camion() {
    }

    // Getters y Setters
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

    // [CORRECCIÓN] Se eliminan los getters/setters de Transportista
}