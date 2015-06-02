/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto.ejb;

import framework.aplicacion.TablaGenerica;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class SQLManauto {

    private Conexion conSql,//Conexion a la base de sigag
            conPostgres;//Cnexion a la base de postgres 2014
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica getEstrucTabla(String nombre) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("SELECT ordinal_position,column_name, data_type\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '"+nombre+"'");
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    //sentencia de conexion a base de datos
    private void conSigag() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desSigag() {
        if (conSql != null) {
            conSql.desconectar();
            conSql = null;
        }
    }

    private void conPostgresql() {
        if (conPostgres == null) {
            conPostgres = new Conexion();
            conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }

    private void desPostgresql() {
        if (conPostgres != null) {
            conPostgres.desconectar();
            conPostgres = null;
        }
    }
}
