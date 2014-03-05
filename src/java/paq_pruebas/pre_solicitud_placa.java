/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;


import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
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
public class pre_solicitud_placa extends Pantalla{
private Tabla set_requisito = new Tabla();
private Tabla tab_gestor = new Tabla();
private Tabla tab_solicitud = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_detalle = new Tabla();
private Combo cmb_vehiculo = new Combo();
private Combo cmb_servicio = new Combo();
private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();
private AutoCompletar aut_busca = new AutoCompletar();

    public pre_solicitud_placa() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT g.IDE_GESTOR,g.CEDULA_GESTOR,g.NOMBRE_GESTOR,a.NOMBRE_EMPRESA\n" 
                                    +"FROM TRANS_GESTOR g,TRANS_COMERCIAL_AUTOMOTORES a\n" 
                                    +"WHERE g.IDE_COMERCIAL_AUTOMOTORES = a.IDE_COMERCIAL_AUTOMOTORES");
        aut_busca.setMetodoChange("buscarGestor");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Gestor:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);

        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("TRANS_GESTOR", "IDE_GESTOR", 1);
        tab_gestor.getColumna("IDE_GESTOR").setVisible(false);
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setCombo("select IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA from TRANS_COMERCIAL_AUTOMOTORES");
        tab_gestor.getColumna("CEDULA_GESTOR").setNombreVisual("Cedula");
        tab_gestor.getColumna("NOMBRE_GESTOR").setNombreVisual("Nombre Completo");
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setLectura(true);
        tab_gestor.getColumna("CEDULA_GESTOR").setLectura(true);
        tab_gestor.getColumna("NOMBRE_GESTOR").setLectura(true);
        tab_gestor.setHeader("INGRESO DE SOLICITUD PARA PLACA");
        tab_gestor.agregarRelacion(tab_solicitud);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_gestor); 
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 2);
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
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 3);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_detalle.agregarRelacion(set_requisito);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle);     
        
        set_requisito.setId("set_requisito");
        set_requisito.setIdCompleto("tab_tabulador:set_requisito");
        set_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 4);
        List lista = new ArrayList();
        Object fila1[] = {
            "0", "NO"
        };
        Object fila2[] = {
            "1", "SI"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        set_requisito.getColumna("CONFIRMAR_REQUISITO").setRadio(lista, "0");   
        set_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n" 
                                                                +"WHERE v.ide_tipo_vehiculo = "+tab_detalle.getValor("IDE_TIPO_VEHICULO")+" AND s.IDE_TIPO_SERVICIO ="+tab_detalle.getValor("IDE_TIPO_SERVICIO")+"");
        
        set_requisito.getColumna("ide_tipo_requisito").actualizarCombo();
        set_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(set_requisito);
        
        tab_tabulador.agregarTab("DETALLE", tabp2);
        tab_tabulador.agregarTab("REQUISITOS", tabp3);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tabp, tabp1,tab_tabulador, "23%","55%", "H");
        div_division.getDivision2().setHeader("SOLICITUD DE INGRESO DE PLACA");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
         utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
       if (tab_solicitud.guardar()) {
            if (tab_detalle.guardar()) {
                if (guardarPantalla().isEmpty()) {
                    utilitario.addUpdate("set_requisito");
                    set_requisito.guardar();
                    }
                }
            }
        }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getSet_requisito() {
        return set_requisito;
    }

    public void setSet_requisito(Tabla set_requisito) {
        this.set_requisito = set_requisito;
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
