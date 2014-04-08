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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
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

    private AutoCompletar aut_busca = new AutoCompletar();
    
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();

    
    private Tabla tab_detalle = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();

    private Texto txt_buscar = new Texto();
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
            
            
    public pre_entrega() {
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
        aut_busca.setAutoCompletar("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_FACTURA,IDE_APROBACION_PLACA FROM TRANS_DETALLE_SOLICITUD_PLACA");
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
    }
    
        public void buscarEmpresa() {
        if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_FACTURA FROM TRANS_DETALLE_SOLICITUD_PLACA where CEDULA_RUC_PROPIETARIO LIKE'" + txt_buscar.getValue() + "'");
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
            utilitario.addUpdate("aut_empresas,pan_opcion");
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
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");
        tab_detalle.getColumna("CEDULA_RUC_RETIRA").setMetodoChange("aceptoretiro");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla pat_panel=new PanelTabla();
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        pat_panel.setPanelTabla(tab_detalle);

        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Entrega");
        itm_actualizar.setIcon("ui-icon-refresh");
        itm_actualizar.setMetodo("Entrega");
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("ENTREGA DE PLACAS");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        pat_panel.getChildren().add(pan_opcion1);
        pat_panel.setPanelTabla(tab_detalle);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru);
//        pat_panel.setPanelTabla(tab_detalle);
//        agregarComponente(pat_panel);
        
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
    }
    
        private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
//        pan_opcion2.getChildren().clear();
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
            dibujarDetalle();
        }
        utilitario.addUpdate("pan_opcion");
    } 
       
            public void aceptoretiro(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_RUC_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_entrega");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_RUC_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
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
