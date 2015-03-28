/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class ReporteFinanciamientoProyecto extends Pantalla {

    private Conexion conSqlProy = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Combo comboAnio = new Combo();
    private Combo comboParametros = new Combo();
    private Dialogo diaAnio = new Dialogo();
    private Grid gridAni = new Grid();
    private Dialogo diaParametros = new Dialogo();
    private Grid gridPa = new Grid();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    String cadena;

    public ReporteFinanciamientoProyecto() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        comboAnio.setId("comboAnio");
        comboAnio.setCombo("SELECT ano_curso,ano_curso as anio from conc_ano ORDER BY ano_curso");

        diaAnio.setId("diaAnio");
        diaAnio.setTitle("SELECCIONE AÑO QUE DESEA VIZUALIZAR"); //titulo
        diaAnio.setWidth("25%"); //siempre en porcentajes  ancho
        diaAnio.setHeight("10%");//siempre porcentaje   alto
        diaAnio.setResizable(false); //para que no se pueda cambiar el tamaño
        gridAni.getChildren().add(new Etiqueta("FINANCIAMIENTO :"));
        gridAni.getChildren().add(comboAnio);
        diaAnio.setDialogo(gridAni);
        diaAnio.getBot_aceptar().setMetodo("conexion");
        gridAni.setColumns(4);
        diaAnio.dibujar();
        agregarComponente(diaAnio);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        diaParametros.setId("diaParametros");
        diaParametros.setTitle("SELECCIONAR FINANCIAMIENTO"); //titulo
        diaParametros.setWidth("65%"); //siempre en porcentajes  ancho
        diaParametros.setHeight("10%");//siempre porcentaje   alto
        diaParametros.setResizable(false); //para que no se pueda cambiar el tamaño
        diaParametros.getBot_aceptar().setMetodo("mostrarReporte");
        gridPa.setColumns(4);
        agregarComponente(diaParametros);
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    public void conexion() {
        if (comboAnio.getValue() != null) {
            conSqlProy.setUnidad_persistencia(utilitario.getPropiedad("poolSqlProyectos" + comboAnio.getValue()));
            conSqlProy.NOMBRE_MARCA_BASE = "sqlserver";

            comboParametros.setId("comboParametros");
            comboParametros.setConexion(conSqlProy);
            comboParametros.setCombo("SELECT DISTINCT\n"
                    + "proyecto_financiamiento,\n"
                    + "proyecto_financiamiento as financiamiento\n"
                    + "from FINAN_PROYECTO\n"
                    + "where proyecto_financiamiento is not null\n"
                    + "order by proyecto_financiamiento");

            sef_formato.setConexion(conSqlProy);

            abrirListaReportes();

        } else {
            utilitario.agregarMensajeInfo("Debe Escoger Año", null);
        }
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
                System.out.println(rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
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

    public Conexion getConSqlProy() {
        return conSqlProy;
    }

    public void setConSqlProy(Conexion conSqlProy) {
        this.conSqlProy = conSqlProy;
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

    public Combo getComboAnio() {
        return comboAnio;
    }

    public void setComboAnio(Combo comboAnio) {
        this.comboAnio = comboAnio;
    }
}
