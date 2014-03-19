/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class Serviciobusqueda {
private Conexion conexion;
private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
public TablaGenerica getPersona(String cedula) {
        //Busca a una persona en la tabla maestra por número de cédula
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT * FROM MAESTRO WHERE cedula='" + cedula.substring(0,cedula.length() - 1) + "' and digito_verificador='" + cedula.substring(cedula.length()-1)+"'");
        tab_persona.ejecutarSql();
//        conexion.desconectar();
        return tab_persona;
    }

    public TablaGenerica getEmpresa(String ruc) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT * FROM MAESTRO_RUC WHERE RUC='" + ruc + "'");
        tab_persona.ejecutarSql();
//        conexion.desconectar();
        return tab_persona;
    }
    
     private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.setUnidad_persistencia(utilitario.getPropiedad("ciudadania"));
            conexion.NOMBRE_MARCA_BASE="sqlserver";
        }
    }
     
}
