/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_turismo;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;


/**
 *
 * @author p-sistemas
 */
public class pre_ingresocon extends Pantalla{
private Tabla tab_categoria = new Tabla();
private Tabla tab_tipo = new Tabla();

    public pre_ingresocon() {
        
        tab_categoria.setId("tab_categoria");
        tab_categoria.setTabla("TUR_CATEGORIA", "ID_CATEGORIA", 1);
        tab_categoria.setHeader("CATEGORIAS DE ESTABLECIMIENTOS");
        tab_categoria.getColumna("ID_CATEGORIA").setNombreVisual("ID");
        tab_categoria.getColumna("DESCRIPCION_CATEG").setNombreVisual("DESCRIPCIÓN");
        tab_categoria.setTipoFormulario(true);
        tab_categoria.agregarRelacion(tab_tipo);   
        tab_categoria.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_categoria);

        tab_tipo.setId("tab_tipo");
        tab_tipo.setTabla("TUR_TIPO_ESTABLECIMIENTO", "CODIGO_TIPO", 2);
        tab_tipo.setHeader("TIPO DE ESTABLECIMIENTO");
        tab_tipo.getColumna("CODIGO_TIPO").setNombreVisual("ID");
        tab_tipo.getColumna("DESCRIPCION_TIPO").setNombreVisual("DESCRIPCIÓN");
        tab_tipo.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tipo);

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
       tab_categoria.guardar();
       tab_tipo.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_categoria() {
        return tab_categoria;
    }

    public void setTab_categoria(Tabla tab_categoria) {
        this.tab_categoria = tab_categoria;
    }

    public Tabla getTab_tipo() {
        return tab_tipo;
    }

    public void setTab_tipo(Tabla tab_tipo) {
        this.tab_tipo = tab_tipo;
    }

}
