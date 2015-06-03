/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.decimoCuarto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class AccesoSistemas extends Pantalla {

    //Conexion a base
    private Conexion conPostgres = new Conexion();
    //Tablas
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla setTabla = new SeleccionTabla();
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    @EJB
    private decimoCuarto datosEmpledo = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Calendario calFechaInicio = new Calendario();
    private Calendario calFechaFin = new Calendario();

    public AccesoSistemas() {
        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);

        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(conPostgres);
        aut_busca.setAutoCompletar("SELECT s.id_solicitud_acceso,\n"
                + "s.cedula_solicitante,\n"
                + "s.nombre_solicitante,\n"
                + "s.direccion_solicitante,\n"
                + "s.nombre_usuario,\n"
                + "a.nombre_sistema\n"
                + "FROM sca_solicitud_acceso s\n"
                + "INNER JOIN sca_sistemas a ON s.id_sistema = a.id_sistema\n");
//                + "WHERE s.estado_solicitud = 'Solicitud'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);

        bar_botones.agregarComponente(new Etiqueta("Solicitud de Acceso:"));
        bar_botones.agregarComponente(aut_busca);

        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE CLAVE DE ACCESO A SISTEMAS");
        agregarComponente(pan_opcion);

        dibujarSolicitud();

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        /*CONFIGURACIÓN DE COMBOS*/
        Grid griBusca = new Grid();
        griBusca.setColumns(2);

        griBusca.getChildren().add(new Etiqueta("FECHA INICIO:"));
        griBusca.getChildren().add(calFechaInicio);
        griBusca.getChildren().add(new Etiqueta("FECHA FINAL:"));
        griBusca.getChildren().add(calFechaFin);

        Boton botAcceso = new Boton();
        botAcceso.setValue("Buscar");
        botAcceso.setIcon("ui-icon-search");
        botAcceso.setMetodo("aceptarRegistro");
        bar_botones.agregarBoton(botAcceso);
        griBusca.getChildren().add(botAcceso);

        setTabla.setId("setTabla");
        setTabla.getTab_seleccion().setConexion(conPostgres);
        setTabla.setSeleccionTabla("SELECT id_solicitud_acceso,fechaing_solicitante as fecha_solicitud,nombre_usuario,nombre_solicitante,direccion_solicitante\n"
                + "FROM sca_solicitud_acceso\n"
                + "where id_solicitud_acceso=-1\n"
                + "ORDER BY id_solicitud_acceso desc, id_solicitud_acceso", "id_solicitud_acceso");
        setTabla.getTab_seleccion().getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion");
        setTabla.getTab_seleccion().getColumna("direccion_solicitante").setLongitud(40);
        setTabla.getTab_seleccion().getColumna("nombre_usuario").setLongitud(40);
        setTabla.getTab_seleccion().getColumna("nombre_solicitante").setLongitud(40);
        setTabla.getTab_seleccion().setEmptyMessage("No Encuentra Datos");
        setTabla.getTab_seleccion().setRows(10);
        setTabla.setWidth("70%");
        setTabla.setRadio();
        setTabla.getGri_cuerpo().setHeader(griBusca);
        setTabla.getBot_aceptar().setMetodo("aceptarBusqueda");
        setTabla.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setTabla);
    }

    //Permite Buscar solicitud que se encuentra Ingresada o Pendiente
    public void Busca_tipo() {
        setTabla.dibujar();
    }

    public void aceptarRegistro() {
        if (calFechaInicio.getValue() != null && calFechaFin.getValue() != null) {
            setTabla.getTab_seleccion().setSql("SELECT id_solicitud_acceso,fechaing_solicitante as fecha_solicitud,nombre_usuario,nombre_solicitante,direccion_solicitante\n"
                    + "FROM sca_solicitud_acceso\n"
                    + "where fechaing_solicitante between '" + calFechaInicio.getFecha() + "' and '" + calFechaFin.getFecha() + "' ORDER BY id_solicitud_acceso desc");
            setTabla.getTab_seleccion().getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion");
            setTabla.getTab_seleccion().getColumna("direccion_solicitante").setLongitud(40);
            setTabla.getTab_seleccion().getColumna("nombre_usuario").setLongitud(40);
            setTabla.getTab_seleccion().getColumna("nombre_solicitante").setLongitud(40);
            setTabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un opción", "");
        }
    }

    //Dibuja la Pantalla
    public void aceptarBusqueda() {
        if (setTabla.getValorSeleccionado() != null) {
            aut_busca.setValor(setTabla.getValorSeleccionado());
            setTabla.cerrar();
            dibujarSolicitud();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
        }
    }

    public void dibujarSolicitud() {
        limpiarPanel();
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setConexion(conPostgres);
        tab_solicitud.setTabla("sca_solicitud_acceso", "id_solicitud_acceso", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_solicitud.setCondicion("id_solicitud_acceso=-1");
        } else {
            tab_solicitud.setCondicion("id_solicitud_acceso=" + aut_busca.getValor());
        }

        tab_solicitud.getColumna("fechaing_solicitante").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("fecha_acceso_usuario").setValorDefecto(utilitario.getFechaActual());

        tab_solicitud.getColumna("id_sistema").setCombo("SELECT id_sistema,nombre_sistema FROM sca_sistemas order by nombre_sistema");
        tab_solicitud.getColumna("id_modulo").setCombo("SELECT id_modulo,nombre_modulo FROM sca_modulos order by nombre_modulo");
        tab_solicitud.getColumna("id_perfil").setCombo("SELECT id_perfil,nombre_perfil FROM sca_perfiles order by nombre_perfil");
        tab_solicitud.getColumna("codigo_usuario").setCombo("SELECT cod_empleado,nombres FROM srh_empleado order by nombres");
        tab_solicitud.getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion order by nombre_dir");
        tab_solicitud.getColumna("cargo_solicitante").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("cargo_usuario").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("codigo_solicitante").setCombo("SELECT cod_empleado,nombres FROM srh_empleado order by nombres");
        tab_solicitud.getColumna("codigo_asigna_acceso").setCombo("SELECT cod_empleado,nombres FROM srh_empleado order by nombres");
        tab_solicitud.getColumna("ingreso_perfil_usuario").setCheck();
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setCheck();
        tab_solicitud.getColumna("lectura_perfil_usuario").setCheck();

        tab_solicitud.getColumna("codigo_solicitante").setMetodoChange("datosSolicitante");
        tab_solicitud.getColumna("codigo_usuario").setMetodoChange("datosUsuario");
        tab_solicitud.getColumna("id_sistema").setMetodoChange("datosModulo");
        tab_solicitud.getColumna("id_modulo").setMetodoChange("datosPerfil");
        tab_solicitud.getColumna("id_perfil").setMetodoChange("activaCasillas");

        tab_solicitud.getColumna("bandera_solicitante").setEtiqueta();
        tab_solicitud.getColumna("bandera_usuario").setEtiqueta();
        tab_solicitud.getColumna("bandera_perfil").setEtiqueta();

        tab_solicitud.getColumna("ingreso_perfil_usuario").setLectura(true);
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setLectura(true);
        tab_solicitud.getColumna("lectura_perfil_usuario").setLectura(true);

        tab_solicitud.getColumna("id_solicitud_acceso").setVisible(false);
        tab_solicitud.getColumna("estado_solicitud").setVisible(false);
        tab_solicitud.getColumna("cedula_solicitante").setVisible(false);
        tab_solicitud.getColumna("nombre_solicitante").setVisible(false);
        tab_solicitud.getColumna("cedula_usuario").setVisible(false);
        tab_solicitud.getColumna("nombre_usuario").setVisible(false);
        tab_solicitud.getColumna("direccion_usuario").setVisible(false);
        tab_solicitud.getColumna("fechact_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("lectura_perfil_usuario").setVisible(false);
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setVisible(false);
        tab_solicitud.getColumna("ingreso_perfil_usuario").setVisible(false);
        tab_solicitud.getColumna("cedula_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("nombre_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("logining_solicitante").setVisible(false);
        tab_solicitud.getColumna("fechact_solicitante").setVisible(false);
        tab_solicitud.getColumna("loginact_solcitante").setVisible(false);
        tab_solicitud.getColumna("logining_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("loginact_acceso_usuario").setVisible(false);

        tab_solicitud.getColumna("logining_solicitante").setValorDefecto(utilitario.getVariable("NICK"));
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tps = new PanelTabla();
        tps.setPanelTabla(tab_solicitud);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(tps);
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        pan_opcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }

    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujarSolicitud();
    }

    public void datosModulo() {
        tab_solicitud.getColumna("id_modulo").setCombo("SELECT id_modulo,nombre_modulo FROM sca_modulos where id_sistema=" + Integer.parseInt(tab_solicitud.getValor("id_sistema")) + " order by nombre_modulo");
        utilitario.addUpdateTabla(tab_solicitud, "id_modulo", "");
    }

    public void datosPerfil() {
        tab_solicitud.getColumna("id_perfil").setCombo("SELECT id_perfil,nombre_perfil FROM sca_perfiles where id_modulo=" + Integer.parseInt(tab_solicitud.getValor("id_modulo")) + " order by nombre_perfil");
        utilitario.addUpdateTabla(tab_solicitud, "id_perfil", "");
    }

    public void datosUsuario() {
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleado(tab_solicitud.getValor("codigo_usuario"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_usuario", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_usuario", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("cargo_usuario", tab_dato.getValor("cod_cargo"));
            tab_solicitud.setValor("direccion_usuario", tab_dato.getValor("cod_direccion"));
            utilitario.addUpdate("tab_solicitud");
        } else {
            utilitario.agregarMensaje("Usuario Sin Datos", "");
        }
    }

    public void datosSolicitante() {
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleado(tab_solicitud.getValor("codigo_solicitante"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_solicitante", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_solicitante", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("cargo_solicitante", tab_dato.getValor("cod_cargo"));
            tab_solicitud.setValor("direccion_solicitante", tab_dato.getValor("cod_direccion"));
//            utilitario.addUpdate("tab_solicitud");
            datosUsuarioAcc();
        } else {
            utilitario.agregarMensaje("Solicitante Sin Datos", "");
        }
    }

    public void activaCasillas() {
        tab_solicitud.getColumna("ingreso_perfil_usuario").setLectura(false);
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setLectura(false);
        tab_solicitud.getColumna("lectura_perfil_usuario").setLectura(false);
        utilitario.addUpdate("tab_solicitud");
    }

    public void estadoAcceso() {
        tab_solicitud.setValor("estado_solicitud", "Asignada");
        utilitario.addUpdate("tab_solicitud");
    }

    public void datosUsuarioAcc() {
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleados(tab_consulta.getValor("NOM_USUA"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_asigna_acceso", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_asigna_acceso", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("codigo_asigna_acceso", tab_dato.getValor("cod_empleado"));
            utilitario.addUpdate("tab_solicitud");
        } else {
            utilitario.agregarMensaje("Usuario Sin Datos", "");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_solicitud.getValor("id_solicitud_acceso") != null) {
            TablaGenerica tab_dato = datosEmpledo.getSolicitudAcceso(Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
            if (!tab_dato.isEmpty()) {
                if (tab_dato.getValor("ID_SISTEMA") != tab_solicitud.getValor("ID_SISTEMA")) {
                    datosEmpledo.setAccesoSistemas("ID_SISTEMA", tab_solicitud.getValor("ID_SISTEMA"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("ID_PERFIL") != tab_solicitud.getValor("ID_PERFIL")) {
                    datosEmpledo.setAccesoSistemas("ID_PERFIL", tab_solicitud.getValor("ID_PERFIL"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("ID_MODULO") != tab_solicitud.getValor("ID_MODULO")) {
                    datosEmpledo.setAccesoSistemas("ID_MODULO", tab_solicitud.getValor("ID_MODULO"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("login_acceso_usuario") != tab_solicitud.getValor("login_acceso_usuario")) {
                    datosEmpledo.setAccesoSistemas("login_acceso_usuario", tab_solicitud.getValor("login_acceso_usuario"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("password_acceso_usuario") != tab_solicitud.getValor("password_acceso_usuario")) {
                    datosEmpledo.setAccesoSistemas("password_acceso_usuario", tab_solicitud.getValor("password_acceso_usuario"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("fecha_acceso_usuario") != tab_solicitud.getValor("fecha_acceso_usuario")) {
                    datosEmpledo.setAccesoSistemas("fecha_acceso_usuario", tab_solicitud.getValor("fecha_acceso_usuario"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("codigo_asigna_acceso") != tab_solicitud.getValor("codigo_asigna_acceso")) {
                    datosEmpledo.setAccesoSistemas("codigo_asigna_acceso", tab_solicitud.getValor("codigo_asigna_acceso"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("cargo_solicitante") != tab_solicitud.getValor("cargo_solicitante")) {
                    datosEmpledo.setAccesoSistemas("cargo_solicitante", tab_solicitud.getValor("cargo_solicitante"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                if (tab_dato.getValor("direccion_solicitante") != tab_solicitud.getValor("direccion_solicitante")) {
                    datosEmpledo.setAccesoSistemas("direccion_solicitante", tab_solicitud.getValor("direccion_solicitante"), Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso")));
                }
                utilitario.agregarMensaje("Registro Guardado", null);
            }
        } else {
            estadoAcceso();
            if (tab_solicitud.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "SOLICITUD ACCESO":
                aceptoAnticipo();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "SOLICITUD ACCESO":
                p_parametros.put("solicitud", Integer.parseInt(tab_solicitud.getValor("id_solicitud_acceso") + ""));
                p_parametros.put("fecha", utilitario.getFechaLarga(tab_solicitud.getValor("fechaing_solicitante") + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Conexion getCon_postgres() {
        return conPostgres;
    }

    public void setCon_postgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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

    public SeleccionTabla getSetTabla() {
        return setTabla;
    }

    public void setSetTabla(SeleccionTabla setTabla) {
        this.setTabla = setTabla;
    }
}
