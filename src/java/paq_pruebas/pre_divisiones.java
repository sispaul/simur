/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.faces.event.AjaxBehaviorEvent;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
public class pre_divisiones extends Pantalla {

    private Conexion con_postgres = new Conexion();
    private Tabla tab_tabla = new Tabla();

    public pre_divisiones() {

        con_postgres.setUnidad_persistencia("otraBase");
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("bodt_bodega", "ide_bodega", 1);
        tab_tabla.setTipoFormulario(true);
        //Ejecutar metodo en la columna marca
        tab_tabla.getColumna("marca").setMetodoChange("cambioMarca");

        tab_tabla.getGrid().setColumns(6);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);


        //Dividir en 2 

        Division div = new Division();
        div.dividir2(null, null, "30%", "h");

        Division div1 = new Division();
        div1.dividir2(div, pat_panel, "50%", "V");

        agregarComponente(div1);

    }

    /**
     * Pone el valor de marca en el campo modelo
     *
     * @param evt evento del cliente
     */
    public void cambioMarca(AjaxBehaviorEvent evt) {
        //1) 
        tab_tabla.modificar(evt);

        ////PRGRAMAR LA FUNCION
        tab_tabla.setValor("modelo", tab_tabla.getValor("marca"));
        //actualizar campo



        //Ejecutar sentecias directamente

        String str_sql = "UPDATE bodt_bodega SET modelo='" + tab_tabla.getValor("modelo") + "' where ide_bodega=" + tab_tabla.getValor("ide_bodega");
        System.out.println(str_sql);
        con_postgres.agregarSql(str_sql);
        con_postgres.guardarPantalla();
        utilitario.addUpdateTabla(tab_tabla, "modelo", "");

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
