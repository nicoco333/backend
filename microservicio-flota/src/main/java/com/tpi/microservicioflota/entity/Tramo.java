package com.tpi.microservicioflota.entity;

import com.tpi.microservicioflota.entity.enums.EstadoTramo;
import com.tpi.microservicioflota.entity.enums.TipoTramo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos") // Coincide con el DER
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTramo") // Coincide con el DER
    private Long id;

    // --- ¡CAMPOS QUE FALTABAN! ---
    // (Basados en tu DER y la lógica del controller)

    @Column(name = "orden") // Del DER
    private Integer orden;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tramo") // Del DER
    private TipoTramo tipoTramo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado") // ¡CRÍTICO! Este campo faltaba.
    private EstadoTramo estado; 

    @Column(name = "costo_aproximado") // Del DER
    private Double costoAproximado;

    @Column(name = "costo_real") // Del DER
    private Double costoReal;

    @Column(name = "fecha_estimada_inicio") // Del DER
    private LocalDateTime fechaEstimadaInicio;
    
    @Column(name = "fecha_estimada_fin") // Del DER
    private LocalDateTime fechaEstimadaFin;

    @Column(name = "fecha_real_inicio") // Del DER
    private LocalDateTime fechaRealInicio; 

    @Column(name = "fecha_real_fin") // Del DER
    private LocalDateTime fechaRealFin; 

    // --- Relaciones ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patente") // Del DER
    private Camion camion;
    
    // (Faltan las relaciones a Ruta y Deposito, pero no son necesarias
    // para que este controlador funcione)

    // Constructor vacío
    public Tramo() { }

    // --- Getters y Setters COMPLETOS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public TipoTramo getTipoTramo() { return tipoTramo; }
    public void setTipoTramo(TipoTramo tipoTramo) { this.tipoTramo = tipoTramo; }

    // ¡MÉTODOS QUE FALTABAN!
    public EstadoTramo getEstado() { return estado; }
    public void setEstado(EstadoTramo estado) { this.estado = estado; }

    public Double getCostoAproximado() { return costoAproximado; }
    public void setCostoAproximado(Double costoAproximado) { this.costoAproximado = costoAproximado; }

    public Double getCostoReal() { return costoReal; }
    public void setCostoReal(Double costoReal) { this.costoReal = costoReal; }

    public LocalDateTime getFechaEstimadaInicio() { return fechaEstimadaInicio; }
    public void setFechaEstimadaInicio(LocalDateTime fechaEstimadaInicio) { this.fechaEstimadaInicio = fechaEstimadaInicio; }

    public LocalDateTime getFechaEstimadaFin() { return fechaEstimadaFin; }
    public void setFechaEstimadaFin(LocalDateTime fechaEstimadaFin) { this.fechaEstimadaFin = fechaEstimadaFin; }

    // ¡MÉTODOS QUE FALTABAN!
    // El controller los llama 'fechaHora...' pero el DER 'fecha_real...'
    // Mapeamos los nombres que usa el controller a los campos del DER.
    public LocalDateTime getFechaHoraInicio() { return this.fechaRealInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaRealInicio = fechaHoraInicio; }

    public LocalDateTime getFechaHoraFin() { return this.fechaRealFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaRealFin = fechaHoraFin; }

    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }
}