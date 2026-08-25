/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.modelo;

import java.time.LocalTime;

/**
 *
 * @author Mary
 */
public class JornadaTurno {
    private String Turno_hor;
    private String Nombre_Jortur;
    private LocalTime Hora_ini;
    private LocalTime Hora_fin;

    public JornadaTurno(String Turno_hor, String Nombre_Jortur, LocalTime Hora_ini, LocalTime Hora_fin) {
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

    public LocalTime getHora_ini() {
        return Hora_ini;
    }

    public void setHora_ini(LocalTime Hora_ini) {
        this.Hora_ini = Hora_ini;
    }

    public LocalTime getHora_fin() {
        return Hora_fin;
    }

    public void setHora_fin(LocalTime Hora_fin) {
        this.Hora_fin = Hora_fin;
    }

    
}