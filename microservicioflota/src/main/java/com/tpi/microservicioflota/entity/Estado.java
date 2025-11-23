package com.tpi.microservicioflota.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estados") // Nombre de la tabla en tu DER
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado") // PK del DER
    private Long idEstado;

    @Column(name = "nombre") // Columna del DER
    private String nombre;

    @Column(name = "ambito") // Columna del DER
    private String ambito;

    // Constructor vacío (requerido por JPA)
    public Estado() {
    }

    // --- Getters y Setters ---

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}