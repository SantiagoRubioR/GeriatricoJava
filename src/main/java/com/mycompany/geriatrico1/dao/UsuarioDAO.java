package com.mycompany.geriatrico1.dao;


import com.mycompany.geriatrico1.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    
    // Cruzamos Usuario y Empleado para validar credenciales y obtener el Cargo
    private static final String VALIDAR_LOGIN = 
        "SELECT e.Cargo_Emp, u.Estado_User FROM Usuario u " +
        "INNER JOIN Empleado e ON u.ID_Emp_User = e.ID_Emp " +
        "WHERE u.Nombre_User = ? AND u.Contrasena_User = ?";

    public String[] iniciarSesion(String usuarioCedula, String password) {
        // Retornará un arreglo: [0] = Cargo, [1] = Estado
        String[] datosAcceso = new String[2];

        try (Connection con = new Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDAR_LOGIN)) {
            
            ps.setString(1, usuarioCedula);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datosAcceso[0] = rs.getString("Cargo_Emp");
                    datosAcceso[1] = rs.getString("Estado_User");
                    return datosAcceso; // Credenciales correctas
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en Login: " + e.getMessage());
        }
        return null; // Credenciales incorrectas o error
    }

    
}