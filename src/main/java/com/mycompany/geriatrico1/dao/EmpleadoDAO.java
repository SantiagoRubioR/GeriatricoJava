package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.conexion.Conexion;
import com.mycompany.geriatrico1.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EmpleadoDAO {

    private static final String INSERT_PERSONA = 
        "INSERT INTO Persona (cedula_Perso, nombre_Perso, apellido1_Perso, apellido2_Perso, telefono_Perso, direccion_Perso, correo_Perso, fecha_nac_Perso, genero_Perso, estado_civil_Perso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    // Usamos RETURNING para atrapar el ID (EMP-0001) que genera el trigger
    private static final String INSERT_EMPLEADO = 
        "INSERT INTO Empleado (Cedula_Perso_Emp, Cargo_Emp, Tipo_contrato_Emp, Estado_Emp) VALUES (?, ?, ?, ?) RETURNING ID_Emp";
    
    private static final String INSERT_USUARIO = 
        "INSERT INTO Usuario (ID_Emp_User, Contrasena_User, Estado_User) VALUES (?, ?, ?)";
    
    private static final String INSERT_MEDICO = 
        "INSERT INTO Medico (ID_Emp_Med, Registro_Profesiona_Med, Nivel_Formacion_Med, Especialidad_Med) VALUES (?, ?, ?, ?)";
    
    private static final String INSERT_ENFERMERA = 
        "INSERT INTO Enfermera (ID_Emp_Enfer, Numero_Licencia_Enfer, Nivel_Formacion_Enfer, Especialidad_Enfer) VALUES (?, ?, ?, ?)";

    public boolean registrarPersonalCompleto(Persona p, Empleado emp, Usuario u, Medico med, Enfermero enf, String rol) {
        try (Connection con = new Conexion().getConnection()) {
            con.setAutoCommit(false); // Inicia transacción 5FN
            
            try (PreparedStatement psPer = con.prepareStatement(INSERT_PERSONA);
                 PreparedStatement psEmp = con.prepareStatement(INSERT_EMPLEADO);
                 PreparedStatement psUsu = con.prepareStatement(INSERT_USUARIO)) {
                
                // 1. Insertar Persona
                psPer.setString(1, p.getCedula());
                psPer.setString(2, p.getNombre1());
                psPer.setString(3, p.getApellido1());
                psPer.setString(4, p.getApellido2());
                psPer.setString(5, p.getTelefono());
                psPer.setString(6, p.getDireccion());
                psPer.setString(7, p.getCorreo());
                psPer.setDate(8, java.sql.Date.valueOf(p.getFechaNacimiento()));
                psPer.setString(9, p.getGenero());
                psPer.setString(10, p.getEstadoCivil());
                psPer.executeUpdate();
                
                // 2. Insertar Empleado y atrapar su ID generado
                psEmp.setString(1, p.getCedula());
                psEmp.setString(2, emp.getCargo());
                psEmp.setString(3, emp.getTipoContrato());
                psEmp.setString(4, emp.getEstado());
                
                String idEmpleadoGenerado = "";
                try (ResultSet rsEmp = psEmp.executeQuery()) {
                    if (rsEmp.next()) {
                        idEmpleadoGenerado = rsEmp.getString("ID_Emp");
                    }
                }
                
                // 3. Crear Usuario para el Login
                psUsu.setString(1, idEmpleadoGenerado);
                psUsu.setString(2, u.getContrasena());
                psUsu.setString(3, "ACTIVO");
                psUsu.executeUpdate();
                
                // 4. Asignar Rol Específico
                if (rol.equals("MEDICO")) {
                    try (PreparedStatement psMed = con.prepareStatement(INSERT_MEDICO)) {
                        psMed.setString(1, idEmpleadoGenerado);
                        psMed.setString(2, med.getRegistroProfesional());
                        psMed.setString(3, med.getNivelFormacion());
                        psMed.setString(4, med.getEspecialidad());
                        psMed.executeUpdate();
                    }
                } else if (rol.equals("ENFERMERA")) {
                    try (PreparedStatement psEnf = con.prepareStatement(INSERT_ENFERMERA)) {
                        psEnf.setString(1, idEmpleadoGenerado);
                        psEnf.setString(2, enf.getNumeroLicencia());
                        psEnf.setString(3, enf.getNivelFormacion());
                        psEnf.setString(4, enf.getEspecialidad());
                        psEnf.executeUpdate();
                    }
                }
                
                con.commit(); // Confirmar transacción
                return true;
                
            } catch (SQLException e) {
                con.rollback(); // Revertir si algo falla
                JOptionPane.showMessageDialog(null, "Error Transaccional:\n" + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }
}