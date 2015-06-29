/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

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
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class AsientosAutomaticos extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabTabla = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private SeleccionTabla setRol = new SeleccionTabla();
    private Combo cmbDistributivo = new Combo();
    private Combo cmbSeleccion = new Combo();
    private Combo cmbCuentas = new Combo();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Texto txtDebe = new Texto();
    private Texto txtHaber = new Texto();
    private Texto txtDevengado = new Texto();
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public AsientosAutomaticos() {
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

        cmbCuentas.setId("cmbCuentas");
        cmbCuentas.setConexion(conPostgres);
        cmbCuentas.setCombo("SELECT ide_cuenta,cue_codigo,cue_descripcion from conc_catalogo_cuentas where nivel in (4,5,6)");

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
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("ide_movimiento=-1");
        } else {
            tabTabla.setCondicion("ide_movimiento=" + autBusca.getValor());
        }
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

        txtDebe.setId("txtDebe");
        txtDebe.setSize(8);
        txtHaber.setId("txtHaber");
        txtHaber.setSize(8);
        txtDevengado.setId("txtDevengado");
        txtDevengado.setSize(8);
        Grid gri_busca = new Grid();
        gri_busca.setColumns(8);
        gri_busca.getChildren().add(new Etiqueta("Cuenta: "));
        gri_busca.getChildren().add(cmbCuentas);
        gri_busca.getChildren().add(new Etiqueta("Debe $: "));
        gri_busca.getChildren().add(txtDebe);
        gri_busca.getChildren().add(new Etiqueta("Haber $: "));
        gri_busca.getChildren().add(txtHaber);
        gri_busca.getChildren().add(new Etiqueta("Devengado $: "));
        gri_busca.getChildren().add(txtDevengado);
        agregarComponente(gri_busca);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setConexion(conPostgres);
        tabDetalle.setSql("SELECT c.ide_detalle_mov,\n"
                + "c.ide_movimiento,\n"
                + "o.cue_codigo,\n"
                + "o.cue_descripcion,\n"
                + "c.mov_debe,\n"
                + "c.mov_haber,\n"
                + "c.mov_devengado,"
                + "null as regresar  \n"
                + "FROM cont_detalle_movimiento c\n"
                + "INNER JOIN public.conc_catalogo_cuentas o ON c.ide_cuenta = o.ide_cuenta\n"
                + "where c.ide_movimiento = " + tabTabla.getValor("ide_movimiento") + "\n"
                + "order by c.ide_detalle_mov desc");
        tabDetalle.setCampoPrimaria("ide_detalle_mov");
        List list = new ArrayList();
        Object fil1[] = {
            "1", " "
        };
        list.add(fil1);;
        tabDetalle.getColumna("regresar").setRadio(list, " ");
        tabDetalle.getColumna("regresar").setLongitud(1);
        tabDetalle.setRows(10);
        tabDetalle.dibujar();
        PanelTabla pnd = new PanelTabla();
        pnd.setPanelTabla(tabDetalle);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir3(pnt, gri_busca, pnd, "33%", "61%", "H");
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
        limpia();
        autBusca.onSelect(evt);
        dibujarPantalla();
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
    }

    public void limpia() {
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void cargaRegistro() {
        setRol.dibujar();
        setRol.getTab_seleccion().limpiar();
    }

    public void aceptoCarga() {
        programas.setCuentaContable(cmbSeleccion.getValue() + "", Integer.parseInt(tabTabla.getValor("ide_movimiento")), Integer.parseInt(tabTabla.getValor("ano")),
                Integer.parseInt(tabTabla.getValor("ide_periodo")), Integer.parseInt(cmbDistributivo.getValue() + ""), Integer.parseInt(setRol.getValorSeleccionado()));
        tabTabla.actualizar();
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        programas.setCuentaConta(Integer.parseInt(cmbCuentas.getValue() + ""), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(txtDebe.getValue() + ""), Double.parseDouble(txtHaber.getValue() + ""), Double.parseDouble(txtDevengado.getValue() + ""));
        tabDetalle.actualizar();
        utilitario.agregarMensaje("Registro Guardado", null);
    }

    @Override
    public void eliminar() {
        for (int i = 0; i < tabDetalle.getTotalFilas(); i++) {
            tabDetalle.getValor(i, "regresar");
            tabDetalle.getValor(i, "cue_codigo");
           if(tabDetalle.getValor(i, "regresar").endsWith("1")){
               
           }
        }
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
