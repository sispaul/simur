/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
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
public class MantenimientoTablas extends Pantalla {

    private Tabla tabDetalle = new Tabla();
    private Tabla tabProgramas = new Tabla();
    private Tabla tabLicencia = new Tabla();
    private Tabla tabModelo = new Tabla();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public MantenimientoTablas() {

        Tabulador tabTabulador = new Tabulador();
        tabTabulador.setId("tabTabulador");


        tabDetalle.setId("tabDetalle");
        tabDetalle.setIdCompleto("tabTabulador:tabDetalle");
        tabDetalle.setTabla("cei_detalle_programas", "detalle_codigo", 1);
        tabDetalle.getColumna("progs_codigo").setCombo("select PROGS_CODIGO,PROGS_DESCRIPCION from CEI_PROGRAMAS");
        tabDetalle.getColumna("licen_codigo").setCombo("select TIPO_LICENCIA_CODIGO,TIPO_LICENCIA_DESCRIPCION from CEI_TIPO_LICENCIA");
        tabDetalle.getColumna("licen_codigo").setMetodoChange("casillas");
        tabDetalle.getColumna("modelo_codigo").setCombo("select MODELO_CODIGO,MODELO_DESCRIPCION from CEI_MODELO_LICENCIA");
        tabDetalle.getColumna("detalle_fecha_compra").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.getColumna("detalle_numero_licencia").setLectura(true);
        tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(true);
        tabDetalle.getColumna("detalle_fecha_compra").setLectura(true);
        tabDetalle.getColumna("modelo_codigo").setLectura(true);
        tabDetalle.getColumna("detalle_codigo").setVisible(false);
        tabDetalle.setTipoFormulario(true);
        tabDetalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tabDetalle);

        tabProgramas.setId("tabProgramas");
        tabProgramas.setIdCompleto("tabTabulador:tabProgramas");
        tabProgramas.setTabla("cei_programas", "progs_codigo", 2);
        tabProgramas.dibujar();
        PanelTabla ptp = new PanelTabla();
        ptp.setPanelTabla(tabProgramas);

        tabLicencia.setId("tabLicencia");
        tabLicencia.setIdCompleto("tabTabulador:tabLicencia");
        tabLicencia.setTabla("cei_tipo_licencia", "tipo_licencia_codigo", 3);
        tabLicencia.dibujar();
        PanelTabla ptl = new PanelTabla();
        ptl.setPanelTabla(tabLicencia);

        tabModelo.setId("tabModelo");
        tabModelo.setIdCompleto("tabTabulador:tabModelo");
        tabModelo.setTabla("cei_modelo_licencia", "modelo_codigo", 4);
        tabModelo.dibujar();
        PanelTabla ptm = new PanelTabla();
        ptm.setPanelTabla(tabModelo);

        tabTabulador.agregarTab("CATALOGO DE PROGRAMAS", ptd);
        tabTabulador.agregarTab("PROGRAMAS", ptp);
        tabTabulador.agregarTab("LICENCIAS", ptl);
        tabTabulador.agregarTab("TIPOS", ptm);
        agregarComponente(tabTabulador);
    }

    public void casillas() {
        TablaGenerica tadDato = accesoDatos.getTipoLicencia(Integer.parseInt(tabDetalle.getValor("licen_codigo")));
        if (!tadDato.isEmpty()) {
            if (tadDato.getValor("TIPO_LICENCIA_DESCRIPCION").equals("CON LICENCIAMIENTO")) {
                tabDetalle.getColumna("detalle_numero_licencia").setLectura(false);
                tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(false);
                tabDetalle.getColumna("detalle_fecha_compra").setLectura(false);
                tabDetalle.getColumna("modelo_codigo").setLectura(false);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            } else {
                tabDetalle.getColumna("detalle_numero_licencia").setLectura(true);
                tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(true);
                tabDetalle.getColumna("detalle_fecha_compra").setLectura(true);
                tabDetalle.getColumna("modelo_codigo").setLectura(true);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            }
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabDetalle.guardar()) {
            if (tabProgramas.guardar()) {
                if (tabLicencia.guardar()) {
                    if (tabModelo.guardar()) {
                        guardarPantalla();
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public Tabla getTabProgramas() {
        return tabProgramas;
    }

    public void setTabProgramas(Tabla tabProgramas) {
        this.tabProgramas = tabProgramas;
    }

    public Tabla getTabLicencia() {
        return tabLicencia;
    }

    public void setTabLicencia(Tabla tabLicencia) {
        this.tabLicencia = tabLicencia;
    }

    public Tabla getTabModelo() {
        return tabModelo;
    }

    public void setTabModelo(Tabla tabModelo) {
        this.tabModelo = tabModelo;
    }
}
