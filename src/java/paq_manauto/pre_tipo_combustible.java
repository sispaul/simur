/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_tipo_combustible extends Pantalla{

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    
    //referencia de tabla
    private Tabla  tab_tabla = new Tabla();
    
    public pre_tipo_combustible() {
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvtipo_combustible", "tipo_combustible_id", 1);
        tab_tabla.setHeader("TIPO DE COMBUSTIBLE / VALOR");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();
        
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir1(ptt);
        agregarComponente(div);
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        con_postgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}
