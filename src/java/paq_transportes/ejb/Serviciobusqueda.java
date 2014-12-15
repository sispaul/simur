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
    private Conexion 
            con_sql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres,//Cnexion a la base de postgres 2014
            con_ciudadania; //Conexion a la base de ciudadania
    
private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
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
    
    //PLACAS DE PAPEL
    public TablaGenerica getPlaca(String placa){
        con_sigag();
        TablaGenerica tab_placa = new TablaGenerica();
        tab_placa.setConexion(con_sql);
        tab_placa.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                            "d.IDE_PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO,\n" +
                            "d.NOMBRE_PROPIETARIO,\n" +
                            "d.NUMERO_RVMO,\n" +
                            "p.PLACA\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                            "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                            "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel') and p.PLACA like '"+placa+"'");
        tab_placa.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_placa;
    }
    
    public TablaGenerica getFactura(String factura){
        con_sigag();
        TablaGenerica tab_factura = new TablaGenerica();
        tab_factura.setConexion(con_sql);
        tab_factura.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                            "d.IDE_PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO,\n" +
                            "d.NOMBRE_PROPIETARIO,\n" +
                            "d.NUMERO_RVMO,\n" +
                            "p.PLACA\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                            "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                            "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel') and d.NUMERO_RVMO LIKE '"+factura+"'");
        tab_factura.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_factura;
    }
    
    public TablaGenerica getGestor(Integer id){
        con_sigag();
        TablaGenerica tab_gestor = new TablaGenerica();
        tab_gestor.setConexion(con_sql);
        tab_gestor.setSql("SELECT s.IDE_SOLICITUD_PLACA,\n" +
                            "g.CEDULA_GESTOR,\n" +
                            "g.NOMBRE_GESTOR,\n" +
                            "d.IDE_DETALLE_SOLICITUD\n" +
                            "FROM\n" +
                            "dbo.TRANS_SOLICITUD_PLACA s,dbo.TRANS_DETALLE_SOLICITUD_PLACA d,dbo.TRANS_GESTOR g\n" +
                            "where \n" +
                            "d.IDE_SOLICITUD_PLACA = s.IDE_SOLICITUD_PLACA and\n" +
                            "s.IDE_GESTOR = g.IDE_GESTOR and\n" +
                            "d.IDE_DETALLE_SOLICITUD ="+id);
        tab_gestor.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_gestor;
    }
    
    //PLACAS FISICAS
     public TablaGenerica getPlacaF(String placa1){ //PAPEL Y DEFINITIVA
        con_sigag();
        TablaGenerica tab_placa = new TablaGenerica();
        tab_placa.setConexion(con_sql);
        tab_placa.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                        "d.IDE_PLACA,\n" +
                        "d.CEDULA_RUC_PROPIETARIO,\n" +
                        "d.NOMBRE_PROPIETARIO,\n" +
                        "d.NUMERO_RVMO,\n" +
                        "p.PLACA\n" +
                        "FROM\n" +
                        "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                        "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                        "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel')\n" +
                        "and p.IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva') and p.PLACA like '"+placa1+"'");
        tab_placa.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_placa;
    }

     public TablaGenerica getPlacaFF(String plac){//DEFINITIVA Y DEFINITIVA
        con_sigag();
        TablaGenerica tab_placa = new TablaGenerica();
        tab_placa.setConexion(con_sql);
        tab_placa.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                        "d.IDE_PLACA,\n" +
                        "d.CEDULA_RUC_PROPIETARIO,\n" +
                        "d.NOMBRE_PROPIETARIO,\n" +
                        "d.NUMERO_RVMO,\n" +
                        "p.PLACA\n" +
                        "FROM\n" +
                        "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                        "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                        "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva')\n" +
                        "and p.IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva') and p.PLACA like '"+plac+"'");
        tab_placa.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_placa;
    }     
     
    public TablaGenerica getCedula(String cedula){//PAPEL Y DEFINITIVA
        con_sigag();
        TablaGenerica tab_factura = new TablaGenerica();
        tab_factura.setConexion(con_sql);
        tab_factura.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                            "d.IDE_PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO,\n" +
                            "d.NOMBRE_PROPIETARIO,\n" +
                            "d.NUMERO_RVMO,\n" +
                            "p.PLACA\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                            "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                            "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel')\n" +
                            "and p.IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva') and d.CEDULA_RUC_PROPIETARIO like '"+cedula+"'");
        tab_factura.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_factura;
    }

    public TablaGenerica getCedulaF(String cedu){//DEFINITIVA Y DEFINITIVA
        con_sigag();
        TablaGenerica tab_factura = new TablaGenerica();
        tab_factura.setConexion(con_sql);
        tab_factura.setSql("SELECT d.IDE_DETALLE_SOLICITUD,\n" +
                            "d.IDE_PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO,\n" +
                            "d.NOMBRE_PROPIETARIO,\n" +
                            "d.NUMERO_RVMO,\n" +
                            "p.PLACA\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,dbo.TRANS_PLACAS p \n" +
                            "where d.IDE_PLACA = p.IDE_PLACA and d.APROBADO_SOLICITUD = 1\n" +
                            "and p.IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva')\n" +
                            "and p.IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva') and d.CEDULA_RUC_PROPIETARIO like '"+cedu+"'");
        tab_factura.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_factura;
    }    
    
    public TablaGenerica getGestors(String cedula) {
        //Busca a una persona en la tabla maestra por número de cédula
        con_sigag();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_sql);
        tab_persona.setSql("SELECT\n" +
                            "g.CEDULA_GESTOR,\n" +
                            "g.NOMBRE_GESTOR,\n" +
                            "c.NOMBRE_EMPRESA\n" +
                            "FROM\n" +
                            "dbo.TRANS_GESTOR g,dbo.TRANS_COMERCIAL_AUTOMOTORES c\n" +
                            "where g.IDE_COMERCIAL_AUTOMOTORES = c.IDE_COMERCIAL_AUTOMOTORES\n" +
                            "and g.CEDULA_GESTOR like '"+cedula+"'");
        tab_persona.ejecutarSql();
        con_sql.desconectar();
        con_sql = null;
        return tab_persona;
    }

     private void con_sigag(){
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia("recursojdbc");
        }
     }
    
    private void con_ciudadanos(){
        if(con_ciudadania == null){
            con_ciudadania = new Conexion();
            con_ciudadania.setUnidad_persistencia("ciudadania");
        }
    }
}
