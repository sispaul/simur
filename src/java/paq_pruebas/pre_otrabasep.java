/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;
/**
 *
 * @author p-sistemas
 */
public class pre_otrabasep extends Pantalla{

    private Conexion con_postgres = new Conexion();

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
    private Tabla tab_tabla = new Tabla();
    
    public pre_otrabasep(){
        con_postgres.setUnidad_persistencia("ProduccionPostgres");
        con_postgres.NOMBRE_MARCA_BASE ="postgres";
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("empleadoasiento", "codigo", 1);
        tab_tabla.setRows(15);
        tab_tabla.dibujar();
        
        
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
        con_postgres.guardarPantalla(); 
    }

    @Override
    public void eliminar() {
     tab_tabla.eliminar();   
    }
    
}
