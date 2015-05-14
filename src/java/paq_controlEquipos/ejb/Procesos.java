/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos.ejb;

import framework.aplicacion.TablaGenerica;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class Procesos {

    private Conexion conSql,
            conPostgres;//Conexion a la base de sigag
    private Utilitario utilitario = new Utilitario();
    private Statement smt;

    public TablaGenerica getTipoLicencia(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT TIPO_LICENCIA_CODIGO,TIPO_LICENCIA_DESCRIPCION FROM CEI_TIPO_LICENCIA where TIPO_LICENCIA_CODIGO =" + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoTabla(String codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT Table_Name, COUNT(*) As NumeroCampos\n"
                + "FROM Information_Schema.Columns\n"
                + "WHERE Table_Name = '" + codigo + "'\n"
                + "GROUP BY Table_Name");
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;
    }

    public TablaGenerica getInfoCampoTabla(String codigo, Integer valor) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT ordinal_position,Column_Name,data_type\n"
                + "FROM Information_Schema.Columns\n"
                + "WHERE Table_Name = '" + codigo + "' and ordinal_position =" + valor + "\n"
                + "ORDER BY ordinal_position");
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;
    }

    public TablaGenerica getCatalgoTablas(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT * FROM CEI_CATALOGO_TABLAS WHERE CATALOGO_CODIGO=" + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getCatalgoTab(String codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT * FROM CEI_CATALOGO_TABLAS WHERE CATALOGO_ORIGEN='" + codigo + "'");
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getCatalgoProg(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT * FROM CEI_DETALLE_PROGRAMAS where DETALLE_CODIGO = " + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getCatalogoDatosql(String datos, String tabla, String condicion) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("select " + datos + " from " + tabla + " where " + condicion + "");
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;
    }

    public TablaGenerica getInfoLicencia(Integer codigo, Integer cod, String valor) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT top 1\n"
                + "d.DETALLE_CODIGO,\n"
                + "p.PROGS_DESCRIPCION,\n"
                + "t.TIPO_LICENCIA_DESCRIPCION,\n"
                + "d.DETALLE_NUMERO_LICENCIA,\n"
                + "d.DETALLE_CANTIDAD,\n"
                + "m.MODELO_DESCRIPCION\n"
                + "FROM CEI_DETALLE_PROGRAMAS d\n"
                + "left join CEI_PROGRAMAS p on d.PROGS_CODIGO = p.PROGS_CODIGO\n"
                + "left join CEI_TIPO_LICENCIA t on d.LICEN_CODIGO = t.TIPO_LICENCIA_CODIGO\n"
                + "left join CEI_MODELO_LICENCIA m on d.MODELO_CODIGO = m.MODELO_CODIGO\n"
                + "where p.PROGS_CODIGO=" + codigo + " and m.MODELO_CODIGO " + valor + " and t.TIPO_LICENCIA_CODIGO =" + cod);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoModelo(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT MODELO_CODIGO,MODELO_DESCRIPCION FROM CEI_MODELO_LICENCIA where MODELO_CODIGO =" + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoPrograma(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT\n"
                + "d.DETALLE_CODIGO,\n"
                + "p.PROGS_DESCRIPCION,\n"
                + "t.TIPO_LICENCIA_DESCRIPCION,\n"
                + "d.DETALLE_NUMERO_LICENCIA,\n"
                + "d.DETALLE_CANTIDAD,\n"
                + "m.MODELO_DESCRIPCION\n"
                + "FROM CEI_DETALLE_PROGRAMAS d \n"
                + "left join CEI_PROGRAMAS p on d.PROGS_CODIGO = p.PROGS_CODIGO \n"
                + "left join CEI_TIPO_LICENCIA t on d.LICEN_CODIGO = t.TIPO_LICENCIA_CODIGO \n"
                + "left join CEI_MODELO_LICENCIA m on d.MODELO_CODIGO = m.MODELO_CODIGO\n"
                + "where d.DETALLE_CODIGO = " + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;
    }

    public void setActuaProve(Integer codigo, String desc, String dato, String valor, String cadena) {
        String au_sql = "update " + desc + " set\n"
                + "" + dato + " ='" + valor + "'\n" +//mve_horometro
                "where " + cadena + "=" + codigo;
        conSql();
        conSql.ejecutarSql(au_sql);
        conSql.desconectar();
        conSql = null;
    }

    public TablaGenerica getCatalogoDatoposgres(String datos, String tabla, String condicion) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("select " + datos + " from " + tabla + " where " + condicion + "");
        tab_funcionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoActivo(String codigo) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("select ide_activo,codigo,nombre,des_activo,marca,modelo,serie,\n"
                + "(select ide_descripcion from afi_estado where ide_estado_activo = a.ide_estado_activo)as estado,\n"
                + "fec_alta,\n"
                + "(case when casa_comercial is null then 'S/CC' else casa_comercial end)as casa_comercial,\n"
                + "(select des_custodio from afi_custodio where ide_custodio = a.ide_custodio) as responsable,\n"
                + "(select cod_empleado from srh_empleado where nombres like (select des_custodio from afi_custodio where ide_custodio = a.ide_custodio)) as cod_empleado,\n"
                + "(case when tipo ='A' then 'ACTIVO' when tipo ='BC' then 'BIENES DE CONTROL' when tipo='BM' then 'BIENES SALIDOS DE LA MUNICIPALIDAD' end) as des_ti,\n"
                + "departamento,des_direccion,id_direccion,id_dependencia\n"
                + "from afi_activos a \n"
                + "inner join afi_ubicacion d on a.ide_ubicacion= d.ide_ubicacion \n"
                + "inner join afi_tipo_activo b on a.ide_tipo_activo = b.ide_tipo_activo \n"
                + "inner join afi_estado c on a.ide_estado_activo= c.ide_estado_activo \n"
                + "left join (Select a.id_ubica,b.des_oficina as departamento,b.des_oficina,c.des_direccion,d.des_dependencia,c.id_direccion,\n"
                + "d.id_dependencia  \n"
                + "from afi_ubica a,afi_oficina b,afi_direccion c,afi_dependencia d \n"
                + "where b.id_oficina=a.id_oficina and a.id_direccion =c.id_direccion and a.id_dependencia=d.id_dependencia \n"
                + "order by des_oficina)p on p.id_ubica =a.id_ubica \n"
                + "where codigo = '" + codigo + "'");
        tab_funcionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoActivo1(String codigo) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("select ide_activo,codigo,nombre,des_activo,marca,modelo,serie,\n"
                + "(select ide_descripcion from afi_estado where ide_estado_activo = a.ide_estado_activo)as estado,\n"
                + "fec_alta,\n"
                + "(case when casa_comercial is null then 'S/CC' else casa_comercial end)as casa_comercial,\n"
                + "(select des_custodio from afi_custodio where ide_custodio = a.ide_custodio) as responsable,\n"
                + "(select cod_empleado from srh_empleado where nombres like (select des_custodio from afi_custodio where ide_custodio = a.ide_custodio)) as cod_empleado,\n"
                + "(case when tipo ='A' then 'ACTIVO' when tipo ='BC' then 'BIENES DE CONTROL' when tipo='BM' then 'BIENES SALIDOS DE LA MUNICIPALIDAD' end) as des_ti,\n"
                + "departamento,des_direccion,id_direccion,id_dependencia\n"
                + "from afi_activos a \n"
                + "inner join afi_ubicacion d on a.ide_ubicacion= d.ide_ubicacion \n"
                + "inner join afi_tipo_activo b on a.ide_tipo_activo = b.ide_tipo_activo \n"
                + "inner join afi_estado c on a.ide_estado_activo= c.ide_estado_activo \n"
                + "left join (Select a.id_ubica,b.des_oficina as departamento,b.des_oficina,c.des_direccion,d.des_dependencia,c.id_direccion,\n"
                + "d.id_dependencia  \n"
                + "from afi_ubica a,afi_oficina b,afi_direccion c,afi_dependencia d \n"
                + "where b.id_oficina=a.id_oficina and a.id_direccion =c.id_direccion and a.id_dependencia=d.id_dependencia \n"
                + "order by des_oficina)p on p.id_ubica =a.id_ubica \n"
                + "where serie = '" + codigo + "'");
        tab_funcionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoEmpleado(String codigo) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("SELECT e.cod_empleado,\n"
                + "e.nombres,\n"
                + "c.nombre_cargo,\n"
                + "d.nombre_dir\n"
                + "FROM srh_empleado e\n"
                + "INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo \n"
                + "INNER JOIN srh_direccion d on e.cod_direccion = d.cod_direccion\n"
                + "where e.cod_empleado = '" + codigo + "'");
        tab_funcionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoProveedor(String cod) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("SELECT ide_proveedor,ruc,titular from tes_proveedores where ide_proveedor='" + cod + "'order by titular");
        tab_funcionario.ejecutarSql();
        conPostgres.desconectar();
        conPostgres = null;
        return tab_funcionario;
    }

    private void conSql() {
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
}
