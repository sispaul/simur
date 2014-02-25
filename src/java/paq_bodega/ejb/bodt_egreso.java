/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author Paolo Benavides
 */
@Stateless
public class bodt_egreso {
    
    private Utilitario utilitario = new Utilitario();
    private Conexion con_postgres = new Conexion();
    
     public String egresoMax() {
         con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
         con_postgres.NOMBRE_MARCA_BASE = "postgres";
         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         tab_consulta.setConexion(con_postgres);
//         TablaGenerica tab_ejecuta=new TablaGenerica();
//        tab_ejecuta.setConexion(con_postgres);
//        tab_ejecuta.setSql(consulta);
//        tab_ejecuta.ejecutarSql();
         tab_consulta.setSql("select \"max\"(numero_egreso) from bodt_concepto_egreso");
         System.out.println("Agrego Script");
         tab_consulta.ejecutarSql();
         System.out.println("Ejecuta Script");
         ValorMax = tab_consulta.getValor("uso")+"";
         System.out.println(ValorMax);
         return ValorMax;
     }
     
     private void conectar() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
     }
}
