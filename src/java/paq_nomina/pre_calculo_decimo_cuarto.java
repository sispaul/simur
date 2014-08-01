/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.decimoCuarto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_calculo_decimo_cuarto extends Pantalla{

    private Panel pan_opcion = new Panel();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //dibujar tablas
    private Tabla tab_decimo = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //COMBOS DE SELECICON
    private Combo cmb_ano = new Combo();
    private Combo cmb_ano1 = new Combo();
    private Combo cmb_descripcion = new Combo();
    private Combo cmb_descripcion1 = new Combo();
    
    // DIALOGO DE ACCIÓN
    private Dialogo dia_dialorol = new Dialogo();
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_rol = new Grid();
    
    private Dialogo dia_dialorol1 = new Dialogo();
    private Grid grid_rol1 = new Grid();

        //
    @EJB
    private decimoCuarto Dcuarto = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_calculo_decimo_cuarto() {
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        agregarComponente(pan_opcion);
        
        Boton bot1 = new Boton();
        bot1.setValue("SUBIR NOMINA");
        bot1.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot1.setMetodo("completa");

        Boton bot2 = new Boton();
        bot2.setValue("CALCULO DECIMO 4to");
        bot2.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot2.setMetodo("decimo_4to_Nom");

        Boton bot3 = new Boton();
        bot3.setValue("MIGRAR DECIMO 4TO A ROLES");
        bot3.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot3.setMetodo("migrar");
       
        Boton bot4 = new Boton();
        bot4.setValue("BORRAR DECIMO CUARTO");
        bot4.setIcon("ui-icon-closethick"); //pone icono de jquery temeroller
        bot4.setMetodo("borrar");

        pan_opcion.getChildren().add(bot1);
        pan_opcion.getChildren().add(bot2);
        pan_opcion.getChildren().add(bot3);
        pan_opcion.getChildren().add(bot4);
        
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //declaracion de cadena de conexion
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
        cmb_descripcion1.setId("cmb_descripcion1");
        cmb_descripcion1.setConexion(con_postgres);
        cmb_descripcion1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo");
        
        cmb_ano1.setId("cmb_ano1");
        cmb_ano1.setConexion(con_postgres);
        cmb_ano1.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso ");
        cmb_ano1.eliminarVacio();
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
//        gri_busca.getChildren().add(new Etiqueta("")); 
        bar_botones.agregarComponente(new Etiqueta("AÑO: "));
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano where ano_curso ="+utilitario.getAnio(utilitario.getFechaActual())+" order by ano_curso ");
        cmb_ano.eliminarVacio();
        bar_botones.agregarComponente(cmb_ano);
//        gri_busca.getChildren().add(cmb_ano);

//        gri_busca.getChildren().add(new Etiqueta("TIPO COLABORADOR: ")); 
        cmb_descripcion.setId("cmb_descripcion");
        cmb_descripcion.setConexion(con_postgres);
        cmb_descripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo order by descripcion");
//        gri_busca.getChildren().add(cmb_descripcion);
//        pan_opcion1.getChildren().add(gri_busca);

        //declaracion de tabla
        tab_decimo.setId("tab_decimo");
        tab_decimo.setConexion(con_postgres);
        tab_decimo.setHeader("CALCULO PARA DECIMO CUARTO SUELDO");
        tab_decimo.setTabla("srh_decimo_cuarto", "ide_decimo_cuarto", 1);
        tab_decimo.getColumna("cod_tipo").setVisible(false);
        tab_decimo.getColumna("ano").setVisible(false);
        tab_decimo.getColumna("ide_columna").setVisible(false);
        tab_decimo.getColumna("ide_periodo").setVisible(false);
        tab_decimo.getColumna("id_distributivo_roles").setVisible(false);
        tab_decimo.getColumna("ide_decimo_cuarto").setVisible(false);
        tab_decimo.getColumna("nombres").setFiltro(true);
        tab_decimo.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_decimo);
        agregarComponente(tpd);
        
         //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialorol.setId("dia_dialorol");
        dia_dialorol.setTitle("REPORTES DE ROLES - DECIMO 4ta REMUNERACIÓN"); //titulo
        dia_dialorol.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialorol.setHeight("20%");//siempre porcentaje   alto 
        dia_dialorol.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialorol.getBot_aceptar().setMetodo("aceptoDecimo4To");
        grid_rol.setColumns(4);
        agregarComponente(dia_dialorol);
        
        dia_dialorol1.setId("dia_dialorol1");
        dia_dialorol1.setTitle("REPORTES DE ROLES - VERIFICACIÓN"); //titulo
        dia_dialorol1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialorol1.setHeight("20%");//siempre porcentaje   alto 
        dia_dialorol1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialorol1.getBot_aceptar().setMetodo("aceptoDecimo4To");
        grid_rol1.setColumns(4);
        agregarComponente(dia_dialorol1);
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
    }
    
    public void completa(){

        Dcuarto.Nomina();
        tab_decimo.actualizar();
        completar_nomina();
    }
    
    public void completar_nomina(){
        for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
            
            Dcuarto.verificar(tab_decimo.getValor(i, "cedula"),Integer.parseInt(tab_decimo.getValor(i, "cod_tipo")));
        }
      tab_decimo.actualizar();
      dia_dialogo.cerrar();
    }
    
 public void decimo_4to_Nom(){
       for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
           tab_decimo.getValor(i, "fecha_ingreso");
           tab_decimo.getValor(i, "cedula");
              Integer anos=0,meses=0,dias=0;
              double dia=0,dia1=0;
                anos=utilitario.getAnio(tab_decimo.getValor(i, "fecha_ingreso"));
                meses=utilitario.getMes(tab_decimo.getValor(i, "fecha_ingreso"));
                dias=utilitario.getDia(tab_decimo.getValor(i, "fecha_ingreso"));
                dia=utilitario.getDia(tab_decimo.getValor(i, "fecha_ingreso"));
           if(calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))>12){
//                            if(dias!=1){
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble("340"));
//                               }
                }else{
                        double valor=0,totdia=0,calculo=0;
                     if(meses == 1 || meses == 2){
                        if(dias!=1){
                            if(dias>1 && dias<=16){
                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                calculo = (((totdia-1)*30)+(30-dia));
                                valor= (calculo*340)/360;
                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                            }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30)+(30-dia);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                            }
                        }else{
                            totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                            calculo = (totdia*30);
                            valor= (calculo*340)/360;
                            Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                        }
                    }else if(meses == 3){
                                 if(dias!=1){
                                            if(dias>1 && dias<=13){
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (((totdia-1)*30)+(30-dia));
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                            }else{
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (totdia*30)+(30-dia);
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                                }
                                }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    }else if(meses == 4){
                              if(dias!=1){
                                            if(dias>1 && dias<=11){
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (((totdia-1)*30)+(30-dia));
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                            }else{
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (totdia*30)+(30-dia);
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                                }
                                }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    } else if(meses == 5){
                              if(dias!=1){
                                            if(dias>1 && dias<=8){
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (((totdia-1)*30)+(30-dia));
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                            }else{
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (totdia*30)+(30-dia);
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                                }
                                }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    }else if(meses == 6){
                               if(dias!=1){
                                            if(dias>1 && dias<=6){
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (((totdia-1)*30)+(30-dia));
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                            }else{
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (totdia*30)+(30-dia);
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                                }
                                }else{
                                        totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (totdia*30);
                                        valor= (calculo*340)/360;
                                        Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    }else if(meses == 7){
                              if(dias!=1){
                                   if(dias>1 && dias<=3){
                                        totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (((totdia-1)*30)+(30-dia));
                                        valor= (calculo*340)/360;
                                        Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                            }else{
                                                totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                calculo = (totdia*30)+(30-dia);
                                                valor= (calculo*340)/360;
                                                Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                                }
                                }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    }else {
                            if(dias!=1){
                                        totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (((totdia-1)*30)+(30-dia));
                                        valor= (calculo*340)/360;
                                        Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                            }else{
                                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = (totdia*30);
                                    valor= (calculo*340)/360;
                                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                                }
                    }
           }
       }
        newColum();
        dias();
    }    
    
 public void dias(){
     for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
          tab_decimo.getValor(i, "cedula");
          tab_decimo.getValor(i, "fecha_ingreso");
                Integer anos=0,meses=0,dias=0,total;
                double dia=0,calculo=0,mes=0;
                anos=utilitario.getAnio(tab_decimo.getValor(i, "fecha_ingreso"));
                meses=utilitario.getMes(tab_decimo.getValor(i, "fecha_ingreso"));
                dias=utilitario.getDia(tab_decimo.getValor(i, "fecha_ingreso"));
                if(calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))<12){
                    if(meses == 1 || meses == 2){
                        if(dias!=1){
                            if(dias>1 && dias<=16){
                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                    calculo = ((total-1)*30)+(30-dias);
                                    int d = (int) Math.floor(calculo);
                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                            }else{
                                total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                calculo = (total*30)+(30-dias);
                                int d = (int) Math.floor(calculo);
                                Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                            }
                        }else{
                                total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                calculo = (total*30);
                                int d = (int) Math.floor(calculo);
                                Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                        }
                    }else if(meses == 3){
                                 if(dias!=1){
                                            if(dias>1 && dias<=13){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                            }else{
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = (total*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                                }
                                }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    }else if(meses == 4){
                              if(dias!=1){
                                            if(dias>1 && dias<=11){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                            }else{
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = (total*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                                }
                                }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    } else if(meses == 5){
                              if(dias!=1){
                                            if(dias>1 && dias<=8){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                            }else{
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = (total*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                                }
                                }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    }else if(meses == 6){
                               if(dias!=1){
                                            if(dias>1 && dias<=6){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                            }else{
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = (total*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                                }
                                }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    }else if(meses == 7){
                              if(dias!=1){
                                   if(dias>1 && dias<=3){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                            }else{
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = (total*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                                }
                                }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    }else {
                            if(dias!=1){
                                                    total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                                    calculo = ((total-1)*30)+(30-dias);
                                                    int d = (int) Math.floor(calculo);
                                                    Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                            }else{
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = (total*30);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                                }
                    }
                }else{
                      if(calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))<13){
                            if(dias!=1){
                                        total=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                                        calculo = ((total-1)*30)+(30-dias);
                                        int d = (int) Math.floor(calculo);
                                        Dcuarto.decimo_dias(tab_decimo.getValor(i, "cedula"), d);
                               }
                       }
                }
         }
             tab_decimo.actualizar();
             utilitario.agregarMensaje("Decimo 4to Sueldo", "Calculado :) ;)");
 }
    public void migrar(){ 
        for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
            tab_decimo.getValor(i, "ide_empleado");
            tab_decimo.getValor(i, "ide_columna");
            tab_decimo.getValor(i, "id_distributivo_roles");
            tab_decimo.getValor(i, "valor_decimo");
            Dcuarto.migrarDescuento(Integer.parseInt(tab_decimo.getValor(i, "id_distributivo_roles")), Integer.parseInt(tab_decimo.getValor(i, "ide_columna")), 
                    tab_consulta.getValor("NICK_USUA")+"", Double.parseDouble(tab_decimo.getValor(i, "valor_decimo")), Integer.parseInt(tab_decimo.getValor(i, "ide_empleado")));
        }
        utilitario.agregarMensaje("Decimo 4to Sueldo", "Subido Con Exito");
        Dcuarto.HistoricoDecimo(tab_consulta.getValor("NICK_USUA")+"");
    }
      
    public void borrar(){
        Dcuarto.borrarDecimo();
        tab_decimo.actualizar();
    }
    
    public void newColum(){
                     Dcuarto.InsertEm();
                     Dcuarto.InsertTra();
    }
    
    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
       rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
                case "REPORTE DE DECIMO 4to":
                    dia_dialorol.Limpiar();
                    grid_rol.getChildren().add(new Etiqueta("AÑO :"));
                    grid_rol.getChildren().add(cmb_ano1);
                    grid_rol.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                    grid_rol.getChildren().add(cmb_descripcion1);
                    dia_dialorol.setDialogo(grid_rol);
                    dia_dialorol.dibujar();
                break;
                case "VERIFICAR NOMINA":
                    dia_dialorol1.Limpiar();
                    grid_rol1.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                    grid_rol1.getChildren().add(cmb_descripcion1);
                    dia_dialorol1.setDialogo(grid_rol1);
                    dia_dialorol1.dibujar();
                break;
                case "VERIFICAR FECHAS VACIAS":
                            aceptoDecimo4To();
                break;
        }
    }
    
    // dibujo de reporte y envio de parametros
    public void aceptoDecimo4To(){
        switch (rep_reporte.getNombre()) {
               case "REPORTE DE DECIMO 4to":
                    TablaGenerica tab_dato = Dcuarto.distibutivo(Integer.parseInt(cmb_descripcion1.getValue()+""));
                        if (!tab_dato.isEmpty()) {
                            TablaGenerica tab_dato2 = Dcuarto.columnas(Integer.parseInt("125"));
                                 if (!tab_dato2.isEmpty()) {
                                     TablaGenerica tab_dato3 = Dcuarto.periodo(Integer.parseInt(utilitario.getMes(utilitario.getFechaActual())+""));
                                        if (!tab_dato3.isEmpty()) {
                                            p_parametros = new HashMap();
                                            p_parametros.put("pide_ano",Integer.parseInt(cmb_ano1.getValue()+""));
                                            p_parametros.put("distributivo",Integer.parseInt(cmb_descripcion1.getValue()+""));
                                            p_parametros.put("descripcion",tab_dato.getValor("descripcion")+"");
                                            p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                                            p_parametros.put("columnas", Integer.parseInt("125"));
                                            p_parametros.put("periodo",Integer.parseInt(utilitario.getMes(utilitario.getFechaActual())+""));
                                            p_parametros.put("descrip",tab_dato2.getValor("descripcion_col")+"");
                                            p_parametros.put("p_nombre",tab_dato3.getValor("per_descripcion")+"");
                                            rep_reporte.cerrar();
                                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                            sef_formato.dibujar();
                                        } else {
                                                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                                }
                                  } else {
                                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                        }
                          } else {
                                  utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                  }
               break;
               case "VERIFICAR NOMINA":
                    TablaGenerica tab_dato1 = Dcuarto.distibutivo(Integer.parseInt(cmb_descripcion1.getValue()+""));
                        if (!tab_dato1.isEmpty()) {
                                            p_parametros = new HashMap();
                                            p_parametros.put("distributivo",Integer.parseInt(cmb_descripcion1.getValue()+""));
                                            p_parametros.put("descripcion",tab_dato1.getValor("descripcion")+"");
                                            p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                                            rep_reporte.cerrar();
                                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                            sef_formato.dibujar();
                          } else {
                                  utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                  }
               break;
               case "VERIFICAR FECHAS VACIAS":
                     p_parametros = new HashMap();
                     rep_reporte.cerrar();
                     sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                     sef_formato.dibujar();
               break;
        }
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
     long diffWeek = diffDays / 7 ; // calcular la diferencia en semanas
     long diffMounth = diffWeek / 4 ; // calcular la diferencia en meses

     return Integer.parseInt(String.valueOf(diffMounth));
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
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        tab_decimo.guardar();
        con_postgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_decimo() {
        return tab_decimo;
    }

    public void setTab_decimo(Tabla tab_decimo) {
        this.tab_decimo = tab_decimo;
    }

    public Combo getCmb_ano() {
        return cmb_ano;
    }

    public void setCmb_ano(Combo cmb_ano) {
        this.cmb_ano = cmb_ano;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Combo getCmb_ano1() {
        return cmb_ano1;
    }

    public void setCmb_ano1(Combo cmb_ano1) {
        this.cmb_ano1 = cmb_ano1;
    }

    public Combo getCmb_descripcion() {
        return cmb_descripcion;
    }

    public void setCmb_descripcion(Combo cmb_descripcion) {
        this.cmb_descripcion = cmb_descripcion;
    }

    public Combo getCmb_descripcion1() {
        return cmb_descripcion1;
    }

    public void setCmb_descripcion1(Combo cmb_descripcion1) {
        this.cmb_descripcion1 = cmb_descripcion1;
    }
    
}
