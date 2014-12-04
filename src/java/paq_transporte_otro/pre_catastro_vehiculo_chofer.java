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
    private Tabla set_marcas = new Tabla();
    private Tabla set_tipos = new Tabla();
    private Tabla set_modelos = new Tabla();
    private Tabla set_versions = new Tabla();
    
    //DECLARACION OBJETOS PARA PARAMETROS
    private SeleccionTabla set_ingresos = new SeleccionTabla();
    
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
    
    String tipo, modelo,version;
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
        bot_registro.setMetodo("ing_marcas");
        bar_botones.agregarBoton(bot_registro);
        
        //Dialogos de Ingreso de parametros necesarios para vehiculo
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
        dia_dialogo.setTitle("IINGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acepta_marca");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);
        
        set_marcas.setId("set_marcas");
        set_marcas.setConexion(con_sql);
        set_marcas.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1 order by LIS_NOMBRE");
        set_marcas.getColumna("LIS_NOMBRE").setFiltro(true);
        set_marcas.setTipoSeleccion(false);
        set_marcas.setRows(10);
        set_marcas.dibujar();
        
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
        grid_o.getChildren().add(set_marcas);
        dia_dialogo.setDialogo(grid_o);
        set_marcas.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void acepta_marca(){
        tipo = null;
        tipo = set_marcas.getValorSeleccionado();
        if (set_marcas.getValorSeleccionado() != null && set_marcas.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(set_marcas.getValorSeleccionado()+"","MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                dia_dialogot.Limpiar();
                dia_dialogot.setDialogo(gridt);
                grid_t.getChildren().add(set_tipos);
                set_tipos.setId("set_tipos");
                set_tipos.setConexion(con_sql);
                set_tipos.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA WHERE TAB_CODIGO = 'TIPO' and LIS_ESTADO = 1 and dependenci ='"+tab_dato.getValor("LIS_NOMBRE")+"' order by LIS_NOMBRE");
                set_tipos.getColumna("LIS_NOMBRE").setFiltro(true);
                set_tipos.setTipoSeleccion(false);
                set_tipos.setRows(10);
                set_tipos.dibujar();
                dia_dialogot.setDialogo(grid_t);
                dia_dialogot.dibujar();
            }else{
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void acepta_tipo(){
        modelo=null;
        modelo=set_tipos.getValorSeleccionado();
        if (set_tipos.getValorSeleccionado() != null && set_tipos.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo+"","MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(set_tipos.getValorSeleccionado()+"","TIPO", tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    dia_dialogom.Limpiar();
                    dia_dialogom.setDialogo(gridm);
                    grid_m.getChildren().add(set_modelos);
                    set_modelos.setId("set_modelos");
                    set_modelos.setConexion(con_sql);
                    set_modelos.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA WHERE TAB_CODIGO = 'MODEL' and LIS_ESTADO = 1 and dependenci ='"+tab_dato1.getValor("LIS_NOMBRE")+"' order by LIS_NOMBRE");
                    set_modelos.getColumna("LIS_NOMBRE").setFiltro(true);
                    set_modelos.setTipoSeleccion(false);
                    set_modelos.setRows(10);
                    set_modelos.dibujar();
                    dia_dialogom.setDialogo(grid_m);
                    dia_dialogom.dibujar();
                }
            }
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void acepta_modelo(){
        version=null;
        version=set_modelos.getValorSeleccionado();
        if (set_modelos.getValorSeleccionado() != null && set_modelos.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo+"","MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(modelo+"","TIPO",tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    TablaGenerica tab_dato2 =aCombustible.get_ExtraDatos(set_modelos.getValorSeleccionado()+"","MODEL", tab_dato1.getValor("LIS_NOMBRE"));
                    if (!tab_dato2.isEmpty()) {
                        dia_dialogov.Limpiar();
                        dia_dialogov.setDialogo(gridv);
                        grid_v.getChildren().add(set_versions);
                        set_versions.setId("set_versions");
                        set_versions.setConexion(con_sql);
                        set_versions.setSql("SELECT LIS_ID,LIS_NOMBRE FROM MVLISTA WHERE TAB_CODIGO = 'VERSI' and LIS_ESTADO = 1 and dependenci ='"+tab_dato2.getValor("LIS_NOMBRE")+"' order by LIS_NOMBRE");
                        set_versions.getColumna("LIS_NOMBRE").setFiltro(true);
                        set_versions.setTipoSeleccion(false);
                        set_versions.setRows(10);
                        set_versions.dibujar();
                        dia_dialogov.setDialogo(grid_v);
                        dia_dialogov.dibujar();
                    }
                }
            }
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
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
        tab_tabla.getColumna("MVE_TIPOCODIGO").setMetodoChange("activarTipo");
        tab_tabla.getColumna("MVE_PLACA").setMetodoChange("infoActivo");
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
        List lista = new ArrayList();
        Object fila1[] = {
            "PLACA", "PLACA"
        };
        Object fila2[] = {
            "CODIGO", "CODIGO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_tabla.getColumna("MVE_TIPOCODIGO").setCombo(lista);
        List listad = new ArrayList();
        Object filas1[] = {
            "ACTIVO", "ACTIVO"
        };
        Object filas2[] = {
            "PASIVO", "PASIVO"
        };
        listad.add(filas1);;
        listad.add(filas2);;
        tab_tabla.getColumna("MVE_ESTADO_REGISTRO").setCombo(listad);
        List listes = new ArrayList();
        Object filase1[] = {
            "ACTIVO", "ACTIVO"
        };
        Object filase2[] = {
            "MANTENIMIENTO", "MANTENIMIENTO"
        };
        Object filase3[] = {
            "DE BAJA", "DE BAJA"
        };
        listes.add(filase1);;
        listes.add(filase2);;
        listes.add(filase3);;
        tab_tabla.getColumna("MVE_ESTADO").setCombo(listes);
        tab_tabla.getColumna("MVE_PLACA").setLectura(true);
        tab_tabla.getColumna("MVE_HOROMETRO").setLectura(true);
        tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(true);
        tab_tabla.getColumna("MVE_ASIGNADO").setVisible(false);
        tab_tabla.getColumna("MVE_OBSERVACIONES").setVisible(false);
        tab_tabla.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINACTUALI").setVisible(false);
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
    
    public void insMarca(){
        if(tmarca.getValue()!= null && tmarca.toString().isEmpty()==false){
            TablaGenerica tab_dato =aCombustible.get_DuplicaDatos(tmarca.getValue()+"","MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Marca ya se Encuentra Registrada", "");
            }else{
                Integer numero = Integer.parseInt(aCombustible.ParametrosMax("MARCA"));
                Integer cantidad=0;
                cantidad=numero +1;
                aCombustible.getParametros(String.valueOf(cantidad),tmarca.getValue()+"","MARCA","N/D",utilitario.getVariable("NICK"));
                tmarca.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Marca");
                set_marcas.actualizar();
                cargarMarca();
            }
        }
    }
    
    public void insTipo(){
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_DuplicaDatos(ttipo.getValue()+"","TIPO", tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    utilitario.agregarMensaje("Tipo ya se Encuentra Registrado", "");
                }else{
                    if(ttipo.getValue()!= null && ttipo.toString().isEmpty()==false){
                    Integer numero = Integer.parseInt(aCombustible.ParametrosMax("TIPO"));
                    Integer cantidad=0;
                    cantidad=numero +1;
                    aCombustible.getParametros(String.valueOf(cantidad),ttipo.getValue()+"","TIPO",tab_dato.getValor("LIS_NOMBRE"),utilitario.getVariable("NICK"));
                    tmarca.limpiar();;
                    ttipo.limpiar();
                    utilitario.agregarMensaje("Registro Guardado", "Tipo");
                    set_tipos.actualizar();
                }
            }
        }
    }
    
    public void insModelo(){
        TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(modelo,"TIPO", tab_dato.getValor("LIS_NOMBRE"));
            if (!tab_dato1.isEmpty()) {
                TablaGenerica tab_dato2 =aCombustible.get_DuplicaDatos(tmodelo.getValue()+"","MODEL", tab_dato1.getValor("LIS_NOMBRE"));
                if (!tab_dato2.isEmpty()) {
                    utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
                }else{
                    if(tmodelo.getValue()!= null && tmodelo.toString().isEmpty()==false){
                        Integer numero = Integer.parseInt(aCombustible.ParametrosMax("MODEL"));
                        Integer cantidad=0;
                        cantidad=numero +1;
                        aCombustible.getParametros(String.valueOf(cantidad),tmodelo.getValue()+"","MODEL",tab_dato1.getValor("LIS_NOMBRE"),utilitario.getVariable("NICK"));
                        tmarca.limpiar();;
                        tmodelo.limpiar();
                        utilitario.agregarMensaje("Registro Guardado", "Modelo");
                        set_modelos.actualizar();
                    }
                }
            }
        }
    }
    
    public void insVersion(){
        TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(modelo,"TIPO", tab_dato.getValor("LIS_NOMBRE"));
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato2 =aCombustible.get_ExtraDatos(version,"MODEL", tab_dato1.getValor("LIS_NOMBRE"));
                if (!tab_dato2.isEmpty()) {
                    TablaGenerica tab_dato3 =aCombustible.get_DuplicaDatos(tversion.getValue()+"","VERSI", tab_dato2.getValor("LIS_NOMBRE"));
                    if (!tab_dato3.isEmpty()) {
                        utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
                    }else{
                        if(tversion.getValue()!= null && tversion.toString().isEmpty()==false){
                            Integer numero = Integer.parseInt(aCombustible.ParametrosMax("VERSI"));
                            Integer cantidad=0;
                            cantidad=numero +1;
                            aCombustible.getParametros(String.valueOf(cantidad),tversion.getValue()+"","VERSI",tab_dato2.getValor("LIS_NOMBRE"),utilitario.getVariable("NICK"));
                            tmarca.limpiar();
                            tversion.limpiar();
                            utilitario.agregarMensaje("Registro Guardado", "Versión");
                            set_versions.actualizar();
                        }
                    }
                }
            }
        }
    }
    
    public void cargarMarca(){
         tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE AS MARCA FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
        utilitario.addUpdateTabla(tab_tabla,"MVE_MARCA","");//actualiza solo componentes
    }
    
    public void cargarTipo(){
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE as TIPO FROM MVLISTA WHERE TAB_CODIGO = 'TIPO' and LIS_ESTADO = 1 and DEPENDENCI ='"+tab_tabla.getValor("MVE_MARCA")+"'");
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
    
    public void activarTipo(){
        tab_tabla.getColumna("MVE_PLACA").setLectura(false);
        tab_tabla.setValor("MVE_PLACA", null);
        utilitario.addUpdate("tab_tabla");
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
        if (set_marcas.getValorSeleccionado() != null && set_marcas.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(set_marcas.getValorSeleccionado()+"","MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                aCombustible.deleteMarca(tab_dato.getValor("LIS_NOMBRE"));
                utilitario.agregarMensaje("Registro eliminado", "Marca");
                set_marcas.actualizar();
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void endTipo(){
        if (set_tipos.getValorSeleccionado() != null && set_tipos.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(set_tipos.getValorSeleccionado()+"","TIPO", tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    String mensaje = "TIPO";
                    aCombustible.deleteParam(tab_dato1.getValor("LIS_NOMBRE"), mensaje, tab_dato.getValor("LIS_NOMBRE"));
                    utilitario.agregarMensaje("Registro eliminado", "Tipo");
                    set_tipos.actualizar();
                }
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void endModelo(){
        if (set_modelos.getValorSeleccionado() != null && set_modelos.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(modelo,"TIPO", tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    TablaGenerica tab_dato2 =aCombustible.get_ExtraDatos(set_modelos.getValorSeleccionado()+"","MODEL", tab_dato1.getValor("LIS_NOMBRE"));
                    if (!tab_dato2.isEmpty()) {
                        String mensaje = "MODEL";
                        aCombustible.deleteParam(tab_dato2.getValor("LIS_NOMBRE"), mensaje, tab_dato1.getValor("LIS_NOMBRE"));
                        utilitario.agregarMensaje("Registro eliminado", "Modelo");
                        set_modelos.actualizar();
                    }

                }
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void endVersion(){
        if (set_versions.getValorSeleccionado() != null && set_versions.getValorSeleccionado().isEmpty() == false) {
            TablaGenerica tab_dato =aCombustible.get_ExtraDatos(tipo,"MARCA", "N/D");
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =aCombustible.get_ExtraDatos(modelo,"TIPO", tab_dato.getValor("LIS_NOMBRE"));
                if (!tab_dato1.isEmpty()) {
                    TablaGenerica tab_dato2 =aCombustible.get_ExtraDatos(version,"MODEL", tab_dato1.getValor("LIS_NOMBRE"));
                    if (!tab_dato2.isEmpty()) {
                        TablaGenerica tab_dato3 =aCombustible.get_ExtraDatos(set_versions.getValorSeleccionado()+"","VERSI", tab_dato2.getValor("LIS_NOMBRE"));
                        if (!tab_dato3.isEmpty()) {
                            String mensaje = "VERSI";
                            aCombustible.deleteParam(tab_dato3.getValor("LIS_NOMBRE"), mensaje, tab_dato2.getValor("LIS_NOMBRE"));
                            utilitario.agregarMensaje("Registro eliminado", "Versión");
                            set_versions.actualizar();
                        }
                    }
                }
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void infoActivo(){
        if(tab_tabla.getValor("MVE_TIPOCODIGO").equals("CODIGO")){
             TablaGenerica tab_dato =aCombustible.getActivos(tab_tabla.getValor("MVE_PLACA"));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarNotificacionInfo(tab_dato.getValor("descripcion"), null);
            }
        }
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

    public Tabla getSet_marcas() {
        return set_marcas;
    }

    public void setSet_marcas(Tabla set_marcas) {
        this.set_marcas = set_marcas;
    }

    public Tabla getSet_tipos() {
        return set_tipos;
    }

    public void setSet_tipos(Tabla set_tipos) {
        this.set_tipos = set_tipos;
    }

    public Tabla getSet_modelos() {
        return set_modelos;
    }

    public void setSet_modelos(Tabla set_modelos) {
        this.set_modelos = set_modelos;
    }

    public Tabla getSet_versions() {
        return set_versions;
    }

    public void setSet_versions(Tabla set_versions) {
        this.set_versions = set_versions;
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
    
}
