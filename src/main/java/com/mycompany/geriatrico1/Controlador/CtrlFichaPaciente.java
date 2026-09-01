package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.vista.panel_principal_paciente;
import com.mycompany.geriatrico1.dao.PacienteDao;
import java.awt.event.ActionEvent;

public class CtrlFichaPaciente implements java.awt.event.ActionListener{

    private panel_principal_paciente vista;
    private String idPaciente;
    private PacienteDao pacDao;

    public CtrlFichaPaciente(panel_principal_paciente vista, String idPaciente) {
        this.vista = vista;
        this.idPaciente = idPaciente;
        this.pacDao = new PacienteDao();
        
        // Ejecutamos la carga del perfil apenas nace el controlador
        cargarDatosPerfil();
        this.vista.btnContactoEmergencia.addActionListener(this);
    }

    private void cargarDatosPerfil() {
        // Pedimos los 3 datos al DAO
        String[] datos = pacDao.obtenerPerfilPaciente(idPaciente);
        
        if (datos[0] != null) { 
            // ¡AJUSTA ESTOS NOMBRES SEGÚN TU DISEÑO EN NETBEANS!
            
            vista.txtNombre.setText(datos[0]); // Nombre y Apellido
            vista.txtFechaDeIngreso.setText(datos[1]); // Fecha de ingreso
            
            // Le quitamos los decimales a la edad por si Postgres nos devuelve "75.0"
            String edadLimpia = datos[2].replace(".0", ""); 
            vista.txtEdad.setText(edadLimpia + " años");
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        if (e.getSource() == vista.btnContactoEmergencia) {
            // 1. Pedimos los datos a la base
            String[] datos = pacDao.obtenerContactoEmergencia(idPaciente);
            
            if (datos[0] == null) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Este paciente no tiene un tutor registrado en el sistema.", "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 2. Instanciamos tu panel de emergencia
            com.mycompany.geriatrico1.vista.panel_contacto_emergencia panelCE = new com.mycompany.geriatrico1.vista.panel_contacto_emergencia();
            
            // 3. Llenamos las cajas de texto (Ajusta los nombres a como los tengas en NetBeans, recuerda ponerlos public)
            panelCE.lblResidente.setText(datos[0]);
            panelCE.lblContactoPrincipal.setText(datos[1]);
            panelCE.txtParentesco.setText(datos[2]);
            panelCE.txtTelefonoPrincipal.setText(datos[3]);
            panelCE.txtCorreo.setText(datos[4]);
            panelCE.txtDireccion.setText(datos[5]);
            
            // 4. Bloqueamos todo para que sea de solo lectura
            panelCE.txtParentesco.setEditable(false);
            panelCE.txtTelefonoPrincipal.setEditable(false);
            panelCE.txtCorreo.setEditable(false);
            panelCE.txtDireccion.setEditable(false);

            // 5. Truco Ninja para abrirlo encima
            javax.swing.JDialog dialogoEmerg = new javax.swing.JDialog();
            dialogoEmerg.setTitle("Contacto de Emergencia");
            dialogoEmerg.setModal(true); // Bloquea lo de atrás
            dialogoEmerg.setContentPane(panelCE);
            dialogoEmerg.pack();
            dialogoEmerg.setLocationRelativeTo(vista);
            dialogoEmerg.setVisible(true);
        }
    }
}