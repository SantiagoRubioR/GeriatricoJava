/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.Controlador;

import com.mycompany.geriatrico1.dao.EmpleadoDAO;
import com.mycompany.geriatrico1.dao.PacienteDao;
import com.mycompany.geriatrico1.vista.Ven_Admin;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Santiago
 */
public class CtrlAdmin {
     private Ven_Admin vista;

    public CtrlAdmin(Ven_Admin vista) {
        this.vista = vista;
        configurarFechaActual();
        
        cargarEstadisticasDashboard();
        
        this.vista.btnRegistrarPacienteRapido.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                com.mycompany.geriatrico1.vista.FichaNewPaciente dlg = new com.mycompany.geriatrico1.vista.FichaNewPaciente();
                com.mycompany.geriatrico1.dao.PacienteDao dao = new com.mycompany.geriatrico1.dao.PacienteDao();
                
                
                com.mycompany.geriatrico1.controlador.CtrlPaciente ctrl = new com.mycompany.geriatrico1.controlador.CtrlPaciente(dlg, dao);
                
                
                dlg.setLocationRelativeTo(null); 
                dlg.setVisible(true);
            }
        });
        this.vista.btnfichaPaciente.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {

        int fila = vista.tablaPacientes.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(null, "¡Seleccione un paciente!", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idSeleccionado = vista.tablaPacientes.getValueAt(fila, 0).toString();

        com.mycompany.geriatrico1.vista.panel_principal_paciente panel = new com.mycompany.geriatrico1.vista.panel_principal_paciente();
        
        com.mycompany.geriatrico1.controlador.CtrlFichaPaciente ctrl = new com.mycompany.geriatrico1.controlador.CtrlFichaPaciente(panel, idSeleccionado);

        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Ficha del Paciente: " + idSeleccionado);
        dialog.setModal(true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
});
    }
    

    public void cargarTablaPacientes() {
        
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaPacientes.getModel();
        
        modelo.setRowCount(0);
        
        PacienteDao dao = new PacienteDao();
        List<Object[]> lista = dao.listarPacientes();
        
        for (Object[] fila : lista) {
            modelo.addRow(fila);
        }
        ocultarColumna(vista.tablaPacientes, 0);
    }
    public void abrirEdicionPaciente() {
        int fila = vista.tablaPacientes.getSelectedRow();

        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla para editar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        com.mycompany.geriatrico1.vista.FichaNewPaciente dip = new com.mycompany.geriatrico1.vista.FichaNewPaciente();

        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(fila);

            String idPac = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();

        try {
            java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
            String sql = "SELECT p.cedula_perso, p.nombre_perso, p.apellido1_perso, p.apellido2_perso, " +
                         "p.telefono_perso, p.correo_perso, p.direccion_perso, p.estado_civil_perso, p.genero_perso, " +
                         "pac.grado_dependencia, pac.tipo_sandre_pac " +
                         "FROM persona p INNER JOIN paciente pac ON p.cedula_perso = pac.cedula_perso_pac " +
                         "WHERE pac.id_pac = ?";
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPac);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dip.txtCedula.setText(rs.getString("cedula_perso"));
                dip.txtNombre.setText(rs.getString("nombre_perso"));
                dip.txtApellido1.setText(rs.getString("apellido1_perso"));
                dip.txtApellido2.setText(rs.getString("apellido2_perso"));
                dip.txtTelef.setText(rs.getString("telefono_perso"));
                dip.txtCorreo.setText(rs.getString("correo_perso"));
                dip.txtDirecc.setText(rs.getString("direccion_perso"));

                String estadoC = rs.getString("estado_civil_perso");
                if (estadoC != null) dip.cmbEstadoCivil.setSelectedItem(estadoC);

                String sangre = rs.getString("tipo_sandre_pac");
                if (sangre != null) dip.cmbTipoSangre.setSelectedItem(sangre);

                String grado = rs.getString("grado_dependencia");
                if (grado != null) dip.cmbGradoDependencia.setSelectedItem(grado);

                String genero = rs.getString("genero_perso");
                if (genero != null) {
                    if (genero.equalsIgnoreCase("M")) {
                        dip.cmbGenero.setSelectedIndex(1);
                    } else if (genero.equalsIgnoreCase("F")) {
                        dip.cmbGenero.setSelectedIndex(2);
                    }
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al pre-cargar datos del paciente: " + e.getMessage());
        }

        dip.txtCedula.setEditable(false);
        dip.btnGuardar.setText("Actualizar Paciente");
        com.mycompany.geriatrico1.dao.PacienteDao daoSecundario = new com.mycompany.geriatrico1.dao.PacienteDao();
        com.mycompany.geriatrico1.controlador.CtrlPaciente ctrlSecundario = new com.mycompany.geriatrico1.controlador.CtrlPaciente(dip, daoSecundario);
        
        dip.setVisible(true);
    }
    
    public void darDeBajaPaciente() {
        int fila = vista.tablaPacientes.getSelectedRow();
        
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla para dar de baja.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idPac = vista.tablaPacientes.getValueAt(fila, 0).toString();
        String nombreCompleto = vista.tablaPacientes.getValueAt(fila, 2).toString() + " " + vista.tablaPacientes.getValueAt(fila, 3).toString();
        
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro que desea dar de baja al paciente:\n" + nombreCompleto + "?\nPasará a estado INACTIVO en el sistema.", 
                "Confirmar Baja Lógica", 
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            com.mycompany.geriatrico1.dao.PacienteDao dao = new com.mycompany.geriatrico1.dao.PacienteDao();
            
            if (dao.darDeBajaPaciente(idPac)) {
                javax.swing.JOptionPane.showMessageDialog(vista, "El paciente " + nombreCompleto + " ha sido dado de baja exitosamente.");
                
                cargarTablaPacientes();
                ocultarColumna(vista.tablaEmpleados, 0);
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al intentar dar de baja en la base de datos.", "Error SQL", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void filtrarTablaPacientes(String textoBusqueda) {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaPacientes.getModel();
        
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelo);
        vista.tablaPacientes.setRowSorter(sorter);
        
        if (textoBusqueda.trim().length() == 0) {
            sorter.setRowFilter(null); 
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + textoBusqueda));
        }
    }
    //--------------------------------EMPLEADOS-------------------------------------
    public void cargarTablaEmpleados() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaEmpleados.getModel();
        
        modelo.setRowCount(0);
        
        com.mycompany.geriatrico1.dao.EmpleadoDAO dao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        java.util.List<Object[]> lista = dao.listarEmpleadosActivos();
        
        for (Object[] fila : lista) {
            modelo.addRow(fila);
            ocultarColumna(vista.tablaEmpleados, 0);
        } 
    }
    public void filtrarTablaEmpleados(String textoBusqueda) {
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaEmpleados.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelo);
        vista.tablaEmpleados.setRowSorter(sorter);
        
        if (textoBusqueda.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + textoBusqueda));
        }
    }


    public void darDeBajaEmpleado() {
        int fila = vista.tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un empleado.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idEmp = vista.tablaEmpleados.getValueAt(fila, 0).toString();
        String nombre = vista.tablaEmpleados.getValueAt(fila, 2).toString() + " " + vista.tablaEmpleados.getValueAt(fila, 3).toString();
        
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(vista, "¿Dar de baja a " + nombre + "?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            EmpleadoDAO dao = new EmpleadoDAO();
            if (dao.darDeBajaEmpleado(idEmp)) {
                // Aquí debes llamar al método que recarga tu tabla de empleados
                cargarTablaEmpleados();
                ocultarColumna(vista.tablaEmpleados, 0);
            }
        }
    }


    public void abrirEdicionEmpleado() {
        int fila = vista.tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un empleado para editar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        com.mycompany.geriatrico1.vista.FichaNuevaCuenta dlg = new com.mycompany.geriatrico1.vista.FichaNuevaCuenta();

        String idEmp = vista.tablaEmpleados.getValueAt(fila, 0).toString();

        try {
            java.sql.Connection con = com.mycompany.geriatrico1.conexion.Conexion.getConnection();
            String sql = "SELECT p.cedula_perso, p.nombre_perso, p.apellido1_perso, p.apellido2_perso, " +
                         "p.telefono_perso, p.correo_perso, p.direccion_perso, p.estado_civil_perso, p.genero_perso, " +
                         "e.rol_emp, e.tipo_contrato_emp " +
                         "FROM persona p INNER JOIN empleado e ON p.cedula_perso = e.cedula_perso_emp " +
                         "WHERE e.id_emp = ?";
            
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idEmp);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dlg.txtCedula.setText(rs.getString("cedula_perso"));
                dlg.txtNombre.setText(rs.getString("nombre_perso"));
                dlg.txtApellido1.setText(rs.getString("apellido1_perso"));
                dlg.txtApellido2.setText(rs.getString("apellido2_perso"));
                dlg.txtTelef.setText(rs.getString("telefono_perso"));
                dlg.txtCorreo.setText(rs.getString("correo_perso"));
                dlg.txtDirecc.setText(rs.getString("direccion_perso"));

                String estadoC = rs.getString("estado_civil_perso");
                if (estadoC != null) dlg.cmbEstCivCuenNue.setSelectedItem(estadoC);

                String rol = rs.getString("rol_emp");
                if (rol != null) dlg.cmbRol.setSelectedItem(rol);

                String contrato = rs.getString("tipo_contrato_emp");
                if (contrato != null) dlg.cmbContraMed.setSelectedItem(contrato);

                String genero = rs.getString("genero_perso");
                if (genero != null) {
                    if (genero.equalsIgnoreCase("M")) {
                        dlg.cmbGenero.setSelectedIndex(1);
                    } else if (genero.equalsIgnoreCase("F")) {
                        dlg.cmbGenero.setSelectedIndex(2);
                    }
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al pre-cargar datos del empleado: " + e.getMessage());
        }

        dlg.txtCedula.setEditable(false);
        dlg.btnGuardarFicha.setText("Actualizar Cuenta");
        dlg.btnGuardarFicha.setToolTipText(idEmp); // Usas el tooltip para guardar el ID. ¡Es un buen truco!

        com.mycompany.geriatrico1.dao.EmpleadoDAO dao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        com.mycompany.geriatrico1.controlador.CtrlEmpleados ctrl = new com.mycompany.geriatrico1.controlador.CtrlEmpleados(dlg, dao);

        dlg.setLocationRelativeTo(vista);
        dlg.setVisible(true);

        cargarTablaEmpleados();
        ocultarColumna(vista.tablaEmpleados, 0);
    }
    
    private void ocultarColumna(javax.swing.JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setResizable(false);
    }
    
    private void configurarFechaActual() {
        LocalDate fechaHoy = LocalDate.now();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        String fechaTexto = fechaHoy.format(formato);

        String fechaFinal = fechaTexto.substring(0, 1).toUpperCase() + fechaTexto.substring(1);

        vista.lblFecha.setText(fechaFinal);
    }
    
    private void cargarEstadisticasDashboard() {
        try {
    
        com.mycompany.geriatrico1.dao.PacienteDao pacienteDao = new com.mycompany.geriatrico1.dao.PacienteDao();
        int totalPacientes = pacienteDao.contarPacientesActivos();
        
        
        com.mycompany.geriatrico1.dao.EmpleadoDAO empleadoDao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        int totalPersonal = empleadoDao.contarPersonalActivo();
        
        com.mycompany.geriatrico1.dao.AlertaDAO alertaDao = new com.mycompany.geriatrico1.dao.AlertaDAO();
        int totalAlertas = alertaDao.contarAlertasPendientes();
        
        com.mycompany.geriatrico1.dao.CuidadoDAO cuidadoDao = new com.mycompany.geriatrico1.dao.CuidadoDAO();
        int totalCuidados = cuidadoDao.contarCuidadosRegistrados();

        vista.actualizarContadoresVista(totalPacientes, totalPersonal, totalAlertas, totalCuidados);

        } catch (Exception e) {
        System.err.println("Error al cargar estadísticas en el dashboard: " + e.getMessage());
    }
}
    
    
 }
    

