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
        
        // Ejecutamos la carga del perfil apenas nace el controlador
        cargarDatosPerfil();
        cargarExpedienteCompleto(this.idPaciente);
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
        cargarExpedienteCompleto(this.idPaciente);
        cargarTablaTratamientos(this.idPaciente);
        cargarTablaCuidados(this.idPaciente);
        cargarTablaHistoriaClinica(this.idPaciente);
    }
    
    public void cargarExpedienteCompleto(String idPaciente) {
        // 1. Instanciar el DAO
        PacienteDao modelo = new PacienteDao(); // Ajusta al nombre real de tu DAO
        
        // 2. Cargar textos en la Vista
        Object[] datosGenerales = modelo.obtenerDatosGeneralesPaciente(idPaciente);
        if (datosGenerales[0] != null) {
            vista.lblCedula.setText("Cédula: " + datosGenerales[0].toString());
            vista.lblEstadoCivil.setText("Estado Civil: " + datosGenerales[1].toString());
            vista.lblTipoSangre.setText("Tipo de Sangre: " + datosGenerales[2].toString());
            vista.lblDependencia.setText("Grado de Dependencia: " + datosGenerales[3].toString());
        }

        // 3. Generar el Gráfico de Evolución
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
            
            // Construir el gráfico
            JFreeChart chart = ChartFactory.createLineChart(
                "Evolución de Signos Vitales", // Título
                "Fechas de Consulta",          // Eje X
                "Valores",                     // Eje Y
                dataset,                       // Datos
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = 
                (org.jfree.chart.renderer.category.LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setDefaultShapesVisible(true);
            // Empaquetar e inyectar en la vista
            ChartPanel chartPanel = new ChartPanel(chart);
            vista.panelGraficoEvolucion.removeAll();
            vista.panelGraficoEvolucion.add(chartPanel, BorderLayout.CENTER);
            vista.panelGraficoEvolucion.validate();
        } else {
            // Si el paciente es nuevo y no tiene historial, limpiamos el panel
            vista.panelGraficoEvolucion.removeAll();
            vista.panelGraficoEvolucion.repaint();
        }
    }
    
    public void cargarTablaTratamientos(String idPaciente) {
        // Definimos las columnas
        String[] columnas = {"ID Oculto", "Tipo de Tratamiento", "Inicio", "Fin", "Estado", "Indicaciones Médicas"};
        DefaultTableModel modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen la tabla dando doble clic
            }
        };
        
        // Pedimos los datos al DAO y llenamos el modelo
        List<Object[]> tratamientos = pacDao.obtenerTratamientos(idPaciente); // Ajusta 'pacDao' al nombre de tu variable
        for (Object[] fila : tratamientos) {
            modeloTabla.addRow(fila);
        }
        
        // Se lo pegamos a la vista
        vista.tablaTratamientos.setModel(modeloTabla);
        
        // Ocultamos la columna 0 (el ID de la base de datos) para que no se vea feo, pero podamos usarlo
        vista.tablaTratamientos.getColumnModel().getColumn(0).setMinWidth(0);
        vista.tablaTratamientos.getColumnModel().getColumn(0).setMaxWidth(0);
        vista.tablaTratamientos.getColumnModel().getColumn(0).setWidth(0);
    }
    
    public void cargarTablaCuidados(String idPaciente) {
        // Definimos las columnas de la bitácora
        String[] columnas = {"Fecha", "Hora", "Enfermero/a a cargo", "Tipo de Cuidado", "Observaciones y Notas"};
        
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueamos la edición manual
            }
        };
        
        // Llamamos al DAO y vaciamos los datos
        java.util.List<Object[]> cuidados = pacDao.obtenerHistorialCuidados(idPaciente); // Ajusta pacDao si se llama distinto
        for (Object[] fila : cuidados) {
            modeloTabla.addRow(fila);
        }
        
        // Se lo pegamos a la vista
        vista.tablaCuidados.setModel(modeloTabla);
    }
    
    public void cargarTablaHistoriaClinica(String idPaciente) {
        // Son muchos datos, así que definimos las columnas precisas
        String[] columnas = {"Fecha", "Hora", "Médico", "Diagnóstico", "Peso (kg)", "Temp (°C)", "FC (lpm)", "P. Arterial", "Estado"};
        
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueamos la edición
            }
        };
        
        java.util.List<Object[]> historia = pacDao.obtenerHistoriaClinica(idPaciente); 
        for (Object[] fila : historia) {
            modeloTabla.addRow(fila);
        }
        
        vista.tablaHistoriaClinica.setModel(modeloTabla);
        
        // Pequeño truco para que la columna "Diagnóstico" sea más ancha que las demás
        vista.tablaHistoriaClinica.getColumnModel().getColumn(3).setPreferredWidth(250);
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
        
        if (e.getSource() == vista.btnFinalizarTratamiento) {
         int filaSeleccionada = vista.tablaTratamientos.getSelectedRow();

         if (filaSeleccionada == -1) {
             javax.swing.JOptionPane.showMessageDialog(null, "Por favor, seleccione un tratamiento de la tabla.");
         } else {
             // Sacamos el ID oculto de la columna 0
             String idDetalle = vista.tablaTratamientos.getValueAt(filaSeleccionada, 0).toString();
             String estadoActual = vista.tablaTratamientos.getValueAt(filaSeleccionada, 4).toString();

             if(estadoActual.equalsIgnoreCase("Completado")) {
                 javax.swing.JOptionPane.showMessageDialog(null, "Este tratamiento ya fue finalizado previamente.");
                 return;
             }

             // Confirmación y ejecución
             int respuesta = javax.swing.JOptionPane.showConfirmDialog(null, "¿Confirma que el tratamiento se ha completado?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
             if(respuesta == javax.swing.JOptionPane.YES_OPTION) {
                 if(pacDao.finalizarTratamiento(idDetalle)) {
                     javax.swing.JOptionPane.showMessageDialog(null, "¡Tratamiento marcado como Terminado!");
                     cargarTablaTratamientos(this.idPaciente); // Recargamos la tabla para ver el cambio
                 }
             }
         }
     }
    }
}