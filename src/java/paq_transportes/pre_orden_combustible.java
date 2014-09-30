/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
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
    private Tabla tab_tipo = new Tabla();
    private SeleccionTabla set_orden = new SeleccionTabla();
    private Tabla set_colaborador = new Tabla();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Busca de comprobante
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Cuadros para texto, busqueda reportes
    private Texto tex_busqueda = new Texto();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid = new Grid();
   private Dialogo dia_dialogor = new Dialogo();
    private Grid grid_dr = new Grid();
    
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Combos de Selección
    private Combo cmb_anio = new Combo();
    private Combo cmb_periodo = new Combo();
    
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
        
        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("BUSCAR CONDUCTOR"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("45%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoConductor");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
        
        /*CONFIGURACIÓN DE COMBOS*/
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        
        gri_busca1.getChildren().add(new Etiqueta("AÑO:"));
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca1.getChildren().add(cmb_anio);
        
        gri_busca1.getChildren().add(new Etiqueta("PERIODO:"));
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca1.getChildren().add(cmb_periodo);
        
         //para poder busca por apelllido el garante
        dia_dialogor.setId("dia_dialogor");
        dia_dialogor.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        dia_dialogor.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogor.setHeight("25%");//siempre porcentaje   alto
        dia_dialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogor.getGri_cuerpo().setHeader(gri_busca1);
        dia_dialogor.getBot_aceptar().setMetodo("aceptoOrden");
        grid_dr.setColumns(4);
        agregarComponente(dia_dialogor);
        
        //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);
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
        tab_tabla.getColumna("anio").setVisible(false);
        tab_tabla.getColumna("periodo").setVisible(false);
        tab_tabla.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_tabla.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
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
    
    //Busqueda por nombre de conductor
    public void buscaConductor(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_d.getChildren().add(set_colaborador);
        set_colaborador.setId("set_colaborador");
        set_colaborador.setConexion(con_sql);
        set_colaborador.setHeader("LISTA DE CONDUCTORES");
        set_colaborador.setSql("SELECT MVE_SECUENCIAL,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_CONDUCTOR LIKE '%"+tab_tabla.getValor("conductor")+"%'");
        set_colaborador.getColumna("MVE_CONDUCTOR").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogo.setDialogo(grid_d);
        set_colaborador.dibujar();
        dia_dialogo.dibujar();
    }
    

    //Busquedad de conductor por apellido
    public void aceptoConductor(){
        if (set_colaborador.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = pCombustible.getConductor(Integer.parseInt(set_colaborador.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =pCombustible.getConductores(tab_dato.getValor("MVE_CONDUCTOR"));
                if (!tab_dato1.isEmpty()) {
                    tab_tabla.setValor("conductor", tab_dato1.getValor("nombres"));
                    tab_tabla.setValor("ci_conductor", tab_dato1.getValor("cedula_pass"));
                    utilitario.addUpdate("tab_tabla");
                }else{
                utilitario.agregarMensajeInfo("Conductor, Disponible", "");
            }
            }else{
                utilitario.agregarMensajeInfo("Conductor, No Seleccionado", "");
            }
        }else {
            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
        }
        dia_dialogo.cerrar();
    }
    
    public void busVehiculo(){
        TablaGenerica tab_dato =pCombustible.getVehiculo(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_datoc = pCombustible.getConductores(tab_dato.getValor("mve_conductor"));
            if (!tab_datoc.isEmpty()) {
                tab_tabla.setValor("descripcion_vehiculo", tab_dato.getValor("descripcion"));
                tab_tabla.setValor("conductor", tab_datoc.getValor("nombres"));
                tab_tabla.setValor("ci_conductor", tab_datoc.getValor("cedula_pass"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Conductor","No Disponible");
            }
        }else{
            utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
        }
                secuencial();
    }

    public void secuencial(){
        if(tab_tabla.getValor("numero_orden")!=null && tab_tabla.getValor("numero_orden").toString().isEmpty() == false){

        }else{
            Integer numero = Integer.parseInt(pCombustible.listaMax());
            Integer cantidad=0;
            cantidad=numero +1;
            tab_tabla.setValor("numero_orden", String.valueOf(cantidad));
            utilitario.addUpdate("tab_tabla");
        }
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
        tab_tabla1.getColumna("anio").setVisible(false);
        tab_tabla1.getColumna("periodo").setVisible(false);
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
        
        tab_tipo.setId("tab_tipo");
        tab_tipo.setConexion(con_sql);
        tab_tipo.setSql("SELECT MVE_SECUENCIAL,MVE_PLACA,MVE_KILOMETRAJE,MVE_TIPO_COMBUSTIBLE,MVE_CAPACIDAD_TANQUE_COMBUSTIBLE \n" +
                "FROM MVVEHICULO WHERE MVE_PLACA = '"+tab_tabla1.getValor("placa_vehiculo")+"'");
        tab_tipo.setCampoPrimaria("MVE_SECUENCIAL");
        tab_tipo.setLectura(true);
        tab_tipo.dibujar();
        
        tab_calculo.getColumna("ide_tipo_combustible").setValorDefecto(tab_tipo.getValor("MVE_TIPO_COMBUSTIBLE"));
        tab_calculo.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_calculo.getColumna("fecha_digitacion").setValorDefecto(utilitario.getFechaActual());
        tab_calculo.getColumna("hora_digitacion").setValorDefecto(utilitario.getFechaHoraActual());
        tab_calculo.getColumna("usu_digitacion").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_calculo.getColumna("placa_vehiculo").setValorDefecto(tab_tabla1.getValor("placa_vehiculo"));
        tab_calculo.getColumna("fecha_digitacion").setVisible(false);
        tab_calculo.getColumna("hora_digitacion").setVisible(false);
        tab_calculo.getColumna("usu_digitacion").setVisible(false);
        tab_calculo.getColumna("ide_calculo_consumo").setVisible(false);
        tab_calculo.getColumna("placa_vehiculo").setVisible(false);
        tab_calculo.getColumna("kilometraje").setMetodoChange("kilometraje");
        tab_calculo.getColumna("galones").setMetodoChange("galones");
        tab_calculo.setTipoFormulario(true);
        tab_calculo.getGrid().setColumns(4);
        tab_calculo.dibujar();
        PanelTabla pte = new PanelTabla();
        pte.setPanelTabla(tab_calculo);
        pte.getMenuTabla().getItem_guardar().setRendered(false);//nucontextual().setrendered(false);
        pte.getMenuTabla().getItem_insertar().setRendered(false);//nucontextual().setrendered(false);
        
        Boton bot_new = new Boton();
        bot_new.setValue("NUEVO");
        bot_new.setIcon("ui-icon-document");
        bot_new.setMetodo("new_fila");
        
        Boton bot_save = new Boton();
        bot_save.setValue("GUARDAR");
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("guardar");
        
        pte.getChildren().add(bot_new);
        pte.getChildren().add(bot_save);
        Division div = new Division();
        div.dividir2(ptt,pte,"35%", "h");
        agregarComponente(div);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    public void new_fila(){
        if (tab_calculo.isFocus()) {
            tab_calculo.insertar();
        }
    }
    
    public void kilometraje(){
        TablaGenerica tab_dato =pCombustible.getKilometraje(tab_tabla1.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_KILOMETRAJE"));
            Double valor2 = Double.valueOf(tab_calculo.getValor("kilometraje"));
            if(valor2>valor1){
                tab_calculo.getColumna("galones").setLectura(false);
                tab_calculo.getColumna("total").setLectura(false);
//                tab_calculo.setValor("ide_tipo_combustible", tab_dato.getValor("MVE_TIPO_COMBUSTIBLE"));
                utilitario.addUpdate("tab_calculo");
            }else{
                utilitario.agregarMensajeError("Kilometraje","Por Debajo del Anterior");
                tab_calculo.getColumna("galones").setLectura(true);
                tab_calculo.getColumna("total").setLectura(true);
                tab_calculo.getColumna("ide_tipo_combustible").setLectura(true);
                utilitario.addUpdate("tab_calculo");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void galones(){
        TablaGenerica tab_dato =pCombustible.getKilometraje(tab_tabla1.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_CAPACIDAD_TANQUE_COMBUSTIBLE"));
            Double valor2 = Double.valueOf(tab_calculo.getValor("galones"));
            if(valor2<valor1){
                tab_calculo.getColumna("total").setLectura(false);
                utilitario.addUpdate("tab_calculo");
                        valor();
            }else{
                utilitario.agregarMensajeError("Galones","Exceden Capacidad de Vehiculo");
                tab_calculo.setValor("galones", null);
                tab_calculo.getColumna("total").setLectura(true);
                utilitario.addUpdate("tab_calculo");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void valor(){
        TablaGenerica tab_dato =pCombustible.getCombustible(Integer.parseInt(tab_calculo.getValor("ide_tipo_combustible")));
        if (!tab_dato.isEmpty()) {
            Double valor;
            valor = (Double.parseDouble(tab_dato.getValor("valor_galon"))*Double.parseDouble(tab_calculo.getValor("galones")));
            tab_calculo.setValor("total", String.valueOf(Math.rint(valor*100)/100));
            utilitario.addUpdate("tab_calculo");
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            tab_tabla.insertar();
        }else{
            tab_tabla1.limpiar();
            tab_calculo.limpiar();
            limpiar();
            ordenConsumo();
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
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
        @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ORDEN DE CONSUMO":
                aceptoOrden();
          break;
           case "CONSUMO PROMEDIO COMBUSTIBLE":
                dia_dialogor.Limpiar();
                dia_dialogor.dibujar();
          break;
           case "CONSUMO RENDIMIENTO COMBUSTIBLE":
                dia_dialogor.Limpiar();
                dia_dialogor.dibujar();
          break;
        }
    } 
    
      public void aceptoOrden(){
        switch (rep_reporte.getNombre()) {
               case "ORDEN DE CONSUMO":
                   
                   TablaGenerica tab_dato =pCombustible.getUsuario(tab_tabla.getValor("autoriza"));
                   if (!tab_dato.isEmpty()) {
                    p_parametros.put("autoriza", tab_dato.getValor("NOM_USUA")+"");
                    p_parametros.put("id", Integer.parseInt(tab_tabla.getValor("ide_orden_consumo")+""));
                    p_parametros.put("vale", Integer.parseInt(tab_tabla.getValor("numero_orden")+""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                   }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                   
               break;
               case "CONSUMO PROMEDIO COMBUSTIBLE":
                    TablaGenerica tab_dato1 =pCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato1.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato1.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                   break;
               case "CONSUMO RENDIMIENTO COMBUSTIBLE":
                    TablaGenerica tab_dato2 =pCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato2.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato2.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                   break;
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

    public Tabla getSet_colaborador() {
        return set_colaborador;
    }

    public void setSet_colaborador(Tabla set_colaborador) {
        this.set_colaborador = set_colaborador;
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
    
}
