/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina.ejb;


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
    public void Nomina(){

        String nomina ="insert into srh_decimo_cuarto (id_distributivo_roles,ano,ide_columna,ide_periodo,cod_tipo,cedula,nombres,ide_empleado)\n" +
                        "select id_distributivo,anio,columna,mes,cod_tipo,cedula_pass,nombres,cod_empleado\n" +
                        "from nomv_decimo_cuarto\n" +
                        "order by id_distributivo, nombres";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
        
    }

    public void InsertEm(){

        String nomina ="insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n" +
                        "select ide_empleado,ano,ide_periodo,125 as  ide_columnas,0.00 as valor, current_date as fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles\n" +
                        "from srh_roles where ano=2013 and ide_periodo=7  and id_distributivo_roles=1  \n" +
                        "and ide_columnas=14";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void InsertTra(){

        String nomina ="insert into srh_roles (ide_empleado,ano,ide_periodo,ide_columnas,valor,fecha_responsable,cod_cargo_rol,ide_programa,id_distributivo_roles)\n" +
                        "select ide_empleado,ano,ide_periodo,125 as ide_columnas,0.0 as valor,now() as fecha_responsable,\n" +
                        "cod_cargo_rol,ide_programa,id_distributivo_roles\n" +
                        "from srh_roles where ano=2013 and ide_periodo=7 and ide_columnas=40 and id_distributivo_roles";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void verificar(String iden){
        String decimo="update srh_decimo_cuarto\n" +
                        "set fecha_ingreso = (SELECT fecha_contrato FROM srh_num_contratos\n" +
                        "where cod_empleado = (SELECT cod_empleado FROM srh_empleado where cedula_pass like '"+iden+"')\n" +
                        "order by fecha_contrato desc LIMIT 1)\n" +
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
    
    public void borrarDecimo() 
                {
        // Forma el sql para el ingreso
    
        String str_sql3 = "DELETE FROM srh_decimo_cuarto";
        conectar();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
     }
     
     public void migrarDescuento(Integer id_distributivo_rol,Integer ide_columna,String nombre,Double valor,Integer ide_emple) 
                {
        // Forma el sql para el ingreso
    
        String str_sql4 = "update SRH_ROLES \n" +
                            "set valor_ingreso= "+valor+", \n" +
                            "valor="+valor+",\n" +
                            "ip_responsable='"+utilitario.getIp()+"',\n" +
                            "nom_responsable='"+nombre+"',\n" +
                            "fecha_responsable='"+utilitario.getFechaActual()+"'\n" +
                            "WHERE SRH_ROLES.ANO="+utilitario.getAnio(utilitario.getFechaActual())+" AND \n" +
                            "SRH_ROLES.IDE_PERIODO="+utilitario.getMes(utilitario.getFechaActual())+" AND \n" +
                            "SRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_rol+" AND \n" +
                            "SRH_ROLES.IDE_COLUMNAS="+ide_columna+" and \n" +
                            "srh_roles.ide_empleado ="+ide_emple;
        conectar();
        con_postgres.ejecutarSql(str_sql4);
        con_postgres.desconectar();
        con_postgres = null;
     }
    
    private void conectar() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
}
    
}
