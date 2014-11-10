/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_asignacion_desaignacion_vehiculo extends Pantalla{

    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    
    private Tabla tab_tabla = new Tabla();
    private SeleccionTabla set_automotores = new SeleccionTabla(); 
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Cajas de Texto
    private Texto tplaca = new Texto();
    private Texto tmarca = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tanio = new Texto();
    private Texto tcolor = new Texto();
    private Texto tconductor = new Texto();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_asignacion_desaignacion_vehiculo() {
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
//        aut_busca.setAutoCompletar("");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Asignación:"));
        bar_botones.agregarComponente(aut_busca);
        
        //Ingreso y busqueda 
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 750px;");
        pan_panel.setHeader("Busqueda para Asignar - DesAsignar");
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        tplaca.setSize(7);
        tmarca.setSize(10);
        tmodelo.setSize(20);
        tanio.setSize(5);
        tcolor.setSize(8);
        tconductor.setSize(45);
        gri_busca.getChildren().add(new Etiqueta("# PLACA :"));
        gri_busca.getChildren().add(tplaca);
        gri_busca.getChildren().add(new Etiqueta("MARCA :"));
        gri_busca.getChildren().add(tmarca);
        gri_busca.getChildren().add(new Etiqueta("MODELO :"));
        gri_busca.getChildren().add(tmodelo);
        gri_busca.getChildren().add(new Etiqueta("AÑO :"));
        gri_busca.getChildren().add(tanio);
        gri_busca.getChildren().add(new Etiqueta("COLOR :"));
        gri_busca.getChildren().add(tcolor);
        gri_busca.getChildren().add(new Etiqueta("CONDUCTOR :"));
        gri_busca.getChildren().add(tconductor);
        pan_panel.getChildren().add(gri_busca);
        
        //Botones
        Panel pan_panel1 = new Panel();
        pan_panel1.setId("pan_panel1");
        pan_panel1.setStyle("width:250px;");
        pan_panel1.setHeader("Acciones De Botones");
        
        Grid gri_botones = new Grid();
        gri_botones.setColumns(2);
        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar: ");
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        Boton bot_news = new Boton();
        bot_news.setValue("Nuevo: ");
        bot_news.setIcon("ui-icon-document");
        bot_news.setMetodo("abrirVerTabla");
        Boton bot_asigna = new Boton();
        bot_asigna.setValue("Asignar :");
        bot_asigna.setIcon("ui-icon-check");
        bot_asigna.setMetodo("Busca_tipo");
        Boton bot_quitar = new Boton();
        bot_quitar.setValue("Des-Asignar: ");
        bot_quitar.setIcon("ui-icon-cancel");
        bot_quitar.setMetodo("Busca_tipo");
        gri_botones.getChildren().add(bot_busca);
        gri_botones.getChildren().add(bot_news);
        gri_botones.getChildren().add(bot_asigna);
        gri_botones.getChildren().add(bot_quitar);
        pan_panel1.getChildren().add(gri_botones);
        
        Division div = new Division();
        div.dividir2(pan_panel,pan_panel1, "54%", "V");
        Division div1 = new Division();
        div1.dividir2(div,pan_opcion, "16%", "H");
        agregarComponente(div1);
        dibujardocumento();
        
        set_automotores.setId("set_automotores");
        set_automotores.getTab_seleccion().setConexion(con_sql);
        set_automotores.setSeleccionTabla("SELECT MVE_SECUENCIAL,MVE_PLACA,MVE_MARCA,MVE_TIPO,MVE_MODELO,MVE_CONDUCTOR,MVE_COLOR,MVE_ANO,MVE_KILOMETRAJE,MVE_HOROMETRO\n" +
                "FROM MVVEHICULO", "MVE_SECUENCIAL");
        set_automotores.getTab_seleccion().getColumna("MVE_PLACA").setFiltro(true);
        set_automotores.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_automotores.getTab_seleccion().setRows(11);
        set_automotores.setWidth("55%"); //siempre en porcentajes  ancho
        set_automotores.setRadio();
        set_automotores.getBot_aceptar().setMetodo("aceptoTabla");
        set_automotores.setHeader("AUTOMOTORES");
        agregarComponente(set_automotores);
    }

    public void abrirVerTabla() {
        set_automotores.dibujar();
    }
    
    public void aceptoTabla(){
        
    }
    
    public void dibujardocumento(){
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvasignarveh", "mav_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mav_secuencial=-1");
        } else {
            tab_tabla.setCondicion("mav_secuencial=" + aut_busca.getValor());
        }
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(ptt);
        pan_opcion.getChildren().add(gru); 
        
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

    public SeleccionTabla getSet_automotores() {
        return set_automotores;
    }

    public void setSet_automotores(SeleccionTabla set_automotores) {
        this.set_automotores = set_automotores;
    }
    
}
