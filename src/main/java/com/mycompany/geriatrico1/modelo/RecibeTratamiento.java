
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.time.LocalTime;


public class RecibeTratamiento {
    
    private String idReciTrata;
    private String idPacReciTrata;
    private String idEncabTraReciTrata;
    private LocalDate fechaReciTrata;
    private LocalTime horaReciTrata;
    private String estadoReciTrata;
    private String observacionesReciTrata;

    public RecibeTratamiento() {
    }

    public String getIdReciTrata() {
        return idReciTrata;
    }

    public void setIdReciTrata(String idReciTrata) {
        this.idReciTrata = idReciTrata;
    }

    public String getIdPacReciTrata() {
        return idPacReciTrata;
    }

    public void setIdPacReciTrata(String idPacReciTrata) {
        this.idPacReciTrata = idPacReciTrata;
    }

    public String getIdEncabTraReciTrata() {
        return idEncabTraReciTrata;
    }

    public void setIdEncabTraReciTrata(String idEncabTraReciTrata) {
        this.idEncabTraReciTrata = idEncabTraReciTrata;
    }

    public LocalDate getFechaReciTrata() {
        return fechaReciTrata;
    }

    public void setFechaReciTrata(LocalDate fechaReciTrata) {
        this.fechaReciTrata = fechaReciTrata;
    }

    public LocalTime getHoraReciTrata() {
        return horaReciTrata;
    }

    public void setHoraReciTrata(LocalTime horaReciTrata) {
        this.horaReciTrata = horaReciTrata;
    }

    public String getEstadoReciTrata() {
        return estadoReciTrata;
    }

    public void setEstadoReciTrata(String estadoReciTrata) {
        this.estadoReciTrata = estadoReciTrata;
    }

    public String getObservacionesReciTrata() {
        return observacionesReciTrata;
    }

    public void setObservacionesReciTrata(String observacionesReciTrata) {
        this.observacionesReciTrata = observacionesReciTrata;
    }
    

}
