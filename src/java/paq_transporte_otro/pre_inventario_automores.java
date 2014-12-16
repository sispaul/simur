/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
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
public class pre_inventario_automores extends Pantalla{
    
    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    
    private Tabla set_marca = new Tabla();
    private Tabla set_tipo = new Tabla();
    private Tabla set_modelo = new Tabla();
    private Tabla set_version = new Tabla();
    
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
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_inventario_automores() {
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        Boton bot_marca = new Boton();
        bot_marca.setValue("INSERTAR MARCA");
        bot_marca.setIcon("ui-icon-comment");
        bot_marca.setMetodo("ing_marcas");
        bar_botones.agregarBoton(bot_marca);
        
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
        dia_dialogo.setTitle("INGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acep_marcas");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);
        
        set_marca.setId("set_marca");
        set_marca.setConexion(con_sql);
        set_marca.setSql("SELECT MVMARCA_ID, MVMARCA_DESCRIPCION FROM MVMARCA order by MVMARCA_DESCRIPCION");
        set_marca.getColumna("MVMARCA_DESCRIPCION").setFiltro(true);
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
        
    }
    
    public void ing_marcas(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_o.getChildren().add(set_marca);
        dia_dialogo.setDialogo(grid_o);
        set_marca.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void insMarca(){
            TablaGenerica tab_dato =aCombustible.get_DuplicaDato(tmarca.getValue()+"");
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Marca ya se Encuentra Registrada", "");
            }else{
                if(tmarca.getValue()!= null && tmarca.toString().isEmpty()==false){
                aCombustible.getParametrom(tmarca.getValue()+"",utilitario.getVariable("NICK"));
                tmarca.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Marca");
                set_marca.actualizar();
//                cargarMarca();
            }
        }
    }
    
    public void endMarca(){
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
                aCombustible.deleteMarcas(Integer.parseInt(set_marca.getValorSeleccionado()));
                utilitario.agregarMensaje("Registro eliminado", "Marca");
                set_marca.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
//    public void cargarMarca(){
//         tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE AS MARCA FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
//        utilitario.addUpdateTabla(tab_tabla,"MVE_MARCA","");//actualiza solo componentes
//    }
    
    public void acep_marcas(){
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
            dia_dialogot.Limpiar();
            dia_dialogot.setDialogo(gridt);
            grid_t.getChildren().add(set_tipo);
            set_tipo.setId("set_tipo");
            set_tipo.setConexion(con_sql);
            set_tipo.setSql("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM dbo.MVTIPO where MVMARCA_ID ="+set_marca.getValorSeleccionado()+" order by MVTIPO_DESCRIPCION");
            set_tipo.getColumna("MVTIPO_DESCRIPCION").setFiltro(true);
            set_tipo.setTipoSeleccion(false);
            set_tipo.setRows(10);
            set_tipo.dibujar();
            dia_dialogot.setDialogo(grid_t);
            dia_dialogot.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insTipo(){
        TablaGenerica tab_dato1 =aCombustible.get_DuplicarDato(ttipo.getValue()+"",Integer.parseInt(set_marca.getValorSeleccionado()));
        if (!tab_dato1.isEmpty()) {
            utilitario.agregarMensaje("Tipo ya se Encuentra Registrado", "");
        }else{
            if(ttipo.getValue()!= null && ttipo.toString().isEmpty()==false){
                aCombustible.getParametrot(ttipo.getValue()+"", utilitario.getVariable("NICK"), Integer.parseInt(set_marca.getValorSeleccionado()));
                ttipo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Tipo");
                set_tipo.actualizar();
            }
        }
    }
        
    public void endTipo(){
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteTipos(Integer.parseInt(set_tipo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Tipo");
            set_tipo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void acepta_tipo(){
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogom.Limpiar();
            dia_dialogom.setDialogo(gridm);
            grid_m.getChildren().add(set_modelo);
            set_modelo.setId("set_modelo");
            set_modelo.setConexion(con_sql);
            set_modelo.setSql("SELECT MVMODELO_ID, MVMODELO_DESCRIPCION FROM MVMODELO where MVTIPO_ID ="+set_modelo.getValorSeleccionado()+"  order by MVMODELO_DESCRIPCION");
            set_modelo.getColumna("MVMODELO_DESCRIPCION").setFiltro(true);
            set_modelo.setTipoSeleccion(false);
            set_modelo.setRows(10);
            set_modelo.dibujar();dia_dialogom.setDialogo(grid_m);
            dia_dialogom.dibujar();
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insModelo(){
        TablaGenerica tab_dato2 =aCombustible.get_DuplicamDato(tmodelo.getValue()+"", Integer.parseInt(set_tipo.getValorSeleccionado()));
        if (!tab_dato2.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        }else{
            if(tmodelo.getValue()!= null && tmodelo.toString().isEmpty()==false){
                aCombustible.getParametros(tmodelo.getValue()+"","MODEL",tab_dato1.getValor("LIS_NOMBRE"),utilitario.getVariable("NICK"));
                tmodelo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Modelo");
                set_modelo.actualizar();
            }
        }
    }
    
    public void endModelo(){
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
                        aCombustible.deleteParam(tab_dato2.getValor("LIS_NOMBRE"), mensaje, tab_dato1.getValor("LIS_NOMBRE"));
                        utilitario.agregarMensaje("Registro eliminado", "Modelo");
                        set_modelo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void acepta_modelo(){
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogov.Limpiar();
            dia_dialogov.setDialogo(gridv);
            grid_v.getChildren().add(set_version);
            set_version.setId("set_version");
            set_version.setConexion(con_sql);
            set_version.setSql("SELECT MVERSION_ID,MVERSION_DESCRIPCION FROM MVVERSION where MVMODELO_ID ="+set_modelo.getValorSeleccionado()+" order by MVERSION_DESCRIPCION");
            set_version.getColumna("MVERSION_DESCRIPCION").setFiltro(true);
            set_version.setTipoSeleccion(false);
            set_version.setRows(10);
            set_version.dibujar();
            dia_dialogov.setDialogo(grid_v);
            dia_dialogov.dibujar();
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insVersion(){
            TablaGenerica tab_dato3 =aCombustible.get_DuplicavDato(tversion.getValue()+"", Integer.parseInt(set_modelo.getValorSeleccionado()));
            if (!tab_dato3.isEmpty()) {
                utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
            }else{
                if(tversion.getValue()!= null && tversion.toString().isEmpty()==false){
                    aCombustible.getParametros(tversion.getValue()+"","VERSI",tab_dato2.getValor("LIS_NOMBRE"),utilitario.getVariable("NICK"));
                    tversion.limpiar();
                    utilitario.agregarMensaje("Registro Guardado", "Versión");
                    set_version.actualizar();
                }
            }
    }
    
    public void endVersion(){
        if (set_version.getValorSeleccionado() != null && set_version.getValorSeleccionado().isEmpty() == false) {
                            aCombustible.deleteParam(tab_dato3.getValor("LIS_NOMBRE"), mensaje, tab_dato2.getValor("LIS_NOMBRE"));
                            utilitario.agregarMensaje("Registro eliminado", "Versión");
                            set_version.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
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

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
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
    
}
