/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Division;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_pantalla extends Pantalla{
    private Panel pan_opcion = new Panel();
    private PanelMenu pam_menu = new PanelMenu();
    
    public pre_pantalla() {
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        
        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("MENU DE OPCIONES");
        agregarComponente(div_division);
    }
    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("MATRICULACIÓN DE VEHICULOS");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("SOLICITUD DE MATRICULA");
        itm_datos_empl.setIcon("ui-icon-key");
//        itm_datos_empl.setMetodo("dibujarEmpresa");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("ENTREGA DE MATRICULA");
        itm_permisos.setIcon("ui-icon-contact");
//        itm_permisos.setMetodo("dibujarPermisos");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);

        // ITEM 2: OPCION 1
        ItemMenu itm_socios = new ItemMenu();
        itm_socios.setValue("DATOS GESTORES");
        itm_socios.setIcon("ui-icon-person");
//        itm_socios.setMetodo("dibujarSocios");
        itm_socios.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_socios);

    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }
    
}
