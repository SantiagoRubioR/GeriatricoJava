package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.modelo.Cuidado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CuidadoDAO {

    //SENTENCIAS
    private static final String INSERTAR_CUIDADO = "INSERT INTO Cuidado (ID_Enfer_Cui, ID_Pac_Cui, Tipo_Cui, Observaciones_Cui) VALUES (?, ?, ?, ?)";
    
    public boolean registrarCuidado(String idEnfermera, String idPaciente, String tipoCuidado, String observaciones) {
        
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERTAR_CUIDADO)) {
            
            ps.setString(1, idEnfermera);
            ps.setString(2, idPaciente);
            ps.setString(3, tipoCuidado); 
            ps.setString(4, observaciones); 
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar cuidado: " + e.getMessage());
            return false;
        }
    }
}