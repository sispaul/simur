/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Paolo
 */
public class pre_articulo extends Pantalla {

    private Conexion con_postgres = new Conexion();
    private Tabla tab_tabla = new Tabla();

    public pre_articulo() {
        //Persistencia a la postgres.
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("bodt_articulos", "ide_bodt_articulo", 1);
        tab_tabla.setHeader("ARTICULOS - BODEGA");
        tab_tabla.getColumna("ide_mat_bodega").setCombo("select ide_mat_bodega,des_material from bodc_material");
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        agregarComponente(pat_panel);

    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}
