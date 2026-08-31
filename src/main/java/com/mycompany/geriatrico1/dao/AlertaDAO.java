package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlertaDAO {
            private static final String INSERTAR_ALERTA_ENF = "INSERT INTO Encabezado_Alerta (ID_Pac_EncabAler, ID_Prioridad_EncabAler, ID_Med_EncabAler) VALUES (?, ?, ?)";

    // ========================================================
    // GENERAR ALERTA DE EMERGENCIA HACIA EL MÉDICO
    // ========================================================
    public boolean generarAlerta(String idPaciente, String idPrioridad, String idMedico) {
        
        
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERTAR_ALERTA_ENF)) {
            
            ps.setString(1, idPaciente);
            ps.setString(2, idPrioridad);
            ps.setString(3, idMedico);   
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al generar alerta: " + e.getMessage());
            return false;
        }
    }
}