/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

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
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class ProyectosFinanciamientos extends Pantalla {

    private Tabla tabTablaPro = new Tabla();
    private Tabla tabTablaFin = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Combo comboParametros = new Combo();
    private Conexion conSqlProy4 = new Conexion();
    private Panel panOpcion = new Panel();
    private AutoCompletar autCompleta = new AutoCompletar();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo diaParametros = new Dialogo();
    private Grid gridPa = new Grid();

    public ProyectosFinanciamientos() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        
        conSqlProy4.setUnidad_persistencia(utilitario.getPropiedad("poolSqlProyectos4"));
        conSqlProy4.NOMBRE_MARCA_BASE = "sqlserver";

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INFORMACIÓN DE PROYECTOS");
        agregarComponente(panOpcion);

        autCompleta.setId("autCompleta");
        autCompleta.setConexion(conSqlProy4);
        autCompleta.setAutoCompletar("SELECT proyecto_codigo,proyecto_codigo as codigo,proyecto_nombre,proyecto_direccion from FINAN_PROYECTO order by proyecto_codigo");
        autCompleta.setMetodoChange("filtroRegistro");
        autCompleta.setSize(180);
        bar_botones.agregarComponente(new Etiqueta("Buscar Proyecto"));
        bar_botones.agregarComponente(autCompleta);

        Boton botLimpiar = new Boton();
        botLimpiar.setIcon("ui-icon-cancel");
        botLimpiar.setMetodo("limpiar1");
        bar_botones.agregarBoton(botLimpiar);

        tabTablaPro.setId("tabTablaPro");
        tabTablaPro.setConexion(conSqlProy4);
        tabTablaPro.setTabla("finan_proyecto", "proyecto_codigo", 1);
        tabTablaPro.getColumna("proyecto_arrastre").setVisible(false);
        tabTablaPro.getColumna("proyecto_contratista1").setVisible(false);
        tabTablaPro.getColumna("proyecto_contratista2").setVisible(false);
        tabTablaPro.getColumna("proyecto_validacion").setVisible(false);
        tabTablaPro.getColumna("proyecto_asig_inicial1").setVisible(false);
        tabTablaPro.getColumna("proyecto_asig_inicial").setVisible(false);
        tabTablaPro.getColumna("proyecto_total_asig").setLectura(true);
        tabTablaPro.getColumna("proyecto_nombre").setLectura(true);
        tabTablaPro.getColumna("proyecto_direccion").setLectura(true);
//        tabTablaPro.agregarRelacion(tabTablaFin);
        tabTablaPro.setTipoFormulario(true);
        tabTablaPro.getGrid().setColumns(2);
        tabTablaPro.dibujar();

        PanelTabla pntp = new PanelTabla();
        pntp.setPanelTabla(tabTablaPro);

//        tabTablaFin.setId("tabTablaFin");
//        tabTablaFin.setConexion(conSqlProy4);
//        tabTablaFin.setTabla("finan_proyecto_financiamiento", "proy_finan_codigo", 2);
//        tabTablaFin.setRows(10);
//        tabTablaFin.dibujar();
//
//        PanelTabla pntf = new PanelTabla();
//        pntf.setPanelTabla(tabTablaFin);

        Division div = new Division();
        div.dividir1(pntp);
//        div.dividir2(pntp, pntf, "35%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conSqlProy4);
        agregarComponente(sef_formato);

        comboParametros.setId("comboParametros");
        comboParametros.setConexion(conSqlProy4);
        comboParametros.setCombo("SELECT DISTINCT\n"
                + "proyecto_financiamiento,\n"
                + "proyecto_financiamiento as financiamiento\n"
                + "from FINAN_PROYECTO\n"
                + "where proyecto_financiamiento is not null\n"
                + "order by proyecto_financiamiento");

        diaParametros.setId("diaParametros");
        diaParametros.setTitle("SELECCIONAR FINANCIAMIENTO"); //titulo
        diaParametros.setWidth("65%"); //siempre en porcentajes  ancho
        diaParametros.setHeight("10%");//siempre porcentaje   alto
        diaParametros.setResizable(false); //para que no se pueda cambiar el tamaño
        diaParametros.getBot_aceptar().setMetodo("mostrarReporte");
        gridPa.setColumns(4);
        agregarComponente(diaParametros);

    }

    public void filtroRegistro(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        autCompleta.onSelect(evt);
        if (autCompleta.getValor() != null) {
            tabTablaPro.setFilaActual(autCompleta.getValor());
            utilitario.addUpdate("tabTablaPro");
        }

    }

    public void limpiar1() {
        autCompleta.limpiar();
        utilitario.addUpdate("autCompleta");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTablaPro.guardar()) {
            if (tabTablaFin.guardar()) {
                conSqlProy4.guardarPantalla();
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
            case "REPORTE POR FINANCIAMIENTOS":
                diaParametros.Limpiar();
                gridPa.getChildren().add(new Etiqueta("FINANCIAMIENTO :"));
                gridPa.getChildren().add(comboParametros);
                diaParametros.setDialogo(gridPa);
                diaParametros.dibujar();
                break;
        }
    }

    public void mostrarReporte() {
        switch (rep_reporte.getNombre()) {
            case "REPORTE POR FINANCIAMIENTOS":
                p_parametros.put("titulo", "PROYECTO CON PARTIDA");
                p_parametros.put("titulo_1", "FINANCIAMIENTO : ");
                p_parametros.put("financiamiento", comboParametros.getValue() + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                System.err.println(p_parametros);
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabTablaPro() {
        return tabTablaPro;
    }

    public void setTabTablaPro(Tabla tabTablaPro) {
        this.tabTablaPro = tabTablaPro;
    }

    public Tabla getTabTablaFin() {
        return tabTablaFin;
    }

    public void setTabTablaFin(Tabla tabTablaFin) {
        this.tabTablaFin = tabTablaFin;
    }

    public Conexion getConSqlProy4() {
        return conSqlProy4;
    }

    public void setConSqlProy4(Conexion conSqlProy4) {
        this.conSqlProy4 = conSqlProy4;
    }

    public AutoCompletar getAutCompleta() {
        return autCompleta;
    }

    public void setAutCompleta(AutoCompletar autCompleta) {
        this.autCompleta = autCompleta;
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
