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
    }

    public void cargarTablaPacientes() {
        
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaPacientes.getModel();
        
        modelo.setRowCount(0);
        
        PacienteDao dao = new PacienteDao();
        List<Object[]> lista = dao.listarPacientes();
        
        // 4. Llenar la tabla fila por fila
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
        // 1. Instanciamos la ventana
        com.mycompany.geriatrico1.vista.FichaNewPaciente dlg = new com.mycompany.geriatrico1.vista.FichaNewPaciente();

        // 2. ¡EL BLINDAJE! Traducimos la fila visual a la fila real de la memoria
        int filaModelo = vista.tablaPacientes.convertRowIndexToModel(fila);

        // 3. Extraemos datos directo del MODELO (usando getModel(), donde la columna 0 sí existe)
        String idPac = vista.tablaPacientes.getModel().getValueAt(filaModelo, 0).toString();
        dlg.txtCedula.setText(vista.tablaPacientes.getModel().getValueAt(filaModelo, 1).toString());
        dlg.txtNombre.setText(vista.tablaPacientes.getModel().getValueAt(filaModelo, 2).toString());
        dlg.txtApellido1.setText(vista.tablaPacientes.getModel().getValueAt(filaModelo, 3).toString());

        // 4. Bloqueamos la cédula y preparamos el botón
        dlg.txtCedula.setEditable(false);
        dlg.btnGuardar.setText("Actualizar Paciente");
        dlg.btnGuardar.setToolTipText(idPac); // Escondemos el ID para usarlo luego

        // 5. Encendemos el controlador secundario y mostramos
        com.mycompany.geriatrico1.dao.PacienteDao dao = new com.mycompany.geriatrico1.dao.PacienteDao(); // Puesto con ruta completa por si acaso
        com.mycompany.geriatrico1.controlador.CtrlPaciente ctrlPac = new com.mycompany.geriatrico1.controlador.CtrlPaciente(dlg, dao);
        dlg.setLocationRelativeTo(vista);
        dlg.setVisible(true);

        // 6. Al cerrarse la ventana de edición, recargamos la tabla automáticamente
        cargarTablaPacientes();
        ocultarColumna(vista.tablaPacientes, 0);
    }
    
    public void darDeBajaPaciente() {
        // 1. Obtenemos la fila que el usuario seleccionó
        int fila = vista.tablaPacientes.getSelectedRow();
        
        // 2. Validamos que haya seleccionado a alguien
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla para dar de baja.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. Extraemos el ID y el Nombre (Asumiendo ID=columna 0, Nombres=columnas 2 y 3)
        String idPac = vista.tablaPacientes.getValueAt(fila, 0).toString();
        String nombreCompleto = vista.tablaPacientes.getValueAt(fila, 2).toString() + " " + vista.tablaPacientes.getValueAt(fila, 3).toString();
        
        // 4. Lanzamos el diálogo de confirmación
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro que desea dar de baja al paciente:\n" + nombreCompleto + "?\nPasará a estado INACTIVO en el sistema.", 
                "Confirmar Baja Lógica", 
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        
        // 5. Si el usuario presiona "Sí"
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Llamamos a tu clase Dao (nota: asegúrate de usar PacienteDao o PacienteDAO según como lo tengas nombrado)
            com.mycompany.geriatrico1.dao.PacienteDao dao = new com.mycompany.geriatrico1.dao.PacienteDao();
            
            if (dao.darDeBajaPaciente(idPac)) {
                javax.swing.JOptionPane.showMessageDialog(vista, "El paciente " + nombreCompleto + " ha sido dado de baja exitosamente.");
                
                // Recargamos la tabla al instante para que se vea el cambio
                cargarTablaPacientes();
                ocultarColumna(vista.tablaEmpleados, 0);
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al intentar dar de baja en la base de datos.", "Error SQL", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void filtrarTablaPacientes(String textoBusqueda) {
        // 1. Obtenemos el modelo de la tabla
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaPacientes.getModel();
        
        // 2. Creamos el sorter (ordenador/filtrador) y se lo aplicamos a la tabla
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelo);
        vista.tablaPacientes.setRowSorter(sorter);
        
        // 3. Aplicamos el filtro según el texto
        if (textoBusqueda.trim().length() == 0) {
            sorter.setRowFilter(null); // Si está vacío, muestra todo
        } else {
            // El "(?i)" hace que la búsqueda ignore mayúsculas/minúsculas
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + textoBusqueda));
        }
    }
    //--------------------------------EMPLEADOS-------------------------------------
    public void cargarTablaEmpleados() {
        // 1. Obtener el modelo de tu tabla (Asegúrate que la variable se llame tablaEmpleados)
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.tablaEmpleados.getModel();
        
        // 2. Limpiar la tabla por si ya tenía datos cargados
        modelo.setRowCount(0);
        
        // 3. Llamar al DAO usando el método que filtra los Inactivos
        com.mycompany.geriatrico1.dao.EmpleadoDAO dao = new com.mycompany.geriatrico1.dao.EmpleadoDAO();
        java.util.List<Object[]> lista = dao.listarEmpleadosActivos();
        
        // 4. Llenar la tabla fila por fila
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

    // ==========================================
    // DAR DE BAJA
    // ==========================================
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

    // ==========================================
    // ABRIR EDICIÓN (INYECCIÓN DE DATOS)
    // ==========================================
    public void abrirEdicionEmpleado() {
        int fila = vista.tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione un empleado para editar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        com.mycompany.geriatrico1.vista.FichaNuevaCuenta dlg = new com.mycompany.geriatrico1.vista.FichaNuevaCuenta();
        
        String idEmp = vista.tablaEmpleados.getValueAt(fila, 0).toString();
        dlg.txtCedula.setText(vista.tablaEmpleados.getValueAt(fila, 1).toString());
        dlg.txtNombre.setText(vista.tablaEmpleados.getValueAt(fila, 2).toString());
        dlg.txtApellido1.setText(vista.tablaEmpleados.getValueAt(fila, 3).toString());
        // Ajusta los setItem de tus combobox según tus variables:
        dlg.cmbRol.setSelectedItem(vista.tablaEmpleados.getValueAt(fila, 4).toString());
        
        dlg.txtCedula.setEditable(false); 
        dlg.btnGuardarFicha.setText("Actualizar Cuenta"); 
        dlg.btnGuardarFicha.setToolTipText(idEmp);

        // Llama al controlador de Empleado (ajusta los nombres de tus clases)
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
        // 1. Obtenemos la fecha exacta del sistema
        LocalDate fechaHoy = LocalDate.now();

        // 2. Creamos el molde con el formato exacto que quieres, forzando el idioma a Español
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        // 3. Traducimos la fecha a texto
        String fechaTexto = fechaHoy.format(formato);

        // 4. Ponemos la primera letra en mayúscula (porque Java devuelve "jueves" en minúscula)
        String fechaFinal = fechaTexto.substring(0, 1).toUpperCase() + fechaTexto.substring(1);

        // 5. Lo enviamos a tu Label (¡Asegúrate de cambiar 'lblFecha' por el nombre real de tu variable!)
        vista.lblFecha.setText(fechaFinal);
    }
 }
    

