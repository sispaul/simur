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
     private Conexion 
            con_sql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres,//Cnexion a la base de postgres 2014
            con_ciudadania; //Conexion a la base de ciudadania
     
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public TablaGenerica getVehiculo(String placa) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT \n" +
                "v.MVE_SECUENCIAL,\n" +
                "v.MVE_PLACA,\n" +
                "v.MVE_TIPO_COMBUSTIBLE,\n" +
                "v.MVE_CONDUCTOR,\n" +
                "(m.MVMARCA_DESCRIPCION+'  '+o.MVMODELO_DESCRIPCION+'  '+r.MVERSION_DESCRIPCION) as descripcion\n" +
                "FROM MVVEHICULO v,\n" +
                "MVMARCA m,\n" +
                "MVMODELO o,\n" +
                "MVTIPO t ,\n" +
                "MVVERSION r\n" +
                "WHERE v.MVE_MARCA = m.MVMARCA_ID \n" +
                "AND v.MVE_TIPO =  t.MVTIPO_ID \n" +
                "AND v.MVE_MODELO = o.MVMODELO_ID \n" +
                "AND t.MVMARCA_ID = m.MVMARCA_ID \n" +
                "AND o.MVTIPO_ID = t.MVTIPO_ID \n" +
                "AND r.MVMODELO_ID = o.MVMODELO_ID \n" +
                "AND v.MVE_PLACA = '"+placa+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public void deleteMarca(String anti){
    String au_sql="DELETE FROM MVLISTA WHERE LIS_NOMBRE ='"+anti+"' and TAB_CODIGO = 'marca'";
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteMarcas(Integer anti){
    String au_sql="delete from MVMARCA where MVMARCA_ID ="+anti;
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteTipos(Integer anti){
    String au_sql="delete from MVTIPO where MVTIPO_ID ="+anti;
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteModelos(Integer anti){
    String au_sql="delete from MVTIPO where MVTIPO_ID ="+anti;
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteversion(Integer anti){
    String au_sql="delete from MVTIPO where MVTIPO_ID ="+anti;
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteaccesorio(String anti){
    String au_sql="update MVDETALLEVEHICULO set MDV_ESTADO = 'DE BAJA' where MDV_CODIGO = '"+anti+"'";
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteaccesorios(String anti){
    String au_sql="update MVDETASIGNACION set MDA_ESTADO = 'DE BAJA' where MDA_CODIGO = '"+anti+"'";
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void updateFecha(String anti,Integer codigo,String observacion){
    String au_sql="update MVVEHICULO set MVE_OBSERVACIONES = '"+observacion+"',MVE_FECHA_BORRADO ="+utilitario.getFormatoFechaSQL(anti)+" where MVE_SECUENCIAL ="+codigo;
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void updateSolicitud(Integer codigo){
    String au_sql="update MVCABSOLICITUD\n" +
            "set MSC_ESTADO_TRAMITE = 'TERMINADA'\n" +
            "from (SELECT\n" +
            "s.MSC_SECUENCIAL\n" +
            "FROM\n" +
            "dbo.MVVEHICULO v\n" +
            "INNER JOIN dbo.MVCABSOLICITUD s ON s.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
            "where v.MVE_SECUENCIAL="+codigo+" and s.MSC_ESTADO_TRAMITE = 'SOLICITUD') AS d\n" +
            "where MVCABSOLICITUD.MSC_SECUENCIAL=d.MSC_SECUENCIAL";
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
    }
    
    public void deleteParam(String anti,String mensaje,String depen){
    String au_sql="DELETE FROM MVLISTA WHERE LIS_NOMBRE ='"+anti+"' and TAB_CODIGO = '"+mensaje+"' and DEPENDENCI='"+depen+"'";
    con_mantenimiento();
    con_manauto.ejecutarSql(au_sql);
    con_manauto.desconectar();
    con_manauto = null;
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
    
    public TablaGenerica getKilometraje(String placa) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "MVE_SECUENCIAL,\n" +
                "MVE_PLACA,\n" +
                "MVE_KILOMETRAJE,\n" +
                "MVE_TIPO_COMBUSTIBLE,\n" +
                "MVE_HOROMETRO,\n" +
                "MVE_CAPACIDAD_TANQUE_COMBUSTIBLE\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_PLACA = '"+placa+"'");
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
        return tab_persona;
    }
    
    public void getAccesorios(String id,String codigo){
        String parametro ="insert into MVDETASIGNACION (MAV_SECUENCIAL,MAV_ESTADO_ASIGNACION,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO)\n" +
                "select '"+id+"' as secuencial,1 as estado,mdv_detalle,mdv_cantidad,mdv_estado from MVDETALLEVEHICULO where MVE_SECUENCIAL = '"+codigo+"'";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public TablaGenerica setVerificar(Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT top 1 MVE_SECUENCIAL,MAV_SECUENCIAL,MAV_DEPARTAMENTO,MAV_NOMBRE_COND,MAV_DIRECCION_COND,\n" +
                "MAV_ESTADO_ASIGNACION,MAV_ESTADO_TRAMITE,MAV_FECHAASIGNACION,MAV_AUTORIZA\n" +
                "FROM MVASIGNARVEH\n" +
                "where MVE_SECUENCIAL = "+tipo+" and MAV_ESTADO_ASIGNACION = 1\n" +
                "order by MAV_SECUENCIAL desc");
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }  
    
    public void actDescargo(Integer tipo,String usu,String fecha){
        String str_sql4 = "UPDATE MVASIGNARVEH\n" +
                "SET MAV_ESTADO_ASIGNACION= '2',\n" +
                "MAV_ESTADO_TRAMITE='DESCARGO',\n" +
                "MAV_LOGINACTUALI='"+usu+"',\n" +
                "MAV_FECHAACTUALI="+utilitario.getFormatoFechaSQL(utilitario.getFechaHoraActual())+"\n" +
                "where MAV_SECUENCIAL ="+tipo;
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
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
    
    public TablaGenerica getComparacion(Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "ide_abastecimiento_combustible,\n" +
                "NUMERO_ABASTECIMIENTO,\n" +
                "NUMERO_VALE_ABASTECIMIENTO\n" +
                "FROM\n" +
                "MVABASTECIMIENTO_COMBUSTIBLE\n" +
                "where ide_abastecimiento_combustible= "+tipo);
        tab_persona.ejecutarSql();
       con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }
    
    public TablaGenerica getDatos(Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MVE_SECUENCIAL,MVE_MOTOR,MVE_CHASIS,MVE_MARCA,MVE_PLACA,MVE_TIPO,MVE_MODELO,MVE_COLOR,MVE_ANO,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO where MVE_SECUENCIAL ="+tipo);
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }
    
    public TablaGenerica getVerificar(Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MVE_SECUENCIAL, count(dbo.MVASIGNARVEH.MAV_ESTADO_ASIGNACION) as valor\n" +
                "FROM MVASIGNARVEH where MVE_SECUENCIAL = "+tipo+" and MAV_ESTADO_ASIGNACION = 1 GROUP BY MVE_SECUENCIAL");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
       con_manauto = null;
       return tab_persona;
    }
    
    public TablaGenerica datosExtraer(Integer tipo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT a.MAV_SECUENCIAL,v.MVE_PLACA,v.MVE_MARCA,v.MVE_MODELO,v.MVE_ANO,v.MVE_COLOR,a.MAV_NOMEMPLEA\n" +
                "FROM MVASIGNARVEH a INNER JOIN MVVEHICULO  v ON a.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "where a.MAV_SECUENCIAL ="+tipo);
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
        
    public String listaMax(String placa) {
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(NUMERO_ABASTECIMIENTO) is null then '0' when count(NUMERO_ABASTECIMIENTO)is not null then count(NUMERO_ABASTECIMIENTO) end) AS maximo\n" +
                 "from MVABASTECIMIENTO_COMBUSTIBLE\n" +
                 "where PLACA_VEHICULO = '"+placa+"' and ANIO = '"+utilitario.getAnio(utilitario.getFechaActual())+"' and PERIODO ='"+utilitario.getMes(utilitario.getFechaActual())+"'");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
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
    
    public void ActHoras(String conduc,Double hora){
        String str_sql4 = "update MVVEHICULO\n" +
                "set MVE_HOROMETRO = "+hora+"\n" +
                "where MVE_PLACA = '"+conduc+"'";
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void actDescargo(Integer tipo,String motivo,String usu,String fecha){
        String str_sql4 = "UPDATE MVASIGNARVEH\n" +
                "SET MAV_ESTADO_ASIGNACION= '0',\n" +
                "MAV_ESTADO_TRAMITE='DESCARGO',\n" +
                "MAV_MOTIVO='"+motivo+"',\n" +
                "MAV_LOGINACTUALI='"+usu+"',\n" +
                "MAV_FECHAACTUALI="+utilitario.getFormatoFechaSQL(utilitario.getFechaHoraActual())+"\n" +
                "where MAV_SECUENCIAL ="+tipo;
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
    }
        
    public void ActRegistro(Integer ide,String vale,Integer tipo,String fecha,String hora,Integer kilo,Double galon,Double total,String placa,String desc,String cond,
            String ci,String usu){
        String str_sql4 = "UPDATE MVABASTECIMIENTO_COMBUSTIBLE\n" +
                "set IDE_TIPO_COMBUSTIBLE="+tipo+",\n" +
                "FECHA_ABASTECIMIENTO='"+fecha+"',\n" +
                "HORA_ABASTECIMIENTO='"+hora+"',\n" +
                "KILOMETRAJE="+kilo+",\n" +
                "GALONES="+galon+",\n" +
                "TOTAL="+total+",\n" +
                "PLACA_VEHICULO='"+placa+"',\n" +
                "DESCRIPCION_VEHICULO='"+desc+"',\n" +
                "CONDUCTOR='"+cond+"',\n" +
                "CI_CONDUCTOR='"+ci+"',\n" +
                "FECHA_ACTUALIZACION='"+utilitario.getFechaActual()+"',\n" +
                "USU_ACTUALIZACION='"+usu+"'\n" +
                "where ide_abastecimiento_combustible ="+ide+" and NUMERO_ABASTECIMIENTO = '"+vale+"'";
        con_mantenimiento();
        con_manauto.ejecutarSql(str_sql4);
        con_manauto.desconectar();
        con_manauto = null;
    }
       
    public String ParametrosMax(String dependencia) {
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(lis_id) is null then '0' when count(lis_id)is not null then count(lis_id) end) AS maximo\n" +
                 "from mvlista where tab_codigo = '"+dependencia+"'");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
    
    public String ParametrosAcc() {
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(MDV_CODIGO) is null then '0' when count(MDV_CODIGO)is not null then count(MDV_CODIGO) end) AS maximo\n" +
                 "from MVDETALLEVEHICULO");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
    
    public String ParametrosAccE() {
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(MDA_CODIGO) is null then '0' when count(MDA_CODIGO)is not null then count(MDA_CODIGO) end) AS maximo\n" +
                 "from MVDETASIGNACION");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
    
    public String SecuencialCab() {
         con_mantenimiento();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_mantenimiento();
         tab_consulta.setConexion(con_manauto);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(msc_solicitud) is null then '0' when count(msc_solicitud)is not null then count(msc_solicitud) end) AS maximo\n" +
                 "from MVCABSOLICITUD");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
    }
    
    public TablaGenerica ParametrosID(String dependencia) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT LIS_ID,DEPENDENCI FROM MVLISTA where DEPENDENCI = '"+dependencia+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
  }
    
    public TablaGenerica get_DuplicaDatos(String nombre,String codigo,String dependencia) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT LIS_ID,LIS_NOMBRE,TAB_CODIGO,DEPENDENCI\n" +
                "FROM MVLISTA\n" +
                "WHERE LIS_NOMBRE='"+nombre+"' AND TAB_CODIGO='"+codigo+"' AND DEPENDENCI ='"+dependencia+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_DuplicarDato(String nombre,Integer codigo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM dbo.MVTIPO where MVMARCA_ID = "+codigo+" and MVTIPO_DESCRIPCION like '"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_DuplicamDato(String nombre,Integer codigo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "MVMODELO_ID,\n" +
                "MVMODELO_DESCRIPCION\n" +
                "FROM\n" +
                "dbo.MVMODELO\n" +
                "where MVMODELO_DESCRIPCION ='"+nombre+"' and  MVTIPO_ID="+codigo);
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_DuplicavDato(String nombre,Integer codigo) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "MVERSION_ID,\n" +
                "MVERSION_DESCRIPCION\n" +
                "FROM MVVERSION\n" +
                "where MVMODELO_ID ="+codigo+" and MVERSION_DESCRIPCION='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_DuplicaDato(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        con_mantenimiento();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "MVMARCA_ID,\n" +
                "MVMARCA_DESCRIPCION,\n" +
                "MVMARCA_ESTADO\n" +
                "FROM\n" +
                "MVMARCA\n" +
                "where MVMARCA_DESCRIPCION like '"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ExtraDatos(String nombre,String codigo,String dependencia) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT LIS_ID,LIS_NOMBRE,TAB_CODIGO,DEPENDENCI\n" +
                "FROM MVLISTA\n" +
                "WHERE LIS_ID='"+nombre+"' AND TAB_CODIGO='"+codigo+"' AND DEPENDENCI ='"+dependencia+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }

    public TablaGenerica get_ExDatos(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT LIS_ID, LIS_NOMBRE\n" +
                "FROM MVLISTA WHERE TAB_CODIGO = 'ACCES'AND LIS_ESTADO = 1 AND LIS_NOMBRE='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public void getParametros(String id,String nombre,String codigo,String dependencia,String login){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"',"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+")";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametrom(String nombre,String login){
        String parametro ="insert into MVMARCA (MVMARCA_DESCRIPCION,MVMARCA_ESTADO,MVMARCA_FECHAING,MVMARCA_LOGININ)\n" +
                "values ('"+nombre+"',1,"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+login+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametrot(String nombre,String login,Integer marca){
        String parametro ="insert into MVTIPO (MVMARCA_ID,MVTIPO_DESCRIPCION,MVTIPO_ESTADO,MVTIPO_FECHAING,MVTIPO_LOGININ)\n" +
                "values ("+marca+",'"+nombre+"',1,"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+login+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametromo(String nombre,String login,Integer tipo){
        String parametro ="insert into MVMODELO(MVTIPO_ID,MVMODELO_DESCRIPCION,MVMODELO_ESTADO,MVMODELO_FECHAING,MVMODELO_LOGININ)\n" +
                "values ("+tipo+",'"+nombre+"',1,"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+login+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    public void getParametrove(String nombre,String login,Integer modelo){
        String parametro ="insert into MVVERSION(MVMODELO_ID,MVERSION_DESCRIPCION,MVERSION_ESTADO,MVERSION_FECHAING,MVERSION_LOGININ)\n" +
                "values ("+modelo+",'"+nombre+"',1,"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+login+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametacce(String nombre,String numero,String cantidad,String estado,String codigo){
        String parametro ="insert into MVDETALLEVEHICULO (MVE_SECUENCIAL,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO,MDV_CODIGO)\n" +
                "values ('"+nombre+"','"+numero+"','"+cantidad+"','"+estado+"','"+codigo+"')";
        con_mantenimiento();
        System.err.println(parametro);
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametacces(String nombre,String numero,String cantidad,String estado,String codigo){
        String parametro ="insert into MVDETASIGNACION (MAV_SECUENCIAL,MAV_ESTADO_ASIGNACION,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO,MDA_CODIGO)\n" +
                "values ('"+nombre+"','1','"+numero+"','"+cantidad+"','"+estado+"','"+codigo+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getNumero(String id,String nombre,String codigo,String dependencia,String login){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"',"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+")";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getParametros1(String id,String nombre,String codigo,String dependencia,String login,String cantidad){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO,LIS_CANTIDAD)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"',"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+cantidad+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
        
    public void getMVDetalle(String codigo,String detalle,Double cantidad,String estado){
        String parametro ="insert into MVDETALLEVEHICULO (MVE_SECUENCIAL,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO)\n" +
                "values('"+codigo+"','"+detalle+"',"+cantidad+",'"+estado+"')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public void getMVDetalleASI(String codigo,String detalle,Double cantidad,String estado){
        String parametro ="insert into MVDETASIGNACION (MAV_SECUENCIAL,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO,MAV_ESTADO_ASIGNACION)\n" +
                "values('"+codigo+"','"+detalle+"',"+cantidad+",'"+estado+"','1')";
        con_mantenimiento();
        con_manauto.ejecutarSql(parametro);
        con_manauto.desconectar();
        con_manauto = null;
    }
    
    public TablaGenerica getDirec(Integer tipo) {
       con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT DISTINCT srh_empleado.cod_direccion,srh_direccion.nombre_dir\n" +
                "FROM srh_empleado, srh_direccion where srh_empleado.cod_direccion = srh_direccion.cod_direccion and srh_direccion.estado_dir= 'ACTIVA' and srh_empleado.cod_direccion ="+tipo);
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    }
        
    public TablaGenerica getCarg(Integer tipo,Integer dir) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT DISTINCT srh_empleado.cod_cargo,srh_cargos.nombre_cargo\n" +
                "FROM srh_empleado INNER JOIN srh_cargos ON srh_empleado.cod_cargo = srh_cargos.cod_cargo\n" +
                "where srh_empleado.cod_direccion = "+dir+" and srh_empleado.cod_cargo="+tipo);
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    }
    
    public TablaGenerica getResp(Integer tipo,Integer dir,String cod) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT cod_empleado,nombres FROM srh_empleado\n" +
                "where estado = 1 and cod_direccion = "+dir+" and cod_cargo = "+tipo+" and cod_empleado='"+cod+"'");
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
    
    public TablaGenerica getChofer(String cedula) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT cod_empleado, cedula_pass,nombres, 1 as activo\n" +
                "FROM srh_empleado\n" +
                "where cod_empleado ='"+cedula+"' and estado = 1\n" +
                "order by nombres");
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    }
    
    public TablaGenerica getActivos(String codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT ide_activo,nombre||'; '||marca||'; MODELO: '||modelo||'; SERIE: '||serie||'; RESPONSABLE: '||nom_responsable as descripcion\n" +
                "FROM afi_activos where codigo = '"+codigo+"'");
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    
    }
    
        ///solicitud de mantenimiento
    
    public TablaGenerica get_ExDatosCom(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT c.MSC_SECUENCIAL,v.MVE_PLACA,v.MVE_MARCA,c.MSC_CONDUCTOR,v.MVE_ANO,v.MVE_MOTOR,v.MVE_CHASIS\n" +
                "FROM MVVEHICULO AS v   \n" +
                "INNER JOIN dbo.MVCABSOLICITUD c ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "WHERE v.MVE_ESTADO_REGISTRO = 'activo' and c.MSC_SECUENCIAL ='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ExDatosSoli(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT v.MVE_SECUENCIAL,v.MVE_MOTOR,v.MVE_CHASIS,v.MVE_PLACA,v.MVE_ANO,v.MVE_KILOMETRAJE,v.MVE_CONDUCTOR,\n" +
                "(SELECT top 1 MSC_FECHA\n" +
                "FROM MVCABSOLICITUD where MVE_SECUENCIAL= v.MVE_SECUENCIAL order by MSC_SECUENCIAL desc) as FECHA\n" +
                "FROM MVVEHICULO v WHERE v.MVE_SECUENCIAL ='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
   
    public TablaGenerica get_ExResgistroSoli(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT\n" +
                "v.MVE_SECUENCIAL,\n" +
                "v.MVE_PLACA,\n" +
                "c.MSC_TIPOSOL,\n" +
                "c.MSC_ESTADO_TRAMITE\n" +
                "FROM MVVEHICULO v\n" +
                "INNER JOIN MVCABSOLICITUD c ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "where v.MVE_PLACA = '"+nombre+"' and c.MSC_ESTADO_TRAMITE = 'SOLICITUD' and c.MSC_ESTADO_REGISTRO ='ACTIVO'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ValiAccesorio(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO FROM MVDETALLEVEHICULO WHERE MVE_SECUENCIAL='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ValiAcces(String nombre) {
        con_mantenimiento();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_manauto);
        tab_persona.setSql("SELECT MAV_SECUENCIAL,MDA_DETALLE FROM MVDETASIGNACION WHERE MAV_SECUENCIAL ='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_manauto.desconectar();
        con_manauto = null;
        return tab_persona;
    }
    
    public void ActuReg(Integer ide,String vale){
        String str_sql4 = "update SIS_BLOQUEO\n" +
                "set MAXIMO_BLOQ ="+ide+"\n" +
                "where TABLA_BLOQ = '"+vale+"'";
        con_sigag();
        con_sql.ejecutarSql(str_sql4);
        con_sql.desconectar();
        con_sql = null;
    }
    
    public String RegisMaxSis() {
         con_sigag();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_sigag();
         tab_consulta.setConexion(con_sql);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when max(IDE_BLOQ) is null then '0' when max(IDE_BLOQ)is not null then max(IDE_BLOQ) end) as maximo\n" +
                 "from SIS_BLOQUEO where Tabla_BLOQ = 'MVDETASIGNACION'");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
    } 

    
    private void con_sigag(){
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }
    
    private void con_mantenimiento(){
        if(con_manauto == null){
            con_manauto = new Conexion();
            con_manauto.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        }
    }
    
    private void con_postgresql(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
   
}
