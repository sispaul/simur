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
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
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
    private Tabla set_accesorio = new Tabla();
    private Tabla set_departamento = new Tabla(); 
    private Tabla set_cargo = new Tabla(); 
    private Tabla set_responsable = new Tabla(); 
    private Tabla set_conductor = new Tabla();
    private Tabla tab_articulo = new Tabla();
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    
    //Cajas de Texto
    private Texto tplaca = new Texto();
    private Texto tmarca = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tanio = new Texto();
    private Texto tcolor = new Texto();
    private Texto tconductor = new Texto();
    private Texto tcomentario = new Texto();
    private Texto taccesorio = new Texto();
    private Texto txt_cantidad = new Texto();
    private Texto txt_estado = new Texto();
    
    //Dialogos
    private Dialogo dia_dialogod = new Dialogo();
    private Dialogo dia_dialogoc = new Dialogo();
    private Dialogo dia_dialogor = new Dialogo();
    private Dialogo dia_dialogore = new Dialogo();
    private Dialogo dia_dialogoco = new Dialogo();
    private Dialogo dia_dialogocon = new Dialogo();
    private Dialogo dia_dialogoa = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_a = new Grid();
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
    private Grid grida = new Grid();
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
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
        
        Boton bot_save = new Boton();
        bot_save.setValue("ACCESORIOS");
        bot_save.setIcon("ui-icon-extlink");
        bot_save.setMetodo("lista");
        bar_botones.agregarComponente(bot_save);
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
        pan_panel1.setStyle("width:300px;");
        pan_panel1.setHeader("Acciones De Botones");
        
        Grid gri_botones = new Grid();
        gri_botones.setColumns(2);
        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar Automotor/Maquinaria: ");
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        gri_botones.getChildren().add(new Etiqueta("NUEVO REGISTRO :"));
        gri_botones.getChildren().add(bot_busca);
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
        set_automotores.getBot_aceptar().setMetodo("filtrarListado");
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
        
        //Para accesorios
        Grid gri_accesorio = new Grid();
        gri_accesorio.setColumns(6);
        gri_accesorio.getChildren().add(new Etiqueta("Accesorio"));
        gri_accesorio.getChildren().add(taccesorio);
        txt_cantidad.setSize(2);
        gri_accesorio.getChildren().add(new Etiqueta("Cantidad"));
        gri_accesorio.getChildren().add(txt_cantidad);
        txt_estado.setSize(10);
        gri_accesorio.getChildren().add(new Etiqueta("Estado"));
        gri_accesorio.getChildren().add(txt_estado);
        Boton bot_accesoriog = new Boton();
        bot_accesoriog.setValue("Guardar");
        bot_accesoriog.setIcon("ui-icon-disk");
        bot_accesoriog.setMetodo("insAccesorio");
        bar_botones.agregarBoton(bot_accesoriog);
        Boton bot_accesorioe = new Boton();
        bot_accesorioe.setValue("Eliminar");
        bot_accesorioe.setIcon("ui-icon-closethick");
        bot_accesorioe.setMetodo("endAccesorio");
        bar_botones.agregarBoton(bot_accesorioe);
        gri_accesorio.getChildren().add(bot_accesoriog);
        gri_accesorio.getChildren().add(bot_accesorioe);
        dia_dialogoa.setId("dia_dialogoa");
        dia_dialogoa.setTitle("ACCESORIOS DE AUTOMOTOR / MAQUINARIA"); //titulo
        dia_dialogoa.setWidth("38%"); //siempre en porcentajes  ancho
        dia_dialogoa.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoa.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoa.getGri_cuerpo().setHeader(gri_accesorio);
        grid_a.setColumns(4);
        agregarComponente(dia_dialogoa);
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);

    }
    
    
    public void abrirVerTabla() {
        set_automotores.dibujar();
    }
    
        //mostrar datos de tabla
    public void buscaRegistro(){
     set_automotores.dibujar();
    }
    
    //Accesorios para asignacion
    public void lista(){
        if(tab_tabla.getValor("mav_secuencial")!=null && tab_tabla.getValor("mav_secuencial").isEmpty() == false){
            TablaGenerica tab_dato =aCombustible.getVehiculo(tplaca.getValue()+"");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_datoa =aCombustible.get_ValiAccesorio(tab_dato.getValor("MVE_SECUENCIAL"));
                if (!tab_datoa.isEmpty()) {
                    TablaGenerica tab_datoac =aCombustible.get_ValiAcces(tab_dato.getValor("MVE_SECUENCIAL"));
                    if (!tab_datoa.isEmpty()) {
                        dibujar();
                    }else{
                        aCombustible.getAccesorios(tab_tabla.getValor("mav_secuencial"), tab_dato.getValor("MVE_SECUENCIAL"));
                        dibujar();
                    }
                }else{
//                    utilitario.agregarMensaje("No encuentra Accesorios", "");
                    dibujar();
                }
            }else{
                utilitario.agregarMensaje("No encuentra Registro", "");
            }
        }else{
            utilitario.agregarMensaje("Debe ubicarse en un Registro", "");
        }
    }
    
    public void dibujar(){
        dia_dialogoa.Limpiar();
        dia_dialogoa.setDialogo(grida);
        grid_a.getChildren().add(set_accesorio);
        set_accesorio.setId("set_accesorio");
        set_accesorio.setConexion(con_sql);
        set_accesorio.setSql("SELECT MDA_CODIGO,MDA_DETALLE,MDA_CANTIDAD,MDA_ESTADO\n" +
                "FROM MVDETASIGNACION WHERE MDA_ESTADO <> 'de baja' and MAV_SECUENCIAL ="+tab_tabla.getValor("mav_secuencial"));
        set_accesorio.getColumna("MDA_DETALLE").setFiltro(true);
        set_accesorio.setRows(9);
        set_accesorio.setTipoSeleccion(false);
        dia_dialogoa.setDialogo(grid_a);
        set_accesorio.dibujar();
        dia_dialogoa.dibujar();
    }
    
    public void insAccesorio(){
        if(taccesorio.getValue()!= null && taccesorio.toString().isEmpty()==false){
            if(txt_estado.getValue()!= null && txt_estado.toString().isEmpty()==false){
                aCombustible.getParametacces(tab_tabla.getValor("mav_secuencial"), taccesorio.getValue()+"", txt_cantidad.getValue()+"", txt_estado.getValue()+"", aCombustible.ParametrosAccE());
                taccesorio.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Accesorio");
                set_accesorio.actualizar();
            }
        }
    }
    
    public void endAccesorio(){
        if (set_accesorio.getValorSeleccionado() != null && set_accesorio.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteaccesorios(set_accesorio.getValorSeleccionado());
            utilitario.agregarMensaje("Registro eliminado", "Accesorio");
            set_accesorio.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void aceptar_lis(){
        if(set_accesorio.getValorSeleccionado()!= null && set_accesorio.getValorSeleccionado().isEmpty() == false){
            if(tab_tabla.getValor("mav_secuencial")!= null && tab_tabla.getValor("mav_secuencial").isEmpty() == false){
                TablaGenerica tab_dato =aCombustible.get_ExtraDatos(set_accesorio.getValorSeleccionado()+"","ACCES", "N/D");
                if (!tab_dato.isEmpty()) {
                    aCombustible.getMVDetalleASI(tab_tabla.getValor("mav_secuencial"), tab_dato.getValor("LIS_NOMBRE")+"", Double.valueOf(set_accesorio.getValor("Cantidad")+""), set_accesorio.getValor("Estado")+""); 
                    tab_articulo.actualizar();
                }else{
                    utilitario.agregarMensaje("Accesorio nose Encuentra en Base", "");
                }
            }else{
                utilitario.agregarMensaje("Solicitud No Existe", "");
            }
        }  
    }
    
    //llenada los datos de la selección de vehiculo/automotor
    public void filtrarListado(){
        if(set_automotores.getValorSeleccionado()!= null && set_automotores.getValorSeleccionado().isEmpty() == false){
            TablaGenerica tab_datov =aCombustible.getVerificar(Integer.parseInt(set_automotores.getValorSeleccionado()));
            if (!tab_datov.isEmpty()) {
                if(Integer.parseInt(tab_datov.getValor("valor"))==1){
                    TablaGenerica tab_datoc =aCombustible.setVerificar(Integer.parseInt(set_automotores.getValorSeleccionado()));
                    if (!tab_datoc.isEmpty()) {
                        if(tab_datoc.getValor("MAV_SECUENCIAL")!=null && tab_datoc.getValor("MAV_SECUENCIAL").isEmpty() == false){
                            aCombustible.actDescargo(Integer.parseInt(tab_datoc.getValor("MAV_SECUENCIAL")),utilitario.getVariable("NICK"), utilitario.getFechaActual());
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
                                utilitario.agregarMensajeInfo(" DATOS ", " NO DISPONIBLES ");
                            }
                        }else{
                            utilitario.agregarMensajeError("RESGISTRO NO PUDO SER DESCARGADO","");
                        }
                    }else{
                        utilitario.agregarMensajeError("RESGISTROS NO PUDO SER UTILIZADOS","");
                    }
                }else{
                    utilitario.agregarMensajeInfo("Automotor Posee Mas de 2 Asignaciones", " Actualmente");
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
        tab_tabla.agregarRelacion(tab_articulo);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);

        
        Division div = new Division();
        div.dividir1(ptt);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
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
        //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        pan_opcion1.getChildren().clear();
        utilitario.addUpdate("pan_opcion");
        utilitario.addUpdate("pan_opcion1");
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void filtrarSolicitud (SelectEvent evt){
        limpiar();
        aut_busca.onSelect(evt);
        dibujardocumento();
        utilitario.addUpdate("aut_busca,pan_opcion");
        completa_dato();
    }
    
    public void completa_dato(){
     TablaGenerica tab_dato =aCombustible.datosExtraer(Integer.parseInt(aut_busca.getValor()));
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
        aCombustible.actDescargo(Integer.parseInt(tab_tabla.getValor("MAV_SECUENCIAL")), tcomentario.getValue()+"",utilitario.getVariable("NICK"), utilitario.getFechaActual());
         utilitario.agregarMensaje("Vehiculo","Disponible");
         dia_dialogoco.cerrar();
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
    
    public void migrar(){
        if(tab_tabla.getValor("mav_secuencial")!=null){
            TablaGenerica tab_dato =aCombustible.getVehiculo(tplaca.getValue()+"");
            if (!tab_dato.isEmpty()) {
                aCombustible.getAccesorios(tab_tabla.getValor("mav_secuencial"), tab_dato.getValor("MVE_SECUENCIAL"));
                utilitario.addUpdate("tab_articulo");
                tab_articulo.actualizar();
            }else{
                utilitario.agregarMensajeInfo("Ingrese Accesorios de Manera Manual", "");
            }
        }else{
            utilitario.agregarMensaje("Guardar Primero el Resgistro Anterior", "");
        }
       utilitario.addUpdate("tab_articulo");
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
           case "COMPROBANTE DE SALIDA":
               abrirReporte();
           break;
        }
    }
    
    public void abrirReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "COMPROBANTE DE SALIDA":
               p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
               p_parametros.put("codigo", Integer.parseInt(tab_tabla.getValor("MAV_SECUENCIAL")+""));
               p_parametros.put("placa", tplaca.getValue()+"");
               rep_reporte.cerrar();
               sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
               sef_formato.dibujar();
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

    public Tabla getSet_accesorio() {
        return set_accesorio;
    }

    public void setSet_accesorio(Tabla set_accesorio) {
        this.set_accesorio = set_accesorio;
    }
    
}
