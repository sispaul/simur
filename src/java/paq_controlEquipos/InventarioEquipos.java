/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_controlEquipos.ejb.Procesos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class InventarioEquipos extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Conexion conSQL = new Conexion();
    private Tabla tabEquipo = new Tabla();
    private Tabla tabAccesorio = new Tabla();
    private Tabla tabAsignacion = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setDatos = new Tabla();
    private Tabla setDatost = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private Dialogo dialogoa = new Dialogo();
    private Dialogo dialogot = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridt = new Grid();
    private Grid grida = new Grid();
    private Grid gridat = new Grid();
    private Panel panOpcion = new Panel();
    private Combo cmbLicencia = new Combo();
    private Combo cmbModelo = new Combo();
    private Combo cmbTipo = new Combo();
    private AutoCompletar autBusca = new AutoCompletar();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public InventarioEquipos() {

        //datos de usuario actual del sistema
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        conSQL.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        conSQL.NOMBRE_MARCA_BASE = "sqlserver";

        cmbLicencia.setId("cmbLicencia");
        cmbLicencia.setCombo("SELECT TIPO_LICENCIA_CODIGO, TIPO_LICENCIA_DESCRIPCION FROM CEI_TIPO_LICENCIA");

        cmbModelo.setId("cmbModelo");
        cmbModelo.setCombo("SELECT MODELO_CODIGO,MODELO_DESCRIPCION FROM CEI_MODELO_LICENCIA");

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT distinct DESC_TIPO_EQUIPO as id,DESC_TIPO_EQUIPO FROM CEI_DESCRIPCION_EQUIPOS");

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT e.DESC_CODIGO,\n"
                + "e.DESC_CODIGO_ACTIVO AS Codigo_Activo,\n"
                + "(case when DESC_MARCA is not null then DESC_MARCA when DESC_MARCA is null then '' end) +' '+\n"
                + "(case when DESC_MODELO is not null then DESC_MODELO when DESC_MODELO is null then '' end) AS descripcion,\n"
                + "e.DESC_SERIE AS Serie,\n"
                + "e.DESC_TIPO_EQUIPO AS Tipo,\n"
                + "a.ASIGNACION_NOMBRE\n"
                + "FROM dbo.CEI_DESCRIPCION_EQUIPOS e\n"
                + "INNER JOIN dbo.CEI_ASIGNACION a ON a.DESC_CODIGO = e.DESC_CODIGO\n"
                + "where a.CATALOGO_CODIGO = (SELECT CATALOGO_CODIGO FROM CEI_CATALOGO_TABLAS where CATALOGO_DESCRIPCION = 'FUNCIONARIO') ");
        autBusca.setMetodoChange("filtrarEquipo");
        autBusca.setSize(80);

        bar_botones.agregarComponente(new Etiqueta("Buscar Equipo:"));
        bar_botones.agregarComponente(autBusca);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE EQUIPOS Y ASIGNACIÓN");
        agregarComponente(panOpcion);

        dialogoa.setId("dialogoa");
        dialogoa.setTitle("DATOS DE ASIGNACIÓN"); //titulo
        dialogoa.setWidth("40%"); //siempre en porcentajes  ancho
        dialogoa.setHeight("40%");//siempre porcentaje   alto
        dialogoa.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoa.getBot_aceptar().setMetodo("aceptarDatos");
        grid.setColumns(4);
        agregarComponente(dialogoa);

        dialogot.setId("dialogot");
        dialogot.setTitle("TIPO DE LICENCIA"); //titulo
        dialogot.setWidth("45%"); //siempre en porcentajes  ancho
        dialogot.setHeight("40%");//siempre porcentaje   alto
        dialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogot.getBot_aceptar().setMetodo("aceptDato");
        gridt.setColumns(4);
        agregarComponente(dialogot);

        dibujaPantalla();

        //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("mostrarDatos");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(cmbTipo);
        gri_busca.getChildren().add(bot_buscar);

        setRegistros.setId("setRegistros");
        setRegistros.setSeleccionTabla("SELECT e.DESC_CODIGO,\n"
                + "e.DESC_CODIGO_ACTIVO AS Codigo_Activo,\n"
                + "(case when DESC_MARCA is not null then DESC_MARCA when DESC_MARCA is null then '' end) +' '+\n"
                + "(case when DESC_MODELO is not null then DESC_MODELO when DESC_MODELO is null then '' end) AS Descripcion,\n"
                + "e.DESC_SERIE AS Serie,\n"
                + "a.ASIGNACION_NOMBRE\n"
                + "FROM dbo.CEI_DESCRIPCION_EQUIPOS e\n"
                + "INNER JOIN dbo.CEI_ASIGNACION a ON a.DESC_CODIGO = e.DESC_CODIGO\n"
                + "where a.CATALOGO_CODIGO = (SELECT CATALOGO_CODIGO FROM CEI_CATALOGO_TABLAS where CATALOGO_DESCRIPCION = 'FUNCIONARIO') and e.DESC_CODIGO=-1 order by Codigo_Activo", "DESC_CODIGO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("Codigo_Activo").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("Descripcion").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("Serie").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("ASIGNACION_NOMBRE").setFiltro(true);
        setRegistros.getTab_seleccion().setRows(12);
        setRegistros.setWidth("75%");
        setRegistros.setRadio();
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("aceptarBusqueda");
        setRegistros.setHeader("BUSCAR EQUIPOS");
        agregarComponente(setRegistros);

    }

    public void buscaRegistro() {
        setRegistros.dibujar();
    }

    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql("SELECT e.DESC_CODIGO,\n"
                    + "e.DESC_CODIGO_ACTIVO AS Codigo_Activo,\n"
                    + "(case when DESC_MARCA is not null then DESC_MARCA when DESC_MARCA is null then '' end) +' '+\n"
                    + "(case when DESC_MODELO is not null then DESC_MODELO when DESC_MODELO is null then '' end) AS Descripcion,\n"
                    + "e.DESC_SERIE AS Serie,\n"
                    + "a.ASIGNACION_NOMBRE\n"
                    + "FROM dbo.CEI_DESCRIPCION_EQUIPOS e\n"
                    + "INNER JOIN dbo.CEI_ASIGNACION a ON a.DESC_CODIGO = e.DESC_CODIGO\n"
                    + "where a.CATALOGO_CODIGO = (SELECT CATALOGO_CODIGO FROM CEI_CATALOGO_TABLAS where CATALOGO_DESCRIPCION = 'FUNCIONARIO') and e.DESC_TIPO_EQUIPO ='" + cmbTipo.getValue() + "'");
            setRegistros.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void aceptarBusqueda() {
        if (setRegistros.getValorSeleccionado() != null) {
            autBusca.setValor(setRegistros.getValorSeleccionado());
            setRegistros.cerrar();
            tabEquipo.setFilaActual(autBusca.getValor());
            utilitario.addUpdate("tabEquipo,autBusca");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Registro", "");
        }
    }

    public void dibujaPantalla() {
        limpiarPanel();
        tabEquipo.setId("tabEquipo");
        tabEquipo.setTabla("cei_descripcion_equipos", "desc_codigo", 1);
        List lista1 = new ArrayList();
        Object fil11[] = {
            "Bueno", "Bueno"
        };
        Object fil21[] = {
            "De Baja", "De Baja"
        };
        Object fil31[] = {
            "Regular", "Regular"
        };
        lista1.add(fil11);
        lista1.add(fil21);
        lista1.add(fil31);
        tabEquipo.getColumna("desc_estado").setCombo(lista1);
        tabEquipo.getColumna("desc_ultimo_mantenimiento").setLectura(true);
        tabEquipo.getColumna("desc_serie").setMetodoChange("infEquipo");
        tabEquipo.getColumna("desc_codigo_activo").setMetodoChange("infEquipo1");
        tabEquipo.agregarRelacion(tabAccesorio);
        tabEquipo.agregarRelacion(tabAsignacion);
        tabEquipo.setTipoFormulario(true);
        tabEquipo.getGrid().setColumns(4);
        tabEquipo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tabEquipo);

        Tabulador tabTabulador = new Tabulador();
        tabTabulador.setId("tabTabulador");

        tabAccesorio.setId("tabAccesorio");
        tabAccesorio.setIdCompleto("tabTabulador:tabAccesorio");
        tabAccesorio.setTabla("cei_accesorios", "acce_codigo", 2);
        tabAccesorio.getColumna("acce_serie").setMetodoChange("infAccesorio");
        tabAccesorio.getColumna("acce_codigo_activo").setMetodoChange("infAccesorio1");
        tabAccesorio.getColumna("acce_fecha_asignacion").setValorDefecto(utilitario.getFechaActual());
        List lista = new ArrayList();
        Object fil1[] = {
            "Asignar", "Asignar"
        };
        Object fil2[] = {
            "De Baja", "De Baja"
        };
        lista.add(fil1);
        lista.add(fil2);
        tabAccesorio.getColumna("acce_estado").setCombo(lista);
        tabAccesorio.getColumna("acce_estado").setMetodoChange("deBaja");
        tabAccesorio.dibujar();
        PanelTabla pta = new PanelTabla();
        pta.setPanelTabla(tabAccesorio);

        tabAsignacion.setId("tabAsignacion");
        tabAsignacion.setIdCompleto("tabTabulador:tabAsignacion");
        tabAsignacion.setTabla("cei_asignacion", "asignacion_codigo", 3);
        tabAsignacion.getColumna("catalogo_codigo").setCombo("select catalogo_codigo,catalogo_descripcion from cei_catalogo_tablas");
        tabAsignacion.getColumna("catalogo_codigo").setMetodoChange("buscarItem");
        tabAsignacion.getColumna("asignacion_fecha").setValorDefecto(utilitario.getFechaActual());
        List list = new ArrayList();
        Object fila[] = {
            "Asignar", "Asignar"
        };
        Object fila2[] = {
            "De Baja", "De Baja"
        };
        list.add(fil1);
        list.add(fil2);
        tabAsignacion.getColumna("asignacion_estado").setCombo(list);
        tabAsignacion.getColumna("asignacion_estado").setMetodoChange("deBaja1");
        tabAsignacion.getColumna("asignacion_cod_empleado").setVisible(false);
        tabAsignacion.dibujar();
        PanelTabla ptas = new PanelTabla();
        ptas.setPanelTabla(tabAsignacion);

        tabTabulador.agregarTab("COMPONENTES", pta);
        tabTabulador.agregarTab("ASIGNACIÓN", ptas);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(pte, tabTabulador, "50%", "H");
        panOpcion.getChildren().add(divTablas);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void filtrarEquipo(SelectEvent evt) {
        autBusca.onSelect(evt);
        if (autBusca.getValor() != null) {
            tabEquipo.setFilaActual(autBusca.getValor());
            utilitario.addUpdate("tabEquipo");
        }
    }

    public void infEquipo() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo(tabEquipo.getValor("desc_serie"));
        if (!tabDato.isEmpty()) {
            tabEquipo.setValor("desc_codigo_activo", tabDato.getValor("codigo"));
            tabEquipo.setValor("desc_marca", tabDato.getValor("marca"));
            tabEquipo.setValor("desc_modelo", tabDato.getValor("modelo"));
            tabEquipo.setValor("desc_descripcion", tabDato.getValor("des_activo"));
            tabEquipo.setValor("desc_tipo_equipo", tabDato.getValor("nombre"));
            utilitario.addUpdate("tabEquipo");
        } else {
            utilitario.agregarMensaje("Equipo No Localizado en la Base de Activos", null);
        }
    }

    public void infEquipo1() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo1(tabEquipo.getValor("desc_codigo_activo"));
        if (!tabDato.isEmpty()) {
            tabEquipo.setValor("desc_codigo_activo", tabDato.getValor("codigo"));
            tabEquipo.setValor("desc_marca", tabDato.getValor("marca"));
            tabEquipo.setValor("desc_modelo", tabDato.getValor("modelo"));
            tabEquipo.setValor("desc_descripcion", tabDato.getValor("des_activo"));
            tabEquipo.setValor("desc_tipo_equipo", tabDato.getValor("nombre"));
            utilitario.addUpdate("tabEquipo");
        } else {
            utilitario.agregarMensaje("Equipo No Localizado en la Base de Activos", null);
        }
    }

    public void infAccesorio() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo(tabAccesorio.getValor("acce_serie"));
        if (!tabDato.isEmpty()) {
            tabAccesorio.setValor("acce_codigo_activo", tabDato.getValor("codigo"));
            tabAccesorio.setValor("acce_marca", tabDato.getValor("marca"));
            tabAccesorio.setValor("acce_modelo", tabDato.getValor("modelo"));
            tabAccesorio.setValor("acce_descripcion", tabDato.getValor("des_activo"));
            utilitario.addUpdate("tabTabulador:tabAccesorio");
        } else {
            utilitario.agregarMensaje("Accesorio No Localizado en la Base de Activos", null);
        }
    }

    public void infAccesorio1() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo1(tabAccesorio.getValor("acce_serie"));
        if (!tabDato.isEmpty()) {
            tabAccesorio.setValor("acce_codigo_activo", tabDato.getValor("codigo"));
            tabAccesorio.setValor("acce_marca", tabDato.getValor("marca"));
            tabAccesorio.setValor("acce_modelo", tabDato.getValor("modelo"));
            tabAccesorio.setValor("acce_descripcion", tabDato.getValor("des_activo"));
            utilitario.addUpdate("tabTabulador:tabAccesorio");
        } else {
            utilitario.agregarMensaje("Accesorio No Localizado en la Base de Activos", null);
        }
    }

    public void deBaja() {
        if (tabAccesorio.getValor("acce_estado").equals("De Baja")) {
            tabAccesorio.setValor("acce_fecha_baja", utilitario.getFechaActual());
            utilitario.addUpdate("tabTabulador:tabAccesorio");
        } else {
            tabAccesorio.setValor("acce_fecha_baja", null);
            utilitario.addUpdate("tabTabulador:tabAccesorio");
        }
    }

    public void deBaja1() {
        if (tabAsignacion.getValor("asignacion_estado").equals("De Baja")) {
            tabAsignacion.setValor("asignacion_fecha_baja", utilitario.getFechaActual());
            utilitario.addUpdate("tabTabulador:tabAsignacion");
        } else {
            tabAsignacion.setValor("asignacion_fecha_baja", null);
            utilitario.addUpdate("tabTabulador:tabAsignacion");
        }
    }

    public void buscarItem() {
        TablaGenerica tabDato = accesoDatos.getCatalgoTablas(Integer.parseInt(tabAsignacion.getValor("catalogo_codigo")));
        if (!tabDato.isEmpty()) {
            dialogoa.Limpiar();
            dialogoa.setDialogo(grid);
            grida.getChildren().add(setDatos);
            setDatos.setId("setDatos");
            if (tabDato.getValor("CATALOGO_BASE").equals("2")) {
                setDatos.setConexion(conPostgres);
            } else {
                setDatos.setConexion(conSQL);
            }
            setDatos.setSql("select " + tabDato.getValor("CATALOGO_CAMPOS") + " from " + tabDato.getValor("CATALOGO_ORIGEN") + " " + tabDato.getValor("CATALOGO_FILTRO") + "");
            setDatos.getColumna("descripcion").setFiltroContenido();
            setDatos.setRows(10);
            setDatos.setTipoSeleccion(false);
            dialogoa.setDialogo(grida);
            setDatos.dibujar();
            dialogoa.dibujar();
        } else {
            utilitario.agregarMensaje("Selecione una Opción", null);
        }
    }

    public void aceptarDatos() {
        TablaGenerica tabDato = accesoDatos.getCatalgoTablas(Integer.parseInt(tabAsignacion.getValor("catalogo_codigo")));
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("CATALOGO_BASE").equals("2")) {
                TablaGenerica tabDatoposql = accesoDatos.getCatalogoDatoposgres(tabDato.getValor("catalogo_campos"), tabDato.getValor("catalogo_origen"), "" + tabDato.getValor("catalogo_primaria") + " =" + setDatos.getValorSeleccionado() + "");
                if (!tabDatoposql.isEmpty()) {
                    if (tabDato.getValor("catalogo_origen").equals("srh_empleado")) {
                        TablaGenerica tabDatosemp = accesoDatos.getInfoEmpleado(tabDatoposql.getValor("codigo"));
                        if (!tabDatosemp.isEmpty()) {
                            tabAsignacion.setValor("asignacion_cod_empleado", tabDatosemp.getValor("cod_empleado"));
                            tabAsignacion.setValor("asignacion_nombre", tabDatosemp.getValor("nombres"));
                            tabAsignacion.setValor("asignacion_descripcion", tabDatosemp.getValor("nombre_cargo"));
                            tabAsignacion.setValor("asignacion_descripcion1", tabDatosemp.getValor("nombre_dir"));
                            utilitario.addUpdate("tabTabulador:tabAsignacion");
                            dialogoa.cerrar();
                        }
                    } else {
                        tabAsignacion.setValor("asignacion_nombre", tabDatoposql.getValor("codigo"));
                        tabAsignacion.setValor("asignacion_descripcion", tabDatoposql.getValor("descripcion"));
                        utilitario.addUpdate("tabTabulador:tabAsignacion");
                        dialogoa.cerrar();
                    }
                } else {
                }
            } else {
                TablaGenerica tabDatosql = accesoDatos.getCatalogoDatosql(tabDato.getValor("catalogo_campos"), tabDato.getValor("catalogo_origen"), "" + tabDato.getValor("catalogo_primaria") + " = " + setDatos.getValorSeleccionado() + "");
                if (!tabDatosql.isEmpty()) {
                    if (tabDato.getValor("catalogo_origen").equals("cei_programas")) {
                        buscarTipo();
                    } else {
                        tabAsignacion.setValor("asignacion_nombre", tabDatosql.getValor("codigo"));
                        tabAsignacion.setValor("asignacion_descripcion", tabDatosql.getValor("descripcion"));
                        utilitario.addUpdate("tabTabulador:tabAsignacion");
                        dialogoa.cerrar();
                    }
                }
            }
        } else {
            utilitario.agregarMensaje("Selecione una Opción", null);
        }
    }

    public void buscarTipo() {
        dialogot.Limpiar();
        dialogot.setDialogo(gridt);
        gridat.getChildren().add(cmbLicencia);
        gridat.getChildren().add(cmbModelo);
        dialogot.setDialogo(gridat);
        dialogot.dibujar();
    }

    public void aceptDato() {
        TablaGenerica tabDato = accesoDatos.getCatalgoTablas(Integer.parseInt(tabAsignacion.getValor("catalogo_codigo")));
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("CATALOGO_BASE").equals("2")) {
            } else {
            }
            TablaGenerica tabDatosql = accesoDatos.getCatalogoDatosql(tabDato.getValor("catalogo_campos"), tabDato.getValor("catalogo_origen"), "" + tabDato.getValor("catalogo_primaria") + " = " + setDatos.getValorSeleccionado() + "");
            if (!tabDatosql.isEmpty()) {
                if (tabDato.getValor("catalogo_origen").equals("cei_programas")) {
                    String cadena = null;
                    if (cmbModelo.getValue() != null) {
                        cadena = "=" + cmbModelo.getValue();
                    } else {
                        cadena = "is " + String.valueOf(cmbModelo.getValue());
                    }
                    TablaGenerica tabDatosqlic = accesoDatos.getInfoLicencia(Integer.parseInt(setDatos.getValorSeleccionado()), Integer.parseInt(cmbLicencia.getValue().toString()), cadena);
                    if (!tabDatosqlic.isEmpty()) {
                        tabAsignacion.setValor("asignacion_nombre", tabDatosqlic.getValor("PROGS_DESCRIPCION"));
                        tabAsignacion.setValor("asignacion_descripcion", tabDatosqlic.getValor("DETALLE_NUMERO_LICENCIA"));
                        tabAsignacion.setValor("asignacion_descripcion1", tabDatosqlic.getValor("TIPO_LICENCIA_DESCRIPCION"));
                        utilitario.addUpdate("tabTabulador:tabAsignacion");
                    } else {
                        TablaGenerica tabDatopro = accesoDatos.getInfoPrograma(Integer.parseInt(setDatos.getValorSeleccionado()));
                        if (!tabDatopro.isEmpty()) {
                            tabAsignacion.setValor("asignacion_nombre", tabDatopro.getValor("PROGS_DESCRIPCION"));
                            tabAsignacion.setValor("asignacion_descripcion", null);
                            tabAsignacion.setValor("asignacion_descripcion1", tabDatopro.getValor("TIPO_LICENCIA_DESCRIPCION"));
                            utilitario.addUpdate("tabTabulador:tabAsignacion");
                        }
                    }
                }
            }
        } else {
            utilitario.agregarMensaje("Selecione una Opción", null);
        }
        dialogoa.cerrar();
        dialogot.cerrar();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabEquipo.guardar()) {
            if (tabAccesorio.guardar()) {
                if (tabAsignacion.guardar()) {
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "HISTORICO DE ASIGNACIONES":
                aceptarDialogo();
                break;
        }
    }

    public void aceptarDialogo() {
        switch (rep_reporte.getNombre()) {
            case "HISTORICO DE ASIGNACIONES":
                p_parametros.put("codigo", Integer.parseInt(tabEquipo.getValor("desc_codigo")) + "");
                p_parametros.put("activo", tabEquipo.getValor("desc_codigo_activo") + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabEquipo() {
        return tabEquipo;
    }

    public void setTabEquipo(Tabla tabEquipo) {
        this.tabEquipo = tabEquipo;
    }

    public Tabla getTabAccesorio() {
        return tabAccesorio;
    }

    public void setTabAccesorio(Tabla tabAccesorio) {
        this.tabAccesorio = tabAccesorio;
    }

    public Tabla getTabAsignacion() {
        return tabAsignacion;
    }

    public void setTabAsignacion(Tabla tabAsignacion) {
        this.tabAsignacion = tabAsignacion;
    }

    public Tabla getSetDatos() {
        return setDatos;
    }

    public void setSetDatos(Tabla setDatos) {
        this.setDatos = setDatos;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getSetDatost() {
        return setDatost;
    }

    public void setSetDatost(Tabla setDatost) {
        this.setDatost = setDatost;
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

    public SeleccionTabla getSetRegistros() {
        return setRegistros;
    }

    public void setSetRegistros(SeleccionTabla setRegistros) {
        this.setRegistros = setRegistros;
    }
}
