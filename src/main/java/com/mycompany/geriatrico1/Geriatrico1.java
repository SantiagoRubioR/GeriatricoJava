/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.geriatrico1;

import com.mycompany.geriatrico1.vista.Dash_login;
import com.mycompany.geriatrico1.vista.Dashboard_Enfermero;
import com.mycompany.geriatrico1.vista.Dashboard_Medico;
import com.mycompany.geriatrico1.vista.Ven_Admin;


/**
 *
 * @author Santiago
 */
public class Geriatrico1 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Dash_login dashlog=new Dash_login();
        dashlog.setVisible(true);
        Dashboard_Medico vista_med = new Dashboard_Medico();
        vista_med.setVisible(false);
        Ven_Admin vista_admin = new Ven_Admin();
        vista_admin.setVisible(false);
        
        
        
        
        
        
    }
    
}
