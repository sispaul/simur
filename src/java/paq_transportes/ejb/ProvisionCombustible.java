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
 private Conexion 
            con_sql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres//Cnexion a la base de postgres 2014
;
private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

//busquedad e vehiculo en manauto/mvvehiculo
    public TablaGenerica getVehiculo(String placa) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
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
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica getConductor(Integer ide) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MVE_SECUENCIAL,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_SECUENCIAL ="+ide);
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    //extraer los datos del conductor desde produccion2014/srh_empleado
    public TablaGenerica getConductores(String nombre) {
        con_postgresql();
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
        con_postgresql();
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
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "IDE_TIPO_COMBUSTIBLE,\n" +
                "DESCRIPCION_COMBUSTIBLE,\n" +
                "VALOR_GALON\n" +
                "FROM\n" +
                "MVTIPO_COMBUSTIBLE\n" +
                "where IDE_TIPO_COMBUSTIBLE ="+tipo);
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica getKilometraje(String placa) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "MVE_SECUENCIAL,\n" +
                "MVE_PLACA,\n" +
                "MVE_KILOMETRAJE,\n" +
                "MVE_TIPO_COMBUSTIBLE,\n" +
                "MVE_CAPACIDAD_TANQUE_COMBUSTIBLE\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_PLACA = '"+placa+"'");
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica getInsertar(Integer ide) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "c.IDE_ORDEN_CONSUMO,\n" +
                "o.IDE_CALCULO_CONSUMO\n" +
                "FROM\n" +
                "dbo.MVORDEN_CONSUMO c\n" +
                "INNER JOIN dbo.MVCALCULO_CONSUMO o ON c.IDE_ORDEN_CONSUMO = o.IDE_ORDEN_CONSUMO\n" +
                "WHERE\n" +
                "c.IDE_ORDEN_CONSUMO = "+ide+" and o.IDE_CALCULO_CONSUMO is not null");
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }
    
    public TablaGenerica getNick(Integer ide,Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "IDE_ORDEN_CONSUMO,\n" +
                "AUTORIZA,\n" +
                "NUMERO_ORDEN\n" +
                "FROM\n" +
                "MVORDEN_CONSUMO\n" +
                "where IDE_ORDEN_CONSUMO = "+ide+" and NUMERO_ORDEN="+tipo);
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }
    
    public TablaGenerica getUsuario(String nick) {
        con_sigag();
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
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
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
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void ActKilometraje(String conduc,Double kilom){
        String str_sql4 = "update MVVEHICULO\n" +
                "set MVE_KILOMETRAJE = "+kilom+"\n" +
                "where MVE_PLACA = '"+conduc+"'";
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getConsumo(Integer orden,String date,String hora,Double kilom,Double galon,Double total,Integer tipo,String placa,String usu){

        String nomina ="insert into MVCALCULO_CONSUMO(IDE_ORDEN_CONSUMO,FECHA_ABASTECIMIENTO,HORA_ABASTECIMIENTO,KILOMETRAJE,GALONES,TOTAL,FECHA_DIGITACION,\n" +
                "HORA_DIGITACION,USU_DIGITACION,IDE_TIPO_COMBUSTIBLE,PLACA_VEHICULO)\n" +
                "values ("+orden+",'"+date+"','"+hora+"',"+kilom+","+galon+","+total+",'"+utilitario.getFechaActual()+"','"+utilitario.getHoraActual()+"','"+usu+"',"+tipo+",'"+placa+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(nomina);
        con_manauto.desconectar();
        con_manauto = null;
        
    }
    ///conexion manauto 
    private void con_sigag(){
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia("recursojdbc");
        }
    }
    
    private void con_mantenimiento(){
        if(con_manauto == null){
            con_manauto = new Conexion();
            con_manauto.setUnidad_persistencia("poolSqlmanAuto");
        }
    }
    
    private void con_postgresql(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia("poolPostgres");
        }
    }

}
