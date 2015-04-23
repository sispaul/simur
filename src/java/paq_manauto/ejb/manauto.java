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
public class manauto {

    private Conexion con_sql,//Conexion a la base de sigag
            con_postgres;//Cnexion a la base de postgres 2014
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    //acciones para marcas
    public TablaGenerica get_DuplicaMarca(String nombre) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mvmarca_id,mvmarca_descripcion,mvmarca_estado\n"
                + "FROM mvmarca_vehiculo\n"
                + "where mvmarca_descripcion like '" + nombre + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_marca(String nombre, String login) {
        String parametro = "insert into mvmarca_vehiculo (mvmarca_descripcion,mvmarca_estado,mvmarca_fechaing,mvmarca_loginin)\n"
                + "values ('" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteMarcas(Integer anti) {
        String au_sql = "delete from mvmarca_vehiculo where mvmarca_id =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //acciones para tipo
    public TablaGenerica get_DuplicaTipo(String nombre, Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mvtipo_id,mvtipo_descripcion FROM mvtipo_vehiculo where mvmarca_id = " + codigo + " and mvtipo_descripcion like '" + nombre + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void setTipo(String nombre, String login, Integer marca) {
        String parametro = "insert into mvtipo_vehiculo (mvmarca_id,mvtipo_descripcion,mvtipo_estado,mvtipo_fechaing,mvtipo_loginin)\n"
                + "values (" + marca + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void getParametacces(String codigo, String nombre, Integer cantidad, String estado) {
        String parametro = "insert into mvdetalle_vehiculo(mdv_detalle,mdv_cantidad,mdv_estado,mve_secuencial,mve_estado)\n"
                + "values ('" + nombre + "'," + cantidad + ",'" + estado + "'," + codigo + ",'1')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setDependencias(String nombre) {
        String parametro = "insert into mvtipo_dependencias(dependencia_descripcion)\n"
                + "values ('" + nombre + "')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteTipos(Integer anti) {
        String au_sql = "delete from mvtipo_vehiculo where mvtipo_id =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setDeleteAbast(Integer anti) {
        String au_sql = "delete from mvabactecimiento_combustible where abastecimiento_id = " + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //acciones para modelo
    public TablaGenerica get_DuplicaModelo(String nombre, Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n"
                + "mvmodelo_id,\n"
                + "mvmodelo_descripcion\n"
                + "FROM\n"
                + "mvmodelo_vehiculo\n"
                + "where mvmodelo_descripcion ='" + nombre + "' and  MVTIPO_ID=" + codigo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void setModelo(String nombre, String login, Integer tipo) {
        String parametro = "insert into mvmodelo_vehiculo(mvtipo_id,mvmodelo_descripcion,mvmodelo_estado,mvmodelo_fechaing,mvmodelo_loginin)\n"
                + "values (" + tipo + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteModelos(Integer anti) {
        String au_sql = "delete from mvmodelo_vehiculo where mvmodelo_id =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteDependencias(Integer anti) {
        String au_sql = "delete from mvtipo_dependencias where dependencia_codigo =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteaccesorios(Integer anti) {
        String au_sql = "update mvdetalle_vehiculo set mve_estado = '0' where mdv_codigo = " + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setAccesorios(Integer anti, String cond, String nombre) {
        String au_sql = "update mv_vehiculo\n"
                + "set mve_cod_conductor='" + cond + "',\n"
                + "mve_conductor='" + nombre + "'\n"
                + "where mve_secuencial =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public TablaGenerica get_DuplicaVersion(String nombre, Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n"
                + "mvversion_id,\n"
                + "mvversion_descripcion\n"
                + "FROM mvversion_vehiculo\n"
                + "where mvmodelo_id =" + codigo + " and mvversion_descripcion='" + nombre + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void setVersion(String nombre, String login, Integer modelo) {
        String parametro = "insert into mvversion_vehiculo(mvmodelo_id,mvversion_descripcion,mvversion_estado,mvversion_fechaing,mvversion_loginin)\n"
                + "values (" + modelo + ",'" + nombre + "',1,'" + utilitario.getFechaActual() + "','" + login + "')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void deleteversion(Integer anti) {
        String au_sql = "delete from mvversion_vehiculo where mvversion_id =" + anti;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //DATOS DE CHOFER
    public TablaGenerica getChofer(String cedula) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT cod_empleado, cedula_pass,nombres, 1 as activo\n"
                + "FROM srh_empleado\n"
                + "where cod_empleado ='" + cedula + "' and estado = 1\n"
                + "order by nombres");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    /////clases para abastecimiento automotriz
    public TablaGenerica getVehiculo(Integer placa) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT v.mve_secuencial, \n"
                + "v.mve_placa, \n"
                + "c.tipo_combustible_id, \n"
                + "v.mve_conductor, \n"
                + "v.mve_cod_conductor, \n"
                + "(case when to_char(v.mve_kilometros_actual, '999999999.99') is null then v.mve_horometro when to_char(v.mve_kilometros_actual, '999999999.99') is not null then to_char(v.mve_kilometros_actual, '999999999.99') end) as rendimiento,\n"
                + "v.mve_capacidad_tanque, \n"
                + "v.mve_kilometros_actual, \n"
                + "v.mve_departamento, \n"
                + "v.mve_numimr \n"
                + "FROM mv_vehiculo AS v  \n"
                + "INNER JOIN  mvtipo_combustible AS c   \n"
                + "ON v.tipo_combustible_id = c.tipo_combustible_id\n"
                + "where v.mve_secuencial =" + placa);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public String listaMax(Integer placa, String anio, String fecha) {
        con_postgresql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        con_postgresql();
        tab_consulta.setConexion(con_postgres);
        tab_consulta.setSql("select 0 as id,\n"
                + "(case when count(abastecimiento_numero) is null then '0' when count(abastecimiento_numero)is not null then count(abastecimiento_numero) end) AS maximo\n"
                + "from mvabactecimiento_combustible\n"
                + "where mve_secuencial = " + placa + " and abastecimiento_anio = '" + anio + "' and abastecimiento_periodo ='" + fecha + "'");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public TablaGenerica getCombustible(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT tipo_combustible_id,tipo_combustible_descripcion,tipo_valor_galon FROM mvtipo_combustible where tipo_combustible_id=" + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica setCombustible(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT abastecimiento_id, mve_secuencial FROM mvabactecimiento_combustible where abastecimiento_id =" + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica setguardar(String tipo, String anio, String mes, Integer placa) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT abastecimiento_id, mve_secuencial FROM mvabactecimiento_combustible\n"
                + "where abastecimiento_anio='" + anio + "' and mve_secuencial=" + placa + " and abastecimiento_periodo='" + mes + "' and abastecimiento_numero='" + tipo + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_Actuabaste(Integer codigo, String vale, String fecha, String cod_cond, String conductor, Integer km, Double total, String gl, String anio, String periodo, String fechaac, String login, String time) {
        String au_sql = "update mvabactecimiento_combustible set abastecimiento_numero_vale='" + vale + "', \n"
                + "abastecimiento_fecha='" + fecha + "', \n"
                + "abastecimiento_conductor='" + cod_cond + "', \n"
                + "abastecimiento_cod_conductor='" + conductor + "', \n"
                + "abastecimiento_kilometraje=" + km + ", \n"
                + "abastecimiento_galones='" + gl + "', \n"
                + "abastecimiento_total=" + total + ", \n"
                + "abastecimiento_anio='" + anio + "', \n"
                + "abastecimiento_periodo='" + periodo + "', \n"
                + "abastecimiento_fechactu='" + fechaac + "', \n"
                + "abastecimiento_loginactu='" + login + "', \n"
                + "abastecimiento_horabas ='" + time + "'\n"
                + "where abastecimiento_id =" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void set_ActuaKM(Integer codigo, Integer km, String des) {
        String au_sql = "update mv_vehiculo\n"
                + "" + des + " =" + km + "\n" +//set mve_kilometros_actual
                "where mve_secuencial=" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void set_ActuaHR(Integer codigo, String hr, String des) {
        String au_sql = "update mv_vehiculo set\n"
                + "" + des + " ='" + hr + "'\n" +//mve_horometro
                "where mve_secuencial=" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //solictud de mantenimiento
    public String Maxsolicitud() {
        con_postgresql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        con_postgresql();
        tab_consulta.setConexion(con_postgres);
        tab_consulta.setSql("select 0 as id,\n"
                + "(case when max(mca_secuencial) is null then '0' when max(mca_secuencial)is not null then max(mca_secuencial) end) as maximo\n"
                + "from mvcab_mantenimiento");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public String Maxsoli_vehiculo(Integer codigo) {
        con_postgresql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        con_postgresql();
        tab_consulta.setConexion(con_postgres);
        tab_consulta.setSql("select 0 as id,\n"
                + "(case when max(mca_secuencial_sol) is null then '0' when max(mca_secuencial_sol)is not null then max(mca_secuencial_sol) end) as maximo\n"
                + "from mvcab_mantenimiento where mca_periodo='" + utilitario.getMes(utilitario.getFechaActual()) + "' and mca_anio='" + utilitario.getAnio(utilitario.getFechaActual()) + "' and mve_secuencial =" + codigo);
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public TablaGenerica getDetalleSol(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mde_codigo,mca_codigo,mde_cod_articulo,mde_articulo FROM mvdetalle_mantenimiento where mca_codigo=" + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica getDatosSoli(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mca_codigo, mca_secuencial,mve_secuencial FROM mvcab_mantenimiento where mca_estado_registro = 'Solicitud' and mve_secuencial=" + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_anulasolic(Integer codigo, String login, String motivo) {
        String au_sql = "update mvcab_mantenimiento \n"
                + "set mca_loginborrado ='" + login + "', mca_fechaborrado='" + utilitario.getFechaActual() + "', mca_motivo_anulacion='" + motivo + "',mca_estado_registro='Anulada'\n"
                + "where mca_codigo=" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public TablaGenerica getDatosSolicitud(Integer tipo, String secuencia) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mca_codigo,mve_secuencial,mca_responsable,mca_cod_responsable,mca_proveedor,mca_cod_proveedor,\n"
                + "mca_autorizado,mca_cod_autoriza,mca_formapago,mca_detalle,mca_kmactual_hora,mca_observacion,mca_tipo_mantenimiento\n"
                + "FROM mvcab_mantenimiento where mca_codigo = " + tipo + " and mca_secuencial='" + secuencia + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_actusolic(Integer codigo, String secuencia, String eti_1, String eti_2, Integer cod_des, String descrip, String login) {
        String au_sql = "update mvcab_mantenimiento set\n"
                + "" + eti_1 + "=" + cod_des + ",\n"
                + "" + eti_2 + "='" + descrip + "',\n"
                + "mca_loginactuali='" + login + "',\n"
                + "mca_fechactuali='" + utilitario.getFechaActual() + "'\n"
                + "where mca_codigo=" + codigo + " and mca_secuencial='" + secuencia + "'";
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //detalle de mantenimiento
    public TablaGenerica getDetaArticulos(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT m.ide_mat_bodega,m.cod_material,m.des_material,a.costo_actual,e.und_medida\n"
                + "FROM bodc_material m\n"
                + "INNER JOIN bodt_articulos a ON a.ide_mat_bodega = m.ide_mat_bodega \n"
                + "inner join valc_medida e on m.ide_medida = e.ide_medida\n"
                + "where m.ide_mat_bodega =" + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica getDetaArticus(Integer tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mde_codigo,mca_codigo,mde_cod_articulo,mde_detalletrab,mde_cantidad,mde_valor,mde_total\n"
                + "FROM mvdetalle_mantenimiento\n"
                + "where mca_codigo = " + tipo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_updateSolicitud(Integer codigo) {
        String au_sql = "update mvcab_mantenimiento\n"
                + "set mca_estado_registro = 'Terminado'\n"
                + "where mca_codigo =" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //Reportes
    public TablaGenerica getDesResporte(String tipo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n"
                + "v.mve_secuencial,\n"
                + "v.mve_placa,\n"
                + "m.mvmarca_descripcion,\n"
                + "t.mvtipo_descripcion,\n"
                + "o.mvmodelo_descripcion,\n"
                + "v.mve_codigo\n"
                + "FROM mv_vehiculo v\n"
                + "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n"
                + "INNER JOIN mvtipo_vehiculo t ON t.mvmarca_id = m.mvmarca_id AND v.mvtipo_id = t.mvtipo_id\n"
                + "INNER JOIN mvmodelo_vehiculo o ON o.mvtipo_id = t.mvtipo_id AND v.mvmodelo_id = o.mvmodelo_id\n"
                + "WHERE v.mve_placa  = '" + tipo + "'or v.mve_codigo = '" + tipo + "'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica getMes(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo = " + periodo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    //acta entrega recepcio
    public TablaGenerica getpedido(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mav_secuencial,mve_secuencial,mav_departamento\n"
                + "FROM mvasignar_vehiculo\n"
                + "where mav_estado_tramite = 'Pedido' and mve_secuencial = " + periodo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica getVehiculoDa(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql(" SELECT mve_secuencial,\n"
                + "mve_kilometros_actual,\n"
                + "mve_capacidad_tanque,\n"
                + "mve_duracion_llanta,\n"
                + "mve_horometro,\n"
                + "mve_rendimientogl_h\n"
                + "FROM mv_vehiculo\n"
                + "where mve_secuencial= " + periodo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public void set_updatepedido(Integer codigo) {
        String au_sql = "update mvasignar_vehiculo\n"
                + "set mav_estado_tramite = 'Terminado'\n"
                + "where mav_estado_tramite = 'Pedido' and mve_secuencial =" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void set_updateValor(Integer codigo, Integer vehiculo, String vale, String descrip, String valor) {
        String au_sql = "update mvabactecimiento_combustible\n"
                + "set " + descrip + " ='" + valor + "'\n"
                + "where abastecimiento_id =" + codigo + " and mve_secuencial=" + vehiculo + " and abastecimiento_numero_vale='" + vale + "' and abastecimiento_ingreso = 'HT'";
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setUpdateVehi(Integer codigo, String vale, String descrip) {
        String au_sql = "update mv_vehiculo\n"
                + "set " + vale + "='" + descrip + "' \n"
                + "where mve_secuencial =" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void setUpdateVehi1(Integer codigo, String vale, Double descrip) {
        String au_sql = "update mv_vehiculo\n"
                + "set " + vale + "=" + descrip + " \n"
                + "where mve_secuencial =" + codigo;
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    public void set_updateValor1(Integer codigo, Integer vehiculo, String vale, String descrip, Double valor) {
        String au_sql = "update mvabactecimiento_combustible\n"
                + "set " + descrip + " =" + valor + "\n"
                + "where abastecimiento_id=" + codigo + " and mve_secuencial=" + vehiculo + " and abastecimiento_numero_vale='" + vale + "' and abastecimiento_ingreso = 'HT'";
        con_postgresql();
        con_postgres.ejecutarSql(au_sql);
        con_postgres.desconectar();
        con_postgres = null;
    }

    //mantenimiento maquinarias
    public TablaGenerica getHorav(Integer periodo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mve_secuencial,mve_horometro FROM mv_vehiculo where mve_secuencial =" + periodo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica setAbasMes(Integer placa, String anio, String periodo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT abastecimiento_id,\n"
                + "abastecimiento_horasmes\n"
                + "FROM mvabactecimiento_combustible\n"
                + "where mve_secuencial =" + placa + " and abastecimiento_anio='" + anio + "' and abastecimiento_periodo='" + periodo + "'\n"
                + "order by abastecimiento_numero desc limit 1");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica getActu(Integer codigo, Integer vehiculo, String vale) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT abastecimiento_id,\n"
                + "mve_secuencial,\n"
                + "abastecimiento_numero_vale,\n"
                + "abastecimiento_fecha,\n"
                + "abastecimiento_galones,\n"
                + "abastecimiento_total,\n"
                + "abastecimiento_valorhora\n"
                + "FROM mvabactecimiento_combustible\n"
                + "where abastecimiento_id =" + codigo + " and mve_secuencial=" + vehiculo + " and abastecimiento_numero_vale='" + vale + "' and abastecimiento_ingreso = 'HT'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    public TablaGenerica setRegistroAbas(Integer registro) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("select * from (\n"
                + "SELECT\n"
                + "abastecimiento_id,\n"
                + "abastecimiento_kilometraje\n"
                + "FROM mvabactecimiento_combustible\n"
                + "where mve_secuencial = " + registro + "\n"
                + "order by abastecimiento_id desc limit 2) as a\n"
                + "order by abastecimiento_id limit 1");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }

    //sentencia de conexion a base de datos
    private void con_sigag() {
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void con_postgresql() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
}
