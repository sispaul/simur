/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_placa extends Pantalla{
private Tabla tab_gestor = new Tabla();
private Tabla tab_solicitud = new Tabla();
private Tabla tab_detalle = new Tabla();
private Tabla tab_requisito = new Tabla();
private Tabla tab_consulta = new Tabla();

    public pre_solicitud_placa() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 1);
        tab_gestor.getColumna("IDE_GESTOR").setVisible(false);
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setCombo("select IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA from TRANS_COMERCIAL_AUTOMOTORES");
        tab_gestor.getColumna("CEDULA_GESTOR").setNombreVisual("Cedula");
        tab_gestor.getColumna("NOMBRE_GESTOR").setNombreVisual("Nombre Completo");
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setLectura(true);
        tab_gestor.getColumna("CEDULA_GESTOR").setLectura(true);
        tab_gestor.getColumna("NOMBRE_GESTOR").setLectura(true);
        tab_gestor.getGrid().setColumns(2);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.agregarRelacion(tab_solicitud);
        tab_gestor.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_gestor);       

        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("trans_solicitud_placa", "ide_solicitud_placa", 2);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("Descripción de Solicitud");
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. Automotores");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("Fecha");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
//        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_solicitud.getGrid().setColumns(2);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("trans_detalle_solicitud_placa", "ide_detalle_solicitud", 3);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo");
        tab_detalle.getGrid().setColumns(2);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setIdCompleto("tab_tabulador:tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 4);
        tab_requisito.getColumna("IDE_DET_REQUISITO").setCombo("SELECT d.IDE_DET_REQUISITO,r.DECRIPCION_REQUISITO\n" 
                                                                +"FROM TRANS_DETALLE_REQUISITO d,TRANS_TIPO_SERVICIO s,TRANS_TIPO_REQUISITO r,trans_tipo_vehiculo v\n" 
                                                                +"WHERE d.IDE_TIPO_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" 
                                                                +"d.IDE_TIPO_REQUISITO = r.IDE_TIPO_REQUISITO AND\n" 
                                                                +"d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" 
                                                                +"v.ide_tipo_vehiculo = 5 AND s.IDE_TIPO_SERVICIO = 1");
        tab_requisito.getGrid().setColumns(2);
        tab_requisito.setTipoFormulario(true);
        tab_requisito.dibujar();
        PanelTabla tabp3 = new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        

        tab_tabulador.agregarTab("DETALLE", tabp2);
        tab_tabulador.agregarTab("REQUISITOS", tabp3);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tabp, tabp1, tab_tabulador, "20%", "53%", "H");
        agregarComponente(div_division);
        
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

    public Tabla getTab_gestor() {
        return tab_gestor;
    }

    public void setTab_gestor(Tabla tab_gestor) {
        this.tab_gestor = tab_gestor;
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
