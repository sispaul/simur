/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_mantenimiento_vehiculo extends Pantalla{
 //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //cadena de conexion a base de datos
    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    
    //identificador para tablas
    private Tabla tab_vehiculo = new Tabla();
    private Tabla tab_mante = new  Tabla();
    private Tabla set_solvehiculo = new Tabla();
    //obejto para seleccion
    private SeleccionTabla set_invehiculo = new SeleccionTabla();
    private SeleccionTabla set_proveedor = new SeleccionTabla();
    private SeleccionTabla set_autorizador = new SeleccionTabla();
    
    //seleccion tipo de mantenimiento
    private Combo cmb_mantenimiento = new Combo();
    
    //variables de busqueda
    String search,nuevo;
    //calendario
    private Calendario cal_fecha = new Calendario();
    private Calendario cal_utlrevision = new Calendario();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //dialogo de seleccion
    private Dialogo dia_dialogov = new Dialogo();
    private Grid grid_v = new Grid();
    private Grid gridv = new Grid();
    
    public pre_mantenimiento_vehiculo() {
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT\n" +
                "v.MVE_SECUENCIAL,\n" +
                "v.MVE_PLACA,\n" +
                "v.MVE_MARCA,\n" +
                "v.MVE_MODELO,\n" +
                "v.MVE_COLOR,\n" +
                "v.MVE_ANO,\n" +
                "v.MVE_ESTADO_REGISTRO,\n" +
                "c.MSC_SECUENCIAL\n" +
                "FROM MVVEHICULO AS v \n" +
                " INNER JOIN dbo.MVCABSOLICITUD c ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "WHERE v.MVE_ESTADO_REGISTRO = 'activo'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        //Ingreso y busqueda 
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 310px;");
        pan_panel.setHeader("SOLICITUD DE MANTENIMIENTO");
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        cmb_mantenimiento.setId("cmb_mantenimiento");
        List lista = new ArrayList();
        Object fila1[] = {
            "PREVENTIVO", "PREVENTIVO"
        };
        Object fila2[] = {
            "CORRECTIVO", "CORRECTIVO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        cmb_mantenimiento.setCombo(lista);
        
        List list = new ArrayList();
        Object filas1[] = {
            "1", "NUEVO"
        };
        Object filas2[] = {
            "2", "BUSCAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Nuevo ");
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("new_regis");
        nuevo = bot_busca.getValue()+"";
        Boton bot_recupera = new Boton();
        bot_recupera.setValue("Buscar ");
        bot_recupera.setIcon("ui-icon-search");
        bot_recupera.setMetodo("new_reg");
        search = bot_recupera.getValue()+"";
        gri_busca.getChildren().add(new Etiqueta(" TIPO DE MANTENIMIENTO :"));
        gri_busca.getChildren().add(cmb_mantenimiento);
        gri_busca.getChildren().add(new Etiqueta(" NUEVO REG. :"));
        gri_busca.getChildren().add(bot_busca);
        gri_busca.getChildren().add(new Etiqueta(" BUSCAR :"));
        gri_busca.getChildren().add(bot_recupera);
        pan_panel.getChildren().add(gri_busca);
        
        //Botones
        Panel pan_panel1 = new Panel();
        pan_panel1.setId("pan_panel1");
        pan_panel1.setStyle("width:1000px;");
        pan_panel1.setHeader("INFORMACIÓN DE AUTOMOTOR/MAQUINARIA");
        
        Grid gri_info = new Grid();
        gri_info.setColumns(2);
        cal_fecha.setSize(12);
        cal_utlrevision.setSize(12);

        gri_info.getChildren().add(new Etiqueta(" ULT. REVISIÓN :"));
        gri_info.getChildren().add(cal_utlrevision);
        gri_info.getChildren().add(new Etiqueta(" SIG. REVISIÓN :"));
        gri_info.getChildren().add(cal_fecha);
        pan_panel1.getChildren().add(gri_info);
        
        Division div = new Division();
        div.dividir2(pan_panel,pan_panel1, "28%", "V");
        Division div1 = new Division();
        div1.dividir2(div,pan_opcion, "23%", "H");
        agregarComponente(div1);
        dibujarMantenimiento(); 
        
        set_proveedor.setId("set_proveedor");
        set_proveedor.getTab_seleccion().setConexion(con_postgres);
        set_proveedor.setSeleccionTabla("SELECT ide_proveedor,ruc,titular,domicilio,actividad,telefono1 FROM tes_proveedores", "ide_proveedor");
        set_proveedor.getTab_seleccion().getColumna("titular").setFiltro(true);
        set_proveedor.getTab_seleccion().setRows(10);
        set_proveedor.setRadio();
        set_proveedor.getBot_aceptar().setMetodo("acepProveedor");
        set_proveedor.setHeader("SELECCIONAR PROVEEDOR");
        agregarComponente(set_proveedor);
        
        set_autorizador.setId("set_autorizador");
        set_autorizador.getTab_seleccion().setConexion(con_postgres);
        set_autorizador.setSeleccionTabla("SELECT cod_empleado,cedula_pass,nombres FROM srh_empleado where estado =1 order by nombres", "cod_empleado");
        set_autorizador.getTab_seleccion().getColumna("nombres").setFiltro(true);
        set_autorizador.getTab_seleccion().setRows(10);
        set_autorizador.setRadio();
        set_autorizador.getBot_aceptar().setMetodo("acepAutoriza");
        set_autorizador.setHeader("SELECCIONAR AUTORIZADOR");
        agregarComponente(set_autorizador);
        
        set_invehiculo.setId("set_invehiculo");
        set_invehiculo.getTab_seleccion().setConexion(con_sql);
        set_invehiculo.setSeleccionTabla("SELECT MVE_SECUENCIAL,MVE_PLACA,MVE_MARCA,MVE_MODELO,MVE_COLOR,MVE_ANO\n" +
                "FROM dbo.MVVEHICULO WHERE MVE_ESTADO_REGISTRO = 'activo' order by MVE_SECUENCIAL", "MVE_SECUENCIAL");
        set_invehiculo.getTab_seleccion().getColumna("MVE_PLACA").setFiltro(true);
        set_invehiculo.getTab_seleccion().setRows(10);
        set_invehiculo.setRadio();
        set_invehiculo.getBot_aceptar().setMetodo("aceplistado");
        set_invehiculo.setHeader("SELECCIONE AUTOMOTOR/MAQUINARIA");
        agregarComponente(set_invehiculo);
        
        dia_dialogov.setId("dia_dialogov");
        dia_dialogov.setTitle("SOLICITUD DE MANTENIMIENTO"); //titulo
        dia_dialogov.setWidth("45%"); //siempre en porcentajes  ancho
        dia_dialogov.setHeight("40%");//siempre porcentaje   alto
        dia_dialogov.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogov.getBot_aceptar().setMetodo("aceptCargas");
        grid_v.setColumns(4);
        agregarComponente(dia_dialogov);
        
        set_solvehiculo.setId("set_solvehiculo");
        set_solvehiculo.setConexion(con_sql);
        set_solvehiculo.setSql("SELECT c.MVE_SECUENCIAL,c.MVE_PLACA,c.MVE_MARCA,c.MVE_MODELO,c.MVE_COLOR,c.MVE_ANO\n" +
                "FROM MVVEHICULO c INNER JOIN MVCABSOLICITUD e ON e.MVE_SECUENCIAL = c.MVE_SECUENCIAL\n" +
                "WHERE c.MVE_ESTADO_REGISTRO = 'activo' and e.MSC_ESTADO_TRAMITE = 'mantenimiento'\n" +
                "ORDER BY c.MVE_SECUENCIAL ASC");
        set_solvehiculo.getColumna("MVE_PLACA").setFiltro(true);
        set_solvehiculo.setTipoSeleccion(false);
        set_solvehiculo.setRows(10);
        set_solvehiculo.dibujar();
    }
    
            //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void new_regis(){
        if(cmb_mantenimiento.getValue()!=null){
            set_invehiculo.dibujar();
        }else{
            utilitario.agregarMensaje("SELECCIONAR TIPO DE MANTENIMIENTO", "");
        }
    }
    
    public void new_reg(){
        if(cmb_mantenimiento.getValue()!=null){
            dia_dialogov.Limpiar();
            dia_dialogov.setDialogo(gridv);
            grid_v.getChildren().add(set_solvehiculo);
            dia_dialogov.setDialogo(grid_v);
            set_solvehiculo.dibujar();
            dia_dialogov.dibujar();
        }else{
            utilitario.agregarMensaje("SELECCIONAR TIPO DE MANTENIMIENTO", "");
        }
    }
    
    public void filtrarSolicitud (){
        limpiar();
        if(set_invehiculo.getSeleccionados()!=null){
            
        }else if (set_solvehiculo.getSeleccionados()!=null){
            
        }else{
            utilitario.agregarMensaje("Seleccionar", "Tipo de Mantenimiento");
        }
//        if (set_registros.getValorSeleccionado() != null) {
//            aut_busca.setValor(set_registros.getValorSeleccionado());
//            dia_dialogore.cerrar();
//            dibujardocumento();
//            utilitario.addUpdate("aut_busca,pan_opcion");
//            completa_dato();
//        } else {
//            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
//        }
    }
    
    public void dibujarMantenimiento(){
        tab_vehiculo.setId("tab_vehiculo");
        tab_vehiculo.setConexion(con_sql);
        tab_vehiculo.setTabla("mvvehiculo", "mve_secuencial", 1);
//                /*Filtro estatico para los datos a mostrar*/
//        if (aut_busca.getValue() == null) {
//            tab_vehiculo.setCondicion("mve_secuencial=-1");
//        } else {
//            tab_vehiculo.setCondicion("mve_secuencial=" + aut_busca.getValor());
//        }
        tab_vehiculo.getColumna("MVE_SECUENCIAL").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPO").setVisible(false);
        tab_vehiculo.getColumna("MVE_ESTADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_DURACION_LLANTA").setVisible(false);
        tab_vehiculo.getColumna("MVE_CAPACIDAD_TANQUE_COMBUSTIBLE").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPO_COMBUSTIBLE ").setVisible(false);
        tab_vehiculo.getColumna("MVE_FECHA_BORRADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGINBORRADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
        tab_vehiculo.getColumna("MVE_FECHAACTUALI").setVisible(false);
        tab_vehiculo.getColumna("MVE_FECHAINGRESO").setVisible(false);
        tab_vehiculo.getColumna("MVE_ASIGNADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPOCODIGO").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGINACTUALI").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPOMEDICION").setVisible(false);
        tab_vehiculo.getColumna("MVE_OBSERVACIONES").setVisible(false);
        tab_vehiculo.getColumna("MVE_NUMIMR").setVisible(false);
        tab_vehiculo.getColumna("MVE_TAMANIO").setVisible(false);
        tab_vehiculo.getColumna("MVE_VERSION").setVisible(false);
        tab_vehiculo.agregarRelacion(tab_mante);
        tab_vehiculo.setTipoFormulario(true);
        tab_vehiculo.getGrid().setColumns(4);
        tab_vehiculo.dibujar();
        PanelTabla tve = new PanelTabla();
        tve.setPanelTabla(tab_vehiculo);
        
        tab_mante.setId("tab_mante");
        tab_mante.setConexion(con_sql);
        tab_mante.setTabla("mvcabsolicitud", "msc_secuencial", 2);
        tab_mante.getColumna("msc_conductor").setValorDefecto(tab_vehiculo.getValor("mve_conductor")+"");
        tab_mante.getColumna("msc_fecha").setValorDefecto(utilitario.getFechaHoraActual());
        tab_mante.getColumna("msc_tipo_mantenimiento").setVisible(false);
        tab_mante.getColumna("msc_solicitud").setVisible(false);
        tab_mante.getColumna("msc_loginingreso").setVisible(false);
        tab_mante.getColumna("msc_fechaingreso").setVisible(false);
        tab_mante.getColumna("msc_loginactuali").setVisible(false);
        tab_mante.getColumna("msc_fechaactuali").setVisible(false);
        tab_mante.getColumna("msc_loginborrado").setVisible(false);
        tab_mante.getColumna("msc_fecha_borrado").setVisible(false);
        tab_mante.getColumna("msc_estado_registro").setVisible(false);
        tab_mante.getColumna("msc_tiposol").setVisible(false);
        tab_mante.getColumna("msc_monto").setVisible(false);
        tab_mante.getColumna("msc_conductor").setVisible(false);
        tab_mante.getColumna("msc_estado_tramite").setLectura(true);
        tab_mante.getColumna("msc_proveedor").setMetodoChange("proveedor");
        tab_mante.getColumna("msc_autorizado").setMetodoChange("autoriza");
        tab_mante.setTipoFormulario(true);
        tab_mante.getGrid().setColumns(2);
        tab_mante.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_mante);
        
        Division div1 = new Division();
        div1.dividir2(tve,tpa, "28%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div1);
        pan_opcion.getChildren().add(gru);
    }
    
    public void estado(){
        tab_mante.setValor("msc_estado_tramite","MANTENIMIENTO");
        tab_mante.setValor("msc_tiposol", cmb_mantenimiento.getValue()+"");
        utilitario.addUpdate("tab_mante");
    }
    
    public void aceplistado(){
        
    }
    
    public void aceptCargas(){
        
    }
    
    public void proveedor(){
        set_proveedor.dibujar();
    }
    
    public void autoriza(){
        set_autorizador.dibujar();
    }
    
    public void acepProveedor(){
        
    }
    
    public void acepAutoriza(){
        
    }
    
    @Override
    public void insertar() {
        tab_mante.insertar();
        estado();
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

    public Tabla getTab_mante() {
        return tab_mante;
    }

    public void setTab_mante(Tabla tab_mante) {
        this.tab_mante = tab_mante;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getTab_vehiculo() {
        return tab_vehiculo;
    }

    public void setTab_vehiculo(Tabla tab_vehiculo) {
        this.tab_vehiculo = tab_vehiculo;
    }

    public SeleccionTabla getSet_proveedor() {
        return set_proveedor;
    }

    public void setSet_proveedor(SeleccionTabla set_proveedor) {
        this.set_proveedor = set_proveedor;
    }

    public SeleccionTabla getSet_autorizador() {
        return set_autorizador;
    }

    public void setSet_autorizador(SeleccionTabla set_autorizador) {
        this.set_autorizador = set_autorizador;
    }

    public SeleccionTabla getSet_invehiculo() {
        return set_invehiculo;
    }

    public void setSet_invehiculo(SeleccionTabla set_invehiculo) {
        this.set_invehiculo = set_invehiculo;
    }

    public Tabla getSet_solvehiculo() {
        return set_solvehiculo;
    }

    public void setSet_solvehiculo(Tabla set_solvehiculo) {
        this.set_solvehiculo = set_solvehiculo;
    }
}
