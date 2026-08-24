
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class EncabezadoAlerta {
    private String idEncabezadoAlerta;
    private String idPaciente;
    private String idPrioridad;
    private LocalDate fecha;
    private LocalTime horaGeneracion;
    private List<DetalleAlerta> listaDetalles;

    public EncabezadoAlerta() {
    }

    public String getIdEncabezadoAlerta() {
        return idEncabezadoAlerta;
    }

    public void setIdEncabezadoAlerta(String idEncabezadoAlerta) {
        this.idEncabezadoAlerta = idEncabezadoAlerta;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(String idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraGeneracion() {
        return horaGeneracion;
    }

    public void setHoraGeneracion(LocalTime horaGeneracion) {
        this.horaGeneracion = horaGeneracion;
    }

    public List getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List listaDetalles) {
        this.listaDetalles = listaDetalles;
    }
    
    
}
