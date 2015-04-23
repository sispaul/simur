/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
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
    private Tabla setDatos = new Tabla();
    private Dialogo dialogoa = new Dialogo();
    private Grid grid = new Grid();
    private Grid grida = new Grid();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public InventarioEquipos() {

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        conSQL.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        conSQL.NOMBRE_MARCA_BASE = "sqlserver";
        
        Tabulador tabTabulador = new Tabulador();
        tabTabulador.setId("tabTabulador");

        tabEquipo.setId("tabEquipo");
        tabEquipo.setTabla("cei_descripcion_equipos", "desc_codigo", 1);
        tabEquipo.getColumna("tipo_codigo").setCombo("SELECT TIPO_CODIGO,TIPO_DESCRIPCION FROM CEI_TIPO_EQUIPOS");
        tabEquipo.getColumna("desc_ultimo_mantenimiento").setLectura(true);
        tabEquipo.getColumna("desc_serie").setMetodoChange("infEquipo");
        tabEquipo.agregarRelacion(tabAccesorio);
        tabEquipo.agregarRelacion(tabAsignacion);
        tabEquipo.setTipoFormulario(true);
        tabEquipo.getGrid().setColumns(4);
        tabEquipo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tabEquipo);

        tabAccesorio.setId("tabAccesorio");
        tabAccesorio.setIdCompleto("tabTabulador:tabAccesorio");
        tabAccesorio.setTabla("cei_accesorios", "acce_codigo", 2);
        tabAccesorio.getColumna("acce_serie").setMetodoChange("infAccesorio");
        tabAccesorio.dibujar();
        PanelTabla pta = new PanelTabla();
        pta.setPanelTabla(tabAccesorio);

        tabAsignacion.setId("tabAsignacion");
        tabAsignacion.setIdCompleto("tabTabulador:tabAsignacion");
        tabAsignacion.setTabla("cei_asignacion", "asignacion_codigo", 3);
        tabAsignacion.getColumna("catalogo_codigo").setCombo("select catalogo_codigo,catalogo_descripcion from cei_catalogo_tablas");
        tabAsignacion.getColumna("catalogo_codigo").setMetodoChange("buscarItem");
        tabAsignacion.getColumna("asignacion_estado").setCheck();
        tabAsignacion.getColumna("asignacion_fecha").setValorDefecto(utilitario.getFechaActual());
        tabAsignacion.dibujar();
        PanelTabla ptas = new PanelTabla();
        ptas.setPanelTabla(tabAsignacion);


        tabTabulador.agregarTab("ACCESORIOS", pta);
        tabTabulador.agregarTab("ASIGNACIÓN", ptas);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(pte, tabTabulador, "50%", "H");
        agregarComponente(divTablas);

        dialogoa.setId("dialogoa");
        dialogoa.setTitle("DATOS DE ASIGNACIÓN"); //titulo
        dialogoa.setWidth("55%"); //siempre en porcentajes  ancho
        dialogoa.setHeight("40%");//siempre porcentaje   alto
        dialogoa.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoa.getBot_aceptar().setMetodo("aceptoDatos");
        grid.setColumns(4);
        agregarComponente(dialogoa);

    }

    public void infEquipo() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo(tabEquipo.getValor("desc_serie"));
        if (!tabDato.isEmpty()) {
            tabEquipo.setValor("desc_codigo_activo", tabDato.getValor("codigo"));
            tabEquipo.setValor("desc_marca", tabDato.getValor("marca"));
            tabEquipo.setValor("desc_modelo", tabDato.getValor("modelo"));
            tabEquipo.setValor("desc_estado", tabDato.getValor("estado"));
            tabEquipo.setValor("desc_descripcion", tabDato.getValor("des_activo"));
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
            tabAccesorio.setValor("acce_estado", tabDato.getValor("estado"));
            tabAccesorio.setValor("acce_descripcion", tabDato.getValor("des_activo"));
            utilitario.addUpdate("tabAccesorio");
        } else {
            utilitario.agregarMensaje("Accesorio No Localizado en la Base de Activos", null);
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
            }else{
                setDatos.setConexion(conSQL);
            }
            setDatos.setSql("select * from " + tabDato.getValor("CATALOGO_ORIGEN") + " " + tabDato.getValor("CATALOGO_FILTRO") + "");
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
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabEquipo.guardar()) {
            if (tabAccesorio.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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
}
