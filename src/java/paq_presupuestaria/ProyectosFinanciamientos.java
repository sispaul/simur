/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
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
    private Conexion conSqlProy4 = new Conexion();
    private Panel panOpcion = new Panel();
    private AutoCompletar autCompleta = new AutoCompletar();

    public ProyectosFinanciamientos() {

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

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabTablaPro.setId("tabTablaPro");
        tabTablaPro.setConexion(conSqlProy4);
        tabTablaPro.setTabla("finan_proyecto", "proyecto_codigo", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (autCompleta.getValue() == null) {
            tabTablaPro.setCondicion("proyecto_codigo=-1");
        } else {
            tabTablaPro.setCondicion("proyecto_codigo=" + autCompleta.getValor());
        }
        tabTablaPro.getColumna("proyecto_arrastre").setVisible(false);
        tabTablaPro.getColumna("proyecto_contratista1").setVisible(false);
        tabTablaPro.getColumna("proyecto_contratista2").setVisible(false);
        tabTablaPro.getColumna("proyecto_validacion").setVisible(false);
        tabTablaPro.getColumna("proyecto_asig_inicial1").setVisible(false);
        tabTablaPro.getColumna("proyecto_financiamiento1").setVisible(false);
        tabTablaPro.getColumna("proyecto_asig_inicial").setVisible(false);
        tabTablaPro.getColumna("proyecto_financiamiento").setVisible(false);
        tabTablaPro.agregarRelacion(tabTablaFin);
        tabTablaPro.setTipoFormulario(true);
        tabTablaPro.getGrid().setColumns(2);
        tabTablaPro.dibujar();

        PanelTabla pntp = new PanelTabla();
        pntp.setPanelTabla(tabTablaPro);

        tabTablaFin.setId("tabTablaFin");
        tabTablaFin.setConexion(conSqlProy4);
        tabTablaFin.setTabla("finan_proyecto_financiamiento", "proy_finan_codigo", 2);
        tabTablaFin.setRows(10);
        tabTablaFin.dibujar();

        PanelTabla pntf = new PanelTabla();
        pntf.setPanelTabla(tabTablaFin);

        Division div = new Division();
        div.dividir2(pntp, pntf, "25%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
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

    public void filtroRegistro(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autCompleta.onSelect(evt);
        dibujarPantalla();
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
}
