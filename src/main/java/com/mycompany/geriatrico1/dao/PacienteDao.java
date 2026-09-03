
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
                     "INNER JOIN Persona per ON p.Cedula_Perso_Pac = per.cedula_Perso" + 
                     " WHERE p.Estado_Pac = 'ACTIVO'";
     
    private static final String GET_PERFIL_PACIENTE = "SELECT p.nombre_Perso || ' ' || p.apellido1_Perso AS nombre, " +
                     "pac.Fecha_ingreso_Pac, " +
                     "DATE_PART('year', age(p.fecha_nac_Perso)) AS edad " +
                     "FROM Paciente pac " +
                     "INNER JOIN Persona p ON pac.Cedula_Perso_Pac = p.cedula_Perso " +
                     "WHERE pac.ID_Pac = ?";

    public boolean registrarPacienteCompleto(Persona residente, Persona tutor, Tutor datosTutor, Paciente datosPac) {
        try (Connection con = new Conexion().getConnection()) {
            con.setAutoCommit(false);
            
            try (PreparedStatement psPerResidente = con.prepareStatement(INSERT_PERSONA);
                 PreparedStatement psPerTutor = con.prepareStatement(INSERT_PERSONA);
                 PreparedStatement psTutor = con.prepareStatement(INSERT_TUTOR);
                 PreparedStatement psPac = con.prepareStatement(INSERT_PACIENTE)) {
                
                // 1. Persona Residente
                psPerResidente.setString(1, residente.getCedula());
                psPerResidente.setString(2, residente.getNombre1());
                psPerResidente.setString(3, residente.getApellido1());
                psPerResidente.setString(4, residente.getApellido2());
                psPerResidente.setString(5, residente.getTelefono());
                psPerResidente.setString(6, residente.getDireccion());
                psPerResidente.setString(7, residente.getCorreo());
                psPerResidente.setDate(8, java.sql.Date.valueOf(residente.getFechaNacimiento()));
                psPerResidente.setString(9, residente.getGenero());
                psPerResidente.setString(10, residente.getEstadoCivil());
                psPerResidente.executeUpdate();
                
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
                psPac.setString(1, residente.getCedula());
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
    
    public boolean actualizarPaciente(Persona residente, Paciente ficha) {
        String sqlPersona = "UPDATE Persona SET nombre_Perso=?, apellido1_Perso=?, apellido2_Perso=?, telefono_Perso=?, direccion_Perso=?, correo_Perso=?, estado_civil_Perso=? WHERE cedula_Perso=?";
        String sqlPaciente = "UPDATE PACIENTE SET Tipo_Sandre_Pac=?, Grado_Dependencia=? WHERE ID_Pac=?";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection()) {
            con.setAutoCommit(false); // Iniciamos transacción
            
            try (java.sql.PreparedStatement psPer = con.prepareStatement(sqlPersona);
                 java.sql.PreparedStatement psPac = con.prepareStatement(sqlPaciente)) {
                
                // Actualizar datos de Persona
                psPer.setString(1, residente.getNombre1());
                psPer.setString(2, residente.getApellido1());
                psPer.setString(3, residente.getApellido2());
                psPer.setString(4, residente.getTelefono());
                psPer.setString(5, residente.getDireccion());
                psPer.setString(6, residente.getCorreo());
                psPer.setString(7, residente.getEstadoCivil());
                psPer.setString(8, residente.getCedula()); // El WHERE
                psPer.executeUpdate();
                
                // Actualizar datos Clínicos del Paciente
                psPac.setString(1, ficha.getTipoSangre());
                psPac.setString(2, ficha.getGradoDependencia());
                psPac.setString(3, ficha.getIdPaciente()); // El WHERE
                psPac.executeUpdate();
                
                con.commit(); // Confirmamos transacción
                return true;
                
            } catch (java.sql.SQLException e) {
                con.rollback();
                javax.swing.JOptionPane.showMessageDialog(null, "Error Transaccional: " + e.getMessage());
                return false;
            }
        } catch (java.sql.SQLException ex) {
            return false;
        }
    }
    
    public boolean darDeBajaPaciente(String idPac) {
        
            String sql = "UPDATE PACIENTE SET Estado_Pac = 'INACTIVO' WHERE ID_Pac = ?";

            try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
                 java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, idPac);
                int filasAfectadas = ps.executeUpdate();

                return filasAfectadas > 0; // Retorna true si se actualizó correctamente

            } catch (java.sql.SQLException e) {
                System.err.println("Error al dar de baja al paciente: " + e.getMessage());
                return false;
            }
        }
    
    public int contarPacientesActivos() {
        int totalPacientes = 0;
        // Cuenta las filas donde el estado sea ACTIVO
        String sql = "SELECT COUNT(*) FROM Paciente WHERE UPPER(estado_Pac) = 'ACTIVO'";
        
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                totalPacientes = rs.getInt(1); 
            }
            
        } catch (Exception e) {
            System.err.println("Error al contar pacientes: " + e.getMessage());
        }
        return totalPacientes;
    }
    
    public String[] obtenerPerfilPaciente(String idPaciente) {
        String[] datos = new String[3];
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(GET_PERFIL_PACIENTE)) {
            
            ps.setString(1, idPaciente);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos[0] = rs.getString("nombre");
                    datos[1] = rs.getString("Fecha_ingreso_Pac");
                    datos[2] = rs.getString("edad");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar perfil: " + e.getMessage());
        }
        return datos;
    }
    

    public String[] obtenerContactoEmergencia(String idPaciente) {
        // Arreglo para: [0]Residente, [1]NombreTutor, [2]Parentesco, [3]Telefono, [4]Correo, [5]Direccion
        String[] datos = new String[6];
        
        String sql = "SELECT p_pac.nombre_Perso || ' ' || p_pac.apellido1_Perso AS residente, " +
                     "p_tut.nombre_Perso || ' ' || p_tut.apellido1_Perso AS contacto, " +
                     "t.Parentesco_Tut, " +
                     "p_tut.telefono_Perso, " +
                     "p_tut.correo_Perso, " +
                     "p_tut.direccion_Perso " +
                     "FROM Paciente pac " +
                     "INNER JOIN Persona p_pac ON pac.Cedula_Perso_Pac = p_pac.cedula_Perso " +
                     "INNER JOIN Tutor_Paciente t ON pac.ID_Tut_Pac = t.ID_Tut " +
                     "INNER JOIN Persona p_tut ON t.Cedula_Perso_Tut = p_tut.cedula_Perso " +
                     "WHERE pac.ID_Pac = ?";
                     
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idPaciente);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos[0] = rs.getString("residente");
                    datos[1] = rs.getString("contacto");
                    datos[2] = rs.getString("Parentesco_Tut");
                    datos[3] = rs.getString("telefono_Perso");
                    datos[4] = rs.getString("correo_Perso");
                    datos[5] = rs.getString("direccion_Perso");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar contacto de emergencia: " + e.getMessage());
        }
        return datos;
    }
    // ========================================================
    // LISTAR PACIENTES ACTIVOS (Para el buscador del Médico)
    // ========================================================
    public java.util.List<String[]> listarPacientesActivos() {
        java.util.List<String[]> lista = new java.util.ArrayList<>();
        
        // Unimos Paciente y Persona para tener ID, Cédula y Nombre completo
        String sql = "SELECT p.ID_Pac, per.cedula_Perso, per.nombre_Perso || ' ' || per.apellido1_Perso AS NombreCompleto " +
                     "FROM Paciente p " +
                     "INNER JOIN Persona per ON p.Cedula_Perso_Pac = per.cedula_Perso " +
                     "WHERE UPPER(p.estado_Pac) = 'ACTIVO'"; 
                     
        try (java.sql.Connection con = new com.mycompany.geriatrico1.conexion.Conexion().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String[] paciente = new String[3];
                paciente[0] = rs.getString("ID_Pac");           // Oculto (o visible, según tu diseño)
                paciente[1] = rs.getString("cedula_Perso");     // Cédula
                paciente[2] = rs.getString("NombreCompleto");   // Nombre y Apellido
                lista.add(paciente);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar pacientes activos: " + e.getMessage());
        }
        return lista;
    }
    public Object[] obtenerDatosGeneralesPaciente(String idPaciente) {
        Object[] datos = new Object[4];
            String sql = "SELECT p.cedula_perso, p.estado_civil_perso, pa.tipo_sandre_pac, pa.grado_dependencia " +
             "FROM Persona p INNER JOIN Paciente pa ON p.cedula_perso = pa.cedula_perso_pac " +
             "WHERE pa.id_pac = ?";
        try {
            Connection con = Conexion.getConnection(); // Ajusta según tu clase de conexión
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                datos[0] = rs.getString("cedula_perso");
                datos[1] = rs.getString("estado_civil_perso");
                datos[2] = rs.getString("tipo_sandre_pac");
                datos[3] = rs.getString("grado_dependencia");
            }
        } catch (Exception e) {
            System.out.println("Error en Modelo (Datos Generales): " + e.getMessage());
        }
        return datos;
    }
    public List<Object[]> obtenerEvolucionVital(String idPaciente) {
        List<Object[]> evolucion = new ArrayList<>();
        // Añadimos la Hora a la consulta
            String sql = "SELECT ehc.Fecha_EncabHistoClin, ehc.Hora_EncabHistoClin, dhc.peso_dethisto, dhc.frecuencia_cardiaca_dethisto " +
             "FROM Encabezado_Historial_Clinico ehc " +
             "INNER JOIN Detalle_Historial_Clinico dhc ON ehc.ID_EncabHistoClin = dhc.ID_EncabHistoClin_DetHisto " +
             "WHERE ehc.ID_Pac_EncabHistoClin = ? " + // ¡Busca por ID!
             "ORDER BY ehc.Fecha_EncabHistoClin ASC, ehc.Hora_EncabHistoClin ASC LIMIT 5";
        try {
            Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] registro = new Object[3];
                // Unimos Fecha y Hora para que la etiqueta en la gráfica NUNCA se repita
                registro[0] = rs.getString("Fecha_EncabHistoClin") + " " + rs.getString("Hora_EncabHistoClin").substring(0, 5); 
                registro[1] = rs.getDouble("peso_dethisto");
                registro[2] = rs.getDouble("frecuencia_cardiaca_dethisto");
                evolucion.add(registro);
            }
        } catch (Exception e) {
            System.out.println("Error en Modelo (Evolución): " + e.getMessage());
        }
        return evolucion;
    }
    
        public List<Object[]> obtenerTratamientos(String idPaciente) {
            List<Object[]> lista = new ArrayList<>();
            // Hacemos INNER JOIN para cruzar el paciente -> recibe_tratamiento -> detalle -> tipo
            String sql = "SELECT dt.id_dettra, tt.nombre_tipotra, dt.fecha_ini_dettra, dt.fecha_fin_dettra, dt.estado_dettra, dt.observaciones_dettra " +
                         "FROM recibe_tratamiento rt " +
                         "INNER JOIN encabezado_tratamiento et ON rt.id_encabtra_recitrata = et.id_encabtra " +
                         "INNER JOIN detalle_tratamiento dt ON et.id_encabtra = dt.id_encabtra_dettra " +
                         "INNER JOIN tipo_tratamiento tt ON dt.id_tipotra_dettra = tt.id_tipotra " +
                         "WHERE rt.id_pac_recitrata = ? " +
                         "ORDER BY dt.estado_dettra ASC, dt.fecha_ini_dettra DESC"; // Los "En proceso" salen primero
            try {
                java.sql.Connection con = Conexion.getConnection();
                java.sql.PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, idPaciente);
                java.sql.ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getString("id_dettra"); // Ocultaremos esto en la tabla luego
                    fila[1] = rs.getString("nombre_tipotra");
                    fila[2] = rs.getString("fecha_ini_dettra");
                    fila[3] = rs.getString("fecha_fin_dettra");
                    fila[4] = rs.getString("estado_dettra");
                    fila[5] = rs.getString("observaciones_dettra");
                    lista.add(fila);
                }
            } catch (Exception e) {
                System.out.println("Error al cargar tratamientos: " + e.getMessage());
            }
            return lista;
        }

        // 2. Método para que el enfermero de cumplimiento al tratamiento
        public boolean finalizarTratamiento(String idDetalleTra) {
            String sql = "UPDATE detalle_tratamiento SET estado_dettra = 'Completado' WHERE id_dettra = ?";
            try {
                java.sql.Connection con = Conexion.getConnection();
                java.sql.PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, idDetalleTra);
                ps.executeUpdate();
                return true;
            } catch (Exception e) {
                System.out.println("Error al finalizar tratamiento: " + e.getMessage());
                return false;
            }
        }
        
        public List<Object[]> obtenerHistorialCuidados(String idPaciente) {
        List<Object[]> lista = new ArrayList<>();
        
        // Súper consulta cruzando 4 tablas para sacar el nombre real del enfermero
        String sql = "SELECT c.fecha_cui, c.hora_cui, " +
                     "p.nombre_perso || ' ' || p.apellido1_perso AS nombre_enfermero, " +
                     "c.tipo_cui, c.observaciones_cui " +
                     "FROM cuidado c " +
                     "INNER JOIN enfermera enf ON c.id_enfer_cui = enf.id_enfer " +
                     "INNER JOIN empleado emp ON enf.id_emp_enfer = emp.id_emp " +
                     "INNER JOIN persona p ON emp.cedula_perso_emp = p.cedula_perso " +
                     "WHERE c.id_pac_cui = ? " +
                     "ORDER BY c.fecha_cui DESC, c.hora_cui DESC"; // Lo más reciente sale arriba
        try {
            java.sql.Connection con = Conexion.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPaciente);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("fecha_cui");
                
                // Le quitamos los milisegundos a la hora para que se vea estético (ej. 14:30)
                String hora = rs.getString("hora_cui");
                fila[1] = (hora != null && hora.length() >= 5) ? hora.substring(0, 5) : hora; 
                
                fila[2] = rs.getString("nombre_enfermero");
                fila[3] = rs.getString("tipo_cui");
                fila[4] = rs.getString("observaciones_cui");
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar historial de cuidados: " + e.getMessage());
        }
        return lista;
    }
        public List<Object[]> obtenerHistoriaClinica(String idPaciente) {
        List<Object[]> lista = new ArrayList<>();
        
        String sql = "SELECT ehc.fecha_encabhistoclin, ehc.hora_encabhistoclin, " +
                     "per.nombre_perso || ' ' || per.apellido1_perso AS nombre_medico, " +
                     "dhc.diagnostico_dethisto, dhc.peso_dethisto, dhc.temperatura_dethisto, " +
                     "dhc.frecuencia_cardiaca_dethisto, " +
                     "pa.presion_sistolica_presart || '/' || pa.presion_diastolica_presart AS presion, " +
                     "dhc.estado_dethisto " +
                     "FROM encabezado_historial_clinico ehc " +
                     "INNER JOIN detalle_historial_clinico dhc ON ehc.id_encabhistoclin = dhc.id_encabhistoclin_dethisto " +
                     "INNER JOIN medico m ON dhc.id_med_dethisto = m.id_med " +
                     "INNER JOIN empleado emp ON m.id_emp_med = emp.id_emp " +
                     "INNER JOIN persona per ON emp.cedula_perso_emp = per.cedula_perso " +
                     "LEFT JOIN presion_arterial pa ON dhc.id_presart_dethisto = pa.id_presart " +
                     "WHERE ehc.id_pac_encabhistoclin = ? " +
                     "ORDER BY ehc.fecha_encabhistoclin DESC, ehc.hora_encabhistoclin DESC";
                     
        try {
            java.sql.Connection con = Conexion.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPaciente);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[9];
                fila[0] = rs.getString("fecha_encabhistoclin");
                
                String hora = rs.getString("hora_encabhistoclin");
                fila[1] = (hora != null && hora.length() >= 5) ? hora.substring(0, 5) : hora; 
                
                fila[2] = rs.getString("nombre_medico");
                fila[3] = rs.getString("diagnostico_dethisto");
                fila[4] = rs.getDouble("peso_dethisto");
                fila[5] = rs.getDouble("temperatura_dethisto");
                fila[6] = rs.getInt("frecuencia_cardiaca_dethisto");
                fila[7] = rs.getString("presion");
                fila[8] = rs.getString("estado_dethisto");
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar historia clínica: " + e.getMessage());
        }
        return lista;
    }
}   
    
    
    

