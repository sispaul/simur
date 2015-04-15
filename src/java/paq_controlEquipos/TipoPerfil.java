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
public class TipoPerfil extends Pantalla {

    private Tabla tabPerfil = new Tabla();

    public TipoPerfil() {
        tabPerfil.setId("tabPerfil");
        tabPerfil.setTabla("cei_tipo_perfil", "perfil_codigo", 1);
        tabPerfil.setHeader("TIPO DE PERFIL");
        tabPerfil.dibujar();
        PanelTabla ptp = new PanelTabla();
        ptp.setPanelTabla(tabPerfil);

        agregarComponente(ptp);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tabPerfil.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabPerfil() {
        return tabPerfil;
    }

    public void setTabPerfil(Tabla tabPerfil) {
        this.tabPerfil = tabPerfil;
    }
    
}
