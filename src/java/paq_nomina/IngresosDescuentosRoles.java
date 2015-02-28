/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
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
public class IngresosDescuentosRoles extends Pantalla {
    //atributo para conexion a base de datos

    private Conexion con_postgres = new Conexion();
    //atributos para combo
    private Combo comboParametros = new Combo();
    private Combo comboDistributivo = new Combo();
    private Combo comboAcciones = new Combo();
    //atributo para pantalla
    private Tabla tab_tabla = new Tabla();

    public IngresosDescuentosRoles() {

        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("srh_descuento", "ide_descuento", 1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
        comboParametros.setId("comboParametros");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "INGRESOS"
        };

        Object fila2[] = {
            "2", "DESCUENTOS"
        };
        
        Object fila3[] = {
            "3", "ANTICIPOS"
        };
        
        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        comboParametros.setCombo(lista);
        bar_botones.agregarComponente(new Etiqueta("Seleccione Parametro : "));
        bar_botones.agregarComponente(comboParametros);

        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(con_postgres);
        comboDistributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        comboDistributivo.setMetodo("mostrarColumna");
        bar_botones.agregarComponente(new Etiqueta("Distributivo : "));
        bar_botones.agregarComponente(comboDistributivo);

        comboAcciones.setId("comboAcciones");
        List list = new ArrayList();
        Object filas1[] = {
            "1", "MIGRAR A ROL"
        };

        Object filas2[] = {
            "2", "BORRAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        comboAcciones.setCombo(list);
        bar_botones.agregarComponente(new Etiqueta("Acción : "));
        comboDistributivo.setMetodo("accionesPantalla");
        bar_botones.agregarComponente(comboAcciones);

    }

    public void mostrarColumna() {
        System.err.println(comboParametros.getValue()+"");
        if (comboParametros.getValue().equals("3")) {
            System.err.println("Ing anticipo");
        } else {
            System.err.println("Ing Demas");
        }
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}
