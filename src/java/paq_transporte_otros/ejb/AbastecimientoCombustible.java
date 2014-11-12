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
                "MVE_HOROMETRO,\n" +
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
                "MAV_FECHAACTUALI='"+fecha+"'\n" +
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
    
    public void getParametros(String id,String nombre,String codigo,String dependencia,String login,String fecha){
        String parametro ="insert into MVLISTA (LIS_ID,LIS_NOMBRE,LIS_ESTADO,TAB_CODIGO,DEPENDENCI,LIS_LOGININGRESO,LIS_FECHAINGRESO)\n" +
                "values ('"+id+"','"+nombre+"',1,'"+codigo+"','"+dependencia+"','"+login+"','"+fecha+"')";
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
