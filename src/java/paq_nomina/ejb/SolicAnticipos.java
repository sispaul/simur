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
public class SolicAnticipos {
// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private Utilitario utilitario = new Utilitario();
    private Conexion conSql,//Conexion a la base de sigag
            conPostgres; //Conexion a la base de ciudadania;

    ///Buscar en roles
    public TablaGenerica empleados(Integer codigo) {
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
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (14) and e.cod_empleado =" + codigo + " -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "order by p.ide_funcion) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "--100%+ 25%\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "--------------------------------------EGRESOS\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica trabajadores(Integer codigo) {
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
                + "where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (40) and e.cod_empleado =" + codigo + " -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (89)  and\n"
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
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (75,76)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (98)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (99)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (101)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS e\n"
                + "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (90,41,45,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(40,89,75,76,90,41,45,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (71)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (59)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (46)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n"
                + "valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(71,59,46,80,91,53,50,73,84,49,5,51,74,85,48,72,108)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n"
                + "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n"
                + ") as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica empleadosCed(String cedula) {
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
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (14) and e.cedula_pass like '" + cedula + "' -- and valor>0  \n"
                + " order by p.ide_funcion) AS aa  \n"
                + "   \n"
                + " left join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0  \n"
                + " order by p.ide_funcion) as a  \n"
                + " on aa.COD_EMPLEADO=a.COD_EMPLEADO  \n"
                + "   \n"
                + " left join  \n"
                + "   \n"
                + " --100%+ 25%  \n"
                + " (select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e  \n"
                + " where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0  \n"
                + " group by E.COD_EMPLEADO) AS b  \n"
                + " on aa.COD_EMPLEADO=b.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c  \n"
                + " on aa.COD_EMPLEADO=c.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d  \n"
                + " on aa.COD_EMPLEADO=d.COD_EMPLEADO  \n"
                + " --------------------------------------EGRESOS  \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f  \n"
                + " on aa.COD_EMPLEADO=f.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g  \n"
                + " on aa.COD_EMPLEADO=g.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h  \n"
                + " on aa.COD_EMPLEADO=h.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=6 and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i  \n"
                + " on aa.COD_EMPLEADO=i.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j  \n"
                + " on aa.COD_EMPLEADO=j.COD_EMPLEADO  \n"
                + "   \n"
                + " LEFT join  \n"
                + "   \n"
                + " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n"
                + " ano= " + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + " and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k  \n"
                + " on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m  \n"
                + " order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica trabajadoresCed(String cedula) {
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
                + "where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (40) and e.cedula_pass like '" + cedula + "' -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (89)  and\n"
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
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (75,76)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (98)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (99)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (101)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS e\n"
                + "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (90,41,45,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(40,89,75,76,90,41,45,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (71)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (59)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (46)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n"
                + "valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(71,59,46,80,91,53,50,73,84,49,5,51,74,85,48,72,108)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n"
                + "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n"
                + ") as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica empleadosNom(String codigo) {
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
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (14) and e.nombres ='" + codigo + "' -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "order by p.ide_funcion) as a\n"
                + "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "--100%+ 25%\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "--------------------------------------EGRESOS\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=1 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica trabajadoresNom(String codigo) {
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
                + "where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (40) and e.nombres ='" + codigo + "' -- and valor>0\n"
                + "order by p.ide_funcion) AS aa\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (89)  and\n"
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
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (75,76)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS b\n"
                + "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (98)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS c\n"
                + "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (99)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS d\n"
                + "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n"
                + "\n"
                + "left join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e\n"
                + "where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (101)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0\n"
                + "group by E.COD_EMPLEADO) AS e\n"
                + "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (90,41,45,115)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n"
                + "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(40,89,75,76,90,41,45,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n"
                + "E.COD_EMPLEADO) as g\n"
                + "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (71)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n"
                + "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (59)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n"
                + "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n"
                + "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in (46)  and\n"
                + "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n"
                + "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n"
                + "valor>0 GROUP BY E.COD_EMPLEADO) as k\n"
                + "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n"
                + "\n"
                + "LEFT join\n"
                + "\n"
                + "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n"
                + "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n"
                + "ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and  id_distributivo_roles=2 and ide_periodo=" + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " and ide_columnas in\n"
                + "(71,59,46,80,91,53,50,73,84,49,5,51,74,85,48,72,108)\n"
                + "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n"
                + "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n"
                + ") as m\n"
                + "order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

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
                + "e.cod_tipo in (4,7,8)\n"
                + "and e.cedula_pass like '" + ci + "'");
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
                + "FROM srh_empleado WHERE estado = 1 AND cod_tipo IN (4,7,8) AND cod_empleado =" + empleado + " order by nombres ");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica Datos(String usuario) {
        con_sigag();
        TablaGenerica tabFuncionario = new TablaGenerica();
        con_sigag();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where NICK_USUA like '" + usuario + "'");
        tabFuncionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica empleado(String ci) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "cod_empleado,\n"
                + "cedula_pass,\n"
                + "nombres,\n"
                + "fecha_ingreso,\n"
                + "fecha_nombramiento,\n"
                + "id_distributivo,\n"
                + "cod_grupo,\n"
                + "cod_tipo,\n"
                + "cod_banco,\n"
                + "cod_cuenta,\n"
                + "numero_cuenta,\n"
                + "cod_tipo\n"
                + "FROM\n"
                + "\"public\".srh_empleado\n"
                + "WHERE\n"
                + "estado = 1 \n"
                + "and cedula_pass like '" + ci + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

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

    public TablaGenerica validar(String id) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_anticipo,ide_empleado_garante,ci_garante,garante,ide_empleado_solicitante,ci_solicitante,solicitante,\n"
                + "fecha_actua_solicitante,ide_estado_anticipo,aprobado_solicitante\n"
                + "FROM srh_anticipo WHERE ci_solicitante like'" + id + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica estado() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo WHERE ide_estado_tipo = 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica ide_listado(Integer lista) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "s.ide_solicitud_anticipo,\n"
                + "s.ide_listado,\n"
                + "s.anio,\n"
                + "d.periodo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo s\n"
                + "INNER JOIN srh_detalle_anticipo d ON d.ide_anticipo = s.ide_solicitud_anticipo\n"
                + "where s.ide_solicitud_anticipo =" + lista);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica get_cuotadic(Integer lista) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT s.ide_solicitud_anticipo,c.val_cuo_adi\n"
                + "FROM srh_solicitud_anticipo s\n"
                + "INNER JOIN srh_calculo_anticipo c \n"
                + "ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where s.ide_solicitud_anticipo =" + lista);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public void migra_lista(String cedula, String usu) {
        String nomina = "insert into srh_solicitud_anticipo (ci_solicitante,solicitante,rmu,id_distributivo,cod_cargo,login_ingre_solicitud,ip_ingre_solicitud,login_aprob_solicitud, \n"
                + "ip_aprob_solicitud,aprobado_solicitante,fecha_aprobacion,ide_listado,fecha_listado,anio,periodo,ide_tipo_anticipo) \n"
                + "SELECT cedula, solicitante,rmu,id_distributivo,cod_cargo, \n"
                + "'" + usu + "' as login_ingreso, \n"
                + "'" + utilitario.getIp() + "' as ip_ingreso, \n"
                + "'" + usu + "' as login_aprobacion, \n"
                + "'" + utilitario.getIp() + "' as ip_aprobacion, \n"
                + "1 as aprobacion, \n"
                + "'" + utilitario.getFechaActual() + "' as fecha_aprobacion, \n"
                + "'LIST-2014-00000' as id_listado, \n"
                + "'" + utilitario.getFechaActual() + "' as fecha_listado, \n"
                + "" + utilitario.getAnio(utilitario.getFechaActual()) + " as anio, \n"
                + "" + utilitario.getMes(utilitario.getFechaActual()) + " as periodo, \n"
                + "2 as tipo_ant \n"
                + "FROM srh_migrar_anticipo \n"
                + "WHERE cedula ilike '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();
    }

    public void migra_calculo(String cedula, Integer ide) {
        String nomina = "insert into srh_calculo_anticipo(fecha_anticipo,valor_anticipo,numero_cuotas_anticipo,valor_cuota_mensual,val_cuo_adi,ide_periodo_anticipo_inicial,\n"
                + "ide_periodo_anticipo_final,ide_solicitud_anticipo,ide_estado_anticipo)\n"
                + "SELECT\n"
                + "'2014-11-19' as fecha,\n"
                + "saldo,\n"
                + "cast((saldo/cuotas_pendientes) as numeric(6,2)) as cuotas,\n"
                + "cast(cuota_adicional as numeric) as cuota_adi,\n"
                + "11 as periodo,\n"
                + "(11+cuotas_pendientes) as fin,\n"
                + "" + ide + " as ide,\n"
                + "2 as aprobado\n"
                + "FROM\n"
                + "srh_migrar_anticipo\n"
                + "where cedula like '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();
    }

    public void migra_calculo1(String cedula, Integer ide) {
        String nomina = "insert into srh_calculo_anticipo(fecha_anticipo,valor_anticipo,numero_cuotas_anticipo,valor_cuota_mensual,val_cuo_adi,ide_periodo_anticipo_inicial,\n"
                + "ide_periodo_anticipo_final,ide_solicitud_anticipo,ide_estado_anticipo)\n"
                + "SELECT\n"
                + "'2014-11-19' as fecha,\n"
                + "saldo,\n"
                + "cuotas_pendientes,\n"
                + "cast(((saldo-cuota_adicional)/(cuotas_pendientes-1)) as numeric(6,2)) as cuotas,\n"
                + "cast(cuota_adicional as numeric) as cuota_adi,\n"
                + "11 as periodo,\n"
                + "(11+cuotas_pendientes) as fin,\n"
                + "" + ide + " as ide,\n"
                + "2 as aprobado\n"
                + "FROM\n"
                + "srh_migrar_anticipo\n"
                + "where cedula like '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();
    }

    public TablaGenerica cod_listado(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_solicitud_anticipo,ci_solicitante FROM srh_solicitud_anticipo where ci_solicitante  like '" + cedula + "' and  ide_listado like 'LIST-2014-00000'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica cod_detalle(Integer ide) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_calculo_anticipo,\n"
                + "fecha_anticipo,\n"
                + "valor_anticipo,\n"
                + "numero_cuotas_anticipo,\n"
                + "valor_cuota_mensual,\n"
                + "val_cuo_adi,\n"
                + "ide_periodo_anticipo_inicial,\n"
                + "ide_periodo_anticipo_final\n"
                + "FROM\n"
                + "srh_calculo_anticipo\n"
                + "where ide_solicitud_anticipo =" + ide);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica login_solicitud(String ide) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_solicitud_anticipo,\n"
                + "ide_listado,\n"
                + "login_aprob_solicitud,\n"
                + "login_ingre_solicitud\n"
                + "FROM\n"
                + "srh_solicitud_anticipo\n"
                + "where ide_listado like '" + ide + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica director() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT e.cod_empleado,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.estado,\n"
                + "e.cod_cargo,\n"
                + "c.nombre_cargo\n"
                + "FROM srh_empleado e\n"
                + "INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo\n"
                + "WHERE  e.cedula_pass = '1702785849' AND e.estado = 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica cargoSolic(String nombre) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT e.cod_empleado,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.estado,\n"
                + "e.cod_cargo,\n"
                + "c.nombre_cargo\n"
                + "FROM srh_empleado e\n"
                + "INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo\n"
                + "WHERE e.nombres like '" + nombre + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public String listaMax() {
        conPostgresql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conPostgresql();
        tab_consulta.setConexion(conPostgres);
        tab_consulta.setSql("select 0 as id, \n"
                + "(case when max(ide_listado) is null then 'LIST-2014-00000' when max(ide_listado)is not null then max(ide_listado) end) AS maximo\n"
                + " from srh_solicitud_anticipo");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public void actuaSolicitud(Integer anti, String cuota, Integer aprob, String usu) {
        String auSql = "update srh_solicitud_anticipo\n"
                + "set aprobado_solicitante =" + aprob + ",\n"
                + "login_aprob_solicitud ='" + usu + "',\n"
                + "ip_aprob_solicitud ='" + utilitario.getIp() + "',\n"
                + "fecha_aprobacion ='" + utilitario.getFechaActual() + "'\n"
                + "WHERE ide_solicitud_anticipo=" + anti + " and ci_solicitante = '" + cuota + "'";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaMigrar(Double rmu, Integer anti, Integer codigo, Integer aprob, Double rmua, String cedula, Integer migrar) {
        String auSql = "UPDATE srh_migrar_anticipo\n"
                + "set rmu=" + rmu + ",\n"
                + "id_distributivo=" + anti + ",\n"
                + "ide_empleado_solicitante=" + codigo + ",\n"
                + "rmu_liquido_anterior=" + rmua + ",\n"
                + "cod_cargo=" + aprob + "\n"
                + "where cedula like'" + cedula + "' and ide_migrar=" + migrar;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaDaMigrar(Integer grupo, Integer tipo, Integer banco, Integer cuenta, String numero, String cedula, Integer migrar) {
        String auSql = "UPDATE srh_migrar_anticipo\n"
                + "set cod_grupo=" + grupo + ",\n"
                + "cod_tipo=" + tipo + ",\n"
                + "cod_banco=" + banco + ",\n"
                + "cod_cuenta=" + cuenta + ",\n"
                + "numero_cuenta='" + numero + "'\n"
                + "where cedula like'" + cedula + "' and ide_migrar=" + migrar;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actualizSolicitud(Integer anti, String cedula) {
        String auSql = "update srh_calculo_anticipo\n"
                + "set ide_estado_anticipo =(SELECT ide_estado_tipo FROM srh_estado_anticipo WHERE estado like 'APROBADO')\n"
                + "where ide_solicitud_anticipo = (SELECT\n"
                + "ide_solicitud_anticipo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo\n"
                + "where ide_solicitud_anticipo = " + anti + " and ci_solicitante like '" + cedula + "' and aprobado_solicitante =1)";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void negarSolicitud(Integer anti, String cedula) {
        String auSql = "update srh_calculo_anticipo\n"
                + "set ide_estado_anticipo =(SELECT ide_estado_tipo FROM srh_estado_anticipo WHERE estado like 'NEGADO')\n"
                + "where ide_solicitud_anticipo = (SELECT\n"
                + "ide_solicitud_anticipo\n"
                + "FROM\n"
                + "srh_solicitud_anticipo\n"
                + "where ide_solicitud_anticipo = " + anti + " and ci_solicitante like '" + cedula + "')";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void llenarListado(Integer solic, Integer ide, String cedula, String lista) {
        String auSql = "UPDATE srh_solicitud_anticipo\n"
                + "set ide_listado ='" + lista + "',\n"
                + "fecha_listado ='" + utilitario.getFechaActual() + "'\n"
                + "where ide_solicitud_anticipo = " + solic + " and ide_empleado_solicitante = " + ide + " and ci_solicitante ilike '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaDevolucion(Integer solic) {
        String auSql = "UPDATE srh_calculo_anticipo set ide_estado_anticipo = 1\n"
                + "where ide_solicitud_anticipo =" + solic;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public TablaGenerica getPeriodoA(Integer ide) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_anticipo,\n"
                + "ide_detalle_anticipo,\n"
                + "ide_periodo_descuento,\n"
                + "periodo,\n"
                + "anio\n"
                + "FROM\n"
                + "srh_detalle_anticipo\n"
                + "where periodo is null and anio is null and ide_anticipo =" + ide);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getSolicitud(Integer ide) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT s.ide_solicitud_anticipo,s.ci_solicitante,s.solicitante,c.valor_anticipo,c.valor_cuota_mensual,c.val_cuo_adi,c.numero_cuotas_pagadas,\n"
                + "c.valor_pagado,(valor_anticipo-valor_pagado)as saldo\n"
                + "FROM srh_solicitud_anticipo s INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where c.ide_estado_anticipo in (2,3) and s.ide_solicitud_anticipo =" + ide);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getSoli_Detalle(String cedula) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT s.ide_solicitud_anticipo,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante,\n"
                + "c.ide_calculo_anticipo,\n"
                + "c.valor_anticipo,\n"
                + "c.valor_pagado,\n"
                + "(valor_anticipo-valor_pagado) AS saldo\n"
                + "FROM srh_solicitud_anticipo s \n"
                + "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where c.ide_estado_anticipo in (2,3) and s.ci_solicitante = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public void set_ActDetalle_PagoAnti(Integer ide, String periodo, String anio, String usu, String fecha) {
        String auSql = "update srh_detalle_anticipo \n"
                + "set ide_periodo_descontado=a.ide_periodo_anticipo, \n"
                + "ide_estado_cuota=1, \n"
                + "usu_pago_anticipado='" + usu + "', \n"
                + "fecha_pago_anticipado='" + fecha + "' \n"
                + "from (select ide_periodo_anticipo,periodo from srh_periodo_anticipo\n"
                + "where periodo = '" + periodo + "' and anio='" + anio + "') as a\n"
                + "where ide_periodo_descontado is null and ide_estado_cuota is null and ide_anticipo =" + ide;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void set_ActDetalle_Pagoliq(Integer ide, Integer periodo, String usu, String fecha) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set ide_periodo_descontado=" + periodo + ",\n"
                + "ide_estado_cuota=1,\n"
                + "usu_cobro_liquidacion='" + usu + "',\n"
                + "fecha_cobro_liquidacion='" + fecha + "'\n"
                + "where ide_periodo_descontado is null and ide_estado_cuota is null and ide_anticipo =" + ide;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void set_ActCalculo_PagoAnti(Integer solic, Integer calcu, String usu, String fecha, String doc, double valor) {
        String auSql = "UPDATE srh_calculo_anticipo \n"
                + "SET ide_estado_anticipo =4, \n"
                + "usu_pago_anticipado='" + usu + "', \n"
                + "fecha_pago_anticipado='" + fecha + "', \n"
                + "numero_documento_pago ='" + doc + "',\n"
                + "numero_cuotas_pagadas= a.numero_cuotas_anticipo,\n"
                + "valor_pagado =(a.valor_pagado+(" + valor + ") )\n"
                + "from (SELECT ide_calculo_anticipo,numero_cuotas_anticipo,valor_pagado,numero_cuotas_pagadas\n"
                + "from srh_calculo_anticipo\n"
                + "where ide_solicitud_anticipo = " + solic + ") as a\n"
                + "where srh_calculo_anticipo.ide_solicitud_anticipo = " + solic + " and srh_calculo_anticipo.ide_calculo_anticipo =" + calcu;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void set_ActCalculo_Pagoliq(Integer solic, Integer calcu, String usu, String fecha, String doc) {
        String auSql = "UPDATE srh_calculo_anticipo\n"
                + "SET ide_estado_anticipo =4,\n"
                + "usu_cobra_liquidacion='" + usu + "',\n"
                + "fecha_cobro_liquidacion='" + fecha + "',\n"
                + "comentario_cobro ='" + doc + "'\n"
                + "where ide_solicitud_anticipo = " + solic + " and ide_calculo_anticipo =" + calcu;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void set_ActSolicitud_PagoAnti(Integer solic) {
        String auSql = "update srh_solicitud_anticipo\n"
                + "set aprobado_solicitante = 2\n"
                + "where ide_solicitud_anticipo = " + solic;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void set_ActSolicitud_Pagoliq(Integer solic) {
        String auSql = "update srh_solicitud_anticipo\n"
                + "set aprobado_solicitante = 2\n"
                + "where ide_solicitud_anticipo = " + solic;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaPerAnio15(Integer anti) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set periodo =d.periodo, \n"
                + "anio =d.anio\n"
                + "from (SELECT\n"
                + "ide_periodo_anticipo,\n"
                + "periodo,\n"
                + "anio\n"
                + "FROM\n"
                + "srh_periodo_anticipo ) as d\n"
                + "where \n"
                + "srh_detalle_anticipo.periodo is null and\n"
                + "srh_detalle_anticipo.anio is null and \n"
                + "d.ide_periodo_anticipo = srh_detalle_anticipo.ide_periodo_descuento  and \n"
                + "srh_detalle_anticipo.ide_anticipo = " + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaPerAnio16(Integer anti) {
        String auSql = "update srh_detalle_anticipo\n"
                + "set periodo =d.periodo, \n"
                + "anio =d.anio\n"
                + "from (SELECT\n"
                + "ide_periodo_anticipo,\n"
                + "periodo,\n"
                + "anio\n"
                + "FROM\n"
                + "srh_periodo_anticipo ) as d\n"
                + "where \n"
                + "srh_detalle_anticipo.periodo is null and\n"
                + "srh_detalle_anticipo.anio is null and \n"
                + "d.ide_periodo_anticipo = srh_detalle_anticipo.ide_periodo_descuento  and \n"
                + "srh_detalle_anticipo.ide_anticipo = " + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaSoliDevolucion(Integer solic) {
        String auSql = "UPDATE srh_solicitud_anticipo set login_aprob_solicitud = null,\n"
                + "ip_aprob_solicitud = null,fecha_aprobacion = null,aprobado_solicitante = null\n"
                + "where ide_solicitud_anticipo =" + solic;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void actuaCalculo(Integer solic, Double valor) {
        String auSql = "UPDATE srh_calculo_anticipo\n"
                + "set valor_cuota_mensual =" + valor + "\n"
                + "where ide_solicitud_anticipo =" + solic;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void llenarSolicitud(Integer anti, String cuota, Double valor, Integer perdes) {
        String auSql = "insert into srh_detalle_anticipo (ide_anticipo,cuota,valor,ide_periodo_descuento)\n"
                + "values (" + anti + ",'" + cuota + "'," + valor + "," + perdes + ")";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void llenarSolicitud1(Integer anti, String cuota, Double valor, Integer perdes, Double valor1) {
        String auSql = "insert into srh_detalle_anticipo (ide_anticipo,cuota,valor,ide_periodo_descuento,valor_cuota_mensual)\n"
                + "values (" + anti + ",'" + cuota + "'," + valor + "," + perdes + "," + valor1 + ")";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteDetalle(Integer anti) {
        String auSql = "delete from srh_detalle_anticipo where ide_solicitud_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteCalculo(Integer anti) {
        String auSql = "delete from srh_calculo_anticipo where ide_solicitud_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteGarante(Integer anti) {
        String auSql = "delete from srh_garante_anticipo where ide_solicitud_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteSolicitud(Integer anti) {
        String auSql = "delete from srh_solicitud_anticipo where ide_solicitud_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteDevolver(Integer anti) {
        String auSql = "delete from srh_detalle_anticipo where ide_anticipo =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public void deleteMigrar() {
        String auSql = "delete from srh_migrar_anticipo";
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    public TablaGenerica getUsuario(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        con_sigag();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT IDE_USUA,NOM_USUA FROM SIS_USUARIO where NICK_USUA like '" + iden + "'");
        tabFuncionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getTranferencia(Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        con_sigag();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT ide_detalle_listado,num_transferencia FROM tes_detalle_comprobante_pago_listado where ide_detalle_listado =" + iden);
        tabFuncionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tabFuncionario;
    }

    private void con_sigag() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
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
