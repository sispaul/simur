/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otros.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class AbastecimientoCombustible {

    private Conexion conexion,con_postgres,con_sql;
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public TablaGenerica getVehiculo(String placa) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                "MVE_SECUENCIAL,\n" +
                "(MVE_MARCA+','+MVE_MODELO+',COLOR:'+MVE_COLOR+','+MVE_VERSION)as descripcion,\n" +
                "MVE_PLACA,\n" +
                "MVE_TIPO_COMBUSTIBLE,\n" +
                "MVE_CONDUCTOR\n" +
                "FROM \n" +
                "MVVEHICULO\n" +
                "where MVE_PLACA ='"+placa+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
    
        //extraer los datos del conductor desde produccion2014/srh_empleado
    public TablaGenerica getConductores(String nombre) {
        conect();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n" +
                "cod_empleado,\n" +
                "cedula_pass,\n" +
                "nombres,\n" +
                "cod_cargo\n" +
                "FROM\n" +
                "srh_empleado\n" +
                "where nombres = '"+nombre+"'");
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    }
    
    public TablaGenerica getKilometraje(String placa) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                "MVE_SECUENCIAL,\n" +
                "MVE_PLACA,\n" +
                "MVE_KILOMETRAJE,\n" +
                "MVE_TIPO_COMBUSTIBLE,\n" +
                "MVE_CAPACIDAD_TANQUE_COMBUSTIBLE\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_PLACA = '"+placa+"'");
        tab_persona.ejecutarSql();
       conexion.desconectar();
       conexion = null;
        return tab_persona;
    }
    
    //extrae datos de catalogo de combustibles SIGAG/trans_tipo_combustible
    public TablaGenerica getCombustible(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                "IDE_TIPO_COMBUSTIBLE,\n" +
                "DESCRIPCION_COMBUSTIBLE,\n" +
                "VALOR_GALON\n" +
                "FROM\n" +
                "MVTIPO_COMBUSTIBLE\n" +
                "where IDE_TIPO_COMBUSTIBLE ="+tipo);
        tab_persona.ejecutarSql();
       conexion.desconectar();
       conexion = null;
        return tab_persona;
    }
    
    public String listaMax() {
         conectar();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(conexion);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when max(NUMERO_ABASTECIMIENTO) is null then '0' when max(NUMERO_ABASTECIMIENTO)is not null then max(NUMERO_ABASTECIMIENTO) end) AS maximo\n" +
                 "from MVABASTECIMIENTO_COMBUSTIBLE");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
    
    public void ActKilometraje(String conduc,Double kilom){
        String str_sql4 = "update MVVEHICULO\n" +
                "set MVE_KILOMETRAJE = "+kilom+"\n" +
                "where MVE_PLACA = '"+conduc+"'";
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
    }
    
    private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        }
    }
    private void conect() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
    }
    private void con_sql() {
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
            con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        }
    }  
}
