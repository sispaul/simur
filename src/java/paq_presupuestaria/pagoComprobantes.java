/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
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
public class pagoComprobantes extends Pantalla {

    //declaracion de conexion
    private Conexion conPostgres = new Conexion();
    private AutoCompletar autBusca = new AutoCompletar();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabComprobante = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Tabla tabListado = new Tabla();
    private SeleccionTabla setComprobante = new SeleccionTabla();
    private SeleccionTabla setLista = new SeleccionTabla();
    private Combo cmbEstado = new Combo();
    private Panel panOpcion = new Panel();//cabecera
    private Panel panOpcion1 = new Panel();//detalle
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pagoComprobantes() {

        //Mostrar el usuario 
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //Creación de Botones; Busqueda,Limpieza
        Boton botBusca = new Boton();
        botBusca.setValue("Busqueda Avanzada");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(botBusca);

        cmbEstado.setId("cmbEstado");
        cmbEstado.setConexion(conPostgres);
        cmbEstado.setCombo("SELECT ide_estado_listado,estado from tes_estado_listado");

        Grid griBusca = new Grid();
        griBusca.setColumns(2);
        griBusca.getChildren().add(new Etiqueta("Estado :"));
        griBusca.getChildren().add(cmbEstado);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar");
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(botBuscar);
        griBusca.getChildren().add(botBuscar);

        setComprobante.setId("setComprobante");
        setComprobante.getTab_seleccion().setConexion(conPostgres);//conexion para seleccion con otra base
        setComprobante.setSeleccionTabla("SELECT ide_listado,fecha_listado,item,ci_envia,(SELECT nombres from srh_empleado where cedula_pass = tes_comprobante_pago_listado\n"
                + ".ci_envia) as nombre FROM tes_comprobante_pago_listado where IDE_LISTADO=-1", "IDE_LISTADO");
        setComprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setComprobante.getTab_seleccion().setRows(10);
        setComprobante.setRadio();
        setComprobante.getGri_cuerpo().setHeader(griBusca);
        setComprobante.getBot_aceptar().setMetodo("aceptarBusqueda");
        setComprobante.setHeader("SELECCIONE LISTADO A PAGAR");
        agregarComponente(setComprobante);

        //Creación de Divisiones
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader(" COMPROBANTE DE PAGOS POR LISTADOS ");
        agregarComponente(panOpcion);

        tabComprobante.setId("tabComprobante");
        tabComprobante.setConexion(conPostgres);
        tabComprobante.setTabla("tes_comprobante_pago_listado", "ide_listado", 1);
        tabComprobante.getColumna("ide_listado").setVisible(false);
        tabComprobante.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tabComprobante.getColumna("IP_ACTUA_PAGA").setVisible(false);
        tabComprobante.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tabComprobante.getColumna("IP_ACTUA_PAGA").setVisible(false);
        tabComprobante.getColumna("CI_ENVIA").setVisible(false);
        tabComprobante.getColumna("ESTADO").setCombo("SELECT ide_estado_listado,estado FROM tes_estado_listado");
        tabComprobante.setTipoFormulario(true);
        tabComprobante.getGrid().setColumns(6);
        tabComprobante.agregarRelacion(tabDetalle);
        tabComprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tabComprobante);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setConexion(conPostgres);
        tabDetalle.setSql("SELECT  \n"
                + "d.ide_detalle_listado, \n"
                + "d.ide_listado,   \n"
                + "d.comprobante,  \n"
                + "d.cedula_pass_beneficiario,  \n"
                + "d.nombre_beneficiario,  \n"
                + "d.valor,   \n"
                + "d.numero_cuenta,  \n"
                + "d.codigo_banco,  \n"
                + "d.ban_nombre,  \n"
                + "d.tipo_cuenta,  \n"
                + "null as proceso  \n"
                + "FROM  \n"
                + "tes_detalle_comprobante_pago_listado AS d  \n"
                + " where ide_listado =" + setComprobante.getValorSeleccionado());
        tabDetalle.setCampoPrimaria("ide_detalle_listado");
        tabDetalle.setCampoOrden("ide_listado");
        List lista = new ArrayList();
        Object fila2[] = {
            "2", "ACREDITAR"
        };
        Object fila3[] = {
            "3", "DEVOLVER"
        };
        lista.add(fila2);;
        lista.add(fila3);;
        tabDetalle.getColumna("proceso").setRadio(lista, " ");
        tabDetalle.getColumna("ide_detalle_listado").setVisible(false);
        tabDetalle.getColumna("cedula_pass_beneficiario").setLongitud(20);
        tabDetalle.getColumna("nombre_beneficiario").setLongitud(20);
        tabDetalle.getColumna("numero_cuenta").setLongitud(5);
        tabDetalle.getColumna("ban_nombre").setLongitud(5);
        tabDetalle.setRows(5);
        tabDetalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(tabDetalle);
        Division div = new Division();
        div.dividir2(tcp, tdd, "42%", "h");
        agregarComponente(div);
    }

    public void dibujarLista() {
    }

    public void abrirBusqueda() {
        setComprobante.dibujar();
        setComprobante.getTab_seleccion().limpiar();
    }

    public void buscarEntrega() {
        if (cmbEstado.getValue() != null && cmbEstado.getValue().toString().isEmpty() == false) {
            setComprobante.getTab_seleccion().setSql("SELECT ide_listado,fecha_listado,item,ci_envia,(SELECT nombres from srh_empleado where cedula_pass = tes_comprobante_pago_listado\n"
                    + ".ci_envia) as nombre FROM tes_comprobante_pago_listado WHERE estado =" + cmbEstado.getValue());
            setComprobante.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un estado", "");
        }
    }

        public void aceptarBusqueda() {
        limpiarPanel();
        if (setComprobante.getValorSeleccionado() != null) {
            TablaGenerica tabDatos = programas.Beneficiarios(Integer.parseInt(setComprobante.getValorSeleccionado()));
            if (!tabDatos.isEmpty()) {
                for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                    TablaGenerica tabInfo = programas.getDatos(tabDatos.getValor(i, "cedula_pass_beneficiario"));
                    if (!tabInfo.isEmpty()) {
                        if (tabInfo.getValor("ban_codigo") != null) {
                            TablaGenerica tabBank = programas.getBanco(null, Integer.parseInt(tabInfo.getValor("ban_codigo")));
                            if (!tabBank.isEmpty()) {
                                programas.setActuDetallePag(Integer.parseInt(tabInfo.getValor("ban_codigo")), tabInfo.getValor("cod_cuenta"), tabBank.getValor("codigo_banco"), tabBank.getValor("ban_nombre"),
                                        Integer.parseInt(setComprobante.getValorSeleccionado()), tabDatos.getValor(i,"comprobante"), Integer.parseInt(tabDatos.getValor(i,"item")),tabInfo.getValor("numero_cuenta"));
                            }
                        } else {
                            TablaGenerica tabBank = programas.getBanco(tabInfo.getValor("codigo_banco"), Integer.SIZE);
                            if (!tabBank.isEmpty()) {
                                programas.setActuDetallePag(Integer.parseInt(tabBank.getValor("ban_codigo")), tabInfo.getValor("cod_cuenta"), tabBank.getValor("codigo_banco"), tabBank.getValor("ban_nombre"),
                                        Integer.parseInt(setComprobante.getValorSeleccionado()), tabDatos.getValor(i,"comprobante"), Integer.parseInt(tabDatos.getValor(i,"item")),tabInfo.getValor("numero_cuenta"));
                            }
                        }
                    }
                }
            }
//            programas.actualizarComprobante(Integer.parseInt(set_comprobante.getValorSeleccionado()));
            autBusca.setValor(setComprobante.getValorSeleccionado());
            setComprobante.cerrar();
            dibujarLista();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un listado", "");
        }
    }
    
    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    private void limpiarPanel() {
        panOpcion1.getChildren().clear();
        panOpcion.getChildren().clear();
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

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public SeleccionTabla getSetComprobante() {
        return setComprobante;
    }

    public void setSetComprobante(SeleccionTabla setComprobante) {
        this.setComprobante = setComprobante;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getTabComprobante() {
        return tabComprobante;
    }

    public void setTabComprobante(Tabla tabComprobante) {
        this.tabComprobante = tabComprobante;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public Tabla getTabListado() {
        return tabListado;
    }

    public void setTabListado(Tabla tabListado) {
        this.tabListado = tabListado;
    }

    public SeleccionTabla getSetLista() {
        return setLista;
    }

    public void setSetLista(SeleccionTabla setLista) {
        this.setLista = setLista;
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
