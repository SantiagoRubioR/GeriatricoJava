package com.mycompany.geriatrico1.Controlador;
//import com.mycompany.geriatrico1.vista.Dashboard_Enfermero;
import com.mycompany.geriatrico1.controlador.CtrlFichaPaciente;
import com.mycompany.geriatrico1.vista.panel_paciente;
import com.mycompany.geriatrico1.vista.panel_principal_paciente;
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
        
        this.vista.btnVerFichaClini.addActionListener(this);
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
           int filaVisual = vista.tablaPacientes.getSelectedRow();
            if (filaVisual == -1) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
            String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();

            // 2. Instanciamos la ventana gigante, PERO NO LA MOSTRAMOS
            com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica vistaCompleta = new com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica();

        
            com.mycompany.geriatrico1.controlador.CtrlEnfermero ctrl = new com.mycompany.geriatrico1.controlador.CtrlEnfermero(vistaCompleta, idPaciente, idEnfermeraActual);

            javax.swing.JDialog ventanaCuidado = new javax.swing.JDialog();
            ventanaCuidado.setTitle("Registrar Nuevo Cuidado");
            ventanaCuidado.setModal(true); // Bloquea lo de atrás
            ventanaCuidado.setContentPane(vistaCompleta.PanelCuidados); // ¡Extraemos solo el panel!
            ventanaCuidado.pack(); // Se encoge al tamaño exacto de tu panel chiquito
            ventanaCuidado.setLocationRelativeTo(vista); // Lo centra
            ventanaCuidado.setVisible(true);
        }

        // ===========================================
        // BOTÓN: GENERAR ALERTA
        // ===========================================
        else if (e.getSource() == vista.btnGenerarAlerta) {
            int filaVisual = vista.tablaPacientes.getSelectedRow();
            if (filaVisual == -1) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Blindaje del ID oculto
            int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
            String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();

            // Instanciamos la vista completa pero NO la mostramos
            com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica vistaCompleta = new com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica();
            
            // Encendemos el controlador para que escuche el botón "Guardar" de esa ventana
            com.mycompany.geriatrico1.controlador.CtrlEnfermero ctrl = new com.mycompany.geriatrico1.controlador.CtrlEnfermero(vistaCompleta, idPaciente, idEnfermeraActual);

            // Extraemos SOLO el PanelAlertas
            // Truco Ninja: Extraemos SOLO el PanelAlertas metido en un JDialog seguro
            javax.swing.JDialog ventanita = new javax.swing.JDialog();
            ventanita.setTitle("Emitir Alerta Médica");
            ventanita.setModal(true);
            ventanita.setContentPane(vistaCompleta.PanelAlerta);
            ventanita.pack();
            ventanita.setLocationRelativeTo(vista); // Lo centra perfecto respecto al enfermero
            ventanita.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cierra solo esta ventanita de forma segura
            ventanita.setVisible(true);
        
        }
        
       if (e.getSource() == vista.btnVerFichaClini) { // Ajusta el nombre de tu botón
        int filaVisual = vista.tablaPacientes.getSelectedRow();
        if (filaVisual == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Blindaje para sacar el ID oculto de la columna 0
        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
        String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();
        
        // Instanciamos tu panel y su controlador
        com.mycompany.geriatrico1.vista.panel_principal_paciente vistaFicha = new com.mycompany.geriatrico1.vista.panel_principal_paciente();
        com.mycompany.geriatrico1.controlador.CtrlFichaPaciente ctrlFicha = new com.mycompany.geriatrico1.controlador.CtrlFichaPaciente(vistaFicha, idPaciente);
        
        // Envolvemos el Panel en un JDialog para que se abra como ventana
        javax.swing.JDialog dialogoEmergente = new javax.swing.JDialog();
        dialogoEmergente.setTitle("Expediente Clínico del Paciente");
        dialogoEmergente.setModal(true); // Bloquea la ventana de atrás hasta que cierres esta
        dialogoEmergente.setContentPane(vistaFicha); // Metemos tu panel aquí adentro
        dialogoEmergente.pack(); // Ajusta el tamaño automáticamente a tu diseño
        dialogoEmergente.setLocationRelativeTo(vista); // Lo centra en la pantalla
        
        // Lo mostramos
        dialogoEmergente.setVisible(true);
    }
    }
    private void ocultarColumna(javax.swing.JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setResizable(false);
}
    
    private void abrirFichaClinica() {
        int filaVisual = vista.tablaPacientes.getSelectedRow();
        
        if (filaVisual == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Blindaje para el ID oculto
        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
        String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();
        
        // Instanciamos panel y controlador
        com.mycompany.geriatrico1.vista.panel_principal_paciente vistaFicha = new com.mycompany.geriatrico1.vista.panel_principal_paciente();
        com.mycompany.geriatrico1.controlador.CtrlFichaPaciente ctrlFicha = new com.mycompany.geriatrico1.controlador.CtrlFichaPaciente(vistaFicha, idPaciente);
        
        // Truco Ninja del JDialog
        javax.swing.JDialog dialogoEmergente = new javax.swing.JDialog();
        dialogoEmergente.setTitle("Expediente Clínico del Paciente");
        dialogoEmergente.setModal(true);
        dialogoEmergente.setContentPane(vistaFicha);
        dialogoEmergente.pack();
        dialogoEmergente.setLocationRelativeTo(vista);
        dialogoEmergente.setVisible(true);
    }
}