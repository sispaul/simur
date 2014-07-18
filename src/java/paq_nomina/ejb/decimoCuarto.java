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
    public void Nomina(){

        String nomina ="insert into srh_decimo_cuarto (id_distributivo_roles,ano,ide_columna,ide_periodo,cod_tipo,fecha_ingreso,cedula,nombres)\n" +
                        "SELECT DISTINCT\n" +
                        "e.id_distributivo,\n" +
                        "extract(year from CURRENT_TIMESTAMP) AS anio,\n" +
                        "(SELECT ide_col FROM srh_columnas where codigo_col like 'D4TO') AS columna,\n" +
                        "extract(month from CURRENT_TIMESTAMP) AS mes,\n" +
                        "e.cod_tipo,\n" +
                        "n.fecha_contrato,\n" +
                        "e.cedula_pass,\n" +
                        "e.nombres\n" +
                        "FROM\n" +
                        "srh_empleado e,\n" +
                        "srh_num_contratos n\n" +
                        "WHERE\n" +
                        "e.estado = 1 AND\n" +
                        "n.cod_empleado = e.cod_empleado\n" +
                        "ORDER BY\n" +
                        "e.id_distributivo ASC,\n" +
                        "e.nombres ASC";
        conectar();
        con_postgres.ejecutarSql(nomina);
        con_postgres.desconectar();
        con_postgres = null;
        
    }
    
    public void decimo_cod(){
        String decimo="update srh_decimo_cuarto set valor_decimo = 340 where cod_tipo = 4 or cod_tipo = 7";
        conectar();
        con_postgres.ejecutarSql(decimo);
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
