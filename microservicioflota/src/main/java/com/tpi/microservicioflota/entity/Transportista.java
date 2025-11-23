package com.tpi.microservicioflota.entity;

import jakarta.persistence.*;
// [CORRECCIÓN] Importar la anotación para ignorar el proxy
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// [CORRECCIÓN] Esta anotación ignora los campos de proxy de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "transportistas")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransportista")
    private Long idTransportista;

    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "apellido")
    private String apellido;

    @Column(name = "licencia")
    private String licencia;

    @Column(name = "telefono")
    private String telefono;

    // ... Getters y Setters (sin cambios) ...
    public Transportista() {
    }
    public Long getIdTransportista() { return idTransportista; }
    public void setIdTransportista(Long idTransportista) { this.idTransportista = idTransportista; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}