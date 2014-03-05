/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes.ejb;

import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;
import framework.aplicacion.TablaGenerica;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
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
    }

//ACTUALIZACION DE ENTREGA EN SOLICITUD

public void actualizarDS(Integer id_en,Integer id_s2,Byte aprob_en)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA="+aprob_en+", FECHA_ENTREGA_PLACA= '"+utilitario.getFechaActual()+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s2; 
            conectar();
            conexion.ejecutarSql(actualiza1);
    }
 //   ACTUALIZACION DE ENTREGA EN PLACA
public void actualizarDE(Integer iden,String ruc,Integer placa)
    {
        String actualiza2 = "update TRANS_PLACA \n" 
                            +"set NOMBRE_PROPIETARIO=nombre,ide_tipo_estado = 2,FECHA_ENTREGA_PLACA='"+utilitario.getFechaActual()+"',CEDULA_RUC_PROPIETARIO=identi\n" 
                            +"from( select CEDULA_RUC_PROPIETARIO as identi,NOMBRE_PROPIETARIO as nombre from TRANS_DETALLE_SOLICITUD_PLACA\n" 
                            +"where IDE_DETALLE_SOLICITUD = "+iden+" and CEDULA_RUC_PROPIETARIO ="+ruc+")a\n" 
                            +"where ide_placa ="+placa; 
            conectar();
            conexion.ejecutarSql(actualiza2);
    }
          
 private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }
}
