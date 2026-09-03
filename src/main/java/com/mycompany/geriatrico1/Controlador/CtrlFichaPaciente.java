package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.vista.panel_principal_paciente;
import com.mycompany.geriatrico1.dao.PacienteDao;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class CtrlFichaPaciente implements java.awt.event.ActionListener{

    private panel_principal_paciente vista;
    private String idPaciente;
    private PacienteDao pacDao;

    public CtrlFichaPaciente(panel_principal_paciente vista, String idPaciente) {
        this.vista = vista;
        this.idPaciente = idPaciente;
        this.pacDao = new PacienteDao();
        this.vista.btnFinalizarTratamiento.addActionListener(this);
        this.vista.btnGenerarReporte.addActionListener(this);
        
        cargarDatosPerfil();
        cargarExpedienteCompleto(this.idPaciente);
        this.vista.btnContactoEmergencia.addActionListener(this);
    }

    private void cargarDatosPerfil() {
        String[] datos = pacDao.obtenerPerfilPaciente(idPaciente);
        
        if (datos[0] != null) { 
            
            vista.txtNombre.setText(datos[0]); 
            vista.txtFechaDeIngreso.setText(datos[1]); 
            
            String edadLimpia = datos[2].replace(".0", ""); 
            vista.txtEdad.setText(edadLimpia + " años");
        }
        cargarExpedienteCompleto(this.idPaciente);
        cargarTablaTratamientos(this.idPaciente);
        cargarTablaCuidados(this.idPaciente);
        cargarTablaHistoriaClinica(this.idPaciente);
    }
    
    public void cargarExpedienteCompleto(String idPaciente) {
        PacienteDao modelo = new PacienteDao();
        
        Object[] datosGenerales = modelo.obtenerDatosGeneralesPaciente(idPaciente);
        if (datosGenerales[0] != null) {
            vista.lblCedula.setText("Cédula: " + datosGenerales[0].toString());
            vista.lblEstadoCivil.setText("Estado Civil: " + datosGenerales[1].toString());
            vista.lblTipoSangre.setText("Tipo de Sangre: " + datosGenerales[2].toString());
            vista.lblDependencia.setText("Grado de Dependencia: " + datosGenerales[3].toString());
        }

        List<Object[]> datosEvolucion = modelo.obtenerEvolucionVital(idPaciente);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (!datosEvolucion.isEmpty()) {
            for (Object[] fila : datosEvolucion) {
                String fecha = fila[0].toString();
                double peso = (Double) fila[1];
                double fc = (Double) fila[2];
                
                dataset.addValue(fc, "Frecuencia Cardíaca (lpm)", fecha);
                dataset.addValue(peso, "Peso (kg)", fecha);
            }
            
            JFreeChart chart = ChartFactory.createLineChart(
                "Evolución de Signos Vitales", 
                "Fechas de Consulta",          
                "Valores",                     
                dataset,                       
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = 
                (org.jfree.chart.renderer.category.LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setDefaultShapesVisible(true);
            ChartPanel chartPanel = new ChartPanel(chart);
            vista.panelGraficoEvolucion.removeAll();
            vista.panelGraficoEvolucion.add(chartPanel, BorderLayout.CENTER);
            vista.panelGraficoEvolucion.validate();
        } else {
            vista.panelGraficoEvolucion.removeAll();
            vista.panelGraficoEvolucion.repaint();
        }
    }
    
    public void cargarTablaTratamientos(String idPaciente) {
        String[] columnas = {"ID Oculto", "Tipo de Tratamiento", "Inicio", "Fin", "Estado", "Indicaciones Médicas"};
        DefaultTableModel modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        List<Object[]> tratamientos = pacDao.obtenerTratamientos(idPaciente); 
        for (Object[] fila : tratamientos) {
            modeloTabla.addRow(fila);
        }
        
        vista.tablaTratamientos.setModel(modeloTabla);
        
        vista.tablaTratamientos.getColumnModel().getColumn(0).setMinWidth(0);
        vista.tablaTratamientos.getColumnModel().getColumn(0).setMaxWidth(0);
        vista.tablaTratamientos.getColumnModel().getColumn(0).setWidth(0);
    }
    
    public void cargarTablaCuidados(String idPaciente) {
        String[] columnas = {"Fecha", "Hora", "Enfermero/a a cargo", "Tipo de Cuidado", "Observaciones y Notas"};
        
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        java.util.List<Object[]> cuidados = pacDao.obtenerHistorialCuidados(idPaciente); // Ajusta pacDao si se llama distinto
        for (Object[] fila : cuidados) {
            modeloTabla.addRow(fila);
        }
        
        vista.tablaCuidados.setModel(modeloTabla);
    }
    
    public void cargarTablaHistoriaClinica(String idPaciente) {
        String[] columnas = {"Fecha", "Hora", "Médico", "Diagnóstico", "Peso (kg)", "Temp (°C)", "FC (lpm)", "P. Arterial", "Estado"};
        
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        java.util.List<Object[]> historia = pacDao.obtenerHistoriaClinica(idPaciente); 
        for (Object[] fila : historia) {
            modeloTabla.addRow(fila);
        }
        
        vista.tablaHistoriaClinica.setModel(modeloTabla);
        
        vista.tablaHistoriaClinica.getColumnModel().getColumn(3).setPreferredWidth(250);
    }
    


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        if (e.getSource() == vista.btnContactoEmergencia) {
            String[] datos = pacDao.obtenerContactoEmergencia(idPaciente);
            
            if (datos[0] == null) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Este paciente no tiene un tutor registrado en el sistema.", "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            com.mycompany.geriatrico1.vista.panel_contacto_emergencia panelCE = new com.mycompany.geriatrico1.vista.panel_contacto_emergencia();
            
            panelCE.lblResidente.setText(datos[0]);
            panelCE.lblContactoPrincipal.setText(datos[1]);
            panelCE.txtParentesco.setText(datos[2]);
            panelCE.txtTelefonoPrincipal.setText(datos[3]);
            panelCE.txtCorreo.setText(datos[4]);
            panelCE.txtDireccion.setText(datos[5]);
            
            panelCE.txtParentesco.setEditable(false);
            panelCE.txtTelefonoPrincipal.setEditable(false);
            panelCE.txtCorreo.setEditable(false);
            panelCE.txtDireccion.setEditable(false);

            javax.swing.JDialog dialogoEmerg = new javax.swing.JDialog();
            dialogoEmerg.setTitle("Contacto de Emergencia");
            dialogoEmerg.setModal(true); // Bloquea lo de atrás
            dialogoEmerg.setContentPane(panelCE);
            dialogoEmerg.pack();
            dialogoEmerg.setLocationRelativeTo(vista);
            dialogoEmerg.setVisible(true);
        }
        
        if (e.getSource() == vista.btnFinalizarTratamiento) {
         int filaSeleccionada = vista.tablaTratamientos.getSelectedRow();

         if (filaSeleccionada == -1) {
             javax.swing.JOptionPane.showMessageDialog(null, "Por favor, seleccione un tratamiento de la tabla.");
         } else {
             String idDetalle = vista.tablaTratamientos.getValueAt(filaSeleccionada, 0).toString();
             String estadoActual = vista.tablaTratamientos.getValueAt(filaSeleccionada, 4).toString();

             if(estadoActual.equalsIgnoreCase("Completado")) {
                 javax.swing.JOptionPane.showMessageDialog(null, "Este tratamiento ya fue finalizado previamente.");
                 return;
             }

             int respuesta = javax.swing.JOptionPane.showConfirmDialog(null, "¿Confirma que el tratamiento se ha completado?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
             if(respuesta == javax.swing.JOptionPane.YES_OPTION) {
                 if(pacDao.finalizarTratamiento(idDetalle)) {
                     javax.swing.JOptionPane.showMessageDialog(null, "¡Tratamiento marcado como Terminado!");
                     cargarTablaTratamientos(this.idPaciente); 
                 }
             }
         }
     }
        
        if (e.getSource() == vista.btnGenerarReporte) {
        String contenidoReporte = pacDao.obtenerTextoReporteGeneral(this.idPaciente);
        
        javax.swing.JTextArea areaImpresion = new javax.swing.JTextArea(contenidoReporte);
        areaImpresion.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        
        try {
            java.text.MessageFormat header = new java.text.MessageFormat("KURY-CARE - Expediente Oficial");
            java.text.MessageFormat footer = new java.text.MessageFormat("Pagina {0}");
            
            boolean impuesto = areaImpresion.print(header, footer);
            
            if (impuesto) {
                javax.swing.JOptionPane.showMessageDialog(null, "¡Reporte general generado exitosamente!");
            }
            
        } catch (java.awt.print.PrinterException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al imprimir el reporte: " + ex.getMessage());
        }
    }
    }
}