/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_entrega_placa extends Pantalla{

private Tabla set_detalle = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla set_entrega = new Tabla();

private Dialogo dia_dialogo = new Dialogo();

private Grid grid = new Grid();
private Grid grid_de = new Grid();

    public pre_entrega_placa() {
                        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();      

        //Configurando el dialogo
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogo.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(2);
        grid_de.getChildren().add(new Etiqueta("SELECCIONE PROPIETARIO"));
        agregarComponente(dia_dialogo);
        
        set_entrega.setId("set_entrega");
        set_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        set_entrega.setHeader("ENTREGA DE PLACAS");
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        set_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_entrega.getColumna("USU_ENTREGA").setLectura(true);
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
                List listaa = new ArrayList();
        Object filaa[] = {
            "0", "NO"
        };
        Object filab[] = {
            "1", "SI"
        };
        listaa.add(filaa);;
        listaa.add(filab);;
        set_entrega.getColumna("ENTREGA_PLACA").setRadio(listaa, "0"); 
        set_entrega.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setNombreVisual("Fecha Entrega");
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC Propietario");
        set_entrega.getColumna("CEDULA_PERSONA_RETIRA ").setNombreVisual("C.I de Quien Retira");
        set_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setNombreVisual("Nombre de Quien Retira");
        set_entrega.getColumna("USU_ENTREGA").setVisible(false);
        set_entrega.getColumna("DESCRIPCION_PERSONA_RETIRA").setNombreVisual("A Notaciones");
        set_entrega.getColumna("ENTREGA_PLACA ").setNombreVisual("Se Entrega Placa");
        set_entrega.getGrid().setColumns(2);
        set_entrega.setTipoFormulario(true);
        set_entrega.dibujar();
        PanelTabla pat_panel=new PanelTabla();  //Para el menu contextual
        pat_panel.setPanelTabla(set_entrega);
        set_entrega.setStyle(null);
        pat_panel.setStyle("width:100%;overflow: auto;");
        agregarComponente(pat_panel);

        
        Boton bot_placa = new Boton();
        bot_placa.setValue("MOSTRAR PROPIETARIO Y PLACA");
        bot_placa.setIcon("ui-icon-document");
        bot_placa.setMetodo("aceptoDialogo()");
        pat_panel.getChildren().add(bot_placa);
        
        set_detalle.setId("set_detalle");
        set_detalle.setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_PLACA,IDE_APROBACION_PLACA,IDE_TIPO_VEHICULO,IDE_SOLICITUD_PLACA,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,APROBADO_SOLICITUD FROM TRANS_DETALLE_SOLICITUD_PLACA");
        set_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setFiltro(true);
        set_detalle.getColumna("NOMBRE_PROPIETARIO").setFiltro(true);
        List lista = new ArrayList();
        Object fila1[] = {
            "0", "NO"
        };
        Object fila2[] = {
            "1", "SI"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        set_detalle.getColumna("APROBADO_SOLICITUD").setRadio(lista, "0"); 
//        set_detalle.getColumna("IDE_PLACA").setCombo("");
//        set_detalle.getColumna("IDE_APROBACION_PLACA").setCombo("");
//        set_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("");
        set_detalle.setTipoSeleccion(false);
        set_detalle.setRows(10);
        set_detalle.dibujar();

        
    }
    
    public void aceptoDialogo() {
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_de.getChildren().add(set_detalle);
        dia_dialogo.setDialogo(grid_de);
        set_detalle.dibujar();
        dia_dialogo.dibujar();
    }
    
        public void aceptoValores() {
        if (set_detalle.getValorSeleccionado()!= null) {
              set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setValorDefecto(set_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
              utilitario.addUpdate("set_entrega");
              dia_dialogo.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
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
    
}
