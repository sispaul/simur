/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class Programas {

    private Conexion conPostgres;
    private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /*
     * GENERAL
     */
    //borrar tabla conc_cedula_presupuestaria_fechas
    public Programas() {
    }

    public void actualizacionPrograma() {
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion)\n"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion\n"
                + "from conc_clasificador,pre_funcion_programa\n"
                + "order by ide_funcion,pre_codigo";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    /*
     * INGRESOS CONSOLIDADOS
     */
    public void eiminarIngreso() {
        String strSqlr = "delete from conc_cedula_presupuestaria_fechas";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void insertaIngresos(Integer ano, Integer tipo, String fecha) {
        // Forma el sql para el ingreso
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo," + ano + ",nivel,'" + fecha + "' from conc_clasificador"
                + " where tipo =" + tipo + " order by pre_codigo ";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();

    }

    public void setCuentaContable(String cuenta, Integer movimiento, Integer anio, Integer periodo, Integer distributivo, Integer columna) {
        // Forma el sql para el ingreso
        String strSqlr = "insert into cont_detalle_movimiento (ide_cuenta,ide_movimiento,mov_debe,mov_haber,mov_devengado,mov_descripcion,ide_tipo_movimiento,doc_deposito)\n"
                + "select(\n"
                + "select ide_cuenta\n"
                + "from conc_catalogo_cuentas\n"
                + "where cue_codigo like '" + cuenta + "%' and cedula = e.cedula_pass\n"
                + "order by ide_cuenta desc limit 1) as cuenta, \n"
                + "" + movimiento + " as movimiento,\n"
                + "" + 0 + " as debe,\n"
                + "r.valor,\n"
                + "" + 0 + " as devengado,\n"
                + "'S/D' as descripcion,\n"
                + "'F' as tipo_movimiento,\n"
                + "'S/D' as doc_deposito\n"
                + "from srh_roles r\n"
                + "inner join srh_empleado e on r.ide_empleado=e.cod_empleado\n"
                + "where r.ano=" + anio + " and r.ide_periodo=" + periodo + " and\n"
                + "r.id_distributivo_roles=" + distributivo + " and r.ide_columnas=" + columna + " and\n"
                + "r.valor >0";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();

    }

    public void actualizarIngresos(Integer combo) {
        // Forma el sql para actualizacion
        String strSqlr = "update conc_cedula_presupuestaria_fechas \n"
                + "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo=" + combo + "";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarComprobante(Integer combo) {
        // Forma el sql para actualizacion
        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set numero_cuenta=link.numero_cuenta,  \n"
                + "tipo_cuenta=link.cod_cuenta,  \n"
                + "codigo_banco=link.codigo_banco,\n"
                + "ban_nombre=link.ban_nombre\n"
                + "from ((select ide_detalle_listado,ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,numero_cuenta, \n"
                + "cod_cuenta, valor,\n"
                + "ban_nombre,codigo_banco from(\n"
                + "(SELECT ide_detalle_listado, \n"
                + "ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,valor\n"
                + "FROM \n"
                + "tes_detalle_comprobante_pago_listado \n"
                + "where ide_listado = " + combo + ") as aa\n"
                + "inner join\n"
                + "(SELECT  \n"
                + "e.cedula_pass, \n"
                + "e.numero_cuenta, \n"
                + "e.cod_cuenta, \n"
                + "b.codigo_banco,\n"
                + "b.ban_nombre\n"
                + "FROM \n"
                + "srh_empleado e , \n"
                + "ocebanco b  \n"
                + "where e.cod_banco = b.ban_codigo) as bb\n"
                + "on aa.cedula_pass_beneficiario = bb.cedula_pass))\n"
                + "union\n"
                + "(select ide_detalle_listado,ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario, \n"
                + "numero_cuenta, \n"
                + "cod_cuenta, valor,ban_nombre ,\n"
                + "codigo_banco from(\n"
                + "(SELECT ide_detalle_listado, \n"
                + "ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,valor\n"
                + "FROM \n"
                + "tes_detalle_comprobante_pago_listado \n"
                + "where ide_listado = " + combo + ") as aa\n"
                + "inner join\n"
                + "(SELECT \n"
                + "p.ide_proveedor, \n"
                + "p.ruc, \n"
                + "p.titular, \n"
                + "p.ban_codigo, \n"
                + "p.numero_cuenta,\n"
                + "(case when p.tipo_cuenta = 'C' then 1  when p.tipo_cuenta = 'A' then 2 end) \n"
                + "as cod_cuenta, \n"
                + "o.codigo_banco, \n"
                + "o.ban_nombre \n"
                + "FROM \n"
                + "tes_proveedores p , \n"
                + "ocebanco o \n"
                + "where p.ban_codigo = o.ban_codigo) as cc\n"
                + "on aa.cedula_pass_beneficiario= cc.ruc) \n"
                + "where cedula_pass_beneficiario!='0')) as link\n"
                + "where tes_detalle_comprobante_pago_listado.ide_detalle_listado =link.ide_detalle_listado  and \n"
                + "tes_detalle_comprobante_pago_listado.ide_listado =link.ide_listado and tes_detalle_comprobante_pago_listado.item =link.item and \n"
                + "tes_detalle_comprobante_pago_listado.cedula_pass_beneficiario=link.cedula_pass_beneficiario and \n"
                + "tes_detalle_comprobante_pago_listado.comprobante =link.comprobante and tes_detalle_comprobante_pago_listado.ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'ENVIADO')";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosIngresos(String ini, String fin, Integer anio, Integer lice) {
        // Forma1 el sql para actualizacion
        String strSqlr = "update conc_cedula_presupuestaria_fechas set \n"
                + "reforma1 = reforma \n"
                + "from ( select ide_clasificador, ((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n"
                + "from pre_anual a,( select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual  from pre_reforma_mes where fecha_reforma between '" + ini + "'"
                + " and '" + fin + "' group by ide_pre_anual) b where a.ide_pre_anual=b.ide_pre_anual and ano=" + anio + " and not ide_clasificador is null group by ide_clasificador ) \n"
                + " a where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas set val_inicial = inicial from \n"
                + " ( select sum (val_presupuestado_i) as inicial, ide_clasificador from pre_anual where  ano=" + anio + " and not ide_clasificador is null group by ide_clasificador ) a \n"
                + " where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas  \n"
                + " set cobradoc1= comprometido, devengado1=devengado from ( select b.ide_clasificador,ano,tipo, \n"
                + " sum( (case when cobradoc is null then 0 else cobradoc end ) )as comprometido, \n"
                + " sum((case when devengado is null then 0 else devengado end) ) as devengado from pre_anual a,\n"
                + "  conc_clasificador b,pre_mensual c where a.ide_clasificador=b.ide_clasificador and a.ide_pre_anual = c.ide_pre_anual and ano=" + anio + ""
                + "  and fecha_ejecucion between '" + ini + "' and '" + fin + "' and tipo= " + lice + " group by b.ide_clasificador,ano,tipo order by tipo ) a \n"
                + "  where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatos2() {
        String strSqlr = "update conc_cedula_presupuestaria_fechas set reforma1 = reforma \n"
                + "from ( select sum(reforma1) as reforma,con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosg3() {
        String strSqlr = "update conc_cedula_presupuestaria_fechas set val_inicial = reforma from ( select sum(val_inicial) as reforma,con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";

        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosg4(Integer ani, Integer tip) {
        String strSqlr = "update conc_cedula_presupuestaria_fechas  set cobradoc1= conbrado, devengado1=devengado \n"
                + "from ( select sum((case when cobradoc1 is null then 0 else cobradoc1 end)) as conbrado, \n"
                + "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado, con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas  where ano_curso=" + ani + " and tipo= " + tip + " group by  con_ide_clasificador ) a \n"
                + "where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }
    /*
     * GASTOS PROGRAMAS
     */

    public void insertarGastos(Integer an, String fec, Integer ti) {
        // Forma el sql para insertar
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,\n"
                + "ide_funcion,des_funcion,cod_funcion,fechaced)\n"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo," + an + ",nivel,ide_funcion,des_funcion,cod_funcion,'" + fec + "'\n"
                + "from conc_clasificador,pre_funcion_programa\n"
                + "where tipo = " + ti + ""
                + "order by ide_funcion,pre_codigo";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void insertRegistro(Integer lista, Integer item, Integer detalle, String comprobar) {
        // Forma el sql para insertar
        String strSqlr = "insert into tes_registro_pagos_listado( ide_listado,item,comprobante,cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,valor,numero_cuenta,ban_nombre,tipo_cuenta,comentario,codigo_banco,\n"
                + "num_documento,ide_estado_listado,ide_detalle_listado,fecha_accion,num_transferencia)\n"
                + "SELECT ide_listado ,item ,comprobante ,cedula_pass_beneficiario ,nombre_beneficiario ,\n"
                + "valor ,numero_cuenta ,ban_nombre ,tipo_cuenta ,comentario ,codigo_banco ,num_documento ,\n"
                + "ide_estado_listado,ide_detalle_listado,'" + utilitario.getFechaActual() + "',num_transferencia\n"
                + "FROM tes_detalle_comprobante_pago_listado\n"
                + "where ide_listado=" + lista + " and item =" + item + " and comprobante like '" + comprobar + "' and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos(Integer cen) {
        // Forma el sql para actualizar
        String strSqlr = "update conc_cedula_presupuestaria_fechas \n"
                + "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo= " + cen;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos1(String in, String fi, Integer an, Integer lic) {
        // Forma el sql para actualizar 1
        String strSqlr = "update conc_cedula_presupuestaria_fechas\n"
                + "set reforma1 = reforma\n"
                + "from (select a.ide_clasificador,sum(reforma) as reforma,ide_funcion\n"
                + "from prec_programas a, (select ide_programa,\n"
                + "((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n"
                + "from pre_anual a,(select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual \n"
                + "from pre_reforma_mes where fecha_reforma between '" + in + "' and '" + fi + "'\n"
                + "group by ide_pre_anual) b\n"
                + "where a.ide_pre_anual=b.ide_pre_anual\n"
                + "and ano=" + an + " and not ide_programa is null\n"
                + "group by ide_programa ) b\n"
                + "where a.ide_programa = b.ide_programa\n"
                + "group by a.ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=" + lic + " and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos2(Integer ani, Integer licn) {
        // Forma el sql para actualizar 2
        String str_sql4 = "update conc_cedula_presupuestaria_fechas\n"
                + "set val_inicial = inicial\n"
                + "from (select ide_clasificador,sum(inicial) as inicial,ide_funcion\n"
                + "from prec_programas a,(select sum (val_presupuestado_i) as inicial, ide_programa\n"
                + "from pre_anual\n"
                + "where  ano=" + ani + " and not  ide_programa is null\n"
                + "group by ide_programa) b\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "group by ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=" + licn + " and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql4);
        desPostgresql();
    }

    public void actualizarTablaGastos3(Integer anio, String ini, String fin, Integer licen) {
        // Forma el sql para actualizar 3
        String str_sql5 = "update conc_cedula_presupuestaria_fechas \n"
                + "set compromiso1= comprometido,\n"
                + "devengado1=devengado,\n"
                + "pagado1=pagado\n"
                + "from (select ide_clasificador,sum(comprometido) as comprometido,sum(devengado) as devengado,sum(pagado) as pagado,ide_funcion\n"
                + "from prec_programas a, (select b.ide_programa,ano,\n"
                + "sum( (case when comprometido is null then 0 else comprometido end ) )as comprometido,\n"
                + "sum((case when devengado is null then 0 else devengado end) ) as devengado,\n"
                + "sum((case when pagado is null then 0 else pagado end) ) as pagado\n"
                + "from pre_anual a, prec_programas b,pre_mensual c\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "and a.ide_pre_anual = c.ide_pre_anual and ano=" + anio + "\n"
                + "and fecha_ejecucion between '" + ini + "' and '" + fin + "'\n"
                + "group by b.ide_programa,ano) b\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "group by ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion \n"
                + "and conc_cedula_presupuestaria_fechas.tipo=" + licen + " and a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql5);
        desPostgresql();
    }

    public void actualizarTablaGastos4(Integer licn) {
        // Forma el sql para actualizar 4
        String str_sql6 = "update conc_cedula_presupuestaria_fechas\n"
                + "set reforma1 = reforma\n"
                + "from (select sum(reforma1) as reforma,con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas\n"
                + "group by con_ide_clasificador,ide_funcion,tipo having tipo=" + licn + ") a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and  a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql6);
        desPostgresql();
    }

    public void actualizarTablaGastos5(Integer lice, Integer ano) {
        // Forma el sql para actualizar 5
        String str_sql7 = "update conc_cedula_presupuestaria_fechas\n"
                + "set val_inicial = reforma\n"
                + "from (select sum(val_inicial) as reforma,con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas\n"
                + "group by con_ide_clasificador,ide_funcion,tipo having tipo=" + lice + ") a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;\n"
                + "update conc_cedula_presupuestaria_fechas \n"
                + "set compromiso1= comprometido,\n"
                + "devengado1=devengado,\n"
                + "pagado1=pagado\n"
                + "from (select sum((case when compromiso1 is null then 0 else compromiso1 end)) as comprometido,\n"
                + "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado,\n"
                + "sum((case when pagado1 is null then 0 else pagado1 end)) as pagado,\n"
                + "con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas \n"
                + "where ano_curso= " + ano + " and tipo= " + lice + "\n"
                + "group by  con_ide_clasificador,ide_funcion,tipo ) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql7);
        desPostgresql();
    }

    public void actuProveedor(Integer provee, String titular, Integer banco, String cuenta, String tipcuen, String activida, String fono, Integer tiprov, String fono1, String ruc, String cuban, String usu) {
        String proveedor = "UPDATE tes_proveedores set titular = '" + titular + "' ,ban_codigo =" + banco + ",numero_cuenta ='" + cuenta + "',\n"
                + "tipo_cuenta = '" + tipcuen + "',actividad ='" + activida + "',telefono1 ='" + fono + "',ide_tipo_proveedor =" + tiprov + " ,\n"
                + "telefono2 ='" + fono1 + "' ,ruc ='" + ruc + "',codigo_banco ='" + cuban + "',\n"
                + "usuario_actua ='" + usu + "',ip_actua = '" + utilitario.getIp() + "',fecha_actua ='" + utilitario.getFechaActual() + "' WHERE ide_proveedor =" + provee;
        conPostgresql();
        conPostgres.ejecutarSql(proveedor);
        desPostgresql();
    }

    public void actuEmpleado(Integer banco, String numero, String usu, Integer ci, Integer tipo) {
        String empleado = "Update srh_empleado set cod_banco=" + banco + ",numero_cuenta='" + numero + "',ip_responsable ='" + utilitario.getIp() + "',"
                + "nom_responsable='" + usu + "',cod_cuenta=" + tipo + ",fecha_responsable='" + utilitario.getFechaActual() + "'WHERE cod_empleado = " + ci;
        conPostgresql();
        conPostgres.ejecutarSql(empleado);
        desPostgresql();
    }

    public void actuListado(String ci, String respon, String usu, Integer ide) {
        String strSqlr = "UPDATE  tes_comprobante_pago_listado \n"
                + "set fecha_pagado = '" + utilitario.getFechaActual() + "',ci_paga='" + ci + "' ,responsable_paga='" + respon + "',"
                + "usuario_actua_paga='" + usu + "',ip_actua_paga='" + utilitario.getIp() + "'\n"
                + "WHERE ide_listado =" + ide;

        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuaComprobante(String cuenta, String nombre, String tipo, String usu, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set usuario_ingre_envia='" + usu + "',\n"
                + "ip_ingre_envia='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'PAGADO')\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void devolverComprobante(String usua, String comprobante, Integer lista, Integer detalle, Integer item) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set usuario_actua_devolucion ='" + usua + "',\n"
                + "ip_actua_devolucion='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO') \n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado = " + lista + " and ide_detalle_listado =" + detalle + " and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void devolverLista(String cedula, Integer lista, Integer item) {

        String strSqlr = "UPDATE tes_comprobante_pago_listado \n"
                + "set devolucion = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO')\n"
                + "WHERE ci_envia like '" + cedula + "' and ide_listado = " + lista + "and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void ActuaListaComp(String comprobante) {

        String strSqlr = "UPDATE tes_comprobante_pago\n"
                + "set estado_comprobante = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado LIKE 'PENDIENTE')\n"
                + "where comprobante = '" + comprobante + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void regreComprobante(String cuenta, String usu, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta='" + cuenta + "',\n"
                + "usuario_ingre_envia='" + usu + "',\n"
                + "ip_ingre_envia='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'ENVIADO')\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void rechazoComprobante(String cuenta, String comprobante, Integer lista, String comentario) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO'),comentario = '" + comentario + "'\n"
                + "WHERE comprobante like'" + comprobante + "'  and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void regresoRechazo(String cuenta, String comprobante, Integer lista) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'PAGADO'),comentario = null\n"
                + "WHERE comprobante like'" + comprobante + "'  and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void numTransferencia(String cuenta, String comprobante, Integer lista, String trans) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set num_transferencia='" + trans + "'\n"
                + "WHERE comprobante like'" + comprobante + "' and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void estadoLisCom(String cuenta) {

        String strSqlr = "UPDATE tes_comprobante_pago\n"
                + "SET estado_comprobante = (SELECT ide_estado_listado FROM tes_estado_listado where estado ='PAGADO')\n"
                + "WHERE comprobante = '" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void numTransComprobante(String numero, String fecha, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set num_documento='" + numero + "',\n"
                + "fecha_transferencia='" + fecha + "'\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarComprobante(Integer codigo, String tipo, Integer detalle, Integer listado, String usu, String registro, Integer estado) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ban_codigo=" + codigo + ",\n"
                + "tipo_cuenta='" + tipo + "',usuario_actua_pagado='" + usu + "',ide_estado_detalle_listado =" + estado + " ,ban_nombre = (SELECT o.ban_nombre FROM ocebanco o WHERE o.ban_codigo =" + codigo + "),num_transferencia = '" + registro + "',ip_actua_pagado='" + utilitario.getIp() + "'\n"
                + "WHERE ide_detalle_listado=" + detalle + " and ide_listado =" + listado;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLis(String ci, String respon, Integer ide) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set fecha_devolucion='" + utilitario.getFechaActual() + "',ci_devolucion='" + ci + "',usuario_actua_devolucion='" + respon + "',"
                + "ip_actua_devolucion ='" + utilitario.getIp() + "'\n"
                + "where ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLisDevol(Integer ide) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set devolucion='1' where ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLisDevolver(Integer ide, Integer item) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set estado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'CERRADO') where item=" + item + " and ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuCuentasBanco(String numero, String nombre, String tipo, String codigo, Integer ide, Integer lis, String compro, String cedula) {
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta ='" + numero + "',\n"
                + "ban_nombre ='" + nombre + "',\n"
                + "tipo_cuenta ='" + tipo + "',\n"
                + "codigo_banco ='" + codigo + "'\n"
                + "WHERE\n"
                + "ide_detalle_listado =" + ide + " and\n"
                + "ide_listado =" + lis + " and\n"
                + "comprobante like '" + compro + "' and\n"
                + "cedula_pass_beneficiario like '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuComponente(String cuenta, Integer codigo, String tipo, Integer detalle, Integer listado, String usu, String registro, Integer estado) {
        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta ='" + cuenta + "',ban_codigo=" + codigo + ",ban_nombre=(SELECT ban_nombre FROM ocebanco WHERE ban_codigo =" + codigo + "),"
                + "tipo_cuenta='" + tipo + "',usuario_actua_devolucion='" + usu + "',num_transferencia='" + registro + "',ip_actua_devolucion='" + utilitario.getIp() + "',ide_estado_detalle_listado=" + estado + "\n"
                + "where ide_detalle_listado = " + detalle + " and ide_listado = " + listado;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDetalleC(String tipo, String numero, String banco, Integer detalle, Integer lista, Integer item, String comprobante, String cedula) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set tipo_cuenta ='" + tipo + "',\n"
                + "numero_cuenta='" + numero + "',\n"
                + "codigo_banco='" + banco + "'\n"
                + "where \n"
                + "ide_detalle_listado=" + detalle + " and\n"
                + "ide_listado=" + lista + " and\n"
                + "item=" + item + " and\n"
                + "comprobante= '" + comprobante + "'and\n"
                + "cedula_pass_beneficiario='" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrden(String tipo, Integer numero, String usu, String comentario) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set tes_estado='Anulada'\n"
                + ",tes_nota='ANULADA'\n"
                + ",tes_login_anu ='" + usu + "'\n"
                + ",tes_comentario_anula ='" + comentario + "'\n"
                + ",tes_fecha_anu ='" + utilitario.getFechaActual() + "'\n"
                + "where tes_ide_orden_pago =" + numero + " and tes_numero_orden ='" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setOrdenPago(String tipo, Integer numero, String etiqueta, String campo) {
        String str_sql = "update tes_orden_pago\n"
                + "set " + etiqueta + "=" + campo + " \n"
                + "where tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql);
        desPostgresql();
    }

    public void setOrdenPagos(String tipo, Integer numero, String etiqueta, Double campo) {
        String str_sql = "update tes_orden_pago\n"
                + "set " + etiqueta + "=" + campo + " \n"
                + "where tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql);
        desPostgresql();
    }

    public void actOrdenTotalPro(String tipo, Integer numero, String asunto, Integer idp, String proveedor, Double valor, String letras, String concepto, String acuerdo, String nota, String comprobante, String fecha, String estado, String usu, String fechaen, String tipoe) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_id_proveedor=" + idp + ",\n"
                + "tes_proveedor='" + proveedor + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_comprobante_egreso='" + comprobante + "',\n"
                + "tes_fecha_comprobante='" + fecha + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_envio='" + usu + "',\n"
                + "tipo_solicitantep='" + tipoe + "',\n"
                + "tes_fecha_envio='" + fechaen + "'\n"
                + //                "tes_fecha_actu='"+utilitario.getFechaActual()+"'\n" +
                "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalPro1(String tipo, Integer numero, String asunto, Integer idp, String proveedor, Double valor, String letras, String concepto, String acuerdo, String nota, String estado, String usu) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_id_proveedor=" + idp + ",\n"
                + "tes_proveedor='" + proveedor + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_mod='" + usu + "',\n"
                + "tes_fecha_mod='" + utilitario.getFechaActual() + "'\n"
                + "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalEmp(String tipo, Integer numero, String asunto, Integer ide, String empleado, Double valor, String letras, String concepto, String acuerdo, String nota, String comprobante, String fecha, String estado, String usu, String fechaen, String tipoa) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_cod_empleado=" + ide + ",\n"
                + "tes_empleado='" + empleado + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_comprobante_egreso='" + comprobante + "',\n"
                + "tes_fecha_comprobante='" + fecha + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_envio='" + usu + "',\n"
                + "tipo_solicitantep='" + tipoa + "',\n"
                + "tes_fecha_envio='" + fechaen + "'\n"
                + //                "tes_fecha_actu='"+utilitario.getFechaActual()+"'\n" +
                "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalEmp1(String tipo, Integer numero, String asunto, Integer ide, String empleado, Double valor, String letras, String concepto, String acuerdo, String nota, String estado, String usu) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_cod_empleado=" + ide + ",\n"
                + "tes_empleado='" + empleado + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_mod='" + usu + "',\n"
                + "tes_fecha_mod='" + utilitario.getFechaActual() + "'\n"
                + "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActuDetallePag(Integer codigo, String cuenta, String banco, String nombre, Integer detalle, String comprobante, Integer item, String cuentan) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set ban_codigo=" + codigo + ",\n"
                + "tipo_cuenta ='" + cuenta + "',\n"
                + "codigo_banco='" + banco + "',\n"
                + "numero_cuenta='" + cuentan + "',\n"
                + "ban_nombre='" + nombre + "'\n"
                + "where ide_listado=" + detalle + " and comprobante ='" + comprobante + "'  and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public TablaGenerica periodo(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_tipo_cuenta,tipo_cuenta,cuenta FROM ocebanco_tipo_cuenta WHERE ide_tipo_cuenta =" + periodo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getEmpleado(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select cod_empleado,nombres,cedula_pass from srh_empleado where cod_empleado=" + periodo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getProveedor(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_proveedor,titular FROM tes_proveedores where ide_proveedor=" + periodo + " order by titular");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica banco(Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ban_codigo,ban_nombre,num_banco FROM ocebanco WHERE ban_codigo =" + banco);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getTranferencia(Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_detalle_listado,ide_listado,num_documento FROM tes_detalle_comprobante_pago_listado where ide_detalle_listado =" + iden);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica item(Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT \n"
                + " ide_listado, \n"
                + " item, \n"
                + " fecha_listado, \n"
                + " ci_envia, \n"
                + " ci_paga, \n"
                + " responsable_paga, \n"
                + " estado \n"
                + " FROM \n"
                + " tes_comprobante_pago_listado \n"
                + " where ci_paga is null and responsable_paga is null and ide_listado =" + banco);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica empleado() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_empleado,estado\n"
                + "FROM srh_empleado WHERE cod_cargo = 101 and estado = 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica Pagos_lista(Integer item, Integer lista) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_detalle_listado,\n"
                + "ide_listado,\n"
                + "comprobante,\n"
                + "cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,\n"
                + "valor,\n"
                + "numero_cuenta,\n"
                + "codigo_banco,\n"
                + "ban_nombre,\n"
                + "tipo_cuenta,\n"
                + "null as proceso\n"
                + "FROM\n"
                + "tes_detalle_comprobante_pago_listado AS d\n"
                + "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'ENVIADO') and item =" + item + " and ide_listado =" + lista);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica empleado1(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "e.cod_empleado,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.numero_cuenta,\n"
                + "e.cod_cuenta,\n"
                + "b.codigo_banco\n"
                + "FROM\n"
                + "srh_empleado e ,\n"
                + "ocebanco b \n"
                + "where e.cod_banco = b.ban_codigo and e.cedula_pass like '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica empleadoCod(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,fecha_ingreso,fecha_nombramiento,relacion_laboral,id_distributivo\n"
                + "FROM srh_empleado where estado = 1 and cod_empleado = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getorden_valida(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT tes_ide_orden_pago,tes_numero_orden,tes_estado,tes_id_proveedor,tes_proveedor,"
                + "tes_cod_empleado,tes_empleado,tes_fecha_envio,tes_fecha_comprobante,tes_comprobante_egreso\n"
                + "FROM tes_orden_pago where tes_numero_orden ='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica proveedor(String ruc) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "p.ide_proveedor,\n"
                + "p.ruc,\n"
                + "p.titular,\n"
                + "p.ban_codigo,\n"
                + "p.numero_cuenta,\n"
                + "p.tipo_cuenta,\n"
                + "p.ide_tipo_proveedor,\n"
                + "p.codigo_banco,\n"
                + "o.ban_nombre\n"
                + "FROM\n"
                + "tes_proveedores p ,\n"
                + "ocebanco o\n"
                + "where p.ban_codigo = o.ban_codigo and ruc like '" + ruc + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica proveedor1(Integer ruc) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "p.ide_proveedor,\n"
                + "p.ruc,\n"
                + "p.titular,\n"
                + "p.ban_codigo,\n"
                + "p.numero_cuenta,\n"
                + "p.tipo_cuenta,\n"
                + "p.ide_tipo_proveedor,\n"
                + "p.codigo_banco,\n"
                + "o.ban_nombre\n"
                + "FROM\n"
                + "tes_proveedores p ,\n"
                + "ocebanco o\n"
                + "where p.ban_codigo = o.ban_codigo and p.ide_proveedor =" + ruc);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica busEstado(String estado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_detalle_listado,ide_listado,item,ide_estado_listado\n"
                + "FROM tes_detalle_comprobante_pago_listado\n"
                + "where comprobante like '" + estado + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica Beneficiarios(Integer item) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_detalle_listado,\n"
                + "ide_listado,\n"
                + "item,\n"
                + "comprobante,\n"
                + "cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,\n"
                + "valor,\n"
                + "numero_cuenta,\n"
                + "tipo_cuenta,\n"
                + "codigo_banco\n"
                + "FROM\n"
                + "tes_detalle_comprobante_pago_listado\n"
                + "where ide_listado =" + item);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getDatos(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select  \n"
                + "(case when a.identificacion is null then b.identificacion when a.identificacion is not null then a.identificacion end) as identificacion, \n"
                + "(case when a.nombres is null then b.nombres when a.nombres is not null then a.nombres end) as nombres,\n"
                + "(case when a.ban_codigo is null then b.ban_codigo when a.ban_codigo is not null then a.ban_codigo end) as ban_codigo, \n"
                + "b.codigo_banco,\n"
                + "(case when a.numero_cuenta is null then b.numero_cuenta when a.numero_cuenta is not null then a.numero_cuenta end) as numero_cuenta, \n"
                + "(case when a.cod_cuenta is null then b.cod_cuenta when a.cod_cuenta is not null then a.cod_cuenta end) as cod_cuenta \n"
                + "from  \n"
                + "(SELECT \n"
                + "cast(cedula_pass as varchar) as identificacion, \n"
                + "nombres, \n"
                + "cast(cod_banco as varchar)as ban_codigo, \n"
                + "cast(numero_cuenta as varchar) as numero_cuenta, \n"
                + "cast(cod_cuenta as varchar) as cod_cuenta\n"
                + "from srh_empleado \n"
                + "where cedula_pass = '" + codigo + "' and \n"
                + "estado = 1)as a \n"
                + "full outer join \n"
                + "(SELECT \n"
                + "cast(p.ruc as varchar) AS identificacion, \n"
                + "p.titular AS nombres,\n"
                + "cast(p.ban_codigo as varchar) AS ban_codigo,\n"
                + "p.codigo_banco, \n"
                + "cast(p.numero_cuenta as varchar) AS numero_cuenta, \n"
                + "(case when p.tipo_cuenta = 'C' then '1'  when p.tipo_cuenta = 'A' then '2' end) AS cod_cuenta \n"
                + "FROM tes_proveedores AS p \n"
                + "where p.ruc = '" + codigo + "'\n"
                + ") as b  \n"
                + "on a.identificacion = b.identificacion");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getBanco(String codigo, Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ban_codigo,ban_nombre,codigo_banco\n"
                + "from ocebanco\n"
                + "where ban_codigo = " + banco + " or codigo_banco = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getAsientoContable(Integer cuenta, Integer movimiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select\n"
                + "ide_detalle_mov,\n"
                + "ide_cuenta,\n"
                + "ide_movimiento,\n"
                + "mov_debe,\n"
                + "mov_haber,\n"
                + "mov_devengado,\n"
                + "mov_descripcion,\n"
                + "ide_tipo_movimiento,\n"
                + "doc_deposito\n"
                + "from cont_detalle_movimiento\n"
                + "where ide_cuenta =" + cuenta + " and ide_movimiento =" + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public String listaMax() {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,\n"
                + "(case when max(num_documento) is null then 'LIST-2014-00000' when max(num_documento)is not null then max(num_documento) end) AS maximo\n"
                + "from tes_detalle_comprobante_pago_listado");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    public String maxComprobantes() {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,(case when max(tes_numero_orden) is null then '0' when max(tes_numero_orden)is not null then max(tes_numero_orden) end) AS maximo\n"
                + "from tes_orden_pago where tes_estado_doc = '1'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
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
