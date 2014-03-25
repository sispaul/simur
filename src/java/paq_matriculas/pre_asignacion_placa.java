/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

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
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_asignacion_placa extends Pantalla{

    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private AutoCompletar aut_busca = new AutoCompletar();
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();
    private Calendario cal_fechabus = new Calendario();
    private Grid grid = new Grid();
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    
    public pre_asignacion_placa() {

        //campos fecha       
        grid.getChildren().add(new Etiqueta("FECHA INICIAL"));
        grid.getChildren().add(cal_fechabus);

        /*
         * Creación de Botones; Busqueda/Limpieza
         */
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
//        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
//        bar_botones.agregarComponente(aut_busca);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
 //campos fecha       
        gri_busca.getChildren().add(new Etiqueta("FECHA INICIAL"));
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
        aut_busca.setMetodoChange("buscarSolicitud");
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
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("MENU DE OPCIONES");
        agregarComponente(div_division);
        dibujarSolicitud();
        
        /*
         * VENTANA DE BUSQUEDA
         */
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("BUSQUEDA DE SOLICITUDES POR FECHA"); //titulo
        dia_dialogoe.setWidth("17%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("8%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("select ide_empresa,nombre,ruc from trans_empresa where ide_empresa=-1", "ide_empresa");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("DIRECCIÓN DE TRANSPORTE");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("SOLICITUD PLACA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);
    }
    
    
    public void dibujarSolicitud(){
        /**
         PANTALLA CABECERA DE SOLICITUS
         */
        tab_solicitud.setId("tab_solicitud"); // NOMBRE PANTALLA - DECLARACION DE CABECERA
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setVisible(false);
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("NOMBRE_EMPRESA").setNombreVisual("NOMBRE EMPRESA");
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setNombreVisual("Nro. SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setNombreVisual("TIPO DE SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setCombo("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tbp_s = new PanelTabla();
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("INGRESO DE SOLICITUD PARA PEDIDO - PLACA");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        tbp_s.getChildren().add(pan_opcion1);
        tbp_s.setPanelTabla(tab_solicitud);
        
        tab_detalle.setId("tab_detalle");//DECLARACION DE DETALLE
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
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
        tab_requisito.getGrid().setColumns(4);
        tab_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
        tbp_r.setPanelTabla(tab_requisito);
   
        Grupo gru = new Grupo();
        gru.getChildren().add(tbp_s);
        gru.getChildren().add(tbp_d);
        gru.getChildren().add(tbp_r);
        pan_opcion.getChildren().add(gru);
    }
    
    public void buscarSolicitud(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_solicitud.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_solicitud");
            utilitario.addUpdate("tab_detalle");
            utilitario.addUpdate("tab_requisito");
        }
        dibujarPanel();
    }
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
//         pan_opcion.getChildren().add(efecto);
    }
   public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
    
       private void dibujarPanel() {
        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
            dibujarSolicitud();
        } //else if (str_opcion.equals("1")) {
            //aceptarAprobacion();
       // }
        utilitario.addUpdate("pan_opcion");
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
