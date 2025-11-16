package com.tpi.microservicio_cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente") // Coincide con el DER
    private Long idCliente;

    @Column(name = "nombre") // Coincide con el DER
    private String nombre;
    
    @Column(name = "apellido") // ¡Nuevo!
    private String apellido;

    @Column(name = "mail") // Coincide con el DER
    private String mail;

    @Column(name = "telefono") // Coincide con el DER
    private String telefono;

    // Constructor vacío
    public Cliente() {
    }

    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}