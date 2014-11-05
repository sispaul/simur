/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
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
public class pre_catastro_vehiculo_chofer extends Pantalla{

    private Conexion con_sql = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //DECLARACION OBJETOS PARA PARAMETROS
    private SeleccionTabla set_marca = new SeleccionTabla();
    private SeleccionTabla set_tipo = new SeleccionTabla();
    private SeleccionTabla set_modelo = new SeleccionTabla();
    private SeleccionTabla set_version = new SeleccionTabla();
    
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_catastro_vehiculo_chofer() {
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_registro = new Boton();
        bot_registro.setValue("INSERTAR MARCA");
        bot_registro.setIcon("ui-icon-comment");
        bot_registro.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot_registro);
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvvehiculo", "mve_secuencial", 1);
        tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE FROM MVLISTA WHERE TAB_CODIGO = 'tipo' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_COLOR").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE FROM MVLISTA where TAB_CODIGO = 'COLOR' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_ANO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE FROM MVLISTA where TAB_CODIGO = 'ANIO' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_TIPO_COMBUSTIBLE").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_tabla.getColumna("MVE_OBSERVACIONES").setVisible(false);
        tab_tabla.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_ESTADO_REGISTRO ").setVisible(false);
        tab_tabla.getColumna("MVE_FECHA_BORRADO ").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAINGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINBORRADO").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tab_tabla);
        agregarComponente(tpg);
        
        //Dialogos de Ingreso de parametros necesarios para vehiculo
        Grid gri_marca = new Grid();
        gri_marca.setColumns(6);
        gri_marca.getChildren().add(new Etiqueta("Ingrese Marca: "));
        gri_marca.getChildren().add(tmarca);
        Boton bot_marca = new Boton();
        bot_marca.setValue("Guardar");
        bot_marca.setIcon("ui-icon-disk");
        bot_marca.setMetodo("insMarca");
        bar_botones.agregarBoton(bot_marca);
        Boton bot_marcax = new Boton();
        bot_marcax.setValue("Eliminar");
        bot_marcax.setIcon("ui-icon-closethick");
        bot_marcax.setMetodo("endMarca");
        bar_botones.agregarBoton(bot_marcax);
        gri_marca.getChildren().add(bot_marca);
        gri_marca.getChildren().add(bot_marcax);
        set_marca.setId("set_marca");
        set_marca.getTab_seleccion().setConexion(con_sql);
        set_marca.setSeleccionTabla("SELECT LIS_NOMBRE,LIS_NOMBRE as MARCA FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1 order by MARCA", "LIS_NOMBRE");
        set_marca.getTab_seleccion().getColumna("MARCA").setFiltro(true);
        set_marca.getTab_seleccion().setRows(11);
        set_marca.setWidth("36%"); //siempre en porcentajes  ancho
        set_marca.setRadio();
        set_marca.getGri_cuerpo().setHeader(gri_marca);
        set_marca.getBot_aceptar().setMetodo("abrirSeleccionTabla1");
        set_marca.setHeader("MARCA");
        agregarComponente(set_marca);
        
        Grid gri_tipo = new Grid();
        gri_tipo.setColumns(6);
        gri_tipo.getChildren().add(new Etiqueta("Ingrese Tipo"));
        gri_tipo.getChildren().add(ttipo);
        Boton bot_tipo = new Boton();
        bot_tipo.setValue("Guardar");
        bot_tipo.setIcon("ui-icon-disk");
        bot_tipo.setMetodo("insTipo");
        bar_botones.agregarBoton(bot_tipo);
        Boton bot_tipox = new Boton();
        bot_tipox.setValue("Eliminar");
        bot_tipox.setIcon("ui-icon-closethick");
        bot_tipox.setMetodo("endMarca");
        bar_botones.agregarBoton(bot_tipox);
        gri_tipo.getChildren().add(bot_tipo);
        gri_tipo.getChildren().add(bot_tipox);
        set_tipo.setId("set_tipo");
        set_tipo.getTab_seleccion().setConexion(con_sql);
        set_tipo.setSeleccionTabla("SELECT LIS_NOMBRE,LIS_NOMBRE as TIPO FROM MVLISTA WHERE TAB_CODIGO = 'TIPO' and LIS_ESTADO = 1 order by tipo", "LIS_NOMBRE");
        set_tipo.getTab_seleccion().getColumna("TIPO").setFiltro(true);
        set_tipo.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_tipo.getTab_seleccion().setRows(11);
        set_tipo.setWidth("36%"); //siempre en porcentajes  ancho
        set_tipo.setRadio();
        set_tipo.getGri_cuerpo().setHeader(gri_tipo);
        set_tipo.getBot_aceptar().setMetodo("abrirSeleccionTabla2");
        set_tipo.setHeader("TIPO");
        agregarComponente(set_tipo);
        
        Grid gri_modelo = new Grid();
        gri_modelo.setColumns(6);
        gri_modelo.getChildren().add(new Etiqueta("Ingreso Modelo"));
        gri_modelo.getChildren().add(tmodelo);
        Boton bot_modelo = new Boton();
        bot_modelo.setValue("Guardar");
        bot_modelo.setIcon("ui-icon-disk");
        bot_modelo.setMetodo("insModelo");
        bar_botones.agregarBoton(bot_modelo);
        Boton bot_modelox = new Boton();
        bot_modelox.setValue("Eliminar");
        bot_modelox.setIcon("ui-icon-closethick");
        bot_modelox.setMetodo("endMarca");
        bar_botones.agregarBoton(bot_modelox);
        gri_modelo.getChildren().add(bot_modelo);
        gri_modelo.getChildren().add(bot_modelox);
        set_modelo.setId("set_modelo");
        set_modelo.getTab_seleccion().setConexion(con_sql);
        set_modelo.setSeleccionTabla("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODELO' and LIS_ESTADO = 1 order by modelo", "LIS_NOMBRE");
        set_modelo.getTab_seleccion().getColumna("MODELO").setFiltro(true);
        set_modelo.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_modelo.getTab_seleccion().setRows(11);
        set_modelo.setWidth("36%"); //siempre en porcentajes  ancho
        set_modelo.setRadio();
        set_modelo.getGri_cuerpo().setHeader(gri_modelo);
        set_modelo.getBot_aceptar().setMetodo("abrirSeleccionTabla3");
        set_modelo.setHeader("MODELO");
        agregarComponente(set_modelo);
        
        Grid gri_version = new Grid();
        gri_version.setColumns(6);
        gri_version.getChildren().add(new Etiqueta("Ingreso Versión"));
        gri_version.getChildren().add(tversion);
        Boton bot_version = new Boton();
        bot_version.setValue("Guardar");
        bot_version.setIcon("ui-icon-disk");
        bot_version.setMetodo("insVersion");
        bar_botones.agregarBoton(bot_version);
        Boton bot_versionx = new Boton();
        bot_versionx.setValue("Eliminar");
        bot_versionx.setIcon("ui-icon-closethick");
        bot_versionx.setMetodo("endVersion");
        bar_botones.agregarBoton(bot_versionx);
        gri_version.getChildren().add(bot_version);
        gri_version.getChildren().add(bot_versionx);
        set_version.setId("set_version");
        set_version.getTab_seleccion().setConexion(con_sql);
        set_version.setSeleccionTabla("SELECT LIS_NOMBRE,LIS_NOMBRE as VERSION FROM MVLISTA WHERE TAB_CODIGO = 'VERSI' and LIS_ESTADO = 1 order by version", "LIS_NOMBRE");
        set_version.getTab_seleccion().getColumna("VERSION").setFiltro(true);
        set_version.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_version.getTab_seleccion().setRows(11);
        set_version.setWidth("36%"); //siempre en porcentajes  ancho
        set_version.setRadio();
        set_version.getGri_cuerpo().setHeader(gri_modelo);
//        set_version.getBot_aceptar().setMetodo("BusVersion");
        set_version.setHeader("VERSIÓN");
        agregarComponente(set_version);
    }

    public void abrirSeleccionTabla() {
        set_marca.dibujar();
    }
       
    public void abrirSeleccionTabla1() {
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
            set_tipo.dibujar();
            set_tipo.getTab_seleccion().setSql("SELECT LIS_NOMBRE,LIS_NOMBRE as TIPO FROM MVLISTA WHERE TAB_CODIGO = 'TIPO' and LIS_ESTADO = 1 and DEPENDENCI = '"+set_marca.getValorSeleccionado()+"'");
            set_tipo.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
        }
    }
        
    public void abrirSeleccionTabla2() {
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            set_modelo.dibujar();
            set_modelo.getTab_seleccion().setSql("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODELO' and LIS_ESTADO = 1 and DEPENDENCI = '"+set_tipo.getValorSeleccionado()+"'");
            set_modelo.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
        }
    }
    
    public void abrirSeleccionTabla3(){
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
            set_version.dibujar();
            set_version.getTab_seleccion().setSql("SELECT LIS_NOMBRE,LIS_NOMBRE as VERSION FROM MVLISTA WHERE TAB_CODIGO = 'VERSI' and LIS_ESTADO = 1 and DEPENDENCI = '"+set_modelo.getValorSeleccionado()+"'");
            set_version.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
        }
    }
    
    public void insMarca(){
        
    }
    
    public void endMarca(){
        
    }
    
    public void insTipo(){
        
    }
    
    public void endTipo(){
        
    }
    
    public void insModelo(){
        
    }
    
    public void endModelo(){
        
    }
    
    public void insVersion(){
        
    }
    
    public void endVersion(){
        
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

    public SeleccionTabla getSet_marca() {
        return set_marca;
    }

    public void setSet_marca(SeleccionTabla set_marca) {
        this.set_marca = set_marca;
    }

    public SeleccionTabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(SeleccionTabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public SeleccionTabla getSet_modelo() {
        return set_modelo;
    }

    public void setSet_modelo(SeleccionTabla set_modelo) {
        this.set_modelo = set_modelo;
    }
    
}
