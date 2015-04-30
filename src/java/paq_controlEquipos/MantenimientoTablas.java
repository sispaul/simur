/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_controlEquipos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import paq_controlEquipos.ejb.Procesos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class MantenimientoTablas extends Pantalla {

    private Tabla tabDetalle = new Tabla();
    private Tabla tabProgramas = new Tabla();
    private Tabla tabLicencia = new Tabla();
    private Tabla tabModelo = new Tabla();
    private Tabla setProveedor = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Dialogo dialogoa = new Dialogo();
    private Grid grid = new Grid();
    private Grid grida = new Grid();
    @EJB
    private Procesos accesoDatos = (Procesos) utilitario.instanciarEJB(Procesos.class);

    public MantenimientoTablas() {

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Tabulador tabTabulador = new Tabulador();
        tabTabulador.setId("tabTabulador");

        Boton botBuscar = new Boton();
        botBuscar.setValue("Proveedor");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("proveedor");
        bar_botones.agregarBoton(botBuscar);
        
        tabDetalle.setId("tabDetalle");
        tabDetalle.setIdCompleto("tabTabulador:tabDetalle");
        tabDetalle.setTabla("cei_detalle_programas", "detalle_codigo", 1);
        tabDetalle.getColumna("progs_codigo").setCombo("select PROGS_CODIGO,PROGS_DESCRIPCION from CEI_PROGRAMAS");
        tabDetalle.getColumna("licen_codigo").setCombo("select TIPO_LICENCIA_CODIGO,TIPO_LICENCIA_DESCRIPCION from CEI_TIPO_LICENCIA");
        tabDetalle.getColumna("licen_codigo").setMetodoChange("casillas");
        tabDetalle.getColumna("modelo_codigo").setCombo("select MODELO_CODIGO,MODELO_DESCRIPCION from CEI_MODELO_LICENCIA");
        tabDetalle.getColumna("modelo_codigo").setMetodoChange("actiCasilla");
        tabDetalle.getColumna("detalle_fecha_compra").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.getColumna("detalle_proveedor").setLectura(true);
        tabDetalle.getColumna("detalle_numero_licencia").setLectura(true);
        tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(true);
        tabDetalle.getColumna("detalle_fecha_compra").setLectura(true);
        tabDetalle.getColumna("modelo_codigo").setLectura(true);
        tabDetalle.getColumna("detalle_codigo").setVisible(false);
        tabDetalle.getColumna("detalle_cod_proveedor").setVisible(false);
        tabDetalle.setTipoFormulario(true);
        tabDetalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.getChildren().add(botBuscar);
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

        dialogoa.setId("dialogoa");
        dialogoa.setTitle("PROVEEDORES"); //titulo
        dialogoa.setWidth("40%"); //siempre en porcentajes  ancho
        dialogoa.setHeight("40%");//siempre porcentaje   alto
        dialogoa.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoa.getBot_aceptar().setMetodo("aceptarDatos");
        grid.setColumns(4);
        agregarComponente(dialogoa);
    }

    public void actiCasilla() {
        TablaGenerica tadDato = accesoDatos.getInfoModelo(Integer.parseInt(tabDetalle.getValor("modelo_codigo")));
        if (!tadDato.isEmpty()) {
            if (tadDato.getValor("MODELO_DESCRIPCION").equals("VOLUMEN")) {
                tabDetalle.getColumna("detalle_numero_licencia").setLectura(false);
                tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(false);
                tabDetalle.getColumna("detalle_fecha_compra").setLectura(false);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            } else {
                tabDetalle.getColumna("detalle_numero_licencia").setLectura(true);
                tabDetalle.getColumna("detalle_tiempo_vigencia").setLectura(true);
                tabDetalle.getColumna("detalle_fecha_compra").setLectura(true);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            }
        }
    }

    public void casillas() {
        TablaGenerica tadDato = accesoDatos.getTipoLicencia(Integer.parseInt(tabDetalle.getValor("licen_codigo")));
        if (!tadDato.isEmpty()) {
            if (tadDato.getValor("TIPO_LICENCIA_DESCRIPCION").equals("CON LICENCIAMIENTO")) {

                tabDetalle.getColumna("modelo_codigo").setLectura(false);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            } else {

                tabDetalle.getColumna("modelo_codigo").setLectura(true);
                utilitario.addUpdate("tabTabulador:tabDetalle");
            }
        }
    }

    public void proveedor() {
        dialogoa.Limpiar();
        dialogoa.setDialogo(grid);
        grida.getChildren().add(setProveedor);
        setProveedor.setId("setProveedor");
        setProveedor.setConexion(conPostgres);
        setProveedor.setSql("SELECT ide_proveedor,ruc,titular from tes_proveedores order by titular");
        setProveedor.getColumna("ruc").setFiltroContenido();
        setProveedor.getColumna("titular").setFiltroContenido();
        setProveedor.setRows(10);
        setProveedor.setTipoSeleccion(false);
        dialogoa.setDialogo(grida);
        setProveedor.dibujar();
        dialogoa.dibujar();
    }

    public void aceptarDatos() {
        TablaGenerica tadDato = accesoDatos.getInfoProveedor(setProveedor.getValorSeleccionado());
        if (!tadDato.isEmpty()) {
            tabDetalle.setValor("detalle_proveedor", tadDato.getValor("titular"));
            tabDetalle.setValor("detalle_cod_proveedor", tadDato.getValor("ide_proveedor"));
            utilitario.addUpdate("tabTabulador:tabDetalle");
            dialogoa.cerrar();
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

    public Tabla getSetProveedor() {
        return setProveedor;
    }

    public void setSetProveedor(Tabla setProveedor) {
        this.setProveedor = setProveedor;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }
    
}
