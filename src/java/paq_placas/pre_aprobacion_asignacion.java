/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_aprobacion_asignacion extends  Pantalla{
private Tabla tab_detalle = new Tabla();
private Tabla tab_aprobacion = new Tabla();
private Tabla tab_requisito = new Tabla();
private AutoCompletar aut_busca = new AutoCompletar();

    public pre_aprobacion_asignacion() {
        
        tab_aprobacion.setId("tab_aprobacion");
        tab_aprobacion.setTabla("trans_aprobacion_placa", "ide_aprobacion_placa", 1);
//        tab_aprobacion.agregarRelacion(tab_detalle);
        tab_aprobacion.setTipoFormulario(true);
        tab_aprobacion.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_aprobacion); 
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("trans_detalle_solicitud_placa", "ide_detalle_solicitud", 2);
        tab_detalle.getColumna("IDE_PLACA").setNombreVisual("Nro. Placa");
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setNombreVisual("Quien Aprueba");
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setNombreVisual("Aprobar");
        tab_detalle.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_detalle.getGrid().setColumns(2);
//        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_detalle); 
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setIdCompleto("tab_tabulador:tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 4);
        tab_requisito.getColumna("IDE_DET_REQUISITO").setCombo("SELECT d.IDE_DET_REQUISITO,r.DECRIPCION_REQUISITO\n" 
                                                                +"FROM TRANS_DETALLE_REQUISITO d,TRANS_TIPO_SERVICIO s,TRANS_TIPO_REQUISITO r,trans_tipo_vehiculo v\n" 
                                                                +"WHERE d.IDE_TIPO_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" 
                                                                +"d.IDE_TIPO_REQUISITO = r.IDE_TIPO_REQUISITO AND\n" 
                                                                +"d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" 
                                                                +"v.ide_tipo_vehiculo = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")+"")+"' AND s.IDE_TIPO_SERVICIO = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")+"")+"'");
        tab_requisito.getGrid().setColumns(2);
//        tab_requisito.setTipoFormulario(true);
        tab_requisito.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_requisito);
        
        tab_tabulador.agregarTab("DETALLE", tabp1);
        tab_tabulador.agregarTab("REQUISITOS", tabp2);
        
        Division div = new Division();
        div.dividir2(tabp, tab_tabulador, "40%", "h");
        agregarComponente(div);

    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
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

    public Tabla getTab_aprobacion() {
        return tab_aprobacion;
    }

    public void setTab_aprobacion(Tabla tab_aprobacion) {
        this.tab_aprobacion = tab_aprobacion;
    }
    
}
