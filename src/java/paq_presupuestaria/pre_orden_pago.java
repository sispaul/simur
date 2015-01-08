/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
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
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    @EJB
    private Programas programas =(Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_orden_pago() {
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_orden.setId("tab_orden");
        tab_orden.setConexion(con_postgres);
        tab_orden.setHeader("ORDENES DE PAGO");
        tab_orden.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        tab_orden.getColumna("tes_anio").setVisible(false);
        tab_orden.getColumna("tes_mes").setVisible(false);
        tab_orden.getColumna("tes_fecha").setVisible(false);
        tab_orden.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_orden.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_orden.getColumna("tes_estado").setVisible(false);
        tab_orden.getColumna("tes_comprobante_egreso").setMetodoChange("estado");
        
        tab_orden.getColumna("tes_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_orden.getColumna("tes_mes").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_orden.getColumna("tes_fecha").setValorDefecto(utilitario.getFechaActual());
        tab_orden.setTipoFormulario(true);
        tab_orden.getGrid().setColumns(4);
        tab_orden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tab_orden);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setSql("SELECT tes_ide_orden_pago,tes_numero_orden,tes_beneficiario,tes_valor,tes_asunto,tes_acuerdo,tes_nota\n" +
                "FROM tes_orden_pago where tes_anio='"+utilitario.getAnio(utilitario.getFechaActual())+"'");
        tab_detalle.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_detalle.getColumna("tes_numero_orden").setFiltro(true);
        tab_detalle.getColumna("tes_beneficiario").setFiltro(true);
        tab_detalle.setNumeroTabla(2);
        tab_detalle.setLectura(true);
        tab_detalle.setRows(10);
        tab_detalle.agregarRelacion(tab_orden);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir2(pto, ptd, "60%", "h");
        agregarComponente(div);
    }

    @Override
    public void insertar() {
        tab_orden.insertar();
        numero();
    }

    @Override
    public void guardar() {
        String reg = new String();
        tab_orden.guardar();
        con_postgres.guardarPantalla();
        reg = tab_orden.getValorSeleccionado();
        tab_detalle.actualizar();
        tab_detalle.setFilaActual(reg);
        tab_detalle.calcularPaginaActual();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void numero(){
        tab_orden.setValor("tes_numero_orden", programas.maxComprobantes());
        utilitario.addUpdate("tab_orden");
    }
    
    public void estado(){
        tab_orden.setValor("tes_estado", "P");
        utilitario.addUpdate("tab_orden");
    }
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_orden() {
        return tab_orden;
    }

    public void setTab_orden(Tabla tab_orden) {
        this.tab_orden = tab_orden;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
    
}
