package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class HorarioDAO {

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
    
    public String[] obtenerTurnoYHorarioEnfermero(String nombreCompleto) {
    String[] infoTurno = {"Sin Turno", "Sin Horario"};
    
    String sql = "SELECT jt.nombre_jortur, jt.hora_ini_jortur, jt.hora_fin_jortur " +
                 "FROM Empleado e " +
                 "INNER JOIN Persona per ON e.Cedula_Perso_Emp = per.Cedula_Perso " +
                 "INNER JOIN Enfermera enf ON e.ID_Emp = enf.ID_Emp_Enfer " +
                 "INNER JOIN Jornada_Turno jt ON enf.ID_JorTur_Enfer = jt.id_jortur " +
                 "WHERE CONCAT(per.nombre_perso, ' ', per.apellido1_Perso) = ?";
                 
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
         
        ps.setString(1, nombreCompleto.trim());
        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                infoTurno[0] = rs.getString("nombre_jortur");
                infoTurno[1] = rs.getString("hora_ini_jortur") + " - " + rs.getString("hora_fin_jortur"); 
            }
        }
    } catch (Exception e) {
        System.out.println("Error obteniendo turno y horario: " + e.getMessage());
    }
    
    return infoTurno;
}
}