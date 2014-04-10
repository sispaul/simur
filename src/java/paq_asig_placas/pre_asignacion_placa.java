/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
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
    
    private Texto txt_comentario = new Texto();
    
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Efecto efecto1 = new Efecto();
    private Efecto efecto2 = new Efecto();
    
    private Calendario cal_fechabus = new Calendario();
    
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogoq = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_dq = new Grid();
    private Grid gride = new Grid();
    private Grid gridq = new Grid();
    
    private Conexion conexion= new Conexion();
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    public pre_asignacion_placa() {
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        
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
        
        /*Boton para asignacion de estados*/
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);    
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
        aut_busca.setAutoCompletar("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA,NOMBRE_GESTOR,DESCRIPCION_SOLICITUD\n" +
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
        div_division.dividir2(pam_menu, pan_opcion, "15%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("DIRECCIÓN DE TRANSPORTE");
        agregarComponente(div_division);
        dibujarSolicitud();
        
        /*
         * VENTANA DE BUSQUEDA
         */
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR ASIGNACIÓN"); //titulo
        dia_dialogoe.setWidth("17%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("8%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA,DESCRIPCION_SOLICITUD FROM TRANS_SOLICITUD_PLACA where IDE_SOLICITUD_PLACA=-1", "IDE_SOLICITUD_PLACA");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
        
        dia_dialogoq.setId("dia_dialogoq");
        dia_dialogoq.setTitle("PORQUE DESEA CANCELAR ASIGNACIÒN"); //titulo
        dia_dialogoq.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoq.setHeight("30%");//siempre porcentaje   alto 
        dia_dialogoq.setResizable(false); //para que no se pueda cambiar el tamaño
        txt_comentario.setSize(50);
        grid_dq.getChildren().add(txt_comentario);
        dia_dialogoq.getBot_aceptar().setMetodo("aceptarDeligar");
        grid_dq.setColumns(4);
        agregarComponente(dia_dialogoq);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
    }
/*creacion de menú*/
    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("ASIGNACIÒN PLACA");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("SOLICITUD PLACA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);
    }
    
    /*Busqueda de empresa para seleccion*/
    public void buscarEmpresa() {
        if (cal_fechabus.getValue() != null && cal_fechabus.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA where FECHA_SOLICITUD ='" + cal_fechabus.getFecha()+"'");
            set_solicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }

    }
/*permite abrir la busqueda para la seleccion de la solicitud aprobar*/
    public void abrirBusqueda() {

        set_solicitud.dibujar();
        cal_fechabus.limpiar();
        set_solicitud.getTab_seleccion().limpiar();
    }
/*aceptaqcion de busqueda y autocompletado d elos datos*/
    public void aceptarBusqueda() {
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
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setVisible(false);
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
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("PEDIDO DE ASIGNACIÒN - PLACA");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        tbp_s.getChildren().add(pan_opcion1);
        tbp_s.setPanelTabla(tab_solicitud);
        
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
        tab_detalle.getColumna("IDE_PLACA").setNombreVisual("PLACA");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");       
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("IDENTIFICACIÓN");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla tbp_d=new PanelTabla(); 
        tbp_d.setPanelTabla(tab_detalle);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setHeader("REQUISITOS DE SOLICITUD");//DECLARACION DE SOLICITUD
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setCombo("SELECT IDE_TIPO_REQUISITO,DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO");
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setLectura(true);
        tab_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
        tbp_r.setPanelTabla(tab_requisito);
   
        Boton bot_asig = new Boton();
        bot_asig.setIcon("ui-icon-check");
        bot_asig.setValue("ASIGNAR PLACA");
        bot_asig.setMetodo("asignar");
        Boton bot_quita = new Boton();
        bot_quita.setValue("QUITAR ASIGNACIÒN");
        bot_quita.setIcon("ui-icon-closethick");
        bot_quita.setMetodo("quitar");
        
        pan_opcion2.setId("pan_opcion2");
	pan_opcion2.setTransient(true);
        pan_opcion2.setHeader("ASIGNACIÒN DE PEDIDO - PLACA");
	efecto2.setType("drop");
	efecto2.setSpeed(150);
	efecto2.setPropiedad("mode", "'show'");
	efecto2.setEvent("load");
        pan_opcion2.getChildren().add(efecto2);
        pan_opcion2.getChildren().add(bot_asig);
        pan_opcion2.getChildren().add(bot_quita);
        Division div = new Division();
        div.dividir2(tbp_r, pan_opcion2, "60%", "v");
        Division div1 = new Division();
        div1.dividir2(div, null, "50%", "h");
        Grupo gru = new Grupo();
        gru.getChildren().add(tbp_s);
        gru.getChildren().add(tbp_d);
        gru.getChildren().add(div1);
        pan_opcion.getChildren().add(gru);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        pan_opcion2.getChildren().clear();
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
 * cracionpanel
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
    conexion.ejecutarSql(asignacion);
    aprobacion();
    }   
       
    public void aprobacion(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        dia_dialogoe.setDialogo(grid_de);
        dia_dialogoe.dibujar();
    }
   public void aceptoValores(){
     TablaGenerica tab_dato = ser_Placa.getAprobacion(utilitario.getFormatoFechaSQL(utilitario.getFechaActual()), Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                identificacion = Integer.parseInt(tab_dato.getValor("IDE_APROBACION_PLACA"));
                ser_Placa.seleccionarP(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), 
                Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")), identificacion);
                utilitario.addUpdate("tab_detalle");
                aceptoValores1();
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
   }
   
   public void aceptoValores1(){
       TablaGenerica tab_dato = ser_Placa.getIDPlaca(identificacion, Integer.parseInt(tab_solicitud.getValor("IDE_SOLICITUD_PLACA")));
            if (!tab_dato.isEmpty()) {
        ser_Placa.estadoPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
        utilitario.agregarMensaje("ASIGNACIÓN REALIZADA", "");
        utilitario.addUpdate("tab_detalle");
        dia_dialogoe.cerrar();
        } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
   }
   public void cancelarValores(){
        utilitario.agregarMensajeInfo("ASIGNACIÓN NO REALIZADA", "");
        dia_dialogoe.cerrar();
   }
           
   // METODOS PARA CANCELACION DE PLACA ASIGNADAS 
    public void quitar (){
        dia_dialogoq.Limpiar();
        dia_dialogoq.setDialogo(gridq);
        dia_dialogoq.setDialogo(grid_dq);
        dia_dialogoq.dibujar();
    }
       
    public void aceptarDeligar(){
        ser_Placa.guardarhistorial(Integer.parseInt(tab_solicitud.getValor("IDE_SOLICITUD_PLACA")), tab_solicitud.getValor("CEDULA_RUC_EMPRESA"), 
                Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"), 
                tab_solicitud.getValor("NOMBRE_EMPRESA"),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")),
                tab_detalle.getValor("NOMBRE_PROPIETARIO"), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), 
                Integer.parseInt(tab_detalle.getValor("NUMERO_FACTURA")), txt_comentario.getValue()+"");
        quitarPlaca();
    } 
    
    public void quitarPlaca(){
        ser_Placa.quitarPlaca(Integer.parseInt(tab_detalle.getValor("IDE_PLACA")));
        quitarDetalle();
    }
    public void quitarDetalle(){
        ser_Placa.quitarDetalle(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
        eliminarAprobacion();
    }
    public void eliminarAprobacion(){
        ser_Placa.eliminarAprobacion(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
        utilitario.agregarMensajeInfo("ASIGNACIÓN ELIMINADA", "");
        dia_dialogoq.cerrar();
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
    
}
