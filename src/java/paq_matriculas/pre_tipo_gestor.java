/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_tipo_gestor extends Pantalla{

    private Tabla tab_tabla = new Tabla();
    
    public pre_tipo_gestor() {
        //id a la tabla
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("trans_tipo_gestor", "ide_tipo_gestor",1);
        tab_tabla.setHeader("TIPOS DE GESTOR");
        tab_tabla.getColumna("ide_tipo_gestor").setNombreVisual("CODIGO");
        tab_tabla.getColumna("descripcion_gestor").setNombreVisual("TIPO GESTOR");
        tab_tabla.getColumna("descripcion_gestor").setMayusculas(true);
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
