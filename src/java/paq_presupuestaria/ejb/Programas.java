/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import org.hsqldb.types.Binary;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class Programas {

    private Conexion con_postgres;
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /*
     * GENERAL
     */
    //borrar tabla conc_cedula_presupuestaria_fechas
    
    public void actualizacionPrograma (){
    String str_sqlr ="insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion)\n" +
                            "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion\n" +
                            "from conc_clasificador,pre_funcion_programa\n" +
                            "order by ide_funcion,pre_codigo";
    conectar();    
    con_postgres.ejecutarSql(str_sqlr);
    con_postgres.desconectar();
    con_postgres = null;
    }
            
/*
 * INGRESOS CONSOLIDADOS
 */
    public void eiminarIngreso (){
    String str_sqlr ="delete from conc_cedula_presupuestaria_fechas";
    conectar();    
    con_postgres.ejecutarSql(str_sqlr);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
   public void insertaIngresos(Integer ano, Integer tipo, String fecha) {
        // Forma el sql para el ingreso
        String str_sql1 =       "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)" +
                                "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,"+ano+",nivel,'"+fecha+"' from conc_clasificador" +
                                " where tipo ="+tipo+" order by pre_codigo ";
    conectar();
    con_postgres.ejecutarSql(str_sql1);
//    con_postgres.desconectar();
//    con_postgres = null;

    }   
   
    public void actualizarIngresos(Integer combo) {
        // Forma el sql para actualizacion
        String str_sql2 = "update conc_cedula_presupuestaria_fechas \n" +
                           "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo="+combo+"";
        conectar();
    con_postgres.ejecutarSql(str_sql2);
    con_postgres.desconectar();
    con_postgres = null;
    } 
    
        public void actualizarDatosIngresos(String ini,String fin, Integer anio, Integer lice) {
        // Forma1 el sql para actualizacion
        String str_sql3 = "update conc_cedula_presupuestaria_fechas set \n" +
                                    "reforma1 = reforma \n" +
                                    "from ( select ide_clasificador, ((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n" +
                                    "from pre_anual a,( select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual  from pre_reforma_mes where fecha_reforma between '"+ini+"'" +
                                    " and '"+fin+"' group by ide_pre_anual) b where a.ide_pre_anual=b.ide_pre_anual and ano="+anio+" and not ide_clasificador is null group by ide_clasificador ) \n" +
                                    " a where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas set val_inicial = inicial from \n" +
                                    " ( select sum (val_presupuestado_i) as inicial, ide_clasificador from pre_anual where  ano="+anio+" and not ide_clasificador is null group by ide_clasificador ) a \n" +
                                    " where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas  \n" +
                                    " set cobradoc1= comprometido, devengado1=devengado from ( select b.ide_clasificador,ano,tipo, \n" +
                                    " sum( (case when cobradoc is null then 0 else cobradoc end ) )as comprometido, \n" +
                                    " sum((case when devengado is null then 0 else devengado end) ) as devengado from pre_anual a,\n" +
                                    "  conc_clasificador b,pre_mensual c where a.ide_clasificador=b.ide_clasificador and a.ide_pre_anual = c.ide_pre_anual and ano="+anio+"" +
                                    "  and fecha_ejecucion between '"+ini+"' and '"+fin+"' and tipo= "+lice+" group by b.ide_clasificador,ano,tipo order by tipo ) a \n" +
                                    "  where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    conectar();
    con_postgres.ejecutarSql(str_sql3);
    con_postgres.desconectar();
    con_postgres = null;
    }
        
    public void actualizarDatos2 (){
        String str_sqlg="update conc_cedula_presupuestaria_fechas set reforma1 = reforma \n" +
                                    "from ( select sum(reforma1) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";  
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
    public void actualizarDatosg3 (){
        String str_sqlg1="update conc_cedula_presupuestaria_fechas set val_inicial = reforma from ( select sum(val_inicial) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    
        conectar();
    con_postgres.ejecutarSql(str_sqlg1);
    con_postgres.desconectar();
    con_postgres = null;
    }

    public void actualizarDatosg4 (Integer ani, Integer tip){
        String str_sqlg2="update conc_cedula_presupuestaria_fechas  set cobradoc1= conbrado, devengado1=devengado \n" +
                                    "from ( select sum((case when cobradoc1 is null then 0 else cobradoc1 end)) as conbrado, \n" +
                                    "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado, con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas  where ano_curso="+ani+" and tipo= "+tip+" group by  con_ide_clasificador ) a \n" +
                                    "where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    conectar();
    con_postgres.ejecutarSql(str_sqlg2);
    con_postgres.desconectar();
    con_postgres = null;
    }    
    /*
     * GASTOS PROGRAMAS
     */
    
    public void insertarGastos(Integer an, String fec, Integer ti) { 
            // Forma el sql para insertar
        String str_sql1 ="insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,\n" +
                                    "ide_funcion,des_funcion,cod_funcion,fechaced)\n" +
                                    "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,"+an+",nivel,ide_funcion,des_funcion,cod_funcion,'"+fec+"'\n" +
                                    "from conc_clasificador,pre_funcion_programa\n" +
                                    "where tipo = "+ti+"" +
                                    "order by ide_funcion,pre_codigo";
        conectar();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void actualizarGastos(Integer cen) { 
            // Forma el sql para actualizar
        String str_sql2 ="update conc_cedula_presupuestaria_fechas \n" +
                                         "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo= "+cen+"";
        conectar();
        con_postgres.ejecutarSql(str_sql2);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
   public void actualizarGastos1(String in , String fi, Integer an ,Integer lic) { 
            // Forma el sql para actualizar 1
        String str_sql3 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set reforma1 = reforma\n" +
                                        "from (select a.ide_clasificador,sum(reforma) as reforma,ide_funcion\n" +
                                        "from prec_programas a, (select ide_programa,\n" +
                                        "((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n" +
                                        "from pre_anual a,(select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual \n" +
                                        "from pre_reforma_mes where fecha_reforma between '"+in+"' and '"+fi+"'\n" +
                                        "group by ide_pre_anual) b\n" +
                                        "where a.ide_pre_anual=b.ide_pre_anual\n" +
                                        "and ano="+an+" and not ide_programa is null\n" +
                                        "group by ide_programa ) b\n" +
                                        "where a.ide_programa = b.ide_programa\n" +
                                        "group by a.ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo="+lic+" and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conectar();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }
   
    public void actualizarGastos2(Integer ani, Integer licn) { 
            // Forma el sql para actualizar 2
        String str_sql4 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set val_inicial = inicial\n" +
                                        "from (select ide_clasificador,sum(inicial) as inicial,ide_funcion\n" +
                                        "from prec_programas a,(select sum (val_presupuestado_i) as inicial, ide_programa\n" +
                                        "from pre_anual\n" +
                                        "where  ano="+ani+" and not  ide_programa is null\n" +
                                        "group by ide_programa) b\n" +
                                        "where a.ide_programa=b.ide_programa\n" +
                                        "group by ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo="+licn+" and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;";
        conectar();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
     }
    
     public void actualizarTablaGastos3(Integer anio, String ini,String fin,Integer licen) { 
            // Forma el sql para actualizar 3
        String str_sql5 ="update conc_cedula_presupuestaria_fechas \n" +
                                        "set compromiso1= comprometido,\n" +
                                        "devengado1=devengado,\n" +
                                        "pagado1=pagado\n" +
                                        "from (select ide_clasificador,sum(comprometido) as comprometido,sum(devengado) as devengado,sum(pagado) as pagado,ide_funcion\n" +
                                        "from prec_programas a, (select b.ide_programa,ano,\n" +
                                        "sum( (case when comprometido is null then 0 else comprometido end ) )as comprometido,\n" +
                                        "sum((case when devengado is null then 0 else devengado end) ) as devengado,\n" +
                                        "sum((case when pagado is null then 0 else pagado end) ) as pagado\n" +
                                        "from pre_anual a, prec_programas b,pre_mensual c\n" +
                                        "where a.ide_programa=b.ide_programa\n" +
                                        "and a.ide_pre_anual = c.ide_pre_anual and ano="+anio+"\n" +
                                        "and fecha_ejecucion between '"+ini+"' and '"+fin+"'\n" +
                                        "group by b.ide_programa,ano) b\n" +
                                        "where a.ide_programa=b.ide_programa\n" +
                                        "group by ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion \n" +
                                        "and conc_cedula_presupuestaria_fechas.tipo="+licen+" and a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;";
        conectar();
        con_postgres.ejecutarSql(str_sql5);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
     public void actualizarTablaGastos4(Integer licn) { 
            // Forma el sql para actualizar 4
        String str_sql6 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set reforma1 = reforma\n" +
                                        "from (select sum(reforma1) as reforma,con_ide_clasificador,ide_funcion,tipo\n" +
                                        "from conc_cedula_presupuestaria_fechas\n" +
                                        "group by con_ide_clasificador,ide_funcion,tipo having tipo="+licn+") a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n" +
                                        "and  a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
         conectar();
        con_postgres.ejecutarSql(str_sql6);
        con_postgres.desconectar();
        con_postgres = null;
    }    
     
      public void actualizarTablaGastos5(Integer lice,Integer ano) { 
            // Forma el sql para actualizar 5
        String str_sql7 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set val_inicial = reforma\n" +
                                        "from (select sum(val_inicial) as reforma,con_ide_clasificador,ide_funcion,tipo\n" +
                                        "from conc_cedula_presupuestaria_fechas\n" +
                                        "group by con_ide_clasificador,ide_funcion,tipo having tipo="+lice+") a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n" +
                                        "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;\n" +
                                        "update conc_cedula_presupuestaria_fechas \n" +
                                        "set compromiso1= comprometido,\n" +
                                        "devengado1=devengado,\n" +
                                        "pagado1=pagado\n" +
                                        "from (select sum((case when compromiso1 is null then 0 else compromiso1 end)) as comprometido,\n" +
                                        "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado,\n" +
                                        "sum((case when pagado1 is null then 0 else pagado1 end)) as pagado,\n" +
                                        "con_ide_clasificador,ide_funcion,tipo\n" +
                                        "from conc_cedula_presupuestaria_fechas \n" +
                                        "where ano_curso= "+ano+" and tipo= "+lice+"\n" +
                                        "group by  con_ide_clasificador,ide_funcion,tipo ) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n" +
                                        "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conectar();
        con_postgres.ejecutarSql(str_sql7);
        con_postgres.desconectar();
        con_postgres = null;
    } 
      
     public void actuProveedor (Integer provee,String titular,Integer banco,String cuenta,String tipcuen,String activida,String fono,Integer tiprov,String fono1,String ruc,String cuban,String usu){
         String proveedor="UPDATE tes_proveedores set titular = '"+titular+"' ,ban_codigo ="+banco+",numero_cuenta ='"+cuenta+"',\n" +
                            "tipo_cuenta = '"+tipcuen+"',actividad ='"+activida+"',telefono1 ='"+fono+"',ide_tipo_proveedor ="+tiprov+" ,\n" +
                            "telefono2 ='"+fono1+"' ,ruc ='"+ruc+"',codigo_banco ='"+cuban+"',\n" +
                            "usuario_actua ='"+usu+"',ip_actua = '"+utilitario.getIp()+"',fecha_actua ='"+utilitario.getFechaActual()+"' WHERE ide_proveedor ="+provee;
         conectar();
        con_postgres.ejecutarSql(proveedor);
        con_postgres.desconectar();
        con_postgres = null;
     }
     
    public void actuEmpleado(Integer banco,String numero,String usu,Integer ci,Integer tipo){
       String empleado="Update srh_empleado set cod_banco="+banco+",numero_cuenta='"+numero+"',ip_responsable ='"+utilitario.getIp()+"',"
                        + "nom_responsable='"+usu+"',cod_cuenta="+tipo+",fecha_responsable='"+utilitario.getFechaActual()+"'WHERE cod_empleado = "+ci;
       conectar();
       con_postgres.ejecutarSql(empleado);
       con_postgres.desconectar();
       con_postgres = null;
    }
   
    public void actuListado(String ci, String respon,String usu,Integer ide){
     String str_sqlg="UPDATE  tes_comprobante_pago_listado \n" +
                    "set fecha_pagado = '"+utilitario.getFechaActual()+"',ci_paga='"+ci+"' ,responsable_paga='"+respon+"',"
                    + "usuario_actua_paga='"+usu+"',ip_actua_paga='"+utilitario.getIp()+"'\n" +
                    "WHERE ide_listado ="+ide;
       
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actualizarComprobante (String cuenta,Integer codigo,String tipo,Integer detalle,Integer listado,String usu,String registro,Integer estado){

        String str_sqlg="UPDATE tes_detalle_comprobante_pago_listado \n" +
                        "set numero_cuenta ='"+cuenta+"',ban_codigo="+codigo+",\n" +
                        "tipo_cuenta='"+tipo+"',usuario_actua_pagado='"+usu+"',ide_estado_detalle_listado ="+estado+" ,ban_nombre = (SELECT o.ban_nombre FROM ocebanco o WHERE o.ban_codigo ="+codigo+"),num_transferencia = '"+registro+"',ip_actua_pagado='"+utilitario.getIp()+"'\n" +
                        "WHERE ide_detalle_listado="+detalle+" and ide_listado ="+listado;  
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actuLis(String ci, String respon,Integer ide){
    String str_sqlg="UPDATE tes_comprobante_pago_listado\n" +
                    "set fecha_devolucion='"+utilitario.getFechaActual()+"',ci_devolucion='"+ci+"',usuario_actua_devolucion='"+respon+"',"
                    + "ip_actua_devolucion ='"+utilitario.getIp()+"'\n" +
                    "where ide_listado = "+ide;
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }

   public void actuLisDevol(Integer ide){
    String str_sqlg="UPDATE tes_comprobante_pago_listado\n" +
                    "set devolucion='1' where ide_listado = "+ide;
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actuLisDevolver(Integer ide){
    String str_sqlg="UPDATE tes_comprobante_pago_listado\n" +
                    "set estado='2' where ide_listado = "+ide;
    conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
      
    public void actuComponente(String cuenta,Integer codigo,String tipo,Integer detalle,Integer listado,String usu,String registro,Integer estado){
    String str_sqlg="UPDATE tes_detalle_comprobante_pago_listado\n" +
                    "set numero_cuenta ='"+cuenta+"',ban_codigo="+codigo+",ban_nombre=(SELECT ban_nombre FROM ocebanco WHERE ban_codigo ="+codigo+"),"
                      + "tipo_cuenta='"+tipo+"',usuario_actua_devolucion='"+usu+"',num_transferencia='"+registro+"',ip_actua_devolucion='"+utilitario.getIp()+"',ide_estado_detalle_listado="+estado+"\n" +
                    "where ide_detalle_listado = "+detalle+" and ide_listado = "+listado;
                        conectar();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;    
    }
    
     public TablaGenerica periodo(Integer periodo){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_tipo_cuenta,tipo_cuenta,cuenta FROM ocebanco_tipo_cuenta WHERE ide_tipo_cuenta ="+periodo);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
     
  public TablaGenerica banco(Integer banco){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ban_codigo,ban_nombre,num_banco FROM ocebanco WHERE ban_codigo ="+banco);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
    
 public TablaGenerica empleado(){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_empleado,estado\n" +
                                "FROM srh_empleado WHERE cod_cargo = 101 and estado = 1");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
  public TablaGenerica empleado1(String cedula){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_empleado,estado,cod_banco,\n" +
                                "(case when cod_cuenta = 1 then 'A' when cod_cuenta = 2 then 'C' when cod_cuenta = 3 then 'O' end ) as tipo_cuenta\n" +
                                ",numero_cuenta\n" +
                                "FROM srh_empleado  WHERE cedula_pass like '"+cedula+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 } 
 
   public TablaGenerica proveedor(String ruc){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_proveedor,ruc,titular,ban_codigo,numero_cuenta,tipo_cuenta,ide_tipo_proveedor,codigo_banco\n" +
                                "FROM tes_proveedores where ruc like '"+ruc+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
   
 public TablaGenerica busEstado(Integer estado){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_detalle_listado,ide_listado,ide_estado_detalle_listado\n" +
                                "FROM tes_detalle_comprobante_pago_listado WHERE ide_detalle_listado ="+estado);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
  
     private void conectar() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
     }
}
