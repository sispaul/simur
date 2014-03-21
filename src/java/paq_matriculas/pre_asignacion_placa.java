/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import javax.swing.JOptionPane;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_asignacion_placa extends Pantalla{
private Tabla tab_solicitud = new Tabla();
private Tabla tab_requisito = new Tabla();
private Tabla tab_aprobacion = new Tabla();
private Tabla tab_consulta = new Tabla();
private Panel pan_opcion = new Panel();
private Efecto efecto = new Efecto();
private Dialogo dia_dialogoe = new Dialogo();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid_de = new Grid();
private Grid gride = new Grid();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();

private AutoCompletar aut_busca = new AutoCompletar();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);

    public pre_asignacion_placa() {
        
                
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA\n" 
                                    +"FROM TRANS_DETALLE_SOLICITUD_PLACA");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
               
        bar_botones.agregarComponente(new Etiqueta("Buscador Propietario:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        /*
         * CREACION DE OBJETOS TABLA
         */
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 1);
        tab_solicitud.setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_PLACA,IDE_TIPO_VEHICULO,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA,IDE_TIPO_SERVICIO FROM TRANS_DETALLE_SOLICITUD_PLACA");
        tab_solicitud.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_solicitud.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_solicitud.getColumna("IDE_TIPO_VEHICULO").setLectura(true);
        tab_solicitud.getColumna("IDE_TIPO_SERVICIO").setLectura(true);
        tab_solicitud.getColumna("APROBADO_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("APROBADO_SOLICITUD").setNombreVisual("APROBADO");
        tab_solicitud.getColumna("IDE_APROBACION_PLACA").setLectura(true);
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
        tab_solicitud.getColumna("IDE_APROBACION_PLACA").setNombreVisual("DOCT. DE APROBACIÓN");
        tab_solicitud.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_solicitud.agregarRelacion(tab_requisito);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.dibujar();
        PanelTabla tbp_s=new PanelTabla(); 
        tbp_s.setPanelTabla(tab_solicitud);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 2);
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setCombo("SELECT IDE_TIPO_REQUISITO,DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO");
        tab_requisito.getColumna("IDE_TIPO_REQUISITO").setLectura(true);
        tab_requisito.getGrid().setColumns(4);
        tab_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
         pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("ASIGNAR PLACA A SOLICITANTE");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("ASIGNAR PLACA");
        bot_bus.setIcon("ui-icon-comment");
        bot_bus.setMetodo("aceptarPlaca");
        pan_opcion.getChildren().add(bot_bus);

	pan_opcion.getChildren().add(efecto);
        tbp_r.getChildren().add(pan_opcion);
        tbp_r.setPanelTabla(tab_requisito);
   
       tab_aprobacion.setId("tab_aprobacion");
       tab_aprobacion.setTabla("trans_aprobacion_placa", "ide_aprobacion_placa", 3);
       tab_aprobacion.getColumna("FECHA_APROBACION").setValorDefecto(utilitario.getFechaActual());
       tab_aprobacion.getColumna("FECHA_APROBACION").setLectura(true);
//       tab_aprobacion.getColumna("USU_APROBACION").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
//       tab_aprobacion.getColumna("USU_APROBACION").setLectura(true);
       tab_aprobacion.getGrid().setColumns(2);
       tab_aprobacion.setTipoFormulario(true);
       tab_aprobacion.dibujar();
        PanelTabla tbp_a=new PanelTabla(); 
        tbp_a.setPanelTabla(tab_aprobacion);
//        tbp_a.setInView(false);
        
        Division div_division = new Division();
        div_division.setId("div_division");
//        div_division.dividir2(tbp_s, tbp_r,"30%", "H");
        div_division.dividir3(tbp_s,tbp_r,tbp_a,"30%","5%", "H");
        agregarComponente(div_division);
        
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR ASIGNACIÓN"); //titulo
        dia_dialogoe.setWidth("16%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("8%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar(); 
        
    }
    
     public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_solicitud.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_solicitud");
            utilitario.addUpdate("tab_requisito");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        }
   
     public void aceptarPlaca(){
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
         tab_aprobacion.insertar();
        ser_Placa.asigancionPlaca(tab_consulta.getValor("NICK_USUA"));
        utilitario.addUpdate("tab_aprobacion");
         System.out.println(tab_solicitud.getValor("IDE_DETALLE_SOLICITUD"));
         System.err.println(tab_solicitud.getValor("IDE_tipo_vehiculo"));
         System.out.println(tab_solicitud.getValor("IDE_tipo_servicio"));
         System.err.println(tab_aprobacion.getValor("IDE_APROBACION_PLACA"));
        System.out.println("Asignacion realizada");
        llamada();
     }
     
     public void llamada(){
         System.out.println(tab_solicitud.getValor("IDE_DETALLE_SOLICITUD"));
         System.err.println(tab_solicitud.getValor("IDE_tipo_vehiculo"));
         System.out.println(tab_solicitud.getValor("IDE_tipo_servicio"));
         System.err.println(tab_aprobacion.getValor("IDE_APROBACION_PLACA"));
        ser_Placa.seleccionarP(Integer.parseInt(tab_solicitud.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_solicitud.getValor("IDE_tipo_vehiculo")), 
        Integer.parseInt(tab_solicitud.getValor("IDE_tipo_servicio")), Integer.parseInt(tab_aprobacion.getValor("IDE_APROBACION_PLACA")));
        utilitario.addUpdate("tab_solicitud");
        System.out.println("Actualizacion realizada");
        asignacion();
     }
     
     public void asignacion(){
         ser_Placa.estadoPlaca(Integer.parseInt(tab_solicitud.getValor("IDE_PLACA")));
        utilitario.agregarMensaje("ASIGNACIÓN REALIZADA", "");
        dia_dialogoe.cerrar();
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
    
   @Override
    public void actualizar() {
    }

    public Tabla getTab_requisito() {
        return tab_requisito;
    }

    public void setTab_requisito(Tabla tab_requisito) {
        this.tab_requisito = tab_requisito;
    }

    public Tabla getTab_aprobacion() {
        return tab_aprobacion;
    }

    public void setTab_aprobacion(Tabla tab_aprobacion) {
        this.tab_aprobacion = tab_aprobacion;
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
