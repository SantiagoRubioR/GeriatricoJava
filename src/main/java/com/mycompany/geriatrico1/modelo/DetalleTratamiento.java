
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;


public class DetalleTratamiento {
    private String idDetTra;
    private String idEncabTraDetTra;
    private String idEncabRecDetTra;
    private String idTipoTra;
    private LocalDate fechaIniDetTra;
    private LocalDate fechaFinDetTra;
    private int duracionDiasDetTra;
    private String estadoDetTra;
    private String observacionesDetTra;

    public DetalleTratamiento() {
    }

    public String getIdDetTra() {
        return idDetTra;
    }

    public void setIdDetTra(String idDetTra) {
        this.idDetTra = idDetTra;
    }

    public String getIdEncabTraDetTra() {
        return idEncabTraDetTra;
    }

    public void setIdEncabTraDetTra(String idEncabTraDetTra) {
        this.idEncabTraDetTra = idEncabTraDetTra;
    }

    public String getIdEncabRecDetTra() {
        return idEncabRecDetTra;
    }

    public void setIdEncabRecDetTra(String idEncabRecDetTra) {
        this.idEncabRecDetTra = idEncabRecDetTra;
    }

    public String getIdTipoTra() {
        return idTipoTra;
    }

    public void setIdTipoTra(String idTipoTra) {
        this.idTipoTra = idTipoTra;
    }

    public LocalDate getFechaIniDetTra() {
        return fechaIniDetTra;
    }

    public void setFechaIniDetTra(LocalDate fechaIniDetTra) {
        this.fechaIniDetTra = fechaIniDetTra;
    }

    public LocalDate getFechaFinDetTra() {
        return fechaFinDetTra;
    }

    public void setFechaFinDetTra(LocalDate fechaFinDetTra) {
        this.fechaFinDetTra = fechaFinDetTra;
    }

    public int getDuracionDiasDetTra() {
        return duracionDiasDetTra;
    }

    public void setDuracionDiasDetTra(int duracionDiasDetTra) {
        this.duracionDiasDetTra = duracionDiasDetTra;
    }

    public String getEstadoDetTra() {
        return estadoDetTra;
    }

    public void setEstadoDetTra(String estadoDetTra) {
        this.estadoDetTra = estadoDetTra;
    }

    public String getObservacionesDetTra() {
        return observacionesDetTra;
    }

    public void setObservacionesDetTra(String observacionesDetTra) {
        this.observacionesDetTra = observacionesDetTra;
    }
    
}
