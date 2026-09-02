package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistorialDAO {

    // ========================================================
    // REGISTRAR HISTORIAL CLÍNICO (Transacción)
    // ========================================================
    public boolean registrarHistorial(String idPaciente, String idMedico, String diagnostico, 
                                      double peso, double temperatura, int frecuencia, String observaciones) {
        Connection con = null;
        try {
            con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
            con.setAutoCommit(false); // Iniciamos transacción

            // 1. Insertamos Encabezado y atrapamos el ID (EHC-XXXX)
            String sqlEncab = "INSERT INTO Encabezado_Historial_Clinico (ID_Pac_EncabHistoClin) " +
                              "VALUES (?) RETURNING ID_EncabHistoClin";
            
            PreparedStatement psEncab = con.prepareStatement(sqlEncab);
            psEncab.setString(1, idPaciente);
            
            ResultSet rs = psEncab.executeQuery();
            String idGeneradoEHC = "";
            if (rs.next()) {
                idGeneradoEHC = rs.getString(1);
            }

            // 2. Insertamos el Detalle
            // Nota: Excluimos ID_PresArt_DetHisto para el MVP (quedará NULL)
            String sqlDetalle = "INSERT INTO Detalle_Historial_Clinico " +
                                "(ID_EncabHistoClin_DetHisto, ID_Med_DetHisto, Diagnostico_DetHisto, " +
                                "Peso_DetHisto, Temperatura_DetHisto, Frecuencia_Cardiaca_DetHisto, Observaciones_DetHisto) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                                
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            psDetalle.setString(1, idGeneradoEHC);
            psDetalle.setString(2, idMedico);
            psDetalle.setString(3, diagnostico);
            psDetalle.setDouble(4, peso);
            psDetalle.setDouble(5, temperatura);
            psDetalle.setInt(6, frecuencia);
            psDetalle.setString(7, observaciones);
            
            psDetalle.executeUpdate();

            con.commit(); // Guardamos los cambios de ambas tablas
            return true;
            
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.err.println("Error en transacción de Historial Clínico: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }
}
