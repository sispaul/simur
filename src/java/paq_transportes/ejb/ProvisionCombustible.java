/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class ProvisionCombustible {
private Conexion conexion,con_postgres,con_sql;
private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

//busquedad e vehiculo en manauto/mvvehiculo
    public TablaGenerica getVehiculo(String placa) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                "MVE_SECUENCIAL,\n" +
                "(MVE_MARCA+','+MVE_MODELO+',COLOR:'+MVE_COLOR+','+MVE_VERSION)as descripcion,\n" +
                "MVE_PLACA,\n" +
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
    public TablaGenerica getConductor(String nombre) {
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
    
    //extrae datos de catalogo de combustibles SIGAG/trans_tipo_combustible
    public TablaGenerica getCombustible(Integer tipo) {
        con_sql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_sql);
        tab_persona.setSql("SELECT\n" +
                "IDE_TIPO_COMBUSTIBLE,\n" +
                "DESCRIPCION_COMBUSTIBLE,\n" +
                "VALOR_GALON\n" +
                "FROM\n" +
                "TRANS_TIPO_COMBUSTIBLE\n" +
                "where IDE_TIPO_COMBUSTIBLE ="+tipo);
        tab_persona.ejecutarSql();
       con_sql.desconectar();
       con_sql = null;
        return tab_persona;
    }
    
    public String listaMax() {
         con_sql();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_sql();
         tab_consulta.setConexion(con_sql);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when max(numero_vale) is null then '0' when max(numero_vale)is not null then max(numero_vale) end) AS maximo\n" +
                 "from trans_vale_consumo");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
 

private void conectar() {
    if (conexion == null) {
        conexion = new Conexion();
        conexion.NOMBRE_MARCA_BASE="sqlserver";
        conexion.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
    }
}

private void con_sql() {
    if (con_sql == null) {
        con_sql = new Conexion();
        con_sql.NOMBRE_MARCA_BASE="sqlserver";
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
    }
}

private void conect() {
    if (con_postgres == null) {
        con_postgres = new Conexion();
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
    }
}


}
