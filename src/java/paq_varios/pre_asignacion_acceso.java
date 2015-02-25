/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.decimoCuarto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_asignacion_acceso extends Pantalla {

    //Conexion a base
    private Conexion con_postgres = new Conexion();
    //Tablas
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    //COMBO PARA BUSQUEDA
    private Combo cmb_documento = new Combo();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private decimoCuarto datosEmpledo = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);

    public pre_asignacion_acceso() {

        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(pan_opcion);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);

        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT s.id_solicitud_acceso,  \n"
                + "s.nombre_solicitante, \n"
                + "s.direccion_solicitante, \n"
                + "s.nombre_usuario, \n"
                + "a.nombre_sistema \n"
                + "FROM sca_solicitud_acceso s \n"
                + "INNER JOIN sca_sistemas a ON s.id_sistema = a.id_sistema");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);

        bar_botones.agregarComponente(new Etiqueta("Solicitud de Acceso:"));
        bar_botones.agregarComponente(aut_busca);

        Grid gri_acceso = new Grid();
        gri_acceso.setColumns(4);
        cmb_documento.setId("cmb_documento");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "Solicitud", "Solicitud"
        };
        Object fila2[] = {
            "Asignada", "Asignada"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmb_documento.setCombo(lista1);
        gri_acceso.getChildren().add(new Etiqueta("PARAMETRO DE BUSQUEDA"));
        gri_acceso.getChildren().add(cmb_documento);
        Boton bot_acceso = new Boton();
        bot_acceso.setValue("Buscar");
        bot_acceso.setIcon("ui-icon-search");
        bot_acceso.setMetodo("aceptarRegistro");
        bar_botones.agregarBoton(bot_acceso);
        gri_acceso.getChildren().add(bot_acceso);

        set_solicitud.setId("set_solicitud");
        set_solicitud.getTab_seleccion().setConexion(con_postgres);
        set_solicitud.setSeleccionTabla("SELECT id_solicitud_acceso,fechaing_solicitante as fecha_solicitud,nombre_solicitante,direccion_solicitante,nombre_usuario\n"
                + "FROM sca_solicitud_acceso\n"
                + "where id_solicitud_acceso=-1\n"
                + "ORDER BY id_solicitud_acceso desc", "id_solicitud_acceso");
        set_solicitud.getTab_seleccion().getColumna("nombre_usuario").setLongitud(50);
        set_solicitud.getTab_seleccion().getColumna("nombre_solicitante").setLongitud(50);
        set_solicitud.getTab_seleccion().getColumna("fecha_solicitud").setLongitud(30);
        set_solicitud.getTab_seleccion().getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion");
        set_solicitud.getTab_seleccion().getColumna("direccion_solicitante").setLongitud(50);
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.setWidth("80%");
        set_solicitud.getGri_cuerpo().setHeader(gri_acceso);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD DE ACCESO A SISTEMAS");
        agregarComponente(set_solicitud);

        dibujarSolicitud();

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
    }

    //Permite Buscar solicitud que se encuentra Ingresada o Pendiente
    public void Busca_tipo() {
        set_solicitud.dibujar();
    }

    public void aceptarRegistro() {
        if (cmb_documento.getValue() != null && cmb_documento.getValue().toString().isEmpty() == false) {
            set_solicitud.getTab_seleccion().setSql("SELECT id_solicitud_acceso,fechaing_solicitante,nombre_solicitante,direccion_solicitante,nombre_usuario,id_sistema,id_modulo\n"
                    + "FROM sca_solicitud_acceso\n"
                    + "where estado_solicitud = '" + cmb_documento.getValue() + "' ORDER BY id_solicitud_acceso desc");
            set_solicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un opción", "");
        }
    }

    //Dibuja la Pantalla
    public void aceptarBusqueda() {
        if (set_solicitud.getValorSeleccionado() != null) {
            aut_busca.setValor(set_solicitud.getValorSeleccionado());
            set_solicitud.cerrar();
            dibujarSolicitud();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
        }
    }

    public void dibujarSolicitud() {
        limpiarPanel();
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setConexion(con_postgres);
        tab_solicitud.setTabla("sca_solicitud_acceso", "id_solicitud_acceso", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_solicitud.setCondicion("id_solicitud_acceso=-1");
        } else {
            tab_solicitud.setCondicion("id_solicitud_acceso=" + aut_busca.getValor());
        }
        tab_solicitud.getColumna("id_sistema").setCombo("SELECT id_sistema,nombre_sistema FROM sca_sistemas order by nombre_sistema");
        tab_solicitud.getColumna("id_modulo").setCombo("SELECT id_modulo,nombre_modulo FROM sca_modulos order by nombre_modulo");
        tab_solicitud.getColumna("id_perfil").setCombo("SELECT id_perfil,nombre_perfil FROM sca_perfiles order by nombre_perfil");
        tab_solicitud.getColumna("codigo_usuario").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_solicitud.getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion order by nombre_dir");
        tab_solicitud.getColumna("cargo_solicitante").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("cargo_usuario").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("codigo_solicitante").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_solicitud.getColumna("codigo_asigna_acceso").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_solicitud.getColumna("logining_solicitante").setVisible(false);
        tab_solicitud.getColumna("fechact_solicitante").setVisible(false);
        tab_solicitud.getColumna("loginact_solcitante").setVisible(false);
        tab_solicitud.getColumna("fechact_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("loginact_acceso_usuario").setVisible(false);
//        tab_solicitud.getColumna("fecha_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("logining_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("direccion_usuario").setVisible(false);
        tab_solicitud.getColumna("direccion_usuario").setVisible(false);
        tab_solicitud.getColumna("cedula_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("nombre_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("estado_solicitud").setVisible(false);
        tab_solicitud.getColumna("cedula_solicitante").setVisible(false);
        tab_solicitud.getColumna("nombre_solicitante").setVisible(false);
//        tab_solicitud.getColumna("cedula_usuario").setVisible(false);
        tab_solicitud.getColumna("nombre_usuario").setVisible(false);
//        tab_solicitud.getColumna("memorando_acceso_usuario").setVisible(false);

        tab_solicitud.getColumna("codigo_asigna_acceso").setMetodoChange("datosUsuario");

        tab_solicitud.getColumna("bandera_solicitante").setEtiqueta();
        tab_solicitud.getColumna("bandera_usuario").setEtiqueta();
        tab_solicitud.getColumna("bandera_perfil").setEtiqueta();

        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.getGrid().setColumns(4);
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

    public void estadoAcceso() {
        tab_solicitud.setValor("estado_solicitud", "Asignada");
        utilitario.addUpdate("tab_solicitud");
    }

    public void datosUsuario() {
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleado(tab_solicitud.getValor("codigo_asigna_acceso"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_asigna_acceso", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_asigna_acceso", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("logining_acceso_usuario", utilitario.getVariable("NICK"));
//            tab_solicitud.setValor("fecha_acceso_usuario", utilitario.getFechaActual());
            utilitario.addUpdate("tab_solicitud");
        } else {
            utilitario.agregarMensaje("Usuario Sin Datos", "");
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        estadoAcceso();
        if (tab_solicitud.guardar()) {
            con_postgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    }

    //llamada a reporte
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
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
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
}
