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
    
private Tabla tab_cabecera = new Tabla();
private Tabla tab_detalle = new Tabla();
private Tabla tab_requisito = new Tabla();
private Tabla tab_consulta = new Tabla();

    public pre_solicitud_placa() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("trans_solicitud_placa", "IDE_SOLICITUD_PLACA", 1);
        tab_cabecera.setHeader("Solicitud Pedido Placa");
        tab_cabecera.getColumna("IDE_SOLICITUD_PLACA").setNombreVisual("ID");
        tab_cabecera.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("Descripción Solicitud");
        tab_cabecera.getColumna("IDE_GESTOR").setNombreVisual("Quien Gestiona");
        tab_cabecera.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. Automotores");
        tab_cabecera.getColumna("FECHA_SOLICITUD").setNombreVisual("Fecha");
        tab_cabecera.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_cabecera.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_cabecera.getColumna("usu_solicitud").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_cabecera.getColumna("usu_solicitud").setVisible(false);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla pat_cabecera = new PanelTabla();
        pat_cabecera.setPanelTabla(tab_cabecera);
        
        Tabulador tab_tabular = new Tabulador();
        tab_tabular.setId("tab_tabular");
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "ide_detalle_solicitud", 2);
        tab_detalle.setHeader("Detalle de Solicitud");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla pat_detalle = new PanelTabla();
        pat_detalle.setPanelTabla(tab_detalle);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("trans_detalle_requisitos_solicitud", "ide_detalle_requisitos_solicitud", 3);
        tab_requisito.setHeader("Requisitos Pedido de Placa");
        tab_requisito.setTipoFormulario(true);
        tab_requisito.dibujar();
        PanelTabla pat_requisito = new PanelTabla();
        pat_requisito.setPanelTabla(tab_requisito);
        
        
        tab_tabular.agregarTab("Detalle", pat_detalle);
        tab_tabular.agregarTab("Requisitos", pat_requisito);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_cabecera, tab_tabular, "40%", "H");
        agregarComponente(div_division);
        
    }

    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
   }

    @Override
    public void guardar() {
         if (tab_cabecera.guardar()) {
            if (tab_detalle.guardar()) {
                if (tab_requisito.guardar()) {
                guardarPantalla();
                }
            }
         }
    }

    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
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
