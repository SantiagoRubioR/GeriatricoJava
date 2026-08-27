package com.mycompany.geriatrico1.controlador;

import com.mycompany.geriatrico1.dao.UsuarioDAO;
import com.mycompany.geriatrico1.vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CtrlLogin implements ActionListener {
    
    private Dash_login vistaLogin;
    private UsuarioDAO dao;

    public CtrlLogin(Dash_login vistaLogin, UsuarioDAO dao) {
        this.vistaLogin = vistaLogin;
        this.dao = dao;
        this.vistaLogin.BtnIngresar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.BtnIngresar) {
            
            String usuario = vistaLogin.txtUsuario.getText().trim();
            String password = new String(vistaLogin.txtContra.getPassword());
            
            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vistaLogin, "Llene todos los campos", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Llamamos al DAO
            String[] acceso = dao.iniciarSesion(usuario, password);

            if (acceso != null) {
                String cargo = acceso[0].toUpperCase();
                String estado = acceso[1].toUpperCase();
                String nombreUsuario = acceso[2];

                if (estado.equals("INACTIVO") || estado.equals("SUSPENDIDO")) {
                    JOptionPane.showMessageDialog(vistaLogin, "Usuario inactivo o suspendido. Contacte al administrador.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cerrar Login
                vistaLogin.dispose();

                // Abrir el Dashboard asignado
                switch (cargo) {
                    case "ADMINISTRADOR":
                        Ven_Admin dashAdmin = new Ven_Admin(nombreUsuario);
                        dashAdmin.setVisible(true);
                        break;
                    case "MEDICO":
                        Dashboard_Medico dashMed = new Dashboard_Medico(nombreUsuario);
                        dashMed.setVisible(true);
                        break;
                    case "ENFERMERO":
                        Dashboard_Enfermero dashEnf = new Dashboard_Enfermero(nombreUsuario);
                        dashEnf.setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Cargo no reconocido por el sistema.");
                }
            } else {
                JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}