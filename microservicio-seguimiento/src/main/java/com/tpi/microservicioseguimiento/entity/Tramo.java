package com.tpi.microservicioseguimiento.entity;

// ¡Los imports de 'enums' desaparecen!
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos")
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

    // --- Relaciones con el DER (¡Corregidas!) ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRuta") // FK a Ruta
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_tramo") // FK a la tabla TipoTramo
    private TipoTramo tipoTramo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patente") // FK a Camion
    private Camion camion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeposito_origen") // FK a Deposito
    private Deposito depositoOrigen;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeposito_destino") // FK a Deposito
    private Deposito depositoDestino;

    // ¡CAMBIO CLAVE! FK a la tabla Estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado") 
    private Estado estado;

    // Constructor vacío
    public Tramo() { }

    // Getters y Setters (solo los necesarios para el controller)
    public Long getIdTramo() { return idTramo; }
    public void setIdTramo(Long idTramo) { this.idTramo = idTramo; }

    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }

    public Ruta getRuta() {
        return ruta;
    }
    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    // ¡MÉTODO CORREGIDO! Ahora usa la entidad Estado
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    // Mapeamos los nombres del controller a los campos del DER
    public LocalDateTime getFechaHoraInicio() { return this.fechaRealInicio; }
    public void setFechaHoraInicio(LocalDateTime fecha) { this.fechaRealInicio = fecha; }

    public LocalDateTime getFechaHoraFin() { return this.fechaRealFin; }
    public void setFechaHoraFin(LocalDateTime fecha) { this.fechaRealFin = fecha; }
}