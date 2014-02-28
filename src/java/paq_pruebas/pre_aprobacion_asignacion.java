/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.List;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_aprobacion_asignacion extends Pantalla{
private Tabla set_solicitud = new Tabla();
private Tabla set_requisito = new Tabla();
private Tabla set_aprobacion = new Tabla();
private Tabla tab_consulta = new Tabla();
private Grid grid = new Grid();
private Grid grid_de = new Grid();
private Dialogo dia_dialogo = new Dialogo();

    public pre_aprobacion_asignacion() {
        
        
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
        set_solicitud.getColumna("IDE_APROBACION_PLACA").setLectura(true);
        set_solicitud.getColumna("APROBADO_SOLICITUD").setLectura(true);
        set_solicitud.getColumna("IDE_PLACA").setLectura(true);
        set_solicitud.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_solicitud.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        set_solicitud.getColumna("ENTREGADA_PLACA").setVisible(false);
        set_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        set_solicitud.getColumna("IDE_PLACA").setNombreVisual("PLACA");
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
        tbp_s.setStyle("width:100%;overflow: auto;");

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        set_requisito.setId("set_requisito");
        set_requisito.setIdCompleto("tab_tabulador:set_requisito");
        set_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 2);
//        set_requisito.setSql("SELECT r.IDE_DETALLE_REQUISITOS_SOLICITUD,t.DECRIPCION_REQUISITO,r.CONFIRMAR_REQUISITO FROM TRANS_DETALLE_SOLICITUD_PLACA d,TRANS_DETALLE_REQUISITOS_SOLICITUD r,TRANS_TIPO_REQUISITO t\n" 
//                             +"WHERE r.IDE_DETALLE_SOLICITUD = d.IDE_DETALLE_SOLICITUD AND r.IDE_TIPO_REQUISITO = t.IDE_TIPO_REQUISITO");
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
         List lista = new ArrayList();
        Object fila1[] = {
            "0", "NO"
        };
        Object fila2[] = {
            "1", "SI"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        set_aprobacion.getColumna("APROBADO").setRadio(lista, "0"); 
        set_aprobacion.getColumna("USU_APROBACION").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_aprobacion.getColumna("USU_APROBACION").setLectura(true);
        set_aprobacion.getGrid().setColumns(2);
        set_aprobacion.setTipoFormulario(true);
        set_aprobacion.dibujar();
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(set_aprobacion);
        
        tab_tabulador.agregarTab("REPRESENTANTE", tbp_r);
        tab_tabulador.agregarTab("APROBAR REQUISITO PLACA", pat_panel5);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(tbp_s, tab_tabulador, "30%", "H");
        agregarComponente(div_division);
        
}
 
    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (set_aprobacion.guardar()) {
//            if (tab_tabla2.guardar()) {
//                if (tab_tabla3.guardar()) {
//                    if (tab_tabla4.isFocus() && tab_tabla4.isFilaInsertada()) {
//                        tab_tabla4.setValor("FECHA_HORA_ACCION_CMDEA", utilitario.getFechaHoraActual());
//                    }
//                    if (tab_tabla4.guardar()) {
//                        if (tab_tabla5.guardar()) {
//                            if (guardarPantalla().isEmpty()) {
//                                aut_busca.actualizar();
//                                aut_busca.setSize(70);
//                                utilitario.addUpdate("aut_busca");
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    @Override
    public void eliminar() {
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
    
}
