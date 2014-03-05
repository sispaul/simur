/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import paq_bodega.ejb.servicioBodega;
import paq_nomina.ejb.mergeDescuento;
import paq_pruebas.ejb.servicioPruebas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author m-paucar
 * 
 *
 */
public class pre_descuento extends Pantalla{
    
    
    
private Tabla tab_tabla = new Tabla();
//1.-
 @EJB
private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
 
   
 
private Conexion con_postgres= new Conexion();
    public pre_descuento() {
        //2.-
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        //3.-
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        tab_tabla.setId("tab_tabla");
        //4.-
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setNumeroTabla(1);       
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
       
     
        
     

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
        Boton bot1 = new Boton();
        bot1.setValue("SOBRESCIRBIR DESCUENTO");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("metodo");
        bar_botones.agregarBoton(bot1); 
     
    
         
    }
    
    
    
  public void metodo() {
     //    
      
      mDescuento.actualizarDescuentoIdeEmpleado();
         
     tab_tabla.actualizar();
    }
            


 
    
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {  
            guardarPantalla();
       }
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

    private static class ser_PRUEBA {

        public ser_PRUEBA() {
        }
    }

   
    
}
