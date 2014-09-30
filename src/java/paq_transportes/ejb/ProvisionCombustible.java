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
    
    public TablaGenerica getConductor(Integer ide) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT MVE_SECUENCIAL,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_SECUENCIAL ="+ide);
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
    
    public TablaGenerica getMes(Integer periodo) {
        conect();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo = "+periodo);
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
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
    
    public TablaGenerica getNick(Integer ide,Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
"IDE_ORDEN_CONSUMO,\n" +
"AUTORIZA,\n" +
"NUMERO_ORDEN\n" +
"FROM\n" +
"MVORDEN_CONSUMO\n" +
"where IDE_ORDEN_CONSUMO = "+ide+" and NUMERO_ORDEN="+tipo);
        tab_persona.ejecutarSql();
       conexion.desconectar();
       conexion = null;
        return tab_persona;
    }
    
    public TablaGenerica getUsuario(String nick) {
        con_sql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_sql);
        tab_persona.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF\n" +
                "and NICK_USUA = '"+nick+"'");
        tab_persona.ejecutarSql();
       con_sql.desconectar();
       con_sql = null;
        return tab_persona;
    }
    
    public String listaMax() {
         conectar();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(conexion);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when max(numero_orden) is null then '0' when max(numero_orden)is not null then max(numero_orden) end) AS maximo\n" +
                 "from mvorden_consumo");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
 
    public void ActualizaAnticipo(Integer tipo,String conduc,Double gal,Double valor,String usu,Integer vale,Integer numero){
        String str_sql4 = "UPDATE MVORDEN_CONSUMO\n" +
                "SET IDE_TIPO_COMBUSTIBLE ="+tipo+",\n" +
                "CONDUCTOR='"+conduc+"',\n" +
                "GALONES="+gal+",\n" +
                "TOTAL="+valor+",\n" +
                "FECHA_MODIFICACION = '"+utilitario.getFechaHoraActual()+"',\n" +
                "MODIFICA = '"+usu+"'\n" +
                "WHERE IDE_ORDEN_CONSUMO ="+vale+" AND NUMERO_VALE ="+numero;
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
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
    
    public void getConsumo(Integer orden,String date,String hora,Double kilom,Double galon,Double total,Integer tipo,String placa,String usu){

        String nomina ="insert into MVCALCULO_CONSUMO(IDE_ORDEN_CONSUMO,FECHA_ABASTECIMIENTO,HORA_ABASTECIMIENTO,KILOMETRAJE,GALONES,TOTAL,FECHA_DIGITACION,\n" +
                "HORA_DIGITACION,USU_DIGITACION,IDE_TIPO_COMBUSTIBLE,PLACA_VEHICULO)\n" +
                "values ("+orden+",'"+date+"','"+hora+"',"+kilom+","+galon+","+total+",'"+utilitario.getFechaActual()+"','"+utilitario.getHoraActual()+"','"+usu+"',"+tipo+",'"+placa+"')";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
        
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
