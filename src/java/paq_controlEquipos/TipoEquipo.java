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
public class TipoEquipo extends Pantalla  {

    private Tabla tabTipo = new Tabla();
    
    public TipoEquipo() {
        
        tabTipo.setId("tabTipo");
        tabTipo.setTabla("cei_tipo_equipos", "tipo_codigo", 1);
        tabTipo.setHeader("TIPO DE EQUIPOS");
        tabTipo.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tabTipo);
        
        agregarComponente(ptt);
        
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tabTipo.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabTipo() {
        return tabTipo;
    }

    public void setTabTipo(Tabla tabTipo) {
        this.tabTipo = tabTipo;
    }
    
}
