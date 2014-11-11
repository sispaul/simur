/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
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
    private Tabla set_departamento = new Tabla(); 
    private Tabla set_cargo = new Tabla(); 
    private Tabla set_responsable = new Tabla(); 
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Cajas de Texto
    private Texto tplaca = new Texto();
    private Texto tmarca = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tanio = new Texto();
    private Texto tcolor = new Texto();
    private Texto tconductor = new Texto();
    
    //Dialogos
    private Dialogo dia_dialogod = new Dialogo();
    private Dialogo dia_dialogoc = new Dialogo();
    private Dialogo dia_dialogor = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_ca = new Grid();
    private Grid grid_re = new Grid();
    private Grid gridd = new Grid();
    private Grid gridc = new Grid();
    private Grid gridr = new Grid();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_asignacion_desaignacion_vehiculo() {
        
         tplaca.setId("tplaca");
         tmarca.setId("tmarca");
         tmodelo.setId("tmodelo");
         tanio.setId("tanio");
         tcolor.setId("tcolor");
         tconductor.setId("tconductor");
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
        set_automotores.getBot_aceptar().setMetodo("aceptoRegistro");
        set_automotores.setHeader("AUTOMOTORES");
        agregarComponente(set_automotores);
        
        dia_dialogod.setId("dia_dialogod");
        dia_dialogod.setTitle("DIRECCIONES"); //titulo
        dia_dialogod.setWidth("45%"); //siempre en porcentajes  ancho
        dia_dialogod.setHeight("40%");//siempre porcentaje   alto
        dia_dialogod.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogod.getBot_aceptar().setMetodo("buscarCarg");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogod);
        
        dia_dialogoc.setId("dia_dialogoc");
        dia_dialogoc.setTitle("CARGOS"); //titulo
        dia_dialogoc.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoc.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoc.getBot_aceptar().setMetodo("buscarServ");
        grid_ca.setColumns(4);
        agregarComponente(dia_dialogoc);
        
        dia_dialogor.setId("dia_dialogor");
        dia_dialogor.setTitle("SERVIDORES"); //titulo
        dia_dialogor.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogor.setHeight("40%");//siempre porcentaje   alto
        dia_dialogor.setResizable(false); //para que no se pueda cambiar el tamaño
//        dia_dialogor.getBot_aceptar().setMetodo("aceptoServ");
        grid_re.setColumns(4);
        agregarComponente(dia_dialogor);
        
        //tabla de seleccion
        set_departamento.setId("set_departamento");
        set_departamento.setConexion(con_postgres);
        set_departamento.setHeader("DIRECCIONES");
        set_departamento.setSql("SELECT DISTINCT srh_empleado.cod_direccion,srh_direccion.nombre_dir\n" +
                "FROM srh_empleado, srh_direccion where srh_empleado.cod_direccion = srh_direccion.cod_direccion and srh_direccion.estado_dir= 'ACTIVA'\n" +
                "order by srh_empleado.cod_direccion");
        set_departamento.getColumna("nombre_dir").setFiltro(true);
        set_departamento.setTipoSeleccion(false);
        set_departamento.setRows(10);
        set_departamento.dibujar();
    }

    public void abrirVerTabla() {
        set_automotores.dibujar();
    }
    
    public void aceptoRegistro(){
        if(set_automotores.getValorSeleccionado()!= null && set_automotores.getValorSeleccionado().isEmpty() == false){
            TablaGenerica tab_dato =aCombustible.getDatos(Integer.parseInt(set_automotores.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                tplaca.setValue(tab_dato.getValor("MVE_PLACA") +""); tmarca.setValue(tab_dato.getValor("MVE_MARCA") +""); tmodelo.setValue(tab_dato.getValor("MVE_MODELO") +"");
                tanio.setValue(tab_dato.getValor("MVE_ANO") +""); tcolor.setValue(tab_dato.getValor("MVE_COLOR") +""); tconductor.setValue(tab_dato.getValor("MVE_CONDUCTOR") +"");
                utilitario.addUpdate("tplaca");utilitario.addUpdate("tmarca");utilitario.addUpdate("tplaca");utilitario.addUpdate("tmodelo");utilitario.addUpdate("tanio");
                utilitario.addUpdate("tcolor");utilitario.addUpdate("tconductor");
                tab_tabla.insertar();
                set_automotores.cerrar();
                conductor();
            }else{
                utilitario.agregarMensajeInfo("No existen Datos", " DISPONIBLES");
            }
        }else{
            utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
        }
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
        tab_tabla.getColumna("MAV_DEPARTAMENTO").setMetodoChange("buscarDir");
        
        tab_tabla.getColumna("MVE_SECUENCIAL").setVisible(false);
        tab_tabla.getColumna("MAV_ESTADO_TRAMITE").setVisible(false);
        tab_tabla.getColumna("MAV_ESTADO_ASIGNACION").setVisible(false);
        tab_tabla.getColumna("MAV_DIRECCION_COND").setVisible(false);
        tab_tabla.getColumna("MAV_MOTIVO").setVisible(false);
        tab_tabla.getColumna("MAV_LOGININGRESO").setVisible(false);
        tab_tabla.getColumna("MAV_FECHAINGRESO").setVisible(false);
        tab_tabla.getColumna("MAV_FECHAACTUALI").setVisible(false);
        tab_tabla.getColumna("MAV_LOGINBORRADO").setVisible(false);
        tab_tabla.getColumna("MAV_FECHABORRADO").setVisible(false);
        tab_tabla.getColumna("MAV_ESTADO_REGISTRO").setVisible(false);
        tab_tabla.getColumna("MAV_LOGINACTUALI").setVisible(false);
        tab_tabla.getColumna("MAV_FECHADESCARGO").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(ptt);
        pan_opcion.getChildren().add(gru); 
    }
    
    public void conductor(){
        tab_tabla.setValor("MVE_SECUENCIAL",set_automotores.getValorSeleccionado()+"");
        tab_tabla.setValor("MAV_ESTADO_TRAMITE","ASIGNADO");
        tab_tabla.setValor("MAV_ESTADO_ASIGNACION","1");
        tab_tabla.setValor("MAV_NOMBRE_COND",tconductor.getValue()+"");
        utilitario.addUpdate("tab_tabla");
    }
    
    public void buscarDir(){
        dia_dialogod.Limpiar();
        dia_dialogod.setDialogo(gridd);
        grid_de.getChildren().add(set_departamento);
        dia_dialogod.setDialogo(grid_de);
        set_departamento.dibujar();
        dia_dialogod.dibujar();
    }
    
    public void buscarCarg(){
        dia_dialogod.cerrar();
        dia_dialogoc.Limpiar();
        dia_dialogoc.setDialogo(gridc);
        grid_ca.getChildren().add(set_cargo);
        set_cargo.setId("set_cargo");
        set_cargo.setConexion(con_postgres);
        set_cargo.setHeader("CARGOS");
        set_cargo.setSql("SELECT DISTINCT srh_empleado.cod_cargo,srh_cargos.nombre_cargo\n" +
                "FROM srh_empleado\n" +
                "INNER JOIN srh_cargos ON srh_empleado.cod_cargo = srh_cargos.cod_cargo\n" +
                "where srh_empleado.cod_direccion = "+set_departamento.getValorSeleccionado()+" order by srh_empleado.cod_cargo");
        set_cargo.getColumna("nombre_cargo").setFiltro(true);
        set_cargo.setTipoSeleccion(false);
        set_cargo.setRows(10);
        dia_dialogoc.setDialogo(grid_ca);
        set_cargo.dibujar();
        dia_dialogoc.dibujar();
    }
    
    public void buscarServ(){
        dia_dialogoc.cerrar();
        dia_dialogor.Limpiar();
        dia_dialogor.setDialogo(gridr);
        grid_re.getChildren().add(set_responsable);
        set_responsable.setId("set_responsable");
        set_responsable.setConexion(con_postgres);
        set_responsable.setHeader("CARGOS");
        set_responsable.setSql("SELECT cod_empleado,nombres FROM srh_empleado\n" +
                "where estado = 1 and cod_direccion = "+set_departamento.getValorSeleccionado()+" and cod_cargo = "+set_cargo.getValorSeleccionado()+" order by nombres");
        set_responsable.getColumna("nombres").setFiltro(true);
        set_responsable.setTipoSeleccion(false);
        set_responsable.setRows(10);
        dia_dialogor.setDialogo(grid_re);
        set_responsable.dibujar();
        dia_dialogor.dibujar();
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

    public Tabla getSet_departamento() {
        return set_departamento;
    }

    public void setSet_departamento(Tabla set_departamento) {
        this.set_departamento = set_departamento;
    }

    public Tabla getSet_cargo() {
        return set_cargo;
    }

    public void setSet_cargo(Tabla set_cargo) {
        this.set_cargo = set_cargo;
    }

    public Tabla getSet_responsable() {
        return set_responsable;
    }

    public void setSet_responsable(Tabla set_responsable) {
        this.set_responsable = set_responsable;
    }
    
}
