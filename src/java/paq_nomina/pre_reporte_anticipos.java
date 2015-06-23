/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.AntiSueldos;
import paq_nomina.ejb.mergeDescuento;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_reporte_anticipos extends Pantalla {

    //Conexion a base
    private Conexion conPostgres = new Conexion();
    private Tabla tabTabla1 = new Tabla();
    private Tabla tabTabla2 = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setRol = new SeleccionTabla();
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Combos de Selección
    private Combo cmbanos = new Combo();
    private Combo cmbmesin = new Combo();
    private Combo cmbmesfin = new Combo();
    //Dialogos
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogovgl = new Dialogo();
    private Dialogo diaDialogoinvg = new Dialogo();
    private Grid gridg = new Grid();
    private Grid gridvgl = new Grid();
    private Grid gridinvg = new Grid();
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
    //COMBOS DE SELECICON
    private Combo cmbanio = new Combo();
    private Combo cmbperiodo = new Combo();
    private Combo cmbdescripcion = new Combo();

    public pre_reporte_anticipos() {

        Boton bot_busca = new Boton();
        bot_busca.setValue("BUSCAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("actualizaLista");
        bar_botones.agregarBoton(bot_busca);

        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tabTabla1.setId("tabTabla1");
        tabTabla1.setConexion(conPostgres);
        tabTabla1.setSql("SELECT DISTINCT s.ide_empleado_solicitante,s.ci_solicitante,s.solicitante\n"
                + "FROM srh_solicitud_anticipo AS s ORDER BY s.solicitante ASC");
        tabTabla1.getColumna("ide_empleado_solicitante").setVisible(false);
        tabTabla1.getColumna("ci_solicitante").setFiltro(true);
        tabTabla1.getColumna("solicitante").setFiltro(true);
        tabTabla1.setRows(26);
        tabTabla1.setLectura(true);
        tabTabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tabTabla1);

        tabTabla2.setId("tabTabla2");
        tabTabla2.setConexion(conPostgres);
        tabTabla2.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 1);
        tabTabla2.getColumna("porcentaje_descuento_diciembre").setVisible(false);
        tabTabla2.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tabTabla2.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tabTabla2.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");

        tabTabla2.getColumna("usu_anulacion").setVisible(false);
        tabTabla2.getColumna("usu_pago_anticipado").setVisible(false);
        tabTabla2.getColumna("usu_cobra_liquidacion").setVisible(false);
        tabTabla2.getColumna("ide_empleado").setVisible(false);
        tabTabla2.getColumna("ide_calculo_anticipo").setVisible(false);
        tabTabla2.setLectura(true);
        tabTabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tabTabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "28%", "V");
        agregarComponente(div_division);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);


        Grid gri_search = new Grid();
        gri_search.setColumns(2);

        gri_search.getChildren().add(new Etiqueta("AÑO: "));
        cmbanos.setId("cmbanos");
        cmbanos.setConexion(conPostgres);
        cmbanos.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        cmbanos.setMetodo("comp_anio");
        gri_search.getChildren().add(cmbanos);

        gri_search.getChildren().add(new Etiqueta("Periodo Inicial: "));
        cmbmesin.setId("cmbmesin");
        cmbmesin.setConexion(conPostgres);
        cmbmesin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_search.getChildren().add(cmbmesin);

        gri_search.getChildren().add(new Etiqueta("Periodo final: "));
        cmbmesfin.setId("cmbmesfin");
        cmbmesfin.setConexion(conPostgres);
        cmbmesfin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_search.getChildren().add(cmbmesfin);

        //para poder busca por apelllido el garante
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("25%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getGri_cuerpo().setHeader(gri_search);
        diaDialogo.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridg.setColumns(4);
        agregarComponente(diaDialogo);

        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);

        gri_busca.getChildren().add(new Etiqueta("AÑO:"));
        cmbanio.setId("cmbanio");
        cmbanio.setConexion(conPostgres);
        cmbanio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca.getChildren().add(cmbanio);

        gri_busca.getChildren().add(new Etiqueta("PERIODO:"));
        cmbperiodo.setId("cmbperiodo");
        cmbperiodo.setConexion(conPostgres);
        cmbperiodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca.getChildren().add(cmbperiodo);

        gri_busca.getChildren().add(new Etiqueta("DESCRIPCIÓN:"));
        cmbdescripcion.setId("cmbdescripcion");
        cmbdescripcion.setConexion(conPostgres);
        cmbdescripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        cmbdescripcion.setMetodo("buscarColumna");
        gri_busca.getChildren().add(cmbdescripcion);

        /*
         * CREACION DE TABLA SELECCION PARA COLUMNAS
         */
        setRol.setId("setRol");
        setRol.getTab_seleccion().setConexion(conPostgres);//conexion para seleccion con otra base
        setRol.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        setRol.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRol.getTab_seleccion().setRows(12);
        setRol.setRadio();
        setRol.getGri_cuerpo().setHeader(gri_busca);
        setRol.getBot_aceptar().setMetodo("aceptoAnticipo");
        setRol.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setRol);
        actualizaLista();

    }

    public void comp_anio() {
        if (Integer.parseInt(cmbanos.getValue() + "") <= 2014) {
            utilitario.agregarMensaje("Detalle No Disponible Para Año Seleccionado", "Escoga otro ???");
        }
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTabla2.setCondicion(getFiltrosAcceso());
            tabTabla2.ejecutarSql();
            utilitario.addUpdate("tabTabla2");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabTabla1.getValorSeleccionado() != null) {

            str_filtros = "ide_empleado = "
                    + String.valueOf(tabTabla1.getValorSeleccionado());

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void buscarColumna() {
        if (cmbdescripcion.getValue() != null && cmbdescripcion.getValue().toString().isEmpty() == false) {
            setRol.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE DISTRIBUTIVO=" + cmbdescripcion.getValue());
            setRol.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo", "");
        }
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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "VER ANTICIPO":
                aceptoAnticipo();
                break;
            case "CONSOLIDADO DE  ANTICIPO":
                diaDialogo.Limpiar();
                diaDialogo.dibujar();
                break;
            case "REPORTE ROLES TOTAL DESCUENTOS":
                setRol.dibujar();
                setRol.getTab_seleccion().limpiar();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "VER ANTICIPO":
                TablaGenerica tab_dato = iAnticipos.getCedula(Integer.parseInt(tabTabla2.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    p_parametros.put("identificacion", tab_dato.getValor("ci_solicitante") + "");
                    p_parametros.put("codigo", Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo") + ""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No puede Mostrar Reporte", "Datos no encontrados");
                }
                break;
            case "CONSOLIDADO DE  ANTICIPO":
                TablaGenerica tab_dato1 = iAnticipos.getTotalAnt(String.valueOf(Integer.parseInt(cmbanos.getValue() + "") - 1));
                if (!tab_dato1.isEmpty()) {
                    p_parametros.put("anio_anterior", String.valueOf(Integer.parseInt(cmbanos.getValue() + "") - 1));
                    p_parametros.put("mes", cmbmesin.getValue() + "");
                    p_parametros.put("mes1", cmbmesfin.getValue() + "");
                    p_parametros.put("anio_actual", cmbanos.getValue() + "");
                    p_parametros.put("anticipo", Double.valueOf(tab_dato1.getValor("anticipo") + ""));
                    p_parametros.put("descuento", Double.valueOf(tab_dato1.getValor("descontar") + ""));
                    p_parametros.put("saldo", Double.valueOf(tab_dato1.getValor("saldo") + ""));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No puede Mostrar Reporte", "Detalle No encontrado");
                }
                break;
            case "REPORTE ROLES TOTAL DESCUENTOS":
                if (setRol.getValorSeleccionado() != null) {
                    TablaGenerica tab_dato3 = mDescuento.periodo(Integer.parseInt(cmbperiodo.getValue() + ""));
                    if (!tab_dato3.isEmpty()) {
                        TablaGenerica tab_dato4 = mDescuento.distibutivo(Integer.parseInt(cmbdescripcion.getValue() + ""));
                        if (!tab_dato4.isEmpty()) {
                            TablaGenerica tab_dato5 = mDescuento.columnas(Integer.parseInt(setRol.getValorSeleccionado() + ""));
                            if (!tab_dato5.isEmpty()) {
                                p_parametros = new HashMap();
                                p_parametros.put("pide_ano", Integer.parseInt(cmbanio.getValue() + ""));
                                p_parametros.put("periodo", Integer.parseInt(cmbperiodo.getValue() + ""));
                                p_parametros.put("p_nombre", tab_dato3.getValor("per_descripcion") + "");
                                p_parametros.put("distributivo", Integer.parseInt(cmbdescripcion.getValue() + ""));
                                p_parametros.put("descripcion", tab_dato4.getValor("descripcion") + "");
                                p_parametros.put("columnas", Integer.parseInt(setRol.getValorSeleccionado() + ""));
                                p_parametros.put("descrip", tab_dato5.getValor("descripcion_col") + "");
                                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                                rep_reporte.cerrar();
                                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                sef_formato.dibujar();
                                setRol.cerrar();
                            } else {
                                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                }
                break;
        }
    }

    public Tabla getTab_tabla1() {
        return tabTabla1;
    }

    public void setTab_tabla1(Tabla tabTabla1) {
        this.tabTabla1 = tabTabla1;
    }

    public Conexion getCon_postgres() {
        return conPostgres;
    }

    public void setCon_postgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTab_tabla2() {
        return tabTabla2;
    }

    public void setTab_tabla2(Tabla tabTabla2) {
        this.tabTabla2 = tabTabla2;
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

    public SeleccionTabla getSet_rol() {
        return setRol;
    }

    public void setSet_rol(SeleccionTabla setRol) {
        this.setRol = setRol;
    }
}
