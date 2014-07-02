/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import paq_nomina.ejb.anticipos;
import static paq_nomina.pre_anticipos_gadmur.calcularAnios;
import static paq_nomina.pre_anticipos_gadmur.calcularMes;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_solicitud_anticipos extends Pantalla{

    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
     //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    
    @EJB
    private anticipos iAnticipos = (anticipos) utilitario.instanciarEJB(anticipos.class);
    
    public pre_solicitud_anticipos() {
        
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD PARA ANTICIPO DE SUELDO");
        agregarComponente(pan_opcion);
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_anticipo", "ide_anticipo", 1);
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_anticipo.getColumna("ide_empleado_solicitante").setMetodoChange("llenarEmpleadoCodigo");
        tab_anticipo.getColumna("ide_empleado_garante").setMetodoChange("llenarGaranteCodigo");
        tab_anticipo.getColumna("solicitante").setMetodoChange("llenarPorNombre");
        tab_anticipo.getColumna("garante").setMetodoChange("llenarGaranteNom");
        tab_anticipo.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_anticipo.getColumna("numero_cuotas_anticipo").setMetodoChange("servidor");
        
        tab_anticipo.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());
        tab_anticipo.getColumna("login_ingre_solicitante").setValorDefecto(utilitario.getVariable("NICK"));
        tab_anticipo.getColumna("ip_ingre_solicitante").setValorDefecto(utilitario.getIp());
        tab_anticipo.getColumna("login_actua_solicitante").setValorDefecto(utilitario.getVariable("NICK"));
        tab_anticipo.getColumna("ip_actua_solicitante").setValorDefecto(utilitario.getIp());
        tab_anticipo.getColumna("fecha_actua_solicitante").setValorDefecto(utilitario.getFechaActual());
        
        tab_anticipo.getColumna("fecha_anticipo").setLectura(true);
        tab_anticipo.getColumna("ide_estado_anticipo").setLectura(true);
        
        tab_anticipo.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_anticipo.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        
        tab_anticipo.getColumna("ide_empleado_autorizador").setVisible(false);
        tab_anticipo.getColumna("ci_autorizador").setVisible(false);
        tab_anticipo.getColumna("autorizador").setVisible(false);
        tab_anticipo.getColumna("fecha_autorizacion").setVisible(false);
        tab_anticipo.getColumna("aprobado_autorizador").setVisible(false);
        tab_anticipo.getColumna("login_ingre_autorizador").setVisible(false);
        tab_anticipo.getColumna("ip_ingre_autorizador").setVisible(false);
        tab_anticipo.getColumna("login_actua_autorizador").setVisible(false);
        tab_anticipo.getColumna("ip_actua_autorizador").setVisible(false);
        tab_anticipo.getColumna("fecha_actua_autorizador").setVisible(false);
        tab_anticipo.getColumna("observacion_autorizador").setVisible(false);
        tab_anticipo.getColumna("login_ingre_solicitante").setVisible(false);
        tab_anticipo.getColumna("ip_ingre_solicitante").setVisible(false);
        tab_anticipo.getColumna("login_actua_solicitante").setVisible(false);
        tab_anticipo.getColumna("ip_actua_solicitante").setVisible(false);
        tab_anticipo.getColumna("fecha_actua_solicitante").setVisible(false);
        tab_anticipo.getColumna("valor_pagado").setVisible(false);
        tab_anticipo.getColumna("numero_cuotas_pagadas").setVisible(false);
        
        tab_anticipo.setTipoFormulario(true);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.agregarRelacion(tab_detalle);
        tab_anticipo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_anticipo);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setTabla("srh_detalle_anticipo", "ide_detalle_anticipo", 2);
        tab_detalle.getColumna("ide_periodo_descuento").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_detalle.getColumna("ide_periodo_descontado").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_detalle.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_detalle);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(tpa);
        gru.getChildren().add(tpd);
        pan_opcion.getChildren().add(gru);
        
    }

//LLENAR DATOS DE SOLICTANTE Y GARANTE
    /*POR IDENTIFICACION*/
        public void llenarDatosE(){
    if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {    
        TablaGenerica tab_dato = iAnticipos.empleados(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("solicitante", tab_dato.getValor("nombres"));
            tab_anticipo.setValor("rmu", tab_dato.getValor("ru"));
            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato.getValor("liquido_recibir"));
            tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
            utilitario.addUpdate("tab_anticipo");
        }else {
           TablaGenerica tab_dato1 = iAnticipos.trabajadores(tab_anticipo.getValor("ci_solicitante"));
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                    tab_anticipo.setValor("rmu", tab_dato1.getValor("su"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                  utilitario.agregarMensajeInfo("No existen Datos", "");
                  }
          }
    } else {
            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
        }
    }
    
    public void llenarGarante(){
    if (utilitario.validarCedula(tab_anticipo.getValor("ci_garante"))) {
        TablaGenerica tab_dato = iAnticipos.Garantemple(tab_anticipo.getValor("ci_garante"));
            if (!tab_dato.isEmpty()) {
                    tab_anticipo.setValor("garante", tab_dato.getValor("nombres"));
                    tab_anticipo.setValor("ide_empleado_garante", tab_dato.getValor("COD_EMPLEADO"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                      utilitario.agregarMensajeInfo("No existen Datos", "");
                      }
    } else {
            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
            }    
    }
    
    /*LLENAR DATOS POR CODIGO DE EMPLEADO*/
    
    public void llenarEmpleadoCodigo(){
        TablaGenerica tab_dato = iAnticipos.empleadoCodigo(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("solicitante", tab_dato.getValor("nombres"));
            tab_anticipo.setValor("rmu", tab_dato.getValor("ru"));
            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato.getValor("liquido_recibir"));
            tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
            utilitario.addUpdate("tab_anticipo");
        }else {
           TablaGenerica tab_dato1 = iAnticipos.trabajadoresCod(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                    tab_anticipo.setValor("rmu", tab_dato1.getValor("su"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                        utilitario.agregarMensajeInfo("No se encunetra en roles", "");
                      }
          }
    }
    
    public void llenarGaranteCodigo(){
      TablaGenerica tab_dato = iAnticipos.empleadoCodigo(Integer.parseInt(tab_anticipo.getValor("ide_empleado_garante")));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("garante", tab_dato.getValor("nombres"));
            tab_anticipo.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
            utilitario.addUpdate("tab_anticipo");
        }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
              }
    }
    
    /*POR NOMBRES Y APELLIDO*/    
        public void llenarPorNombre(){
      TablaGenerica tab_dato = iAnticipos.empleadoNombre(tab_anticipo.getValor("solicitante"));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
            tab_anticipo.setValor("rmu", tab_dato.getValor("ru"));
            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato.getValor("liquido_recibir"));
            tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
            utilitario.addUpdate("tab_anticipo");
        }else {
           TablaGenerica tab_dato1 = iAnticipos.trabajadorNombre(tab_anticipo.getValor("solicitante"));
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
                    tab_anticipo.setValor("rmu", tab_dato1.getValor("su"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                        utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                  }
          }  
    }
    
    public void llenarGaranteNom(){
        TablaGenerica tab_dato = iAnticipos.empleadoNom(tab_anticipo.getValor("garante"));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
            tab_anticipo.setValor("ide_empleado_garante", tab_dato.getValor("COD_EMPLEADO"));
            utilitario.addUpdate("tab_anticipo");
        }else {
                utilitario.agregarMensajeInfo("No existen Datos", "");
          }   
    }    
      
    /*VALIDACION DE VALOR A PERCIBIR EN EL ANTICIPO DE ACUERDO A REMUNERACION LIQUIDA ANTERIOR PERCIBIDA*/
    
        public void remuneracion(){
        double  dato1 = 0,dato2=0;
        
        dato2 = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"));
        dato1 = Double.parseDouble(tab_anticipo.getValor("valor_anticipo"));
        
        if((dato1/dato2)<=1){
         tab_anticipo.setValor("numero_cuotas_anticipo", "2");
         utilitario.addUpdate("tab_anticipo");
         utilitario.agregarMensajeInfo("Tiempo de Cobro por Monto", "2 Meses");
         servidor();
         tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);
                
        }else if((dato1/dato2)>1&&(dato1/dato2)<3){
            tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(false);
            utilitario.addUpdate("tab_anticipo");
            utilitario.agregarMensajeInfo("Ingresar Plazo de Cobro", "");
                
        }else{
            utilitario.agregarMensajeInfo("El Monto Excede Remuneracion", "Liquida Percibida Anterior");
        }
    }
    
        
    /*VALIDACION DE PERSONA QUE SOLICITA ANTICIPO*/    
    public void servidor(){
        
        Integer anos=0, dias=0,meses=0,anos1=0,dias1=0,meses1=0;
        
        TablaGenerica tab_dato = iAnticipos.empleado(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            
            if(tab_dato.getValor("id_distributivo").equals("1")){//VALIDACION DE EMPLEADOS
                utilitario.agregarMensajeInfo("Saludos", "Empleado");
                    if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=10){//VALIDACION POR DIA HASTA 10
                                if(tab_dato.getValor("cod_tipo").equals("3")|| tab_dato.getValor("cod_tipo").equals("4")|| tab_dato.getValor("cod_tipo").equals("8")|| tab_dato.getValor("cod_tipo").equals("10")){
                                        if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=15){
                                            
                                        }else{
                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "15 MESES");
                                            }
                                    }else{
                                            meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                            if(meses!=1){
                                                
                                                }else{
                                                    
                                                     }
                                        }
                        }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>11 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=28){//VALIDACION POR DIAS DEL 11 AL 28
                                        if(tab_dato.getValor("cod_tipo").equals("3")|| tab_dato.getValor("cod_tipo").equals("4")|| tab_dato.getValor("cod_tipo").equals("8")|| tab_dato.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=15){
                                            
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "15 MESES");
                                                            }
                                            }else{
                                                    meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                                    if(meses!=1){
                                                
                                                        }else{
                                                
                                                            }
                                                }
                                }
                }else if(tab_dato.getValor("id_distributivo").equals("2")){//VALIDACION DE TRABAJADORES
                        utilitario.agregarMensajeInfo("Saludos", "Trabajador");
                            if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=10){//VALIDACION POR DIA HASTA 10
                                        if(tab_dato.getValor("cod_tipo").equals("3")|| tab_dato.getValor("cod_tipo").equals("4")|| tab_dato.getValor("cod_tipo").equals("8")|| tab_dato.getValor("cod_tipo").equals("10")){
                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=15){
                                            
                                                    }else{
                                                            utilitario.agregarMensaje("Tiempo Maximo de Pago", "15 MESES");
                                                        }
                                            }else{
                                                    meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                                    if(meses!=1){
                                                
                                                        }else{

                                                            }
                                                }
                                }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>11 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=28){//VALIDACION POR DIAS DEL 11 AL 28
                                            if(tab_dato.getValor("cod_tipo").equals("3")|| tab_dato.getValor("cod_tipo").equals("4")|| tab_dato.getValor("cod_tipo").equals("8")|| tab_dato.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=15){
                                            
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "15 MESES");
                                                            }
                                                }else{
                                                        meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                                        if(meses!=1){
                                                
                                                            }else{

                                                                }
                                                    }
                                        }
                        }
            
            }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
               }
    }
        
            
    
    //CALCULAR MESES
    public static int calcularMes(Calendar cal1,Calendar cal2) {
    // conseguir la representacion de la fecha en milisegundos
     long milis1 = cal1.getTimeInMillis();
     long milis2 = cal2.getTimeInMillis();
     long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
     long diffSeconds = diff / 1000; // calcular la diferencia en segundos
     long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
     long diffHours = diffMinutes / 60 ; // calcular la diferencia en horas a
     long diffDays = diffHours / 24 ; // calcular la diferencia en dias
     long diffWeek = diffDays / 7 ; // calcular la diferencia en semanas
     long diffMounth = diffWeek / 4 ; // calcular la diferencia en meses

     return Integer.parseInt(String.valueOf(diffMounth));
    }
    
    @Override
    public void insertar() {
        tab_anticipo.insertar();
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
    
}
