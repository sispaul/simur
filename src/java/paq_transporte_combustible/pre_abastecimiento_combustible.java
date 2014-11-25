/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_combustible;

import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_abastecimiento_combustible extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //Para tabla de
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    public pre_abastecimiento_combustible() {
        
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("REGISTRO DE ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(pan_opcion);
        dibujarAbastecimiento();
    }

    public void dibujarAbastecimiento(){
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvabastecimiento_combustible", "ide_abastecimiento_combustible", 1);
        tab_tabla.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_tabla.getColumna("placa_vehiculo").setMetodoChange("busPlaca");
        tab_tabla.getColumna("ide_tipo_combustible").setLectura(true);
        tab_tabla.getColumna("descripcion_vehiculo").setLectura(true);
        tab_tabla.getColumna("total").setLectura(true);
        tab_tabla.getColumna("ide_abastecimiento_combustible").setVisible(false);
        tab_tabla.getColumna("ci_conductor").setVisible(false);
        tab_tabla.getColumna("fecha_digitacion").setVisible(false);
        tab_tabla.getColumna("hora_digitacion").setVisible(false);
        tab_tabla.getColumna("usu_digitacion").setVisible(false);
        tab_tabla.getColumna("estado").setVisible(false);
        tab_tabla.getColumna("fecha_actualizacion").setVisible(false);
        tab_tabla.getColumna("usu_actualizacion").setVisible(false);
        tab_tabla.getColumna("anio").setVisible(false);
        tab_tabla.getColumna("periodo").setVisible(false);
        tab_tabla.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_tabla.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir1(ptt);
        agregarComponente(div);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    public void busPlaca(){
        
    }
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }
    
}
