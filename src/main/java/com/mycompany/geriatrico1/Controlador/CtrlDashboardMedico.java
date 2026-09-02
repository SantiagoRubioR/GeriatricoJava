package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.vista.Dashboard_Medico;
import com.mycompany.geriatrico1.dao.AlertaDAO;
import com.mycompany.geriatrico1.dao.PacienteDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class CtrlDashboardMedico implements ActionListener {
    private String idPacienteSeleccionado = "";
    private Dashboard_Medico vista;
    private AlertaDAO alertaDao;
    private java.util.List<String[]> listaPacientesActivos;
    private String nombrePacienteSeleccionado = "";

    public CtrlDashboardMedico(Dashboard_Medico vista) {
        this.vista = vista;
        this.alertaDao = new AlertaDAO();

        // 1. Ponemos a escuchar a los 3 botones de María
        this.vista.btnAtender1.addActionListener(this);
        this.vista.btnAtender2.addActionListener(this);
        this.vista.btnAtender3.addActionListener(this);
        this.vista.btnCargarAlertas.addActionListener(this);
        // 2. Cargamos las alertas al iniciar
        cargarPanelAlertas();
        cargarTablaPacientesActivos();
        this.vista.tablaPacientesActivos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarPacienteParaConsulta();
            }
        });
  }
    
    private void cargarTablaPacientesActivos() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaPacientesActivos.getModel();
        modelo.setRowCount(0); 
        
        PacienteDao pacDao = new PacienteDao();
        listaPacientesActivos = pacDao.listarPacientesActivos(); // Guardamos en memoria
        
        for (String[] pac : listaPacientesActivos) {
            // pac[0] = ID (Lo ignoramos aquí)
            // pac[1] = Cédula, pac[2] = Nombre Completo
            modelo.addRow(new Object[]{pac[1], pac[2]}); // ¡Solo 2 datos para 2 columnas!
        }
    }

   
    private void cargarPanelAlertas() {
        // Primero, escondemos los 3 paneles por si no hay alertas
        vista.panelAlerta1.setVisible(false);
        vista.panelAlerta2.setVisible(false);
        vista.panelAlerta3.setVisible(false);

        List<String[]> alertas = alertaDao.obtenerAlertasPendientes();

        // Llenamos el Panel 1 si hay al menos 1 alerta
        if (alertas.size() > 0) {
            vista.lblNombrePaciente1.setText(alertas.get(0)[1]); 
            vista.lblObservacion1.setText(alertas.get(0)[2]);
            vista.lblHora1.setText("Hoy " + alertas.get(0)[3]);
            vista.btnAtender1.setToolTipText(alertas.get(0)[0]);
            vista.panelAlerta1.setVisible(true); 
        }

        
        if (alertas.size() > 1) {
            vista.lblNombrePaciente2.setText(alertas.get(1)[1]);
            vista.lblObservacion2.setText(alertas.get(1)[2]);
            vista.lblHora2.setText("Hoy " + alertas.get(1)[3]);
            vista.btnAtender2.setToolTipText(alertas.get(1)[0]); 
            vista.panelAlerta2.setVisible(true);
        }

        // Llenamos el Panel 3 si hay 3 alertas
        if (alertas.size() > 2) {
            vista.lblNombrePaciente3.setText(alertas.get(2)[1]);
            vista.lblObservacion3.setText(alertas.get(2)[2]);
            vista.lblHora3.setText("Hoy " + alertas.get(2)[3]);
            vista.btnAtender3.setToolTipText(alertas.get(2)[0]); 
            vista.panelAlerta3.setVisible(true);
        }
    }
    
    private void seleccionarPacienteParaConsulta() {
        int filaVisual = vista.tablaPacientesActivos.getSelectedRow();
        if (filaVisual == -1) return;

        int filaModelo = vista.tablaPacientesActivos.convertRowIndexToModel(filaVisual);
        
        // Sacamos la cédula y el nombre de la tabla
        String cedula = vista.tablaPacientesActivos.getModel().getValueAt(filaModelo, 0).toString();
        nombrePacienteSeleccionado = vista.tablaPacientesActivos.getModel().getValueAt(filaModelo, 1).toString();

        // Buscamos el ID oculto usando la lista que guardamos en memoria
        for (String[] pac : listaPacientesActivos) {
            if (pac[1].equals(cedula)) {
                idPacienteSeleccionado = pac[0]; // ¡Atrapamos el ID_Pac sin que la tabla explote!
                break;
            }
        }

        // --- LLENAR LA TABLA SUPERIOR DEL HISTORIAL (Como está en tu diseño) ---
        javax.swing.table.DefaultTableModel modeloHistorial = (javax.swing.table.DefaultTableModel) vista.tablaPacientesHistorial.getModel(); // ¡Ajusta el nombre de la tabla!
        modeloHistorial.setRowCount(0); 
        modeloHistorial.addRow(new Object[]{cedula, nombrePacienteSeleccionado}); 
        
        // (Opcional) Si quieres cargar la tabla de Antecedentes, iría aquí...
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ==========================================================
        // BOTONES DE ATENDER ALERTA
        // ==========================================================
        
        // Identificamos cuál de los 3 botones se presionó y extraemos su ID oculto
        String idAlertaAtender = null;

        if (e.getSource() == vista.btnAtender1) {
            idAlertaAtender = vista.btnAtender1.getToolTipText();
        } else if (e.getSource() == vista.btnAtender2) {
            idAlertaAtender = vista.btnAtender2.getToolTipText();
        } else if (e.getSource() == vista.btnAtender3) {
            idAlertaAtender = vista.btnAtender3.getToolTipText();
        }

        // Si atrapamos un ID, vamos a la base de datos
        if (idAlertaAtender != null) {
            if (alertaDao.atenderAlerta(idAlertaAtender)) {
                JOptionPane.showMessageDialog(vista, "Alerta marcada como atendida.");
                
                // ¡Recargamos los paneles para que la siguiente alerta suba!
                cargarPanelAlertas(); 
            } else {
                JOptionPane.showMessageDialog(vista, "Error al atender la alerta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == vista.btnCargarAlertas) {
            cargarPanelAlertas(); 
        }
        
        if (e.getSource() == vista.NuevaConsulta) { // Usa el nombre exacto del botón de tu imagen
            
            // 1. Validamos que haya dado clic en la tabla primero
            if (idPacienteSeleccionado.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Primero seleccione un paciente de la lista lateral.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return; // Cortamos la ejecución para que no cambie de panel
            }

            // 2. Llenamos el nombre en el TextField del panel verde
            // (Ajusta 'txtPacienteNuevaEntrada' al nombre real que tenga esa caja en NetBeans)
            vista.txtBuscar.setText(nombrePacienteSeleccionado); 
            vista.txtBuscar.setEditable(false);

            // 3. Ejecutamos la navegación exacta que hizo María en tu captura
            vista.seleccionarBoton(vista.NuevaConsulta);
            vista.panel.setVisible(true); // El panel verde
            vista.PanelAlertas.setVisible(false);
            vista.PanelHistorial.setVisible(false);
            vista.PanelGenerar.setVisible(false);
        }
    }
        
    }