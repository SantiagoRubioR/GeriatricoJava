/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.Controlador;

/**
 *
 * @author USUSRIO_ PC
 */
public class Sesion {
    
    private static String idUsuario;
    private static String idEmpleado;
    private static String nombreCompleto;

    public static void iniciarSesion(
            String idUsuario,
            String idEmpleado,
            String nombreCompleto) {

        Sesion.idUsuario = idUsuario;
        Sesion.idEmpleado = idEmpleado;
        Sesion.nombreCompleto = nombreCompleto;
    }

    public static String getIdUsuario() {
        return idUsuario;
    }

    public static String getIdEmpleado() {
        return idEmpleado;
    }

    public static String getNombreCompleto() {
        return nombreCompleto;
    }

    public static void cerrarSesion() {
        idUsuario = null;
        idEmpleado = null;
        nombreCompleto = null;
    }

    
}
