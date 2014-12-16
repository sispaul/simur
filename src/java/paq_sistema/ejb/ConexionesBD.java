/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema.ejb;

import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class ConexionesBD {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Conexion 
            con_sql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres,//Cnexion a la base de postgres 2014
            con_ciudadania; //Conexion a la base de ciudadania
    private Utilitario utilitario = new Utilitario();
    
    private void con_sigag(){
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia("recursojdbc");
        }
    }
    
    private void con_mantenimiento(){
        if(con_manauto == null){
            con_manauto = new Conexion();
            con_manauto.setUnidad_persistencia("poolSqlmanAuto");
        }
    }
    
    private void con_postgresql(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
    
    private void con_ciudadanos(){
        if(con_ciudadania == null){
            con_ciudadania = new Conexion();
            con_ciudadania.setUnidad_persistencia("ciudadania");
        }
    }
}
