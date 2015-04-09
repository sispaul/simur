/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class Programas extends Pantalla {

    private Tabla tabProgramas = new Tabla();
    private Tabla tabLicencias = new Tabla();

    public Programas() {
        tabProgramas.setId("tabProgramas");
        tabProgramas.setTabla("cei_programas", "progs_codigo", 1);
        tabProgramas.dibujar();
        PanelTabla ptp = new PanelTabla();
        ptp.setPanelTabla(tabProgramas);

        tabLicencias.setId("tabLicencias");
        tabLicencias.setTabla("ce_licencia_programas", "licen_codigo", 2);
        tabLicencias.dibujar();
        PanelTabla ptl = new PanelTabla();
        ptl.setPanelTabla(tabLicencias);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(ptp, ptl, "40%", "H");
        agregarComponente(divTablas);

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
