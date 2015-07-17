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
 * @author KEJA
 */
@Stateless
public class AntiSueldos {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Utilitario utilitario = new Utilitario();
    private Conexion conPostgres;

    public TablaGenerica getVerifRegistro(Integer registro, String nombre) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select cod_notaria,nombre_registro from srh_notarias_abogados where not_cod_notaria= " + registro + " and nombre_registro = '" + nombre + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getVerifRegistro1(Integer registro, Integer codigo, String nombre) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select cod_notaria,nombre_registro from srh_notarias_abogados where not_cod_notaria= " + registro + " and cod_notaria= " + registro + " and nombre_registro = '" + nombre + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //Saca información de los servidores que tiens solicitudes pendientes
    public TablaGenerica VerifEmpleid(String cedu, Integer tipo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "s.ide_empleado_solicitante,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante,\n"
                + "s.ide_tipo_anticipo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo s\n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE\n"
                + "s.ci_solicitante LIKE '" + cedu + "' and s.ide_tipo_anticipo =" + tipo + "\n"
                + "and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR\n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR\n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

//Busqueda en roles de datos de servidor que solicita por numero de cedula
    public TablaGenerica empleadosCed(String cedula, Integer anio, Integer mes) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from  \n"
                + " (select aa.*,a.FONDOS_RESERVA,(case when a.FONDOS_RESERVA is NULL then '0' when a.FONDOS_RESERVA > 0 then a.FONDOS_RESERVA end ) as confirma1,  \n"
                + " b.HORAS_EXTRAS,(case when b.HORAS_EXTRAS is NULL then '0' when b.HORAS_EXTRAS > 0 then b.HORAS_EXTRAS end ) as confirma2,  \n"
                + " c.OTROS_INGRESOS,(case when c.OTROS_INGRESOS is NULL then '0' when c.OTROS_INGRESOS > 0 then c.OTROS_INGRESOS end ) as confirma3,  \n"
                + " d.TOTAL_INGRESOS,f.APORTE_IESS,g.IMPUESTO_RENTA,(case when g.IMPUESTO_RENTA is NULL then '0' when g.IMPUESTO_RENTA > 0 then g.IMPUESTO_RENTA end ) as confirma4,  \n"
                + " h.PRESTAMO_IESS,(case when h.PRESTAMO_IESS is NULL then '0' when h.PRESTAMO_IESS > 0 then h.PRESTAMO_IESS end ) as confirma5,  \n"
                + " i.ANTICIPOS,(case when i.ANTICIPOS is NULL then '0' when i.ANTICIPOS > 0 then i.ANTICIPOS end ) as confirma6,  \n"
                + " j.OTROS_EGRESOS,(case when j.OTROS_EGRESOS is NULL then '0' when j.OTROS_EGRESOS > 0 then j.OTROS_EGRESOS end ) as confirma7,k.TOTAL_EGRESOS from  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,(case when e.partida_indv is NULL then '0' when e.partida_indv <> NULL then e.partida_indv end ) as confirma,c.cod_cargo,c.nombre_cargo,r.valor AS RU,id_distributivo_roles  \n"
                + "   \n"
                + " from srh_roles as r inner join prec_programas as  p  \n"
                + " on r.ide_programa=p.ide_programa  \n"
                + " inner join srh_empleado as e  \n"
                + " on e.cod_empleado=r.ide_empleado  \n"
                + " inner join srh_cargos  as c  \n"
                + " on c.cod_cargo=e.cod_cargo  \n"
                + " where  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (14) and e.cedula_pass like '" + cedula + "' -- and valor>0  \n"
                + " order by p.ide_funcion) AS aa  \n"
                + "   \n"
                + " left join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0  \n"
                + " order by p.ide_funcion) as a  \n"
                + " on aa.COD_EMPLEADO=a.COD_EMPLEADO  \n"
                + "   \n"
                + " left join  \n"
                + "   \n"
                + " --100%+ 25%  \n"
                + " (select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e  \n"
                + " where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0  \n"
                + " group by E.COD_EMPLEADO) AS b  \n"
                + " on aa.COD_EMPLEADO=b.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c  \n"
                + " on aa.COD_EMPLEADO=c.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d  \n"
                + " on aa.COD_EMPLEADO=d.COD_EMPLEADO  \n"
                + " --------------------------------------EGRESOS  \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1and ide_periodo=" + mes + " and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f  \n"
                + " on aa.COD_EMPLEADO=f.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g  \n"
                + " on aa.COD_EMPLEADO=g.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h  \n"
                + " on aa.COD_EMPLEADO=h.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=6 and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i  \n"
                + " on aa.COD_EMPLEADO=i.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j  \n"
                + " on aa.COD_EMPLEADO=j.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + anio + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k  \n"
                + " on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m  \n"
                + " order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //trabajador por numero de cedulas
    public TablaGenerica trabajadoresCed(String cedula, Integer anio, Integer mes) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n"
                + "(\n"
                + "select aa.*,a.FONDOS_RESERVA,b.HORAS_EXTRAS,\n"
                + "c.SUB_FAMILIAR,d.SUB_ANTIGUEDAD,e.SUB_COMISARIATO,f.OTROS_INGRESOS,h.APORTE_IESS,i.PRESTAMO_IESS,j.ANTICIPOS,k.OTROS_EGRESOS,\n"
                + "l.TOTAL_EGRESOS,total_ingresos\n"
                + "from\n"
                + "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,c.cod_cargo,c.nombre_cargo,r.valor AS SU,id_distributivo_roles\n"
                + "from srh_roles as r inner join prec_programas as  p\n"
                + "on r.ide_programa=p.ide_programa\n"
                + "inner join srh_empleado as e\n"
                + "on e.cod_empleado=r.ide_empleado\n"
                + "inner join srh_cargos  as c\n"
                + "on c.cod_cargo=e.cod_cargo\n"
                + "where ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (40) and e.cedula_pass like '" + cedula + "' -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (89)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "order by p.ide_funcion) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "--100%+ 50%\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (75,76)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (98)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (99)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (101)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS e\n"
                + "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (90,41,45,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(40,125,45,75,76,89,98,99,100,102,101,107,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (71)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (59)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (46)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n"
                + "valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(71,59,53,46,72,56,84,74,57,73,47,80,85,48,50,108,55,51,52,106,112,91,110)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n"
                + "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n"
                + ") as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //Busqueda de solicitudes pendientes por codigo de servidor
    public TablaGenerica VerifEmpleCod(Integer codigo, Integer tipo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "s.ide_empleado_solicitante,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante\n"
                + "FROM\n"
                + "srh_solicitud_anticipo s\n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE\n"
                + "s.ide_empleado_solicitante = " + codigo + " and s.ide_tipo_anticipo = " + tipo + "\n"
                + "and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR\n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR\n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //Busqueda de servidor por apellido
    public TablaGenerica empleados(Integer codigo, Integer anio, Integer mes) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n"
                + "(select aa.*,a.FONDOS_RESERVA,(case when a.FONDOS_RESERVA is NULL then '0' when a.FONDOS_RESERVA > 0 then a.FONDOS_RESERVA end ) as confirma1,\n"
                + "b.HORAS_EXTRAS,(case when b.HORAS_EXTRAS is NULL then '0' when b.HORAS_EXTRAS > 0 then b.HORAS_EXTRAS end ) as confirma2,\n"
                + "c.OTROS_INGRESOS,(case when c.OTROS_INGRESOS is NULL then '0' when c.OTROS_INGRESOS > 0 then c.OTROS_INGRESOS end ) as confirma3,\n"
                + "d.TOTAL_INGRESOS,f.APORTE_IESS,g.IMPUESTO_RENTA,(case when g.IMPUESTO_RENTA is NULL then '0' when g.IMPUESTO_RENTA > 0 then g.IMPUESTO_RENTA end ) as confirma4,\n"
                + "h.PRESTAMO_IESS,(case when h.PRESTAMO_IESS is NULL then '0' when h.PRESTAMO_IESS > 0 then h.PRESTAMO_IESS end ) as confirma5,\n"
                + "i.ANTICIPOS,(case when i.ANTICIPOS is NULL then '0' when i.ANTICIPOS > 0 then i.ANTICIPOS end ) as confirma6,\n"
                + "j.OTROS_EGRESOS,(case when j.OTROS_EGRESOS is NULL then '0' when j.OTROS_EGRESOS > 0 then j.OTROS_EGRESOS end ) as confirma7,k.TOTAL_EGRESOS from\n"
                + "\n"
                + "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,(case when e.partida_indv is NULL then '0' when e.partida_indv <> NULL then e.partida_indv end ) as confirma,c.cod_cargo,c.nombre_cargo,r.valor AS RU,id_distributivo_roles\n"
                + "\n"
                + "from srh_roles as r inner join prec_programas as  p\n"
                + "on r.ide_programa=p.ide_programa\n"
                + "inner join srh_empleado as e\n"
                + "on e.cod_empleado=r.ide_empleado\n"
                + "inner join srh_cargos  as c\n"
                + "on c.cod_cargo=e.cod_cargo\n"
                + "where\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (14) and e.cod_empleado =" + codigo + " -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "order by p.ide_funcion) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "--100%+ 25%\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "--------------------------------------EGRESOS\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1and ide_periodo=" + mes + " and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + mes + " and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica trabajadores(Integer codigo, Integer anio, Integer mes) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n"
                + "(\n"
                + "select aa.*,a.FONDOS_RESERVA,b.HORAS_EXTRAS,\n"
                + "c.SUB_FAMILIAR,d.SUB_ANTIGUEDAD,e.SUB_COMISARIATO,f.OTROS_INGRESOS,h.APORTE_IESS,i.PRESTAMO_IESS,j.ANTICIPOS,k.OTROS_EGRESOS,\n"
                + "l.TOTAL_EGRESOS,total_ingresos\n"
                + "from\n"
                + "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,c.cod_cargo,c.nombre_cargo,r.valor AS SU,id_distributivo_roles\n"
                + "from srh_roles as r inner join prec_programas as  p\n"
                + "on r.ide_programa=p.ide_programa\n"
                + "inner join srh_empleado as e\n"
                + "on e.cod_empleado=r.ide_empleado\n"
                + "inner join srh_cargos  as c\n"
                + "on c.cod_cargo=e.cod_cargo\n"
                + "where ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (40) and e.cod_empleado =" + codigo + " -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (89)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "order by p.ide_funcion) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "--100%+ 50%\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (75,76)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (98)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (99)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (101)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS e\n"
                + "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (90,41,45,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(40,125,45,75,76,89,98,99,100,102,101,107,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (71)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (59)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in (46)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n"
                + "valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(71,59,53,46,72,56,84,74,57,73,47,80,85,48,50,108,55,51,52,106,112,91,110)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n"
                + "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n"
                + ") as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //Verifica si garante se encuentra disponibles
    public TablaGenerica VerifGaranteid(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "g.ci_garante,\n"
                + "g.garante,\n"
                + "c.ide_estado_anticipo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo s\n"
                + "INNER JOIN srh_garante_anticipo g ON g.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where g.ci_garante like '" + cedula + "' and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR \n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR \n"
                + "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //busqueda de garante por cedulas
    public TablaGenerica Garantemple(String ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "e.cod_empleado, e.cedula_pass,\n"
                + "e.nombres,e.fecha_ingreso,\n"
                + "e.fecha_nombramiento,e.id_distributivo,\n"
                + "e.cod_tipo,i.tipo\n"
                + "FROM\n"
                + "srh_empleado e,\n"
                + "srh_tipo_empleado i\n"
                + "WHERE\n"
                + "e.estado = 1 AND\n"
                + "e.cod_tipo = i.cod_tipo AND\n"
                + "e.cod_tipo in (4,7,8,10)\n"
                + "and e.cedula_pass like '" + ci + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica Garantemple1(String ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "e.cod_empleado, e.cedula_pass,\n"
                + "e.nombres,e.fecha_ingreso,\n"
                + "e.fecha_nombramiento,e.id_distributivo,\n"
                + "e.cod_tipo,i.tipo\n"
                + "FROM\n"
                + "srh_empleado e,\n"
                + "srh_tipo_empleado i\n"
                + "WHERE\n"
                + "e.estado = 1 AND\n"
                + "e.cod_tipo = i.cod_tipo AND\n"
                + "e.cod_tipo in (4,7,8,10,3)\n"
                + "and e.cedula_pass like '" + ci + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getGarante(String ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "e.cod_empleado, e.cedula_pass,\n"
                + "e.nombres,e.fecha_ingreso,\n"
                + "e.fecha_nombramiento,e.id_distributivo,\n"
                + "e.cod_tipo,i.tipo\n"
                + "FROM\n"
                + "srh_empleado e,\n"
                + "srh_tipo_empleado i\n"
                + "WHERE\n"
                + "e.estado = 1 AND\n"
                + "e.cod_tipo = i.cod_tipo\n"
                + "and e.cedula_pass like '" + ci + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica GarNumConat(Integer ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cod_tipo \n"
                + "FROM srh_num_contratos\n"
                + "where cod_tipo in (4,7,8,10) and cod_empleado =" + ci);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica GarNumConat1(Integer ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cod_tipo \n"
                + "FROM srh_num_contratos\n"
                + "where cod_tipo in (4,7,8,10,3) and cod_empleado =" + ci);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //buscar garante por apellido
    public TablaGenerica VerifGaranteCod(Integer codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "s.ide_empleado_solicitante,\n"
                + "g.ide_empleado_garante,\n"
                + "g.ci_garante,\n"
                + "g.garante\n"
                + "FROM\n"
                + "srh_solicitud_anticipo AS s\n"
                + "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "INNER JOIN srh_garante_anticipo g ON g.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE\n"
                + "g.ide_empleado_garante = " + codigo + " AND\n"
                + "(c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO') OR\n"
                + "c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO') OR\n"
                + "c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica GaranteNom(Integer empleado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,id_distributivo,cod_tipo\n"
                + "FROM srh_empleado WHERE estado = 1 AND cod_tipo IN (4,7,8,3) AND cod_empleado =" + empleado + " order by nombres ");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    //Permite utilizar las fecha de inicio y fin de contrato
    public TablaGenerica FechaContrato(Integer codigoE) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT fecha_contrato,cod_tipo,ide_num_contrato,fecha_fin\n"
                + "FROM srh_num_contratos\n"
                + "where cod_empleado = " + codigoE + "\n"
                + "ORDER BY fecha_contrato DESC LIMIT 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica periodos(String mes, String anio) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_periodo_anticipo,periodo,mes,anio FROM srh_periodo_anticipo where mes like '" + mes + "' and anio like '" + anio + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica periodos1(Integer id) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_periodo_anticipo,periodo,mes,anio FROM srh_periodo_anticipo where ide_periodo_anticipo =" + id);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCedula(Integer id) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "c.ide_empleado,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante,\n"
                + "s.ide_solicitud_anticipo\n"
                + "FROM srh_calculo_anticipo c\n"
                + "INNER JOIN srh_solicitud_anticipo s ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where c.ide_calculo_anticipo = " + id);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica pagosAndelantados(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "s.ide_solicitud_anticipo,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante,\n"
                + "s.rmu,\n"
                + "c.valor_anticipo,\n"
                + "c.numero_cuotas_anticipo,\n"
                + "c.valor_pagado,\n" + "c.numero_cuotas_pagadas,\n"
                + "(c.valor_anticipo-c.valor_pagado)as saldo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo AS s\n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where ci_solicitante = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getReCalculo(Integer codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT s.ide_solicitud_anticipo, \n"
                + "s.ci_solicitante, \n"
                + "(select remuneracion from srh_empleado where cod_empleado = s.ide_empleado_solicitante) AS remuneracion, \n"
                + "c.valor_anticipo, \n"
                + "c.numero_cuotas_anticipo, \n"
                + "c.valor_cuota_mensual, \n"
                + "c.val_cuo_adi, \n"
                + "c.valor_pagado, \n"
                + "s.id_distributivo, \n"
                + "c.porcentaje_descuento_diciembre, \n"
                + "c.numero_cuotas_pagadas,\n"
                + "(select sum(valor) as valor from srh_detalle_anticipo \n"
                + "where ide_anticipo= s.ide_solicitud_anticipo and ide_estado_cuota is not null and usu_pago_anticipado is null and usu_cobro_liquidacion is null) as valor_acumulado \n"
                + "from srh_solicitud_anticipo s \n"
                + "inner join srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo \n"
                + "where c.ide_estado_anticipo in (1,2,3) and s.ide_solicitud_anticipo = " + codigo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDetalleReCalculo(Integer codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select * from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + codigo + " and ide_periodo_descontado is null\n"
                + "order by ide_detalle_anticipo");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getEmpleadoInfo(String cedula, Integer anio, Integer mes) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select aa.*,(a.TOTAL_INGRESOS-b.TOTAL_EGRESOS) as liquido from (\n"
                + "select E.COD_EMPLEADO,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,c.cod_cargo,r.valor AS RU,id_distributivo_roles   \n"
                + "from srh_roles as r inner join prec_programas as  p   \n"
                + "on r.ide_programa=p.ide_programa   \n"
                + "inner join srh_empleado as e   \n"
                + "on e.cod_empleado=r.ide_empleado   \n"
                + "inner join srh_cargos  as c   \n"
                + "on c.cod_cargo=e.cod_cargo   \n"
                + "where ano= " + anio + "  \n"
                + "and ide_periodo=" + mes + " and ide_columnas in (14,40) and e.cedula_pass like '" + cedula + "'   \n"
                + "order by p.ide_funcion\n"
                + ") as aa\n"
                + "inner join \n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + anio + " \n"
                + "and ide_periodo=" + mes + " and ide_columnas in\n"
                + "(86,14,92,93,19,18,20,40,125,45,75,76,89,98,99,100,102,101,107,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "inner join\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=2015\n"
                + " and ide_periodo=1 and ide_columnas in\n"
                + "(33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12,71,59,53,46,72,56,84,74,57,73,47,80,85,48,50,108,55,51,52,106,112,91,110)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getEmpleadoInf(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,\n"
                + "cod_banco,\n"
                + "cod_tipo,\n"
                + "cod_cuenta,\n"
                + "cod_grupo,\n"
                + "numero_cuenta,\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "cod_cargo,\n"
                + "remuneracion,\n"
                + "id_distributivo\n"
                + "FROM srh_empleado\n"
                + "WHERE srh_empleado.cedula_pass = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getIdDetalleReCalculo(Integer codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id, ide_detalle_anticipo from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + codigo + " and ide_periodo_descontado is null\n"
                + "order by ide_detalle_anticipo desc\n"
                + "limit 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getTotalAnt(String anio) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select a.id,a.anticipo,b.descontar,(a.anticipo-b.descontar)as saldo from\n"
                + "(SELECT 0 as id,\n"
                + "sum(c.valor_anticipo) as anticipo\n"
                + "FROM srh_calculo_anticipo AS c\n"
                + "INNER JOIN srh_solicitud_anticipo s ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE substring(cast(c.fecha_anticipo as varchar) from 0 for 5) = '" + anio + "') as a\n"
                + "left join\n"
                + "(select 0 as id,sum(((case when a.descontado is null then 0 when a.descontado is not null then a.descontado end)+\n"
                + "(case when b.anticipado is null then 0 when b.anticipado is not null then b.anticipado end)+\n"
                + "(case when c.liquidacion is null then 0 when c.liquidacion is not null then c.liquidacion end ))) as descontar from\n"
                + "(SELECT\n"
                + "sum(d.valor) as descontado,\n"
                + "s.id_distributivo\n"
                + "FROM\n"
                + "srh_detalle_anticipo d\n"
                + "INNER JOIN srh_solicitud_anticipo s ON d.ide_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE d.anio = '" + anio + "' AND\n"
                + "d.ide_estado_cuota = 1 AND\n"
                + "usu_pago_anticipado is null AND\n"
                + "usu_cobro_liquidacion is null\n"
                + "GROUP BY s.id_distributivo)as a\n"
                + "left join\n"
                + "(SELECT sum(d.valor) as anticipado,\n"
                + "s.id_distributivo\n"
                + "FROM srh_detalle_anticipo d\n"
                + "INNER JOIN srh_solicitud_anticipo s ON d.ide_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE d.ide_estado_cuota = 1  AND substring(cast(d.fecha_pago_anticipado as varchar) from 0 for 5) = '" + anio + "'\n"
                + "GROUP BY s.id_distributivo) as b\n"
                + "on a.id_distributivo = b.id_distributivo\n"
                + "left join\n"
                + "(SELECT sum(d.valor) as liquidacion,\n"
                + "s.id_distributivo\n"
                + "FROM srh_detalle_anticipo d\n"
                + "INNER JOIN srh_solicitud_anticipo s ON d.ide_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE d.ide_estado_cuota = 1  AND substring(cast(d.fecha_cobro_liquidacion as varchar) from 0 for 5) = '" + anio + "'\n"
                + "GROUP BY s.id_distributivo) as c\n"
                + "on a.id_distributivo = c.id_distributivo) as b\n"
                + "on a.id = b.id");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getListaAnticipos(Integer anio, Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT s.ide_solicitud_anticipo, \n"
                + "r.ide_empleado, \n"
                + "r.valor, \n"
                + "r.ide_periodo, \n"
                + "r.ano, \n"
                + "r.id_distributivo_roles \n"
                + "FROM srh_solicitud_anticipo AS s , \n"
                + "srh_roles AS r, \n"
                + "srh_calculo_anticipo c \n"
                + "WHERE s.ide_empleado_solicitante = r.ide_empleado and \n"
                + "c.ide_solicitud_anticipo = s.ide_solicitud_anticipo and \n"
                + "r.ano = " + anio + " and \n"
                + "r.ide_periodo = " + periodo + " and \n"
                + "r.ide_columnas IN (1, 46)and \n"
                + "c.ide_estado_anticipo in (2,3) \n"
                + "ORDER BY r.id_distributivo_roles");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDetallaListaAnticipos(Integer solicitud, String anio, String periodo, Double valor) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_anticipo,\n"
                + "valor,\n"
                + "ide_periodo_descuento,\n"
                + "ide_estado_cuota,\n"
                + "cuota,\n"
                + "periodo,\n"
                + "anio\n"
                + "from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + solicitud + " and periodo = '" + periodo + "' and anio = '" + anio + "' and valor = " + valor);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public void InsertarAnticipo(String anio, String periodo) {
        // Forma el sql para el ingreso
        String strSql3 = "insert into srh_historial_cuotas_anticipos (his_ide_empleado,his_nombre,his_periodo,his_anio,his_cuota_roles,his_cuota_anticipo,his_cuota_nueva,his_cuotas_faltantes\n"
                + ",his_cuotas_anticipo,his_cuotas_pagadas,his_ide_solicitud,his_mes_actual,his_anio_actual,his_distributivo,his_ide_tipo_anticipo,his_cuota_individual,his_cuota_diferencia)\n"
                + "select ide_empleado,solicitante,periodo,anio,valor_roles,valor_anticipo,\n"
                + "cast((case when cuota_faltante =1 and valor_roles = 0 then valor_anticipo   \n"
                + "when cuota_faltante >=2 and valor_roles = 0 then (valor_anticipo/(cuota_faltante-1))\n"
                + "when cuota_faltante >=2 and valor_roles > 0 then ((valor_anticipo-valor_roles)/(cuota_faltante-1)) end )as numeric(5,2)) as cuota_nueva\n"
                + ",cuota_faltante\n"
                + ",numero_cuotas_anticipo\n"
                + ",numero_cuotas_pagadas,\n"
                + "ide_solicitud_anticipo\n"
                + ",extract(month from current_date) as mes,extract(year from current_date) as ano,\n"
                + "id_distributivo,ide_tipo_anticipo\n"
                + ",(case when cuota_unificada is not null then cuota_unificada when cuota_unificada is null then '0'end)as cuotas_separadas ,\n"
                + "(case when diferencia is not null then diferencia when diferencia is null then '0'end) as diferencias\n"
                + "from (\n"
                + "select a.ide_solicitud_anticipo,b.ide_empleado,a.solicitante,a.id_distributivo,a.ide_tipo_anticipo\n"
                + ",a.anio,a.periodo,a.valor as valor_anticipo, b.valor as valor_roles,\n"
                + "(case when a.ide_tipo_anticipo =1 then (b.valor-(case when a.ide_tipo_anticipo =1 then (b.valor-a.valor) end )) when a.ide_tipo_anticipo =2 \n"
                + "then (b.valor-(case when a.ide_tipo_anticipo =2 and (b.valor- a.valor) >0 then (b.valor- a.valor) end )) end ) as cuota_unificada,\n"
                + "(case when(a.valor-b.valor)>0 then (a.valor-b.valor) end) as diferencia,\n"
                + "a.numero_cuotas_anticipo,\n"
                + "a.numero_cuotas_pagadas,\n"
                + "a.cuota_faltante\n"
                + "from(\n"
                + "SELECT a.ide_empleado_solicitante,\n"
                + "a.solicitante,\n"
                + "d.valor,\n"
                + "d.anio,\n"
                + "d.periodo,\n"
                + "a.id_distributivo,\n"
                + "a.ide_tipo_anticipo,\n"
                + "a.ide_solicitud_anticipo,\n"
                + "f.numero_cuotas_anticipo,\n"
                + "f.numero_cuotas_pagadas,\n"
                + "(f.numero_cuotas_anticipo-\n"
                + "f.numero_cuotas_pagadas) as cuota_faltante\n"
                + "FROM srh_detalle_anticipo d \n"
                + ",srh_solicitud_anticipo a  \n"
                + ",srh_calculo_anticipo as f\n"
                + "WHERE d.ide_anticipo = a.ide_solicitud_anticipo  \n"
                + "and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo   \n"
                + "and d.periodo = '" + periodo + "'  \n"
                + "--and d.ide_estado_cuota is null  \n"
                + "and d.anio = '" + anio + "'  \n"
                + "and f.ide_estado_anticipo <> 5\n"
                + "order by a.ide_empleado_solicitante\n"
                + ")as a\n"
                + "inner join \n"
                + "(SELECT DISTINCT\n"
                + "r.ide_empleado,\n"
                + "s.solicitante,\n"
                + "r.valor\n"
                + "FROM srh_solicitud_anticipo AS s ,\n"
                + "srh_roles AS r\n"
                + "WHERE s.ide_empleado_solicitante = r.ide_empleado AND\n"
                + "r.ano = " + anio + " AND\n"
                + "r.ide_periodo = " + periodo + " AND\n"
                + "r.ide_columnas IN (1, 46)\n"
                + "ORDER BY r.ide_empleado ASC)as b\n"
                + "on a.ide_empleado_solicitante = b.ide_empleado\n"
                + "where (a.valor- b.valor) <>0) as p";
        conPostgresql();
        conPostgres.ejecutarSql(strSql3);
        desPostgresql();
    }

    public void setNotaria(Integer codigo, String nombre) {
        String parametro = "insert into srh_notarias_abogados (not_cod_notaria,nombre_registro)\n"
                + "values (" + codigo + ",'" + nombre + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setAbogado(Integer codigo, Integer abogado, String nombre) {
        String parametro = "insert into srh_notarias_abogados (not_cod_notaria,ab_cod_notaria,nombre_registro)\n"
                + "values (" + codigo + "," + abogado + ",'" + nombre + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setActuCalculo(Integer anti, Double cuota) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set valor = " + cuota + "\n"
                + "from (SELECT ide_detalle_anticipo,\n"
                + "ide_estado_cuota,\n"
                + "periodo,\n"
                + "anio\n"
                + "from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + anti + " and ide_estado_cuota is null and periodo <>'12') as a\n"
                + "where srh_detalle_anticipo.ide_detalle_anticipo = a.ide_detalle_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setActuCalculo1(Integer anti, Double cuota) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set valor = " + cuota + "\n"
                + "from (SELECT ide_detalle_anticipo,\n"
                + "ide_estado_cuota,\n"
                + "periodo,\n"
                + "anio\n"
                + "from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + anti + " and ide_estado_cuota is null) as a\n"
                + "where srh_detalle_anticipo.ide_detalle_anticipo = a.ide_detalle_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setActuRedondeo(Integer anti, Double cuota) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set valor = b.valor\n"
                + "from (select a.ide_detalle_anticipo,a.ide_periodo_descuento,\n"
                + "(case when b.valor >0.0 then b.valor+" + cuota + " when  b.valor <=0.0 then " + cuota + " end ) as valor\n"
                + "from \n"
                + "(select ide_detalle_anticipo,ide_periodo_descuento,ide_anticipo from srh_detalle_anticipo\n"
                + "where ide_anticipo = " + anti + " order by ide_detalle_anticipo desc limit 1) as a\n"
                + "inner join \n"
                + "(SELECT s.ide_solicitud_anticipo,\n"
                + "(c.valor_anticipo-\n"
                + "(select sum(valor) from srh_detalle_anticipo where ide_anticipo = s.ide_solicitud_anticipo))as valor,c.valor_anticipo\n"
                + "FROM srh_solicitud_anticipo AS s\n"
                + "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "WHERE s.ide_solicitud_anticipo = " + anti + ") as b\n"
                + "on a.ide_anticipo =b.ide_solicitud_anticipo) as b\n"
                + "where srh_detalle_anticipo.ide_detalle_anticipo = b.ide_detalle_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteCalculo(Integer anti, Integer cal, String usu) {
        String auSql = "UPDATE srh_calculo_anticipo\n"
                + "SET ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado ='NEGADO') ,\n"
                + "usu_anulacion = '" + usu + "' where ide_calculo_anticipo = " + cal + " and ide_solicitud_anticipo = " + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setRecacular(Integer codigo, Double cal, Double usu) {
        String auSql = "update srh_calculo_anticipo\n"
                + "set valor_cuota_mensual = " + cal + ", \n"
                + "val_cuo_adi = " + usu + "\n"
                + "where ide_solicitud_anticipo = " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setResolicitud(Integer codigo, String cal, String usu) {
        String auSql = "update srh_solicitud_anticipo\n"
                + "set recal_usuario = '" + usu + "',\n"
                + "recal_ip = '" + cal + "'\n"
                + "where ide_solicitud_anticipo = " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setRecalculo(Integer codigo, Double valor, Integer detalle) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set valor = " + valor + "\n"
                + "where ide_detalle_anticipo = " + detalle + "\n"
                + "and ide_anticipo =" + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteSolicitud(Integer anti, String cedula, String usu, String comen, String fecha) {
        String auSql = "UPDATE srh_solicitud_anticipo\n"
                + "SET usu_anulacion='" + usu + "',\n"
                + "comen_anulacion='" + comen + "',\n"
                + "fecha_anulacion='" + fecha + "',\n"
                + "aprobado_solicitante =0\n"
                + "where ide_solicitud_anticipo = " + anti + " and ci_solicitante ='" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteDetalle(Integer anti) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set ide_periodo_descontado= 0,\n"
                + "ide_estado_cuota = 0\n"
                + "where ide_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void ActualizaAnticipo(String anioac, String anioan) {
        String strSql4 = "update srh_calculo_anticipo   \n"
                + "set valor_pagado =h.pagado,  \n"
                + "numero_cuotas_pagadas=cuota,  \n"
                + "ide_estado_anticipo = 3  \n"
                + "from (SELECT ide_anticipo,sum(valor) as pagado,sum(ide_estado_cuota) as cuota \n"
                + "FROM srh_detalle_anticipo  \n"
                + "where ide_estado_cuota = 1 and  \n"
                + "ide_periodo_descuento in (select DISTINCT ide_periodo_descuento from srh_detalle_anticipo  \n"
                + "where ide_estado_cuota = 1 and usu_pago_anticipado is null and fecha_pago_anticipado is null and anio BETWEEN '" + anioan + "' and '" + anioac + "' order by ide_periodo_descuento) \n"
                + "GROUP BY ide_anticipo \n"
                + "order by ide_anticipo) h  \n"
                + "where srh_calculo_anticipo.ide_solicitud_anticipo = h.ide_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void ActualizarDetalleAnticipo(Integer anio, Integer periodo) {
        // Forma el sql para el ingreso
        String strSql4 = "update srh_detalle_anticipo \n"
                + "set ide_periodo_descontado = srh_detalle_anticipo.ide_periodo_descuento, \n"
                + "ide_estado_cuota = 1 \n"
                + "from  (\n"
                + "SELECT\n"
                + "s.ide_solicitud_anticipo,\n"
                + "s.ide_empleado_solicitante,\n"
                + "s.solicitante,\n"
                + "r.ide_roles,\n"
                + "r.ide_empleado,\n"
                + "r.valor,\n"
                + "r.ide_periodo,\n"
                + "r.ano,\n"
                + "r.id_distributivo_roles\n"
                + "FROM\n"
                + "\"public\".srh_solicitud_anticipo AS s ,\n"
                + "\"public\".srh_roles AS r,\n"
                + "\"public\".srh_calculo_anticipo c\n"
                + "WHERE\n"
                + "s.ide_empleado_solicitante = r.ide_empleado AND\n"
                + "c.ide_solicitud_anticipo = s.ide_solicitud_anticipo and\n"
                + "r.ano = " + anio + " AND\n"
                + "r.ide_periodo = " + periodo + " AND\n"
                + "r.ide_columnas IN (1, 46)AND\n"
                + "c.ide_estado_anticipo in (2,3)\n"
                + "ORDER BY\n"
                + "r.id_distributivo_roles\n"
                + ") d \n"
                + "WHERE \n"
                + "srh_detalle_anticipo.ide_anticipo = d.ide_solicitud_anticipo and\n"
                + "srh_detalle_anticipo.valor = d.valor and \n"
                + "srh_detalle_anticipo.periodo = cast (d.ide_periodo as varchar) and \n"
                + "srh_detalle_anticipo.anio = cast (d.ano as varchar)";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void CamAnticipoF() {
        String strSql4 = "update srh_calculo_anticipo \n"
                + "SET ide_estado_anticipo = 4 \n"
                + "from ( \n"
                + "SELECT n1.pagado,n2.ide_solicitud_anticipo\n"
                + "from (SELECT count(ide_anticipo) as pagado,ide_anticipo FROM srh_detalle_anticipo where ide_estado_cuota = 1  \n"
                + "GROUP BY ide_anticipo order by ide_anticipo) n1 \n"
                + "inner join (select numero_cuotas_anticipo,ide_solicitud_anticipo from srh_calculo_anticipo order by ide_solicitud_anticipo) n2 \n"
                + "on n1.ide_anticipo = n2.ide_solicitud_anticipo and n1.pagado = n2.numero_cuotas_anticipo) d1 \n"
                + "WHERE srh_calculo_anticipo.ide_solicitud_anticipo = d1.ide_solicitud_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void CamAnticipoSo() {
        String strSql4 = "update srh_solicitud_anticipo\n"
                + "set aprobado_solicitante = 2\n"
                + "from (\n"
                + "SELECT n1.pagado,n2.ide_anticipo\n"
                + "from (SELECT count(ide_anticipo) as pagado,ide_anticipo FROM srh_detalle_anticipo where ide_estado_cuota = 1 \n"
                + "GROUP BY ide_anticipo) n1\n"
                + "inner join (SELECT count(ide_anticipo) as pagando,ide_anticipo FROM srh_detalle_anticipo GROUP BY ide_anticipo) n2\n"
                + "on n1.ide_anticipo = n2.ide_anticipo and n1.pagado = n2.pagando ) d1\n"
                + "WHERE srh_solicitud_anticipo.ide_solicitud_anticipo = d1.ide_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void setCuota(String anio, String periodo) {
        String strSql4 = "UPDATE srh_detalle_anticipo\n"
                + "set ide_periodo_descontado=cast(a.his_periodo as int),\n"
                + " ide_estado_cuota='1'\n"
                + "from (SELECT\n"
                + "his_codigo,\n"
                + "his_periodo,\n"
                + "his_anio,\n"
                + "his_ide_solicitud,\n"
                + "his_cuota_individual\n"
                + "from srh_historial_cuotas_anticipos\n"
                + "where  his_mes_actual = '" + periodo + "' and \n"
                + "his_anio_actual = '" + anio + "') as a\n"
                + "where srh_detalle_anticipo.ide_anticipo=a.his_ide_solicitud\n"
                + "and srh_detalle_anticipo.valor=a.his_cuota_individual\n"
                + "and srh_detalle_anticipo.periodo=a.his_periodo\n"
                + "and srh_detalle_anticipo.anio=a.his_anio";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void ProrrogacionCuota(String anio, String periodo, String mes) {
        String strSql4 = "update srh_detalle_anticipo\n"
                + "set valor =a.cuota_real\n"
                + "FROM\n"
                + "(SELECT\n"
                + "h.his_codigo,\n"
                + "h.his_cuota_nueva,\n"
                + "d.ide_anticipo,\n"
                + "d.ide_detalle_anticipo,\n"
                + "d.valor,\n"
                + "d.ide_periodo_descuento,\n"
                + "(d.valor+h.his_cuota_nueva)as cuota_real\n"
                + "FROM\n"
                + "srh_historial_cuotas_anticipos h ,\n"
                + "srh_detalle_anticipo d\n"
                + "WHERE\n"
                + "h.his_mes_actual = '" + periodo + "' \n"
                + "and h.his_anio_actual = '" + anio + "'\n"
                + "and h.his_ide_solicitud=d.ide_anticipo \n"
                + "and d.ide_estado_cuota is null \n"
                + "and d.ide_periodo_descuento <> '" + mes + "'\n"
                + "order by h.his_codigo) as a\n"
                + "where srh_detalle_anticipo.ide_detalle_anticipo = a.ide_detalle_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void setActualizacionDatos(Integer anticipo, Double valor, String periodo, String anio) {
        String strSql4 = "update srh_detalle_anticipo  \n"
                + "set ide_periodo_descontado = srh_detalle_anticipo.ide_periodo_descuento, \n"
                + "valor = " + valor + "\n"
                + ",ide_estado_cuota = 1  \n"
                + "WHERE ide_anticipo = " + anticipo + "\n"
                + "and periodo = '" + periodo + "'\n"
                + "and anio = '" + anio + "'\n"
                + "and valor =" + valor;
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void setActualizacionDatos1(Integer anticipo, Double valor, String periodo, String anio) {
        String strSql4 = "update srh_detalle_anticipo  \n"
                + "set ide_periodo_descontado = srh_detalle_anticipo.ide_periodo_descuento, \n"
                + "valor = " + valor + "\n"
                + ",ide_estado_cuota = 1  \n"
                + "WHERE ide_anticipo = " + anticipo + "\n"
                + "and periodo = '" + periodo + "'\n"
                + "and anio = '" + anio + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void setDeleteRegistro(Integer anti) {
        String auSql = "delete from srh_notarias_abogados where cod_notaria = " + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setDeleteNotaria(Integer anti) {
        String auSql = "delete from srh_documentos_declaraciones where doc_codigo = " + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    //metodo que posee la cadena de conexion a base de datos
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
