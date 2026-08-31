package com.mycompany.geriatrico1.Controlador;
//import com.mycompany.geriatrico1.vista.Dashboard_Enfermero;
import com.mycompany.geriatrico1.vista.panel_paciente;
//import com.mycompany.geriatrico1.vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CtrlDashboardEnfermero implements ActionListener {

    private panel_paciente vista;
    
    private String idEnfermeraActual;

    public CtrlDashboardEnfermero(panel_paciente vista, String cedulaUsuario) {
        this.vista = vista;
        
        // 1. Convertimos la cédula del login en el ID de la enfermera
        com.mycompany.geriatrico1.dao.EmpleadoDAO empDao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        this.idEnfermeraActual = empDao.obtenerIdEnfermeraPorCedula(cedulaUsuario);
        
        this.vista.btnAgregarCuidado.addActionListener(this);
        this.vista.btnGenerarAlerta.addActionListener(this);
        
        // 3. Llenamos la tabla al abrir
        cargarTablaPacientes();
        ocultarColumna(vista.tablaPacientes, 0);
    }

    private void cargarTablaPacientes() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaPacientes.getModel();
        modelo.setRowCount(0);
        com.mycompany.geriatrico1.dao.PacienteDao daoPac = new com.mycompany.geriatrico1.dao.PacienteDao();
        
        for (Object[] fila : daoPac.listarPacientes()) {
            modelo.addRow(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // ===========================================
        // BOTÓN: REGISTRAR CUIDADO
        // ===========================================
        if (e.getSource() == vista.btnAgregarCuidado) {
            int fila = vista.tablaPacientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla primero.");
                return;
            }

            String idPaciente = vista.tablaPacientes.getValueAt(fila, 0).toString();
            String tipoCuidado = JOptionPane.showInputDialog(vista, "Ingrese el tipo de cuidado (Ej: Medicación, Aseo):");
            
            if (tipoCuidado != null && !tipoCuidado.trim().isEmpty()) {
                com.mycompany.geriatrico1.dao.CuidadoDAO cuiDao = new com.mycompany.geriatrico1.dao.CuidadoDAO();
                if (cuiDao.registrarCuidado(idEnfermeraActual, idPaciente, tipoCuidado, "Registrado desde Dashboard")) {
                    JOptionPane.showMessageDialog(vista, "Cuidado registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar cuidado.");
                }
            }
        }

        // ===========================================
        // BOTÓN: GENERAR ALERTA
        // ===========================================
        else if (e.getSource() == vista.btnGenerarAlerta) {
            int fila = vista.tablaPacientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla primero.");
                return;
            }

            String idPaciente = vista.tablaPacientes.getValueAt(fila, 0).toString();
            // Por tiempo, usamos la prioridad alta por defecto y un médico fijo. 
            // Esto lo pueden mejorar luego.
            String idPrioridad = "PRI-0001"; 
            String idMedico = JOptionPane.showInputDialog(vista, "ID del Médico a notificar (Ej: MED-0001):", "MED-0001");
            
            if (idMedico != null && !idMedico.trim().isEmpty()) {
                com.mycompany.geriatrico1.dao.AlertaDAO alerDao = new com.mycompany.geriatrico1.dao.AlertaDAO();
                if (alerDao.generarAlerta(idPaciente, idPrioridad, idMedico)) {
                    JOptionPane.showMessageDialog(vista, "Alerta roja enviada al médico.");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al enviar alerta. Revise el ID del médico.");
                }
            }
        }
    }
    private void ocultarColumna(javax.swing.JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setResizable(false);
}
}