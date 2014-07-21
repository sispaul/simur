/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_nomina.ejb.bloqueoRol;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_bloqueo_rol extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //dibujar tablas
    private Tabla tab_decimo = new Tabla();
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    
    //
    @EJB
    private bloqueoRol roles = (bloqueoRol) utilitario.instanciarEJB(bloqueoRol.class);
    
    public pre_bloqueo_rol() {
        //declaracion de cadena de conexion
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        //declaracion de tabla
        tab_decimo.setId("tab_decimo");
        tab_decimo.setConexion(con_postgres);
        tab_decimo.setTabla("cont_periodo_actual", "ide_periodo", 1);
        List lista = new ArrayList();
        Object fila1[] = {
            "S", "SI"
        };
        Object fila2[] = {
            "N", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_decimo.getColumna("bloqueo_rol").setRadio(lista, "S");
        
        tab_decimo.getColumna("per_descripcion").setLectura(true);
        tab_decimo.getColumna("per_actual").setVisible(false);
        tab_decimo.getColumna("per_bloqueo").setVisible(false);
        tab_decimo.getColumna("mayorizado").setVisible(false);
        tab_decimo.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_decimo);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setTitle("BLOQUEO DE ROL POR PERIODO");
        pan_opcion.getChildren().add(tpd);
        agregarComponente(pan_opcion);
        
    }

    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        
        TablaGenerica tab_dato = roles.verifSI(tab_decimo.getValor("bloqueo_rol"));
           if (!tab_dato.isEmpty()) {
               if(Integer.parseInt(tab_dato.getValor("cantidad"))<1 && tab_dato.getValor("bloqueo_rol").equals("S")){
                   tab_decimo.guardar();
                   con_postgres.guardarPantalla();
               }else{
                        if(Integer.parseInt(tab_dato.getValor("cantidad"))>1 && tab_dato.getValor("bloqueo_rol").equals("N")){
                            tab_decimo.guardar();
                            con_postgres.guardarPantalla();
                        }else{
                            utilitario.agregarMensajeError("Mas de Dos valores", "SI");
                        }
               }
               
           }else {
                   tab_decimo.guardar();
                   con_postgres.guardarPantalla();
                  }
           
    }

    
    @Override
    public void eliminar() {
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_decimo() {
        return tab_decimo;
    }

    public void setTab_decimo(Tabla tab_decimo) {
        this.tab_decimo = tab_decimo;
    }
    
}
