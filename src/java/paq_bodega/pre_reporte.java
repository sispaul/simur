/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Paolo
 */
public class pre_reporte extends Pantalla {

    private Conexion con_postgres = new Conexion();
    private Tabla set_tabla = new Tabla();
    private Tabla set_tablaDes = new Tabla();
    private Tabla set_tablaEnc = new Tabla();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Consulta
    private Tabla tab_consulta = new Tabla();
    //Dialogos
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogoDes = new Dialogo();
    private Dialogo dia_dialogoEnc = new Dialogo();
    private Dialogo dia_dialogoDE = new Dialogo();
    private Dialogo dia_dialogoGD = new Dialogo();
    private Dialogo dia_dialogoGE = new Dialogo();
    private Dialogo dia_dialogoSM = new Dialogo();
    private Dialogo dia_dialogoIM = new Dialogo();
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    private Grid grid_de = new Grid();
    private Grid grid_gd = new Grid();
    private Grid grid_ge = new Grid();
    private Grid grid = new Grid();
    private Etiqueta etifec = new Etiqueta();

    public pre_reporte() {
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        bar_botones.limpiar(); /// deja en blanco la barra de botones

        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:270px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("REPORTES GRUPOS MATERIAL");
        grid_pant.getChildren().add(eti_encab);
        Boton bot_lista = new Boton();
        bot_lista.setValue("LISTA DE REPORTES");
        bot_lista.setMetodo("abrirListaReportes");
        grid_pant.getChildren().add(bot_lista);
        agregarComponente(grid_pant);

        //Configuracion grid Fechas
        etifec.setStyle("font-size:16px;color:blue");
        etifec.setValue("SELECCIONE RANGO DE FECHAS");
        grid.setColumns(4);
        //campos fecha       
        grid.getChildren().add(new Etiqueta("FECHA INICIAL"));
        grid.getChildren().add(cal_fechaini);
        grid.getChildren().add(new Etiqueta("   FECHA FINAL"));
        grid.getChildren().add(cal_fechafin);

        ///configurar la tabla Seleccion Grupos
        //Configurando el dialogo
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("BODEGA - GRUPOS"); //titulo
        dia_dialogo.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("60%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoDialogo");

        ///configurar tabla Grupos
        set_tabla.setId("set_tabla");
        set_tabla.setConexion(con_postgres);
        set_tabla.setSql("SELECT id_grupo,descripcion from bodc_grupo order by descripcion");
        set_tabla.getColumna("descripcion").setFiltro(true);
        set_tabla.setRows(15);
        set_tabla.setTipoSeleccion(false);
        set_tabla.dibujar();

        agregarComponente(dia_dialogo);


        ///configurar la tabla Seleccion Destino
        //Configurando el dialogo
        dia_dialogoDes.setId("dia_dialogoDes");
        dia_dialogoDes.setTitle("BODEGA - DESTINOS"); //titulo
        dia_dialogoDes.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoDes.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoDes.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoDes.getBot_aceptar().setMetodo("aceptoDialogo");
        ///configurar tabla Destino
        set_tablaDes.setId("set_tablaDes");
        set_tablaDes.setConexion(con_postgres);
        set_tablaDes.setSql("select id_destino, descripcion from bodc_destinos ORDER BY descripcion");
        set_tablaDes.getColumna("descripcion").setFiltro(true);
        set_tablaDes.setRows(15);
        set_tablaDes.setTipoSeleccion(false);
        set_tablaDes.dibujar();

        agregarComponente(dia_dialogoDes);

        ///configurar la tabla Seleccion Encargado
        //Configurando el dialogo
        dia_dialogoEnc.setId("dia_dialogoEnc");
        dia_dialogoEnc.setTitle("BODEGA - ENCARGADOS"); //titulo
        dia_dialogoEnc.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoEnc.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoEnc.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEnc.getBot_aceptar().setMetodo("aceptoDialogo");

        ///configurar tabla Encargado
        set_tablaEnc.setId("set_tablaEnc");
        set_tablaEnc.setConexion(con_postgres);
        set_tablaEnc.setSql("select cod_empleado, nombres from srh_empleado order by nombres");
        set_tablaEnc.getColumna("nombres").setFiltro(true);
        set_tablaEnc.setRows(15);
        set_tablaEnc.setTipoSeleccion(false);
        set_tablaEnc.dibujar();

        agregarComponente(dia_dialogoEnc);

        ///configurar la tabla Seleccion MOVIMIENTOS POR DESTINO Y ENCARGADO
        //Configurando el dialogo
        dia_dialogoDE.setId("dia_dialogoDE");
        dia_dialogoDE.setTitle("BODEGA - MOVIMIENTOS POR DESTINO Y ENCARGADO"); //titulo
        dia_dialogoDE.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogoDE.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoDE.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoDE.getBot_aceptar().setMetodo("aceptoDialogo");

        grid_de.setColumns(2);
        grid_de.getChildren().add(new Etiqueta("SELECCIONE DESTINO"));
        grid_de.getChildren().add(new Etiqueta("SELECCIONE ENCARGADO"));

        agregarComponente(dia_dialogoDE);


        ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y DESTINO
        //Configurando el dialogo
        dia_dialogoGD.setId("dia_dialogoGD");
        dia_dialogoGD.setTitle("BODEGA - MOVIMIENTOS POR GRUPO Y DESTINO"); //titulo
        dia_dialogoGD.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogoGD.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoGD.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoGD.getBot_aceptar().setMetodo("aceptoDialogo");

        grid_gd.setColumns(2);
        grid_gd.getChildren().add(new Etiqueta("SELECCIONE GRUPO"));
        grid_gd.getChildren().add(new Etiqueta("SELECCIONE DESTINO"));

        agregarComponente(dia_dialogoGD);


        ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y ENCARGADO
        //Configurando el dialogo
        dia_dialogoGE.setId("dia_dialogoGE");
        dia_dialogoGE.setTitle("BODEGA - MOVIMIENTOS POR GRUPO Y ENCARGADO"); //titulo
        dia_dialogoGE.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogoGE.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoGE.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoGE.getBot_aceptar().setMetodo("aceptoDialogo");

        grid_ge.setColumns(2);
        grid_ge.getChildren().add(new Etiqueta("SELECCIONE GRUPO"));
        grid_ge.getChildren().add(new Etiqueta("SELECCIONE ENCARGADO"));

        agregarComponente(dia_dialogoGE);



        ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y ENCARGADO
        //Configurando el dialogo
        dia_dialogoSM.setId("dia_dialogoSM");
        dia_dialogoSM.setTitle("BODEGA - SALIDA DE MATERIALES"); //titulo
        dia_dialogoSM.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogoSM.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoSM.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoSM.getBot_aceptar().setMetodo("aceptoDialogo");

        agregarComponente(dia_dialogoSM);



        ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y ENCARGADO
        //Configurando el dialogo
        dia_dialogoIM.setId("dia_dialogoIM");
        dia_dialogoIM.setTitle("BODEGA - INGRESO DE MATERIALES"); //titulo
        dia_dialogoIM.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogoIM.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoIM.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoIM.getBot_aceptar().setMetodo("aceptoDialogo");

        agregarComponente(dia_dialogoIM);


        /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);

        /**
         * PERSONA RESPONSABLE
         */
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fechaini.setFechaActual();
        cal_fechafin.setFechaActual();
        switch (rep_reporte.getNombre()) {
            case "LISTADO DE ARTICULOS POR GRUPO":
                dia_dialogo.Limpiar();
                Etiqueta eti0 = new Etiqueta();
                eti0.setStyle("font-size:18px;color:blue");
                eti0.setValue("SELECCIONE GRUPO");
                dia_dialogo.setDialogo(eti0);
                dia_dialogo.setDialogo(set_tabla);
                set_tabla.dibujar();
                dia_dialogo.dibujar();
                break;
            case "LISTADO DE ARTICULOS POR GRUPO (TODOS)":
                aceptoDialogo();
                break;
            case "LISTA DE STOCK DE MATERIALES POR GRUPO":
                dia_dialogo.Limpiar();
                Etiqueta eti = new Etiqueta();
                eti.setStyle("font-size:18px;color:blue");
                eti.setValue("SELECCIONE GRUPO");
                dia_dialogo.setDialogo(eti);
                dia_dialogo.setDialogo(set_tabla);
                set_tabla.dibujar();
                dia_dialogo.dibujar();
                break;
            case "LISTA DE STOCK DE MATERIALES POR GRUPO(TODOS)":
                aceptoDialogo();
                break;
            case "MOVIMIENTOS POR DESTINO":
                dia_dialogoDes.Limpiar();
                //Agrega Fechas
                dia_dialogoDes.setDialogo(etifec);
                dia_dialogoDes.setDialogo(grid);

                //Etiqueta tabla
                Etiqueta eti2 = new Etiqueta();
                eti2.setStyle("font-size:16px;color:blue");
                eti2.setValue("SELECCIONE DESTINO");
                dia_dialogoDes.setDialogo(eti2);
                dia_dialogoDes.setDialogo(set_tablaDes);
                set_tablaDes.dibujar();
                dia_dialogoDes.dibujar();
                break;
            case "MOVIMIENTOS POR ENCARGADO":
                dia_dialogoEnc.Limpiar();
                //Agrega Fechas
                dia_dialogoEnc.setDialogo(etifec);
                dia_dialogoEnc.setDialogo(grid);

                //Etiqueta tabla
                Etiqueta eti3 = new Etiqueta();
                eti3.setStyle("font-size:16px;color:blue");
                eti3.setValue("SELECCIONE ENCARGADO");
                dia_dialogoEnc.setDialogo(eti3);
                dia_dialogoEnc.setDialogo(set_tablaEnc);
                set_tablaEnc.dibujar();
                dia_dialogoEnc.dibujar();
                break;
            case "MOVIMIENTOS POR DESTINO Y ENCARGADO":
                dia_dialogoDE.Limpiar();
                //Agrega Fechas
                dia_dialogoDE.setDialogo(etifec);
                dia_dialogoDE.setDialogo(grid);

                //Configura grid
                grid_de.getChildren().add(set_tablaDes);
                grid_de.getChildren().add(set_tablaEnc);
                dia_dialogoDE.setDialogo(grid_de);
                set_tablaDes.dibujar();
                set_tablaEnc.dibujar();
                dia_dialogoDE.dibujar();
                break;
            case "MOVIMIENTOS POR GRUPO Y DESTINO":
                dia_dialogoGD.Limpiar();
                //Agrega Fechas
                dia_dialogoGD.setDialogo(etifec);
                dia_dialogoGD.setDialogo(grid);

                //Configura grid
                grid_gd.getChildren().add(set_tabla);
                grid_gd.getChildren().add(set_tablaDes);
                dia_dialogoGD.setDialogo(grid_gd);
                set_tablaDes.dibujar();
                set_tablaEnc.dibujar();
                dia_dialogoGD.dibujar();
                break;
            case "MOVIMIENTOS POR GRUPO Y ENCARGADO":
                dia_dialogoGE.Limpiar();
                //Agrega Fechas
                dia_dialogoGE.setDialogo(etifec);
                dia_dialogoGE.setDialogo(grid);

                //Configura grid
                grid_ge.getChildren().add(set_tabla);
                grid_ge.getChildren().add(set_tablaEnc);
                dia_dialogoGE.setDialogo(grid_ge);
                set_tabla.dibujar();
                set_tablaEnc.dibujar();
                dia_dialogoGE.dibujar();
                break;


            case "LISTADO DE SALIDA DE MATERIALES":
                dia_dialogoSM.Limpiar();
                //Agrega Fechas
                dia_dialogoSM.setDialogo(etifec);
                dia_dialogoSM.setDialogo(grid);
                dia_dialogoSM.dibujar();
                break;

            case "LISTADO DE INGRESO DE MATERIALES":
                dia_dialogoIM.Limpiar();
                //Agrega Fechas
                dia_dialogoIM.setDialogo(etifec);
                dia_dialogoIM.setDialogo(grid);
                dia_dialogoIM.dibujar();
                break;
        }
    }

    public void aceptoDialogo() {
        if (utilitario.isFechasValidas(cal_fechaini.getFecha(), cal_fechafin.getFecha())) {
            switch (rep_reporte.getNombre()) {
                case "LISTADO DE ARTICULOS POR GRUPO":
                    //los parametros de este reporte
                    if (set_tabla.getValorSeleccionado() != null) {
                        p_parametros = new HashMap();
                        //p_parametros.put("p_grupo", set_tabla.getSeleccionados());
                        p_parametros.put("p_grupo", set_tabla.getValorSeleccionado());
                        p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA") + "");
                        dia_dialogo.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }

                    break;
                case "LISTADO DE ARTICULOS POR GRUPO (TODOS)":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    break;
                case "LISTA DE STOCK DE MATERIALES POR GRUPO":
                    if (set_tabla.getValorSeleccionado() != null) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pide_grupo", set_tabla.getValorSeleccionado());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                        dia_dialogo.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "LISTA DE STOCK DE MATERIALES POR GRUPO(TODOS)":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    //utilitario.agregarMensaje("USER: ", rep_reporte.getPath());
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    break;
                case "MOVIMIENTOS POR DESTINO":
                    if (set_tablaDes.getValorSeleccionado() != null) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pdestino", set_tablaDes.getValorSeleccionado());
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoDes.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "MOVIMIENTOS POR ENCARGADO":
                    if (set_tablaEnc.getValorSeleccionado() != null) {
                        p_parametros = new HashMap();
                        p_parametros.put("pcod_empleado", set_tablaEnc.getValorSeleccionado());
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoEnc.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "MOVIMIENTOS POR DESTINO Y ENCARGADO":
                    if ((set_tablaDes.getValorSeleccionado() != null) && (set_tablaEnc.getValorSeleccionado() != null)) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pdestino", Integer.parseInt(set_tablaDes.getValorSeleccionado()));
                        p_parametros.put("pencargado", Integer.parseInt(set_tablaEnc.getValorSeleccionado()));
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoDE.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "MOVIMIENTOS POR GRUPO Y DESTINO":
                    if ((set_tabla.getValorSeleccionado() != null) && (set_tablaDes.getValorSeleccionado() != null)) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pgrupo", Integer.parseInt(set_tabla.getValorSeleccionado()));
                        p_parametros.put("pdestino", Integer.parseInt(set_tablaDes.getValorSeleccionado()));
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoGD.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "MOVIMIENTOS POR GRUPO Y ENCARGADO":
                    if ((set_tabla.getValorSeleccionado() != null) && (set_tablaEnc.getValorSeleccionado() != null)) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pgrupo", Integer.parseInt(set_tabla.getValorSeleccionado()));
                        p_parametros.put("pencargado", Integer.parseInt(set_tablaEnc.getValorSeleccionado()));
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoGE.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "LISTADO DE SALIDA DE MATERIALES":
                    if (set_tabla.getValorSeleccionado() != null) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoSM.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
                case "LISTADO DE INGRESO DE MATERIALES":
                    if (set_tabla.getValorSeleccionado() != null) {
                        //los parametros de este reporte
                        p_parametros = new HashMap();
                        p_parametros.put("pfec_inicial", cal_fechaini.getFecha());
                        p_parametros.put("pfec_final", cal_fechafin.getFecha());
                        p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                        dia_dialogoIM.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                    break;
            }
        } else {
            utilitario.agregarMensaje("Fechas", "Rango de Fechas no valido");
        }
    }

    public void abrirDialogo() {
        dia_dialogo.dibujar();
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

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(Tabla set_tabla) {
        this.set_tabla = set_tabla;
    }

    public Tabla getSet_tablaEnc() {
        return set_tablaEnc;
    }

    public void setSet_tablaEnc(Tabla set_tablaEnc) {
        this.set_tablaEnc = set_tablaEnc;
    }

    public Tabla getSet_tablaDes() {
        return set_tablaDes;
    }

    public void setSet_tablaDes(Tabla set_tablaDes) {
        this.set_tablaDes = set_tablaDes;
    }

    public Calendario getCal_fechaini() {
        return cal_fechaini;
    }

    public void setCal_fechaini(Calendario cal_fechaini) {
        this.cal_fechaini = cal_fechaini;
    }

    public Calendario getCal_fechafin() {
        return cal_fechafin;
    }

    public void setCal_fechafin(Calendario cal_fechafin) {
        this.cal_fechafin = cal_fechafin;
    }
}
