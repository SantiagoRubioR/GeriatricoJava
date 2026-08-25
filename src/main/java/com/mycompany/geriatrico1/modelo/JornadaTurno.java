/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.modelo;

/**
 *
 * @author Mary
 */
public class JornadaTurno {
    private String Turno_hor;
    private String Nombre_Jortur;
    private String Hora_ini;
    private String Hora_fin;

    public JornadaTurno(String Turno_hor, String Nombre_Jortur, String Hora_ini, String Hora_fin) {
        this.Turno_hor = Turno_hor;
        this.Nombre_Jortur = Nombre_Jortur;
        this.Hora_ini = Hora_ini;
        this.Hora_fin = Hora_fin;
    }

    public String getTurno_hor() {
        return Turno_hor;
    }

    public void setTurno_hor(String Turno_hor) {
        this.Turno_hor = Turno_hor;
    }

    public String getNombre_Jortur() {
        return Nombre_Jortur;
    }

    public void setNombre_Jortur(String Nombre_Jortur) {
        this.Nombre_Jortur = Nombre_Jortur;
    }

    public String getHora_ini() {
        return Hora_ini;
    }

    public void setHora_ini(String Hora_ini) {
        this.Hora_ini = Hora_ini;
    }

    public String getHora_fin() {
        return Hora_fin;
    }

    public void setHora_fin(String Hora_fin) {
        this.Hora_fin = Hora_fin;
    }
    
    
    
}
