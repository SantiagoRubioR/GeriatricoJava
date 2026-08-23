
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;
import java.util.List;


public class DetalleAlerta {
    private String idEncabGenAler;
    private String idPacEncabAler;
    private String idPrioridadEncabAler;
    private String idMedEncabAler; 
    
    private List<DetalleAlerta> listaDetalles;

    public DetalleAlerta() {
    }

    public String getIdEncabGenAler() {
        return idEncabGenAler;
    }

    public void setIdEncabGenAler(String idEncabGenAler) {
        this.idEncabGenAler = idEncabGenAler;
    }

    public String getIdPacEncabAler() {
        return idPacEncabAler;
    }

    public void setIdPacEncabAler(String idPacEncabAler) {
        this.idPacEncabAler = idPacEncabAler;
    }

    public String getIdPrioridadEncabAler() {
        return idPrioridadEncabAler;
    }

    public void setIdPrioridadEncabAler(String idPrioridadEncabAler) {
        this.idPrioridadEncabAler = idPrioridadEncabAler;
    }

    public String getIdMedEncabAler() {
        return idMedEncabAler;
    }

    public void setIdMedEncabAler(String idMedEncabAler) {
        this.idMedEncabAler = idMedEncabAler;
    }

    public List<DetalleAlerta> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleAlerta> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }
    
    
    
}
