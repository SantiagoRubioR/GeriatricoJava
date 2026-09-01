package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.vista.Dashboard_Medico;
import com.mycompany.geriatrico1.dao.AlertaDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class CtrlDashboardMedico implements ActionListener {

    private Dashboard_Medico vista;
    private AlertaDAO alertaDao;

    public CtrlDashboardMedico(Dashboard_Medico vista) {
        this.vista = vista;
        this.alertaDao = new AlertaDAO();

        // 1. Ponemos a escuchar a los 3 botones de María
        this.vista.btnAtender1.addActionListener(this);
        this.vista.btnAtender2.addActionListener(this);
        this.vista.btnAtender3.addActionListener(this);

        // 2. Cargamos las alertas al iniciar
        cargarPanelAlertas();
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
    }
}