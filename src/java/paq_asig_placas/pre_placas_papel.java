/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_placas_papel extends Pantalla{

    //TABLA
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    
    //TEXTO
    private Texto txt_numero = new Texto();
    
    //COMBO PARA BUSQUEDA
    private Combo cmb_documento = new Combo();
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    @EJB
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    public pre_placas_papel() {
        
        bar_botones.quitarBotonInsertar();
	bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
	bar_botones.quitarBotonsNavegacion();
        
         //Consulta de usuario conectado
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,d.NUMERO_RVMO,p.PLACA\n" +
                                    "FROM TRANS_DETALLE_SOLICITUD_PLACA d,TRANS_PLACAS p,TRANS_TIPO_ESTADO e\n" +
                                    "WHERE d.IDE_PLACA = p.IDE_PLACA AND p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Propietario:"));
        bar_botones.agregarComponente(aut_busca);
        
        Grid gri_matriz = new Grid();
        gri_matriz.setColumns(2);
        gri_matriz.setWidth("100%");
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 350px;");
        pan_panel.setHeader("Ingresar Datos de Busqueda ");
        txt_numero.setId("txt_numero");
        txt_numero.setStyle("width: 99%;");
        cmb_documento.setId("cmb_documento");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "1", "# PLACA"
        };
        Object fila2[] = {
            "2", "# RVMO"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmb_documento.setCombo(lista1);
        cmb_documento.setStyle("width: 99%;");
        gri_matriz.getChildren().add(new Etiqueta("SELECCION DE BUSQUEDA : "));
        gri_matriz.getChildren().add(cmb_documento);
        gri_matriz.getChildren().add(new Etiqueta("INGRESE # :"));
        gri_matriz.getChildren().add(txt_numero);
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscaEntrega");
        pan_opcion1.getChildren().add(bot_buscar);
        Boton bot_new = new Boton();
        bot_new.setValue("Limpiar");
        bot_new.setIcon("ui-icon-document");
        bot_new.setMetodo("limpiar");
        pan_opcion1.getChildren().add(bot_new);
        pan_panel.getChildren().add(gri_matriz);
        pan_panel.getChildren().add(pan_opcion1);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("DATOS DE PROPIETARIO - ENTREGA PLACA");
        
        Division div = new Division();
        div.dividir2(pan_panel, pan_opcion, "21%", "h");
        agregarComponente(div);
        
        // CONFIGURACIÓN DE OBJETO REPORTE
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    public void buscaEntrega(){
        if(cmb_documento.getValue().equals("1")){//PLACA
                 TablaGenerica tab_dato = serviciobusqueda.getPlaca(txt_numero.getValue()+"");
                    if (!tab_dato.isEmpty()) {
                             aut_busca.setValor(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
                           dibujar();
                            utilitario.addUpdate("aut_busca,pan_opcion");
                         } else {
                                utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                 }
            }else if(cmb_documento.getValue().equals("2")){//FACTURA
                        TablaGenerica tab_dato = serviciobusqueda.getFactura(txt_numero.getValue()+"");
                            if (!tab_dato.isEmpty()) {
                                    aut_busca.setValor(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));                    
                                   dibujar();
                                    utilitario.addUpdate("aut_busca,pan_opcion");
                                } else {
                                        utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                        }
                   }
    }
    
    
     public void dibujar(){
     if (aut_busca.getValue() != null) {
            limpiarPanel();
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 1);
                /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_detalle.setCondicion("IDE_DETALLE_SOLICITUD=-1");
        } else {
            tab_detalle.setCondicion("IDE_DETALLE_SOLICITUD=" + aut_busca.getValor());
        }
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS");
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setValorDefecto(utilitario.getFechaActual());
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setLectura(true);
        tab_detalle.getColumna("TIPO_VEHICULO").setVisible(false);
        tab_detalle.getColumna("TIPO_SERVICIO").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA ").setVisible(false);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla pat_panel=new PanelTabla();
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("ejeMetodo");
         
        pat_panel.getMenuTabla().getChildren().add(itm_actualizar);
        pat_panel.setPanelTabla(tab_detalle);
        
        pan_opcion2.setId("pan_opcion2");
	pan_opcion2.setTransient(true);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Guardar");
        bot_buscar.setIcon("ui-icon-disk");
        bot_buscar.setMetodo("ejeMetodo");
        
        pan_opcion2.getChildren().add(bot_buscar);
        Boton bot_print = new Boton();
        bot_print.setValue("Imprimir");
        bot_print.setIcon("ui-icon-print");
        bot_print.setMetodo("abrirListaReportes");
        pan_opcion2.getChildren().add(bot_print);
        
        //CREACION DE DE CAMPOS QUE MOSTRARAN LOS DATOS EN GRID DENTRO DE UN PANEL
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        gru.getChildren().add(pan_opcion2);
        pan_opcion.getChildren().add(gru);
        usuario();
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
    }
     
    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    } 
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
   if(cmb_documento.getValue().equals("1")){
        pan_opcion.getChildren().clear();
        pan_opcion2.getChildren().clear();
        }else  if(cmb_documento.getValue().equals("2")){
        pan_opcion.getChildren().clear();
        pan_opcion2.getChildren().clear();
        }
    }
    
    public void usuario(){
        if(cmb_documento.getValue().equals("1")){//PLACA
                 TablaGenerica tab_dato = serviciobusqueda.getPlaca(txt_numero.getValue()+"");
                    if (!tab_dato.isEmpty()) {
                            TablaGenerica tab_dato1 = serviciobusqueda.getGestor(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                            if (!tab_dato1.isEmpty()) { 
                                System.out.println(tab_dato1.getValor("CEDULA_GESTOR"));
                                tab_detalle.setValor("CEDULA_PERSONA_RETIRA ", tab_dato1.getValor("CEDULA_GESTOR"));
                                tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato1.getValor("NOMBRE_GESTOR"));
                                utilitario.addUpdate("tab_detalle");
                                } else {
                                        utilitario.agregarMensajeInfo("Nose encuentra gestor", "");
                                        }
                         } else {
                                utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                 }
            }else if(cmb_documento.getValue().equals("2")){//FACTURA
                        TablaGenerica tab_dato = serviciobusqueda.getFactura(txt_numero.getValue()+"");
                            if (!tab_dato.isEmpty()) {
                                    TablaGenerica tab_dato1 = serviciobusqueda.getGestor(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                                    if (!tab_dato1.isEmpty()) {  
                                        tab_detalle.setValor("CEDULA_PERSONA_RETIRA", tab_dato1.getValor("CEDULA_GESTOR"));
                                        tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato1.getValor("NOMBRE_GESTOR"));
                                        utilitario.addUpdate("tab_detalle");
                                        } else {
                                                utilitario.agregarMensajeInfo("Nose encuentra gestor", "");
                                                }
                                } else {
                                        utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                        }
                   }        
    }
    
       public void ejeMetodo(){
             ser_Placa.entregaPlaca(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"), tab_detalle.getValor("NOMBRE_PROPIETARIO"), tab_detalle.getValor("CEDULA_PERSONA_RETIRA"), 
             tab_detalle.getValor("NOMBRE_PERSONA_RETIRA"), tab_consulta.getValor("NICK_USUA"), Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
             utilitario.agregarMensaje("Guardado Correctamente ", "");
             ejeGuardar();
         }
    
         
    public void ejeGuardar(){
        TablaGenerica tab_dato = ser_Placa.getIDEntrega(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                ser_Placa.actualizarDS(Integer.parseInt(tab_dato.getValor("IDE_ENTREGA_PLACA")),Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),tab_detalle.getValor("NOMBRE_PERSONA_RETIRA"), tab_detalle.getValor("CEDULA_PERSONA_RETIRA"),tab_detalle.getValor("NUMERO_MATRICULA"));
                utilitario.addUpdate("tab_detalle");
                actualizarDE();
            } else {
                utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
            }
    }   
         
    public void actualizarDE(){
        ser_Placa.actualizarDE(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"), Integer.parseInt(tab_detalle.getValor("IDE_PLACA")));
    }
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "IMPRIMIR PLACA":
               aceptoDialogo();
               break;                       
        }
    }     
       
        public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "IMPRIMIR PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("placa", Integer.parseInt(tab_detalle.getValor("ide_placa")+""));
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;  
        }
    }
    
    

    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
    
}
