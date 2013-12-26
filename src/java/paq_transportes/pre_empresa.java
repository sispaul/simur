/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_registros.ejb.ServicioRegistros;

/**
 *
 * @author Diego
 */
public class pre_empresa extends Pantalla {

    private Tabla tab_tabla;
    private Tabla tab_permisos;
    private Tabla tab_socios;
    private Tabla tab_vehiculos;
    private Tabla tab_permiso_x_ruta;
    private Tabla tab_permiso_provicional;
    private Tabla tab_recorrido;
    private Tabla tab_seguro;
    private Tabla tab_revision;
    private AutoCompletar aut_empresas = new AutoCompletar();
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private SeleccionTabla sel_tab_tipo_vehiculo = new SeleccionTabla();
    private SeleccionTabla sel_tab_empresa = new SeleccionTabla();
    private SeleccionTabla sel_tab_estado_socio = new SeleccionTabla();
    @EJB
    private ServicioRegistros ser_registros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);
    private SeleccionTabla set_empresa = new SeleccionTabla();
    private Texto tex_busqueda = new Texto();

    public pre_empresa() {

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);

        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        tex_busqueda.setSize(45);
        gri_busca.getChildren().add(tex_busqueda);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);

        set_empresa.setId("set_empresa");
        set_empresa.setSeleccionTabla("select ide_empresa,nombre,ruc from trans_empresa where ide_empresa=-1", "ide_empresa");
        set_empresa.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_empresa.getTab_seleccion().setRows(10);
        set_empresa.setRadio();
        set_empresa.getGri_cuerpo().setHeader(gri_busca);
        set_empresa.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_empresa.setHeader("BUSCAR EMBRESA");
        agregarComponente(set_empresa);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sef_reporte.setId("sef_reporte");
        agregarComponente(sef_reporte);

        sel_tab_tipo_vehiculo.setId("sel_tab_tipo_vehiculo");
        sel_tab_tipo_vehiculo.setTitle("SELECCION DE TIPO DE VEHICULOS");
        sel_tab_tipo_vehiculo.setSeleccionTabla("SELECT codigo,nombre from trans_tipo where codigo=-1", "codigo");

        sel_tab_tipo_vehiculo.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab_tipo_vehiculo.setRadio();
        agregarComponente(sel_tab_tipo_vehiculo);

        sel_tab_empresa.setId("sel_tab_empresa");
        sel_tab_empresa.setTitle("SELECCION DE EMPRESAS");
        sel_tab_empresa.setSeleccionTabla("select ide_empresa,nombre,ruc from trans_empresa WHERE ide_empresa=-1 order by nombre", "ide_empresa");
        sel_tab_empresa.getTab_seleccion().getColumna("nombre").setFiltroContenido();
        sel_tab_empresa.getTab_seleccion().getColumna("ruc").setFiltro(true);
        sel_tab_empresa.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab_empresa.setRadio();
        agregarComponente(sel_tab_empresa);


        sel_tab_estado_socio.setId("sel_tab_estado_socio");
        sel_tab_estado_socio.setTitle("SELECCION DE ESTADO DE SOCIOS");
        sel_tab_estado_socio.setSeleccionTabla("SELECT ide_estado_socio,des_estado_socio FROM trans_estado_socio WHERE ide_estado_socio=-1", "ide_estado_socio");
        sel_tab_estado_socio.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_tab_estado_socio);

        aut_empresas.setId("aut_empresas");
        aut_empresas.setAutoCompletar("select ide_empresa,nombre,ruc from trans_empresa");
        aut_empresas.setMetodoChange("filtrarEmpresa");
        aut_empresas.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Empresa:"));
        bar_botones.agregarComponente(aut_empresas);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        bar_botones.agregarReporte();


        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("MENU DE OPCIONES");
        agregarComponente(div_division);
        dibujarEmpresa();
    }

    private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("EMPRESAS DE TRANSPORTE PUBLICO");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("DATOS EMPRESA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarEmpresa");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("PERMISOS");
        itm_permisos.setIcon("ui-icon-key");
        itm_permisos.setMetodo("dibujarPermisos");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);

        // ITEM 2: OPCION 1
        ItemMenu itm_socios = new ItemMenu();
        itm_socios.setValue("SOCIOS");
        itm_socios.setIcon("ui-icon-person");
        itm_socios.setMetodo("dibujarSocios");
        itm_socios.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_socios);

    }

    public void buscarEmpresa() {
        if (tex_busqueda.getValue() != null && tex_busqueda.getValue().toString().isEmpty() == false) {
            set_empresa.getTab_seleccion().setSql("select ide_empresa,nombre,ruc from trans_empresa where upper(nombre)LIKE'%" + tex_busqueda.getValue() + "%'");
            set_empresa.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }

    }

    public void abrirBusqueda() {

        set_empresa.dibujar();
        tex_busqueda.limpiar();
        set_empresa.getTab_seleccion().limpiar();
    }

    public void aceptarBusqueda() {
        if (set_empresa.getValorSeleccionado() != null) {
            aut_empresas.setValor(set_empresa.getValorSeleccionado());
            set_empresa.cerrar();
            dibujarPanel();
            utilitario.addUpdate("aut_empresas,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }

    }

    public void dibujarPermisos() {
        if (aut_empresas.getValue() != null) {
            str_opcion = "1";
            limpiarPanel();
            tab_permisos = new Tabla();
            tab_permiso_x_ruta = new Tabla();
            tab_permiso_provicional = new Tabla();
            tab_permisos.setId("tab_permisos");
            tab_permisos.setTabla("trans_permiso", "ide_permiso", 2);
            tab_permisos.getColumna("ide_empresa").setVisible(false);
            tab_permisos.setCondicion("ide_empresa=" + aut_empresas.getValor());
            tab_permisos.setTipoFormulario(true);
            tab_permisos.getGrid().setColumns(4);
            tab_permisos.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permisos.getColumna("fecha_responsable").setVisible(false);
            tab_permisos.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permisos.getColumna("ip_responsable").setVisible(false);
            tab_permisos.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permisos.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_permisos.getColumna("HORARIO_INICIO").setControl("Texto");
            tab_permisos.getColumna("HORARIO_INICIO").setTipoJava("java.lang.String");
            tab_permisos.getColumna("HORARIO_INICIO").setTipo("Text");                        	
             tab_permisos.getColumna("HORARIO_FINAL").setControl("Texto");
            tab_permisos.getColumna("HORARIO_FINAL").setTipoJava("java.lang.String");
            tab_permisos.getColumna("HORARIO_FINAL").setTipo("Text");  
            tab_permisos.agregarRelacion(tab_permiso_x_ruta);
            tab_permisos.agregarRelacion(tab_permiso_provicional);
            tab_permisos.setHeader("PERMISO DE OPERACIÓN");            
            tab_permisos.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_permisos);
            tab_permisos.setStyle(null);
            pat_panel1.setStyle("width:100%;overflow: auto;");



            tab_permiso_x_ruta.setId("tab_permiso_x_ruta");
            tab_recorrido = new Tabla();
            tab_permiso_x_ruta.setIdCompleto("tab_tabulador:tab_permiso_x_ruta");
            tab_permiso_x_ruta.setHeader("PERMISOS POR RUTA");
            tab_permiso_x_ruta.setTabla("trans_permiso_x_ruta", "ide_permiso_ruta", 5);
            tab_permiso_x_ruta.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permiso_x_ruta.getColumna("fecha_responsable").setVisible(false);
            tab_permiso_x_ruta.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permiso_x_ruta.getColumna("ip_responsable").setVisible(false);
            tab_permiso_x_ruta.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permiso_x_ruta.getColumna("NOM_RESPONSABLE").setVisible(false);
            List<String[]> lis_tipo = new ArrayList();
            lis_tipo.add(new String[]{"POPULAR", "POPULAR"});
            lis_tipo.add(new String[]{"ESPECIAL", "ESPECIAL"});
            tab_permiso_x_ruta.getColumna("TIPO_SERVICIO").setRadio(lis_tipo, "ESPECIAL");
            tab_permiso_x_ruta.getColumna("TIPO_SERVICIO").setRadioVertical(true);
            tab_permiso_x_ruta.setTipoFormulario(true);
            tab_permiso_x_ruta.getGrid().setColumns(6);
            tab_permiso_x_ruta.agregarRelacion(tab_recorrido);
            tab_permiso_x_ruta.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_permiso_x_ruta);
            //  tab_permiso_x_ruta.setStyle(null);
            // pat_panel2.setStyle("width:100%;overflow: auto;");

            tab_recorrido.setId("tab_recorrido");
            tab_recorrido.setIdCompleto("tab_tabulador:tab_recorrido");
            tab_recorrido.setTabla("trans_recorrido", "ide_recorrido", 7);
            tab_recorrido.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_recorrido.getColumna("fecha_responsable").setVisible(false);
            tab_recorrido.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_recorrido.getColumna("ip_responsable").setVisible(false);
            tab_recorrido.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_recorrido.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_recorrido.setHeader("RECORRIDOS");
            tab_recorrido.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_recorrido);


            Grupo gri_per_ruta = new Grupo();
            gri_per_ruta.getChildren().add(pat_panel2);
            gri_per_ruta.getChildren().add(pat_panel4);


            Tabulador tab_tabulador = new Tabulador();
            tab_tabulador.setId("tab_tabulador");
            tab_tabulador.agregarTab("PERMISO POR RUTA", gri_per_ruta);

            tab_permiso_provicional.setId("tab_permiso_provicional");
            tab_permiso_provicional.setIdCompleto("tab_tabulador:tab_permiso_provicional");
            tab_permiso_provicional.setTabla("trans_permiso_provicional", "ide_permiso_provicional", 6);
            tab_permiso_provicional.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_permiso_provicional.getColumna("fecha_responsable").setVisible(false);
            tab_permiso_provicional.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_permiso_provicional.getColumna("ip_responsable").setVisible(false);
            tab_permiso_provicional.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_permiso_provicional.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_permiso_provicional.setTipoFormulario(true);
            tab_permiso_provicional.getGrid().setColumns(4);
            tab_permiso_provicional.setHeader("PERMISO PROVICIONAL");
            tab_permiso_provicional.getColumna("ubi_codigo").setCombo("select ubi_codigo,ubi_descri from oceubica order by ubi_descri");

            tab_permiso_provicional.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_permiso_provicional);
            tab_tabulador.agregarTab("PERMISO PROVICIONAL", pat_panel3);
            tab_permiso_provicional.setStyle(null);
            pat_panel3.setStyle("width:100%;overflow: auto;");

            Grupo gru = new Grupo();
            gru.getChildren().add(pat_panel1);
            gru.getChildren().add(tab_tabulador);

            pan_opcion.getChildren().add(gru);
            pan_opcion.setTitle("INFORMACION DEL PERMISO DE OPERACION DE LA EMPRESA");

        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }

    public void dibujarSocios() {
        if (aut_empresas.getValue() != null) {
            str_opcion = "2";
            limpiarPanel();
            tab_socios = new Tabla();
            tab_vehiculos = new Tabla();
            tab_socios.setId("tab_socios");
            tab_socios.setTabla("trans_socios", "ide_socios", 3);
            tab_socios.getColumna("ide_empresa").setVisible(false);
            tab_socios.setCondicion("ide_empresa=" + aut_empresas.getValor());
            tab_socios.getColumna("ide_tipo_licencia").setCombo("trans_tipo_licencia", "ide_tipo_licencia", "des_tipo_licencia", "");
            tab_socios.setTipoFormulario(true);
            tab_socios.getGrid().setColumns(6);
            tab_socios.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_socios.getColumna("fecha_responsable").setVisible(false);
            tab_socios.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_socios.getColumna("ip_responsable").setVisible(false);
            tab_socios.getColumna(" NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_socios.getColumna(" NOM_RESPONSABLE").setVisible(false);
            tab_socios.getColumna("estado_socio").setCombo("trans_estado_socio", "ide_estado_socio", "des_estado_socio", "");
            tab_socios.getColumna("cedula").setMetodoChange("cargarSocio");
            tab_socios.agregarRelacion(tab_vehiculos);
            tab_socios.setHeader("SOCIOS");
            tab_socios.getColumna("instruccion").setCombo("trans_nivel_instruccion", "ide_nivel_inst", "des_nivel_inst", "");
            tab_socios.getColumna("cursos_municipales").setCombo("trans_cursos_municipales", "ide_curso_municipal", "des_curso", "");

            List<String[]> lis_control = new ArrayList();
            lis_control.add(new String[]{"NO", "NO"});
            lis_control.add(new String[]{"SI", "SI"});

            tab_socios.getColumna("seguros_salud").setRadio(lis_control, "NO");
            tab_socios.getColumna("control_salud").setRadio(lis_control, "NO");


            tab_socios.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_socios);
            tab_socios.setStyle(null);
            pat_panel2.setStyle("width:100%;overflow: auto;");
            pan_opcion.setTitle("SOCIOS QUE PERTENECEN A LA EMPRESA");

            tab_seguro = new Tabla();
            tab_revision = new Tabla();
            tab_vehiculos.setId("tab_vehiculos");
            tab_vehiculos.setTabla("trans_vehiculos", "ide_vehiculo", 4);
            tab_vehiculos.setHeader("VEHÍCULOS DEL SOCIO");
            tab_vehiculos.setRows(10);
            tab_vehiculos.getColumna("ide_modelo").setCombo("select ide_modelo,des_modelo,marca from trans_modelos mode INNER JOIN trans_marcas marca on mode.ide_marca=marca.ide_marca order by des_modelo,marca");
            tab_vehiculos.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_vehiculos.getColumna("fecha_responsable").setVisible(false);
            tab_vehiculos.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_vehiculos.getColumna("ip_responsable").setVisible(false);
            tab_vehiculos.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_vehiculos.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_vehiculos.setTipoFormulario(true);
            tab_vehiculos.getColumna("FEC_ALTA").setControl("Calendario");
            tab_vehiculos.getColumna("FEC_BAJA").setControl("Calendario");
            tab_vehiculos.getColumna("ide_tipo_vehiculo").setCombo("trans_tipo_vehiculo", "ide_tipo_vehiculo", "des_tipo_vehiculo", "");
            tab_vehiculos.getColumna("estado").setCombo("trans_estado_vehiculo", "ide_estado_vehiculo", "des_estado_vehiculo", "");
            tab_vehiculos.getColumna("estado_vehiculo").setCombo("trans_situacion_vehiculo", "ide_situacion", "des_situacion", "");
            tab_vehiculos.getColumna("combustible").setCombo("trans_combustible", "ide_combustible", "des_combustible", "");

            tab_vehiculos.getGrid().setColumns(6);
            tab_vehiculos.agregarRelacion(tab_seguro);
            tab_vehiculos.agregarRelacion(tab_revision);
            tab_vehiculos.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_vehiculos);
            tab_vehiculos.setStyle(null);
            pat_panel3.setStyle("width:100%;overflow: auto;");

            tab_seguro.setId("tab_seguro");
            tab_seguro.setTabla("trans_seguro", "ide_seguro", 8);
            tab_seguro.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_seguro.getColumna("fecha_responsable").setVisible(false);
            tab_seguro.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_seguro.getColumna("ip_responsable").setVisible(false);
            tab_seguro.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_seguro.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_seguro.getColumna("IDE_ASEGURADORA").setCombo("trans_aseguradora", "IDE_ASEGURADORA", "nombre", "");
            tab_seguro.setHeader("SEGURO DEL VEHÍCULO");
            tab_socios.getColumna("control_salud").setRadio(lis_control, "NO");

            tab_seguro.setTipoFormulario(true);
            tab_seguro.getGrid().setColumns(6);
            tab_seguro.getColumna("FEC_EMISION").setControl("Calendario");
            tab_seguro.getColumna("FEC_CADUCIDAD").setControl("Calendario");
            tab_seguro.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_seguro);
            tab_seguro.setStyle(null);
            pat_panel4.setStyle("width:100%;overflow: auto;");


            tab_revision.setId("tab_revision");

            tab_revision.setTabla("trans_revision_vehicular", "ide_revision", 9);
            tab_revision.getColumna("fecha_responsable").setCalendarioFechaHora();
            tab_revision.getColumna("fecha_responsable").setVisible(false);
            tab_revision.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
            tab_revision.getColumna("ip_responsable").setVisible(false);
            tab_revision.getColumna("NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
            tab_revision.getColumna("NOM_RESPONSABLE").setVisible(false);
            tab_revision.getColumna("tipo").setCombo("trans_tipo_revision", "ide_tipo_revision", "des_tipo_revision", "");
            tab_revision.getColumna("renovado").setRadio(lis_control, "NO");
            tab_revision.setTipoFormulario(true);
            tab_revision.setHeader("REVISIÓN VEHICULAR");
            tab_revision.getGrid().setColumns(6);
            tab_revision.getColumna("FEC_REVISION").setControl("Calendario");
            tab_revision.getColumna("FOTO").setUpload("vehiculos");
            tab_revision.getColumna("FOTO").setImagen("94", "94");
            tab_revision.dibujar();

            PanelTabla pat_panel5 = new PanelTabla();
            pat_panel5.setPanelTabla(tab_revision);
            tab_revision.setStyle(null);
            pat_panel5.setStyle("width:100%;overflow: auto;");


            Grupo gru = new Grupo();
            gru.getChildren().add(pat_panel2);
            gru.getChildren().add(pat_panel3);
            gru.getChildren().add(pat_panel4);
            gru.getChildren().add(pat_panel5);
            pan_opcion.getChildren().add(gru);
        } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Empresa en el autocompletar");
            limpiar();
        }
    }

    public void dibujarEmpresa() {
        str_opcion = "0";
        limpiarPanel();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("trans_empresa", "ide_empresa", 1);
        if (aut_empresas.getValue() == null) {
            tab_tabla.setCondicion("ide_empresa=-1");
        } else {
            tab_tabla.setCondicion("ide_empresa=" + aut_empresas.getValor());
        }
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getColumna("codigo").setCombo("trans_tipo", "codigo", "nombre", "");
        tab_tabla.getColumna("fecha_responsable").setCalendarioFechaHora();
        tab_tabla.getColumna("fecha_responsable").setVisible(false);
        tab_tabla.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
        tab_tabla.getColumna("ip_responsable").setVisible(false);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.getColumna("ruc").setMetodoChange("cargarEmpresa");
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.getColumna("FEC_PATENTE").setControl("Calendario");
        tab_tabla.getColumna("FEC_PAGO_OCUPACION_VIA").setControl("Calendario");
        tab_tabla.getColumna(" NOM_RESPONSABLE").setValorDefecto(utilitario.getVariable("NICK"));
        tab_tabla.getColumna(" NOM_RESPONSABLE").setVisible(false);
        tab_tabla.setHeader("DATOS DE LA EMPRESA");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        tab_tabla.setStyle(null);
        pat_panel.setStyle("width:100%;overflow: auto;");

        pan_opcion.setTitle("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        pan_opcion.getChildren().add(pat_panel);
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
        aut_empresas.limpiar();
        utilitario.addUpdate("aut_empresas");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }

    public void filtrarEmpresa(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        aut_empresas.onSelect(evt);
        dibujarPanel();
    }

    private void dibujarPanel() {
        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
            dibujarEmpresa();
        } else if (str_opcion.equals("1")) {
            dibujarPermisos();
        } else if (str_opcion.equals("2")) {
            dibujarSocios();
        }
        utilitario.addUpdate("pan_opcion");
    }

    @Override
    public void insertar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFocus()) {
                aut_empresas.limpiar();
                utilitario.addUpdate("aut_empresas");
                tab_tabla.limpiar();
                tab_tabla.insertar();
                if (tab_tabla.isFilaInsertada()) {
                    tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
                }
            }
        } else if (str_opcion.equals("1")) {

            if (tab_permisos.isFocus()) {
                tab_permisos.insertar();
                if (tab_permisos.isFilaInsertada()) {
                    tab_permisos.setValor("ide_empresa", utilitario.getFechaHoraActual());
                }
            } else if (tab_permiso_x_ruta.isFocus()) {
                tab_permiso_x_ruta.insertar();
            } else if (tab_permiso_provicional.isFocus()) {
                tab_permiso_provicional.insertar();
            } else if (tab_recorrido.isFocus()) {
                tab_recorrido.insertar();
            }
        } else if (str_opcion.equals("2")) {
            if (tab_socios.isFocus()) {
                tab_socios.insertar();
                if (tab_socios.isFilaInsertada()) {
                    tab_socios.setValor("ide_empresa", utilitario.getFechaHoraActual());
                }
            } else if (tab_vehiculos.isFocus()) {
                tab_vehiculos.insertar();
            } else if (tab_revision.isFocus()) {
                tab_revision.insertar();
            } else if (tab_seguro.isFocus()) {
                tab_seguro.insertar();
            }
        }

    }

    public void cargarSocio() {
        if (utilitario.validarCedula(tab_socios.getValor("cedula"))) {
            TablaGenerica tab_dato = ser_registros.getPersona(tab_socios.getValor("cedula"));
            if (!tab_dato.isEmpty()) {
                //Cargo la información de la base de datos maestra   
                tab_socios.setValor("nombre", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_socios");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
        }

    }

    public void cargarEmpresa() {
        if (utilitario.validarRUC(tab_tabla.getValor("ruc"))) {
            TablaGenerica tab_dato = ser_registros.getEmpresa(tab_tabla.getValor("ruc"));
            if (!tab_dato.isEmpty()) {
                //Cargo la información de la base de datos maestra   
                tab_tabla.setValor("nombre", tab_dato.getValor("RAZON_SOCIAL"));
                tab_tabla.setValor("direccion", tab_dato.getValor("DIRECCION"));
                tab_tabla.setValor("telefono", tab_dato.getValor("telefono"));
                tab_tabla.setValor("e_mail", tab_dato.getValor("mail"));
                utilitario.addUpdate("tab_tabla");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
        }

    }

    @Override
    public void guardar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFilaInsertada()) {
                //Actualiza nuevamente la fecha
                tab_tabla.setValor("fecha_responsable", utilitario.getFechaHoraActual());
            }
            if (utilitario.validarRUC(tab_tabla.getValor("ruc"))) {
                tab_tabla.guardar();
            } else {
                utilitario.agregarMensajeError("El Número de RUC no es válido", "");
                return;
            }
        } else if (str_opcion.equals("1")) {
            if (tab_permisos.isFilaInsertada()) {
                //Actualiza nuevamente la fecha
                tab_permisos.setValor("fecha_responsable", utilitario.getFechaHoraActual());
            }
            tab_permisos.guardar();
            tab_permiso_x_ruta.guardar();
            tab_permiso_provicional.guardar();
        } else if (str_opcion.equals("2")) {
            if (utilitario.validarCedula(tab_socios.getValor("cedula"))) {
                if (tab_permisos.isFilaInsertada()) {
                    //Actualiza nuevamente la fecha
                    tab_socios.setValor("fecha_responsable", utilitario.getFechaHoraActual());
                }
                tab_socios.guardar();
                tab_vehiculos.guardar();
                tab_seguro.guardar();
                tab_revision.guardar();
                if (guardarPantalla().isEmpty()) {
                    aut_empresas.actualizar();
                    aut_empresas.setSize(70);
                    if (tab_tabla.isFilaInsertada() == false) {
                        aut_empresas.setValor(tab_tabla.getValorSeleccionado());
                    }
                    utilitario.addUpdate("aut_empresas");
                }
            } else {
                utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                return;
            }
        }
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_empresas.actualizar();
        aut_empresas.setSize(70);
        utilitario.addUpdate("aut_empresas");
    }

    @Override
    public void eliminar() {
        if (str_opcion.equals("0")) {
            if (tab_tabla.isFocus()) {
                if (tab_tabla.eliminar()) {
                    aut_empresas.actualizar();
                    aut_empresas.setSize(70);
                    utilitario.addUpdate("aut_empresas");
                }
            }
        } else if (str_opcion.equals("1")) {
            if (tab_permisos.isFocus()) {
                tab_permisos.eliminar();
            } else if (tab_permiso_x_ruta.isFocus()) {
                tab_permiso_x_ruta.eliminar();
            } else if (tab_permiso_provicional.isFocus()) {
                tab_permiso_provicional.eliminar();
            }
            actualizarPanel();
        } else if (str_opcion.equals("2")) {
            if (tab_socios.isFocus()) {
                tab_socios.eliminar();
            } else if (tab_vehiculos.isFocus()) {
                tab_vehiculos.eliminar();
            } else if (tab_seguro.isFocus()) {
                tab_seguro.eliminar();
            } else if (tab_revision.isFocus()) {
                tab_revision.eliminar();
            }
            actualizarPanel();
        }
    }

    private void actualizarPanel() {
        if (str_opcion.equals("1")) {
            if (tab_permisos.isFocus()) {
                tab_permiso_x_ruta.ejecutarSql();
                tab_permiso_provicional.ejecutarSql();
                tab_recorrido.ejecutarSql();
            } else if (tab_permiso_x_ruta.isFocus()) {
                tab_recorrido.ejecutarSql();
            }
        } else if (str_opcion.equals("2")) {
            if (tab_socios.isFocus()) {
                tab_vehiculos.ejecutarSql();
            } else if (tab_vehiculos.isFocus()) {
                tab_seguro.ejecutarSql();
                tab_revision.ejecutarSql();
            }
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Empresas Catastradas")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "EMPRESAS CATASTRADAS 1");
                sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_reporte.dibujar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Empresas Catastradas Tipo")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                sel_tab_tipo_vehiculo.getTab_seleccion().setSql("SELECT codigo,nombre from trans_tipo");
                sel_tab_tipo_vehiculo.getTab_seleccion().ejecutarSql();
                rep_reporte.cerrar();
                sel_tab_tipo_vehiculo.dibujar();
            } else if (sel_tab_tipo_vehiculo.isVisible()) {
                if (sel_tab_tipo_vehiculo.getValorSeleccionado() != null && !sel_tab_tipo_vehiculo.getValorSeleccionado().isEmpty()) {
                    p_parametros.put("tipo_vehiculo", Integer.parseInt(sel_tab_tipo_vehiculo.getValorSeleccionado()));
                    p_parametros.put("titulo", "EMPRESAS CATASTRADAS");
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sel_tab_tipo_vehiculo.cerrar();
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar una Tipo de Transporte");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Permiso Operacion")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                sel_tab_empresa.getTab_seleccion().setSql("select ide_empresa,nombre,ruc from trans_empresa order by nombre");
                sel_tab_empresa.getTab_seleccion().ejecutarSql();
                rep_reporte.cerrar();
                sel_tab_empresa.dibujar();
            } else if (sel_tab_empresa.isVisible()) {
                String str_seleccionados = sel_tab_empresa.getSeleccionados();
                if (sel_tab_empresa.getSeleccionados() != null) {
                    p_parametros.put("empresa", str_seleccionados);
                    p_parametros.put("titulo", "PERMISO DE OPERACIÓN ");
                    sel_tab_empresa.cerrar();
                    sel_tab_estado_socio.getTab_seleccion().setSql("SELECT ide_estado_socio,des_estado_socio FROM trans_estado_socio");
                    sel_tab_estado_socio.getTab_seleccion().ejecutarSql();
                    sel_tab_estado_socio.dibujar();
                    sel_tab_empresa.cerrar();
                    sel_tab_estado_socio.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar una Empresa");
                }
            } else if (sel_tab_estado_socio.isVisible()) {
                if (sel_tab_estado_socio.getSeleccionados() != null && !sel_tab_estado_socio.getSeleccionados().isEmpty()) {
                    p_parametros.put("estado_socio", sel_tab_estado_socio.getSeleccionados());

                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sel_tab_estado_socio.cerrar();
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al Menos un Estado de Socios");
                }
            }

        }
    }

    @Override
    public void inicio() {
        super.inicio(); //To change body of generated methods, choose Tools | Templates.
        actualizarPanel();
    }

    @Override
    public void fin() {
        super.fin(); //To change body of generated methods, choose Tools | Templates.
        actualizarPanel();
    }

    @Override
    public void siguiente() {
        super.siguiente(); //To change body of generated methods, choose Tools | Templates.
        actualizarPanel();
    }

    @Override
    public void atras() {
        super.atras(); //To change body of generated methods, choose Tools | Templates.
        actualizarPanel();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_permisos() {
        return tab_permisos;
    }

    public void setTab_permisos(Tabla tab_permisos) {
        this.tab_permisos = tab_permisos;
    }

    public Tabla getTab_socios() {
        return tab_socios;
    }

    public void setTab_socios(Tabla tab_socios) {
        this.tab_socios = tab_socios;
    }

    public Tabla getTab_vehiculos() {
        return tab_vehiculos;
    }

    public void setTab_vehiculos(Tabla tab_vehiculos) {
        this.tab_vehiculos = tab_vehiculos;
    }

    public AutoCompletar getAut_empresas() {
        return aut_empresas;
    }

    public void setAut_empresas(AutoCompletar aut_empresas) {
        this.aut_empresas = aut_empresas;
    }

    public Tabla getTab_permiso_x_ruta() {
        return tab_permiso_x_ruta;
    }

    public void setTab_permiso_x_ruta(Tabla tab_permiso_x_ruta) {
        this.tab_permiso_x_ruta = tab_permiso_x_ruta;
    }

    public Tabla getTab_permiso_provicional() {
        return tab_permiso_provicional;
    }

    public void setTab_permiso_provicional(Tabla tab_permiso_provicional) {
        this.tab_permiso_provicional = tab_permiso_provicional;
    }

    public Tabla getTab_recorrido() {
        return tab_recorrido;
    }

    public void setTab_recorrido(Tabla tab_recorrido) {
        this.tab_recorrido = tab_recorrido;
    }

    public Tabla getTab_seguro() {
        return tab_seguro;
    }

    public void setTab_seguro(Tabla tab_seguro) {
        this.tab_seguro = tab_seguro;
    }

    public Tabla getTab_revision() {
        return tab_revision;
    }

    public void setTab_revision(Tabla tab_revision) {
        this.tab_revision = tab_revision;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }

    public SeleccionTabla getSel_tab_tipo_vehiculo() {
        return sel_tab_tipo_vehiculo;
    }

    public void setSel_tab_tipo_vehiculo(SeleccionTabla sel_tab_tipo_vehiculo) {
        this.sel_tab_tipo_vehiculo = sel_tab_tipo_vehiculo;
    }

    public SeleccionTabla getSel_tab_empresa() {
        return sel_tab_empresa;
    }

    public void setSel_tab_empresa(SeleccionTabla sel_tab_empresa) {
        this.sel_tab_empresa = sel_tab_empresa;
    }

    public SeleccionTabla getSel_tab_estado_socio() {
        return sel_tab_estado_socio;
    }

    public void setSel_tab_estado_socio(SeleccionTabla sel_tab_estado_socio) {
        this.sel_tab_estado_socio = sel_tab_estado_socio;
    }

    public SeleccionTabla getSet_empresa() {
        return set_empresa;
    }

    public void setSet_empresa(SeleccionTabla set_empresa) {
        this.set_empresa = set_empresa;
    }
}
