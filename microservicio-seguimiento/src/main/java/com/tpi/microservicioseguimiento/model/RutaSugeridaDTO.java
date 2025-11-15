package com.tpi.microservicioseguimiento.model;

public class RutaSugeridaDTO {

    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;
    private double costoEstimado; // ¡La nueva información!

    // Constructor vacío
    public RutaSugeridaDTO() {
    }

    // Getters y Setters
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public double getKilometros() { return kilometros; }
    public void setKilometros(double kilometros) { this.kilometros = kilometros; }
    public String getDuracionTexto() { return duracionTexto; }
    public void setDuracionTexto(String duracionTexto) { this.duracionTexto = duracionTexto; }
    public double getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(double costoEstimado) { this.costoEstimado = costoEstimado; }
}