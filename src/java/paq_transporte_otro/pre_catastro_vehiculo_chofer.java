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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_catastro_vehiculo_chofer extends Pantalla{

    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_articulo = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_conductor = new Tabla();
    
    //DECLARACION OBJETOS PARA PARAMETROS
    private SeleccionTabla set_ingresos = new SeleccionTabla();
    private SeleccionTabla set_marca = new SeleccionTabla();
    private SeleccionTabla set_tipo = new SeleccionTabla();
    private SeleccionTabla set_modelo = new SeleccionTabla();
    private SeleccionTabla set_version = new SeleccionTabla();
    
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    
    //DECLARACIÓN DE DIALOGOS
    private Dialogo dia_dialogoC = new Dialogo();
    private Grid grid_co = new Grid();
    private Grid gridc = new Grid();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    
    private Texto txt_item = new Texto();
    private Texto txt_cantidad = new Texto();
    private Texto txt_estado = new Texto();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_catastro_vehiculo_chofer() {
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("INGRESO DE VEHICULOS");
        agregarComponente(pan_opcion);
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirVerTabla");
        bar_botones.agregarBoton(bot_busca);
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT MVE_SECUENCIAL,MVE_PLACA,MVE_MARCA,MVE_MODELO,MVE_CHASIS FROM MVVEHICULO");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_registro = new Boton();
        bot_registro.setValue("INSERTAR MARCA");
        bot_registro.setIcon("ui-icon-comment");
        bot_registro.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot_registro);
        
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
        bot_tipox.setMetodo("endTipo");
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
        bot_modelox.setMetodo("endModelo");
        bar_botones.agregarBoton(bot_modelox);
        gri_modelo.getChildren().add(bot_modelo);
        gri_modelo.getChildren().add(bot_modelox);
        set_modelo.setId("set_modelo");
        set_modelo.getTab_seleccion().setConexion(con_sql);
        set_modelo.setSeleccionTabla("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODEL' and LIS_ESTADO = 1 order by modelo", "LIS_NOMBRE");
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
        set_version.getGri_cuerpo().setHeader(gri_version);
        set_version.setHeader("VERSIÓN");
        agregarComponente(set_version);
        
        //CONFIGURACION DE DIALOGO SELECCION DE GESTOR
        dia_dialogoC.setId("dia_dialogoC");
        dia_dialogoC.setTitle("CONDUCTORES - DISPONIBLES"); //titulo
        dia_dialogoC.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogoC.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoC.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoC.getBot_aceptar().setMetodo("aceptoConductor");
        grid_co.setColumns(2);
        agregarComponente(dia_dialogoC);
        dibujaIngreso();
        
        set_ingresos.setId("set_ingresos");
        set_ingresos.getTab_seleccion().setConexion(con_sql);
        set_ingresos.setSeleccionTabla("SELECT MVE_SECUENCIAL,MVE_PLACA,(MVE_MARCA+','+MVE_TIPO+','+MVE_MODELO+','+MVE_VERSION+','+MVE_COLOR+','+MVE_ANO) as descripcion FROM MVVEHICULO", "MVE_SECUENCIAL");
        set_ingresos.getTab_seleccion().getColumna("MVE_PLACA").setFiltro(true);
        set_ingresos.getTab_seleccion().setRows(10);
        set_ingresos.setRadio();
        set_ingresos.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_ingresos.setHeader("BUSCAR SOLICITUD POR CEDULA");
        agregarComponente(set_ingresos);
    }

    public void abrirVerTabla() {
        set_ingresos.dibujar();
    }
    
        //Dibuja la Pantalla
    public void aceptarBusqueda() {
            if (set_ingresos.getValorSeleccionado() != null) {
                aut_busca.setValor(set_ingresos.getValorSeleccionado());
                set_ingresos.cerrar();
                dibujaIngreso();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
            }
    }
    
    public void dibujaIngreso(){
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvvehiculo", "mve_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mve_secuencial=-1");
        } else {
            tab_tabla.setCondicion("mve_secuencial=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE AS MARCA FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE AS TIPO FROM MVLISTA WHERE TAB_CODIGO = 'tipo' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_TIPO_COMBUSTIBLE").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_tabla.getColumna("MVE_MODELO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODEL' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_VERSION").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as VERSION FROM MVLISTA WHERE TAB_CODIGO = 'VERSI' and LIS_ESTADO = 1");
        tab_tabla.getColumna("MVE_MARCA").setMetodoChange("cargarTipo");
        tab_tabla.getColumna("MVE_TIPO").setMetodoChange("cargarModelo");
        tab_tabla.getColumna("MVE_MODELO").setMetodoChange("cargarVersion");
        tab_tabla.getColumna("MVE_TIPOMEDICION").setMetodoChange("activarCasilla");
        tab_tabla.getColumna("MVE_CONDUCTOR").setMetodoChange("aceptoDialogoc");
        List list = new ArrayList();
        Object fil1[] = {
            "KILOMETROS", "KILOMETROS"
        };
        Object fil2[] = {
            "HORAS", "HORAS"
        };
        list.add(fil1);;
        list.add(fil2);;
        tab_tabla.getColumna("MVE_TIPOMEDICION").setRadio(list, " ");
        tab_tabla.getColumna("MVE_HOROMETRO").setLectura(true);
        tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(true);
        tab_tabla.getColumna("MVE_ASIGNADO").setVisible(false);
        tab_tabla.getColumna("MVE_OBSERVACIONES").setVisible(false);
        tab_tabla.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHA_BORRADO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAINGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINBORRADO").setVisible(false);
        tab_tabla.getColumna("MVE_NUMIMR").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tab_tabla);
        
        Grid gri_medio = new Grid();
        gri_medio.setColumns(6);
        Boton bot_delete1 = new Boton();
        bot_delete1.setValue("ELIMINAR");
        bot_delete1.setIcon("ui-icon-closethick");
        bot_delete1.setMetodo("eliminar");

        tab_articulo.setId("tab_articulo");
        tab_articulo.setConexion(con_sql);
        tab_articulo.setSql("SELECT MVE_SECUENCIAL,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO FROM MVDETALLEVEHICULO where MVE_SECUENCIAL ="+tab_tabla.getValor("MVE_SECUENCIAL"));
        tab_articulo.getColumna("MVE_SECUENCIAL").setVisible(false);
        tab_articulo.getGrid().setColumns(4);
        tab_articulo.setLectura(true);
        tab_articulo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_articulo);
        Grid gri_modelo = new Grid();
        gri_modelo.setColumns(10);
        gri_modelo.getChildren().add(new Etiqueta("Accesorio :"));
        gri_modelo.getChildren().add(txt_item);
        gri_modelo.getChildren().add(new Etiqueta("Cantidad :"));
        gri_modelo.getChildren().add(txt_cantidad);
        gri_modelo.getChildren().add(new Etiqueta("Estado :"));
        gri_modelo.getChildren().add(txt_estado);
        Boton bot_delete = new Boton();
        bot_delete.setValue("ELIMINAR");
        bot_delete.setIcon("ui-icon-closethick");
        bot_delete.setMetodo("eliminar");
               
        Boton bot_save = new Boton();
        bot_save.setValue("GUARDAR");
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("ing");
        gri_modelo.getChildren().add(bot_save);
        gri_modelo.getChildren().add(bot_delete);
        pan_opcion1.getChildren().add(gri_modelo);
        
        Division div = new Division();
        div.dividir3(tpg, pan_opcion1,tpa, "50%", "42%","h");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
        //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        pan_opcion1.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujaIngreso();
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
            set_modelo.getTab_seleccion().setSql("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODEL' and LIS_ESTADO = 1 and DEPENDENCI = '"+set_tipo.getValorSeleccionado()+"'");
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
        if(tmarca.getValue()!= null && tmarca.toString().isEmpty()==false){
            Integer numero = Integer.parseInt(aCombustible.ParametrosMax("MARCA"));
            Integer cantidad=0;
            cantidad=numero +1;
            aCombustible.getParametros(String.valueOf(cantidad),tmarca.getValue()+"","MARCA","N/D",utilitario.getVariable("NICK"),utilitario.getFechaActual());
            tmarca.limpiar();
        }
    }
    
    public void insTipo(){
        if(ttipo.getValue()!= null && ttipo.toString().isEmpty()==false){
            Integer numero = Integer.parseInt(aCombustible.ParametrosMax("TIPO"));
            Integer cantidad=0;
            cantidad=numero +1;
            aCombustible.getParametros(String.valueOf(cantidad),ttipo.getValue()+"","TIPO",set_marca.getValorSeleccionado(),utilitario.getVariable("NICK"),utilitario.getFechaActual());
            ttipo.limpiar();
        }
    }
    
    public void insModelo(){
        if(tmodelo.getValue()!= null && tmodelo.toString().isEmpty()==false){
            Integer numero = Integer.parseInt(aCombustible.ParametrosMax("MODELO"));
            Integer cantidad=0;
            cantidad=numero +1;
            aCombustible.getParametros(String.valueOf(cantidad),tmodelo.getValue()+"","MODEL",set_tipo.getValorSeleccionado(),utilitario.getVariable("NICK"),utilitario.getFechaActual());
            tmodelo.limpiar();
        }
    }
    
    public void insVersion(){
        if(tversion.getValue()!= null && tversion.toString().isEmpty()==false){
            Integer numero = Integer.parseInt(aCombustible.ParametrosMax("VERSION"));
            Integer cantidad=0;
            cantidad=numero +1;
            aCombustible.getParametros(String.valueOf(cantidad),tversion.getValue()+"","VERSI",set_modelo.getValorSeleccionado(),utilitario.getVariable("NICK"),utilitario.getFechaActual());
            tversion.limpiar();
        }
    }
    
    public void cargarTipo(){
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as TIPO FROM MVLISTA WHERE TAB_CODIGO = 'tipo' and LIS_ESTADO = 1 and DEPENDENCI ='"+tab_tabla.getValor("MVE_MARCA")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_TIPO","");//actualiza solo componentes
    }
    
    public void cargarModelo(){
        tab_tabla.getColumna("MVE_MODELO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as MODELO FROM MVLISTA WHERE TAB_CODIGO = 'MODEL' and LIS_ESTADO = 1 and DEPENDENCI ='"+tab_tabla.getValor("MVE_TIPO")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_MODELO","");//actualiza solo componentes
    }
    
    public void cargarVersion(){
        tab_tabla.getColumna("MVE_VERSION").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as VERSION FROM MVLISTA WHERE TAB_CODIGO = 'VERSI' and LIS_ESTADO = 1 and DEPENDENCI='"+tab_tabla.getValor("MVE_MODELO")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_VERSION","");//actualiza solo componentes
    }
    
    public void activarCasilla(){
        if(tab_tabla.getValor("MVE_TIPOMEDICION").equals("HORAS")){
            tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(true);
            tab_tabla.getColumna("MVE_HOROMETRO").setLectura(false);
            utilitario.addUpdate("tab_tabla");
        }else{
            tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(false);
            tab_tabla.getColumna("MVE_HOROMETRO").setLectura(true);
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void aceptoDialogoc() {
        dia_dialogoC.Limpiar();
        dia_dialogoC.setDialogo(gridc);
        grid_co.getChildren().add(set_conductor);
        set_conductor.setId("set_conductor");
        set_conductor.setConexion(con_postgres);
        set_conductor.setSql("SELECT cod_empleado, cedula_pass,nombres\n" +
                "FROM srh_empleado\n" +
                "where cod_cargo in (SELECT cod_cargo FROM srh_cargos WHERE nombre_cargo like '%CHOFER%') and estado = 1\n" +
                "order by nombres");
        set_conductor.getColumna("nombres").setFiltro(true);
        set_conductor.setRows(12);
        set_conductor.setTipoSeleccion(false);
        dia_dialogoC.setDialogo(grid_co);
        set_conductor.dibujar();
        dia_dialogoC.dibujar();
    }
    
    public void aceptoConductor(){
        if (set_conductor.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato =aCombustible.getChofer(set_conductor.getValorSeleccionado());
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("MVE_CONDUCTOR", tab_dato.getValor("nombres"));
                tab_tabla.setValor("MVE_ASIGNADO", tab_dato.getValor("activo"));
                utilitario.addUpdate("tab_tabla");
                dia_dialogoC.cerrar();
            }else{
                utilitario.agregarMensajeInfo("No existen Datos", "");
            }
        }
    }
    
    public void endMarca(){
        
    }
    
    public void endTipo(){
        
    }
    
    public void endModelo(){
        
    }
    
    public void endVersion(){
        
    }
    
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            tab_tabla.insertar();
        }
        tab_articulo.actualizar();
    }

    @Override
    public void guardar() {
        if(tab_tabla.guardar()){
            con_sql.guardarPantalla(); 
        }
    }
    
    public void seleccionar_tabla(SelectEvent evt) {
        tab_articulo.seleccionarFila(evt);
    }
    public void ing(){
        aCombustible.getMVDetalle(tab_tabla.getValor("MVE_SECUENCIAL"), txt_item.getValue()+"", Double.valueOf(txt_cantidad.getValue()+""), txt_estado.getValue()+"");
        tab_articulo.actualizar();
    }
    
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public SeleccionTabla getSet_version() {
        return set_version;
    }

    public void setSet_version(SeleccionTabla set_version) {
        this.set_version = set_version;
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

    public SeleccionTabla getSet_ingresos() {
        return set_ingresos;
    }

    public void setSet_ingresos(SeleccionTabla set_ingresos) {
        this.set_ingresos = set_ingresos;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
