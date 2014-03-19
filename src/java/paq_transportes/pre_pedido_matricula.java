/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_pedido_matricula extends Pantalla{

private AutoCompletar aut_busca = new AutoCompletar();
private Tabla tab_solicitud = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_detalle = new Tabla();
private Dialogo dia_dialogo2 = new Dialogo();
private Dialogo dia_dialogo3 = new Dialogo();
private Grid grid2 = new Grid();
private Grid grid_de2 = new Grid();
private Grid grid3 = new Grid();
private Grid grid_de3 = new Grid();
private Tabla set_vehiculo = new Tabla();
private Tabla set_servicio = new Tabla();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);


    public pre_pedido_matricula() {
        
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("IDE_GESTOR").setCombo("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR FROM TRANS_GESTOR ORDER BY NOMBRE_GESTOR");
        tab_solicitud.getColumna("IDE_GESTOR").setFiltro(true);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("Descripción de Solicitud");
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. Automotores");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("Fecha");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        Boton bot_placa = new Boton();
        bot_placa.setValue("INGRESO NUEVA SOLICITUD");
        bot_placa.setIcon("ui-icon-document");
        bot_placa.setMetodo("aceptoDialogo2");
        tabp2.getChildren().add(bot_placa);
        tabp2.setPanelTabla(tab_detalle); 
        
       tab_tabulador.agregarTab("DETALLE DE SOLICTUD DE MATRICULA", tabp2);
        
        Division div_division = new Division();
        div_division.dividir2(tabp1, tab_tabulador, "35%", "H");
        agregarComponente(div_division);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar(); 
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
        
        dia_dialogo2.setId("dia_dialogo2");
        dia_dialogo2.setTitle("VEHICULO - QUE SE SOLICITA"); //titulo
        dia_dialogo2.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo2.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo2.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo2.getBot_aceptar().setMetodo("aceptoValores2");
        grid_de2.setColumns(4);
        grid_de2.getChildren().add(new Etiqueta("SELECCIONE VEHICULO"));
        agregarComponente(dia_dialogo2);
        
        dia_dialogo3.setId("dia_dialogo3");
        dia_dialogo3.setTitle("SERVICIO - FUNCIONAMIENTO DE VEHICULO"); //titulo
        dia_dialogo3.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo3.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo3.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo3.getBot_aceptar().setMetodo("aceptoValores3");
        grid_de3.setColumns(4);
        grid_de3.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo3);
    }
    

 public void aceptoDialogo2() {
        dia_dialogo2.Limpiar();
        dia_dialogo2.setDialogo(grid2);
        grid_de2.getChildren().add(set_vehiculo);
        dia_dialogo2.setDialogo(grid_de2);
        set_vehiculo.dibujar();
        dia_dialogo2.dibujar();
    }
    
    public void aceptoValores2(){
        if (set_vehiculo.getValorSeleccionado()!= null) {
                        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setValorDefecto(set_vehiculo.getValorSeleccionado());
                        aceptoDialogo3();
                        dia_dialogo2.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    
     public void aceptoDialogo3() {
        dia_dialogo3.Limpiar();
        dia_dialogo3.setDialogo(grid3);
        grid_de3.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo3.setDialogo(grid_de3);
        set_servicio.dibujar();
        dia_dialogo3.dibujar();
    }
    
    public void aceptoValores3() {
            if (set_servicio.getValorSeleccionado()!= null) {
                        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setValorDefecto(set_servicio.getValorSeleccionado());
                        tab_detalle.insertar();
                        dia_dialogo3.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    } 
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_solicitud.guardar()) {
                      if (tab_detalle.guardar()) {
//                          ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
                           utilitario.addUpdate("tab_detalle");
                           guardarPantalla();
                            }
                        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public Tabla getSet_vehiculo() {
        return set_vehiculo;
    }

    public void setSet_vehiculo(Tabla set_vehiculo) {
        this.set_vehiculo = set_vehiculo;
    }

    public Tabla getSet_servicio() {
        return set_servicio;
    }

    public void setSet_servicio(Tabla set_servicio) {
        this.set_servicio = set_servicio;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
