
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EncabezadoTratamiento {
    
    private String idEncabTra;
    private String idMedEncabTra;
    private LocalDate fechaEncabTra;
    private LocalTime horaEncabTra;
    
    private List<DetalleTratamiento> listaDetallesTratamiento;

    public EncabezadoTratamiento() {
    }

    public String getIdEncabTra() {
        return idEncabTra;
    }

    public void setIdEncabTra(String idEncabTra) {
        this.idEncabTra = idEncabTra;
    }

    public String getIdMedEncabTra() {
        return idMedEncabTra;
    }

    public void setIdMedEncabTra(String idMedEncabTra) {
        this.idMedEncabTra = idMedEncabTra;
    }

    public LocalDate getFechaEncabTra() {
        return fechaEncabTra;
    }

    public void setFechaEncabTra(LocalDate fechaEncabTra) {
        this.fechaEncabTra = fechaEncabTra;
    }

    public LocalTime getHoraEncabTra() {
        return horaEncabTra;
    }

    public void setHoraEncabTra(LocalTime horaEncabTra) {
        this.horaEncabTra = horaEncabTra;
    }

    public List getListaDetallesTratamiento() {
        return listaDetallesTratamiento;
    }

    public void setListaDetallesTratamiento(List listaDetallesTratamiento) {
        this.listaDetallesTratamiento = listaDetallesTratamiento;
    }
    
}
