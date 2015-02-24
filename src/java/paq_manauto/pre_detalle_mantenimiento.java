/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import paq_manauto.ejb.manauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_detalle_mantenimiento extends Pantalla {

    //Conexion a base
    private Conexion con_postgres = new Conexion();
    //Declaraciones de tablas
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    //Declaracion Texto
    private Texto ttotal = new Texto();
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);

    public pre_detalle_mantenimiento() {
        ttotal.setId("ttotal");

        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_postgres);
        tab_cabecera.setSql("SELECT c.mca_codigo,c.mca_secuencial,c.mca_tipo_mantenimiento,'Codigo_Placa:  '||v.mve_placa||'; Marca:  '||m.mvmarca_descripcion||'; Tipo:  '||\n"
                + "t.tipo_combustible_descripcion||'; Modelo:  '||\n"
                + "o.mvmodelo_descripcion||'; Versión: '||\n"
                + "r.mvversion_descripcion as descripcion,\n"
                + "c.mca_autorizado,\n"
                + "c.mca_proveedor,\n"
                + "c.mca_responsable,\n"
                + "c.mca_detalle,\n"
                + "c.mca_observacion\n"
                + "FROM mvcab_mantenimiento c,mv_vehiculo v\n"
                + "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n"
                + "INNER JOIN mvtipo_combustible t ON v.tipo_combustible_id = t.tipo_combustible_id\n"
                + "INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id\n"
                + "INNER JOIN mvversion_vehiculo r ON r.mvmodelo_id = o.mvmodelo_id AND v.mvversion_id = r.mvversion_id\n"
                + "WHERE c.mca_estado_registro = 'Solicitud' and c.mve_secuencial = v.mve_secuencial");
        tab_cabecera.getColumna("mca_codigo").setVisible(false);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.setLectura(true);
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);

        Boton bot_end = new Boton();
        bot_end.setValue("Terminar Solictud");
        bot_end.setIcon("ui-icon-disk");
        bot_end.setMetodo("termina");
        bar_botones.agregarBoton(bot_end);

        Grid gri_total = new Grid();
        gri_total.setColumns(6);
        gri_total.getChildren().add(bot_end);
        gri_total.getChildren().add(new Etiqueta("Valor Total: "));
        gri_total.getChildren().add(ttotal);
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setTabla("mvdetalle_mantenimiento", "mde_codigo", 1);
        tab_detalle.getColumna("mde_cod_articulo").setCombo("SELECT m.ide_mat_bodega,m.cod_material,m.des_material,a.costo_actual,e.und_medida \n" + "FROM bodc_material m\n"
                + "INNER JOIN bodt_articulos a ON a.ide_mat_bodega = m.ide_mat_bodega \n"
                + "inner join valc_medida e on m.ide_medida = e.ide_medida\n"
                + "order by m.des_material");
        tab_detalle.getColumna("mde_cod_articulo").setAutoCompletar();
        tab_detalle.getColumna("mde_cod_articulo").setMetodoChange("valor");
        tab_detalle.getColumna("mde_cantidad").setMetodoChange("calculo");
        tab_detalle.getColumna("mde_articulo").setVisible(false);
        tab_detalle.getColumna("mde_fechacomp").setVisible(false);
        tab_detalle.getColumna("mde_comprobante").setVisible(false);
        tab_detalle.setRows(10);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.getChildren().add(gri_total);
        ptd.setPanelTabla(tab_detalle);

        Division div = new Division();
        div.dividir2(ptc, ptd, "40%", "H");
        agregarComponente(div);
    }

    public void valor() {
        TablaGenerica tab_dato = aCombustible.getDetaArticulos(Integer.parseInt(tab_detalle.getValor("mde_cod_articulo")));
        if (!tab_dato.isEmpty()) {
            tab_detalle.setValor("mde_valor", tab_dato.getValor("costo_actual"));
            utilitario.addUpdate("tab_detalle");
        }
    }

    public void calculo() {
        TablaGenerica tab_dato = aCombustible.getDetaArticulos(Integer.parseInt(tab_detalle.getValor("mde_cod_articulo")));
        if (!tab_dato.isEmpty()) {
            Double valor = 0.0;
            valor = Double.valueOf(tab_dato.getValor("costo_actual")) * Double.valueOf(tab_detalle.getValor("mde_cantidad"));
            tab_detalle.setValor("mde_total", String.valueOf(Math.rint(valor * 100) / 100));
            utilitario.addUpdate("tab_detalle");
            suma();
        }
    }

    public void suma() {
        ttotal.clearInitialState();
        ttotal.limpiar();
        Double valor = 0.0;
        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            tab_detalle.getValor(i, "MDE_TOTAL");
            valor = Double.valueOf(tab_detalle.getSumaColumna("MDE_TOTAL"));
        }
        ttotal.setValue(valor);
        utilitario.addUpdate("ttotal");
    }

    public void termina() {
        TablaGenerica tab_dato = aCombustible.getDetaArticus(Integer.parseInt(tab_cabecera.getValor("mca_codigo")));
        if (!tab_dato.isEmpty()) {
            aCombustible.set_updateSolicitud(Integer.parseInt(tab_cabecera.getValor("mca_codigo")));
            tab_cabecera.actualizar();
        } else {
            utilitario.agregarMensaje("Agregar al Menos Motivo de mantenimiento", "");
        }
    }

    @Override
    public void insertar() {
        tab_detalle.insertar();
    }

    @Override
    public void guardar() {
        if (tab_detalle.guardar()) {
            con_postgres.guardarPantalla();
        }
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

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
}
