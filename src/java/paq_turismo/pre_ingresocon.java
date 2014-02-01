/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_turismo;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
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
public class pre_ingresocon extends Pantalla{
    private Conexion con_postgres = new Conexion();
    private Etiqueta eti_etiqueta= new Etiqueta();
    private Etiqueta eti_etiqueta2= new Etiqueta();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    
    public pre_ingresocon() {
        
        Grid gri_grid = new Grid();
        gri_grid.setColumns(2);
//        Grid gri_grid = new Grid();
//        gri_grid.setColumns(2);
//        eti_cabecera.setValue("Selecionar Parametros de Busqueda");
//        gri_grid.getChildren().add(eti_cabecera);
//        
//        sec_rango.setId("sec_rango");
//        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
//        sec_rango.setFechaActual();
//        agregarComponente(sec_rango);
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        //2) le dcimos con que marca de base va a trabar, soporta oracle,postgres,sqlserver,mysql
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        

//        tab_tabla.setId("tab_tabla");
//        tab_tabla.setConexion(con_postgres);
//
//        tab_tabla.setTabla("conc_cedula_presupuestaria_fechas", "ide_cedula_presupuestaria", 1);
//        tab_tabla.getColumna("ide_cedula_presupuestaria").setVisible(false);
//        tab_tabla.getColumna("ide_clasificador").setVisible(false);
//        tab_tabla.getColumna("con_ide_clasificador").setVisible(false);
//        tab_tabla.getColumna("pre_descripcion").setVisible(false);
//        List lista = new ArrayList();
//        Object fila1[] = {
//            "1", "Ingreso Consolidado"
//        };
//        Object fila2[] = {
//            "2", "Gatos Programados"
//        };
//        lista.add(fila1);;
//        lista.add(fila2);;
//        tab_tabla.getColumna("tipo").setRadio(lista, "1");
//        tab_tabla.getColumna("ano_curso").setCombo("select ano_curso,ano_curso from conc_ano");
//        tab_tabla.getColumna("reforma1").setVisible(false);
//        tab_tabla.getColumna("compromiso1").setVisible(false);
//        tab_tabla.getColumna("devengado1").setVisible(false);
//        tab_tabla.getColumna("pagado1").setVisible(false);
//        tab_tabla.getColumna("cobrado1").setVisible(false);
//        tab_tabla.getColumna("grupo").setVisible(false);
//        tab_tabla.getColumna("pre_codigo").setVisible(false);
//        tab_tabla.getColumna("val_inicial").setVisible(false);
//         List lista1 = new ArrayList();
//        Object fili1[] = {
//            "1", "1"
//        };
//        Object fili2[] = {
//            "2", "2"
//        };
//        Object fili3[] = {
//            "3", "3"
//        };
//        Object fili4[] = {
//            "4", "4"
//        };
//         Object fili5[] = {
//            "5", "5"
//        };
//        Object fili6[] = {
//            "6", "6"
//        };
//         Object fili7[] = {
//            "7", "7"
//        };
//        lista1.add(fili1);;
//        lista1.add(fili2);;
//        lista1.add(fili3);;
//        lista1.add(fili4);;
//        lista1.add(fili5);;
//        lista1.add(fili6);;
//        lista1.add(fili7);;
//        tab_tabla.getColumna("nivel").setRadio(lista1, "1");
//        tab_tabla.getColumna("cobradoc1").setVisible(false);
//        tab_tabla.getColumna("ide_funcion").setVisible(false);
//        tab_tabla.getColumna("des_funcion").setVisible(false);
//        tab_tabla.getColumna("fechaced").setNombreVisual("FECHA INICIO");
//        tab_tabla.getColumna("cod_funcion").setVisible(false);
//        tab_tabla.setTipoFormulario(true);
//        tab_tabla.dibujar();
//
//        PanelTabla pat_panel = new PanelTabla();
//        pat_panel.setPanelTabla(tab_tabla);
//        agregarComponente(pat_panel);
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_postgres);
        
        tab_tabla1.setTabla("conc_cedula_presupuestaria_fechas", "ide_cedula_presupuestaria", 1);
        tab_tabla1.getColumna("ide_cedula_presupuestaria").setVisible(false);
        tab_tabla1.getColumna("ide_clasificador").setVisible(false);
        tab_tabla1.getColumna("con_ide_clasificador").setVisible(false);
        tab_tabla1.getColumna("pre_descripcion").setVisible(false);
        tab_tabla1.getColumna("tipo").setVisible(false);
        tab_tabla1.getColumna("ano_curso").setVisible(false);
        tab_tabla1.getColumna("reforma1").setVisible(false);
        tab_tabla1.getColumna("compromiso1").setVisible(false);
        tab_tabla1.getColumna("devengado1").setVisible(false);
        tab_tabla1.getColumna("pagado1").setVisible(false);
        tab_tabla1.getColumna("cobrado1").setVisible(false);
        tab_tabla1.getColumna("grupo").setVisible(false);
        tab_tabla1.getColumna("pre_codigo").setVisible(false);
        tab_tabla1.getColumna("val_inicial").setVisible(false);
        List lista2 = new ArrayList();
        Object fil1[] = {
            "1", "1"
        };
        Object fil2[] = {
            "2", "2"
        };
        Object fil3[] = {
            "3", "3"
        };
        Object fil4[] = {
            "4", "4"
        };
         Object fil5[] = {
            "5", "5"
        };
        Object fil6[] = {
            "6", "6"
        };
         Object fil7[] = {
            "7", "7"
        };
        lista2.add(fil1);;
        lista2.add(fil2);;
        lista2.add(fil3);;
        lista2.add(fil4);;
        lista2.add(fil5);;
        lista2.add(fil6);;
        lista2.add(fil7);;
        tab_tabla1.getColumna("nivel").setRadio(lista2, "1");
        tab_tabla1.getColumna("cobradoc1").setVisible(false);
        tab_tabla1.getColumna("ide_funcion").setVisible(false);
        tab_tabla1.getColumna("des_funcion").setVisible(false);
        tab_tabla1.getColumna("fechaced").setNombreVisual("FECHA FINAL");
        tab_tabla1.getColumna("cod_funcion").setVisible(false);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        agregarComponente(pat_panel1);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        
        //Creacion de objeto reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
//        Division div = new Division();
//        div.dividir2(null, pat_panel1, "20%", "h");
//        agregarComponente(div);
//        
    }
 @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
   public void aceptarReporte() {
              //los parametros de este reporte
             Map p_parametros = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("CEDULAS PRESUPUESTARIAS")) {
            if (tab_tabla.getValorSeleccionado() != null) {
                    p_parametros.put("tipo",  tab_tabla.getValor("tipo"));
                    p_parametros.put("ano",  tab_tabla.getValor("ano_curso"));
                    p_parametros.put("niveli",  tab_tabla.getValor("nivel"));                    
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
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
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
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
}
