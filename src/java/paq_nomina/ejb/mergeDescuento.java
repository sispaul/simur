/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina.ejb;

import javax.ejb.Stateless;
import framework.componentes.Tabla;
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
    private Conexion con_postgres= new Conexion();
   
  
    
          
    
    
         public void actualizarDescuento(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
         {
        // Forma el sql para el ingreso
           
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
    
        String str_sql1;
        str_sql1 = "update srh_descuento set ide_empleado=srh_empleado.cod_empleado "
                  +"from  srh_empleado where srh_descuento.ANO=2014 and srh_descuento.IDE_PERIODO=3 and "
                  +"srh_descuento.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" and "
                  +"srh_descuento.IDE_COLUMNA="+ide_columna+" and srh_empleado.cedula_pass=srh_descuento.cedula";
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
       }   
         
          public void actualizarDescuento1(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
         {
        // Forma el sql para el ingreso
           
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
    
        String str_sql1;
        str_sql1 ="update srh_descuento set ide_empleado_rol=srh_roles.ide_empleado "
                  +"from sRH_ROLES WHERE sRH_ROLES.ANO=2014 AND sRH_ROLES.IDE_PERIODO=3 AND "
                  +"sRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" AND sRH_ROLES.IDE_COLUMNAS="+ide_columna+" and "
                  +"srh_roles.ide_empleado=srh_descuento.ide_empleado";              
        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
       }   
         
         public void borrarDescuento() 
                {
        // Forma el sql para el ingreso
           
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
    
              String str_sql3 = "DELETE FROM srh_descuento";
              con_postgres.ejecutarSql(str_sql3);
              con_postgres.desconectar();
     }   
       public void migrarDescuento(Integer ano,Integer ide_periodo,Integer id_distributivo_roles,Integer ide_columna) 
                {
        // Forma el sql para el ingreso
           
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
    
              String str_sql4 = "update sRH_ROLES set valor_egreso=srh_descuento.descuento"
                                +", valor=srh_descuento.descuento  from srh_descuento"
                                +" WHERE sRH_ROLES.ANO=2014 AND sRH_ROLES.IDE_PERIODO=3 AND"
                                +" sRH_ROLES.ID_DISTRIBUTIVO_ROLES="+id_distributivo_roles+" AND sRH_ROLES.IDE_COLUMNAS="+ide_columna+" and "
                                +"srh_roles.ide_empleado=srh_descuento.ide_empleado";

              con_postgres.ejecutarSql(str_sql4);
              con_postgres.desconectar();
     }   
}      

