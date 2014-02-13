/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
@Stateless
public class servicioPruebas {

    private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public int getStickProducto(String ide_prod) {

        ///toda la programacion

        return 0;
    }

    public void ejemploTabla() {
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM SIS_USUARIO");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            System.out.println(tab_consulta.getValor(i, "NOM_USUA"));
        }
        
        //Pol de conexiones
        Conexion con_postgres=null;
        
        TablaGenerica tab_otra=new TablaGenerica();
        tab_otra.setConexion(con_postgres);
        tab_otra.setSql("SELECT .....");
        tab_otra.ejecutarSql();
        
        
        
    }
}
