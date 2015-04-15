/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_controlEquipos.ejb.Procesos;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class CatalogoEquipos extends Pantalla {

    private Tabla tabEquipo = new Tabla();
    private Tabla tabAccesorio = new Tabla();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public CatalogoEquipos() {

        tabEquipo.setId("tabEquipo");
        tabEquipo.setTabla("cei_descripcion_equipos", "desc_codigo", 1);
        tabEquipo.getColumna("desc_serie").setMetodoChange("infEquipo");
        tabEquipo.agregarRelacion(tabAccesorio);
        tabEquipo.setTipoFormulario(true);
        tabEquipo.getGrid().setColumns(4);
        tabEquipo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tabEquipo);

        tabAccesorio.setId("tabAccesorio");
        tabAccesorio.setTabla("cei_accesorios", "acce_codigo", 2);
        tabAccesorio.dibujar();
        PanelTabla pta = new PanelTabla();
        pta.setPanelTabla(tabAccesorio);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(pte, pta, "60%", "H");
        agregarComponente(divTablas);

    }

    public void infEquipo() {
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabEquipo.guardar()) {
            if (tabAccesorio.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabEquipo() {
        return tabEquipo;
    }

    public void setTabEquipo(Tabla tabEquipo) {
        this.tabEquipo = tabEquipo;
    }

    public Tabla getTabAccesorio() {
        return tabAccesorio;
    }

    public void setTabAccesorio(Tabla tabAccesorio) {
        this.tabAccesorio = tabAccesorio;
    }
}
