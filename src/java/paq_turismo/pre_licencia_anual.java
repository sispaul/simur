/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_turismo;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_licencia_anual extends Pantalla {
    //declaracion de tablas
    private Tabla tab_establecimiento = new Tabla();
    private Tabla tab_licencia = new Tabla();
    private Tabla tab_consulta = new Tabla();
//autocompletar datos
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    private AutoCompletar aut_busca = new AutoCompletar();
//configuracion reporte
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private Panel pan_opcion = new Panel();

    public pre_licencia_anual() {

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT e.CODIGO_ESTAB,e.IDENTIFICACION_ESTAB,e.CLAVE_CATASTRAL,e.NOMBRE_ESTAB,t.DESCRIPCION_TIPO\n"
                + "FROM TUR_ESTABLECIMIENTOS e\n"
                + "LEFT JOIN TUR_TIPO_ESTABLECIMIENTO t ON e.CODIGO_TIPO = t.CODIGO_TIPO\n"
                + "LEFT JOIN TUR_LICENCIA l ON l.CODIGO_ESTAB = e.CODIGO_ESTAB AND l.CODIGO_TIPO = t.CODIGO_TIPO");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);

        sef_reporte.setId("sef_reporte");
        agregarComponente(sef_reporte);
        bar_botones.agregarReporte();

        tab_establecimiento.setId("tab_establecimiento");
        tab_establecimiento.setTabla("TUR_ESTABLECIMIENTOS", "CODIGO_ESTAB", 1);
        tab_establecimiento.getColumna("CODIGO_TIPO").setCombo("select CODIGO_TIPO,DESCRIPCION_TIPO from TUR_TIPO_ESTABLECIMIENTO");
        tab_establecimiento.setHeader("DATOS ESTABLECIMIENTO");
        tab_establecimiento.getColumna("CODIGO_ESTAB").setNombreVisual("ID");
        tab_establecimiento.getColumna("CODIGO_TIPO").setNombreVisual("TIPO ESTABLECIMIENTO");
        tab_establecimiento.getColumna("NOMBRE_ESTAB").setNombreVisual("NOMBRE ESTABLECIMIENTO");
        tab_establecimiento.getColumna("IDENTIFICACION_ESTAB").setNombreVisual("ID/RUC");
        tab_establecimiento.getColumna("REGISTRO_ESTAB").setMascara("17.05.50.9999");
        tab_establecimiento.getColumna("CIUDAD_ESTAB").setNombreVisual("CIUDAD");
        tab_establecimiento.getColumna("CATEGORIA_ESTAB").setNombreVisual("CATEGORIA");
        tab_establecimiento.getColumna("REPRESENTATE_ESTAB").setNombreVisual("REPRESENTANTE LEGAL");
        tab_establecimiento.getColumna("DIRECCION_ESTAB").setNombreVisual("DIRECCIÓN");
        tab_establecimiento.getColumna("CANTON_ESTAB").setNombreVisual("CANTON");
        tab_establecimiento.getColumna("CLAVE_CATASTRAL").setNombreVisual("CLAVE CATASTRAL");
        tab_establecimiento.getColumna("TELEFONO_ESTAB").setNombreVisual("TELEFONO");
        tab_establecimiento.getColumna("PROVINCIA_ESTAB").setNombreVisual("PROVINCIA");
        tab_establecimiento.setTipoFormulario(true);
        tab_establecimiento.getGrid().setColumns(6);
        tab_establecimiento.dibujar();
        tab_establecimiento.agregarRelacion(tab_licencia);
        PanelTabla tabp = new PanelTabla();
        tabp.setMensajeWarn("DIRECCIÓN DE TURISMO");
        tabp.setPanelTabla(tab_establecimiento);
        tab_establecimiento.setStyle(null);
        tabp.setStyle("width:100%;overflow: auto;");

        tab_licencia.setId("tab_licencia");
//        tab_licencia.setHeader("LICENCIA ANUAL DE FUNCIONAMIENTO");
        tab_licencia.setTabla("TUR_LICENCIA", "CODIGO_LICEN", 2);
        tab_licencia.getColumna("CODIGO_TIPO").setCombo("select CODIGO_TIPO,DESCRIPCION_TIPO from TUR_TIPO_ESTABLECIMIENTO ");
//        tab_licencia.getColumna("CODIGO_TIPO").setValorDefecto(tab_establecimiento.getValor("CODIGO_TIPO"));
        tab_licencia.getColumna("CODIGO_LICEN").setNombreVisual("ID");
        tab_licencia.getColumna("CODIGO_TIPO").setNombreVisual("TIPO ESTABLECIMIENTO");
        tab_licencia.getColumna("NUMERO_LICENCIA").setNombreVisual("Nro. PERMISO");
        tab_licencia.getColumna("FECHA_EXPEDICION").setNombreVisual("FECHA EXPEDICION");
        tab_licencia.getColumna("FECHA_EXPEDICION").setValorDefecto(utilitario.getFechaActual());
        tab_licencia.getColumna("FECHA_EXPEDICION").setLectura(true);
        tab_licencia.getColumna("FECHA_CADUCIDAD").setNombreVisual("FECHA CADUCIDAD");
        tab_licencia.setTipoFormulario(true);
        tab_licencia.getGrid().setColumns(2);
        tab_licencia.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_licencia);
        tab_licencia.setStyle(null);


        pan_opcion.setId("pan_opcion");
        pan_opcion.setStyle("font-size:12px;color:black;text-align:left;");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("LICENCIA ANUAL DE FUNCIONAMIENTO");


        pan_opcion.getChildren().add(tabp1);
        Division div = new Division();
        div.dividir2(tabp, pan_opcion, "40%", "h");

        agregarComponente(div);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }
//    public void tipo(){
//        tab_licencia.getColumna("CODIGO_TIPO").setCombo("select CODIGO_TIPO,DESCRIPCION_TIPO from TUR_TIPO_ESTABLECIMIENTO WHERE CODIGO_TIPO ="+tab_establecimiento.getValor("CODIGO_TIPO"));
//        utilitario.addUpdateTabla(tab_licencia,"CODIGO_TIPO","");//actualiza solo componentes
//    }
    @Override
    public void aceptarReporte() {
        //los parametros de este reporte
        Map p_parametros = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("LICENCIA ANUAL DE FUNCIONAMIENTO")) {
            if (tab_establecimiento.getValorSeleccionado() != null) {
                p_parametros.put("clave", tab_establecimiento.getValor("CLAVE_CATASTRAL"));
                p_parametros.put("id", tab_establecimiento.getValor("IDENTIFICACION_ESTAB"));
                p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_reporte.dibujar();
            } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
        }

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_establecimiento.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_establecimiento");
        }
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();

    }

    @Override
    public void guardar() {
        if (tab_establecimiento.guardar()) {
            if (tab_licencia.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_establecimiento.isFocus()) {
            tab_establecimiento.eliminar();
        } else if (tab_licencia.isFocus()) {
            tab_licencia.eliminar();
        }
    }

    public Tabla getTab_establecimiento() {
        return tab_establecimiento;
    }

    public void setTab_establecimiento(Tabla tab_establecimiento) {
        this.tab_establecimiento = tab_establecimiento;
    }

    public Tabla getTab_licencia() {
        return tab_licencia;
    }

    public void setTab_licencia(Tabla tab_licencia) {
        this.tab_licencia = tab_licencia;
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }
}
