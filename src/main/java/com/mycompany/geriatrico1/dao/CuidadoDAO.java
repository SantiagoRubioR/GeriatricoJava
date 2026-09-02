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
    
    public int contarCuidadosRegistrados() {
    int totalCuidados = 0;
    // Asegúrate de que el nombre de la tabla sea exactamente el de tu base de datos (ej. 'cuidado')
    String sql = "SELECT COUNT(*) FROM cuidado";
    
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            totalCuidados = rs.getInt(1);
        }
    } catch (Exception e) {
        System.err.println("Error al contar cuidados: " + e.getMessage());
    }
    return totalCuidados;
    }
    
    public int contarCuidadosPendientesEnfermero() {
    int totalPendientes = 0;
    // Ajusta la consulta según el criterio de "pendiente" que maneje tu tabla Cuidado
    String sql = "SELECT COUNT(*) FROM Cuidado"; 
    
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
         
        if (rs.next()) {
            totalPendientes = rs.getInt(1);
        }
    } catch (Exception e) {
        System.out.println("Error al contar cuidados pendientes del enfermero: " + e.getMessage());
    }
    
    return totalPendientes;
}
}