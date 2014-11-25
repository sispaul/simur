/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_mantenimiento_vehiculo extends Pantalla{

    private Conexion con_sql = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    
    public pre_mantenimiento_vehiculo() {
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
         
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setSql("SELECT LIS_ID,LIS_NOMBRE,LIS_NOMBRE as NOMBRE FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
        tab_tabla.setCampoPadre("LIS_NOMBRE");
        tab_tabla.setRows(10);
        tab_tabla.agregarRelacion(tab_tabla1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_sql);
        tab_tabla1.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA WHERE TAB_CODIGO = 'tipo' and LIS_ESTADO = 1 and DEPENDENCI = '"+tab_tabla.getValorSeleccionado()+"'");
        tab_tabla1.setRows(10);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel1, "21%", "H");
        agregarComponente(div_division);
        
    }
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        con_sql.guardarPantalla();
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }
    
}
