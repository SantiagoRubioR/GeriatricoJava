
package com.mycompany.geriatrico1.modelo;

import java.time.LocalDate;

public class Persona {
    private String cedula;
    private String nombre1;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String direccion;
    private String correo;
    private LocalDate fechaNacimiento;
    private String genero;
    private String estadoCivil;

    public Persona() {
    }

    public String getCedula() {
        return cedula;}

    public void setCedula(String cedula) {
        this.cedula = cedula;}

    public String getNombre1() {
        return nombre1;}

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;}

    public String getApellido1() {
        return apellido1;}

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;}

    public String getApellido2() {
        return apellido2;}

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;}

    public String getTelefono() {
        return telefono;}

    public void setTelefono(String telefono) {
        this.telefono = telefono;}

    public String getDireccion() {
        return direccion;}

    public void setDireccion(String direccion) {
        this.direccion = direccion;}

    public String getCorreo() {
        return correo;}

    public void setCorreo(String correo) {
        this.correo = correo;}

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;}

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;}

    public String getGenero() {
        return genero;}

    public void setGenero(String genero) {
        this.genero = genero;}

    public String getEstadoCivil() {
        return estadoCivil;}

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;}
    
    
}
