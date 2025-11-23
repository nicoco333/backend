package com.tpi.microservicioseguimiento.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// [CORRECCIÓN] Importar la anotación para romper el bucle
import com.fasterxml.jackson.annotation.JsonBackReference;
// [CORRECCIÓN] Importar para ignorar proxies (soluciona error de Flota)
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tramos")
// [CORRECCIÓN] Ignorar proxies para evitar errores 500 al serializar
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTramo")
    private Long idTramo;
    
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "costo_aproximado")
    private Double costoAproximado;
    @Column(name = "costo_real")
    private Double costoReal;
    @Column(name = "fecha_estimada_inicio")
    private LocalDateTime fechaEstimadaInicio;
    @Column(name = "fecha_estimada_fin")
    private LocalDateTime fechaEstimadaFin;
    @Column(name = "fecha_real_inicio")
    private LocalDateTime fechaRealInicio; 
    @Column(name = "fecha_real_fin")
    private LocalDateTime fechaRealFin; 

    // [CORRECCIÓN] Esta es la "referencia de vuelta" que evita el bucle
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRuta")
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_tramo")
    private TipoTramo tipoTramo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patente")
    private Camion camion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeposito_origen")
    private Deposito depositoOrigen;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeposito_destino")
    private Deposito depositoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado") 
    private Estado estado;

    // --- Getters y Setters ---
    public Tramo() { }
    public Long getIdTramo() { return idTramo; }
    public void setIdTramo(Long idTramo) { this.idTramo = idTramo; }
    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }
    public Ruta getRuta() { return ruta; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public LocalDateTime getFechaHoraInicio() { return this.fechaRealInicio; }
    public void setFechaHoraInicio(LocalDateTime fecha) { this.fechaRealInicio = fecha; }
    public LocalDateTime getFechaHoraFin() { return this.fechaRealFin; }
    public void setFechaHoraFin(LocalDateTime fecha) { this.fechaRealFin = fecha; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public Double getCostoAproximado() { return costoAproximado; }
    public void setCostoAproximado(Double costoAproximado) { this.costoAproximado = costoAproximado; }
    public TipoTramo getTipoTramo() { return tipoTramo; }
    public void setTipoTramo(TipoTramo tipoTramo) { this.tipoTramo = tipoTramo; }
    public Deposito getDepositoOrigen() { return depositoOrigen; }
    public void setDepositoOrigen(Deposito depositoOrigen) { this.depositoOrigen = depositoOrigen; }
    public Deposito getDepositoDestino() { return depositoDestino; }
    public void setDepositoDestino(Deposito depositoDestino) { this.depositoDestino = depositoDestino; }
}