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
        
        activarAlertaRojaLongitud(vista.txtCedula, 10);
        activarAlertaRojaLongitud(vista.txtTelef, 10);


        if(vista.txtNumLicenEnfer != null){
            activarAlertaRojaFormato(vista.txtNumLicenEnfer, "^ENF-2026-\\d{5}$");
        }


        if(vista.txtRegisProfMed != null){
            activarAlertaRojaFormato(vista.txtRegisProfMed, "^REG-MED-\\d{4}$");
        }
        
    }
    
    private void activarAlertaRojaLongitud(javax.swing.JTextField campoTexto, int longitudExacta) {
        campoTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (campoTexto.getText().trim().length() != longitudExacta) {
                    campoTexto.setForeground(java.awt.Color.RED);
                } else {
                    campoTexto.setForeground(java.awt.Color.BLACK);
                }
            }
        });
    }

    private void activarAlertaRojaFormato(javax.swing.JTextField campoTexto, String patronRegex) {
        campoTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (!campoTexto.getText().trim().matches(patronRegex)) {
                    campoTexto.setForeground(java.awt.Color.RED);
                } else {
                    campoTexto.setForeground(java.awt.Color.BLACK);
                }
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardarFicha) {
            
            if (vista.btnGuardarFicha.getText().equalsIgnoreCase("Actualizar Cuenta")) {
                String estadoC = vista.cmbEstCivCuenNue.getSelectedItem().toString();
                String rol = vista.cmbRol.getSelectedItem().toString();
                String contrato = vista.cmbContraMed.getSelectedItem().toString();

                if (estadoC.contains("Seleccione") || rol.contains("Seleccione") || contrato.contains("Seleccione")) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Por favor, seleccione valores válidos en Estado Civil, Rol y Tipo de Contrato.");
            return;
        }
                try {
                    Persona persona = new Persona();
                    persona.setCedula(vista.txtCedula.getText().trim()); 
                    persona.setNombre1(vista.txtNombre.getText().trim());
                    persona.setApellido1(vista.txtApellido1.getText().trim());
                    persona.setApellido2(vista.txtApellido2.getText().trim());
                    persona.setTelefono(vista.txtTelef.getText().trim());
                    persona.setDireccion(vista.txtDirecc.getText().trim());
                    persona.setCorreo(vista.txtCorreo.getText().trim());
                    persona.setEstadoCivil(vista.cmbEstCivCuenNue.getSelectedItem().toString());


                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(vista.btnGuardarFicha.getToolTipText()); 
                    empleado.setCargo(vista.cmbRol.getSelectedItem().toString());
                    empleado.setTipoContrato(vista.cmbContraMed.getSelectedItem().toString());

                    if (dao.actualizarEmpleadoTransaccional(persona, empleado)) {
                        javax.swing.JOptionPane.showMessageDialog(vista, "Empleado actualizado con éxito.");
                        vista.dispose();
                    }
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
                }
                return;
            }
            

            String cedula = vista.txtCedula.getText().trim();
            String nombre = vista.txtNombre.getText().trim();
            String apellido1 = vista.txtApellido1.getText().trim();
            String apellido2 = vista.txtApellido2.getText().trim();
            String telefono = vista.txtTelef.getText().trim();
            String correo = vista.txtCorreo.getText().trim();
            String rol = vista.cmbRol.getSelectedItem().toString().toUpperCase();

           
            if (!Validador.esCedulaValida(cedula)) {
                JOptionPane.showMessageDialog(vista, "La cédula ingresada es inválida o no cumple el formato ecuatoriano.", "Validación de Cédula", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!Validador.esNombreValido(nombre) || !Validador.esNombreValido(apellido1) || !Validador.esNombreValido(apellido2)) {
                JOptionPane.showMessageDialog(vista, "Los nombres y apellidos son obligatorios, no pueden contener números ni caracteres repetidos en exceso.", "Validación de Texto", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!Validador.esTelefonoValido(telefono)) {
                JOptionPane.showMessageDialog(vista, "El teléfono debe tener exactamente 10 dígitos y empezar con '09'.", "Validación de Teléfono", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!Validador.esCorreoValido(correo)) {
                JOptionPane.showMessageDialog(vista, "El formato del correo electrónico es incorrecto (Ej: usuario@dominio.com).", "Validación de Correo", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (vista.txtDirecc.getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "La dirección es obligatoria.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (vista.dateNaci.getDate() == null) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una fecha de nacimiento válida.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
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
                
                java.util.Date fecha = vista.dateNaci.getDate();
                persona.setFechaNacimiento(new java.sql.Date(fecha.getTime()).toLocalDate());

                Empleado empleado = new Empleado();
                empleado.setCargo(rol);
                empleado.setTipoContrato(vista.cmbContraMed.getSelectedItem().toString());

                Usuario usuario = new Usuario();
                usuario.setContrasena(cedula); 

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
                    
                    String textoHorario = vista.cbxHoraEnfer.getSelectedItem().toString();
                    String idHorarioT = "";
                    if (textoHorario.startsWith("Matutin")) {
                    idHorarioT = "JTU-0001";
                    } else if (textoHorario.startsWith("Vespertin")) {
                    idHorarioT = "JTU-0002";
                    } else if (textoHorario.startsWith("Nocturn")) {
                    idHorarioT = "JTU-0003";
                    }
                    enfermera.setIdHorario(idHorarioT);
                    System.out.println("Enviando a PostgreSQL el código de jornada: [" + idHorarioT + "]");
                }

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