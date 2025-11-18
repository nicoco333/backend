package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;
import java.util.List;
// [CORRECCIÓN] Importar la anotación para romper el bucle
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRuta")
    private Long idRuta;

    @Column(name = "idSolicitud", nullable = false)
    private Long idSolicitud;

    @Column(name = "descripcion")
    private String descripcion;

    // [CORRECCIÓN] Esta es la "parte principal" de la relación
    @JsonManagedReference
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tramo> tramos;

    // ... Getters y Setters ...
    public Ruta() { }
    public Long getIdRuta() { return idRuta; }
    public void setIdRuta(Long idRuta) { this.idRuta = idRuta; }
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Tramo> getTramos() { return tramos; }
    public void setTramos(List<Tramo> tramos) { this.tramos = tramos; }
}