/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_simplediego extends Pantalla {
    
    private Tabla tab_tabla=new Tabla();

    public pre_simplediego() {
        
        //1 Id a la tabla
        tab_tabla.setId("tab_tabla");        
        tab_tabla.setTabla("CMT_ACCION", "IDE_CMACC", 1);
        
        tab_tabla.dibujar();
        
        PanelTabla pat_panel=new PanelTabla();  //Para el menu contextual
        pat_panel.setPanelTabla(tab_tabla);
        
        agregarComponente(pat_panel);
        
        
    }
  
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
    
}
