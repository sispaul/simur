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
import paq_nomina.ejb.mergeDescuento;

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
private Tabla tab_consulta = new Tabla();
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
        bot1.setValue("DEPURAR DESCUENTO");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("completar");
       bar_botones.agregarBoton(bot1); 
     
    
        Boton bot2 = new Boton();
        bot2.setValue("MIGRAR DESCUENTO");
        bot2.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot2.setMetodo("migrar");
       bar_botones.agregarBoton(bot2); 
       
        Boton bot3 = new Boton();
        bot3.setValue("BORRAR DESCUENTO");
        bot3.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot3.setMetodo("borrar");
       bar_botones.agregarBoton(bot3);  
    }
    
    
    
      public void completar() {
  
         Integer ano;
         Integer ide_periodo;
         Integer id_distributivo_roles;
         Integer ide_columna;
                 
         
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
         tab_consulta.ejecutarSql();
         ano = Integer.parseInt(tab_consulta.getValor("ano"));
         ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_columna"));
         id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
         ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;
         
        mDescuento.actualizarDescuento(ano, ide_periodo, id_distributivo_roles, ide_columna);
        mDescuento.actualizarDescuento1(ano, ide_periodo, id_distributivo_roles, ide_columna);
        tab_tabla.actualizar();
        
        }
      
       public void migrar(){                     
         Integer ano;
         Integer ide_periodo;
         Integer id_distributivo_roles;
         Integer ide_columna;
                 
         
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
         tab_consulta.ejecutarSql();
         ano = Integer.parseInt(tab_consulta.getValor("ano"));
         ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_columna"));
         id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
         ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;  
         mDescuento.migrarDescuento(ano,ide_periodo,id_distributivo_roles,ide_columna);
         }
                    
         public void borrar()
         {
         mDescuento.borrarDescuento();
         tab_tabla.actualizar();
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
