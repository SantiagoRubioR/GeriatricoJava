/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.geriatrico1.Controlador;

import com.mycompany.geriatrico1.dao.PacienteDao;
import com.mycompany.geriatrico1.vista.Ven_Admin;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Santiago
 */
public class CtrlAdmin {
    private Ven_Admin vista;

    public CtrlAdmin(Ven_Admin vista) {
        this.vista = vista;
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
    }
 }   

