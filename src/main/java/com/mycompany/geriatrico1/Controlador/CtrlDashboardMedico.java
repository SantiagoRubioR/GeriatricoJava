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
    private java.util.List<Object[]> antecedentesMemoria;

    public CtrlDashboardMedico(Dashboard_Medico vista) {
        this.vista = vista;
        this.alertaDao = new AlertaDAO();

        // 1. Ponemos a escuchar a los 3 botones de María
        this.vista.btnAtender1.addActionListener(this);
        this.vista.btnAtender2.addActionListener(this);
        this.vista.btnAtender3.addActionListener(this);
        this.vista.btnCargarAlertas.addActionListener(this);
        this.vista.NuevaConsulta.addActionListener(this);
        this.vista.NuevaConsulta.addActionListener(this);
        this.vista.btnGuardarHistorial.addActionListener(this);
        inicializarComboBoxesTratamiento();
        // 2. Cargamos las alertas al iniciar
        cargarPanelAlertas();
        cargarTablaPacientesActivos();
        this.vista.btnGenerarTratamiento.addActionListener(this);
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
        
        cargarTablaAntecedentes();

        vista.txtDiagnosticoAntecedentes.setText("");
        vista.txtObservacionesAntecedentes.setText("");
        // Poner "oreja" a la tabla de Antecedentes
        this.vista.tablaAntecedentes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarDetalleAntecedente();
            }
        });
        vista.txtPaciente.setText(nombrePacienteSeleccionado); 
        vista.txtPaciente.setEditable(false);
    }
    
    private void cargarTablaAntecedentes() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaAntecedentes.getModel(); // Ajusta el nombre de tu tabla
        modelo.setRowCount(0);
        
        com.mycompany.geriatrico1.dao.HistorialDAO histDao = new com.mycompany.geriatrico1.dao.HistorialDAO();
        antecedentesMemoria = histDao.obtenerAntecedentesPaciente(idPacienteSeleccionado);
        
        for (Object[] ant : antecedentesMemoria) {
            // Solo metemos a la tabla lo que cabe visualmente (índices 0 al 6)
            modelo.addRow(new Object[]{ant[0], ant[1], ant[2], ant[3], ant[4], ant[5], ant[6]}); 
        }
    }
    
    private void mostrarDetalleAntecedente() {
        int filaVisual = vista.tablaAntecedentes.getSelectedRow();
        if (filaVisual == -1) return;

        int filaModelo = vista.tablaAntecedentes.convertRowIndexToModel(filaVisual);
        
        // Recuperamos los datos de nuestra lista en memoria (donde guardamos todo)
        if (antecedentesMemoria != null && filaModelo < antecedentesMemoria.size()) {
            Object[] datosFila = antecedentesMemoria.get(filaModelo);
            
            String diagnostico = datosFila[7] != null ? datosFila[7].toString() : "";
            String observaciones = datosFila[8] != null ? datosFila[8].toString() : "";
            
           
            vista.txtDiagnosticoAntecedentes.setText(diagnostico); 
            vista.txtObservacionesAntecedentes.setText(observaciones); 
        }
    }
    
    private void inicializarComboBoxesTratamiento() {
        com.mycompany.geriatrico1.dao.TratamientoDAO traDao = new com.mycompany.geriatrico1.dao.TratamientoDAO();
        
        vista.cmbNombreTratamiento.removeAllItems(); // Ajusta el nombre de tu JComboBox
        for (String tipo : traDao.listarTiposTratamiento()) {
            vista.cmbNombreTratamiento.addItem(tipo);
        }
        
        vista.cmbNombreMedicamento.removeAllItems(); // Ajusta el nombre de tu JComboBox
        for (String med : traDao.listarMedicamentos()) {
            vista.cmbNombreMedicamento.addItem(med);
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
        if (e.getSource() == vista.btnCargarAlertas) {
            cargarPanelAlertas(); 
        }
        
        if (e.getSource() == vista.NuevaConsulta) {
            
            // 1. Validamos que haya dado clic en la tabla primero
            if (idPacienteSeleccionado.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Primero seleccione un paciente de la lista lateral.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return; 
            }

            
            vista.txtBuscar.setText(nombrePacienteSeleccionado); 
            vista.txtBuscar.setEditable(false);

            // 3. Ejecutamos la navegación 
            vista.seleccionarBoton(vista.NuevaConsulta);
            vista.panel.setVisible(true); // El panel verde
            vista.PanelAlertas.setVisible(false);
            vista.PanelHistorial.setVisible(false);
            vista.PanelGenerar.setVisible(false);
        }
        
        if (e.getSource() == vista.btnGuardarHistorial) { // Ajusta el nombre de tu botón
            
            // 1. Verificamos que el ID oculto esté cargado
            if (idPacienteSeleccionado.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la lista lateral primero.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // 2. Extraemos los datos de la interfaz
                double peso = Double.parseDouble(vista.txtPeso.getText().trim());
                double temp = Double.parseDouble(vista.txtTemp.getText().trim());
                int frec = Integer.parseInt(vista.txtFrec.getText().trim());
                
                // Presión separada (Asegúrate de haber puesto dos TextFields)
                double sis = Double.parseDouble(vista.txtPresionSis.getText().trim());
                double dias = Double.parseDouble(vista.txtPresionDias.getText().trim());
                
                String diagnostico = vista.txtDiagnostico.getText().trim();
                String motivo = vista.txtMotivo.getText().trim();
                String obs = vista.txtObservaciones.getText().trim();

                if (diagnostico.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "El diagnóstico es obligatorio.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Unimos el motivo a las observaciones para que la base de datos lo acepte
                String observacionesFinales = "Motivo: " + motivo + " | " + obs;
                
                // El ID del Médico que está usando el sistema (Salvavidas MVP)
                String idMedicoActual = "MED-0001"; 

                // 3. Enviamos todo a la base de datos (Usando el HistorialDAO que te di antes)
                com.mycompany.geriatrico1.dao.HistorialDAO histDao = new com.mycompany.geriatrico1.dao.HistorialDAO();
                
                if (histDao.registrarConsulta(idPacienteSeleccionado, idMedicoActual, peso, sis, dias, temp, frec, diagnostico, observacionesFinales)) {
                    
                    javax.swing.JOptionPane.showMessageDialog(vista, "Consulta guardada exitosamente en el expediente.");
                    
                    // 4. Limpiamos las cajas de texto
                    vista.txtPeso.setText("");
                    vista.txtTemp.setText("");
                    vista.txtFrec.setText("");
                    vista.txtPresionSis.setText("");
                    vista.txtPresionDias.setText("");
                    vista.txtDiagnostico.setText("");
                    vista.txtMotivo.setText("");
                    vista.txtObservaciones.setText("");
                    
                    // 5. ¡VOLVEMOS A CARGAR LA TABLA DE ANTECEDENTES PARA QUE APAREZCA EL NUEVO REGISTRO!
                    cargarTablaAntecedentes(); 
                    
                    // 6. Lógica automática de alertas de emergencia
                    if (sis > 140 || dias > 90 || temp > 38.5 || frec > 100) {
                        com.mycompany.geriatrico1.dao.AlertaDAO alertaDao = new com.mycompany.geriatrico1.dao.AlertaDAO();
                        String detalleAlerta = "SISTEMA AUTOMÁTICO: Signos vitales alterados registrados en consulta. T:" + temp + " FC:" + frec;
                        alertaDao.registrarAlerta(idPacienteSeleccionado, "PRI-0001", idMedicoActual, detalleAlerta);
                        
                        javax.swing.JOptionPane.showMessageDialog(vista, "¡ALERTA AUTOMÁTICA EMITIDA AL SISTEMA!\nSignos vitales del paciente fuera de rango normal.", "Alerta Crítica", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                    
                } else {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Error al guardar la consulta.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error: Los campos de Peso, Presión, Temperatura y Frecuencia deben ser únicamente numéricos.", "Error de Formato", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (e.getSource() == vista.btnGenerarTratamiento) { // Ajusta tu botón
            
            if (idPacienteSeleccionado.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la lista lateral primero.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validamos que hayan elegido opciones válidas en los combos
            if (vista.cmbNombreTratamiento.getSelectedIndex() <= 0 || vista.cmbNombreMedicamento.getSelectedIndex() <= 0) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Debe seleccionar un Tratamiento y un Medicamento.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Truco: Extraer solo el ID de los Combos (Ej: "TTR-0001 - Tratamiento..." -> "TTR-0001")
                String comboTratamiento = vista.cmbNombreTratamiento.getSelectedItem().toString();
                String idTipoTratamiento = comboTratamiento.split(" - ")[0]; 
                
                String comboMedicamento = vista.cmbNombreMedicamento.getSelectedItem().toString();
                String idMedicamento = comboMedicamento.split(" - ")[0];

                // Extraer fechas de los JDateChooser (Ajusta los nombres jdFechaInicio / jdFechaFin)
                if (vista.dateInicioTrata.getDate() == null || vista.dateFinTrata.getDate() == null) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Las fechas de inicio y fin son obligatorias.");
                    return;
                }
                java.sql.Date fechaIni = new java.sql.Date(vista.dateInicioTrata.getDate().getTime());
                java.sql.Date fechaFin = new java.sql.Date(vista.dateFinTrata.getDate().getTime());

                // Datos de la receta
                int cantidad = Integer.parseInt(vista.txtCantidadMed.getText().trim());
                String dosis = vista.txtDosisMed.getText().trim();
                String frecuencia = vista.cmbFrecuencia.getSelectedItem().toString();
                String duracionReceta = vista.cmbDuracion.getSelectedItem().toString();
                
                // Unimos las observaciones para no perder nada
                String obsTratamiento = vista.txtObservacionesTratamiento.getText().trim();
                String obsReceta = vista.txtObservacionesReceta.getText().trim();
                String observacionesFinales = "Tratamiento: " + obsTratamiento + " | Receta: " + obsReceta;

                String idMedicoActual = "MED-0001"; // El doc logueado

                com.mycompany.geriatrico1.dao.TratamientoDAO traDao = new com.mycompany.geriatrico1.dao.TratamientoDAO();
                
                if (traDao.registrarTratamientoCompleto(idPacienteSeleccionado, idMedicoActual, idTipoTratamiento, fechaIni, fechaFin, observacionesFinales, idMedicamento, cantidad, dosis, frecuencia, duracionReceta)) {
                    
                    javax.swing.JOptionPane.showMessageDialog(vista, "¡Tratamiento y Receta generados exitosamente!");
                    
                    // Limpieza visual
                    vista.cmbNombreTratamiento.setSelectedIndex(0);
                    vista.cmbNombreMedicamento.setSelectedIndex(0);
                    vista.dateInicioTrata.setDate(null);
                    vista.dateFinTrata.setDate(null);
                    vista.txtCantidadMed.setText("");
                    vista.txtDosisMed.setText("");
                    vista.txtObservacionesTratamiento.setText("");
                    vista.txtObservacionesReceta.setText("");
                    vista.cmbFrecuencia.setSelectedIndex(0);
                    vista.cmbDuracion.setSelectedIndex(0);
                    
                } else {
                    javax.swing.JOptionPane.showMessageDialog(vista, "Error al generar el tratamiento. Revise los datos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error: La cantidad de medicamentos debe ser un número entero.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
        
    }