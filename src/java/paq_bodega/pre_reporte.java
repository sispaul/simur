/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Paolo
 */
public class pre_reporte extends Pantalla {
    private Conexion con_postgres= new Conexion();
    private SeleccionTabla set_tabla = new SeleccionTabla();
    private SeleccionTabla set_tablaMat = new SeleccionTabla();
    private SeleccionTabla set_tablaDes = new SeleccionTabla();
    //Buscar 
    private Texto tex_busca = new Texto();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Tabla tab_consulta = new Tabla();
    
    public pre_reporte() {
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        
        bar_botones.limpiar(); /// deja en blanco la barra de botones
        tex_busca.setId("tex_busca");
        
        ///configurar el seleccion tabla Grupos
        set_tabla.setId("set_tabla");

        Grid gri_busca = new Grid();
        gri_busca.setColumns(3);
        gri_busca.getChildren().add(tex_busca);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar");
        bot_busca.setMetodo("buscarMarca");
        gri_busca.getChildren().add(bot_busca);

        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Todos");
        bot_ver.setMetodo("verTodos");
        gri_busca.getChildren().add(bot_ver);

        set_tabla.getGri_cuerpo().setHeader(gri_busca);
        
        set_tabla.setTitle("SELECCIONE GRUPO");
        set_tabla.setSeleccionTabla("SELECT id_grupo,descripcion from bodc_grupo", "id_grupo");
        //set_tabla.getTab_seleccion().getColumna("descripcion").setFiltro(true);
        set_tabla.setRadio();
        set_tabla.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(set_tabla);


///configurar el seleccion tabla Materiales
        set_tablaMat.setId("set_tablaMat");

        Grid gri_buscaMat = new Grid();
        gri_buscaMat.setColumns(3);
        gri_buscaMat.getChildren().add(tex_busca);

        Boton bot_buscaMat = new Boton();
        bot_buscaMat.setValue("Buscar");
        bot_buscaMat.setMetodo("buscarMateriales");
        gri_buscaMat.getChildren().add(bot_buscaMat);

        Boton bot_verMat = new Boton();
        bot_verMat.setValue("Ver Todos");
        bot_verMat.setMetodo("verTodosMat");
        gri_buscaMat.getChildren().add(bot_verMat);

        set_tablaMat.getGri_cuerpo().setHeader(gri_buscaMat);
        set_tablaMat.setTitle("SELECCIONE GRUPO");
        set_tablaMat.setSeleccionTabla("select ide_mat_bodega, des_material from bodc_material", "ide_mat_bodega");
        //set_tabla.getTab_seleccion().getColumna("des_material").setFiltro(true);
        set_tablaMat.setRadio();
        set_tablaMat.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(set_tablaMat);

        ///configurar el seleccion tabla DESTINO
        set_tablaDes.setId("set_tablaDes");

        Grid gri_buscaDes = new Grid();
        gri_buscaDes.setColumns(3);
        gri_buscaDes.getChildren().add(tex_busca);

        Boton bot_buscaDes = new Boton();
        bot_buscaDes.setValue("Buscar");
        bot_buscaDes.setMetodo("buscarMateriales");
        gri_buscaDes.getChildren().add(bot_buscaDes);

        Boton bot_verDes = new Boton();
        bot_verDes.setValue("Ver Todos");
        bot_verDes.setMetodo("verTodosMat");
        gri_buscaDes.getChildren().add(bot_verDes);

        set_tablaDes.getGri_cuerpo().setHeader(gri_buscaDes);
        set_tablaDes.setTitle("SELECCIONE DESTINO");
        set_tablaDes.setSeleccionTabla("select id_destino, descripcion from bodc_destinos", "id_destino");
        //set_tablaDes.getTab_seleccion().getColumna("descripcion").setFiltro(true);
        set_tablaDes.setRadio();
        set_tablaDes.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(set_tablaDes);

        
         /**
         * CONFIGURACIÓN DE ONJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        /**
         * PERSONA RESPONSABLE
         */
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

    public SeleccionTabla getSet_tablaMat() {
        return set_tablaMat;
    }

    public void setSet_tablaMat(SeleccionTabla set_tablaMat) {
        this.set_tablaMat = set_tablaMat;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {

        switch (rep_reporte.getNombre()) {
            case "LISTADO DE ARTICULOS POR GRUPO":
                if (rep_reporte.isVisible()) {
                    rep_reporte.cerrar();
                    set_tabla.dibujar();
                } else if (set_tabla.isVisible()) {
                    //los parametros de este reporte
                    if (set_tabla.getValorSeleccionado() != null) {     
                        p_parametros = new HashMap();
                        //p_parametros.put("p_grupo", set_tabla.getSeleccionados());
                        p_parametros.put("p_grupo", set_tabla.getValorSeleccionado());
                        p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
                        set_tabla.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                }
                break;
            case "LISTADO DE TODOS ARTICULOS":
                if (rep_reporte.isVisible()) {
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
            case "LISTA DE STOCK DE MATERIALES POR GRUPO":
                if (rep_reporte.isVisible()) {
                    rep_reporte.cerrar();
                    set_tabla.dibujar();
                } else if (set_tabla.isVisible()) {
                    if (set_tabla.getValorSeleccionado() != null) {     
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("pide_grupo", set_tabla.getValorSeleccionado());
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    //utilitario.agregarMensaje("Reporte", rep_reporte.getPath());
                    set_tabla.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                }
             case "LISTA DE STOCK DE MATERIALES POR GRUPO(TODOS)":
                if (rep_reporte.isVisible()) {
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    //utilitario.agregarMensaje("USER: ", rep_reporte.getPath());
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                case "MOVIMIENTOS POR DESTINO":
                if (rep_reporte.isVisible()) {
                    rep_reporte.cerrar();
                    set_tablaDes.dibujar();
                } else if (set_tablaDes.isVisible()) {
                    if (set_tablaDes.getValorSeleccionado() != null) {     
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("pdestino", Integer.parseInt(set_tablaDes.getValorSeleccionado()));
                    p_parametros.put("pfec_inicial", "01/01/2013");
                    p_parametros.put("pfec_final", "31/12/2013");
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    set_tablaDes.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                }
             case "LISTA 2":
                if (rep_reporte.isVisible()) {
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    //utilitario.agregarMensaje("USER: ", rep_reporte.getPath());
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
               break;
        }
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

    public void verTodos() {
        set_tabla.getTab_seleccion().setSql("SELECT id_grupo,descripcion from bodc_grupo");
        set_tabla.getTab_seleccion().ejecutarSql();
    }

    public void buscarMarca() {
        if (tex_busca.getValue() != null && tex_busca.getValue().toString().isEmpty() == false) {
            set_tabla.getTab_seleccion().setSql("SELECT id_grupo,descripcion from bodc_grupo where descripcion like '%" + tex_busca.getValue() + "%'");
            set_tabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensaje("Debe ingresar un valor en el texto", "");
        }

    }

      public void abrirSeleccionTabla() {
        set_tabla.dibujar();
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

    public SeleccionTabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(SeleccionTabla set_tabla) {
        this.set_tabla = set_tabla;
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

    public SeleccionTabla getSet_tablaDes() {
        return set_tablaDes;
    }

    public void setSet_tablaDes(SeleccionTabla set_tablaDes) {
        this.set_tablaDes = set_tablaDes;
    }
}
