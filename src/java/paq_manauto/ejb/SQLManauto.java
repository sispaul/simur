/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto.ejb;

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
    private Statement smt;

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

    // Metodo dinamico para construir un insert a partir de un Map con campos y valores
    public Integer insertar(String tabla, Map datos) {
        try {
            conPostgresql();
            String sql;
            StringBuilder campos = new StringBuilder();
            StringBuilder valores = new StringBuilder();

            for (Iterator it = datos.keySet().iterator(); it.hasNext();) {
                String llave = (String) it.next();
                campos.append(llave).append(",");
                if (datos.get(llave) instanceof Date) {
                    valores.append("'").append(new SimpleDateFormat("yyyy-MM-dd").format((Date) datos.get(llave))).append("',");
                } else {
                    valores.append("'").append(datos.get(llave).toString()).append("',");
                }
            }
            sql = "INSERT INTO " + tabla + " ("
                    + campos.toString().substring(0, campos.toString().length() - 1)
                    + ") VALUES ("
                    + valores.toString().substring(0, valores.toString().length() - 1)
                    + ")";

            int registrosAfectados = smt.executeUpdate(sql.toString());
            System.out.println("Registros afectados: " + registrosAfectados + " registros");
            desPostgresql();
            return registrosAfectados;

        } catch (Exception ex) {
            System.out.println("Error en la insercion");
        }
        return 0;
    }

    public synchronized Integer actualizar(String tabla, String pk_name, Integer pk, Map datos) {
        try {
            conPostgresql();
            StringBuilder campos = new StringBuilder();

            for (Iterator it = datos.keySet().iterator(); it.hasNext();) {
                String llave = (String) it.next();
                campos.append(llave).append("=");
                if (datos.get(llave) instanceof Date) {
                    campos.append("'")
                            .append(new java.sql.Date(((Date) datos.get(llave)).getTime()).toString()).append("',");
                } else {
                    campos.append("'")
                            .append(datos.get(llave).toString()).append("',");
                }
            }

            String sql = "UPDATE " + tabla + " SET "
                    + campos.toString().substring(0, campos.toString().length() - 1)
                    + " WHERE " + pk_name + "=" + pk;

            System.out.println(sql);

            int registrosAfectados = smt.executeUpdate(sql);
            System.out.println("Registros afectados: " + registrosAfectados + " registros");

            desPostgresql();
            return registrosAfectados;

        } catch (Exception ex) {
            System.out.println("Error en la actualizacion: " + ex.toString());
        }

        return 0;
    }
}
