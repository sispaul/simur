/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_placa extends Pantalla{
private Tabla tab_gestor = new Tabla();
private Tabla tab_solicitud = new Tabla();
private Tabla tab_detalle = new Tabla();
private Tabla tab_requisito = new Tabla();
private Tabla tab_consulta = new Tabla();
private Dialogo dia_dialogo = new Dialogo();
private Grid grid = new Grid();
private Grid grid_de = new Grid();
//autocompletar datos
private AutoCompletar aut_busca = new AutoCompletar();

    public pre_solicitud_placa() {

        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogo.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("30%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
//        dia_dialogo.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(4);
        grid_de.getChildren().add(new Etiqueta("SELECCIONE VEHICULO"));
        agregarComponente(dia_dialogo);
        
        Boton bot = new Boton();
        bot.setValue("ASIGNAR ESTADOS");
        bot.setMetodo("aceptoDialogo");
        bar_botones.agregarBoton(bot);
              
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
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 1);
        tab_gestor.getColumna("IDE_GESTOR").setVisible(false);
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setCombo("select IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA from TRANS_COMERCIAL_AUTOMOTORES");
        tab_gestor.getColumna("CEDULA_GESTOR").setNombreVisual("Cedula");
        tab_gestor.getColumna("NOMBRE_GESTOR").setNombreVisual("Nombre Completo");
        tab_gestor.getColumna("IDE_COMERCIAL_AUTOMOTORES").setLectura(true);
        tab_gestor.getColumna("CEDULA_GESTOR").setLectura(true);
        tab_gestor.getColumna("NOMBRE_GESTOR").setLectura(true);
        tab_gestor.getGrid().setColumns(2);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.agregarRelacion(tab_solicitud);
        tab_gestor.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_gestor);       

        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("trans_solicitud_placa", "ide_solicitud_placa", 2);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("Descripción de Solicitud");
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. Automotores");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("Fecha");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_solicitud.getGrid().setColumns(2);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("trans_detalle_solicitud_placa", "ide_detalle_solicitud", 3);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("select ide_tipo_servicio,descripcion_servicio from trans_tipo_servicio");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        tab_detalle.getGrid().setColumns(2);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setSql("SELECT d.IDE_DET_REQUISITO,r.DECRIPCION_REQUISITO,t.CONFIRMAR_REQUISITO\n" 
                            +"FROM TRANS_DETALLE_REQUISITOS_SOLICITUD t,TRANS_DETALLE_REQUISITO d,TRANS_TIPO_SERVICIO s,TRANS_TIPO_REQUISITO r,trans_tipo_vehiculo v \n" 
                            +"WHERE d.IDE_TIPO_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND d.IDE_TIPO_REQUISITO = r.IDE_TIPO_REQUISITO \n" 
                            +"AND d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo= '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")+"")+"' AND s.IDE_TIPO_SERVICIO = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")+"")+"'");
        tab_requisito.setRows(5);
        tab_requisito.setTipoSeleccion(false);
        tab_requisito.dibujar();
        
//        tab_requisito.setId("tab_requisito");
//        tab_requisito.setIdCompleto("tab_tabulador:tab_requisito");
//        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 4);
//        tab_requisito.getColumna("IDE_DET_REQUISITO").setCombo("SELECT d.IDE_DET_REQUISITO,r.DECRIPCION_REQUISITO\n" 
//                                                                +"FROM TRANS_DETALLE_REQUISITO d,TRANS_TIPO_SERVICIO s,TRANS_TIPO_REQUISITO r,trans_tipo_vehiculo v\n" 
//                                                                +"WHERE d.IDE_TIPO_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" 
//                                                                +"d.IDE_TIPO_REQUISITO = r.IDE_TIPO_REQUISITO AND\n" 
//                                                                +"d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" 
//                                                                +"v.ide_tipo_vehiculo = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")+"")+"' AND s.IDE_TIPO_SERVICIO = '"+Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")+"")+"'");
//        tab_requisito.getGrid().setColumns(2);
////        tab_requisito.setTipoFormulario(true);
//        tab_requisito.dibujar();
//        PanelTabla tabp3 = new PanelTabla();
//        tabp3.setPanelTabla(tab_requisito);
        

        tab_tabulador.agregarTab("DETALLE", tabp2);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(tabp, tabp2, "53%", "H");
        agregarComponente(div_division);
        
    }
    
       public void buscarGestor(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_gestor.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_gestor");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
        
 public void aceptoDialogo() {
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_de.getChildren().add(tab_requisito);
        dia_dialogo.dibujar();
    }
        
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
      if (tab_solicitud.guardar()) {
          if (tab_detalle.guardar()) {
              if (tab_requisito.guardar()) {
                guardarPantalla();
              }
            }
        }
    }

    @Override
    public void eliminar() {
     if (tab_solicitud.isFocus()) {
            tab_solicitud.eliminar();
        } else if (tab_detalle.isFocus()) {
            tab_detalle.eliminar();
        }else if(tab_requisito.isFocus()) {
            tab_requisito.eliminar();
        }
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

    public Tabla getTab_requisito() {
        return tab_requisito;
    }

    public void setTab_requisito(Tabla tab_requisito) {
        this.tab_requisito = tab_requisito;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
