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
    private Conexion con_postgres;
    
    //Saca información de los servidores que tiens solicitudes pendientes
    public TablaGenerica VerifEmpleid(String cedu,Integer tipo ){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "s.ide_empleado_solicitante,\n" +
                "s.ci_solicitante,\n" +
                "s.solicitante,\n" +
                "s.ide_tipo_anticipo\n" +
                "FROM\n" +
                "srh_solicitud_anticipo s\n" +
                "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "WHERE\n" +
                "s.ci_solicitante LIKE '"+cedu+"' and s.ide_tipo_anticipo ="+tipo+"\n" +
                "and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR\n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR\n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
//Busqueda en roles de datos de servidor que solicita por numero de cedula
    public TablaGenerica empleadosCed(String cedula,Integer anio,Integer mes){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from  \n" +
                                " (select aa.*,a.FONDOS_RESERVA,(case when a.FONDOS_RESERVA is NULL then '0' when a.FONDOS_RESERVA > 0 then a.FONDOS_RESERVA end ) as confirma1,  \n" +
                                " b.HORAS_EXTRAS,(case when b.HORAS_EXTRAS is NULL then '0' when b.HORAS_EXTRAS > 0 then b.HORAS_EXTRAS end ) as confirma2,  \n" +
                                " c.OTROS_INGRESOS,(case when c.OTROS_INGRESOS is NULL then '0' when c.OTROS_INGRESOS > 0 then c.OTROS_INGRESOS end ) as confirma3,  \n" +
                                " d.TOTAL_INGRESOS,f.APORTE_IESS,g.IMPUESTO_RENTA,(case when g.IMPUESTO_RENTA is NULL then '0' when g.IMPUESTO_RENTA > 0 then g.IMPUESTO_RENTA end ) as confirma4,  \n" +
                                " h.PRESTAMO_IESS,(case when h.PRESTAMO_IESS is NULL then '0' when h.PRESTAMO_IESS > 0 then h.PRESTAMO_IESS end ) as confirma5,  \n" +
                                " i.ANTICIPOS,(case when i.ANTICIPOS is NULL then '0' when i.ANTICIPOS > 0 then i.ANTICIPOS end ) as confirma6,  \n" +
                                " j.OTROS_EGRESOS,(case when j.OTROS_EGRESOS is NULL then '0' when j.OTROS_EGRESOS > 0 then j.OTROS_EGRESOS end ) as confirma7,k.TOTAL_EGRESOS from  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,(case when e.partida_indv is NULL then '0' when e.partida_indv <> NULL then e.partida_indv end ) as confirma,c.cod_cargo,c.nombre_cargo,r.valor AS RU,id_distributivo_roles  \n" +
                                "   \n" +
                                " from srh_roles as r inner join prec_programas as  p  \n" +
                                " on r.ide_programa=p.ide_programa  \n" +
                                " inner join srh_empleado as e  \n" +
                                " on e.cod_empleado=r.ide_empleado  \n" +
                                " inner join srh_cargos  as c  \n" +
                                " on c.cod_cargo=e.cod_cargo  \n" +
                                " where  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (14) and e.cedula_pass like '"+cedula+"' -- and valor>0  \n" +
                                " order by p.ide_funcion) AS aa  \n" +
                                "   \n" +
                                " left join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0  \n" +
                                " order by p.ide_funcion) as a  \n" +
                                " on aa.COD_EMPLEADO=a.COD_EMPLEADO  \n" +
                                "   \n" +
                                " left join  \n" +
                                "   \n" +
                                " --100%+ 25%  \n" +
                                " (select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e  \n" +
                                " where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0  \n" +
                                " group by E.COD_EMPLEADO) AS b  \n" +
                                " on aa.COD_EMPLEADO=b.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c  \n" +
                                " on aa.COD_EMPLEADO=c.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d  \n" +
                                " on aa.COD_EMPLEADO=d.COD_EMPLEADO  \n" +
                                " --------------------------------------EGRESOS  \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1and ide_periodo="+mes+" and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f  \n" +
                                " on aa.COD_EMPLEADO=f.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g  \n" +
                                " on aa.COD_EMPLEADO=g.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h  \n" +
                                " on aa.COD_EMPLEADO=h.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo=6 and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i  \n" +
                                " on aa.COD_EMPLEADO=i.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j  \n" +
                                " on aa.COD_EMPLEADO=j.COD_EMPLEADO  \n" +
                                "   \n" +
                                " LEFT join  \n" +
                                "   \n" +
                                " (select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and  \n" +
                                " ano= "+anio+"  \n" +
                                " and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k  \n" +
                                " on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m  \n" +
                                " order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //trabajador por numero de cedulas
    public TablaGenerica trabajadoresCed(String cedula,Integer anio,Integer mes){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n" +
                        "(\n" +
                        "select aa.*,a.FONDOS_RESERVA,b.HORAS_EXTRAS,\n" +
                        "c.SUB_FAMILIAR,d.SUB_ANTIGUEDAD,e.SUB_COMISARIATO,f.OTROS_INGRESOS,h.APORTE_IESS,i.PRESTAMO_IESS,j.ANTICIPOS,k.OTROS_EGRESOS,\n" +
                        "l.TOTAL_EGRESOS,total_ingresos\n" +
                        "from\n" +
                        "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,c.cod_cargo,c.nombre_cargo,r.valor AS SU,id_distributivo_roles\n" +
                        "from srh_roles as r inner join prec_programas as  p\n" +
                        "on r.ide_programa=p.ide_programa\n" +
                        "inner join srh_empleado as e\n" +
                        "on e.cod_empleado=r.ide_empleado\n" +
                        "inner join srh_cargos  as c\n" +
                        "on c.cod_cargo=e.cod_cargo\n" +
                        "where ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (40) and e.cedula_pass like '"+cedula+"' -- and valor>0\n" +
                        "order by p.ide_funcion) AS aa\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (89)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "order by p.ide_funcion) as a\n" +
                        "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "--100%+ 50%\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (75,76)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS b\n" +
                        "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (98)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS c\n" +
                        "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (99)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS d\n" +
                        "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (101)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS e\n" +
                        "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (90,41,45,115)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n" +
                        "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(40,125,45,75,76,89,98,99,100,102,101,107,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n" +
                        "E.COD_EMPLEADO) as g\n" +
                        "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (71)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n" +
                        "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (59)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n" +
                        "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (46)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n" +
                        "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n" +
                        "valor>0 GROUP BY E.COD_EMPLEADO) as k\n" +
                        "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(71,59,53,46,72,56,84,74,57,73,47,80,85,48,50,108,55,51,52,106,112,91,110)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n" +
                        "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n" +
                        ") as m\n" +
                        "order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //Busqueda de solicitudes pendientes por codigo de servidor
    
    public TablaGenerica VerifEmpleCod(Integer codigo,Integer tipo){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "s.ide_empleado_solicitante,\n" +
                "s.ci_solicitante,\n" +
                "s.solicitante\n" +
                "FROM\n" +
                "srh_solicitud_anticipo s\n" +
                "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "WHERE\n" +
                "s.ide_empleado_solicitante = "+codigo+" and s.ide_tipo_anticipo = "+tipo+"\n" +
                "and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR\n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR\n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //Busqueda de servidor por apellido
    public TablaGenerica empleados(Integer codigo,Integer anio,Integer mes){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n" +
                                "(select aa.*,a.FONDOS_RESERVA,(case when a.FONDOS_RESERVA is NULL then '0' when a.FONDOS_RESERVA > 0 then a.FONDOS_RESERVA end ) as confirma1,\n" +
                                "b.HORAS_EXTRAS,(case when b.HORAS_EXTRAS is NULL then '0' when b.HORAS_EXTRAS > 0 then b.HORAS_EXTRAS end ) as confirma2,\n" +
                                "c.OTROS_INGRESOS,(case when c.OTROS_INGRESOS is NULL then '0' when c.OTROS_INGRESOS > 0 then c.OTROS_INGRESOS end ) as confirma3,\n" +
                                "d.TOTAL_INGRESOS,f.APORTE_IESS,g.IMPUESTO_RENTA,(case when g.IMPUESTO_RENTA is NULL then '0' when g.IMPUESTO_RENTA > 0 then g.IMPUESTO_RENTA end ) as confirma4,\n" +
                                "h.PRESTAMO_IESS,(case when h.PRESTAMO_IESS is NULL then '0' when h.PRESTAMO_IESS > 0 then h.PRESTAMO_IESS end ) as confirma5,\n" +
                                "i.ANTICIPOS,(case when i.ANTICIPOS is NULL then '0' when i.ANTICIPOS > 0 then i.ANTICIPOS end ) as confirma6,\n" +
                                "j.OTROS_EGRESOS,(case when j.OTROS_EGRESOS is NULL then '0' when j.OTROS_EGRESOS > 0 then j.OTROS_EGRESOS end ) as confirma7,k.TOTAL_EGRESOS from\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,(case when e.partida_indv is NULL then '0' when e.partida_indv <> NULL then e.partida_indv end ) as confirma,c.cod_cargo,c.nombre_cargo,r.valor AS RU,id_distributivo_roles\n" +
                                "\n" +
                                "from srh_roles as r inner join prec_programas as  p\n" +
                                "on r.ide_programa=p.ide_programa\n" +
                                "inner join srh_empleado as e\n" +
                                "on e.cod_empleado=r.ide_empleado\n" +
                                "inner join srh_cargos  as c\n" +
                                "on c.cod_cargo=e.cod_cargo\n" +
                                "where\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (14) and e.cod_empleado ="+codigo+" -- and valor>0\n" +
                                "order by p.ide_funcion) AS aa\n" +
                                "\n" +
                                "left join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0\n" +
                                "order by p.ide_funcion) as a\n" +
                                "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n" +
                                "\n" +
                                "left join\n" +
                                "\n" +
                                "--100%+ 25%\n" +
                                "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e\n" +
                                "where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0\n" +
                                "group by E.COD_EMPLEADO) AS b\n" +
                                "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c\n" +
                                "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d\n" +
                                "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n" +
                                "--------------------------------------EGRESOS\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1and ide_periodo="+mes+" and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n" +
                                "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g\n" +
                                "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n" +
                                "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n" +
                                "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n" +
                                "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+anio+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+mes+" and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k\n" +
                                "on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m\n" +
                                "order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario; 
    }
    
    public TablaGenerica trabajadores(Integer codigo,Integer anio,Integer mes){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("select *,(TOTAL_INGRESOS-TOTAL_EGRESOS) as LIQUIDO_RECIBIR from\n" +
                        "(\n" +
                        "select aa.*,a.FONDOS_RESERVA,b.HORAS_EXTRAS,\n" +
                        "c.SUB_FAMILIAR,d.SUB_ANTIGUEDAD,e.SUB_COMISARIATO,f.OTROS_INGRESOS,h.APORTE_IESS,i.PRESTAMO_IESS,j.ANTICIPOS,k.OTROS_EGRESOS,\n" +
                        "l.TOTAL_EGRESOS,total_ingresos\n" +
                        "from\n" +
                        "(select E.COD_EMPLEADO,e.cod_biometrico,e.cod_banco,cod_tipo,cod_cuenta,cod_grupo,e.numero_cuenta,e.cedula_pass,e.nombres,e.partida_pres,e.partida_indv,c.cod_cargo,c.nombre_cargo,r.valor AS SU,id_distributivo_roles\n" +
                        "from srh_roles as r inner join prec_programas as  p\n" +
                        "on r.ide_programa=p.ide_programa\n" +
                        "inner join srh_empleado as e\n" +
                        "on e.cod_empleado=r.ide_empleado\n" +
                        "inner join srh_cargos  as c\n" +
                        "on c.cod_cargo=e.cod_cargo\n" +
                        "where ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (40) and e.cod_empleado ="+codigo+" -- and valor>0\n" +
                        "order by p.ide_funcion) AS aa\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (89)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "order by p.ide_funcion) as a\n" +
                        "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "--100%+ 50%\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (75,76)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS b\n" +
                        "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (98)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS c\n" +
                        "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (99)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS d\n" +
                        "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (101)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS e\n" +
                        "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (90,41,45,115)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n" +
                        "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(40,125,45,75,76,89,98,99,100,102,101,107,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n" +
                        "E.COD_EMPLEADO) as g\n" +
                        "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (71)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n" +
                        "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (59)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n" +
                        "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in (46)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n" +
                        "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n" +
                        "valor>0 GROUP BY E.COD_EMPLEADO) as k\n" +
                        "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+anio+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+mes+" and ide_columnas in\n" +
                        "(71,59,53,46,72,56,84,74,57,73,47,80,85,48,50,108,55,51,52,106,112,91,110)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n" +
                        "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n" +
                        ") as m\n" +
                        "order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //Verifica si garante se encuentra disponibles
    
    public TablaGenerica VerifGaranteid(String cedula){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "g.ci_garante,\n" +
                "g.garante,\n" +
                "c.ide_estado_anticipo\n" +
                "FROM\n" +
                "srh_solicitud_anticipo s\n" +
                "INNER JOIN srh_garante_anticipo g ON g.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "where g.ci_garante like '"+cedula+"' and (c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR \n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO')OR \n" +
                "c.ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //busqueda de garante por cedulas
    public TablaGenerica Garantemple(String ci){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "e.cod_empleado, e.cedula_pass,\n" +
                "e.nombres,e.fecha_ingreso,\n" +
                "e.fecha_nombramiento,e.id_distributivo,\n" +
                "e.cod_tipo,i.tipo\n" +
                "FROM\n" +
                "srh_empleado e,\n" +
                "srh_tipo_empleado i\n" +
                "WHERE\n" +
                "e.estado = 1 AND\n" +
                "e.cod_tipo = i.cod_tipo AND\n" +
                "e.cod_tipo in (4,7,8,10)\n" +
                "and e.cedula_pass like '"+ci+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica Garantemple1(String ci){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "e.cod_empleado, e.cedula_pass,\n" +
                "e.nombres,e.fecha_ingreso,\n" +
                "e.fecha_nombramiento,e.id_distributivo,\n" +
                "e.cod_tipo,i.tipo\n" +
                "FROM\n" +
                "srh_empleado e,\n" +
                "srh_tipo_empleado i\n" +
                "WHERE\n" +
                "e.estado = 1 AND\n" +
                "e.cod_tipo = i.cod_tipo AND\n" +
                "e.cod_tipo in (4,7,8,10,3)\n" +
                "and e.cedula_pass like '"+ci+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica GarNumConat(Integer ci){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cod_empleado,cod_tipo \n" +
                                "FROM srh_num_contratos\n" +
                                "where cod_tipo in (4,7,8,10) and cod_empleado ="+ci);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica GarNumConat1(Integer ci){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cod_empleado,cod_tipo \n" +
                                "FROM srh_num_contratos\n" +
                                "where cod_tipo in (4,7,8,10,3) and cod_empleado ="+ci);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
        
    //buscar garante por apellido
    public TablaGenerica VerifGaranteCod(Integer codigo){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "s.ide_empleado_solicitante,\n" +
                "g.ide_empleado_garante,\n" +
                "g.ci_garante,\n" +
                "g.garante\n" +
                "FROM\n" +
                "srh_solicitud_anticipo AS s\n" +
                "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "INNER JOIN srh_garante_anticipo g ON g.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "WHERE\n" +
                "g.ide_empleado_garante = "+codigo+" AND\n" +
                "(c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO') OR\n" +
                "c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO') OR\n" +
                "c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica GaranteNom(Integer empleado){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cod_empleado,cedula_pass,nombres,id_distributivo,cod_tipo\n" +
                "FROM srh_empleado WHERE estado = 1 AND cod_tipo IN (4,7,8,3) AND cod_empleado ="+empleado+" order by nombres ");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    //Permite utilizar las fecha de inicio y fin de contrato
    public TablaGenerica FechaContrato(Integer codigoE){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT fecha_contrato,cod_tipo,ide_num_contrato,fecha_fin\n" +
                "FROM srh_num_contratos\n" +
                "where cod_empleado = "+codigoE+"\n" +
                "ORDER BY fecha_contrato DESC LIMIT 1");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica periodos(String mes, String anio ){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_periodo_anticipo,periodo,mes,anio FROM srh_periodo_anticipo where mes like '"+mes+"' and anio like '"+anio+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica periodos1(Integer id ){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_periodo_anticipo,periodo,mes,anio FROM srh_periodo_anticipo where ide_periodo_anticipo ="+id);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica pagosAndelantados(String cedula){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                "s.ide_solicitud_anticipo,\n" +
                "s.ci_solicitante,\n" +
                "s.solicitante,\n" +
                "s.rmu,\n" +
                "c.valor_anticipo,\n" +
                "c.numero_cuotas_anticipo,\n" +
                "c.valor_pagado,\n" +"c.numero_cuotas_pagadas,\n" +
                "(c.valor_anticipo-c.valor_pagado)as saldo\n" +
                "FROM\n" +
                "srh_solicitud_anticipo AS s\n" +
                "INNER JOIN srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "where ci_solicitante = '"+cedula+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }
    
    public TablaGenerica VerificarCuota(String anio,String periodo){
        con_postgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        con_postgresql();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT his_codigo,his_ide_empleado,his_nombre,his_periodo,his_anio,his_cuota_roles,his_cuota_anticipo,his_cuota_nueva,\n" +
                "his_cuotas_faltantes,his_cuotas_anticipo,his_cuotas_pagadas\n" +
                "FROM srh_historial_cuotas_anticipos\n" +
                "where his_anio='"+anio+"' and his_periodo='"+periodo+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
    }    
    
    public void InsertarAnticipo(String periodo,String anio){
        // Forma el sql para el ingreso
        String str_sql3 = "insert into srh_historial_cuotas_anticipos(his_ide_solicitud,his_ide_empleado,his_nombre,his_periodo,his_anio,his_cuota_roles,\n" +
"his_cuota_anticipo,his_cuota_nueva,his_cuotas_faltantes,his_cuotas_anticipo,his_cuotas_pagadas,his_periodo_descuento,his_mes_actual,his_anio_actual)\n" +
"select ide_solicitud_anticipo,ide_empleado_solicitante,solicitante,periodo,anio,cuota_roles,cuota_anticipo,cuota_nueva,cuotas_faltan,numero_cuotas_anticipo,numero_cuotas_pagadas,ide_periodo_descuento  \n" +
",extract(month from current_date) as mes,extract(year from current_date) as ano from \n" +
"(select aa.*,a.valor as cuota_roles,b.valor as cuota_anticipo, \n" +
"cast((case when aa.cuotas_faltan =1 then b.valor  \n" +
"when aa.cuotas_faltan >=2 then ((b.valor-a.valor)/(aa.cuotas_faltan-1)) end )as numeric(5,2)) as cuota_nueva \n" +
"from \n" +
"(SELECT a.ide_solicitud_anticipo, \n" +
"a.ide_empleado_solicitante, \n" +
"a.solicitante, \n" +
"d.periodo, \n" +
"d.anio, \n" +
"f.numero_cuotas_anticipo, \n" +
"f.numero_cuotas_pagadas, \n" +
"(f.numero_cuotas_anticipo-f.numero_cuotas_pagadas) as cuotas_faltan,\n" +
"d.ide_periodo_descuento\n" +
"from srh_detalle_anticipo d  \n" +
",srh_solicitud_anticipo a   \n" +
",srh_calculo_anticipo as f \n" +
"where d.ide_anticipo = a.ide_solicitud_anticipo   \n" +
"and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo    \n" +
"and d.periodo = '"+periodo+"'   \n" +
"and d.ide_estado_cuota is null   \n" +
"and d.anio = '"+anio+"'   \n" +
"and f.ide_estado_anticipo in (2,3) \n" +
"order by a.solicitante)as aa \n" +
"left join  \n" +
"(SELECT  \n" +
"s.ide_solicitud_anticipo,  \n" +
"s.ide_empleado_solicitante,  \n" +
"s.solicitante,  \n" +
"r.ide_roles,  \n" +
"r.ide_empleado,  \n" +
"r.valor,  \n" +
" r.ide_periodo,  \n" +
" r.ano,  \n" +
" r.id_distributivo_roles  \n" +
" FROM  srh_solicitud_anticipo AS s ,  \n" +
" srh_roles AS r,  \n" +
" srh_calculo_anticipo c  \n" +
" WHERE  \n" +
" s.ide_empleado_solicitante = r.ide_empleado AND  \n" +
" c.ide_solicitud_anticipo = s.ide_solicitud_anticipo and  \n" +
" r.ano =  "+anio+"  AND  \n" +
" r.ide_periodo =  "+periodo+"  AND  \n" +
" r.ide_columnas IN (1, 46)AND  \n" +
" c.ide_estado_anticipo in (2,3)  \n" +
" ORDER BY  \n" +
" r.id_distributivo_roles)as a \n" +
" on aa.ide_empleado_solicitante= a.ide_empleado_solicitante \n" +
" left join  \n" +
" (select cast (id_distributivo as int)  \n" +
" ,cast (anio as int)  \n" +
" ,(case when id_distributivo = 1 then 1 when id_distributivo = 2 then 46 end ) AS dist  \n" +
" ,cast (periodo as int)  \n" +
" ,valor,ci_solicitante  \n" +
" ,solicitante  \n" +
" ,ide_empleado  \n" +
" ,ide_empleado_solicitante   \n" +
" from (   \n" +
" select * from (   \n" +
" SELECT a.id_distributivo  \n" +
" ,d.anio,sum(d.valor) as valor  \n" +
" ,a.ci_solicitante, a.solicitante  \n" +
" ,a.ide_empleado_solicitante as ide_empleado  \n" +
" ,a.ide_empleado_solicitante  \n" +
" ,d.periodo   \n" +
" from srh_detalle_anticipo d  \n" +
" ,srh_solicitud_anticipo a   \n" +
" ,srh_calculo_anticipo as f   \n" +
" where d.ide_anticipo = a.ide_solicitud_anticipo   \n" +
" and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo    \n" +
" and d.periodo = '"+periodo+"'   \n" +
" and d.ide_estado_cuota is null   \n" +
" and d.anio = '"+anio+"'   \n" +
" and f.ide_estado_anticipo <> 5   \n" +
" group by a.ci_solicitante  \n" +
" ,a.id_distributivo  \n" +
" ,d.anio, a.solicitante  \n" +
" ,a.ide_empleado_solicitante  \n" +
" ,a.ide_empleado_solicitante  \n" +
" ,d.periodo   \n" +
" having count(a.ci_solicitante)<=1 order by a.solicitante) as a   \n" +
" union select * from (   \n" +
" SELECT a.id_distributivo  \n" +
" ,d.anio  \n" +
" ,sum(d.valor) as valor  \n" +
" ,a.ci_solicitante  \n" +
" ,a.solicitante  \n" +
" ,a.ide_empleado_solicitante as ide_empleado  \n" +
" ,a.ide_empleado_solicitante   \n" +
" ,d.periodo   \n" +
" FROM srh_detalle_anticipo d  \n" +
" ,srh_solicitud_anticipo a  \n" +
" ,srh_calculo_anticipo as f   \n" +
" WHERE d.ide_anticipo = a.ide_solicitud_anticipo   \n" +
" and a.ide_solicitud_anticipo  = f.ide_solicitud_anticipo   \n" +
" and d.periodo = '"+periodo+"'    \n" +
" and d.ide_estado_cuota is null   \n" +
" and d.anio = '"+anio+"'  \n" +
" and f.ide_estado_anticipo <> 5   \n" +
" group by a.ci_solicitante  \n" +
" ,a.id_distributivo  \n" +
" ,d.anio  \n" +
" ,a.solicitante  \n" +
" ,a.ide_empleado_solicitante  \n" +
" ,a.ide_empleado_solicitante  \n" +
" ,d.periodo   \n" +
" having count(a.ci_solicitante)>1 order by a.solicitante) as b ) as c order by solicitante) as b \n" +
" on aa.ide_empleado_solicitante= b.ide_empleado \n" +
" )as p";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteCalculo(Integer anti,Integer cal,String usu){
        String au_sql="UPDATE srh_calculo_anticipo\n" +
                "SET ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado ='NEGADO') ,\n" +
                "usu_anulacion = '"+usu+"' where ide_calculo_anticipo = "+cal+" and ide_solicitud_anticipo = "+anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteSolicitud(Integer anti,String cedula,String usu,String comen,String fecha){
        String au_sql="UPDATE srh_solicitud_anticipo\n" +
                "SET usu_anulacion='"+usu+"',\n" +
                "comen_anulacion='"+comen+"',\n" +
                "fecha_anulacion='"+fecha+"',\n" +
                "aprobado_solicitante =0\n" +
                "where ide_solicitud_anticipo = "+anti+" and ci_solicitante ='"+cedula+"'";
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteDetalle(Integer anti){
        String au_sql="update srh_detalle_anticipo\n" +
                "set ide_periodo_descontado= 0,\n" +
                "ide_estado_cuota = 0\n" +
                "where ide_anticipo ="+anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void ActualizaAnticipo(String anio){
        String str_sql4 = "update srh_calculo_anticipo  \n" +
                "set valor_pagado =h.pagado, \n" +
                "numero_cuotas_pagadas=cuota, \n" +
                "ide_estado_anticipo = 3 \n" +
                "from (SELECT ide_anticipo,sum(valor) as pagado,sum(ide_estado_cuota) as cuota\n" +
                "FROM srh_detalle_anticipo \n" +
                "where ide_estado_cuota = 1 and \n" +
                "periodo in (select DISTINCT periodo from srh_detalle_anticipo \n" +
                "where ide_estado_cuota = 1 and usu_pago_anticipado is null and fecha_pago_anticipado is null and anio = '"+anio+"' order by periodo)\n" +
                "GROUP BY ide_anticipo\n" +
                "order by ide_anticipo) h \n" +
                "where srh_calculo_anticipo.ide_solicitud_anticipo = h.ide_anticipo";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }   
    
    public void ActualizarDetalleAnticipo(Integer anio,Integer periodo){
        // Forma el sql para el ingreso
        String str_sql4 = "update srh_detalle_anticipo \n" +
                "set ide_periodo_descontado = srh_detalle_anticipo.ide_periodo_descuento, \n" +
                "ide_estado_cuota = 1 \n" +
                "from  (\n" +
                "SELECT\n" +
                "s.ide_solicitud_anticipo,\n" +
                "s.ide_empleado_solicitante,\n" +
                "s.solicitante,\n" +
                "r.ide_roles,\n" +
                "r.ide_empleado,\n" +
                "r.valor,\n" +
                "r.ide_periodo,\n" +
                "r.ano,\n" +
                "r.id_distributivo_roles\n" +
                "FROM\n" +
                "\"public\".srh_solicitud_anticipo AS s ,\n" +
                "\"public\".srh_roles AS r,\n" +
                "\"public\".srh_calculo_anticipo c\n" +
                "WHERE\n" +
                "s.ide_empleado_solicitante = r.ide_empleado AND\n" +
                "c.ide_solicitud_anticipo = s.ide_solicitud_anticipo and\n" +
                "r.ano = "+anio+" AND\n" +
                "r.ide_periodo = "+periodo+" AND\n" +
                "r.ide_columnas IN (1, 46)AND\n" +
                "c.ide_estado_anticipo in (2,3)\n" +
                "ORDER BY\n" +
                "r.id_distributivo_roles\n" +
                ") d \n" +
                "WHERE \n" +
                "srh_detalle_anticipo.ide_anticipo = d.ide_solicitud_anticipo and\n" +
                "srh_detalle_anticipo.valor = d.valor and \n" +
                "srh_detalle_anticipo.periodo = cast (d.ide_periodo as varchar) and \n" +
                "srh_detalle_anticipo.anio = cast (d.ano as varchar)";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void CamAnticipoF(){
        String str_sql4 = "update srh_calculo_anticipo \n" +
                "SET ide_estado_anticipo = 4 \n" +
                "from ( \n" +
                "SELECT n1.pagado,n2.ide_solicitud_anticipo\n" +
                "from (SELECT count(ide_anticipo) as pagado,ide_anticipo FROM srh_detalle_anticipo where ide_estado_cuota = 1  \n" +
                "GROUP BY ide_anticipo order by ide_anticipo) n1 \n" +
                "inner join (select numero_cuotas_anticipo,ide_solicitud_anticipo from srh_calculo_anticipo order by ide_solicitud_anticipo) n2 \n" +
                "on n1.ide_anticipo = n2.ide_solicitud_anticipo and n1.pagado = n2.numero_cuotas_anticipo) d1 \n" +
                "WHERE srh_calculo_anticipo.ide_solicitud_anticipo = d1.ide_solicitud_anticipo";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void CamAnticipoSo(){
        String str_sql4 = "update srh_solicitud_anticipo\n" +
                "set aprobado_solicitante = 2\n" +
                "from (\n" +
                "SELECT n1.pagado,n2.ide_anticipo\n" +
                "from (SELECT count(ide_anticipo) as pagado,ide_anticipo FROM srh_detalle_anticipo where ide_estado_cuota = 1 \n" +
                "GROUP BY ide_anticipo) n1\n" +
                "inner join (SELECT count(ide_anticipo) as pagando,ide_anticipo FROM srh_detalle_anticipo GROUP BY ide_anticipo) n2\n" +
                "on n1.ide_anticipo = n2.ide_anticipo and n1.pagado = n2.pagando ) d1\n" +
                "WHERE srh_solicitud_anticipo.ide_solicitud_anticipo = d1.ide_anticipo";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void ProrrogacionCuota(String anio,String periodo,String mes){
        String str_sql4 = "update srh_detalle_anticipo\n" +
"set valor =a.cuota_real\n" +
"FROM\n" +
"(SELECT\n" +
"h.his_codigo,\n" +
"h.his_cuota_nueva,\n" +
"d.ide_anticipo,\n" +
"d.ide_detalle_anticipo,\n" +
"d.valor,\n" +
"d.ide_periodo_descuento,\n" +
"(d.valor+h.his_cuota_nueva)as cuota_real\n" +
"FROM\n" +
"srh_historial_cuotas_anticipos h ,\n" +
"srh_detalle_anticipo d\n" +
"WHERE\n" +
"h.his_mes_actual = '"+periodo+"' \n" +
"and h.his_anio_actual = '"+anio+"'\n" +
"and h.his_ide_solicitud=d.ide_anticipo \n" +
"and d.ide_estado_cuota is null \n" +
"and d.ide_periodo_descuento <> '"+mes+"'\n" +
"order by h.his_codigo) as a\n" +
"where srh_detalle_anticipo.ide_detalle_anticipo = a.ide_detalle_anticipo";
        con_postgresql();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    //metodo que posee la cadena de conexion a base de datos
    private void con_postgresql(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
}
