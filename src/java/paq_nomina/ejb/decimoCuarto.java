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
public class decimoCuarto {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Utilitario utilitario = new Utilitario();
    private Conexion conPostgres;

    //Forma la nomina e insercion en la tabla srh_decimo_cuarto
    public void Nomina() {

        String nomina = "insert into srh_decimo_cuarto (id_distributivo_roles,ano,ide_columna,ide_periodo,cod_tipo,cedula,nombres,ide_empleado)\n"
                + "SELECT DISTINCT on(e.nombres,e.cedula_pass,e.cod_empleado) \n"
                + " e.id_distributivo, \n"
                + " extract(year from CURRENT_TIMESTAMP) AS anio, \n"
                + " (SELECT ide_col FROM srh_columnas where codigo_col like 'D4TO') AS columna, \n"
                + " extract(month from CURRENT_TIMESTAMP) AS mes, \n"
                + " n.cod_tipo, \n"
                + " e.cedula_pass, \n"
                + " e.nombres, \n"
                + " e.cod_empleado \n"
                + " FROM \n"
                + " srh_empleado AS e \n"
                + " INNER JOIN  srh_num_contratos AS n ON e.cod_empleado = n.cod_empleado \n"
                + " WHERE \n"
                + " e.estado = 1 \n"
                + " ORDER BY \n"
                + " e.nombres ASC, \n"
                + " e.cedula_pass ASC, \n"
                + " e.cod_empleado ASC";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();

    }

    public void InsertEm() {

        String nomina = "insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n"
                + "select DISTINCT ide_empleado,ano,ide_periodo,125 as  ide_columnas,0.00 as valor, current_date as fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles\n"
                + "from srh_roles where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + " and ide_periodo=7 and id_distributivo_roles=1 and ide_columnas=14";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();
    }

    public void InsertTra() {

        String nomina = "insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n"
                + "select DISTINCT ide_empleado,ano,ide_periodo,125 as ide_columnas,0.0 as valor,current_date as fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles\n"
                + "from srh_roles where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + " and ide_periodo=7 and ide_columnas=40 and id_distributivo_roles=2";
        conPostgresql();
        conPostgres.ejecutarSql(nomina);
        desPostgresql();
    }

    public void verificar(String iden, Integer codigo) {
        String decimo = "update srh_decimo_cuarto\n"
                + "set fecha_ingreso = (SELECT fecha_contrato FROM srh_num_contratos\n"
                + "where cod_empleado = (SELECT cod_empleado FROM srh_empleado where cedula_pass like '" + iden + "' )\n"
                + "order by fecha_contrato desc LIMIT 1),\n"
                + "descripcion_periodo = (SELECT tipo FROM srh_tipo_empleado where cod_tipo = " + codigo + " )\n"
                + "where cedula like '" + iden + "'";
        conPostgresql();
        conPostgres.ejecutarSql(decimo);
        desPostgresql();
    }

    public void decimo_cont(String iden, Double valor) {
        String decimo = "update srh_decimo_cuarto \n"
                + "set valor_decimo = " + valor + " where cedula like '" + iden + "'";
        conPostgresql();
        conPostgres.ejecutarSql(decimo);
        desPostgresql();
    }

    public void decimo_dias(String iden, Integer dias) {
        String decimo = "update srh_decimo_cuarto \n"
                + "set dias = " + dias + " where cedula like '" + iden + "'";
        conPostgresql();
        conPostgres.ejecutarSql(decimo);
        desPostgresql();
    }

    public void setAccesoSistemas(String desc, String iden, Integer solic) {
        String decimo = "update sca_solicitud_acceso\n"
                + "set " + desc + "='" + iden + "' \n"
                + "where id_solicitud_acceso = " + solic;
        conPostgresql();
        conPostgres.ejecutarSql(decimo);
        desPostgresql();
    }

    public void borrarDecimo() {
        // Forma el sql para el ingreso

        String strSql = "DELETE FROM srh_decimo_cuarto";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        desPostgresql();
    }

    public void migrarDescuento(Integer id_distributivo_rol, Integer ide_columna, String nombre, Double valor, Integer ide_emple) {
        // Forma el sql para el ingreso
        String strSql4 = "update SRH_ROLES \n"
                + "set valor_ingreso= " + valor + ", \n"
                + "valor=" + valor + ",\n"
                + "ip_responsable='" + utilitario.getIp() + "',\n"
                + "nom_responsable='" + nombre + "',\n"
                + "ide_periodo=" + utilitario.getMes(utilitario.getFechaActual()) + ",\n"
                + "fecha_responsable='" + utilitario.getFechaActual() + "'\n"
                + "WHERE SRH_ROLES.ANO=" + utilitario.getAnio(utilitario.getFechaActual()) + " AND \n"
                + "SRH_ROLES.ID_DISTRIBUTIVO_ROLES=" + id_distributivo_rol + " AND \n"
                + "SRH_ROLES.IDE_COLUMNAS=" + ide_columna + " and \n"
                + "srh_roles.ide_empleado =" + ide_emple;
        conPostgresql();
        conPostgres.ejecutarSql(strSql4);
        desPostgresql();
    }

    public void HistoricoDecimo(String usu) {
        // Forma el sql para el ingreso

        String strSql = "insert into srh_decimo_cuarto_historico(id_distributivo_roles,ano,ide_columna,ide_periodo,cod_tipo,fecha_ingreso,valor_decimo,cedula,nombres,ide_decimo_cuarto,ide_empleado,descripcion_periodo,dias,usu_calculo) \n"
                + " SELECT \n"
                + " id_distributivo_roles, \n"
                + " ano, \n"
                + " ide_columna, \n"
                + " ide_periodo, \n"
                + " cod_tipo, \n"
                + " fecha_ingreso, \n"
                + " valor_decimo, \n"
                + " cedula, \n"
                + " nombres, \n"
                + " ide_decimo_cuarto, \n"
                + " ide_empleado, \n"
                + " descripcion_periodo, \n"
                + " dias,\n"
                + " '" + usu + "' as usu\n"
                + " FROM srh_decimo_cuarto \n"
                + " order by nombres asc";
        conPostgresql();
        conPostgres.ejecutarSql(strSql);
        desPostgresql();
    }

    public TablaGenerica periodo(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo=" + periodo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica distibutivo(Integer distri) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_distributivo,descripcion FROM srh_tdistributivo where id_distributivo=" + distri);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica columnas(Integer colum) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=" + colum);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getSolicitudAcceso(Integer colum) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_solicitud_acceso,id_sistema,id_perfil,id_modulo,login_acceso_usuario,password_acceso_usuario,fecha_acceso_usuario,cedula_asigna_acceso,codigo_asigna_acceso,nombre_asigna_acceso,cargo_solicitante,direccion_solicitante\n"
                + "from sca_solicitud_acceso\n"
                + "where id_solicitud_acceso = " + colum);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosEmpleado(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_cargo,cod_direccion\n"
                + "FROM srh_empleado\n"
                + "where cedula_pass = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDatoEmpleado(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_cargo,cod_direccion\n"
                + "FROM srh_empleado\n"
                + "where cod_empleado = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDatoEmpleados(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres FROM srh_empleado where nombres = '" + cedula + "' and estado = 1 order by nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
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
