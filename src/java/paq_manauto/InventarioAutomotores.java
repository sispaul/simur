/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_manauto.ejb.SQLManauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class InventarioAutomotores extends Pantalla {

    //conexion a base de datos
    private Conexion conPostgres = new Conexion();
    //para dibujar pantalla
    private Tabla setMarca = new Tabla();
    private Tabla setTipo = new Tabla();
    private Tabla setModelo = new Tabla();
    private Tabla setVersion = new Tabla();
    private Tabla tabAutomotores = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabAccesorios = new Tabla();
    //Dialogo de Ingreso de tablas
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Dialogo diaDialogom = new Dialogo();
    private Dialogo diaDialogov = new Dialogo();
    private Grid gridO = new Grid();
    private Grid gridT = new Grid();
    private Grid gridM = new Grid();
    private Grid gridV = new Grid();
    private Grid grid = new Grid();
    private Grid gridti = new Grid();
    private Grid gridma = new Grid();
    private Grid gridve = new Grid();
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    //buscar solicitud
    private AutoCompletar autBusca = new AutoCompletar();
    private Integer pk;
    @EJB
    private manauto aCombustible = (SQLManauto) utilitario.instanciarEJB(SQLManauto.class);
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();

    public InventarioAutomotores() {

        //permite utilizar los datos del usuario conectado 
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //ingreso de paramestros de vehiculo
        Boton botMarca = new Boton();
        botMarca.setValue("INSERTAR MARCA");
        botMarca.setIcon("ui-icon-gear");
        botMarca.setMetodo("ingMarcas");
        bar_botones.agregarBoton(botMarca);

        //para marca,tipo,modelo,version
        Grid griMarcas = new Grid();
        griMarcas.setColumns(6);
        griMarcas.getChildren().add(new Etiqueta("Ingrese Marca: "));
        griMarcas.getChildren().add(tmarca);
        Boton botMarcas = new Boton();
        botMarcas.setValue("Guardar");
        botMarcas.setIcon("ui-icon-disk");
        botMarcas.setMetodo("insMarca");
        bar_botones.agregarBoton(botMarcas);
        Boton botMarcaxs = new Boton();
        botMarcaxs.setValue("Eliminar");
        botMarcaxs.setIcon("ui-icon-closethick");
        botMarcaxs.setMetodo("endMarca");
        bar_botones.agregarBoton(botMarcaxs);
        griMarcas.getChildren().add(botMarcas);
        griMarcas.getChildren().add(botMarcaxs);
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("INGRESO DE MARCA"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getGri_cuerpo().setHeader(griMarcas);
        diaDialogo.getBot_aceptar().setMetodo("acepMarcas");
        gridO.setColumns(4);
        agregarComponente(diaDialogo);

        setMarca.setId("setMarca");
        setMarca.setConexion(conPostgres);
        setMarca.setSql("SELECT mvmarca_id, mvmarca_descripcion FROM mvmarca_vehiculo order by mvmarca_descripcion");
        setMarca.getColumna("mvmarca_descripcion").setFiltro(true);
        setMarca.setTipoSeleccion(false);
        setMarca.setRows(10);
        setMarca.dibujar();

        Grid griTipos = new Grid();
        griTipos.setColumns(6);
        griTipos.getChildren().add(new Etiqueta("Ingrese Tipo"));
        griTipos.getChildren().add(ttipo);
        Boton botTipos = new Boton();
        botTipos.setValue("Guardar");
        botTipos.setIcon("ui-icon-disk");
        botTipos.setMetodo("insTipo");
        bar_botones.agregarBoton(botTipos);
        Boton botTipoxs = new Boton();
        botTipoxs.setValue("Eliminar");
        botTipoxs.setIcon("ui-icon-closethick");
        botTipoxs.setMetodo("endTipo");
        bar_botones.agregarBoton(botTipoxs);
        griTipos.getChildren().add(botTipos);
        griTipos.getChildren().add(botTipoxs);
        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("IINGRESO DE TIPO"); //titulo
        diaDialogot.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("40%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getGri_cuerpo().setHeader(griTipos);
        diaDialogot.getBot_aceptar().setMetodo("aceptaTipo");
        gridT.setColumns(4);
        agregarComponente(diaDialogot);

        Grid griModelos = new Grid();
        griModelos.setColumns(6);
        griModelos.getChildren().add(new Etiqueta("Ingreso Modelo"));
        griModelos.getChildren().add(tmodelo);
        Boton botModelos = new Boton();
        botModelos.setValue("Guardar");
        botModelos.setIcon("ui-icon-disk");
        botModelos.setMetodo("insModelo");
        bar_botones.agregarBoton(botModelos);
        Boton botModeloxs = new Boton();
        botModeloxs.setValue("Eliminar");
        botModeloxs.setIcon("ui-icon-closethick");
        botModeloxs.setMetodo("endModelo");
        bar_botones.agregarBoton(botModeloxs);
        griModelos.getChildren().add(botModelos);
        griModelos.getChildren().add(botModeloxs);
        diaDialogom.setId("diaDialogom");
        diaDialogom.setTitle("INGRESO DE MODELO"); //titulo
        diaDialogom.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogom.setHeight("40%");//siempre porcentaje   alto
        diaDialogom.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogom.getGri_cuerpo().setHeader(griModelos);
        diaDialogom.getBot_aceptar().setMetodo("aceptaModelo");
        gridM.setColumns(4);
        agregarComponente(diaDialogom);

        Grid griVersions = new Grid();
        griVersions.setColumns(6);
        griVersions.getChildren().add(new Etiqueta("Ingreso Versión"));
        griVersions.getChildren().add(tversion);
        Boton botVersions = new Boton();
        botVersions.setValue("Guardar");
        botVersions.setIcon("ui-icon-disk");
        botVersions.setMetodo("insVersion");
        bar_botones.agregarBoton(botVersions);
        Boton botVersionxs = new Boton();
        botVersionxs.setValue("Eliminar");
        botVersionxs.setIcon("ui-icon-closethick");
        botVersionxs.setMetodo("endVersion");
        bar_botones.agregarBoton(botVersionxs);
        griVersions.getChildren().add(botVersions);
        griVersions.getChildren().add(botVersionxs);
        diaDialogov.setId("diaDialogov");
        diaDialogov.setTitle("INGRESO DE VERSIONES"); //titulo
        diaDialogov.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogov.setHeight("40%");//siempre porcentaje   alto
        diaDialogov.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogov.getGri_cuerpo().setHeader(griVersions);
        gridV.setColumns(4);
        agregarComponente(diaDialogov);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE INVENTARIO VEHICULOS/MAQUINARIA PARA ABASTECIMIENTOS");
        agregarComponente(panOpcion);

        dibujaIngreso();

        //Auto busqueda para, verificar solicitud
        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("SELECT v.MVE_SECUENCIAL,mve_codigo,v.MVE_PLACA,m.MVMARCA_DESCRIPCION,o.MVMODELO_DESCRIPCION,v.MVE_CHASIS\n"
                + "FROM MV_VEHICULO AS v ,mvmarca_vehiculo AS m ,mvmodelo_vehiculo o\n"
                + "WHERE v.mvmarca_id = m.mvmarca_id and v.mvmodelo_id = o.mvmodelo_id");
        autBusca.setMetodoChange("filtrarSolicitud");
        autBusca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(autBusca);

    }

    //PANTALLA DE REGISTRO DE INFORMACIÓN
    public void dibujaIngreso() {
        limpiarPanel();
        tabAutomotores.setId("tabAutomotores");
        tabAutomotores.setConexion(conPostgres);
        tabAutomotores.setTabla("mv_vehiculo", "mve_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (autBusca.getValue() == null) {
            tabAutomotores.setCondicion("mve_secuencial=-1");
        } else {
            tabAutomotores.setCondicion("mve_secuencial=" + autBusca.getValor());
        }
        tabAutomotores.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,mvmarca_descripcion FROM mvmarca_vehiculo order by mvmarca_descripcion");
        tabAutomotores.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo ORDER BY MVTIPO_DESCRIPCION");
        tabAutomotores.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo order by MVMODELO_DESCRIPCION");
        tabAutomotores.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo order by mvversion_descripcion");
        tabAutomotores.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||''||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tabAutomotores.getColumna("mvmarca_id").setMetodoChange("cargarTipo");
        tabAutomotores.getColumna("mvtipo_id").setMetodoChange("cargarModelo");
        tabAutomotores.getColumna("mvmodelo_id").setMetodoChange("cargarVersion");
        tabAutomotores.getColumna("mve_tipomedicion").setMetodoChange("activarCasilla");
        tabAutomotores.getColumna("mve_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tabAutomotores.getColumna("mve_cod_conductor").setFiltroContenido();
        tabAutomotores.getColumna("mve_cod_conductor").setMetodoChange("conductor");
        tabAutomotores.getColumna("mve_kilometros_actual").setMetodoChange("recorrido");
        tabAutomotores.getColumna("MVE_HOROMETRO").setMetodoChange("recorrido");
        tabAutomotores.getColumna("MVE_TIPOCODIGO").setMetodoChange("activarTipo");
        tabAutomotores.getColumna("MVE_HOROMETRO").setMascara("9999:99");
        tabAutomotores.getColumna("mve_rendimientogl_h").setMascara("9/1");
        tabAutomotores.getColumna("MVE_LOGININGRESO").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabAutomotores.getColumna("MVE_FECHAINGRESO").setValorDefecto(utilitario.getFechaActual());
        List list = new ArrayList();
        Object fil1[] = {
            "1", "KILOMETROS"
        };
        Object fil2[] = {
            "2", "HORAS"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabAutomotores.getColumna("MVE_TIPOMEDICION").setRadio(list, " ");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Automotor"
        };
        Object fila2[] = {
            "2", "Maquinaria"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabAutomotores.getColumna("MVE_TIPOCODIGO").setCombo(lista);
        tabAutomotores.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
        List listes = new ArrayList();
        Object filase1[] = {
            "BUENO", "BUENO"
        };
        Object filase2[] = {
            "REGULAR", "REGULAR"
        };
        Object filase3[] = {
            "DAÑADO", "DAÑADO"
        };
        listes.add(filase1);;
        listes.add(filase2);;
        listes.add(filase3);;
        tabAutomotores.getColumna("MVE_ESTADO").setCombo(listes);
        tabAutomotores.getColumna("MVE_PLACA").setLectura(true);
        tabAutomotores.getColumna("mve_codigo").setLectura(true);
        tabAutomotores.getColumna("MVE_HOROMETRO").setLectura(true);
        tabAutomotores.getColumna("mve_kilometros_actual").setLectura(true);
        tabAutomotores.getColumna("mve_asignacion").setVisible(false);
        tabAutomotores.getColumna("mve_observcaciones").setVisible(false);
        tabAutomotores.getColumna("mve_tipo_ingreso").setVisible(false);
        tabAutomotores.getColumna("mve_loginingreso").setVisible(false);
        tabAutomotores.getColumna("mve_fechaingreso").setVisible(false);
        tabAutomotores.getColumna("mve_conductor").setVisible(false);
        tabAutomotores.getColumna("mve_numimr").setVisible(false);
        tabAutomotores.getColumna("mve_secuencial").setVisible(false);
        tabAutomotores.getColumna("mve_rendimientogl_h").setLectura(true);
        tabAutomotores.setTipoFormulario(true);
        tabAutomotores.getGrid().setColumns(4);
        tabAutomotores.dibujar();
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tabAutomotores);

        Division div = new Division();
        div.dividir1(tpg);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autBusca.onSelect(evt);
        dibujaIngreso();
    }

    public void activarTipo() {
        if (tabAutomotores.getValor("MVE_TIPOCODIGO").equals("1")) {
            tabAutomotores.getColumna("MVE_PLACA").setLectura(false);
            tabAutomotores.setValor("MVE_PLACA", null);
            tabAutomotores.getColumna("mve_codigo").setLectura(false);
            tabAutomotores.setValor("mve_codigo", null);
            utilitario.addUpdate("tabAutomotores");
        } else {
            tabAutomotores.getColumna("MVE_PLACA").setLectura(false);
            tabAutomotores.setValor("MVE_PLACA", null);
            tabAutomotores.getColumna("mve_codigo").setLectura(false);
            tabAutomotores.setValor("mve_codigo", null);
            utilitario.addUpdate("tabAutomotores");
        }
    }

    public void recorrido() {
        if (tabAutomotores.getValor("mve_kilometros_actual") != null) {
            utilitario.addUpdate("tabAutomotores");
        } else {
            String minutos = tabAutomotores.getValor("MVE_HOROMETRO").substring(5, 7);
            if (Integer.parseInt(minutos) < 60) {
                utilitario.addUpdate("tabAutomotores");
            } else {
                utilitario.agregarMensaje("Minutos No Deben Ser Mayor", "60");
            }
        }
    }

    //ACTUALIZAR COMBOS
    public void cargarMarca() {
        tabAutomotores.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM mvmarca_vehiculo order by MVMARCA_DESCRIPCION");
        utilitario.addUpdateTabla(tabAutomotores, "mvmarca_id", "");//actualiza solo componentes
    }

    public void cargarTipo() {
        tabAutomotores.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo where mvmarca_id ='" + tabAutomotores.getValor("mvmarca_id") + "'");
        utilitario.addUpdateTabla(tabAutomotores, "mvtipo_id", "");//actualiza solo componentes
    }

    public void cargarModelo() {
        tabAutomotores.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo where mvtipo_id ='" + tabAutomotores.getValor("mvtipo_id") + "'");
        utilitario.addUpdateTabla(tabAutomotores, "mvmodelo_id", "");//actualiza solo componentes
    }

    public void cargarVersion() {
        tabAutomotores.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo where mvmodelo_id='" + tabAutomotores.getValor("mvmodelo_id") + "'");
        utilitario.addUpdateTabla(tabAutomotores, "mvversion_id", "");//actualiza solo componentes
    }

    //DATOS PARA CONDUCTOR
    public void conductor() {
        TablaGenerica tabDato = aCombustible.getChofer(tabAutomotores.getValor("mve_cod_conductor"));
        if (!tabDato.isEmpty()) {
            tabAutomotores.setValor("mve_conductor", tabDato.getValor("nombres"));
            tabAutomotores.setValor("mve_asignacion", tabDato.getValor("activo"));
            utilitario.addUpdate("tabAutomotores");
        } else {
            utilitario.agregarMensajeInfo("No existen Datos", "");
        }
    }

    //COMPONENTES ADICIONALES ACTIVAR
    public void activarCasilla() {
        if (tabAutomotores.getValor("mve_tipomedicion").equals("2")) {
            tabAutomotores.getColumna("mve_kilometros_actual").setLectura(true);
            tabAutomotores.getColumna("mve_horometro").setLectura(false);
            tabAutomotores.setValor("mve_tipo_ingreso", "M");
            tabAutomotores.setValor("MVE_NUMIMR", "H");
            tabAutomotores.getColumna("mve_rendimientogl_h").setLectura(false);
            utilitario.addUpdate("tabAutomotores");
        } else {
            tabAutomotores.getColumna("mve_kilometros_actual").setLectura(false);
            tabAutomotores.getColumna("mve_horometro").setLectura(true);
            tabAutomotores.setValor("mve_tipo_ingreso", "A");
            tabAutomotores.setValor("MVE_NUMIMR", "K");
            tabAutomotores.getColumna("mve_rendimientogl_h").setLectura(true);
            utilitario.addUpdate("tabAutomotores");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        Map campos = new HashMap();

        try {
            campos.put("marca", marca.getText());
            campos.put("calibre", calibre.getText());
            campos.put("serie", serie.getText());
            campos.put("fecha_registro", fechaRegistro.getCalendar().getTime());
            campos.put("tipo", tipo.getText());

            if (pk == 0) {
                aCombustible.insertar("armas", campos);

            } else {
                aCombustible.actualizar("armas", "armas_id", pk, campos);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void eliminar() {
    }

}
