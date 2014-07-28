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
public class pre_decimo_cuarto extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //dibujar tablas
    private Tabla tab_decimo = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //
    @EJB
    private decimoCuarto Dcuarto = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //COMBOS DE SELECICON
    private Combo cmb_anio = new Combo();
    private Combo cmb_descripcion = new Combo();
    
    // DIALOGO DE ACCIÓN
    private Dialogo dia_dialorol = new Dialogo();
    private Grid grid_rol = new Grid();
    
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
        bot2.setValue("CALCULO DECIMO 4to");
        bot2.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot2.setMetodo("decimo_4to_Nom");
        bar_botones.agregarBoton(bot2);
     
        Boton bot4 = new Boton();
        bot4.setValue("MIGRAR DESCUENTO");
        bot4.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot4.setMetodo("migrar");
        bar_botones.agregarBoton(bot4); 
       
        Boton bot5 = new Boton();
        bot5.setValue("BORRAR DESCUENTO");
        bot5.setIcon("ui-icon-closethick"); //pone icono de jquery temeroller
        bot5.setMetodo("borrar");
        bar_botones.agregarBoton(bot5);
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
               /*CONFIGURACIÓN DE COMBOS*/
       
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        
        gri_busca.getChildren().add(new Etiqueta("AÑO:"));
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca.getChildren().add(cmb_anio);
        
        gri_busca.getChildren().add(new Etiqueta("DESCRIPCIÓN:"));
        cmb_descripcion.setId("cmb_descripcion");
        cmb_descripcion.setConexion(con_postgres);
        cmb_descripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        gri_busca.getChildren().add(cmb_descripcion);
        
        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialorol.setId("dia_dialorol");
        dia_dialorol.setTitle("REPORTES DE ROLES - DECIMO 4ta REMUNERACIÓN"); //titulo
        dia_dialorol.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialorol.setHeight("20%");//siempre porcentaje   alto 
        dia_dialorol.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialorol.getBot_aceptar().setMetodo("aceptoDecimo4To");
        grid_rol.setColumns(4);
        agregarComponente(dia_dialorol);

    }
    
    public void completa(){
        Dcuarto.Nomina();
        tab_decimo.actualizar();
        completar_nomina();
    }
    
    public void completar_nomina(){
        for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
            Dcuarto.verificar(tab_decimo.getValor(i, "cedula"));
        }
      tab_decimo.actualizar();
    }
    
    public void decimo_4to_Nom(){
       for (int i = 0; i < tab_decimo.getTotalFilas(); i++) {
           tab_decimo.getValor(i, "fecha_ingreso");
           tab_decimo.getValor(i, "cedula");
              Integer anos=0,meses=0,dias=0;
                anos=utilitario.getAnio(tab_decimo.getValor(i, "fecha_ingreso"));
                meses=utilitario.getMes(tab_decimo.getValor(i, "fecha_ingreso"));
                dias=utilitario.getDia(tab_decimo.getValor(i, "fecha_ingreso"));
           if(calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31))>=360){
                     Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble("340"));
                }else{
                    double valor=0,totdia=0;
                    totdia=calcularDias(new GregorianCalendar(anos,meses,dias),new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()),7,31));
                    valor= (totdia*340)/360;
                    Dcuarto.decimo_cont(tab_decimo.getValor(i, "cedula"),Double.parseDouble(String.valueOf(Math.rint(valor*100)/100)));
                    }
       }
        tab_decimo.actualizar();
        newColum();
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
    }
      
    public void borrar(){
        Dcuarto.borrarDecimo();
        tab_decimo.actualizar();
    }
    
    public void newColum(){
        
                     Dcuarto.InsertEm();
                     Dcuarto.InsertTra();
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
                    grid_rol.getChildren().add(cmb_anio);
                    grid_rol.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                    grid_rol.getChildren().add(cmb_descripcion);
                    dia_dialorol.setDialogo(grid_rol);
                    dia_dialorol.dibujar();
                break;
        }
    }
    
    // dibujo de reporte y envio de parametros
    public void aceptoDecimo4To(){
        switch (rep_reporte.getNombre()) {
               case "REPORTE DE DECIMO 4to":
                    TablaGenerica tab_dato = Dcuarto.distibutivo(Integer.parseInt(cmb_descripcion.getValue()+""));
                        if (!tab_dato.isEmpty()) {
                            TablaGenerica tab_dato2 = Dcuarto.columnas(Integer.parseInt("125"));
                                 if (!tab_dato2.isEmpty()) {
                                     TablaGenerica tab_dato3 = Dcuarto.periodo(Integer.parseInt(utilitario.getMes(utilitario.getFechaActual())+""));
                                        if (!tab_dato3.isEmpty()) {
                                            p_parametros = new HashMap();
                                            p_parametros.put("pide_ano",Integer.parseInt(cmb_anio.getValue()+""));
                                            p_parametros.put("distributivo",Integer.parseInt(cmb_descripcion.getValue()+""));
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
    
}
