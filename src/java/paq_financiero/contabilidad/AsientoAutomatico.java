/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class AsientoAutomatico extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabTabla = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private SeleccionTabla setRol = new SeleccionTabla();
    private Combo cmbDistributivo = new Combo();
    private Combo cmbSeleccion = new Combo();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public AsientoAutomatico() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("ASIENTO AUTOMATICO DE ROLES");
        agregarComponente(panOpcion);

        Boton botAcceso = new Boton();
        botAcceso.setValue("Cargar Asiento");
        botAcceso.setIcon("ui-icon-search");
        botAcceso.setMetodo("cargaRegistro");
        bar_botones.agregarBoton(botAcceso);

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("select ide_movimiento,nro_comprobante,detalle_aciento from cont_movimiento");
        autBusca.setSize(100);
        autBusca.setMetodoChange("filtrarRegistro");
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autBusca);

        Boton botLimpiar = new Boton();
        botLimpiar.setIcon("ui-icon-cancel");
        botLimpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(botLimpiar);

        cmbDistributivo.setId("cmbDistributivo");
        cmbDistributivo.setConexion(conPostgres);
        cmbDistributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        cmbDistributivo.setMetodo("buscarColumna");

        cmbSeleccion.setId("cmbSeleccion");
        cmbSeleccion.setConexion(conPostgres);
        cmbSeleccion.setCombo("select cue_codigo, cue_codigo as descripcion from conc_catalogo_cuentas\n"
                + "where nivel = 5 and grupo_nivel ='S' order by cue_codigo");

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("SELECCIONE PARAMETROS"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoCarga");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        gri_busca.getChildren().add(new Etiqueta("Servidor "));
        gri_busca.getChildren().add(cmbDistributivo);
        gri_busca.getChildren().add(new Etiqueta("Cuenta "));
        gri_busca.getChildren().add(cmbSeleccion);
        setRol.setId("setRol");
        setRol.getTab_seleccion().setConexion(conPostgres);//conexion para seleccion con otra base
        setRol.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        setRol.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRol.getTab_seleccion().setRows(9);
        setRol.setRadio();
        setRol.getGri_cuerpo().setHeader(gri_busca);
        setRol.getBot_aceptar().setMetodo("aceptoCarga");
        setRol.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setRol);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("cont_movimiento", "ide_movimiento", 1);
//        if (autBusca.getValue() == null) {
//            tabTabla.setCondicion("ide_movimiento=-1");
//        } else {
//            tabTabla.setCondicion("ide_movimiento=" + autBusca.getValor());
//        }
        tabTabla.getColumna("ide_movimiento").setVisible(false);
        tabTabla.getColumna("IDE_CENTRO_COSTO").setVisible(false);
        tabTabla.getColumna("MOV_FECHA").setVisible(false);
        tabTabla.getColumna("MOV_USUARIO").setVisible(false);
        tabTabla.getColumna("BANDERA").setVisible(false);
        tabTabla.getColumna("CODIGO_AUX").setVisible(false);
        tabTabla.getColumna("TIPO_AUX").setVisible(false);
        tabTabla.getColumna("IDDOCUMENTO_SAI").setVisible(false);
        tabTabla.getColumna("TIPO").setVisible(false);
        tabTabla.getColumna("IDE_DOCUMENTO").setVisible(false);
        tabTabla.getColumna("IDE_COMPROBANTE").setVisible(false);
        tabTabla.getColumna("IDE_CONCEPTO").setVisible(false);
        tabTabla.getColumna("IDE_TITULO").setVisible(false);
        tabTabla.getColumna("IDE_ENTREGA_DETALLE").setVisible(false);
        tabTabla.getColumna("IP_RESPONSABLE").setVisible(false);
        tabTabla.getColumna("IDE_IMPUESTO").setVisible(false);
        tabTabla.getColumna("TIPO_ASIENTO").setVisible(false);
        tabTabla.getColumna("IDE_TIPO_MOVIMIENTO").setVisible(false);
        tabTabla.getColumna("ANO").setVisible(false);
        tabTabla.getColumna("FECHA_MOVIMIENTO").setVisible(false);
        tabTabla.getColumna("ASIENTO").setVisible(false);
        tabTabla.getColumna("IDE_COMPROBANTE_PAGADO").setVisible(false);
        tabTabla.getColumna("BANDERA_SIGEF").setVisible(false);
        tabTabla.getColumna("IP_INGRE").setVisible(false);
        tabTabla.getColumna("IP_ACTUA").setVisible(false);

        tabTabla.agregarRelacion(tabDetalle);
        tabTabla.setTipoFormulario(true);
        tabTabla.setLectura(true);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setConexion(conPostgres);
        tabDetalle.setTabla("cont_detalle_movimiento", "ide_detalle_mov", 2);
        tabDetalle.getColumna("ide_cuenta").setCombo("SELECT ide_cuenta,cue_codigo,cue_descripcion from conc_catalogo_cuentas");
        tabDetalle.getColumna("ide_tipo_movimiento").setVisible(false);
        tabDetalle.getColumna("doc_deposito").setVisible(false);
        tabDetalle.getColumna("ide_clasificador").setVisible(false);
        tabDetalle.getColumna("mov_documento").setVisible(false);
        tabDetalle.getColumna("mov_usuario").setVisible(false);
        tabDetalle.getColumna("iddocumento_sai").setVisible(false);
        tabDetalle.getColumna("ide_tipo").setVisible(false);
        tabDetalle.getColumna("partida_sai").setVisible(false);
        tabDetalle.getColumna("conciliado_sai").setVisible(false);
        tabDetalle.getColumna("sai_numero").setVisible(false);
        tabDetalle.getColumna("conciliacion_m").setVisible(false);
        tabDetalle.getColumna("ide_programa").setVisible(false);
        tabDetalle.getColumna("ide_tes_certificacion").setVisible(false);
        tabDetalle.getColumna("estado_deta_mov").setVisible(false);
        tabDetalle.getColumna("ide_detalle_mov_pagado").setVisible(false);
        tabDetalle.getColumna("bandera_sigef").setVisible(false);
        tabDetalle.getColumna("cuenta_provisional").setVisible(false);
        tabDetalle.getColumna("codigo_alfa").setVisible(false);
        tabDetalle.getColumna("mov_descripcion").setVisible(false);
        tabDetalle.dibujar();
        PanelTabla pnd = new PanelTabla();
        pnd.setPanelTabla(tabDetalle);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(pnt, pnd, "30%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(divTablas);
        panOpcion.getChildren().add(gru);
    }

    public void buscarColumna() {
        if (cmbDistributivo.getValue() != null && cmbDistributivo.getValue().toString().isEmpty() == false) {
            setRol.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM srh_columnas WHERE distributivo=" + cmbDistributivo.getValue());
            setRol.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una elemento", "");
        }
    }

    public void filtrarRegistro(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autBusca.onSelect(evt);
        dibujarPantalla();
//        autBusca.onSelect(evt);
//        if (autBusca.getValor() != null) {
//            tabDetalle.setFilaActual(autBusca.getValor());
//            utilitario.addUpdate("tabDetalle");
//        }
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void cargaRegistro() {
        setRol.dibujar();
        setRol.getTab_seleccion().limpiar();
    }

    public void aceptoCarga() {
//        TablaGenerica tabDato = programas.getAsientoContable(Integer.parseInt(tabDetalle.getValor("ide_cuenta")), Integer.parseInt(tabDetalle.getValor("ide_movimiento")));
        System.err.println(cmbSeleccion.getValue());
        System.err.println(tabTabla.getValor("ide_movimiento"));
        System.err.println(tabTabla.getValor("ano"));
        System.err.println(tabTabla.getValor("ide_periodo"));
        System.err.println(cmbDistributivo.getValue());
        System.err.println(setRol.getValorSeleccionado());
//        programas.setCuentaContable(cmbSeleccion.getValue() + "", Integer.parseInt(tabTabla.getValor("ide_movimiento")), Integer.parseInt(tabTabla.getValor("ano")),
//                Integer.parseInt(tabTabla.getValor("ide_periodo")), Integer.parseInt(cmbDistributivo.getValue() + ""), Integer.parseInt(setRol.getValorSeleccionado()));

//        tabTabla.actualizar();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabDetalle.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public SeleccionTabla getSetRol() {
        return setRol;
    }

    public void setSetRol(SeleccionTabla setRol) {
        this.setRol = setRol;
    }
}
