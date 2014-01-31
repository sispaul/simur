/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestarias;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingresocon extends Pantalla{

    private Conexion con_postgres = new Conexion();
    private Etiqueta eti_cabecera = new Etiqueta();
    private Combo cmd_comboti = new Combo();
    private Combo cmd_comboan = new Combo();
    private Combo cmd_comboni = new Combo();
    private Combo cmd_combonf = new Combo();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Tabla tab_consulta = new Tabla();
    private Map p_parametros = new HashMap();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    private SeleccionCalendario sec_rango1 = new SeleccionCalendario();
    
    public pre_ingresocon() {
        bar_botones.limpiar();
        //conexion a postgres
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        
        Grid gri_grid = new Grid();
        gri_grid.setColumns(2);
        eti_cabecera.setValue("Selecionar Parametros de Busqueda");
        gri_grid.getChildren().add(eti_cabecera);
        
        // elegir año
        
        cmd_comboan.setId("cmd_comboan");
        cmd_comboan.setTitle("Año");
        cmd_comboan.setCombo("select ano_curso,ano_curso from conc_ano");
        gri_grid.getChildren().add(cmd_comboan);
        
        //elegir niveles
        cmd_comboni.setId("cmd_comboni");
        cmd_comboni.setCombo("SELECT ide_cedula_presupuestaria,nivel from conc_cedula_presupuestaria_fechas");
        gri_grid.getChildren().add(cmd_comboni);
        
        cmd_combonf.setId("combonf");
        cmd_combonf.setCombo("SELECT ide_cedula_presupuestaria,nivel from conc_cedula_presupuestaria_fechas");
        gri_grid.getChildren().add(cmd_combonf);
        
         //elegir tipo de cedula
        cmd_comboti.setId("cmd_comboti");
        cmd_comboti.setCombo("SELECT ide_cedula_presupuestaria,tipo from conc_cedula_presupuestaria_fechas");
        gri_grid.getChildren().add(cmd_comboti);
        
        agregarComponente(gri_grid);
        
         //Creacion de objeto reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
    }
    public void reporteIngreso(){
                //los parametros de este reporte
                rep_reporte.setNombre("CEDULAS PRESUPUESTARIAS");
                rep_reporte.setPath("rep_reportes/ingreso_consolidado.jasper");
                p_parametros = new HashMap();
                p_parametros.put("tipo", cmd_comboti.getValue()+"");
                p_parametros.put("ano", cmd_comboan.getValue()+"");
                p_parametros.put("niveli", cmd_comboni.getValue()+"");
                p_parametros.put("nivelf", cmd_combonf.getValue()+"");
                p_parametros.put("fechaI", sec_rango.getFecha1String());
                p_parametros.put("fechaF", sec_rango.getFecha2String());
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
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
}
