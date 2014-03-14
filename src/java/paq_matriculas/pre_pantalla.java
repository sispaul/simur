/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_pantalla extends Pantalla{
    //Textos para auto busqueda
    private Texto txt_cedula = new Texto();
    private Texto txt_nombre = new Texto();
    private Texto txt_empresa = new Texto();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Combo cmb_tipos = new Combo();
    private Tabla tab_gestor = new Tabla();
    
    private Panel pan_opcion = new Panel();
    private Calendario cal_calendario = new Calendario();
    
    private Tabla tab_consulta = new Tabla();
    private Efecto efecto = new Efecto();
    private Conexion con_ciudadania= new Conexion();
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    public pre_pantalla() {
        
        con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadaniajdbc"));
        con_ciudadania.NOMBRE_MARCA_BASE="sqlserver";

	Panel pan_menu = new Panel();
	pan_menu.setHeader("OPCIONES");

	Grupo gru_panel_izquierda = new Grupo();
	gru_panel_izquierda.getChildren().add(pan_menu);
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 255px");
        pan_panel.setHeader("Buscar Datos de Solicitante");
        gru_panel_izquierda.getChildren().add(pan_panel);
         
        cmb_tipos.setId("cmb_tipos");
        cmb_tipos.setStyle("width: 99%;");
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
        txt_empresa.setDisabled(true);
        pan_panel.getChildren().add(new Etiqueta("EMPRESA :"));
        pan_panel.getChildren().add(txt_empresa);
        
        txt_nombre.setId("txt_nombre");
        txt_nombre.setStyle("width: 99%;");
        txt_nombre.setDisabled(true);
        pan_panel.getChildren().add(new Etiqueta("NOMBRE GESTOR :"));
        pan_panel.getChildren().add(txt_nombre);
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("Buscar");
        bot_bus.setIcon("ui-icon-locked");
        bot_bus.setMetodo("buscarPersona");
        
        pan_panel.getChildren().add(bot_bus);
        Boton bot_new = new Boton();
        bot_new.setId("bot_new");
        bot_new.setValue("Limpiar");
        bot_new.setIcon("ui-icon-locked");
        bot_new.setMetodo("LimpiarBoton");
        pan_panel.getChildren().add(bot_new);
                
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
        
        
    }
public void buscarSolicitante(){

	pan_opcion.setHeader("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        tab_solicitud.setMostrarNumeroRegistros(false);       
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setVisible(false);
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setValorDefecto(utilitario.getVariable(cmb_tipos.getValue()+""));
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
//        tab_solicitud.getColumna("IDE_GESTOR").setValorDefecto();
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
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
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("TIPO DE SERVICIO");
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
        Boton bot1 = new Boton();
        bot1.setValue("MOSTRAR REQUISITOS");
        bot1.setMetodo("aceptoRequisitos");
        bot1.setIcon("ui-icon-search");
        tabp2.getChildren().add(bot1);

        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
//        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
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
        System.out.println("ejecuta1");
        ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
        utilitario.addUpdate("tab_requisito");
        tab_requisito.actualizar();
        System.out.println("ejecuta2");
    }
    
    public void buscarPersona(){
        if (utilitario.validarRUC(txt_cedula.getValue()+"")){
            
            tab_solicitud.insertar();
            tab_detalle.insertar();
            con_ciudadania.desconectar();
         }else if(utilitario.validarCedula(txt_cedula.getValue()+"")){
                     
                    tab_solicitud.insertar();
                    tab_detalle.insertar();
                    con_ciudadania.desconectar();
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
    
}
