/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_detalle_mantenimiento extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //dibujar tablas
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla set_articulos = new Tabla();
    
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    private Texto taccesorio = new Texto();
    private Texto txt_cantidad = new Texto();
    private Texto txt_estado = new Texto();

    //Dialogo de Ingreso de tablas
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_o = new Grid();
    private Grid grid = new Grid();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    public pre_detalle_mantenimiento() {
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Articulos ");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("lista");
        bar_botones.agregarBoton(bot_busca);
        //para poder busca por apelllido el garante
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
        dia_dialogo.setTitle("IINGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acepta_marca");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);
        
        set_articulos.setId("set_articulos");
        set_articulos.setConexion(con_sql);
        set_articulos.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1 order by LIS_NOMBRE");
        set_articulos.getColumna("LIS_NOMBRE").setFiltro(true);
        set_articulos.setTipoSeleccion(false);
        set_articulos.setRows(10);
        set_articulos.dibujar();
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvvehiculo", "mve_secuencial", 1);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.agregarRelacion(tab_cabecera);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_sql);
        tab_cabecera.setTabla("mvcabmanteni", "mca_secuencial", 2);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_sql);
        tab_detalle.setTabla("mvdetmateni", "mde_codigo", 3);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir3(ptt, ptc, ptd, "%", "%", "H");
        agregarComponente(div);
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
