/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
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
 * @author KEJA
 */
public class pre_pago_comprobantes extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_detalle1 = new Tabla();
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//cabecera
    private Panel pan_opcion1 = new Panel();//detalle
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    private Texto txt_num_listado = new Texto();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Calendario
    private Calendario cal_fecha = new Calendario();
    private Calendario cal_fecha1= new Calendario();
    private Calendario cal_fecha2 = new Calendario();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_pago_comprobantes() {
        
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_financiero.png");
        agregarComponente(quinde);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        //Creación de Botones; Busqueda,Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        Boton bot_select = new Boton();
        bot_select.setValue("seleccion");
        bot_select.setIcon("ui-icon-check");
        bot_select.setMetodo("seleccion");
        bar_botones.agregarBoton(bot_select);
        
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
        pan_opcion.setHeader(" COMPROBANTE DE PAGOS POR LISTADOS ");
        agregarComponente(pan_opcion);
        
        //Busqueda de comprobantes
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        txt_buscar.setSize(20);
        gri_busca1.getChildren().add(new Etiqueta("# ITEM :"));
        gri_busca1.getChildren().add(txt_buscar);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca1.getChildren().add(bot_buscar);
        
        set_comprobante.setId("set_comprobante");
        set_comprobante.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_comprobante.setSeleccionTabla("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado FROM tes_comprobante_pago_listado where IDE_LISTADO=-1", "IDE_LISTADO");
        set_comprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_comprobante.getTab_seleccion().setRows(5);
        set_comprobante.setRadio();
        set_comprobante.setWidth("40%");
        set_comprobante.setHeight("30%");
        set_comprobante.getGri_cuerpo().setHeader(gri_busca1);
        set_comprobante.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_comprobante.setHeader("BUSCAR LISTADO O ITEM  A PAGAR");
        agregarComponente(set_comprobante);
        
        //CONFIGURACIÓN DE OBJETO REPORTE
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("FECHA: "));
        gru_lis.getChildren().add(cal_fecha1);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);
        
        set_lista.setId("set_lista");
        set_lista.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_lista.setSeleccionTabla("SELECT DISTINCT on (num_documento)ide_detalle_listado,num_documento FROM tes_detalle_comprobante_pago_listado WHERE ide_detalle_listado=-1  order by num_documento", "ide_detalle_listado");
        set_lista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_lista.getTab_seleccion().setRows(10);
        set_lista.setRadio();
        set_lista.setWidth("20%");
        set_lista.setHeight("40%");
        set_lista.getGri_cuerpo().setHeader(gru_lis);
        set_lista.getBot_aceptar().setMetodo("aceptoAnticipo");
        set_lista.setHeader("SELECCIONE LISTADO");
        agregarComponente(set_lista);
    }

    //Buscar Listado de Comprobantes mediante # de listado "ITEM"
    public void abrirBusqueda(){
      set_comprobante.dibujar();
      txt_buscar.limpiar();
      set_comprobante.getTab_seleccion().limpiar();
      limpiarPanel();
    }
    
    public void buscarEntrega() {
      if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
                 set_comprobante.getTab_seleccion().setSql("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado \n" +
                                                            "FROM tes_comprobante_pago_listado WHERE item =" + txt_buscar.getValue());
                 set_comprobante.getTab_seleccion().ejecutarSql();
                 limpiar();
          } else {
                 utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
                }
    }
    
    public void aceptarBusqueda() {
        limpiarPanel();
      if (set_comprobante.getValorSeleccionado() != null) {
          programas.actualizarComprobante(Integer.parseInt(set_comprobante.getValorSeleccionado()));
             aut_busca.setValor(set_comprobante.getValorSeleccionado());
             set_comprobante.cerrar();
             dibujarLista();
             utilitario.addUpdate("aut_busca,pan_opcion");
         } else {
                utilitario.agregarMensajeInfo("Debe seleccionar un listado", "");
                }
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
    
    //busqueda de documento creado para pago de comprobantes
    public void buscarColumna() {
        if (cal_fecha1.getValue() != null && cal_fecha1.getValue().toString().isEmpty() == false ) {
            set_lista.getTab_seleccion().setSql("SELECT DISTINCT on (num_documento)ide_detalle_listado,num_documento FROM tes_detalle_comprobante_pago_listado where fecha_transferencia='"+cal_fecha1.getFecha()+"' order by num_documento");
            set_lista.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    //Para Pagos de Comprobantes    
    public void dibujarLista(){
        txt_num_listado.setDisabled(true); //Desactiva el cuadro de texto
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
//        tab_comprobante.getColumna("devolucion").setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado");
        tab_comprobante.getColumna("devolucion").setVisible(true);
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(tab_detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tcp.setPanelTabla(tab_comprobante);
        
        //tabla detalle
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setSql("SELECT \n" +
                            "d.ide_detalle_listado,\n" +
                            "d.ide_listado,  \n" +
                            "d.comprobante, \n" +
                            "d.cedula_pass_beneficiario, \n" +
                            "d.nombre_beneficiario, \n" +
                            "d.valor,  \n" +
                            "d.numero_cuenta, \n" +
                            "d.codigo_banco, \n" +
                            "d.ban_nombre, \n" +
                            "d.tipo_cuenta, \n" +
                            "null as proceso \n" +
                            "FROM \n" +
                            "tes_detalle_comprobante_pago_listado AS d \n" +
                            "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'ENVIADO') and item =" + txt_buscar.getValue());
        tab_detalle.setCampoPrimaria("ide_detalle_listado");
        tab_detalle.setCampoOrden("ide_listado");
        List lista = new ArrayList();
        Object fila2[] = {
            "2", "ACREDITAR"
        };
        Object fila3[] = {
            "3", "DEVOLVER"
        };
        lista.add(fila2);;
        lista.add(fila3);;
        tab_detalle.getColumna("proceso").setRadio(lista, " ");
        tab_detalle.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle.getColumna("cedula_pass_beneficiario").setLongitud(20);
        tab_detalle.getColumna("nombre_beneficiario").setLongitud(20);
        tab_detalle.getColumna("numero_cuenta").setLongitud(5);
        tab_detalle.getColumna("ban_nombre").setLongitud(5);
        tab_detalle.setRows(5);
        tab_detalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(tab_detalle);
        
//        ItemMenu itm_actualizar = new ItemMenu();
//        itm_actualizar.setValue("Actualizar Banco");
//        itm_actualizar.setIcon("ui-icon-arrow-4-diag");
//        itm_actualizar.setMetodo("tipoCuenta");
//        tdd.getMenuTabla().getChildren().add(itm_actualizar);
//        
//        tdd.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
//        tdd.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        Division div = new Division();
        div.dividir2(tcp, tdd, "42%", "h");
        
        //lista de con comprobantes que van hacer pagados
        tab_detalle1.setId("tab_detalle1");
        tab_detalle1.setConexion(con_postgres);
        tab_detalle1.setSql("SELECT  \n" +
                        " d.ide_detalle_listado,  \n" +
                        " d.ide_listado,  \n" +
                        " d.item,  \n" +
                        " d.comprobante,  \n" +
                        " d.cedula_pass_beneficiario,  \n" +
                        " d.nombre_beneficiario,  \n" +
                        " d.valor,  \n" +
                        " d.numero_cuenta,  \n" +
                        " d.ban_nombre,  \n" +
                        " d.tipo_cuenta,  \n" +
                        " null as regresar  \n" +
                        " FROM  \n" +
                        " tes_detalle_comprobante_pago_listado AS d  \n" +
                        " where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') and num_documento is null and item ="+tab_comprobante.getValor("item"));
        tab_detalle1.setCampoPrimaria("ide_detalle_listado");
        tab_detalle1.setCampoOrden("ide_listado");
        List list = new ArrayList();
        Object fil1[] = {
            "1", " "
        };
        list.add(fil1);;
        tab_detalle1.getColumna("regresar").setRadio(list, " ");
        tab_detalle1.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle1.getColumna("ide_listado").setVisible(false);
        tab_detalle1.getColumna("item").setVisible(false);
        tab_detalle1.getColumna("cedula_pass_beneficiario").setLongitud(20);
        tab_detalle1.getColumna("nombre_beneficiario").setLongitud(20);
        tab_detalle1.getColumna("numero_cuenta").setLongitud(5);
        tab_detalle1.getColumna("ban_nombre").setLongitud(5);
        
        tab_detalle1.setRows(5);
        tab_detalle1.dibujar();
        PanelTabla tda = new PanelTabla();
        tda.setPanelTabla(tab_detalle1);
        
        cal_fecha.setDisabled(true); //Desactiva el cuadro de texto
        txt_num_listado.setId("txt_num_listado");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        gri_busca.getChildren().add(new Etiqueta("Fecha : ")); 
        cal_fecha.setFechaActual();
        gri_busca.getChildren().add(cal_fecha);
        gri_busca.getChildren().add(new Etiqueta("# Documento : "));    
        gri_busca.getChildren().add(txt_num_listado);
        
        Boton bot_save = new Boton();
        bot_save.setValue("Guardar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("estadoComp");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Quitar de Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("regresa");
        
        gri_busca.getChildren().add(bot_save);
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(div, gri_busca, tda, "48%", "45%", "H");
        pan_opcion.getChildren().add(div_division);
        usuario();
             } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    //selecciona todo.
    public void seleccion(){
        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            if(tab_detalle.getValor(i, "proceso")!=null){
                tab_detalle.setValor(i, "proceso", " ");
                utilitario.addUpdate("tab_detalle");
            }else{
                tab_detalle.setValor(i, "proceso", "2");
                utilitario.addUpdate("tab_detalle");
            }
        }
    }
    
    //Carga el usuario que acredita el pago
    public void usuario(){
      TablaGenerica tab_dato1 = programas.item(Integer.parseInt(set_comprobante.getValorSeleccionado()));
          if (!tab_dato1.isEmpty()) {
              TablaGenerica tab_dato = programas.empleado();
              if (!tab_dato.isEmpty()) {
                   tab_comprobante.setValor("responsable_paga", tab_dato.getValor("nombres"));
                   tab_comprobante.setValor("ci_paga", tab_dato.getValor("cedula_pass"));
                   utilitario.addUpdate("tab_comprobante");
              } else {
                  utilitario.agregarMensajeInfo("El Número de RUC O C.I. ingresado no existe en la base de datos del municipio", "");
              }
          }
    }
    
    //permite devolver comprobnate en caso de no acrditar con esa lista..
    public void regresa(){
        for (int i = 0; i < tab_detalle1.getTotalFilas(); i++) {
            if(tab_detalle1.getValor(i, "regresar")!=null){
                programas.regreComprobante(tab_detalle1.getValor(i, "numero_cuenta"),utilitario.getVariable("NICK"),tab_detalle1.getValor(i, "comprobante"),
                        Integer.parseInt(tab_detalle1.getValor(i, "ide_listado")),Integer.parseInt(tab_detalle1.getValor(i, "ide_detalle_listado")));
            }
        }
        tab_detalle1.actualizar();
        utilitario.agregarMensaje("Comprobante", "Regreso a Listado");
        tab_detalle.actualizar();
    }
    
    public void estadoComp(){
        for (int i = 0; i < tab_detalle1.getTotalFilas(); i++) {
            programas.ActuaListaComp(tab_detalle1.getValor(i, "comprobante"));
        }
        save_lista();
    }
    
    //Genera numero aleatorio
    public void save_lista(){
        txt_num_listado.setDisabled(true); //Desactiva el cuadro de texto
        String numero = programas.listaMax();
        String valor,anio,num;
        Integer cantidad=0;
        anio=String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
        valor=numero.substring(10,15);
        cantidad = Integer.parseInt(valor)+1;
        if(numero!=null){
            if(cantidad>=0 && cantidad<=9){
                num = "0000"+String.valueOf(cantidad);
                String cadena = "LIST"+"-"+anio+"-"+num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
               } else if(cantidad>=10 && cantidad<=99){
                          num = "000"+String.valueOf(cantidad);
                          String cadena = "LIST"+"-"+anio+"-"+num;
                        txt_num_listado.setValue(cadena + "");
                        utilitario.addUpdate("txt_num_listado");
                         }else if(cantidad>=100 && cantidad<=999){
                                   num = "00"+String.valueOf(cantidad);
                                   String cadena = "LIST"+"-"+anio+"-"+num;
                                    txt_num_listado.setValue(cadena + "");
                                    utilitario.addUpdate("txt_num_listado");
                                  }else if(cantidad>=1000 && cantidad<=9999){
                                            num = "0"+String.valueOf(cantidad);
                                            String cadena = "LIST"+"-"+anio+"-"+num;
                                            txt_num_listado.setValue(cadena + "");
                                            utilitario.addUpdate("txt_num_listado");
                                           }else if(cantidad>=10000 && cantidad<=99999){
                                                     num = String.valueOf(cantidad);
                                                     String cadena = "LIST"+"-"+anio+"-"+num;
                                                    txt_num_listado.setValue(cadena + "");
                                                    utilitario.addUpdate("txt_num_listado");
                                                    }
        }
        save_listado();
    }
    
    //cuarda documento con el cual se acreditara los comprobantes
    public void save_listado(){
        for (int i = 0; i < tab_detalle1.getTotalFilas(); i++) {
            programas.numTransComprobante(txt_num_listado.getValue()+"", cal_fecha.getFecha(), tab_detalle1.getValor(i, "comprobante"),Integer.parseInt(tab_detalle1.getValor(i, "ide_listado"))
                    ,Integer.parseInt(tab_detalle1.getValor(i, "ide_detalle_listado")));
        }
        utilitario.agregarMensaje("Comprobante", "Generado");
        tab_detalle1.actualizar();
//        TablaGenerica tab_dato1 = programas.Pagos_lista(Integer.parseInt(tab_comprobante.getValor("ide_listado")), Integer.parseInt(tab_comprobante.getValor("item")));
//        if (!tab_dato1.isEmpty()) {
//        }else{
//            programas.actuLisDevolver(Integer.parseInt(tab_comprobante.getValor("ide_listado")), Integer.parseInt(tab_comprobante.getValor("item")));
//            utilitario.addUpdate("tab_comprobante");
//        }
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        TablaGenerica tab_dato1 = programas.item(Integer.parseInt(tab_comprobante.getValor("ide_listado")));
          if (!tab_dato1.isEmpty()) {
              programas.actuListado(tab_comprobante.getValor("CI_PAGA"), tab_comprobante.getValor("RESPONSABLE_PAGA"), tab_consulta.getValor("NICK_USUA"), 
            Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
              for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                  if(tab_detalle.getValor(i, "proceso")!=null){
                      if(tab_detalle.getValor(i, "proceso").equals("2")){
                          programas.actuaComprobante(tab_detalle.getValor(i, "numero_cuenta"),tab_detalle.getValor(i, "ban_nombre"),
                        tab_detalle.getValor(i, "tipo_cuenta"),  utilitario.getVariable("NICK"),tab_detalle.getValor(i, "comprobante"),Integer.parseInt(tab_detalle.getValor(i, "ide_listado")),Integer.parseInt(tab_detalle.getValor(i, "ide_detalle_listado")));
                      }else{
                          programas.devolverComprobante(utilitario.getVariable("NICK"), tab_detalle.getValor(i, "comprobante"), Integer.parseInt(tab_detalle.getValor(i, "ide_listado")), Integer.parseInt(tab_detalle.getValor(i, "ide_detalle_listado")), Integer.parseInt(tab_comprobante.getValor("item")));
                          programas.devolverLista(tab_comprobante.getValor("ci_envia"), Integer.parseInt(tab_detalle.getValor(i, "ide_listado")),Integer.parseInt(tab_comprobante.getValor("item")));
                          utilitario.addUpdate("tab_comprobante");
                      }
                  }
            }
          }else{
              for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                  if(tab_detalle.getValor(i, "proceso")!=null){
                      if(tab_detalle.getValor(i, "proceso").equals("2")){
                          programas.actuaComprobante(tab_detalle.getValor(i, "numero_cuenta"),tab_detalle.getValor(i, "ban_nombre"),
                        tab_detalle.getValor(i, "tipo_cuenta"),  utilitario.getVariable("NICK"),tab_detalle.getValor(i, "comprobante"),Integer.parseInt(tab_detalle.getValor(i, "ide_listado")),Integer.parseInt(tab_detalle.getValor(i, "ide_detalle_listado")));
                      }else{
                          programas.devolverComprobante(utilitario.getVariable("NICK"), tab_detalle.getValor(i, "comprobante"), Integer.parseInt(tab_detalle.getValor(i, "ide_listado")), Integer.parseInt(tab_detalle.getValor(i, "ide_detalle_listado")), Integer.parseInt(tab_comprobante.getValor("item")));
                          programas.devolverLista(tab_comprobante.getValor("ci_envia"), Integer.parseInt(tab_detalle.getValor(i, "ide_listado")),Integer.parseInt(tab_comprobante.getValor("item")));
                          utilitario.addUpdate("tab_comprobante");
                      }
                  }
              }
          }
                tab_detalle.actualizar();
                tab_detalle1.actualizar();
    }

    @Override
    public void eliminar() {
    }

    /*CREACION DE REPORTES */
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
        @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fecha1.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "LISTA DE ACREDITACION":
                 set_lista.dibujar();
                set_lista.getTab_seleccion().limpiar();
          break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "LISTA DE ACREDITACION":
                    TablaGenerica tab_dato = programas.getTranferencia(Integer.parseInt(set_lista.getValorSeleccionado()));
               if (!tab_dato.isEmpty()) {
//                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("fecha_acre", cal_fecha1.getFecha()+"");
                    p_parametros.put("num_tran", tab_dato.getValor("num_documento")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;
        }
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_comprobante() {
        return tab_comprobante;
    }

    public void setTab_comprobante(Tabla tab_comprobante) {
        this.tab_comprobante = tab_comprobante;
    }

    public SeleccionTabla getSet_comprobante() {
        return set_comprobante;
    }

    public void setSet_comprobante(SeleccionTabla set_comprobante) {
        this.set_comprobante = set_comprobante;
    }

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
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
