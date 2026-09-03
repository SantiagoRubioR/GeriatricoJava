package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.dao.PacienteDao;
import com.mycompany.geriatrico1.modelo.*;
import com.mycompany.geriatrico1.controlador.Validador; // Tu clase de validaciones
import com.mycompany.geriatrico1.vista.FichaNewPaciente; // Tu vista real
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class CtrlPaciente implements ActionListener {

    private FichaNewPaciente vista;
    private PacienteDao dao;

    public CtrlPaciente(FichaNewPaciente vista, PacienteDao dao) {
        this.vista = vista;
        this.dao = dao;
        activarAlertaRojaLongitud(vista.txtCedula, 10);
        activarAlertaRojaLongitud(vista.txtTelef, 10);
        // Escuchamos el clic del botón GUARDAR
        this.vista.btnGuardar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.btnGuardar) {
            
            if (vista.btnGuardar.getText().equalsIgnoreCase("Actualizar Paciente")) {
                String estadoC = vista.cmbEstadoCivil.getSelectedItem().toString();
                String sangre = vista.cmbTipoSangre.getSelectedItem().toString();
                String grado = vista.cmbGradoDependencia.getSelectedItem().toString();
        
                if (estadoC.contains("Seleccione") || sangre.contains("Seleccione") || grado.contains("Seleccione")) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Por favor, seleccione valores válidos en Estado Civil, Tipo de Sangre y Grado de Dependencia antes de actualizar.");
                return; 
        }
                try {
                    Persona residente = new Persona();
                    residente.setCedula(vista.txtCedula.getText().trim()); 
                    residente.setNombre1(vista.txtNombre.getText().trim());
                    residente.setApellido1(vista.txtApellido1.getText().trim());
                    residente.setApellido2(vista.txtApellido2.getText().trim());
                    residente.setTelefono(vista.txtTelef.getText().trim());
                    residente.setDireccion(vista.txtDirecc.getText().trim());
                    residente.setCorreo(vista.txtCorreo.getText().trim());
                    residente.setEstadoCivil(vista.cmbEstadoCivil.getSelectedItem().toString());

                    Paciente ficha = new Paciente();
                    ficha.setIdPaciente(vista.btnGuardar.getToolTipText()); // Recuperamos el ID oculto
                    ficha.setGradoDependencia(vista.cmbGradoDependencia.getSelectedItem().toString());
                    ficha.setTipoSangre(vista.cmbTipoSangre.getSelectedItem().toString());

                    if (dao.actualizarPaciente(residente, ficha)) {
                        javax.swing.JOptionPane.showMessageDialog(vista, "Paciente actualizado correctamente.");
                        vista.dispose(); // Cierra y vuelve al Dashboard
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(vista, "Error al actualizar en la BD.");
                    }
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
                }
                return; // IMPORTANTE: Corta la ejecución para que no haga un INSERT abajo
            }
            
            // 0. EXTRACCIÓN DE TEXTOS PARA VALIDACIÓN
            
            String cedPac = vista.txtCedula.getText().trim();
            String nomPac = vista.txtNombre.getText().trim();
            String ape1Pac = vista.txtApellido1.getText().trim();
            String ape2Pac = vista.txtApellido2.getText().trim();
            String telPac = vista.txtTelef.getText().trim();
            String corPac = vista.txtCorreo.getText().trim();
            
            String cedTut = vista.txtCedulaTutor.getText().trim();
            String nomTut = vista.txtNombreTutor.getText().trim();
            String ape1Tut = vista.txtApellidoTutor.getText().trim();
            String ape2Tut = vista.txtApellido1Tutor.getText().trim();
            String telTut = vista.txtTelefonoTutor.getText().trim();
            String corTut = vista.txtCorreoTutor.getText().trim();

            // ==========================================
            // 1. BARRERA DE VALIDACIÓN GLOBAL
            // ==========================================
            
            // 1.1 Validar Cédulas
            if (!Validador.esCedulaValida(cedPac)) {
                JOptionPane.showMessageDialog(vista, "La cédula del paciente es inválida o no cumple el formato ecuatoriano.", "Validación de Cédula", JOptionPane.ERROR_MESSAGE);
                return; // Detiene el proceso
            }
            if (!Validador.esCedulaValida(cedTut)) {
                JOptionPane.showMessageDialog(vista, "La cédula del tutor es inválida o no cumple el formato ecuatoriano.", "Validación de Cédula", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.2 Validar Nombres y Apellidos (Al menos el primer nombre y primer apellido de ambos)
            if (!Validador.esNombreValido(nomPac) || !Validador.esNombreValido(ape1Pac) || 
                !Validador.esNombreValido(nomTut) || !Validador.esNombreValido(ape1Tut)) {
                JOptionPane.showMessageDialog(vista, "Los nombres y primeros apellidos no pueden contener números ni caracteres repetidos en exceso.", "Validación de Texto", JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            // 1.3 Validar Teléfonos
            if (!Validador.esTelefonoValido(telPac) || !Validador.esTelefonoValido(telTut)) {
                JOptionPane.showMessageDialog(vista, "Los teléfonos deben tener exactamente 10 dígitos y empezar con '09'.", "Validación de Teléfono", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1.4 Validar Correos Electrónicos
            if (!Validador.esCorreoValido(corPac) || !Validador.esCorreoValido(corTut)) {
                JOptionPane.showMessageDialog(vista, "Uno de los correos electrónicos no tiene un formato válido (ej. usuario@dominio.com).", "Validación de Correo", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ==========================================
            // 2. CONSTRUCCIÓN DE OBJETOS Y GUARDADO EN BD
            // ==========================================
            try {
                // --- DATOS PERSONALES DEL PACIENTE ---
                Persona residente = new Persona();
                residente.setCedula(cedPac);
                residente.setNombre1(nomPac);
                residente.setApellido1(ape1Pac);
                residente.setApellido2(ape2Pac);
                java.util.Date fechaPac = vista.dateNaci.getDate();
                LocalDate localDatePac = new java.sql.Date(fechaPac.getTime()).toLocalDate();
                residente.setFechaNacimiento(localDatePac);
                //residente.setFechaNacimiento(LocalDate.parse(vista.dateNaci.toString().trim()));
                residente.setGenero(vista.cmbGenero.getSelectedItem().toString().substring(0, 1));
                residente.setEstadoCivil(vista.cmbEstadoCivil.getSelectedItem().toString());
                residente.setTelefono(telPac);
                residente.setDireccion(vista.txtDirecc.getText().trim());
                residente.setCorreo(corPac);

                // --- DATOS CLÍNICOS DE INGRESO ---
                Paciente ficha = new Paciente();
                ficha.setGradoDependencia(vista.cmbGradoDependencia.getSelectedItem().toString());
                ficha.setTipoSangre(vista.cmbTipoSangre.getSelectedItem().toString());

                // --- DATOS PERSONALES DEL TUTOR ---
                Persona tutor = new Persona();
                tutor.setCedula(cedTut);
                tutor.setNombre1(nomTut);
                tutor.setApellido1(ape1Tut);
                tutor.setApellido2(ape2Tut);
                java.util.Date fechaTut = vista.dateNacitTutor.getDate();
                LocalDate localDateTut = new java.sql.Date(fechaTut.getTime()).toLocalDate();
                tutor.setFechaNacimiento(localDateTut);
                //tutor.setFechaNacimiento(LocalDate.parse(vista.dateNacitTutor.toString().trim()));
                tutor.setGenero(vista.cmbGeneroTutor.getSelectedItem().toString().substring(0, 1));
                tutor.setEstadoCivil(vista.cmbEstadoCivilTutor.getSelectedItem().toString());
                tutor.setTelefono(telTut);
                tutor.setDireccion(vista.txtDireccionTutor.getText().trim());
                tutor.setCorreo(corTut);

                // --- VÍNCULO DEL TUTOR ---
                Tutor vinculo = new Tutor();
                vinculo.setParentesco(vista.cmbParentescoTutor.getSelectedItem().toString());
                vinculo.setTipoTutor(vista.cmbTipoTutor.getSelectedItem().toString());

                // --- INYECCIÓN A LA BASE DE DATOS ---
                boolean exito = dao.registrarPacienteCompleto(residente, tutor, vinculo, ficha);
                
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "El paciente y su tutor han sido registrados exitosamente en la Base de Datos.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    vista.dispose(); // Cierra la ventana tras guardar con éxito
                } else {
                    JOptionPane.showMessageDialog(vista, "Ocurrió un error en la base de datos al intentar guardar. Verifique si la cédula ya existe.", "Error de BD", JOptionPane.ERROR_MESSAGE);
                }

            } catch (java.time.format.DateTimeParseException exDate) {
                JOptionPane.showMessageDialog(vista, "Una de las fechas de nacimiento es inválida. Use el formato AAAA-MM-DD (Ej: 1950-12-31).", "Error de Fecha", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Ocurrió un error inesperado al procesar los datos.\nDetalle: " + ex.getMessage(), "Alerta de Sistema", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void activarAlertaRojaLongitud(javax.swing.JTextField campoTexto, int longitudExacta) {
        campoTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // Si lo que escriben no tiene exactamente la longitud pedida, se pinta rojo
                if (campoTexto.getText().trim().length() != longitudExacta) {
                    campoTexto.setForeground(java.awt.Color.RED);
                } else {
                    campoTexto.setForeground(java.awt.Color.BLACK); // Vuelve a la normalidad
                }
            }
        });
    }
    
    
}