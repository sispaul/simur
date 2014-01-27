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
public class pre_funcionamiento3 extends Pantalla{
private Tabla tab_tabla1 = new Tabla();
private Tabla tab_tabla2 = new Tabla();
private Tabla tab_tabla3 = new Tabla();
private Tabla tab_tabla4 = new Tabla();

   
    public pre_funcionamiento3() {
        
                
        tab_tabla1.setId("tab_tabla1");
	tab_tabla1.setTipoFormulario(true);
	tab_tabla1.setTabla("TUR_REPRESENTA_ESTABLECIMIENTO", "IDE_RESTABLE", 1);
	tab_tabla1.getColumna("ID_TESTABL").setCombo("SELECT ID_TESTABL,NOMB_TESTABL from TUR_ESTABLECIMIENTO");
        tab_tabla1.getColumna("IDE_CMREP").setCombo("select IDE_CMREP,NOMBRES_APELLIDOS_CMREP from CMT_REPRESENTANTE");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.dibujar();
	PanelTabla pat_panel1 = new PanelTabla();
	pat_panel1.setMensajeWarn("DIRECCION DE TURISMO");
	pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
	tab_tabla2.setTipoFormulario(true);
	tab_tabla2.setTabla("TUR_ESTABLECIMIENTO", "ID_TESTABL", 2);
	tab_tabla2.agregarRelacion(tab_tabla4);
        tab_tabla2.dibujar();
	PanelTabla pat_panel2 = new PanelTabla();
	pat_panel2.setMensajeWarn("DATOS DE ESTABLECIMIENTO");
	pat_panel2.setPanelTabla(tab_tabla2);
        
        
        tab_tabla3.setId("tab_tabla3");
	tab_tabla3.setTipoFormulario(true);
	tab_tabla3.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 3);
	tab_tabla3.agregarRelacion(tab_tabla4);
        tab_tabla3.dibujar();
	PanelTabla pat_panel3 = new PanelTabla();
	pat_panel3.setMensajeWarn("DATOS DE REPRESENTANTE");
	pat_panel3.setPanelTabla(tab_tabla3);
        
        
        
       tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTipoFormulario(true);
	tab_tabla4.setTabla("tur_licencia", "ID_TLICEN", 4);
	tab_tabla4.getColumna("ID_TCATEG").setCombo("select ID_TCATEG,DESC_TCATEG from tur_categoria");
        tab_tabla4.getColumna("NUM_TLICEN").setNombreVisual("No. LICENCIA:");
        tab_tabla4.getColumna("FECHAI_TLICEN").setLectura(true);
        tab_tabla4.getColumna("REG_TLICEN").setNombreVisual("No. REGISTRO:");
	tab_tabla4.getColumna("FECHAF_TLICEN").setNombreVisual("FECHA CADUCA:");
        
	tab_tabla4.dibujar();
	PanelTabla pat_panel4 = new PanelTabla();
	pat_panel4.setPanelTabla(tab_tabla4);
        pat_panel4.setMensajeWarn("LICENICA DE FUNCIONAMIENTO ANUAL");

        
		Division div_horizontal = new Division();
		div_horizontal.dividir2(pat_panel2, pat_panel3, "50%", "V");
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(div_horizontal, pat_panel4, "35%", "H");
		agregarComponente(div_division);
        
    }

    @Override
    public void insertar() {
    tab_tabla1.insertar();
    tab_tabla2.insertar();
    }

    @Override
    public void guardar() {
    tab_tabla1.insertar();
    tab_tabla2.insertar();
    guardarPantalla();
    }

    @Override
    public void eliminar() {
    tab_tabla1.insertar();
    tab_tabla2.insertar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}
