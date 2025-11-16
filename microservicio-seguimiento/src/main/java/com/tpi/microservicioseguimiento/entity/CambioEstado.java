package com.tpi.microservicioseguimiento.entity;

// ¡El import del Enum desaparece!
import jakarta.persistence.*;
import java.time.LocalDate; // Para fecha
import java.time.LocalTime; // Para hora

@Entity
@Table(name = "cambios_estado") // Coincide con el DER
public class CambioEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cambioEstado") // Coincide con el DER
    private Long id;

    @Column(name = "fechaInicio") // Coincide con el DER
    private LocalDate fechaInicio;

    @Column(name = "horaInicio") // Coincide con el DER
    private LocalTime horaInicio;
    
    @Column(name = "fechaFin") // Coincide con el DER
    private LocalDate fechaFin;

    @Column(name = "horaFin") // Coincide con el DER
    private LocalTime horaFin;

    // ¡CAMBIO CLAVE! FK a la tabla Estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado", nullable = false) // Coincide con el DER
    private Estado estado;

    // (Tu DER no tiene este enlace, pero tu lógica anterior sí. Lo mantenemos
    // porque es lógicamente necesario para saber a qué tramo pertenece el cambio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tramo_id") 
    private Tramo tramo; 

    // Constructor vacío
    public CambioEstado() {
        this.fechaInicio = LocalDate.now();
        this.horaInicio = LocalTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Tramo getTramo() { return tramo; }
    public void setTramo(Tramo tramo) { this.tramo = tramo; }
}