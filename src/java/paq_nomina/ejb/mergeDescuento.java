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
 * @author m-paucar
 */
@Stateless
public class mergeDescuento {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Utilitario utilitario = new Utilitario();
    private Conexion con_postgres;

    public void actualizarDescuento(Integer ano, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna) {
        // Forma el sql para el ingreso

        String str_sql1;
        str_sql1 = "update srh_descuento set ide_empleado=srh_empleado.cod_empleado "
                + "from  srh_empleado where srh_descuento.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " and srh_descuento.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " and "
                + "srh_descuento.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " and "
                + "srh_descuento.IDE_COLUMNA=" + ide_columna + " and srh_empleado.cedula_pass=srh_descuento.cedula";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void actualizarDescuento1(Integer ano, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna) {
        // Forma el sql para el ingreso

        String str_sql1;
        str_sql1 = "update srh_descuento set ide_empleado_rol=srh_roles.ide_empleado "
                + "from sRH_ROLES WHERE sRH_ROLES.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " AND sRH_ROLES.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " AND "
                + "sRH_ROLES.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " AND sRH_ROLES.IDE_COLUMNAS=" + ide_columna + " and "
                + "srh_roles.ide_empleado=srh_descuento.ide_empleado";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void borrarDescuento() {
        // Forma el sql para el ingreso

        String str_sql3 = "DELETE FROM srh_descuento";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void migrarDescuento(String empleado, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna, String nombre) {
        // Forma el sql para el ingreso

        String str_sql4 = "update SRH_ROLES set valor_egreso=srh_descuento.descuento"
                + ", valor=srh_descuento.descuento,ip_responsable='" + utilitario.getIp() + "',nom_responsable='" + nombre + "',fecha_responsable='" + utilitario.getFechaActual() + "'  from srh_descuento"//MODIFICACION
                + " WHERE SRH_ROLES.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " AND SRH_ROLES.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " AND"
                + " SRH_ROLES.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " AND SRH_ROLES.IDE_COLUMNAS=" + ide_columna + " and "
                + "srh_roles.ide_empleado='" + empleado + "'";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void ActualizaDatos(String cedula, Integer colum, Integer dis) {
        String str_sql4 = "update srh_descuento\n"
                + "set id_distributivo_roles =d1.id_distributivo ,\n"
                + "ano = " + utilitario.getAnio(utilitario.getFechaActual()) + " ,\n"
                + "ide_columna = " + colum + ",\n"
                + "ide_periodo = " + utilitario.getMes(utilitario.getFechaActual()) + ",\n"
                + "ide_empleado = d1.cod_empleado,\n"
                + "ide_empleado_rol =cast(d1.indentificacion_empleado as numeric) \n"
                + "from (SELECT\n"
                + "id_distributivo,\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "cod_empleado,\n"
                + "indentificacion_empleado\n"
                + "FROM\n"
                + "srh_empleado\n"
                + "WHERE\n"
                + "srh_empleado.cedula_pass = '" + cedula + "') d1\n"
                + "where srh_descuento.cedula = d1.cedula_pass and d1.id_distributivo =" + dis;
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void InsertarAnticipo(Integer tipo) {
        // Forma el sql para el ingreso
        String str_sql3 = "insert into srh_descuento (id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol) \n"
                + "select cast (id_distributivo as int)\n"
                + ",cast (anio as int)\n"
                + ",(case when id_distributivo = 1 then 1 when id_distributivo = 2 then 46 end ) AS dist\n"
                + ", cast (periodo as int)\n"
                + ",valor,ci_solicitante\n"
                + ",solicitante\n"
                + ",ide_empleado\n"
                + ",ide_empleado_solicitante \n"
                + "from ( \n"
                + "select * from ( \n"
                + "SELECT a.id_distributivo\n"
                + ",d.anio,sum(d.valor) as valor\n"
                + ",a.ci_solicitante, a.solicitante\n"
                + ", a.ide_empleado_solicitante as ide_empleado\n"
                + ", a.ide_empleado_solicitante\n"
                + ",d.periodo \n"
                + "FROM srh_detalle_anticipo d\n"
                + ", srh_solicitud_anticipo a \n"
                + ",srh_calculo_anticipo as f \n"
                + "WHERE d.ide_anticipo = a.ide_solicitud_anticipo \n"
                + "and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo  \n"
                + "AND d.periodo = '" + utilitario.getMes(utilitario.getFechaActual()) + "' \n"
                + "and a.id_distributivo = " + tipo + " \n"
                + "and d.ide_estado_cuota is null \n"
                + "and d.anio = '" + utilitario.getAnio(utilitario.getFechaActual()) + "' \n"
                + "and f.ide_estado_anticipo <> 5 \n"
                + "GROUP BY a.ci_solicitante\n"
                + ",a.id_distributivo\n"
                + ",d.anio, a.solicitante\n"
                + ", a.ide_empleado_solicitante\n"
                + ", a.ide_empleado_solicitante\n"
                + ",d.periodo \n"
                + "having count(a.ci_solicitante)<=1 order by a.solicitante) as a \n"
                + "UNION select * from ( \n"
                + "SELECT a.id_distributivo\n"
                + ",d.anio\n"
                + ",sum(d.valor) as valor\n"
                + ",a.ci_solicitante\n"
                + ", a.solicitante\n"
                + ", a.ide_empleado_solicitante as ide_empleado\n"
                + ", a.ide_empleado_solicitante \n"
                + ",d.periodo \n"
                + "FROM srh_detalle_anticipo d\n"
                + ", srh_solicitud_anticipo a\n"
                + ",srh_calculo_anticipo as f \n"
                + "WHERE d.ide_anticipo = a.ide_solicitud_anticipo \n"
                + "and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo \n"
                + "AND d.periodo = '" + utilitario.getMes(utilitario.getFechaActual()) + "' \n"
                + "and a.id_distributivo = " + tipo + " \n"
                + "and d.ide_estado_cuota is null \n"
                + "and d.anio = '" + utilitario.getAnio(utilitario.getFechaActual()) + "'\n"
                + "and f.ide_estado_anticipo <> 5 \n"
                + "GROUP BY a.ci_solicitante\n"
                + ",a.id_distributivo\n"
                + ",d.anio\n"
                + ", a.solicitante\n"
                + ", a.ide_empleado_solicitante\n"
                + ", a.ide_empleado_solicitante\n"
                + ",d.periodo \n"
                + "having count(a.ci_solicitante)>1 order by a.solicitante) as b ) as c order by solicitante";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setSubsidioFamiliar(Integer tipo, Double porcentaje) {
        // Forma el sql para el ingreso
        String str_sql3 = "insert into srh_descuento(id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol)\n"
                + "select a.id_distributivo," + utilitario.getAnio(utilitario.getFechaActual()) + " as anio," + tipo + " as columna," + utilitario.getMes(utilitario.getFechaActual()) + " as periodo,"
                + "cast(to_char((((a.sueldo_basico*" + porcentaje + ")/100)*b.numero_hijos),'99G999.99') as numeric) as descuento,a.cedula_pass,a.nombres,a.cod_empleado,b.cod_empleado as id_empleado\n"
                + "from \n"
                + "(SELECT\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "id_distributivo,\n"
                + "cod_empleado,\n"
                + "sueldo_basico,\n"
                + "estado\n"
                + "from srh_empleado\n"
                + ")as a\n"
                + "inner join(\n"
                + "select count(p.cod_empleado) as numero_hijos,p.cod_empleado from (\n"
                + "SELECT DISTINCT\n"
                + "nombres_apellidos,\n"
                + "parentesco,\n"
                + "fecha_nacimiento,\n"
                + "cod_empleado\n"
                + "from srh_cargas\n"
                + "where cast((select to_char(age(fecha_nacimiento::date),'YY'))as numeric)<18\n"
                + "order by cod_empleado ) as p\n"
                + "GROUP BY p.cod_empleado) as b\n"
                + "on a.cod_empleado =b.cod_empleado\n"
                + "where a.id_distributivo = 2 and a.estado = 1\n"
                + "order by a.nombres";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setSubsidioAntiguedad(Integer columna, Double porcentaje,Integer codigo,Integer dis) {
        // Forma el sql para el ingreso
        String str_sql3 = "insert into srh_descuento (id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol)\n"
                + "SELECT \n"
                + "e.id_distributivo,\n"
                + ""+utilitario.getAnio(utilitario.getFechaActual())+" as anio,\n"
                + ""+columna+" as columna,\n"
                + ""+utilitario.getMes(utilitario.getFechaActual())+" as periodo,\n"
                + "cast(to_char((((cast(e.remuneracion as numeric)*0.25)/100)*cast((select extract(year from age('"+utilitario.getFechaActual()+"'::date ,n.fecha_contrato::date)))as numeric)),'99G999.99')as numeric) as valor,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.cod_empleado,\n"
                + "n.cod_empleado as id_empleado\n"
                + "FROM srh_empleado e\n"
                + "INNER JOIN srh_num_contratos n ON n.cod_empleado = e.cod_empleado\n"
                + "WHERE e.estado = 1 AND\n"
                + "e.id_distributivo = "+dis+" AND\n"
                + "n.fecha_contrato > '2005-01-01' and n.fecha_contrato <'"+utilitario.getAnio(utilitario.getFechaActual())+"-01-01'\n"
                + "and e.cod_empleado = "+codigo+"\n"
                + "ORDER BY e.nombres limit 1";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public TablaGenerica sumaPeriodo() {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT sum(d.valor) as total\n"
                + "FROM  \n"
                + "srh_detalle_anticipo d,  \n"
                + "srh_periodo_anticipo q,  \n"
                + "srh_solicitud_anticipo a\n"
                + "WHERE  \n"
                + "d.ide_periodo_descuento = q.ide_periodo_anticipo AND  \n"
                + "d.ide_anticipo = a.ide_solicitud_anticipo AND  \n"
                + "d.ide_periodo_descuento  = " + utilitario.getMes(utilitario.getFechaActual()) + " and   \n"
                + "q.anio like '" + utilitario.getAnio(utilitario.getFechaActual()) + "'\n"
                + "GROUP BY q.periodo");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica periodo(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo=" + periodo);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica getColumnas(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_col,descripcion_col,distributivo,porcentaje_subsidio from srh_columnas where ide_col = " + periodo);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica getTrabajadores(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cedula_pass,nombres,cod_empleado\n"
                + "from srh_empleado\n"
                + "where estado=1 and id_distributivo = " + periodo);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica Suma() {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT sum(descuento) as total \n"
                + "FROM srh_descuento where ano = '" + utilitario.getAnio(utilitario.getFechaActual()) + "' and ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()));
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica distibutivo(Integer distri) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT id_distributivo,descripcion FROM srh_tdistributivo where id_distributivo=" + distri);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica columnas(Integer colum) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=" + colum);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica VerificarRol() {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_roles,ide_columnas FROM srh_roles where ano = " + utilitario.getAnio(utilitario.getFechaActual()) + " and ide_periodo = " + utilitario.getMes(utilitario.getFechaActual()));
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica Direc_Asist(Integer codigo) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n"
                + "c.cod_cargo,\n"
                + "c.nombre_cargo,\n"
                + "e.cedula_pass,\n"
                + "e.nombres\n"
                + "FROM srh_cargos c\n"
                + "INNER JOIN srh_empleado e ON e.cod_cargo = c.cod_cargo\n"
                + "WHERE c.cod_cargo =" + codigo);//299
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }

    public TablaGenerica getConfirmaDatos(String anio, Integer codigo, String empleado, Integer distributivo) {
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_roles,ide_empleado,id_distributivo_roles\n"
                + "FROM srh_roles\n"
                + "where ano = '" + anio + "' and ide_periodo = " + codigo + " and ide_empleado = '" + empleado + "' and id_distributivo_roles = " + distributivo);
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
