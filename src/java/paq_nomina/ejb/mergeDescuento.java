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
 * @author m-paucar
 */
@Stateless
public class mergeDescuento {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Utilitario utilitario = new Utilitario();
    private Conexion con_postgres;
      
    
         public void actualizarDescuento(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
         {
        // Forma el sql para el ingreso
    
        String str_sql1;
        str_sql1 = "update srh_descuento set ide_empleado=srh_empleado.cod_empleado "
                  +"from  srh_empleado where srh_descuento.ANO="+utilitario.getAnio(utilitario.getFechaActual())+" and srh_descuento.IDE_PERIODO="+(utilitario.getMes(utilitario.getFechaActual())-1)+" and "
                  +"srh_descuento.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" and "
                  +"srh_descuento.IDE_COLUMNA="+ide_columna+" and srh_empleado.cedula_pass=srh_descuento.cedula";
        conectar();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
       }   
         
          public void actualizarDescuento1(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
         {
        // Forma el sql para el ingreso
    
        String str_sql1;
        str_sql1 ="update srh_descuento set ide_empleado_rol=srh_roles.ide_empleado "
                  +"from sRH_ROLES WHERE sRH_ROLES.ANO="+utilitario.getAnio(utilitario.getFechaActual())+" AND sRH_ROLES.IDE_PERIODO="+(utilitario.getMes(utilitario.getFechaActual())-1)+" AND "
                  +"sRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" AND sRH_ROLES.IDE_COLUMNAS="+ide_columna+" and "
                  +"srh_roles.ide_empleado=srh_descuento.ide_empleado";  
        conectar();
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
        con_postgres = null;
       }   
         
         public void borrarDescuento() 
                {
        // Forma el sql para el ingreso
    
        String str_sql3 = "DELETE FROM srh_descuento";
        conectar();
        con_postgres.ejecutarSql(str_sql3);
        con_postgres.desconectar();
        con_postgres = null;
     }   
       public void migrarDescuento(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
                {
        // Forma el sql para el ingreso
    
        String str_sql4 = "update sRH_ROLES set valor_egreso=srh_descuento.descuento"
                                +", valor=srh_descuento.descuento  from srh_descuento"
                                +" WHERE sRH_ROLES.ANO="+utilitario.getAnio(utilitario.getFechaActual())+" AND sRH_ROLES.IDE_PERIODO="+(utilitario.getMes(utilitario.getFechaActual())-1)+" AND"
                                +" sRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" AND sRH_ROLES.IDE_COLUMNAS="+ide_columna+" and "
                                +"srh_roles.ide_empleado=srh_descuento.ide_empleado";
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

