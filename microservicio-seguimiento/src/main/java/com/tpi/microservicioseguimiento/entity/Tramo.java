package com.tpi.microservicioseguimiento.entity;

import com.tpi.microservicioseguimiento.entity.enums.EstadoTramo;
import com.tpi.microservicioseguimiento.entity.enums.TipoTramo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos")
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "origen, destino"  (simplificado como String)
    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    // "tipo (...)" 
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTramo tipo;

    // "estado (...)" 
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTramo estado;

    // "costoAproximado, costoReal" 
    @Column(name = "costo_aproximado")
    private Double costoAproximado;

    @Column(name = "costo_real")
    private Double costoReal;

    // "fechaHoraInicio, fechaHoraFin" 
    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    // "camion" 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion_dominio") // Se relaciona con el ID (dominio) del Camion
    private Camion camion;

    // --- Constructores, Getters y Setters (Sin Lombok) ---

    public Tramo() {
    }

    // Getters
    public Long getId() { return id; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public TipoTramo getTipo() { return tipo; }
    public EstadoTramo getEstado() { return estado; }
    public Double getCostoAproximado() { return costoAproximado; }
    public Double getCostoReal() { return costoReal; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public Camion getCamion() { return camion; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrigen(String origen) { this.origen = origen; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setTipo(TipoTramo tipo) { this.tipo = tipo; }
    public void setEstado(EstadoTramo estado) { this.estado = estado; }
    public void setCostoAproximado(Double costoAproximado) { this.costoAproximado = costoAproximado; }
    public void setCostoReal(Double costoReal) { this.costoReal = costoReal; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }
    public void setCamion(Camion camion) { this.camion = camion; }
}