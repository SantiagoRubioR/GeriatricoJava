package com.mycompany.geriatrico1.dao;


import com.mycompany.geriatrico1.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    
    // Cruzamos Usuario y Empleado para validar credenciales y obtener el Cargo
    private static final String VALIDAR_LOGIN = 
        "SELECT e.Cargo_Emp, u.Estado_User, p.nombre_Perso, p.apellido1_Perso FROM usuario u " +
        "INNER JOIN empleado e ON u.ID_Emp_User = e.ID_Emp " +
        "INNER JOIN persona p ON e.Cedula_Perso_Emp = p.cedula_Perso " +
        "WHERE u.Nombre_User = ? AND u.Contrasena_User = ?";
    
    public String[] iniciarSesion(String usuarioCedula, String password) {
        // Retorna un arreglo: [0] = Cargo, [1] = Estado , [2] = nombre
        String[] datosAcceso = new String[3];

        try (Connection con = new Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDAR_LOGIN)) {
            
            ps.setString(1, usuarioCedula);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datosAcceso[0] = rs.getString("Cargo_Emp");
                    datosAcceso[1] = rs.getString("Estado_User");
                    datosAcceso[2] = rs.getString("nombre_Perso") + " " + rs.getString("apellido1_Perso");
                    return datosAcceso; // Credenciales correctas
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en Login: " + e.getMessage());
        }
        return null; // Error 
    }

    
}