/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_registros.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
@Stateless
public class ServicioRegistros {

    private Utilitario utilitario = new Utilitario();
    private Conexion 
            con_sql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres,//Cnexion a la base de postgres 2014
            con_ciudadania; //Conexion a la base de ciudadania

    public TablaGenerica getPersona(String cedula) {
        //Busca a una persona en la tabla maestra por número de cédula
        con_ciudadanos();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_ciudadania);
        tab_persona.setSql("SELECT * FROM MAESTRO WHERE cedula='" + cedula.substring(0,cedula.length() - 1) + "' and digito_verificador='" + cedula.substring(cedula.length()-1)+"'");
        tab_persona.ejecutarSql();
        con_ciudadania.desconectar();
        con_ciudadania = null;
        return tab_persona;
    }

    public TablaGenerica getPersonaPasaporte(String pasaporte) {
        //Busca a una persona en la tabla maestra por número de pasaporte
        con_ciudadanos();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_ciudadania);
        tab_persona.setSql("SELECT * FROM MAESTRO_PASAPORTE WHERE CODIGO='" + pasaporte + "'");
        tab_persona.ejecutarSql();
        con_ciudadania.desconectar();
        con_ciudadania = null;
        return tab_persona;
    }

    public TablaGenerica getEmpresa(String ruc) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        con_ciudadanos();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_ciudadania);
        tab_persona.setSql("SELECT * FROM MAESTRO_RUC WHERE RUC='" + ruc + "'");
        tab_persona.ejecutarSql();
        con_ciudadania.desconectar();
        con_ciudadania = null;
        return tab_persona;
    }

    private void con_ciudadanos(){
        if(con_ciudadania == null){
            con_ciudadania = new Conexion();
            con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadania"));
        }
    }
}
