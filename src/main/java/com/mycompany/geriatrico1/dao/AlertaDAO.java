package com.mycompany.geriatrico1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {


    public boolean registrarAlerta(String idPaciente, String idPrioridad, String idMedico, String observaciones) {
        Connection con = null;
        try {
            con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
            con.setAutoCommit(false); 

            String sqlEncab = "INSERT INTO Encabezado_Alerta (ID_Pac_EncabAler, ID_Prioridad_EncabAler, ID_Med_EncabAler) " +
                              "VALUES (?, ?, ?) RETURNING ID_EncabAler";
            
            PreparedStatement psEncab = con.prepareStatement(sqlEncab);
            psEncab.setString(1, idPaciente);
            psEncab.setString(2, idPrioridad);
            psEncab.setString(3, idMedico);
            
            ResultSet rs = psEncab.executeQuery();
            String idGeneradoEAL = "";
            if (rs.next()) {
                idGeneradoEAL = rs.getString(1); 
            }

            String sqlDetalle = "INSERT INTO Detalle_Alerta (ID_EncabAler_DetAler, Estado_DetAler, Observaciones_DetAler) " +
                                "VALUES (?, ?, ?)";
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            psDetalle.setString(1, idGeneradoEAL);
            psDetalle.setString(2, "Pendiente"); 
            psDetalle.setString(3, observaciones);
            
            psDetalle.executeUpdate();

            con.commit();
            return true;
            
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {} 
            System.err.println("Error en transacción de alerta: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }


    public List<String[]> obtenerAlertasPendientes() {
        List<String[]> lista = new ArrayList<>();
        
        
        String sql = "SELECT a.ID_EncabAler, " +
                     "p.nombre_Perso || ' ' || p.apellido1_Perso AS Paciente, " +
                     "d.Observaciones_DetAler, " +
                     "a.Hora_EncabAler " +
                     "FROM Encabezado_Alerta a " +
                     "INNER JOIN Detalle_Alerta d ON a.ID_EncabAler = d.ID_EncabAler_DetAler " +
                     "INNER JOIN Paciente pac ON a.ID_Pac_EncabAler = pac.ID_Pac " +
                     "INNER JOIN Persona p ON pac.Cedula_Perso_Pac = p.cedula_Perso " +
                     "WHERE UPPER(d.Estado_DetAler) = 'PENDIENTE' " + // Solo traemos las no atendidas
                     "ORDER BY a.ID_Prioridad_EncabAler ASC, pac.Grado_Dependencia ASC LIMIT 3"; 
                     
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String[] alerta = new String[4];
                alerta[0] = rs.getString("ID_EncabAler"); 
                alerta[1] = rs.getString("Paciente");
                alerta[2] = rs.getString("Observaciones_DetAler"); 
                String horaCruda = rs.getString("Hora_EncabAler");
                alerta[3] = (horaCruda != null && horaCruda.length() >= 5) ? horaCruda.substring(0, 5) : horaCruda;
                lista.add(alerta);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar alertas pendientes: " + e.getMessage());
        }
        return lista;
    }


    public boolean atenderAlerta(String idAlerta) {
        String sql = "UPDATE Detalle_Alerta SET Estado_DetAler = 'Atendida' WHERE ID_EncabAler_DetAler = ?";
        
        try (Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idAlerta);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error al atender alerta: " + e.getMessage());
            return false;
        }
    }
    

    public int contarAlertasPendientes() {
    int totalAlertas = 0;
    String sql = "SELECT COUNT(*) FROM Detalle_Alerta WHERE UPPER(Estado_DetAler) = 'PENDIENTE'";
    
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            totalAlertas = rs.getInt(1);
        }
    } catch (Exception e) {
        System.err.println("Error al contar alertas: " + e.getMessage());
    }
    return totalAlertas;
}
    
    public int contarAlertasCriticasPendientes() {
    int totalCriticas = 0;
    String sql = "SELECT COUNT(*) " +
             "FROM Detalle_Alerta da " +
             "INNER JOIN Encabezado_Alerta ea ON da.ID_EncabAler_DetAler = ea.ID_EncabAler " +
             "WHERE UPPER(da.Estado_DetAler) = 'PENDIENTE' " +
             "AND ea.ID_Prioridad_EncabAler = 'PRI-0001'";
    
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            totalCriticas = rs.getInt(1);
        }
    } catch (Exception e) {
        System.err.println("Error al contar alertas críticas: " + e.getMessage());
    }
    return totalCriticas;
}
    
}