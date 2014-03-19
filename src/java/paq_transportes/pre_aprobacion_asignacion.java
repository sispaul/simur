/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_aprobacion_asignacion extends Pantalla{
private Tabla set_solicitud = new Tabla();
private Tabla set_requisito = new Tabla();
private Tabla set_aprobacion = new Tabla();
private Tabla tab_consulta = new Tabla();
private AutoCompletar aut_busca = new AutoCompletar();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);

    public pre_aprobacion_asignacion() {
        
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

                
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar(); 
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 1);
        set_solicitud.setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_PLACA,IDE_TIPO_VEHICULO,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA,IDE_TIPO_SERVICIO FROM TRANS_DETALLE_SOLICITUD_PLACA");
        set_solicitud.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_solicitud.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        set_solicitud.getColumna("IDE_TIPO_VEHICULO").setLectura(true);
        set_solicitud.getColumna("IDE_TIPO_SERVICIO").setLectura(true);
        set_solicitud.getColumna("APROBADO_SOLICITUD").setLectura(true);
        set_solicitud.getColumna("APROBADO_SOLICITUD").setNombreVisual("Aprobado");
        set_solicitud.getColumna("IDE_APROBACION_PLACA").setLectura(true);
        set_solicitud.getColumna("IDE_PLACA").setLectura(true);
        set_solicitud.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_solicitud.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        set_solicitud.getColumna("ENTREGADA_PLACA").setVisible(false);
        set_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        set_solicitud.getColumna("IDE_PLACA").setNombreVisual("PLACA");
        set_solicitud.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");       
        set_solicitud.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("SERVICIO");
        set_solicitud.getColumna("IDE_TIPO_VEHICULO").setNombreVisual("VEHICULO");
        set_solicitud.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("PROPIETARIO");
        set_solicitud.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("IDENTIFICACIÓN");
        set_solicitud.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        set_solicitud.agregarRelacion(set_requisito);
        set_solicitud.setTipoFormulario(true);
        set_solicitud.getGrid().setColumns(4);
        set_solicitud.dibujar();
        PanelTabla tbp_s=new PanelTabla(); 
        tbp_s.setPanelTabla(set_solicitud);
        set_solicitud.setStyle(null);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        set_requisito.setId("set_requisito");
        set_requisito.setIdCompleto("tab_tabulador:set_requisito");
        set_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 2);
        set_requisito.getColumna("IDE_TIPO_REQUISITO").setCombo("SELECT IDE_TIPO_REQUISITO,DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO");
        set_requisito.getColumna("IDE_TIPO_REQUISITO").setLectura(true);
        set_requisito.getGrid().setColumns(4);
        set_requisito.dibujar();
        PanelTabla tbp_r=new PanelTabla(); 
        tbp_r.setPanelTabla(set_requisito);
   
        
        set_aprobacion.setId("set_aprobacion");
        set_aprobacion.setIdCompleto("tab_tabulador:set_aprobacion");
        set_aprobacion.setTabla("trans_aprobacion_placa", "ide_aprobacion_placa", 3);
        set_aprobacion.getColumna("IDE_APROBACION_PLACA").setNombreVisual("ID");
        set_aprobacion.getColumna("FECHA_APROBACION").setNombreVisual("Fecha de Aprobacion");
        set_aprobacion.getColumna("USU_APROBACION").setNombreVisual("Quien Aprueba");
        set_aprobacion.getColumna("COMENTARIO_APROBACION").setNombreVisual("Comentarios");
        set_aprobacion.getColumna("FECHA_APROBACION").setValorDefecto(utilitario.getFechaActual());
        set_aprobacion.getColumna("FECHA_APROBACION").setLectura(true);
        set_aprobacion.getColumna("USU_APROBACION").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_aprobacion.getColumna("USU_APROBACION").setLectura(true);
        set_aprobacion.getGrid().setColumns(2);
        set_aprobacion.setTipoFormulario(true);
        set_aprobacion.dibujar();
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(set_aprobacion);
        
        tab_tabulador.agregarTab("REQUISITOS", tbp_r);
        tab_tabulador.agregarTab("APROBAR REQUISITO PLACA", pat_panel5);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(tbp_s, tab_tabulador, "30%", "H");
        agregarComponente(div_division);
        
}
     public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            set_solicitud.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("set_solicitud");
            utilitario.addUpdate("set_requisito");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        }
    
    @Override
    public void insertar() {
    set_aprobacion.insertar();
    }

    @Override
    public void guardar() {
        if (set_aprobacion.guardar()) {
            set_aprobacion.actualizar();
            utilitario.addUpdate("set_aprobacion");
            if (guardarPantalla().isEmpty()) {
            ser_Placa.seleccionarP(Integer.parseInt(set_solicitud.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(set_solicitud.getValor("IDE_tipo_vehiculo")), Integer.parseInt(set_solicitud.getValor("IDE_tipo_servicio")), Byte.parseByte(set_aprobacion.getValor("APROBADO")), Integer.parseInt(set_aprobacion.getValor("IDE_APROBACION_PLACA")));
            set_solicitud.actualizar();
            utilitario.addUpdate("set_solicitud");
            }
        }
    }

    @Override
    public void eliminar() {
        set_aprobacion.eliminar();
    }

    
    public Tabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(Tabla set_solicitud) {
        this.set_solicitud = set_solicitud;
    }

    public Tabla getSet_requisito() {
        return set_requisito;
    }

    public void setSet_requisito(Tabla set_requisito) {
        this.set_requisito = set_requisito;
    }

    public Tabla getSet_aprobacion() {
        return set_aprobacion;
    }

    public void setSet_aprobacion(Tabla set_aprobacion) {
        this.set_aprobacion = set_aprobacion;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
