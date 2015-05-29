/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina.ejb;

import framework.aplicacion.TablaGenerica;
import java.math.BigDecimal;
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
    private Conexion conPostgres;

    public void actualizarDescuento(Integer ano, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna) {
        // Forma el sql para el ingreso

        String strSql;
        strSql = "update srh_descuento set ide_empleado=srh_empleado.cod_empleado "
                + "from  srh_empleado where srh_descuento.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " and srh_descuento.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " and "
                + "srh_descuento.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " and "
                + "srh_descuento.IDE_COLUMNA=" + ide_columna + " and srh_empleado.cedula_pass=srh_descuento.cedula";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void actualizarDescuento1(Integer ano, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna) {
        // Forma el sql para el ingreso

        String strSql;
        strSql = "update srh_descuento set ide_empleado_rol=srh_roles.ide_empleado "
                + "from sRH_ROLES WHERE sRH_ROLES.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " AND sRH_ROLES.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " AND "
                + "sRH_ROLES.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " AND sRH_ROLES.IDE_COLUMNAS=" + ide_columna + " and "
                + "srh_roles.ide_empleado=srh_descuento.ide_empleado";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void borrarDescuento() {
        // Forma el sql para el ingreso

        String strSql = "delete from srh_descuento";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void migrarDescuento(String empleado, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna, String nombre) {
        // Forma el sql para el ingreso

        String strSql = "update SRH_ROLES set valor_egreso=srh_descuento.descuento"
                + ", valor=srh_descuento.descuento,ip_responsable='" + utilitario.getIp() + "',nom_responsable='" + nombre + "',fecha_responsable='" + utilitario.getFechaActual() + "'  from srh_descuento"//MODIFICACION
                + " WHERE SRH_ROLES.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " AND SRH_ROLES.IDE_PERIODO=" + utilitario.getMes(utilitario.getFechaActual()) + " AND"
                + " SRH_ROLES.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_roles + " AND SRH_ROLES.IDE_COLUMNAS=" + ide_columna + " and "
                + "srh_roles.ide_empleado='" + empleado + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setActuHisFondos(Integer anio, Integer periodo, Integer persona, Double descuento, String nombre) {
        // Forma el sql para el ingreso

        String strSql = "update srh_valor_roles_historial\n"
                + "set \n"
                + "descuento =" + descuento + ",\n"
                + "fondos_reserva ='" + nombre + "' \n"
                + "where ano =" + anio + " and ide_periodo =" + periodo + " and ide_empleado =" + persona;
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setAcumulado(String codigo, Double descuento) {
        // Forma el sql para el ingreso

        String strSql = "update srh_autorizacion_acumulacion\n"
                + "set autoriza_acumulado_cuarto ="+descuento+" \n"
                + "where autoriza_cod_empleado = '"+codigo+"'";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setmigrarDescuento(String empleado, Integer ide_periodo, Integer id_distributivo_roles, Integer ide_columna,
            String nombre, String desc, Integer anio, Double valor) {
        // Forma el sql para el ingreso

        String strSql = "update srh_roles \n"
                + "set " + desc + "=" + valor + "\n"
                + ",valor=" + valor + "\n"
                + ",ip_responsable='" + utilitario.getIp() + "'\n"
                + ",nom_responsable='" + nombre + "'\n"
                + ",fecha_responsable='" + utilitario.getFechaActual() + "'\n"
                + "from srh_descuento\n"
                + "WHERE srh_roles.ano=" + anio + "\n"
                + "AND srh_roles.ide_periodo=" + ide_periodo + "\n"
                + "AND srh_roles.id_distributivo_roles=" + id_distributivo_roles + "\n"
                + "AND srh_roles.ide_columnas=" + ide_columna + "\n"
                + "AND srh_roles.ide_empleado='" + empleado + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void ActualizaDatos(String cedula, Integer colum, Integer dis) {
        String strSql = "update srh_descuento\n"
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
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setHoras100_50(String cedula, Integer colum, Integer dis, Double valor, Double parametro) {
        String strSql = "update srh_descuento \n"
                + "set id_distributivo_roles =d1.id_distributivo , \n"
                + "ano = '" + utilitario.getAnio(utilitario.getFechaActual()) + "', \n"
                + "ide_columna =" + colum + " , \n"
                + "ide_periodo = '" + utilitario.getMes(utilitario.getFechaActual()) + "', \n"
                + "ide_empleado = d1.cod_empleado, \n"
                + "ide_empleado_rol =d1.indentificacion_empleado,\n"
                + "descuento = (" + valor + "*((d1.remuneracion/240)*" + parametro + "))\n"
                + "from (SELECT\n"
                + "id_distributivo,\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "cod_empleado,\n"
                + "cod_empleado AS indentificacion_empleado,\n"
                + "remuneracion\n"
                + "FROM  srh_empleado"
                + " where cedula_pass ='" + cedula + "') d1 \n"
                + "where srh_descuento.cedula = d1.cedula_pass and d1.id_distributivo =" + dis;
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setHoras25(String cedula, Integer colum, Integer dis, Double valor) {
        String strSql = "update srh_descuento \n"
                + "set id_distributivo_roles =d1.id_distributivo , \n"
                + "ano = '" + utilitario.getAnio(utilitario.getFechaActual()) + "', \n"
                + "ide_columna =" + colum + " , \n"
                + "ide_periodo = '" + utilitario.getMes(utilitario.getFechaActual()) + "', \n"
                + "ide_empleado = d1.cod_empleado, \n"
                + "ide_empleado_rol =d1.indentificacion_empleado,\n"
                + "descuento = (" + valor + "*((((d1.remuneracion/240)*25)/100)+(d1.remuneracion/240)))\n"
                + "from (SELECT\n"
                + "id_distributivo,\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "cod_empleado,\n"
                + "cod_empleado AS indentificacion_empleado,\n"
                + "remuneracion\n"
                + "FROM  srh_empleado"
                + " where cedula_pass ='" + cedula + "') d1 \n"
                + "where srh_descuento.cedula = d1.cedula_pass and d1.id_distributivo =" + dis;
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void InsertarAnticipo(Integer tipo) {
        // Forma el sql para el ingreso
        String strSql = "insert into srh_descuento (id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol) \n"
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
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setSubsidioFamiliar(Integer tipo, Double porcentaje, Double sueldo) {
        // Forma el sql para el ingreso
        String strSql = "insert into srh_descuento(id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol)\n"
                + "select a.id_distributivo," + utilitario.getAnio(utilitario.getFechaActual()) + " as anio," + tipo + " as columna," + utilitario.getMes(utilitario.getFechaActual()) + " as periodo,"
                + "cast(to_char((((" + sueldo + "*" + porcentaje + ")/100)*b.numero_hijos),'99G999.99') as numeric) as descuento,a.cedula_pass,a.nombres,a.cod_empleado,b.cod_empleado as id_empleado\n"
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
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setSubsidioAntiguedad(Integer columna, Double porcentaje, Integer codigo, Integer dis) {
        // Forma el sql para el ingreso
        String strSql = "insert into srh_descuento (id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol)\n"
                + "SELECT \n"
                + "e.id_distributivo,\n"
                + "" + utilitario.getAnio(utilitario.getFechaActual()) + " as anio,\n"
                + "" + columna + " as columna,\n"
                + "" + utilitario.getMes(utilitario.getFechaActual()) + " as periodo,\n"
                + "cast(to_char((((cast(e.remuneracion as numeric)*" + porcentaje + ")/100)*cast((select extract(year from age('" + utilitario.getFechaActual() + "'::date ,n.fecha_contrato::date)))as numeric)),'99G999.99')as numeric) as valor,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.cod_empleado,\n"
                + "n.cod_empleado as id_empleado\n"
                + "FROM srh_empleado e\n"
                + "INNER JOIN srh_num_contratos n ON n.cod_empleado = e.cod_empleado\n"
                + "WHERE e.estado = 1 AND\n"
                + "e.id_distributivo = " + dis + " AND\n"
                + "n.fecha_contrato > '2005-01-01' and n.fecha_contrato <'" + utilitario.getAnio(utilitario.getFechaActual()) + "-01-01'\n"
                + "and e.cod_empleado = " + codigo + "\n"
                + "ORDER BY e.nombres limit 1";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setHistoricoFondos(Integer codigo, String fondos) {
        // Forma el sql para el ingreso
        String strSql = "insert into srh_valor_roles_historial (id_distributivo_roles,ano,ide_columna,ide_periodo,valor,descuento,cedula,nombres,ide_empleado,ide_empleado_rol,fondos_reserva)\n"
                + "SELECT id_distributivo_roles,ano,ide_columna,ide_periodo,valor,descuento,cedula,nombres,ide_empleado,ide_empleado_rol," + fondos + "\n"
                + "from srh_descuento\n"
                + "where ide_empleado =" + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
//        System.err.println(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setFondosReserva(Integer columna, Double porcentaje, Integer dis, String col, Integer col1, Integer col2, Integer codigo) {
        // Forma el sql para el ingreso
        String strSql = "insert into srh_descuento(id_distributivo_roles,ano,ide_columna,ide_periodo,descuento,cedula,nombres,ide_empleado,ide_empleado_rol)\n"
                + "select id_distributivo," + utilitario.getAnio(utilitario.getFechaActual()) + "," + columna + "," + utilitario.getMes(utilitario.getFechaActual()) + ",cast(to_char((((RMU+HORAS_EXTRAS+SUB_ROGACION)*" + porcentaje + ")/100),'99G999.99')as numeric) as valor,cedula_pass,nombres,cod_empleado,cod_empleado from \n"
                + "(select aa.*,\n"
                + "(case when a.HORAS_EXTRAS is NULL then '0' when a.HORAS_EXTRAS > 0 then a.HORAS_EXTRAS end ) as HORAS_EXTRAS\n"
                + ",(case when b.SUB_ROGACION is NULL then '0' when b.SUB_ROGACION > 0 then b.SUB_ROGACION end ) as SUB_ROGACION\n"
                + "from \n"
                + "(select DISTINCT e.id_distributivo,e.cod_empleado,e.cedula_pass,e.nombres,r.valor AS RMU\n"
                + "from srh_roles as r inner join prec_programas as  p\n"
                + "on r.ide_programa=p.ide_programa\n"
                + "inner join srh_empleado as e\n"
                + "on e.cod_empleado=r.ide_empleado\n"
                + "inner join srh_cargos  as c\n"
                + "on c.cod_cargo=e.cod_cargo\n"
                + "where ano='" + utilitario.getAnio(utilitario.getFechaActual()) + "'\n"
                + "and  id_distributivo_roles=" + dis + " and ide_periodo in(" + utilitario.getMes(utilitario.getFechaActual()) + ") and ide_columnas in (" + col2 + ")\n"
                + ") as aa\n"
                + "left join \n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS HORAS_EXTRAS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano='" + utilitario.getAnio(utilitario.getFechaActual()) + "' and  id_distributivo_roles=" + dis + " and ide_periodo in(" + utilitario.getMes(utilitario.getFechaActual()) + ") and ide_columnas in (" + col + ")\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO \n"
                + "left join\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS SUB_ROGACION  from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano='" + utilitario.getAnio(utilitario.getFechaActual()) + "' and  id_distributivo_roles=" + dis + " and ide_periodo in(" + utilitario.getMes(utilitario.getFechaActual()) + ") and ide_columnas in (" + col1 + ") \n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO \n"
                + ") as m\n"
                + "where cod_empleado = " + codigo + "\n"
                + "order by nombres";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setDatosServidor(String login, String codigo) {
        String strSql = "insert into srh_autorizacion_acumulacion (autoriza_cod_empleado,autoriza_empleado,autoriza_id_distributivo,autoriza_fecha_ingreso,autoriza_fecha_final,autoriza_anio,autoriza_decimo_tercero,autoriza_decimo_cuarto,autoriza_login_ingreso,autoriza_fecha_creacion)\n"
                + "SELECT\n"
                + "cod_empleado,\n"
                + "nombres,\n"
                + "id_distributivo,\n"
                + "(SELECT max( fecha_contrato ) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado ) as fecha_contrato, \n"
                + "(SELECT max(fecha_fin) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado) as fecha_fin,\n"
                + "'" + utilitario.getAnio(utilitario.getFechaActual()) + "',\n"
                + "'1',\n"
                + "'1',\n"
                + "'" + login + "' ,\n"
                + "'" + utilitario.getFechaActual() + "'  \n"
                + "FROM srh_empleado\n"
                + "WHERE estado = 1 and cod_empleado = '" + codigo + "'\n"
                + "ORDER BY id_distributivo,nombres";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public void setDatosCalculo(String codigo, String empleado, String columna, Double valor, String estado,
            Double rmu, Double hxe, Double subrogacion, String distributivo) {
        String strSql = "insert into srh_decimo_cuarto_tercero (decimo_cod_empleado,decimo_empleado,decimo_columna,decimo_anio,decimo_periodo,decimo_valor,decimo_estado,decimo_rmu,decimo_horas_extra,decimo_subrogacion,decimo_id_distributivo)\n"
                + "values ('" + codigo + "','" + empleado + "','" + columna + "','" + utilitario.getAnio(utilitario.getFechaActual()) + "','" + utilitario.getMes(utilitario.getFechaActual()) + "'," + valor + ",'" + estado + "'," + rmu + "," + hxe + "," + subrogacion + ",'" + distributivo + "')";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        conPostgres.desconectar();
        conPostgres = null;
    }

    public TablaGenerica sumaPeriodo() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT sum(d.valor) as total\n"
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
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica periodo(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo=" + periodo);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getColumnas(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_col,descripcion_col,distributivo,porcentaje_subsidio from srh_columnas where ide_col = " + periodo);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getTrabajadores(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cedula_pass,nombres,cod_empleado\n"
                + "from srh_empleado\n"
                + "where estado=1 and id_distributivo = " + periodo);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica Suma() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT sum(descuento) as total \n"
                + "FROM srh_descuento where ano = '" + utilitario.getAnio(utilitario.getFechaActual()) + "' and ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()));
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica distibutivo(Integer distri) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_distributivo,descripcion FROM srh_tdistributivo where id_distributivo=" + distri);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica columnas(Integer colum) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=" + colum);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica VerificarRol() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_roles,ide_columnas FROM srh_roles where ano = " + utilitario.getAnio(utilitario.getFechaActual()) + " and ide_periodo = " + utilitario.getMes(utilitario.getFechaActual()));
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica Direc_Asist(Integer codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "c.cod_cargo,\n"
                + "c.nombre_cargo,\n"
                + "e.cedula_pass,\n"
                + "e.nombres\n"
                + "FROM srh_cargos c\n"
                + "INNER JOIN srh_empleado e ON e.cod_cargo = c.cod_cargo\n"
                + "WHERE c.cod_cargo =" + codigo);//299
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getConfirmaDatos(String anio, Integer codigo, String empleado, Integer distributivo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_roles,ide_empleado,id_distributivo_roles\n"
                + "FROM srh_roles\n"
                + "where ano = '" + anio + "' and ide_periodo = " + codigo + " and ide_empleado = '" + empleado + "' and id_distributivo_roles = " + distributivo);
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getConfirmaFondos(String anio, Integer codigo, String empleado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ano,ide_periodo,descuento,cedula,nombres,ide_empleado,fondos_reserva\n"
                + "from srh_valor_roles_historial\n"
                + "where ide_periodo=" + codigo + " and ano = '" + anio + "' and ide_empleado= '" + empleado + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getInfoD3T(String anio, Integer codigo, String empleado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ano,ide_periodo,descuento,cedula,nombres,ide_empleado,fondos_reserva\n"
                + "from srh_valor_roles_historial\n"
                + "where ide_periodo=" + codigo + " and ano = '" + anio + "' and ide_empleado= '" + empleado + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getInfoD4T(String anio, Integer codigo, String empleado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ano,ide_periodo,descuento,cedula,nombres,ide_empleado,fondos_reserva\n"
                + "from srh_valor_roles_historial\n"
                + "where ide_periodo=" + codigo + " and ano = '" + anio + "' and ide_empleado= '" + empleado + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getCalculoD3T(Integer anio, Integer periodo, String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select \n"
                + "a.cod_empleado,"
                + "remuneracion,\n"
                + "(case when b.horas_extras is null then 0 when b.horas_extras is not null then b.horas_extras end) as hxe ,\n"
                + "(case when c.sub_rogacion is null then 0 when c.sub_rogacion is not null then c.sub_rogacion end) as sbr\n"
                + "from \n"
                + "(SELECT\n"
                + "cod_empleado,\n"
                + "remuneracion\n"
                + "from srh_empleado\n"
                + "where estado=1 and id_distributivo is not null) as a\n"
                + "left join \n"
                + "(SELECT ide_empleado,\n"
                + "sum(valor) AS horas_extras\n"
                + "FROM srh_roles \n"
                + "WHERE ano = " + anio + " AND\n"
                + "id_distributivo_roles = 2 AND\n"
                + "ide_periodo = " + periodo + " AND\n"
                + "ide_columnas IN (75, 76) AND\n"
                + "valor > 0\n"
                + "GROUP BY ide_empleado)as b\n"
                + "on a.cod_empleado = b.ide_empleado\n"
                + "left join \n"
                + "(SELECT ide_empleado,\n"
                + "sum(valor) AS sub_rogacion\n"
                + "FROM srh_roles\n"
                + "WHERE ano = " + anio + " AND\n"
                + "id_distributivo_roles = 1 AND\n"
                + "ide_periodo = " + periodo + " AND\n"
                + "ide_columnas IN (18) AND\n"
                + "valor > 0\n"
                + "GROUP BY ide_empleado) as c\n"
                + "on a.cod_empleado = c.ide_empleado\n"
                + "where cod_empleado = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getCalculoD4T(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,\n"
                + "(SELECT porcentaje_subsidio\n"
                + "from srh_columnas \n"
                + "where ide_col in (25,70)\n"
                + "and distributivo = srh_empleado.id_distributivo)as sbu  \n"
                + "from srh_empleado \n"
                + "where cod_empleado = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getListEmpleados() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "cod_empleado, \n"
                + "nombres, \n"
                + "id_distributivo,\n"
                + "(case when \n"
                + "(SELECT max(fecha_contrato) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado) is NULL\n"
                + "then srh_empleado.fecha_ingreso when (SELECT max(fecha_contrato) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado) is not NULL\n"
                + "then (SELECT max(fecha_contrato) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado)end)\n"
                + "as fecha_contrato,  \n"
                + "(SELECT max(fecha_fin) from srh_num_contratos where cod_empleado = srh_empleado.cod_empleado) as fecha_fin \n"
                + "FROM srh_empleado \n"
                + "WHERE estado = 1  and id_distributivo is not null\n"
                + "ORDER BY id_distributivo,nombres");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getNumeroFilas() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,nombres,id_distributivo\n"
                + "FROM srh_empleado \n"
                + "WHERE estado = 1  and id_distributivo is not null\n"
                + "ORDER BY id_distributivo,nombres");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getNumFilas(String codigo, String anio) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT autoriza_cod_empleado,autoriza_empleado,autoriza_id_distributivo\n"
                + "from srh_autorizacion_acumulacion\n"
                + "where autoriza_anio = '" + anio + "' and autoriza_cod_empleado = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getInfoAcumulacion(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT autoriza_cod_empleado, \n"
                + "autoriza_id_distributivo, \n"
                + "autoriza_empleado, \n"
                + "(select remuneracion from srh_empleado where cast(cod_empleado as VARCHAR) = srh_autorizacion_acumulacion.autoriza_cod_empleado) AS remuneracion, \n"
                + "autoriza_fecha_ingreso, \n"
                + "autoriza_fecha_final, \n"
                + "autoriza_anio, \n"
                + "autoriza_decimo_tercero, \n"
                + "autoriza_fecha_creacion, \n"
                + "autoriza_acumulado_tercero, \n"
                + "autoriza_acumulado_cuarto, \n"
                + "autoriza_decimo_cuarto \n"
                + "from srh_autorizacion_acumulacion \n"
                + "where autoriza_id_distributivo = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getInfoListaPago(String codigo, String anio, String periodo, String columna) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT decimo_cod_empleado,\n"
                + "decimo_empleado,\n"
                + "decimo_rmu,\n"
                + "decimo_id_distributivo\n"
                + "FROM srh_decimo_cuarto_tercero\n"
                + "where decimo_anio = '" + anio + "' and decimo_periodo= '" + periodo + "' and decimo_cod_empleado= '" + codigo + "' and decimo_columna = '" + columna + "'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }

    public TablaGenerica getPeriodos(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT periodo_fecha_inicial,periodo_fecha_final\n"
                + "from srh_periodo_sueldo\n"
                + "where periodo_columna = '" + codigo + "' and periodo_estado = 'S'");
        tabFuncionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tabFuncionario;
    }


    private void conPostgresql() {
        if (conPostgres == null) {
            conPostgres = new Conexion();
            conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
}
