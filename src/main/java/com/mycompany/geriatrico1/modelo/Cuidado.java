
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cuidado {
    private String idCuidado;
    private String idEnfermera;
    private String idPaciente;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipoCuidado;
    private String observaciones;

    public Cuidado() {
    }

    public String getIdCuidado() {
        return idCuidado;
    }

    public void setIdCuidado(String idCuidado) {
        this.idCuidado = idCuidado;
    }

    public String getIdEnfermera() {
        return idEnfermera;
    }

    public void setIdEnfermera(String idEnfermera) {
        this.idEnfermera = idEnfermera;
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

    public String getTipoCuidado() {
        return tipoCuidado;
    }

    public void setTipoCuidado(String tipoCuidado) {
        this.tipoCuidado = tipoCuidado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
    
}
