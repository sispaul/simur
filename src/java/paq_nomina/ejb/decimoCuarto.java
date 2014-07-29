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
    private Conexion con_postgres;
    
    //Forma la nomina e insercion en la tabla srh_decimo_cuarto
    public void Nomina(Integer dis){

        String nomina ="insert into srh_decimo_cuarto (id_distributivo_roles,ano,ide_columna,ide_periodo,cod_tipo,cedula,nombres,ide_empleado)\n" +
                        "SELECT DISTINCT on(e.cedula_pass,e.cod_empleado,e.nombres)\n" +
                        "e.id_distributivo,\n" +
                        "extract(year from CURRENT_TIMESTAMP) AS anio,\n" +
                        "(SELECT ide_col FROM srh_columnas where codigo_col like 'D4TO') AS columna,\n" +
                        "8 as periodo,\n" +
                        "n.cod_tipo,\n" +
                        "e.cedula_pass,\n" +
                        "e.nombres,\n" +
                        "e.cod_empleado\n" +
                        "FROM\n" +
                        "\"public\".srh_empleado AS e\n" +
                        "INNER JOIN \"public\".srh_num_contratos AS n ON e.cod_empleado = n.cod_empleado\n" +
                        "WHERE\n" +
                        "e.estado = 1 and e.id_distributivo = "+dis+"\n" +
                        "ORDER BY\n" +
                        "e.cedula_pass ASC,\n" +
                        "e.cod_empleado ASC,\n" +
                        "e.nombres ASC";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
        
    }

    public void InsertEm(){

        String nomina ="insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n" +
                        "select DISTINCT ide_empleado,ano,ide_periodo,125 as  ide_columnas,0.00 as valor, current_date as fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles\n" +
                        "from srh_roles where ano="+utilitario.getAnio(utilitario.getFechaActual())+" and ide_periodo=6 and id_distributivo_roles=1 and ide_columnas=14";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void InsertTra(){

        String nomina ="insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n" +
                        "select DISTINCT ide_empleado,ano,ide_periodo,125 as ide_columnas,0.0 as valor,current_date as fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles\n" +
                        "from srh_roles where ano="+utilitario.getAnio(utilitario.getFechaActual())+" and ide_periodo=6 and ide_columnas=40 and id_distributivo_roles=2";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void verificar(String iden,Integer codigo){
        String decimo="update srh_decimo_cuarto\n" +
                        "set fecha_ingreso = (SELECT fecha_contrato FROM srh_num_contratos\n" +
                        "where cod_empleado = (SELECT cod_empleado FROM srh_empleado where cedula_pass like '"+iden+"' )\n" +
                        "order by fecha_contrato desc LIMIT 1),\n" +
                        "descripcion_periodo = (SELECT tipo FROM srh_tipo_empleado where cod_tipo = "+codigo+" )\n" +
                        "where cedula like '"+iden+"'";
        conectar();
        con_postgres.ejecutarSql(decimo);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void decimo_cont(String iden,Double valor){
        String decimo="update srh_decimo_cuarto \n" +
                       "set valor_decimo = "+valor+" where cedula like '"+iden+"'";
        conectar();
        con_postgres.ejecutarSql(decimo);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void decimo_dias(String iden,Integer dias){
        String decimo="update srh_decimo_cuarto \n" +
                       "set dias = "+dias+" where cedula like '"+iden+"'";
        conectar();
        con_postgres.ejecutarSql(decimo);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void borrarDecimo() 
                {
        // Forma el sql para el ingreso
    
        String str_sql3 = "DELETE FROM srh_decimo_cuarto";
        conectar();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
     }
     
     public void migrarDescuento(Integer id_distributivo_rol,Integer ide_columna,String nombre,Double valor,Integer ide_emple){
        // Forma el sql para el ingreso
        String str_sql4 = "update SRH_ROLES \n" +
                            "set valor_ingreso= "+valor+", \n" +
                            "valor="+valor+",\n" +
                            "ip_responsable='"+utilitario.getIp()+"',\n" +
                            "nom_responsable='"+nombre+"',\n" +
                            "ide_periodo="+utilitario.getMes(utilitario.getFechaActual())+",\n" +
                            "fecha_responsable='"+utilitario.getFechaActual()+"'\n" +
                            "WHERE SRH_ROLES.ANO="+utilitario.getAnio(utilitario.getFechaActual())+" AND \n" +
                            "SRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_rol+" AND \n" +
                            "SRH_ROLES.IDE_COLUMNAS="+ide_columna+" and \n" +
                            "srh_roles.ide_empleado ="+ide_emple;
        conectar();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
     }
    
     public TablaGenerica periodo(Integer periodo){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual where ide_periodo="+periodo );
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
 
 public TablaGenerica distibutivo(Integer distri){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT id_distributivo,descripcion FROM srh_tdistributivo where id_distributivo="+distri);
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
 
 public TablaGenerica columnas(Integer colum){
        conectar();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conectar();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col="+colum );
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
     
    private void conectar() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
}
    
}
