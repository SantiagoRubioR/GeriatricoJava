package com.mycompany.geriatrico1.controlador;

public class Validador {

    public static boolean esCedulaValida(String cedula) {
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false; 
        }
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) {
            return false;
        }
        
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            if (valor >= 10) valor -= 9;
            suma += valor;
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma + 9) / 10) * 10;
        int calculado = decenaSuperior - suma;
        if (calculado == 10) calculado = 0;
        
        return calculado == digitoVerificador;
    }

    public static boolean esNombreValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$") && !texto.matches(".*(.)\\1\\1.*");
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && telefono.matches("^09\\d{8}$");
    }

    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}