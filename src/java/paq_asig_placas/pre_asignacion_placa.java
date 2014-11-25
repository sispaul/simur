/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;


/**
 *
 * @author KEJA
 */
public class pre_asignacion_placa extends Pantalla{

Integer identificacion;

    private AutoCompletar aut_busca = new AutoCompletar();
    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    private SeleccionTabla set_placa = new SeleccionTabla();
    
    private Texto txt_comentario = new Texto();
    private Texto txt_busca = new Texto();
        
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Efecto efecto1 = new Efecto();
    
    private Calendario cal_fechabus = new Calendario();
    private Calendario cal_fechabus1 = new Calendario();
    private Calendario cal_fechabus2 = new Calendario();
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogol = new Dialogo();
    private Dialogo dia_dialogoq = new Dialogo();
    private Dialogo dia_dialogou = new Dialogo();
    private Dialogo dia_dialogods = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_dl = new Grid();
    private Grid grid_du = new Grid();
    private Grid grid_ds = new Grid();
    private Grid grid_dq = new Grid();
    private Grid gride = new Grid();
    private Grid gridq = new Grid();
    private Grid gridl = new Grid();
    private Grid gridu = new Grid();
    private Grid grids = new Grid();
    
    private Combo cmb_usuario = new Combo();
    private Combo cmb_tipos = new Combo();
    
    private Conexion con_sql= new Conexion();
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    private Combo cmb_rango = new Combo();
    private Combo cmb_placas = new Combo();
    public pre_asignacion_placa() {
            con_sql.NOMBRE_MARCA_BASE="sqlserver";
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        
                bar_botones.quitarBotonInsertar();
		bar_botones.quitarBotonEliminar();
                bar_botones.quitarBotonGuardar();
                
         /*
         * Creación de Botones; Busqueda/Limpieza
         */
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        cmb_usuario.setId("cmb_usuario");
        cmb_usuario.setCombo("SELECT IDE_USUA,NICK_USUA FROM SIS_USUARIO WHERE IDE_PERF = 13");
        cmb_usuario.eliminarVacio();
        
        /*Boton para asignacion de estados*/
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2); 
        gri_busca.getChildren().add(new Etiqueta("USUARIO"));
        gri_busca.getChildren().add(cmb_usuario);
        gri_busca.getChildren().add(new Etiqueta("BUSCAR FECHA"));
        gri_busca.getChildren().add(cal_fechabus);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        /*
         * METODO PARA AUTOCOMPLETAR LA BUSQUEDA
         */
        
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA,NOMBRE_GESTOR\n" +
                                    "FROM TRANS_SOLICITUD_PLACA");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        /*
         * Creación de Divisiones/Menú
         */
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "11%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("ASIGNACIÓN");
        agregarComponente(div_division);
        dibujarSolicitud();
        ////
        cmb_placas.setId("cmb_placas");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "DEFINITIVA", "DEFINITIVA"
        };
        Object fila2[] = {
            "PAPEL", "PAPEL"
        };
        
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmb_placas.setCombo(lista1);
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR ASIGNACIÓN"); //titulo
        dia_dialogoe.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("10%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA where IDE_SOLICITUD_PLACA=-1", "IDE_SOLICITUD_PLACA");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
        
        Grid gri_placa = new Grid();
        gri_placa.setColumns(2);
        txt_comentario.setSize(50);
        gri_placa.getChildren().add(new Etiqueta("RAZON POR LA QUE SE DESVINCULA"));
        gri_placa.getChildren().add(txt_comentario);
        txt_busca.setSize(9);
        gri_placa.getChildren().add(new Etiqueta("# PLACA"));
        gri_placa.getChildren().add(txt_busca);
        Boton bot_placa = new Boton();
        bot_placa.setValue("Buscar");
        bot_placa.setIcon("ui-icon-search");
        bot_placa.setMetodo("buscarPlaca");
        bar_botones.agregarBoton(bot_placa);
        gri_placa.getChildren().add(bot_placa);
        
        set_placa.setId("set_placa");
        set_placa.setSeleccionTabla("SELECT IDE_DETALLE_SOLICITUD,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_RVMO FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA where IDE_DETALLE_SOLICITUD=-1", "IDE_DETALLE_SOLICITUD");
        set_placa.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_placa.getTab_seleccion().setRows(10);
        set_placa.setRadio();
        set_placa.getGri_cuerpo().setHeader(gri_placa);
        set_placa.getBot_aceptar().setMetodo("aceptarDeligar");
        set_placa.setHeader("BUSCAR PLACA - DESASIGNACION");
        agregarComponente(set_placa);
        
        
        dia_dialogoq.setId("dia_dialogoq");
        dia_dialogoq.setTitle("PORQUE DESEA CANCELAR ASIGNACIÒN"); //titulo
        dia_dialogoq.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoq.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogoq.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoq.getBot_aceptar().setMetodo("aceptarDeligar");
        grid_dq.setColumns(4);
        agregarComponente(dia_dialogoq);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        /*
         * VENTANA DE BUSQUEDA
         */
       
        dia_dialogol.setId("dia_dialogol");
        dia_dialogol.setTitle("SELECCIONAR USUARIO"); //titulo
        dia_dialogol.setWidth("26%"); //siempre en porcentajes  ancho
        dia_dialogol.setHeight("25%");//siempre porcentaje   alto 
        dia_dialogol.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogol.getBot_aceptar().setMetodo("aceptoReporte");
        grid_dl.setColumns(4);
        agregarComponente(dia_dialogol);
        
        dia_dialogou.setId("dia_dialogou");
        dia_dialogou.setTitle("SELECCIONAR USUARIO"); //titulo
        dia_dialogou.setWidth("28%"); //siempre en porcentajes  ancho
        dia_dialogou.setHeight("25%");//siempre porcentaje   alto 
        dia_dialogou.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogou.getBot_aceptar().setMetodo("aceptoReporte");
        grid_du.setColumns(4);
        agregarComponente(dia_dialogou);
        
        dia_dialogods.setId("dia_dialogods");
        dia_dialogods.setTitle("SELECCIONAR FECHAS PARA REPORTE"); //titulo
        dia_dialogods.setWidth("26%"); //siempre en porcentajes  ancho
        dia_dialogods.setHeight("25%");//siempre porcentaje   alto 
        dia_dialogods.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogods.getBot_aceptar().setMetodo("aceptoReporte");
        grid_ds.setColumns(4);
        agregarComponente(dia_dialogods);
        
        Boton bot_asig = new Boton();
        bot_asig.setIcon("ui-icon-check");
        bot_asig.setValue("ASIGNAR");
        bot_asig.setMetodo("asignar");
        bar_botones.agregarBoton(bot_asig);
        Boton bot_quita = new Boton();
        bot_quita.setValue("QUITAR");
        bot_quita.setIcon("ui-icon-closethick");
//        bot_quita.setMetodo("quitar");
        bot_quita.setMetodo("abrirBusPlaca");
        bar_botones.agregarBoton(bot_quita);
    }
/*creacion de menú*/
    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("PLACAS");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("SOLICITUD");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);
        
    }
    
    /*Busqueda de empresa para seleccion*/
    public void buscarEmpresa() {
        if (cal_fechabus.getValue() != null && cal_fechabus.getValue().toString().isEmpty() == false) {
               if (cmb_usuario.getValue()!= null) {
                   TablaGenerica tab_dato = ser_Placa.getUsuario(Integer.parseInt(cmb_usuario.getValue()+""));
                     if (!tab_dato.isEmpty()) {
                        set_solicitud.getTab_seleccion().setSql("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA where USU_SOLICITUD LIKE '"+tab_dato.getValor("usuarioin")+"' AND FECHA_SOLICITUD ='" + cal_fechabus.getFecha()+"'");
                        set_solicitud.getTab_seleccion().ejecutarSql();
                        } else {
                               utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                               }
                  }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    public void buscarPlaca(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
                     if (!tab_dato.isEmpty()) {
                            set_placa.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_RVMO FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA where IDE_PLACA= "+tab_dato.getValor("IDE_PLACA"));
                            set_placa.getTab_seleccion().ejecutarSql();
                        } else {
                               utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                               }
    }
/*permite abrir la busqueda para la seleccion de la solicitud aprobar*/
    public void abrirBusqueda() {
        limpiarPanel();
        limpiar();
        set_solicitud.dibujar();
        cal_fechabus.limpiar();
        set_solicitud.getTab_seleccion().limpiar();
    }
    
    public void abrirBusPlaca() {
        set_placa.dibujar();
        set_placa.getTab_seleccion().limpiar();
    }
/*aceptaqcion de busqueda y autocompletado d elos datos*/
    public void aceptarBusqueda() {
        limpiarPanel();
        limpiar();
        if (set_solicitud.getValorSeleccionado() != null) {
            aut_busca.setValor(set_solicitud.getValorSeleccionado());
            set_solicitud.cerrar();
            dibujarPanel();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }

    }
    
    /*Dibujar datos a mostrar*/
    public void dibujarSolicitud(){
         if (aut_busca.getValue() != null) {
            str_opcion = "0";
            limpiarPanel();
        /**
         PANTALLA CABECERA DE SOLICITUS
         */
        tab_solicitud.setId("tab_solicitud"); // NOMBRE PANTALLA - DECLARACION DE CABECERA
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_solicitud.setCondicion("IDE_SOLICITUD_PLACA=-1");
        } else {
            tab_solicitud.setCondicion("IDE_SOLICITUD_PLACA=" + aut_busca.getValor());
        }
//        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setNombreVisual("Nro. SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_SOLICTUD").setNombreVisual("TIPO DE SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_SOLICTUD").setCombo("SELECT IDE_TIPO_SOLICTUD,DESCRIPCION_SOLICITUD FROM TRANS_TIPO_SOLICTUD");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tbp_s = new PanelTabla();
        tbp_s.setPanelTabla(tab_solicitud);
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("PEDIDO DE ASIGNACIÒN - PLACA");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        pan_opcion1.getChildren().add(tbp_s);
        
        tab_detalle.setId("tab_detalle");//DECLARACION DE DETALLE
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,descripcion_vehiculo FROM trans_vehiculo_tipo");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setLectura(true);
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setLectura(true);
        tab_detalle.getColumna("IDE_PLACA").setLectura(true);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("IDE_PLACA").setNombreVisual("PLACA");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS");       
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("IDENTIFICACIÓN");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_detalle.getColumna("CEDULA_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla tbp_d=new PanelTabla(); 
        tbp_d.setPanelTabla(tab_detalle);
        pan_opcion1.getChildren().add(tbp_d);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setHeader("REQUISITOS DE SOLICITUD");//DECLARACION DE SOLICITUD
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setCombo("SELECT IDE_TIPO_REQUISITO,DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO");
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setLectura(true);
        tab_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
        tbp_r.setPanelTabla(tab_requisito);
        pan_opcion1.getChildren().add(tbp_r);
   
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_opcion1);
//        gru.getChildren().add(div1);
        pan_opcion.getChildren().add(gru);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división centraasignarl central
        pan_opcion.getChildren().clear();
        pan_opcion.clearInitialState();
        pan_opcion2.getChildren().clear();
        pan_opcion2.clearInitialState();
        pan_opcion1.getChildren().clear();
        pan_opcion1.clearInitialState();
//         pan_opcion.getChildren().add(efecto);
    }
   public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
  
   
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        aut_busca.onSelect(evt);
        dibujarPanel();
    }
/*
 * creacion panel
 */
       private void dibujarPanel() {
        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
            dibujarSolicitud();
        }
        utilitario.addUpdate("pan_opcion");
    } 
       
 // METODOS PARA LA ASIGNACION DE PLACA Y CAMBIO DE ESTADO A PLACA ASIGNADA      
    public void asignar (){
    String asignacion="INSERT INTO TRANS_APROBACION_PLACA (FECHA_APROBACION,APROBADO,USU_APROBACION,IDE_DETALLE_SOLICITUD) "
            + "VALUES ("+ utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",1,'"+tab_consulta.getValor("NICK_USUA")+"',"+tab_detalle.getValor("IDE_DETALLE_SOLICITUD")+")";
    con_sql.ejecutarSql(asignacion);
    aprobacion();
    }   
       
    public void aprobacion(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        dia_dialogoe.setDialogo(grid_de);
        dia_dialogoe.dibujar();
    }
       
   public void aceptoValores(){
       Integer fisicap = Integer.parseInt(ser_Placa.getTipoDisponibleFP(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO"))));
       Integer papel = Integer.parseInt(ser_Placa.getTipoDisponiblePP(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO"))));
        if(fisicap> 0){
                 TablaGenerica tab_dato = ser_Placa.getAprobacion(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
                    if (!tab_dato.isEmpty()) {
                     // Cargo la información de la base de datos maestra 
                     identificacion = Integer.parseInt(tab_dato.getValor("IDE_APROBACION_PLACA"));
                     ser_Placa.seleccionarPF(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), 
                     Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")), identificacion);
                     aceptoValores1();
                     dia_dialogoe.cerrar();
                     utilitario.agregarMensajeInfo("Placas Fisica Asignada", "");
                     } else {
                         utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ", "");
                         }      
                } else if(fisicap <=0 && papel> 0 ){
                     TablaGenerica tab_dato = ser_Placa.getAprobacion(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
                    if (!tab_dato.isEmpty()) {
                        // Cargo la información de la base de datos maestra 
                        identificacion = Integer.parseInt(tab_dato.getValor("IDE_APROBACION_PLACA"));
                        ser_Placa.seleccionarPP(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), 
                        Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")), identificacion);
                        aceptoValores1();
                        dia_dialogoe.cerrar();
                        utilitario.agregarMensajeInfo("Placa Papel Asignada", "");
                        } else {
                            utilitario.agregarMensajeInfo("Datos no validos", "");
                            } 
                      }else{
                            utilitario.agregarMensajeError("No Existen Placas Disponibles", "");
                            }
   }
   
   
   public void aceptoValores1(){
       TablaGenerica tab_dato = ser_Placa.getIDPlaca(identificacion, Integer.parseInt(tab_solicitud.getValor("IDE_SOLICITUD_PLACA")));
       if (!tab_dato.isEmpty()) {
        ser_Placa.estadoPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
        utilitario.agregarMensaje("ASIGNACIÓN REALIZADA", "");
                dia_dialogoe.cerrar();
                TablaGenerica tab_dato1 = ser_Placa.placasAsigna(Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")),tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
                    if (!tab_dato1.isEmpty()) {
                        System.err.println(tab_dato1.getValor("placa"));
                        utilitario.agregarMensaje("NUMERO DE PLACAS", tab_dato1.getValor("placa"));
                        utilitario.agregarMensaje("NOMBRE PROPIETARIO", tab_detalle.getValor("nombre_propietario"));
                        utilitario.addUpdate("tab_detalle");
                        tab_detalle.actualizar();
                } else {
                        utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
                        utilitario.addUpdate("tab_detalle");
                    }
//        placasDispo();
        } else {
                utilitario.agregarMensajeInfo("Datos no disponibles ", "");
            }
   }
   public void cancelarValores(){
        utilitario.agregarMensajeInfo("ASIGNACIÓN NO REALIZADA", "");
        dia_dialogoe.cerrar();
   }
   
   public void placasDispo(){
       String cadena1,cadena2,cadena3,cadena4;
       cadena4 = null;
   TablaGenerica tab_dato = ser_Placa.placasDis(Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("NUMERO DE PLACAS", tab_dato.getValor("placa"));
                utilitario.agregarMensaje("NUMERO DE PLACAS", tab_detalle.getValor("nombre_propietario"));
                utilitario.addUpdate("tab_detalle");
//                tab_detalle.actualizar();
        } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
                utilitario.addUpdate("tab_detalle");
            }
   }
           
   // METODOS PARA CANCELACION DE PLACA ASIGNADAS 
    public void quitar (){
        dia_dialogoq.Limpiar();
        dia_dialogoq.setDialogo(gridq);
        txt_comentario.setSize(50);
        gridq.getChildren().add(new Etiqueta("MOTIVO DE DESVINCULACIÓN DE PLACA:"));
        gridq.getChildren().add(txt_comentario);
        dia_dialogoq.setDialogo(grid_dq);
        dia_dialogoq.dibujar();
    }
       
    public void aceptarDeligar(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing");
                     ser_Placa.guardarhistorial(Integer.parseInt(tab_dato.getValor("IDE_SOLICITUD_PLACA")), tab_dato.getValor("CEDULA_RUC_EMPRESA"), 
                     Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")), tab_dato.getValor("CEDULA_RUC_PROPIETARIO"), 
                     tab_dato.getValor("NOMBRE_EMPRESA"),Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO")),
                     tab_dato.getValor("NOMBRE_PROPIETARIO"), Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO")), 
                     tab_dato.getValor("NUMERO_RVMO"), txt_comentario.getValue()+"",tab_dato.getValor("ide_placa"));
                     quitarPlaca();
                     set_placa.cerrar();
                  } else {
                          utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                          }
    } 
    
    public void quitarPlaca(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing1");
                    ser_Placa.quitarPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
                    quitarDetalle();
                } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                       }
    }
    
    public void quitarDetalle(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing3");
                    ser_Placa.quitarDetalle(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                    eliminarAprobacion();
                } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
    }
    
    public void eliminarAprobacion(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing4");
                    ser_Placa.eliminarAprobacion(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                    utilitario.agregarMensajeInfo("ASIGNACIÓN ELIMINADA", "");
                    ser_Placa.actuEstado1(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                } else {
                       utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                       }
    }
    
    //CREACION DE REPORTES
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fechabus1.setFechaActual();
        cal_fechabus2.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "SOLICITUD POR USUARIO":
                dia_dialogou.Limpiar();
                dia_dialogou.setDialogo(gridu);
                grid_du.getChildren().add(new Etiqueta("DESDE FECHA:"));
                grid_du.getChildren().add(cal_fechabus1);
                cal_fechabus1.setFechaActual();
                grid_du.getChildren().add(new Etiqueta("HASTA FECHA:"));
                grid_du.getChildren().add(cal_fechabus2);
                cal_fechabus2.setFechaActual();
                grid_du.getChildren().add(new Etiqueta("ELEGIR USUARIO:"));
                cmb_usuario.setId("cmb_usuario");
                cmb_usuario.setCombo("SELECT IDE_USUA,NICK_USUA FROM SIS_USUARIO WHERE IDE_PERF = 13");
                cmb_usuario.eliminarVacio();
                grid_du.getChildren().add(cmb_usuario);
                dia_dialogou.setDialogo(grid_du);
                dia_dialogou.dibujar();
               break;
           case "REPORTE ASIGNADAS":
                    dia_dialogol.Limpiar();
                    dia_dialogol.setDialogo(gridl);
                    cmb_tipos.setId("cmb_tipos");
                    cmb_tipos.setCombo("SELECT IDE_TIPO_SOLICTUD,DESCRIPCION_SOLICITUD FROM TRANS_TIPO_SOLICTUD");
                    cmb_tipos.eliminarVacio();
                    grid_dl.getChildren().add(cmb_tipos);
                    dia_dialogol.setDialogo(grid_dl);
                    dia_dialogol.dibujar();
               break;
          case"REPORTE DESASIGNADAS":
                dia_dialogods.Limpiar();
                dia_dialogods.setDialogo(grids);
                grid_ds.getChildren().add(new Etiqueta("DESDE FECHA:"));
                grid_ds.getChildren().add(cal_fechabus1);
                cal_fechabus1.setFechaActual();
                grid_ds.getChildren().add(new Etiqueta("HASTA FECHA:"));
                grid_ds.getChildren().add(cal_fechabus2);
                cal_fechabus2.setFechaActual();
                dia_dialogods.setDialogo(grid_ds);
                dia_dialogods.dibujar();
              break;
                
        }
    }
    
    public void aceptoReporte(){
        switch (rep_reporte.getNombre()) {
               case "SOLICITUD POR USUARIO":
                   if (cmb_usuario.getValue()!= null) {
                          TablaGenerica tab_dato = ser_Placa.getUsuario(Integer.parseInt(cmb_usuario.getValue()+""));
                     if (!tab_dato.isEmpty()) {
                      p_parametros = new HashMap();
                      p_parametros.put("pide_fechai",cal_fechabus1.getFecha());
                      p_parametros.put("pide_fechaf",cal_fechabus2.getFecha());
                      p_parametros.put("persona", tab_dato.getValor("usuarioin"));
                      p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
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
               case "REPORTE ASIGNADAS":
                      p_parametros = new HashMap();
                      p_parametros.put("tipo", Integer.parseInt(cmb_tipos.getValue()+""));           
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();               
               break;
               case"REPORTE DESASIGNADAS":
                    p_parametros = new HashMap();
                    p_parametros.put("pide_fechai",cal_fechabus1.getFecha());
                    p_parametros.put("pide_fechaf",cal_fechabus2.getFecha());
                    p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
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

    public Combo getCmb_usuario() {
        return cmb_usuario;
    }

    public void setCmb_usuario(Combo cmb_usuario) {
        this.cmb_usuario = cmb_usuario;
    }

    public Combo getCmb_rango() {
        return cmb_rango;
    }

    public void setCmb_rango(Combo cmb_rango) {
        this.cmb_rango = cmb_rango;
    }

    public Combo getCmb_placas() {
        return cmb_placas;
    }

    public void setCmb_placas(Combo cmb_placas) {
        this.cmb_placas = cmb_placas;
    }

    public SeleccionTabla getSet_placa() {
        return set_placa;
    }

    public void setSet_placa(SeleccionTabla set_placa) {
        this.set_placa = set_placa;
    }

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }
    
}
