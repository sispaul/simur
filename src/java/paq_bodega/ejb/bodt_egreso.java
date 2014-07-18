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
    private Conexion con_postgres;
    
     public String egresoMax() {
         conectar();

         String ValorMax;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select 0 as id, (case when max(ide_egreso) is null then 1 else max(ide_egreso)+1 end) as maximo from bodt_concepto_egreso");
         tab_consulta.ejecutarSql();
         ValorMax = tab_consulta.getValor("maximo");
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
