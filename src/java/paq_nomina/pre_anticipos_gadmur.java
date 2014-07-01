/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.anticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_anticipos_gadmur extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
     
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_solici = new SeleccionTabla();
    private SeleccionTabla set_verif = new SeleccionTabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Efecto efecto1 = new Efecto();
    //
    String selec_mes = new String();
    
    //texto para busqueda de solicitud
    private Texto txt_ci = new Texto();
    private Texto txt_ide = new Texto();
    private Texto txt_ide1 = new Texto();
    
    //dialogo para reporte
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_d = new Grid();
    
     ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //clase logica
    @EJB
    private anticipos iAnticipos = (anticipos) utilitario.instanciarEJB(anticipos.class);
    
    public pre_anticipos_gadmur() {
        
        Boton bot4 = new Boton();
        bot4.setValue("ANULAR SOLICITUD");
        bot4.setIcon("ui-icon-closethick"); //pone icono de jquery temeroller
        bot4.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot4);
        
        Boton bot3 = new Boton();
        bot3.setValue("VERIFICAR SOLICITUD");
        bot3.setIcon("ui-icon-search"); //pone icono de jquery temeroller
        bot3.setMetodo("abrirVerificacion");
        bar_botones.agregarBoton(bot3);
        
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
        pan_opcion.setHeader("ANTICIPOS DE SUELDO");
        efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion.getChildren().add(efecto1);
        agregarComponente(pan_opcion);
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_anticipo", "ide_anticipo", 1);
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_anticipo.getColumna("ide_empleado_solicitante").setMetodoChange("llenarEmpleadoCodigo");
        tab_anticipo.getColumna("ide_empleado_garante").setMetodoChange("llenarGaranteCodigo");
        tab_anticipo.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_anticipo.getColumna("numero_cuotas_anticipo").setMetodoChange("servidor");
        tab_anticipo.getColumna("solicitante").setMetodoChange("llenarPorNombre");
        tab_anticipo.getColumna("ide_empleado_garante").setMetodoChange("llenarGaranteCod");
        tab_anticipo.getColumna("garante").setMetodoChange("llenarGaranteNom");
        
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
        
        /*          * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
         //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("BUSCAR ESTADO DE SOLICTUD"); //titulo
        dia_dialogoe.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("15%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        txt_ide.setSize(9);
        gri_busca.getChildren().add(new Etiqueta("# CEDULA: "));
        gri_busca.getChildren().add(txt_ide);
        Boton bot_placa = new Boton();
        bot_placa.setValue("Buscar");
        bot_placa.setIcon("ui-icon-search");
        bot_placa.setMetodo("buscarSolicitud");
        bar_botones.agregarBoton(bot_placa);
        gri_busca.getChildren().add(bot_placa);
        
        // CREACION DE TABLA SELECCION PARA COLUMNAS
        set_solici.setId("set_solici");
        set_solici.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_solici.setSeleccionTabla("SELECT ide_anticipo,fecha_anticipo,valor_anticipo,ci_solicitante,solicitante,ide_estado_anticipo,aprobado_solicitante,\n" +
                                      "aprobado_autorizador FROM srh_anticipo WHERE ide_anticipo=-1", "ide_anticipo");
        set_solici.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solici.getTab_seleccion().setRows(10);
        set_solici.setRadio();
        set_solici.getGri_cuerpo().setHeader(gri_busca);
        set_solici.getBot_aceptar().setMetodo("aceptoAnulacion");
        set_solici.setHeader("ELIGA SOLICITUD A ANULAR");
        agregarComponente(set_solici);
        
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        txt_ide1.setSize(9);
        gri_busca1.getChildren().add(new Etiqueta("# CEDULA: "));
        gri_busca1.getChildren().add(txt_ide1);
        Boton bot_placa1 = new Boton();
        bot_placa1.setValue("Buscar");
        bot_placa1.setIcon("ui-icon-search");
        bot_placa1.setMetodo("buscarVerificion");
        bar_botones.agregarBoton(bot_placa1);
        gri_busca1.getChildren().add(bot_placa1);
        
        // CREACION DE TABLA SELECCION PARA COLUMNAS
        set_verif.setId("set_verif");
        set_verif.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_verif.setSeleccionTabla("SELECT ide_anticipo,fecha_anticipo,valor_anticipo,ci_solicitante,solicitante,ide_estado_anticipo,aprobado_autorizador FROM srh_anticipo WHERE ide_anticipo=-1", "ide_anticipo");
        set_verif.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_verif.getTab_seleccion().setRows(10);
        set_verif.setRadio();
        set_verif.getGri_cuerpo().setHeader(gri_busca1);
        set_verif.getBot_aceptar().setMetodo("aceptoVerificacion");
        set_verif.setHeader("VERIFICAR SOLICITUD");
        agregarComponente(set_verif);
        
    }

    //ANULACION DE SOLICITUD
    public void abrirBusqueda() {
        set_solici.dibujar();
        set_solici.getTab_seleccion().limpiar();
    }
    
    public void buscarSolicitud() {
            if (txt_ide.getValue() != null && txt_ide.getValue().toString().isEmpty() == false) {
                    set_solici.getTab_seleccion().setSql("SELECT ide_anticipo,fecha_anticipo,valor_anticipo,ci_solicitante,solicitante,ide_estado_anticipo,aprobado_solicitante,\n" +
                                                            "aprobado_autorizador FROM srh_anticipo WHERE ci_solicitante like '"+txt_ide.getValue()+"'");
                    set_solici.getTab_seleccion().ejecutarSql();
                }else {
                    utilitario.agregarMensajeInfo("Debe ingresar Cedula", "");
                    }
    }
    
    public void aceptoAnulacion() {
        if (set_solici.getValorSeleccionado() != null) {
                iAnticipos.anularSolicitud(Integer.parseInt(set_solici.getValorSeleccionado()));
                utilitario.agregarMensaje("Solicitud", "Borrada");
                set_solici.cerrar();
            } else {
                    utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
                    }
    }
    
    //VERIFICAR SOLICITUD
    
    public void abrirVerificacion() {
        set_verif.dibujar();
        set_verif.getTab_seleccion().limpiar();
    }
    
    public void buscarVerificion() {
            if (txt_ide1.getValue() != null && txt_ide1.getValue().toString().isEmpty() == false) {
                    set_verif.getTab_seleccion().setSql("SELECT ide_anticipo,fecha_anticipo,valor_anticipo,ci_solicitante,solicitante,ide_estado_anticipo,\n" +
                                                            "aprobado_autorizador FROM srh_anticipo WHERE aprobado_autorizador = 't' and ci_solicitante like '"+txt_ide1.getValue()+"'");
                    set_verif.getTab_seleccion().ejecutarSql();
                }else {
                    utilitario.agregarMensajeInfo("Debe ingresar Cedula", "");
                    }
    }
    
    public void aceptoVerificacion() {
        if (set_verif.getValorSeleccionado() != null) {
                utilitario.agregarMensaje("Solicitud", "Verificada");
                set_verif.cerrar();
            } else {
                    utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
                    }
    }
    
    
    //Mostrar Datos de Solicitante x numero de cedula
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
    
    //Mostrar Datos de Garante x numero de cedula
    public void llenarGarante(){
    if (utilitario.validarCedula(tab_anticipo.getValor("ci_garante"))) {
        TablaGenerica tab_dato = iAnticipos.empleado(tab_anticipo.getValor("ci_garante"));
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

    //Mostrar datos de garante x nombre
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
    
    //Mostrar datos garante x codigo
    public void llenarGaranteCod(){
        TablaGenerica tab_dato = iAnticipos.empleadoCod(tab_anticipo.getValor("ide_empleado_garante"));
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
            tab_anticipo.setValor("garante", tab_dato.getValor("nombres"));
            utilitario.addUpdate("tab_anticipo");
        }else {
                utilitario.agregarMensajeInfo("No existen Datos", "");
          }   
    }    
        
    //Mostrar Datos de empleado x codigo
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
    
    //Mostrar Datos de garante x codigo
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
    
    //Busqueda por apellido y nombre
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
       //monto de anticipo de acuerdo ultima remuneracion liquida recibida
    public void remuneracion(){
        double  dato1,dato2;
        //variable
        String mes, anio,dia, fecha;
        Integer calculo1,calculo2,calculo3;
        
        dato2 = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"));
        dato1 = Double.parseDouble(tab_anticipo.getValor("valor_anticipo"));
        
        anio = String.valueOf(utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO")));
        if((dato1/dato2)<=1){
         tab_anticipo.setValor("numero_cuotas_anticipo", "2");
         utilitario.addUpdate("tab_anticipo");
         utilitario.agregarMensajeInfo("Por el Monto", "Cobro de Anticipo 2 meses");
         servidor();
         tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);
         calculo1 = 12 -  Integer.parseInt(mes);
         calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                if(calculo2<=0){
                    calculo3 = calculo2*-1;
                    fecha = String.valueOf(Integer.parseInt(anio)+1)+"-"+String.valueOf(calculo3+1)+"-"+dia;
                    tab_anticipo.setValor("numero_cuotas_anticipo", fecha);
                    utilitario.addUpdate("tab_anticipo");
                    utilitario.agregarMensajeInfo("Por el Monto", "Cobro de Anticipo 2 meses");
                     servidor();
                     tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);
                } 
        }else if((dato1/dato2)>1&&(dato1/dato2)<3){
            tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(false);
            utilitario.addUpdate("tab_anticipo");
            utilitario.agregarMensajeInfo("Ingresar", "Plazo de Cobro de Anticipo");
                
        }else{
            utilitario.agregarMensajeInfo("Monto Excede Capacidad de Pago", "");
        }
    }     
    
    //calculo automatico de tiempo de inicio y expiracion de anticipo para empleados contrato mas de un año
    public void llenarFecha(){
        String mes, anio,dia,fecha,busca;
        Integer calculo,calculo1,calculo2,calculo3;
        
        anio = String.valueOf(utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO")));
        if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=10){
                 if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
                        calculo1 = 12 -  Integer.parseInt(mes);
                        calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                               if(calculo2<=0){
                                   TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                   if (!tab_dato.isEmpty()) {
                                       fecha= tab_dato.getValor("ide_periodo_anticipo");
                                       tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                       TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(mes)-1),String.valueOf(Integer.parseInt(anio)+1));
                                       if (!tab_dato1.isEmpty()) {
                                           fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                           utilitario.addUpdate("tab_anticipo");
                                       } else {
                                               utilitario.agregarMensajeInfo("No existen Datos", "");
                                               }
                                   }else {
                                          utilitario.agregarMensajeInfo("No existen Datos", "");
                                          }
                               }       
                       }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
                           calculo1 = 12 -  Integer.parseInt(mes);
                           calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                   if(calculo2<0){
                                   calculo3 = calculo2*-1;
                                   TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                    if (!tab_dato.isEmpty()) {
                                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                                        tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                         utilitario.addUpdate("tab_anticipo");
                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo3-1),String.valueOf(Integer.parseInt(anio)+1));
                                            if (!tab_dato1.isEmpty()) {
                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                utilitario.addUpdate("tab_anticipo");
                                            } else {
                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                    }
                                    }else {
                                           utilitario.agregarMensajeInfo("No existen Datos", "");
                                           }                 
                                   }if(calculo2>=0){
                                       calculo= 12 - calculo2;
                                        TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                            if (!tab_dato.isEmpty()) {
                                                fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                 utilitario.addUpdate("tab_anticipo");
                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }                   
                                   }
                       }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                           calculo1 = 12 -  Integer.parseInt(mes);
                           calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                           if(calculo2<0){
                                   calculo3 = calculo2*-1;
                                   TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                   if (!tab_dato.isEmpty()) {
                                       fecha= tab_dato.getValor("ide_periodo_anticipo");
                                       tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                       TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo3-1),String.valueOf(Integer.parseInt(anio)+1));
                                       if (!tab_dato1.isEmpty()) {
                                           fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                           utilitario.addUpdate("tab_anticipo");
                                       } else {
                                               utilitario.agregarMensajeInfo("No existen Datos", "");
                                               }
                                   }else {
                                          utilitario.agregarMensajeInfo("No existen Datos", "1");
                                          }
                           }

                       } else {
                              utilitario.agregarMensajeInfo("Tiempo Maximo de Anticipo", "18 Meses");
                              }
        }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>=11 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))< 28 ){
                     if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
                            calculo1 = 12 -  Integer.parseInt(mes);
                            calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                   if(calculo2<=0){
                                       TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)+1));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                               utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }
                                   }       
                           }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
                               calculo1 = 12 -  Integer.parseInt(mes);
                               calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                       if(calculo2<0){
                                       calculo3 = calculo2*-1;
                                       TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo3),String.valueOf(Integer.parseInt(anio)+1));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                               utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }                 
                                       }if(calculo2>=0){
                                           calculo= 12 - calculo2;
                                            TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo),String.valueOf(Integer.parseInt(anio)));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                               utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }                   
                                       }
                           }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                               calculo1 = 12 -  Integer.parseInt(mes);
                               calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                               if(calculo2<0){
                                       calculo3 = calculo2*-1;
                                       TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo3),String.valueOf(Integer.parseInt(anio)+1));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                               utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "1");
                                              }
                               }

                           } else {
                                  utilitario.agregarMensajeInfo("Tiempo Maximo de Anticipo", "18 Meses");
                                  }
        } ////
    }
    
   //calculo automatico de tiempo de inicio y expiracion de anticipo para empleados y trabajadores contrato menos de un año
    public void llenarFecha1(){
         String mes, anio,dia, fecha;
        Integer calculo,calculo1,calculo2,calculo3;
        
        anio = String.valueOf(utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO")));
        
        if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=10){
        if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
         calculo1 = 12 -  Integer.parseInt(mes);
         calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                if(calculo2<=0){
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                        tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                         utilitario.addUpdate("tab_anticipo");
                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(mes)-1),String.valueOf(Integer.parseInt(anio)+1));
                        if (!tab_dato1.isEmpty()) {
                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                            tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                            utilitario.addUpdate("tab_anticipo");
                        } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                    }else {
                           utilitario.agregarMensajeInfo("No existen Datos", "");
                           }
                }       
        }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
            calculo1 = 12 -  Integer.parseInt(mes);
            calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                    if(calculo2<0){
                        calculo3 = calculo2*-1;
                        System.err.println(calculo2*-1);
                        System.err.println(calculo3);
                        TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                        if (!tab_dato.isEmpty()) {
                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                            tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                            utilitario.addUpdate("tab_anticipo");
                            Integer cal_mes = Integer.parseInt(mes)+ Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                            if(calculo3==1){
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(cal_mes-calculo3),String.valueOf(Integer.parseInt(anio)));
                                    if (!tab_dato1.isEmpty()) {
                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                    } else {
                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                            }
                            }else if(calculo3>1){
                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(cal_mes-12),String.valueOf(Integer.parseInt(anio)+1));
                                        if (!tab_dato1.isEmpty()) {
                                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                            tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                        } else {
                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                }
                                    }   
                        }else {
                               utilitario.agregarMensajeInfo("No existen Datos", "");
                           }                 
                    }if(calculo2>=0){
                        calculo= 12 - calculo2;
                         TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato.isEmpty()) {
                                fecha= tab_dato.getValor("ide_periodo_anticipo");
                                tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                 utilitario.addUpdate("tab_anticipo");
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo-1),String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tab_anticipo");
                                } else {
                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                        }
                            }else {
                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                   }                   
                    }
        }else {
               utilitario.agregarMensajeInfo("Tiempo corresponde al tipo de contrato", "");
               }            
        }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>=11 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<28){
                      if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
                            calculo1 = 12 -  Integer.parseInt(mes);
                            calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                   if(calculo2<=0){
                                       TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)+1));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                               utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }
                                   }       
                           }if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
                               calculo1 = 12 -  Integer.parseInt(mes);
                               calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                       if(calculo2<0){
                                           calculo3 = calculo2*-1;
                                            System.err.println(calculo2*-1);
                                           System.err.println(calculo3);
                                           TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                           if (!tab_dato.isEmpty()) {
                                               fecha= tab_dato.getValor("ide_periodo_anticipo");
                                               tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                utilitario.addUpdate("tab_anticipo");
                                               TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo3),String.valueOf(Integer.parseInt(anio)+1));
                                               if (!tab_dato1.isEmpty()) {
                                                   fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                   tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                   utilitario.addUpdate("tab_anticipo");
                                               } else {
                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                       }
                                           }else {
                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }                 
                                       }if(calculo2>=0){
                                           calculo= 12 - calculo2;
                                            TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                       if (!tab_dato.isEmpty()) {
                                           fecha= tab_dato.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                           TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo),String.valueOf(Integer.parseInt(anio)));
                                           if (!tab_dato1.isEmpty()) {
                                               fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                           tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                           utilitario.addUpdate("tab_anticipo");
                                           } else {
                                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                                   }
                                       }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }                   
                                       }
                           }else {
                                  utilitario.agregarMensajeInfo("Tiempo corresponde al tipo de contrato", "");
                                  }
        }//
    }
    
    //Validacion de tipo de servidor que solicita anticipo
    public void servidor(){
        //variable
    String mes, anio,dia, fecha,fechai;
    Integer calculo,calculo1,calculo2,calculo3,mensaje,mes1,anio1,dia1;
    
        anio = String.valueOf(utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO")));
        fechai= String.valueOf(Integer.parseInt(anio)-1) +"-"+mes+"-"+dia;
        
        anio1 =utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO"));
        mes1 = utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO"));
        dia1 = utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"));
        
        Integer anos, dias,meses,anos1,dias1,meses1;
        
        
        TablaGenerica tab_dato = iAnticipos.empleado(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            
            if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=10){//
                    if(tab_dato.getValor("id_distributivo").equals("1")){
                        utilitario.agregarMensajeInfo("Saludos", "Empleado");
                        anos=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                        dias=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                        meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));               
                        if(calcularAnios(new GregorianCalendar(anos,dias,meses))>1){
                            llenarFecha();
                            cuotas();
                            }else
                            if(meses!=1){
                                anos1=1+utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                meses1=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                   if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))){
                                       llenarFecha1();
                                       cuotas();
                                       utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                   }else{
                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));
                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));  
                                   }
                            }else{
                                            anos1=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                            dias1=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                                            if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,(mes1-1),28),new GregorianCalendar(anos1,12,28))){
                                                llenarFecha1();
                                                cuotas();
                                                utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                            }else{
                                                    tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(1+calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                }
                            }

                    }else if(tab_dato.getValor("id_distributivo").equals("2")){
                         utilitario.agregarMensajeInfo("Saludos", "Trabajador");
                        anos=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                        dias=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                        meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));               
                        if(calcularAnios(new GregorianCalendar(anos,dias,meses))>1){
                            System.err.println("hola");
                            llenarFecha();
                            System.err.println("hola1");
                            cuotas();
                            System.err.println("hola2");
                            }else
                            if(meses!=1){
                                anos1=1+utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                meses1=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));
                                   if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))){
                                       System.err.println("hola3");
                                       llenarFecha1();
                                       cuotas();
                                       utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                   }else{
                                       System.err.println("hola4");
                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));
                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));  
                                   }
                            }else{
                                    anos1=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                    dias1=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,(mes1-1),28),new GregorianCalendar(anos1,12,28))){
                                              System.err.println("hola5");
                                            llenarFecha1();
                                            cuotas();
                                              utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                            }else{
                                                System.err.println("hola6");
                                                    tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(1+calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                }
                            }
                    }
                
            }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>11 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=28){
                        if(tab_dato.getValor("id_distributivo").equals("1")){
                          utilitario.agregarMensajeInfo("Saludos", "Empleado");
                          anos=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                          dias=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                          meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));               
                          if(calcularAnios(new GregorianCalendar(anos,dias,meses))>1){
                              llenarFecha();
                              cuotas();
                              }else
                              if(meses!=1){
                                  anos1=1+utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                  meses1=utilitario.getMes(tab_dato.getValor("fecha_ingreso"))-1;
                                     if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))){
                                         llenarFecha1();
                                         cuotas();
                                         utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                     }else{
                                          tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));
                                          utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));  
                                     }
                              }else{
                                              anos1=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                              dias1=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                                              if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))){
                                                  llenarFecha1();
                                                  cuotas();
                                                  utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                              }else{
                                                      tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                      utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));   
                                              }
                              }
                      }else if(tab_dato.getValor("id_distributivo").equals("2")){
                           utilitario.agregarMensajeInfo("Saludos", "Trabajador");
                          anos=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                          dias=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                          meses=utilitario.getMes(tab_dato.getValor("fecha_ingreso"));               
                          if(calcularAnios(new GregorianCalendar(anos,dias,meses))>1){
                              llenarFecha();
                              cuotas();
                              }else
                              if(meses!=1){
                                  anos1=1+utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                  meses1=utilitario.getMes(tab_dato.getValor("fecha_ingreso"))-1;
                                     if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))){
                                         llenarFecha1();
                                         cuotas();
                                         utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                     }else{   
                                              tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));
                                              utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,meses1,28))));
                                          }
                              }else{
                                              anos1=utilitario.getAnio(tab_dato.getValor("fecha_ingreso"));
                                              dias1=utilitario.getDia(tab_dato.getValor("fecha_ingreso"));
                                              if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))){
                                                  llenarFecha1();
                                                  cuotas();
                                                  utilitario.addUpdateTabla(tab_anticipo,"numero_cuotas_anticipo","");
                                              }else{  
                                                      tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                      utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anio1,mes1,28),new GregorianCalendar(anos1,12,28))));
                                                  }
                              }
                      }
            }
        }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
               }
    }
    
    //calculo de cuotas a pagar y cuota especial
    public void cuotas(){
        tramite();
        Integer rango;
        double valora,valorm,media,rmu,valan;
        rmu =Double.parseDouble(tab_anticipo.getValor("rmu"));
        valan= Double.parseDouble(tab_anticipo.getValor("valor_anticipo"));
        
        TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
        if (!tab_dato.isEmpty()) {
            rango = Integer.parseInt(tab_dato.getValor("periodo"))+Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
            if(rango > 12){
                valora= ((rmu*70)/100);
                valorm = (valan-valora)/(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))-1);
                media = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"))/2;
                if((valorm-media)<=0){
                    tab_anticipo.setValor("valor_cuota_adicional", String.valueOf(valora));
                    tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                    utilitario.addUpdate("tab_anticipo");
                }
            }else if(rango <= 12){
                    valora = Double.parseDouble(tab_anticipo.getValor("valor_anticipo")) / Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                    media = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"))/2;
                    if( media > valora){
                        tab_anticipo.setValor("valor_cuota_adicional", "NULL");
                        tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora*100)/100));
                        utilitario.addUpdate("tab_anticipo");
                    } else if( media < valora){
                                tab_anticipo.setValor("valor_cuota_adicional", "NULL");
                                tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora*100)/100));
                                utilitario.addUpdate("tab_anticipo");
                    }
                }
            
        }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
               }
    }
    
   //calcular tiempo de servicio              
public static int calcularAnios(Calendar fechaNac) {
    Calendar fechaActual = Calendar.getInstance();
    // Cálculo de las diferencias.
    int anios = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
    int meses = fechaActual.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
    int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
  
    if(meses < 0 // Aún no es el mes 
       || (meses==0 && dias < 0)) { // o es el mes pero no ha llegado el día.
        anios--;
    }
    return anios;
}

//calcular meses disponibles
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

//busqueda de mes para asignacion de periodo
 public String meses(Integer numero){
     
    		switch (numero){
    		case 12:
    				selec_mes = "Diciembre";
    				break;
                case 11:
    				selec_mes = "Noviembre";
    				break;
    		case 10:
    				selec_mes = "Octubre";
    				break;
                case 9:
    				selec_mes = "Septiembre";
    				break;
    		case 8:
    				selec_mes = "Agosto";
    				break;
    		case 7:
    				selec_mes = "Julio";
    				break;
                case 6:
    				selec_mes = "Junio";
    				break;
    		case 5:
    				selec_mes = "Mayo";
    				break;
                case 4:
    				selec_mes = "Abril";
    				break;
    		case 3:
    				selec_mes = "Marzo";
    				break;
    		case 2:
    				selec_mes = "Febrero";
    				break;
    		case 1:
    				selec_mes = "Enero";
    				break;
    		}
    		return selec_mes;
    }

    @Override
    public void insertar() {
      tab_anticipo.insertar();
    }

    @Override
    public void guardar() {
        Integer cedula, cedula1;
        TablaGenerica tab_dato = iAnticipos.validar(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            cedula = Integer.parseInt(tab_anticipo.getValor("ci_solicitante"));
            cedula1 = Integer.parseInt(tab_dato.getValor("ci_solicitante"));
            if(tab_anticipo.getValor("ci_solicitante").equals(tab_dato.getValor("ci_solicitante"))){
                if(tab_dato.getValor("ide_estado_anticipo").equals("4")){
                    if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
//                            if(tab_anticipo.getValor("APROBADO_SOLICITANTE")!= NULL){
                                tab_anticipo.guardar();
                                con_postgres.guardarPantalla();
//                            } else {
//                                     utilitario.agregarMensajeError("Debe aprobar la solicitud", "");
//                            }
                        
                    } else {
                            utilitario.agregarMensajeError("El Número de Cedula no es válido", "");
                            }
                }else{
                    utilitario.agregarMensajeError("Posee actualmente un Solicitud Ingresada", "En proceso");
                    }
           }else{
                utilitario.agregarMensajeError("Posee actualmente un Solicitud Ingresada", "En proceso");
                }
        }else{
            if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
                //                            if(tab_anticipo.getValor("APROBADO_SOLICITANTE")!= NULL){
                                                    tab_anticipo.guardar();
                                                    con_postgres.guardarPantalla();
                    //                            } else {
//                                     utilitario.agregarMensajeError("Debe aprobar la solicitud", "");
//                            }
                } else {
                    utilitario.agregarMensajeError("El Número de Cedula no es válido", "");
                }
        }
    }

    @Override
    public void eliminar() {
        Integer cedula, cedula1;
        TablaGenerica tab_dato = iAnticipos.validar(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            cedula = Integer.parseInt(tab_anticipo.getValor("ci_solicitante"));
            cedula1 = Integer.parseInt(tab_dato.getValor("ci_solicitante"));
            if(tab_anticipo.getValor("ci_solicitante").equals(tab_dato.getValor("ci_solicitante"))){
                if(tab_dato.getValor("ide_estado_anticipo").equals("4")||tab_dato.getValor("ide_estado_anticipo").equals("1")){
                    tab_anticipo.eliminar();
                }else{
                    utilitario.agregarMensajeError("Solicitud", "En proceso");
                    }
           }else{
                utilitario.agregarMensajeError("Solicitud", "En proceso");
                }
        }else{
            tab_anticipo.eliminar();
        }
    }

    /*  LLAMADA DE REPORTE  SOLICITUD */
        
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ESTADO DE SOLICITUD":
                dia_dialogoe.Limpiar();
                txt_ci.setSize(20);
                grid_de.getChildren().add(new Etiqueta("C.I. EMPLEADO O TRABAJADOR:"));
                grid_de.getChildren().add(txt_ci);
                dia_dialogoe.setDialogo(grid_de);
                dia_dialogoe.dibujar();
           break;
           case "DETALLE DE ANTICIPO":
                dia_dialogoe.Limpiar();
                txt_ci.setSize(20);
                grid_de.getChildren().add(new Etiqueta("C.I. EMPLEADO O TRABAJADOR:"));
                grid_de.getChildren().add(txt_ci);
                dia_dialogoe.setDialogo(grid_de);
                dia_dialogoe.dibujar();
           break;
        }
    }     
       
    public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "ESTADO DE SOLICITUD":
                    p_parametros.put("pide_ci",txt_ci.getValue()+"");
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
               case "DETALLE DE ANTICIPO":
                    p_parametros.put("pide_ci",txt_ci.getValue()+"");
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }

    }
    
    
   public void tramite(){
      TablaGenerica tab_dato = iAnticipos.estado();
        if (!tab_dato.isEmpty()) {
            tab_anticipo.setValor("ide_estado_anticipo", tab_dato.getValor("ide_estado_tipo"));
            utilitario.addUpdate("tab_anticipo");
         }else {
          utilitario.agregarMensajeInfo("No existen Datos", "");
          }
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

    public SeleccionTabla getSet_solici() {
        return set_solici;
    }

    public void setSet_solici(SeleccionTabla set_solici) {
        this.set_solici = set_solici;
    }

    public SeleccionTabla getSet_verif() {
        return set_verif;
    }

    public void setSet_verif(SeleccionTabla set_verif) {
        this.set_verif = set_verif;
    }
    
}
