/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class bloqueoRol {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Utilitario utilitario = new Utilitario();
    private Conexion con_postgres;

    public TablaGenerica verifSI(String dato) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT count(*) as cantidad,bloqueo_rol FROM cont_periodo_actual where bloqueo_rol like '" + dato + "' GROUP BY bloqueo_rol");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    private void con_postgresql() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
}
