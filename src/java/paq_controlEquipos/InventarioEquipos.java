/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import paq_controlEquipos.ejb.Procesos;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class InventarioEquipos extends Pantalla {

    private Tabla tabEquipo = new Tabla();
    private Tabla tabAccesorio = new Tabla();
    private Tabla tabAsignacion = new Tabla();
    
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public InventarioEquipos() {

        Tabulador tabTabulador = new Tabulador();
        tabTabulador.setId("tabTabulador");

        tabEquipo.setId("tabEquipo");
        tabEquipo.setTabla("cei_descripcion_equipos", "desc_codigo", 1);
        tabEquipo.getColumna("tipo_codigo").setCombo("SELECT TIPO_CODIGO,TIPO_DESCRIPCION FROM CEI_TIPO_EQUIPOS");
        tabEquipo.getColumna("desc_ultimo_mantenimiento").setLectura(true);
        tabEquipo.getColumna("desc_serie").setMetodoChange("infEquipo");
        tabEquipo.agregarRelacion(tabAccesorio);
        tabEquipo.setTipoFormulario(true);
        tabEquipo.getGrid().setColumns(4);
        tabEquipo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tabEquipo);

        tabAccesorio.setId("tabAccesorio");
        tabAccesorio.setIdCompleto("tabTabulador:tabAccesorio");
        tabAccesorio.setTabla("cei_accesorios", "acce_codigo", 2);
        tabAccesorio.getColumna("acce_serie").setMetodoChange("infAccesorio");
        tabAccesorio.dibujar();
        PanelTabla pta = new PanelTabla();
        pta.setPanelTabla(tabAccesorio);
        
        tabAsignacion.setId("tabAsignacion");
        tabAsignacion.setTabla("cei_asignacion", "asignacion_codigo", 3);
        tabAsignacion.setTipoFormulario(true);
        tabAsignacion.dibujar();
        PanelTabla ptas = new PanelTabla();
        ptas.setPanelTabla(tabAsignacion);
        
        
        tabTabulador.agregarTab("ACCESORIOS", pta);
        tabTabulador.agregarTab("ASIGNACIÓN", ptas);
        
        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir2(pte, tabTabulador, "50%", "H");
        agregarComponente(divTablas);

    }

    public void infEquipo() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo(tabEquipo.getValor("desc_serie"));
        if (!tabDato.isEmpty()) {
            tabEquipo.setValor("desc_codigo_activo", tabDato.getValor("codigo"));
            tabEquipo.setValor("desc_marca", tabDato.getValor("marca"));
            tabEquipo.setValor("desc_modelo", tabDato.getValor("modelo"));
            tabEquipo.setValor("desc_estado", tabDato.getValor("estado"));
            tabEquipo.setValor("desc_descripcion", tabDato.getValor("des_activo"));
            utilitario.addUpdate("tabEquipo");
        } else {
            utilitario.agregarMensaje("Equipo No Localizado en la Base de Activos", null);
        }
    }

    public void infAccesorio() {
        TablaGenerica tabDato = accesoDatos.getInfoActivo(tabAccesorio.getValor("desc_serie"));
        if (!tabDato.isEmpty()) {
            tabAccesorio.setValor("acce_codigo_activo", tabDato.getValor("codigo"));
            tabAccesorio.setValor("acce_marca", tabDato.getValor("marca"));
            tabAccesorio.setValor("acce_modelo", tabDato.getValor("modelo"));
            tabAccesorio.setValor("acce_estado", tabDato.getValor("estado"));
            tabAccesorio.setValor("acce_descripcion", tabDato.getValor("des_activo"));
            utilitario.addUpdate("tabAccesorio");
        } else {
            utilitario.agregarMensaje("Accesorio No Localizado en la Base de Activos", null);
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabEquipo.guardar()) {
            if (tabAccesorio.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabEquipo() {
        return tabEquipo;
    }

    public void setTabEquipo(Tabla tabEquipo) {
        this.tabEquipo = tabEquipo;
    }

    public Tabla getTabAccesorio() {
        return tabAccesorio;
    }

    public void setTabAccesorio(Tabla tabAccesorio) {
        this.tabAccesorio = tabAccesorio;
    }

    public Tabla getTabAsignacion() {
        return tabAsignacion;
    }

    public void setTabAsignacion(Tabla tabAsignacion) {
        this.tabAsignacion = tabAsignacion;
    }
    
}
