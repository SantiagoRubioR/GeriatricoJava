/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.modelo;

/**
 *
 * @author Mary
 */
public class DetallePresionArterial {
    private String ID_PresArt;
    private int Pre_Sistolica;
    private int Pre_Diastolica;

    public DetallePresionArterial(String ID_PresArt, int Pre_Sistolica, int Pre_Diastolica) {
        this.ID_PresArt = ID_PresArt;
        this.Pre_Sistolica = Pre_Sistolica;
        this.Pre_Diastolica = Pre_Diastolica;
    }

    public String getID_PresArt() {
        return ID_PresArt;
    }

    public void setID_PresArt(String ID_PresArt) {
        this.ID_PresArt = ID_PresArt;
    }

    public int getPre_Sistolica() {
        return Pre_Sistolica;
    }

    public void setPre_Sistolica(int Pre_Sistolica) {
        this.Pre_Sistolica = Pre_Sistolica;
    }

    public int getPre_Diastolica() {
        return Pre_Diastolica;
    }

    public void setPre_Diastolica(int Pre_Diastolica) {
        this.Pre_Diastolica = Pre_Diastolica;
    }

    
}