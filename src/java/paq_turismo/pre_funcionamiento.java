/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_turismo;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author p-sistemas
 */
public class pre_funcionamiento extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    private Map p_parametros = new HashMap();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
//obejto tabla usuario
    private Tabla tab_consulta = new Tabla();
    
    public pre_funcionamiento() {
             
        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT\n" +
                                                "CODIGO_FUN,\n" +
                                                "CEDULA_REPRESENTANTE,\n" +
                                                "CLAVE_CATASTRAL,\n" +
                                                "NOMBRE_ESTABLECIMIENTO,\n" +
                                                "REPRESENTANTE\n" +
                                                "\n" +
                                                "FROM\n" +
                                                "TUR_FUNCIONAMIENTO_ANUAL");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);
        
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

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("TUR_FUNCIONAMIENTO_ANUAL", "CODIGO_FUN", 1);
        tab_tabla1.setHeader("DIRECCIÓN DE TURISMO");
        tab_tabla1.getColumna("CODIGO_FUN").setNombreVisual("ID");
        tab_tabla1.getColumna("NUMERO_LICENCIA").setNombreVisual("No. LICENCIA");
        tab_tabla1.getColumna("FECHA_EXPEDICION").setNombreVisual("FECHA EXPEDICIÓN");
        tab_tabla1.getColumna("NOMBRE_ESTABLECIMIENTO").setNombreVisual("ESTABLECIMIENTO");
        tab_tabla1.getColumna("CLAVE_CATASTRAL").setNombreVisual("CLAVE CATASTRAL");
        tab_tabla1.getColumna("REGISTRO_NUMERO").setNombreVisual("No. REGISTRO");
        tab_tabla1.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tab_tabla1.getColumna("FECHA_EXPIRACION").setNombreVisual("FECHA CADUCIDAD");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.dibujar();
               
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setMensajeWarn("LICENCIA ANUAL DE FUNCIONAMIENTO");
        pat_panel1.setPanelTabla(tab_tabla1);

        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:300px;left:70px;");
        quinde.setValue("imagenes/logo.png");
        pat_panel1.setWidth("100%");
        pat_panel1.getChildren().add(quinde);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);
        
        //agregar usuario actual
        bar_botones.agregarReporte();      
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();


    }
    
@Override
    public void aceptarReporte() {
              //los parametros de este reporte
             Map p_parametros = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("LICENCIA ANUAL")) {
            if (tab_tabla1.getValorSeleccionado() != null) {
                    p_parametros.put("clave", tab_tabla1.getValor("CLAVE_CATASTRAL"));
                    p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
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

            tab_tabla1.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_tabla1");
         
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
        if (tab_tabla1.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }
    
}
