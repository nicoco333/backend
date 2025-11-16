package com.tpi.microservicioflota.entity;

// ¡Los imports de 'enums' desaparecen!
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos") // Coincide con el DER
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTramo") // Coincide con el DER
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

    // (Omitimos 'idRuta' porque Flota no lo necesita)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_tramo") // FK a la tabla TipoTramo
    private TipoTramo tipoTramo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patente") // FK a Camion
    private Camion camion;

    // (Omitimos Depósitos porque Flota no los necesita)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado") // ¡CAMBIO CLAVE! FK a la tabla Estado
    private Estado estado;

    // Constructor vacío
    public Tramo() { }

    // --- Getters y Setters ---

    public Long getIdTramo() { return idTramo; }
    public void setIdTramo(Long idTramo) { this.idTramo = idTramo; }

    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }

    // ¡MÉTODOS CORREGIDOS! Ahora usan la entidad Estado
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    // Mapeamos los nombres del controller a los campos del DER
    public LocalDateTime getFechaHoraInicio() { return this.fechaRealInicio; }
    public void setFechaHoraInicio(LocalDateTime fecha) { this.fechaRealInicio = fecha; }

    public LocalDateTime getFechaHoraFin() { return this.fechaRealFin; }
    public void setFechaHoraFin(LocalDateTime fecha) { this.fechaRealFin = fecha; }

    // (Puedes añadir el resto de Getters/Setters si los necesitas)
}