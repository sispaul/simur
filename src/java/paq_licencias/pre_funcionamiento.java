/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_licencias;

import framework.componentes.Boton;
import paq_licencias.*;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_funcionamiento extends Pantalla {

    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();

    public pre_funcionamiento() {

        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("TUR_ESTABLECIMIENTO", "ID_TESTABL", 1);
        tab_cabecera.setHeader("LICENCIA ANUAL DE FUNCIONAMIENTO");
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.agregarRelacion(tab_detalle); ///relación        
        tab_cabecera.dibujar();

        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cabecera);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TUR_LICENCIA", "ID_TLICEN", 2);
        //tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_detalle);


        Boton bot1 = new Boton();
        bot1.setValue("SELECCION REPRESENTANTE");
        bot1.setIcon("ui-icon-document"); 
        bot1.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot1);
        
        Division div = new Division();
        div.dividir2(tabp, tabp1, "40%", "H");
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
