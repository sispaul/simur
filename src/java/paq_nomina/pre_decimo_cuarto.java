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
       for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
           tab_decimo.getValor(i, "fecha_ingreso");
           tab_decimo.getValor(i, "ide_decimo_cuarto");
           tab_decimo.getValor(i, "cod_tipo");
           tab_decimo.getValor(i, "cedula");
                       if(tab_decimo.getValor(i, "cod_tipo").equals("4") ||tab_decimo.getValor(i, "cod_tipo").equals("7")){
                           Dcuarto.decimo_nom(tab_decimo.getValor(i, "cedula"),Integer.parseInt(tab_decimo.getValor(i, "ide_decimo_cuarto")),Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")), Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")));
                           tab_decimo.actualizar();
                 }else{
                       Integer anos=0,meses=0,dias=0;
                       anos=utilitario.getAnio(tab_decimo.getValor(i, "fecha_ingreso"));
                       meses=utilitario.getMes(tab_decimo.getValor(i, "fecha_ingreso"));
                       dias=utilitario.getDia(tab_decimo.getValor(i, "fecha_ingreso"));
                       
                                    tab_decimo.getValor(i, "fecha_ingreso");
                                    tab_decimo.getValor(i, "ide_decimo_cuarto");
                                    tab_decimo.getValor(i, "cod_tipo");
                                    tab_decimo.getValor(i, "cedula");
                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                             Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble("340"),Integer.parseInt(tab_decimo.getValor(i, "ide_decimo_cuarto")),Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")), Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")));
                           }else {
                                    double valor=0,totdia=0;
                                        if(calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))>=360){
                                            Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble("340"),Integer.parseInt(tab_decimo.getValor(i, "ide_decimo_cuarto")),Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")), Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")));
                                        }else if (calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))<360){
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                valor= (totdia*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)),Integer.parseInt(tab_decimo.getValor(i, "ide_decimo_cuarto")),Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")), Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")));
                                        }
                            }
                       } 
       }
        tab_decimo.actualizar();
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
    
    //CALCULAR ANIOS
    public static int calcularAnios(Calendar AnioLab) {
    Calendar fechaActual = Calendar.getInstance();
    // Cálculo de las diferencias.
    int anios = fechaActual.get(Calendar.YEAR) - AnioLab.get(Calendar.YEAR);
    int meses = fechaActual.get(Calendar.MONTH) - AnioLab.get(Calendar.MONTH);
    int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - AnioLab.get(Calendar.DAY_OF_MONTH);
  
    if(meses < 0 // Aún no es el mes 
       || (meses==0 && dias < 0)) { // o es el mes pero no ha llegado el día.
        anios--;
    }
    return anios;
    }
    
    public Tabla getTab_decimo() {
        return tab_decimo;
    }

    public void setTab_decimo(Tabla tab_decimo) {
        this.tab_decimo = tab_decimo;
    }
    
}
