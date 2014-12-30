/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.mergeDescuento;
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
    private Tabla tab_consulta = new Tabla();
    private Tabla set_desvehiculo = new Tabla();
    
    //obejto para seleccion
    private SeleccionTabla set_invehiculo = new SeleccionTabla();
    private SeleccionTabla set_proveedor = new SeleccionTabla();
    private SeleccionTabla set_autorizador = new SeleccionTabla();
    
    //seleccion tipo de mantenimiento
    private Combo cmb_mantenimiento = new Combo();
    private Combo cmb_impresion = new Combo();
    
    //variables de busqueda
    String search,nuevo;
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Dialogos
    private Dialogo dia_dialogod = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gridd = new Grid();
    
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    private Programas aprogramas = (Programas) utilitario.instanciarEJB(Programas.class);
   private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
   
   ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
   
    public pre_mantenimiento_vehiculo() {

        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
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
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setValue("Limpiar");
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpia_pa");
        bar_botones.agregarBoton(bot_limpiar);
        
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
        set_invehiculo.getBot_aceptar().setMetodo("des_historial");
        set_invehiculo.setHeader("SELECCIONE AUTOMOTOR/MAQUINARIA");
        agregarComponente(set_invehiculo);
        
        set_desvehiculo.setId("set_desvehiculo");
        set_desvehiculo.setConexion(con_sql);
        set_desvehiculo.setSql("");
        set_desvehiculo.setRows(10);
        set_desvehiculo.dibujar();
        
        dia_dialogod.setId("dia_dialogod");
        dia_dialogod.setTitle("SELECCIONE OPCIÓN"); //titulo
        dia_dialogod.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogod.setHeight("10%");//siempre porcentaje   alto
        dia_dialogod.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogod.getBot_aceptar().setMetodo("abrirReporte");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogod);
        
                /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);
        
    }
    
        //permite limpiar el formulario sin guardar o mantener algun dato
    public void limpia_pa(){
        if(tab_mante.getValor("msc_secuencial")!=null){
            limpiar();
            tab_mante.limpiar();
            tab_mante.getChildren().clear();
//            utilitario.agregarMensaje("Limpia Formulario", "");
        }else{
            limpiar();
            utilitario.agregarMensaje("Limpia Formulario", "Registro Nuevo");
            tab_mante.limpiar();
            tab_mante.getChildren().clear();
        }
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
    
    public void des_historial(){
        TablaGenerica tab_datos =aCombustible.get_ExDatosCom(set_invehiculo.getValorSeleccionado());
        if (!tab_datos.isEmpty()) {
            TablaGenerica tab_dato =aCombustible.get_ExResgistroSoli(tab_datos.getValor("MVE_PLACA"));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Cierre solicitud anterior", tab_dato.getValor("MVE_PLACA"));
            }else{
                aceplistado();
            }
        }else{
            utilitario.agregarMensaje("Registro No Disponible", "");
        }
    }
    
    public void acep_historial(){
        
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
             aut_busca.limpiar();
             utilitario.addUpdate("aut_busca");
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
//        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_mante.setCondicion("msc_secuencial=-1");
        } else {
            tab_mante.setCondicion("msc_secuencial=" + aut_busca.getValor());
        }
        tab_mante.getColumna("msc_conductor").setValorDefecto(tconductor.getValue()+"");
        tab_mante.getColumna("msc_fecha").setValorDefecto(utilitario.getFechaHoraActual());
        tab_mante.getColumna("msc_secuencial").setVisible(false);
        tab_mante.getColumna("msc_tipo_mantenimiento").setVisible(false);
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
        tab_mante.setValor("msc_estado_tramite","SOLICITUD");
        tab_mante.setValor("msc_tiposol", cmb_mantenimiento.getValue()+"");
        tab_mante.setValor("msc_secuencial",aCombustible.SecuencialCab(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getMes(utilitario.getFechaActual())), Integer.parseInt(tab_vehiculo.getValor("mve_secuencial"))));
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

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ORDEN DE TRABAJO":
               Grid gri_seleccion = new Grid();
               gri_seleccion.setColumns(2);
               cmb_impresion.setId("cmb_impresion");
               List listar = new ArrayList();
               Object filai1[] = {
                   "0", "NO"
               };
               Object filai2[] = {
                   "1", "SI"
               };
               listar.add(filai1);;
               listar.add(filai2);;
               cmb_impresion.setCombo(listar);
               gri_seleccion.getChildren().add(new Etiqueta(" RE IMPRESION :"));
               gri_seleccion.getChildren().add(cmb_impresion);
               
               dia_dialogod.Limpiar();
               dia_dialogod.setDialogo(gridd);
               grid_de.getChildren().add(gri_seleccion);
               dia_dialogod.setDialogo(grid_de);
               dia_dialogod.dibujar();
           break;
        }
    }
    
    public void abrirReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ORDEN DE TRABAJO":
               String anio;
               anio=String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
               TablaGenerica tab_datod = mDescuento.Direc_Asist(Integer.parseInt("229"));
               if(!tab_datod.isEmpty()){
                   if(cmb_impresion.getValue().equals("0")){
                       TablaGenerica tab_dato =aCombustible.ParametrosID(tab_mante.getValor("msc_secuencial")+"");
                       if(!tab_dato.isEmpty()){
                           Integer numero = Integer.parseInt(aCombustible.ParametrosMax("ORDEN"));
                           String cadena = anio+"-"+String.valueOf(numero)+"";
                           p_parametros.put("secuencial", cadena);
                       }else{
                           Integer numero = Integer.parseInt(aCombustible.ParametrosMax("ORDEN"));
                           Integer cantidad=0;
                           cantidad=numero +1;
                           String cadena = anio+"-"+String.valueOf(numero)+"";
                           p_parametros.put("secuencial", cadena);
                           aCombustible.getNumero(String.valueOf(cantidad), "ORDEN", "ORDEN", tab_mante.getValor("msc_secuencial"),utilitario.getVariable("NICK"));
                       }
                   }else {
                       Integer numero = Integer.parseInt(aCombustible.ParametrosMax("ORDEN"));
                       String cadena = anio+"-"+String.valueOf(numero)+"";
                       p_parametros.put("secuencial", cadena);
                   }
                   p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                   p_parametros.put("placa", tplaca.getValue()+"");
                   p_parametros.put("id", tab_mante.getValor("msc_secuencial")+"");
                   p_parametros.put("numero", cmb_impresion.getValue()+"");
                   p_parametros.put("fecha_orden", utilitario.getFechaLarga(utilitario.getFechaActual())+"");
                   p_parametros.put("director", tab_datod.getValor("nombres")+"");
                   p_parametros.put("mantenimiento", tab_mante.getValor("msc_solicitud")+"");
                   rep_reporte.cerrar();
                   sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                   sef_formato.dibujar();
               }else{
                   utilitario.agregarMensaje("Director No Disponible", "");
               }
               break;
        }
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

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Tabla getSet_desvehiculo() {
        return set_desvehiculo;
    }

    public void setSet_desvehiculo(Tabla set_desvehiculo) {
        this.set_desvehiculo = set_desvehiculo;
    }
    
}
