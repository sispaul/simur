/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.anticipos;
import static paq_nomina.pre_solicitud_anticipos.calcularAnios;
import static paq_nomina.pre_solicitud_anticipos.calcularMes;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;


/**
 *
 * @author KEJA
 */
public class pre_anticipos_gadmur extends Pantalla{

    
        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    private Texto txt_cedula = new Texto();
    private Combo cmb_usu = new Combo();
     private Calendario cal_fecha = new Calendario();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
        
        // DIALOGO DE ACCIÓN
    private Dialogo dia_dialogou = new Dialogo();
    private Dialogo dia_dialogoi = new Dialogo();
    private Grid grid_du = new Grid();
    private Grid grid_di = new Grid();
    
    @EJB
    private anticipos iAnticipos = (anticipos) utilitario.instanciarEJB(anticipos.class);
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    public pre_anticipos_gadmur() {
                //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT  \n" +
                                    "ide_anticipo,  \n" +
                                    "ide_empleado_solicitante,  \n" +
                                    "ci_solicitante,  \n" +
                                    "solicitante,  \n" +
                                    "ide_estado_anticipo  \n" +
                                    "FROM srh_anticipo  \n" +
                                    "where ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'INGRESADO')OR\n" +
                                    "ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'AUTORIZADO')OR\n" +
                                    "ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'COBRADO')\n" +
                                    "order by fecha_anticipo");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);

        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_anticipo", "ide_anticipo", 1);
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_anticipo.getColumna("ide_empleado_solicitante").setMetodoChange("llenarEmpleadoCodigo");
        tab_anticipo.getColumna("ide_empleado_garante").setMetodoChange("llenarGaranteCodigo");
//        tab_anticipo.getColumna("solicitante").setMetodoChange("llenarPorNombre");
//        tab_anticipo.getColumna("garante").setMetodoChange("llenarGaranteNom");
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
        tab_anticipo.getColumna("id_distributivo").setLectura(true);
        tab_anticipo.getColumna("garante").setLectura(true);
        tab_anticipo.getColumna("solicitante").setLectura(true);
        
        tab_anticipo.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_anticipo.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_anticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        
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
        tab_anticipo.getColumna("observacion_solicitante").setVisible(false);
        
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
        Division div = new Division();
        div.dividir2(tpa, tpd, "55%", "h");
        agregarComponente(div);
        
        //CONFIGURACIÓN DE OBJETO REPORTE
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        cmb_usu.setId("cmb_usuario");
        cmb_usu.setCombo("SELECT IDE_USUA,NICK_USUA FROM SIS_USUARIO WHERE IDE_PERF <> 1");
        cmb_usu.eliminarVacio();
        
                //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialogou.setId("dia_dialogou");
        dia_dialogou.setTitle("REPORTES DE ANTICIPOS DIARIO"); //titulo
        dia_dialogou.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogou.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogou.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogou.getBot_aceptar().setMetodo("aceptoDescuentos");
        grid_du.setColumns(4);
        agregarComponente(dia_dialogou);
        
        dia_dialogoi.setId("dia_dialogoi");
        dia_dialogoi.setTitle("REPORTE DE ESTADO DE ANTICIPO"); //titulo
        dia_dialogoi.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogoi.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogoi.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoi.getBot_aceptar().setMetodo("aceptoDescuentos");
        grid_di.setColumns(4);
        agregarComponente(dia_dialogoi);
        
    }

    //PARA BUSQUEDA DE SOLICITUD INGRESADA
    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_anticipo.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_anticipo");
        }
    }
    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
    
    //LLENAR DATOS DE SOLICTANTE Y GARANTE     /*POR IDENTIFICACION*/
    public void llenarDatosE(){//SOLICITANTE
        TablaGenerica tab_dato = iAnticipos.VerifEmpleid(tab_anticipo.getValor("ci_solicitante"));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Pendiente");
       }else {
              if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {    
                    TablaGenerica tab_dato1 = iAnticipos.empleados(tab_anticipo.getValor("ci_solicitante"));//empleados
                    if (!tab_dato1.isEmpty()) {
                        tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                        tab_anticipo.setValor("rmu", tab_dato1.getValor("ru"));
                        tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                        tab_anticipo.setValor("ide_empleado_solicitante", tab_dato1.getValor("COD_EMPLEADO"));
                        tab_anticipo.setValor("id_distributivo", tab_dato1.getValor("id_distributivo_roles"));
                        utilitario.addUpdate("tab_anticipo");
                    }else {
                       TablaGenerica tab_dato2 = iAnticipos.trabajadores(tab_anticipo.getValor("ci_solicitante"));//trabajadores
                            if (!tab_dato2.isEmpty()) {
                                tab_anticipo.setValor("solicitante", tab_dato2.getValor("nombres"));
                                tab_anticipo.setValor("rmu", tab_dato2.getValor("su"));
                                tab_anticipo.setValor("rmu_liquido_anterior", tab_dato2.getValor("liquido_recibir"));
                                tab_anticipo.setValor("ide_empleado_solicitante", tab_dato2.getValor("COD_EMPLEADO"));
                                tab_anticipo.setValor("id_distributivo", tab_dato2.getValor("id_distributivo_roles"));
                                utilitario.addUpdate("tab_anticipo");
                            }else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                      }
                } else {
                        utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                    }
              }
    }
 
    public void llenarGarante(){//GARANTE
        TablaGenerica tab_dato = iAnticipos.VerifGaranteid(tab_anticipo.getValor("ci_garante"));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Garante No Disponible", "");
       }else {
                   if (utilitario.validarCedula(tab_anticipo.getValor("ci_garante"))) {
                        TablaGenerica tab_dato1 = iAnticipos.Garantemple(tab_anticipo.getValor("ci_garante"));
                           if (!tab_dato1.isEmpty()) {
                                   tab_anticipo.setValor("garante", tab_dato1.getValor("nombres"));
                                   tab_anticipo.setValor("ide_empleado_garante", tab_dato1.getValor("COD_EMPLEADO"));
                                   utilitario.addUpdate("tab_anticipo");
                                }else {
                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                      }    
                    } else {
                            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                            }
              }
    }
    
    /*LLENAR DATOS POR CODIGO DE EMPLEADO*/
    public void llenarEmpleadoCodigo(){
        
       TablaGenerica tab_dato = iAnticipos.VerifEmplecod(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Pendiente");
       }else {
               TablaGenerica tab_dato1 = iAnticipos.empleadoCodigo(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                    tab_anticipo.setValor("rmu", tab_dato1.getValor("ru"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ci_solicitante", tab_dato1.getValor("cedula_pass"));
                    tab_anticipo.setValor("id_distributivo", tab_dato1.getValor("id_distributivo_roles"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                   TablaGenerica tab_dato2 = iAnticipos.trabajadoresCod(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));//trabajadores
                        if (!tab_dato2.isEmpty()) {
                            tab_anticipo.setValor("solicitante", tab_dato2.getValor("nombres"));
                            tab_anticipo.setValor("rmu", tab_dato2.getValor("su"));
                            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato2.getValor("liquido_recibir"));
                            tab_anticipo.setValor("ci_solicitante", tab_dato2.getValor("cedula_pass"));
                            tab_anticipo.setValor("id_distributivo", tab_dato2.getValor("id_distributivo_roles"));
                            utilitario.addUpdate("tab_anticipo");
                        }else {
                                utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                              }
                  }
              }
    }
    
    public void llenarGaranteCodigo(){

       TablaGenerica tab_dato = iAnticipos.VerifGarantecod(Integer.parseInt(tab_anticipo.getValor("ide_empleado_garante")));
       if (!tab_dato.isEmpty()) {
           utilitario.agregarMensajeInfo("Garante No Disponible", "");
       }else {
              TablaGenerica tab_dato1 = iAnticipos.empleadoCodigo(Integer.parseInt(tab_anticipo.getValor("ide_empleado_garante")));
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("garante", tab_dato1.getValor("nombres"));
                    tab_anticipo.setValor("ci_garante", tab_dato1.getValor("cedula_pass"));
                    utilitario.addUpdate("tab_anticipo");
                }else {
                       utilitario.agregarMensajeInfo("No existen Datos", "");
                      }
              }
    }
    
    //VALIDACION DE VALOR A PERCIBIR EN EL ANTICIPO DE ACUERDO A REMUNERACION LIQUIDA ANTERIOR PERCIBIDA
    
    public void remuneracion(){
        double  dato1 = 0,dato2=0,compara=0; 
        dato2 = Double.parseDouble(tab_anticipo.getValor("rmu"));
        dato1 = Double.parseDouble(tab_anticipo.getValor("valor_anticipo"));
       compara = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"));
       
       tab_anticipo.setValor("valor_cuota_mensual", "NULL");
       tab_anticipo.setValor("numero_cuotas_anticipo", "NULL");
       tab_anticipo.setValor("valor_cuota_adicional", "NULL");
       
     if(compara>0){
            if((dato1/dato2)<=1){
                tab_anticipo.setValor("numero_cuotas_anticipo", "2");
                utilitario.addUpdate("tab_anticipo");
                utilitario.agregarMensajeInfo("Tiempo de Cobro por Monto", "2 Meses");

                if(tab_anticipo.getValor("numero_cuotas_anticipo").equals("2")){
                    llenarFecha();
                    cuotas();
             }
             tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);

            }else if((dato1/dato2)>1&&(dato1/dato2)<3){
                    tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(false);
                    utilitario.addUpdate("tab_anticipo");
                    utilitario.agregarMensajeInfo("Ingresar Plazo de Cobro", "");

            }else{
                utilitario.agregarMensajeInfo("El Monto Excede Remuneracion", "Liquida Percibida");
                        tab_anticipo.setValor("valor_cuota_mensual", "NULL");
                        tab_anticipo.setValor("numero_cuotas_anticipo", "NULL");
                        tab_anticipo.setValor("valor_cuota_adicional", "NULL");
                        utilitario.addUpdate("tab_anticipo");
            }
    }else{
            utilitario.agregarMensajeError("Remuneracion Anterior", "Saldo Negativo");
            tab_anticipo.getColumna("valor_anticipo").setLectura(true);
            tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);
            utilitario.addUpdate("tab_anticipo");
        }
    }
   
        /*VALIDACION DE PERSONA QUE SOLICITA ANTICIPO*/    
    public void servidor(){
        
        Integer anos=0, dias=0,meses=0,mesesf=0,aniosf=0,diasf=0,meses_a=0,anios_a=0,dias_a=0;
        
        TablaGenerica tab_dato = iAnticipos.empleado(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            anios_a = utilitario.getAnio(utilitario.getFechaActual());
            meses_a = utilitario.getMes(utilitario.getFechaActual());
            dias_a = utilitario.getDia(utilitario.getFechaActual());
            TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
            if (!tab_datof.isEmpty()) {
            if(tab_dato.getValor("id_distributivo").equals("1")){//VALIDACION DE EMPLEADOS
                    if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                                            anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                            dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                            meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                if(tab_datof.getValor("cod_tipo").equals("4")||tab_datof.getValor("cod_tipo").equals("10")){
                                        if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                                        llenarFecha();
                                        cuotas();
                                        }else{
                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                            }
                                    }else if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=12){
                                                    llenarFecha();
                                                    cuotas();    
                                                    }else{
                                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                                        }
                                                }else{
                                                        mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                        aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                        diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                          if(mesesf!=1){//si el ingreso es diferente de enero
                                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                        llenarFecha();
                                                                        cuotas();
                                                                        }else{
                                                                            tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                            utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                        }
                                                              }else{
                                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                        llenarFecha();
                                                                        cuotas();
                                                                    }else{
                                                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                    } 
                                                                   }
                                                    }

                        }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28
                                        if(tab_datof.getValor("cod_tipo").equals("4")||tab_datof.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                                                        llenarFecha();
                                                        cuotas();
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                            }
                                            }else{
                                                    anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                                    dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                    meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                    if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=12){
                                                                    llenarFecha();
                                                                    cuotas();
                                                                }else{
                                                                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                                                }
                                                        }else{
                                                                mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                  if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                    cuotas();
                                                                                }else{
                                                                                    tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                } 
                                                                      }else{
                                                                            if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                llenarFecha();
                                                                                cuotas();
                                                                            }else{
                                                                                tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                            }  
                                                                           }
                                                            }
                                                }
                                }
                }else if(tab_dato.getValor("id_distributivo").equals("2")){//VALIDACION DE TRABAJADORES
                            if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=16){//VALIDACION POR DIA HASTA 10
                                                    anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                                    dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                    meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                        if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                                                    llenarFecha();
                                                    cuotas();
                                                    }else{
                                                            utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                        }                                              
                                            }else {if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                                                            if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=12){
                                                                llenarFecha();
                                                                cuotas();
                                                            }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                                            }
                                                        }else{
                                                                mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                  if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                 if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                     llenarFecha();
                                                                                        cuotas();
                                                                                    }else{
                                                                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                      }else{
                                                                            if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){ 
                                                                                llenarFecha();
                                                                                    cuotas();
                                                                                }else{
                                                                                    tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                }  
                                                                           }
                                                            }
                            }
                                }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28
                                            if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                                                            llenarFecha();
                                                            cuotas();
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                            }
                                                }else{
                                                        anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                                        dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                        meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=12){
                                                                    llenarFecha();
                                                                     cuotas();
                                                                }else{
                                                                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                                                }
                                                            }else{
                                                                    mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                    aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                    diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                      if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                     cuotas();
                                                                                    }else{
                                                                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                          }else{
                                                                               if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                    cuotas();
                                                                                    }else{
                                                                                        tab_anticipo.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                               }
                                                                }
                                                    }
                                        }
                        }
                        }else {
               utilitario.agregarMensajeError("Solicitante de anticipo", "No posee historia laboral");
               }
            }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
               }
    }
    
        //VALIDACION DE FECHAS
    
    public void llenarFecha(){

        Integer calculo=0,calculo1=0,calculo2=0,calculo3=0,mess=0;
        String mes,anio,dia,fecha,busca;
        
        anio = String.valueOf(utilitario.getAnio(tab_anticipo.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO")));
        
        mess = utilitario.getMes(tab_anticipo.getValor("FECHA_ANTICIPO"));
        if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=15){
             if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                if(calculo1<0){
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                         if (!tab_dato.isEmpty()) {
                                       //fecha inicial
                                       fecha= tab_dato.getValor("ide_periodo_anticipo");
                                       tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                        //fecha final
                                        if(mes.equals("1")){//mes de enero
                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                        }else{//otros meses
                                              TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes)-1),String.valueOf(Integer.parseInt(anio)+1));
                                                if (!tab_dato2.isEmpty()) {
                                                    fecha= tab_dato2.getValor("ide_periodo_anticipo");
                                                    tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                } else {
                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                        }
                                        }
                             }else {
                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                   }
                }
            }else if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
                        Integer cuota =0; 
                        calculo = 12 - Integer.parseInt(mes);
                        calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                        if(calculo1<0){
                              TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato.isEmpty()) {
                                        //fecha inicial
                                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                                        tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                        //fecha final
                                        calculo2 = calculo1*-1;
                                        cuota = Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                        if(mes.equals("2")){//para febrero
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                        }else if(mes.equals("3")){
                                            if(cuota == 11){
                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                    if (!tab_datoa.isEmpty()) {
                                                        fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                            }else{
                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                            }
                                            }else if(mes.equals("4")){
                                                if(cuota == 11||cuota == 10){
                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                    if (!tab_datoa.isEmpty()) {
                                                        fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                                }else{
                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_anticipo");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                                }
                                                }else if(mes.equals("5")){
                                                    if(cuota == 11||cuota == 10||cuota == 9){
                                                        TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                        if (!tab_datoa.isEmpty()) {
                                                            fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                            tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                            utilitario.addUpdate("tab_anticipo");
                                                        } else {
                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                }
                                                    }else{
                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                        if (!tab_dato1.isEmpty()) {
                                                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                            tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                            utilitario.addUpdate("tab_anticipo");
                                                        } else {
                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                }
                                                    }
                                                    }else if(mes.equals("6")){
                                                        if(cuota == 11||cuota == 10||cuota == 9||cuota == 8){
                                                            TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                            if (!tab_datoa.isEmpty()) {
                                                                fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                utilitario.addUpdate("tab_anticipo");
                                                            } else {
                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                    }
                                                        }else{
                                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                            if (!tab_dato1.isEmpty()) {
                                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                utilitario.addUpdate("tab_anticipo");
                                                            } else {
                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                    }
                                                        }
                                                        }else if(mes.equals("7")){
                                                            if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7){
                                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                          if (!tab_datoa.isEmpty()) {
                                                                              fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                              tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                              utilitario.addUpdate("tab_anticipo");
                                                                          } else {
                                                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                  }
                                                            }else{
                                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                if (!tab_dato1.isEmpty()) {
                                                                    fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                    tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                    utilitario.addUpdate("tab_anticipo");
                                                                } else {
                                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                        }
                                                            }
                                                            }else if(mes.equals("8")){
                                                                if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6){
                                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                              if (!tab_datoa.isEmpty()) {
                                                                                  fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                  tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                  utilitario.addUpdate("tab_anticipo");
                                                                              } else {
                                                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                      }
                                                                }else{
                                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                    if (!tab_dato1.isEmpty()) {
                                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                        utilitario.addUpdate("tab_anticipo");
                                                                    } else {
                                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                            }
                                                                }
                                                                }else if(mes.equals("9")){
                                                                    if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5){
                                                                        TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                  if (!tab_datoa.isEmpty()) {
                                                                                      fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                      tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                      utilitario.addUpdate("tab_anticipo");
                                                                                  } else {
                                                                                          utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                          }
                                                                    }else{
                                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                        if (!tab_dato1.isEmpty()) {
                                                                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                            tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                            utilitario.addUpdate("tab_anticipo");
                                                                        } else {
                                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                }
                                                                    }
                                                                    }else if(mes.equals("10")){
                                                                        if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4){
                                                                            TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                      if (!tab_datoa.isEmpty()) {
                                                                                          fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                          tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                          utilitario.addUpdate("tab_anticipo");
                                                                                      } else {
                                                                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                              }
                                                                        }else{
                                                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                            if (!tab_dato1.isEmpty()) {
                                                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                utilitario.addUpdate("tab_anticipo");
                                                                            } else {
                                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                    }
                                                                        }
                                                                        }else if(mes.equals("11")){
                                                                            if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4||cuota == 3){
                                                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                          if (!tab_datoa.isEmpty()) {
                                                                                              fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                              tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                              utilitario.addUpdate("tab_anticipo");
                                                                                          } else {
                                                                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                                  }
                                                                            }else{
                                                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                                if (!tab_dato1.isEmpty()) {
                                                                                    fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                    tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                    utilitario.addUpdate("tab_anticipo");
                                                                                } else {
                                                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                        }
                                                                            }
                                                                            }else if(mes.equals("12")){
                                                                                if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4||cuota == 1){
                                                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                              if (!tab_datoa.isEmpty()) {
                                                                                                  fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                                  tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                                  utilitario.addUpdate("tab_anticipo");
                                                                                              } else {
                                                                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                                      }
                                                                                }else{
                                                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                                    if (!tab_dato1.isEmpty()) {
                                                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                        tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                                                        utilitario.addUpdate("tab_anticipo");
                                                                                    } else {
                                                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                            }
                                                                                }
                                                                            }
                                    }else {
                                          utilitario.agregarMensajeInfo("No existen Datos", "");
                                          }
                        }else if(calculo1>0){
                                 TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                    if (!tab_dato.isEmpty()) {
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
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
                        }else  if(calculo1==0){
                                TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                     if (!tab_dato.isEmpty()) {
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
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
                    }else if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                            calculo = 12 - Integer.parseInt(mes);
                            calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                            if(calculo1<0){
                                TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                     if (!tab_dato.isEmpty()) {
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-12-1),String.valueOf(Integer.parseInt(anio)+1));
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
                    }
            }else if(utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_anticipo.getValor("FECHA_ANTICIPO"))<=31 ){
                        if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))==12){
                        calculo = 12 - Integer.parseInt(mes);
                        calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                        if(calculo1<0){
                            TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                 if (!tab_dato.isEmpty()) {
                                      //fecha inicial
                                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                                        tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_anticipo");
                                        //fecha final
                                              TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)+1));
                                                if (!tab_dato2.isEmpty()) {
                                                    fecha= tab_dato2.getValor("ide_periodo_anticipo");
                                                    tab_anticipo.setValor("ide_periodo_anticipo_final", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                } else {
                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                        }
                                     }else {
                                           utilitario.agregarMensajeInfo("No existen Datos", "");
                                           }
                        }
                    }else if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<12){
                                Integer cuota =0;
                                calculo = 12 - Integer.parseInt(mes);
                                calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                if(calculo1<0){
                                      TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                        if (!tab_dato.isEmpty()) {
                                             //fecha inicial
                                                fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                utilitario.addUpdate("tab_anticipo");
                                                //fecha final
                                                calculo2 = calculo1*-1;
                                                cuota = Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                                       TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo2),String.valueOf(Integer.parseInt(anio)+1));
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
                                }else if(calculo1>0){
                                         TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                            if (!tab_dato.isEmpty()) {
                                                 //fecha inicial
                                                    fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                    tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                    //fecha final
                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess),String.valueOf(Integer.parseInt(anio)));
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
                                }else  if(calculo1==0){
                                        TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato.isEmpty()) {
                                                  //fecha inicial
                                                    fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                    tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                    //fecha final
                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess),String.valueOf(Integer.parseInt(anio)));
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
                            }else if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))<=18){
                                    calculo = 12 - Integer.parseInt(mes);
                                    calculo1 = calculo - Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                                    if(calculo1<0){
                                        TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato.isEmpty()) {
                                                  //fecha inicial
                                                    fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                    tab_anticipo.setValor("ide_periodo_anticipo_inicial", fecha);
                                                     utilitario.addUpdate("tab_anticipo");
                                                     //fecha final
                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))+mess-12),String.valueOf(Integer.parseInt(anio)+1));
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
                            }
                    }
    }
    
    //CALCULO DE CUOTAS Y CUOTA ESPECIAL
    public void cuotas(){
        tramite();
        
        Integer rango;
        double valora=0,valora1=0,valorm=0,media=0,rmu=0,valan=0,valorff=0;
        rmu =Double.parseDouble(tab_anticipo.getValor("rmu"));
        valan= Double.parseDouble(tab_anticipo.getValor("valor_anticipo"));
        media = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"))/2;
        
        TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
        if (!tab_dato.isEmpty()) {
            rango = Integer.parseInt(tab_dato.getValor("periodo"))+Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
            
            if(rango > 12){
                valora= ((rmu*70)/100);
                if(valora>=Double.parseDouble(tab_anticipo.getValor("valor_anticipo"))){
                        valorff= ((valan*80)/100);
                        valorm = (valan-valorff)/(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))-1);
                        
                        if(media > (Math.rint(valorm*100)/100)){
                            tab_anticipo.setValor("valor_cuota_adicional", String.valueOf(valorff));
                            tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                            utilitario.addUpdate("tab_anticipo");
                            }else{
                                    utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                  }
                }else{
                        valorm = (valan-valora)/(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))-1);
                        
                        if(media > (Math.rint(valorm*100)/100)){
                            tab_anticipo.setValor("valor_cuota_adicional", String.valueOf(valora));
                            tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                            utilitario.addUpdate("tab_anticipo");
                            }else{
                                   utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                }
                }
//                
            }else if(rango <= 12){
                    
                    valora1 = Double.parseDouble(tab_anticipo.getValor("valor_anticipo")) / Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                    if( media > (Math.rint(valora1*100)/100)){
                        tab_anticipo.setValor("valor_cuota_adicional", "NULL");
                        tab_anticipo.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1*100)/100));
                        utilitario.addUpdate("tab_anticipo");
                    }else{
                            utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                    }
                }
        }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
               }
    }
    
        //CALCULAR MESES
    public static int calcularMes(Calendar cal1,Calendar cal2) {
    // conseguir la representacion de la fecha en milisegundos
     long milis1 = cal1.getTimeInMillis();//fecha actual
     long milis2 = cal2.getTimeInMillis();//fecha futura
     long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
     long diffSeconds = diff / 1000; // calcular la diferencia en segundos
     long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
     long diffHours = diffMinutes / 60 ; // calcular la diferencia en horas a
     long diffDays = diffHours / 24 ; // calcular la diferencia en dias
     long diffWeek = diffDays / 7 ; // calcular la diferencia en semanas
     long diffMounth = diffWeek / 4 ; // calcular la diferencia en meses

     return Integer.parseInt(String.valueOf(diffMounth));
    }
    
    //CALCULAR ANIOS
    public static int calcularAnios(Calendar AnioLab) {
    Calendar fechaActual = Calendar.getInstance();
    // Cálculo de las diferencias.
    int anios = fechaActual.get(Calendar.YEAR) - AnioLab.get(Calendar.YEAR);
    int meses = fechaActual.get(Calendar.MONTH) - AnioLab.get(Calendar.MONTH);
    int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - AnioLab.get(Calendar.DAY_OF_MONTH);
  
    if(meses < 0 // Aún no es el mes 
       || (meses==0 && dias < 0)) { // o es el mes pero no ha llegado el día.
        anios--;
    }
    return anios;
    }
    
    //BUSQUEDA DE MES PARA LA AISGNACION DEL PERIODO
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
        if (tab_anticipo.isFocus()) {
            tab_anticipo.insertar();
        }
    }

    @Override
    public void guardar() {
      TablaGenerica tab_dato = iAnticipos.validar(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            if(tab_anticipo.getValor("ci_solicitante").equals(tab_dato.getValor("ci_solicitante"))){
                if(tab_dato.getValor("ide_estado_anticipo").equals("4")){
                    if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
                                tab_anticipo.guardar();
                                con_postgres.guardarPantalla();
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
                                                    tab_anticipo.guardar();
                                                    con_postgres.guardarPantalla();
                } else {
                    utilitario.agregarMensajeError("El Número de Cedula no es válido", "");
                }
        }
    }

    @Override
    public void eliminar() {
        
     TablaGenerica tab_dato = iAnticipos.validar(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            if(tab_anticipo.getValor("ci_solicitante").equals(tab_dato.getValor("ci_solicitante"))){
                if(tab_dato.getValor("ide_estado_anticipo").equals("4")||tab_dato.getValor("ide_estado_anticipo").equals("1")){
                    if (tab_anticipo.isFocus()) {
                            tab_anticipo.eliminar();
                        }
                }else{
                    utilitario.agregarMensajeError("Solicitud", "En proceso");
                    }
           }else{
                utilitario.agregarMensajeError("Solicitud", "En proceso");
                }
        }else{
              if (tab_anticipo.isFocus()) {
                     tab_anticipo.eliminar();
                 }
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
    
    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
     //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fecha.setFechaActual();
        switch (rep_reporte.getNombre()) {
                case "REPORTE SOLICITUD POR FECHA":
                    cal_fecha.limpiar();
                    dia_dialogou.Limpiar();
                    grid_du.getChildren().add(new Etiqueta("SELECCIONE FECHA :"));
                    grid_du.getChildren().add(cal_fecha);
                    grid_du.getChildren().add(new Etiqueta("ESCOGA USUARIO :"));
                    grid_du.getChildren().add(cmb_usu);
                    dia_dialogou.setDialogo(grid_du);
                    dia_dialogou.dibujar();
                break;
                case "COMPROBANTE DE SOLICITUD":
                    dia_dialogoi.Limpiar();
                    grid_di.getChildren().add(new Etiqueta("CEDULA DE SOLICITANTE :"));
                    grid_di.getChildren().add(txt_cedula);
                    dia_dialogoi.setDialogo(grid_di);
                    dia_dialogoi.dibujar();
                break;
                }
        }
    
     // dibujo de reporte y envio de parametros
    public void aceptoDescuentos(){
        switch (rep_reporte.getNombre()) {
                case "REPORTE SOLICITUD POR FECHA":
                     if (cmb_usu.getValue()!= null) {
                          TablaGenerica tab_dato = ser_Placa.getUsuario(Integer.parseInt(cmb_usu.getValue()+""));
                            if (!tab_dato.isEmpty()) {
                                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                                p_parametros.put("pide_fechai", cal_fecha.getFecha()+"");
                                p_parametros.put("persona", tab_dato.getValor("usuarioin")+"");
                                rep_reporte.cerrar();
                                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                sef_formato.dibujar();
                               } else {
                                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                        }
                      }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
                break;
                case "COMPROBANTE DE SOLICITUD":
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("identificacion", txt_cedula.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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
