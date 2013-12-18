/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

/**
 *
 * @author Diego
 */
public class pre_empresa extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    
     private Tabla tab_permisos= new Tabla();
     private Tabla tab_socios= new Tabla();

    public pre_empresa() {
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("trans_empresa", "ide_empresa", 1);
        tab_tabla.setHeader("CATASTRO DE EMPRESAS DE TRANSPORTE PUBLICO");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getColumna("codigo").setCombo("trans_tipo", "codigo", "nombre", "");
        tab_tabla.getColumna("fecha_responsable").setCalendarioFechaHora();
        tab_tabla.getColumna("fecha_responsable").setLectura(true);        
        tab_tabla.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
        tab_tabla.getGrid().setColumns(6);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
               
        Tabulador tab_tabulador=new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_permisos.setId("tab_permisos");
        tab_permisos.setIdCompleto("tab_tabulador:tab_permisos");
        tab_permisos.setTabla("tab_permisos", "ide_permiso", 2);
        tab_permisos.setTipoFormulario(true);
        tab_permisos.getGrid().setColumns(6);
        tab_permisos.getColumna("fecha_responsable").setCalendarioFechaHora();
        tab_permisos.getColumna("fecha_responsable").setLectura(true);        
        tab_permisos.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());

        tab_permisos.dibujar();        
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_permisos);         
        tab_tabulador.agregarTab("INF. PERMISO", pat_panel1);
        
        tab_socios.setId("tab_socios");
        tab_socios.setIdCompleto("tab_tabulador:tab_socios");
        tab_socios.setTabla("trans_socios", "ide_socios", 3);
        tab_socios.getColumna("ide_tipo_licencia").setCombo("trans_tipo_licencia","ide_tipo_licencia","des_tipo_licencia","");
        tab_socios.setTipoFormulario(true);
        tab_socios.getGrid().setColumns(6);
        tab_socios.getColumna("fecha_responsable").setCalendarioFechaHora();
        tab_socios.getColumna("fecha_responsable").setLectura(true);        
        tab_socios.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());

        tab_socios.dibujar();        
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_socios);         
        tab_tabulador.agregarTab("SOCIOS", pat_panel2);
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel,tab_tabulador,"H","50%");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
        if (tab_tabla.isFilaInsertada()) {
            tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla.isFilaInsertada()) {
            //Actualiza nuevamente la fecha
            tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
        }
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_permisos() {
        return tab_permisos;
    }

    public void setTab_permisos(Tabla tab_permisos) {
        this.tab_permisos = tab_permisos;
    }

    public Tabla getTab_socios() {
        return tab_socios;
    }

    public void setTab_socios(Tabla tab_socios) {
        this.tab_socios = tab_socios;
    }
    
}
