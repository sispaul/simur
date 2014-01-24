/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
public class pre_otrabasedj extends Pantalla {

    private Conexion con_postgres = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();

    public pre_otrabasedj() {
        //1) asignar persistencia con la que va a trabajar
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        //2) le dcimos con que marca de base va a trabar, soporta oracle,postgres,sqlserver,mysql
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_tabla.setId("tab_tabla");
        //3 asignamos la conexion 
        tab_tabla.setConexion(con_postgres);

        tab_tabla.setTabla("empleadoasiento", "codigo", 1);
        tab_tabla.setRows(15);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        agregarComponente(pat_panel);

        ///configurar reporte

        bar_botones.agregarReporte();
        agregarComponente(rep_reporte);

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getNombre().equals("Reporte Postgres")) {
            rep_reporte.cerrar();
            sef_formato.setConexion(con_postgres);
            sef_formato.setSeleccionFormatoReporte(null, rep_reporte.getPath());
            sef_formato.dibujar();
        }
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }
}
