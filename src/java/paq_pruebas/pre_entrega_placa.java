/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;


/**
 *
 * @author p-sistemas
 */
public class pre_entrega_placa extends Pantalla{
private Tabla set_detalle = new Tabla();
private Tabla set_entrega = new Tabla();
private Tabla tab_consulta = new Tabla();
private AutoCompletar aut_busca = new AutoCompletar();

    public pre_entrega_placa() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        
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
        
        
        set_detalle.setId("set_detalle");
        set_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 1);
        set_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setFiltro(true);
        set_detalle.getColumna("NOMBRE_PROPIETARIO").setFiltro(true);
        set_detalle.getColumna("ide_entrega_placa").setVisible(false);
        set_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACA");
        set_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        set_detalle.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        set_detalle.setTipoFormulario(true);
        set_detalle.getGrid().setColumns(4);
        set_detalle.dibujar();
        PanelTabla tabp=new PanelTabla();  //Para el menu contextual
        tabp.setPanelTabla(set_detalle);
        set_detalle.setStyle(null);
        tabp.setStyle("width:100%;overflow: auto;");
        
        set_entrega.setId("set_entrega");
        set_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 2);
        set_entrega.setHeader("ENTREGA DE PLACAS");
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        set_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_entrega.getColumna("USU_ENTREGA").setLectura(true);
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        set_entrega.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setNombreVisual("Fecha Entrega");
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setVisible(false);
        set_entrega.getColumna("CEDULA_PERSONA_RETIRA ").setNombreVisual("C.I de Quien Retira");
        set_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setNombreVisual("Nombre de Quien Retira");
        set_entrega.getColumna("USU_ENTREGA").setVisible(false);
        set_entrega.getColumna("DESCRIPCION_PERSONA_RETIRA").setNombreVisual("A Notaciones");
        set_entrega.getColumna("ENTREGA_PLACA ").setNombreVisual("Se Entrega Placa");
        set_entrega.getGrid().setColumns(2);
        set_entrega.setTipoFormulario(true);
        set_entrega.dibujar();
        PanelTabla tabp1=new PanelTabla();  //Para el menu contextual
        tabp1.setPanelTabla(set_entrega);
        set_entrega.setStyle(null);
        tabp1.setStyle("width:100%;overflow: auto;");
        
        Division div = new Division(); 
        div.dividir2(tabp, tabp1, "30%", "h");
        agregarComponente(div);
    }

     public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            set_detalle.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("set_detalle");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        }
    
    @Override
    public void insertar() {
        set_entrega.insertar();
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setValorDefecto(set_detalle.getValor("CEDULA_RUC_PROPIETARIO")); 
    }

    @Override
    public void guardar() {
        set_entrega.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
    }

    public Tabla getSet_detalle() {
        return set_detalle;
    }

    public void setSet_detalle(Tabla set_detalle) {
        this.set_detalle = set_detalle;
    }

    public Tabla getSet_entrega() {
        return set_entrega;
    }

    public void setSet_entrega(Tabla set_entrega) {
        this.set_entrega = set_entrega;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
                     
}