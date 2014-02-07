/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Boton;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class opcion extends Pantalla{
private SeleccionTabla set_tabla = new SeleccionTabla();
private Tabla tab_tabla=new Tabla();

    public opcion() {

                //1 Id a la tabla
        tab_tabla.setId("tab_tabla");        
        tab_tabla.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 1);
        tab_tabla.setHeader("REPRESENTANTES"); //Poner titulo a la tabla 
        
        tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setLectura(true); //La columna  se hace de lectura
        tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_CMREP").setEstilo("font-size:18px;color:blue;"); //estilo css
        tab_tabla.setTipoFormulario(true); //convierte en formulario
        //HACER COMBO
        tab_tabla.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");        
        //Ultimo metodo de la tabla
        tab_tabla.dibujar();
        
        PanelTabla pat_panel=new PanelTabla();  //Para el menu contextual
        pat_panel.setPanelTabla(tab_tabla);
        
        agregarComponente(pat_panel);
        
////configurar seleccion unica
        set_tabla.setId("set_tabla");
        Grid gri_busca = new Grid();
        set_tabla.getGri_cuerpo().setHeader(gri_busca);
        set_tabla.setTitle("SELECCIONE MARCAS");
        set_tabla.setSeleccionTabla("SELECT ide_marca,marca from trans_marcas", "ide_marca");
//      set_tabla.getTab_seleccion().getColumna("marca").setFiltro(true);
        agregarComponente(set_tabla);

        Boton bot1 = new Boton();
        bot1.setValue("ABRIR SELECCION TABLA");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot1);
    }
    

    public void abrirSeleccionTabla() {
        set_tabla.dibujar();
    }    
    
    @Override
    public void insertar() {
      }

    @Override
    public void guardar() {
     }

    @Override
    public void eliminar() {
    }

 
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionTabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(SeleccionTabla set_tabla) {
        this.set_tabla = set_tabla;
    }
    
}
