/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_dobledj extends Pantalla {

    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();

    public pre_dobledj() {

        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("trans_marcas", "ide_marca", 1);
        tab_cabecera.setHeader("LISTADO DE MARCAS");
        tab_cabecera.agregarRelacion(tab_detalle); ///relación        
        tab_cabecera.dibujar();

        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cabecera);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("trans_modelos", "ide_modelo", 2);
        tab_detalle.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_detalle);


        Division div = new Division();
        div.dividir2(tabp, tabp1, "40%", "h");

        agregarComponente(div);


    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_cabecera.guardar();
        tab_detalle.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
}
