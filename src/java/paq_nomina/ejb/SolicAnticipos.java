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
    private Conexion con_postgres;
    private Conexion conexion;
    
    ///Buscar en roles
    public TablaGenerica empleados(String nombre){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
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
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (14) and e.nombres like '"+nombre+"' -- and valor>0\n" +
                                "order by p.ide_funcion) AS aa\n" +
                                "\n" +
                                "left join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (86)  and r.ide_programa=p.ide_programa and valor>0\n" +
                                "order by p.ide_funcion) as a\n" +
                                "on aa.COD_EMPLEADO=a.COD_EMPLEADO\n" +
                                "\n" +
                                "left join\n" +
                                "\n" +
                                "--100%+ 25%\n" +
                                "(select E.COD_EMPLEADO,sum(r.valor) AS HORAS_EXTRAS from srh_roles as r, prec_programas as  p, srh_empleado as e\n" +
                                "where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (92,93)  and r.ide_programa=p.ide_programa and valor>0\n" +
                                "group by E.COD_EMPLEADO) AS b\n" +
                                "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as c\n" +
                                "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (86,14,92,93,19,18,20)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as d\n" +
                                "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n" +
                                "--------------------------------------EGRESOS\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (33)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n" +
                                "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS IMPUESTO_RENTA from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (22)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as g\n" +
                                "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (21)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n" +
                                "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (1)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n" +
                                "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n" +
                                "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n" +
                                "\n" +
                                "LEFT join\n" +
                                "\n" +
                                "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r, prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                                "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                                "and  id_distributivo_roles=1 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (33,22,21,1,7,4,8,6,9,5,2,13,39,3,11,10,111,12)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as k\n" +
                                "on aa.COD_EMPLEADO=k.COD_EMPLEADO) as m\n" +
                                "order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario; 
 }
    
    
    public TablaGenerica trabajadores(String nombre){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
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
                        "where ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (40) and e.nombres like '"+nombre+"' -- and valor>0\n" +
                        "order by p.ide_funcion) AS aa\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,r.valor AS FONDOS_RESERVA from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (89)  and\n" +
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
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (75,76)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS b\n" +
                        "on aa.COD_EMPLEADO=b.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_FAMILIAR from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (98)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS c\n" +
                        "on aa.COD_EMPLEADO=c.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_ANTIGUEDAD from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (99)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS d\n" +
                        "on aa.COD_EMPLEADO=d.COD_EMPLEADO\n" +
                        "\n" +
                        "left join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,sum(r.valor) AS SUB_COMISARIATO from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e\n" +
                        "where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (101)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0\n" +
                        "group by E.COD_EMPLEADO) AS e\n" +
                        "on aa.COD_EMPLEADO=e.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (90,41,45,115)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as f\n" +
                        "on aa.COD_EMPLEADO=f.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_INGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in\n" +
                        "(40,89,75,76,90,41,45,115)  and r.ide_programa=p.ide_programa and valor>0 GROUP BY\n" +
                        "E.COD_EMPLEADO) as g\n" +
                        "on aa.COD_EMPLEADO=g.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS APORTE_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (71)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as h\n" +
                        "on aa.COD_EMPLEADO=h.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS PRESTAMO_IESS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (59)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as i\n" +
                        "on aa.COD_EMPLEADO=i.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS ANTICIPOS from srh_roles as r, prec_programas\n" +
                        "as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in (46)  and\n" +
                        "r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as j\n" +
                        "on aa.COD_EMPLEADO=j.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS OTROS_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in\n" +
                        "(80,91,53,50,73,84,49,5,51,74,85,48,72,108)  and r.ide_programa=p.ide_programa and\n" +
                        "valor>0 GROUP BY E.COD_EMPLEADO) as k\n" +
                        "on aa.COD_EMPLEADO=k.COD_EMPLEADO\n" +
                        "\n" +
                        "LEFT join\n" +
                        "\n" +
                        "(select E.COD_EMPLEADO,SUM(r.valor) AS TOTAL_EGRESOS from srh_roles as r,\n" +
                        "prec_programas as  p, srh_empleado as e where e.cod_empleado=r.ide_empleado and\n" +
                        "ano="+utilitario.getAnio(utilitario.getFechaActual())+"\n" +
                        "and  id_distributivo_roles=2 and ide_periodo="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and ide_columnas in\n" +
                        "(71,59,46,80,91,53,50,73,84,49,5,51,74,85,48,72,108)\n" +
                        "and r.ide_programa=p.ide_programa and valor>0 GROUP BY E.COD_EMPLEADO) as l\n" +
                        "on aa.COD_EMPLEADO=l.COD_EMPLEADO\n" +
                        ") as m\n" +
                        "order by nombres");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
   
 public TablaGenerica VerifGaranteid(String cedu ){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                                "garante,\n" +
                                "ci_garante,\n" +
                                "ide_empleado_garante\n" +
                                "FROM\n" +
                                "srh_anticipo\n" +
                                "where ci_garante like '"+cedu+"' and (ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')OR\n" +
                                "ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'AUTORIZADO')OR\n" +
                                "ide_estado_anticipo  = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'PAGANDO'))");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
    
 public TablaGenerica Garantemple(String ci){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT\n" +
                                "e.cod_empleado, e.cedula_pass,\n" +
                                "e.nombres,e.fecha_ingreso,\n" +
                                "e.fecha_nombramiento,e.id_distributivo,\n" +
                                "e.cod_tipo,i.tipo\n" +
                                "FROM\n" +
                                "\"public\".srh_empleado e,\n" +
                                "\"public\".srh_tipo_empleado i\n" +
                                "WHERE\n" +
                                "e.estado = 1 AND\n" +
                                "e.cod_tipo = i.cod_tipo AND\n" +
                                "e.cod_tipo in (4,10)\n" +
                                "and e.cedula_pass like '"+ci+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
 
    
     public TablaGenerica Datos(String usuario){
        conectarSQL();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectarSQL();
        tab_funcionario.setConexion(conexion);
        tab_funcionario.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where NICK_USUA like '"+usuario+"'");
        tab_funcionario.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_funcionario;
 }
    
    private void conectar() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
}
    
     private void conectarSQL() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
            
        }
    }
    
}
