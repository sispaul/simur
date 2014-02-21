/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
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
private Tabla tab_tipo = new Tabla();
private SeleccionTabla set_tabla = new SeleccionTabla();


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
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla pat_cabecera = new PanelTabla();
        pat_cabecera.setPanelTabla(tab_cabecera);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "ide_detalle_solicitud", 2);
        tab_detalle.setHeader("Detalle de Solicitud");
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_detalle.getColumna("IDE_PLACA").setNombreVisual("Placa");
        tab_detalle.getColumna("IDE_PLACA").setLectura(true);
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("Tipo Servicio");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO from TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setNombreVisual("Tipo Vehiculo");
         List listar = new ArrayList();
        Object fila11[] = {
            "4", "MOTO"
        };
        Object fila22[] = {
            "5", "VEHICULO"
        };
        listar.add(fila11);;
        listar.add(fila22);;
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setRadio(listar, "4");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("CI/RUC");
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("Nombre Propietario");
        tab_detalle.getColumna("NUMERO_FACTURA ").setNombreVisual("Nro. Vehiculo");     
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla pat_detalle = new PanelTabla();
        pat_detalle.setPanelTabla(tab_detalle);
        
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("trans_detalle_requisitos_solicitud", "ide_detalle_requisitos_solicitud", 3);
        tab_requisito.setHeader("Requisitos");
//        tab_requisito.getColumna("ide_det_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO\n" 
//                                                                +"FROM TRANS_TIPO_SERVICIO s ,TRANS_TIPO_REQUISITO r,trans_tipo_vehiculo t,TRANS_DETALLE_REQUISITO d\n" 
//                                                                +"WHERE d.IDE_TIPO_VEHICULO = t.ide_tipo_vehiculo AND d.IDE_TIPO_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO \n" 
//                                                                +"AND d.IDE_TIPO_REQUISITO = r.IDE_TIPO_REQUISITO \n" 
//                                                                +"AND t.ide_tipo_vehiculo = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")+"")+"'\n" 
//                                                                +"AND s.ide_tipo_SERVICIO = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")+"")+"'");
//        
        tab_requisito.dibujar();
        PanelTabla pat_requisito = new PanelTabla();
        pat_requisito.setPanelTabla(tab_requisito);
        
        Division div = new Division();
        div.dividir3(pat_cabecera, pat_detalle, pat_requisito, "22%", "47%", "h");
        agregarComponente(div);

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
