/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import paq_nomina.*;
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
public class pre_bloqueo_combustible extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //dibujar tablas
    private Tabla tab_consumo = new Tabla();
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    
    //
    @EJB
    private bloqueoRol roles = (bloqueoRol) utilitario.instanciarEJB(bloqueoRol.class);
    
    public pre_bloqueo_combustible() {
        //declaracion de cadena de conexion
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        //declaracion de tabla
        tab_consumo.setId("tab_consumo");
        tab_consumo.setConexion(con_postgres);
        tab_consumo.setTabla("cont_periodo_actual", "ide_periodo", 1);
        List lista = new ArrayList();
        Object fila1[] = {
            "S", "SI"
        };
        Object fila2[] = {
            "N", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_consumo.getColumna("bloqueo_combustible").setRadio(lista, "S");
        
        tab_consumo.getColumna("per_descripcion").setLectura(true);
        tab_consumo.getColumna("per_actual").setVisible(false);
        tab_consumo.getColumna("per_bloqueo").setVisible(false);
        tab_consumo.getColumna("mayorizado").setVisible(false);
        tab_consumo.getColumna("bloqueo_rol").setVisible(false);
        tab_consumo.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_consumo);
        
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
        
        TablaGenerica tab_dato = roles.verifSI(tab_consumo.getValor("bloqueo_rol"));
           if (!tab_dato.isEmpty()) {
               if(Integer.parseInt(tab_dato.getValor("cantidad"))<1 && tab_dato.getValor("bloqueo_rol").equals("S")){
                   tab_consumo.guardar();
                   con_postgres.guardarPantalla();
               }else{
                        if(Integer.parseInt(tab_dato.getValor("cantidad"))>1 && tab_dato.getValor("bloqueo_rol").equals("N")){
                            tab_consumo.guardar();
                            con_postgres.guardarPantalla();
                        }else{
                            utilitario.agregarMensajeError("Mas de Dos valores", "SI");
                        }
               }
               
           }else {
                   tab_consumo.guardar();
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
        return tab_consumo;
    }

    public void setTab_decimo(Tabla tab_consumo) {
        this.tab_consumo = tab_consumo;
    }
    
}
