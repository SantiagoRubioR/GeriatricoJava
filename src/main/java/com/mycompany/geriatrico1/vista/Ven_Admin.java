/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.geriatrico1.vista;
//import com.mycompany.geriatrico1.vista.Ven_Admin;
import com.mycompany.geriatrico1.Controlador.CtrlAdmin;
import com.mycompany.geriatrico1.conexion.Conexion;
import com.mycompany.geriatrico1.controlador.CtrlEmpleados;
import com.mycompany.geriatrico1.dao.EmpleadoDAO;
import com.mycompany.geriatrico1.dao.ReporteDAO;
import com.mycompany.geriatrico1.modelo.Reporte;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author USUSRIO_ PC
 */
public class Ven_Admin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Ven_Admin.class.getName());
    private panelGrafica panelGraficaChart;
    private boolean formularioListo = false;
    /**
     * Creates new form Ven_Admin
     */
    public Ven_Admin(String nombreUsuario) {
        initComponents();
        cargarConsultas();
        cargarPeriodos();
        cargarGraficas();
        configurarFechas();
        
        panelSolicitudAcceso.setVisible(false);
        panelCuentas.setVisible(false);
        panelInicio.setVisible(true);
        panelPacientes.setVisible(false);
       
        panelReportes.setVisible(false);
        
        
        lblUsuario.setText("Admin: " + nombreUsuario);
        lblSaludo.setText("Hola " + nombreUsuario);
        
        formularioListo = true;


    }
    public void actualizarContadoresVista(int pacientes, int personal, int alertas, int cuidados) {
        lblPacientes.setText(String.valueOf(pacientes));
        lblCuentasPer.setText(String.valueOf(personal));
        lblAlertas.setText(String.valueOf(alertas));
        lblCuidados.setText(String.valueOf(cuidados));
    }
    
    //METODOS PARA REPORTE
    private void cargarConsultas() {

    comboxConsulta.removeAllItems();

    comboxConsulta.addItem(
        "Pacientes por tipo de sangre"
    );

    comboxConsulta.addItem(
        "Pacientes por grado de dependencia"
    );

    comboxConsulta.addItem(
        "Empleados por cargo"
    );

    comboxConsulta.addItem(
        "Empleados por estado"
    );

    comboxConsulta.addItem(
        "Médicos por especialidad"
    );

    comboxConsulta.addItem(
        "Enfermeras por especialidad"
    );

    comboxConsulta.addItem(
        "Personas por género"
    );

    comboxConsulta.addItem(
        "Alertas por prioridad"
    );

    comboxConsulta.addItem(
        "Alertas por estado"
    );

    comboxConsulta.addItem(
        "Diagnósticos más frecuentes"
    );

    comboxConsulta.addItem(
        "Presión arterial"
    );

    comboxConsulta.addItem(
        "Cuidados por tipo"
    );

    comboxConsulta.addItem(
        "Cuidados por enfermera"
    );

    comboxConsulta.addItem(
        "Medicamentos por vía"
    );

    comboxConsulta.addItem(
        "Medicamentos por fabricante"
    );

    comboxConsulta.addItem(
        "Medicamentos próximos a caducar"
    );

    comboxConsulta.addItem(
        "Recetas por mes"
    );

    comboxConsulta.addItem(
        "Medicamentos más recetados"
    );

    comboxConsulta.addItem(
        "Tratamientos por estado"
    );

    comboxConsulta.addItem(
        "Tratamientos por tipo"
    );

    comboxConsulta.addItem("Tratamientos por médico");

    comboxConsulta.addItem( "Historiales clínicos por mes");

    comboxConsulta.addItem("Horarios por día");

    comboxConsulta.addItem("Usuarios creados");
}
    
    private void cargarPeriodos() {

    comboxPeriodo.removeAllItems();

    comboxPeriodo.addItem("Todos");
    comboxPeriodo.addItem("Hoy");
    comboxPeriodo.addItem("Este mes");
    comboxPeriodo.addItem("Últimos 3 meses");
    comboxPeriodo.addItem("Últimos 4 meses");
    comboxPeriodo.addItem("Últimos 6 meses");
    comboxPeriodo.addItem("Este año");
    comboxPeriodo.addItem("Personalizado");
}
    
    private void cargarGraficas() {

    comboxTipGrafica.removeAllItems();

    comboxTipGrafica.addItem(
        "Gráfica de barras"
    );

    comboxTipGrafica.addItem(
        "Gráfica circular"
    );
    
}
    private void configurarFechas() {

    txtFechaIni.setEnabled(false);
    txtFechaFin.setEnabled(false);

    txtFechaIni.setText("");
    txtFechaFin.setText("");
}
    private Date[] obtenerFechas() {

    String periodo =
            comboxPeriodo.getSelectedItem().toString();

    Date fechaIni = null;
    Date fechaFin = null;

    java.time.LocalDate hoy =
            java.time.LocalDate.now();

    switch (periodo) {

        case "Todos":

            break;

        case "Hoy":

            fechaIni =
                    Date.valueOf(hoy);

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Este mes":

            fechaIni =
                    Date.valueOf(
                        hoy.withDayOfMonth(1)
                    );

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Últimos 3 meses":

            fechaIni =
                    Date.valueOf(
                        hoy.minusMonths(3)
                    );

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Últimos 4 meses":

            fechaIni =
                    Date.valueOf(
                        hoy.minusMonths(4)
                    );

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Últimos 6 meses":

            fechaIni =
                    Date.valueOf(
                        hoy.minusMonths(6)
                    );

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Este año":

            fechaIni=
                    Date.valueOf(
                        hoy.withDayOfYear(1)
                    );

            fechaFin =
                    Date.valueOf(hoy);

            break;

        case "Personalizado":

            try {

                fechaIni =
                    Date.valueOf(
                        txtFechaIni
                            .getText()
                            .trim()
                    );

                fechaFin =
                    Date.valueOf(
                        txtFechaFin
                            .getText()
                            .trim()
                    );

            } catch (IllegalArgumentException e) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ingrese las fechas correctamente.\n"
                    + "Formato: YYYY-MM-DD"
                );

                return null;
            }

            break;
    }

    return new Date[]{
        fechaIni,
        fechaFin
    };
}

    private boolean validarFechas(
        Date fechaIni,
        Date fechaFin) {

    if (fechaIni == null ||
        fechaFin == null) {

        return true;
    }

    if (fechaIni.after(fechaFin)) {

        JOptionPane.showMessageDialog(
            this,
            "La fecha inicial no puede ser "
            + "mayor que la fecha final."
        );

        return false;
    }

    return true;
}
    
    private boolean consultaPermiteFecha(
        int tipoConsulta) {

    switch (tipoConsulta) {

        case 1:
        case 2:
        case 3:
        case 4:
        case 8:
        case 9:
        case 10:
        case 12:
        case 13:
        case 17:
        case 18:
        case 19:
        case 21:
        case 22:
        case 24:

            return true;

        default:

            return false;
    }
}
    
    
    //GRAFICAS

    private void generarGraficaBarras(List<Reporte> resultados) {

        String titulo =
                comboxConsulta.getSelectedItem().toString();

        if (panelGraficaChart == null) {

            panelGraficaChart = new panelGrafica();
            panelGraficaChart.setPreferredSize(new java.awt.Dimension(340, 300));

            panelGraficas.setLayout(new BorderLayout());
            panelGraficas.add(panelGraficaChart, BorderLayout.CENTER);
        }

        panelGraficaChart.mostrarBarras(resultados, titulo);

        panelGraficas.revalidate();
        panelGraficas.repaint();
    }

    private void generarGraficaCircular(List<Reporte> resultados) {

        String titulo =
                comboxConsulta.getSelectedItem().toString();

        if (panelGraficaChart == null) {

            panelGraficaChart = new panelGrafica();
            panelGraficaChart.setPreferredSize(new java.awt.Dimension(340, 300));

            panelGraficas.setLayout(new BorderLayout());
            panelGraficas.add(panelGraficaChart, BorderLayout.CENTER);
        }

        panelGraficaChart.mostrarCircular(resultados, titulo);

        panelGraficas.revalidate();
        panelGraficas.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pacientes = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inicio = new javax.swing.JLabel();
        cuentas = new javax.swing.JLabel();
        reportes = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        panelSolicitudAcceso = new javax.swing.JPanel();
        panelCuentas = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        txtBusPersonal = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable();
        btnNuevoPersonal = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        panelPacientes = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        txtBuscarPaciente = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaPacientes = new javax.swing.JTable();
        btnNuevoPaciente = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnfichaPaciente = new javax.swing.JButton();
        panelReportes = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboxConsulta = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        comboxTipGrafica = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        comboxPeriodo = new javax.swing.JComboBox<>();
        panelPersonalizado = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFechaIni = new javax.swing.JTextField();
        txtFechaFin = new javax.swing.JTextField();
        panelGraficas = new javax.swing.JPanel();
        panelInicio = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblPacientes = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lblCuentasPer = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        lblAlertas = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        lblCuidados = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        lblSaludo = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnRegistrarPacienteRapido = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        btnCrearCuenta = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(238, 242, 246));

        jPanel1.setBackground(new java.awt.Color(0, 128, 128));
        jPanel1.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setBackground(new java.awt.Color(24, 76, 74));
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("SALIR");
        btnSalir.addActionListener(this::btnSalirActionPerformed);
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, -1, -1));

        jLabel1.setFont(new java.awt.Font("Poor Richard", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Panel de administración");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        pacientes.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        pacientes.setForeground(new java.awt.Color(255, 255, 255));
        pacientes.setText("Pacientes");
        pacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pacientesMouseClicked(evt);
            }
        });
        jPanel1.add(pacientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 100, -1));

        jLabel4.setFont(new java.awt.Font("Poor Richard", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("GESTIÓN");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 100, -1));

        inicio.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        inicio.setForeground(new java.awt.Color(255, 255, 255));
        inicio.setText("Inicio");
        inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inicioMouseClicked(evt);
            }
        });
        jPanel1.add(inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 109, 90, -1));

        cuentas.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        cuentas.setForeground(new java.awt.Color(255, 255, 255));
        cuentas.setText("Empleados");
        cuentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cuentasMouseClicked(evt);
            }
        });
        jPanel1.add(cuentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 100, -1));

        reportes.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        reportes.setForeground(new java.awt.Color(255, 255, 255));
        reportes.setText("Reportes");
        reportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportesMouseClicked(evt);
            }
        });
        jPanel1.add(reportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 100, -1));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 386, 276, -1));
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 476, -1, -1));

        lblUsuario.setFont(new java.awt.Font("Poor Richard", 0, 20)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("Administrador/a");
        jPanel1.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 456, 190, -1));

        javax.swing.GroupLayout panelSolicitudAccesoLayout = new javax.swing.GroupLayout(panelSolicitudAcceso);
        panelSolicitudAcceso.setLayout(panelSolicitudAccesoLayout);
        panelSolicitudAccesoLayout.setHorizontalGroup(
            panelSolicitudAccesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4148, Short.MAX_VALUE)
        );
        panelSolicitudAccesoLayout.setVerticalGroup(
            panelSolicitudAccesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2042, Short.MAX_VALUE)
        );

        panelCuentas.setBackground(new java.awt.Color(245, 247, 250));

        jPanel12.setBackground(new java.awt.Color(245, 247, 250));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Cambria", 1, 30)); // NOI18N
        jLabel6.setText("Cuentas del personal");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, -1, -1));

        jLabel23.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Enfermeros/as y médicos con acceso al sistema ");
        jPanel12.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jPanel11.setBackground(new java.awt.Color(245, 247, 250));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBusPersonal.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtBusPersonal.addActionListener(this::txtBusPersonalActionPerformed);
        txtBusPersonal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusPersonalKeyReleased(evt);
            }
        });
        jPanel11.add(txtBusPersonal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 196, -1));

        jLabel24.setText("Buscar: ");
        jPanel11.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Cedula", "Nombre", "Apellido", "Rol"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaEmpleados);

        jScrollPane5.setViewportView(jScrollPane4);

        btnNuevoPersonal.setBackground(new java.awt.Color(0, 102, 102));
        btnNuevoPersonal.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14)); // NOI18N
        btnNuevoPersonal.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevoPersonal.setText("+ Nueva cuenta");
        btnNuevoPersonal.addActionListener(this::btnNuevoPersonalActionPerformed);

        jButton1.setText("Editar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton3.setForeground(new java.awt.Color(255, 51, 51));
        jButton3.setText("Dar de Baja");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout panelCuentasLayout = new javax.swing.GroupLayout(panelCuentas);
        panelCuentas.setLayout(panelCuentasLayout);
        panelCuentasLayout.setHorizontalGroup(
            panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuentasLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCuentasLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(385, 385, 385)
                        .addComponent(btnNuevoPersonal))
                    .addGroup(panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelCuentasLayout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(146, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCuentasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(159, 159, 159))
        );
        panelCuentasLayout.setVerticalGroup(
            panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNuevoPersonal)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        panelPacientes.setBackground(new java.awt.Color(245, 247, 250));

        jPanel13.setBackground(new java.awt.Color(245, 247, 250));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 30)); // NOI18N
        jLabel7.setText("Pacientes ");
        jPanel13.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, -1));

        jLabel25.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Pacientes registrados en el sistema");
        jPanel13.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 290, -1));

        jPanel16.setBackground(new java.awt.Color(245, 247, 250));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBuscarPaciente.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtBuscarPaciente.addActionListener(this::txtBuscarPacienteActionPerformed);
        txtBuscarPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPacienteKeyReleased(evt);
            }
        });
        jPanel16.add(txtBuscarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 194, -1));

        tablaPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cedula", "Nombre", "Apellido", "Grado Dep.", "Tipo Sangre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tablaPacientes);

        jScrollPane7.setViewportView(jScrollPane6);

        btnNuevoPaciente.setBackground(new java.awt.Color(0, 102, 102));
        btnNuevoPaciente.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14)); // NOI18N
        btnNuevoPaciente.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevoPaciente.setText("+ Nuevo Paciente");
        btnNuevoPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoPacienteMouseClicked(evt);
            }
        });
        btnNuevoPaciente.addActionListener(this::btnNuevoPacienteActionPerformed);

        btnEditar.setText("Editar");
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        btnEditar.addActionListener(this::btnEditarActionPerformed);

        jButton2.setForeground(new java.awt.Color(255, 51, 51));
        jButton2.setText("Dar de Baja");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        btnfichaPaciente.setText("Ver Ficha");

        javax.swing.GroupLayout panelPacientesLayout = new javax.swing.GroupLayout(panelPacientes);
        panelPacientes.setLayout(panelPacientesLayout);
        panelPacientesLayout.setHorizontalGroup(
            panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPacientesLayout.createSequentialGroup()
                .addGroup(panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelPacientesLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnfichaPaciente)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(30, 30, 30)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPacientesLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPacientesLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelPacientesLayout.createSequentialGroup()
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNuevoPaciente)))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        panelPacientesLayout.setVerticalGroup(
            panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPacientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNuevoPaciente)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditar)
                    .addComponent(jButton2)
                    .addComponent(btnfichaPaciente))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        panelReportes.setBackground(new java.awt.Color(245, 247, 250));
        panelReportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(245, 247, 250));

        jLabel29.setFont(new java.awt.Font("Cambria", 1, 30)); // NOI18N
        jLabel29.setText("Reportes");

        jLabel32.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(102, 102, 102));
        jLabel32.setText("Resumen general de la actividad del sistema");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel29))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jLabel32))
        );

        panelReportes.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 54, -1, -1));

        jLabel2.setText("Consulta: ");
        panelReportes.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 201, -1, -1));

        comboxConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboxConsulta.addActionListener(this::comboxConsultaActionPerformed);
        panelReportes.add(comboxConsulta, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 198, -1, -1));

        jLabel3.setText("Tipo de gráfica: ");
        panelReportes.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 425, -1, -1));

        comboxTipGrafica.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelReportes.add(comboxTipGrafica, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 422, -1, -1));

        jLabel10.setText("Periodo");
        panelReportes.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 255, -1, -1));

        comboxPeriodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboxPeriodo.addActionListener(this::comboxPeriodoActionPerformed);
        panelReportes.add(comboxPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 252, -1, -1));

        panelPersonalizado.setBackground(new java.awt.Color(245, 247, 250));

        jLabel11.setText("Fecha incial: ");

        jLabel16.setText("Fecha final: ");

        javax.swing.GroupLayout panelPersonalizadoLayout = new javax.swing.GroupLayout(panelPersonalizado);
        panelPersonalizado.setLayout(panelPersonalizadoLayout);
        panelPersonalizadoLayout.setHorizontalGroup(
            panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalizadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel16))
                .addGap(48, 48, 48)
                .addGroup(panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFechaIni)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        panelPersonalizadoLayout.setVerticalGroup(
            panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalizadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtFechaIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelPersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelReportes.add(panelPersonalizado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 302, -1, -1));

        panelGraficas.setBackground(new java.awt.Color(245, 247, 250));

        javax.swing.GroupLayout panelGraficasLayout = new javax.swing.GroupLayout(panelGraficas);
        panelGraficas.setLayout(panelGraficasLayout);
        panelGraficasLayout.setHorizontalGroup(
            panelGraficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        panelGraficasLayout.setVerticalGroup(
            panelGraficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );

        panelReportes.add(panelGraficas, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 390, 370));

        panelInicio.setBackground(new java.awt.Color(245, 247, 250));
        panelInicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 153)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Pacientes activos");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        lblPacientes.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 22)); // NOI18N
        lblPacientes.setText("0");
        jPanel3.add(lblPacientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 30, 30));

        panelInicio.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 140, 90));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 204)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText(" Cuentas del Personal");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 70, 150, -1));

        lblCuentasPer.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 22)); // NOI18N
        lblCuentasPer.setText("0");
        jPanel2.add(lblCuentasPer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        panelInicio.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 160, 90));

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 51, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Alertas pendientes");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        lblAlertas.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 22)); // NOI18N
        lblAlertas.setText("0");
        jPanel4.add(lblAlertas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        panelInicio.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 170, 90));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));
        jPanel5.setForeground(new java.awt.Color(102, 102, 102));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Cuidados Registrados");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        lblCuidados.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 22)); // NOI18N
        lblCuidados.setText("0");
        jPanel5.add(lblCuidados, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        panelInicio.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 130, 170, 90));

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        jLabel26.setText("Acciones rápidas");
        panelInicio.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        jPanel8.setBackground(new java.awt.Color(245, 247, 250));

        lblFecha.setBackground(new java.awt.Color(245, 247, 250));
        lblFecha.setFont(new java.awt.Font("Tempus Sans ITC", 0, 18)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(102, 102, 102));
        lblFecha.setText("Jueves, 3 de septiembre de 2026");

        lblSaludo.setBackground(new java.awt.Color(245, 247, 250));
        lblSaludo.setFont(new java.awt.Font("Cambria", 1, 30)); // NOI18N
        lblSaludo.setText("Hola, ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblSaludo, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 103, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(lblSaludo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(lblFecha)
                .addContainerGap())
        );

        panelInicio.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 590, 80));

        jLabel27.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText(" Registra enfermeros /as y médicos");
        jLabel27.setMaximumSize(new java.awt.Dimension(210, 24));
        jLabel27.setMinimumSize(new java.awt.Dimension(210, 24));
        jLabel27.setName(""); // NOI18N
        jLabel27.setPreferredSize(new java.awt.Dimension(210, 24));
        panelInicio.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, 240, 20));

        jLabel28.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("Registra un nuevo paciente");
        panelInicio.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 460, -1, -1));

        btnRegistrarPacienteRapido.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 16)); // NOI18N
        btnRegistrarPacienteRapido.setText("Registrar Paciente");
        btnRegistrarPacienteRapido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        panelInicio.add(btnRegistrarPacienteRapido, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 340, 240, 100));

        jLabel35.setFont(new java.awt.Font("Yu Gothic", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setText("y nuevos pacientes.");
        panelInicio.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 480, 140, -1));

        btnCrearCuenta.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 16)); // NOI18N
        btnCrearCuenta.setText("Crear cuenta");
        btnCrearCuenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        btnCrearCuenta.addActionListener(this::btnCrearCuentaActionPerformed);
        panelInicio.add(btnCrearCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 240, 100));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelSolicitudAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(647, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 1884, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(136, 136, 136)
                                .addComponent(panelSolicitudAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelCuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 622, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportesMouseClicked
        // TODO add your handling code here:
        panelReportes.setVisible(true);
       
        panelSolicitudAcceso.setVisible(false);
        panelInicio.setVisible(false);
        panelCuentas.setVisible(false);
        panelPacientes.setVisible(false);
        
        seleccionarLabel(reportes);
    }//GEN-LAST:event_reportesMouseClicked

    private void inicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inicioMouseClicked
        // TODO add your handling code here:
        
        panelInicio.setVisible(true);
        panelCuentas.setVisible(false);
        panelSolicitudAcceso.setVisible(false);
        panelPacientes.setVisible(false);
        
        panelReportes.setVisible(false);
        
        seleccionarLabel(inicio);
    }//GEN-LAST:event_inicioMouseClicked

    private void cuentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cuentasMouseClicked
        // TODO add your handling code here:
        panelCuentas.setVisible(true);
        panelInicio.setVisible(false);
        panelSolicitudAcceso.setVisible(false);
        panelPacientes.setVisible(false);
       
        panelReportes.setVisible(false);
        CtrlAdmin controlador = new CtrlAdmin(this);
        controlador.cargarTablaEmpleados();
        
        seleccionarLabel(cuentas);
        
    }//GEN-LAST:event_cuentasMouseClicked

    private void pacientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pacientesMouseClicked
        // TODO add your handling code here:
        panelPacientes.setVisible(true);
        panelCuentas.setVisible(false);
        panelInicio.setVisible(false);
        panelSolicitudAcceso.setVisible(false);
        
        panelReportes.setVisible(false);
        
        CtrlAdmin controladorAdmin = new CtrlAdmin(this);
        controladorAdmin.cargarTablaPacientes();
        
        seleccionarLabel(pacientes);
        
    }//GEN-LAST:event_pacientesMouseClicked

    public void seleccionarLabel(javax.swing.JLabel labelSeleccionado) {

    javax.swing.JLabel[] labels = {inicio, cuentas, pacientes, reportes};

    for (javax.swing.JLabel label : labels) {

        if (label == labelSeleccionado) {
            label.setOpaque(true);
            label.setBackground(new Color(51, 153, 153));
        } else {
            label.setOpaque(true);
            label.setBackground(null);
        }
    }
}
    
    
    
    private void txtBusPersonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusPersonalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusPersonalActionPerformed

    private void txtBuscarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarPacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPacienteActionPerformed

    private void btnNuevoPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoPacienteActionPerformed
        
    com.mycompany.geriatrico1.vista.FichaNewPaciente ventanaRegistro = new com.mycompany.geriatrico1.vista.FichaNewPaciente();

    // 2. Llamas a la base de datos (El Modelo)
    com.mycompany.geriatrico1.dao.PacienteDao daoPac = new com.mycompany.geriatrico1.dao.PacienteDao();

    // 3. ¡EL CEREBRO! Conectas la ventana y la BD a través del Controlador
    com.mycompany.geriatrico1.controlador.CtrlPaciente controlador = new com.mycompany.geriatrico1.controlador.CtrlPaciente(ventanaRegistro, daoPac);

    // 4. Muestras la ventana en pantalla
    ventanaRegistro.setVisible(true);
    }//GEN-LAST:event_btnNuevoPacienteActionPerformed

    private void btnNuevoPersonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoPersonalActionPerformed
        com.mycompany.geriatrico1.vista.FichaNuevaCuenta dlg = new com.mycompany.geriatrico1.vista.FichaNuevaCuenta();
        dlg.btnGuardarFicha.setText("Guardar");
        
        com.mycompany.geriatrico1.dao.EmpleadoDAO dao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        CtrlEmpleados ctrl = new CtrlEmpleados(dlg, dao);        // TODO add your handling code here:
        
        
        dlg.setLocationRelativeTo(this);
         dlg.setVisible(true);
    
    
        //cargarTablaEmpleados();
    }//GEN-LAST:event_btnNuevoPersonalActionPerformed

    private void btnCrearCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearCuentaActionPerformed
            // TODO add your handling code here:
        FichaNuevaCuenta ventanaPersonal = new FichaNuevaCuenta();
        EmpleadoDAO daoEmp = new EmpleadoDAO();
        CtrlEmpleados ctrl = new CtrlEmpleados(ventanaPersonal, daoEmp);
        ventanaPersonal.setLocationRelativeTo(null);
        ventanaPersonal.setVisible(true);
        
    }//GEN-LAST:event_btnCrearCuentaActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();

        Dash_login lg = new Dash_login();
        lg.setVisible(true);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        CtrlAdmin controlador = new CtrlAdmin(this);
        controlador.abrirEdicionPaciente();
    }//GEN-LAST:event_btnEditarMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
    CtrlAdmin controlador = new CtrlAdmin(this);
    controlador.darDeBajaPaciente();
    }//GEN-LAST:event_jButton2MouseClicked

    private void txtBuscarPacienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPacienteKeyReleased
      CtrlAdmin controlador = new CtrlAdmin(this);
      controlador.filtrarTablaPacientes(txtBuscarPaciente.getText()); // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPacienteKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnNuevoPacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoPacienteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoPacienteMouseClicked

    private void txtBusPersonalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusPersonalKeyReleased
    new CtrlAdmin(this).filtrarTablaEmpleados(txtBusPersonal.getText()); 
    }//GEN-LAST:event_txtBusPersonalKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new CtrlAdmin(this).abrirEdicionEmpleado();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        new CtrlAdmin(this).darDeBajaEmpleado();
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        new CtrlAdmin(this).darDeBajaEmpleado();
    }//GEN-LAST:event_jButton1MouseClicked

    private void comboxConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboxConsultaActionPerformed
        // TODO add your handling code here:
        if (!formularioListo) {
            return;
        }

        int tipoConsulta =
        comboxConsulta.getSelectedIndex() + 1;

        boolean permiteFecha =
        consultaPermiteFecha(
            tipoConsulta
        );

        comboxPeriodo.setEnabled(
            permiteFecha
        );

        txtFechaIni.setEnabled(false);
        txtFechaFin.setEnabled(false);

        generarReporteYGrafica();
    }//GEN-LAST:event_comboxConsultaActionPerformed

    private void comboxPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboxPeriodoActionPerformed
        // TODO add your handling code here:
        
        if (comboxPeriodo.getSelectedItem() == null) {
            return;
        }

        String periodo = comboxPeriodo.getSelectedItem().toString();

        if (periodo.equals("Personalizado")) {
            panelPersonalizado.setVisible(true);

            txtFechaIni.setEnabled(true);
            txtFechaFin.setEnabled(true);

        } else {

            txtFechaIni.setEnabled(false);
            txtFechaFin.setEnabled(false);

            txtFechaIni.setText("");
            txtFechaFin.setText("");
        }

        if (!formularioListo) {
            return;
        }

        generarReporteYGrafica();
    }//GEN-LAST:event_comboxPeriodoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditarActionPerformed
    private void generarReporteYGrafica() {

        // 1. OBTENER CONSULTA
        int tipoConsulta =
               comboxConsulta.getSelectedIndex() + 1;

        // 2. OBTENER FECHAS
        Date[] fechas =
                obtenerFechas();

        if (fechas == null) {
            return;
        }

        Date fechaIni = fechas[0];
        Date fechaFin = fechas[1];

        // 3. VALIDAR FECHAS
        if (!validarFechas(
                fechaIni,
                fechaFin)) {

            return;
        }

        // 4. OBTENER TIPO DE GRÁFICA
        int tipoGrafica =
                comboxTipGrafica.getSelectedIndex();

        // 5. CONECTAR A POSTGRESQL Y EJECUTAR
        try {

            Connection cn =
                    Conexion.getConnection();

            ReporteDAO dao =
                    new ReporteDAO(cn);

            List<Reporte> resultados =
                    dao.ejecutarReporte(
                        tipoConsulta,
                        fechaIni,
                        fechaFin
                    );

            // 6. COMPROBAR RESULTADOS
            if (resultados.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "No existen datos para "
                    + "el período seleccionado."
                );

                return;
            }

            // 7. MOSTRAR GRÁFICA
            if (tipoGrafica == 0) {

                generarGraficaBarras(
                    resultados
                );

            } else {

                generarGraficaCircular(
                    resultados
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error al generar reporte:\n"
                + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void comboxTipGraficaActionPerformed(java.awt.event.ActionEvent evt) {

        if (comboxTipGrafica.getSelectedItem() == null) {
            return;
        }

        if (!formularioListo) {
            return;
        }

        generarReporteYGrafica();
    }
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrearCuenta;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnNuevoPaciente;
    private javax.swing.JButton btnNuevoPersonal;
    public javax.swing.JButton btnRegistrarPacienteRapido;
    private javax.swing.JButton btnSalir;
    public javax.swing.JButton btnfichaPaciente;
    private javax.swing.JComboBox<String> comboxConsulta;
    private javax.swing.JComboBox<String> comboxPeriodo;
    private javax.swing.JComboBox<String> comboxTipGrafica;
    private javax.swing.JLabel cuentas;
    private javax.swing.JLabel inicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JLabel lblAlertas;
    public javax.swing.JLabel lblCuentasPer;
    public javax.swing.JLabel lblCuidados;
    public javax.swing.JLabel lblFecha;
    public javax.swing.JLabel lblPacientes;
    public javax.swing.JLabel lblSaludo;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel pacientes;
    private javax.swing.JPanel panelCuentas;
    private javax.swing.JPanel panelGraficas;
    private javax.swing.JPanel panelInicio;
    private javax.swing.JPanel panelPacientes;
    private javax.swing.JPanel panelPersonalizado;
    private javax.swing.JPanel panelReportes;
    private javax.swing.JPanel panelSolicitudAcceso;
    private javax.swing.JLabel reportes;
    public javax.swing.JTable tablaEmpleados;
    public javax.swing.JTable tablaPacientes;
    public javax.swing.JTextField txtBusPersonal;
    private javax.swing.JTextField txtBuscarPaciente;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaIni;
    // End of variables declaration//GEN-END:variables
}
