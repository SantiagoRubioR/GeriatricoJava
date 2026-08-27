package com.mycompany.geriatrico1.dao;

import com.mycompany.geriatrico1.conexion.Conexion;
import com.mycompany.geriatrico1.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {

    // Consultas SQL adaptadas exactamente a tu script de base de datos
    private static final String INSERT_PERSONA = 
        "INSERT INTO Persona (cedula_Perso, nombre_Perso, apellido1_Perso, apellido2_Perso, telefono_Perso, direccion_Perso, correo_Perso, fecha_nac_Perso, genero_Perso, estado_civil_Perso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    // Usamos RETURNING ID_Emp para atrapar el ID que genera tu Trigger
    private static final String INSERT_EMPLEADO = 
        "INSERT INTO Empleado (Cedula_Perso_Emp, Cargo_Emp, Tipo_contrato_Emp, Estado_Emp) VALUES (?, ?, ?, 'Activo') RETURNING ID_Emp";
    
    private static final String INSERT_USUARIO = 
        "INSERT INTO Usuario (ID_Emp_User, Contrasena_User) VALUES (?, ?)";
    
    private static final String INSERT_ADMIN = 
        "INSERT INTO Administrador (ID_Emp_Admin, estado_admin) VALUES (?, 'ACTIVO')";
        
    private static final String INSERT_MEDICO = 
        "INSERT INTO Medico (ID_Emp_Med, Registro_Profesiona_Med, Nivel_Formacion_Med, Especialidad_Med) VALUES (?, ?, ?, ?)";
        
    // Omitimos intencionalmente el Horario para que el Trigger asuma NULL de forma segura
    private static final String INSERT_ENFERMERA = 
        "INSERT INTO Enfermera (ID_Emp_Enfer, Numero_Licencia_Enfer, Nivel_Formacion_Enfer, Especialidad_Enfer) VALUES (?, ?, ?, ?)";

    public boolean registrarPersonalCompleto(Persona persona, Empleado empleado, Usuario usuario, Administrador admin, Medico medico, Enfermero enfermera, String rol) {
        Connection con = null;
        try {
            con = new Conexion().getConnection();
            con.setAutoCommit(false); // Inicia Transacción

            // 1. Guardar Persona
            try (PreparedStatement psPer = con.prepareStatement(INSERT_PERSONA)) {
                psPer.setString(1, persona.getCedula());
                psPer.setString(2, persona.getNombre1());
                psPer.setString(3, persona.getApellido1());
                psPer.setString(4, persona.getApellido2());
                psPer.setString(5, persona.getTelefono());
                psPer.setString(6, persona.getDireccion());
                psPer.setString(7, persona.getCorreo());
                psPer.setDate(8, java.sql.Date.valueOf(persona.getFechaNacimiento()));
                psPer.setString(9, persona.getGenero());
                psPer.setString(10, persona.getEstadoCivil());
                psPer.executeUpdate();
            }

            // 2. Guardar Empleado y recuperar su ID generado por el Trigger
            String idEmpleadoGenerado = "";
            try (PreparedStatement psEmp = con.prepareStatement(INSERT_EMPLEADO)) {
                psEmp.setString(1, persona.getCedula());
                psEmp.setString(2, empleado.getCargo());
                psEmp.setString(3, empleado.getTipoContrato());
                
                try (ResultSet rs = psEmp.executeQuery()) {
                    if (rs.next()) {
                        idEmpleadoGenerado = rs.getString("ID_Emp");
                    }
                }
            }

            // 3. Guardar Usuario vinculado al Empleado
            try (PreparedStatement psUser = con.prepareStatement(INSERT_USUARIO)) {
                psUser.setString(1, idEmpleadoGenerado);
                psUser.setString(2, usuario.getContrasena());
                psUser.executeUpdate();
            }

            // 4. Derivar a la tabla del Rol Específico
            if (rol.equalsIgnoreCase("ADMINISTRADOR")) {
                try (PreparedStatement psAdm = con.prepareStatement(INSERT_ADMIN)) {
                    psAdm.setString(1, idEmpleadoGenerado);
                    psAdm.executeUpdate();
                }
            } else if (rol.equalsIgnoreCase("MEDICO")) {
                try (PreparedStatement psMed = con.prepareStatement(INSERT_MEDICO)) {
                    psMed.setString(1, idEmpleadoGenerado);
                    psMed.setString(2, medico.getRegistroProfesional());
                    psMed.setString(3, medico.getNivelFormacion());
                    psMed.setString(4, medico.getEspecialidad());
                    psMed.executeUpdate();
                }
            } else if (rol.equalsIgnoreCase("ENFERMERO") || rol.equalsIgnoreCase("ENFERMERA")) {
                try (PreparedStatement psEnf = con.prepareStatement(INSERT_ENFERMERA)) {
                    psEnf.setString(1, idEmpleadoGenerado);
                    psEnf.setString(2, enfermera.getNumeroLicencia());
                    psEnf.setString(3, enfermera.getNivelFormacion());
                    psEnf.setString(4, enfermera.getEspecialidad());
                    psEnf.executeUpdate();
                }
            }

            con.commit(); // Si todo sale bien, guardar definitivamente
            return true;

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            javax.swing.JOptionPane.showMessageDialog(null, "Error SQL en Registro de Personal:\n" + e.getMessage(), "Error Transaccional", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
}