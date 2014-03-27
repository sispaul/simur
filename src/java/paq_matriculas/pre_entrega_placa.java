/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;
/**
 *
 * @author KEJA
 */
public class pre_entrega_placa extends Pantalla{

    private Tabla tab_entrega = new Tabla();
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Panel pan_opcion3 = new Panel();
    private Panel pan_opcion4 = new Panel();
    private Efecto efecto = new Efecto();

    private Texto tex_placa = new Texto();
    private Texto tex_placa1 = new Texto();
    private Texto tex_placa2 = new Texto();
    private Texto tex_placa3 = new Texto();
    private Texto tex_placa4 = new Texto();
    private Texto tex_placa5 = new Texto();
    private Texto tex_placa6 = new Texto();
    private Texto tex_placa7 = new Texto();
    private Texto tex_placa8 = new Texto();
    private Texto tex_placa9 = new Texto();
    private Texto tex_placa10 = new Texto();
    
    public pre_entrega_placa() {

        pan_opcion2.setId("pan_opcion2");
	pan_opcion2.setTransient(true);
        
        Grid gri_fechai = new Grid();
        gri_fechai.setColumns(2);
        gri_fechai.getChildren().add(new Etiqueta("FECHA SOLICITUD: "));
        tex_placa.setId("tex_placa");
        gri_fechai.getChildren().add(tex_placa);

        Grid gri_usin = new Grid();
        gri_usin.setColumns(2);
        gri_usin.getChildren().add(new Etiqueta("USUARIO - INGRESO: "));
        tex_placa1.setId("tex_placa1");
        gri_usin.getChildren().add(tex_placa1);
        
        pan_opcion3.setId("pan_opcion3");
	pan_opcion3.setTransient(true);
        
        Grid gri_fechaa = new Grid();
        gri_fechaa.setColumns(2);
        gri_fechaa.getChildren().add(new Etiqueta("NRO SOLICITUD: "));
        tex_placa2.setId("tex_placa2");
        gri_fechaa.getChildren().add(tex_placa2);

        Grid gri_placa = new Grid();
        gri_placa.setColumns(2);
        gri_placa.getChildren().add(new Etiqueta("EMPRESA GESTIONA: "));
        tex_placa3.setId("tex_placa3");
        gri_placa.getChildren().add(tex_placa3);
        
        Grid gri_usap = new Grid();
        gri_usap.setColumns(2);
        gri_usap.getChildren().add(new Etiqueta("GESTOR: "));
        tex_placa4.setId("tex_placa4");
        gri_usap.getChildren().add(tex_placa4);
        
        pan_opcion4.setId("pan_opcion4");
	pan_opcion4.setTransient(true);
        
        Grid gri_soli = new Grid();
        gri_soli.setColumns(2);
        gri_soli.getChildren().add(new Etiqueta("TIPO SOLICITUD: "));
        tex_placa5.setId("tex_placa5");
        gri_soli.getChildren().add(tex_placa5);
         
        Grid gri_usp = new Grid();
        gri_usp.setColumns(2);
        gri_usp.getChildren().add(new Etiqueta("AUTOMOTOR: "));
        tex_placa6.setId("tex_placa6");
        gri_usp.getChildren().add(tex_placa6);
//        
        Grid gri_u = new Grid();
        gri_u .setColumns(2);
        gri_u .getChildren().add(new Etiqueta("SERVICIO: "));
        tex_placa7.setId("tex_placa7");
        gri_u .getChildren().add(tex_placa7);
        
        pan_opcion2.getChildren().add(gri_fechai);
        pan_opcion2.getChildren().add(gri_usin);
        pan_opcion3.getChildren().add(gri_fechaa);
        pan_opcion3.getChildren().add(gri_placa);
        pan_opcion3.getChildren().add(gri_usap);
        pan_opcion4.getChildren().add(gri_soli);
        pan_opcion4.getChildren().add(gri_usp);
        pan_opcion4.getChildren().add(gri_u);
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        
        Grid gri_ti = new Grid();
        gri_ti.setColumns(2);
        gri_ti.getChildren().add(new Etiqueta("FECHA DE APROBACIÒN: "));
        tex_placa8.setId("tex_placa8");
        gri_ti.getChildren().add(tex_placa8);
        
        Grid gri_pl = new Grid();
        gri_pl.setColumns(2);
        gri_pl.getChildren().add(new Etiqueta("NRO. PLACA: "));
        tex_placa9.setId("tex_placa9");
        gri_pl.getChildren().add(tex_placa9);
        
        Grid gri_pr = new Grid();
        gri_pr .setColumns(2);
        gri_pr.getChildren().add(new Etiqueta("USUARIO - APROBACIÒN : "));
        gri_pr.getChildren().add(tex_placa10);
        
        pan_opcion1.getChildren().add(gri_ti);
        pan_opcion1.getChildren().add(gri_pl);
        pan_opcion1.getChildren().add(gri_pr);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        tab_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("aceptarPlaca");
        tab_entrega.getGrid().setColumns(4);
        tab_entrega.setTipoFormulario(true);
        tab_entrega.dibujar();
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("ENTREGA DE PLACAS");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");

        PanelTabla tab_e = new PanelTabla();
        tab_e.setPanelTabla(tab_entrega);
        pan_opcion.getChildren().add(efecto);
        pan_opcion.getChildren().add(tab_e);
        pan_opcion.getChildren().add(pan_opcion2);
        pan_opcion.getChildren().add(pan_opcion3);
        pan_opcion.getChildren().add(pan_opcion4);
        pan_opcion.getChildren().add(pan_opcion1);
        agregarComponente(pan_opcion);
    }
   
    
    @Override
    public void insertar() {
        tab_entrega.insertar();
    }
    @Override
    public void guardar() {

    }
    @Override
    public void eliminar() {
    }

    public Tabla getTab_entrega() {
        return tab_entrega;
    }

    public void setTab_entrega(Tabla tab_entrega) {
        this.tab_entrega = tab_entrega;
    }
    
}