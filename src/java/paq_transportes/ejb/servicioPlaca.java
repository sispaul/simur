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

//ACTUALIZACION DE APROBACION EN SOLICITUD y ASIGNACION AUTOMATICA DE PLACA
public void seleccionarP(Integer id_s,Integer vehiculo,Integer servicio,Byte aprobado,Integer id_a)
{
      String actualiza ="update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set APROBADO_SOLICITUD="+aprobado+",IDE_APROBACION_PLACA= "+id_a+",ide_placa = (SELECT TOP 1 IDE_PLACA FROM TRANS_PLACA\n" 
                            +"WHERE IDE_TIPO_ESTADO =3 AND IDE_TIPO_VEHICULO= "+vehiculo+" AND IDE_TIPO_SERVICIO = "+servicio+" AND IDE_TIPO_PLACA = 1\n" 
                            +"ORDER BY PLACA ASC) where IDE_DETALLE_SOLICITUD="+id_s;
            conectar();
            conexion.ejecutarSql(actualiza);
            conexion.desconectar();
    }

//ACTUALIZACION DE ENTREGA EN SOLICITUD

public void actualizarDS(Integer id_en,Integer id_s2,Byte aprob_en)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA="+aprob_en+", FECHA_ENTREGA_PLACA= '"+utilitario.getFechaActual()+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s2; 
            conectar();
            conexion.ejecutarSql(actualiza1);
            conexion.desconectar();
    }
 //   ACTUALIZACION DE ENTREGA EN PLACA
public void actualizarDE(Integer iden,String ruc,Integer placa)
    {
        String actualiza2 = "update TRANS_PLACA \n" 
                            +"set NOMBRE_PROPIETARIO=nombre,ide_tipo_estado = 2,FECHA_ENTREGA_PLACA='"+utilitario.getFechaActual()+"',CEDULA_RUC_PROPIETARIO=identi\n" 
                            +"from( select CEDULA_RUC_PROPIETARIO as identi,NOMBRE_PROPIETARIO as nombre from TRANS_DETALLE_SOLICITUD_PLACA\n" 
                            +"where IDE_DETALLE_SOLICITUD = "+iden+" and CEDULA_RUC_PROPIETARIO ="+ruc+")a\n" 
                            +"where placa ide_tipo_estado <> 2 and ide_placa ="+placa; 
            conectar();
            conexion.ejecutarSql(actualiza2);
            conexion.desconectar();
    }

public void insertarRequisito(Integer detalle,Integer tipo,Integer servicio){
    String insertar="INSERT INTO TRANS_DETALLE_REQUISITOS_SOLICITUD (IDE_TIPO_REQUISITO,IDE_DETALLE_SOLICITUD)\n" 
                    +"SELECT r.IDE_TIPO_REQUISITO AS IDE_TIPO_REQUISITO,"+detalle+" FROM TRANS_TIPO_REQUISITO r\n" 
                    +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                    +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n" 
                    +"WHERE v.ide_tipo_vehiculo ="+tipo+" AND s.IDE_TIPO_SERVICIO ="+servicio;
//   String insertar="INSERT INTO TRANS_DETALLE_REQUISITOS_SOLICITUD (IDE_TIPO_REQUISITO)\n" 
//                    +"SELECT r.IDE_TIPO_REQUISITO AS IDE_TIPO_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
//                    +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
//                    +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n" 
//                    +"WHERE v.ide_tipo_vehiculo ="+tipo+" AND s.IDE_TIPO_SERVICIO ="+servicio;
            conectar();
            conexion.ejecutarSql(insertar);
            conexion.desconectar();
}

public void actulizarRequisito(Byte requisito,Integer detalle,Integer solicitud,Integer tipo){
//    String actua ="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD \n" 
//                    +"SET CONFIRMAR_REQUISITO = "+requisito+" \n" 
//                    +"FROM (SELECT IDE_DETALLE_REQUISITOS_SOLICITUD as id, IDE_TIPO_REQUISITO as requisito, IDE_DETALLE_SOLICITUD as detalle\n" 
//                    +"FROM TRANS_DETALLE_REQUISITOS_SOLICITUD\n" 
//                    +"WHERE IDE_DETALLE_SOLICITUD = "+detalle+" and  IDE_DETALLE_REQUISITOS_SOLICITUD = "+solicitud+") a \n" 
//                    +"WHERE IDE_DETALLE_REQUISITOS_SOLICITUD=a.id AND IDE_TIPO_REQUISITO=a.requisito AND IDE_DETALLE_SOLICITUD="+detalle;
    String actua ="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD SET CONFIRMAR_REQUISITO = "+requisito+" \n" 
                    +"WHERE IDE_DETALLE_REQUISITOS_SOLICITUD="+detalle+" AND IDE_TIPO_REQUISITO= "+tipo+" AND IDE_DETALLE_SOLICITUD= "+solicitud;
    conectar();
    conexion.ejecutarSql(actua);
    conexion.desconectar();
}

public void actualizarEstado(Integer codigo,Byte confirma){
    String actual="UPDATE TRANS_DETALLE_REQUISITOS_SOLICITUD set CONFIRMAR_REQUISITO = "+confirma+" WHERE IDE_DETALLE_SOLICITUD ="+codigo;
    conectar();
    conexion.ejecutarSql(actual);
    conexion.desconectar();
}

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
      
 private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.NOMBRE_MARCA_BASE="sqlserver";
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
            
        }
    }
}
