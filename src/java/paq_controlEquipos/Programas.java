/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
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
public class Programas extends Pantalla {

    private Tabla tabProgramas = new Tabla();
    private Tabla tabLicencias = new Tabla();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public Programas() {
        tabProgramas.setId("tabProgramas");
        tabProgramas.setTabla("cei_programas", "progs_codigo", 1);
        tabProgramas.setHeader("LISTADO DE PROGRAMAS");
        tabProgramas.agregarRelacion(tabLicencias);
        tabProgramas.dibujar();
        PanelTabla ptp = new PanelTabla();
        ptp.setPanelTabla(tabProgramas);

        tabLicencias.setId("tabLicencias");
        tabLicencias.setTabla("cei_licencia_programas", "licen_codigo", 2);
        tabLicencias.setHeader("LISTADO DE LICENCIAS POR PROGRAMAS");
        tabLicencias.getColumna("tipo_licencia_codigo").setCombo("select tipo_licencia_codigo,tipo_licencia_descripcion from cei_tipo_licencia");
        tabLicencias.getColumna("tipo_licencia_codigo").setMetodoChange("Parametros");
        tabLicencias.getColumna("licen_fecha_compra").setLectura(true);
        tabLicencias.getColumna("licen_numero_licencia").setLectura(true);
        tabLicencias.getColumna("licen_tiempo_vigencia").setLectura(true);
        tabLicencias.getColumna("licen_cantidad").setLectura(true);
        tabLicencias.dibujar();
        PanelTabla ptl = new PanelTabla();
        ptl.setPanelTabla(tabLicencias);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(ptp, ptl, "55%", "H");
        agregarComponente(divTablas);

    }

    public void Parametros() {
        TablaGenerica tabDato = accesoDatos.getTipoLicencia(Integer.parseInt(tabLicencias.getValor("tipo_licencia_codigo")));
        if (!tabDato.isEmpty()) {
            System.err.println(tabDato.getValor("tipo_licencia_descripcion"));
            if (tabDato.getValor("tipo_licencia_descripcion").equals("PAGADA")) {
                tabLicencias.getColumna("licen_fecha_compra").setLectura(false);
                tabLicencias.getColumna("licen_numero_licencia").setLectura(false);
                tabLicencias.getColumna("licen_tiempo_vigencia").setLectura(false);
                tabLicencias.getColumna("licen_cantidad").setLectura(false);
                utilitario.addUpdate("tabLicencias");
            } else {
                tabLicencias.getColumna("licen_fecha_compra").setLectura(true);
                tabLicencias.getColumna("licen_numero_licencia").setLectura(true);
                tabLicencias.getColumna("licen_tiempo_vigencia").setLectura(true);
                tabLicencias.getColumna("licen_cantidad").setLectura(true);
                utilitario.addUpdate("tabLicencias");
            }
        } else {
            utilitario.agregarMensaje("Tipo de Licencia no encontrada", null);
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabProgramas.guardar()) {
            if (tabLicencias.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabProgramas() {
        return tabProgramas;
    }

    public void setTabProgramas(Tabla tabProgramas) {
        this.tabProgramas = tabProgramas;
    }

    public Tabla getTabLicencias() {
        return tabLicencias;
    }

    public void setTabLicencias(Tabla tabLicencias) {
        this.tabLicencias = tabLicencias;
    }
}
