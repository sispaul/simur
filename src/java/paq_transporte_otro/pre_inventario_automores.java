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
public class pre_inventario_automores extends Pantalla{
    
    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    
    private Tabla set_marca = new Tabla();
    private Tabla set_tipo = new Tabla();
    private Tabla set_modelo = new Tabla();
    private Tabla set_version = new Tabla();
    private Tabla set_conductor = new Tabla();
    private Tabla set_accesorio = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //Dialogo de Ingreso de tablas
    private Dialogo dia_dialogoC = new Dialogo();
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogot = new Dialogo();
    private Dialogo dia_dialogom = new Dialogo();
    private Dialogo dia_dialogov = new Dialogo();
    private Dialogo dia_dialogoa = new Dialogo();
    private Grid grid_o = new Grid();
    private Grid grid_t = new Grid();
    private Grid grid_m = new Grid();
    private Grid grid_v = new Grid();
    private Grid grid_co = new Grid();
    private Grid grid_a = new Grid();
    private Grid grid = new Grid();
    private Grid gridt = new Grid();
    private Grid gridm = new Grid();
    private Grid gridv = new Grid();
    private Grid gridc = new Grid();
    private Grid grida = new Grid();
    
    //DECLARACION OBJETO TEXTO
    private Texto tmarca = new Texto();
    private Texto ttipo = new Texto();
    private Texto tmodelo = new Texto();
    private Texto tversion = new Texto();
    private Texto taccesorio = new Texto();
    private Texto txt_cantidad = new Texto();
    private Texto txt_estado = new Texto();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_inventario_automores() {
        
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
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("INGRESO DE VEHICULOS");
        agregarComponente(pan_opcion);
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT v.MVE_SECUENCIAL,v.MVE_PLACA,m.MVMARCA_DESCRIPCION,o.MVMODELO_DESCRIPCION,v.MVE_CHASIS\n" +
                "FROM MVVEHICULO AS v ,MVMARCA AS m ,MVMODELO o\n" +
                "WHERE v.MVE_MARCA = m.MVMARCA_ID and v.MVE_MODELO = o.MVMODELO_ID");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_marca = new Boton();
        bot_marca.setValue("INSERTAR MARCA");
        bot_marca.setIcon("ui-icon-gear");
        bot_marca.setMetodo("ing_marcas");
        bar_botones.agregarBoton(bot_marca);
        
        Boton bot_accesorio = new Boton();
        bot_accesorio.setValue("INSERTAR ACCESORIOS");
        bot_accesorio.setIcon("ui-icon-wrench");
        bot_accesorio.setMetodo("ing_accesorio");
        bar_botones.agregarBoton(bot_accesorio);
        
        //para marca,tipo,modelo,version
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
        dia_dialogo.setTitle("INGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acep_marcas");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);
        
        set_marca.setId("set_marca");
        set_marca.setConexion(con_sql);
        set_marca.setSql("SELECT MVMARCA_ID, MVMARCA_DESCRIPCION FROM MVMARCA order by MVMARCA_DESCRIPCION");
        set_marca.getColumna("MVMARCA_DESCRIPCION").setFiltro(true);
        set_marca.setTipoSeleccion(false);
        set_marca.setRows(10);
        set_marca.dibujar();
        
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
        
        dibujaIngreso();
        
        dia_dialogoC.setId("dia_dialogoC");
        dia_dialogoC.setTitle("CONDUCTORES - DISPONIBLES"); //titulo
        dia_dialogoC.setWidth("40%"); //siempre en porcentajes  ancho
        dia_dialogoC.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoC.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoC.getBot_aceptar().setMetodo("aceptoConductor");
        grid_co.setColumns(2);
        agregarComponente(dia_dialogoC);
        
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
        
    }
    //PANTALLA DE REGISTRO DE INFORMACIÓN
    
    public void dibujaIngreso(){
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvvehiculo", "mve_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mve_secuencial=-1");
        } else {
            tab_tabla.setCondicion("mve_secuencial=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM MVMARCA order by MVMARCA_DESCRIPCION");
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM MVTIPO ORDER BY MVTIPO_DESCRIPCION");
        tab_tabla.getColumna("MVE_MODELO").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM MVMODELO order by MVMODELO_DESCRIPCION");
        tab_tabla.getColumna("MVE_VERSION").setCombo("SELECT MVERSION_ID,MVERSION_DESCRIPCION FROM MVVERSION ORDER BY MVERSION_DESCRIPCION");
        tab_tabla.getColumna("MVE_TIPO_COMBUSTIBLE").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_tabla.getColumna("MVE_MARCA").setMetodoChange("cargarTipo");
        tab_tabla.getColumna("MVE_TIPO").setMetodoChange("cargarModelo");
        tab_tabla.getColumna("MVE_MODELO").setMetodoChange("cargarVersion");
        tab_tabla.getColumna("MVE_TIPOMEDICION").setMetodoChange("activarCasilla");
        tab_tabla.getColumna("MVE_CONDUCTOR").setMetodoChange("aceptoDialogoc");
        tab_tabla.getColumna("MVE_TIPOCODIGO").setMetodoChange("activarTipo");
        tab_tabla.getColumna("MVE_KILOMETRAJE").setMetodoChange("recorrido");
        tab_tabla.getColumna("MVE_HOROMETRO").setMetodoChange("recorrido");
        tab_tabla.getColumna("MVE_HOROMETRO").setMascara("9999:99");
        tab_tabla.getColumna("MVE_LOGININGRESO").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("MVE_FECHAINGRESO").setValorDefecto(utilitario.getFechaActual());
        List list = new ArrayList();
        Object fil1[] = {
            "1", "KILOMETROS"
        };
        Object fil2[] = {
            "2", "HORAS"
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
        tab_tabla.getColumna("MVE_RENDIMIENTO").setLectura(true);
        tab_tabla.getColumna("MVE_ASIGNADO").setVisible(false);
        tab_tabla.getColumna("MVE_CI_CONDUCTOR").setVisible(false);
        tab_tabla.getColumna("MVE_OBSERVACIONES").setVisible(false);
        tab_tabla.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_FECHA_BORRADO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAINGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_FECHAACTUALI").setVisible(false);
        tab_tabla.getColumna("MVE_LOGINBORRADO").setVisible(false);
        tab_tabla.getColumna("MVE_NUMIMR").setVisible(false);
        tab_tabla.getColumna("MVE_TIPO_INGRESO").setVisible(false);
        tab_tabla.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla tpg = new PanelTabla();
        tpg.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir1(tpg);
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    
    //BUSQUEDA DE REGISTRO
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujaIngreso();
    }
    
    public void recorrido(){
        if(tab_tabla.getValor("MVE_KILOMETRAJE")!=null){
            tab_tabla.setValor("MVE_NUMIMR", "K");
            utilitario.addUpdate("tab_tabla");
       }else{
        String minutos =tab_tabla.getValor("MVE_HOROMETRO").substring(5,7);
        if(Integer.parseInt(minutos)<60){
            tab_tabla.setValor("MVE_NUMIMR", "M");
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensaje("Minutos No Deben Ser Mayor", "60");
        }
        }
    }
    
    //DATOS PARA VEHICULO
    //MARCA
    public void ing_marcas(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_o.getChildren().add(set_marca);
        dia_dialogo.setDialogo(grid_o);
        set_marca.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void insMarca(){
            TablaGenerica tab_dato =aCombustible.get_DuplicaDato(tmarca.getValue()+"");
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Marca ya se Encuentra Registrada", "");
            }else{
                if(tmarca.getValue()!= null && tmarca.toString().isEmpty()==false){
                aCombustible.getParametrom(tmarca.getValue()+"",utilitario.getVariable("NICK"));
                tmarca.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Marca");
                set_marca.actualizar();
                cargarMarca();
            }
        }
    }
    
    public void endMarca(){
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
                aCombustible.deleteMarcas(Integer.parseInt(set_marca.getValorSeleccionado()));
                utilitario.agregarMensaje("Registro eliminado", "Marca");
                set_marca.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //TIPO
    public void acep_marcas(){
        if (set_marca.getValorSeleccionado() != null && set_marca.getValorSeleccionado().isEmpty() == false) {
            dia_dialogot.Limpiar();
            dia_dialogot.setDialogo(gridt);
            grid_t.getChildren().add(set_tipo);
            set_tipo.setId("set_tipo");
            set_tipo.setConexion(con_sql);
            set_tipo.setSql("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM dbo.MVTIPO where MVMARCA_ID ="+set_marca.getValorSeleccionado()+" order by MVTIPO_DESCRIPCION");
            set_tipo.getColumna("MVTIPO_DESCRIPCION").setFiltro(true);
            set_tipo.setTipoSeleccion(false);
            set_tipo.setRows(10);
            set_tipo.dibujar();
            dia_dialogot.setDialogo(grid_t);
            dia_dialogot.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insTipo(){
        TablaGenerica tab_dato1 =aCombustible.get_DuplicarDato(ttipo.getValue()+"",Integer.parseInt(set_marca.getValorSeleccionado()));
        if (!tab_dato1.isEmpty()) {
            utilitario.agregarMensaje("Tipo ya se Encuentra Registrado", "");
        }else{
            if(ttipo.getValue()!= null && ttipo.toString().isEmpty()==false){
                aCombustible.getParametrot(ttipo.getValue()+"", utilitario.getVariable("NICK"), Integer.parseInt(set_marca.getValorSeleccionado()));
                ttipo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Tipo");
                set_tipo.actualizar();
            }
        }
    }
        
    public void endTipo(){
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteTipos(Integer.parseInt(set_tipo.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro eliminado", "Tipo");
            set_tipo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    //MODELO
    public void acepta_tipo(){
        if (set_tipo.getValorSeleccionado() != null && set_tipo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogom.Limpiar();
            dia_dialogom.setDialogo(gridm);
            grid_m.getChildren().add(set_modelo);
            set_modelo.setId("set_modelo");
            set_modelo.setConexion(con_sql);
            set_modelo.setSql("SELECT MVMODELO_ID, MVMODELO_DESCRIPCION FROM MVMODELO where MVTIPO_ID ="+set_tipo.getValorSeleccionado()+"  order by MVMODELO_DESCRIPCION");
            set_modelo.getColumna("MVMODELO_DESCRIPCION").setFiltro(true);
            set_modelo.setTipoSeleccion(false);
            set_modelo.setRows(10);
            set_modelo.dibujar();dia_dialogom.setDialogo(grid_m);
            dia_dialogom.dibujar();
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insModelo(){
        TablaGenerica tab_dato2 =aCombustible.get_DuplicamDato(tmodelo.getValue()+"", Integer.parseInt(set_tipo.getValorSeleccionado()));
        if (!tab_dato2.isEmpty()) {
            utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
        }else{
            if(tmodelo.getValue()!= null && tmodelo.toString().isEmpty()==false){
                aCombustible.getParametromo(tmodelo.getValue()+"",utilitario.getVariable("NICK"),Integer.parseInt(set_tipo.getValorSeleccionado()));
                tmodelo.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Modelo");
                set_modelo.actualizar();
            }
        }
    }
    
    public void endModelo(){
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
                        aCombustible.deleteModelos(Integer.parseInt(set_modelo.getValorSeleccionado()));
                        utilitario.agregarMensaje("Registro eliminado", "Modelo");
                        set_modelo.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        } 
    }
    //VERSIÓN
    public void acepta_modelo(){
        if (set_modelo.getValorSeleccionado() != null && set_modelo.getValorSeleccionado().isEmpty() == false) {
            dia_dialogov.Limpiar();
            dia_dialogov.setDialogo(gridv);
            grid_v.getChildren().add(set_version);
            set_version.setId("set_version");
            set_version.setConexion(con_sql);
            set_version.setSql("SELECT MVERSION_ID,MVERSION_DESCRIPCION FROM MVVERSION where MVMODELO_ID ="+set_modelo.getValorSeleccionado()+" order by MVERSION_DESCRIPCION");
            set_version.getColumna("MVERSION_DESCRIPCION").setFiltro(true);
            set_version.setTipoSeleccion(false);
            set_version.setRows(10);
            set_version.dibujar();
            dia_dialogov.setDialogo(grid_v);
            dia_dialogov.dibujar();
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    public void insVersion(){
            TablaGenerica tab_dato3 =aCombustible.get_DuplicavDato(tversion.getValue()+"", Integer.parseInt(set_modelo.getValorSeleccionado()));
            if (!tab_dato3.isEmpty()) {
                utilitario.agregarMensaje("Modelo ya se Encuentra Registrado", "");
            }else{
                if(tversion.getValue()!= null && tversion.toString().isEmpty()==false){
                    aCombustible.getParametrove(tversion.getValue()+"",utilitario.getVariable("NICK"),Integer.parseInt(set_modelo.getValorSeleccionado()));
                    tversion.limpiar();
                    utilitario.agregarMensaje("Registro Guardado", "Versión");
                    set_version.actualizar();
                }
            }
    }
    
    public void endVersion(){
        if (set_version.getValorSeleccionado() != null && set_version.getValorSeleccionado().isEmpty() == false) {
                            aCombustible.deleteversion(Integer.parseInt(set_version.getValorSeleccionado()));
                            utilitario.agregarMensaje("Registro eliminado", "Versión");
                            set_version.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }

    //ACTUALIZAR COMBOS
    public void cargarMarca(){
        tab_tabla.getColumna("MVE_MARCA").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM MVMARCA order by MVMARCA_DESCRIPCION");
        utilitario.addUpdateTabla(tab_tabla,"MVE_MARCA","");//actualiza solo componentes
    }
    
    public void cargarTipo(){
        tab_tabla.getColumna("MVE_TIPO").setCombo("SELECT MVTIPO_ID,MVTIPO_DESCRIPCION FROM MVTIPO where MVMARCA_ID ='"+tab_tabla.getValor("MVE_MARCA")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_TIPO","");//actualiza solo componentes
    }
    
    public void cargarModelo(){
        tab_tabla.getColumna("MVE_MODELO").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM MVMODELO where MVTIPO_ID ='"+tab_tabla.getValor("MVE_TIPO")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_MODELO","");//actualiza solo componentes
    }
    
    public void cargarVersion(){
        tab_tabla.getColumna("MVE_VERSION").setCombo("SELECT MVERSION_ID,MVERSION_DESCRIPCION FROM MVVERSION where MVMODELO_ID='"+tab_tabla.getValor("MVE_MODELO")+"'");
        utilitario.addUpdateTabla(tab_tabla,"MVE_VERSION","");//actualiza solo componentes
    }

    //DATOS PARA CONDUCTOR
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
                tab_tabla.setValor("MVE_CI_CONDUCTOR", tab_dato.getValor("cod_empleado"));
                tab_tabla.setValor("MVE_ASIGNADO", tab_dato.getValor("activo"));
                utilitario.addUpdate("tab_tabla");
                dia_dialogoC.cerrar();
            }else{
                utilitario.agregarMensajeInfo("No existen Datos", "");
            }
        }
    }
    
    //COMPONENTES ADICIONALES ACTIVAR
    public void activarCasilla(){
        if(tab_tabla.getValor("MVE_TIPOMEDICION").equals("2")){
            tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(true);
            tab_tabla.getColumna("MVE_HOROMETRO").setLectura(false);
            tab_tabla.getColumna("MVE_RENDIMIENTO").setLectura(false);
            utilitario.addUpdate("tab_tabla");
        }else{
            tab_tabla.getColumna("MVE_KILOMETRAJE").setLectura(false);
            tab_tabla.getColumna("MVE_HOROMETRO").setLectura(true);
            tab_tabla.getColumna("MVE_RENDIMIENTO").setLectura(true);
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void activarTipo(){
        if(tab_tabla.getValor("MVE_TIPOCODIGO").equals("placa")){
            tab_tabla.getColumna("MVE_PLACA").setLectura(false);
            tab_tabla.setValor("MVE_PLACA", null);
            tab_tabla.setValor("MVE_TIPO_INGRESO", "A");
            utilitario.addUpdate("tab_tabla");
        }else{
            tab_tabla.getColumna("MVE_PLACA").setLectura(false);
            tab_tabla.setValor("MVE_PLACA", null);
            tab_tabla.setValor("MVE_TIPO_INGRESO", "M");
            utilitario.addUpdate("tab_tabla");
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
    
    public void infoActivo(){
        if(tab_tabla.getValor("MVE_TIPOCODIGO").equals("CODIGO")){
             TablaGenerica tab_dato =aCombustible.getActivos(tab_tabla.getValor("MVE_PLACA"));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarNotificacionInfo(tab_dato.getValor("descripcion"), null);
            }
        }
    }
    
    //para ingreso de accesorios
    public void ing_accesorio(){
        if(tab_tabla.getValor("mve_secuencial")!=null && tab_tabla.getValor("mve_secuencial").isEmpty() == false){
            dia_dialogoa.Limpiar();
            dia_dialogoa.setDialogo(grida);
            grid_a.getChildren().add(set_accesorio);
            set_accesorio.setId("set_accesorio");
            set_accesorio.setConexion(con_sql);
            set_accesorio.setSql("SELECT MDV_CODIGO,MDV_DETALLE,MDV_CANTIDAD,MDV_ESTADO FROM MVDETALLEVEHICULO WHERE MDV_ESTADO <> 'DE BAJA' AND MVE_SECUENCIAL='"+tab_tabla.getValor("mve_secuencial")+"' ORDER BY MDV_DETALLE");
            set_accesorio.getColumna("MDV_DETALLE").setFiltro(true);
            set_accesorio.setRows(12);
            set_accesorio.setTipoSeleccion(false);
            dia_dialogoa.setDialogo(grid_a);
            set_accesorio.dibujar();
            dia_dialogoa.dibujar();
            
        }else{
            utilitario.agregarMensaje("Ubicarse en Registro", "");
        }
    }
    
    public void insAccesorio(){
        
        if(taccesorio.getValue()!= null && taccesorio.toString().isEmpty()==false){
            if(txt_estado.getValue()!= null && txt_estado.toString().isEmpty()==false){
                aCombustible.getParametacce(tab_tabla.getValor("mve_secuencial"), taccesorio.getValue()+"", txt_cantidad.getValue()+"", txt_estado.getValue()+"", aCombustible.ParametrosAcc());
                taccesorio.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Accesorio");
                set_accesorio.actualizar();
            }
        }
    }
    
    public void endAccesorio(){
        if (set_accesorio.getValorSeleccionado() != null && set_accesorio.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteaccesorio(set_accesorio.getValorSeleccionado());
            utilitario.agregarMensaje("Registro eliminado", "Accesorio");
            set_accesorio.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
    }
    
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_tabla.guardar()){
            con_sql.guardarPantalla(); 
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public Tabla getSet_marca() {
        return set_marca;
    }

    public void setSet_marca(Tabla set_marca) {
        this.set_marca = set_marca;
    }

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_modelo() {
        return set_modelo;
    }

    public void setSet_modelo(Tabla set_modelo) {
        this.set_modelo = set_modelo;
    }

    public Tabla getSet_version() {
        return set_version;
    }

    public void setSet_version(Tabla set_version) {
        this.set_version = set_version;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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

    public Tabla getSet_accesorio() {
        return set_accesorio;
    }

    public void setSet_accesorio(Tabla set_accesorio) {
        this.set_accesorio = set_accesorio;
    }
    
}
