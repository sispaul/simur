/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;


import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;


/**
 *
 * @author KEJA
 */
public class pre_ingreso_solicitante extends Pantalla{
    // DELCARACION OBJETOS PANEL
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();

    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_gestor = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    //DECLARACION OBJETO DIALOGO
    private Dialogo dia_dialogoEN = new Dialogo();
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogoDes = new Dialogo();
    private Grid grid_en = new Grid();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    
    private Texto txt_ced_ruc = new Texto();
    private Combo cmb_estado = new Combo();
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
     @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    
    public pre_ingreso_solicitante() {
        /*
         * Saca el usuario que esta igresando al sistema
         */
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("CEDULA_RUC_EMPRESA").setMetodoChange("cargarEmpresa");
        tab_solicitud.getColumna("IDE_TIPO_SOLICTUD").setCombo("SELECT IDE_TIPO_SOLICTUD,DESCRIPCION_SOLICITUD FROM TRANS_TIPO_SOLICTUD");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("buscaPersona");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,descripcion_vehiculo FROM trans_vehiculo_tipo");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setMetodoChange("cargarServicio");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_RETIRA").setVisible(false);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        Boton bot_req = new Boton();
        bot_req.setValue("MOSTRAR REQUISITOS");
        bot_req.setIcon("ui-icon-note");
        bot_req.setMetodo("ingresoRequisitos");
        tabp2.setPanelTabla(tab_detalle);
        tabp2.getChildren().add(bot_req);
        
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("INGRESO DE SOLICITUD PARA PEDIDO - PLACA");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        pan_opcion1.getChildren().add(tabp1);
        pan_opcion1.getChildren().add(tabp2);

        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_vehiculo_tipo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");       
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.getMenuTabla().getItem_eliminar().setRendered(false);//nucontextual().setrendered(false);
        tabp3.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        tabp3.getMenuTabla().getItem_guardar().setRendered(false);//nucontextual().setrendered(false);
        tabp3.getMenuTabla().getItem_insertar().setRendered(false);//nucontextual().setrendered(false);
        tabp3.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tabp3.setPanelTabla(tab_requisito);
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Actualizar");
        itm_actualizar.setIcon("ui-icon-refresh");
        itm_actualizar.setMetodo("Actualizar");
         
        tabp3.getMenuTabla().getChildren().add(itm_actualizar);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pan_opcion1, tabp3, "53%",  "H");
        agregarComponente(div_division);
        
                //CONFIGURACION DE DIALOGO SELECCION DE GESTOR
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("GESTORES - SELECCIONE GESTOR DE EMPRESA"); //titulo
        dia_dialogoEN.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoValores");
        
         grid_en.setColumns(2);
         grid_en.getChildren().add(new Etiqueta("SELECCIONE GESTOR"));
        agregarComponente(dia_dialogoEN);
        
                 /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
               ///configurar la tabla Seleccion Destino
        //Configurando el dialogo
        dia_dialogoDes.setId("dia_dialogoDes");
        dia_dialogoDes.setTitle("SOLICITUD - INGRESADA"); //titulo
        dia_dialogoDes.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogoDes.setHeight("18%");//siempre porcentaje   alto
        dia_dialogoDes.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoDes.getBot_aceptar().setMetodo("aceptoDialogo");
        ///configurar tabla Destino
         agregarComponente(dia_dialogoDes);
         
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
         //campos fecha
         gri_busca.getChildren().add(new Etiqueta("FECHA INICIO"));
        gri_busca.getChildren().add(cal_fechaini);
        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL"));
        gri_busca.getChildren().add(cal_fechafin);
        gri_busca.getChildren().add(new Etiqueta("TIPO DE SOLICITUD"));
        cmb_estado.setCombo("SELECT IDE_TIPO_SOLICTUD,DESCRIPCION_SOLICITUD FROM TRANS_TIPO_SOLICTUD");
        gri_busca.getChildren().add(cmb_estado);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_SOLICITUD_PLACA,NOMBRE_GESTOR,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA WHERE IDE_SOLICITUD_PLACA=-1", "IDE_SOLICITUD_PLACA");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptoDialogo");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
    }
    
    public void buscarEmpresa() {
        if (cal_fechaini.getValue() != null && cal_fechaini.getValue().toString().isEmpty() == false && cal_fechafin.getValue() != null && cal_fechafin.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT IDE_SOLICITUD_PLACA,NOMBRE_GESTOR,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA WHERE IDE_TIPO_SOLICTUD="+cmb_estado.getValue()+" AND FECHA_SOLICITUD BETWEEN '"+cal_fechaini.getFecha()+"' AND '"+cal_fechafin.getFecha()+"'");
            set_solicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    public void cargarServicio(){
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO, DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO where IDE_TIPO_VEHICULO ="+tab_detalle.getValor("IDE_TIPO_VEHICULO"));
        utilitario.addUpdateTabla(tab_detalle,"IDE_TIPO_SERVICIO","");//actualiza solo componentes
    }
    /*
     * CREACION DE METODOS DE BUSQUEDA CON RELACION A VALIDACIONES DE CEDULA O RUC
     */
    public void buscaPersona(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } 
    }

        public void cargarEmpresa() {
            if (utilitario.validarRUC(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"))) {
            TablaGenerica tab_dato = ser_Placa.getGestor1(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("NOMBRE_GESTOR"));
                utilitario.addUpdate("tab_solicitud");
            } else {
            TablaGenerica tab_dato2 = ser_Placa.getGestor(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
            if (!tab_dato2.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato2.getValor("NOMBRE_EMPRESA"));
                utilitario.addUpdate("tab_solicitud");
                           aceptoDialogoe();
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos", "");
            }
            }
         }else if (utilitario.validarCedula(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"))) {
            TablaGenerica tab_dato1 = ser_Placa.getGestor1(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
            if (!tab_dato1.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato1.getValor("NOMBRE_GESTOR"));
                utilitario.addUpdate("tab_solicitud");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ", "");
            }
        }
    }
        /*
         * LLAMADO PARA LA CREACION DE REQUISITOS AUTOMATICOS DEPENDIENTO TIPO Y SOLICITUD
         */
        
   public void ingresoRequisitos() {
       ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
       tab_requisito.actualizar();
       utilitario.addUpdate("tab_requisito");
    }
   
   /*
         * DIBUJO DE DIALOGO PARA LA SELECCION DE GESTORES
         */
   public void aceptoDialogoe() {
        dia_dialogoEN.Limpiar();
        dia_dialogoEN.setDialogo(gride);
        grid_de.getChildren().add(set_gestor);
        set_gestor.setId("set_gestor");
        set_gestor.setSql("SELECT g.IDE_GESTOR,g.CEDULA_GESTOR,g.NOMBRE_GESTOR,g.ESTADO FROM TRANS_GESTOR g,TRANS_COMERCIAL_AUTOMOTORES c\n" 
                           +"WHERE g.IDE_COMERCIAL_AUTOMOTORES = c.IDE_COMERCIAL_AUTOMOTORES AND c.RUC_EMPRESA ="+tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
        set_gestor.getColumna("CEDULA_GESTOR").setFiltro(true);
        set_gestor.setTipoSeleccion(false);
        dia_dialogoEN.setDialogo(grid_de);
        set_gestor.dibujar();
        dia_dialogoEN.dibujar();
    }  
    /*
     * ACEPTACIÓN DE VALORES SELECCIONADOS DENTRO DEL DIALOGO
     */
        public void aceptoValores() {
        if (set_gestor.getValorSeleccionado()!= null) {
                        tab_solicitud.setValor("ide_gestor", set_gestor.getValorSeleccionado());
                             TablaGenerica tab_dato = ser_Placa.getIDGestor(Integer.parseInt(set_gestor.getValorSeleccionado()));
                              if (!tab_dato.isEmpty()) {
                                    // Cargo la información de la base de datos maestra  
                                    tab_solicitud.setValor("nombre_gestor", tab_dato.getValor("NOMBRE_GESTOR"));
                                    utilitario.addUpdate("tab_solicitud");
                                } else {
                                    utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
                                }
                        dia_dialogoEN.cerrar();
                        utilitario.addUpdate("tab_solicitud");
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
        
   public void devolverGestor() {
        if (set_gestor.getValorSeleccionado()!= null) {
                        tab_solicitud.setValor("ide_gestor", set_gestor.getValorSeleccionado());
                        dia_dialogoEN.cerrar();
                        utilitario.addUpdate("tab_solicitud");
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }    
        /*
         * LLAMADA DE REPORTE  SOLICITUD
         */
        
        @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
//        cal_fechaini.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "SOLICITUD MATRICULA":
               aceptoDialogo();
               break;
           case "SOLICITUD MATRICULA FECHA":
                dia_dialogoDes.Limpiar();
                //Agrega Fechas
                set_solicitud.dibujar();
                cal_fechaini.limpiar();
                cal_fechafin.limpiar();
                set_solicitud.getTab_seleccion().limpiar();
               break;
                
        }
    }     
       
        public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "SOLICITUD MATRICULA":
                      p_parametros = new HashMap();
                      p_parametros.put("ruc", tab_solicitud.getValor("CEDULA_RUC_EMPRESA")+"");
                      p_parametros.put("fecha", tab_solicitud.getValor("FECHA_SOLICITUD")+"");
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;
               case "SOLICITUD MATRICULA FECHA":
                    if (set_solicitud.getValorSeleccionado()!= null) {
                          TablaGenerica tab_dato = ser_Placa.getIDSolicitud(Integer.parseInt(set_solicitud.getValorSeleccionado()));
                            if (!tab_dato.isEmpty()) {
                      p_parametros = new HashMap();
                      p_parametros.put("ruc", tab_dato.getValor("CEDULA_RUC_EMPRESA")+"");
                      p_parametros.put("fecha", tab_dato.getValor("FECHA_SOLICITUD")+"");
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
                      } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
                         }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                           } 
               break;
        }

    }

    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }
    
    @Override
    public void insertar() {
        if (tab_solicitud.isFocus()) {
         tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.insertar();
                     }   
    }

    @Override
    public void guardar() {
        if (tab_solicitud.guardar()) {
               utilitario.addUpdate("tab_solicitud");
            if (tab_detalle.guardar()) {
                 guardarPantalla();
                 utilitario.addUpdate("tab_detalle");
              }
      } 
    }

    @Override
    public void eliminar() {
         if (tab_solicitud.isFocus()) {
         tab_solicitud.eliminar();
            } else if (tab_detalle.isFocus()) {
                        ser_Placa.borrarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
                        tab_detalle.eliminar();
                     }
    }

        public void Actualizar() {
        requisito();
        utilitario.addUpdate("tab_requisito");
        utilitario.agregarMensaje("Registros Almacenados Corectamente", "");
    }
    /*
     * FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES
     */
     public void seleccionar_tabla1(SelectEvent evt) {
        tab_solicitud.seleccionarFila(evt);
    }
    public void requisito(){
                          for (int i = 0; i < tab_requisito.getTotalFilas(); i++) {
                          tab_requisito.getValor(i, "CONFIRMAR_REQUISITO");
                          tab_requisito.getValor(i, "IDE_TIPO_REQUISITO");
                          tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD");
                          ser_Placa.actulizarRequisito(utilitario.StringToByte(tab_requisito.getValor(i,"CONFIRMAR_REQUISITO")),Integer.parseInt(tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD"))
                        ,Integer.parseInt(tab_requisito.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_requisito.getValor(i, "IDE_TIPO_REQUISITO")));
                          utilitario.addUpdate("tab_requisito");
                        }
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

    public Tabla getSet_gestor() {
        return set_gestor;
    }

    public void setSet_gestor(Tabla set_gestor) {
        this.set_gestor = set_gestor;
    }

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
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