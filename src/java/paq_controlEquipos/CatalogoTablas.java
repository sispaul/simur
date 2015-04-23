/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class CatalogoTablas extends Pantalla {

    private Tabla tabCatalogo = new Tabla();

    public CatalogoTablas() {

        tabCatalogo.setId("tabCatalogo");
        tabCatalogo.setTabla("cei_catalogo_tablas", "catalogo_codigo", 1);
        tabCatalogo.setHeader("CATALOGO DE TABLAS PARA APLICACION");
        List lista = new ArrayList();
        Object fila1[] = {"1", "SQL"};
        Object fila2[] = {"2", "POSTGRES"};
        lista.add(fila1);
        lista.add(fila2);
        tabCatalogo.getColumna("catalogo_base").setCombo(lista);
        tabCatalogo.dibujar();

        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tabCatalogo);
        agregarComponente(ptc);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabCatalogo.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabCatalogo() {
        return tabCatalogo;
    }

    public void setTabCatalogo(Tabla tabCatalogo) {
        this.tabCatalogo = tabCatalogo;
    }
}
