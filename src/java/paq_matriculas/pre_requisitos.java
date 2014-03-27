/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author KEJA
 */
public class pre_requisitos extends Pantalla{
private Tabla tab_vehiculo = new Tabla();
private Tabla tab_servicio = new Tabla();
private Tabla tab_requisitos = new Tabla();

    public pre_requisitos() {
        tab_vehiculo.setId("tab_vehiculo");
        tab_vehiculo.setHeader("INGRESO DE REQUISITOS PARA AUTOMOTORES");
        tab_vehiculo.setTabla("trans_tipo_vehiculo", "ide_tipo_vehiculo", 1);
        tab_vehiculo.getColumna("ide_tipo_vehiculo").setNombreVisual("ID");
        tab_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("TIPO DE VEHICULO");
        tab_vehiculo.agregarRelacion(tab_servicio);
        tab_vehiculo.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_vehiculo);
        tab_vehiculo.setStyle(null);
        tabp.setStyle("width:100%;overflow: auto;");
      
        tab_servicio.setId("tab_servicio");
        tab_servicio.setHeader("TIPO DE AUTOMOTOR");
        tab_servicio.setTabla("trans_tipo_servicio", "ide_tipo_servicio", 2);
        tab_servicio.getColumna("ide_tipo_servicio").setNombreVisual("ID");
        tab_servicio.getColumna("descripcion_servicio").setNombreVisual("TIPO DE SERVICIO");
        tab_servicio.getColumna("descripcion_servicio").setMayusculas(true);
        tab_servicio.agregarRelacion(tab_requisitos);
        tab_servicio.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_servicio);

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_requisitos.setId("tab_requisitos");
        tab_requisitos.setIdCompleto("tab_tabulador:tab_requisitos");
        tab_requisitos.setTabla("trans_tipo_requisito", "ide_tipo_requisito", 3);
        tab_requisitos.getColumna("ide_tipo_requisito").setNombreVisual("ID");
        tab_requisitos.getColumna("decripcion_requisito").setNombreVisual("TIPO DE REQUISITO");
        tab_requisitos.getColumna("decripcion_requisito").setMayusculas(true);
        tab_requisitos.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_requisitos);
        tab_tabulador.agregarTab("REQUISITOS PARA AUTOMOTORES", tabp2);

        Division div = new Division();
        div.dividir3(tabp, tabp1, tab_tabulador, "26%", "50%", "H");
        agregarComponente(div);
        
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_vehiculo.guardar()) {
            if (tab_servicio.guardar()) {
                if (tab_requisitos.guardar()) {
                            guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_vehiculo() {
        return tab_vehiculo;
    }

    public void setTab_vehiculo(Tabla tab_vehiculo) {
        this.tab_vehiculo = tab_vehiculo;
    }

    public Tabla getTab_servicio() {
        return tab_servicio;
    }

    public void setTab_servicio(Tabla tab_servicio) {
        this.tab_servicio = tab_servicio;
    }

    public Tabla getTab_requisitos() {
        return tab_requisitos;
    }

    public void setTab_requisitos(Tabla tab_requisitos) {
        this.tab_requisitos = tab_requisitos;
    }
   
}
