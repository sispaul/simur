/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;


import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;


/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_in extends Pantalla{
    private Tabla tab_requisito = new Tabla();
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    public pre_solicitud_in() {
        tab_requisito.setId("tab_requisito");
        tab_requisito.setIdCompleto("tab_tabulador:tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 1);
        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
        tab_requisito.getColumna("ide_tipo_requisito").setLectura(true);
        tab_requisito.getColumna("CONFIRMAR_REQUISITO").setNombreVisual("CONFIRMAR");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        agregarComponente(tabp3);
        

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tab_requisito.getTotalFilas(); i++) {
            tab_requisito.getValor(i, "CONFIRMAR_REQUISITO");
            tab_requisito.getValor(i, "IDE_TIPO_REQUISITO");
            tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD");
//            System.err.println(tab_requisito.getValor(i, "CONFIRMAR_REQUISITO"));
//            if(tab_requisito.getValor(i, "CONFIRMAR_REQUISITO").equals(true)){
                ser_Placa.actulizarRequisito(utilitario.StringToByte(tab_requisito.getValor(i,"CONFIRMAR_REQUISITO")),Integer.parseInt(tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD"))
                        ,Integer.parseInt(tab_requisito.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_requisito.getValor(i, "IDE_TIPO_REQUISITO")));
//                System.err.println(tab_requisito.getValor(i, "CONFIRMAR_REQUISITO"));
                utilitario.addUpdate("tab_requisito");
//            }
        }
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_requisito() {
        return tab_requisito;
    }

    public void setTab_requisito(Tabla tab_requisito) {
        this.tab_requisito = tab_requisito;
    }
    
}
