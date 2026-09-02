/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.modelo.Reporte;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUSRIO_ PC
 */
public class ReporteDAO {
     private Connection conexion;

    public ReporteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Reporte> ejecutarReporte(
            int tipoConsulta,
            Date fechaInicio,
            Date fechaFin) throws java.sql.SQLException {

        List<Reporte> lista = new ArrayList<>();

        String sql =
            "SELECT * FROM ejecutar_reporte(?, ?, ?)";

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            // Tipo de consulta
            ps.setInt(1, tipoConsulta);

            // Fecha inicial
            if (fechaInicio != null) {
                ps.setDate(2, fechaInicio);
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            // Fecha final
            if (fechaFin != null) {
                ps.setDate(3, fechaFin);
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String categoria =
                        rs.getString("categoria");

                long cantidad =
                        rs.getLong("cantidad");

                lista.add(
                    new Reporte(
                        categoria,
                        cantidad
                    )
                );
            }

        }

        return lista;
    }
    
}
