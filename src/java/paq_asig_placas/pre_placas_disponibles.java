/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_placas_disponibles extends Pantalla {

    //declaracion de tablas
    private Tabla tab_placas = new Tabla();
    //combos de seleccion
    private Combo cmb_vehiculo = new Combo();
    private Combo cmb_servicio = new Combo();
    private Combo cmb_tipo = new Combo();

    public pre_placas_disponibles() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        bar_botones.agregarComponente(new Etiqueta("Vehiculo :"));
        bar_botones.agregarComponente(cmb_vehiculo);
        bar_botones.agregarSeparador();

        bar_botones.agregarComponente(new Etiqueta("Servicio :"));
        bar_botones.agregarComponente(cmb_servicio);
        bar_botones.agregarSeparador();

        bar_botones.agregarComponente(new Etiqueta("Tipo Placa :"));
        bar_botones.agregarComponente(cmb_tipo);
        bar_botones.agregarSeparador();

        cmb_vehiculo.setId("cmb_vehiculo");
        cmb_vehiculo.setCombo("SELECT IDE_TIPO_VEHICULO, DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        cmb_vehiculo.setMetodo("servicio");

        cmb_servicio.setId("cmb_servicio");
        cmb_servicio.setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");

        cmb_tipo.setId("cmb_tipo");
        cmb_tipo.setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");

        Boton bot_busca = new Boton();
        bot_busca.setValue("MOSTRAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Actualizarlista");
        bar_botones.agregarBoton(bot_busca);

        tab_placas.setId("tab_placas");
        tab_placas.setTabla("TRANS_PLACAS", "IDE_PLACA", 1);
        tab_placas.getColumna("IDE_TIPO_PLACA").setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");
        tab_placas.getColumna("IDE_TIPO_VEHICULO").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_SERVICIO").setVisible(false);
        tab_placas.getColumna("IDE_INGRESO_PLACAS").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_ESTADO").setVisible(false);
        tab_placas.getColumna("CEDULA_RUC_PROPIETARIO").setVisible(false);
        tab_placas.getColumna("FECHA_REGISTRO_PLACA").setVisible(false);
        tab_placas.getColumna("NOMBRE_PROPIETARIO").setVisible(false);
        tab_placas.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_PLACA2").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_ESTADO2").setVisible(false);
        tab_placas.getColumna("FECHA_DEFINITIVA_PLACA").setVisible(false);
        tab_placas.getColumna("USU_ENTREGA").setVisible(false);
        tab_placas.getColumna("FECHA_ENTREGA_FINAL").setVisible(false);
        tab_placas.setHeader("Inventario de Placas Disponibles");
        tab_placas.setLectura(true);
        tab_placas.setRows(30);
        tab_placas.dibujar();
        PanelTabla tbp_d = new PanelTabla();
        tbp_d.setPanelTabla(tab_placas);
        Grupo gru = new Grupo();
        gru.getChildren().add(tbp_d);
        agregarComponente(gru);
    }

    public void servicio() {
        cmb_servicio.setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO where IDE_TIPO_VEHICULO= " + cmb_vehiculo.getValue());
        utilitario.addUpdate("cmb_servicio");
    }

    public void Actualizarlista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_placas.setCondicion(getFiltrosAcceso());
            tab_placas.ejecutarSql();
            utilitario.addUpdate("tab_placas");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cmb_vehiculo.getValue() != null
                && cmb_servicio.getValue() != null) {

            str_filtros = " IDE_TIPO_VEHICULO = "
                    + cmb_vehiculo.getValue();
            str_filtros += " AND IDE_TIPO_SERVICIO = "
                    + cmb_servicio.getValue();
            str_filtros += " AND IDE_TIPO_ESTADO = 2";
            str_filtros += " AND IDE_TIPO_PLACA = "
                    + cmb_tipo.getValue();

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

    public Tabla getTab_placas() {
        return tab_placas;
    }

    public void setTab_placas(Tabla tab_placas) {
        this.tab_placas = tab_placas;
    }
}
