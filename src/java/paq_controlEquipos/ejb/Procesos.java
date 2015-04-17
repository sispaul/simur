/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos.ejb;

import framework.aplicacion.TablaGenerica;
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

    public TablaGenerica getTipoLicencia(Integer codigo) {
        conSql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conSql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("SELECT TIPO_LICENCIA_CODIGO,TIPO_LICENCIA_DESCRIPCION from CEI_TIPO_LICENCIA where TIPO_LICENCIA_CODIGO =" + codigo);
        tab_funcionario.ejecutarSql();
        conSql.desconectar();
        conSql = null;
        return tab_funcionario;

    }

    public TablaGenerica getInfoActivo(String codigo) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conPostgres);
        tab_funcionario.setSql("select  \n"
                + "ide_activo,codigo, \n"
                + "nombre,des_activo, \n"
                + "marca,modelo,serie, \n"
                + "(select ide_descripcion from afi_estado where ide_estado_activo = a.ide_estado_activo)as estado \n"
                + ",des_oficina,  \n"
                + "vida_util, \n"
                + "(select des_custodio from afi_custodio where ide_custodio = a.ide_custodio) as responsable, \n"
                + "egreso_bodega, \n"
                + "des_direccion,des_dependencia  \n"
                + "from afi_activos a  \n"
                + "inner join afi_ubicacion d on a.ide_ubicacion= d.ide_ubicacion  \n"
                + "inner join afi_tipo_activo b on a.ide_tipo_activo = b.ide_tipo_activo  \n"
                + "inner join afi_estado c on a.ide_estado_activo= c.ide_estado_activo  \n"
                + "left join (Select a.id_ubica, \n"
                + "b.des_oficina,c.des_direccion,d.des_dependencia  from afi_ubica a,afi_oficina b,afi_direccion c, \n"
                + "afi_dependencia d  \n"
                + "where b.id_oficina=a.id_oficina and  \n"
                + "a.id_direccion =c.id_direccion and  \n"
                + "a.id_dependencia=d.id_dependencia order by des_oficina)p  \n"
                + "on p.id_ubica =a.id_ubica\n"
                + "where serie = '" + codigo + "'");
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
