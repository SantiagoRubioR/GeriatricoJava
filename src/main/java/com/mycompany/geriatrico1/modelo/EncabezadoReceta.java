
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EncabezadoReceta {
    
    private String idEncabRec;
    private LocalDate fechaEncabRec;
    private LocalTime horaEncabRec;
    // Relación 1 a N para Java:
    private List<DetalleReceta> listaDetallesReceta;

    public EncabezadoReceta() {
    }

    public String getIdEncabRec() {
        return idEncabRec;
    }

    public void setIdEncabRec(String idEncabRec) {
        this.idEncabRec = idEncabRec;
    }

    public LocalDate getFechaEncabRec() {
        return fechaEncabRec;
    }

    public void setFechaEncabRec(LocalDate fechaEncabRec) {
        this.fechaEncabRec = fechaEncabRec;
    }

    public LocalTime getHoraEncabRec() {
        return horaEncabRec;
    }

    public void setHoraEncabRec(LocalTime horaEncabRec) {
        this.horaEncabRec = horaEncabRec;
    }

    public List getListaDetallesReceta() {
        return listaDetallesReceta;
    }

    public void setListaDetallesReceta(List listaDetallesReceta) {
        this.listaDetallesReceta = listaDetallesReceta;
    }
    
    
}
