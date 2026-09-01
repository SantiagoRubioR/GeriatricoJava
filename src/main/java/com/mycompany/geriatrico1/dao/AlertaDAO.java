package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AlertaDAO {

    // ========================================================
    // REGISTRAR NUEVA ALERTA (MVP)
    // ========================================================
    public boolean registrarAlerta(String idPaciente, String idPrioridad, String idMedico, String observaciones) {
        // El Trigger de la BD autogenera el ID_EAL, la Fecha y la Hora
        String sql = "INSERT INTO Encabezado_Alerta (ID_Pac_EAL, ID_Pri_EAL, ID_Med_EAL, Observaciones_EAL) VALUES (?, ?, ?, ?)";
        
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idPaciente);
            ps.setString(2, idPrioridad);
            ps.setString(3, idMedico);
            ps.setString(4, observaciones);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error al registrar alerta: " + e.getMessage());
            return false;
        }
    }
    public java.util.List<String[]> obtenerAlertasPendientes() {
        java.util.List<String[]> lista = new java.util.ArrayList<>();
        
        // El SELECT hace JOIN con Paciente y Persona. 
        // Ordena por Prioridad (PRI-0001 es Alta) y luego por el ID de Dependencia
        String sql = "SELECT a.ID_EAL, " +
                     "p.nombre_Perso || ' ' || p.apellido1_Perso AS Paciente, " +
                     "a.Observaciones_EAL, " +
                     "a.Hora_EAL " +
                     "FROM Encabezado_Alerta a " +
                     "INNER JOIN Paciente pac ON a.ID_Pac_EAL = pac.ID_Pac " +
                     "INNER JOIN Persona p ON pac.Cedula_Perso_Pac = p.cedula_Perso " +
                     "ORDER BY a.ID_Pri_EAL ASC, pac.ID_Grado_Dep_Pac ASC LIMIT 3"; 
                     // LIMIT 3 porque María solo dibujó 3 paneles
                     
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String[] alerta = new String[4];
                alerta[0] = rs.getString("ID_EAL"); // El ID oculto que usaremos para atenderla
                alerta[1] = rs.getString("Paciente");
                alerta[2] = rs.getString("Observaciones_EAL");
                alerta[3] = rs.getString("Hora_EAL");
                lista.add(alerta);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar alertas: " + e.getMessage());
        }
        return lista;
    }

    // ========================================================
    // ATENDER ALERTA
    // ========================================================
    public boolean atenderAlerta(String idAlerta) {
        // MVP: Borramos la alerta de la bandeja una vez atendida para limpiar la pantalla
        String sql = "DELETE FROM Encabezado_Alerta WHERE ID_EAL = ?";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idAlerta);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error al atender alerta: " + e.getMessage());
            return false;
        }
    }
}