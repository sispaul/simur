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
import paq_sistema.aplicacion.Pantalla;
import paq_manauto.ejb.manauto;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingresoauto_maqui extends Pantalla {

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    //para dibujar pantalla
    private Tabla set_marca = new Tabla();
    private Tabla set_tipo = new Tabla();
    private Tabla set_modelo = new Tabla();
    private Tabla set_version = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_accesorios = new Tabla();
    //Dialogo de Ingreso de tablas
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogot = new Dialogo();
    private Dialogo dia_dialogom = new Dialogo();
    private Dialogo dia_dialogov = new Dialogo();
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
    private AutoCompletar aut_busca = new AutoCompletar();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);

    public pre_ingresoauto_maqui() {

        //permite utilizar los datos del usuario conectado 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        //ingreso de paramestros de vehiculo
        Boton bot_marca = new Boton();
        bot_marca.setValue("INSERTAR MARCA");
        bot_marca.setIcon("ui-icon-gear");
        bot_marca.setMetodo("ing_marcas");
        bar_botones.agregarBoton(bot_marca);

        //para marca,tipo,modelo,version
        Grid gri_marcas = new Grid();
        gri_marcas.setColumns(6);
        gri_marcas.getChildren().add(new Etiqueta("Ingrese Marca: "));
        gri_marcas.getChildren().add(tmarca);
        Boton bot_marcas = new Boton();
        bot_marcas.setValue("Guardar");
        bot_marcas.setIcon("ui-icon-disk");
        bot_marcas.setMetodo("insMarca");
        bar_botones.agregarBoton(bot_marcas);
        Boton bot_marcaxs = new Boton();
        bot_marcaxs.setValue("Eliminar");
        bot_marcaxs.setIcon("ui-icon-closethick");
        bot_marcaxs.setMetodo("endMarca");
        bar_botones.agregarBoton(bot_marcaxs);
        gri_marcas.getChildren().add(bot_marcas);
        gri_marcas.getChildren().add(bot_marcaxs);
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("INGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acep_marcas");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);

        set_marca.setId("set_marca");
        set_marca.setConexion(con_postgres);
        set_marca.setSql("SELECT mvmarca_id, mvmarca_descripcion FROM mvmarca_vehiculo order by mvmarca_descripcion");
        set_marca.getColumna("mvmarca_descripcion").setFiltro(true);
        set_marca.setTipoSeleccion(false);
        set_marca.setRows(10);
        set_marca.dibujar();

        Grid gri_tipos = new Grid();
        gri_tipos.setColumns(6);
        gri_tipos.getChildren().add(new Etiqueta("Ingrese Tipo"));
        gri_tipos.getChildren().add(ttipo);
        Boton bot_tipos = new Boton();
        bot_tipos.setValue("Guardar");
        bot_tipos.setIcon("ui-icon-disk");
        bot_tipos.setMetodo("insTipo");
        bar_botones.agregarBoton(bot_tipos);
        Boton bot_tipoxs = new Boton();
        bot_tipoxs.setValue("Eliminar");
        bot_tipoxs.setIcon("ui-icon-closethick");
        bot_tipoxs.setMetodo("endTipo");
        bar_botones.agregarBoton(bot_tipoxs);
        gri_tipos.getChildren().add(bot_tipos);
        gri_tipos.getChildren().add(bot_tipoxs);
        dia_dialogot.setId("dia_dialogot");
        dia_dialogot.setTitle("IINGRESO DE TIPO"); //titulo
        dia_dialogot.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogot.setHeight("40%");//siempre porcentaje   alto
        dia_dialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogot.getGri_cuerpo().setHeader(gri_tipos);
        dia_dialogot.getBot_aceptar().setMetodo("acepta_tipo");
        grid_t.setColumns(4);
        agregarComponente(dia_dialogot);

        Grid gri_modelos = new Grid();
        gri_modelos.setColumns(6);
        gri_modelos.getChildren().add(new Etiqueta("Ingreso Modelo"));
        gri_modelos.getChildren().add(tmodelo);
        Boton bot_modelos = new Boton();
        bot_modelos.setValue("Guardar");
        bot_modelos.setIcon("ui-icon-disk");
        bot_modelos.setMetodo("insModelo");
        bar_botones.agregarBoton(bot_modelos);
        Boton bot_modeloxs = new Boton();
        bot_modeloxs.setValue("Eliminar");
        bot_modeloxs.setIcon("ui-icon-closethick");
        bot_modeloxs.setMetodo("endModelo");
        bar_botones.agregarBoton(bot_modeloxs);
        gri_modelos.getChildren().add(bot_modelos);
        gri_modelos.getChildren().add(bot_modeloxs);
        dia_dialogom.setId("dia_dialogom");
        dia_dialogom.setTitle("INGRESO DE MODELO"); //titulo
        dia_dialogom.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogom.setHeight("40%");//siempre porcentaje   alto
        dia_dialogom.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogom.getGri_cuerpo().setHeader(gri_modelos);
        dia_dialogom.getBot_aceptar().setMetodo("acepta_modelo");
        grid_m.setColumns(4);
        agregarComponente(dia_dialogom);

        Grid gri_versions = new Grid();
        gri_versions.setColumns(6);
        gri_versions.getChildren().add(new Etiqueta("Ingreso Versión"));
        gri_versions.getChildren().add(tversion);
        Boton bot_versions = new Boton();
        bot_versions.setValue("Guardar");
        bot_versions.setIcon("ui-icon-disk");
        bot_versions.setMetodo("insVersion");
        bar_botones.agregarBoton(bot_versions);
        Boton bot_versionxs = new Boton();
        bot_versionxs.setValue("Eliminar");
        bot_versionxs.setIcon("ui-icon-closethick");
        bot_versionxs.setMetodo("endVersion");
        bar_botones.agregarBoton(bot_versionxs);
        gri_versions.getChildren().add(bot_versions);
        gri_versions.getChildren().add(bot_versionxs);
        dia_dialogov.setId("dia_dialogov");
        dia_dialogov.setTitle("INGRESO DE VERSIONES"); //titulo
        dia_dialogov.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogov.setHeight("40%");//siempre porcentaje   alto
        dia_dialogov.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogov.getGri_cuerpo().setHeader(gri_versions);
        grid_v.setColumns(4);
        agregarComponente(dia_dialogov);

        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("INGRESO DE VEHICULOS/MAQUINARIA");
        agregarComponente(pan_opcion);

        dibujaIngreso();

        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT v.MVE_SECUENCIAL,mve_codigo,v.MVE_PLACA,m.MVMARCA_DESCRIPCION,o.MVMODELO_DESCRIPCION,v.MVE_CHASIS\n"
                + "FROM MV_VEHICULO AS v ,mvmarca_vehiculo AS m ,mvmodelo_vehiculo o\n"
                + "WHERE v.mvmarca_id = m.mvmarca_id and v.mvmodelo_id = o.mvmodelo_id");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
    }

    //PANTALLA DE REGISTRO DE INFORMACIÓN
    public void dibujaIngreso() {
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mv_vehiculo", "mve_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mve_secuencial=-1");
        } else {
            tab_tabla.setCondicion("mve_secuencial=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,mvmarca_descripcion FROM mvmarca_vehiculo order by mvmarca_descripcion");
        tab_tabla.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo ORDER BY MVTIPO_DESCRIPCION");
        tab_tabla.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo order by MVMODELO_DESCRIPCION");
        tab_tabla.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo order by mvversion_descripcion");
        tab_tabla.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||''||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tab_tabla.getColumna("mvmarca_id").setMetodoChange("cargarTipo");
        tab_tabla.getColumna("mvtipo_id").setMetodoChange("cargarModelo");
        tab_tabla.getColumna("mvmodelo_id").setMetodoChange("cargarVersion");
        tab_tabla.getColumna("mve_tipomedicion").setMetodoChange("activarCasilla");
        tab_tabla.getColumna("mve_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_tabla.getColumna("mve_cod_conductor").setFiltroContenido();
        tab_tabla.getColumna("mve_cod_conductor").setMetodoChange("conductor");
        tab_tabla.getColumna("mve_kilometros_actual").setMetodoChange("recorrido");
        tab_tabla.getColumna("MVE_HOROMETRO").setMetodoChange("recorrido");
        tab_tabla.getColumna("MVE_TIPOCODIGO").setMetodoChange("activarTipo");
        tab_tabla.getColumna("MVE_HOROMETRO").setMascara("9999:99");
        tab_tabla.getColumna("mve_rendimientogl_h").setMascara("9/1");
        tab_tabla.getColumna("MVE_LOGININGRESO").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("MVE_FECHAINGRESO").setValorDefecto(utilitario.getFechaActual());
        List list = new ArrayList();
        Object fil1[] = {
            "1", "KILOMETROS"
        };
        Object fil2[] = {
            "2", "HORAS"
        };
        list.add(fil1);;
        list.add(fil2);;
        tab_tabla.getColumna("MVE_TIPOMEDICION").setRadio(list, " ");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Automotor"
        };
        Object fila2[] = {
            "2", "Maquinaria"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_tabla.getColumna("MVE_TIPOCODIGO").setCombo(lista);
        tab_tabla.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
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
        tab_tabla.getColumna("MVE_ESTADO").setCombo(listes);
        tab_tabla.getColumna("MVE_PLACA").setLectura(true);
        tab_tabla.getColumna("mve_codigo").setLectura(true);
        tab_tabla.getColumna("MVE_HOROMETRO").setLectura(true);
        tab_tabla.getColumna("mve_kilometros_actual").setLectura(true);
        tab_tabla.getColumna("mve_asignacion").setVisible(false);
        tab_tabla.getColumna("mve_observcaciones").setVisible(false);
        tab_tabla.getColumna("mve_tipo_ingreso").setVisible(false);
        tab_tabla.getColumna("mve_loginingreso").setVisible(false);
        tab_tabla.getColumna("mve_fechaingreso").setVisible(false);
        tab_tabla.getColumna("mve_conductor").setVisible(false);
        tab_tabla.getColumna("mve_numimr").setVisible(false);
        tab_tabla.getColumna("mve_secuencial").setVisible(false);
        tab_tabla.getColumna("mve_rendimientogl_h").setLectura(true);
        tab_tabla.agregarRelacion(tab_accesorios);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tab_tabla);

        tab_accesorios.setId("tab_accesorios");
        tab_accesorios.setConexion(con_postgres);
        tab_accesorios.setHeader("Accesorios");
        tab_accesorios.setTabla("mvdetalle_vehiculo", "mdv_codigo", 2);
        List listae = new ArrayList();
        Object filae1[] = {
            "EXCELENTE", "EXCELENTE"
        };
        Object filae2[] = {
            "BUENO", "BUENO"
        };
        Object filae3[] = {
            "REGULAR", "REGULAR"
        };
        Object filae4[] = {
            "DE BAJA", "DE BAJA"
        };
        listae.add(filae1);;
        listae.add(filae2);;
        listae.add(filae3);;
        listae.add(filae4);;
        tab_accesorios.getColumna("mdv_estado").setCombo(listae);
        tab_accesorios.setRows(7);
        tab_accesorios.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_accesorios);

        Division div = new Division();
        div.dividir2(tpg, tpa, "60%", "H");
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

    //PARAMETROS PARA VEHICULO
    //MARCA
    public void ing_marcas() {
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_o.getChildren().add(set_marca);
        dia_dialogo.setDialogo(grid_o);
        set_marca.dibujar();
        dia_dialogo.dibujar();
    }

    public void insMarca() {
        TablaGenerica tab_dato = aCombustible.get_DuplicaMarca(tmarca.getValue() + "");
        if (!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Marca ya se Encuentra Registrada", "");
        } else {
            if (tmarca.getValue() != null && tmarca.toString().isEmpty() == false) {
                aCombustible.set_marca(tmarca.getValue() + "", utilitario.getVariable("NICK"));
                tmarca.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Marca");
                set_marca.actualizar();
                cargarMarca();
            }
        }
    }

    public void endMarca() {
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteMarcas(Integer.parseInt(set_marca.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Marca");
            set_marca.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //TIPO

    public void acep_marcas() {
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
            dia_dialogot.Limpiar();
            dia_dialogot.setDialogo(gridt);
            grid_t.getChildren().add(set_tipo);
            set_tipo.setId("set_tipo");
            set_tipo.setConexion(con_postgres);
            set_tipo.setSql("SELECT mvtipo_id,mvtipo_descripcion FROM mvtipo_vehiculo where mvmarca_id =" + set_marca.getValorSeleccionado() + " order by mvtipo_descripcion");
            set_tipo.getColumna("mvtipo_descripcion").setFiltro(true);
            set_tipo.setTipoSeleccion(false);
            set_tipo.setRows(10);
            set_tipo.dibujar();
            dia_dialogot.setDialogo(grid_t);
            dia_dialogot.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insTipo() {
        TablaGenerica tab_dato1 = aCombustible.get_DuplicaTipo(ttipo.getValue() + "", Integer.parseInt(set_marca.getValorSeleccionado()));
        if (!tab_dato1.isEmpty()) {
            utilitario.agregarMensaje("Tipo ya se Encuentra Registrado", "");
        } else {
            if (ttipo.getValue() != null && ttipo.toString().isEmpty() == false) {
                aCombustible.setTipo(ttipo.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(set_marca.getValorSeleccionado()));
                ttipo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Tipo");
                set_tipo.actualizar();
            }
        }
    }

    public void endTipo() {
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteTipos(Integer.parseInt(set_tipo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Tipo");
            set_tipo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //MODELO

    public void acepta_tipo() {
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogom.Limpiar();
            dia_dialogom.setDialogo(gridm);
            grid_m.getChildren().add(set_modelo);
            set_modelo.setId("set_modelo");
            set_modelo.setConexion(con_postgres);
            set_modelo.setSql("SELECT mvmodelo_id,mvmodelo_descripcion FROM mvmodelo_vehiculo where mvtipo_id =" + set_tipo.getValorSeleccionado() + "  order by mvmodelo_descripcion");
            set_modelo.getColumna("mvmodelo_descripcion").setFiltro(true);
            set_modelo.setTipoSeleccion(false);
            set_modelo.setRows(10);
            set_modelo.dibujar();
            dia_dialogom.setDialogo(grid_m);
            dia_dialogom.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insModelo() {
        TablaGenerica tab_dato2 = aCombustible.get_DuplicaModelo(tmodelo.getValue() + "", Integer.parseInt(set_tipo.getValorSeleccionado()));
        if (!tab_dato2.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        } else {
            if (tmodelo.getValue() != null && tmodelo.toString().isEmpty() == false) {
                aCombustible.setModelo(tmodelo.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(set_tipo.getValorSeleccionado()));
                tmodelo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Modelo");
                set_modelo.actualizar();
            }
        }
    }

    public void endModelo() {
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteModelos(Integer.parseInt(set_modelo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Modelo");
            set_modelo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //VERSIÓN

    public void acepta_modelo() {
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogov.Limpiar();
            dia_dialogov.setDialogo(gridv);
            grid_v.getChildren().add(set_version);
            set_version.setId("set_version");
            set_version.setConexion(con_postgres);
            set_version.setSql("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo where mvmodelo_id =" + set_modelo.getValorSeleccionado() + " order by mvversion_descripcion");
            set_version.getColumna("mvversion_descripcion").setFiltro(true);
            set_version.setTipoSeleccion(false);
            set_version.setRows(10);
            set_version.dibujar();
            dia_dialogov.setDialogo(grid_v);
            dia_dialogov.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    public void insVersion() {
        TablaGenerica tab_dato3 = aCombustible.get_DuplicaVersion(tversion.getValue() + "", Integer.parseInt(set_modelo.getValorSeleccionado()));
        if (!tab_dato3.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        } else {
            if (tversion.getValue() != null && tversion.toString().isEmpty() == false) {
                aCombustible.setVersion(tversion.getValue() + "", utilitario.getVariable("NICK"), Integer.parseInt(set_modelo.getValorSeleccionado()));
                tversion.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Versión");
                set_version.actualizar();
            }
        }
    }

    public void endVersion() {
        if (set_version.getValorSeleccionado() != null && set_version.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteversion(Integer.parseInt(set_version.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Versión");
            set_version.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    //BUSQUEDA DE REGISTRO
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujaIngreso();
    }

    public void activarTipo() {
        if (tab_tabla.getValor("MVE_TIPOCODIGO").equals("1")) {
            tab_tabla.getColumna("MVE_PLACA").setLectura(false);
            tab_tabla.setValor("MVE_PLACA", null);
            tab_tabla.getColumna("mve_codigo").setLectura(false);
            tab_tabla.setValor("mve_codigo", null);
            utilitario.addUpdate("tab_tabla");
        } else {
            tab_tabla.getColumna("MVE_PLACA").setLectura(false);
            tab_tabla.setValor("MVE_PLACA", null);
            tab_tabla.getColumna("mve_codigo").setLectura(false);
            tab_tabla.setValor("mve_codigo", null);
            utilitario.addUpdate("tab_tabla");
        }
    }

    public void recorrido() {
        if (tab_tabla.getValor("mve_kilometros_actual") != null) {
            utilitario.addUpdate("tab_tabla");
        } else {
            String minutos = tab_tabla.getValor("MVE_HOROMETRO").substring(5, 7);
            if (Integer.parseInt(minutos) < 60) {
                utilitario.addUpdate("tab_tabla");
            } else {
                utilitario.agregarMensaje("Minutos No Deben Ser Mayor", "60");
            }
        }
    }

    //ACTUALIZAR COMBOS
    public void cargarMarca() {
        tab_tabla.getColumna("mvmarca_id").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM mvmarca_vehiculo order by MVMARCA_DESCRIPCION");
        utilitario.addUpdateTabla(tab_tabla, "mvmarca_id", "");//actualiza solo componentes
    }

    public void cargarTipo() {
        tab_tabla.getColumna("mvtipo_id").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM mvtipo_vehiculo where mvmarca_id ='" + tab_tabla.getValor("mvmarca_id") + "'");
        utilitario.addUpdateTabla(tab_tabla, "mvtipo_id", "");//actualiza solo componentes
    }

    public void cargarModelo() {
        tab_tabla.getColumna("mvmodelo_id").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM mvmodelo_vehiculo where mvtipo_id ='" + tab_tabla.getValor("mvtipo_id") + "'");
        utilitario.addUpdateTabla(tab_tabla, "mvmodelo_id", "");//actualiza solo componentes
    }

    public void cargarVersion() {
        tab_tabla.getColumna("mvversion_id").setCombo("SELECT mvversion_id,mvversion_descripcion FROM mvversion_vehiculo where mvmodelo_id='" + tab_tabla.getValor("mvmodelo_id") + "'");
        utilitario.addUpdateTabla(tab_tabla, "mvversion_id", "");//actualiza solo componentes
    }

    //DATOS PARA CONDUCTOR
    public void conductor() {
        TablaGenerica tab_dato = aCombustible.getChofer(tab_tabla.getValor("mve_cod_conductor"));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("mve_conductor", tab_dato.getValor("nombres"));
            tab_tabla.setValor("mve_asignacion", tab_dato.getValor("activo"));
            utilitario.addUpdate("tab_tabla");
        } else {
            utilitario.agregarMensajeInfo("No existen Datos", "");
        }
    }

    //COMPONENTES ADICIONALES ACTIVAR
    public void activarCasilla() {
        if (tab_tabla.getValor("mve_tipomedicion").equals("2")) {
            tab_tabla.getColumna("mve_kilometros_actual").setLectura(true);
            tab_tabla.getColumna("mve_horometro").setLectura(false);
            tab_tabla.setValor("mve_tipo_ingreso", "M");
            tab_tabla.setValor("MVE_NUMIMR", "H");
            tab_tabla.getColumna("mve_rendimientogl_h").setLectura(false);
            utilitario.addUpdate("tab_tabla");
        } else {
            tab_tabla.getColumna("mve_kilometros_actual").setLectura(false);
            tab_tabla.getColumna("mve_horometro").setLectura(true);
            tab_tabla.setValor("mve_tipo_ingreso", "A");
            tab_tabla.setValor("MVE_NUMIMR", "K");
            tab_tabla.getColumna("mve_rendimientogl_h").setLectura(true);
            utilitario.addUpdate("tab_tabla");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.getValor("mve_secuencial") != null) {
            TablaGenerica tab_dato = aCombustible.getVehiculoDa(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            if (!tab_dato.isEmpty()) {
                if (tab_tabla.getValor("mve_kilometros_actual")!=(tab_dato.getValor("mve_kilometros_actual"))) {
                    System.err.println("");
                    aCombustible.setUpdateVehi1(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), "mve_kilometros_actual", Double.valueOf(tab_tabla.getValor("mve_kilometros_actual")));
                }
                if (tab_tabla.getValor("mve_capacidad_tanque")!=(tab_dato.getValor("mve_capacidad_tanque"))) {
                    aCombustible.setUpdateVehi1(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), "mve_capacidad_tanque", Double.valueOf(tab_tabla.getValor("mve_capacidad_tanque")));
                }
                if (tab_tabla.getValor("mve_horometro")!=(tab_dato.getValor("mve_horometro"))) {
                    aCombustible.setUpdateVehi(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), "mve_horometro", tab_tabla.getValor("mve_horometro"));
                }
                if (tab_tabla.getValor("mve_rendimientogl_h")!=(tab_dato.getValor("mve_rendimientogl_h"))) {
                    aCombustible.setUpdateVehi(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), "mve_rendimientogl_h", tab_tabla.getValor("mve_rendimientogl_h"));
                }
                if (tab_tabla.getValor("mve_duracion_llanta")!=(tab_dato.getValor("mve_duracion_llanta"))) {
                    aCombustible.setUpdateVehi(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), "mve_duracion_llanta", tab_tabla.getValor("mve_duracion_llanta"));
                }
                utilitario.agregarMensaje("Registro Actualizado", null);
            } else {
                utilitario.agregarMensajeInfo("No existen Datos", "");
            }
        } else if (tab_tabla.guardar()) {
            if (tab_tabla.guardar()) {
                if (tab_accesorios.guardar()) {
                    con_postgres.guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getSet_marca() {
        return set_marca;
    }

    public void setSet_marca(Tabla set_marca) {
        this.set_marca = set_marca;
    }

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_modelo() {
        return set_modelo;
    }

    public void setSet_modelo(Tabla set_modelo) {
        this.set_modelo = set_modelo;
    }

    public Tabla getSet_version() {
        return set_version;
    }

    public void setSet_version(Tabla set_version) {
        this.set_version = set_version;
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

    public Tabla getTab_accesorios() {
        return tab_accesorios;
    }

    public void setTab_accesorios(Tabla tab_accesorios) {
        this.tab_accesorios = tab_accesorios;
    }
}
