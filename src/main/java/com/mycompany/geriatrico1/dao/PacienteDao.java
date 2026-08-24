
package com.mycompany.geriatrico1.dao;
//Packages Relacionales
import com.mycompany.geriatrico1.conexion.Conexion;
import com.mycompany.geriatrico1.modelo.Paciente;
import com.mycompany.geriatrico1.modelo.Persona;
import com.mycompany.geriatrico1.modelo.Tutor;
//Packages BaseDO

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PacienteDao {
    private static final String INSERTAR_PERSONA = "(Cedula_perso, Nombre_Perso, Apellido1_Perso, Apellido2_Perso, Telefono_Perso, Direccion_Perso, Correo_Elec_Perso, Fecha_Nac_Perso, Genero_Perso, Estado_Civil_Perso) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\"";
    private static final String INSERTAR_PACIENTE = "INSERT INTO PACIENTE (ID_Pac, Cedula_Perso_Pac, ID_Tut, Tipo_Sandre_Pac) VALUES (?, ?, ?, ?)";
    
    public boolean registrarPacienteTransaccional(Persona abuelo, Persona tutor, Tutor datosTutor, Paciente datosPaciente) {
        
        // 2. EL ESTILO DE TU PROFESORA: try-with-resources para la conexión
        try (Connection conn = Conexion.getConnection()) {
            
            // 3. LA REGLA DE LA 5FN: Apagamos el autocommit para proteger la base
            conn.setAutoCommit(false); 
            
            try (PreparedStatement psAbuelo = conn.prepareStatement(INSERTAR_PERSONA);
                 PreparedStatement psPaciente = conn.prepareStatement(INSERTAR_PACIENTE)) {
                 
                // Ejecutamos el primer INSERT (Persona)
                psAbuelo.setString(1, abuelo.getCedula());
                psAbuelo.setString(2, abuelo.getNombre1());
                // ... llenar el resto
                psAbuelo.executeUpdate();
                
                // Ejecutamos el INSERT final (Paciente)
                psPaciente.setString(1, datosPaciente.getIdPaciente());
                psPaciente.setString(2, abuelo.getCedula());
                // ... llenar el resto
                psPaciente.executeUpdate();
                
                // Si llegamos hasta aquí sin errores, guardamos todo de golpe
                conn.commit(); 
                return true;
                
            } catch (SQLException e) {
                // Si falla CUALQUIER insert, deshacemos todo
                conn.rollback(); 
                System.err.println("Error en transacción 5FN: " + e.getMessage());
                return false;
            }
            
        } catch (SQLException ex) {
            System.err.println("Error de conexión: " + ex.getMessage());
            return false;
        }
    }
}   
    
    
    

