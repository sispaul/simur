/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_entrega_placa extends Pantalla{
Integer consulta;
private Tabla set_detalle = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_placa = new Tabla();
private Tabla set_entrega = new Tabla();
private Dialogo dia_dialogoe = new Dialogo();
private Grid grid_de = new Grid();
private Grid grid = new Grid();
private Texto cedula = new Texto();
private Etiqueta eti_etiqueta = new Etiqueta();
    ///REPORTES
private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Map p_parametros = new HashMap();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    public pre_entrega_placa() {
                        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();      

        //Configurando el dialogo
        dia_dialogoe.setId("dia_dialogo");
        dia_dialogoe.setTitle("PLACAS - ENTREGAS"); //titulo
        dia_dialogoe.setWidth("55%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(2);
        agregarComponente(dia_dialogoe);
        
        set_entrega.setId("set_entrega");
        set_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        set_entrega.setHeader("ENTREGA DE PLACAS");
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        set_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_entrega.getColumna("USU_ENTREGA").setLectura(true);
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        set_entrega.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setNombreVisual("Fecha Entrega");
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC Propietario");
        set_entrega.getColumna("CEDULA_PERSONA_RETIRA").setNombreVisual("C.I de Quien Retira");
        set_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setNombreVisual("Nombre de Quien Retira");
        set_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setMayusculas(true);
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
        bot_placa.setValue("BUSCAR PROPIETARIO Y PLACA");
        bot_placa.setIcon("ui-icon-document");
        bot_placa.setMetodo("aceptoDialogoe");
        eti_etiqueta.setStyle("font-size:12px;color:black;text-align:center;");
        eti_etiqueta.setValue("Buscar Propietario Por Cedula:");
        pat_panel.getChildren().add(eti_etiqueta);
        pat_panel.getChildren().add(cedula);
        pat_panel.getChildren().add(bot_placa);

       
        tab_placa.setId("tab_placa");
        tab_placa.setSql("SELECT IDE_PLACA,PLACA,IDE_TIPO_ESTADO FROM TRANS_PLACA");
        tab_placa.setCampoPrimaria("IDE_PLACA");
        tab_placa.setLectura(true);
        tab_placa.dibujar(); 
        
                 /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
    }

        
    public void aceptoDialogoe() {
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(grid);
        set_detalle.setId("set_detalle");
        set_detalle.setSql("SELECT d.IDE_DETALLE_SOLICITUD,d.IDE_PLACA,d.IDE_APROBACION_PLACA,d.IDE_TIPO_VEHICULO,d.IDE_SOLICITUD_PLACA,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,APROBADO_SOLICITUD,p.IDE_TIPO_ESTADO\n" 
                            +"FROM TRANS_DETALLE_SOLICITUD_PLACA d ,TRANS_PLACA p\n" 
                            +"WHERE d.IDE_PLACA = p.IDE_PLACA AND d.CEDULA_RUC_PROPIETARIO LIKE '"+cedula.getValue()+"'");
        set_detalle.setTipoSeleccion(false); 
        grid_de.getChildren().add(set_detalle);
        dia_dialogoe.setDialogo(grid_de);
        set_detalle.dibujar();
        dia_dialogoe.dibujar();
    }
    
        public void aceptoValores() {
        if (set_detalle.getValorSeleccionado()!= null) {
              set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setValorDefecto(set_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
              consulta = Integer.parseInt(set_detalle.getValor("IDE_DETALLE_SOLICITUD"));
              utilitario.addUpdate("set_entrega");
              set_entrega.insertar();
              dia_dialogoe.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }

       
    
 @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ENTREGA DE PLACA":
               aceptoDialogo();
               break;      
                
        }
    }
     
    public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "ENTREGA DE PLACA":
//                    if ((set_entrega.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                      p_parametros = new HashMap();
                      p_parametros.put("cedula", set_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"");
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
//                    } else {
//                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
//                    }
               break;                   
        }

    }

    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }    
        
    @Override
    public void insertar() {
        set_entrega.insertar();  
    }

    @Override
    public void guardar() {
    if (utilitario.validarCedula(set_entrega.getValor("CEDULA_PERSONA_RETIRA"))) {
     if (set_entrega.guardar()) {
            if (guardarPantalla().isEmpty()) {
//                ser_Placa.actualizarDS(Integer.parseInt(set_entrega.getValor("ide_entrega_placa")),consulta,utilitario.StringToByte(set_entrega.getValor("ENTREGA_PLACA")));
                set_entrega.actualizar();
                utilitario.addUpdate("set_solicitud");
                ser_Placa.actualizarDE(consulta, set_detalle.getValor("CEDULA_RUC_PROPIETARIO"), Integer.parseInt(set_detalle.getValor("ide_placa")));
            }
        }else {
            utilitario.agregarMensajeInfo("No Puede Guardar Placa Entregada", "");
        }
     } else {
             utilitario.agregarMensajeError("El Número de CEDULA no es válido", "");
             return;
            }    
  }

    @Override
    public void eliminar() {
        set_entrega.eliminar();
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }
    
}
