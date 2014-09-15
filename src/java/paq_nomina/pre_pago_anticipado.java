/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_nomina.ejb.AntiSueldos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_pago_anticipado extends Pantalla{

    //Conexio a base de datos
    private Conexion con_postgres = new Conexion();
    
    //Para tabla
    private Tabla tab_pago = new Tabla();
    
    //Extrae datos adicionales, que se necesita, de una clase logica
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    
    public pre_pago_anticipado() {
    
        //Cadena de conexion par aotra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_pago.setId("tab_pago");
        tab_pago.setConexion(con_postgres);
        tab_pago.setTabla("srh_pago_anticipado", "ide_pago_anticipo", 1);
        tab_pago.getColumna("ci_solicitante").setMetodoChange("buscar");
        tab_pago.setTipoFormulario(true);
        tab_pago.getGrid().setColumns(4);
        tab_pago.dibujar();
        
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tab_pago);
        agregarComponente(tpg);
        
    }

    public void buscar (){
        TablaGenerica tab_dato = iAnticipos.pagosAndelantados(tab_pago.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            tab_pago.setValor("solicitante", tab_dato.getValor("solicitante"));
            tab_pago.setValor("rmu", tab_dato.getValor("rmu"));
            tab_pago.setValor("valor_anticipo", tab_dato.getValor("valor_anticipo"));
            tab_pago.setValor("numero_cuotas_anticipo", tab_dato.getValor("numero_cuotas_anticipo"));
            tab_pago.setValor("valor_pagado_anticipo", tab_dato.getValor("valor_pagado"));
            tab_pago.setValor("numero_cuotas_pagadas", tab_dato.getValor("numero_cuotas_pagadas"));
            tab_pago.setValor("saldo", tab_dato.getValor("saldo"));
            utilitario.addUpdate("tab_pago");
        }else {
            utilitario.agregarMensajeInfo("No existen Datos", "");
        }     
    }
    
    @Override
    public void insertar() {
        tab_pago.insertar();
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

    public Tabla getTab_pago() {
        return tab_pago;
    }

    public void setTab_pago(Tabla tab_pago) {
        this.tab_pago = tab_pago;
    }
    
}
