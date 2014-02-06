/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
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
public class pre_presupuestarias extends Pantalla{
//Declaracion de combos
private Combo cmb_ano = new Combo();
private Combo cmb_niveli = new Combo();
private Combo cmb_nivelf = new Combo();
private Combo cmb_licenti = new Combo();

//declaracion de conexion
private Conexion con_postgres= new Conexion();

//Objetos reportes y calendarios
private SeleccionCalendario sec_rango = new SeleccionCalendario();
private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Reporte rep_reporte = new Reporte();
private Map p_parametros = new HashMap();

//obejtos eqyiquetas array y componentes
private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta2= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();
private Etiqueta eti_etiqueta3= new Etiqueta();
private Etiqueta eti_etiqueta4= new Etiqueta();
private Etiqueta eti_etiqueta5= new Etiqueta();
private Etiqueta eti_encab= new Etiqueta();
//Creacion Calendarios
private Calendario cal_fechain = new Calendario();
private Calendario cal_fechafin = new Calendario();

//obejto tabla usuario
private Tabla tab_consulta = new Tabla();


    public pre_presupuestarias() {


        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
                
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        Grid grid_pant = new Grid();
        grid_pant.setColumns(2);
        grid_pant.setStyle("text-align:center;position:absolute;top:70px;left:80px;");
        eti_encab.setStyle("font-size:22px;color:olive;text-align:center;");
        eti_encab.setValue("CEDULAS PRESUPUESTARIAS DE INGRESOS Y GASTOS");
        grid_pant.getChildren().add(eti_encab);
        
        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:70px;left:100px;");
        quinde.setValue("imagenes/logo.png");
        grid_pant.setWidth("100%");
        grid_pant.getChildren().add(quinde);
        agregarComponente(grid_pant);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        gri_busca.setStyle("text-align:left;position:absolute;top:140px;left:600px;");
        eti_etiqueta.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta.setValue("Año:");
        gri_busca.getChildren().add(eti_etiqueta);     
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano");
        gri_busca.getChildren().add(cmb_ano);
        
        eti_etiqueta2.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta2.setValue("Nivel Inicial:");
        gri_busca.getChildren().add(eti_etiqueta2); 
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
        gri_busca.getChildren().add(cmb_niveli);       
        
        eti_etiqueta1.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta1.setValue("Nivel Final:");
        gri_busca.getChildren().add(eti_etiqueta1); 
        cmb_nivelf.setId("cmb_nivelf");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "1", "1"
        };
        Object fila2[] = {
            "2", "2"
        };
        Object fila3[] = {
            "3", "3"
        };
        Object fila4[] = {
            "4", "4"
        };
        Object fila5[] = {
            "5", "5"
        };
        Object fila6[] = {
            "6", "6"
        };
        Object fila7[] = {
            "7", "7"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        lista1.add(fila3);;
        lista1.add(fila4);;
        lista1.add(fila5);;
        lista1.add(fila6);;
        lista1.add(fila7);;
        cmb_nivelf.setCombo(lista1);
        gri_busca.getChildren().add(cmb_nivelf);
        
        eti_etiqueta3.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta3.setValue("Fecha Inicial:");
        gri_busca.getChildren().add(eti_etiqueta3);
        cal_fechain.setId("cal_fechain");
        cal_fechain.setFechaActual();
        gri_busca.getChildren().add(cal_fechain);
        
        eti_etiqueta4.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta4.setValue("Fecha Final:");
        gri_busca.getChildren().add(eti_etiqueta4);
        cal_fechafin.setId("cal_fechafin");
        cal_fechafin.setFechaActual();
        gri_busca.getChildren().add(cal_fechafin);
        
        eti_etiqueta5.setStyle("font-size:19px;color:navy;text-align:center;");
        eti_etiqueta5.setValue("Tipo Cedula:");
        gri_busca.getChildren().add(eti_etiqueta5);      
        cmb_licenti.setId("cmb_licenti");
        List lista2 = new ArrayList();
        Object filat[] = {
            "1", "Ingresos Consolidados"
        };
        Object filau[] = {
            "2", "Gastos Programados"
        };
        lista2.add(filat);;
        lista2.add(filau);;
        cmb_licenti.setCombo(lista2);
        gri_busca.getChildren().add(cmb_licenti);
        agregarComponente(gri_busca);


        
  //llamar y ejecutar reporte              
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        
        sef_reporte.setId("sef_reporte");
        sef_reporte.setConexion(con_postgres);
        agregarComponente(sef_reporte);
        
        
//agregar usuario actual
        bar_botones.agregarReporte();      
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
    }
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }    
    
@Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Ingresos Consolidados")) {  
                    deleteTabla();
                    p_parametros = new HashMap();
                    p_parametros.put("ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("fechai", cal_fechain.getFecha());
                    p_parametros.put("fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+""));
                    p_parametros.put("niveli",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("nivelf",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    System.err.println(rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else
                   if(rep_reporte.getReporteSelecionado().equals("Gastos Programados")) { 
//                       deleteTablagas();
                    p_parametros = new HashMap();
                    p_parametros.put("ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("fechai", cal_fechain.getFecha());
                    p_parametros.put("fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+""));
                    p_parametros.put("niveli",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("nivelf",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
//                    System.err.println(rep_reporte.getPath());
                    sef_reporte.dibujar();
                }
                    else
                {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
    }

        public void deleteTabla() { 
            // Forma el sql para eliminar
        String str_sql ="delete from conc_cedula_presupuestaria_fechas";
        con_postgres.ejecutarSql(str_sql);
        insertarDatos();
    }
    
        
     public void insertarDatos() {
        // Forma el sql para el ingreso
        String str_sql1 =       "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)" +
                                "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,'"+Integer.parseInt(cmb_ano.getValue()+"")+"',nivel,'"+cal_fechafin.getFecha()+"' from conc_clasificador" +
                                " where tipo ='"+Integer.parseInt(cmb_licenti.getValue()+"")+"'order by pre_codigo ";

        con_postgres.ejecutarSql(str_sql1);
        actualizarDatos();
    }   
             
   
    public void actualizarDatos() {
        // Forma el sql para actualizacion
        String str_sql2 = "update conc_cedula_presupuestaria_fechas  \n" +
                                    "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0, \n" +
                                    "val_inicial=0 where tipo ='"+Integer.parseInt(cmb_licenti.getValue()+"")+"'";
        con_postgres.ejecutarSql(str_sql2);
        actualizarDatos1();
    }
         
   
    public void actualizarDatos1() {
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
    
    
    
    
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Combo getCmb_ano() {
        return cmb_ano;
    }

    public void setCmb_ano(Combo cmb_ano) {
        this.cmb_ano = cmb_ano;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
