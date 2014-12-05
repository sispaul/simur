/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
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
    
    private Texto tconductor = new Texto();
    private Texto tdependencia = new Texto();
    private Texto tmotor = new Texto();
    private Texto tchasis = new Texto();
    private Texto tplaca = new Texto();
    private Texto tcodigo = new Texto();
    private Texto tanio = new Texto();
    private Texto tKM = new Texto();
    private Texto tfecha = new Texto();
    
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
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
   private Programas aprogramas = (Programas) utilitario.instanciarEJB(Programas.class);
   
    public pre_mantenimiento_vehiculo() {

        tconductor.setId("tconductor");
        tdependencia.setId("tdependencia");
        tmotor.setId("tmotor");
        tchasis.setId("tchasis");
        tplaca.setId("tplaca");
        tcodigo.setId("tcodigo");
        tanio.setId("tanio");
        tKM.setId("tKM");
        tfecha.setId("tfecha");
        
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
                "c.MSC_SECUENCIAL,\n" +
                "v.MVE_PLACA, \n" +
                "v.MVE_MARCA, \n" +
                "v.MVE_MODELO, \n" +
                "v.MVE_COLOR, \n" +
                "v.MVE_ANO, \n" +
                "v.MVE_ESTADO_REGISTRO,\n" +
                "v.MVE_SECUENCIAL\n" +
                "FROM MVVEHICULO AS v  \n" +
                "INNER JOIN dbo.MVCABSOLICITUD c ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL \n" +
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
        gri_busca.getChildren().add(new Etiqueta(" TIPO DE MANTENIMIENTO :"));
        gri_busca.getChildren().add(cmb_mantenimiento);
        gri_busca.getChildren().add(new Etiqueta(" NUEVO REG. :"));
        gri_busca.getChildren().add(bot_busca);
        pan_panel.getChildren().add(gri_busca);
        
        //Botones
        Panel pan_matriz = new Panel();
        pan_matriz.setId("pan_matriz");
        pan_matriz.setStyle("width: 989px;");
        pan_matriz.setHeader("INFORMACIÓN DE AUTOMOTOR/MAQUINARIA");
        
        Panel pan_panel1 = new Panel();
        pan_panel1.setId("pan_panel1");
        pan_panel1.setStyle("width:500px;");

        Grid gri_info = new Grid();
        gri_info.setColumns(4);
        tfecha.setSize(12);
        gri_info.getChildren().add(new Etiqueta(" ULT. REVISIÓN :"));
        gri_info.getChildren().add(tfecha);
        gri_info.getChildren().add(new Etiqueta(" KM ACTUAL :"));
        gri_info.getChildren().add(tKM);
        pan_panel1.getChildren().add(gri_info);
        
        Panel pan_panel2 = new Panel();
        pan_panel2.setId("pan_panel2");
        pan_panel2.setStyle("width:970px;");

        Grid gri_info1 = new Grid();
        gri_info1.setColumns(6);
        tconductor.setSize(35);
        tdependencia.setSize(35);
        tplaca.setSize(9);
        tcodigo.setSize(15);
        tanio.setSize(9);
        tKM.setSize(12);
        gri_info1.getChildren().add(new Etiqueta(" CONDUCTOR :"));
        gri_info1.getChildren().add(tconductor);
        gri_info1.getChildren().add(new Etiqueta(" DEPENDENCIA :"));
        gri_info1.getChildren().add(tdependencia);
        gri_info1.getChildren().add(new Etiqueta(" PLACA :"));
        gri_info1.getChildren().add(tplaca);
        gri_info1.getChildren().add(new Etiqueta(" CODIGO :"));
        gri_info1.getChildren().add(tcodigo);
        gri_info1.getChildren().add(new Etiqueta(" MOTOR :"));
        gri_info1.getChildren().add(tmotor);
        gri_info1.getChildren().add(new Etiqueta(" CHASIS :"));
        gri_info1.getChildren().add(tchasis);
        gri_info1.getChildren().add(new Etiqueta(" AÑO :"));
        gri_info1.getChildren().add(tanio);
        pan_panel2.getChildren().add(gri_info1);
        pan_matriz.getChildren().add(pan_panel1);
        pan_matriz.getChildren().add(pan_panel2);
        Division div = new Division();
        div.dividir2(pan_panel,pan_matriz, "28%", "V");
        Division div1 = new Division();
        div1.dividir2(div,pan_opcion, "32%", "H");
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
    
    public void filtrarSolicitud (SelectEvent evt){
        limpiar();
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            dibujarMantenimiento();
            utilitario.addUpdate("aut_busca,pan_opcion");
            datos_vehiculo();
        }     
    }
    
    public void aceplistado(){
        if(set_invehiculo.getValorSeleccionado()!=null && set_invehiculo.getValorSeleccionado().toString().isEmpty()==false){
         TablaGenerica tab_dato =aCombustible.get_ExDatosSoli(set_invehiculo.getValorSeleccionado());
         if (!tab_dato.isEmpty()) {
             
             tconductor.setValue(tab_dato.getValor("MVE_CONDUCTOR"));
             tmotor.setValue(tab_dato.getValor("MVE_MOTOR"));
             tchasis.setValue(tab_dato.getValor("MVE_CHASIS"));
             tplaca.setValue(tab_dato.getValor("MVE_PLACA"));
             tanio.setValue(tab_dato.getValor("MVE_ANO"));
             tKM.setValue(tab_dato.getValor("MVE_KILOMETRAJE"));
             tfecha.setValue(tab_dato.getValor("FECHA"));
             
             utilitario.addUpdate("tconductor");
             utilitario.addUpdate("tmotor");
             utilitario.addUpdate("tchasis");
             utilitario.addUpdate("tplaca");
             utilitario.addUpdate("tanio");
             utilitario.addUpdate("tKM");
             utilitario.addUpdate("tfecha");
             set_invehiculo.cerrar();
             tab_mante.insertar();
             tab_mante.setValor("MVE_SECUENCIAL", set_invehiculo.getValorSeleccionado());
             utilitario.addUpdate("tab_mante");
             estado();
         }else{
             utilitario.agregarMensaje("Datos no Encontrados", "");
         }
        }else{
            utilitario.agregarMensaje("Seleccionar Al menos un Registro", "");
        }
    }
    
    public void datos_vehiculo(){
        TablaGenerica tab_dato =aCombustible.get_ExDatosCom(aut_busca.getValor());
         if (!tab_dato.isEmpty()) {
             tconductor.setValue(tab_dato.getValor("MSC_CONDUCTOR"));
             tmotor.setValue(tab_dato.getValor("MVE_MOTOR"));
             tchasis.setValue(tab_dato.getValor("MVE_CHASIS"));
             tplaca.setValue(tab_dato.getValor("MVE_PLACA"));
             tanio.setValue(tab_dato.getValor("MVE_ANO"));

             utilitario.addUpdate("tconductor");
             utilitario.addUpdate("tmotor");
             utilitario.addUpdate("tchasis");
             utilitario.addUpdate("tplaca");
             utilitario.addUpdate("tanio");

         }else{
             utilitario.agregarMensaje("Datos no Encontrados", "");
         }
    }
    
    public void dibujarMantenimiento(){

        tab_mante.setId("tab_mante");
        tab_mante.setConexion(con_sql);
        tab_mante.setTabla("mvcabsolicitud", "msc_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_mante.setCondicion("msc_secuencial=-1");
        } else {
            tab_mante.setCondicion("msc_secuencial=" + aut_busca.getValor());
        }
        tab_mante.getColumna("msc_conductor").setValorDefecto(tconductor.getValue()+"");
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
        tab_mante.getColumna("MVE_SECUENCIAL").setVisible(false);
        tab_mante.getColumna("msc_conductor").setVisible(false);
        tab_mante.getColumna("msc_estado_tramite").setLectura(true);
        tab_mante.getColumna("msc_proveedor").setMetodoChange("proveedor");
        tab_mante.getColumna("msc_autorizado").setMetodoChange("autoriza");
        tab_mante.setTipoFormulario(true);
        tab_mante.getGrid().setColumns(2);
        tab_mante.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_mante);
        Grupo gru = new Grupo();
        gru.getChildren().add(tpa);
        pan_opcion.getChildren().add(gru);
    }
    
    public void estado(){
        tab_mante.setValor("msc_estado_tramite","MANTENIMIENTO");
        tab_mante.setValor("msc_tiposol", cmb_mantenimiento.getValue()+"");
        utilitario.addUpdate("tab_mante");
    }
    
    public void proveedor(){
        set_proveedor.dibujar();
    }
    public void acepProveedor (){
        if(set_proveedor.getValorSeleccionado()!= null && set_proveedor.getValorSeleccionado().toString().isEmpty()==false){
            TablaGenerica tab_dato =aprogramas.proveedor1(Integer.parseInt(set_proveedor.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                tab_mante.setValor("msc_proveedor", tab_dato.getValor("titular")+"");
                utilitario.addUpdate("tab_mante");
                set_proveedor.cerrar();
            }
        }else{
            utilitario.agregarMensaje("Seleccione un Registro", "");
        }
    }
    public void autoriza(){
        set_autorizador.dibujar();
    }
    
    public void acepAutoriza(){
        if(set_autorizador.getValorSeleccionado()!= null && set_autorizador.getValorSeleccionado().toString().isEmpty()==false){
            TablaGenerica tab_dato =aprogramas.empleadoCod(set_autorizador.getValorSeleccionado());
            if (!tab_dato.isEmpty()) {
                tab_mante.setValor("MSC_AUTORIZADO", tab_dato.getValor("nombres")+"");
                utilitario.addUpdate("tab_mante");
                set_autorizador.cerrar();
            }
        }else{
            utilitario.agregarMensaje("Seleccione un Registro", "");
        }
    }
    
    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        tab_mante.guardar();
        con_sql.guardarPantalla();
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
