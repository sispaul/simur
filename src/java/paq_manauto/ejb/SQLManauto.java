/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto.ejb;

import framework.aplicacion.TablaGenerica;
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

    public TablaGenerica getNumeroCampos(String nombre) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("SELECT table_name,count(*) As NumeroCampos\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "'\n"
                + "GROUP BY table_name");
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public TablaGenerica getCatalogoDato(String datos, String tabla, String condicion) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("select " + datos + " from " + tabla + " where " + condicion + "");
        tab_funcionario.ejecutarSql();
        desPostgresql();
        return tab_funcionario;
    }

    public TablaGenerica getEstrucTabla(String nombre, Integer posicion) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("SELECT ordinal_position,column_name, data_type\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "' and ordinal_position = " + posicion);
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public void setActuaRegis(Integer codigo, String desc, String dato, String valor, String cadena) {
        String au_sql = "update " + desc + " set\n"
                + "" + dato + " ='" + valor + "'\n" +//mve_horometro
                "where " + cadena + "=" + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(au_sql);
        desPostgresql();
    }

    public TablaGenerica getChofer(String cedula) {
        conPostgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conPostgres);
        tab_persona.setSql("SELECT cod_empleado, cedula_pass,nombres, 1 as activo\n"
                + "FROM srh_empleado\n"
                + "where cod_empleado ='" + cedula + "' and estado = 1\n"
                + "order by nombres");
        tab_persona.ejecutarSql();
        desPostgresql();
        return tab_persona;
    }

    public TablaGenerica getDuplicaMarca(String nombre) {
        conPostgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        conPostgresql();
        tab_persona.setConexion(conPostgres);
        tab_persona.setSql("SELECT mvmarca_id,mvmarca_descripcion,mvmarca_estado\n"
                + "FROM mvmarca_vehiculo\n"
                + "where mvmarca_descripcion like '" + nombre + "'");
        tab_persona.ejecutarSql();
        desPostgresql();
        return tab_persona;
    }

    public void setMarca(String nombre, String login) {
        String parametro = "insert into mvmarca_vehiculo (mvmarca_descripcion,mvmarca_estado,mvmarca_fechaing,mvmarca_loginin)\n"
                + "values ('" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setDeleteMarcas(Integer anti) {
        String au_sql = "delete from mvmarca_vehiculo where mvmarca_id =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(au_sql);
        desPostgresql();
    }

    public TablaGenerica getDuplicaTipo(String nombre, Integer codigo) {
        conPostgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        conPostgresql();
        tab_persona.setConexion(conPostgres);
        tab_persona.setSql("SELECT mvtipo_id,mvtipo_descripcion FROM mvtipo_vehiculo where mvmarca_id = " + codigo + " and mvtipo_descripcion like '" + nombre + "'");
        tab_persona.ejecutarSql();
        desPostgresql();
        return tab_persona;
    }

    public void setTipo(String nombre, String login, Integer marca) {
        String parametro = "insert into mvtipo_vehiculo (mvmarca_id,mvtipo_descripcion,mvtipo_estado,mvtipo_fechaing,mvtipo_loginin)\n"
                + "values (" + marca + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setDeleteTipos(Integer anti) {
        String au_sql = "delete from mvtipo_vehiculo where mvtipo_id =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(au_sql);
        desPostgresql();
    }

    public TablaGenerica getDuplicaModelo(String nombre, Integer codigo) {
        conPostgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        conPostgresql();
        tab_persona.setConexion(conPostgres);
        tab_persona.setSql("SELECT\n"
                + "mvmodelo_id,\n"
                + "mvmodelo_descripcion\n"
                + "FROM\n"
                + "mvmodelo_vehiculo\n"
                + "where mvmodelo_descripcion ='" + nombre + "' and  MVTIPO_ID=" + codigo);
        tab_persona.ejecutarSql();
        desPostgresql();
        return tab_persona;
    }

    public void setModelo(String nombre, String login, Integer tipo) {
        String parametro = "insert into mvmodelo_vehiculo(mvtipo_id,mvmodelo_descripcion,mvmodelo_estado,mvmodelo_fechaing,mvmodelo_loginin)\n"
                + "values (" + tipo + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setDeleteModelos(Integer anti) {
        String au_sql = "delete from mvmodelo_vehiculo where mvmodelo_id =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(au_sql);
        desPostgresql();
    }

    public TablaGenerica getDuplicaVersion(String nombre, Integer codigo) {
        conPostgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        conPostgresql();
        tab_persona.setConexion(conPostgres);
        tab_persona.setSql("SELECT\n"
                + "mvversion_id,\n"
                + "mvversion_descripcion\n"
                + "FROM mvversion_vehiculo\n"
                + "where mvmodelo_id =" + codigo + " and mvversion_descripcion='" + nombre + "'");
        tab_persona.ejecutarSql();
        desPostgresql();
        return tab_persona;
    }

    public void setVersion(String nombre, String login, Integer modelo) {
        String parametro = "insert into mvversion_vehiculo(mvmodelo_id,mvversion_descripcion,mvversion_estado,mvversion_fechaing,mvversion_loginin)\n"
                + "values (" + modelo + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setDeleteversion(Integer anti) {
        String au_sql = "delete from mvversion_vehiculo where mvversion_id =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(au_sql);
        desPostgresql();
    }

    //sentencia de conexion a base de datos
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
