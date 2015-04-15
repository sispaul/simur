/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class TipoLicencias extends Pantalla {

    private Tabla tabLicencias = new Tabla();

    public TipoLicencias() {
        
        tabLicencias.setId("tabLicencias");
        tabLicencias.setTabla("cei_tipo_licencia", "tipo_licencia_codigo", 1);
        tabLicencias.setHeader("TIPO DE LICENCIAS");
        tabLicencias.dibujar();
        PanelTabla ptl = new PanelTabla();
        ptl.setPanelTabla(tabLicencias);

        agregarComponente(ptl);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tabLicencias.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabLicencias() {
        return tabLicencias;
    }

    public void setTabLicencias(Tabla tabLicencias) {
        this.tabLicencias = tabLicencias;
    }
}
