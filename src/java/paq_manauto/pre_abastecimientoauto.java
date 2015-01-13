/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_abastecimientoauto extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Para tabla de
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    public pre_abastecimientoauto() {
        //desactiva botones de navegación
        bar_botones.quitarBotonsNavegacion();
        
        //datos de usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        
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
