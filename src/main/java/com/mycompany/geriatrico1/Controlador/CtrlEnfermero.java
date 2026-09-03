package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.vista.Ficha_Alerta_Estado_FichaClinica;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CtrlEnfermero implements ActionListener {

    private Ficha_Alerta_Estado_FichaClinica vistaEnf;
    private String idPaciente;
    private String idEnfermeraActual;

    public CtrlEnfermero(Ficha_Alerta_Estado_FichaClinica vistaEnf, String idPaciente, String idEnfermeraActual) {
        this.vistaEnf = vistaEnf;
        this.idPaciente = idPaciente;
        this.idEnfermeraActual = idEnfermeraActual;

        this.vistaEnf.btnGuardarCuidado.addActionListener(this);
        this.vistaEnf.btnGuardarAlerta.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if (e.getSource() == vistaEnf.btnGuardarCuidado) { 
            
            String tipoCuidado = vistaEnf.cmbTipoCuidado.getSelectedItem().toString();
            if (tipoCuidado.equals("Seleccione uno...")) {
                JOptionPane.showMessageDialog(vistaEnf, "Por favor, seleccione un tipo de cuidado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String observaciones = vistaEnf.txtObservaciones.getText().trim();
            
            if (tipoCuidado.equals("Otros") && observaciones.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEnf, "Si selecciona 'Otros', detalle el cuidado en Observaciones.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (idEnfermeraActual == null || idEnfermeraActual.isEmpty()) {
                idEnfermeraActual = "ENF-0001"; 
            }

            com.mycompany.geriatrico1.dao.CuidadoDAO cuiDao = new com.mycompany.geriatrico1.dao.CuidadoDAO();
            
            if (cuiDao.registrarCuidado(idEnfermeraActual, idPaciente, tipoCuidado, observaciones)) {
                JOptionPane.showMessageDialog(vistaEnf, "Cuidado registrado con éxito en el historial.");
                javax.swing.SwingUtilities.getWindowAncestor(vistaEnf.PanelCuidados).dispose();
                
                vistaEnf.cmbTipoCuidado.setSelectedIndex(0);
                vistaEnf.txtObservaciones.setText("");
                
            } else {
                JOptionPane.showMessageDialog(vistaEnf, "Error al registrar el cuidado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == vistaEnf.btnGuardarAlerta) {
            
            String seleccionPrioridad = vistaEnf.cmbEstadoAlerta.getSelectedItem().toString();
            if (seleccionPrioridad.equals("Seleccione uno...")) {
                javax.swing.JOptionPane.showMessageDialog(vistaEnf, "Por favor, seleccione el nivel de alerta.");
                return;
            }

            String idPrioridad = "";
            switch (seleccionPrioridad) {
                case "Critica": idPrioridad = "PRI-0001"; break;
                case "Moderada":        idPrioridad = "PRI-0002"; break;
                case "Leve":         idPrioridad = "PRI-0003"; break;
            }

            String observaciones = vistaEnf.txtObservacionesAlerta.getText().trim();
            
            String idMedicoAsignado = "MED-0001"; 

            com.mycompany.geriatrico1.dao.AlertaDAO alertaDao = new com.mycompany.geriatrico1.dao.AlertaDAO();
            
            if (alertaDao.registrarAlerta(idPaciente, idPrioridad, idMedicoAsignado, observaciones)) {
                javax.swing.JOptionPane.showMessageDialog(vistaEnf, "¡Alerta emitida al personal médico exitosamente!");
                
                javax.swing.SwingUtilities.getWindowAncestor(vistaEnf.btnGuardarAlerta).dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vistaEnf, "Error al generar la alerta.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}