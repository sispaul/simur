/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.anticipos;
import static paq_nomina.pre_anticipos_gadmur.calcularAnios;
import static paq_nomina.pre_anticipos_gadmur.calcularMes;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
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
    
    private Tabla set_solictante = new Tabla();
    private Tabla set_garante = new Tabla();
            
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Efecto efecto1 = new Efecto();
    //
    String selec_mes = new String();
    
    //texto para busqueda de solicitud
    private Texto txt_ci = new Texto();
    
    //dialogo para reporte
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid gride = new Grid();
    private Grid grid_e = new Grid();
    
     private Dialogo dia_dialogog = new Dialogo();
    private Grid gridg = new Grid();
    private Grid grid_g = new Grid();
    
     ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //clase logica
    @EJB
    private anticipos iAnticipos = (anticipos) utilitario.instanciarEJB(anticipos.class);
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
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
        tab_anticipo.getColumna("solicitante").setMetodoChange("buscarSolicitante");
        tab_anticipo.getColumna("ide_empleado_garante").setMetodoChange("llenarGaranteCod");
        tab_anticipo.getColumna("garante").setMetodoChange("buscarGarante");
        
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
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        
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
        dia_dialogoe.setTitle("SELECCIONAR DATOS SOLICITANTE"); //titulo
        dia_dialogoe.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("45%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptarSolictante");
        gride.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        dia_dialogog.setId("dia_dialogog");
        dia_dialogog.setTitle("SELECCIONAR DATOS GARANTE"); //titulo
        dia_dialogog.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogog.setHeight("45%");//siempre porcentaje   alto 
        dia_dialogog.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogog.getBot_aceptar().setMetodo("aceptarGarante");
        gridg.setColumns(4);
        agregarComponente(dia_dialogog);
    }

    //LLENAR DATOS DE SOLICITANTE Y GARANTE POR NUEMRO DE CEDULA
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
    
    //BUSCAR SOLICITANTE Y GARANTE POR NOMBRE O APELLIDOS
    public void buscarSolicitante(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_e.getChildren().add(set_solictante);
        set_solictante.setId("set_solictante");
        set_solictante.setConexion(con_postgres);
        set_solictante.setHeader("LISTA DE COLABORADORES");
        set_solictante.setSql("SELECT cedula_pass,nombres,cedula_pass as cedula,cod_empleado FROM srh_empleado WHERE nombres LIKE '%"+tab_anticipo.getValor("solicitante")+"%'");
        set_solictante.getColumna("nombres").setFiltro(true);
        set_solictante.setRows(10);
        set_solictante.setTipoSeleccion(false);
        dia_dialogoe.setDialogo(grid_e);
        set_solictante.dibujar();
        dia_dialogoe.dibujar();
    }
    
    public void buscarGarante(){
        dia_dialogog.Limpiar();
        dia_dialogog.setDialogo(gridg);
        grid_g.getChildren().add(set_garante);
        set_garante.setId("set_garante");
        set_garante.setConexion(con_postgres);
        set_garante.setHeader("LISTA DE COLABORADORES");
        set_garante.setSql("SELECT cedula_pass,cedula_pass as cedula,nombres,cod_empleado FROM srh_empleado WHERE nombres LIKE '%"+tab_anticipo.getValor("garante")+"%'");
        set_garante.getColumna("nombres").setFiltro(true);
        set_garante.setRows(10);
        set_garante.setTipoSeleccion(false);
        dia_dialogog.setDialogo(grid_g);
        set_garante.dibujar();
        dia_dialogog.dibujar();
    }
    
    //ACEPTAR DATOS DE SOLITANTE Y GARANTE POR NOMBRE Y APELLIDOS
    public void aceptarSolictante(){
         if (set_solictante.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = iAnticipos.empleados(set_solictante.getValorSeleccionado());
                if (!tab_dato.isEmpty()) {
                    tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
                    tab_anticipo.setValor("solicitante", tab_dato.getValor("nombres"));
                    tab_anticipo.setValor("rmu", tab_dato.getValor("ru"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
                    utilitario.addUpdate("tab_anticipo");
                    dia_dialogoe.cerrar();
        }else {
           TablaGenerica tab_dato1 = iAnticipos.trabajadores(set_solictante.getValorSeleccionado());
                if (!tab_dato1.isEmpty()) {
                    tab_anticipo.setValor("ci_solicitante", tab_dato.getValor("cedula_pass"));
                    tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                    tab_anticipo.setValor("rmu", tab_dato1.getValor("su"));
                    tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                    tab_anticipo.setValor("ide_empleado_solicitante", tab_dato.getValor("COD_EMPLEADO"));
                    utilitario.addUpdate("tab_anticipo");
                    dia_dialogoe.cerrar();
                }else {
                  utilitario.agregarMensajeInfo("No existen Datos", "");
                  }
          }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    }
    
    public void aceptarGarante(){
         if (set_garante.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = ser_Placa.Funcionario(set_garante.getValorSeleccionado());
                if (!tab_dato.isEmpty()) {
                     tab_anticipo.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
                     tab_anticipo.setValor("ide_empleado_garante", tab_dato.getValor("cod_empleado"));
                     tab_anticipo.setValor("garante", tab_dato.getValor("nombres"));
                      utilitario.addUpdate("tab_anticipo");
                      dia_dialogog.cerrar();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos empleados del municipio", "");
                               }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    }
    
    //LLENAR DATOS DE EMPLEADO Y GARANTE X CODIGO DE EMPLEADO
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
    
    
   //VALOR DE ANTICIPO DE ACUERDO A LA ULTIMA REMUNERACION LIQUIDA PERCIBIDA
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
//         servidor();
         tab_anticipo.getColumna("numero_cuotas_anticipo").setLectura(true);
         calculo1 = 12 -  Integer.parseInt(mes);
         calculo2 = calculo1- Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"));
                if(calculo2<=0){
                    calculo3 = calculo2*-1;
                    fecha = String.valueOf(Integer.parseInt(anio)+1)+"-"+String.valueOf(calculo3+1)+"-"+dia;
                    tab_anticipo.setValor("numero_cuotas_anticipo", fecha);
                    utilitario.addUpdate("tab_anticipo");
                    utilitario.agregarMensajeInfo("Por el Monto", "Cobro de Anticipo 2 meses");
//                     servidor();
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
    
    //VALIDACION DE TIPO DEL SERVIDOR QUE SOLICITA EL ANTICIPO


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

    public Tabla getSet_garante() {
        return set_garante;
    }

    public void setSet_garante(Tabla set_garante) {
        this.set_garante = set_garante;
    }

    public Tabla getSet_solictante() {
        return set_solictante;
    }

    public void setSet_solictante(Tabla set_solictante) {
        this.set_solictante = set_solictante;
    }
    
}
