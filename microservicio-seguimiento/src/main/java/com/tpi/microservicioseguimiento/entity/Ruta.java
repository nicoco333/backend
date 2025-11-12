package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "solicitud" (El TPI sugiere vincularla a una solicitud)
    // Usamos el ID de la solicitud (que vive en la otra BD/servicio)
    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;

    // "cantidadTramos", "cantidadDepositos"
    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos;

    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos;

    // Una Ruta está compuesta por muchos Tramos
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ruta_id")
    private List<Tramo> tramos;

    // Constructor vacío
    public Ruta() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }
    public Integer getCantidadTramos() { return cantidadTramos; }
    public void setCantidadTramos(Integer cantidadTramos) { this.cantidadTramos = cantidadTramos; }
    public Integer getCantidadDepositos() { return cantidadDepositos; }
    public void setCantidadDepositos(Integer cantidadDepositos) { this.cantidadDepositos = cantidadDepositos; }
    public List<Tramo> getTramos() { return tramos; }
    public void setTramos(List<Tramo> tramos) { this.tramos = tramos; }
}