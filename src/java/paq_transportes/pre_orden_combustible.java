/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.ProvisionCombustible;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_orden_combustible extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //Declaración de Tablas
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_calculo = new Tabla();
    private SeleccionTabla set_orden = new SeleccionTabla();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Busca de comprobante
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Cuadros para texto, busqueda reportes
    private Texto tex_busqueda = new Texto();
    
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    @EJB
    private ProvisionCombustible pCombustible = (ProvisionCombustible) utilitario.instanciarEJB(ProvisionCombustible.class);
    
    public pre_orden_combustible() {
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar Orden");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("BusOrdenC");
        bar_botones.agregarBoton(bot_busca);
        
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT IDE_ORDEN_CONSUMO,NUMERO_ORDEN,FECHA_ORDEN,PLACA_VEHICULO\n" +
                "FROM MVORDEN_CONSUMO");
        aut_busca.setMetodoChange("buscarOrden");
        aut_busca.setSize(70);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Orden:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("CONSUMO DE COMBUSTIBLE GADMUR");
        agregarComponente(pan_opcion);
        ordenConsumo();//dibuja orden
        
        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        tex_busqueda.setSize(45);
        gri_busca.getChildren().add(new Etiqueta("Buscar Solicitud:"));
        gri_busca.getChildren().add(tex_busqueda);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarOrden");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_orden.setId("set_orden");
        set_orden.getTab_seleccion().setConexion(con_sql);
        set_orden.setSeleccionTabla("SELECT IDE_ORDEN_CONSUMO,NUMERO_ORDEN,FECHA_ORDEN,PLACA_VEHICULO FROM MVORDEN_CONSUMO where IDE_ORDEN_CONSUMO=-1", "IDE_ORDEN_CONSUMO");
        set_orden.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_orden.getTab_seleccion().setRows(10);
        set_orden.setRadio();
        set_orden.getGri_cuerpo().setHeader(gri_busca);
        set_orden.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_orden.setHeader("BUSCAR N°. ORDEN PROVISIÓN DE COMBUSTIBLE");
        agregarComponente(set_orden);
    }
    
    
    public void BusOrdenC() {
        limpiarPanel();
            set_orden.dibujar();
            tex_busqueda.limpiar();
            set_orden.getTab_seleccion().limpiar();
    }
    
    public void buscarOrden() {
            if (tex_busqueda.getValue() != null && tex_busqueda.getValue().toString().isEmpty() == false) {
                set_orden.getTab_seleccion().setSql("SELECT IDE_ORDEN_CONSUMO,NUMERO_ORDEN,FECHA_ORDEN,PLACA_VEHICULO FROM MVORDEN_CONSUMO where NUMERO_ORDEN = '"+tex_busqueda.getValue()+"'");
                set_orden.getTab_seleccion().ejecutarSql();
            } else {
                utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
            }
    }
    
        //Dibuja la Pantalla
    public void aceptarBusqueda() {
        limpiar();
        limpiarPanel();
            if (set_orden.getValorSeleccionado() != null) {
                aut_busca.setValor(set_orden.getValorSeleccionado());
                set_orden.cerrar();
                dibujarOrden();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
            }
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
    
    public void ordenConsumo(){
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvorden_consumo", "ide_orden_consumo", 1);
                /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("ide_orden_consumo=-1");
        } else {
            tab_tabla.setCondicion("ide_orden_consumo=" + aut_busca.getValor());
        }
        tab_tabla.setHeader("ORDEN DE PROVISIÓN DE COMBUSTIBLE");
        tab_tabla.getColumna("placa_vehiculo").setMetodoChange("busVehiculo");
        tab_tabla.getColumna("conductor").setMetodoChange("buscaConductor");
        tab_tabla.getColumna("fecha_orden").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla.getColumna("AUTORIZA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("ci_conductor").setVisible(false);
        tab_tabla.getColumna("autoriza").setVisible(false);
        tab_tabla.getColumna("ide_orden_consumo").setVisible(false);
        tab_tabla.agregarRelacion(tab_calculo);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_tabla);
        Division div = new Division();
        div.dividir1(ptc);
        agregarComponente(div);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    public void dibujarOrden(){
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_sql);
        tab_tabla1.setTabla("mvorden_consumo", "ide_orden_consumo", 2);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla1.setCondicion("ide_orden_consumo=-1");
        } else {
            tab_tabla1.setCondicion("ide_orden_consumo=" + aut_busca.getValor());
        }
        tab_tabla1.setHeader("ORDEN DE PROVISIÓN DE COMBUSTIBLE");
        tab_tabla1.getColumna("placa_vehiculo").setMetodoChange("busVehiculo");
        tab_tabla1.getColumna("conductor").setMetodoChange("buscaConductor");
        tab_tabla1.getColumna("fecha_orden").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla1.getColumna("AUTORIZA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla1.getColumna("ci_conductor").setVisible(false);
        tab_tabla1.getColumna("autoriza").setVisible(false);
        tab_tabla1.getColumna("ide_orden_consumo").setVisible(false);
        tab_tabla1.agregarRelacion(tab_calculo);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla1);
        
        tab_calculo.setId("tab_calculo");
        tab_calculo.setConexion(con_sql);
        tab_calculo.setTabla("mvcalculo_consumo", "ide_calculo_consumo", 3);
        tab_calculo.setHeader("DATOS DE PROVISIÓN DE COMBUSTIBLE");        
//        tab_tipo.setId("tab_tipo");
//        tab_tipo.setConexion(con_sql);
//        tab_tipo.setSql("SELECT MVE_SECUENCIAL,MVE_PLACA,MVE_KILOMETRAJE,MVE_TIPO_COMBUSTIBLE,MVE_CAPACIDAD_TANQUE_COMBUSTIBLE \n" +
//                "FROM MVVEHICULO WHERE MVE_PLACA = '"+tab_tabla.getValor("placa_vehiculo")+"'");
//        tab_tipo.setCampoPrimaria("MVE_SECUENCIAL");
//        tab_tipo.setLectura(true);
//        tab_tipo.dibujar();
//        
//        tab_calculo.getColumna("ide_tipo_combustible").setValorDefecto(tab_tipo.getValor("MVE_TIPO_COMBUSTIBLE"));
        tab_calculo.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_calculo.getColumna("fecha_digitacion").setValorDefecto(utilitario.getFechaActual());
        tab_calculo.getColumna("hora_digitacion").setValorDefecto(utilitario.getFechaHoraActual());
        tab_calculo.getColumna("usu_digitacion").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_calculo.getColumna("fecha_digitacion").setVisible(false);
        tab_calculo.getColumna("hora_digitacion").setVisible(false);
        tab_calculo.getColumna("usu_digitacion").setVisible(false);
        tab_calculo.getColumna("ide_calculo_consumo").setVisible(false);
        tab_calculo.getColumna("ide_tipo_combustible").setMetodoChange("clean");
//        tab_calculo.getColumna("galones").setMetodoChange("valor");
        tab_calculo.getColumna("kilometraje").setMetodoChange("kilometraje");
        tab_calculo.getColumna("galones").setMetodoChange("galones");
        tab_calculo.setTipoFormulario(true);
        tab_calculo.getGrid().setColumns(4);
        tab_calculo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tab_calculo);
        
        Division div = new Division();
        div.dividir2(ptt,pte,"35%", "h");
        agregarComponente(div);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            limpiar();
            limpiarPanel();
            aut_busca.limpiar();
            utilitario.addUpdate("aut_busca");
            tab_tabla1.limpiar();
            ordenConsumo();
            tab_calculo.limpiar();
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_tabla.guardar()){
            if(tab_calculo.guardar()){
                con_sql.guardarPantalla();
            }
        }
        actuKilometrajes();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void actuKilometrajes(){
        if(tab_calculo.getValor("ide_calculo_consumo")!=null && tab_calculo.getValor("ide_calculo_consumo").toString().isEmpty() == false){
            pCombustible.ActKilometraje(tab_tabla1.getValor("placa_vehiculo"), Double.parseDouble(tab_calculo.getValor("kilometraje")));
        }
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public SeleccionTabla getSet_orden() {
        return set_orden;
    }

    public void setSet_orden(SeleccionTabla set_orden) {
        this.set_orden = set_orden;
    }

    public Tabla getTab_calculo() {
        return tab_calculo;
    }

    public void setTab_calculo(Tabla tab_calculo) {
        this.tab_calculo = tab_calculo;
    }
    
}
