package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistorialDAO {


    public boolean registrarHistorial(String idPaciente, String idMedico, String diagnostico, 
                                      double peso, double temperatura, int frecuencia, String observaciones) {
        Connection con = null;
        try {
            con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
            con.setAutoCommit(false); // Iniciamos transacción

            String sqlEncab = "INSERT INTO Encabezado_Historial_Clinico (ID_Pac_EncabHistoClin) " +
                              "VALUES (?) RETURNING ID_EncabHistoClin";
            
            PreparedStatement psEncab = con.prepareStatement(sqlEncab);
            psEncab.setString(1, idPaciente);
            
            ResultSet rs = psEncab.executeQuery();
            String idGeneradoEHC = "";
            if (rs.next()) {
                idGeneradoEHC = rs.getString(1);
            }

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

            con.commit(); 
            return true;
            
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.err.println("Error en transacción de Historial Clínico: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }
    
    public java.util.List<Object[]> obtenerAntecedentesPaciente(String idPaciente) {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        
        String sql = "SELECT e.Fecha_EncabHistoClin, e.Hora_EncabHistoClin, d.Peso_DetHisto, " +
                     "p.Presion_Sistolica_PresArt || '/' || p.Presion_Diastolica_PresArt AS Presion, " +
                     "d.Temperatura_DetHisto, d.Frecuencia_Cardiaca_DetHisto, d.Estado_DetHisto, " +
                     "d.Diagnostico_DetHisto, d.Observaciones_DetHisto " +
                     "FROM Encabezado_Historial_Clinico e " +
                     "INNER JOIN Detalle_Historial_Clinico d ON e.ID_EncabHistoClin = d.ID_EncabHistoClin_DetHisto " +
                     "INNER JOIN Presion_Arterial p ON d.ID_PresArt_DetHisto = p.ID_PresArt " +
                     "WHERE e.ID_Pac_EncabHistoClin = ? " +
                     "ORDER BY e.Fecha_EncabHistoClin DESC, e.Hora_EncabHistoClin DESC";
                     
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idPaciente);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[9];
                    fila[0] = rs.getString("Fecha_EncabHistoClin");
                    fila[1] = rs.getString("Hora_EncabHistoClin");
                    fila[2] = rs.getDouble("Peso_DetHisto");
                    fila[3] = rs.getString("Presion");
                    fila[4] = rs.getDouble("Temperatura_DetHisto");
                    fila[5] = rs.getInt("Frecuencia_Cardiaca_DetHisto");
                    fila[6] = rs.getString("Estado_DetHisto");
                    fila[7] = rs.getString("Diagnostico_DetHisto"); 
                    fila[8] = rs.getString("Observaciones_DetHisto") != null ? rs.getString("Observaciones_DetHisto") : "";
                    lista.add(fila);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar antecedentes: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean registrarConsulta(String idPaciente, String idMedico, double peso, double presionSis, double presionDias, double temperatura, int frecuencia, String diagnostico, String observaciones) {
        Connection con = null;
        try {
            con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
            con.setAutoCommit(false); 

            String sqlPresion = "INSERT INTO Presion_Arterial (Presion_Sistolica_PresArt, Presion_Diastolica_PresArt) VALUES (?, ?) RETURNING ID_PresArt";
            PreparedStatement psPresion = con.prepareStatement(sqlPresion);
            psPresion.setDouble(1, presionSis);
            psPresion.setDouble(2, presionDias);
            ResultSet rsPresion = psPresion.executeQuery();
            String idPresionGenerado = "";
            if (rsPresion.next()) idPresionGenerado = rsPresion.getString(1);

            // 2. Guardar Encabezado y atrapar su ID
            String sqlEncab = "INSERT INTO Encabezado_Historial_Clinico (ID_Pac_EncabHistoClin) VALUES (?) RETURNING ID_EncabHistoClin";
            PreparedStatement psEncab = con.prepareStatement(sqlEncab);
            psEncab.setString(1, idPaciente);
            ResultSet rsEncab = psEncab.executeQuery();
            String idEncabGenerado = "";
            if (rsEncab.next()) idEncabGenerado = rsEncab.getString(1);

            // 3. Guardar el Detalle Clínico uniendo los IDs anteriores
            String sqlDetalle = "INSERT INTO Detalle_Historial_Clinico (ID_EncabHistoClin_DetHisto, ID_Med_DetHisto, ID_PresArt_DetHisto, Diagnostico_DetHisto, Peso_DetHisto, Temperatura_DetHisto, Frecuencia_Cardiaca_DetHisto, Observaciones_DetHisto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            psDetalle.setString(1, idEncabGenerado);
            psDetalle.setString(2, idMedico);
            psDetalle.setString(3, idPresionGenerado);
            psDetalle.setString(4, diagnostico);
            psDetalle.setDouble(5, peso);
            psDetalle.setDouble(6, temperatura);
            psDetalle.setInt(7, frecuencia);
            psDetalle.setString(8, observaciones);
            
            psDetalle.executeUpdate();

            // Todo perfecto, guardamos en la base de datos
            con.commit();
            return true;
            
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.err.println("Error grave en el historial: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }
}
