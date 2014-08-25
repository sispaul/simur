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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_pagos_listados extends Pantalla{

   //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_detalle1 = new Tabla();
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//cabecera
    private Panel pan_opcion1 = new Panel();//detalle
    private Panel pan_opcion2 = new Panel();//componentes
    private Panel pan_opcion3 = new Panel();//listado
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    private Texto txt_buscar1 = new Texto();
    private Texto txt_num_listado = new Texto();
    
    private Calendario cal_fecha = new Calendario();
    
        //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
        
    public pre_pagos_listados() {
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento1.png");
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
        gri_busca1.getChildren().add(new Etiqueta("LISTADO # :"));
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
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(tab_detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tab_comprobante);
        
        //tabla detalle
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setSql("SELECT\n" +
                            "ide_detalle_listado,\n" +
                            "ide_listado,\n" +
                            "item,\n" +
                            "comprobante,\n" +
                            "cedula_pass_beneficiario,\n" +
                            "nombre_beneficiario,\n" +
                            "valor,\n" +
                            "usuario_ingre_envia,\n" +
                            "usuario_actua_envia,\n" +
                            "ip_ingre_envia,\n" +
                            "ip_actua_envia,\n" +
                            "numero_cuenta,\n" +
                            "ban_codigo,\n" +
                            "ban_nombre,\n" +
                            "tipo_cuenta,\n" +
                            "usuario_actua_pagado,\n" +
                            "ip_actua_pagado,\n" +
                            "usuario_actua_devolucion,\n" +
                            "ip_actua_devolucion,\n" +
                            "ide_estado_listado\n" +
                            "FROM\n" +
                            "tes_detalle_comprobante_pago_listado");
        tab_detalle.setCampoPrimaria("ide_detalle_listado");
        tab_detalle.setCampoOrden("ide_listado");
        List lista = new ArrayList();
        Object fila2[] = {
            "2", "PAGAR"
        };
        Object fila3[] = {
            "3", "DEVOLVER"
        };
        lista.add(fila2);;
        lista.add(fila3);;
        tab_detalle.getColumna("ide_estado_listado").setRadio(lista, " ");
        
        tab_detalle.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle.getColumna("item").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_detalle.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_detalle.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_detalle.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        tab_detalle.setRows(5);
        tab_detalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(tab_detalle);

        //tabla de detalle comprobantes a pagar
        tab_detalle1.setId("tab_detalle1");
        tab_detalle1.setConexion(con_postgres);
        tab_detalle1.setSql("SELECT\n" +
                            "ide_detalle_listado,\n" +
                            "ide_listado,\n" +
                            "item,\n" +
                            "comprobante,\n" +
                            "cedula_pass_beneficiario,\n" +
                            "nombre_beneficiario,\n" +
                            "valor,\n" +
                            "usuario_ingre_envia,\n" +
                            "usuario_actua_envia,\n" +
                            "ip_ingre_envia,\n" +
                            "ip_actua_envia,\n" +
                            "numero_cuenta,\n" +
                            "ban_codigo,\n" +
                            "ban_nombre,\n" +
                            "tipo_cuenta,\n" +
                            "usuario_actua_pagado,\n" +
                            "ip_actua_pagado,\n" +
                            "usuario_actua_devolucion,\n" +
                            "ip_actua_devolucion,\n" +
                            "ide_estado_listado\n" +
                            "FROM\n" +
                            "tes_detalle_comprobante_pago_listado");
        tab_detalle1.setCampoPrimaria("ide_detalle_listado");
        tab_detalle1.setCampoOrden("ide_listado");
        tab_detalle1.getColumna("ide_detalle_listado").setVisible(false);
        tab_detalle1.getColumna("ide_listado").setVisible(false);
        tab_detalle1.getColumna("item").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_detalle1.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_detalle1.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_detalle1.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_detalle1.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        tab_detalle1.setRows(5);
        tab_detalle1.dibujar();
        PanelTabla tda = new PanelTabla();
        tda.setPanelTabla(tab_detalle1);
        
        Division div = new Division();
        div.dividir2(tcp, tdd, "42%", "h");
        
        Grupo gru = new Grupo();
        pan_opcion.getChildren().add(gru);
        txt_num_listado.setId("txt_num_listado");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        gri_busca.getChildren().add(new Etiqueta("Fecha : ")); 
        cal_fecha.setFechaActual();
        gri_busca.getChildren().add(cal_fecha);
        gri_busca.getChildren().add(new Etiqueta("# Transferencia : "));    
        gri_busca.getChildren().add(txt_num_listado);
        
        Boton bot_save = new Boton();
        bot_save.setValue("Guardar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("save_lista");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Quitar de Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("devolver");
        
        gri_busca.getChildren().add(bot_save);
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(div, gri_busca, tda, "48%", "45%", "H");
        pan_opcion.getChildren().add(div_division);
        usuario(Integer.parseInt(tab_comprobante.getValor("ide_listado")));
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    public void usuario(Integer valor){
      TablaGenerica tab_dato1 = programas.item(Integer.parseInt(tab_detalle.getValor("cedula_pass_beneficiario")));
          if (!tab_dato1.isEmpty()) {
                    System.err.println("Holas");
      }else{
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
    }

    @Override
    public void eliminar() {
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public SeleccionTabla getSet_comprobante() {
        return set_comprobante;
    }

    public void setSet_comprobante(SeleccionTabla set_comprobante) {
        this.set_comprobante = set_comprobante;
    }
    
}
