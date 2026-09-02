package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class HorarioDAO {

    // Actualiza la enfermera para asignarle su ID de horario
    private static final String ASIGNAR_TURNO_ENFERMERA = 
        "UPDATE Enfermera SET ID_Hor_Enfer = ? WHERE ID_Enfer = ?";

    public boolean asignarTurno(String idHorario, String idEnfermera) {
        try (Connection con = new Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(ASIGNAR_TURNO_ENFERMERA)) {
            
            ps.setString(1, idHorario);
            ps.setString(2, idEnfermera);
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al asignar turno:\n" + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}