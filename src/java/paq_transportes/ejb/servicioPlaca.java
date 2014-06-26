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
 * @author KEJA
 */
@Stateless
public class servicioPlaca {
private Conexion conexion;
private Utilitario utilitario = new Utilitario();
private Conexion con_postgres;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

//ASIGNACION DE ESTADO A PLACAS NUEVAS
public void placaNew(Integer placa)
{
      String nueva ="UPDATE TRANS_PLACAS\n" +
                    "set IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible'),\n" +
                    "IDE_TIPO_PLACA ="+placa+",IDE_TIPO_PLACA2 ="+placa+" WHERE IDE_TIPO_ESTADO is null";
      conectar();
      conexion.ejecutarSql(nueva);
      conexion.desconectar();
      conexion = null;
    }

public void placaActual(String placa)
{
      String nueva ="UPDATE TRANS_PLACAS set IDE_TIPO_PLACA2 =(SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'DEFINITIVA'),\n" +
                    "FECHA_DEFINITIVA_PLACA = '"+utilitario.getFechaActual()+"' WHERE PLACA LIKE'"+placa+"'";
      conectar();
      conexion.ejecutarSql(nueva);
      conexion.desconectar();
      conexion = null;
    }

//ACTUALIZACION DE APROBACION EN SOLICITUD y ASIGNACION AUTOMATICA DE PLACA
public void seleccionarPF(Integer id_s,Integer vehiculo,Integer servicio,Integer id_a)
{
      String actualiza ="update TRANS_DETALLE_SOLICITUD_PLACA \n" +
                        "set APROBADO_SOLICITUD= 1,IDE_APROBACION_PLACA= "+id_a+",ide_placa =\n" +
                        "(SELECT TOP 1 IDE_PLACA \n" +
                        "FROM (SELECT TOP 2 IDE_PLACA FROM TRANS_PLACAS\n" +
                        "WHERE IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')\n" +
                        "AND IDE_TIPO_VEHICULO= "+vehiculo+" \n" +
                        "AND IDE_TIPO_SERVICIO = "+servicio+"\n" +
                        "AND IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'definitiva')) \n" +
                        "TRANS_PLACAS ORDER BY NEWID(),IDE_PLACA ASC) where IDE_DETALLE_SOLICITUD="+id_s;
      conectar();
      conexion.ejecutarSql(actualiza);
      conexion.desconectar();
      conexion = null;
    }

public void seleccionarPP(Integer id_s,Integer vehiculo,Integer servicio,Integer id_a)
{
      String actualiza ="update TRANS_DETALLE_SOLICITUD_PLACA \n" +
                        "set APROBADO_SOLICITUD= 1,IDE_APROBACION_PLACA= "+id_a+",ide_placa =\n" +
                        "(SELECT TOP 1 IDE_PLACA \n" +
                        "FROM (SELECT TOP 2 IDE_PLACA FROM TRANS_PLACAS\n" +
                        "WHERE IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')\n" +
                        "AND IDE_TIPO_VEHICULO= "+vehiculo+" \n" +
                        "AND IDE_TIPO_SERVICIO = "+servicio+"\n" +
                        "AND IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel')\n" +
                        "AND IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel')) \n" +
                        "TRANS_PLACAS ORDER BY NEWID(),IDE_PLACA ASC) where IDE_DETALLE_SOLICITUD="+id_s;
      conectar();
      conexion.ejecutarSql(actualiza);
      conexion.desconectar();
      conexion = null;
    }
//ACTUALIZACION DE ENTREGA EN SOLICITUD
public void actualizarDS(Integer id_en,Integer id_s2,String nombre, String cedula, String compro)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA=1,CEDULA_PERSONA_RETIRA='"+cedula+"',NOMBRE_PERSONA_RETIRA='"+nombre+"',NUMERO_MATRICULA = '"+compro+"',FECHA_ENTREGA_PLACA= '"+utilitario.getFechaActual()+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s2; 
        conectar();
        conexion.ejecutarSql(actualiza1);
        conexion.desconectar();
        conexion = null;
    }

public void actualizarDS1(Integer id_en,Integer id_s2,String nombre, String cedula)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA=1,CEDULA_PERSONA_RETIRA='"+cedula+"',NOMBRE_PERSONA_RETIRA='"+nombre+"',FECHA_ENTREGA_PLACA= '"+utilitario.getFechaActual()+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s2; 
        conectar();
        conexion.ejecutarSql(actualiza1);
        conexion.desconectar();
        conexion = null;
    }

 //   ACTUALIZACION DE ENTREGA EN PLACA/CAMBIO DE ESTADO DE PLACA A ENTREGADA
public void actualizarDE(Integer iden,String ruc,Integer placa)
    {
        String actualiza2 = "update TRANS_PLACAS set NOMBRE_PROPIETARIO=nombre,ide_tipo_estado = \n" +
                            "(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'entregada'),\n" +
                            "FECHA_ENTREGA_PLACA='"+utilitario.getFechaActual()+"',CEDULA_RUC_PROPIETARIO=identi,\n" +
                            "IDE_TIPO_ESTADO2 = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada')\n" +
                            "from( select CEDULA_RUC_PROPIETARIO as identi,NOMBRE_PROPIETARIO as nombre from TRANS_DETALLE_SOLICITUD_PLACA\n" +
                            "where IDE_DETALLE_SOLICITUD = "+iden+" and CEDULA_RUC_PROPIETARIO ="+ruc+")a\n" +
                            "where ide_tipo_estado <> (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO \n" +
                            "WHERE DESCRIPCION_ESTADO LIKE 'entregada') and ide_placa ="+placa; 
        conectar();
        conexion.ejecutarSql(actualiza2);
        conexion.desconectar();
        conexion = null;
    }
//ACTUALIZACION DE REQUISITOS
public void actulizarRequisito(Byte requisito,Integer detalle,Integer solicitud,Integer tipo){
        String actua ="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD SET CONFIRMAR_REQUISITO = "+requisito+" \n" 
                      +"WHERE IDE_DETALLE_REQUISITOS_SOLICITUD="+detalle+" AND IDE_TIPO_REQUISITO= "+tipo+" AND IDE_DETALLE_SOLICITUD= "+solicitud;
        conectar();
        conexion.ejecutarSql(actua);
        conexion.desconectar();
        conexion = null;
}

//
public void actuEstado(Integer id){
        String actua ="update TRANS_DETALLE_SOLICITUD_PLACA set estado = 1 where IDE_DETALLE_SOLICITUD ="+id;
        conectar();
        conexion.ejecutarSql(actua);
        conexion.desconectar();
        conexion = null;
}

public void actuEstado1(Integer id){
        String actua ="update TRANS_DETALLE_SOLICITUD_PLACA set estado = 0 where IDE_DETALLE_SOLICITUD ="+id;
        conectar();
        conexion.ejecutarSql(actua);
        conexion.desconectar();
        conexion = null;
}
//CAMBIO DE ESTADO A VERDADERO
public void actualizarEstado(Integer codigo,Byte confirma){
        String actual="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD set CONFIRMAR_REQUISITO = "+confirma+" WHERE IDE_DETALLE_SOLICITUD ="+codigo;
        conectar();
        conexion.ejecutarSql(actual);
        conexion.desconectar();
        conexion = null;
}
//CAMBIO DE ESTADO DE PLACA A ASIGNADA
public void estadoPlaca(Integer placa){
        String placa1 ="UPDATE TRANS_PLACAS\n" +
                       "set IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada'),IDE_TIPO_ESTADO2 = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada') \n" +
                       "WHERE IDE_PLACA ="+placa;
        conectar();
        conexion.ejecutarSql(placa1);
        conexion.desconectar();
        conexion = null;
}

//QUITAR ASIGNACION DE PLACA

public void quitarPlaca(Integer placa){
        String quitar ="update TRANS_PLACAS set ide_tipo_estado = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible'),\n" +
                       "ide_tipo_estado2 = null WHERE ide_placa ="+placa;
        conectar();
        conexion.ejecutarSql(quitar);
        conexion.desconectar();
        conexion = null;
}

//QUITAR PLACA DE DETALLE SOLICITUD

public void quitarDetalle(Integer detal){
        String detalle ="UPDATE TRANS_DETALLE_SOLICITUD_PLACA set aprobado_solicitud = NULL, ide_aprobacion_placa = null,ide_placa = NULL\n" +
                        "WHERE ide_detalle_solicitud ="+detal;
        conectar();
        conexion.ejecutarSql(detalle);
        conexion.desconectar();
        conexion = null;
}

public void actualFinal(Integer detal,String cedula,String nombre ){
        String detalle ="UPDATE TRANS_DETALLE_SOLICITUD_PLACA SET CEDULA_PERSONA_CAMBIO ='"+cedula+"' ,NOMBRE_PERSONA_CAMBIO ='"+nombre+"'\n" +
                        "WHERE dbo.TRANS_DETALLE_SOLICITUD_PLACA.IDE_DETALLE_SOLICITUD ="+detal;
        conectar();
        conexion.ejecutarSql(detalle);
        conexion.desconectar();
        conexion = null;
}

public void actualFinalPlaca(Integer detal,Integer entrega,String usuario ){
        String detalle ="UPDATE TRANS_PLACAS SET IDE_TIPO_ESTADO2 =(SELECT IDE_TIPO_ESTADO FROM TRANS_PLACAS WHERE IDE_PLACA ="+entrega+"),USU_ENTREGA ='"+usuario+"',FECHA_ENTREGA_FINAL ='"+utilitario.getFechaActual()+"' WHERE IDE_PLACA ="+detal;
        conectar();
        conexion.ejecutarSql(detalle);
        conexion.desconectar();
        conexion = null;
}

public void actualPlacaDes(String placa){
        String detalle ="update TRANS_PLACAS\n" +
                        "set IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO like 'asignada'),\n" +
                        "IDE_TIPO_ESTADO2 = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO like 'asignada')\n" +
                        "WHERE PLACA LIKE '"+placa+"'";
        conectar();
        conexion.ejecutarSql(detalle);
        conexion.desconectar();
        conexion = null;
}

//ELIMINAR APROBACION

public void eliminarAprobacion(Integer eliminar){
        String aprobacion ="delete TRANS_APROBACION_PLACA WHERE ide_detalle_solicitud ="+eliminar;
        conectar();
        conexion.ejecutarSql(aprobacion);
        conexion.desconectar();
        conexion = null;
}
//CREACION DE REQUISITOS
public void insertarRequisito(Integer detalle,Integer tipo,Integer servicio){

        String insertar="INSERT INTO TRANS_DETALLE_REQUISITOS_SOLICITUD (IDE_TIPO_REQUISITO,IDE_DETALLE_SOLICITUD,CONFIRMAR_REQUISITO)\n" 
                    +"SELECT r.IDE_TIPO_REQUISITO AS IDE_TIPO_REQUISITO,"+detalle+",1 FROM TRANS_TIPO_REQUISITO r\n" 
                    +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                    +"INNER JOIN trans_vehiculo_tipo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n" 
                    +"WHERE v.ide_tipo_vehiculo ="+tipo+" AND s.IDE_TIPO_SERVICIO ="+servicio;
        conectar();
        conexion.ejecutarSql(insertar);
        conexion.desconectar();
        conexion = null; 
}

//INGRESO DE APROBACION DE SOLICITUD
public void actualPlaca(Integer vehiculo,Integer servicio,Integer ingreso,Integer tipo,String placa){
        String actual="INSERT INTO TRANS_PLACAS (ide_placa,FECHA_REGISTRO_PLACA,IDE_TIPO_VEHICULO,IDE_TIPO_SERVICIO,IDE_INGRESO_PLACAS,\n" +
        "IDE_TIPO_PLACA,IDE_TIPO_ESTADO,PLACA,IDE_TIPO_PLACA2) \n" +
        "VALUES ((select MAX(ide_placa) +1 from TRANS_PLACAS)," + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +","+vehiculo+","+servicio+","+ingreso+","+tipo+",(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible'),'"+placa+"',"+tipo+")";
        conectar();
        conexion.ejecutarSql(actual);
        conexion.desconectar();
        conexion = null;
}

//INGRESO DE APROBACION DE SOLICITUD
public void asigancionPlaca(String usuario){
        String actual="INSERT INTO TRANS_APROBACION_PLACA (FECHA_APROBACION,APROBADO,USU_APROBACION)\n" +
                      "VALUES (" + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",1,'"+usuario+"')";
        conectar();
        conexion.ejecutarSql(actual);
        conexion.desconectar();
        conexion = null;
}

//ENTREGA DE PLACAS
public void entregaPlaca(String ci,String nom,String cedula,String nome,String coment,Integer soli){
        String actual="INSERT INTO TRANS_ENTREGA_PLACA (FECHA_ENTREGA_PLACA,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,CEDULA_PERSONA_RETIRA,NOMBRE_PERSONA_RETIRA,USU_ENTREGA,IDE_DETALLE_SOLICITUD)\n" +
                      "VALUES (" + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",'"+ci+"','"+nom+"','"+cedula+"','"+nome+"','"+coment+"',"+soli+")";
        conectar();
        conexion.ejecutarSql(actual);
        conexion.desconectar();
        conexion = null;
}

//GUARDAR AUDITORIA DE PLACAS DESASIGNADAS
public void guardarhistorial(Integer ide,String ruc,Integer detalle,String cedula,String nome,Integer servicio,String nomb, Integer vehiculo,String factura,String coment,String placa){
        String actual="INSERT INTO TRANS_QUITAR_ASIGNACION (IDE_SOLICITUD,RUC_CEDULA_EMPRESA,NOMBRE_EMPRESA,IDE_DETALLE,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,\n" +
                      "TIPO_SERVICIO,TIPO_VEHICULO,FACTURA,COMENTARIO,PLACA,FECHA_LIBERADO)VALUES("+ide+",'"+ruc+"','"+nome+"',"+detalle+",'"+cedula+"','"+nomb+"',"+servicio+","+vehiculo+",'"+factura+"','"+coment+"','"+placa+"'," + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +")";
        conectar();
        conexion.ejecutarSql(actual);
        conexion.desconectar();
        conexion = null;
}

//devoluciones
public void devolucion(String quien,String vehiculo,String servicio,String placa,String comentario,String fecha,String usu,String acta){
    String devolver ="insert into trans_devolucion (FECHA_DEVOLUCION,QUIEN_SOLICITA,DEV_VEHICULO,DEV_SERVICIO,PLACA,COMENTARIO,FECHA_REGISTRO,NUMERO_ACTA) values "
            + "(" + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",'"+quien+"','"+vehiculo+"','"+servicio+"','"+placa+"','"+comentario+"','"+fecha+"','"+usu+"','"+acta+"')";
    conectar();
    conexion.ejecutarSql(devolver);
    conexion.desconectar();
    conexion = null;
}

//DUPLICAR
public void nuevo(Integer codigo,Integer id, String cedu,String nom,Integer cod,String usu,String nomg){
    String devolver ="insert into TRANS_SOLICITUD_PLACA \n" +
                    "(IDE_SOLICITUD_PLACA,IDE_GESTOR,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA,IDE_TIPO_SOLICTUD,FECHA_SOLICITUD,USU_SOLICITUD,NOMBRE_GESTOR)\n" +
                    "values("+codigo+","+id+",'"+cedu+"','"+nom+"',"+cod+","+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+usu+"','"+nomg+"')";
    conectar();
    conexion.ejecutarSql(devolver);
    conexion.desconectar();
    conexion = null;
}

public void DataNew(Integer codigo, Integer ser,Integer veh,Integer soli,String cedula, String nomb,String fact){
    String devolver ="INSERT INTO TRANS_DETALLE_SOLICITUD_PLACA (IDE_DETALLE_SOLICITUD,IDE_SOLICITUD_PLACA,IDE_TIPO_SERVICIO,\n" +
"IDE_TIPO_VEHICULO,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,NUMERO_RVMO,TIPO_SERVICIO,TIPO_VEHICULO,ESTADO)\n" +
"VALUES ("+codigo+","+soli+","+ser+","+veh+",'"+cedula+"','"+nomb+"','"+fact+"',"+ser+","+veh+",'1')";
    conectar();
    conexion.ejecutarSql(devolver);
    conexion.desconectar();
    conexion = null;
}

public void InsHistoPlaca(String placa, String comentario,String usu){
    String devolver ="insert into TRANS_PLACAS_REGISTRADAS_ANT(FECHA_APROBACION,PLACA,COMENTARIO,USUARIO) \n" +
                    "values ("+utilitario.getFormatoFechaSQL(utilitario.getFechaActual())+",'"+placa+"','"+comentario+"','"+usu+"')";
    conectar();
    conexion.ejecutarSql(devolver);
    conexion.desconectar();
    conexion = null;
}

//BORRA REGISTROS DE DETALLE REQUISITOS
public void borrarRequisito(Integer requisito){
        String requisit="DELETE FROM TRANS_DETALLE_REQUISITOS_SOLICITUD WHERE IDE_DETALLE_SOLICITUD ="+requisito;
        conectar();
        conexion.ejecutarSql(requisit);
        conexion.desconectar();
        conexion = null;
}
//DUPLICAR
public void deleteRequisito(Integer requisito){
        String requisit="DELETE FROM TRANS_DETALLE_REQUISITOS_SOLICITUD WHERE IDE_DETALLE_SOLICITUD ="+requisito;
        conectar();
        conexion.ejecutarSql(requisit);
        conexion.desconectar();
        conexion = null;
}

public void deleteDetalle(Integer requisito){
        String requisit="DELETE FROM TRANS_DETALLE_SOLICITUD_PLACA WHERE IDE_DETALLE_SOLICITUD ="+requisito;
        conectar();
        conexion.ejecutarSql(requisit);
        conexion.desconectar();
        conexion = null;
}

//RECUPERACION DE INFORMACION
   public TablaGenerica getGestor(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT * FROM TRANS_COMERCIAL_AUTOMOTORES WHERE TRANS_COMERCIAL_AUTOMOTORES.RUC_EMPRESA ='" + iden+ "'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
   
   //DUPLICAR ID
    public String getGestorNum() {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        tab_consulta.setConexion(conexion);
        tab_consulta.setSql("SELECT TOP 1 IDE_SOLICITUD_PLACA +1 as siguiente,0 as id  from TRANS_SOLICITUD_PLACA order by IDE_SOLICITUD_PLACA DESC");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("siguiente");
        return ValorMax;
    }
   
    public String getGestorNumDe() {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        String ValorFinal;
        TablaGenerica tab_persona = new TablaGenerica();
        conectar();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT TOP 1 IDE_DETALLE_SOLICITUD +1 as codigo,0 as id1  from TRANS_DETALLE_SOLICITUD_PLACA order by IDE_DETALLE_SOLICITUD DESC");
        tab_persona.ejecutarSql();
        ValorFinal = tab_persona.getValor("codigo");
        return ValorFinal;
    }
    
    //DUPLICAR
   public TablaGenerica getNuevoReg (Integer cod){
       conectar();
       TablaGenerica tab_nuevo = new TablaGenerica();
       tab_nuevo.setConexion(conexion);
       tab_nuevo.setSql("SELECT p.IDE_SOLICITUD_PLACA,\n" +
                        "p.IDE_GESTOR,p.CEDULA_RUC_EMPRESA,\n" +
                        "p.NOMBRE_EMPRESA,p.IDE_TIPO_SOLICTUD,\n" +
                        "p.FECHA_SOLICITUD,p.USU_SOLICITUD,\n" +
                        "p.NOMBRE_GESTOR,s.IDE_DETALLE_SOLICITUD,\n" +
                        "s.IDE_TIPO_SERVICIO,s.IDE_TIPO_VEHICULO,\n" +
                        "s.IDE_SOLICITUD_PLACA,s.CEDULA_RUC_PROPIETARIO,\n" +
                        "s.NOMBRE_PROPIETARIO,s.NUMERO_RVMO,\n" +
                        "s.TIPO_VEHICULO,s.TIPO_SERVICIO,\n" +
                        "s.ESTADO\n" +
                        "FROM dbo.TRANS_SOLICITUD_PLACA p,dbo.TRANS_DETALLE_SOLICITUD_PLACA s \n" +
                        "where s.IDE_SOLICITUD_PLACA = p.IDE_SOLICITUD_PLACA and\n" +
                        "s.IDE_DETALLE_SOLICITUD ="+cod+" and s.estado = 0");
       
    tab_nuevo.ejecutarSql();
    conexion.desconectar();
    conexion = null;
    return tab_nuevo;
   }
   
   public TablaGenerica getRecuperar(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT CEDULA_GESTOR,NOMBRE_GESTOR,IDE_GESTOR FROM TRANS_GESTOR WHERE ESTADO = 1 AND CEDULA_GESTOR='" + iden+ "'");
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    } 
   
   public TablaGenerica getGestor1(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT CEDULA_GESTOR,NOMBRE_GESTOR,IDE_GESTOR FROM TRANS_GESTOR WHERE ESTADO = 1 AND CEDULA_GESTOR='" + iden+ "'");
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    } 
   
      public TablaGenerica getGestor2(Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT CEDULA_GESTOR,NOMBRE_GESTOR,IDE_GESTOR FROM TRANS_GESTOR WHERE ESTADO = 1 AND IDE_GESTOR='" + iden+ "'");
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    } 

  public TablaGenerica getAprobacion(Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_APROBACION_PLACA,FECHA_APROBACION,APROBADO,USU_APROBACION,IDE_DETALLE_SOLICITUD FROM TRANS_APROBACION_PLACA\n" +
                            "WHERE IDE_DETALLE_SOLICITUD ="+iden+"order by IDE_APROBACION_PLACA ASC");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }       
  public TablaGenerica getREntrega(Integer propie) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
      conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT DISTINCT p.IDE_DETALLE_SOLICITUD,p.CEDULA_RUC_PROPIETARIO,\n" +
                            "p.NOMBRE_PROPIETARIO,s.IDE_SOLICITUD_PLACA,s.FECHA_SOLICITUD,\n" +
                            "s.NOMBRE_EMPRESA,s.NOMBRE_GESTOR,s.USU_SOLICITUD,\n" +
                            "g.DESCRIPCION_SOLICITUD,v.descripcion_vehiculo,r.DESCRIPCION_SERVICIO,\n" +
                            "a.FECHA_APROBACION,a.USU_APROBACION,t.PLACA,\n" +
                            "t.IDE_PLACA,r.IDE_TIPO_SERVICIO,v.ide_tipo_vehiculo,\n" +
                            "a.IDE_APROBACION_PLACA,p.NUMERO_RVMO,t.FECHA_ENTREGA_PLACA,\n" +
                            "k.DESCRIPCION_ESTADO,x.NOMBRE_PERSONA_RETIRA,x.USU_ENTREGA,\n" +
                            "z.DESCRIPCION_PLACA\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA AS p ,\n" +
                            "dbo.TRANS_SOLICITUD_PLACA AS s ,\n" +
                            "dbo.TRANS_TIPO_SOLICTUD AS g ,\n" +
                            "dbo.trans_vehiculo_tipo AS v ,\n" +
                            "dbo.TRANS_TIPO_SERVICIO AS r ,\n" +
                            "dbo.TRANS_APROBACION_PLACA AS a ,\n" +
                            "dbo.TRANS_PLACAS AS t ,\n" +
                            "dbo.TRANS_TIPO_ESTADO AS k ,\n" +
                            "dbo.TRANS_ENTREGA_PLACA AS x,\n" +
                            "dbo.TRANS_TIPO_PLACA z \n" +
                            "WHERE\n" +
                            "p.IDE_SOLICITUD_PLACA = s.IDE_SOLICITUD_PLACA AND\n" +
                            "s.IDE_TIPO_SOLICTUD = g.IDE_TIPO_SOLICTUD AND\n" +
                            "p.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" +
                            "r.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" +
                            "p.IDE_TIPO_SERVICIO = r.IDE_TIPO_SERVICIO AND\n" +
                            "p.IDE_APROBACION_PLACA = a.IDE_APROBACION_PLACA AND\n" +
                            "t.IDE_TIPO_SERVICIO = r.IDE_TIPO_SERVICIO AND\n" +
                            "p.IDE_PLACA = t.IDE_PLACA AND\n" +
                            "t.IDE_TIPO_ESTADO = k.IDE_TIPO_ESTADO AND\n" +
                            "p.IDE_ENTREGA_PLACA = x.IDE_ENTREGA_PLACA AND\n" +
                            "t.IDE_TIPO_PLACA = z.IDE_TIPO_PLACA AND p.IDE_DETALLE_SOLICITUD ="+propie);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;

        return tab_persona;
    }

    public TablaGenerica getEntrega(Integer propie) {
        //Busca a una empresa en la tabla maestra_ruc por ruc 
      conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT DISTINCT p.IDE_DETALLE_SOLICITUD,p.CEDULA_RUC_PROPIETARIO,p.NOMBRE_PROPIETARIO,\n" +
                            "s.IDE_SOLICITUD_PLACA,s.FECHA_SOLICITUD,s.NOMBRE_EMPRESA,s.NOMBRE_GESTOR,\n" +
                            "s.USU_SOLICITUD,g.DESCRIPCION_SOLICITUD,v.descripcion_vehiculo,\n" +
                            "r.DESCRIPCION_SERVICIO,a.FECHA_APROBACION,a.USU_APROBACION,\n" +
                            "t.PLACA,t.IDE_PLACA,r.IDE_TIPO_SERVICIO,\n" +
                            "v.ide_tipo_vehiculo,a.IDE_APROBACION_PLACA,p.NUMERO_RVMO,\n" +
                            "t.FECHA_ENTREGA_PLACA,k.DESCRIPCION_ESTADO\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA AS p ,\n" +
                            "dbo.TRANS_SOLICITUD_PLACA AS s ,\n" +
                            "dbo.TRANS_TIPO_SOLICTUD AS g ,\n" +
                            "dbo.trans_vehiculo_tipo AS v ,\n" +
                            "dbo.TRANS_TIPO_SERVICIO AS r ,\n" +
                            "dbo.TRANS_APROBACION_PLACA AS a ,\n" +
                            "dbo.TRANS_PLACAS AS t ,\n" +
                            "dbo.TRANS_TIPO_ESTADO AS k\n" +
                            "WHERE\n" +
                            "p.IDE_SOLICITUD_PLACA = s.IDE_SOLICITUD_PLACA AND\n" +
                            "s.IDE_TIPO_SOLICTUD = g.IDE_TIPO_SOLICTUD AND\n" +
                            "p.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" +
                            "r.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" +
                            "p.IDE_TIPO_SERVICIO = r.IDE_TIPO_SERVICIO AND\n" +
                            "p.IDE_APROBACION_PLACA = a.IDE_APROBACION_PLACA AND\n" +
                            "t.IDE_TIPO_SERVICIO = r.IDE_TIPO_SERVICIO AND\n" +
                            "p.IDE_PLACA = t.IDE_PLACA AND\n" +
                            "t.IDE_TIPO_ESTADO = k.IDE_TIPO_ESTADO AND p.IDE_DETALLE_SOLICITUD ="+propie);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;

        return tab_persona;
    }
  
  public TablaGenerica getIDPlaca(Integer aprobado, Integer solicitud) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_DETALLE_SOLICITUD,IDE_PLACA,IDE_TIPO_VEHICULO,IDE_TIPO_SERVICIO,IDE_APROBACION_PLACA,\n" +
                            "IDE_SOLICITUD_PLACA,ENTREGADA_PLACA FROM TRANS_DETALLE_SOLICITUD_PLACA\n" +
                            "WHERE IDE_SOLICITUD_PLACA ="+solicitud+" and IDE_APROBACION_PLACA ="+aprobado);
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

  public TablaGenerica getIDGestor(Integer gestor) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO FROM TRANS_GESTOR WHERE  IDE_GESTOR="+gestor);
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

 public TablaGenerica getIDActa(Integer acta) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_INGRESO_PLACAS,FECHA_ENVIO_ACTA, FECHA_REGISTRO_ACTA, ANO,\n" +
                            "NUMERO_ACTA,ENTREGADO_ACTA,RECIBIDO_ACTA,USU_INGRESO FROM TRANS_INGRESOS_PLACAS WHERE IDE_INGRESO_PLACAS ="+acta);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

      public TablaGenerica getIDSolicitud(Integer soli) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_SOLICITUD_PLACA,FECHA_SOLICITUD,CEDULA_RUC_EMPRESA,NOMBRE_EMPRESA FROM TRANS_SOLICITUD_PLACA WHERE IDE_SOLICITUD_PLACA ="+soli);
        tab_persona.ejecutarSql();
        
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getIDEntrega(Integer solii) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_ENTREGA_PLACA,IDE_DETALLE_SOLICITUD FROM TRANS_ENTREGA_PLACA WHERE IDE_DETALLE_SOLICITUD ="+solii);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }      

public TablaGenerica placasDis(Integer veh,Integer ser) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT COUNT(*) as numero, v.DESCRIPCION_VEHICULO, s.DESCRIPCION_SERVICIO,\n" +
                            "p.PLACA\n" +
                            "FROM TRANS_PLACAS p,TRANS_VEHICULO_TIPO v, TRANS_TIPO_SERVICIO s\n" +
                            "WHERE  p.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND s.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND \n" +
                            "p.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND p.IDE_TIPO_VEHICULO = "+veh+" AND p.IDE_TIPO_SERVICIO = "+ser+" AND\n" +
                            "p.IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada')\n" +
                            "GROUP BY v.DESCRIPCION_VEHICULO, s.DESCRIPCION_SERVICIO,\n" +
                            "p.PLACA");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    } 

 public TablaGenerica placasAsigna(Integer veh,Integer ser,String cedu) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                            "v.DESCRIPCION_VEHICULO,\n" +
                            "s.DESCRIPCION_SERVICIO,\n" +
                            "p.PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO\n" +
                            "FROM\n" +
                            "dbo.TRANS_PLACAS AS p ,\n" +
                            "dbo.TRANS_VEHICULO_TIPO AS v ,\n" +
                            "dbo.TRANS_TIPO_SERVICIO AS s,\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d \n" +
                            "WHERE\n" +
                            "p.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND\n" +
                            "s.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND\n" +
                            "p.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                            "d.IDE_PLACA = p.IDE_PLACA AND \n" +
                            "d.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND\n" +
                            "p.IDE_TIPO_VEHICULO = 4 AND\n" +
                            "p.IDE_TIPO_SERVICIO = 39 AND\n" +
                            "p.IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada') AND\n" +
                            "d.CEDULA_RUC_PROPIETARIO LIKE '1717632366'\n" +
                            "GROUP BY\n" +
                            "v.DESCRIPCION_VEHICULO,\n" +
                            "s.DESCRIPCION_SERVICIO,\n" +
                            "p.PLACA,\n" +
                            "d.CEDULA_RUC_PROPIETARIO");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    } 

public TablaGenerica getDevEmpresa(Integer solii) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA,RUC_EMPRESA FROM TRANS_COMERCIAL_AUTOMOTORES WHERE IDE_COMERCIAL_AUTOMOTORES ="+solii);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getValidarPlaca(Integer solii, Integer tipo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO,v.IDE_TIPO_VEHICULO,v.DESCRIPCION_VEHICULO\n" +
                            "FROM dbo.TRANS_TIPO_SERVICIO s,dbo.TRANS_VEHICULO_TIPO v \n" +
                            "WHERE s.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND s.IDE_TIPO_SERVICIO = "+solii+" and v.IDE_TIPO_VEHICULO ="+tipo);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getUsuario(Integer usu) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_USUA,NICK_USUA as usuarioin,IDE_PERF FROM SIS_USUARIO WHERE IDE_USUA ="+usu);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getPlacaActual(Integer placas) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA WHERE IDE_TIPO_PLACA ="+placas);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getPlacaActualEli(String placas) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                            "s.IDE_SOLICITUD_PLACA,\n" +
                            "s.CEDULA_RUC_EMPRESA,\n" +
                            "s.NOMBRE_EMPRESA,\n" +
                            "d.IDE_DETALLE_SOLICITUD,\n" +
                            "d.CEDULA_RUC_PROPIETARIO,\n" +
                            "d.NOMBRE_PROPIETARIO,\n" +
                            "d.IDE_TIPO_SERVICIO,\n" +
                            "d.IDE_TIPO_VEHICULO,\n" +
                            "p.IDE_PLACA,\n" +
                            "d.NUMERO_RVMO\n" +
                            "FROM\n" +
                            "dbo.TRANS_DETALLE_SOLICITUD_PLACA d ,\n" +
                            "dbo.TRANS_SOLICITUD_PLACA s,\n" +
                            "dbo.TRANS_PLACAS p\n" +
                            "where d.IDE_SOLICITUD_PLACA = s.IDE_SOLICITUD_PLACA and \n" +
                            "d.IDE_PLACA = p.IDE_PLACA and\n" +
                            "p.PLACA like '"+placas+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getEstadoPlaca(Integer estados) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO WHERE IDE_TIPO_VEHICULO ="+estados);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getVehiculo_Serv(Integer vehiculo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_TIPO_VEHICULO,DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO where IDE_TIPO_VEHICULO="+vehiculo);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

public TablaGenerica getServicio_Veh(Integer servicio,Integer vehiculo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO\n" +
                            "FROM TRANS_TIPO_SERVICIO WHERE IDE_TIPO_VEHICULO ="+vehiculo+" and IDE_TIPO_SERVICIO ="+servicio);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

 public String getTipoDisponibleF() {
        conectar();
        String conteo;
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("select 0 as id,count(*) as comparar \n" +
                            "from TRANS_PLACAS \n" +
                            "where IDE_TIPO_PLACA = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA\n" +
                            "WHERE DESCRIPCION_PLACA LIKE 'definitiva') \n" +
                            "AND IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')");
        tab_persona.ejecutarSql();
        conteo = tab_persona.getValor("comparar");
        return conteo;
    }

 public String getTipoDisponibleFP(Integer ve,Integer ser) {
        conectar();
        String conteo;
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("select 0 as id,count(*) as comparar from TRANS_PLACAS \n" +
                            "where IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA\n" +
                            "WHERE DESCRIPCION_PLACA LIKE 'definitiva') AND \n" +
                            "IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible') AND\n" +
                            "IDE_TIPO_VEHICULO = "+ve+" and IDE_TIPO_SERVICIO ="+ser);
                                    tab_persona.ejecutarSql();
        conteo = tab_persona.getValor("comparar");
        return conteo;
    }

public String getTipoDisponiblePP(Integer ve,Integer ser) {
        conectar();
        String conteo;
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("select 1 as id,count(*) as comparar from TRANS_PLACAS \n" +
                            "where IDE_TIPO_PLACA2 = (SELECT IDE_TIPO_PLACA FROM TRANS_TIPO_PLACA WHERE DESCRIPCION_PLACA LIKE 'papel') \n" +
                            "AND IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible') AND\n" +
                            "IDE_TIPO_VEHICULO = "+ve+" and IDE_TIPO_SERVICIO ="+ser);
        tab_persona.ejecutarSql();
        conteo = tab_persona.getValor("comparar");
        return conteo;
    }

public TablaGenerica getDevolucion(Integer placas) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT p.IDE_PLACA,\n" +
                            "p.IDE_TIPO_SERVICIO,p.PLACA,\n" +
                            "i.IDE_INGRESO_PLACAS,i.FECHA_REGISTRO_ACTA,\n" +
                            "i.NUMERO_ACTA,v.DESCRIPCION_VEHICULO,\n" +
                            "s.DESCRIPCION_SERVICIO\n" +
                            "FROM dbo.TRANS_PLACAS AS p ,\n" +
                            "dbo.TRANS_INGRESOS_PLACAS AS i ,\n" +
                            "dbo.TRANS_VEHICULO_TIPO AS v,\n" +
                            "dbo.TRANS_TIPO_SERVICIO s \n" +
                            "WHERE p.IDE_INGRESO_PLACAS = i.IDE_INGRESO_PLACAS AND\n" +
                            "p.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND\n" +
                            "p.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                            "p.IDE_PLACA ="+placas);
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

    public TablaGenerica getPlacaBus(String placas) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                            "CODIGO_ENTREGA,\n" +
                            "CEDULA_QUIEN_RETIRA,\n" +
                            "NOMBRE_QUIEN_RETIRA,\n" +
                            "FECHA_RETIRO,\n" +
                            "PLACA,\n" +
                            "PARTICULAR_EMPRESA\n" +
                            "FROM\n" +
                            "TRANS_PLACAS_PENDIENTES\n" +
                            "where placa like '"+placas+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }

    public TablaGenerica getPlacaBusc(String placas) {
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT\n" +
                            "CODIGO_PLACA,PLACA\n" +
                            "FROM\n" +
                            "TRANS_PLACAS_ANTIGUAS\n" +
                            "WHERE placa like '"+placas+"'");
        tab_persona.ejecutarSql();
        conexion.desconectar();
        conexion = null;
        return tab_persona;
    }
    
    
 private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
            
        }
    }
 
 public TablaGenerica Funcionario(String empleado){
        conec();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conec();
        tab_funcionario.setConexion(con_postgres);
        tab_funcionario.setSql("SELECT cedula_pass,nombres FROM srh_empleado WHERE cedula_pass LIKE '"+empleado+"'");
        tab_funcionario.ejecutarSql();
        con_postgres.desconectar();
        con_postgres = null;
        return tab_funcionario;
        
 }
 
 private void conec() {
        if (con_postgres == null) {
            con_postgres = new Conexion();
            con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
            con_postgres.NOMBRE_MARCA_BASE = "postgres";
        }
     }
}
