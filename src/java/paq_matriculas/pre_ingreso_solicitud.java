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
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud extends Pantalla{
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_detalle = new Tabla();
    
    public pre_ingreso_solicitud() {
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("OPCIONES DE SOLICITUD");
        agregarComponente(div_division);
        
        dibujarSolicitud();
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }
        private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("REQUISITOS DE SOLICITUD");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_solicitud = new ItemMenu();
        itm_solicitud.setValue("SOLICITUD");
        itm_solicitud.setIcon("ui-icon-contact");
        itm_solicitud.setMetodo("dibujarSolicitud");
        itm_solicitud.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_solicitud);

        // ITEM 2 : OPCION 1
        ItemMenu itm_requisitos = new ItemMenu();
        itm_requisitos.setValue("REQUISITOS DE SOLICITUD");
        itm_requisitos.setIcon("ui-icon-person");
        itm_requisitos.setMetodo("dibujarRquisitos");
        itm_requisitos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_requisitos);   
    }
    
    public void dibujarSolicitud(){
        str_opcion = "0";
        limpiarPanel();
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.setHeader("SOLICITUD DE PLACA");
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        pan_opcion.setTitle("INGRESO DE SOLICITUD DE PLACA");
        pan_opcion.getChildren().add(tab_solicitud);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle);
        
        Grupo gru1 = new Grupo();
        gru1.getChildren().add(tab_solicitud);
        gru1.getChildren().add(tab_detalle);
        pan_opcion.getChildren().add(gru1);
    }
    
    public void dibujarRquisitos(){
        str_opcion = "1";
        limpiarPanel();
        
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
    
    
    
    @Override
    public void insertar() {
        if (str_opcion.equals("0")) {
            if (tab_solicitud.isFocus()) {
                tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                tab_detalle.insertar();
            }  
        }else if (str_opcion.equals("1")) {
//            if (tab_gestor1.isFocus()) {
//                tab_gestor1.insertar();
//            }
        }
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
    
}
