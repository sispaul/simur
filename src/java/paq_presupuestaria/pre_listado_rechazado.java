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
 * @author p-sistemas
 */
public class pre_listado_rechazado extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla detalle = new Tabla();
    private Tabla detalle1 = new Tabla();//rechazadas
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    String num_listado;
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//numero de tranferencia
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    
    //Calendario
    private Calendario cal_fecha = new Calendario();
    private Calendario cal_fecha1 = new Calendario();
    
    //Combo
    private Combo cmb_estado = new Combo();
    
    //dialogo para reporte
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid grid_d = new Grid();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_listado_rechazado() {
        
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
        
        cmb_estado.setId("cmb_esatdo");
        cmb_estado.setConexion(con_postgres);
        cmb_estado.setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado where ide_estado_listado BETWEEN 2 and 3");
        
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
        aut_busca.setAutoCompletar("SELECT DISTINCT on (d.num_documento) t.ide_listado,d.num_documento\n" +
                                    "FROM tes_comprobante_pago_listado t,tes_detalle_comprobante_pago_listado d\n" +
                                    "where d.ide_listado = t.ide_listado and\n" +
                                    "d.ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO')\n" +
                                    "order by d.num_documento");
        aut_busca.setSize(100);
//        bar_botones.agregarComponente(new Etiqueta("Busca Listado:"));
//        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
//        bar_botones.agregarBoton(bot_limpiar);
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        agregarComponente(pan_opcion);
        
        //Busqueda de comprobantes
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        txt_buscar.setSize(20);
        gri_busca1.getChildren().add(new Etiqueta("ELEGIR FECHA :"));
        gri_busca1.getChildren().add(cal_fecha);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca1.getChildren().add(bot_buscar);
        
        set_comprobante.setId("set_comprobante");
        set_comprobante.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_comprobante.setSeleccionTabla("SELECT DISTINCT on (num_documento) ide_detalle_listado,num_documento\n" +
                                            "FROM tes_detalle_comprobante_pago_listado where ide_detalle_listado=-1 order by num_documento", "ide_detalle_listado");
        set_comprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_comprobante.getTab_seleccion().setRows(10);
        set_comprobante.setRadio();
        set_comprobante.setWidth("40%");
        set_comprobante.setHeight("50%");
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
        
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("SELECCIONES PARAMETROS PARA REPORTE"); //titulo
        dia_dialogo.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("25%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoAnticipo");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
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
    
    public void abrirBusqueda(){
      set_comprobante.dibujar();
      txt_buscar.limpiar();
      set_comprobante.getTab_seleccion().limpiar();
      limpiarPanel();
      detalle.limpiar();
      detalle1.limpiar();
    }
    
    public void buscarEntrega() {
      if (cal_fecha.getValue() != null && cal_fecha.getValue().toString().isEmpty() == false) {
                 set_comprobante.getTab_seleccion().setSql("SELECT DISTINCT on (num_documento) ide_detalle_listado,num_documento\n" +
                                                            "FROM tes_detalle_comprobante_pago_listado\n" +
                                                            "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') AND fecha_transferencia ='"+ cal_fecha.getFecha()+"' order by num_documento");
                 set_comprobante.getTab_seleccion().ejecutarSql();
                 limpiar();
          } else {
                 utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
                }
    }
    
    public void aceptarBusqueda() {
        limpiarPanel();
        if (set_comprobante.getValorSeleccionado() != null) {
            TablaGenerica tab_dato = programas.getTranferencia(Integer.parseInt(set_comprobante.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                aut_busca.setValor(tab_dato.getValor("ide_listado"));
                set_comprobante.cerrar();
                num_listado = tab_dato.getValor("num_documento");
                dibujarLista();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
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
//        pan_opcion1.getChildren().clear();
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
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tab_comprobante);
        //tabla detalle
        detalle.setId("detalle");
        detalle.setConexion(con_postgres);
        detalle.setSql("SELECT  \n" +
                        "d.ide_detalle_listado,  \n" +
                        "d.ide_listado,  \n" +
                        "d.item,  \n" +
                        "d.comprobante,  \n" +
                        "d.cedula_pass_beneficiario,  \n" +
                        "d.nombre_beneficiario,  \n" +
                        "d.valor,  \n" +
                        "d.num_documento,  \n" +
                        "null AS observacion ,\n" +
                        "null as comentario_transaccion \n" +
                        "FROM     \n" +
                        "tes_detalle_comprobante_pago_listado AS d  \n" +
                        "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') and num_transferencia is null and d.num_documento like '"+num_listado+"'");
        detalle.getColumna("ide_detalle_listado").setVisible(false);
        List list = new ArrayList();
        Object fil1[] = {
            "1", "Rechazado"
        };
        Object fil2[] = {
            "2", "Aceptado"
        };
        list.add(fil1);;
        list.add(fil2);;
        detalle.getColumna("observacion").setRadio(list, "");
        detalle.getColumna("item").setVisible(false);
        detalle.getColumna("ide_listado").setVisible(false);
        detalle.setRows(5);
        detalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setMensajeWarn("LISTADO DE TRANSACCIONES PAGADAS SIN COMPROBANTE");
        tdd.setPanelTabla(detalle);
        
        //tabla rechazadas
        detalle1.setId("detalle1");
        detalle1.setConexion(con_postgres);
        detalle1.setSql("SELECT    \n" +
                " d.ide_detalle_listado,    \n" +
                " d.ide_listado,    \n" +
                " d.item,    \n" +
                " d.comprobante,    \n" +
                " d.cedula_pass_beneficiario,    \n" +
                " d.nombre_beneficiario,    \n" +
                " d.valor,    \n" +
                " d.num_documento,    \n" +
                " d.comentario,    \n" +
                " null AS pagar   \n" +
                " FROM       \n" +
                " tes_detalle_comprobante_pago_listado AS d    \n" +
                " where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'RECHAZADO') and num_documento = '"+num_listado+"'");
        detalle1.getColumna("ide_detalle_listado").setVisible(false);
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };

        lista.add(fila1);;
        detalle1.getColumna("pagar").setRadio(lista, "");
        detalle1.getColumna("item").setVisible(false);
        detalle1.getColumna("ide_listado").setVisible(false);
        
        detalle1.setRows(5);
        detalle1.dibujar();
        PanelTabla tdr = new PanelTabla();
        tdr.setMensajeWarn("LISTADO DE TRANSACCIONES RECHAZADAS");
        tdr.setPanelTabla(detalle1);
        cal_fecha.setDisabled(true); //Desactiva el cuadro de texto
        Grupo gru = new Grupo();
        pan_opcion.getChildren().add(gru);
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Pagar Pedientes");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("regresa");
        
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tdd, gri_busca, tdr, "35%", "58%", "H");
        
        pan_opcion.getChildren().add(div_division);
             } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        for (int i = 0; i < detalle.getTotalFilas(); i++) { 
            if(detalle.getValor(i, "observacion")!=null){
                if(detalle.getValor(i, "observacion").equals("1")){
                    programas.rechazoComprobante(detalle.getValor(i, "num_documento"), detalle.getValor(i, "comprobante"), Integer.parseInt(detalle.getValor(i, "ide_listado")),detalle.getValor(i, "comentario_transaccion"))
                            ;
                    programas.insertRegistro(Integer.parseInt(detalle.getValor(i, "ide_listado")), Integer.parseInt(detalle.getValor(i, "item")), Integer.parseInt(detalle.getValor(i, "ide_detalle_listado")), detalle.getValor(i, "comprobante"));
                }else {
                    if(detalle.getValor(i, "comentario_transaccion")!=null && detalle.getValor(i, "comentario_transaccion").toString().isEmpty() == false){
                        programas.numTransferencia(detalle.getValor(i, "num_documento"), detalle.getValor(i, "comprobante"), Integer.parseInt(detalle.getValor(i, "ide_listado")),detalle.getValor(i, "comentario_transaccion"));
                        
                        programas.insertRegistro(Integer.parseInt(detalle.getValor(i, "ide_listado")), Integer.parseInt(detalle.getValor("item")), Integer.parseInt(detalle.getValor(i, "ide_detalle_listado")), detalle.getValor(i, "comprobante"));
                    }
                }
            }
        }
        detalle.actualizar();
        detalle1.actualizar();
    }

    public void regresa(){
        for (int i = 0; i < detalle1.getTotalFilas(); i++) { 
            if(detalle1.getValor(i, "pagar")!=null){
                if(detalle1.getValor(i, "pagar").equals("1")){
                    programas.regresoRechazo(detalle1.getValor(i, "num_documento"), detalle1.getValor(i, "comprobante"), Integer.parseInt(detalle1.getValor(i, "ide_listado")));
                }
            }
        }
        detalle1.actualizar();
        utilitario.agregarMensaje("Comprobante", "Listo para Registrase");
        detalle.actualizar();
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
            case "CONFIRMACION DE ACREDITACION":
                 set_lista.dibujar();
                set_lista.getTab_seleccion().limpiar();
                break;
           case "COMPROBANTES POR ESTADO":
                dia_dialogo.Limpiar();
                grid.getChildren().add(new Etiqueta("Seleccione Fecha :"));
                grid.getChildren().add(cal_fecha1);
                grid.getChildren().add(new Etiqueta("Seleccione Estado :"));
                grid.getChildren().add(cmb_estado);
                dia_dialogo.setDialogo(grid);
                dia_dialogo.dibujar();
                break;
        }
    }
        public void aceptoAnticipo(){
            switch (rep_reporte.getNombre()) {
                case "CONFIRMACION DE ACREDITACION":
                    TablaGenerica tab_dato = programas.getTranferencia(Integer.parseInt(set_lista.getValorSeleccionado()));
                    if (!tab_dato.isEmpty()) {
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                        p_parametros.put("fecha_acre", cal_fecha1.getFecha()+"");
                        p_parametros.put("num_tran", tab_dato.getValor("num_documento")+"");
                        rep_reporte.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case"COMPROBANTES POR ESTADO":
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                        p_parametros.put("fecha_acre", cal_fecha1.getFecha()+"");
                        p_parametros.put("estado", Integer.parseInt(cmb_estado.getValue()+""));
                        rep_reporte.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getDetalle() {
        return detalle;
    }

    public void setDetalle(Tabla detalle) {
        this.detalle = detalle;
    }

    public Tabla getDetalle1() {
        return detalle1;
    }

    public void setDetalle1(Tabla detalle1) {
        this.detalle1 = detalle1;
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

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
    }
    
}
