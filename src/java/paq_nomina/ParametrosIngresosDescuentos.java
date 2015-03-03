/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Division;
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
public class ParametrosIngresosDescuentos extends Pantalla{

    private Conexion conPostgres = new Conexion();
    private Tabla tabParametros = new Tabla();
    
    public ParametrosIngresosDescuentos() {
        
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";
        
        tabParametros.setId("tabParametros");
        tabParametros.setConexion(conPostgres);
        tabParametros.setTabla("srh_columnas", "ide_col", 1);
        List list = new ArrayList();
        Object filas1[] = {
            "1", "EMPLEADO"
        };
        Object filas2[] = {
            "2", "TRABAJADOR"
        };
        list.add(filas1);;
        list.add(filas2);;
        tabParametros.getColumna("distributivo").setCombo(list);
        tabParametros.getColumna("distributivo").setLectura(true);
        tabParametros.getColumna("descripcion_col").setLectura(true);
        tabParametros.getColumna("titulo_col").setVisible(false);
        tabParametros.getColumna("codigo_col").setVisible(false);
        tabParametros.getColumna("ingreso_descuento").setVisible(false);
        tabParametros.getColumna("formula").setVisible(false);
        tabParametros.getColumna("impuesto_renta").setVisible(false);
        tabParametros.getColumna("activa").setVisible(false);
        tabParametros.getColumna("tipo_col").setVisible(false);
        tabParametros.getColumna("ip_responsable").setVisible(false);
        tabParametros.getColumna("nom_responsable").setVisible(false);
        tabParametros.getColumna("fecha_responsable").setVisible(false);
        tabParametros.getColumna("rollover").setVisible(false);
        tabParametros.getColumna("calcula").setVisible(false);
        tabParametros.getColumna("orden").setVisible(false);
        tabParametros.dibujar();
        PanelTabla panPanel = new PanelTabla();
        panPanel.setPanelTabla(tabParametros);

        Division divDivision = new Division();
        divDivision.setId("divDivision");
        divDivision.dividir1(panPanel);
        agregarComponente(divDivision);
        
    }

    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {
        if (tabParametros.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {

    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabParametros() {
        return tabParametros;
    }

    public void setTabParametros(Tabla tabParametros) {
        this.tabParametros = tabParametros;
    }
    
}
