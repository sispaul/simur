/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_gestor_ingreso extends Pantalla{
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Tabla tab_comercial = new Tabla();
    private Tabla tab_gestor = new Tabla();
    private Tabla tab_gestor1 = new Tabla();

    
    public pre_gestor_ingreso() {
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("TIPOS DE GESTORES");
        agregarComponente(div_division);
        dibujarEmpresa();
    }
    
        private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("CREACION DE GESTORES");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("GESTORES DE EMPRESA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarEmpresa");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("GESTORES PARTICULARES");
        itm_permisos.setIcon("ui-icon-person");
        itm_permisos.setMetodo("dibujarGestor");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);
        
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
   

    public void dibujarEmpresa(){
        str_opcion = "0";
        limpiarPanel();
        
        tab_comercial.setId("tab_comercial");
        tab_comercial.setTabla("trans_comercial_automotores", "ide_comercial_automotores", 1);
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("NOMBRE DE EMPRESA");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
        tab_comercial.getColumna("ruc_empresa").setUnico(true);
        tab_comercial.getColumna("direccion_empresa").setNombreVisual("DIRECCIÓN");
        tab_comercial.getColumna("telefono_empresa").setNombreVisual("TELÉFONO");
        tab_comercial.agregarRelacion(tab_gestor);
        tab_comercial.getGrid().setColumns(2);
        tab_comercial.setTipoFormulario(true);
        tab_comercial.setHeader("DATOS DE EMPRESA");
        tab_comercial.dibujar();
        PanelTabla pat_comercial = new PanelTabla();
        pat_comercial.setPanelTabla(tab_comercial);
        pan_opcion.setTitle(" INGRESO DE DATOS DE GESTORES");
        pan_opcion.getChildren().add(pat_comercial);
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("ide_tipo_gestor").setNombreVisual("TIPO GESTOR");
        tab_gestor.getColumna("ide_tipo_gestor").setCombo("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR WHERE DESCRIPCION_GESTOR LIKE 'EMPRESA'");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("CEDULA");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("NOMBRE");
        tab_gestor.getColumna("direccion_gestor").setNombreVisual("DIRECCIÓN");
        tab_gestor.getColumna("telefono_gestor").setNombreVisual("TELÉFONO");
        tab_gestor.getColumna("estado").setNombreVisual("ESTADO");
        tab_gestor.getColumna("nombre_gestor").setMayusculas(true);
        tab_gestor.setHeader("DATOS DE GESTOR");
        tab_gestor.getColumna("direccion_gestor").setMayusculas(true);
        tab_gestor.getGrid().setColumns(2);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor);
        
        Grupo gru1 = new Grupo();
        gru1.getChildren().add(pat_comercial);
        gru1.getChildren().add(pat_gestor);
        pan_opcion.getChildren().add(gru1);

    }
    
    public void dibujarGestor(){
        str_opcion = "1";
        limpiarPanel();
        tab_gestor1.setId("tab_gestor1");
        tab_gestor1.setTabla("trans_gestor", "ide_gestor", 3);
        tab_gestor1.getColumna("ide_gestor").setNombreVisual("ID");
//        tab_gestor1.getColumna("ide_tipo_gestor").setNombreVisual("TIPO GESTOR");
        tab_gestor.getColumna("ide_tipo_gestor").setCombo("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR");
        tab_gestor1.getColumna("cedula_gestor").setNombreVisual("CEDULA");
        tab_gestor1.getColumna("nombre_gestor").setNombreVisual("NOMBRE");
        tab_gestor1.getColumna("direccion_gestor").setNombreVisual("DIRECCIÓN");
        tab_gestor1.getColumna("telefono_gestor").setNombreVisual("TELÉFONO");
        tab_gestor1.getColumna("estado").setNombreVisual("ESTADO");
        tab_gestor1.getColumna("ide_comercial_automotores").setVisible(false);
        tab_gestor1.getColumna("nombre_gestor").setMayusculas(true);
        tab_gestor1.getColumna("direccion_gestor").setMayusculas(true);
        tab_gestor1.setHeader("DATOS DE GESTOR PARTICULAR");
        tab_gestor1.getGrid().setColumns(2);
        tab_gestor1.setTipoFormulario(true);
        tab_gestor1.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor1);
        pan_opcion.getChildren().add(pat_gestor);
        
    }
    
    @Override
    public void insertar() {
        if (str_opcion.equals("0")) {
            if (tab_comercial.isFocus()) {
                tab_comercial.insertar();
            } else if (tab_gestor.isFocus()) {
                tab_gestor.insertar();
            }  
        }else if (str_opcion.equals("1")) {
            if (tab_gestor1.isFocus()) {
                tab_gestor1.insertar();
            }
        }
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_comercial() {
        return tab_comercial;
    }

    public void setTab_comercial(Tabla tab_comercial) {
        this.tab_comercial = tab_comercial;
    }

    public Tabla getTab_gestor() {
        return tab_gestor;
    }

    public void setTab_gestor(Tabla tab_gestor) {
        this.tab_gestor = tab_gestor;
    }

    public Tabla getTab_gestor1() {
        return tab_gestor1;
    }

    public void setTab_gestor1(Tabla tab_gestor1) {
        this.tab_gestor1 = tab_gestor1;
    }

    
}
