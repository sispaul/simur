/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Administrador
 */
public class pre_simplepb extends Pantalla{
    private Tabla tab_tabla=new Tabla();

    public pre_simplepb() {
        //id a la tabla
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("CMT_GENERO", "IDE_CMGEN", 1);//nombre de la tabla, pk de tabla, numero tabla
        tab_tabla.setHeader("Genero");
        
        tab_tabla.getColumna("FECHA_INGRE").setLectura(true);
        tab_tabla.getColumna("FECHA_INGRE").setEstilo("font-size:20px;color:red;");
        
                
        
        tab_tabla.dibujar(); //Ultima Metodo de la tabla.
        
        PanelTabla pat_panel=new PanelTabla();
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
