/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author KEJA
 */
public class pre_tipo_solicitud extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_tipo_solicitud() {
        //Tabla
        //id a la tabla
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("TRANS_TIPO_SOLICTUD", "IDE_TIPO_SOLICTUD", 1);
        tab_tabla.setHeader("TIPOS DE SOLICTUD");
        tab_tabla.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}
