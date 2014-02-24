/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
public class pre_bodt_entrega1 extends Pantalla{
    
    private Conexion con_postgres = new Conexion();
    private Tabla tab_cab_cons = new Tabla();
    private Tabla tab_det_egre = new Tabla();
    private Grid grid = new Grid();

    public pre_bodt_entrega1() {
         //Persistencia a la postgres.
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        grid.setColumns(4);
        tab_cab_cons.setId("tab_cab_cons");
        tab_cab_cons.setConexion(con_postgres);
        tab_cab_cons.setTabla("bodt_concepto_egreso", "ide_egreso", 1);
        tab_cab_cons.setHeader("Requisicion de Materiales CONSUMO INTERNO");
        tab_cab_cons.getColumna("ide_bodega").setVisible(false);
        tab_cab_cons.getColumna("particular").setVisible(false);
        tab_cab_cons.getColumna("ubicacion").setVisible(false);
        tab_cab_cons.getColumna("ide_requisicion").setVisible(false);
        tab_cab_cons.getColumna("uso2").setVisible(false);
        tab_cab_cons.setTipoFormulario(true);
        tab_cab_cons.agregarRelacion(tab_det_egre); ///relación        
        tab_cab_cons.getGrid().setColumns(4);
        //tab_cab_cons.getGrid().setDir("h");
        tab_cab_cons.dibujar();

        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cab_cons);

        tab_det_egre.setId("tab_det_egre");
        tab_det_egre.setConexion(con_postgres);
        tab_det_egre.setTabla("bodt_egreso", "ide_bod_egreso", 2);
        tab_det_egre.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_det_egre);


        Division div = new Division();
        div.dividir2(tabp, tabp1, "60%", "h");

        agregarComponente(div);
    }

    public Tabla getTab_cab_cons() {
        return tab_cab_cons;
    }

    public void setTab_cab_cons(Tabla tab_cab_cons) {
        this.tab_cab_cons = tab_cab_cons;
    }

    public Tabla getTab_det_egre() {
        return tab_det_egre;
    }

    public void setTab_det_egre(Tabla tab_det_egre) {
        this.tab_det_egre = tab_det_egre;
    }
    
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_cab_cons.guardar();
        tab_det_egre.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
}
