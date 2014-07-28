/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_aprobacion_anticipos extends Pantalla{

    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_garante = new Tabla();
    private Tabla tab_parametros = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    public pre_aprobacion_anticipos() {
        
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
