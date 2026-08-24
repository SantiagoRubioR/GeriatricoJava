
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EncabezadoHistorialClinico {
    
    private String idEncabezadoHistorial;
    private String idPaciente;
    private LocalDate fecha;
    private LocalTime hora;
    private List<DetalleHistorialClinico> listaDetalles; 

    public EncabezadoHistorialClinico() {
    }

    public String getIdEncabezadoHistorial() {
        return idEncabezadoHistorial;
    }

    public void setIdEncabezadoHistorial(String idEncabezadoHistorial) {
        this.idEncabezadoHistorial = idEncabezadoHistorial;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public List<DetalleHistorialClinico> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleHistorialClinico> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }
    
    
}
