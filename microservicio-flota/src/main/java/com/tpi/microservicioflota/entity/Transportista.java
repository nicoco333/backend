package com.tpi.microservicioflota.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transportistas")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "telefono")
    private String telefono;

    // 1. Constructor vacío (requerido por JPA)
    public Transportista() {
    }

    // 2. Constructor con parámetros (el que ya tenías)
    public Transportista(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // 3. Getters y Setters (los que @Data hacía por ti)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}