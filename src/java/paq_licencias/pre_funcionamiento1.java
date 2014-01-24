/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_licencias;

import paq_pruebas.*;
import com.google.common.collect.HashBiMap;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
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
public class pre_funcionamiento1 extends Pantalla {

    private Dialogo dia_dialogo = new Dialogo();
    private SeleccionTabla set_tabla = new SeleccionTabla();
    private SeleccionTabla set_modelos = new SeleccionTabla();
    private Texto tex_busca = new Texto();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Panel pan_opcion = new Panel();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla=new Tabla();
    
    public pre_funcionamiento1() {
        
      // bar_botones.limpiar(); /// deja en blanco la barra de botones
        ///configurar el seleccion tabla 

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

        Boton bot1 = new Boton();
        bot1.setValue("SELECCION REPRESENTANTE");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot1);


        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(null, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("DIRECCIÓN DE TURISMO");
        agregarComponente(div_division);
       
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

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

    public void buscarRepresentante() {
        if (tex_busca.getValue() != null && tex_busca.getValue().toString().isEmpty() == false) {
            set_tabla.getTab_seleccion().setSql("SELECT IDE_CMREP,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP,EMAIL_CMREP FROM CMT_REPRESENTANTE where DOCUMENTO_IDENTIDAD_CMREP like '%" + tex_busca.getValue() + "%'");
            set_tabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensaje("Debe ingresar un valor en el texto", "");
        }

    }

    /**
     * se ejecuta cuando da click en el boton aceptar del componente
     * selecciontabla set_modelos
     */

    /**
     * Dibuja el componente selecciontabla set_modelos
     */

    public void aceptoSeleccionTabla() {
        if (set_tabla.getSeleccionados() != null && set_tabla.getSeleccionados().isEmpty() == false) {
            utilitario.agregarMensaje("SELECCIONADOS", set_tabla.getSeleccionados());
            set_tabla.cerrar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void abrirSeleccionTabla() {
        set_tabla.dibujar();
    }

    public void aceptoDialogo() {
        utilitario.agregarMensaje("ACEPTO EL DIALOGO", "");
        //cierra el dialog
        dia_dialogo.cerrar();
    }

    public void abrirDialogo() {
        dia_dialogo.dibujar();
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

    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }

    public SeleccionTabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(SeleccionTabla set_tabla) {
        this.set_tabla = set_tabla;
    }

    public SeleccionTabla getSet_modelos() {
        return set_modelos;
    }

    public void setSet_modelos(SeleccionTabla set_modelos) {
        this.set_modelos = set_modelos;
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
}
