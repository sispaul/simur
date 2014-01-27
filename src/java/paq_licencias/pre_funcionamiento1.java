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
public class pre_funcionamiento1 extends Pantalla{
private Tabla tab_tabla1 = new Tabla();
private Tabla tab_tabla2 = new Tabla();

   
    public pre_funcionamiento1() {
        
                
        tab_tabla1.setId("tab_tabla1");
	tab_tabla1.setTipoFormulario(true);
	tab_tabla1.setTabla("TUR_REPRESENTA_ESTABLECIMIENTO", "IDE_RESTABLE", 1);
	tab_tabla1.getColumna("ID_TESTABL").setCombo("SELECT ID_TESTABL,NOMB_TESTABL from TUR_ESTABLECIMIENTO");
        tab_tabla1.getColumna("IDE_CMREP").setCombo("select IDE_CMREP,NOMBRES_APELLIDOS_CMREP from CMT_REPRESENTANTE");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
	PanelTabla pat_panel1 = new PanelTabla();
	pat_panel1.setMensajeWarn("DATOS DE ESTABLECIMIENTO");
	pat_panel1.setPanelTabla(tab_tabla1);
        
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTipoFormulario(true);
	tab_tabla2.setTabla("tur_licencia", "ID_TLICEN", 2);
	tab_tabla2.getColumna("ID_TCATEG").setCombo("select ID_TCATEG,DESC_TCATEG from tur_categoria");
        tab_tabla2.getColumna("NUM_TLICEN").setNombreVisual("No. LICENCIA:");
        tab_tabla2.getColumna("FECHAI_TLICEN").setLectura(true);
        tab_tabla2.getColumna("REG_TLICEN").setNombreVisual("No. REGISTRO:");
	tab_tabla2.getColumna("FECHAF_TLICEN").setNombreVisual("FECHA CADUCA:");
        
	tab_tabla2.dibujar();
	PanelTabla pat_panel3 = new PanelTabla();
	pat_panel3.setPanelTabla(tab_tabla2);
        pat_panel3.setMensajeWarn("LICENICA DE FUNCIONAMIENTO ANUAL");
	pat_panel3.getMenuTabla().getItem_eliminar().setRendered(false);

		Division div_horizontal = new Division();
		div_horizontal.dividir2(pat_panel1, null, "99%", "V");
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(div_horizontal, pat_panel3, "35%", "H");
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
