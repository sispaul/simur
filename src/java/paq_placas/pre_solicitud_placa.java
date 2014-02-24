/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_placa extends Pantalla{
private Tabla tab_solicitud = new Tabla();
    public pre_solicitud_placa() {
 tab_solicitud.setId("tab_solicitud");
 tab_solicitud.setTabla("trans_solicitud_placa", "ide_solicitud_placa", 1);
 
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
}
