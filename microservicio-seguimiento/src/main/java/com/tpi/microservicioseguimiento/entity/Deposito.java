package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "depositos")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDeposito") // Coincide con el DER
    private Long idDeposito;

    @Column(name = "nombre") // Coincide con el DER
    private String nombre;

    @Column(name = "costoDia") // Coincide con el DER
    private Double costoDia;

    @Column(name = "direccionTextual") // Coincide con el DER
    private String direccionTextual;

    @Column(name = "latitud") // Coincide con el DER
    private Double latitud;

    @Column(name = "longitud") // Coincide con el DER
    private Double longitud;

    // Constructor vacío
    public Deposito() {
    }

    // Getters y Setters
    public Long getIdDeposito() { return idDeposito; }
    public void setIdDeposito(Long idDeposito) { this.idDeposito = idDeposito; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getCostoDia() { return costoDia; }
    public void setCostoDia(Double costoDia) { this.costoDia = costoDia; }

    public String getDireccionTextual() { return direccionTextual; }
    public void setDireccionTextual(String direccionTextual) { this.direccionTextual = direccionTextual; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}