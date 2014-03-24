/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;


import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
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


/**
 *
 * @author KEJA
 */
public class pre_aprobado extends Pantalla{
private Tabla tab_solicitud = new Tabla();
private Tabla tab_requisito = new Tabla();
private Tabla tab_aprobacion = new Tabla();
private Tabla tab_consulta = new Tabla();

private AutoCompletar aut_busca = new AutoCompletar();
private SeleccionTabla set_solicitud = new SeleccionTabla();
private Texto tex_busqueda = new Texto();
private Panel pan_opcion = new Panel();
private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
private PanelMenu pam_menu = new PanelMenu();

private Dialogo dia_dialogoe = new Dialogo();
private Grid grid_de = new Grid();
private Grid gride = new Grid();

@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    public pre_aprobado() {
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_DETALLE_SOLICITUD,IDE_SOLICITUD_PLACA,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA \n" +
                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        tex_busqueda.setSize(45);
        gri_busca.getChildren().add(tex_busqueda);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA \n" +
                                        "FROM TRANS_DETALLE_SOLICITUD_PLACA WHERE IDE_DETALLE_SOLICITUD=-1", "IDE_DETALLE_SOLICITUD");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR PERSONA");
        agregarComponente(set_solicitud);
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
       
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR ASIGNACIÓN"); //titulo
        dia_dialogoe.setWidth("17%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("8%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
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

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("APROBACIÓN");
        itm_permisos.setIcon("ui-icon-key");
        itm_permisos.setMetodo("aceptarAprobacion");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);

    }
    
        public void aceptarPlaca(){
//        tab_aprobacion.actualizar();
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        dia_dialogoe.setDialogo(grid_de);
        dia_dialogoe.dibujar();
     }    
      
     public void cancelarValores(){
        utilitario.agregarMensajeInfo("ASIGNACIÓN NO REALIZADA", "");
        dia_dialogoe.cerrar();
     }
      
     public void aceptoValores(){
        ser_Placa.seleccionarP(Integer.parseInt(tab_solicitud.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_solicitud.getValor("IDE_tipo_vehiculo")), 
        Integer.parseInt(tab_solicitud.getValor("IDE_tipo_servicio")), Integer.parseInt(tab_aprobacion.getValor("IDE_APROBACION_PLACA")));
        tab_solicitud.actualizar();
        System.out.println(tab_solicitud.getValor("IDE_PLACA"));
        ser_Placa.estadoPlaca(Integer.parseInt(tab_solicitud.getValor("IDE_PLACA")));
        utilitario.agregarMensaje("ASIGNACIÓN REALIZADA", "");
        dia_dialogoe.cerrar();
     }    
        
    public void dibujarSolicitud(){
        if (aut_busca.getValue() != null) {
            str_opcion = "0";
            limpiarPanel();
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 1);
        if (aut_busca.getValue() == null) {
            tab_solicitud.setCondicion("IDE_DETALLE_SOLICITUDa=-1");
        } else {
            tab_solicitud.setCondicion("IDE_DETALLE_SOLICITUD=" + aut_busca.getValor());
        }
        tab_solicitud.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_solicitud.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_solicitud.getColumna("IDE_TIPO_VEHICULO").setLectura(true);
        tab_solicitud.getColumna("IDE_TIPO_SERVICIO").setLectura(true);
        tab_solicitud.getColumna("IDE_PLACA").setLectura(true);
        tab_solicitud.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_solicitud.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_solicitud.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("IDE_PLACA").setNombreVisual("PLACA");
        tab_solicitud.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");       
        tab_solicitud.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("SERVICIO");
        tab_solicitud.getColumna("IDE_TIPO_VEHICULO").setNombreVisual("VEHICULO");
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("PROPIETARIO");
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setLectura(true);
        tab_solicitud.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("IDENTIFICACIÓN");
        tab_solicitud.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        tab_solicitud.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_solicitud.agregarRelacion(tab_requisito);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.dibujar();
        PanelTabla tbp_s=new PanelTabla(); 
        tbp_s.setPanelTabla(tab_solicitud);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setHeader("REQUISITOS DE SOLICITUD");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 2);
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setCombo("SELECT IDE_TIPO_REQUISITO,DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO");
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setLectura(true);
        tab_requisito.getGrid().setColumns(4);
        tab_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
        tbp_r.setPanelTabla(tab_requisito);
   
            Grupo gru = new Grupo();
            gru.getChildren().add(tbp_s);
            gru.getChildren().add(tbp_r);
            pan_opcion.getChildren().add(gru);
            
            } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }
    
    public void aceptarAprobacion(){
            str_opcion = "0";
            limpiarPanel();
        tab_aprobacion.setId("tab_aprobacion");
       tab_aprobacion.setTabla("trans_aprobacion_placa", "ide_aprobacion_placa", 3);
       tab_aprobacion.getColumna("IDE_APROBACION_PLACA").setNombreVisual("ID");
       tab_aprobacion.getColumna("FECHA_APROBACION").setNombreVisual("FECHA APROBACIÓN");
       tab_aprobacion.getColumna("USU_APROBACION").setNombreVisual("USU. APRUEBA");
       tab_aprobacion.getColumna("COMENTARIO_APROBACION").setNombreVisual("COMENTARIO");
       tab_aprobacion.getColumna("FECHA_APROBACION").setValorDefecto(utilitario.getFechaActual());
       tab_aprobacion.getColumna("FECHA_APROBACION").setLectura(true);
       tab_aprobacion.getColumna("USU_APROBACION").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
       tab_aprobacion.getColumna("USU_APROBACION").setLectura(true);
       tab_aprobacion.getGrid().setColumns(2);
       tab_aprobacion.setTipoFormulario(true);
       tab_aprobacion.dibujar();
        PanelTabla tbp_a=new PanelTabla();
        tbp_a.setPanelTabla(tab_aprobacion);
        pan_opcion.getChildren().add(tbp_a);
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
//         pan_opcion.getChildren().add(efecto);
    }
    
    public void buscarEmpresa() {
        if (tex_busqueda.getValue() != null && tex_busqueda.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA \n" +
                                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA\n" +
                                                    "WHERE UPPER(CEDULA_RUC_PROPIETARIO) LIKE'" + tex_busqueda.getValue() + "'");
            set_solicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }

    }

    public void abrirBusqueda() {
        set_solicitud.dibujar();
        tex_busqueda.limpiar();
        set_solicitud.getTab_seleccion().limpiar();
    }

    public void aceptarBusqueda() {
        if (set_solicitud.getValorSeleccionado() != null) {
            aut_busca.setValor(set_solicitud.getValorSeleccionado());
            set_solicitud.cerrar();
            dibujarPanel();
            utilitario.addUpdate("aut_busca,pan_opcion");

            
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una propietario", "");
        }

    }
    
    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_solicitud.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_solicitud");
            utilitario.addUpdate("tab_requisito");
        }
        dibujarPanel();
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
    
       private void dibujarPanel() {
        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
            dibujarSolicitud();
        } else if (str_opcion.equals("1")) {
            aceptarAprobacion();
        }
        utilitario.addUpdate("pan_opcion");
    }     
        
    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }    
        
    @Override
    public void insertar() {
                tab_aprobacion.insertar();
    }

    @Override
    public void guardar() {
        if (tab_aprobacion.guardar()) {
            if (guardarPantalla().isEmpty()) {
                aceptarPlaca();
            }
        }
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

    public Tabla getTab_aprobacion() {
        return tab_aprobacion;
    }

    public void setTab_aprobacion(Tabla tab_aprobacion) {
        this.tab_aprobacion = tab_aprobacion;
    }

}