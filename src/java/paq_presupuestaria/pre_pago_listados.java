/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
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
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_pago_listados extends Pantalla{

     //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
     //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
     //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    private Texto txt_buscar1 = new Texto();
    
    //Dialogo
    private Dialogo dia_reporte = new Dialogo();
    private Grid grid_re = new Grid();
    private Grid gridre = new Grid();
    
    private Dialogo dia_reporte1 = new Dialogo();
    private Grid grid_re1 = new Grid();
    private Grid gridre1 = new Grid();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Calendario
    private Calendario cal_inicial = new Calendario();
    private Calendario cal_final = new Calendario();
    
    //combos
    private Combo cmb_tipos = new Combo();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_pago_listados() {
        
        //bloquedo
        bar_botones.quitarBotonInsertar();
	bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        
        //Consulta de usuario conectado
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //Cadena de conexion
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //para reporte
        cmb_tipos.setId("cmb_tipos");
        cmb_tipos.setConexion(con_postgres);
        cmb_tipos.setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado");
        
         //Creación de Botones; Busqueda,Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        //Auto complentar en el formulario
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado,usuario_ingre_envia\n" +
                                   "FROM tes_comprobante_pago_listado");
        aut_busca.setSize(100);
        bar_botones.agregarComponente(new Etiqueta("Busca Listado:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader(" COMPROBATE DE PAGOS POR LISTADOS ");
        efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion.getChildren().add(efecto1);
        agregarComponente(pan_opcion);
        
        //Busqueda de comprobantes
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        txt_buscar.setSize(20);
        gri_busca.getChildren().add(new Etiqueta("LISTADO # :"));
        gri_busca.getChildren().add(txt_buscar);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_comprobante.setId("set_comprobante");
        set_comprobante.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_comprobante.setSeleccionTabla("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado FROM tes_comprobante_pago_listado where IDE_LISTADO=-1", "IDE_LISTADO");
        set_comprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_comprobante.getTab_seleccion().setRows(5);
        set_comprobante.setRadio();
        set_comprobante.getGri_cuerpo().setHeader(gri_busca);
        set_comprobante.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_comprobante.setHeader("BUSCAR LISTADO A PAGAR");
        agregarComponente(set_comprobante);
        
        //Para reporte
        dia_reporte.setId("dia_reporte");
        dia_reporte.setTitle("REPORTE POR TIPO Y FECHAS COMPROBANTES (PAGOS/DEVOLUCIONES)"); //titulo
        dia_reporte.setWidth("40%"); //siempre en porcentajes  ancho
        dia_reporte.setHeight("20%");//siempre porcentaje   alto
        dia_reporte.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_reporte.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_re.setColumns(4);
        agregarComponente(dia_reporte);
        
        dia_reporte1.setId("dia_reporte1");
        dia_reporte1.setTitle("REPORTE POR LISTADO"); //titulo
        dia_reporte1.setWidth("20%"); //siempre en porcentajes  ancho
        dia_reporte1.setHeight("12%");//siempre porcentaje   alto
        dia_reporte1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_reporte1.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_re1.setColumns(4);
        agregarComponente(dia_reporte1);
        
        /*** CONFIGURACIÓN DE OBJETO REPORTE***/
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
    }

    //limpieza paneles y abrir busqueda
    public void limpiar() {
      aut_busca.limpiar();
      utilitario.addUpdate("aut_busca");
    }  

    private void limpiarPanel() {
        //borra el contenido de la división central
      pan_opcion1.getChildren().clear();
      pan_opcion.getChildren().clear();
    }

    public void abrirBusqueda(){
      set_comprobante.dibujar();
      txt_buscar.limpiar();
      set_comprobante.getTab_seleccion().limpiar();
      limpiarPanel();
    }
    
    public void buscarEntrega() {
      if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
                 set_comprobante.getTab_seleccion().setSql("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado \n" +
                                                            "FROM tes_comprobante_pago_listado WHERE ide_listado =" + txt_buscar.getValue());
                 set_comprobante.getTab_seleccion().ejecutarSql();
                 limpiar();
          } else {
                 utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
                }
    }
    
    public void aceptarBusqueda() {
        limpiarPanel();
      if (set_comprobante.getValorSeleccionado() != null) {
             aut_busca.setValor(set_comprobante.getValorSeleccionado());
             set_comprobante.cerrar();
             dibujarLista();
             utilitario.addUpdate("aut_busca,pan_opcion");
         } else {
                utilitario.agregarMensajeInfo("Debe seleccionar un listado", "");
                }
    }
    
    //Para Pagos de Comprobantes    
    public void dibujarLista(){
     if (aut_busca.getValue() != null) {
         limpiarPanel();
        //comprobante pago listado
        tab_comprobante.setId("tab_comprobante");
        tab_comprobante.setConexion(con_postgres);
        tab_comprobante.setTabla("tes_comprobante_pago_listado", "ide_listado", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_comprobante.setCondicion("ide_listado=-1");
        } else {
            tab_comprobante.setCondicion("ide_listado=" + aut_busca.getValor());
        }
        tab_comprobante.getColumna("ci_paga").setLectura(true);
        tab_comprobante.getColumna("responsable_paga").setLectura(true);
        tab_comprobante.getColumna("FECHA_LISTADO").setLectura(true);
        tab_comprobante.getColumna("CI_ENVIA").setLectura(true);
        tab_comprobante.getColumna("RESPONSABLE_ENVIA").setLectura(true);
        tab_comprobante.getColumna("ESTADO").setLectura(true);
        tab_comprobante.getColumna("DEVOLUCION").setLectura(false);
        
        tab_comprobante.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("FECHA_PAGADO").setVisible(false);
        tab_comprobante.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_PAGA").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_PAGA").setVisible(false);
        tab_comprobante.getColumna("FECHA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("CI_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        
        tab_comprobante.getColumna("ESTADO").setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado");
        
        tab_comprobante.agregarRelacion(tab_detalle);
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.getGrid().setColumns(4);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tcp.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        
        ItemMenu itm_guardar = new ItemMenu();
        itm_guardar.setValue("Guardar");
        itm_guardar.setIcon("ui-icon-disk");
        itm_guardar.setMetodo("guardarComp");
        
        tcp.getMenuTabla().getChildren().add(itm_guardar);
        tcp.setPanelTabla(tab_comprobante);
        
        //detalle comprobante pago listado
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setTabla("tes_detalle_comprobante_pago_listado", "ide_detalle_listado", 2);
        tab_detalle.getColumna("num_transferencia").setMetodoChange("Pago");
        
        tab_detalle.getColumna("CEDULA_PASS_BENEFICIARIO ").setLectura(true);
        tab_detalle.getColumna("NOMBRE_BENEFICIARIO").setLectura(true);
        tab_detalle.getColumna("TIPO_CUENTA").setLectura(true);
        tab_detalle.getColumna("BAN_CODIGO").setLectura(true);
        tab_detalle.getColumna("NUMERO_CUENTA").setLectura(true);
        tab_detalle.getColumna("BAN_NOMBRE").setVisible(false);
        tab_detalle.getColumna("ITEM").setLectura(true);
        tab_detalle.getColumna("COMPROBANTE").setLectura(true);
        tab_detalle.getColumna("VALOR").setLectura(true);
        tab_detalle.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "ENVIADO"
        };
        Object fila2[] = {
            "2", "PAGAR"
        };
        Object fila3[] = {
            "3", "DEVOLVER"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        tab_detalle.getColumna("ide_estado_detalle_listado").setRadio(lista, "1");
        tab_detalle.getColumna("BAN_CODIGO").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
                
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar(); 
        PanelTabla tdp = new PanelTabla();
        tdp.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tdp.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("guardarDetal");
         
        ItemMenu itm_cuenta = new ItemMenu();
        itm_cuenta.setValue("Confirmar Cuenta");
        itm_cuenta.setIcon("ui-icon-refresh");
        itm_cuenta.setMetodo("tipoCuenta");
        
        tdp.getMenuTabla().getChildren().add(itm_actualizar);
        tdp.getMenuTabla().getChildren().add(itm_cuenta);      
        tdp.setPanelTabla(tab_detalle);
        
        pan_opcion1.getChildren().add(tcp);
        pan_opcion1.getChildren().add(tdp);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pan_opcion1);
        pan_opcion.getChildren().add(gru);
        usuario();
     } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    public void usuario(){
     TablaGenerica tab_dato = programas.empleado();
     if (!tab_dato.isEmpty()) {
          tab_comprobante.setValor("responsable_paga", tab_dato.getValor("nombres"));
          tab_comprobante.setValor("ci_paga", tab_dato.getValor("cedula_pass"));
          utilitario.addUpdate("tab_comprobante");
          } else {
                 utilitario.agregarMensajeInfo("El Número de RUC O C.I. ingresado no existe en la base de datos del municipio", "");
                 }
    }
 
    public void buscarEstado(){
     if (utilitario.validarCedula(tab_detalle.getValor("cedula_pass_beneficiario"))) {
         TablaGenerica tab_dato = programas.empleado1(tab_detalle.getValor("cedula_pass_beneficiario"));
         if (!tab_dato.isEmpty()) {
              if(tab_detalle.getValor("cedula_pass_beneficiario").equals(tab_dato.getValor("cedula_pass"))){
                  utilitario.agregarMensajeInfo("TIPO BENEFICIARIO", "EMPLEADO");
                }else{
                      TablaGenerica tab_dato1 = programas.proveedor(tab_detalle.getValor("cedula_pass_beneficiario"));
                        if (!tab_dato1.isEmpty()) {
                             if(tab_detalle.getValor("cedula_pass_beneficiario").equals(tab_dato1.getValor("cedula_pass"))){
                                utilitario.agregarMensajeInfo("TIPO BENEFICIARIO", "PROVEEDOR");
                               }
                         }else {
                                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
                               }
                  }
            }else{
                  utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
                 }
     } else if (utilitario.validarRUC(tab_detalle.getValor("cedula_pass_beneficiario"))) {
                  utilitario.agregarMensajeInfo("TIPO BENEFICIARIO", "PROVEEDOR");
                } else {
                       utilitario.agregarMensajeError("El Número de Identificación no es válido", "");
                       }
    }
    
    
     //Metodo Para Guardar Pago
    public void guardarComp(){
       programas.actuListado(tab_comprobante.getValor("CI_PAGA"), tab_comprobante.getValor("RESPONSABLE_PAGA"), tab_consulta.getValor("NICK_USUA"), 
       Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
       utilitario.agregarMensaje("Datos Guardados Satisfactoriamente", "");
       utilitario.addUpdate("tab_comprobante");
    }
  
    public void guardarDetal(){
      TablaGenerica tab_dato = programas.busEstado(Integer.parseInt(tab_detalle.getValor("ide_detalle_listado")));
      if (!tab_dato.isEmpty()) {
           if(Integer.parseInt(tab_dato.getValor("ide_estado_detalle_listado"))!=2 && Integer.parseInt(tab_dato.getValor("ide_estado_detalle_listado"))!=3 ){
                  //Pago de comprobantes
                  if(Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado"))!=1 && Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado"))!=3 ){
                     programas.actualizarComprobante(tab_detalle.getValor("NUMERO_CUENTA"), Integer.parseInt(tab_detalle.getValor("BAN_CODIGO")), tab_detalle.getValor("TIPO_CUENTA"),
                     Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_LISTADO")), Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")), tab_consulta.getValor("NICK_USUA"),tab_detalle.getValor("num_transferencia"),Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado")));
                     pagado();
                     utilitario.addUpdate("tab_comprobante");
                     utilitario.addUpdate("tab_detalle");
                    }else if(Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado"))!=2 && Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado"))!=1 ){
                        //Devolucion de Comprobantes
                             guardarDevolucion ();
                             programas.actuComponente(tab_detalle.getValor("NUMERO_CUENTA"), Integer.parseInt(tab_detalle.getValor("BAN_CODIGO")), tab_detalle.getValor("TIPO_CUENTA"), 
                             Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_LISTADO")), Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")), tab_consulta.getValor("NICK_USUA"),tab_detalle.getValor("num_transferencia"),Integer.parseInt(tab_detalle.getValor("ide_estado_detalle_listado")));
                             estado();
                             utilitario.addUpdate("tab_comprobante");
                             utilitario.addUpdate("tab_detalle");
                            }
                            utilitario.agregarMensaje("Datos Guardados Satisfactoriamente", "");
           }else{
                 utilitario.agregarMensaje("Item Pagado o Devuelto,", "Eliga uno Enviado");
                }
        }else {
              utilitario.agregarMensajeInfo("Datos no disponibles", "");
             }
    }

 //Actualizacion tipo de cuenta
  public void tipoCuenta(){
       
    if (utilitario.validarCedula(tab_detalle.getValor("cedula_pass_beneficiario"))) {
       TablaGenerica tab_dato = programas.empleado1(tab_detalle.getValor("cedula_pass_beneficiario"));
         if (!tab_dato.isEmpty()) {
             tab_detalle.setValor("numero_cuenta", tab_dato.getValor("numero_cuenta"));
             tab_detalle.setValor("ban_codigo", tab_dato.getValor("cod_banco"));
             tab_detalle.setValor("tipo_cuenta", tab_dato.getValor("tipo_cuenta"));
             utilitario.addUpdate("tab_detalle");
         }else {
                utilitario.agregarMensajeInfo("Datos no disponibles", "");
            }
     } else if (utilitario.validarRUC(tab_detalle.getValor("cedula_pass_beneficiario"))) {
         TablaGenerica tab_dato = programas.proveedor(tab_detalle.getValor("cedula_pass_beneficiario"));
         if (!tab_dato.isEmpty()) {
             tab_detalle.setValor("numero_cuenta", tab_dato.getValor("numero_cuenta"));
             tab_detalle.setValor("ban_codigo", tab_dato.getValor("ban_codigo"));
             tab_detalle.setValor("tipo_cuenta", tab_dato.getValor("tipo_cuenta"));
             utilitario.addUpdate("tab_detalle");
         }else {
                utilitario.agregarMensajeInfo("Datos no disponibles", "");
            }
     } else  {
            utilitario.agregarMensajeError("El Número de Identificación no es válido", "");
            }
  }
   
  public void guardarDevolucion (){
          programas.actuLis(tab_comprobante.getValor("ci_paga"), tab_comprobante.getValor("RESPONSABLE_PAGA"), Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
          utilitario.addUpdate("tab_comprobante");
  }
   
  public void estado(){
        programas.actuLisDevol(Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
        utilitario.addUpdate("tab_comprobante");
//        tab_comprobante.actualizar();
  }
 
  public void pagado(){
        programas.actuLisDevolver(Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
        utilitario.addUpdate("tab_comprobante");
//        tab_comprobante.actualizar();
  }
  
  public void Pago(){
      tab_detalle.setValor("ide_estado_detalle_listado", "2");
      utilitario.addUpdate("tab_detalle");
  }
  //Llamado a reportes
   @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
               case "LISTADO POR TIPO":
                dia_reporte.Limpiar();
                dia_reporte.setDialogo(gridre);
                grid_re.getChildren().add(new Etiqueta("FECHA DESDE :"));
                grid_re.getChildren().add(cal_inicial);
                cal_inicial.setFechaActual();
                grid_re.getChildren().add(new Etiqueta("FECHA HASTA :"));
                grid_re.getChildren().add(cal_final);
                cal_final.setFechaActual();
                grid_re.getChildren().add(new Etiqueta("TIPO DE COMPROBANTE:"));
                grid_re.getChildren().add(cmb_tipos);
                dia_reporte.setDialogo(grid_re);
                dia_reporte.dibujar();
               break;
               case "POR NUMERO LISTADO":
                dia_reporte1.Limpiar();
                dia_reporte1.setDialogo(gridre1);
                txt_buscar.setSize(7);
                grid_re1.getChildren().add(new Etiqueta("LISTADO # :"));
                grid_re1.getChildren().add(txt_buscar1);
                dia_reporte1.setDialogo(grid_re1);
                dia_reporte1.dibujar();
               break;
        }
    }    
    
 public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "LISTADO POR TIPO":
                      p_parametros = new HashMap();
                      p_parametros.put("tipo", Integer.parseInt(cmb_tipos.getValue()+""));
                      p_parametros.put("pide_fechai", cal_inicial.getFecha());
                      p_parametros.put("pide_fechaf", cal_final.getFecha());
                      p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;
               case "POR NUMERO LISTADO":
                      p_parametros = new HashMap();
                      p_parametros.put("tipo", Integer.parseInt(txt_buscar1.getValue()+""));
                      p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
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
     if (tab_comprobante.guardar()) {
          if (tab_detalle.guardar()) {
              con_postgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    }

    @Override
    public void siguiente() {
        super.siguiente(); //To change body of generated methods, choose Tools | Templates.
    buscarEstado();
    }

    @Override
    public void atras() {
        super.atras(); //To change body of generated methods, choose Tools | Templates.
    buscarEstado();
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    public Tabla getTab_comprobante() {
        return tab_comprobante;
    }

    public void setTab_comprobante(Tabla tab_comprobante) {
        this.tab_comprobante = tab_comprobante;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public SeleccionTabla getSet_comprobante() {
        return set_comprobante;
    }

    public void setSet_comprobante(SeleccionTabla set_comprobante) {
        this.set_comprobante = set_comprobante;
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
    
}