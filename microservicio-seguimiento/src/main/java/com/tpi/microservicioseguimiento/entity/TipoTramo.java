package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_tramo") // Coincide con el DER
public class TipoTramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipo") // Coincide con el DER
    private Long idTipo;

    @Column(name = "nombre") // Coincide con el DER
    private String nombre;
    
    @Column(name = "descripcion") // Coincide con el DER
    private String descripcion;

    // Constructor vacío
    public TipoTramo() {
    }

    // Getters y Setters
    public Long getIdTipo() { return idTipo; }
    public void setIdTipo(Long idTipo) { this.idTipo = idTipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}