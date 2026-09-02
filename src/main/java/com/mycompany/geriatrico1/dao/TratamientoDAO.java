package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TratamientoDAO {

    // ========================================================
    // LLENAR COMBOBOXES
    // ========================================================
    public List<String> listarTiposTratamiento() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT ID_TipoTra, Nombre_TipoTra FROM Tipo_Tratamiento";
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            lista.add("Seleccione un tratamiento...");
            while (rs.next()) {
                lista.add(rs.getString("ID_TipoTra") + " - " + rs.getString("Nombre_TipoTra"));
            }
        } catch (Exception e) {
            System.err.println("Error cargar tipos tratamiento: " + e.getMessage());
        }
        return lista;
    }

    public List<String> listarMedicamentos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT ID_Medicam, Nombre_Medicam, Concentracion_Medicam FROM Medicamento";
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            lista.add("Seleccione un medicamento...");
            while (rs.next()) {
                lista.add(rs.getString("ID_Medicam") + " - " + rs.getString("Nombre_Medicam") + " (" + rs.getString("Concentracion_Medicam") + ")");
            }
        } catch (Exception e) {
            System.err.println("Error cargar medicamentos: " + e.getMessage());
        }
        return lista;
    }

    // ========================================================
    // MEGA-TRANSACCIÓN: TRATAMIENTO + RECETA
    // ========================================================
   public boolean registrarTratamientoCompleto(
            String idPaciente, String idMedico, String idTipoTratamiento, 
            java.sql.Date fechaIni, java.sql.Date fechaFin, String observaciones,
            String idMedicamento, int cantidad, String dosis, String frecuencia, String duracionReceta) {
        
        Connection con = null;
        try {
            con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
            con.setAutoCommit(false); 

            // 1. ENCABEZADO TRATAMIENTO
            String sqlETR = "INSERT INTO Encabezado_Tratamiento (ID_Med_EncabTra) VALUES (?) RETURNING ID_EncabTra";
            PreparedStatement psETR = con.prepareStatement(sqlETR);
            psETR.setString(1, idMedico);
            ResultSet rsETR = psETR.executeQuery();
            if (!rsETR.next()) throw new Exception("Fallo al generar ID de Encabezado Tratamiento");
            String idETR = rsETR.getString(1);

            // 2. ENCABEZADO RECETA (Forzamos la fecha para que JDBC no se confunda)
            String sqlERE = "INSERT INTO Encabezado_Receta (Fecha_EncabRec) VALUES (CURRENT_DATE) RETURNING ID_EncabRec";
            PreparedStatement psERE = con.prepareStatement(sqlERE);
            ResultSet rsERE = psERE.executeQuery();
            if (!rsERE.next()) throw new Exception("Fallo al generar ID de Encabezado Receta");
            String idERE = rsERE.getString(1);

            // 3. DETALLE RECETA 
            String sqlDRE = "INSERT INTO Detalle_Receta (ID_EncabRec_DetRec, ID_Medicam_DetRec, Cantidad_DetRec, Dosis_DetRec, Frecuencia_DetRec, Duracion_DetRec) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psDRE = con.prepareStatement(sqlDRE);
            psDRE.setString(1, idERE);
            psDRE.setString(2, idMedicamento);
            psDRE.setInt(3, cantidad);
            psDRE.setString(4, dosis);
            psDRE.setString(5, frecuencia);
            psDRE.setString(6, duracionReceta);
            psDRE.executeUpdate();

            // 4. DETALLE TRATAMIENTO 
            String sqlDTR = "INSERT INTO Detalle_Tratamiento (ID_EncabTra_DetTra, ID_EncabRec_DetTra, ID_TipoTra_DetTra, Fecha_ini_DetTra, Fecha_fin_DetTra, Estado_DetTra, Observaciones_DetTra) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psDTR = con.prepareStatement(sqlDTR);
            psDTR.setString(1, idETR);
            psDTR.setString(2, idERE);
            psDTR.setString(3, idTipoTratamiento);
            psDTR.setDate(4, fechaIni);
            psDTR.setDate(5, fechaFin);
            psDTR.setString(6, "En proceso"); 
            psDTR.setString(7, observaciones);
            psDTR.executeUpdate();

            // 5. RECIBE TRATAMIENTO
            String sqlRTR = "INSERT INTO Recibe_Tratamiento (ID_Pac_ReciTrata, ID_EncabTra_ReciTrata, Estado_ReciTrata) VALUES (?, ?, ?)";
            PreparedStatement psRTR = con.prepareStatement(sqlRTR);
            psRTR.setString(1, idPaciente);
            psRTR.setString(2, idETR);
            psRTR.setString(3, "En proceso"); 
            psRTR.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.err.println("Error en transacción: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }
}