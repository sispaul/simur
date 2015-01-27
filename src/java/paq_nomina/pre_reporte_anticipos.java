/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_reporte_anticipos extends Pantalla{
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    
    public pre_reporte_anticipos() {
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("BUSCAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Actualizarlista");
        bar_botones.agregarBoton(bot_busca);
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_postgres);
        tab_tabla1.setSql("SELECT DISTINCT s.ide_empleado_solicitante,s.ci_solicitante,s.solicitante\n" +
                "FROM srh_solicitud_anticipo AS s ORDER BY s.solicitante ASC");
        tab_tabla1.getColumna("ide_empleado_solicitante").setVisible(false);
        tab_tabla1.getColumna("ci_solicitante").setFiltro(true);
        tab_tabla1.getColumna("solicitante").setFiltro(true);
        tab_tabla1.setRows(26);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(con_postgres);
        tab_tabla2.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 1);
        tab_tabla2.getColumna("porcentaje_descuento_diciembre").setVisible(false);
        tab_tabla2.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_tabla2.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_tabla2.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        
        tab_tabla2.getColumna("usu_anulacion").setVisible(false);
        tab_tabla2.getColumna("usu_pago_anticipado").setVisible(false);
        tab_tabla2.getColumna("usu_cobra_liquidacion").setVisible(false);
        tab_tabla2.getColumna("ide_empleado").setVisible(false);
        tab_tabla2.getColumna("ide_calculo_anticipo").setVisible(false);
        tab_tabla2.setLectura(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1,pat_panel2, "28%", "V");
        agregarComponente(div_division);
    }

    public void Actualizarlista(){
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tabla2.setCondicion(getFiltrosAcceso());
            tab_tabla2.ejecutarSql();
            utilitario.addUpdate("tab_tabla2");
        }
    }
    
    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tab_tabla1.getValorSeleccionado() != null
                ) {

			str_filtros = "ide_empleado = "
					+ String.valueOf(tab_tabla1.getValorSeleccionado());

		} else {
			utilitario.agregarMensajeInfo("Filtros no válidos",
					"Debe ingresar los fitros de vehiculo y servicio");
		}
		return str_filtros;
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}
