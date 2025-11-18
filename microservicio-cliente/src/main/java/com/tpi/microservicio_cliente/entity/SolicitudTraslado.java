package com.tpi.microservicio_cliente.entity;

import jakarta.persistence.*;
import java.time.LocalDate; // Import para 'fecha'

// [CORRECCIÓN] Importar CascadeType
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "solicitudes_traslado")
public class SolicitudTraslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSolicitud")
    private Long idSolicitud;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "latitudOrigen")
    private Double latitudOrigen;
    
    @Column(name = "longitudOrigen")
    private Double longitudOrigen;

    @Column(name = "latitudDestino")
    private Double latitudDestino;

    @Column(name = "longitudDestino")
    private Double longitudDestino;

    @Column(name = "direccionTextualDestino")
    private String direccionTextualDestino;

    @Column(name = "costoEstimado")
    private Double costoEstimado;

    @Column(name = "tiempoEstimado")
    private Double tiempoEstimado;

    @Column(name = "costoFinal")
    private Double costoFinal;

    @Column(name = "tiempoReal")
    private Double tiempoReal;

    @Column(name = "idRuta")
    private Long idRuta;

    // [CORRECCIÓN] Se añade CascadeType.ALL
    // Esto le dice a Hibernate que guarde el Contenedor junto con la Solicitud.
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idContenedor")
    private Contenedor contenedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado")
    private Estado estado;

    // Constructor vacío
    public SolicitudTraslado() {
    }

    // --- Getters y Setters (Sin cambios) ---
    
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Double getLatitudOrigen() { return latitudOrigen; }
    public void setLatitudOrigen(Double latitudOrigen) { this.latitudOrigen = latitudOrigen; }

    public Double getLongitudOrigen() { return longitudOrigen; }
    public void setLongitudOrigen(Double longitudOrigen) { this.longitudOrigen = longitudOrigen; }

    public Double getLatitudDestino() { return latitudDestino; }
    public void setLatitudDestino(Double latitudDestino) { this.latitudDestino = latitudDestino; }

    public Double getLongitudDestino() { return longitudDestino; }
    public void setLongitudDestino(Double longitudDestino) { this.longitudDestino = longitudDestino; }

    public String getDireccionTextualDestino() { return direccionTextualDestino; }
    public void setDireccionTextualDestino(String direccionTextualDestino) { this.direccionTextualDestino = direccionTextualDestino; }

    public Double getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(Double costoEstimado) { this.costoEstimado = costoEstimado; }

    public Double getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(Double tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }

    public Double getCostoFinal() { return costoFinal; }
    public void setCostoFinal(Double costoFinal) { this.costoFinal = costoFinal; }

    public Double getTiempoReal() { return tiempoReal; }
    public void setTiempoReal(Double tiempoReal) { this.tiempoReal = tiempoReal; }

    public Long getIdRuta() { return idRuta; }
    public void setIdRuta(Long idRuta) { this.idRuta = idRuta; }

    public Contenedor getContenedor() { return contenedor; }
    public void setContenedor(Contenedor contenedor) { this.contenedor = contenedor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}