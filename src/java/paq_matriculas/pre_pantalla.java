/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import paq_registros.ejb.ServicioRegistros;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_pantalla extends Pantalla{
    //Textos para auto busqueda
    private Texto txt_cedula = new Texto();
    private Texto txt_nombre = new Texto();
    private Texto txt_empresa = new Texto();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_empresa = new Tabla();
    private Tabla tab_persona = new Tabla();
    private Combo cmb_tipos = new Combo();
    private Tabla tab_gestor = new Tabla();
    private Tabla set_vehiculo1 = new Tabla();
    private Tabla set_servicio1 = new Tabla ();
    
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Panel pan_opcion = new Panel();
    private Calendario cal_calendario = new Calendario();
    
    private Tabla tab_consulta = new Tabla();
    private Efecto efecto = new Efecto();
    private Conexion con_ciudadania= new Conexion();
    private Etiqueta etifec = new Etiqueta();
    private Dialogo dia_dialogoEN = new Dialogo();
    private Grid gridd = new Grid();
    private Grid grid_en = new Grid();
    private Grid grid_dp = new Grid();
    private Dialogo dia_dialogod = new Dialogo();
    private Dialogo dia_dialogoDP = new Dialogo();
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    @EJB
    private ServicioRegistros ser_registros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);
    public pre_pantalla() {
        
        con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadaniajdbc"));
        con_ciudadania.NOMBRE_MARCA_BASE="sqlserver";

        etifec.setStyle("font-size:16px;color:blue");
        etifec.setValue("SELECCIONE RANGO DE FECHAS");
        gridd.setColumns(4);
        //campos fecha       
        gridd.getChildren().add(new Etiqueta("FECHA INICIAL"));
        gridd.getChildren().add(cal_fechaini);
        gridd.getChildren().add(new Etiqueta("     FECHA FINAL"));
        gridd.getChildren().add(cal_fechafin);
        
                ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y ENCARGADO
        //Configurando el dialogo
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("PLACAS - PLACAS ENTREGADAS"); //titulo
        dia_dialogoEN.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoDialogo1");
        
         grid_en.setColumns(2);
         grid_en.getChildren().add(new Etiqueta("SELECCIONE Vehiculo"));
         agregarComponente(dia_dialogoEN);
        
          /***CREACION DE OBJETOS TABLA***/
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogoe.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_de.setColumns(4);
        grid_en.getChildren().add(new Etiqueta("SELECCIONE Servicio"));
        agregarComponente(dia_dialogoe);
         
	Panel pan_menu = new Panel();
	pan_menu.setHeader("OPCIONES");
//
	Grupo gru_panel_izquierda = new Grupo();
	gru_panel_izquierda.getChildren().add(pan_menu);
//        Panel pan_panel = new Panel();
//        pan_panel.setId("pan_panel");
//        pan_panel.setStyle("width: 255px");
//        pan_panel.setHeader("Buscar Datos de Solicitante");
//        gru_panel_izquierda.getChildren().add(pan_panel);
//         
//        cmb_tipos.setId("cmb_tipos");
//        cmb_tipos.setStyle("width: 99%;");
//        cmb_tipos.setCombo("select ide_tipo_gestor,descripcion_gestor from trans_tipo_gestor");
//        pan_panel.getChildren().add(new Etiqueta("TIPO SOLICITANTE : "));
//        pan_panel.getChildren().add(cmb_tipos);
//        
//        txt_cedula.setId("txt_cedula");
//        txt_cedula.setStyle("width: 99%;");
//        pan_panel.getChildren().add(new Etiqueta("C.I/RUC. : "));
//        pan_panel.getChildren().add(txt_cedula);
////        
//        txt_empresa.setId("txt_empresa");
//        txt_empresa.setStyle("width: 99%;");
//        txt_empresa.setDisabled(true);
//        pan_panel.getChildren().add(new Etiqueta("EMPRESA :"));
//        pan_panel.getChildren().add(txt_empresa);
//        
//        txt_nombre.setId("txt_nombre");
//        txt_nombre.setStyle("width: 99%;");
//        txt_nombre.setDisabled(true);
//        pan_panel.getChildren().add(new Etiqueta("NOMBRE GESTOR :"));
//        pan_panel.getChildren().add(txt_nombre);
//        
//        Boton bot_bus = new Boton();
//        bot_bus.setId("bot_bus");
//        bot_bus.setValue("Buscar");
//        bot_bus.setIcon("ui-icon-locked");
//        bot_bus.setMetodo("buscarPersona");
//        
//        pan_panel.getChildren().add(bot_bus);
//        Boton bot_new = new Boton();
//        bot_new.setId("bot_new");
//        bot_new.setValue("Limpiar");
//        bot_new.setIcon("ui-icon-locked");
//        bot_new.setMetodo("LimpiarBoton");
//        pan_panel.getChildren().add(bot_new);
                
        cal_calendario.setMode("inline");
	Panel pan_calendario = new Panel();
	pan_calendario.setHeader("CALENDARIO");
	pan_calendario.getChildren().add(cal_calendario);
        gru_panel_izquierda.getChildren().add(pan_calendario);
               
	Panel pan_empresa = new Panel();
	pan_empresa.setHeader("EMPRESA");
	pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");
	pan_opcion.getChildren().add(efecto);
        
	buscarSolicitante();
	Division div_division = new Division();
	div_division.setId("div_division");
	div_division.dividir2(gru_panel_izquierda, pan_opcion, "18%", "V");
	agregarComponente(div_division);
        
        //agregar usuario actual     
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_gestor.setCampoPrimaria("IDE_USUA");
        tab_gestor.setLectura(true);
        tab_gestor.dibujar();
        
         /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        set_vehiculo1.setId("set_vehiculo1");
        set_vehiculo1.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo1.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo1.setRows(5);
        set_vehiculo1.setTipoSeleccion(false);
        set_vehiculo1.dibujar();
        
        set_servicio1.setId("set_servicio1");
        set_servicio1.setSql("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        set_servicio1.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio1.setRows(10);
        set_servicio1.setTipoSeleccion(false);
        set_servicio1.dibujar();
        
        
    }
public void buscarSolicitante(){

	pan_opcion.setHeader("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        tab_solicitud.setMostrarNumeroRegistros(false);       
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("RUC_EMPRESA").setMetodoChange("cargarEmpresa");
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setLectura(true);
        tab_solicitud.getColumna("NOMBRE_EMPRESA").setLectura(true);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setCombo("select ide_tipo_gestor,descripcion_gestor from trans_tipo_gestor");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
  
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setMayusculas(true);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("NOMBRE PROPIETARIO");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC");
        tab_detalle.getColumna("NUMERO_FACTURA").setNombreVisual("Nro.FACTURA");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setNombreVisual("TIPO DE VEHICULO");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo, des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 and 5");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("TIPO DE SERVICIO"); 
//        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("TRANS_TIPO_SERVICIO", "IDE_TIPO_SERVICIO", "DESCRIPCION_SERVICIO", "IDE_TIPO_VEHICULO="+tab_detalle.getValor("ide_tipo_vehiculo")+"");
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. TRAMITE");
        
        tab_detalle.setTipoFormulario(true);
        agregarComponente(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle); 
//        Boton bot1 = new Boton();
//        bot1.setValue("MOSTRAR REQUISITOS");
//        bot1.setMetodo("aceptoRequisitos");
//        bot1.setIcon("ui-icon-search");
//        tabp2.getChildren().add(bot1);

        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
//      tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
//                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
//                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        
		Grid gri_tabla = new Grid();
		gri_tabla.getChildren().add(tabp1);
                gri_tabla.getChildren().add(tabp2);
                gri_tabla.getChildren().add(tabp3);
		pan_opcion.getChildren().add(gri_tabla);

    }
    public void LimpiarBoton(){
        txt_cedula.limpiar();
        utilitario.addUpdate("txt_cedula");
        txt_nombre.limpiar();
        utilitario.addUpdate("txt_nombre");
        
    }
    
    public void aceptoRequisitos(){
//        ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
        utilitario.addUpdate("tab_requisito");
        tab_requisito.actualizar();
    }

//    public void cargarSocio() {
//        if (utilitario.validarCedula(tab_socios.getValor("cedula"))) {
//            TablaGenerica tab_dato = ser_registros.getPersona(tab_socios.getValor("cedula"));
//            if (!tab_dato.isEmpty()) {
//                // Cargo la información de la base de datos maestra   
//                tab_socios.setValor("nombre", tab_dato.getValor("nombre"));
//                utilitario.addUpdate("tab_socios");
//            } else {
//                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
//            }
//        } else {
//            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
//        }
//
//    }

    public void cargarEmpresa() {
        if (utilitario.validarRUC(tab_solicitud.getValor("RUC_EMPRESA"))) {
            TablaGenerica tab_dato = ser_registros.getEmpresa(tab_solicitud.getValor("RUC_EMPRESA"));
            if (!tab_dato.isEmpty()) {
                //Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA ", tab_dato.getValor("RAZON_SOCIAL"));
//                tab_tabla.setValor("direccion", tab_dato.getValor("DIRECCION"));
//                tab_tabla.setValor("telefono", tab_dato.getValor("telefono"));
//                tab_tabla.setValor("e_mail", tab_dato.getValor("mail"));
                utilitario.addUpdate("tab_solicitud");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
        }

    }        
//    public void buscarPersona(){
//        if (utilitario.validarRUC(tab_detalle.getValor("RUC_EMPRESA"))){
//        tab_empresa.setId("tab_generica");
//        tab_empresa.setSql("SELECT a.IDE_COMERCIAL_AUTOMOTORES,a.NOMBRE_EMPRESA,g.IDE_GESTOR,\n" 
//                            +"g.NOMBRE_GESTOR,a.RUC_EMPRESA\n" 
//                            +"FROM TRANS_COMERCIAL_AUTOMOTORES a,TRANS_GESTOR g\n" 
//                            +"WHERE g.IDE_COMERCIAL_AUTOMOTORES = a.IDE_COMERCIAL_AUTOMOTORES AND a.RUC_EMPRESA ='"+tab_solicitud.getValor("RUC_EMPRESA")+"'");
////        tab_empresa.dibujar();
//        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setValorDefecto(tab_empresa.getValor("NOMBRE_GESTOR")+"");
//        tab_detalle.getColumna("NOMBRE_EMPRESA").setValorDefecto(tab_empresa.getValor("NOMBRE_EMPRESA")+"");
//        utilitario.addUpdate("NOMBRE_PROPIETARIO"); //Actualiza la pantalla
//        utilitario.addUpdate("NOMBRE_EMPRESA"); //Actualiza la pantalla
////        txt_empresa.setValue(tab_empresa.getValor("NOMBRE_EMPRESA")+"");
////        txt_nombre.setValue(tab_empresa.getValor("NOMBRE_GESTOR")+"");
////        utilitario.addUpdate("txt_nombre"); //Actualiza la pantalla
////        utilitario.addUpdate("txt_empresa"); //Actualiza la pantalla
////            tab_solicitud.insertar();
////            tab_detalle.insertar();
//        
//         }else if(utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))){
//                    tab_persona.setId("tab_persona");
//                    tab_persona.setSql("SELECT IDE_GESTOR,NOMBRE_GESTOR FROM TRANS_GESTOR WHERE CEDULA_GESTOR ='"+tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO")+"'");
//                    tab_persona.dibujar();
//                    tab_solicitud.getColumna("NOMBRE_EMPRESA").setLectura(true);
//                    tab_solicitud.getColumna("RUC_EMPRESA").setLectura(true);
//                    tab_solicitud.getColumna("NOMBRE_EMPRESA").setValorDefecto(null);
//                    tab_solicitud.getColumna("RUC_EMPRESA").setValorDefecto(null);
////                    tab_solicitud.insertar();
////                    tab_detalle.insertar();
////                    utilitario.addUpdate("NOMBRE_EMPRESA"); //Actualiza la pantalla
////                    utilitario.addUpdate("RUC_EMPRESA"); //Actualiza la pantalla
//                    tab_detalle.getColumna("NOMBRE_PROPIETARIO").setValorDefecto(tab_empresa.getValor("NOMBRE_GESTOR")+"");
////                    tab_detalle.insertar();
//                    utilitario.addUpdate("NOMBRE_PROPIETARIO"); //Actualiza la pantalla
//                 }else {
//                           utilitario.agregarMensajeError("El Número de Identificacion no es válido", "");
////                         return;
//                     }
//}

@Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();            
        cal_fechaini.setFechaActual();
        cal_fechafin.setFechaActual();
        switch (rep_reporte.getNombre()) {
            case "REPORTE PERIODO":
                dia_dialogoEN.Limpiar();
                //Agrega Fechas
                dia_dialogoEN.setDialogo(etifec);
                dia_dialogoEN.setDialogo(gridd);
                //Configura grid
                grid_en.getChildren().add(set_vehiculo1);
                dia_dialogoEN.setDialogo(grid_en);
                set_vehiculo1.dibujar();
                dia_dialogoEN.dibujar();
               break;
           case "REPORTE DIARIO":
                aceptoDialogo();
               break;      
                
        }
    }
  
    
    public void aceptoDialogo() {
        if (utilitario.isFechasValidas(cal_fechaini.getFecha(), cal_fechafin.getFecha())){
        switch (rep_reporte.getNombre()) {
               case "REPORTE PERIODO":
                    if ((set_vehiculo1.getValorSeleccionado() != null) && (set_servicio1.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("pide_veh", Integer.parseInt(set_vehiculo1.getValorSeleccionado()));
                    p_parametros.put("pide_serv", Integer.parseInt(set_servicio1.getValorSeleccionado()));
                    p_parametros.put("pide_fechai", cal_fechaini.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    dia_dialogoEN.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;
               case "REPORTE DIARIO":
                    p_parametros = new HashMap();
                    p_parametros.put("pide_fechai", utilitario.getFechaActual());
                    p_parametros.put("pide_fechaf", utilitario.getFechaActual());
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    dia_dialogoDP.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    System.err.println(p_parametros);
                    System.err.println(rep_reporte.getPath());
                    sef_formato.dibujar();                
        }
        }else{
            utilitario.agregarMensaje("Fechas", "Rango de Fechas no valido");
        }
    }

        public void aceptoDialogo1() {
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_servicio1);
        set_servicio1.setId("set_servicio");
        set_servicio1.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo1.getValorSeleccionado());
        set_servicio1.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio1.setRows(10);
        set_servicio1.setTipoSeleccion(false);
        dia_dialogoe.setDialogo(grid_de);
        set_servicio1.dibujar();
        dia_dialogoe.dibujar();
    }
    
    public void abrirDialogo() {
        dia_dialogod.dibujar();
    }    
    
//    public void buscaPersona(){
//        if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))){
//        tab_empresa.setId("tab_generica");
//        tab_empresa.setSql("SELECT RUC,RAZON_SOCIAL FROM MAESTRO_RUC WHERE RUC ='"+tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO")+"'");
//        tab_empresa.dibujar();
//        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setValorDefecto(tab_empresa.getValor("NOMBRE_GESTOR")+"");
//        utilitario.addUpdate("NOMBRE_PROPIETARIO"); //Actualiza la pantalla
//
//         }else if(utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))){
//                    tab_persona.setId("tab_persona");
//                    tab_persona.setSql("SELECT (cedula+digito_verificador) as cedula_ind,nombre FROM MAESTRO WHERE (cedula+digito_verificador) ='"+tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO")+"'");
//                    tab_persona.dibujar();
//                    tab_detalle.getColumna("NOMBRE_PROPIETARIO").setValorDefecto(tab_empresa.getValor("NOMBRE_GESTOR")+"");
////                    tab_detalle.insertar();
//                    utilitario.addUpdate("NOMBRE_PROPIETARIO"); //Actualiza la pantalla
//                 }else {
//                           utilitario.agregarMensajeError("El Número de Identificacion no es válido", "");
//                         return;
//                     }
//    }
    
    @Override
    public void insertar() {
    if (tab_solicitud.isFocus()) {
         tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.insertar();
                     } else if (tab_requisito.isFocus()) {
                                  tab_requisito.insertar();
                            }   
    }

    @Override
    public void guardar() {
    if (tab_solicitud.isFocus()) {
         tab_solicitud.guardar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.guardar();
                     } else if (tab_requisito.isFocus()) {
                                  tab_requisito.guardar();
                            }        
    }

    @Override
    public void eliminar() {
        if (tab_solicitud.isFocus()) {
         tab_solicitud.eliminar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.eliminar();
                     } else if (tab_requisito.isFocus()) {
                                  tab_requisito.eliminar();
                            }   
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public Tabla getTab_requisito() {
        return tab_requisito;
    }

    public void setTab_requisito(Tabla tab_requisito) {
        this.tab_requisito = tab_requisito;
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

    public Tabla getSet_vehiculo1() {
        return set_vehiculo1;
    }

    public void setSet_vehiculo1(Tabla set_vehiculo1) {
        this.set_vehiculo1 = set_vehiculo1;
    }

    public Tabla getSet_servicio1() {
        return set_servicio1;
    }

    public void setSet_servicio1(Tabla set_servicio1) {
        this.set_servicio1 = set_servicio1;
    }
    
}
