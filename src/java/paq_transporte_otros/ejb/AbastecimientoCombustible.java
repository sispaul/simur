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
    
    public void deleteMarca(String anti){
    String au_sql="DELETE FROM MVLISTA WHERE LIS_NOMBRE ='"+anti+"' and TAB_CODIGO = 'marca'";
    conectar();
    conexion.ejecutarSql(au_sql);
    conexion.desconectar();
    conexion = null;
    }
    
    public void deleteParam(String anti,String mensaje,String depen){
    String au_sql="DELETE FROM MVLISTA WHERE LIS_NOMBRE ='"+anti+"' and TAB_CODIGO = '"+mensaje+"' and DEPENDENCI='"+depen+"'";
    conectar();
    conexion.ejecutarSql(au_sql);
    conexion.desconectar();
    conexion = null;
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
                "MVE_HOROMETRO,\n" +
                "MVE_CAPACIDAD_TANQUE_COMBUSTIBLE\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_PLACA = '"+placa+"'");
        tab_persona.ejecutarSql();
       conexion.desconectar();
       conexion = null;
        return tab_persona;
    }
    
        public void getAccesorios(String id,String codigo){
        String parametro ="insert into MVDETASIGNACION (MAV_SECUENCIAL,MAV_ESTADO_ASIGNACION,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO)\n" +
                "select '"+id+"' as secuencial,1 as estado,mdv_detalle,mdv_cantidad,mdv_estado from MVDETALLEVEHICULO where MVE_SECUENCIAL = '"+codigo+"'";
        conectar();
        conexion.ejecutarSql(parametro);
        conexion.desconectar();
        conexion = null;
    }
    
            public TablaGenerica setVerificar(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT top 1 MVE_SECUENCIAL,MAV_SECUENCIAL,MAV_DEPARTAMENTO,MAV_NOMBRE_COND,MAV_DIRECCION_COND,\n" +
                "MAV_ESTADO_ASIGNACION,MAV_ESTADO_TRAMITE,MAV_FECHAASIGNACION,MAV_AUTORIZA\n" +
                "FROM MVASIGNARVEH\n" +
                "where MVE_SECUENCIAL = "+tipo+" and MAV_ESTADO_ASIGNACION = 1\n" +
                "order by MAV_SECUENCIAL desc");
        tab_persona.ejecutarSql();
        conexion.desconectar();
       conexion = null;
       return tab_persona;
    }  
            
              public void actDescargo(Integer tipo,String usu,String fecha){
        String str_sql4 = "UPDATE MVASIGNARVEH\n" +
                "SET MAV_ESTADO_ASIGNACION= '0',\n" +
                "MAV_ESTADO_TRAMITE='DESCARGO',\n" +
                "MAV_LOGINACTUALI='"+usu+"',\n" +
                "MAV_FECHAACTUALI="+utilitario.getFormatoFechaSQL(utilitario.getFechaHoraActual())+"\n" +
                "where MAV_SECUENCIAL ="+tipo;
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
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
    
    public TablaGenerica getComparacion(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                "ide_abastecimiento_combustible,\n" +
                "NUMERO_ABASTECIMIENTO,\n" +
                "NUMERO_VALE_ABASTECIMIENTO\n" +
                "FROM\n" +
                "MVABASTECIMIENTO_COMBUSTIBLE\n" +
                "where ide_abastecimiento_combustible= "+tipo);
        tab_persona.ejecutarSql();
       conexion.desconectar();
       conexion = null;
       return tab_persona;
    }
    
    public TablaGenerica getDatos(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT MVE_SECUENCIAL,MVE_MOTOR,MVE_CHASIS,MVE_MARCA,MVE_PLACA,MVE_TIPO,MVE_MODELO,MVE_COLOR,MVE_ANO,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO where MVE_SECUENCIAL ="+tipo);
        tab_persona.ejecutarSql();
        conexion.desconectar();
       conexion = null;
       return tab_persona;
    }
    
    public TablaGenerica getVerificar(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT MVE_SECUENCIAL, count(dbo.MVASIGNARVEH.MAV_ESTADO_ASIGNACION) as valor\n" +
                "FROM MVASIGNARVEH where MVE_SECUENCIAL = "+tipo+" and MAV_ESTADO_ASIGNACION = 1 GROUP BY MVE_SECUENCIAL");
        tab_persona.ejecutarSql();
        conexion.desconectar();
       conexion = null;
       return tab_persona;
    }
    
    public TablaGenerica datosExtraer(Integer tipo) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT a.MAV_SECUENCIAL,v.MVE_PLACA,v.MVE_MARCA,v.MVE_MODELO,v.MVE_ANO,v.MVE_COLOR,a.MAV_NOMEMPLEA\n" +
                "FROM MVASIGNARVEH a INNER JOIN MVVEHICULO  v ON a.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "where a.MAV_SECUENCIAL ="+tipo);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
        
    public String listaMax(String placa) {
         conectar();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(conexion);
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
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
    }
    
    public void ActHoras(String conduc,Double hora){
        String str_sql4 = "update MVVEHICULO\n" +
                "set MVE_HOROMETRO = "+hora+"\n" +
                "where MVE_PLACA = '"+conduc+"'";
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
    }
    
    public void actDescargo(Integer tipo,String motivo,String usu,String fecha){
        String str_sql4 = "UPDATE MVASIGNARVEH\n" +
                "SET MAV_ESTADO_ASIGNACION= '0',\n" +
                "MAV_ESTADO_TRAMITE='DESCARGO',\n" +
                "MAV_MOTIVO='"+motivo+"',\n" +
                "MAV_LOGINACTUALI='"+usu+"',\n" +
                "MAV_FECHAACTUALI="+utilitario.getFormatoFechaSQL(utilitario.getFechaHoraActual())+"\n" +
                "where MAV_SECUENCIAL ="+tipo;
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
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
        conectar();
        conexion.ejecutarSql(str_sql4);
        conexion.desconectar();
        conexion = null;
    }
       
    public String ParametrosMax(String dependencia) {
         conectar();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(conexion);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when count(lis_id) is null then '0' when count(lis_id)is not null then count(lis_id) end) AS maximo\n" +
                 "from mvlista where tab_codigo = '"+dependencia+"'");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
  }
    
    public TablaGenerica get_DuplicaDatos(String nombre,String codigo,String dependencia) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT LIS_ID,LIS_NOMBRE,TAB_CODIGO,DEPENDENCI\n" +
                "FROM MVLISTA\n" +
                "WHERE LIS_NOMBRE='"+nombre+"' AND TAB_CODIGO='"+codigo+"' AND DEPENDENCI ='"+dependencia+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ExtraDatos(String nombre,String codigo,String dependencia) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT LIS_ID,LIS_NOMBRE,TAB_CODIGO,DEPENDENCI\n" +
                "FROM MVLISTA\n" +
                "WHERE LIS_ID='"+nombre+"' AND TAB_CODIGO='"+codigo+"' AND DEPENDENCI ='"+dependencia+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

    public TablaGenerica get_ExDatos(String nombre) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT LIS_ID, LIS_NOMBRE\n" +
                "FROM MVLISTA WHERE TAB_CODIGO = 'ACCES'AND LIS_ESTADO = 1 AND LIS_NOMBRE='"+nombre+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
    
    public void getParametros(String id,String nombre,String codigo,String dependencia,String login){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"',"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+")";
        conectar();
        conexion.ejecutarSql(parametro);
        conexion.desconectar();
        conexion = null;
    }

    public void getParametros1(String id,String nombre,String codigo,String dependencia,String login,String cantidad){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO,LIS_CANTIDAD)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"',"+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+cantidad+"')";
        conectar();
        conexion.ejecutarSql(parametro);
        conexion.desconectar();
        conexion = null;
    }
        
    public void getMVDetalle(String codigo,String detalle,Double cantidad,String estado){
        String parametro ="insert into MVDETALLEVEHICULO (MVE_SECUENCIAL,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO)\n" +
                "values('"+codigo+"','"+detalle+"',"+cantidad+",'"+estado+"')";
        conectar();
        conexion.ejecutarSql(parametro);
        conexion.desconectar();
        conexion = null;
    }
    
    public void getMVDetalleASI(String codigo,String detalle,Double cantidad,String estado){
        String parametro ="insert into MVDETASIGNACION (MAV_SECUENCIAL,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO,MAV_ESTADO_ASIGNACION)\n" +
                "values('"+codigo+"','"+detalle+"',"+cantidad+",'"+estado+"','1')";
        conectar();
        conexion.ejecutarSql(parametro);
        conexion.desconectar();
        conexion = null;
    }
    
    public TablaGenerica getDirec(Integer tipo) {
        conect();
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
        conect();
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
        conect();
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
        conect();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo = "+periodo);
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
        return tab_persona;
    }   
    
    public TablaGenerica getChofer(String cedula) {
        conect();
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
        conect();
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
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT c.MSC_SECUENCIAL,v.MVE_PLACA,v.MVE_MARCA,c.MSC_CONDUCTOR,v.MVE_ANO,v.MVE_MOTOR,v.MVE_CHASIS\n" +
                "FROM MVVEHICULO AS v   \n" +
                "INNER JOIN dbo.MVCABSOLICITUD c ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "WHERE v.MVE_ESTADO_REGISTRO = 'activo' and c.MSC_SECUENCIAL ='"+nombre+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
    
    public TablaGenerica get_ExDatosSoli(String nombre) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT v.MVE_SECUENCIAL,v.MVE_MOTOR,v.MVE_CHASIS,v.MVE_PLACA,v.MVE_ANO,v.MVE_KILOMETRAJE,v.MVE_CONDUCTOR,\n" +
                "(SELECT top 1 MSC_FECHA\n" +
                "FROM MVCABSOLICITUD where MVE_SECUENCIAL= v.MVE_SECUENCIAL order by MSC_SECUENCIAL desc) as FECHA\n" +
                "FROM MVVEHICULO v WHERE v.MVE_SECUENCIAL ='"+nombre+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
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
    
    public void ActuReg(Integer ide,String vale){
        String str_sql4 = "update SIS_BLOQUEO\n" +
                "set MAXIMO_BLOQ ="+ide+"\n" +
                "where TABLA_BLOQ = '"+vale+"'";
        con_sqll();
        con_sql.ejecutarSql(str_sql4);
        con_sql.desconectar();
        con_sql = null;
    }
    
    public String RegisMaxSis() {
         con_sqll();
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         con_sqll();
         tab_consulta.setConexion(con_sql);
         tab_consulta.setSql("select 0 as id,\n" +
                 "(case when max(IDE_BLOQ) is null then '0' when max(IDE_BLOQ)is not null then max(IDE_BLOQ) end) as maximo\n" +
                 "from SIS_BLOQUEO where Tabla_BLOQ = 'MVDETASIGNACION'");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
         return ValorMax;
    } 
    
    private void con_sqll() {
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }
}
