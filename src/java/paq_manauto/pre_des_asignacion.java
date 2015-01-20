/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_des_asignacion extends Pantalla{

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    
    //tablas
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_accesorios = new Tabla();
    
    public pre_des_asignacion() {

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
