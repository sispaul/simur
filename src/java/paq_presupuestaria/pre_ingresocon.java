/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Dialogo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingresocon extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //Declaracion de combos
    private Combo cmb_ano = new Combo();
    private Combo cmb_niveli = new Combo();
    private Combo cmb_nivelf = new Combo();
    private Combo cmb_licenti = new Combo();
    
    //Creacion Calendarios
    private Calendario cal_fechain = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    
    private Panel pan_opcion = new Panel();
    private Efecto efecto = new Efecto();
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //obejto tabla usuario
    private Tabla tab_consulta = new Tabla();
    
    // DIALOGO DE ACCIÓN
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_de1 = new Grid();
    
    private Tabla set_distribu = new Tabla();
     @EJB
    private Programas programas =(Programas) utilitario.instanciarEJB(Programas.class);
     
    public pre_ingresocon() {
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        
//         inicioCedula();
         Panel tabp2 = new Panel();
         tabp2.setStyle("font-size:19px;color:black;text-align:center;");
         tabp2.setHeader("CEDULAS PRESUPUESTARIAS INGRESOS Y GASTOS - SALDOS NEGATIVOS");
        /*
         * COMBO QUE MUESTRAS LOS AÑOS DE 
         */
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        gri_busca.getChildren().add(new Etiqueta("AÑO: "));    
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso ");
        cmb_ano.eliminarVacio();
        gri_busca.getChildren().add(cmb_ano);
        
        /*
         * COMBOS QUE PERMITE SELECCIONAR EL NIVEL DE BUSQUEDA PARA LAS CEDULAS DE GASTOS E INGRESOS
         */

        gri_busca.getChildren().add(new Etiqueta("NIVEL INICIAL: "));
        cmb_niveli.setId("cmb_niveli");
        List lista = new ArrayList();
        Object filaa[] = {
            "1", "1"
        };
        Object filab[] = {
            "2", "2"
        };
        Object filac[] = {
            "3", "3"
        };
        Object filad[] = {
            "4", "4"
        };
        Object filae[] = {
            "5", "5"
        };
        Object filaf[] = {
            "6", "6"
        };
        Object filag[] = {
            "7", "7"
        };
        lista.add(filaa);;
        lista.add(filab);;
        lista.add(filac);;
        lista.add(filad);;
        lista.add(filae);;
        lista.add(filaf);;
        lista.add(filag);;
        cmb_niveli.setCombo(lista);
        cmb_niveli.eliminarVacio();
        gri_busca.getChildren().add(cmb_niveli);       
        
        gri_busca.getChildren().add(new Etiqueta("NIVEL FINAL: "));
        cmb_nivelf.setId("cmb_nivelf");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "7", "7"
        };
        Object fila2[] = {
            "6", "6"
        };
        Object fila3[] = {
            "5", "5"
        };
        Object fila4[] = {
            "4", "4"
        };
        Object fila5[] = {
            "3", "3"
        };
        Object fila6[] = {
            "2", "2"
        };
        Object fila7[] = {
            "1", "1"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        lista1.add(fila3);;
        lista1.add(fila4);;
        lista1.add(fila5);;
        lista1.add(fila6);;
        lista1.add(fila7);;
        cmb_nivelf.setCombo(lista1);
        cmb_nivelf.eliminarVacio();
        gri_busca.getChildren().add(cmb_nivelf);
        
        gri_busca.getChildren().add(new Etiqueta("FECHA INICIAL: "));
        cal_fechain.setId("cal_fechain");
        cal_fechain.setFechaActual();
        cal_fechain.setTipoBoton(true);
        gri_busca.getChildren().add(cal_fechain);
        
        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL: "));
        cal_fechafin.setId("cal_fechafin");
        cal_fechafin.setFechaActual();
        cal_fechafin.setTipoBoton(true);
        gri_busca.getChildren().add(cal_fechafin);
        
        /*
         * PROGRAMAS
         */
        gri_busca.getChildren().add(new Etiqueta("TIPO CEDULA: "));
        cmb_licenti.setId("cmb_licenti");
        List lista2 = new ArrayList();
        Object filat[] = {
            "1", "INGRESOS CONSOLIDADOS"
        };
        Object filau[] = {
            "2", "GASTOS POR PROGRAMAS"
        };
        lista2.add(filat);;
        lista2.add(filau);;
        cmb_licenti.setCombo(lista2);
//        cmb_licenti.eliminarVacio();
        gri_busca.getChildren().add(cmb_licenti);
                
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setStyle("font-size:12px;color:black;text-align:left;");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");

        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:190px;left:500px;");
        quinde.setValue("imagenes/logo.png");
//        tabp2.setWidth("100%");
        
        pan_opcion.getChildren().add(efecto);
        pan_opcion.getChildren().add(quinde);
        pan_opcion.getChildren().add(gri_busca);
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("IMPRESIÓN DE REPORTE");
        bot_bus.setIcon("ui-icon-print");
        bot_bus.setMetodo("abrirListaReportes");
        
        tabp2.getChildren().add(pan_opcion);
        tabp2.getChildren().add(bot_bus);
        agregarComponente(tabp2);
        
         /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
   
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
                //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("GASTOS PROGRAMAS"); //titulo
        dia_dialogoe.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("35%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);

    }

    public void inicioCedula(){

        programas.eiminarIngreso();
        programas.actualizacionPrograma();
               programas.eiminarIngreso();
               programas.insertaIngresos(Integer.parseInt(cmb_ano.getValue()+""), Integer.parseInt(cmb_licenti.getValue()+""), cal_fechafin.getFecha());
               programas.actualizarIngresos(Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarDatosIngresos(cal_fechain.getFecha(), cal_fechafin.getFecha(), Integer.parseInt(cmb_ano.getValue()+""), Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarDatos2();
               programas.actualizarDatosg3();
               programas.actualizarDatosg4(Integer.parseInt(cmb_ano.getValue()+""), Integer.parseInt(cmb_licenti.getValue()+""));
    }
    
      //ACTUALIZACIONES PARA OPCION DE GASTOS PROGRAMADOS 
    public void deleteTablaGastos() { 

        programas.eiminarIngreso();
        programas.insertarGastos(Integer.parseInt(cmb_ano.getValue()+""), cal_fechafin.getFecha(), Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarGastos(Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarGastos1(cal_fechain.getFecha(), cal_fechafin.getFecha(), Integer.parseInt(cmb_ano.getValue()+""), Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarGastos2(Integer.parseInt(cmb_ano.getValue()+""), Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarTablaGastos3(Integer.parseInt(cmb_ano.getValue()+""), cal_fechain.getFecha(), cal_fechafin.getFecha(), Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarTablaGastos4(Integer.parseInt(cmb_licenti.getValue()+""));
               programas.actualizarTablaGastos5(Integer.parseInt(cmb_licenti.getValue()+""), Integer.parseInt(cmb_ano.getValue()+""));
    }
    
    /*
         * LLAMADA DE REPORTE  SOLICITUD
         */
        
        @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "INGRESOS CONSOLIDADOS":
               inicioCedula();
               aceptoDialogo();
               break;
           case "GASTOS PROGRAMAS POR TIPO":
               deleteTablaGastos(); 
               dia_dialogoe.Limpiar();
                set_distribu.setId("set_distribu");
                set_distribu.setConexion(con_postgres);
                set_distribu.setSql("select DISTINCT cod_funcion,cod_funcion as Codigo,des_funcion from  conc_cedula_presupuestaria_fechas  WHERE tipo ="+cmb_licenti.getValue()+" ORDER BY cod_funcion");
                set_distribu.setRows(13);
                set_distribu.setTipoSeleccion(false);
                
                grid_de.getChildren().add(set_distribu);
                dia_dialogoe.setDialogo(grid_de);
                set_distribu.dibujar();
                dia_dialogoe.dibujar();
                

           break;
           case "GASTOS PROGRAMAS TOTAL":
               deleteTablaGastos();
               aceptoDialogo();
           break;
                
        }
    }     
       
    public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "INGRESOS CONSOLIDADOS":
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+""));
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    System.out.println(p_parametros);
                    System.out.println(rep_reporte.getPath());
                    System.out.println(sef_formato);
                    sef_formato.dibujar();
                    System.out.println(sef_formato);
               break;
               case "GASTOS PROGRAMAS POR TIPO":
                   if (set_distribu.getValorSeleccionado()!= null) {
                    p_parametros = new HashMap();
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+"")); 
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("funcion",Integer.parseInt(set_distribu.getValorSeleccionado()+""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                     }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
               break;
               case "GASTOS PROGRAMAS TOTAL":
                    p_parametros = new HashMap();
                    p_parametros.put("pide_ano",Integer.parseInt(cmb_ano.getValue()+""));
                    p_parametros.put("pide_fechai", cal_fechain.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+"")); 
                    p_parametros.put("pnivel1",Integer.parseInt(cmb_niveli.getValue()+"")); 
                    p_parametros.put("pnivel2",Integer.parseInt(cmb_nivelf.getValue()+""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }

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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Combo getCmb_ano() {
        return cmb_ano;
    }

    public void setCmb_ano(Combo cmb_ano) {
        this.cmb_ano = cmb_ano;
    }

    public Combo getCmb_niveli() {
        return cmb_niveli;
    }

    public void setCmb_niveli(Combo cmb_niveli) {
        this.cmb_niveli = cmb_niveli;
    }

    public Combo getCmb_nivelf() {
        return cmb_nivelf;
    }

    public void setCmb_nivelf(Combo cmb_nivelf) {
        this.cmb_nivelf = cmb_nivelf;
    }

    public Combo getCmb_licenti() {
        return cmb_licenti;
    }

    public void setCmb_licenti(Combo cmb_licenti) {
        this.cmb_licenti = cmb_licenti;
    }

    public Calendario getCal_fechain() {
        return cal_fechain;
    }

    public void setCal_fechain(Calendario cal_fechain) {
        this.cal_fechain = cal_fechain;
    }

    public Calendario getCal_fechafin() {
        return cal_fechafin;
    }

    public void setCal_fechafin(Calendario cal_fechafin) {
        this.cal_fechafin = cal_fechafin;
    }

    public Tabla getSet_distribu() {
        return set_distribu;
    }

    public void setSet_distribu(Tabla set_distribu) {
        this.set_distribu = set_distribu;
    }
    
}