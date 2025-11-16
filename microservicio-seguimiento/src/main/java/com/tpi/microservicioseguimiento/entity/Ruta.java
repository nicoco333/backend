package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuta") // Coincide con el DER
    private Long idRuta;

    // El ID de la solicitud (del ms-cliente)
    @Column(name = "idSolicitud", nullable = false) // Coincide con el DER
    private Long idSolicitud;

    @Column(name = "descripcion") // Coincide con el DER
    private String descripcion;

    // Una Ruta tiene muchos Tramos
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tramo> tramos;

    // Constructor vacío
    public Ruta() {
    }

    // Getters y Setters
    public Long getIdRuta() { return idRuta; }
    public void setIdRuta(Long idRuta) { this.idRuta = idRuta; }
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Tramo> getTramos() { return tramos; }
    public void setTramos(List<Tramo> tramos) { this.tramos = tramos; }
}