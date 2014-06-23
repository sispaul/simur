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
public class pre_placas_fisicas extends Pantalla{

    //TABLA
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_detalle1 = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Panel pan_opcion3 = new Panel();
    private Panel pan_opcion4 = new Panel();
    
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
    
    public pre_placas_fisicas() {
        
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
            "1", "# CEDULA/RUC"
        };
        Object fila2[] = {
            "2", "# PLACA"
        };
//        lista1.add(fila1);;
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
        pan_opcion.setHeader("DATOS DE PROPIETARIO - ENTREGA PLACA FISICA");
        
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
//        if(cmb_documento.getValue().equals("1")){//CEDULA/RUC
//                 TablaGenerica tab_dato = serviciobusqueda.getCedula(txt_numero.getValue()+"");
//                    if (!tab_dato.isEmpty()) {//papel a fisica
//                             aut_busca.setValor(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
//                           dibujar();
//                            utilitario.addUpdate("aut_busca,pan_opcion");
//                         } else {
//                                TablaGenerica tab_dato1 = serviciobusqueda.getCedulaF(txt_numero.getValue()+"");
//                                        if (!tab_dato1.isEmpty()) {//fisica a fisica
//                                                aut_busca.setValor(tab_dato1.getValor("IDE_DETALLE_SOLICITUD"));
//                                                dibujarF();
//                                                utilitario.addUpdate("aut_busca,pan_opcion");
//                                             } else {
//                                                    utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
//                                                     }
//                                 }
//            }else 
            if(cmb_documento.getValue().equals("2")){//PLACA
                        TablaGenerica tab_dato = serviciobusqueda.getPlacaF(txt_numero.getValue()+"");
                            if (!tab_dato.isEmpty()) {//papel a fisica
                                    aut_busca.setValor(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));                    
                                   dibujar();
                                    utilitario.addUpdate("aut_busca,pan_opcion");
                                } else {
                                        TablaGenerica tab_dato1 = serviciobusqueda.getPlacaFF(txt_numero.getValue()+"");
                                            if (!tab_dato1.isEmpty()) {//fisica a fisica
                                                    aut_busca.setValor(tab_dato1.getValor("IDE_DETALLE_SOLICITUD"));                    
                                                    dibujarF();        
                                                    utilitario.addUpdate("aut_busca,pan_opcion");
                                                } else {
                                                        utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                                        }
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
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setMetodoChange("aceptoCambio");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla pat_panel=new PanelTabla();
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("actuaMetodo");
         
        pat_panel.getMenuTabla().getChildren().add(itm_actualizar);
        pat_panel.setPanelTabla(tab_detalle);
        
        pan_opcion2.setId("pan_opcion2");
	pan_opcion2.setTransient(true);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Guardar");
        bot_buscar.setIcon("ui-icon-disk");
        bot_buscar.setMetodo("actuaMetodo");
        
        pan_opcion4.setId("pan_opcion4");
	pan_opcion4.setTransient(true);
        
        pan_opcion2.getChildren().add(bot_buscar);
        Boton bot_print = new Boton();
        bot_print.setValue("Imprimir");
        bot_print.setIcon("ui-icon-print");
        bot_print.setMetodo("abrirListaReportes");
        pan_opcion2.getChildren().add(bot_print);
        pan_opcion4.getChildren().add(pat_panel);
        //CREACION DE DE CAMPOS QUE MOSTRARAN LOS DATOS EN GRID DENTRO DE UN PANEL
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_opcion4);
        gru.getChildren().add(pan_opcion2);
        pan_opcion.getChildren().add(gru);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
    }    
    
  public void aceptoCambio(){
        if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                
                                    } else {
                                    utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
                                        }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
                                    } else {
                                        utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
                                    }
        }
     }
    
   public void dibujarF(){
     if (aut_busca.getValue() != null) {
            limpiarPanel();
        tab_detalle1.setId("tab_detalle1");
        tab_detalle1.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 1);
                /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_detalle1.setCondicion("IDE_DETALLE_SOLICITUD=-1");
        } else {
            tab_detalle1.setCondicion("IDE_DETALLE_SOLICITUD=" + aut_busca.getValor());
        }
        tab_detalle1.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle1.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        tab_detalle1.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle1.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS");
        tab_detalle1.getColumna("FECHA_ENTREGA_PLACA").setValorDefecto(utilitario.getFechaActual());
        tab_detalle1.getColumna("FECHA_ENTREGA_PLACA").setLectura(true);
        tab_detalle1.getColumna("TIPO_VEHICULO").setVisible(false);
        tab_detalle1.getColumna("TIPO_SERVICIO").setVisible(false);
        tab_detalle1.getColumna("CEDULA_PERSONA_CAMBIO").setVisible(false);
        tab_detalle1.getColumna("NOMBRE_PERSONA_CAMBIO").setVisible(false);
        tab_detalle1.getColumna("CEDULA_PERSONA_RETIRA").setMetodoChange("aceptoCambio1");
        tab_detalle1.setTipoFormulario(true);
        tab_detalle1.getGrid().setColumns(4);
        tab_detalle1.dibujar();
        PanelTabla pat_panel=new PanelTabla();
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("ejeMetodo");
         
        pat_panel.getMenuTabla().getChildren().add(itm_actualizar);
        pat_panel.setPanelTabla(tab_detalle1);
        
        pan_opcion2.setId("pan_opcion2");
	pan_opcion2.setTransient(true);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Guardar");
        bot_buscar.setIcon("ui-icon-disk");
        bot_buscar.setMetodo("ejeMetodo");
        
        pan_opcion3.setId("pan_opcion3");
	pan_opcion3.setTransient(true);
        
        pan_opcion2.getChildren().add(bot_buscar);
        Boton bot_print = new Boton();
        bot_print.setValue("Imprimir");
        bot_print.setIcon("ui-icon-print");
        bot_print.setMetodo("abrirListaReportes");
        pan_opcion2.getChildren().add(bot_print);
        
        //CREACION DE DE CAMPOS QUE MOSTRARAN LOS DATOS EN GRID DENTRO DE UN PANEL
        pan_opcion3.getChildren().add(pat_panel);
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_opcion3);
        gru.getChildren().add(pan_opcion2);
        pan_opcion.getChildren().add(gru);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
    }    
    
     public void aceptoCambio1(){
        if (utilitario.validarCedula(tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle1.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                
                                    } else {
                                    utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
                                        }
        } else if (utilitario.validarRUC(tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle1.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
                                    } else {
                                        utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
                                    }
        }
     }
   
    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    } 
    
    private void limpiarPanel() {
        //borra el contenido de la división central
        pan_opcion.getChildren().clear();
        pan_opcion2.getChildren().clear();
        pan_opcion3.getChildren().clear();
        pan_opcion4.getChildren().clear();
    }
    
    public void ejeMetodo(){
            ser_Placa.entregaPlaca(tab_detalle1.getValor("CEDULA_RUC_PROPIETARIO"), tab_detalle1.getValor("NOMBRE_PROPIETARIO"), tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"), 
             tab_detalle1.getValor("NOMBRE_PERSONA_RETIRA"), tab_consulta.getValor("NICK_USUA"), Integer.parseInt(tab_detalle1.getValor("IDE_DETALLE_SOLICITUD")));
             utilitario.agregarMensaje("Guardado Correctamente ", "");
             ejeGuardar();
    }
         
    public void ejeGuardar(){
        TablaGenerica tab_dato = ser_Placa.getIDEntrega(Integer.parseInt(tab_detalle1.getValor("IDE_DETALLE_SOLICITUD")));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                ser_Placa.actualizarDS1(Integer.parseInt(tab_dato.getValor("IDE_ENTREGA_PLACA")),Integer.parseInt(tab_detalle1.getValor("IDE_DETALLE_SOLICITUD")),tab_detalle1.getValor("NOMBRE_PERSONA_RETIRA"), tab_detalle1.getValor("CEDULA_PERSONA_RETIRA"));
                utilitario.addUpdate("tab_detalle");
                actualizarDE();
            } else {
                utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
            }
    }   
         
    public void actualizarDE(){
        ser_Placa.actualizarDE(Integer.parseInt(tab_detalle1.getValor("IDE_DETALLE_SOLICITUD")), tab_detalle1.getValor("CEDULA_RUC_PROPIETARIO"), Integer.parseInt(tab_detalle1.getValor("IDE_PLACA")));
    }
    
    
    public void actuaMetodo(){
     ser_Placa.actualFinal(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),tab_detalle.getValor("CEDULA_PERSONA_CAMBIO"),tab_detalle.getValor("NOMBRE_PERSONA_CAMBIO"));
     utilitario.agregarMensaje("Guardado Correctamente ", "");
     actualizaMetodo();
    }
    
    public void actualizaMetodo(){
     ser_Placa.actualFinalPlaca(Integer.parseInt(tab_detalle.getValor("IDE_PLACA")), Integer.parseInt(tab_detalle.getValor("IDE_PLACA")), tab_consulta.getValor("NICK_USUA"));
    }
      
    
     @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "COMPROBANTE DE ENTREGA PLACA":
               aceptoDialogo();
               break;       
    }     
    }
    public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "COMPROBANTE DE ENTREGA PLACA":
                        TablaGenerica tab_dato = serviciobusqueda.getPlacaF(txt_numero.getValue()+"");
                            if (!tab_dato.isEmpty()) {//papel a fisica
                                    p_parametros = new HashMap();
                                    p_parametros.put("cedula", tab_detalle.getValor("CEDULA_RUC_PROPIETARIO")+"");
                                    p_parametros.put("vehiculo", Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")+""));
                                    p_parametros.put("servicio", Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")+""));
                                    p_parametros.put("factura", tab_detalle.getValor("NUMERO_RVMO")+"");
                                    p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                                    rep_reporte.cerrar();
                                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                    sef_formato.dibujar();
                                } else {
                                        TablaGenerica tab_dato1 = serviciobusqueda.getPlacaFF(txt_numero.getValue()+"");
                                            if (!tab_dato1.isEmpty()) {//fisica a fisica
                                                        p_parametros = new HashMap();
                                                        p_parametros.put("cedula", tab_detalle1.getValor("CEDULA_RUC_PROPIETARIO")+"");
                                                        p_parametros.put("vehiculo", Integer.parseInt(tab_detalle1.getValor("IDE_TIPO_VEHICULO")+""));
                                                        p_parametros.put("servicio", Integer.parseInt(tab_detalle1.getValor("IDE_TIPO_SERVICIO")+""));
                                                        p_parametros.put("factura", tab_detalle1.getValor("NUMERO_RVMO")+"");
                                                        p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                                                        rep_reporte.cerrar();
                                                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                                        sef_formato.dibujar();
                                                } else {
                                                        utilitario.agregarMensajeInfo("Nose encuentra aprobado", "");
                                                        }
                                }                   
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

    public Tabla getTab_detalle1() {
        return tab_detalle1;
    }

    public void setTab_detalle1(Tabla tab_detalle1) {
        this.tab_detalle1 = tab_detalle1;
    }
    
}