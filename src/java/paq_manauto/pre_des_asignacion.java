/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

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
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_manauto.ejb.manauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_des_asignacion extends Pantalla{

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    
    //tablas
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla set_accesorio = new Tabla();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //
    private Dialogo dia_dialogoa = new Dialogo();
    private Grid grid_a = new Grid();
    private Grid grida = new Grid();
    
    //
    private Texto taccesorio = new Texto();
    private Texto txt_cantidad = new Texto();
    private Texto txt_estado = new Texto();
    private Texto tplaca = new Texto();
    
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
     
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_des_asignacion() {
        //datos de usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT a.mav_secuencial,v.mve_placa,m.mvmarca_descripcion,v.mve_ano\n" +
                "FROM mvasignar_vehiculo a\n" +
                "INNER JOIN mv_vehiculo v ON a.mve_secuencial = v.mve_secuencial\n" +
                "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n" +
                "where mav_estado_tramite = 'Cambio'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar : "));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_save = new Boton();
        bot_save.setValue("ACCESORIOS");
        bot_save.setIcon("ui-icon-extlink");
        bot_save.setMetodo("dibujar");
        bar_botones.agregarComponente(bot_save);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("ACTA ENTREGA / RECEPCIÓN");
        agregarComponente(pan_opcion);
        
        dibujaIngreso();
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
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

    public void dibujaIngreso(){
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvasignar_vehiculo", "mav_secuencial", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("mav_secuencial=-1");
        } else {
            tab_tabla.setCondicion("mav_secuencial=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("mav_fechaingreso").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("mav_loginingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial,\n" +
"((case when v.mve_placa is NULL then v.mve_codigo when v.mve_placa is not null then v.mve_placa end )||'/'||m.mvmarca_descripcion ||'/'||o.mvmodelo_descripcion ||'/'|| v.mve_color||'/'||v.mve_ano ) AS descripcion\n" +
"FROM mv_vehiculo v\n" +
"INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n" +
"INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id");
        tab_tabla.getColumna("mve_secuencial").setFiltroContenido();
        tab_tabla.getColumna("mve_secuencial").setMetodoChange("vehiculo");
        tab_tabla.getColumna("mav_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado order by nombres");
        tab_tabla.getColumna("mav_cargoemplea").setCombo("select cod_empleado,nombres from srh_empleado where estado =1 order by nombres");
        tab_tabla.getColumna("mav_cargoemplea").setFiltro(true);
        tab_tabla.getColumna("mav_cargoemplea").setMetodoChange("conductor");
            
        tab_tabla.getColumna("mav_autoriza").setValorDefecto(tab_consulta.getValor("NOM_USUA"));
        
        tab_tabla.getColumna("mav_nombre_cond").setVisible(false);
        tab_tabla.getColumna("mav_nomemplea").setVisible(false);
        tab_tabla.getColumna("mav_telefono_cond").setVisible(false);
        tab_tabla.getColumna("mav_cod_autoriza").setVisible(false);
        tab_tabla.getColumna("mav_autoriza").setVisible(false);
        tab_tabla.getColumna("mav_fechactuali").setVisible(false);
        tab_tabla.getColumna("mav_loginactuali").setVisible(false);
        tab_tabla.getColumna("mav_fechasignacion").setVisible(false);
        tab_tabla.getColumna("mav_loginingreso").setVisible(false);
        tab_tabla.getColumna("mav_fechadescargo").setVisible(false);
        tab_tabla.getColumna("mav_estado_asignacion").setVisible(false);
        tab_tabla.getColumna("mav_estado_registro").setVisible(false);
        tab_tabla.setTipoFormulario(true);
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
    
    //BUSQUEDA DE REGISTRO
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujaIngreso();
    }
    
    public void vehiculo (){
        TablaGenerica tab_datoi =aCombustible.getpedido(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_datoi.isEmpty()) {
            aCombustible.set_updatepedido(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            TablaGenerica tab_dato =aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mav_cod_conductor", tab_dato.getValor("mve_cod_conductor"));
                tab_tabla.setValor("mav_nombre_cond", tab_dato.getValor("mve_conductor"));
                tab_tabla.setValor("mav_departamento", tab_dato.getValor("mve_departamento"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
            }
        }else{
            TablaGenerica tab_dato =aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mav_cod_conductor", tab_dato.getValor("mve_cod_conductor"));
                tab_tabla.setValor("mav_nombre_cond", tab_dato.getValor("mve_conductor"));
                tab_tabla.setValor("mav_departamento", tab_dato.getValor("mve_departamento"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
            }
        }
    }
    
    public void conductor(){
            TablaGenerica tab_dato =aCombustible.getChofer(tab_tabla.getValor("mav_cargoemplea"));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mav_nomemplea", tab_dato.getValor("nombres"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeInfo("No existen Datos", "");
            }
    }
    
    public void tramite(){
        tab_tabla.setValor("mav_estado_tramite", "Cambio");
        utilitario.addUpdate("tab_tabla");
    }
    
    //Accesorios para asignacion
    public void dibujar(){
        dia_dialogoa.Limpiar();
        dia_dialogoa.setDialogo(grida);
        grid_a.getChildren().add(set_accesorio);
        set_accesorio.setId("set_accesorio");
        set_accesorio.setConexion(con_postgres);
        set_accesorio.setSql("SELECT mdv_codigo,mdv_detalle,mdv_cantidad,mdv_estado\n" +
                "FROM mvdetalle_vehiculo where mve_estado= '1' and mve_secuencial= "+tab_tabla.getValor("mve_secuencial"));
        set_accesorio.getColumna("mdv_detalle").setFiltro(true);
        set_accesorio.setRows(9);
        set_accesorio.setTipoSeleccion(false);
        dia_dialogoa.setDialogo(grid_a);
        set_accesorio.dibujar();
        dia_dialogoa.dibujar();
    }
    
    public void insAccesorio(){
        if(taccesorio.getValue()!= null && taccesorio.toString().isEmpty()==false){
            if(txt_estado.getValue()!= null && txt_estado.toString().isEmpty()==false){
                aCombustible.getParametacces(tab_tabla.getValor("mve_secuencial"), taccesorio.getValue()+"", Integer.parseInt(txt_cantidad.getValue()+""), txt_estado.getValue()+"");
                taccesorio.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "Accesorio");
                set_accesorio.actualizar();
            }
        }
    }
    
    public void endAccesorio(){
        if (set_accesorio.getValorSeleccionado() != null && set_accesorio.getValorSeleccionado().isEmpty() == false) {
            aCombustible.deleteaccesorios(Integer.parseInt(set_accesorio.getValorSeleccionado()));
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
        tramite();
    }

    @Override
    public void guardar() {
        if(tab_tabla.guardar()){
            con_postgres.guardarPantalla(); 
        }
        actu_conductor();
    }

    @Override
    public void eliminar() {
    }

    public void actu_conductor(){
         aCombustible.setAccesorios(Integer.parseInt(tab_tabla.getValor("mve_secuencial")), tab_tabla.getValor("mav_cargoemplea"), tab_tabla.getValor("mav_nomemplea"));
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
               p_parametros.put("codigo", Integer.parseInt(tab_tabla.getValor("mav_secuencial")+""));
               p_parametros.put("placa", Integer.parseInt(tab_tabla.getValor("mve_secuencial")+""));
               rep_reporte.cerrar();
               sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
               System.err.println(p_parametros);
               sef_formato.dibujar();
           break;
        }
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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

    public Tabla getSet_accesorio() {
        return set_accesorio;
    }

    public void setSet_accesorio(Tabla set_accesorio) {
        this.set_accesorio = set_accesorio;
    }
    
}
