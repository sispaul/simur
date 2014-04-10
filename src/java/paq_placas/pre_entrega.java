/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
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
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_entrega extends Pantalla{
Integer consulta,placa,vehiculo,servicio;
String cedula,factura;
    
    private Texto tex_fecha = new Texto();
    private Texto tex_empresa = new Texto();
    private Texto tex_gestor = new Texto();
    private Texto tex_usu_in = new Texto();
    private Texto tex_fech_apro = new Texto();
    private Texto tex_placa = new Texto();
    private Texto tex_usu_ap = new Texto();
    
    private AutoCompletar aut_busca = new AutoCompletar();
    
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Panel pan_opcion3 = new Panel();
    private Efecto efecto1 = new Efecto();

        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    private Tabla tab_detalle = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();

    private Texto txt_buscar = new Texto();
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
            
    private Etiqueta eti_etiqueta= new Etiqueta();
    private Etiqueta eti_etiqueta1= new Etiqueta();     
    
    public pre_entrega() {
                bar_botones.quitarBotonInsertar();
		bar_botones.quitarBotonEliminar();
		bar_botones.quitarBotonsNavegacion();
        
        /*
         * Creación de Botones; Busqueda/Limpieza
         */
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        /*
         * METODO PARA AUTOCOMPLETAR LA BUSQUEDA
         */
        
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,d.NUMERO_FACTURA,p.PLACA\n" +
                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA d,TRANS_PLACA p,TRANS_TIPO_ESTADO e\n" +
                                    "WHERE d.IDE_PLACA = p.IDE_PLACA AND p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO AND e.DESCRIPCION_ESTADO LIKE 'asignada'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Propietario:"));
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
        efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion.getChildren().add(efecto1);
        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "10%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("TRANSPORTES");
        agregarComponente(div_division);
        dibujarDetalle();
        
        /*Boton para asignacion de estados*/
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);    
        txt_buscar.setSize(30);
        gri_busca.getChildren().add(txt_buscar);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_FACTURA FROM TRANS_DETALLE_SOLICITUD_PLACA where IDE_DETALLE_SOLICITUD=-1", "IDE_DETALLE_SOLICITUD");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR PROPIETARIO");
        agregarComponente(set_solicitud);
        
        /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }
    
        public void buscarEmpresa() {
        if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,d.IDE_PLACA,d.NUMERO_FACTURA,\n" +
                                                    "d.IDE_SOLICITUD_PLACA,d.IDE_TIPO_SERVICIO,d.IDE_APROBACION_PLACA,\n" +
                                                    "d.IDE_TIPO_VEHICULO,d.APROBADO_SOLICITUD\n" +
                                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA d,TRANS_PLACA p,TRANS_TIPO_ESTADO e\n" +
                                                    "WHERE d.IDE_PLACA = p.IDE_PLACA AND p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO AND\n" +
                                                    "e.DESCRIPCION_ESTADO LIKE 'asignada' AND d.CEDULA_RUC_PROPIETARIO LIKE'" + txt_buscar.getValue() + "'");
            set_solicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }

    }

    public void abrirBusqueda() {
        set_solicitud.dibujar();
        txt_buscar.limpiar();
        set_solicitud.getTab_seleccion().limpiar();
    }

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
    
    /*creacion de menú*/
    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("PLACA");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("ENTREGA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);
    }
    
    public void dibujarDetalle(){
        if (aut_busca.getValue() != null) {
            str_opcion = "0";
            limpiarPanel();
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_detalle.setCondicion("IDE_DETALLE_SOLICITUD=-1");
        } else {
            tab_detalle.setCondicion("IDE_DETALLE_SOLICITUD=" + aut_busca.getValor());
        }
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setMetodoChange("aceptoretiro");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla pat_panel=new PanelTabla();
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.setPanelTabla(tab_detalle);
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Entrega");
        itm_actualizar.setIcon("ui-icon-refresh");
        itm_actualizar.setMetodo("Entrega");
        
                /*
         * CREACION DE DE CAMPOS QUE MOSTRARAN LOS DATOS EN GRID DENTRO DE UN PANEL
         */
        
        pan_opcion1.setId("pan_opcion1");
        pan_opcion1.setHeader("REFERENCIAS DE SOLICITUD - APROBACIÒN");
        pan_opcion1.getChildren().add(pat_panel);
        pan_opcion1.getChildren().add(pan_opcion2);
        pan_opcion1.getChildren().add(pan_opcion3);

        Grid gri_fechai = new Grid();
        gri_fechai.setColumns(2);
        gri_fechai.getChildren().add(new Etiqueta("FECHA SOLICITUD: "));
        tex_fecha.setId("tex_fecha");
        gri_fechai.getChildren().add(tex_fecha);

        Grid gri_usin = new Grid();
        gri_usin.setColumns(2);
        gri_usin.getChildren().add(new Etiqueta("USUARIO - INGRESO: "));
        tex_usu_in.setId("tex_usu_in");
        gri_usin.getChildren().add(tex_usu_in);
        
        Grid gri_placa = new Grid();
        gri_placa.setColumns(2);
        tex_empresa.setId("tex_empresa");
        tex_empresa.setStyle("width: 220%;");
        gri_placa.getChildren().add(new Etiqueta("EMPRESA GESTIONA: "));
        gri_placa.getChildren().add(tex_empresa);
        
        Grid gri_usap = new Grid();
        gri_usap.setColumns(2);
        tex_gestor.setId("tex_gestor");
        tex_gestor.setStyle("width: 199%;");
        gri_usap.getChildren().add(new Etiqueta("GESTOR: "));
        gri_usap.getChildren().add(tex_gestor);
        
        Grid gri_pl = new Grid();
        gri_pl.setColumns(2);
        gri_pl.getChildren().add(new Etiqueta("TIPO SOLICITUD: "));
        tex_placa.setId("tex_placa9");
        gri_pl.getChildren().add(tex_placa);
        
        Grid gri_ti = new Grid();
        gri_ti.setColumns(2);
        gri_ti.getChildren().add(new Etiqueta("FECHA DE APROBACIÒN: "));
        tex_fech_apro.setId("tex_fech_apro");
        gri_ti.getChildren().add(tex_fech_apro);
       
        Grid gri_pr = new Grid();
        gri_pr .setColumns(2);
        gri_pr.getChildren().add(new Etiqueta("USUARIO - APROBACIÒN : "));
        gri_pr.getChildren().add(tex_usu_ap);
        
        pan_opcion2.getChildren().add(gri_fechai);
        pan_opcion2.getChildren().add(gri_pl);
        pan_opcion2.getChildren().add(gri_placa);
        pan_opcion2.getChildren().add(gri_usap);
        pan_opcion2.getChildren().add(gri_usin);
        pan_opcion3.getChildren().add(gri_ti);
        pan_opcion3.getChildren().add(gri_pr);
        
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.setHeader("ENTREGA DE PLACAS- PARTICULAR/EMPRESA");
        pan_opcion.getChildren().add(gru);
        pan_opcion.getChildren().add(pan_opcion1);
        aceptoValores();
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
    }
    
        private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        pan_opcion2.getChildren().clear();
        pan_opcion3.getChildren().clear();
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
            dibujarDetalle();
        }
        utilitario.addUpdate("pan_opcion");
    } 
       
            public void aceptoretiro(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_entrega");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        }
     }   
     
         public void aceptoValores(){
        if (set_solicitud.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = ser_Placa.getEntrega(Integer.parseInt(set_solicitud.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
//                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("NOMBRE_PROPIETARIO"));
                tex_fecha.setValue(tab_dato.getValor("FECHA_SOLICITUD"));
                tex_empresa.setValue(tab_dato.getValor("NOMBRE_EMPRESA"));
                tex_gestor.setValue(tab_dato.getValor("NOMBRE_GESTOR"));
                tex_usu_in.setValue(tab_dato.getValor("USU_SOLICITUD"));
                
                tex_fech_apro.setValue(tab_dato.getValor("FECHA_APROBACION"));
                tex_placa.setValue(tab_dato.getValor("PLACA"));
                tex_usu_ap.setValue(tab_dato.getValor("USU_APROBACION"));
                
                eti_etiqueta.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta.setValue(tab_dato.getValor("PLACA"));
                eti_etiqueta1.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta1.setValue("PLACA:");
                pan_opcion2.getChildren().add(eti_etiqueta1);
                pan_opcion2.getChildren().add(eti_etiqueta);
                
                consulta = Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
                cedula = tab_dato.getValor("CEDULA_RUC_PROPIETARIO");
                placa = Integer.parseInt(tab_dato.getValor("IDE_PLACA"));
                vehiculo=Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO"));
                servicio=Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO"));
                factura=tab_dato.getValor("NUMERO_FACTURA");
                
                utilitario.addUpdate("tab_detalle");
                utilitario.addUpdate("pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos", "");
            }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
     }       
            
     public void Entrega(){
         
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

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
    }
    
}
