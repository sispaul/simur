/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud extends Pantalla{

    private Combo cmb_tipos = new Combo();
    //Tablas para Seleccion de atributos
    private Tabla set_tipo = new Tabla();
    private int int_opcion = 1;
    private Tabla set_gestor = new Tabla();
    private Tabla set_gestores = new Tabla();
    private Tabla set_empresa = new Tabla();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Conexion con_ciudadania= new Conexion();
//Autocompletar datos en pantalla    
    private AutoCompletar aut_gestor = new AutoCompletar();
//Textos para auto busqueda
    private Texto txt_cedula = new Texto();
    private Texto txt_nombre = new Texto();
    private Texto txt_empresa = new Texto();
    
//Dibujar Paneles y Menú
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    public pre_ingreso_solicitud() {
        
        con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadaniajdbc"));
        con_ciudadania.NOMBRE_MARCA_BASE="sqlserver";
        
        //BOTON DE SELECCION DE OPCIONES
        Boton bot_ingreso = new Boton();
        bot_ingreso.setValue("SELECCIONAR GESTOR");
        bot_ingreso.setIcon("ui-icon-comment");
        bot_ingreso.setMetodo("aceptoDialogo");
        bar_botones.agregarBoton(bot_ingreso);
        
        //BUSQUEDA Y LLENO AUTOMATICO EN CAMPOS
        
        aut_gestor.setId("aut_empresas");
        aut_gestor.setAutoCompletar("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO FROM TRANS_GESTOR");
        aut_gestor.setMetodoChange("filtrarGestor");
        aut_gestor.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Gestor:"));
        bar_botones.agregarComponente(aut_gestor);
        
        ////Configurar Seleccion Tipo de Gestor
        set_tipo.setId("set_tipo");
        set_tipo.setSql("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR ORDER BY DESCRIPCION_GESTOR");
        set_tipo.setRows(5);
        set_tipo.setTipoSeleccion(false);
        set_tipo.dibujar();
        
        set_empresa.setId("set_empresa");
        set_empresa.setSql("SELECT IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA,RUC_EMPRESA FROM TRANS_COMERCIAL_AUTOMOTORES");
        set_empresa.getColumna("RUC_EMPRESA").setFiltro(true);
        set_empresa.getColumna("NOMBRE_EMPRESA").setFiltro(true);
        set_empresa.setRows(10);
        set_empresa.setTipoSeleccion(false);
        set_empresa.dibujar();
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("SOLICITUD MATRICULA");
        agregarComponente(div_division);
        dibujarSolicitud();
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

     private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("INGRESO SOLICITUD");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("PEDIDO DE PLACA");
        itm_datos_empl.setIcon("ui-icon-note");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

    }
    
    /******DIBUJAR SOLICITUDA Y REQUERIMIENTOS******/
       
     private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
//        aut_gestor.limpiar();
//        utilitario.addUpdate("aut_gestor");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void dibujarSolicitud(){
        str_opcion = "0";
        limpiarPanel();
        
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 700px;top: 200px;");
//        pan_panel.setStyle("text-align:center;position:absolute;top:70px;left:100px;");
        pan_panel.setHeader("Buscar Daros de Solicitante");
       
        cmb_tipos.setId("cmb_tipos");
        cmb_tipos.setStyle("width: 99%;");
//        cmb_tipos.setStyle("text-align:center;position:absolute;top:60px;left:170px;");
        cmb_tipos.setCombo("select ide_tipo_gestor,descripcion_gestor from trans_tipo_gestor");
        pan_panel.getChildren().add(new Etiqueta("TIPO SOLICITANTE : "));
        pan_panel.getChildren().add(cmb_tipos);
        
        txt_cedula.setId("txt_cedula");
        txt_cedula.setStyle("width: 99%;");
        pan_panel.getChildren().add(new Etiqueta("C.I/RUC. : "));
        pan_panel.getChildren().add(txt_cedula);
//        
        txt_empresa.setId("txt_empresa");
        txt_empresa.setStyle("width: 99%;");
        pan_panel.getChildren().add(new Etiqueta("EMPRESA :"));
        pan_panel.getChildren().add(txt_empresa);
        
        txt_nombre.setId("txt_nombre");
        txt_nombre.setStyle("width: 99%;");
        pan_panel.getChildren().add(new Etiqueta("NOMBRE GESTOR :"));
        pan_panel.getChildren().add(txt_nombre);
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("Buscar");
        bot_bus.setIcon("ui-icon-locked");
        bot_bus.setMetodo("BuscarSolicitante");

        pan_panel.setFooter(bot_bus);
        pan_panel.getChildren().add(bot_bus);
        Boton bot_new = new Boton();
        bot_new.setId("bot_new");
        bot_new.setValue("Limpiar");
        bot_new.setIcon("ui-icon-locked");
        bot_new.setMetodo("LimpiarBoton");
//        bot_new.setMetodoRuta("pre_login.ingresar");
//        bot_new.setOnclick("dimiensionesNavegador()");
        pan_panel.setFooter(bot_new);
        pan_panel.getChildren().add(bot_new);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_panel);

        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setVisible(false);
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        tab_solicitud.setStyle(null);
        pan_opcion.setTitle("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        pan_opcion.getChildren().add(tabp1);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setMayusculas(true);
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
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
//        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
//                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
//                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);   

        gru.getChildren().add(tabp1);
        gru.getChildren().add(tabp2);
        gru.getChildren().add(tabp3);
        pan_opcion.getChildren().add(gru);
    }
    
    public void LimpiarBoton(){
        txt_cedula.limpiar();
        utilitario.addUpdate("txt_cedula");
        txt_nombre.limpiar();
        utilitario.addUpdate("txt_nombre");
        
    }
     
    public void BuscarSolicitante(){

        if (utilitario.validarRUC(txt_cedula.getValue()+"")){
            
        tab_solicitud.insertar();
        tab_detalle.insertar();
        }else if(utilitario.validarCedula(txt_cedula.getValue()+""))
        {
            
        tab_solicitud.insertar();
        tab_detalle.insertar();
        }else {
                utilitario.agregarMensajeError("El Número de Identificacion no es válido", "");
                return;
            }
        
        txt_cedula.limpiar();
        utilitario.addUpdate("txt_cedula");
        txt_nombre.limpiar();
        utilitario.addUpdate("txt_nombre");
        
    }    
    
    @Override
    public void insertar() {
    if (str_opcion.equals("0")) {
        if (tab_solicitud.isFocus()) {
                tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                tab_detalle.insertar();
            }  
        }
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_gestor() {
        return set_gestor;
    }

    public void setSet_gestor(Tabla set_gestor) {
        this.set_gestor = set_gestor;
    }

    public Tabla getSet_gestores() {
        return set_gestores;
    }

    public void setSet_gestores(Tabla set_gestores) {
        this.set_gestores = set_gestores;
    }

    public Tabla getSet_empresa() {
        return set_empresa;
    }

    public void setSet_empresa(Tabla set_empresa) {
        this.set_empresa = set_empresa;
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

    public AutoCompletar getAut_gestor() {
        return aut_gestor;
    }

    public void setAut_gestor(AutoCompletar aut_gestor) {
        this.aut_gestor = aut_gestor;
    }
    
}
