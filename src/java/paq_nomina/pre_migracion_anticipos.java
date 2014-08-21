/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_nomina.ejb.SolicAnticipos;
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
    
        @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_migracion_anticipos() {
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_save = new Boton();
        bot_save.setValue("Migrar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-extlink");
        bot_save.setMetodo("migrar");
        bar_botones.agregarBoton(bot_save);
        
        //Encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento1.png");
        agregarComponente(quinde);
        
        tab_migracion.setId("tab_migracion");
        tab_migracion.setConexion(con_postgres);
        tab_migracion.setTabla("srh_migrar_anticipo", "ide_migrar", 1);
        tab_migracion.setRows(15);
        tab_migracion.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("MIGRACION DE DATOS DE ANTICIPOS PENDIENTES");
        pat_panel.setPanelTabla(tab_migracion);
        agregarComponente(pat_panel);
        
    }

    public void migrar(){
        for (int i = 0; i < tab_migracion.getTotalFilas(); i++) {
            tab_migracion.getValor(i, "cedula");
            iAnticipos.migra_lista(tab_migracion.getValor(i, "cedula"),utilitario.getVariable("NICK"));
            

                  
//                           if(tab_migracion.getValor(i, "id_distributivo").equals("1")){//detalle solicitud de empleados
//                                 if((utilitario.getDia(tab_migracion.getValor(i, "fecha_anticipo")))>10){
//                                     if((Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))+(utilitario.getMes(tab_migracion.getValor(i, "fecha_anticipo"))))>12){
//                                            for (int j = 0; j < (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1); j++){
//                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                            if (!tab_dato.isEmpty()) {
//                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
//                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_migracion.getValor(i,"val_cuo_adi")), 
//                                                       Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//
//                                                   }else{
//                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual")), 
//                                                           Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                                       }
//                                            }else {
//                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
//                                               }
//                                        }
//                                           Double valorp=0.0,valors=0.0,totall=0.0;
//                                           valorp = (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual"));
//                                           valors= Double.parseDouble(tab_migracion.getValor(i,"val_cuo_adi"))+valorp ;
//                                           totall = Double.parseDouble(tab_migracion.getValor(i,"valor_anticipo"))-valors ;
//                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), tab_migracion.getValor(i,"numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
//                                           Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_final")));
//                                     }else{
//                                      for (int j = 0; j < (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1); j++){
//                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual")), 
//                                                        Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                     }
//                                        Double valor1=0.0,total=0.0;
//                                        valor1 = (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual"));
//                                        total = Double.parseDouble(tab_migracion.getValor(i,"valor_anticipo"))-valor1 ;
//                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), tab_migracion.getValor(i,"numero_cuotas_anticipo"), total, 
//                                        Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_final")));
//                                          }
//                                 }else {
//                                       if((Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))+(utilitario.getMes(tab_migracion.getValor(i, "fecha_anticipo"))))-1>12){
//                                           for (int j = 0; j < (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1); j++){
//                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                            if (!tab_dato.isEmpty()) {
//                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
//                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_migracion.getValor(i,"val_cuo_adi")), 
//                                                       Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//
//                                                   }else{
//                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual")), 
//                                                           Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                                       }
//                                            }else {
//                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
//                                               }
//                                        }
//                                           Double valorp=0.0,valors=0.0,totall=0.0;
//                                           valorp = (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual"));
//                                           valors= Double.parseDouble(tab_migracion.getValor(i,"val_cuo_adi"))+valorp ;
//                                           totall = Double.parseDouble(tab_migracion.getValor(i,"valor_anticipo"))-valors ;
//                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), tab_migracion.getValor(i,"numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
//                                           Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_final")));
//                                         }else{
//                                      for (int j = 0; j < (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1); j++){
//                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual")), 
//                                                        Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                     }
//                                        Double valor1=0.0,total=0.0;
//                                        valor1 = (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual"));
//                                        total = Double.parseDouble(tab_migracion.getValor(i,"valor_anticipo"))-valor1 ;
//                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), tab_migracion.getValor(i,"numero_cuotas_anticipo"), total, 
//                                        Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_final")));
//                                              }
//                                 }
//                             }else{//detalle para solicitud de trabajadores
//                                    for (int j = 0; j < (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1); j++){
//                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual")), 
//                                                  Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_inicial"))+j);
//                                   } 
//                                      Double valor1=0.0,total=0.0;
//                                      valor1 = (Integer.parseInt(tab_migracion.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_migracion.getValor(i,"valor_cuota_mensual"));
//                                      total = Double.parseDouble(tab_migracion.getValor(i,"valor_anticipo"))-valor1 ;
//                                      iAnticipos.llenarSolicitud(Integer.parseInt(tab_migracion.getValor(i,"ide_solicitud_anticipo")), tab_migracion.getValor(i,"numero_cuotas_anticipo"), total, 
//                                                  Integer.parseInt(tab_migracion.getValor(i,"ide_periodo_anticipo_final")));   
//                                   
//                             }
        }
        migra_calculo();
    }
    
    public void migra_calculo(){
         for (int i = 0; i < tab_migracion.getTotalFilas(); i++) {
            tab_migracion.getValor(i, "cedula");
            TablaGenerica tab_datos = iAnticipos.cod_listado(tab_migracion.getValor(i, "cedula"));
            System.err.println("holas21");
             if (!tab_datos.isEmpty()) {
                 System.err.println("holas2");
                  iAnticipos.migra_calculo(tab_migracion.getValor(i, "cedula"), Integer.parseInt(tab_datos.getValor("ide_solicitud_anticipo")));
             }else{
                 System.err.println("holas3");
                 utilitario.agregarMensaje("No se encuentra en base", tab_migracion.getValor(i, "cedula"));
             }
         }

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
    
}
