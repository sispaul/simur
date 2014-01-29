/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
    public class pre_report extends Pantalla{
    private Combo cmb_combo = new Combo();
    private Conexion con_postgres= new Conexion();
    private Etiqueta eti_etiqueta= new Etiqueta();
    private Etiqueta eti_etiqueta2= new Etiqueta();
    private Boton bot_imprimir = new Boton();

    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Tabla tab_consulta = new Tabla();

    public pre_report() {
        bar_botones.limpiar();
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        
        Grid gri_grid = new Grid();
        gri_grid.setColumns(2);
        
        eti_etiqueta.setValue("Seleccione Grupo: ");
        gri_grid.getChildren().add(eti_etiqueta);
        
        cmb_combo.setId("cmb_combo");
        //cmb_combo.setConexion(con_postgres);
        cmb_combo.setCombo("SELECT id_grupo,descripcion from bodc_grupo");
        gri_grid.getChildren().add(cmb_combo);
        
        eti_etiqueta2.setValue("LISTADO DE ARTICULOS POR GRUPO");
        gri_grid.getChildren().add(eti_etiqueta2);
        
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setMetodo("imprimirReporte");
        gri_grid.getChildren().add(bot_imprimir);
        
        agregarComponente(gri_grid);
        
        /**
         * CONFIGURACIÓN DE ONJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        //Seleccionar Usuario Actual
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
    }
    
     public void imprimirReporte(){
        
        //los parametros de este reporte
                rep_reporte.setNombre("LISTADO DE ARTICULOS POR GRUPO");
                rep_reporte.setPath("rep_bodega/rep_grupos.jasper");
                p_parametros = new HashMap();
                p_parametros.put("p_grupo", cmb_combo.getValue()+"");
                p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
                //utilitario.agregarMensaje("DIRECCION", cmb_combo.getValue()+"");
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            
        
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

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    public Combo getCmb_combo() {
        return cmb_combo;
    }

    public void setCmb_combo(Combo cmb_combo) {
        this.cmb_combo = cmb_combo;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
