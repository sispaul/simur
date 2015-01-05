/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria.ejb;

import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class ProgramasAnterior {

    private Conexion con_postgres;
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    /*
 * INGRESOS CONSOLIDADOS
 */
    public void eiminarIngreso (){
    String str_sqlr ="delete from conc_cedula_presupuestaria_fechas";
    con_postgresqlAnt();    
    con_postgres.ejecutarSql(str_sqlr);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actualizacionPrograma (){
    String str_sqlr ="insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion)\n" +
                            "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion\n" +
                            "from conc_clasificador,pre_funcion_programa\n" +
                            "order by ide_funcion,pre_codigo";
    con_postgresqlAnt();    
    con_postgres.ejecutarSql(str_sqlr);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void insertaIngresos(Integer ano, Integer tipo, String fecha) {
        // Forma el sql para el ingreso
        String str_sql1 =       "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)" +
                                "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,"+ano+",nivel,'"+fecha+"' from conc_clasificador" +
                                " where tipo ="+tipo+" order by pre_codigo ";
    con_postgresqlAnt();
    con_postgres.ejecutarSql(str_sql1);
    } 
    
    public void actualizarIngresos(Integer combo) {
        // Forma el sql para actualizacion
        String str_sql2 = "update conc_cedula_presupuestaria_fechas \n" +
                           "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo="+combo+"";
    con_postgresqlAnt();
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
    con_postgresqlAnt();
    con_postgres.ejecutarSql(str_sql3);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actualizarDatos2 (){
        String str_sqlg="update conc_cedula_presupuestaria_fechas set reforma1 = reforma \n" +
                                    "from ( select sum(reforma1) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";  
    con_postgresqlAnt();
    con_postgres.ejecutarSql(str_sqlg);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    public void actualizarDatosg3 (){
        String str_sqlg1="update conc_cedula_presupuestaria_fechas set val_inicial = reforma from ( select sum(val_inicial) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    
    con_postgresqlAnt();
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
    con_postgresqlAnt();
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
        con_postgresqlAnt();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void actualizarGastos(Integer cen) { 
            // Forma el sql para actualizar
        String str_sql2 ="update conc_cedula_presupuestaria_fechas \n" +
                "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo= "+cen;
        con_postgresqlAnt();
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
        con_postgresqlAnt();
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
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo="+licn+" and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        con_postgresqlAnt();
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
                                        "and conc_cedula_presupuestaria_fechas.tipo="+licen+" and a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        con_postgresqlAnt();
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
        con_postgresqlAnt();
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
        con_postgresqlAnt();
        con_postgres.ejecutarSql(str_sql7);
        con_postgres.desconectar();
        con_postgres = null;
    }      
          
    private void con_postgresqlAnt(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgresAnt"));
        }
    }
}
