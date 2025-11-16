package com.tpi.microservicioflota.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transportistas")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransportista") // Coincide con el DER
    private Long idTransportista;

    @Column(name = "nombre") // Coincide con el DER
    private String nombre;
    
    @Column(name = "apellido") // ¡Nuevo!
    private String apellido;

    @Column(name = "licencia") // ¡Nuevo!
    private String licencia;

    @Column(name = "telefono") // Coincide con el DER
    private String telefono;

    // Constructor vacío
    public Transportista() {
    }

    // Getters y Setters
    public Long getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Long idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}