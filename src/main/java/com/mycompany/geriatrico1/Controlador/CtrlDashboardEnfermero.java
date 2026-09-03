package com.mycompany.geriatrico1.Controlador;
//import com.mycompany.geriatrico1.vista.Dashboard_Enfermero;
import com.mycompany.geriatrico1.controlador.CtrlFichaPaciente;
import com.mycompany.geriatrico1.dao.AlertaDAO;
import com.mycompany.geriatrico1.dao.EmpleadoDAO;
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
    
    private javax.swing.table.TableRowSorter<javax.swing.table.TableModel> sorterPacientes;

    public CtrlDashboardEnfermero(panel_paciente vista, String cedulaUsuario) {
        
        this.vista = vista;
        vista.txtBuscaPaciAler.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            filtrarTablaPacientes();
        }
        });
        com.mycompany.geriatrico1.dao.EmpleadoDAO empDao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        this.idEnfermeraActual = empDao.obtenerIdEnfermeraPorCedula(cedulaUsuario);
        
        this.vista.btnAgregarCuidado.addActionListener(this);
        this.vista.btnGenerarAlerta.addActionListener(this);
        this.vista.btnCargarPaciente.addActionListener(this);
        
        this.vista.btnVerFichaClini.addActionListener(this);
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
        sorterPacientes = new javax.swing.table.TableRowSorter<>(modelo);
        this.vista.tablaPacientes.setRowSorter(sorterPacientes);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if (e.getSource() == vista.btnAgregarCuidado) {
           int filaVisual = vista.tablaPacientes.getSelectedRow();
            if (filaVisual == -1) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
            String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();

            com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica vistaCompleta = new com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica();

        
            com.mycompany.geriatrico1.controlador.CtrlEnfermero ctrl = new com.mycompany.geriatrico1.controlador.CtrlEnfermero(vistaCompleta, idPaciente, idEnfermeraActual);

            javax.swing.JDialog ventanaCuidado = new javax.swing.JDialog();
            ventanaCuidado.setTitle("Registrar Nuevo Cuidado");
            ventanaCuidado.setModal(true); 
            ventanaCuidado.setContentPane(vistaCompleta.PanelCuidados); 
            ventanaCuidado.pack();
            ventanaCuidado.setLocationRelativeTo(vista);
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
            
            int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
            String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();

            com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica vistaCompleta = new com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica();
            
            com.mycompany.geriatrico1.controlador.CtrlEnfermero ctrl = new com.mycompany.geriatrico1.controlador.CtrlEnfermero(vistaCompleta, idPaciente, idEnfermeraActual);

            javax.swing.JDialog ventanita = new javax.swing.JDialog();
            ventanita.setTitle("Emitir Alerta Médica");
            ventanita.setModal(true);
            ventanita.setContentPane(vistaCompleta.PanelAlerta);
            ventanita.pack();
            ventanita.setLocationRelativeTo(vista); 
            ventanita.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 
            ventanita.setVisible(true);
        
        }
        
       if (e.getSource() == vista.btnVerFichaClini) { 
        int filaVisual = vista.tablaPacientes.getSelectedRow();
        if (filaVisual == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
        String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();
        
        // Instanciamos tu panel y su controlador
        com.mycompany.geriatrico1.vista.panel_principal_paciente vistaFicha = new com.mycompany.geriatrico1.vista.panel_principal_paciente();
        com.mycompany.geriatrico1.controlador.CtrlFichaPaciente ctrlFicha = new com.mycompany.geriatrico1.controlador.CtrlFichaPaciente(vistaFicha, idPaciente);
        
        javax.swing.JDialog dialogoEmergente = new javax.swing.JDialog();
        dialogoEmergente.setTitle("Expediente Clínico del Paciente");
        dialogoEmergente.setModal(true);
        dialogoEmergente.setContentPane(vistaFicha); 
        dialogoEmergente.pack(); 
        dialogoEmergente.setLocationRelativeTo(vista); 
        
        dialogoEmergente.setVisible(true);
    }
       if (e.getSource() == vista.btnCargarPaciente) {
        cargarTablaPacientes(); 
}
       
    }
    private void ocultarColumna(javax.swing.JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setResizable(false);
}
    
    private void abrirFichaClinica() {
        int filaVisual = this.vista.tablaPacientes.getSelectedRow();
        
        if (filaVisual == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(filaVisual);
        String idPaciente = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();
        
        com.mycompany.geriatrico1.vista.panel_principal_paciente vistaFicha = new com.mycompany.geriatrico1.vista.panel_principal_paciente();
        com.mycompany.geriatrico1.controlador.CtrlFichaPaciente ctrlFicha = new com.mycompany.geriatrico1.controlador.CtrlFichaPaciente(vistaFicha, idPaciente);
        
        javax.swing.JDialog dialogoEmergente = new javax.swing.JDialog();
        dialogoEmergente.setTitle("Expediente Clínico del Paciente");
        dialogoEmergente.setModal(true);
        dialogoEmergente.setContentPane(vistaFicha);
        dialogoEmergente.pack();
        dialogoEmergente.setLocationRelativeTo(vista);
        dialogoEmergente.setVisible(true);
        
   
    }
    
    private void filtrarTablaPacientes() {
        String texto = vista.txtBuscaPaciAler.getText().trim();
        
        if (sorterPacientes != null) {
            if (texto.isEmpty()) {
                sorterPacientes.setRowFilter(null); 
            } else {
                sorterPacientes.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texto));
            }
        }
    }
    
}