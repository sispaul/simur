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
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_confirmacion_requisitos extends Pantalla{
private Tabla tab_detalle = new Tabla();
private Tabla set_requisito = new Tabla();
private AutoCompletar aut_busca = new AutoCompletar();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    public pre_confirmacion_requisitos() {
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_DETALLE_SOLICITUD,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_FACTURA\n" +
                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA");
        aut_busca.setMetodoChange("buscarGestor");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Gestor:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 1);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.agregarRelacion(set_requisito);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle);
        
        set_requisito.setId("set_requisito");
        set_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 2);
        set_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        set_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(set_requisito);
       

        Division div_division = new Division();
        div_division.dividir2(tabp2, tabp3,"55%", "H");
        div_division.getDivision2().setHeader("CONFIRMACIÓN DE REQUISITOS");
        agregarComponente(div_division);
    }
     public void buscarGestor(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_detalle.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_detalle");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        }   
        
    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
         set_requisito.guardar();
         System.err.println(Integer.parseInt(set_requisito.getValor("IDE_DETALLE_SOLICITUD")));
         ser_Placa.actualizarEstado(Integer.parseInt(set_requisito.getValor("IDE_DETALLE_SOLICITUD")), utilitario.StringToByte(set_requisito.getValor("CONFIRMAR_REQUISITO")));
         System.err.println(utilitario.StringToByte(set_requisito.getValor("CONFIRMAR_REQUISITO")));
    }

    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public Tabla getSet_requisito() {
        return set_requisito;
    }

    public void setSet_requisito(Tabla set_requisito) {
        this.set_requisito = set_requisito;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
