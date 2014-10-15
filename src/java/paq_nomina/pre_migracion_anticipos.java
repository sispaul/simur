/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.SolicAnticipos;
import paq_nomina.ejb.mergeDescuento;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_migracion_anticipos extends Pantalla{

    private Tabla tab_migracion = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    private Conexion con_postgres= new Conexion();
    
            ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
        @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
    public pre_migracion_anticipos() {
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_com = new Boton();
        bot_com.setValue("Completar Listado");
        bot_com.setExcluirLectura(true);
        bot_com.setIcon("ui-icon-extlink");
        bot_com.setMetodo("completa");
        bar_botones.agregarBoton(bot_com);
        
        Boton bot_save = new Boton();
        bot_save.setValue("Migrar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-extlink");
        bot_save.setMetodo("migra_calculo");
        bar_botones.agregarBoton(bot_save);
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Borrar Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("borrar");
        bar_botones.agregarBoton(bot_delete);
        
        //Encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento1.png");
        agregarComponente(quinde);
        
        tab_migracion.setId("tab_migracion");
        tab_migracion.setConexion(con_postgres);
        tab_migracion.setTabla("srh_migrar_anticipo", "ide_migrar", 1);
        tab_migracion.setRows(13);
        tab_migracion.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("MIGRACION DE DATOS DE ANTICIPOS PENDIENTES");
        pat_panel.setPanelTabla(tab_migracion);
        agregarComponente(pat_panel);
        
                 /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
    }

    public void borrar(){
        iAnticipos.deleteMigrar();
        tab_migracion.actualizar();
    }
    
    public void completa(){
        for (int i = 0; i < tab_migracion.getTotalFilas(); i++) {
            tab_migracion.getValor(i, "cedula");
            TablaGenerica tab_dato = iAnticipos.empleadosCed(tab_migracion.getValor(i,"cedula"));//empleados
                if (!tab_dato.isEmpty()) {
                    iAnticipos.actuaMigrar(Double.parseDouble(tab_dato.getValor("ru")), Integer.parseInt(tab_dato.getValor("id_distributivo_roles")), 
                            Integer.parseInt(tab_dato.getValor("cod_cargo")), tab_migracion.getValor(i,"cedula"), Integer.parseInt(tab_migracion.getValor(i,"ide_migrar")));
                }else {
                    TablaGenerica tab_dato1 = iAnticipos.trabajadoresCed(tab_migracion.getValor(i,"cedula"));//trabajadores
                    if (!tab_dato1.isEmpty()) {
                        iAnticipos.actuaMigrar(Double.parseDouble(tab_dato1.getValor("su")), Integer.parseInt(tab_dato1.getValor("id_distributivo_roles")), 
                            Integer.parseInt(tab_dato1.getValor("cod_cargo")), tab_migracion.getValor(i,"cedula"), Integer.parseInt(tab_migracion.getValor(i,"ide_migrar")));
                    }
        }
    }
        tab_migracion.actualizar();
    }
    public void migra_calculo(){
         for (int i = 0; i < tab_migracion.getTotalFilas(); i++) {
            tab_migracion.getValor(i, "cedula");
                 iAnticipos.migra_lista(tab_migracion.getValor(i, "cedula"),utilitario.getVariable("NICK"));
                 TablaGenerica tab_datos = iAnticipos.cod_listado(tab_migracion.getValor(i, "cedula"));
                 if (!tab_datos.isEmpty()) {
                     iAnticipos.migra_calculo(tab_migracion.getValor(i, "cedula"), Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")));                
                 }else{
                     utilitario.agregarMensaje("No se encuentra en base", tab_migracion.getValor(i, "cedula"));
                 }
             }
//         migrar_detalle();
    }
    
    public void migrar_detalle(){
        for (int i = 0; i < tab_migracion.getTotalFilas(); i++) {
            tab_migracion.getValor(i, "cedula");
            tab_migracion.getValor(i, "id_distributivo");
              TablaGenerica tab_datos = iAnticipos.cod_listado(tab_migracion.getValor(i, "cedula"));
                if (!tab_datos.isEmpty()) {
                   TablaGenerica tab_datoss = iAnticipos.cod_detalle( Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")));
                    if (!tab_datoss.isEmpty()) {
                            if(tab_migracion.getValor(i, "id_distributivo").equals("1")){
                                //detalle solicitud de empleados
                                 if((utilitario.getDia(tab_datoss.getValor("fecha_anticipo")))>10){
                                     if((Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))+(utilitario.getMes(tab_datoss.getValor("fecha_anticipo"))))>12){  
                                         for (int j = 0; j < (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1); j++){
                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                            if (!tab_dato.isEmpty()) {
                                                   if(tab_dato.getValor("mes").equals("Diciembre")){
                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_datoss.getValor("val_cuo_adi")), 
                                                       Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);

                                                   }else{
                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual")), 
                                                           Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                                       }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                               }
                                        }
                                           Double valorp=0.0,valors=0.0,totall=0.0;
                                           valorp = (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual"));
                                           valors= Double.parseDouble(tab_datoss.getValor("val_cuo_adi"))+valorp ;
                                           totall = Double.parseDouble(tab_datoss.getValor("valor_anticipo"))-valors ;
                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), tab_datoss.getValor("numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
                                           Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_final")));
                                     }else{
                                      for (int j = 0; j < (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1); j++){
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual")), 
                                                        Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                     }
                                        Double valor1=0.0,total=0.0;
                                        valor1 = (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual"));
                                        total = Double.parseDouble(tab_datoss.getValor("valor_anticipo"))-valor1 ;
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")),tab_datoss.getValor("numero_cuotas_anticipo"), total, 
                                        Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_final"))-1);
                                          }
                                 }else {
                                     System.err.println("holae1");
                                       if((Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))+(utilitario.getMes(tab_datoss.getValor("fecha_anticipo"))))>12){
                                           for (int j = 0; j < (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1); j++){
                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                            if (!tab_dato.isEmpty()) {
                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_datoss.getValor("val_cuo_adi")), 
                                                       Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                                       System.err.println("holae");
                                                   }else{
                                                       System.err.println("holad");
                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual")), 
                                                           Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                                       }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                               }
                                        }
                                           System.err.println("holac");
                                           Double valorp=0.0,valors=0.0,totall=0.0;
                                           valorp = (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual"));
                                           valors= Double.parseDouble(tab_datoss.getValor("val_cuo_adi"))+valorp ;
                                           totall = Double.parseDouble(tab_datoss.getValor("valor_anticipo"))-valors ;
                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), tab_datoss.getValor("numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
                                           Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_final"))-1);//
                                         }else{
                                      for (int j = 0; j < (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1); j++){
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual")), 
                                                        Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                            System.err.println("holab");
                                     }
                                      System.err.println("holaa");
                                        Double valor1=0.0,total=0.0;
                                        valor1 = (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual"));
                                        total = Double.parseDouble(tab_datoss.getValor("valor_anticipo"))-valor1 ;
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), tab_datoss.getValor("numero_cuotas_anticipo"), total, 
                                        Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_final")));
                                              }
                                 }
                             }else{//detalle para solicitud de trabajadores
                                    for (int j = 0; j < (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1); j++){
                                        System.err.println("hola11");
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), String.valueOf(1+j), 
                                                Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual")), 
                                                  Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_inicial"))+j);
                                   } 
                                      Double valor1=0.0,total=0.0;
                                      valor1 = (Integer.parseInt(tab_datoss.getValor("numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_datoss.getValor("valor_cuota_mensual"));
                                      total = Double.parseDouble(tab_datoss.getValor("valor_anticipo"))-valor1 ;
                                      System.err.println(valor1);
                                      System.err.println(total);
                                      iAnticipos.llenarSolicitud(Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")), tab_datoss.getValor("numero_cuotas_anticipo"), total, 
                                                  Integer.parseInt(tab_datoss.getValor("ide_periodo_anticipo_final")));   
                             }
                    }else{
                        utilitario.agregarMensaje("No se encuentra en base", tab_migracion.getValor(i, "cedula"));
                    }
                     
                }else{
                    utilitario.agregarMensaje("No se encuentra en base", tab_migracion.getValor(i, "cedula"));
                }
                                  }
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        if (tab_migracion.guardar()) {
               con_postgres.guardarPantalla();
           }
    }

    @Override
    public void eliminar() {
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "VERIFICAR MIGRACION":
                aceptoAnticipo();
          break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "VERIFICAR MIGRACION":
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }
    }
    
    public Tabla getTab_migracion() {
        return tab_migracion;
    }

    public void setTab_migracion(Tabla tab_migracion) {
        this.tab_migracion = tab_migracion;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
    
}
