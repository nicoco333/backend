package com.tpi.microservicio_cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estados")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado") // Coincide con el DER
    private Long idEstado;

    @Column(name = "nombre") // Coincide con el DER
    private String nombre;

    @Column(name = "ambito") // Coincide con el DER
    private String ambito;

    // Constructor vacío
    public Estado() {
    }

    // Getters y Setters
    public Long getIdEstado() { return idEstado; }
    public void setIdEstado(Long idEstado) { this.idEstado = idEstado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAmbito() { return ambito; }
    public void setAmbito(String ambito) { this.ambito = ambito; }
}