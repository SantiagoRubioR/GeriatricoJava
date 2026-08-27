package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.dao.EmpleadoDAO;
import com.mycompany.geriatrico1.modelo.*;
import com.mycompany.geriatrico1.controlador.Validador;
import com.mycompany.geriatrico1.vista.FichaNuevaCuenta; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class CtrlEmpleados implements ActionListener {

    private FichaNuevaCuenta vista;
    private EmpleadoDAO dao;

    public CtrlEmpleados(FichaNuevaCuenta vista, EmpleadoDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.vista.btnGuardarFicha.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardarFicha) {
            
            // 0. CAPTURA DE TEXTOS BÁSICOS
            String cedula = vista.txtCedula.getText().trim();
            String nombre = vista.txtNombre.getText().trim();
            String apellido1 = vista.txtApellido1.getText().trim();
            String apellido2 = vista.txtApellido2.getText().trim();
            String telefono = vista.txtTelef.getText().trim();
            String correo = vista.txtCorreo.getText().trim();
            String rol = vista.cmbRol.getSelectedItem().toString().toUpperCase();

           // 1.1 Validar Cédula
            if (!Validador.esCedulaValida(cedula)) {
                JOptionPane.showMessageDialog(vista, "La cédula ingresada es inválida o no cumple el formato ecuatoriano.", "Validación de Cédula", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.2 Validar Nombres y Apellidos (No vacíos, sin números, sin letras repetidas)
            if (!Validador.esNombreValido(nombre) || !Validador.esNombreValido(apellido1) || !Validador.esNombreValido(apellido2)) {
                JOptionPane.showMessageDialog(vista, "Los nombres y apellidos son obligatorios, no pueden contener números ni caracteres repetidos en exceso.", "Validación de Texto", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.3 Validar Teléfono
            if (!Validador.esTelefonoValido(telefono)) {
                JOptionPane.showMessageDialog(vista, "El teléfono debe tener exactamente 10 dígitos y empezar con '09'.", "Validación de Teléfono", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.4 Validar Correo
            if (!Validador.esCorreoValido(correo)) {
                JOptionPane.showMessageDialog(vista, "El formato del correo electrónico es incorrecto (Ej: usuario@dominio.com).", "Validación de Correo", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.5 Validar Dirección vacía
            if (vista.txtDirecc.getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "La dirección es obligatoria.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 1.6 Validar que se haya seleccionado una fecha de nacimiento
            if (vista.dateNaci.getDate() == null) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una fecha de nacimiento válida.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // 2. DATOS PERSONALES
                Persona persona = new Persona();
                persona.setCedula(cedula);
                persona.setNombre1(nombre);
                persona.setApellido1(apellido1);
                persona.setApellido2(apellido2);
                persona.setTelefono(telefono);
                persona.setDireccion(vista.txtDirecc.getText().trim());
                persona.setCorreo(correo);
                persona.setGenero(vista.cmbGenero.getSelectedItem().toString().substring(0, 1));
                persona.setEstadoCivil(vista.cmbEstCivCuenNue.getSelectedItem().toString());
                
                // Extraer fecha del JDateChooser
                java.util.Date fecha = vista.dateNaci.getDate();
                persona.setFechaNacimiento(new java.sql.Date(fecha.getTime()).toLocalDate());

                // 3. DATOS LABORALES
                Empleado empleado = new Empleado();
                empleado.setCargo(rol);
                // NOTA: Ajusta "cmbContratoAdmin" al nombre real de tu combobox de contrato según el panel activo
                empleado.setTipoContrato(vista.cmbContraMed.getSelectedItem().toString());

                // 4. USUARIO Y CONTRASEÑA AUTOMÁTICOS
                Usuario usuario = new Usuario();
                // El Trigger de la BD crea el username. Nosotros enviamos la cédula como contraseña por defecto.
                usuario.setContrasena(cedula); 

                // 5. DATOS ESPECÍFICOS DE ROL
                Administrador admin = new Administrador();
                Medico medico = new Medico();
                Enfermero enfermera = new Enfermero();

                if (rol.equals("MEDICO")) {
                    
                    medico.setRegistroProfesional(vista.txtRegisProfMed.getText().trim());
                    medico.setNivelFormacion(vista.cmbNivelFormaMed.getSelectedItem().toString());
                    medico.setEspecialidad(vista.txtEspeMed.getText().trim());
                    
                } else if (rol.equals("ENFERMERO") || rol.equals("ENFERMERA")) {
                    enfermera.setNumeroLicencia(vista.txtNumLicenEnfer.getText().trim());
                    enfermera.setNivelFormacion(vista.cbxNivelForma.getSelectedItem().toString());
                    enfermera.setEspecialidad(vista.txtEspeEnfer.getText().trim());
                }

                // 6. ENVIAR A LA BASE DE DATOS
                if (dao.registrarPersonalCompleto(persona, empleado, usuario, admin, medico, enfermera, rol)) {
                    JOptionPane.showMessageDialog(vista, "Personal registrado.\nUsuario: " + cedula + "\nContraseña Temporal: " + cedula, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    vista.dispose();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Revise que todos los campos y fechas estén llenos.\n" + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}