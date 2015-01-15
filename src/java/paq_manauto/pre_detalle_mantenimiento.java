/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_detalle_mantenimiento extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Declaraciones de tablas
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    
    //Declaracion Texto
    private Texto ttotal = new Texto();
    
    public pre_detalle_mantenimiento() {
        ttotal.setId("ttotal");
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_postgres);
        tab_cabecera.setSql("SELECT mca_codigo,mve_secuencial,mca_secuencial,mca_cod_responsable,mca_cod_proveedor,mca_cod_autoriza,mca_detalle,mca_observacion,mca_tipo_mantenimiento\n" +
                "FROM mvcab_mantenimiento where mca_estado_tramite ='Pendiente'");
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);
        
        Boton bot_end = new Boton();
        bot_end.setValue("Terminar Solictud");
        bot_end.setIcon("ui-icon-disk");
        bot_end.setMetodo("termina");
        bar_botones.agregarBoton(bot_end);
        
        Grid gri_total = new Grid();
        gri_total.setColumns(6);
        gri_total.getChildren().add(bot_end);
        gri_total.getChildren().add(new Etiqueta("Valor Total: "));
        gri_total.getChildren().add(ttotal);
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setTabla("mvdetalle_mantenimiento", "mde_codigo", 1);
        tab_detalle.setRows(10);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.getChildren().add(gri_total);
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir2(ptc, ptd,"40%", "H");
        agregarComponente(div);
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
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

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
    
}
