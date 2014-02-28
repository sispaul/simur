/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes.ejb;

import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;
import framework.aplicacion.TablaGenerica;
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

//ACTUALIZACION DE APROBACION EN SOLICITUD
public void actualizarD(Byte aprobado,Integer id_a,Integer id_s)
    {
        String actualiza = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set APROBADO_SOLICITUD="+aprobado+", IDE_APROBACION_PLACA= "+id_a+"\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s; 
            conectar();
            conexion.ejecutarSql(actualiza);
    }
//ACTUALIZACION DE ENTREGA EN SOLICITUD
public void actualizarDS(Integer id_en,Byte entrega,String id_enf,Integer id_s)
    {
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set IDE_ENTREGA_PLACA="+id_en+",ENTREGADA_PLACA="+entrega+", FECHA_ENTREGA_PLACA= '"+id_enf+"'\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s; 
            conectar();
            conexion.ejecutarSql(actualiza1);
    }
 //   ACTUALIZACION DE ENTREGA EN PLACA
public void actualizarDE(String cedula,String nombre,String id_enf,Integer id_s)
    {
        System.err.println(id_enf);
        String actualiza1 = "update TRANS_DETALLE_SOLICITUD_PLACA \n" 
                            +"set CEDULA_RUC_PROPIETARIO="+cedula+",NOMBRE_PROPIETARIO="+nombre+", FECHA_ENTREGA_PLACA="+id_enf+"\n" 
                            +"where IDE_DETALLE_SOLICITUD="+id_s; 
            conectar();
            conexion.ejecutarSql(actualiza1);
    }
 //ASIGNACION AUTOMATICA DE PLACAS   
public String seleccionarP(Integer vehiculo,Integer servicio)
    {
        conectar();
         String placa;
         TablaGenerica tab_consulta = new TablaGenerica();
         conectar();
         tab_consulta.setConexion(conexion);
         tab_consulta.setSql("SELECT TOP 1 IDE_PLACA,PLACA\n" 
                            +"FROM TRANS_PLACA\n" 
                            +"WHERE IDE_TIPO_ESTADO =3 AND IDE_TIPO_VEHICULO= "+vehiculo+" AND IDE_TIPO_SERVICIO = "+servicio+" AND IDE_TIPO_PLACA = 1\n" 
                            +"ORDER BY PLACA ASC");
           
         tab_consulta.ejecutarSql();
         placa = tab_consulta.getValor("IDE_PLACA");
         return placa;

    }
          
 private void conectar() {
        if (conexion == null) {
            conexion = new Conexion();
            conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }
}
