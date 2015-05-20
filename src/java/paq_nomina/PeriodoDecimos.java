/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class PeriodoDecimos extends Pantalla {

    private Tabla tabPeriodo = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Panel panOpcion = new Panel();

    public PeriodoDecimos() {
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tabPeriodo.setId("tabPeriodo");
        tabPeriodo.setConexion(conPostgres);
        tabPeriodo.setTabla("srh_periodo_sueldo", "periodo_id", 1);
        List list = new ArrayList();
        Object fil1[] = {
            "D3","D3R"
        };
        Object fil2[] = {
            "D4","D4R"
        };
        list.add(fil1);
        list.add(fil2);
        tabPeriodo.getColumna("periodo_columna").setCombo(list);
        
        List lista = new ArrayList();
        Object fila1[] = {
            "S", "SI"
        };
        Object fila2[] = {
            "N", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabPeriodo.getColumna("periodo_estado").setRadio(lista,"S");
        tabPeriodo.setRows(15);
        tabPeriodo.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabPeriodo);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTA DE PERIODOS PARA CALCULO DE DECIMOS");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);

    }

    @Override
    public void insertar() {
        tabPeriodo.insertar();
    }

    @Override
    public void guardar() {
        if (tabPeriodo.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabPeriodo.eliminar();
    }

    public Tabla getTabPeriodo() {
        return tabPeriodo;
    }

    public void setTabPeriodo(Tabla tabPeriodo) {
        this.tabPeriodo = tabPeriodo;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }
}
