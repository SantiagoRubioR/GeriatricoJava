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
        "INSERT INTO Administrador (ID_Emp_Admin) VALUES (?)";
        
    private static final String INSERT_MEDICO = 
        "INSERT INTO Medico (ID_Emp_Med, Registro_Profesiona_Med, Nivel_Formacion_Med, Especialidad_Med) VALUES (?, ?, ?, ?)";
        
    // Omitimos intencionalmente el Horario para que el Trigger asuma NULL de forma segura
    private static final String INSERT_ENFERMERA = 
        "INSERT INTO Enfermera (ID_Emp_Enfer, ID_JorTur_Enfer, Numero_Licencia_Enfer, Nivel_Formacion_Enfer, Especialidad_Enfer) VALUES (?, ?, ?, ?, ?)";
    private static final String LISTAR_EMPLEADOS = "SELECT e.ID_Emp, p.cedula_Perso, p.nombre_Perso, p.apellido1_Perso, e.Cargo_Emp, e.Tipo_contrato_Emp " +
                     "FROM Empleado e " +
                     "INNER JOIN Persona p ON e.Cedula_Perso_Emp = p.cedula_Perso " +
                     "WHERE e.Estado_Emp = 'Activo'";
    
    private static final String ELIMINAR_EMPLEADO = "UPDATE Empleado SET Estado_Emp = 'Inactivo' WHERE ID_Emp = ?";
    
    private static final String ACTUALIZAR_PERSONA = "UPDATE Persona SET nombre_Perso=?, apellido1_Perso=?, apellido2_Perso=?, telefono_Perso=?, direccion_Perso=?, correo_Perso=?, estado_civil_Perso=? WHERE cedula_Perso=?";
        
    private static final String ACTUALIZAR_EMPLEADO = "UPDATE Empleado SET Cargo_Emp=?, Tipo_contrato_Emp=? WHERE ID_Emp=?";
     
    private static final String OBTENER_ID_ENF = "SELECT enf.ID_Enfer FROM Enfermera enf " +
                     "INNER JOIN Empleado emp ON enf.ID_Emp_Enfer = emp.ID_Emp " +
                     "WHERE emp.Cedula_Perso_Emp = ?";

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
                    psEnf.setString(2, enfermera.getIdHorario());
                    psEnf.setString(3, enfermera.getNumeroLicencia());
                    psEnf.setString(4, enfermera.getNivelFormacion());
                    psEnf.setString(5, enfermera.getEspecialidad());
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
    
    public java.util.List<Object[]> listarEmpleadosActivos() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(LISTAR_EMPLEADOS);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString("ID_Emp");
                fila[1] = rs.getString("cedula_Perso");
                fila[2] = rs.getString("nombre_Perso");
                fila[3] = rs.getString("apellido1_Perso");
                fila[4] = rs.getString("Cargo_Emp");
                fila[5] = rs.getString("Tipo_contrato_Emp");
                lista.add(fila);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
        return lista;
    }

    // ==========================================
    // 2. DAR DE BAJA (ELIMINAR LÓGICO)
    // ==========================================
    public boolean darDeBajaEmpleado(String idEmp) {
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(ELIMINAR_EMPLEADO)) {
            
            ps.setString(1, idEmp);
            return ps.executeUpdate() > 0;
            
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    // ==========================================
    // 3. ACTUALIZAR TRANSACCIONAL
    // ==========================================
    public boolean actualizarEmpleadoTransaccional(Persona persona, Empleado empleado) {
        String ACTUALIZAR_PERSONA = "UPDATE Persona SET nombre_Perso=?, apellido1_Perso=?, apellido2_Perso=?, telefono_Perso=?, direccion_Perso=?, correo_Perso=?, estado_civil_Perso=? WHERE cedula_Perso=?";
        
        String ACTUALIZAR_EMPLEADO = "UPDATE Empleado SET Cargo_Emp=?, Tipo_contrato_Emp=? WHERE ID_Emp=?";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection()) {
            con.setAutoCommit(false);
            
            try (java.sql.PreparedStatement psPer = con.prepareStatement(ACTUALIZAR_PERSONA);
                 java.sql.PreparedStatement psEmp = con.prepareStatement(ACTUALIZAR_EMPLEADO)) {
                
                psPer.setString(1, persona.getNombre1());
                psPer.setString(2, persona.getApellido1());
                psPer.setString(3, persona.getApellido2());
                psPer.setString(4, persona.getTelefono());
                psPer.setString(5, persona.getDireccion());
                psPer.setString(6, persona.getCorreo());
                psPer.setString(7, persona.getEstadoCivil());
                psPer.setString(8, persona.getCedula()); 
                psPer.executeUpdate();
                
                psEmp.setString(1, empleado.getCargo());
                psEmp.setString(2, empleado.getTipoContrato());
                psEmp.setString(3, empleado.getIdEmpleado()); 
                psEmp.executeUpdate();
                
                con.commit();
                return true;
            } catch (java.sql.SQLException e) {
                con.rollback();
                return false;
            }
        } catch (java.sql.SQLException ex) {
            return false;
        }
    }
    
    public java.util.List<Object[]> listarPersonalConHorarios() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        
        // Consulta multi-tabla para extraer nombres, cédula, cargo y el nombre de la jornada (Matutino/Vespertino/Nocturno)
        String sql = "SELECT e.ID_Emp, per.cedula_Perso, per.nombre_Perso, per.apellido1_Perso, e.Cargo_Emp, " +
                     "COALESCE(jt.Nombre_JorTur, 'Sin Horario Asignado') AS Horario " +
                     "FROM Empleado e " +
                     "INNER JOIN Persona per ON e.Cedula_Perso_Emp = per.cedula_Perso " +
                     "LEFT JOIN Enfermera enf ON e.ID_Emp = enf.ID_Emp_Enfer " +
                     "LEFT JOIN Jornada_Turno jt ON enf.ID_JorTur_Enfer = jt.ID_JorTur " +
                     "WHERE e.Estado_Emp = 'Activo'";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString("ID_Emp");
                fila[1] = rs.getString("cedula_Perso");
                fila[2] = rs.getString("nombre_Perso");
                fila[3] = rs.getString("apellido1_Perso");
                fila[4] = rs.getString("Cargo_Emp");
                fila[5] = rs.getString("Horario");
                lista.add(fila);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar horarios del personal: " + e.getMessage());
        }
        return lista;
    }
    
   public String obtenerIdEnfermeraPorCedula(String cedulaUsuario) {
        String id = null;
        String sql = "SELECT enf.ID_Enfer FROM Enfermera enf " +
                     "INNER JOIN Empleado emp ON enf.ID_Emp_Enfer = emp.ID_Emp " +
                     "WHERE emp.Cedula_Perso_Emp = ?";
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedulaUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) id = rs.getString("ID_Enfer");
            }
        } catch (Exception e) {}
        return id;
    }
   
   public int contarPersonalActivo() {
        int total = 0;
        // Cuenta las filas donde el estado del empleado sea Activo
        String sql = "SELECT COUNT(*) FROM Empleado WHERE UPPER(Estado_Emp) = 'ACTIVO'";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getInt(1); // Atrapa el número que devuelve el COUNT(*)
            }
            
        } catch (Exception e) {
            System.err.println("Error al contar personal: " + e.getMessage());
        }
        return total;
    }
   
   public void rellenarComboEnfermeros(javax.swing.JComboBox<String> comboBox) {
    comboBox.removeAllItems(); // Limpiamos por si acaso
    
    String sql = "SELECT per.nombre_Perso, per.apellido1_Perso " +
                 "FROM Empleado e " +
                 "INNER JOIN Persona per ON e.Cedula_Perso_Emp = per.cedula_Perso " +
                 "LEFT JOIN Enfermera enf ON e.ID_Emp = enf.ID_Emp_Enfer " +
                 "WHERE e.Estado_Emp = 'Activo' AND e.Cargo_Emp = 'ENFERMERO'";
                 
    try (java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            String nombreCompleto = rs.getString("nombre_Perso") + " " + rs.getString("apellido1_Perso");
            comboBox.addItem(nombreCompleto); // Agregamos cada enfermero al combo
        }
        
    } catch (Exception e) {
        System.err.println("Error al llenar combo de enfermeros: " + e.getMessage());
    }
}
}