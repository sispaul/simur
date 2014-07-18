/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Boton;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import paq_nomina.ejb.decimoCuarto;
import static paq_nomina.pre_anticipos_gadmur.calcularMes;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_decimo_cuarto extends Pantalla{

    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //dibujar tablas
    private Tabla tab_decimo = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //
    
    @EJB
    private decimoCuarto Dcuarto = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);
    
    public pre_decimo_cuarto() {
        
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //declaracion de cadena de conexion
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        //declaracion de tabla
        tab_decimo.setId("tab_decimo");
        tab_decimo.setConexion(con_postgres);
        tab_decimo.setTabla("srh_decimo_cuarto", "ide_decimo_cuarto", 1);
        tab_decimo.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_decimo);
        agregarComponente(tpd);
        
        Boton bot1 = new Boton();
        bot1.setValue("SUBIR NOMINA");
        bot1.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot1.setMetodo("completa");
        bar_botones.agregarBoton(bot1);
        
        Boton bot2 = new Boton();
        bot2.setValue("Calculo Decimo 4to");
        bot2.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot2.setMetodo("decimo_4to");
        bar_botones.agregarBoton(bot2);
        
        Boton bot3 = new Boton();
        bot3.setValue("DEPURAR DESCUENTO");
        bot3.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot3.setMetodo("verificar");
        bar_botones.agregarBoton(bot3); 
     
        Boton bot4 = new Boton();
        bot4.setValue("MIGRAR DESCUENTO");
        bot4.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot4.setMetodo("abrirDialogo");
        bar_botones.agregarBoton(bot4); 
       
        Boton bot5 = new Boton();
        bot5.setValue("BORRAR DESCUENTO");
        bot5.setIcon("ui-icon-closethick"); //pone icono de jquery temeroller
        bot5.setMetodo("borrar");
        bar_botones.agregarBoton(bot5);
    }

    public void completa(){
        Dcuarto.Nomina();
        tab_decimo.actualizar();
    }
    
    public void decimo_4to(){
        if(tab_decimo.getValor("cod_tipo").equals("4") || tab_decimo.getValor("cod_tipo").equals("7") ){
             Dcuarto.decimo_cod();
             tab_decimo.actualizar();
        }else{
             Integer anos=0, dias=0,meses=0,anio_a=0;
             anio_a= utilitario.getAnio(utilitario.getFechaActual());
             
             calcularMes(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(anio_a,7,30));
             
        }
    }
    
    public void verificar(){
        
    }
    
    public void abrirDialogo(){
        
    }
      
    public void borrar(){
        
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    //CALCULAR MESES
    public static int calcularDias(Calendar cal1,Calendar cal2) {
    // conseguir la representacion de la fecha en milisegundos
     long milis1 = cal1.getTimeInMillis();//fecha actual
     long milis2 = cal2.getTimeInMillis();//fecha futura
     long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
     long diffSeconds = diff / 1000; // calcular la diferencia en segundos
     long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
     long diffHours = diffMinutes / 60 ; // calcular la diferencia en horas a
     long diffDays = diffHours / 24 ; // calcular la diferencia en dias
     //long diffWeek = diffDays / 7 ; // calcular la diferencia en semanas
     //long diffMounth = diffWeek / 4 ; // calcular la diferencia en meses

     return Integer.parseInt(String.valueOf(diffDays));
    }
    
    public Tabla getTab_decimo() {
        return tab_decimo;
    }

    public void setTab_decimo(Tabla tab_decimo) {
        this.tab_decimo = tab_decimo;
    }
    
}
