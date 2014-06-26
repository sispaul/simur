/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_placa_asignada extends Pantalla{

    private AutoCompletar aut_busca = new AutoCompletar();
    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    private SeleccionTabla set_placa = new SeleccionTabla();
    
    //
    private Combo cmb_usuario = new Combo();
    
    //
    private Calendario cal_fechabus = new Calendario();
    
    //
    private Texto txt_comentario = new Texto();
    private Texto txt_comentario1 = new Texto();
    private Texto txt_busca = new Texto();
    private Texto txt_placa = new Texto();
    
    //
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogoq = new Dialogo();
    private Dialogo dia_dialogoa = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_dq = new Grid();
    private Grid grid_da = new Grid();
    private Grid gride = new Grid();
    private Grid gridq = new Grid();
    private Grid grida = new Grid();
    
    //
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();

    //
    Integer identificacion;
    
    //
     private Conexion conexion= new Conexion();
     
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    public pre_placa_asignada() {
        
        conexion.NOMBRE_MARCA_BASE="sqlserver";
        conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("ASIGNACIÒN DE PLACAS POR SOLICITUD");
        agregarComponente(pan_opcion);
        
        //Creación de Botones; Busqueda/Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        cmb_usuario.setId("cmb_usuario");
        cmb_usuario.setCombo("SELECT IDE_USUA,NICK_USUA FROM SIS_USUARIO WHERE IDE_PERF <> 1");
        cmb_usuario.eliminarVacio();
        
        //Boton para asignacion de estados
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2); 
        gri_busca.getChildren().add(new Etiqueta("USUARIO"));
        gri_busca.getChildren().add(cmb_usuario);
        gri_busca.getChildren().add(new Etiqueta("BUSCAR FECHA"));
        gri_busca.getChildren().add(cal_fechabus);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        // METODO PARA AUTOCOMPLETAR LA BUSQUEDA
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA,NOMBRE_GESTOR\n" +
                                    "FROM TRANS_SOLICITUD_PLACA");
//        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(75);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        ///
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR ASIGNACIÓN"); //titulo
        dia_dialogoe.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("10%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.setSeleccionTabla("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA where IDE_SOLICITUD_PLACA=-1", "IDE_SOLICITUD_PLACA");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD");
        agregarComponente(set_solicitud);
        
        Grid gri_placa = new Grid();
        gri_placa.setColumns(2);
        txt_comentario.setSize(50);
        gri_placa.getChildren().add(new Etiqueta("RAZON POR LA QUE SE DESVINCULA"));
        gri_placa.getChildren().add(txt_comentario);
        txt_busca.setSize(9);
        gri_placa.getChildren().add(new Etiqueta("# PLACA"));
        gri_placa.getChildren().add(txt_busca);
        Boton bot_placa = new Boton();
        bot_placa.setValue("Buscar");
        bot_placa.setIcon("ui-icon-search");
        bot_placa.setMetodo("buscarPlaca");
        bar_botones.agregarBoton(bot_placa);
        gri_placa.getChildren().add(bot_placa);
        
        set_placa.setId("set_placa");
        set_placa.setSeleccionTabla("SELECT IDE_DETALLE_SOLICITUD,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_RVMO FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA where IDE_DETALLE_SOLICITUD=-1", "IDE_DETALLE_SOLICITUD");
        set_placa.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_placa.getTab_seleccion().setRows(10);
        set_placa.setRadio();
        set_placa.getGri_cuerpo().setHeader(gri_placa);
        set_placa.getBot_aceptar().setMetodo("aceptarDeligar");
        set_placa.setHeader("BUSCAR PLACA - DESASIGNACION");
        agregarComponente(set_placa);
        
        dia_dialogoq.setId("dia_dialogoq");
        dia_dialogoq.setTitle("PORQUE DESEA CANCELAR ASIGNACIÒN"); //titulo
        dia_dialogoq.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoq.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogoq.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoq.getBot_aceptar().setMetodo("aceptarDeligar");
        grid_dq.setColumns(4);
        agregarComponente(dia_dialogoq);
        
        dia_dialogoa.setId("dia_dialogoa");
        dia_dialogoa.setTitle("PLACA ASGINADA - ANULAR"); //titulo
        dia_dialogoa.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoa.setHeight("20%");//siempre porcentaje   alto 
        dia_dialogoa.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoa.getBot_aceptar().setMetodo("desahPlaca");
        grid_da.setColumns(4);
        agregarComponente(dia_dialogoa);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        ///
        Boton bot_asig = new Boton();
        bot_asig.setIcon("ui-icon-check");
        bot_asig.setValue("ASIGNAR");
        bot_asig.setMetodo("asignar");
        bar_botones.agregarBoton(bot_asig);
        
        Boton bot_quita = new Boton();
        bot_quita.setValue("QUITAR");
        bot_quita.setIcon("ui-icon-closethick");
        bot_quita.setMetodo("abrirBusPlaca");
        bar_botones.agregarBoton(bot_quita);
        
        Boton bot_dips = new Boton();
        bot_dips.setValue("DISPONIBLE");
        bot_dips.setIcon("ui-icon-circle-zoomout");
        bot_dips.setMetodo("abrirPlacaAsig");
        bar_botones.agregarBoton(bot_dips);
    }

    //BUSCAR SOLICITUDES INGRESADAS POR EMPRESA
    public void buscarEmpresa() {
        if (cal_fechabus.getValue() != null && cal_fechabus.getValue().toString().isEmpty() == false) {
               if (cmb_usuario.getValue()!= null) {
                   TablaGenerica tab_dato = ser_Placa.getUsuario(Integer.parseInt(cmb_usuario.getValue()+""));
                     if (!tab_dato.isEmpty()) {
                        set_solicitud.getTab_seleccion().setSql("SELECT IDE_SOLICITUD_PLACA,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA where USU_SOLICITUD LIKE '"+tab_dato.getValor("usuarioin")+"' AND FECHA_SOLICITUD ='" + cal_fechabus.getFecha()+"'");
                        set_solicitud.getTab_seleccion().ejecutarSql();
                        } else {
                               utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                               }
                  }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
/*permite abrir la busqueda para la seleccion de la solicitud aprobar*/
    public void abrirBusqueda() {
        limpiarPanel();
        limpiar();
        set_solicitud.dibujar();
        cal_fechabus.limpiar();
        set_solicitud.getTab_seleccion().limpiar();
    }
    
    /*Dibujar datos a mostrar*/
    public void dibujarSolicitud(){
         if (aut_busca.getValue() != null) {
         limpiarPanel();
        //PANTALLA CABECERA DE SOLICITUS */
        tab_solicitud.setId("tab_solicitud"); // NOMBRE PANTALLA - DECLARACION DE CABECERA
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        /*Filtro estatico para los datos a mostrar*/
            if (aut_busca.getValue() == null) {
                tab_solicitud.setCondicion("IDE_SOLICITUD_PLACA=-1");
            } else {
                tab_solicitud.setCondicion("IDE_SOLICITUD_PLACA=" + aut_busca.getValor());
            }
//        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setNombreVisual("Nro. SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_SOLICTUD").setNombreVisual("TIPO DE SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_SOLICTUD").setCombo("SELECT IDE_TIPO_SOLICTUD,DESCRIPCION_SOLICITUD FROM TRANS_TIPO_SOLICTUD");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tbp_s = new PanelTabla();
        tbp_s.setPanelTabla(tab_solicitud);
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.getChildren().add(tbp_s);
        
        tab_detalle.setId("tab_detalle");//DECLARACION DE DETALLE
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA","IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("IDE_PLACA").setLectura(true);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_detalle.getColumna("CEDULA_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_RETIRA").setVisible(false);
        tab_detalle.getColumna("IDE_PLACA").setNombreVisual("PLACA");
        tab_detalle.getColumna("IDE_PLACA").setCombo("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS");       
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("IDENTIFICACIÓN");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("ID");
        tab_detalle.getColumna("CEDULA_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("NOMBRE_PERSONA_CAMBIO").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ESTADO").setVisible(false);
        tab_detalle.getColumna("NUMERO_MATRICULA").setVisible(false);
        tab_detalle.getColumna("TIPO_VEHICULO").setVisible(false);
        tab_detalle.getColumna("TIPO_SERVICIO").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.dibujar();
        PanelTabla tbp_d=new PanelTabla(); 
        tbp_d.setPanelTabla(tab_detalle);
        pan_opcion1.getChildren().add(tbp_d);
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_opcion1);
        pan_opcion.getChildren().add(gru);
        
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }
    
    /*aceptaqcion de busqueda y autocompletado d elos datos*/
    public void aceptarBusqueda() {
        if (set_solicitud.getValorSeleccionado() != null) {
            aut_busca.setValor(set_solicitud.getValorSeleccionado());
            set_solicitud.cerrar();
           dibujarSolicitud();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división centraasignarl central
        pan_opcion.getChildren().clear();
        pan_opcion.clearInitialState();
        pan_opcion1.getChildren().clear();
        pan_opcion1.clearInitialState();
    }
    
   public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
    
   //ASIGNACION DE PLACAS
    public void asignar (){
    String asignacion="INSERT INTO TRANS_APROBACION_PLACA (FECHA_APROBACION,APROBADO,USU_APROBACION,IDE_DETALLE_SOLICITUD) "
            + "VALUES ("+ utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",1,'"+tab_consulta.getValor("NICK_USUA")+"',"+tab_detalle.getValor("IDE_DETALLE_SOLICITUD")+")";
    conexion.ejecutarSql(asignacion);
    aprobacion();
    }   
       
    public void aprobacion(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        dia_dialogoe.setDialogo(grid_de);
        dia_dialogoe.dibujar();
    }
       
   public void aceptoValores(){
       Integer fisicap = Integer.parseInt(ser_Placa.getTipoDisponibleFP(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO"))));
       Integer papel = Integer.parseInt(ser_Placa.getTipoDisponiblePP(Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO"))));
        if(fisicap> 0){
                 TablaGenerica tab_dato = ser_Placa.getAprobacion(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
                    if (!tab_dato.isEmpty()) {
                     // Cargo la información de la base de datos maestra 
                     identificacion = Integer.parseInt(tab_dato.getValor("IDE_APROBACION_PLACA"));
                     ser_Placa.seleccionarPF(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), 
                     Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")), identificacion);
                     aceptoValores1();
                     dia_dialogoe.cerrar();
                     tab_detalle.actualizar();
                     utilitario.agregarMensajeInfo("Placas Fisica Asignada", "");
                     } else {
                         utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ", "");
                         }      
                } else if(fisicap <=0 && papel> 0 ){
                     TablaGenerica tab_dato = ser_Placa.getAprobacion(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")));
                    if (!tab_dato.isEmpty()) {
                        // Cargo la información de la base de datos maestra 
                        identificacion = Integer.parseInt(tab_dato.getValor("IDE_APROBACION_PLACA"));
                        ser_Placa.seleccionarPP(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")), Integer.parseInt(tab_detalle.getValor("IDE_tipo_vehiculo")), 
                        Integer.parseInt(tab_detalle.getValor("IDE_tipo_servicio")), identificacion);
                        aceptoValores1();
                        dia_dialogoe.cerrar();
                        tab_detalle.actualizar();
                        utilitario.agregarMensajeInfo("Placa Papel Asignada", "");
                        } else {
                            utilitario.agregarMensajeInfo("Datos no validos", "");
                            } 
                      }else{
                            utilitario.agregarMensajeError("No Existen Placas Disponibles", "");
                            }
   }
   
   
   public void aceptoValores1(){
       TablaGenerica tab_dato = ser_Placa.getIDPlaca(identificacion, Integer.parseInt(tab_solicitud.getValor("IDE_SOLICITUD_PLACA")));
       if (!tab_dato.isEmpty()) {
        ser_Placa.estadoPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
        utilitario.agregarMensaje("ASIGNACIÓN REALIZADA", "");
                dia_dialogoe.cerrar();
        } else {
                utilitario.agregarMensajeInfo("Datos no disponibles ", "");
            }
   }
   
   public void cancelarValores(){
        utilitario.agregarMensajeInfo("ASIGNACIÓN NO REALIZADA", "");
        dia_dialogoe.cerrar();
   }
   
   // METODOS PARA CANCELACION DE PLACA ASIGNADAS 
    public void quitar (){
        dia_dialogoq.Limpiar();
        dia_dialogoq.setDialogo(gridq);
        txt_comentario.setSize(50);
        gridq.getChildren().add(new Etiqueta("MOTIVO DE DESVINCULACIÓN DE PLACA:"));
        gridq.getChildren().add(txt_comentario);
        dia_dialogoq.setDialogo(grid_dq);
        dia_dialogoq.dibujar();
    }
      
    public void abrirBusPlaca() {
        set_placa.dibujar();
        set_placa.getTab_seleccion().limpiar();
    }
    
        public void buscarPlaca(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
                     if (!tab_dato.isEmpty()) {
                            set_placa.getTab_seleccion().setSql("SELECT IDE_DETALLE_SOLICITUD,NOMBRE_PROPIETARIO,IDE_PLACA,NUMERO_RVMO FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA where IDE_PLACA= "+tab_dato.getValor("IDE_PLACA"));
                            set_placa.getTab_seleccion().ejecutarSql();
                        } else {
                               utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                               }
    }
    
    public void aceptarDeligar(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing");
                     ser_Placa.guardarhistorial(Integer.parseInt(tab_dato.getValor("IDE_SOLICITUD_PLACA")), tab_dato.getValor("CEDULA_RUC_EMPRESA"), 
                     Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")), tab_dato.getValor("CEDULA_RUC_PROPIETARIO"), 
                     tab_dato.getValor("NOMBRE_EMPRESA"),Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO")),
                     tab_dato.getValor("NOMBRE_PROPIETARIO"), Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO")), 
                     tab_dato.getValor("NUMERO_RVMO"), txt_comentario.getValue()+"",tab_dato.getValor("ide_placa"));
                     quitarPlaca();
                     set_placa.cerrar();
                  } else {
                          utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                          }
    } 
    
    public void quitarPlaca(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing1");
                    ser_Placa.quitarPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
                    quitarDetalle();
                } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                       }
    }
    
    public void quitarDetalle(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing3");
                    ser_Placa.quitarDetalle(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                    eliminarAprobacion();
                } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
    }
    
    public void eliminarAprobacion(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(txt_busca.getValue()+"");
             if (!tab_dato.isEmpty()) {
                 System.err.println("Ing4");
                    ser_Placa.eliminarAprobacion(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                    utilitario.agregarMensajeInfo("ASIGNACIÓN ELIMINADA", "");
                    ser_Placa.actuEstado1(Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD")));
                } else {
                       utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                       }
    }
   
    //
    
    public void abrirPlacaAsig (){
        dia_dialogoa.Limpiar();
        dia_dialogoa.setDialogo(grida);
        txt_comentario1.setSize(50);
        grida.getChildren().add(new Etiqueta("COMENTARIO DE BLOQUEO:"));
        grida.getChildren().add(txt_comentario1);
        txt_placa.setSize(10);
        grida.getChildren().add(new Etiqueta("PLACA:"));
        grida.getChildren().add(txt_placa);
        dia_dialogoa.setDialogo(grid_da);
        dia_dialogoa.dibujar();
    }
    
    public void desahPlaca(){
        ser_Placa.actualPlacaDes(txt_placa.getValue()+"");
        ser_Placa.InsHistoPlaca(txt_placa.getValue()+"", txt_comentario1.getValue()+"", tab_consulta.getValor("NICK_USUA"));
        dia_dialogoa.cerrar();
    }
    
    @Override
    public void insertar() {
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
    }
    
    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
    }

    public SeleccionTabla getSet_placa() {
        return set_placa;
    }

    public void setSet_placa(SeleccionTabla set_placa) {
        this.set_placa = set_placa;
    }
    
}
