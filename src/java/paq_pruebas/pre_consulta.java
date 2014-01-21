/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_consulta extends Pantalla {

    private Tabla tab_consulta = new Tabla();

    public pre_consulta() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT IDE_CMREP,NOMBRES_APELLIDOS_CMREP,DIRECCION_CMREP,DETALLE_CMTID,DOCUMENTO_IDENTIDAD_CMREP FROM CMT_REPRESENTANTE repre\n"
                + "INNER JOIN  CMT_TIPO_DOCUMENTO doc on repre.IDE_CMTID=doc.IDE_CMTID");

        tab_consulta.setCampoPrimaria("IDE_CMREP");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);

        agregarComponente(pat_panel);

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

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }
}
