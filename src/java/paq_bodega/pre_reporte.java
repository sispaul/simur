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

/**
 *
 * @author Diego
 */
public class pre_reporte extends Pantalla {

    private SeleccionTabla set_tabla = new SeleccionTabla();
    //Buscar 
    private Texto tex_busca = new Texto();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Tabla tab_consulta = new Tabla();
    
    public pre_reporte() {
        bar_botones.limpiar(); /// deja en blanco la barra de botones
      
        ///configurar el seleccion tabla 
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

        

         /**
         * CONFIGURACIÓN DE ONJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getNombre().equals("LISTADO DE ARTICULOS POR GRUPO")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                set_tabla.dibujar();
            } else if (set_tabla.isVisible()) {
                //los parametros de este reporte
                                        
                p_parametros = new HashMap();
                p_parametros.put("p_grupo", set_tabla.getValorSeleccionado());
                p_parametros.put("p_nomresp", tab_consulta.getValor("NICK_USUA")+"");
                set_tabla.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }else if(rep_reporte.getNombre().equals("LISTADO DE TODOS ARTICULOS")){
            if (rep_reporte.isVisible()) {
                //los parametros de este reporte
                rep_reporte.cerrar();
                p_parametros = new HashMap();
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }else if(rep_reporte.getNombre().equals("LISTA DE STOCK DE MATERIALES POR GRUPO")){
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                set_tabla.dibujar();
            } else if (set_tabla.isVisible()) {
                //los parametros de este reporte
                           
                p_parametros = new HashMap();
                p_parametros.put("p_grupo", set_tabla.getValorSeleccionado());
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                set_tabla.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }else if(rep_reporte.getNombre().equals("LISTA DE STOCK DE MATERIALES POR GRUPO(TODOS)")){
            if (rep_reporte.isVisible()) {
                //los parametros de este reporte
                rep_reporte.cerrar();
                p_parametros = new HashMap();
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }
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
}
