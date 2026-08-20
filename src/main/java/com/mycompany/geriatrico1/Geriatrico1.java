/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.geriatrico1;

import com.mycompany.geriatrico1.vista.Dash_login;
import com.mycompany.geriatrico1.vista.Dashboard_Enfermero;
import com.mycompany.geriatrico1.vista.Dashboard_Medico;


/**
 *
 * @author Santiago
 */
public class Geriatrico1 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Dashboard_Medico vista_med = new Dashboard_Medico();
        vista_med.setVisible(true);
        
        /*
        Dashboard_Enfermero d_Enf = new Dashboard_Enfermero();
        Dash_login l = new Dash_login();
        d_Enf.setVisible(true);
        l.setVisible(false);
        */
    }
    
}
