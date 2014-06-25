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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;
import java.util.Timer;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_ingreso_solicitante extends Pantalla{
    
    // DECLARACION OBJETOS PANEL
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Efecto efecto1 = new Efecto();

    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_gestor = new Tabla();
    private Tabla set_colaborador = new Tabla();
    private Tabla set_colaborador1 = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    private SeleccionTabla set_activadas = new SeleccionTabla();
    
    //DECLARACION OBJETO DIALOGO
    private Dialogo dia_dialogoEN = new Dialogo();
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogoDes = new Dialogo();
    private Dialogo dia_dialogoc = new Dialogo();
    private Dialogo dia_dialogoc1 = new Dialogo();
    private Dialogo dia_dialogore = new Dialogo();
    private Grid grid_re = new Grid();
    private Grid grid_en = new Grid();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    private Grid grid_dc = new Grid();
    private Grid gridc = new Grid();
    private Grid grid_dc1 = new Grid();
    private Grid gridc1 = new Grid();
    private Grid gridre = new Grid();
    
    private Combo cmb_estado = new Combo();
    private Combo cmb_documento = new Combo();
    
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //TEXTO
    private Texto txt_numero = new Texto();
    private Conexion conexion= new Conexion();
    
     @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    private       // Aquí se pone en marcha el timer cada segundo.
     Timer timer = new Timer();
    
    String solicitud,detalle;

    public pre_ingreso_solicitante() {
         //Creación de Botones; Busqueda/Limpieza

        conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        
            solicitud=ser_Placa.getGestorNum();
            detalle = ser_Placa.getGestorNumDe();
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Retenidos");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
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
        tab_solicitud.getColumna("NOMBRE_EMPRESA").setMetodoChange("buscaColaborador");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
         tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
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
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setMetodoChange("LlenarCMBVe");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setMetodoChange("LlenarCMBSe");
        tab_detalle.getColumna("TIPO_VEHICULO").setMetodoChange("cargarServicio");
        tab_detalle.getColumna("TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,descripcion_vehiculo FROM trans_vehiculo_tipo");
        tab_detalle.getColumna("TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("TIPO_VEHICULO").setLectura(true);
        tab_detalle.getColumna("TIPO_SERVICIO").setLectura(true);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setMetodoChange("ingresoRequisitos");
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("NUMERO_MATRICULA").setVisible(false);
        tab_detalle.getColumna("ESTADO").setVisible(false);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        Boton bot_new = new Boton();
        bot_new.setValue("NUEVO");
        bot_new.setIcon("ui-icon-document");
        bot_new.setMetodo("insertar");
        
        Boton bot_save = new Boton();
        bot_save.setValue("GUARDAR");
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("guardar");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("ELIMINAR");
        bot_delete.setIcon("ui-icon-closethick");
        bot_delete.setMetodo("eliminar");
        
        Boton bot_req = new Boton();
        bot_req.setValue("MOSTRAR REQUISITOS");
        bot_req.setIcon("ui-icon-note");
        bot_req.setMetodo("ingresoRequisitos");
        
         pan_opcion2.setId("pan_opcion2");
         pan_opcion2.getChildren().add(bot_new);
         pan_opcion2.getChildren().add(bot_delete);
        tabp2.setPanelTabla(tab_detalle);
        tabp2.getChildren().add(pan_opcion2);
        
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
        pan_opcion1.getChildren().add(tabp3);
        agregarComponente(pan_opcion1);
        
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
        
        //CONFIGURACIÓN DE OBJETO REPORTE
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
        
        dia_dialogoc.setId("dia_dialogoc");
        dia_dialogoc.setTitle("BUSCAR DATOS DE EMPRESA"); //titulo
        dia_dialogoc.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoc.setHeight("45%");//siempre porcentaje   alto
        dia_dialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoc.getBot_aceptar().setMetodo("aceptoColaborador");
        grid_dc.setColumns(4);
        agregarComponente(dia_dialogoc);
        
        dia_dialogoc1.setId("dia_dialogoc1");
        dia_dialogoc1.setTitle("SELECICONAR GESTOR"); //titulo
        dia_dialogoc1.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoc1.setHeight("35%");//siempre porcentaje   alto
        dia_dialogoc1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoc1.getBot_aceptar().setMetodo("aceptoGestorco");
        grid_dc1.setColumns(4);
        agregarComponente(dia_dialogoc1);
        
        dia_dialogore.setId("dia_dialogore");
        dia_dialogore.setTitle("INGRESAR PARAMETROS DE BUSQUEDA"); //titulo
        dia_dialogore.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogore.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogore.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogore.getBot_aceptar().setMetodo("aceptoRetenida");
        grid_re.setColumns(4);
        agregarComponente(dia_dialogore);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_SOLICITUD_PLACA,NOMBRE_GESTOR,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA WHERE IDE_SOLICITUD_PLACA=-1", "IDE_SOLICITUD_PLACA");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptoDialogo");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
        
        cmb_documento.setId("cmb_documento");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "1", "# FACTURA"
        };
        Object fila2[] = {
            "2", "# SOLICITUD"
        };
        
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmb_documento.setCombo(lista1);
        
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
         //campos fecha
        gri_busca1.getChildren().add(new Etiqueta("ESCOGER"));
        gri_busca1.getChildren().add(cmb_documento);
        txt_numero.setSize(20);
        gri_busca1.getChildren().add(new Etiqueta("INGRESE #"));
        gri_busca1.getChildren().add(txt_numero);
        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("buscarSolicitud");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);
        
        set_activadas.setId("set_activadas");
        set_activadas.setSeleccionTabla("SELECT IDE_DETALLE_SOLICITUD,IDE_SOLICITUD_PLACA,NOMBRE_PROPIETARIO,NUMERO_RVMO FROM TRANS_DETALLE_SOLICITUD_PLACA WHERE IDE_DETALLE_SOLICITUD=-1", "IDE_DETALLE_SOLICITUD");
        set_activadas.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_activadas.getTab_seleccion().setRows(10);
        set_activadas.setRadio();
        set_activadas.getGri_cuerpo().setHeader(gri_busca1);
        set_activadas.getBot_aceptar().setMetodo("aceptarBusquedaDE");
        set_activadas.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_activadas);
    }
    
    /*permite abrir la busqueda para la seleccion de la solicitud aprobar*/
    public void abrirBusqueda() {
        set_activadas.dibujar();
        set_activadas.getTab_seleccion().limpiar();
    }
    
    public void buscarSolicitud() {
        if(cmb_documento.getValue().equals("1")){//factura
                if (txt_numero.getValue() != null && txt_numero.getValue().toString().isEmpty() == false ) {
                        set_activadas.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_SOLICITUD_PLACA,NOMBRE_PROPIETARIO,NUMERO_RVMO FROM TRANS_DETALLE_SOLICITUD_PLACA where ESTADO = 0 and NUMERO_RVMO ="+Integer.parseInt(txt_numero.getValue()+""));
                        set_activadas.getTab_seleccion().ejecutarSql();
                    } else {
                            utilitario.agregarMensajeInfo("Debe seleccionar una opción", "");
                            }
        }else if(cmb_documento.getValue().equals("2")){//solicitud
                    if (txt_numero.getValue() != null && txt_numero.getValue().toString().isEmpty() == false ) {
                            set_activadas.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_SOLICITUD_PLACA,NOMBRE_PROPIETARIO,NUMERO_RVMO FROM TRANS_DETALLE_SOLICITUD_PLACA where ESTADO = 0 and IDE_SOLICITUD_PLACA ="+Integer.parseInt(txt_numero.getValue()+""));
                            set_activadas.getTab_seleccion().ejecutarSql();
                        } else {
                                utilitario.agregarMensajeInfo("Debe seleccionar una opciòn", "");
                                }
        }
    }
    
    /*aceptacion de busqueda y autocompletado d elos datos*/
    public void aceptarBusquedaDE() {
        if (set_activadas.getValorSeleccionado() != null) {
            TablaGenerica tab_dato = ser_Placa.getNuevoReg(Integer.parseInt(set_activadas.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                                //solicitud
                                ser_Placa.nuevo(Integer.parseInt(solicitud), Integer.parseInt(tab_dato.getValor("IDE_GESTOR")), tab_dato.getValor("CEDULA_RUC_EMPRESA"), tab_dato.getValor("NOMBRE_EMPRESA"), Integer.parseInt(tab_dato.getValor("IDE_TIPO_SOLICTUD")), tab_dato.getValor("USU_SOLICITUD"), tab_dato.getValor("NOMBRE_GESTOR"));
                                ingresaD();
                            }else {
                                    utilitario.agregarMensajeInfo("no existe datos1", "");
                                }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }

    }
    
    public void ingresaD(){
     try {
     Thread.sleep(2000);
        if (set_activadas.getValorSeleccionado() != null) {
            TablaGenerica tab_dato = ser_Placa.getNuevoReg(Integer.parseInt(set_activadas.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                ser_Placa.DataNew(Integer.parseInt(detalle),Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO")), Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(solicitud), tab_dato.getValor("CEDULA_RUC_PROPIETARIO"), tab_dato.getValor("NOMBRE_PROPIETARIO"), 
                                tab_dato.getValor("NUMERO_RVMO"));
                }else {
                                    utilitario.agregarMensajeInfo("no existe datos1", "");
                                }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
         } catch (InterruptedException e) {
     }
    }
    
    public void actuaD(){
    try {
     Thread.sleep(2000);    
        if (set_activadas.getValorSeleccionado() != null) {
            TablaGenerica tab_dato = ser_Placa.getNuevoReg(Integer.parseInt(set_activadas.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
//        ser_Placa.insertarRequisito(Integer.parseInt(detalle), Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO")));
                ser_Placa.deleteRequisito(Integer.parseInt(detalle));                
                ser_Placa.deleteDetalle(Integer.parseInt(detalle));
        utilitario.agregarMensaje("Registro Actualizado", ""); 
        }else {
                                    utilitario.agregarMensajeInfo("no existe datos1", "");
                                }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
            } catch (InterruptedException e) {
     }
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
        tab_detalle.getColumna("TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO, DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO where IDE_TIPO_VEHICULO ="+tab_detalle.getValor("TIPO_VEHICULO"));
        utilitario.addUpdateTabla(tab_detalle,"TIPO_SERVICIO","");//actualiza solo componentes
    }
    
    //CREACION DE METODOS DE BUSQUEDA CON RELACION A VALIDACIONES DE CEDULA O RUC
    public void buscaPersona(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                ingresoRequisitos();
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
                ingresoRequisitos();
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } 
    }

    public void buscaColaborador(){
        dia_dialogoc.Limpiar();
        dia_dialogoc.setDialogo(gridc);
        grid_dc.getChildren().add(set_colaborador);
        set_colaborador.setId("set_colaborador");
        set_colaborador.setHeader("LISTA DE EMPRESAS");
        set_colaborador.setSql("SELECT IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA,RUC_EMPRESA FROM TRANS_COMERCIAL_AUTOMOTORES WHERE NOMBRE_EMPRESA LIKE '%"+tab_solicitud.getValor("NOMBRE_EMPRESA")+"%'");
        set_colaborador.getColumna("NOMBRE_EMPRESA").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogoc.setDialogo(grid_dc);
        set_colaborador.dibujar();
        dia_dialogoc.dibujar();
       }
    
    public void buscaGestorco(){
        dia_dialogoc1.Limpiar();
        dia_dialogoc1.setDialogo(gridc1);
        grid_dc1.getChildren().add(set_colaborador1);
        set_colaborador1.setId("set_colaborador1");
        set_colaborador1.setHeader("LISTA DE GESTORES");
        set_colaborador1.setSql("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO FROM TRANS_GESTOR WHERE IDE_COMERCIAL_AUTOMOTORES ="+set_colaborador.getValorSeleccionado());
        set_colaborador1.setRows(10);
        set_colaborador1.setTipoSeleccion(false);
        dia_dialogoc1.setDialogo(grid_dc1);
        set_colaborador1.dibujar();
        dia_dialogoc1.dibujar();
       }    
    
    public void aceptoColaborador(){
     if (set_colaborador.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = ser_Placa.getDevEmpresa(Integer.parseInt(set_colaborador.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                     tab_solicitud.setValor("cedula_ruc_empresa", tab_dato.getValor("RUC_EMPRESA"));
                     tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("NOMBRE_EMPRESA"));
                      utilitario.addUpdate("tab_solicitud");
                      dia_dialogoc.cerrar();
                      buscaGestorco();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                               }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    } 
    
       public void aceptoGestorco(){
     if (set_colaborador1.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = ser_Placa.getGestor2(Integer.parseInt(set_colaborador1.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                     tab_solicitud.setValor("NOMBRE_GESTOR", tab_dato.getValor("NOMBRE_GESTOR"));
                     tab_solicitud.setValor("IDE_GESTOR", tab_dato.getValor("IDE_GESTOR"));
                     tab_solicitud.setValor("IDE_TIPO_SOLICTUD", "6");
                      utilitario.addUpdate("tab_solicitud");
                      dia_dialogoc1.cerrar();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                               }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    }
    
    public void cargarEmpresa() {
      if (utilitario.validarRUC(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"))) {
            TablaGenerica tab_dato = ser_Placa.getGestor1(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
                if (!tab_dato.isEmpty()) {
                    // Cargo la información de la base de datos maestra   
                    tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("NOMBRE_GESTOR"));
                    tab_solicitud.setValor("IDE_TIPO_SOLICTUD", "6");
                    utilitario.addUpdate("tab_solicitud");
                } else {
            TablaGenerica tab_dato2 = ser_Placa.getGestor(tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
                if (!tab_dato2.isEmpty()) {
                    // Cargo la información de la base de datos maestra   
                    tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato2.getValor("NOMBRE_EMPRESA"));
                    tab_solicitud.setValor("IDE_TIPO_SOLICTUD", "6");
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
                tab_solicitud.setValor("IDE_TIPO_SOLICTUD", "8");
                utilitario.addUpdate("tab_solicitud");
            } else {
                    utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ", "");
                   }
        }
    }
    
   //LLAMADO PARA LA CREACION DE REQUISITOS AUTOMATICOS DEPENDIENTO TIPO Y SOLICITUD    
   public void ingresoRequisitos() {
       tab_detalle.guardar();
       guardarPantalla();
       utilitario.addUpdate("tab_detalle");
       ser_Placa.actuEstado(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
       saveRequisito();
    }
   
   //Requisitos pra cada solicitud
   public void saveRequisito(){
     try {
     Thread.sleep(2000);
       ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
       tab_requisito.actualizar();
       utilitario.addUpdate("tab_requisito");
     } catch (InterruptedException e) {
     }       
   }
   
   /* DIBUJO DE DIALOGO PARA LA SELECCION DE GESTORES */
   public void aceptoDialogoe() {
        dia_dialogoEN.Limpiar();
        dia_dialogoEN.setDialogo(gride);
        grid_de.getChildren().add(set_gestor);
        set_gestor.setId("set_gestor");
        set_gestor.setSql("SELECT g.IDE_GESTOR,g.CEDULA_GESTOR,g.NOMBRE_GESTOR,g.ESTADO FROM TRANS_GESTOR g,TRANS_COMERCIAL_AUTOMOTORES c\n" 
                           +"WHERE g.IDE_COMERCIAL_AUTOMOTORES = c.IDE_COMERCIAL_AUTOMOTORES AND estado = 1 AND c.RUC_EMPRESA ="+tab_solicitud.getValor("CEDULA_RUC_EMPRESA"));
        set_gestor.getColumna("CEDULA_GESTOR").setFiltro(true);
        set_gestor.setTipoSeleccion(false);
        dia_dialogoEN.setDialogo(grid_de);
        set_gestor.dibujar();
        dia_dialogoEN.dibujar();
    }  
   
    // ACEPTACIÓN DE VALORES SELECCIONADOS DENTRO DEL DIALOGO
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
        
   //devuelve gestor seleccionado
   public void devolverGestor() {
        if (set_gestor.getValorSeleccionado()!= null) {
                        tab_solicitud.setValor("ide_gestor", set_gestor.getValorSeleccionado());
                        dia_dialogoEN.cerrar();
                        utilitario.addUpdate("tab_solicitud");
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
   
   //llenado por ide de vehiculo y servicio
   public void LlenarCMBVe(){
       TablaGenerica tab_dato = ser_Placa.getVehiculo_Serv(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")));
       if (!tab_dato.isEmpty()) {
           tab_detalle.setValor("TIPO_VEHICULO", tab_dato.getValor("IDE_TIPO_VEHICULO"));
           utilitario.addUpdate("tab_detalle");
       } else {
              utilitario.agregarMensajeInfo("No existe Automotor", "");
             }
   }
   public void LlenarCMBSe(){
     TablaGenerica tab_dato = ser_Placa.getServicio_Veh(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")));
       if (!tab_dato.isEmpty()) {
           tab_detalle.setValor("TIPO_SERVICIO", tab_dato.getValor("IDE_TIPO_SERVICIO"));
           utilitario.addUpdate("tab_detalle");
       } else {
              utilitario.agregarMensajeInfo("No existe Servicio", "");
             }
   }
   
   public void buscaRetenida(){
        dia_dialogore.Limpiar();
        dia_dialogore.setDialogo(gridre);
        gridre.getChildren().add(new Etiqueta("QUE VA A BUSCAR:"));
        gridre.getChildren().add(cmb_documento);
        txt_numero.setSize(20);
        gridre.getChildren().add(new Etiqueta("INGRESE # :"));
        gridre.getChildren().add(txt_numero);
        dia_dialogore.setDialogo(grid_re);
        dia_dialogore.dibujar();
   }
   
   public void aceptoRetenida(){
       if(cmb_documento.getValue().equals("1")){
           
            utilitario.agregarMensajeInfo("Factura","");
           dia_dialogore.cerrar();
            }else if(cmb_documento.getValue().equals("2")){
                     utilitario.agregarMensajeInfo("Solicitud","");
                    dia_dialogore.cerrar();
                    }
   }
        /*  LLAMADA DE REPORTE SOLICITUD  */
        @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
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
    
    public void crearNuevo(){
        
    }
    
    public void confirNuevo(){
        
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
         if (utilitario.validarRUC(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"))) {
                if (tab_solicitud.guardar()) {
                        utilitario.addUpdate("tab_solicitud");
                        if (tab_detalle.guardar()) {
                                guardarPantalla();
                                utilitario.addUpdate("tab_detalle");
                            }
                }
                }else if (utilitario.validarCedula(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"))) {
                            if (tab_solicitud.guardar()) {
                                utilitario.addUpdate("tab_solicitud");
                                if (tab_detalle.guardar()) {
                                    guardarPantalla();
                                    utilitario.addUpdate("tab_detalle");
                                }
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
        utilitario.agregarMensaje("Registros Almacenados Correctamente", "");
    }
   
    /*      FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES     */
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

    public Tabla getSet_colaborador() {
        return set_colaborador;
    }

    public void setSet_colaborador(Tabla set_colaborador) {
        this.set_colaborador = set_colaborador;
    }

    public Tabla getSet_colaborador1() {
        return set_colaborador1;
    }

    public void setSet_colaborador1(Tabla set_colaborador1) {
        this.set_colaborador1 = set_colaborador1;
    }

    public SeleccionTabla getSet_activadas() {
        return set_activadas;
    }

    public void setSet_activadas(SeleccionTabla set_activadas) {
        this.set_activadas = set_activadas;
    }
    
}