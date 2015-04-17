/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class OrdenPago extends Pantalla {
//Conexion a base

    private Conexion conPostgres = new Conexion();
    //Tabla Normal
    private Tabla tabOrden = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Tabla tabConsulta = new Tabla();
    //Dialogo Busca 
    private Dialogo dialogo = new Dialogo();
    private Dialogo dialogm = new Dialogo();
    private Grid gridd = new Grid();
    private Grid gridm = new Grid();
    private Grid grid = new Grid();
    private Grid grim = new Grid();
    //Texto de Ingreso
    Texto textMotivo = new Texto();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public OrdenPago() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();
        //usuario actual del sistema
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setExcluirLectura(true);
        bot_anular.setIcon("ui-icon-cancel");
        bot_anular.setMetodo("quitar");
        bar_botones.agregarBoton(bot_anular);

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tabOrden.setId("tabOrden");
        tabOrden.setConexion(conPostgres);
        tabOrden.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        tabOrden.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
        tabOrden.getColumna("tes_id_proveedor").setFiltroContenido();
        tabOrden.getColumna("tes_id_proveedor").setMetodoChange("proveedor");
        tabOrden.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado order by nombres");
        tabOrden.getColumna("tes_cod_empleado").setFiltroContenido();
        tabOrden.getColumna("tes_cod_empleado").setMetodoChange("empleado");
        tabOrden.getColumna("tes_estado_doc").setValorDefecto("1");
        tabOrden.getColumna("tes_letrero").setEtiqueta();
        tabOrden.getColumna("tes_anio").setVisible(false);
        tabOrden.getColumna("tes_mes").setVisible(false);
        tabOrden.getColumna("tes_fecha_ingreso").setVisible(false);
        tabOrden.getColumna("tes_ide_orden_pago").setVisible(false);
        tabOrden.getColumna("tes_ide_orden_pago").setVisible(false);
        tabOrden.getColumna("tes_proveedor").setVisible(false);
        tabOrden.getColumna("tes_estado_doc").setVisible(false);
        tabOrden.getColumna("tes_empleado").setVisible(false);
        tabOrden.getColumna("tipo_solicitantep").setVisible(false);
        tabOrden.getColumna("tes_login_ing").setVisible(false);
        tabOrden.getColumna("tes_login_anu").setVisible(false);
        tabOrden.getColumna("tes_fecha_anu").setVisible(false);
        tabOrden.getColumna("tes_login_actu").setVisible(false);
        tabOrden.getColumna("tes_fecha_actu").setVisible(false);
        tabOrden.getColumna("tes_login_envio").setVisible(false);
        tabOrden.getColumna("tes_comentario_anula").setVisible(false);
        tabOrden.getColumna("tes_login_mod").setVisible(false);
        tabOrden.getColumna("tes_fecha_mod").setVisible(false);
        tabOrden.getColumna("tes_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabOrden.getColumna("tes_mes").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tabOrden.getColumna("tes_fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("tes_login_ing").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("tes_valor").setMetodoChange("numLetras");
        tabOrden.getColumna("tes_comprobante_egreso").setMetodoChange("comprobante");
        tabOrden.setTipoFormulario(true);
        tabOrden.getGrid().setColumns(4);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        agregarComponente(pto);

        //para poder buscar por apellido el solicitante
        dialogm.setId("dia_dialogm");
        dialogm.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        dialogm.setWidth("30%"); //siempre en porcentajes  ancho
        dialogm.setHeight("20%");//siempre porcentaje   alto
        dialogm.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogm.getBot_aceptar().setMetodo("anular");
        gridm.setColumns(4);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setConexion(conPostgres);
        tabDetalle.setSql("select \n"
                + "a.tes_ide_orden_pago,\n"
                + "a.numero_orden,\n"
                + "a.numero_comprobante,\n"
                + "(case when b.titular is not null then b.titular when b.titular is null then c.nombres end ) as beneficiario,\n"
                + "a.asunto,\n"
                + "a.valor,\n"
                + "a.concepto,\n"
                + "a.acuerdo\n"
                + "from (\n"
                + "SELECT \n"
                + "tes_numero_orden AS numero_orden,\n"
                + "tes_comprobante_egreso AS numero_comprobante,\n"
                + "tes_asunto AS asunto,\n"
                + "tes_valor AS valor,\n"
                + "tes_concepto AS concepto,\n"
                + "tes_acuerdo AS acuerdo,\n"
                + "tes_ide_orden_pago,\n"
                + "(case when tes_id_proveedor is null then tes_cod_empleado when tes_id_proveedor is not null then tes_id_proveedor end) as cod_benefi\n"
                + "FROM tes_orden_pago where tes_estado_doc = '1' and tes_anio='" + utilitario.getAnio(utilitario.getFechaActual()) + "') as a\n"
                + "left join \n"
                + "(SELECT ide_proveedor,titular FROM tes_proveedores) as b\n"
                + "on a.cod_benefi = b.ide_proveedor\n"
                + "left join \n"
                + "(SELECT cedula_pass,nombres,cod_empleado FROM srh_empleado) as c\n"
                + "on a.cod_benefi = c.cod_empleado\n"
                + "order by numero_orden desc");
        tabDetalle.getColumna("tes_ide_orden_pago").setVisible(false);
        tabDetalle.getColumna("numero_orden").setFiltroContenido();
        tabDetalle.getColumna("beneficiario").setFiltroContenido();
        tabDetalle.getColumna("acuerdo").setFiltroContenido();
        tabDetalle.getColumna("numero_comprobante").setFiltroContenido();
        tabDetalle.getColumna("acuerdo").setLongitud(50);
        tabDetalle.agregarRelacion(tabOrden);
        tabDetalle.setNumeroTabla(2);
        tabDetalle.setLectura(true);
        tabDetalle.setRows(7);
        tabDetalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tabDetalle);

        Division div = new Division();
        div.dividir2(pto, ptd, "55%", "h");
        agregarComponente(div);

        // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);
    }

    public void numLetras() {
        Object objeto = (Object) tabOrden.getValor("tes_valor");
        tabOrden.setValor("tes_valor_letras", utilitario.getLetrasDolarNumero((objeto)));
        utilitario.addUpdate("tabOrden");//actualiza solo componentes
    }

    public void proveedor() {
        if (tabOrden.getValor("tes_id_proveedor") != null) {
            TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tabOrden.getValor("tes_id_proveedor")));
            try {
                Thread.sleep(1000);
                if (!tab_dato.isEmpty()) {
                    tabOrden.setValor("tes_proveedor", tab_dato.getValor("titular"));
                    tabOrden.setValor("tes_cod_empleado", null);
                    tabOrden.setValor("tes_empleado", null);
                    tabOrden.setValor("tes_estado", "Pendiente");
                    tabOrden.setValor("tipo_solicitantep", "2");
                    utilitario.addUpdate("tabOrden");//actualiza solo componente
                    System.out.println("Nombre de proveedor>>>>>>\t" + tabOrden.getValor("tes_proveedor"));
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            tabOrden.getColumna("tes_cod_empleado").setLectura(false);
            utilitario.addUpdate("tabOrden");//actualiza solo componentes
        }
    }

    public void empleado() {
        if (tabOrden.getValor("tes_cod_empleado") != null) {
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tabOrden.getValor("tes_cod_empleado")));
            try {
                Thread.sleep(1000);
                if (!tab_dato.isEmpty()) {
                    tabOrden.setValor("tes_empleado", tab_dato.getValor("nombres"));
                    tabOrden.setValor("tes_id_proveedor", null);
                    tabOrden.setValor("tes_proveedor", null);
                    tabOrden.setValor("tes_estado", "Pendiente");
                    tabOrden.setValor("tipo_solicitantep", "1");
                    utilitario.addUpdate("tabOrden");//actualiza solo componentes
                    System.out.println("Nombre de empleado>>>>>>\t" + tabOrden.getValor("tes_empleado"));
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            tabOrden.getColumna("tes_id_proveedor").setLectura(false);
            utilitario.addUpdate("tabOrden");//actualiza solo componentes
        }
    }

    public void quitar() {
        dialogm.Limpiar();
        dialogm.setDialogo(grim);
        textMotivo.setSize(50);
        grim.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN :"));
        grim.getChildren().add(new Etiqueta("_____________________________________________________"));
        grim.getChildren().add(textMotivo);
        dialogm.setDialogo(gridm);
        dialogm.dibujar();
    }

    public void anular() {
        String reg = new String();
        programas.actOrden(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), tabConsulta.getValor("NICK_USUA"), textMotivo.getValue() + "");
        utilitario.addUpdate("tabOrden");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        reg = tabOrden.getValorSeleccionado();
        tabDetalle.actualizar();
        tabDetalle.setFilaActual(reg);
        tabDetalle.calcularPaginaActual();
        dialogm.cerrar();
    }

    public void numero() {
        String numero = programas.maxComprobantes();
        String num;
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "000000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "00000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "0000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = "00" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100000 && cantidad <= 999999) {
                num = "0" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000000 && cantidad <= 9999999) {
                num = String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            }
        }
        utilitario.addUpdate("tabOrden");
    }

    public void comprobante() {
        if (tabOrden.getValor("tes_estado") != "Anulada") {
            tabOrden.setValor("tes_estado", "Pagado");
            utilitario.addUpdate("tabOrden");
        } else {
            utilitario.agregarMensajeInfo("Comprobante No Puede Ser Pagado", "Se Encuentra Anulado");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        String reg = new String();
        TablaGenerica tabDato = programas.getorden_valida(tabOrden.getValor("tes_numero_orden"));
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("tes_estado").equals("Anulada")) {
                utilitario.agregarMensajeInfo("Comprobante No Puede Ser Pagado", "Se Encuentra Anulado");
            } else {
                try {
                    if (tabOrden.getValor("tes_cod_empleado") != null || tabOrden.getValor("tes_cod_empleado").equals(tabDato.getValor("tes_cod_empleado"))) {
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_cod_empleado", tabOrden.getValor("tes_cod_empleado"));
                    }
                } catch (NullPointerException ex) {
                    programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_cod_empleado", null);
                }
                try {
                    if (tabOrden.getValor("tes_empleado") != null || tabOrden.getValor("tes_empleado").equals(tabDato.getValor("tes_empleado"))) {
                        utilitario.addUpdate("tabOrden");//actualiza solo componentes
                        System.out.println("Nombre de empleado>>>>>>\t" + tabOrden.getValor("tes_empleado"));
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_empleado", "'" + tabOrden.getValor("tes_empleado") + "'");
                    }
                } catch (NullPointerException ex) {
                    programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_empleado", null);
                }
                try {
                    if (tabOrden.getValor("tes_id_proveedor") != null || tabOrden.getValor("tes_id_proveedor").equals(tabDato.getValor("tes_id_proveedor"))) {
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_id_proveedor", tabOrden.getValor("tes_id_proveedor"));
                    }
                } catch (NullPointerException ex) {
                    programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_id_proveedor", null);
                }
                try {
                    if (tabOrden.getValor("tes_proveedor") != null || tabOrden.getValor("tes_proveedor").equals(tabDato.getValor("tes_proveedor"))) {
                        utilitario.addUpdate("tabOrden");//actualiza solo componente
                        System.out.println("Nombre de proveedor>>>>>>\t" + tabOrden.getValor("tes_proveedor"));
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_proveedor", "'" + tabOrden.getValor("tes_proveedor") + "'");
                    }
                } catch (NullPointerException ex) {
                    programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_proveedor", null);
                }
                try {
                    if (tabOrden.getValor("tes_valor") != null || tabOrden.getValor("tes_valor").equals(tabDato.getValor("tes_valor"))) {
                        programas.setOrdenPagos(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_valor", Double.parseDouble(tabOrden.getValor("tes_valor")));
                    }
                } catch (NullPointerException ex) {
                    System.err.println(tabOrden.getValor("tes_valor"));
                }
                try {
                    if (tabOrden.getValor("tes_comprobante_egreso") != null || tabOrden.getValor("tes_comprobante_egreso").equals(tabDato.getValor("tes_comprobante_egreso"))) {
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_comprobante_egreso", "'" + tabOrden.getValor("tes_comprobante_egreso") + "'");
                    }
                } catch (NullPointerException ex) {
                    System.err.println(tabOrden.getValor("tes_comprobante_egreso"));
                }
                try {
                    if (tabOrden.getValor("tes_fecha_comprobante") != null || tabOrden.getValor("tes_fecha_comprobante").equals(tabDato.getValor("tes_fecha_comprobante"))) {
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_fecha_comprobante", "'" + tabOrden.getValor("tes_fecha_comprobante") + "'");
                    }
                } catch (NullPointerException ex) {
                    System.err.println(tabOrden.getValor("tes_fecha_comprobante"));
                }
                try {
                    if (tabOrden.getValor("tes_fecha_envio") != null || tabOrden.getValor("tes_fecha_envio").equals(tabDato.getValor("tes_fecha_envio"))) {
                        programas.setOrdenPago(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), "tes_fecha_envio", "'" + tabOrden.getValor("tes_fecha_envio") + "'");
                    }
                } catch (NullPointerException ex) {
                    System.err.println(tabOrden.getValor("tes_fecha_envio"));
                }
                reg = tabOrden.getValorSeleccionado();
                tabDetalle.actualizar();
                tabDetalle.setFilaActual(reg);
                tabDetalle.calcularPaginaActual();
                utilitario.agregarMensaje("Registro Actualizado", "");
            }
        } else {
            if (tabDato.getValor("tes_ide_orden_pago") != null) {
            } else {
                if (tabOrden.guardar()) {
                    conPostgres.guardarPantalla();
                    reg = tabOrden.getValorSeleccionado();
                    tabDetalle.actualizar();
                    tabDetalle.setFilaActual(reg);
                    tabDetalle.calcularPaginaActual();
                }
            }
        }
    }

    @Override
    public void eliminar() {
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("id_orden", tabOrden.getValor("tes_numero_orden") + "");
                p_parametros.put("id_documento", Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago") + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabOrden() {
        return tabOrden;
    }

    public void setTabOrden(Tabla tabOrden) {
        this.tabOrden = tabOrden;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }
}
