
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;


public class Paciente {
    private String idPaciente;
    private String cedulaPersona;
    private String idTutor;
    private LocalDate fechaIngreso;
    private String tipoSangre;
    private String gradoDependencia;

    public Paciente() {
    }


    
    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getCedulaPersona() {
        return cedulaPersona;
    }

    public void setCedulaPersona(String cedulaPersona) {
        this.cedulaPersona = cedulaPersona;
    }

    public String getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(String idTutor) {
        this.idTutor = idTutor;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getGradoDependencia() {
        return gradoDependencia;
    }

    public void setGradoDependencia(String gradoDependencia) {
        this.gradoDependencia = gradoDependencia;
    }
    
}
