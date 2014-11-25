/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_orden_pago extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    private Tabla tab_orden = new Tabla();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Cajas de Texto
    private Texto tcomprobante = new Texto();
    private Calendario cal_fecha = new Calendario();
    
    public pre_orden_pago() {
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_orden_pago,tes_numero_orden_pago,tes_beneficiario,tes_asunto,tes_valor FROM tes_orden_pago");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        tcomprobante.setSize(15);
        gri_busca.getChildren().add(new Etiqueta("# COMPROBANTE DE EGRESO :"));
        gri_busca.getChildren().add(tcomprobante);
        gri_busca.getChildren().add(new Etiqueta("FECHA DE ENTREGA :"));
        gri_busca.getChildren().add(cal_fecha);
        
        Division div = new Division();
        div.dividir2(null, gri_busca, "40%", "V");
        Division div1 = new Division();
        div1.dividir2(div,pan_opcion, "30%", "H");
        agregarComponente(div1);
        dibujaOrden();
    }

    public void dibujaOrden(){
        
        tab_orden.setId("tab_orden");
        tab_orden.setConexion(con_postgres);
        tab_orden.setTabla("tes_orden_pago", "ide_orden_pago", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_orden.setCondicion("ide_orden_pago=-1");
        } else {
            tab_orden.setCondicion("ide_orden_pago=" + aut_busca.getValor());
        }
        tab_orden.getColumna("tes_fecha").setLectura(true);
        tab_orden.getColumna("tes_comprobate_egreso").setLectura(true);
        tab_orden.getColumna("ide_orden_pago").setVisible(false);
        tab_orden.getColumna("tes_orden").setVisible(false);
        tab_orden.setTipoFormulario(true);
        tab_orden.getGrid().setColumns(4);
        tab_orden.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_orden);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru); 
    }
    @Override
    public void insertar() {
        tab_orden.insertar();
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_orden() {
        return tab_orden;
    }

    public void setTab_orden(Tabla tab_orden) {
        this.tab_orden = tab_orden;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }
    
}
