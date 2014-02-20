/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_gestores extends Pantalla{
private Tabla tab_comercial = new Tabla();
private Tabla tab_gestor = new Tabla();

    public pre_gestores() {
        
        tab_comercial.setId("tab_comercial");
        tab_comercial.setTabla("trans_comercial_automotores", "ide_comercial_automotores", 1);
        tab_comercial.setHeader("Datos de Empresas");
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("Nombre de Establecimiento");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
        tab_comercial.getColumna("direccion_empresa").setNombreVisual("Dirección");
        tab_comercial.getColumna("telefono_empresa").setNombreVisual("Teléfono");
        tab_comercial.getGrid().setColumns(2);
        tab_comercial.agregarRelacion(tab_gestor);
        tab_comercial.dibujar();
        PanelTabla pat_comercial = new PanelTabla();
        pat_comercial.setPanelTabla(tab_comercial);
                
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.setHeader("Datos de Gestores");
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("Cedula");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("Nombre");
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor);

        Division div_division = new Division();
        div_division.dividir2(pat_comercial, pat_gestor, "50%", "H");
        agregarComponente(div_division);
 
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
                if (tab_comercial.guardar()) {
            if (tab_gestor.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_comercial() {
        return tab_comercial;
    }

    public void setTab_comercial(Tabla tab_comercial) {
        this.tab_comercial = tab_comercial;
    }

    public Tabla getTab_gestor() {
        return tab_gestor;
    }

    public void setTab_gestor(Tabla tab_gestor) {
        this.tab_gestor = tab_gestor;
    }
    
}
