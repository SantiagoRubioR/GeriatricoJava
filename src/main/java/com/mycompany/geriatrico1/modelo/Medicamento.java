
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;


public class Medicamento {
    private String idMedicam;
    private String nombreMedicam;
    private String presentacion;
    private String concentracion;
    private String descripcion;
    private String nomFabricante;
    private String viaAdministracion;
    private LocalDate fechaCadu;
    private String observaciones;

    public Medicamento() {
    }

    public String getIdMedicam() {
        return idMedicam;
    }

    public void setIdMedicam(String idMedicam) {
        this.idMedicam = idMedicam;
    }

    public String getNombreMedicam() {
        return nombreMedicam;
    }

    public void setNombreMedicam(String nombreMedicam) {
        this.nombreMedicam = nombreMedicam;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNomFabricante() {
        return nomFabricante;
    }

    public void setNomFabricante(String nomFabricante) {
        this.nomFabricante = nomFabricante;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public LocalDate getFechaCadu() {
        return fechaCadu;
    }

    public void setFechaCadu(LocalDate fechaCadu) {
        this.fechaCadu = fechaCadu;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}
