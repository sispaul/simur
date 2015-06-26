/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_manauto.ejb.SQLManauto;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class OrdenesPago extends Pantalla {

    private Conexion conPostgres = new Conexion();
    //Tabla Normal
    private Tabla tabOrden = new Tabla();
    private Tabla setRegistro = new Tabla();
    private Tabla tabConsulta = new Tabla();
    //Dialogo Busca 
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridm = new Grid();
    private Grid gridt = new Grid();
    private Grid gridD = new Grid();
    private Grid gridT = new Grid();
    //Texto de Ingreso
    Texto txtMotivo = new Texto();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    private SQLManauto aCombustible = (SQLManauto) utilitario.instanciarEJB(SQLManauto.class);
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();

    public OrdenesPago() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBuscar);

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("select a.tes_ide_orden_pago,   \n"
                + "a.numero_orden,    \n"
                + "(case when  a.tes_cod_empleado is null then b.titular when a.tes_cod_empleado is not null then c.nombres end)   \n"
                + "as beneficiario,   \n"
                + "a.valor,    \n"
                + "a.acuerdo   \n"
                + "from (SELECT\n"
                + "tes_numero_orden AS numero_orden,     \n"
                + "tes_valor AS valor,    \n"
                + "tes_acuerdo AS acuerdo,   \n"
                + "tes_ide_orden_pago,   \n"
                + "tes_cod_empleado,  \n"
                + "(case when tes_id_proveedor is null then tes_cod_empleado when tes_id_proveedor is not null then tes_id_proveedor end) as cod_benefi   \n"
                + "FROM tes_orden_pago where tes_estado_doc = '1' ) as a   \n"
                + "left join    \n"
                + "(SELECT ide_proveedor,titular FROM tes_proveedores) as b   \n"
                + "on a.cod_benefi = b.ide_proveedor   \n"
                + "left join    \n"
                + "(SELECT cedula_pass,nombres,cod_empleado FROM srh_empleado) as c   \n"
                + "on a.cod_benefi = c.cod_empleado\n"
                + "order by numero_orden desc");
        autBusca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autBusca);

        Boton botAnular = new Boton();
        botAnular.setValue("Anular");
        botAnular.setExcluirLectura(true);
        botAnular.setIcon("ui-icon-cancel");
        botAnular.setMetodo("quitar");
        bar_botones.agregarBoton(botAnular);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ORDENES DE PAGO");
        agregarComponente(panOpcion);

        // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("anular");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("LISTADO DE ORDENES DE PAGO REALIZADAS"); //titulo
        diaDialogot.setWidth("95%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("85%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getBot_aceptar().setMetodo("cargarRegistro");
        gridt.setColumns(4);
        agregarComponente(diaDialogot);


        setRegistro.setId("setRegistro");
        setRegistro.setConexion(conPostgres);
        setRegistro.setSql("select   \n"
                + " a.tes_ide_orden_pago,  \n"
                + " a.numero_orden,  \n"
                + " a.numero_comprobante as comprobante,\n"
                + "(case when  a.tes_cod_empleado is null then b.titular when a.tes_cod_empleado is not null then c.nombres end)  \n"
                + "as beneficiario,  \n"
                + " a.asunto,  \n"
                + " a.valor,  \n"
                + " a.concepto,  \n"
                + " a.acuerdo  \n"
                + " from (  \n"
                + " SELECT   \n"
                + " tes_numero_orden AS numero_orden,  \n"
                + " tes_comprobante_egreso AS numero_comprobante,  \n"
                + " tes_asunto AS asunto,  \n"
                + " tes_valor AS valor,  \n"
                + " tes_concepto AS concepto,  \n"
                + " tes_acuerdo AS acuerdo,  \n"
                + " tes_ide_orden_pago,  \n"
                + " tes_cod_empleado, \n"
                + " (case when tes_id_proveedor is null then tes_cod_empleado when tes_id_proveedor is not null then tes_id_proveedor end) as cod_benefi  \n"
                + " FROM tes_orden_pago where tes_estado_doc = '1' and tes_anio='" + utilitario.getAnio(utilitario.getFechaActual()) + "') as a  \n"
                + " left join   \n"
                + " (SELECT ide_proveedor,titular FROM tes_proveedores) as b  \n"
                + " on a.cod_benefi = b.ide_proveedor  \n"
                + " left join   \n"
                + " (SELECT cedula_pass,nombres,cod_empleado FROM srh_empleado) as c  \n"
                + " on a.cod_benefi = c.cod_empleado  \n"
                + " order by numero_orden desc");
        setRegistro.getColumna("tes_ide_orden_pago").setVisible(false);
        setRegistro.getColumna("numero_orden").setLongitud(4);
        setRegistro.getColumna("comprobante").setLongitud(4);
        setRegistro.getColumna("numero_orden").setFiltroContenido();
        setRegistro.getColumna("beneficiario").setFiltroContenido();
        setRegistro.getColumna("acuerdo").setFiltroContenido();
        setRegistro.getColumna("comprobante").setFiltroContenido();
        setRegistro.getColumna("acuerdo").setLongitud(50);
        setRegistro.getColumna("beneficiario").setLongitud(80);
        setRegistro.setLectura(true);
        setRegistro.setRows(13);
        setRegistro.dibujar();

        dibujarOrden();

    }

    public void dibujarOrden() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setConexion(conPostgres);
        tabOrden.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        if (autBusca.getValue() == null) {
            tabOrden.setCondicion("tes_ide_orden_pago=-1");
        } else {
            tabOrden.setCondicion("tes_ide_orden_pago=" + autBusca.getValor());
        }
        tabOrden.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
        tabOrden.getColumna("tes_id_proveedor").setFiltroContenido();
        tabOrden.getColumna("tes_id_proveedor").setMetodoChange("proveedor");
        tabOrden.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado order by nombres");
        tabOrden.getColumna("tes_cod_empleado").setFiltroContenido();
        tabOrden.getColumna("tes_cod_empleado").setMetodoChange("empleado");
        tabOrden.getColumna("tes_estado_doc").setValorDefecto("1");
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
        tabOrden.getGrid().setColumns(2);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    public void buscaRegistro() {
        diaDialogot.Limpiar();
        diaDialogot.setDialogo(gridt);
        gridT.getChildren().add(setRegistro);
        diaDialogot.setDialogo(gridT);
        setRegistro.dibujar();
        diaDialogot.dibujar();
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void numLetras() {
        Object objeto = (Object) tabOrden.getValor("tes_valor");
        tabOrden.setValor("tes_valor_letras", utilitario.getLetrasDolarNumero((objeto)));
        utilitario.addUpdate("tabOrden");//actualiza solo componentes
    }

    public void proveedor() {
        if (tabOrden.getValor("tes_id_proveedor") != null) {
            TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tabOrden.getValor("tes_id_proveedor")));
            if (!tab_dato.isEmpty()) {
                tabOrden.setValor("tes_proveedor", tab_dato.getValor("titular"));
                tabOrden.setValor("tes_empleado", null);
                tabOrden.setValor("tes_cod_empleado", null);
                tabOrden.setValor("tes_estado", "Pendiente");
                tabOrden.setValor("tipo_solicitantep", "2");
                utilitario.addUpdate("tabOrden");//actualiza solo componente
                System.out.println("Nombre de proveedor>>>>>>\t" + tabOrden.getValor("tes_proveedor"));
                tabOrden.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado order by nombres");
                utilitario.addUpdateTabla(tabOrden, "tes_cod_empleado", "");//actualiza solo componentes
            }
        } else {
            tabOrden.getColumna("tes_cod_empleado").setLectura(false);
            utilitario.addUpdate("tabOrden");//actualiza solo componentes
        }
    }

    public void empleado() {
        if (tabOrden.getValor("tes_cod_empleado") != null) {
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tabOrden.getValor("tes_cod_empleado")));
            if (!tab_dato.isEmpty()) {
                tabOrden.setValor("tes_empleado", tab_dato.getValor("nombres"));
                tabOrden.setValor("tes_proveedor", null);
                tabOrden.setValor("tes_id_proveedor", null);
                tabOrden.setValor("tes_estado", "Pendiente");
                tabOrden.setValor("tipo_solicitantep", "1");
                utilitario.addUpdate("tabOrden");//actualiza solo componentes
                System.out.println("Nombre de empleado>>>>>>\t" + tabOrden.getValor("tes_empleado"));
                tabOrden.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
                utilitario.addUpdateTabla(tabOrden, "tes_id_proveedor", "");//actualiza solo componentes
            }
        } else {
            tabOrden.getColumna("tes_id_proveedor").setLectura(false);
            utilitario.addUpdate("tabOrden");//actualiza solo componentes
        }
    }

    public void quitar() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        txtMotivo.setSize(50);
        gridm.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN :"));
        grid.getChildren().add(new Etiqueta("_____________________________________________________"));
        grid.getChildren().add(txtMotivo);
        diaDialogo.setDialogo(gridD);
        diaDialogo.dibujar();
    }

    public void anular() {
        programas.actOrden(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), tabConsulta.getValor("NICK_USUA"), txtMotivo.getValue() + "");
        utilitario.addUpdate("tabOrden");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        diaDialogo.cerrar();
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

    public void cargarRegistro() {
        if (setRegistro.getValorSeleccionado() != null) {
            autBusca.setValor(setRegistro.getValorSeleccionado());
            dibujarOrden();
            diaDialogot.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        if (tabOrden.getValor("tes_ide_orden_pago") != null) {
            TablaGenerica tabInfo = aCombustible.getCatalogoDato("*", tabOrden.getTabla(), "tes_ide_orden_pago = " + tabOrden.getValor("tes_ide_orden_pago") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = aCombustible.getNumeroCampos(tabOrden.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = aCombustible.getEstrucTabla(tabOrden.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabOrden.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        aCombustible.setActuaRegis(Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), tabOrden.getTabla(), tabInfoColum1.getValor("Column_Name"), tabOrden.getValor(tabInfoColum1.getValor("Column_Name")), "tes_ide_orden_pago");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else {
            if (tabOrden.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    }

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

    public Tabla getSetRegistro() {
        return setRegistro;
    }

    public void setSetRegistro(Tabla setRegistro) {
        this.setRegistro = setRegistro;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
