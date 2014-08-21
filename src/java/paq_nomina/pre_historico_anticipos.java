/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_historico_anticipos extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Creacion de Arbol
    private Arbol arb_arbol = new Arbol();
    
    //Tabla
    private Tabla tab_anio= new Tabla();
    public pre_historico_anticipos() {
        bar_botones.quitarBotonInsertar();
	bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
	bar_botones.quitarBotonsNavegacion();
        //Encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento1.png");
        bar_botones.agregarComponente(quinde);
        
        //Dibujar Tabla
        tab_anio.setId("tab_anio");
//        tab_anio.setConexion(con_postgres);
        tab_anio.setNumeroTabla(1);
        tab_anio.agregarArbol(arb_arbol);
        tab_anio.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("HISTORIAL DE ANTICIPOS");
        pat_panel.setPanelTabla(tab_anio);
        
        //Dibujar Arbol
        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel, "15%", "V");
        agregarComponente(div_division);  
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

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_anio() {
        return tab_anio;
    }

    public void setTab_anio(Tabla tab_anio) {
        this.tab_anio = tab_anio;
    }
    
}
