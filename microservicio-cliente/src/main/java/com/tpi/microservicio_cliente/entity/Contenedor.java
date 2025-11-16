package com.tpi.microservicio_cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contenedores")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContenedor") // Coincide con el DER
    private Long idContenedor;

    @Column(name = "peso") // Coincide con el DER
    private Double peso;

    @Column(name = "volumen") // Coincide con el DER
    private Double volumen;

    // --- Relaciones del DER ---
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente") // FK idCliente
    private Cliente cliente;

    // idDeposito es una FK a un servicio externo (Seguimiento).
    // Por ahora, lo guardamos solo como el ID.
    @Column(name = "idDeposito") // FK idDeposito
    private Long idDeposito;

    // Constructor vacío
    public Contenedor() {
    }

    // Getters y Setters
    public Long getIdContenedor() { return idContenedor; }
    public void setIdContenedor(Long idContenedor) { this.idContenedor = idContenedor; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getVolumen() { return volumen; }
    public void setVolumen(Double volumen) { this.volumen = volumen; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Long getIdDeposito() { return idDeposito; }
    public void setIdDeposito(Long idDeposito) { this.idDeposito = idDeposito; }
}