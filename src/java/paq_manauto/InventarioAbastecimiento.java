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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_manauto.ejb.SQLManauto;
import paq_sistema.aplicacion.Pantalla;
import paq_manauto.ejb.manauto;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class InventarioAbastecimiento extends Pantalla {

    //conexion a base de datos
    private Conexion conPostgres = new Conexion();
    //para dibujar pantalla
    private Tabla setMarca = new Tabla();
    private Tabla setTipo = new Tabla();
    private Tabla setModelo = new Tabla();
    private Tabla setVersion = new Tabla();
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    //Dialogo de Ingreso de tablas
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Dialogo diaDialogom = new Dialogo();
    private Dialogo diaDialogov = new Dialogo();
    private Grid grid_o = new Grid();
    private Grid grid_t = new Grid();
    private Grid grid_m = new Grid();
    private Grid grid_v = new Grid();
    private Grid grid = new Grid();
    private Grid gridt = new Grid();
    private Grid gridm = new Grid();
    private Grid gridv = new Grid();
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    //buscar solicitud
    private AutoCompletar autBusca = new AutoCompletar();
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    private SQLManauto bCombustible = (SQLManauto) utilitario.instanciarEJB(SQLManauto.class);

    public InventarioAbastecimiento() {

        //permite utilizar los datos del usuario conectado 
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //para marca,tipo,modelo,version
        Grid griMarcas = new Grid();
        griMarcas.setColumns(6);
        griMarcas.getChildren().add(new Etiqueta("Ingrese Marca: "));
        griMarcas.getChildren().add(tmarca);
        Boton botMarcas = new Boton();
        botMarcas.setValue("Guardar");
        botMarcas.setIcon("ui-icon-disk");
        botMarcas.setMetodo("insMarca");
        Boton botMarcaxs = new Boton();
        botMarcaxs.setValue("Eliminar");
        botMarcaxs.setIcon("ui-icon-closethick");
        botMarcaxs.setMetodo("endMarca");
        griMarcas.getChildren().add(botMarcas);
        griMarcas.getChildren().add(botMarcaxs);
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("INGRESO DE MARCA"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getGri_cuerpo().setHeader(griMarcas);
        diaDialogo.getBot_aceptar().setMetodo("acep_marcas");
        grid_o.setColumns(4);
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
        Boton botTipoxs = new Boton();
        botTipoxs.setValue("Eliminar");
        botTipoxs.setIcon("ui-icon-closethick");
        botTipoxs.setMetodo("endTipo");
        griTipos.getChildren().add(botTipos);
        griTipos.getChildren().add(botTipoxs);
        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("IINGRESO DE TIPO"); //titulo
        diaDialogot.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("40%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getGri_cuerpo().setHeader(griTipos);
        diaDialogot.getBot_aceptar().setMetodo("acepta_tipo");
        grid_t.setColumns(4);
        agregarComponente(diaDialogot);

        Grid griModelos = new Grid();
        griModelos.setColumns(6);
        griModelos.getChildren().add(new Etiqueta("Ingreso Modelo"));
        griModelos.getChildren().add(tmodelo);
        Boton bot_modelos = new Boton();
        bot_modelos.setValue("Guardar");
        bot_modelos.setIcon("ui-icon-disk");
        bot_modelos.setMetodo("insModelo");
        Boton bot_modeloxs = new Boton();
        bot_modeloxs.setValue("Eliminar");
        bot_modeloxs.setIcon("ui-icon-closethick");
        bot_modeloxs.setMetodo("endModelo");
        griModelos.getChildren().add(bot_modelos);
        griModelos.getChildren().add(bot_modeloxs);
        diaDialogom.setId("diaDialogom");
        diaDialogom.setTitle("INGRESO DE MODELO"); //titulo
        diaDialogom.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogom.setHeight("40%");//siempre porcentaje   alto
        diaDialogom.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogom.getGri_cuerpo().setHeader(griModelos);
        diaDialogom.getBot_aceptar().setMetodo("acepta_modelo");
        grid_m.setColumns(4);
        agregarComponente(diaDialogom);

        Grid griVersions = new Grid();
        griVersions.setColumns(6);
        griVersions.getChildren().add(new Etiqueta("Ingreso Versión"));
        griVersions.getChildren().add(tversion);
        Boton botVersions = new Boton();
        botVersions.setValue("Guardar");
        botVersions.setIcon("ui-icon-disk");
        botVersions.setMetodo("insVersion");
        Boton botVersionxs = new Boton();
        botVersionxs.setValue("Eliminar");
        botVersionxs.setIcon("ui-icon-closethick");
        botVersionxs.setMetodo("endVersion");
        griVersions.getChildren().add(botVersions);
        griVersions.getChildren().add(botVersionxs);
        diaDialogov.setId("diaDialogov");
        diaDialogov.setTitle("INGRESO DE VERSIONES"); //titulo
        diaDialogov.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogov.setHeight("40%");//siempre porcentaje   alto
        diaDialogov.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogov.getGri_cuerpo().setHeader(griVersions);
        grid_v.setColumns(4);
        agregarComponente(diaDialogov);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("INGRESO DE INVENTARIO PARA ABASTECIMIENTOS");
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

        //ingreso de paramestros de vehiculo
        Boton botMarca = new Boton();
        botMarca.setValue("INSERTAR MARCA");
        botMarca.setIcon("ui-icon-gear");
        botMarca.setMetodo("ing_marcas");
        bar_botones.agregarBoton(botMarca);
    }

    //PANTALLA DE REGISTRO DE INFORMACIÓN
    public void dibujaIngreso() {
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("mv_vehiculo", "mve_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("mve_secuencial=-1");
        } else {
            tabTabla.setCondicion("mve_secuencial=" + autBusca.getValor());
        }
        tabTabla.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,mvmarca_descripcion FROM mvmarca_vehiculo order by mvmarca_descripcion");
        tabTabla.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo ORDER BY MVTIPO_DESCRIPCION");
        tabTabla.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo order by MVMODELO_DESCRIPCION");
        tabTabla.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo order by mvversion_descripcion");
        tabTabla.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||''||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tabTabla.getColumna("mvmarca_id").setMetodoChange("cargarTipo");
        tabTabla.getColumna("mvtipo_id").setMetodoChange("cargarModelo");
        tabTabla.getColumna("mvmodelo_id").setMetodoChange("cargarVersion");
        tabTabla.getColumna("mve_tipomedicion").setMetodoChange("activarCasilla");
        tabTabla.getColumna("mve_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tabTabla.getColumna("mve_cod_conductor").setFiltroContenido();
        tabTabla.getColumna("mve_cod_conductor").setMetodoChange("conductor");
        tabTabla.getColumna("mve_kilometros_actual").setMetodoChange("recorrido");
        tabTabla.getColumna("MVE_HOROMETRO").setMetodoChange("recorrido");
        tabTabla.getColumna("MVE_TIPOCODIGO").setMetodoChange("activarTipo");
        tabTabla.getColumna("MVE_HOROMETRO").setMascara("9999:99");
        tabTabla.getColumna("mve_rendimientogl_h").setMascara("9/1");
        tabTabla.getColumna("MVE_LOGININGRESO").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.getColumna("MVE_FECHAINGRESO").setValorDefecto(utilitario.getFechaActual());
        List list = new ArrayList();
        Object fil1[] = {
            "1", "KILOMETROS"
        };
        Object fil2[] = {
            "2", "HORAS"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabTabla.getColumna("MVE_TIPOMEDICION").setRadio(list, " ");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Automotor"
        };
        Object fila2[] = {
            "2", "Maquinaria"
        };
        Object fila3[] = {
            "3", "Otros"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        tabTabla.getColumna("MVE_TIPOCODIGO").setCombo(lista);
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
        tabTabla.getColumna("MVE_ESTADO").setCombo(listes);
        tabTabla.getColumna("MVE_PLACA").setLectura(true);
        tabTabla.getColumna("mve_codigo").setLectura(true);
        tabTabla.getColumna("MVE_HOROMETRO").setLectura(true);
        tabTabla.getColumna("mve_kilometros_actual").setLectura(true);
        tabTabla.getColumna("mve_asignacion").setVisible(false);
        tabTabla.getColumna("mve_observcaciones").setVisible(false);
        tabTabla.getColumna("mve_tipo_ingreso").setVisible(false);
        tabTabla.getColumna("mve_loginingreso").setVisible(false);
        tabTabla.getColumna("mve_fechaingreso").setVisible(false);
        tabTabla.getColumna("mve_conductor").setVisible(false);
        tabTabla.getColumna("mve_secuencial").setVisible(false);
        tabTabla.getColumna("mve_rendimientogl_h").setLectura(true);
        tabTabla.setTipoFormulario(true);
        tabTabla.getGrid().setColumns(4);
        tabTabla.dibujar();
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tabTabla);

        Division div = new Division();
        div.dividir1(tpg);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    //limpia y borrar el contenido de la pantalla
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

    //PARAMETROS PARA VEHICULO
    //MARCA
    public void ing_marcas() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        grid_o.getChildren().add(setMarca);
        diaDialogo.setDialogo(grid_o);
        setMarca.dibujar();
        diaDialogo.dibujar();
    }

    public void insMarca() {
        TablaGenerica tab_dato = aCombustible.get_DuplicaMarca(tmarca.getValue() + "");
        if (!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Marca ya se Encuentra Registrada", "");
        } else {
            if (tmarca.getValue() != null && tmarca.toString().isEmpty() == false) {
                aCombustible.setMarca(tmarca.getValue() + "", utilitario.getVariable("NICK"));
                tmarca.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Marca");
                setMarca.actualizar();
                cargarMarca();
            }
        }
    }

    public void endMarca() {
        if (setMarca.getValorSeleccionado() != null && setMarca.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteMarcas(Integer.parseInt(setMarca.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Marca");
            setMarca.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //TIPO

    public void acep_marcas() {
        if (setMarca.getValorSeleccionado() != null && setMarca.getValorSeleccionado().isEmpty() == false) {
            diaDialogot.Limpiar();
            diaDialogot.setDialogo(gridt);
            grid_t.getChildren().add(setTipo);
            setTipo.setId("setTipo");
            setTipo.setConexion(conPostgres);
            setTipo.setSql("SELECT mvtipo_id,mvtipo_descripcion FROM mvtipo_vehiculo where mvmarca_id =" + setMarca.getValorSeleccionado() + " order by mvtipo_descripcion");
            setTipo.getColumna("mvtipo_descripcion").setFiltro(true);
            setTipo.setTipoSeleccion(false);
            setTipo.setRows(10);
            setTipo.dibujar();
            diaDialogot.setDialogo(grid_t);
            diaDialogot.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insTipo() {
        TablaGenerica tab_dato1 = aCombustible.get_DuplicaTipo(ttipo.getValue() + "", Integer.parseInt(setMarca.getValorSeleccionado()));
        if (!tab_dato1.isEmpty()) {
            utilitario.agregarMensaje("Tipo ya se Encuentra Registrado", "");
        } else {
            if (ttipo.getValue() != null && ttipo.toString().isEmpty() == false) {
                aCombustible.setTipo(ttipo.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(setMarca.getValorSeleccionado()));
                ttipo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Tipo");
                setTipo.actualizar();
            }
        }
    }

    public void endTipo() {
        if (setTipo.getValorSeleccionado() != null && setTipo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteTipos(Integer.parseInt(setTipo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Tipo");
            setTipo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //MODELO

    public void acepta_tipo() {
        if (setTipo.getValorSeleccionado() != null && setTipo.getValorSeleccionado().isEmpty() == false) {
            diaDialogom.Limpiar();
            diaDialogom.setDialogo(gridm);
            grid_m.getChildren().add(setModelo);
            setModelo.setId("setModelo");
            setModelo.setConexion(conPostgres);
            setModelo.setSql("SELECT mvmodelo_id,mvmodelo_descripcion FROM mvmodelo_vehiculo where mvtipo_id =" + setTipo.getValorSeleccionado() + "  order by mvmodelo_descripcion");
            setModelo.getColumna("mvmodelo_descripcion").setFiltro(true);
            setModelo.setTipoSeleccion(false);
            setModelo.setRows(10);
            setModelo.dibujar();
            diaDialogom.setDialogo(grid_m);
            diaDialogom.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insModelo() {
        TablaGenerica tab_dato2 = aCombustible.get_DuplicaModelo(tmodelo.getValue() + "", Integer.parseInt(setTipo.getValorSeleccionado()));
        if (!tab_dato2.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        } else {
            if (tmodelo.getValue() != null && tmodelo.toString().isEmpty() == false) {
                aCombustible.setModelo(tmodelo.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(setTipo.getValorSeleccionado()));
                tmodelo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Modelo");
                setModelo.actualizar();
            }
        }
    }

    public void endModelo() {
        if (setModelo.getValorSeleccionado() != null && setModelo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteModelos(Integer.parseInt(setModelo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Modelo");
            setModelo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //VERSIÓN

    public void acepta_modelo() {
        if (setModelo.getValorSeleccionado() != null && setModelo.getValorSeleccionado().isEmpty() == false) {
            diaDialogov.Limpiar();
            diaDialogov.setDialogo(gridv);
            grid_v.getChildren().add(setVersion);
            setVersion.setId("setVersion");
            setVersion.setConexion(conPostgres);
            setVersion.setSql("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo where mvmodelo_id =" + setModelo.getValorSeleccionado() + " order by mvversion_descripcion");
            setVersion.getColumna("mvversion_descripcion").setFiltro(true);
            setVersion.setTipoSeleccion(false);
            setVersion.setRows(10);
            setVersion.dibujar();
            diaDialogov.setDialogo(grid_v);
            diaDialogov.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insVersion() {
        TablaGenerica tab_dato3 = aCombustible.get_DuplicaVersion(tversion.getValue() + "", Integer.parseInt(setModelo.getValorSeleccionado()));
        if (!tab_dato3.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        } else {
            if (tversion.getValue() != null && tversion.toString().isEmpty() == false) {
                aCombustible.setVersion(tversion.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(setModelo.getValorSeleccionado()));
                tversion.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Versión");
                setVersion.actualizar();
            }
        }
    }

    public void endVersion() {
        if (setVersion.getValorSeleccionado() != null && setVersion.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteversion(Integer.parseInt(setVersion.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Versión");
            setVersion.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    //BUSQUEDA DE REGISTRO
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        autBusca.onSelect(evt);
        dibujaIngreso();
    }

    public void activarTipo() {
        if (tabTabla.getValor("MVE_TIPOCODIGO").equals("1")) {
            tabTabla.getColumna("MVE_PLACA").setLectura(false);
            tabTabla.setValor("MVE_PLACA", null);
            tabTabla.getColumna("mve_codigo").setLectura(false);
            tabTabla.setValor("mve_codigo", null);
            utilitario.addUpdate("tabTabla");
        } else {
            tabTabla.getColumna("MVE_PLACA").setLectura(false);
            tabTabla.setValor("MVE_PLACA", null);
            tabTabla.getColumna("mve_codigo").setLectura(false);
            tabTabla.setValor("mve_codigo", null);
            utilitario.addUpdate("tabTabla");
        }
    }

    public void recorrido() {
        if (tabTabla.getValor("mve_kilometros_actual") != null) {
            utilitario.addUpdate("tabTabla");
        } else {
            String minutos = tabTabla.getValor("MVE_HOROMETRO").substring(5, 7);
            if (Integer.parseInt(minutos) < 60) {
                utilitario.addUpdate("tabTabla");
            } else {
                utilitario.agregarMensaje("Minutos No Deben Ser Mayor", "60");
            }
        }
    }

    //ACTUALIZAR COMBOS
    public void cargarMarca() {
        tabTabla.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM mvmarca_vehiculo order by MVMARCA_DESCRIPCION");
        utilitario.addUpdateTabla(tabTabla, "mvmarca_id", "");//actualiza solo componentes
    }

    public void cargarTipo() {
        tabTabla.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo where mvmarca_id ='" + tabTabla.getValor("mvmarca_id") + "'");
        utilitario.addUpdateTabla(tabTabla, "mvtipo_id", "");//actualiza solo componentes
    }

    public void cargarModelo() {
        tabTabla.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo where mvtipo_id ='" + tabTabla.getValor("mvtipo_id") + "'");
        utilitario.addUpdateTabla(tabTabla, "mvmodelo_id", "");//actualiza solo componentes
    }

    public void cargarVersion() {
        tabTabla.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo where mvmodelo_id='" + tabTabla.getValor("mvmodelo_id") + "'");
        utilitario.addUpdateTabla(tabTabla, "mvversion_id", "");//actualiza solo componentes
    }

    //DATOS PARA CONDUCTOR
    public void conductor() {
        TablaGenerica tab_dato = aCombustible.getChofer(tabTabla.getValor("mve_cod_conductor"));
        if (!tab_dato.isEmpty()) {
            tabTabla.setValor("mve_conductor", tab_dato.getValor("nombres"));
            tabTabla.setValor("mve_asignacion", tab_dato.getValor("activo"));
            utilitario.addUpdate("tabTabla");
        } else {
            utilitario.agregarMensajeInfo("No existen Datos", "");
        }
    }

    //COMPONENTES ADICIONALES ACTIVAR
    public void activarCasilla() {
        if (tabTabla.getValor("mve_tipomedicion").equals("2")) {
            tabTabla.getColumna("mve_kilometros_actual").setLectura(true);
            tabTabla.getColumna("mve_horometro").setLectura(false);
            tabTabla.setValor("mve_tipo_ingreso", "M");
            tabTabla.setValor("MVE_NUMIMR", "H");
            tabTabla.getColumna("mve_rendimientogl_h").setLectura(false);
            utilitario.addUpdate("tabTabla");
        } else {
            tabTabla.getColumna("mve_kilometros_actual").setLectura(false);
            tabTabla.getColumna("mve_horometro").setLectura(true);
            tabTabla.setValor("mve_tipo_ingreso", "A");
            tabTabla.setValor("MVE_NUMIMR", "K");
            tabTabla.getColumna("mve_rendimientogl_h").setLectura(true);
            utilitario.addUpdate("tabTabla");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.getValor("mve_secuencial") != null) {
            TablaGenerica tabInfo = bCombustible.getCatalogoDato("*", tabTabla.getTabla(), "mve_secuencial = " + tabTabla.getValor("mve_secuencial") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = bCombustible.getNumeroCampos(tabTabla.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = bCombustible.getEstrucTabla(tabTabla.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabTabla.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        bCombustible.setActuaRegis(Integer.parseInt(tabTabla.getValor("mve_secuencial")), tabTabla.getTabla(), tabInfoColum1.getValor("Column_Name"), tabTabla.getValor(tabInfoColum1.getValor("Column_Name")), "mve_secuencial");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else if (tabTabla.guardar()) {
            if (tabTabla.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getSetMarca() {
        return setMarca;
    }

    public void setSetMarca(Tabla setMarca) {
        this.setMarca = setMarca;
    }

    public Tabla getSetTipo() {
        return setTipo;
    }

    public void setSetTipo(Tabla setTipo) {
        this.setTipo = setTipo;
    }

    public Tabla getSetModelo() {
        return setModelo;
    }

    public void setSetModelo(Tabla setModelo) {
        this.setModelo = setModelo;
    }

    public Tabla getSetVersion() {
        return setVersion;
    }

    public void setSetVersion(Tabla setVersion) {
        this.setVersion = setVersion;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
