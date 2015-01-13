/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class manauto {
    private Conexion 
            con_sql,//Conexion a la base de sigag
            con_postgres;//Cnexion a la base de postgres 2014
     
    private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    //acciones para marcas
    public TablaGenerica get_DuplicaMarca(String nombre) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mvmarca_id,mvmarca_descripcion,mvmarca_estado\n" +
                "FROM mvmarca_vehiculo\n" +
                "where mvmarca_descripcion like '"+nombre+"'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }
    
    public void set_marca(String nombre,String login){
        String parametro ="insert into mvmarca_vehiculo (mvmarca_descripcion,mvmarca_estado,mvmarca_fechaing,mvmarca_loginin)\n" +
                "values ('"+nombre+"',1,'"+utilitario.getFechaActual()+"','"+login+"')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteMarcas(Integer anti){
    String au_sql="delete from mvmarca_vehiculo where mvmarca_id ="+anti;
    con_postgresql();
    con_postgres.ejecutarSql(au_sql);
    con_postgres.desconectar();
    con_postgres = null;
    }
      
    //acciones para tipo
    public TablaGenerica get_DuplicaTipo(String nombre,Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT mvtipo_id,mvtipo_descripcion FROM mvtipo_vehiculo where mvmarca_id = "+codigo+" and mvtipo_descripcion like '"+nombre+"'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }
    
    public void setTipo(String nombre,String login,Integer marca){
        String parametro ="insert into mvtipo_vehiculo (mvmarca_id,mvtipo_descripcion,mvtipo_estado,mvtipo_fechaing,mvtipo_loginin)\n" +
                "values ("+marca+",'"+nombre+"',1,'"+utilitario.getFechaActual()+"','"+login+"')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteTipos(Integer anti){
    String au_sql="delete from mvtipo_vehiculo where mvtipo_id ="+anti;
    con_postgresql();
    con_postgres.ejecutarSql(au_sql);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    //acciones para modelo
    public TablaGenerica get_DuplicaModelo(String nombre,Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n" +
                "mvmodelo_id,\n" +
                "mvmodelo_descripcion\n" +
                "FROM\n" +
                "mvmodelo_vehiculo\n" +
                "where mvmodelo_descripcion ='"+nombre+"' and  MVTIPO_ID="+codigo);
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }
    
    public void setModelo(String nombre,String login,Integer tipo){
        String parametro ="insert into mvmodelo_vehiculo(mvtipo_id,mvmodelo_descripcion,mvmodelo_estado,mvmodelo_fechaing,mvmodelo_loginin)\n" +
                "values ("+tipo+",'"+nombre+"',1,'"+utilitario.getFechaActual()+"','"+login+"')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteModelos(Integer anti){
    String au_sql="delete from mvmodelo_vehiculo where mvmodelo_id ="+anti;
    con_postgresql();
    con_postgres.ejecutarSql(au_sql);
    con_postgres.desconectar();
    con_postgres = null;
    }    
    
    public TablaGenerica get_DuplicaVersion(String nombre,Integer codigo) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        con_postgresql();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT\n" +
                "mvversion_id,\n" +
                "mvversion_descripcion\n" +
                "FROM mvversion_vehiculo\n" +
                "where mvmodelo_id ="+codigo+" and mvversion_descripcion='"+nombre+"'");
        tab_persona.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_persona;
    }
    
    public void setVersion(String nombre,String login,Integer modelo){
        String parametro ="insert into mvversion_vehiculo(mvmodelo_id,mvversion_descripcion,mvversion_estado,mvversion_fechaing,mvversion_loginin)\n" +
                "values ("+modelo+",'"+nombre+"',1,'"+utilitario.getFechaActual()+"','"+login+"')";
        con_postgresql();
        con_postgres.ejecutarSql(parametro);
        con_postgres.desconectar();
        con_postgres = null;
    }
    
    public void deleteversion(Integer anti){
    String au_sql="delete from mvversion_vehiculo where mvversion_id ="+anti;
    con_postgresql();
    con_postgres.ejecutarSql(au_sql);
    con_postgres.desconectar();
    con_postgres = null;
    }
    
    //DATOS DE CHOFER
    public TablaGenerica getChofer(String cedula) {
        con_postgresql();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_postgres);
        tab_persona.setSql("SELECT cod_empleado, cedula_pass,nombres, 1 as activo\n" +
                "FROM srh_empleado\n" +
                "where cod_empleado ='"+cedula+"' and estado = 1\n" +
                "order by nombres");
        tab_persona.ejecutarSql();
       con_postgres.desconectar();
       con_postgres = null;
       return tab_persona;
    }
    
    //sentencia de conexion a base de datos
    private void con_sigag(){
        if (con_sql == null) {
            con_sql = new Conexion();
            con_sql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }
    
    private void con_postgresql(){
        if(con_postgres == null){
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }
    
}
