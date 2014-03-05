/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina.ejb;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;


@Stateless
public class mergeDescuento {
    
    private Utilitario utilitario = new Utilitario();

    private Conexion con_postgres= new Conexion();
    private Tabla tab_descuentos = new Tabla();
 
    
    /**
     * 
     * @param ANO
     * @param IDE_PERIODO
     * @param ID_DISTRIBUTIVO_ROLES
     * @param IDE_COLUMNA 
     */
    
       public void actualizarDescuentoIdeEmpleado() {
        // Forma el sql para el ingreso
           
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        Integer  men_ano= null ;
        Integer men_periodo = null;
        Integer men_descuentos= null;
        Integer men_columna=null;
        
        tab_descuentos.setId("tab_descuentos");
        tab_descuentos.setConexion(con_postgres);
        tab_descuentos.setSql("select ano,ide_periodo,ide_distributivo_roles,ide_columna from srh_descuento");
        tab_descuentos.setCampoPrimaria("ide_descuentos");
//        tab_descuentos.dibujar();
              for (int i = 0; i < tab_descuentos.getTotalFilas(); i++) {
                  men_ano= parseInt(tab_descuentos.getValor(i, "ano"));
                          

            men_periodo=parseInt(tab_descuentos.getValor(i, "ide_periodo"));
            men_descuentos=parseInt(tab_descuentos.getValor(i, "id_distributivo_roles"));
            men_columna=parseInt(tab_descuentos.getValor(i, "ide_columna"));
        }
        
        String str_sql1 = "update srh_descuento  set ide_empleado=srh_empleado.cod_empleado "
                + "from  srh_empleado e  where e.cedula_pass=cedula AND  srh_descuento.ANO=2014 AND srh_descuento.IDE_PERIODO=2 AND srh_descuento.ID_DISTRIBUTIVO_ROLES=2 "
                + "AND srh_descuento.IDE_COLUMNA=46";

        con_postgres.ejecutarSql(str_sql1);
        con_postgres.desconectar();
       
    }   

    private Integer parseInt(String valor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
  
