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
 * @author p-sistemas
 */
public class pre_simplepaul extends Pantalla {

 private Tabla tab_tabla = new Tabla  (); //declaracion de objeto

    public pre_simplepaul() {
     tab_tabla.setId("tab_tabla"); 
     tab_tabla.setTabla("bodt_bodega","ide_bodega",1);/*a que tabla vamos a dirigirnos*/
     //header cabecera
     tab_tabla.setHeader("ACCIONES BODEGA DE VEHICULOS");//TITULO A UNA TABLA
     //bloqueo a una columna deseada o se pùede utilizarlo mediante combos, listas
     tab_tabla.getColumna("marca").setLectura(true);
     // convertir en formulario
     tab_tabla.setTipoFormulario(true);
     // estilo de columna o tabla
     //tab_tabla.getColumna("modelo").setEstilo(font.);
     // hacer combo
     tab_tabla.getColumna("modelo").setCombo("");
     
     //ultimo metodo del la tabla, en si la ultima sentencia
     tab_tabla.dibujar();
    /*contenedor, menu contextual*/
        PanelTabla pat_panel = new PanelTabla();
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
