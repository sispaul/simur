/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.AntiSueldos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class AnticipoEspecial extends Pantalla {

    //Conexion a base
    private Conexion conPostgres = new Conexion();
    //tablas
    private Tabla tabAnticipo = new Tabla();
    private Tabla tabGarante = new Tabla();
    private Tabla tabParametros = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setColaborador = new Tabla();
    private Tabla setSolicitante = new Tabla();
    private Tabla setSolicitu = new Tabla();
    private Tabla setListado = new Tabla();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    //PARA ASIGNACION DE MES
    String selecMes = new String();
    //buscar solicitud
    private AutoCompletar autBusca = new AutoCompletar();
    //Cuadros para texto, busqueda reportes
    private Texto texBusqueda = new Texto();
    //Dialogo Busca 
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogos = new Dialogo();
    private Dialogo diaDialogoca = new Dialogo();
    private Dialogo diaDialogoso = new Dialogo();
    private Dialogo diaDialogosr = new Dialogo();
    private Dialogo diaDialogorec = new Dialogo();
    private Grid gridD = new Grid();
    private Grid gridCa = new Grid();
    private Grid gridSo = new Grid();
    private Grid gridDs = new Grid();
    private Grid gridRec = new Grid();
    private Grid grid = new Grid();
    private Grid grids = new Grid();
    private Grid gridso = new Grid();
    private Grid gridr = new Grid();
    private Grid gridrec = new Grid();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    //para seleciconar opcines para el reporte
    private Combo cmbSeleccion = new Combo();
    //Extrae datos adicionales, que se necesita, de una clase logica
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);

    public AnticipoEspecial() {

        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
        agregarComponente(quinde);

        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS - ESPECIALES");
        agregarComponente(pan_opcion);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setValue("Limpiar");
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-person");
        bot_limpiar.setMetodo("limpia_pa");
        bar_botones.agregarBoton(bot_limpiar);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //Auto busqueda para, verificar solicitud
        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,aprobado_solicitante FROM srh_solicitud_anticipo");
        autBusca.setMetodoChange("filtrarSolicitud");
        autBusca.setSize(80);

        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(autBusca);

        Boton bot_recalculo = new Boton();
        bot_recalculo.setValue("Recalculo");
        bot_recalculo.setExcluirLectura(true);
        bot_recalculo.setIcon("ui-icon-clock");
        bot_recalculo.setMetodo("listRecalculo");
        bar_botones.agregarBoton(bot_recalculo);

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        texBusqueda.setSize(45);
        gri_busca.getChildren().add(texBusqueda);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarSolicitud");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);

        setSolicitud.setId("setSolicitud");
        setSolicitud.getTab_seleccion().setConexion(conPostgres);
        setSolicitud.setSeleccionTabla("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo where ide_solicitud_anticipo=-1", "ide_solicitud_anticipo");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca);
        setSolicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        setSolicitud.setHeader("BUSCAR SOLICITUD POR CEDULA");
        agregarComponente(setSolicitud);

        //para poder buscar por apellido el solicitante
        diaDialogos.setId("diaDialogos");
        diaDialogos.setTitle("BUSCAR SOLICITANTE"); //titulo
        diaDialogos.setWidth("35%"); //siempre en porcentajes  ancho
        diaDialogos.setHeight("50%");//siempre porcentaje   alto
        diaDialogos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogos.getBot_aceptar().setMetodo("aceptoSolicitante");
        gridDs.setColumns(4);
        agregarComponente(diaDialogos);

        //para poder busca por apelllido el garante
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("BUSCAR SERVIDOR"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("45%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoColaborador");
        gridD.setColumns(4);
        agregarComponente(diaDialogo);

        cmbSeleccion.setId("cmbSeleccion");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "1", "CEDULA"
        };
        Object fila2[] = {
            "2", "APELLIDO"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmbSeleccion.setCombo(lista1);

        diaDialogoca.setId("diaDialogoca");
        diaDialogoca.setTitle("SELECCIONAR TIPO DE BUSQUEDA"); //titulo
        diaDialogoca.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogoca.setHeight("15%");//siempre porcentaje   alto
        diaDialogoca.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogoca.getBot_aceptar().setMetodo("abrirBusqueda");
        gridCa.setColumns(4);
        agregarComponente(diaDialogoca);

        diaDialogoso.setId("diaDialogoso");
        diaDialogoso.setTitle("BUSQUEDA DE SOLICITUD POR APELLIDO"); //titulo
        diaDialogoso.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogoso.setHeight("45%");//siempre porcentaje   alto
        diaDialogoso.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogoso.getBot_aceptar().setMetodo("aceptarBusqueda");
        gridSo.setColumns(4);
        agregarComponente(diaDialogoso);

        diaDialogosr.setId("diaDialogosr");
        diaDialogosr.setTitle("BUSQUEDA DE CEDULA"); //titulo
        diaDialogosr.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogosr.setHeight("15%");//siempre porcentaje   alto
        diaDialogosr.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosr.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridr.setColumns(4);
        agregarComponente(diaDialogosr);

        diaDialogorec.setId("diaDialogorec");
        diaDialogorec.setTitle("BUSQUEDA DE SERVIDOR POR ANTICIPO"); //titulo
        diaDialogorec.setWidth("55%"); //siempre en porcentajes  ancho
        diaDialogorec.setHeight("50%");//siempre porcentaje   alto
        diaDialogorec.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogorec.getBot_aceptar().setMetodo("aceptoListado");
        gridRec.setColumns(4);
        agregarComponente(diaDialogorec);

        dibujarSolicitud();

    }

    //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }

    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autBusca.onSelect(evt);
        dibujarSolicitud();
    }

    //permite limpiar el formulario sin guardar o mantener algun dato
    public void limpia_pa() {
        if (tabAnticipo.getValor("ide_solicitud_anticipo") != null) {
            utilitario.agregarMensaje("Limpia Formulario", "No Guardado");
        } else {
            eliminar();
            insertar();
        }
    }

    //Platilla para ingreso de datos
    public void dibujarSolicitud() {
        limpiarPanel();
        tabAnticipo.setId("tabAnticipo");
        tabAnticipo.setConexion(conPostgres);
        tabAnticipo.setTabla("srh_solicitud_anticipo", "ide_solicitud_anticipo", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (autBusca.getValue() == null) {
            tabAnticipo.setCondicion("ide_solicitud_anticipo=-1");
        } else {
            tabAnticipo.setCondicion("ide_solicitud_anticipo=" + autBusca.getValor());
        }
        //Metodos para buscar los datos a llenar en el formulario
        tabAnticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");

        tabAnticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tabAnticipo.getColumna("cod_banco").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tabAnticipo.getColumna("cod_cuenta").setCombo("SELECT cod_cuenta,nombre FROM ocecuentas");
        tabAnticipo.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tabAnticipo.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tabAnticipo.getColumna("cod_grupo").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        tabAnticipo.getColumna("ide_tipo_anticipo").setCombo("SELECT ide_tipo_anticipo,tipo FROM srh_tipo_anticipo");

        tabAnticipo.getColumna("login_ingre_solicitud").setValorDefecto(utilitario.getVariable("NICK"));
        tabAnticipo.getColumna("ip_ingre_solicitud").setValorDefecto(utilitario.getIp());
        tabAnticipo.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabAnticipo.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tabAnticipo.getColumna("ide_tipo_ingreso_anticipo").setValorDefecto("E");

        tabAnticipo.getColumna("login_ingre_solicitud").setVisible(false);
        tabAnticipo.getColumna("IDE_EMPLEADO_SOLICITANTE").setVisible(false);
        tabAnticipo.getColumna("ip_ingre_solicitud").setVisible(false);
        tabAnticipo.getColumna("login_aprob_solicitud").setVisible(false);
        tabAnticipo.getColumna("ip_aprob_solicitud").setVisible(false);
        tabAnticipo.getColumna("aprobado_solicitante").setVisible(false);
        tabAnticipo.getColumna("fecha_aprobacion").setVisible(false);
        tabAnticipo.getColumna("ide_listado").setVisible(false);
        tabAnticipo.getColumna("fecha_listado").setVisible(false);
        tabAnticipo.getColumna("anio").setVisible(false);
        tabAnticipo.getColumna("periodo").setVisible(false);
        tabAnticipo.getColumna("ide_tipo_ingreso_anticipo").setVisible(false);
        tabAnticipo.getColumna("cod_grupo").setVisible(false);
        tabAnticipo.getColumna("cod_banco").setVisible(false);
        tabAnticipo.getColumna("cod_cuenta").setVisible(false);
        tabAnticipo.getColumna("numero_cuenta").setVisible(false);
        tabAnticipo.getColumna("usu_anulacion").setVisible(false);
        tabAnticipo.getColumna("comen_anulacion").setVisible(false);
        tabAnticipo.getColumna("fecha_anulacion").setVisible(false);
        tabAnticipo.getColumna("recal_usuario").setVisible(false);
        tabAnticipo.getColumna("recal_ip").setVisible(false);
        tabAnticipo.setTipoFormulario(true);
        tabAnticipo.agregarRelacion(tabGarante);
        tabAnticipo.agregarRelacion(tabParametros);
        tabAnticipo.getGrid().setColumns(4);
        tabAnticipo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setMensajeWarn("DATOS DE SOLICITANTE");
        tpa.setPanelTabla(tabAnticipo);

        tabGarante.setId("tabGarante");
        tabGarante.setConexion(conPostgres);
        tabGarante.setTabla("srh_garante_anticipo", "ide_garante_anticipo", 2);

        //Metodos para buscar los datos a llenar en el formulario del garante
        tabGarante.getColumna("ci_garante").setMetodoChange("llenarGarante");

        tabGarante.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tabGarante.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");

        tabGarante.getColumna("IDE_GARANTE_ANTICIPO ").setVisible(false);
        tabGarante.getColumna("IDE_EMPLEADO_GARANTE").setVisible(false);
        tabGarante.setTipoFormulario(true);
        tabGarante.getGrid().setColumns(8);
        tabGarante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setMensajeWarn("DATOS DE GARANTE");
        tpd.setPanelTabla(tabGarante);

        tabParametros.setId("tabParametros");
        tabParametros.setConexion(conPostgres);
        tabParametros.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 3);

        //Metodos para buscar los datos a llenar en el formulario del garante
        tabParametros.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tabParametros.getColumna("numero_cuotas_anticipo").setMetodoChange("porcentaje");
        tabParametros.getColumna("porcentaje_descuento_diciembre").setMetodoChange("cuotas");

        tabParametros.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tabParametros.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tabParametros.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");

        tabParametros.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());

        tabParametros.getColumna("porcentaje_descuento_diciembre").setLongitud(1);
        tabParametros.getColumna("val_cuo_adi").setLongitud(1);
        tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
        tabParametros.getColumna("val_cuo_adi").setLectura(true);
        tabParametros.getColumna("IDE_CALCULO_ANTICIPO").setVisible(false);
        tabParametros.getColumna("numero_cuotas_pagadas").setVisible(false);
        tabParametros.getColumna("valor_pagado").setVisible(false);
        tabParametros.getColumna("usu_anulacion").setVisible(false);
        tabParametros.getColumna("usu_pago_anticipado").setVisible(false);
        tabParametros.getColumna("fecha_pago_anticipado").setVisible(false);
        tabParametros.getColumna("numero_documento_pago").setVisible(false);
        tabParametros.getColumna("usu_cobra_liquidacion").setVisible(false);
        tabParametros.getColumna("fecha_cobro_liquidacion").setVisible(false);
        tabParametros.getColumna("comentario_cobro").setVisible(false);
        tabParametros.getColumna("ide_empleado").setVisible(false);

        tabParametros.setTipoFormulario(true);
        tabParametros.getGrid().setColumns(6);
        tabParametros.dibujar();

        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("DATOS DE ANTICIPO A SOLICITAR");
        tpp.setPanelTabla(tabParametros);

        PanelTabla t = new PanelTabla();
        Division div = new Division();
        div.setId("div");
        div.dividir2(tpp, t, "63%", "H");

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tpa, tpd, div, "36%", "49%", "H");
        agregarComponente(div_division);

        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        pan_opcion.getChildren().add(gru);
    }

    //BUSCAR SOLICITANTE POR CEDULA
    public void llenarDatosE() {//SOLICITANTE
        Integer anio = 0, mes = 0;
        TablaGenerica tab_dato = iAnticipos.VerifEmpleid(tabAnticipo.getValor("ci_solicitante"), Integer.parseInt(tabAnticipo.getValor("ide_tipo_anticipo")));
        if (!tab_dato.isEmpty()) {
            if (tab_dato.getValor("ide_tipo_anticipo").equals("1")) {
                utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Ordinario Pendiente");
            } else {
                utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo ExtraOrdinario Pendiente");
            }
        } else {
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                anio = utilitario.getAnio(utilitario.getFechaActual());
                mes = utilitario.getMes(utilitario.getFechaActual()) - 1;
            } else {
                anio = utilitario.getAnio(utilitario.getFechaActual()) - 1;
                mes = 12;
            }

            if (utilitario.validarCedula(tabAnticipo.getValor("ci_solicitante"))) {
                TablaGenerica tabDatos = iAnticipos.getEmpleadoInfo(tabAnticipo.getValor("ci_solicitante"), anio, mes);
                if (!tabDatos.isEmpty()) {
                    tabAnticipo.setValor("ide_empleado_solicitante", tabDatos.getValor("COD_EMPLEADO"));
                    tabAnticipo.setValor("ci_solicitante", tabDatos.getValor("cedula_pass"));
                    tabAnticipo.setValor("solicitante", tabDatos.getValor("nombres"));
                    tabAnticipo.setValor("rmu", tabDatos.getValor("ru"));
                    tabAnticipo.setValor("rmu_liquido_anterior", tabDatos.getValor("liquido"));
                    tabAnticipo.setValor("id_distributivo", tabDatos.getValor("id_distributivo_roles"));
                    tabAnticipo.setValor("cod_cargo", tabDatos.getValor("cod_cargo"));
                    tabAnticipo.setValor("cod_grupo", tabDatos.getValor("cod_grupo"));
                    tabAnticipo.setValor("cod_tipo", tabDatos.getValor("cod_tipo"));
                    tabAnticipo.setValor("cod_banco", tabDatos.getValor("cod_banco"));
                    tabAnticipo.setValor("cod_cuenta", tabDatos.getValor("cod_cuenta"));
                    tabAnticipo.setValor("numero_cuenta", tabDatos.getValor("numero_cuenta"));
                    utilitario.addUpdate("tabAnticipo");
                } else {
                    TablaGenerica tabDato = iAnticipos.getEmpleadoInf(tabAnticipo.getValor("ci_solicitante"));
                    if (!tabDato.isEmpty()) {
                        tabAnticipo.setValor("ide_empleado_solicitante", tabDato.getValor("COD_EMPLEADO"));
                        tabAnticipo.setValor("ci_solicitante", tabDato.getValor("cedula_pass"));
                        tabAnticipo.setValor("solicitante", tabDato.getValor("nombres"));
                        tabAnticipo.setValor("rmu", tabDato.getValor("remuneracion"));
                        tabAnticipo.setValor("rmu_liquido_anterior", tabDato.getValor("remuneracion"));
                        tabAnticipo.setValor("id_distributivo", tabDato.getValor("id_distributivo"));
                        tabAnticipo.setValor("cod_cargo", tabDato.getValor("cod_cargo"));
                        tabAnticipo.setValor("cod_grupo", tabDato.getValor("cod_grupo"));
                        tabAnticipo.setValor("cod_tipo", tabDato.getValor("cod_tipo"));
                        tabAnticipo.setValor("cod_banco", tabDato.getValor("cod_banco"));
                        tabAnticipo.setValor("cod_cuenta", tabDato.getValor("cod_cuenta"));
                        tabAnticipo.setValor("numero_cuenta", tabDato.getValor("numero_cuenta"));
                        utilitario.addUpdate("tabAnticipo");
                    } else {
                        utilitario.agregarMensaje("No existen datos generados para anticipo", null);
                    }
                }
            } else {
                utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
            }
        }
    }

    // BUSCAR GARANTE POR CEDULA
    public void llenarGarante() {
        if (utilitario.validarCedula(tabGarante.getValor("ci_garante"))) {
            TablaGenerica tabDato1 = iAnticipos.getGarante(tabGarante.getValor("ci_garante"));
            if (!tabDato1.isEmpty()) {
                tabGarante.setValor("garante", tabDato1.getValor("nombres"));
                tabGarante.setValor("ide_empleado_garante", tabDato1.getValor("cod_empleado"));
                tabGarante.setValor("cod_tipo", tabDato1.getValor("cod_tipo"));
                tabGarante.setValor("id_distributivo", tabDato1.getValor("id_distributivo"));
                utilitario.addUpdate("tabGarante");
            } else {
                utilitario.agregarMensajeInfo("Garante No Cumple Requisitos", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
        }
    }

    public void remuneracion() {
        double dato1 = 0, dato2 = 0, dato3 = 0;
        dato2 = Double.parseDouble(tabAnticipo.getValor("rmu"));
        dato1 = Double.parseDouble(tabParametros.getValor("valor_anticipo"));
        dato3 = (Double.parseDouble(tabAnticipo.getValor("rmu")) / 2);
        if (Integer.parseInt(tabAnticipo.getValor("ide_tipo_anticipo")) != 1) {
            if ((dato1 / dato2) <= 1) {
                utilitario.agregarMensaje("Ingrese Cuotas Para Cobro", "Por Mes");
            } else if ((dato1 / dato2) > 1 && (dato1 / dato2) <= 3) {//HASTA 3 REMUNERACIONES 
                tabParametros.getColumna("numero_cuotas_anticipo").setLectura(false);
                tabParametros.setValor("numero_cuotas_anticipo", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tabParametros.setValor("valor_cuota_mensual", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tabParametros");
                utilitario.agregarMensaje("Ingrese Cuotas Para Cobro", "Por Mes");
            } else {
                utilitario.agregarMensajeInfo("Monto Excede Remuneración", "");
                tabParametros.setValor("valor_cuota_mensual", "NULL");
                tabParametros.setValor("numero_cuotas_anticipo", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tabParametros.setValor("valor_cuota_mensual", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tabParametros");
            }
        } else {
            if (Double.parseDouble(tabParametros.getValor("valor_anticipo")) <= dato3) {
                tabParametros.setValor("numero_cuotas_anticipo", "1");
                utilitario.addUpdate("tabParametros");
                utilitario.agregarMensajeInfo("Anticipo Ordinario", "Plazo Maximo de Cobro, 1 Meses");
                llenarFecha();
                cuotas();
                tabParametros.getColumna("numero_cuotas_anticipo").setLectura(true);
            } else {
                utilitario.agregarMensaje("Monto Maximo en Anticipo Ordinario", "50% De la Remuneración");
                tabParametros.setValor("numero_cuotas_anticipo", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tabParametros");
            }
        }
    }

    //Verifica si se debe ingresar, el 70% para cobro
    public void porcentaje() {
        Integer porcentaje = 0;
        if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {//VALIDACION POR DIA HASTA 10
            if (tabAnticipo.getValor("id_distributivo").equals("1")) {
                porcentaje = utilitario.getMes(utilitario.getFechaActual()) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) - 1;
                if (porcentaje == 12) {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                } else if (porcentaje > 12) {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                } else {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                }
            } else {
                tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tabParametros.setValor("valor_cuota_mensual", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tabParametros");
                servidor();
            }
        } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {//VALIDACION POR DIAS DEL 11 AL 28
            if (tabAnticipo.getValor("id_distributivo").equals("1")) {
                porcentaje = utilitario.getMes(utilitario.getFechaActual()) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (porcentaje == 12) {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                } else if (porcentaje > 12) {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                } else {
                    tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tabParametros.setValor("valor_cuota_mensual", "NULL");
                    tabParametros.setValor("val_cuo_adi", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tabParametros");
                    servidor();
                }
            } else {
                tabParametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                tabParametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tabParametros.setValor("valor_cuota_mensual", "NULL");
                tabParametros.setValor("val_cuo_adi", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tabParametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tabParametros");
                servidor();
            }
        }
    }

    /*VALIDACION DE TIMEPO DE COBRO QUE SE SOLICITA PARA EL ANTICIPO*/
    public void servidor() {
        Integer anos = 0, dias = 0, meses = 0, mesesf = 0, aniosf = 0, diasf = 0, meses_a = 0, anios_a = 0, dias_a = 0, anof = 0, diaf = 0, mesf = 0;
        anios_a = utilitario.getAnio(utilitario.getFechaActual());
        meses_a = utilitario.getMes(utilitario.getFechaActual());
        dias_a = utilitario.getDia(utilitario.getFechaActual());
        if (tabAnticipo.getValor("id_distributivo").equals("1")) {
//            if(tabAnticipo.getValor("cod_tipo").equals("4") ||tabAnticipo.getValor("cod_tipo").equals("8")){
            if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {//VALIDACION POR DIA HASTA 10
                if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) >= 1 && Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) <= 12) {
                    Integer porcentaje = 0;
                    porcentaje = utilitario.getMes(utilitario.getFechaActual()) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) - 1;
                    if (porcentaje == 12) {
                        llenarFecha();
                        cuotas();
                    } else if (porcentaje > 12) {
                        llenarFecha();
                        utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                    } else {
                        llenarFecha();
                        cuotas();
                    }
                } else {
                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                }
            } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {//VALIDACION POR DIAS DEL 11 AL 28  
                if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) >= 1 && Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) <= 12) {
                    Integer porcentaje = 0;
                    porcentaje = utilitario.getMes(utilitario.getFechaActual()) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                    if (porcentaje == 12) {
                        llenarFecha();
                        cuotas();
                    } else if (porcentaje > 12) {
                        llenarFecha();
                        utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                    } else {
                        llenarFecha();
                        cuotas();
                    }
                } else {
                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                }
            }
        } else if (tabAnticipo.getValor("id_distributivo").equals("2")) {
            if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {//VALIDACION POR DIA HASTA 10
                if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) >= 1 && Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) <= 12) {
                    llenarFecha();
                    cuotas();
                } else {
                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                }
            } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {//VALIDACION POR DIAS DEL 11 AL 28  
                if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) >= 1 && Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) <= 12) {
                    llenarFecha();
                    cuotas();
                } else {
                    utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                }
            }
        }
    }

    //VALIDACION DE FECHAS
    public void llenarFecha() {

        Integer calculo = 0, calculo1 = 0, calculo2 = 0, calculo3 = 0, mess = 0;
        String mes, anio, dia, fecha, busca;

        anio = String.valueOf(utilitario.getAnio(tabParametros.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tabParametros.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")));
        mess = utilitario.getMes(tabParametros.getValor("FECHA_ANTICIPO"));
        if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {
            if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) == 12) {
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (calculo1 < 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabParametros");
                        //fecha final
                        if (mes.equals("1")) {//mes de enero
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"))), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        } else {//otros meses
                            TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes) - 1), String.valueOf(Integer.parseInt(anio) + 1));
                            if (!tab_dato2.isEmpty()) {
                                fecha = tab_dato2.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            } else if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) < 12) {
                Integer cuota = 0;
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (calculo1 < 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabParametros");
                        //fecha final
                        calculo2 = calculo1 * -1;
                        cuota = Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));

                        if (mes.equals("2")) {//para febrero
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        } else if (mes.equals("3")) {
                            if (cuota == 11) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("4")) {
                            if (cuota == 11 || cuota == 10) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("5")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("6")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("7")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("8")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7 || cuota == 6) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("9")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7 || cuota == 6 || cuota == 5) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("10")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7 || cuota == 6 || cuota == 5 || cuota == 4) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("11")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7 || cuota == 6 || cuota == 5 || cuota == 4 || cuota == 3) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        } else if (mes.equals("12")) {
                            if (cuota == 11 || cuota == 10 || cuota == 9 || cuota == 8 || cuota == 7 || cuota == 6 || cuota == 5 || cuota == 4 || cuota == 1) {
                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2 - 1), String.valueOf(Integer.parseInt(anio) + 1));
                                if (!tab_datoa.isEmpty()) {
                                    fecha = tab_datoa.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            } else {
                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato1.isEmpty()) {
                                    fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                    tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                    utilitario.addUpdate("tabParametros");
                                } else {
                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                            }
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                } else if (calculo1 > 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        if (Integer.parseInt(tabAnticipo.getValor("ide_tipo_anticipo")) != 1) {
                            //fecha inicial
                            fecha = tab_dato.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                            utilitario.addUpdate("tabAnticipo");
                            //fecha final
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        } else {
                            //fecha inicial
                            fecha = tab_dato.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                            utilitario.addUpdate("tabAnticipo");
                            //fecha final
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                } else if (calculo1 == 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        if (Integer.parseInt(tabAnticipo.getValor("ide_tipo_anticipo")) != 1) {
                            //fecha inicial
                            fecha = tab_dato.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                            utilitario.addUpdate("tabParametros");
                            //fecha final
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        } else {
                            //fecha inicial
                            fecha = tab_dato.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                            utilitario.addUpdate("tabAnticipo");
                            //fecha final
                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess - 1), String.valueOf(Integer.parseInt(anio)));
                            if (!tab_dato1.isEmpty()) {
                                fecha = tab_dato1.getValor("ide_periodo_anticipo");
                                tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                                utilitario.addUpdate("tabParametros");
                            } else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                            }
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            }
        } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {
            if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) == 12) {
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (calculo1 < 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes) + 1), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabParametros");
                        //fecha final
                        TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes)), String.valueOf(Integer.parseInt(anio) + 1));
                        if (!tab_dato2.isEmpty()) {
                            fecha = tab_dato2.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                            utilitario.addUpdate("tabParametros");
                        } else {
                            utilitario.agregarMensajeInfo("No existen Datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            } else if (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) < 12) {
                Integer cuota = 0;
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (calculo1 < 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes) + 1), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabParametros");
                        //fecha final
                        calculo2 = calculo1 * -1;
                        cuota = Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo2), String.valueOf(Integer.parseInt(anio) + 1));
                        if (!tab_dato1.isEmpty()) {
                            fecha = tab_dato1.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                            utilitario.addUpdate("tabParametros");
                        } else {
                            utilitario.agregarMensajeInfo("No existen Datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                } else if (calculo1 > 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes) + 1), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabAnticipo");
                        //fecha final
                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess), String.valueOf(Integer.parseInt(anio)));
                        if (!tab_dato1.isEmpty()) {
                            fecha = tab_dato1.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                            utilitario.addUpdate("tabParametros");
                        } else {
                            utilitario.agregarMensajeInfo("No existen Datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                } else if (calculo1 == 0) {
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes) + 1), String.valueOf(Integer.parseInt(anio)));
                    if (!tab_dato.isEmpty()) {
                        //fecha inicial
                        fecha = tab_dato.getValor("ide_periodo_anticipo");
                        tabParametros.setValor("ide_periodo_anticipo_inicial", fecha);
                        utilitario.addUpdate("tabAnticipo");
                        //fecha final
                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) + mess), String.valueOf(Integer.parseInt(anio)));
                        if (!tab_dato1.isEmpty()) {
                            fecha = tab_dato1.getValor("ide_periodo_anticipo");
                            tabParametros.setValor("ide_periodo_anticipo_final", fecha);
                            utilitario.addUpdate("tabParametros");
                        } else {
                            utilitario.agregarMensajeInfo("No existen Datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            }
        }
    }

    //CALCULO DE CUOTAS Y CUOTA ESPECIAL
    public void cuotas() {

        Integer periodo = 0;
        if (tabAnticipo.getValor("id_distributivo").equals("1")) {
            if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {
                periodo = utilitario.getMes(tabParametros.getValor("FECHA_ANTICIPO")) - 1 + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (periodo > 12) {
                    if (Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")) >= 70 && Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")) <= 100) {
                        calculo_valor();
                    } else {
                        utilitario.agregarMensaje("Al menos el 70% de Sueldo", "Para Cuota de Diciembre");
                    }
                } else {
                    calculo_valor();
                }
            } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {
                periodo = utilitario.getMes(tabParametros.getValor("FECHA_ANTICIPO")) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                if (periodo > 12) {
                    if (Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")) >= 70 && Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")) <= 100) {
                        calculo_valor();
                    } else {
                        utilitario.agregarMensaje("Al menos el 70% de Sueldo", "Maximo el 100%, Para Cuota de Diciembre");
                    }
                } else {
                    calculo_valor();
                }
            }
        } else {
            calculo_valor();
        }
    }

    public void calculo_valor() {
        Integer rango;
        double valora = 0, valora1 = 0, valorm = 0, media = 0, rmu = 0, valan = 0, valorff = 0;
        rmu = Double.parseDouble(tabAnticipo.getValor("rmu"));
        valan = Double.parseDouble(tabParametros.getValor("valor_anticipo"));
        media = Double.parseDouble(tabAnticipo.getValor("rmu_liquido_anterior")) / 2;
        if (tabAnticipo.getValor("ide_tipo_anticipo").equals("1")) {
        } else {
            if (tabAnticipo.getValor("id_distributivo").equals("1")) {
                if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 15) {
                    TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tabParametros.getValor("ide_periodo_anticipo_inicial")));
                    if (!tab_dato.isEmpty()) {
                        rango = Integer.parseInt(tab_dato.getValor("periodo")) + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) - 1;
                        if (rango > 12) {
                            valora = ((rmu * (Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")))) / 100);
                            if (valora >= Double.parseDouble(tabParametros.getValor("valor_anticipo"))) {
                                utilitario.agregarMensajeError("Cuota Diciembre Excede Sueldo Anterior", "");
                            } else {
                                valorm = (valan - valora) / (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) - 1);
                                tabParametros.setValor("val_cuo_adi", String.valueOf(valora));
                                tabParametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm * 100) / 100));
                                utilitario.addUpdate("tabParametros");
                            }
                        } else if (rango <= 12) {
                            valora1 = Double.parseDouble(tabParametros.getValor("valor_anticipo")) / Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                            tabParametros.setValor("val_cuo_adi", "NULL");
                            tabParametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1 * 100) / 100));
                            utilitario.addUpdate("tabParametros");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                } else if (utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) >= 16 && utilitario.getDia(tabParametros.getValor("FECHA_ANTICIPO")) <= 31) {
                    TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tabParametros.getValor("ide_periodo_anticipo_inicial")));
                    if (!tab_dato.isEmpty()) {
                        rango = (Integer.parseInt(tab_dato.getValor("periodo")) - 1 + Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")));
                        if (rango > 12) {
                            valora = ((rmu * (Integer.parseInt(tabParametros.getValor("porcentaje_descuento_diciembre")))) / 100);
                            if (valora >= Double.parseDouble(tabParametros.getValor("valor_anticipo"))) {
                                utilitario.agregarMensajeError("Cuota Diciembre Excede Sueldo Anterior", "");
                            } else {
                                valorm = (valan - valora) / (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")) - 1);
                                tabParametros.setValor("val_cuo_adi", String.valueOf(valora));
                                tabParametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm * 100) / 100));
                                utilitario.addUpdate("tabParametros");
                            }
                        } else if (rango <= 12) {
                            valora1 = Double.parseDouble(tabParametros.getValor("valor_anticipo")) / Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo"));
                            tabParametros.setValor("val_cuo_adi", "NULL");
                            tabParametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1 * 100) / 100));
                            utilitario.addUpdate("tabParametros");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            } else {
                TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tabParametros.getValor("ide_periodo_anticipo_inicial")));
                if (!tab_dato.isEmpty()) {
                    valorm = (valan) / (Integer.parseInt(tabParametros.getValor("numero_cuotas_anticipo")));
                    tabParametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm * 100) / 100));
                    utilitario.addUpdate("tabParametros");
                } else {
                    utilitario.agregarMensajeInfo("No existen Datos", "");
                }
            }
        }
    }

    //Permite Buscar solicitud que se encuentra Ingresada o Pendiente
    public void Busca_tipo() {
        diaDialogoca.Limpiar();
        gridCa.getChildren().add(new Etiqueta("ELEGIR PARAMETRO DE BUSQUEDA:"));
        gridCa.getChildren().add(cmbSeleccion);
        diaDialogoca.setDialogo(gridCa);
        diaDialogoca.dibujar();
    }

    public void buscarSolicitud() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo WHERE ci_solicitante LIKE '" + texBusqueda.getValue() + "'");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        diaDialogoso.Limpiar();
        diaDialogoso.setDialogo(gridso);
        gridSo.getChildren().add(setSolicitu);
        setSolicitu.setId("setSolicitu");
        setSolicitu.setConexion(conPostgres);
        setSolicitu.setHeader("LISTADO DE SERVIDORES");
        setSolicitu.setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo where ci_solicitante is not null");
        setSolicitu.getColumna("solicitante").setFiltro(true);
        setSolicitu.setRows(10);
        setSolicitu.setTipoSeleccion(false);
        diaDialogoso.setDialogo(gridSo);
        setSolicitu.dibujar();
        diaDialogoso.dibujar();
    }

    public void abrirBusqueda() {
        if (cmbSeleccion.getValue().equals("1")) {
            setSolicitud.dibujar();
            texBusqueda.limpiar();
            setSolicitud.getTab_seleccion().limpiar();
        } else {
            buscarSolicitud1();
        }
    }

    public void aceptarBusqueda() {
        if (cmbSeleccion.getValue().equals("1")) {
            if (setSolicitud.getValorSeleccionado() != null) {
                autBusca.setValor(setSolicitud.getValorSeleccionado());
                setSolicitud.cerrar();
                dibujarSolicitud();
                diaDialogoca.cerrar();
                utilitario.addUpdate("autBusca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
            }
        } else {
            if (setSolicitu.getValorSeleccionado() != null) {
                autBusca.setValor(setSolicitu.getValorSeleccionado());
                diaDialogoso.cerrar();
                dibujarSolicitud();
                diaDialogoca.cerrar();
                utilitario.addUpdate("autBusca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
            }
        }
    }

    public void listRecalculo() {

        diaDialogorec.Limpiar();
        diaDialogorec.setDialogo(gridrec);
        gridRec.getChildren().add(setListado);
        setListado.setId("setListado");
        setListado.setConexion(conPostgres);
        setListado.setHeader("LISTADO ANTICIPOS VIGENTES");
        setListado.setSql("select s.ide_solicitud_anticipo,\n"
                + "s.ci_solicitante,\n"
                + "s.solicitante,\n"
                + "c.valor_anticipo,\n"
                + "c.numero_cuotas_anticipo,\n"
                + "c.valor_cuota_mensual,\n"
                + "c.val_cuo_adi,\n"
                + "c.valor_pagado\n"
                + "from srh_solicitud_anticipo s\n"
                + "inner join srh_calculo_anticipo c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo\n"
                + "where c.ide_estado_anticipo in (1,2,3)\n"
                + "order by s.ide_solicitud_anticipo ASC");
        setListado.getColumna("ci_solicitante").setFiltro(true);
        setListado.getColumna("solicitante").setFiltro(true);
        setListado.setRows(10);
        setListado.setTipoSeleccion(false);
        diaDialogorec.setDialogo(gridRec);
        setListado.dibujar();
        diaDialogorec.dibujar();
    }

    public void aceptoListado() {
        double anticipo = 0.0, pagado, remuneracion = 0.0, cuota = 0.0, cuota1 = 0.0, diferencia = 0.0, cuotan = 0.0;
        BigDecimal bd, acu, cuo;
        TablaGenerica tabLista = iAnticipos.getReCalculo(Integer.parseInt(setListado.getValorSeleccionado()));
        if (!tabLista.isEmpty()) {
            anticipo = Double.parseDouble(tabLista.getValor("valor_anticipo"));
            pagado = Double.parseDouble(tabLista.getValor("valor_pagado"));
            remuneracion = Double.parseDouble(tabLista.getValor("remuneracion"));
            if (tabLista.getValor("porcentaje_descuento_diciembre") != null) {
                bd = new BigDecimal((remuneracion * (Integer.parseInt(tabLista.getValor("porcentaje_descuento_diciembre"))) / 100));
                cuota = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                diferencia = ((anticipo - pagado) - cuota);
                TablaGenerica tabDetalle = iAnticipos.getDetalleReCalculo(Integer.parseInt(setListado.getValorSeleccionado()));
                if (!tabDetalle.isEmpty()) {
                    for (int i = 0; i < tabDetalle.getTotalFilas() - 1; i++) {
                        if (tabDetalle.getValor(i, "periodo").equals("12")) {
                            iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuota, Integer.parseInt(tabDetalle.getValor(i, "ide_detalle_anticipo")));
                        } else {
                            cuo = new BigDecimal(diferencia / (tabDetalle.getTotalFilas() - 1));
                            cuotan = cuo.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuotan, Integer.parseInt(tabDetalle.getValor(i, "ide_detalle_anticipo")));
                        }
                    }
                    TablaGenerica tabIdDetalle = iAnticipos.getIdDetalleReCalculo(Integer.parseInt(setListado.getValorSeleccionado()));
                    if (!tabIdDetalle.isEmpty()) {
                        acu = new BigDecimal((anticipo - (((((cuotan)) * (tabDetalle.getTotalFilas() - 2)) + cuota) + pagado)));
                        cuota1 = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuota1, Integer.parseInt(tabIdDetalle.getValor(" ide_detalle_anticipo")));
                        iAnticipos.setRecacular(Integer.parseInt(setListado.getValorSeleccionado()), cuotan, cuota);
                        iAnticipos.setResolicitud(Integer.parseInt(setListado.getValorSeleccionado()), utilitario.getIp(), utilitario.getVariable("NICK"));
                    }
                }
            } else {
                bd = new BigDecimal(remuneracion / 100);
                cuota = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                diferencia = ((anticipo - pagado) - cuota);
                TablaGenerica tabDetalle = iAnticipos.getDetalleReCalculo(Integer.parseInt(setListado.getValorSeleccionado()));
                if (!tabDetalle.isEmpty()) {
                    for (int i = 0; i < tabDetalle.getTotalFilas() - 1; i++) {
                        if (tabDetalle.getValor(i, "periodo").equals("12")) {
                            iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuota, Integer.parseInt(tabDetalle.getValor(i, "ide_detalle_anticipo")));
                        } else {
                            cuo = new BigDecimal(diferencia / (tabDetalle.getTotalFilas() - 1));
                            cuotan = cuo.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuotan, Integer.parseInt(tabDetalle.getValor(i, "ide_detalle_anticipo")));
                        }
                    }
                    TablaGenerica tabIdDetalle = iAnticipos.getIdDetalleReCalculo(Integer.parseInt(setListado.getValorSeleccionado()));
                    if (!tabIdDetalle.isEmpty()) {
                        acu = new BigDecimal((anticipo - (((((cuotan)) * (tabDetalle.getTotalFilas() - 2)) + cuota) + pagado)));
                        cuota1 = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        iAnticipos.setRecalculo(Integer.parseInt(setListado.getValorSeleccionado()), cuota1, Integer.parseInt(tabIdDetalle.getValor(" ide_detalle_anticipo")));
                        iAnticipos.setRecacular(Integer.parseInt(setListado.getValorSeleccionado()), cuotan, cuota);
                        iAnticipos.setResolicitud(Integer.parseInt(setListado.getValorSeleccionado()), utilitario.getIp(), utilitario.getVariable("NICK"));
                    }
                }
            }
        } else {
            utilitario.agregarMensaje("Anticipo No Encontrado", null);
        }
        setListado.actualizar();
    }

    @Override
    public void insertar() {
        if (tabAnticipo.isFocus()) {
            autBusca.limpiar();
            utilitario.addUpdate("autBusca");
            tabAnticipo.limpiar();
            tabAnticipo.insertar();
            tabGarante.limpiar();
            tabGarante.insertar();
            tabParametros.limpiar();
            tabParametros.insertar();
        }
    }

    @Override
    public void guardar() {
        double dato3 = 0;
        if (tabAnticipo.getValor("ide_solicitud_anticipo") != null) {
            utilitario.agregarMensaje("Registro No Puede Ser Guardado", "");
        } else {
            dato3 = (Double.parseDouble(tabAnticipo.getValor("rmu")) / 2);
            if (tabAnticipo.getValor("ide_tipo_anticipo").equals("1")) {
                if (Double.parseDouble(tabParametros.getValor("valor_anticipo")) <= dato3) {
                    if (tabAnticipo.guardar()) {
                        if (tabGarante.guardar()) {
                            tabParametros.setValor("ide_estado_anticipo", "1");
                            if (tabParametros.guardar()) {
                                conPostgres.guardarPantalla();
                            }
                        }
                    }
                } else {
                    utilitario.agregarMensaje("Registro No Puede Ser Guardado", "Anticipo Ordinario No Cumple Condición");
                }
            } else {
                if (tabAnticipo.guardar()) {
                    if (tabGarante.guardar()) {
                        tabParametros.setValor("ide_estado_anticipo", "1");
                        if (tabParametros.guardar()) {
                            conPostgres.guardarPantalla();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tabParametros.isFocus()) {
            if (tabParametros.eliminar()) {
                if (tabGarante.eliminar()) {
                    tabAnticipo.eliminar();
                }
            }
        }
    }

    //CALCULAR MESES
    public static int calcularMes(Calendar cal1, Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60; // calcular la diferencia en horas a
        long diffDays = diffHours / 24; // calcular la diferencia en dias
        long diffWeek = diffDays / 7; // calcular la diferencia en semanas
        long diffMounth = diffWeek / 4; // calcular la diferencia en meses
        return Integer.parseInt(String.valueOf(diffMounth));
    }

    //CALCULAR ANIOS
    public static int calcularAnios(Calendar AnioLab) {
        Calendar fechaActual = Calendar.getInstance();
        // Cálculo de las diferencias.
        int anios = fechaActual.get(Calendar.YEAR) - AnioLab.get(Calendar.YEAR);
        int meses = fechaActual.get(Calendar.MONTH) - AnioLab.get(Calendar.MONTH);
        int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - AnioLab.get(Calendar.DAY_OF_MONTH);
        if (meses < 0 // Aún no es el mes 
                || (meses == 0 && dias < 0)) { // o es el mes pero no ha llegado el día.
            anios--;
        }
        return anios;
    }

    //BUSQUEDA DE MES PARA LA AISGNACION DEL PERIODO
    public String meses(Integer numero) {
        switch (numero) {
            case 12:
                selecMes = "DICIEMBRE";
                break;
            case 11:
                selecMes = "NOVIEMBRE";
                break;
            case 10:
                selecMes = "OCTUBRE";
                break;
            case 9:
                selecMes = "SEPTIEMBRE";
                break;
            case 8:
                selecMes = "AGOSTO";
                break;
            case 7:
                selecMes = "JULIO";
                break;
            case 6:
                selecMes = "JUNIO";
                break;
            case 5:
                selecMes = "MAYO";
                break;
            case 4:
                selecMes = "ABRIL";
                break;
            case 3:
                selecMes = "MARZO";
                break;
            case 2:
                selecMes = "FEBRERO";
                break;
            case 1:
                selecMes = "ENERO";
                break;
        }
        return selecMes;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabAnticipo() {
        return tabAnticipo;
    }

    public void setTabAnticipo(Tabla tabAnticipo) {
        this.tabAnticipo = tabAnticipo;
    }

    public Tabla getTabParametros() {
        return tabParametros;
    }

    public void setTabParametros(Tabla tabParametros) {
        this.tabParametros = tabParametros;
    }

    public Tabla getTabConsulta() {
        return tabConsulta;
    }

    public void setTabConsulta(Tabla tabConsulta) {
        this.tabConsulta = tabConsulta;
    }

    public Tabla getSetColaborador() {
        return setColaborador;
    }

    public void setSetColaborador(Tabla setColaborador) {
        this.setColaborador = setColaborador;
    }

    public Tabla getSetSolicitante() {
        return setSolicitante;
    }

    public void setSetSolicitante(Tabla setSolicitante) {
        this.setSolicitante = setSolicitante;
    }

    public Tabla getSetSolicitu() {
        return setSolicitu;
    }

    public void setSetSolicitu(Tabla setSolicitu) {
        this.setSolicitu = setSolicitu;
    }

    public Tabla getSetListado() {
        return setListado;
    }

    public void setSetListado(Tabla setListado) {
        this.setListado = setListado;
    }

    public SeleccionTabla getSetSolicitud() {
        return setSolicitud;
    }

    public void setSetSolicitud(SeleccionTabla setSolicitud) {
        this.setSolicitud = setSolicitud;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getTabGarante() {
        return tabGarante;
    }

    public void setTabGarante(Tabla tabGarante) {
        this.tabGarante = tabGarante;
    }
}
