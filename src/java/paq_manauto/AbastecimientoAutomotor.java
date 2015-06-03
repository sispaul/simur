/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_manauto.ejb.SQLManauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class AbastecimientoAutomotor extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabTabla = new Tabla();
    private SeleccionTabla setTabla = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autCompleta = new AutoCompletar();
    private Calendario calFechaInicio = new Calendario();
    private Calendario calFechaFin = new Calendario();
    private Dialogo dialogoa = new Dialogo();
    private Grid grid = new Grid();
    private Grid grida = new Grid();
    @EJB
    private SQLManauto aCombustible = (SQLManauto) utilitario.instanciarEJB(SQLManauto.class);

    public AbastecimientoAutomotor() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("abrirCuadro");
        bar_botones.agregarBoton(botBuscar);

        autCompleta.setId("autCompleta");
        autCompleta.setConexion(conPostgres);
        autCompleta.setAutoCompletar("SELECT a.abastecimiento_id,\n"
                + "a.abastecimiento_fecha,\n"
                + "a.abastecimiento_numero_vale,\n"
                + "v.mve_placa,\n"
                + "v.mve_codigo\n"
                + "FROM mvabactecimiento_combustible a\n"
                + "INNER JOIN mv_vehiculo v ON a.mve_secuencial = v.mve_secuencial\n"
                + "where a.abastecimiento_tipo_medicion = '1'");
        autCompleta.setMetodoChange("filtrarSolicitud");
        autCompleta.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autCompleta);

        /*CONFIGURACIÓN DE COMBOS*/
        Grid griBusca = new Grid();
        griBusca.setColumns(2);

        griBusca.getChildren().add(new Etiqueta("FECHA INICIO:"));
        griBusca.getChildren().add(calFechaInicio);
        griBusca.getChildren().add(new Etiqueta("FECHA FINAL:"));
        griBusca.getChildren().add(calFechaFin);

        Boton botAcceso = new Boton();
        botAcceso.setValue("Buscar");
        botAcceso.setIcon("ui-icon-search");
        botAcceso.setMetodo("aceptarRegistro");
        bar_botones.agregarBoton(botAcceso);
        griBusca.getChildren().add(botAcceso);

        setTabla.setId("setTabla");
        setTabla.getTab_seleccion().setConexion(conPostgres);
        setTabla.setSeleccionTabla("SELECT a.abastecimiento_id,\n"
                + "a.abastecimiento_fecha,\n"
                + "a.abastecimiento_numero_vale,\n"
                + "v.mve_placa,\n"
                + "v.mve_codigo\n"
                + "FROM mvabactecimiento_combustible a\n"
                + "INNER JOIN mv_vehiculo v ON a.mve_secuencial = v.mve_secuencial\n"
                + "where a.abastecimiento_tipo_medicion = '1'", "abastecimiento_id");
        setTabla.getTab_seleccion().setEmptyMessage("No Encuentra Datos");
        setTabla.getTab_seleccion().setRows(10);
        setTabla.setRadio();
        setTabla.getGri_cuerpo().setHeader(griBusca);
        setTabla.getBot_aceptar().setMetodo("buscarRegistro");
        setTabla.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setTabla);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("mvabactecimiento_combustible", "abastecimiento_id", 1);
        if (autCompleta.getValue() == null) {
            tabTabla.setCondicion("abastecimiento_id=-1");
        } else {
            tabTabla.setCondicion("abastecimiento_id=" + autCompleta.getValor());
        }
        tabTabla.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||'/'||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tabTabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial,\n"
                + "((case when v.mve_placa is NULL then v.mve_codigo when v.mve_placa is not null then v.mve_placa end )||'/'||m.mvmarca_descripcion ||'/'||o.mvmodelo_descripcion)as descripcion\n"
                + "FROM mv_vehiculo v\n"
                + "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n"
                + "INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id\n"
                + "WHERE v.mve_tipo_ingreso in('A','O')");
        tabTabla.getColumna("abastecimiento_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tabTabla.getColumna("mve_secuencial").setFiltroContenido();
        tabTabla.getColumna("mve_secuencial").setMetodoChange("busPlaca");
        tabTabla.getColumna("abastecimiento_kilometraje").setMetodoChange("kilometraje");
        tabTabla.getColumna("abastecimiento_galones").setMetodoChange("galones");
        tabTabla.getColumna("abastecimiento_tipo_medicion").setValorDefecto("1");
        tabTabla.getColumna("abastecimiento_logining").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.getColumna("abastecimiento_fechaing").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("tipo_combustible_id").setLectura(true);
        tabTabla.getColumna("abastecimiento_numero").setLectura(true);
        tabTabla.getColumna("abastecimiento_total").setLectura(true);
        tabTabla.getColumna("mve_secuencial").setLectura(true);
        tabTabla.getColumna("abastecimiento_fechaing").setVisible(false);
        tabTabla.getColumna("abastecimiento_logining").setVisible(false);
        tabTabla.getColumna("abastecimiento_tipo_medicion").setVisible(false);
        tabTabla.getColumna("abastecimiento_valorhora").setVisible(false);
        tabTabla.getColumna("abastecimiento_anio").setVisible(false);
        tabTabla.getColumna("abastecimiento_periodo").setVisible(false);
        tabTabla.getColumna("abastecimiento_id").setVisible(false);
        tabTabla.getColumna("abastecimiento_horasmes").setVisible(false);
        tabTabla.setTipoFormulario(true);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();
        PanelTabla pntt = new PanelTabla();
        pntt.setPanelTabla(tabTabla);

        Grupo gru = new Grupo();
        gru.getChildren().add(pntt);
        panOpcion.getChildren().add(gru);

    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autCompleta.limpiar();
        utilitario.addUpdate("autCompleta");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void abrirCuadro() {
        setTabla.dibujar();
    }

    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autCompleta.onSelect(evt);
        dibujarPantalla();
    }

    public void aceptarRegistro() {
        if (calFechaInicio.getValue() != null && calFechaFin.getValue() != null) {
            setTabla.getTab_seleccion().setSql("SELECT a.abastecimiento_id,\n"
                    + "a.abastecimiento_fecha,\n"
                    + "a.abastecimiento_numero_vale,\n"
                    + "v.mve_placa,\n"
                    + "v.mve_codigo\n"
                    + "FROM mvabactecimiento_combustible a\n"
                    + "INNER JOIN mv_vehiculo v ON a.mve_secuencial = v.mve_secuencial\n"
                    + "where a.abastecimiento_fecha BETWEEN '" + calFechaInicio.getFecha() + "' and '" + calFechaFin.getFecha() + "'\n"
                    + "and a.abastecimiento_tipo_medicion = '1'");
            setTabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un rago de fechas", "");
        }
    }

    public void buscarRegistro() {
        if (setTabla.getValorSeleccionado() != null) {
            autCompleta.setValor(setTabla.getValorSeleccionado());
            dibujarPantalla();
            setTabla.cerrar();
            utilitario.addUpdate("autCompleta,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    @Override
    public void insertar() {
        if (tabTabla.isFocus()) {
            tabTabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tabTabla.getValor("abastecimiento_id") != null) {
            TablaGenerica tabInfo = aCombustible.getCatalogoDato("*", tabTabla.getTabla(), "abastecimiento_id = " + tabTabla.getValor("abastecimiento_id") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = aCombustible.getNumeroCampos(tabTabla.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = aCombustible.getEstrucTabla(tabTabla.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabTabla.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        aCombustible.setActuaRegis(Integer.parseInt(tabTabla.getValor("abastecimiento_id")), tabTabla.getTabla(), tabInfoColum1.getValor("Column_Name"), tabTabla.getValor(tabInfoColum1.getValor("Column_Name")), "abastecimiento_id");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else {
            if (tabTabla.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
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

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public SeleccionTabla getSetTabla() {
        return setTabla;
    }

    public void setSetTabla(SeleccionTabla setTabla) {
        this.setTabla = setTabla;
    }

    public AutoCompletar getAutCompleta() {
        return autCompleta;
    }

    public void setAutCompleta(AutoCompletar autCompleta) {
        this.autCompleta = autCompleta;
    }
}
