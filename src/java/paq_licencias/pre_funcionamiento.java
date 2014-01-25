/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_licencias;

import framework.componentes.Boton;
import paq_licencias.*;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_funcionamiento extends Pantalla {

    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private SeleccionTabla set_tabla = new SeleccionTabla();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
     private Map p_parametros = new HashMap();
     private Texto tex_busca = new Texto();
     
    public pre_funcionamiento() {

        set_tabla.setId("set_tabla");

        Grid gri_busca = new Grid();
        gri_busca.setColumns(4);
        gri_busca.getChildren().add(tex_busca);
        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar Identificación");
        bot_busca.setMetodo("buscarRepresentante");
        gri_busca.getChildren().add(bot_busca);
        set_tabla.getGri_cuerpo().setHeader(gri_busca);
        
        
        set_tabla.setTitle("SELECCIONE REPRESENTANTE");
        set_tabla.setSeleccionTabla("SELECT IDE_CMREP,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP,EMAIL_CMREP FROM CMT_REPRESENTANTE", "IDE_CMREP");
        set_tabla.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setFiltro(true);
        set_tabla.getTab_seleccion().setRows(10);
        set_tabla.setRadio();
        set_tabla.getGri_cuerpo().setHeader(gri_busca);
        set_tabla.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(set_tabla);
        
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("TUR_ESTABLECIMIENTO", "ID_TESTABL", 1);
        tab_cabecera.setHeader("LICENCIA ANUAL DE FUNCIONAMIENTO");
        tab_cabecera.agregarRelacion(tab_detalle); ///relación        
        tab_cabecera.dibujar();

        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cabecera);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TUR_LICENCIA", "ID_TLICEN", 2);
        tab_detalle.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_detalle);


        Boton bot1 = new Boton();
        bot1.setValue("SELECCION REPRESENTANTE");
        bot1.setIcon("ui-icon-document"); 
        bot1.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot1);
        
        Division div = new Division();
        div.dividir2(tabp, tabp1, "40%", "H");
        agregarComponente(div);

    }
    
   public void abrirSeleccionTabla() {
        set_tabla.dibujar();
    }
        public void buscarRepresentante() {
        if (tex_busca.getValue() != null && tex_busca.getValue().toString().isEmpty() == false) {
            set_tabla.getTab_seleccion().setSql("SELECT IDE_CMREP,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP,EMAIL_CMREP FROM CMT_REPRESENTANTE where DOCUMENTO_IDENTIDAD_CMREP like '%" + tex_busca.getValue() + "%'");
            set_tabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensaje("Debe ingresar un valor en el texto", "");
        }

    }
        @Override
    public void aceptarReporte() {
        if (rep_reporte.getNombre().equals("Reporte Representantes")) {

            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                set_tabla.dibujar();
                } else if (set_tabla.isVisible()) {
                //los parametros de este reporte
                p_parametros = new HashMap();
                p_parametros.put("p_titulo", "PARAMETRO TITULO DEL REPRESENTANTE");
                p_parametros.put("p_marcas", set_tabla.getSeleccionados());
                set_tabla.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }

    }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_cabecera.guardar();
        tab_detalle.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public SeleccionTabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(SeleccionTabla set_tabla) {
        this.set_tabla = set_tabla;
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
