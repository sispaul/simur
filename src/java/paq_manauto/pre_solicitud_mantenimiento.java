/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_manauto.ejb.manauto;
import paq_nomina.ejb.mergeDescuento;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_mantenimiento extends Pantalla {

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    //declaracion de tablas
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    //Dialogo Busca 
    private Dialogo dia_dialogm = new Dialogo();
    private Grid grid_m = new Grid();
    private Grid grim = new Grid();
    //Texto de Ingreso
    Texto txt_motivo = new Texto();
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //seleccion tipo de mantenimiento
    private Combo cmb_impresion = new Combo();
    //Dialogos
    private Dialogo dia_dialogod = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gridd = new Grid();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public pre_solicitud_mantenimiento() {

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setExcluirLectura(true);
        bot_anular.setIcon("ui-icon-cancel");
        bot_anular.setMetodo("quitar");
        bar_botones.agregarBoton(bot_anular);

        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT m.mca_codigo,v.mve_placa,a.mvmarca_descripcion,t.mvtipo_descripcion,o.mvmodelo_descripcion\n"
                + "FROM mvcab_mantenimiento m\n"
                + "INNER JOIN mv_vehiculo v ON m.mve_secuencial = v.mve_secuencial\n"
                + "INNER JOIN mvmarca_vehiculo a ON v.mvmarca_id = a.mvmarca_id\n"
                + "INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id\n"
                + "INNER JOIN mvtipo_vehiculo t ON t.mvmarca_id = a.mvmarca_id \n"
                + "AND v.mvtipo_id = t.mvtipo_id \n"
                + "AND o.mvtipo_id = t.mvtipo_id\n"
                + "WHERE m.mca_estado_registro = 'Solicitud'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);

        dia_dialogm.setId("dia_dialogm");
        dia_dialogm.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        dia_dialogm.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogm.setHeight("20%");//siempre porcentaje   alto
        dia_dialogm.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogm.getBot_aceptar().setMetodo("anular");
        grid_m.setColumns(4);
        agregarComponente(dia_dialogm);

        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICTUD DE MANTENIMIENTO VEHICULOS/MAQUINARIA");
        agregarComponente(pan_opcion);

        dibujarSolicitud();

        dia_dialogod.setId("dia_dialogod");
        dia_dialogod.setTitle("SELECCIONE OPCIÓN"); //titulo
        dia_dialogod.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogod.setHeight("10%");//siempre porcentaje   alto
        dia_dialogod.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogod.getBot_aceptar().setMetodo("abrirReporte");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogod);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
    }

    public void dibujarSolicitud() {
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvcab_mantenimiento", "mca_codigo", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mca_codigo=-1");
        } else {
            tab_tabla.setCondicion("mca_codigo=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial, \n"
                + "((case when v.mve_placa is NULL then v.mve_codigo when v.mve_placa is not null then v.mve_placa end )||' / '||m.mvmarca_descripcion ||' / '||t.mvtipo_descripcion||' / '||o.mvmodelo_descripcion ||' / '||v.mve_ano) AS descripcion \n"
                + "FROM mv_vehiculo AS v \n"
                + "INNER JOIN mvmarca_vehiculo AS m ON v.mvmarca_id = m.mvmarca_id \n"
                + "INNER JOIN mvmodelo_vehiculo AS o ON v.mvmodelo_id = o.mvmodelo_id \n"
                + "INNER JOIN mvtipo_vehiculo t ON t.mvmarca_id = m.mvmarca_id \n"
                + "ORDER BY v.mve_secuencial ASC");
        tab_tabla.getColumna("mca_cod_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores order by titular");
//        tab_tabla.getColumna("mca_cod_autoriza").setCombo("select cod_empleado,nombres from srh_empleado where estado = 1 or cod_empleado = 100 order by nombres");
        tab_tabla.getColumna("mca_cod_responsable").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");

        tab_tabla.getColumna("mve_secuencial").setFiltroContenido();
        tab_tabla.getColumna("mca_cod_proveedor").setFiltroContenido();
        tab_tabla.getColumna("mca_cod_responsable").setFiltroContenido();

        tab_tabla.getColumna("mca_cod_proveedor").setLectura(true);
        tab_tabla.getColumna("mca_monto").setLectura(true);

        tab_tabla.getColumna("mca_fechainman").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("mca_horainman").setValorDefecto(String.valueOf(utilitario.getHoraActual()));
        tab_tabla.getColumna("mca_loginingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("mca_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_tabla.getColumna("mca_periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_tabla.getColumna("mca_tiposol").setValorDefecto("1");

        tab_tabla.getColumna("mca_tipo_mantenimiento").setMetodoChange("tipo_mantenimiento");
        tab_tabla.getColumna("mve_secuencial").setMetodoChange("caracteristicas");
        tab_tabla.getColumna("mca_cod_proveedor").setMetodoChange("proveedor");
        tab_tabla.getColumna("mca_cod_autoriza").setMetodoChange("empleado");
        tab_tabla.getColumna("mca_cod_responsable").setMetodoChange("responsable");

        List lista = new ArrayList();
        Object fila1[] = {
            "INTERNO", "INTERNO"
        };
        Object fila2[] = {
            "EXTERNO", "EXTERNO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_tabla.getColumna("mca_tipo_mantenimiento").setRadio(lista, " ");

        tab_tabla.getColumna("mca_estado_tramite").setEtiqueta();
        tab_tabla.getColumna("mca_formapago").setVisible(false);
        tab_tabla.getColumna("mca_tipomedicion").setVisible(false);
        tab_tabla.getColumna("mca_cod_autoriza").setVisible(false);
        tab_tabla.getColumna("mca_tiposol").setVisible(false);
        tab_tabla.getColumna("mca_horasaman").setVisible(false);
        tab_tabla.getColumna("mca_horainman").setVisible(false);
        tab_tabla.getColumna("mca_fechasaman").setVisible(false);
        tab_tabla.getColumna("mca_kmanterior_hora").setVisible(false);
        tab_tabla.getColumna("mca_loginingreso").setVisible(false);
        tab_tabla.getColumna("mca_loginactuali").setVisible(false);
        tab_tabla.getColumna("mca_fechactuali").setVisible(false);
        tab_tabla.getColumna("mca_loginborrado").setVisible(false);
        tab_tabla.getColumna("mca_fechaborrado").setVisible(false);
        tab_tabla.getColumna("mca_anio").setVisible(false);
        tab_tabla.getColumna("mca_periodo").setVisible(false);
        tab_tabla.getColumna("mca_codigo").setVisible(false);
        tab_tabla.getColumna("mca_autorizado").setVisible(false);
        tab_tabla.getColumna("mca_proveedor").setVisible(false);
        tab_tabla.getColumna("mca_responsable").setVisible(false);
        tab_tabla.getColumna("mca_motivo_anulacion").setVisible(false);
        tab_tabla.getColumna("mca_secuencial_sol").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(2);
        tab_tabla.dibujar();

        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);

        Division div = new Division();
        div.dividir1(ptt);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }

    //limpia y borrar el contenido de la pantalla
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

    //BUSQUEDA DE REGISTRO
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujarSolicitud();
    }

    public void proveedor() {
        TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tab_tabla.getValor("mca_cod_proveedor")));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("mca_proveedor", tab_dato.getValor("titular"));
            utilitario.addUpdate("tab_tabla");//actualiza solo componentes
        }
    }

    //busca datos de vehiculo que se selecciona
    public void caracteristicas() {
        TablaGenerica tab_dato = aCombustible.getDatosSoli(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_dato.isEmpty()) {
            tab_tabla.getColumna("mca_tipo_mantenimiento").setLectura(true);
            utilitario.addUpdate("tab_tabla");
            utilitario.agregarMensaje("Aun Mantiene una Solictud Pendiente", "");
        } else {
            TablaGenerica tab_datoa = aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            if (!tab_datoa.isEmpty()) {
                tab_tabla.getColumna("mca_tipo_mantenimiento").setLectura(false);
                tab_tabla.setValor("mca_responsable", tab_datoa.getValor("mve_conductor"));
                tab_tabla.setValor("mca_cod_responsable", tab_datoa.getValor("mve_cod_conductor"));
                tab_tabla.setValor("mca_kmactual_hora", tab_datoa.getValor("rendimiento"));
                tab_tabla.setValor("mca_secuencial_sol", String.valueOf(Integer.parseInt(aCombustible.Maxsoli_vehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")))) + 1));
                utilitario.addUpdate("tab_tabla");
            } else {
                utilitario.agregarMensajeError("No Se Encuentra Responsable", "");
            }
        }
    }

    public void empleado() {
        TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tab_tabla.getValor("mca_cod_autoriza")));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("mca_autorizado", tab_dato.getValor("nombres"));
            utilitario.addUpdate("tab_tabla");//actualiza solo componentes
        }
    }

    public void responsable() {
        TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tab_tabla.getValor("mca_cod_responsable")));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("mca_responsable", tab_dato.getValor("nombres"));
            utilitario.addUpdate("tab_tabla");//actualiza solo componentes
        }
    }

    public void secu_soli() {
        tab_tabla.setValor("mca_secuencial", String.valueOf(Integer.parseInt(aCombustible.Maxsolicitud()) + 1));
        tab_tabla.setValor("mca_estado_registro", "Solicitud");
        utilitario.addUpdate("tab_tabla");
    }

    public void quitar() {
        dia_dialogm.Limpiar();
        dia_dialogm.setDialogo(grim);
        txt_motivo.setSize(50);
        grim.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE SOLICITUD :"));
        grim.getChildren().add(new Etiqueta("_____________________________________________________"));
        grim.getChildren().add(txt_motivo);
        dia_dialogm.setDialogo(grid_m);
        dia_dialogm.dibujar();
    }

    public void anular() {
        TablaGenerica tab_dato = aCombustible.getDetalleSol(Integer.parseInt(tab_tabla.getValor("mca_codigo")));
        if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Solicitud No Puede Anularse", "Se Encuentra Tramitandose");
        } else {
            aCombustible.set_anulasolic(Integer.parseInt(tab_tabla.getValor("mca_codigo")), tab_consulta.getValor("NICK_USUA"), txt_motivo.getValue() + "");
            utilitario.addUpdate("tab_tabla");
            utilitario.agregarMensaje("SOLICITUD ANULADA", "");
            dia_dialogm.cerrar();
        }
    }

    public void tipo_mantenimiento() {
        if (tab_tabla.getValor("mca_tipo_mantenimiento").equals("INTERNO")) {
            tab_tabla.getColumna("mca_monto").setLectura(false);
            tab_tabla.getColumna("mca_cod_proveedor").setLectura(true);
            utilitario.addUpdate("tab_tabla");
        } else {
            tab_tabla.getColumna("mca_monto").setLectura(true);
            tab_tabla.getColumna("mca_cod_proveedor").setLectura(false);
            utilitario.addUpdate("tab_tabla");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        secu_soli();
    }

    @Override
    public void guardar() {
        if (tab_tabla.getValor("mca_codigo") != null) {
            TablaGenerica tab_dato = aCombustible.getDatosSolicitud(Integer.parseInt(tab_tabla.getValor("mca_codigo")), tab_tabla.getValor("mca_secuencial"));
            if (!tab_dato.isEmpty()) {
                if (Integer.parseInt(tab_tabla.getValor("mca_cod_responsable")) != Integer.parseInt(tab_dato.getValor("mca_cod_responsable"))) {
                    aCombustible.set_actusolic(Integer.parseInt(tab_tabla.getValor("mca_codigo")), tab_tabla.getValor("mca_secuencial"), "mca_cod_responsable",
                            "mca_responsable", Integer.parseInt(tab_tabla.getValor("mca_cod_responsable")), tab_tabla.getValor("mca_responsable"),
                            tab_consulta.getValor("NICK_USUA"));
                }
                if (Integer.parseInt(tab_tabla.getValor("mca_cod_proveedor")) != Integer.parseInt(tab_dato.getValor("mca_cod_proveedor"))) {
                    aCombustible.set_actusolic(Integer.parseInt(tab_tabla.getValor("mca_codigo")), tab_tabla.getValor("mca_secuencial"), "mca_cod_proveedor",
                            "mca_proveedor", Integer.parseInt(tab_tabla.getValor("mca_cod_proveedor")), tab_tabla.getValor("mca_proveedor"),
                            tab_consulta.getValor("NICK_USUA"));
                }
                if (Integer.parseInt(tab_tabla.getValor("mca_cod_autoriza")) != Integer.parseInt(tab_dato.getValor("mca_cod_autoriza"))) {
                    aCombustible.set_actusolic(Integer.parseInt(tab_tabla.getValor("mca_codigo")), tab_tabla.getValor("mca_secuencial"), "mca_cod_autoriza",
                            "mca_autorizado", Integer.parseInt(tab_tabla.getValor("mca_cod_autoriza")), tab_tabla.getValor("mca_autorizado"),
                            tab_consulta.getValor("NICK_USUA"));
                }
                if (Integer.parseInt(tab_tabla.getValor("mca_cod_responsable")) == Integer.parseInt(tab_dato.getValor("mca_cod_responsable"))
                        && Integer.parseInt(tab_tabla.getValor("mca_cod_proveedor")) == Integer.parseInt(tab_dato.getValor("mca_cod_proveedor"))
                        && Integer.parseInt(tab_tabla.getValor("mca_cod_autoriza")) == Integer.parseInt(tab_dato.getValor("mca_cod_autoriza"))) {
                    if (tab_tabla.guardar()) {
                        con_postgres.guardarPantalla();
                    }
                }
                utilitario.agregarMensaje("Registro Actualizado", "");
            }
        } else {
            if (tab_tabla.guardar()) {
                con_postgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    }

    //    *CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "SOLICITUD DE MANTENIMIENTO":
                abrirReporte();
                break;
        }
    }

    public void abrirReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "SOLICITUD DE MANTENIMIENTO":
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                p_parametros.put("numero", Integer.parseInt(tab_tabla.getValor("mca_secuencial") + ""));
                p_parametros.put("fecha_orden", utilitario.getFechaLarga(tab_tabla.getValor("mca_fechainman")) + "");
                p_parametros.put("placa", Integer.parseInt(tab_tabla.getValor("mve_secuencial") + ""));
                p_parametros.put("codigo", Integer.parseInt(tab_tabla.getValor("mca_codigo") + ""));
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

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
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
}
