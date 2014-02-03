/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_presupuestarias extends Pantalla{
private Combo cmb_ano = new Combo();
private Combo cmb_niveli = new Combo();
private Combo cmb_nivelf = new Combo();
private Combo cmb_licenti = new Combo();
private Conexion con_postgres= new Conexion();
private SeleccionCalendario sec_rango = new SeleccionCalendario();
private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
private Reporte rep_reporte = new Reporte();
private Map p_parametros = new HashMap();
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta2= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();
private Etiqueta eti_etiqueta3= new Etiqueta();
private Etiqueta eti_etiqueta4= new Etiqueta();
private Etiqueta eti_etiqueta5= new Etiqueta();
private Calendario cal_inicio = new Calendario();
private Calendario cal_final = new Calendario();

    public pre_presupuestarias() {
        
        
        
        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
                    
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        
        eti_etiqueta.setValue("Año:");
        gri_busca.getChildren().add(eti_etiqueta);     
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano");
        gri_busca.getChildren().add(cmb_ano);
        
        eti_etiqueta2.setValue("Nivel Inicial:");
        gri_busca.getChildren().add(eti_etiqueta2); 
        cmb_niveli.setId("cmb_niveli");
        cmb_niveli.setConexion(con_postgres);
        cmb_niveli.setCombo("select ide_cedula_presupuestaria, nivel from conc_cedula_presupuestaria_fechas");
        gri_busca.getChildren().add(cmb_niveli);       
        
        
        eti_etiqueta1.setValue("Nivel Final:");
        gri_busca.getChildren().add(eti_etiqueta1); 
        cmb_nivelf.setId("cmb_nivelf");
        cmb_nivelf.setConexion(con_postgres);
        cmb_nivelf.setCombo("select ide_cedula_presupuestaria, nivel from conc_cedula_presupuestaria_fechas");
        gri_busca.getChildren().add(cmb_nivelf);
        
        eti_etiqueta3.setValue("Fecha Inicial:");
        gri_busca.getChildren().add(eti_etiqueta3);
        cal_inicio.setId("cal_inicio");
        gri_busca.getChildren().add(cal_inicio);
        
        eti_etiqueta4.setValue("Fecha Final:");
        gri_busca.getChildren().add(eti_etiqueta4);
        cal_final.setId("cal_final");
        gri_busca.getChildren().add(cal_final);
        
        
        eti_etiqueta5.setValue("Tipo Cedula:");
        gri_busca.getChildren().add(eti_etiqueta5);
       
        cmb_licenti.setId("cmb_licenti");
        cmb_licenti.setConexion(con_postgres);
        cmb_licenti.setCombo("select ide_cedula_presupuestaria, tipo from conc_cedula_presupuestaria_fechas");
        gri_busca.getChildren().add(cmb_licenti);
        agregarComponente(gri_busca);
        
        
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        
        sef_reporte.setId("sef_reporte");
        agregarComponente(sef_reporte);
        bar_botones.agregarReporte();

        
    }
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }    
    
@Override
    public void aceptarReporte() {
    System.err.println("Ingresando");
        if (rep_reporte.getReporteSelecionado().equals("CEDULAS PRESUPUESTARIAS")) {
                    //p_parametros.put("tipo", cmb_licenti.getValue()+"");
                    p_parametros.put("tipo",Integer.parseInt(cmb_licenti.getValue()+""));
                       System.out.println(cmb_ano.getValue());
                    //p_parametros.put("ano", cmb_ano.getValue()+"");
                    p_parametros.put("ano",Integer.parseInt(cmb_ano.getValue()+""));
                       System.out.println(cmb_licenti.getValue());
                    //p_parametros.put("niveli", cmb_niveli.getValue()+"");
                    p_parametros.put("niveli",Integer.parseInt(cmb_niveli.getValue()+""));   
                       System.out.println(cmb_nivelf.getValue());
                    //p_parametros.put("nivelF", cmb_nivelf.getValue()+"");
                    p_parametros.put("nivelf",Integer.parseInt(cmb_nivelf.getValue()+""));
                       System.out.println(cmb_niveli.getValue());
                    p_parametros.put("fechai", cal_inicio.getFecha());
                       System.out.println(cal_inicio.getValue());
                    p_parametros.put("fechaf", cal_final.getFecha());
                       System.out.println(cal_final.getValue());
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                System.err.println("Saliendo");
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

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Combo getCmb_ano() {
        return cmb_ano;
    }

    public void setCmb_ano(Combo cmb_ano) {
        this.cmb_ano = cmb_ano;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    
}
