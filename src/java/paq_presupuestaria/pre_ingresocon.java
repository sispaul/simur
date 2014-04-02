/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingresocon extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //Declaracion de combos
    private Combo cmb_ano = new Combo();
    private Combo cmb_niveli = new Combo();
    private Combo cmb_nivelf = new Combo();
    private Combo cmb_licenti = new Combo();
    private Combo cmb_progam = new Combo();
    
    //Creacion Calendarios
    private Calendario cal_fechain = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    
    private Panel pan_opcion = new Panel();
    private Efecto efecto = new Efecto();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //obejto tabla usuario
    private Tabla tab_consulta = new Tabla();
    
    public pre_ingresocon() {
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        String str_sqlt ="delete from conc_cedula_presupuestaria_fechas";
        con_postgres.ejecutarSql(str_sqlt);
        String str_sqlr ="insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion)\n" +
                            "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion\n" +
                            "from conc_clasificador,pre_funcion_programa\n" +
                            "order by ide_funcion,pre_codigo";
        con_postgres.ejecutarSql(str_sqlr);
        
         Panel tabp2 = new Panel();
         tabp2.setStyle("font-size:19px;color:black;text-align:center;");
         tabp2.setHeader("CEDULAS PRESUPUESTARIAS INGRESOS Y GASTOS - SALDOS NEGATIVOS");
        /*
         * COMBO QUE MUESTRAS LOS AÑOS DE 
         */
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        gri_busca.getChildren().add(new Etiqueta("AÑO: "));    
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso ");
        cmb_ano.eliminarVacio();
        gri_busca.getChildren().add(cmb_ano);
        
        /*
         * COMBOS QUE PERMITE SELECCIONAR EL NIVEL DE BUSQUEDA PARA LAS CEDULAS DE GASTOS E INGRESOS
         */

        gri_busca.getChildren().add(new Etiqueta("NIVEL INICIAL: "));
        cmb_niveli.setId("cmb_niveli");
        List lista = new ArrayList();
        Object filaa[] = {
            "1", "1"
        };
        Object filab[] = {
            "2", "2"
        };
        Object filac[] = {
            "3", "3"
        };
        Object filad[] = {
            "4", "4"
        };
        Object filae[] = {
            "5", "5"
        };
        Object filaf[] = {
            "6", "6"
        };
        Object filag[] = {
            "7", "7"
        };
        lista.add(filaa);;
        lista.add(filab);;
        lista.add(filac);;
        lista.add(filad);;
        lista.add(filae);;
        lista.add(filaf);;
        lista.add(filag);;
        cmb_niveli.setCombo(lista);
        cmb_niveli.eliminarVacio();
        gri_busca.getChildren().add(cmb_niveli);       
        
        gri_busca.getChildren().add(new Etiqueta("NIVEL FINAL: "));
        cmb_nivelf.setId("cmb_nivelf");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "7", "7"
        };
        Object fila2[] = {
            "6", "6"
        };
        Object fila3[] = {
            "5", "5"
        };
        Object fila4[] = {
            "4", "4"
        };
        Object fila5[] = {
            "3", "3"
        };
        Object fila6[] = {
            "2", "2"
        };
        Object fila7[] = {
            "1", "1"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        lista1.add(fila3);;
        lista1.add(fila4);;
        lista1.add(fila5);;
        lista1.add(fila6);;
        lista1.add(fila7);;
        cmb_nivelf.setCombo(lista1);
        cmb_nivelf.eliminarVacio();
        gri_busca.getChildren().add(cmb_nivelf);
        
        gri_busca.getChildren().add(new Etiqueta("FECHA INICIAL: "));
        cal_fechain.setId("cal_fechain");
        cal_fechain.setFechaActual();
        cal_fechain.setTipoBoton(true);
        gri_busca.getChildren().add(cal_fechain);
        
        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL: "));
        cal_fechafin.setId("cal_fechafin");
        cal_fechafin.setFechaActual();
        cal_fechafin.setTipoBoton(true);
        gri_busca.getChildren().add(cal_fechafin);
        
        /*
         * PROGRAMAS
         */
        gri_busca.getChildren().add(new Etiqueta("TIPO CEDULA: "));
        cmb_licenti.setId("cmb_licenti");
        List lista2 = new ArrayList();
        Object filat[] = {
            "1", "INGRESOS CONSOLIDADOS"
        };
        Object filau[] = {
            "2", "GASTOS PROGRAMADOS"
        };
        lista2.add(filat);;
        lista2.add(filau);;
        cmb_licenti.setCombo(lista2);
        cmb_licenti.eliminarVacio();
        cmb_licenti.setMetodo("programas");
        gri_busca.getChildren().add(cmb_licenti);
        
        /*
         * TIPOS DE GASTOS
         */
        gri_busca.getChildren().add(new Etiqueta("PROGRAMAS: "));
        cmb_progam.setId("cmb_progam");
        cmb_progam.setConexion(con_postgres);
//        cmb_progam.setCombo("select DISTINCT cod_funcion,des_funcion from  conc_cedula_presupuestaria_fechas ORDER BY des_funcion");
        gri_busca.getChildren().add(cmb_progam);
        
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setStyle("font-size:12px;color:black;text-align:left;");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");

        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:190px;left:500px;");
        quinde.setValue("imagenes/logo.png");
//        tabp2.setWidth("100%");
        
        pan_opcion.getChildren().add(efecto);
        pan_opcion.getChildren().add(quinde);
        pan_opcion.getChildren().add(gri_busca);
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("IMPRESIÓN DE REPORTE");
        bot_bus.setIcon("ui-icon-print");
        bot_bus.setMetodo("abrirListaReportes");
        
        tabp2.getChildren().add(pan_opcion);
        tabp2.getChildren().add(bot_bus);
        agregarComponente(tabp2);
        
         /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
   
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

    public void programas (){
        if(cmb_licenti.getValue().equals("1")){
        cmb_progam.setCombo("select DISTINCT cod_funcion,des_funcion from  conc_cedula_presupuestaria_fechas WHERE ano_curso = 1900 ORDER BY des_funcion");
        utilitario.addUpdate("cmb_progam");
        }
        else {
        cmb_progam.setCombo("select DISTINCT cod_funcion,des_funcion from  conc_cedula_presupuestaria_fechas  WHERE tipo ="+cmb_licenti.getValue()+" ORDER BY des_funcion");
        utilitario.addUpdate("cmb_progam");
        cmb_progam.eliminarVacio();
        }
    }
    
    /*
         * LLAMADA DE REPORTE  SOLICITUD
         */
        
        @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "INGRESOS CONSOLIDADOS":
               deleteTablaIngresos();
               aceptoDialogo();
               break;
           case "GASTOS PROGRAMADOS POR TIPO":
               deleteTablaGastos();
               aceptoDialogo();
           break;
           case "GASTOS PROGRAMADOS TOTAL":
               deleteTablaGastos();
               aceptoDialogo();
           break;
                
        }
    }     
       
        public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "INGRESOS CONSOLIDADOS":
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+""));
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
               case "GASTOS PROGRAMADOS POR TIPO":
                    p_parametros = new HashMap();
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+"")); 
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("funcion",cmb_progam.getValue());
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
               case "GASTOS PROGRAMADOS TOTAL":
                    p_parametros = new HashMap();
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+"")); 
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }

    }

    //ACTUALIZACIONES PARA OPCION DE INGRESOS CONSOLIDADOS
        public void deleteTablaIngresos() { 
            // Forma el sql para eliminar
        String str_sql ="delete from conc_cedula_presupuestaria_fechas";
        con_postgres.ejecutarSql(str_sql);
        insertarDatosIngresos();
    }
   
     public void insertarDatosIngresos() {
        // Forma el sql para el ingreso
        String str_sql1 =       "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)" +
                                "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,'"+Integer.parseInt(cmb_ano.getValue()+"")+"',nivel,'"+cal_fechafin.getFecha()+"' from conc_clasificador" +
                                " where tipo ='"+Integer.parseInt(cmb_licenti.getValue()+"")+"'order by pre_codigo ";

        con_postgres.ejecutarSql(str_sql1);
        actualizarDatosIngresos();
    }   
             
    public void actualizarDatosIngresos() {
        // Forma el sql para actualizacion
        String str_sql2 = "update conc_cedula_presupuestaria_fechas \n" +
                           "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"'";
        con_postgres.ejecutarSql(str_sql2);
        actualizarDatosIngresos1();
    } 
      
    public void actualizarDatosIngresos1() {
        // Forma1 el sql para actualizacion
        String str_sql3 = "update conc_cedula_presupuestaria_fechas set \n" +
                                    "reforma1 = reforma \n" +
                                    "from ( select ide_clasificador, ((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n" +
                                    "from pre_anual a,( select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual  from pre_reforma_mes where fecha_reforma between '"+cal_fechain.getFecha()+"'" +
                                    " and '"+cal_fechafin.getFecha()+"' group by ide_pre_anual) b where a.ide_pre_anual=b.ide_pre_anual and ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"' and not ide_clasificador is null group by ide_clasificador ) \n" +
                                    " a where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas set val_inicial = inicial from \n" +
                                    " ( select sum (val_presupuestado_i) as inicial, ide_clasificador from pre_anual where  ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"' and not ide_clasificador is null group by ide_clasificador ) a \n" +
                                    " where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas  \n" +
                                    " set cobradoc1= comprometido, devengado1=devengado from ( select b.ide_clasificador,ano,tipo, \n" +
                                    " sum( (case when cobradoc is null then 0 else cobradoc end ) )as comprometido, \n" +
                                    " sum((case when devengado is null then 0 else devengado end) ) as devengado from pre_anual a,\n" +
                                    "  conc_clasificador b,pre_mensual c where a.ide_clasificador=b.ide_clasificador and a.ide_pre_anual = c.ide_pre_anual and ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"'" +
                                    "  and fecha_ejecucion between '"+cal_fechain.getFecha()+"' and '"+cal_fechafin.getFecha()+"' and tipo= '"+Integer.parseInt(cmb_licenti.getValue()+"")+"' group by b.ide_clasificador,ano,tipo order by tipo ) a \n" +
                                    "  where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
   con_postgres.ejecutarSql(str_sql3);
   actualizarDatos2 ();
    }
    
      public void actualizarDatos2 (){
        String str_sqlg="update conc_cedula_presupuestaria_fechas set reforma1 = reforma \n" +
                                    "from ( select sum(reforma1) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";  
    con_postgres.ejecutarSql(str_sqlg);
    actualizarDatosg3 ();
    }
    public void actualizarDatosg3 (){
        String str_sqlg1="update conc_cedula_presupuestaria_fechas set val_inicial = reforma from ( select sum(val_inicial) as reforma,con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    con_postgres.ejecutarSql(str_sqlg1);
    actualizarDatosg4 ();
    }

    public void actualizarDatosg4 (){
        String str_sqlg2="update conc_cedula_presupuestaria_fechas  set cobradoc1= conbrado, devengado1=devengado \n" +
                                    "from ( select sum((case when cobradoc1 is null then 0 else cobradoc1 end)) as conbrado, \n" +
                                    "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado, con_ide_clasificador \n" +
                                    "from conc_cedula_presupuestaria_fechas  where ano_curso='"+Integer.parseInt(cmb_ano.getValue()+"")+"' and tipo= '"+Integer.parseInt(cmb_licenti.getValue()+"")+"' group by  con_ide_clasificador ) a \n" +
                                    "where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
    con_postgres.ejecutarSql(str_sqlg2);
    }      
    
  //ACTUALIZACIONES PARA OPCION DE GASTOS PROGRAMADOS 
    public void deleteTablaGastos() { 
            // Forma el sql para eliminar
        String str_sql ="delete from conc_cedula_presupuestaria_fechas";
        con_postgres.ejecutarSql(str_sql);
        insertarTablaGastos();
    }
    public void insertarTablaGastos() { 
            // Forma el sql para insertar
        String str_sql1 ="insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,\n" +
                                    "ide_funcion,des_funcion,cod_funcion,fechaced)\n" +
                                    "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,'"+Integer.parseInt(cmb_ano.getValue()+"")+"',nivel,ide_funcion,des_funcion,cod_funcion,'"+cal_fechafin.getFecha()+"'\n" +
                                    "from conc_clasificador,pre_funcion_programa\n" +
                                    "where tipo = '"+Integer.parseInt(cmb_licenti.getValue()+"")+"'" +
                                    "order by ide_funcion,pre_codigo";
        con_postgres.ejecutarSql(str_sql1);
        actualizarTablaGastos();
    }
    public void actualizarTablaGastos() { 
            // Forma el sql para actualizar
        String str_sql2 ="update conc_cedula_presupuestaria_fechas \n" +
                                         "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo= '"+Integer.parseInt(cmb_licenti.getValue()+"")+"'";
        con_postgres.ejecutarSql(str_sql2);
        actualizarTablaGastos1();
    }
    public void actualizarTablaGastos1() { 
            // Forma el sql para actualizar 1
        String str_sql3 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set reforma1 = reforma\n" +
                                        "from (select a.ide_clasificador,sum(reforma) as reforma,ide_funcion\n" +
                                        "from prec_programas a, (select ide_programa,\n" +
                                        "((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n" +
                                        "from pre_anual a,(select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual \n" +
                                        "from pre_reforma_mes where fecha_reforma between '"+cal_fechain.getFecha()+"' and '"+cal_fechafin.getFecha()+"'\n" +
                                        "group by ide_pre_anual) b\n" +
                                        "where a.ide_pre_anual=b.ide_pre_anual\n" +
                                        "and ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"' and not ide_programa is null\n" +
                                        "group by ide_programa ) b\n" +
                                        "where a.ide_programa = b.ide_programa\n" +
                                        "group by a.ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"' and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        con_postgres.ejecutarSql(str_sql3);
        actualizarTablasGastos2();
    }
    public void actualizarTablasGastos2() { 
            // Forma el sql para actualizar 2
        String str_sql4 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set val_inicial = inicial\n" +
                                        "from (select ide_clasificador,sum(inicial) as inicial,ide_funcion\n" +
                                        "from prec_programas a,(select sum (val_presupuestado_i) as inicial, ide_programa\n" +
                                        "from pre_anual\n" +
                                        "where  ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"' and not  ide_programa is null\n" +
                                        "group by ide_programa) b\n" +
                                        "where a.ide_programa=b.ide_programa\n" +
                                        "group by ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"' and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;";
        con_postgres.ejecutarSql(str_sql4);
        actualizarTablaGastos3();
    }
    public void actualizarTablaGastos3() { 
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
                                        "and a.ide_pre_anual = c.ide_pre_anual and ano='"+Integer.parseInt(cmb_ano.getValue()+"")+"'\n" +
                                        "and fecha_ejecucion between '"+cal_fechain.getFecha()+"' and '"+cal_fechafin.getFecha()+"'\n" +
                                        "group by b.ide_programa,ano) b\n" +
                                        "where a.ide_programa=b.ide_programa\n" +
                                        "group by ide_clasificador,ide_funcion) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion \n" +
                                        "and conc_cedula_presupuestaria_fechas.tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"' and a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;";
        con_postgres.ejecutarSql(str_sql5);
        actualizarTablaGastos4();
    }
    public void actualizarTablaGastos4() { 
            // Forma el sql para actualizar 4
        String str_sql6 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set reforma1 = reforma\n" +
                                        "from (select sum(reforma1) as reforma,con_ide_clasificador,ide_funcion,tipo\n" +
                                        "from conc_cedula_presupuestaria_fechas\n" +
                                        "group by con_ide_clasificador,ide_funcion,tipo having tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"') a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n" +
                                        "and  a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        con_postgres.ejecutarSql(str_sql6);
        actualizarTablaGastos5();
    }
    public void actualizarTablaGastos5() { 
            // Forma el sql para actualizar 5
        String str_sql7 ="update conc_cedula_presupuestaria_fechas\n" +
                                        "set val_inicial = reforma\n" +
                                        "from (select sum(val_inicial) as reforma,con_ide_clasificador,ide_funcion,tipo\n" +
                                        "from conc_cedula_presupuestaria_fechas\n" +
                                        "group by con_ide_clasificador,ide_funcion,tipo having tipo='"+Integer.parseInt(cmb_licenti.getValue()+"")+"') a\n" +
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
                                        "where ano_curso= '"+Integer.parseInt(cmb_ano.getValue()+"")+"' and tipo= '"+Integer.parseInt(cmb_licenti.getValue()+"")+"'\n" +
                                        "group by  con_ide_clasificador,ide_funcion,tipo ) a\n" +
                                        "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n" +
                                        "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        con_postgres.ejecutarSql(str_sql7);
    }
        
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Combo getCmb_ano() {
        return cmb_ano;
    }

    public void setCmb_ano(Combo cmb_ano) {
        this.cmb_ano = cmb_ano;
    }

    public Combo getCmb_niveli() {
        return cmb_niveli;
    }

    public void setCmb_niveli(Combo cmb_niveli) {
        this.cmb_niveli = cmb_niveli;
    }

    public Combo getCmb_nivelf() {
        return cmb_nivelf;
    }

    public void setCmb_nivelf(Combo cmb_nivelf) {
        this.cmb_nivelf = cmb_nivelf;
    }

    public Combo getCmb_licenti() {
        return cmb_licenti;
    }

    public void setCmb_licenti(Combo cmb_licenti) {
        this.cmb_licenti = cmb_licenti;
    }

    public Combo getCmb_progam() {
        return cmb_progam;
    }

    public void setCmb_progam(Combo cmb_progam) {
        this.cmb_progam = cmb_progam;
    }

    public Calendario getCal_fechain() {
        return cal_fechain;
    }

    public void setCal_fechain(Calendario cal_fechain) {
        this.cal_fechain = cal_fechain;
    }

    public Calendario getCal_fechafin() {
        return cal_fechafin;
    }

    public void setCal_fechafin(Calendario cal_fechafin) {
        this.cal_fechafin = cal_fechafin;
    }
    
}