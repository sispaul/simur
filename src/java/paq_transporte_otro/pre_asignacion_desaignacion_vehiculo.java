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
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_automotores = new SeleccionTabla(); 
    private Tabla set_departamento = new Tabla(); 
    private Tabla set_cargo = new Tabla(); 
    private Tabla set_responsable = new Tabla(); 
    private Tabla set_registros = new Tabla();
    private Tabla set_conductor = new Tabla();
    private Tabla tab_articulo = new Tabla();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Cajas de Texto
    private Texto tplaca = new Texto();
    private Texto tmarca = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tanio = new Texto();
    private Texto tcolor = new Texto();
    private Texto tconductor = new Texto();
    private Texto tcomentario = new Texto();
    
    //Dialogos
    private Dialogo dia_dialogod = new Dialogo();
    private Dialogo dia_dialogoc = new Dialogo();
    private Dialogo dia_dialogor = new Dialogo();
    private Dialogo dia_dialogore = new Dialogo();
    private Dialogo dia_dialogoco = new Dialogo();
    private Dialogo dia_dialogocon = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_ca = new Grid();
    private Grid grid_re = new Grid();
    private Grid grid_res = new Grid();
    private Grid grid_co = new Grid();
    private Grid grid_con = new Grid();
    private Grid gridd = new Grid();
    private Grid gridc = new Grid();
    private Grid gridr = new Grid();
    private Grid gridre = new Grid();
    private Grid gridco = new Grid();
    private Grid gridcon = new Grid();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_asignacion_desaignacion_vehiculo() {
        
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
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
        aut_busca.setAutoCompletar("SELECT a.MAV_SECUENCIAL,v.MVE_PLACA,a.MAV_NOMEMPLEA,a.MAV_CARGOEMPLEA,a.MAV_DEPARTAMENTO,a.MAV_NOMBRE_COND\n" +
                "FROM MVASIGNARVEH a INNER JOIN MVVEHICULO v ON a.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "where a.MAV_ESTADO_ASIGNACION = 1 order by v.MVE_PLACA");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Asignación:"));
        bar_botones.agregarComponente(aut_busca);
        
        //Ingreso y busqueda 
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 750px;");
        pan_panel.setHeader("ENTREGA - RECEPCION");
        
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
        bot_busca.setMetodo("buscaRegistro");
        Boton bot_news = new Boton();
        bot_news.setValue("Nuevo: ");
        bot_news.setIcon("ui-icon-document");
        bot_news.setMetodo("abrirVerTabla");
        Boton bot_asigna = new Boton();
        bot_asigna.setValue("Asignar :");
        bot_asigna.setIcon("ui-icon-check");
        bot_asigna.setMetodo("guardar");
        Boton bot_quitar = new Boton();
        bot_quitar.setValue("Des-Asignar: ");
        bot_quitar.setIcon("ui-icon-cancel");
        bot_quitar.setMetodo("Entrega");
        gri_botones.getChildren().add(bot_news);
        gri_botones.getChildren().add(bot_asigna);
        gri_botones.getChildren().add(bot_busca);
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
        dia_dialogoc.setWidth("50%"); //siempre en porcentajes  ancho
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
        dia_dialogor.getBot_aceptar().setMetodo("servidor");
        grid_re.setColumns(4);
        agregarComponente(dia_dialogor);
        
        dia_dialogore.setId("dia_dialogore");
        dia_dialogore.setTitle("REGISTROS ASIGNADOS"); //titulo
        dia_dialogore.setWidth("62%"); //siempre en porcentajes  ancho
        dia_dialogore.setHeight("40%");//siempre porcentaje   alto
        dia_dialogore.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogore.getBot_aceptar().setMetodo("filtrarSolicitud");
        grid_res.setColumns(4);
        agregarComponente(dia_dialogore);
        
        dia_dialogoco.setId("dia_dialogoco");
        dia_dialogoco.setTitle("NOVEDADES AL ENTREGA EL AUTOMOTOR"); //titulo
        dia_dialogoco.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogoco.setHeight("20%");//siempre porcentaje   alto
        dia_dialogoco.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoco.getBot_aceptar().setMetodo("actuResgitro");
        grid_res.setColumns(4);
        agregarComponente(dia_dialogoco);
        
        dia_dialogocon.setId("dia_dialogocon");
        dia_dialogocon.setTitle("CONDUCTORES - DISPONIBLES"); //titulo
        dia_dialogocon.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogocon.setHeight("50%");//siempre porcentaje   alto
        dia_dialogocon.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogocon.getBot_aceptar().setMetodo("aceptoConductor");
        grid_con.setColumns(2);
        agregarComponente(dia_dialogocon);
        
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

        set_registros.setId("set_registros");
        set_registros.setConexion(con_sql);
        set_registros.setHeader("REGISTROS PARA DESCARGO");
        set_registros.setSql("SELECT a.MAV_SECUENCIAL,v.MVE_PLACA,a.MAV_NOMEMPLEA,a.MAV_CARGOEMPLEA,a.MAV_DEPARTAMENTO,a.MAV_NOMBRE_COND\n" +
                "FROM MVASIGNARVEH a INNER JOIN MVVEHICULO v ON a.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
                "where a.MAV_ESTADO_ASIGNACION = 1 order by v.MVE_PLACA");
        set_registros.getColumna("MVE_PLACA").setFiltro(true);
        set_registros.setTipoSeleccion(false);
        set_registros.setRows(10);
        set_registros.dibujar();
    }

    public void abrirVerTabla() {
        set_automotores.dibujar();
    }
    
    public void aceptoRegistro(){
        if(set_automotores.getValorSeleccionado()!= null && set_automotores.getValorSeleccionado().isEmpty() == false){
            TablaGenerica tab_datov =aCombustible.getVerificar(Integer.parseInt(set_automotores.getValorSeleccionado()));
            if (!tab_datov.isEmpty()) {
                if(Integer.parseInt(tab_datov.getValor("valor"))<0){
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
                    utilitario.agregarMensajeInfo("Automotor se encuentra aun", " ASIGNADO");
                }
            }else{
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
            }
        }else{
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
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
        tab_tabla.getColumna("MAV_FECHAASIGNACION").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla.getColumna("MAV_LOGININGRESO").setValorDefecto(utilitario.getVariable("NICK"));
        tab_tabla.getColumna("MAV_FECHAINGRESO").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla.getColumna("MAV_DEPARTAMENTO").setMetodoChange("buscarDir");
        tab_tabla.getColumna("MAV_NOMBRE_COND").setMetodoChange("aceptoDialogocon");
        
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

        tab_articulo.setId("tab_articulo");
        tab_articulo.setConexion(con_sql);
        tab_articulo.setSql("SELECT MVE_SECUENCIAL,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO FROM MVDETALLEVEHICULO where MVE_SECUENCIAL ="+tab_tabla.getValor("MVE_SECUENCIAL"));
        tab_articulo.getColumna("MVE_SECUENCIAL").setVisible(false);
        tab_articulo.getGrid().setColumns(4);
        tab_articulo.setLectura(true);
        tab_articulo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_articulo);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(ptt);
        gru.getChildren().add(tpa);
        pan_opcion.getChildren().add(gru); 
    }
    
    public void conductor(){
        tab_tabla.setValor("MVE_SECUENCIAL",set_automotores.getValorSeleccionado()+"");
        tab_tabla.setValor("MAV_ESTADO_TRAMITE","ASIGNADO");
        tab_tabla.setValor("MAV_ESTADO_ASIGNACION","1");
        tab_tabla.setValor("MAV_NOMBRE_COND",tconductor.getValue()+"");
        utilitario.addUpdate("tab_tabla");
        qiAutoriza();
    }
    
    public void qiAutoriza(){
        tab_tabla.setValor("MAV_AUTORIZA",tab_consulta.getValor("NOM_USUA")+"");
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
        direccion();
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
        cargo();
    }
    
    public void direccion(){
        TablaGenerica tab_dato =aCombustible.getDirec(Integer.parseInt(set_departamento.getValorSeleccionado()));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("MAV_DEPARTAMENTO",tab_dato.getValor("nombre_dir")+"");
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("Información Dir","No Disponible");
        }
    }
    
    public void cargo(){
        TablaGenerica tab_dato =aCombustible.getCarg(Integer.parseInt(set_cargo.getValorSeleccionado()), Integer.parseInt(set_departamento.getValorSeleccionado()));
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("MAV_CARGOEMPLEA",tab_dato.getValor("nombre_cargo")+"");
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("Información Carg","No Disponible");
        }
    }
    
    public void servidor(){
        TablaGenerica tab_dato =aCombustible.getResp(Integer.parseInt(set_cargo.getValorSeleccionado()), Integer.parseInt(set_departamento.getValorSeleccionado()), set_responsable.getValorSeleccionado());
        if (!tab_dato.isEmpty()) {
            tab_tabla.setValor("MAV_NOMEMPLEA",tab_dato.getValor("nombres")+"");
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("Información Carg","No Disponible");
        }
        dia_dialogor.cerrar();
    }
    
    public void buscaRegistro(){
        dia_dialogore.Limpiar();
        dia_dialogore.setDialogo(gridre);
        grid_res.getChildren().add(set_registros);
        dia_dialogore.setDialogo(grid_res);
        set_registros.dibujar();
        dia_dialogore.dibujar();
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

    public void filtrarSolicitud (){
        limpiar();
        if (set_registros.getValorSeleccionado() != null) {
            aut_busca.setValor(set_registros.getValorSeleccionado());
            dia_dialogore.cerrar();
            dibujardocumento();
            utilitario.addUpdate("aut_busca,pan_opcion");
            completa_dato();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }
    }
    
    public void completa_dato(){
     TablaGenerica tab_dato =aCombustible.datosExtraer(Integer.parseInt(set_registros.getValorSeleccionado()));
        if (!tab_dato.isEmpty()) {
            tplaca.setValue(tab_dato.getValor("MVE_PLACA") +""); tmarca.setValue(tab_dato.getValor("MVE_MARCA") +""); tmodelo.setValue(tab_dato.getValor("MVE_MODELO") +"");
            tanio.setValue(tab_dato.getValor("MVE_ANO") +""); tcolor.setValue(tab_dato.getValor("MVE_COLOR") +""); tconductor.setValue(tab_dato.getValor("MAV_NOMEMPLEA") +"");
            utilitario.addUpdate("tplaca");utilitario.addUpdate("tmarca");utilitario.addUpdate("tplaca");utilitario.addUpdate("tmodelo");utilitario.addUpdate("tanio");
            utilitario.addUpdate("tcolor");utilitario.addUpdate("tconductor");
        }else{
            utilitario.agregarMensajeError("Información","No Disponible");
        }
    }
    
    public void Entrega(){
        
        dia_dialogoco.Limpiar();
        dia_dialogoco.setDialogo(gridco);
        tcomentario.setSize(60);
        gridco.getChildren().add(new Etiqueta(" INGRESE COMENTARIO :"));
        gridco.getChildren().add(tcomentario);
        dia_dialogoco.setDialogo(grid_co);
        dia_dialogoco.dibujar();
        
    }
    
    public void actuResgitro(){
        if(tab_tabla.getValor("MAV_SECUENCIAL")!=null && tab_tabla.getValor("MAV_SECUENCIAL").isEmpty() == false){
        aCombustible.actDescargo(Integer.parseInt(tab_tabla.getValor("MAV_SECUENCIAL")), tcomentario.getValue()+"",utilitario.getVariable("NICK"), utilitario.getFechaHoraActual());
         utilitario.agregarMensaje("Información","No Disponible");
        }else{
            utilitario.agregarMensajeError("RESGISTRO NO DISPONIBLE","");
        }
    }
    
    public void aceptoDialogocon() {
        dia_dialogocon.Limpiar();
        dia_dialogocon.setDialogo(gridcon);
        grid_con.getChildren().add(set_conductor);
        set_conductor.setId("set_conductor");
        set_conductor.setConexion(con_postgres);
        set_conductor.setSql("SELECT cod_empleado, cedula_pass,nombres\n" +
                "FROM srh_empleado\n" +
                "where cod_cargo in (SELECT cod_cargo FROM srh_cargos WHERE nombre_cargo like '%CHOFER%') and estado = 1\n" +
                "order by nombres");
        set_conductor.getColumna("nombres").setFiltro(true);
        set_conductor.setRows(12);
        set_conductor.setTipoSeleccion(false);
        dia_dialogocon.setDialogo(grid_con);
        set_conductor.dibujar();
        dia_dialogocon.dibujar();
    }
    
    public void aceptoConductor(){
        if (set_conductor.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato =aCombustible.getChofer(set_conductor.getValorSeleccionado());
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("MVE_CONDUCTOR", tab_dato.getValor("nombres"));
                tab_tabla.setValor("MVE_ASIGNADO", tab_dato.getValor("activo"));
                utilitario.addUpdate("tab_tabla");
                dia_dialogocon.cerrar();
            }else{
                utilitario.agregarMensajeInfo("No existen Datos", "");
            }
        }
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        con_sql.guardarPantalla();
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

    public Tabla getSet_registros() {
        return set_registros;
    }

    public void setSet_registros(Tabla set_registros) {
        this.set_registros = set_registros;
    }

    public Tabla getSet_conductor() {
        return set_conductor;
    }

    public void setSet_conductor(Tabla set_conductor) {
        this.set_conductor = set_conductor;
    }

    public Tabla getTab_articulo() {
        return tab_articulo;
    }

    public void setTab_articulo(Tabla tab_articulo) {
        this.tab_articulo = tab_articulo;
    }
    
}
