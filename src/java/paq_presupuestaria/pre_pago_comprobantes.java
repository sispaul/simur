/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
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
import java.util.HashMap;
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
    private Calendario cal_fecha1 = new Calendario();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_pago_comprobantes() {
        
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
        set_lista.setSeleccionTabla("SELECT DISTINCT on (num_transferencia)ide_detalle_listado,num_transferencia FROM tes_detalle_comprobante_pago_listado WHERE ide_detalle_listado=-1  order by num_transferencia", "ide_detalle_listado");
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
             aut_busca.setValor(set_comprobante.getValorSeleccionado());
             set_comprobante.cerrar();
//             dibujarLista();
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
            set_lista.getTab_seleccion().setSql("SELECT DISTINCT on (num_transferencia)ide_detalle_listado,num_transferencia FROM tes_detalle_comprobante_pago_listado where fecha_transferencia='"+cal_fecha1.getFecha()+"' order by num_transferencia");
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
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(tab_detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tab_comprobante);
        pan_opcion.getChildren().add(tcp);
        usuario();
             } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
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
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        TablaGenerica tab_dato1 = programas.item(Integer.parseInt(set_comprobante.getValorSeleccionado()));
          if (!tab_dato1.isEmpty()) {
              programas.actuListado(tab_comprobante.getValor("CI_PAGA"), tab_comprobante.getValor("RESPONSABLE_PAGA"), tab_consulta.getValor("NICK_USUA"), 
            Integer.parseInt(tab_comprobante.getValor("IDE_LISTADO")));
          }
          
    }

    @Override
    public void eliminar() {
    }
    
}
