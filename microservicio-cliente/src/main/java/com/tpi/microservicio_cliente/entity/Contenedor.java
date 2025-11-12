package com.tpi.microservicio_cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contenedores")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "identificación única"
    @Column(name = "identificacion_unica", nullable = false, unique = true)
    private String identificacionUnica;

    // "peso"
    @Column(name = "peso_kg", nullable = false)
    private Double peso;

    // "volumen"
    @Column(name = "volumen_m3", nullable = false)
    private Double volumen;

    // Constructor vacío
    public Contenedor() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdentificacionUnica() { return identificacionUnica; }
    public void setIdentificacionUnica(String identificacionUnica) { this.identificacionUnica = identificacionUnica; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public Double getVolumen() { return volumen; }
    public void setVolumen(Double volumen) { this.volumen = volumen; }
}
