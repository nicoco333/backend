package com.tpi.microservicioflota.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_tramo") // Nombre de la tabla en tu DER
public class TipoTramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipo") // PK del DER
    private Long idTipo;

    @Column(name = "nombre") // Columna del DER
    private String nombre;

    @Column(name = "descripcion") // Columna del DER
    private String descripcion;

    // Constructor vacío (requerido por JPA)
    public TipoTramo() {
    }

    // --- Getters y Setters ---

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}