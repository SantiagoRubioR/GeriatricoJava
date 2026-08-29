
package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.conexion.Conexion;
import com.mycompany.geriatrico1.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PacienteDao {

    private static final String INSERT_PERSONA = 
        "INSERT INTO Persona (cedula_Perso, nombre_Perso, apellido1_Perso, apellido2_Perso, telefono_Perso, direccion_Perso, correo_Perso, fecha_nac_Perso, genero_Perso, estado_civil_Perso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_TUTOR = 
        "INSERT INTO Tutor_Paciente (Cedula_Perso_Tut, Parentesco_Tut, Tipo_Tut) VALUES (?, ?, ?) RETURNING ID_Tut";
    
    private static final String INSERT_PACIENTE = 
        "INSERT INTO PACIENTE (Cedula_Perso_Pac, ID_Tut_Pac, Tipo_Sandre_Pac, Grado_Dependencia) VALUES (?, ?, ?, ?)";
    
     private static final String LISTARPACIENTE = "SELECT p.ID_Pac, per.cedula_Perso, per.nombre_Perso, per.apellido1_Perso, p.Grado_Dependencia, p.Tipo_Sandre_Pac " +
                     "FROM PACIENTE p " +
                     "INNER JOIN Persona per ON p.Cedula_Perso_Pac = per.cedula_Perso";

    public boolean registrarPacienteCompleto(Persona abuelo, Persona tutor, Tutor datosTutor, Paciente datosPac) {
        try (Connection con = new Conexion().getConnection()) {
            con.setAutoCommit(false);
            
            try (PreparedStatement psPerAbuelo = con.prepareStatement(INSERT_PERSONA);
                 PreparedStatement psPerTutor = con.prepareStatement(INSERT_PERSONA);
                 PreparedStatement psTutor = con.prepareStatement(INSERT_TUTOR);
                 PreparedStatement psPac = con.prepareStatement(INSERT_PACIENTE)) {
                
                // 1. Persona Abuelo
                psPerAbuelo.setString(1, abuelo.getCedula());
                psPerAbuelo.setString(2, abuelo.getNombre1());
                psPerAbuelo.setString(3, abuelo.getApellido1());
                psPerAbuelo.setString(4, abuelo.getApellido2());
                psPerAbuelo.setString(5, abuelo.getTelefono());
                psPerAbuelo.setString(6, abuelo.getDireccion());
                psPerAbuelo.setString(7, abuelo.getCorreo());
                psPerAbuelo.setDate(8, java.sql.Date.valueOf(abuelo.getFechaNacimiento()));
                psPerAbuelo.setString(9, abuelo.getGenero());
                psPerAbuelo.setString(10, abuelo.getEstadoCivil());
                psPerAbuelo.executeUpdate();
                
                // 2. Persona Tutor
                psPerTutor.setString(1, tutor.getCedula());
                psPerTutor.setString(2, tutor.getNombre1());
                psPerTutor.setString(3, tutor.getApellido1());
                psPerTutor.setString(4, tutor.getApellido2());
                psPerTutor.setString(5, tutor.getTelefono());
                psPerTutor.setString(6, tutor.getDireccion());
                psPerTutor.setString(7, tutor.getCorreo());
                psPerTutor.setDate(8, java.sql.Date.valueOf(tutor.getFechaNacimiento()));
                psPerTutor.setString(9, tutor.getGenero());
                psPerTutor.setString(10, tutor.getEstadoCivil());
                psPerTutor.executeUpdate();
                
                // Registrar 
                psTutor.setString(1, tutor.getCedula());
                psTutor.setString(2, datosTutor.getParentesco());
                psTutor.setString(3, datosTutor.getTipoTutor());
                
                String idTutorGenerado = "";
                try (ResultSet rsTut = psTutor.executeQuery()) {
                    if (rsTut.next()) {
                        idTutorGenerado = rsTut.getString("ID_Tut");
                    }
                }
                
                // 4. Registrar Paciente
                psPac.setString(1, abuelo.getCedula());
                psPac.setString(2, idTutorGenerado);
                psPac.setString(3, datosPac.getTipoSangre());
                psPac.setString(4, datosPac.getGradoDependencia());
                psPac.executeUpdate();
                
                con.commit();
                return true;
                
            } catch (SQLException e) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Error en Registro Paciente:\n" + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }
    public List<Object[]> listarPacientes() {
        List<Object[]> lista = new ArrayList<>();
        

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(LISTARPACIENTE);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = new Object[6];
                    fila[0] = rs.getString("ID_Pac");
                    fila[1] = rs.getString("cedula_Perso");
                    fila[2] = rs.getString("nombre_Perso");
                    fila[3] = rs.getString("apellido1_Perso");
                    fila[4] = rs.getString("Grado_Dependencia");
                    fila[5] = rs.getString("Tipo_Sandre_Pac");
                lista.add(fila);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }
}   
    
    
    

