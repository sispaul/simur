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
public class servicioPlaca {
private Conexion conexion;
private Utilitario utilitario = new Utilitario();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

//ASIGNACION DE ESTADO A PLACAS NUEVAS
public void placaNew()
{
      String nueva ="UPDATE TRANS_PLACA\n" +
                    "set IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')\n" +
                    "WHERE IDE_TIPO_ESTADO is null";
            conectar();
            conexion.ejecutarSql(nueva);
    }

//ACTUALIZACION DE APROBACION EN SOLICITUD y ASIGNACION AUTOMATICA DE PLACA
public void seleccionarP(Integer id_s,Integer vehiculo,Integer servicio,Integer id_a)
{
      String actualiza ="update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                         +"set APROBADO_SOLICITUD= 1,IDE_APROBACION_PLACA= "+id_a+",ide_placa = (SELECT TOP 1 IDE_PLACA FROM TRANS_PLACA\n" +
                        "WHERE IDE_TIPO_ESTADO =(SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')\n" +
                        "AND IDE_TIPO_VEHICULO= "+vehiculo+" AND IDE_TIPO_SERVICIO = "+servicio+" ORDER BY IDE_PLACA ASC) where IDE_DETALLE_SOLICITUD="+id_s;
            conectar();
            conexion.ejecutarSql(actualiza);
    }

//ACTUALIZACION DE ENTREGA EN SOLICITUD
public void actualizarDS(Integer id_en,Integer id_s2)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA=1, FECHA_ENTREGA_PLACA= '"+utilitario.getFechaActual()+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s2; 
            conectar();
            conexion.ejecutarSql(actualiza1);
    }
 //   ACTUALIZACION DE ENTREGA EN PLACA/CAMBIO DE ESTADO DE PLACA A ENTREGADA
public void actualizarDE(Integer iden,String ruc,Integer placa)
    {
        String actualiza2 = "update TRANS_PLACA \n" 
                            +"set NOMBRE_PROPIETARIO=nombre,ide_tipo_estado = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'entregada'),FECHA_ENTREGA_PLACA='"+utilitario.getFechaActual()+"',CEDULA_RUC_PROPIETARIO=identi\n" 
                            +"from( select CEDULA_RUC_PROPIETARIO as identi,NOMBRE_PROPIETARIO as nombre from TRANS_DETALLE_SOLICITUD_PLACA\n" 
                            +"where IDE_DETALLE_SOLICITUD = "+iden+" and CEDULA_RUC_PROPIETARIO ="+ruc+")a\n" 
                            +"where ide_tipo_estado <> (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO \n" +
                            "WHERE DESCRIPCION_ESTADO LIKE 'entregada') and ide_placa ="+placa; 
            conectar();
            conexion.ejecutarSql(actualiza2);
    }
//ACTUALIZACION DE REQUISITOS
public void actulizarRequisito(Byte requisito,Integer detalle,Integer solicitud,Integer tipo){
    String actua ="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD SET CONFIRMAR_REQUISITO = "+requisito+" \n" 
                    +"WHERE IDE_DETALLE_REQUISITOS_SOLICITUD="+detalle+" AND IDE_TIPO_REQUISITO= "+tipo+" AND IDE_DETALLE_SOLICITUD= "+solicitud;
    conectar();
    conexion.ejecutarSql(actua);
//    conexion.desconectar();
}
//CAMBIO DE ESTADO A VERDADERO
public void actualizarEstado(Integer codigo,Byte confirma){
    String actual="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD set CONFIRMAR_REQUISITO = "+confirma+" WHERE IDE_DETALLE_SOLICITUD ="+codigo;
    conectar();
    conexion.ejecutarSql(actual);
//    conexion.desconectar();
}
//CAMBIO DE ESTADO DE PLACA A ASIGNADA
public void estadoPlaca(Integer placa){
        String placa1 ="UPDATE TRANS_PLACA\n" +
                      "set IDE_TIPO_ESTADO = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'asignada')\n" +
                      "WHERE IDE_PLACA ="+placa;
    conectar();
    conexion.ejecutarSql(placa1);
}

//QUITAR ASIGNACION DE PLACA

public void quitarPlaca(Integer placa){
    String quitar ="update TRANS_PLACA set ide_tipo_estado = (SELECT IDE_TIPO_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO LIKE 'disponible')\n" +
                    "WHERE ide_placa ="+placa;
    conectar();
    conexion.ejecutarSql(quitar);
}

//QUITAR PLACA DE DETALLE SOLICITUD

public void quitarDetalle(Integer detal){
    String detalle ="UPDATE TRANS_DETALLE_SOLICITUD_PLACA set aprobado_solicitud = NULL, ide_aprobacion_placa = null,ide_placa = NULL\n" +
                    "WHERE ide_detalle_solicitud ="+detal;
    conectar();
    conexion.ejecutarSql(detalle);
}

//ELIMINAR APROBACION

public void eliminarAprobacion(Integer eliminar){
    String aprobacion ="delete table TRANS_APROBACION_PLACA WHERE ide_detalle_solicitud ="+eliminar;
    conectar();
    conexion.ejecutarSql(aprobacion);
}
//CREACION DE REQUISITOS
public void insertarRequisito(Integer detalle,Integer tipo,Integer servicio){
    String insertar="INSERT INTO TRANS_DETALLE_REQUISITOS_SOLICITUD (IDE_TIPO_REQUISITO,IDE_DETALLE_SOLICITUD)\n" 
                    +"SELECT r.IDE_TIPO_REQUISITO AS IDE_TIPO_REQUISITO,"+detalle+" FROM TRANS_TIPO_REQUISITO r\n" 
                    +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                    +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n" 
                    +"WHERE v.ide_tipo_vehiculo ="+tipo+" AND s.IDE_TIPO_SERVICIO ="+servicio;
            conectar();
            conexion.ejecutarSql(insertar);
//            conexion.desconectar();
}

//INGRESO DE APROBACION DE SOLICITUD
public void asigancionPlaca(String usuario){
    String actual="INSERT INTO TRANS_APROBACION_PLACA (FECHA_APROBACION,APROBADO,USU_APROBACION)\n" +
                    "VALUES (" + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) +",1,'"+usuario+"')";
    conectar();
    conexion.ejecutarSql(actual);
//    conexion.desconectar();
}

//GUARDAR AUDITORIA DE PLACAS DESASIGNADAS
public void guardarhistorial(Integer ide,String ruc,Integer detalle,String cedula,String nome,Integer servicio,String nomb, Integer vehiculo,Integer factura,String coment){
    String actual="INSERT INTO TRANS_QUITAR_ASIGNACION (IDE_SOLICITUD,RUC_CEDULA_EMPRESA,NOMBRE_EMPRESA,IDE_DETALLE,CEDULA_RUC_PROPIETARIO,NOMBRE_PROPIETARIO,\n" +
                   "TIPO_SERVICIO,TIPO_VEHICULO,FACTURA,COMENTARIO)VALUES("+ide+",'"+ruc+"','"+nome+"',"+detalle+",'"+cedula+"','"+nomb+"',"+servicio+","+vehiculo+","+factura+",'"+coment+"')";
    conectar();
    conexion.ejecutarSql(actual);
//    conexion.desconectar();
}
//RECUPERACION DE INFORMACION
   public TablaGenerica getGestor(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT * FROM TRANS_COMERCIAL_AUTOMOTORES WHERE TRANS_COMERCIAL_AUTOMOTORES.RUC_EMPRESA ='" + iden+ "'");
        tab_persona.ejecutarSql();
//        conexion.desconectar();
        return tab_persona;
    }
    
      public TablaGenerica getGestor1(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT CEDULA_GESTOR,NOMBRE_GESTOR,IDE_GESTOR FROM TRANS_GESTOR WHERE CEDULA_GESTOR='" + iden+ "'");
        tab_persona.ejecutarSql();
        
//        conexion.desconectar();
        return tab_persona;
    } 

  public TablaGenerica getAprobacion(String fecha,Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT top 1 IDE_APROBACION_PLACA,FECHA_APROBACION,APROBADO,USU_APROBACION,IDE_DETALLE_SOLICITUD\n" +
                            "FROM TRANS_APROBACION_PLACA\n" +
                            "WHERE FECHA_APROBACION ="+fecha+" AND IDE_DETALLE_SOLICITUD ="+iden+"order by IDE_APROBACION_PLACA ASC");
        tab_persona.ejecutarSql();
        
//        conexion.desconectar();
        return tab_persona;
    }       
      public TablaGenerica getEntrega(Integer propie) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conectar();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(conexion);
        tab_persona.setSql("SELECT DISTINCT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,p.PLACA,v.des_tipo_vehiculo,d.IDE_PLACA,\n" +
                            "p.IDE_TIPO_VEHICULO,p.IDE_TIPO_SERVICIO,d.NUMERO_FACTURA\n" +
                            "FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA AS d ,dbo.TRANS_PLACA AS p ,dbo.trans_tipo_vehiculo v\n" +
                            "WHERE d.IDE_PLACA = p.IDE_PLACA ANDd.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND d.IDE_DETALLE_SOLICITUD ="+propie);
        tab_persona.ejecutarSql();
        
//        conexion.desconectar();
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
        
//        conexion.desconectar();
        return tab_persona;
    }
      
 private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
            
        }
    }
}
