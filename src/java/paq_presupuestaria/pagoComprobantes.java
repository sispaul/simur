/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
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
    private SeleccionTabla setComprobante = new SeleccionTabla();
    private Combo cmbEstado = new Combo();
    private Panel panOpcion = new Panel();//cabecera
    private Panel panOpcion1 = new Panel();//detalle

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

    //limpieza paneles y abrir busqueda
    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    private void limpiarPanel() {
        //borra el contenido de la división central
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
}
