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
public class pre_solicitud_mantenimiento extends Pantalla{
    
    //cadena de conexion a base de datos
    private Conexion con_sql = new Conexion();
    private Conexion con_postgres = new Conexion();
    
    //identificador para tablas
    private Tabla tab_vehiculo = new Tabla();
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //obejto para seleccion
    private SeleccionTabla set_proveedor = new SeleccionTabla();
    private SeleccionTabla set_autorizador = new SeleccionTabla();
    
    //seleccion tipo de mantenimiento
    private Combo cmb_impresion = new Combo();
    
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
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    private Panel pan_limpiar = new Panel();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    public pre_solicitud_mantenimiento() {
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("INGRESO DE VEHICULOS");
        agregarComponente(pan_opcion);
        
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT v.MVE_SECUENCIAL,v.MVE_PLACA,m.MVMARCA_DESCRIPCION,o.MVMODELO_DESCRIPCION,v.MVE_ANO\n" +
                "FROM dbo.MVVEHICULO AS v , dbo.MVMARCA AS m , dbo.MVMODELO AS o\n" +
                "WHERE v.MVE_MARCA = m.MVMARCA_ID AND v.MVE_MODELO = o.MVMODELO_ID AND v.MVE_ESTADO = 'activo'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpia_pa");
        bar_botones.agregarBoton(bot_limpiar);
        
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
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);
        
        dibujarSolicitud();
        
        dia_dialogod.setId("dia_dialogod");
        dia_dialogod.setTitle("SELECCIONE OPCIÓN"); //titulo
        dia_dialogod.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogod.setHeight("10%");//siempre porcentaje   alto
        dia_dialogod.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogod.getBot_aceptar().setMetodo("abrirReporte");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogod);
    }

    public void dibujarSolicitud(){
        tab_vehiculo.setId("tab_vehiculo");
        tab_vehiculo.setConexion(con_sql);
        tab_vehiculo.setTabla("mvvehiculo", "mve_secuencial", 1);
        //        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_vehiculo.setCondicion("mve_secuencial=-1");
        } else {
            tab_vehiculo.setCondicion("mve_secuencial=" + aut_busca.getValor());
        }
        tab_vehiculo.getColumna("mve_secuencial").setVisible(false);
        tab_vehiculo.getColumna("MVE_MARCA").setCombo("SELECT MVMARCA_ID,MVMARCA_DESCRIPCION FROM MVMARCA order by MVMARCA_DESCRIPCION");
        tab_vehiculo.getColumna("MVE_MODELO").setCombo("SELECT MVMODELO_ID,MVMODELO_DESCRIPCION FROM MVMODELO order by MVMODELO_DESCRIPCION");
        tab_vehiculo.getColumna("MVE_TIPO").setVisible(false);
        tab_vehiculo.getColumna("MVE_COLOR").setVisible(false);
        tab_vehiculo.getColumna("MVE_ANO").setVisible(false);
        tab_vehiculo.getColumna("MVE_ESTADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_VERSION").setVisible(false);
        tab_vehiculo.getColumna("MVE_TAMANIO").setVisible(false);
        tab_vehiculo.getColumna("MVE_NUMIMR").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPOCODIGO").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPOMEDICION").setVisible(false);
        tab_vehiculo.getColumna("MVE_ASIGNADO").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGININGRESO").setVisible(false);
        tab_vehiculo.getColumna("MVE_FECHAINGRESO").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGINACTUALI").setVisible(false);
        tab_vehiculo.getColumna("MVE_FECHAACTUALI").setVisible(false);
        tab_vehiculo.getColumna("MVE_ESTADO_REGISTRO").setVisible(false);
        tab_vehiculo.getColumna("MVE_LOGINBORRADO ").setVisible(false);
        tab_vehiculo.getColumna("MVE_TIPO_COMBUSTIBLE").setVisible(false);
        tab_vehiculo.getColumna("MVE_CAPACIDAD_TANQUE_COMBUSTIBLE").setVisible(false);
        tab_vehiculo.getColumna("MVE_DURACION_LLANTA").setVisible(false);
        tab_vehiculo.agregarRelacion(tab_cabecera);
        tab_vehiculo.setTipoFormulario(true);
        tab_vehiculo.getGrid().setColumns(4);
        tab_vehiculo.setLectura(true);
        tab_vehiculo.dibujar();
        PanelTabla ptv = new PanelTabla();
        ptv.setPanelTabla(tab_vehiculo);
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_sql);
        tab_cabecera.setTabla("mvcabmanteni", "mca_secuencial", 2);
        tab_cabecera.getColumna("mca_responsable").setValorDefecto(tab_vehiculo.getValor("mve_conductor"));
         List lista = new ArrayList();
        Object fila1[] = {
            "INTERNO", "INTERNO"
        };
        Object fila2[] = {
            "EXTERNO", "EXTERNO"
        };
        Object fila3[] = {
            "OTRO", "OTRO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        tab_cabecera.getColumna("mca_tipo_mantenimiento").setCombo(lista);
        tab_cabecera.getColumna("mca_fechainman").setValorDefecto(utilitario.getFechaActual());
        tab_cabecera.getColumna("mca_secuencial").setVisible(false);
        tab_cabecera.getColumna("mca_fechasoli").setVisible(false);
        tab_cabecera.getColumna("mca_fechasoli").setValorDefecto(utilitario.getHoraActual());
        tab_cabecera.getColumna("mca_fechasaman").setVisible(false);
        tab_cabecera.getColumna("mca_kmanterior").setVisible(false);
        tab_cabecera.getColumna("mca_kmactual").setVisible(false);
        tab_cabecera.getColumna("mca_acotaciones").setVisible(false);
        tab_cabecera.getColumna("mca_tipomedicion").setVisible(false);
        tab_cabecera.getColumna("mca_loginingreso").setVisible(false);
        tab_cabecera.getColumna("mca_fechaingreso").setVisible(false);
        tab_cabecera.getColumna("mca_loginactuali").setVisible(false);
        tab_cabecera.getColumna("mca_fechaactuali").setVisible(false);
        tab_cabecera.getColumna("mca_loginborrado").setVisible(false);
        tab_cabecera.getColumna("mca_fecha_borrado").setVisible(false);
        tab_cabecera.getColumna("mca_estado_registro").setVisible(false);
        tab_cabecera.getColumna("mca_tiposol").setVisible(false);
        tab_cabecera.getColumna("mca_monto").setVisible(false);
        tab_cabecera.getColumna("mca_responsable").setVisible(false);
        tab_cabecera.getColumna("mca_anio").setVisible(false);
        tab_cabecera.getColumna("mca_periodo").setVisible(false);
        tab_cabecera.getColumna("mca_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_cabecera.getColumna("mca_periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_cabecera.getColumna("mca_estado_tramite").setLectura(true);
        tab_cabecera.getColumna("mca_proveedor").setMetodoChange("proveedor");
        tab_cabecera.getColumna("mca_autorizado").setMetodoChange("autoriza");
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(2);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);
        
        Division div = new Division();
        div.dividir2(ptv, ptc, "30%", "h");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    public void filtrarSolicitud (SelectEvent evt){
        limpiar();
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            dibujarSolicitud();
            utilitario.addUpdate("aut_busca,pan_opcion");
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
    
    //Estado De Solicitud
    public void estado(){
        tab_cabecera.setValor("mca_estado_tramite","SOLICITUD");
        tab_cabecera.setValor("msc_secuencial",aCombustible.SecuencialCab(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getMes(utilitario.getFechaActual())), Integer.parseInt(tab_vehiculo.getValor("mve_secuencial"))));
        utilitario.addUpdate("tab_cabecera");
    }
    
    //Información obligatoria de proveedor y autorizador
    public void proveedor(){
        set_proveedor.dibujar();
    }
    
    public void acepProveedor (){
        if(set_proveedor.getValorSeleccionado()!= null && set_proveedor.getValorSeleccionado().toString().isEmpty()==false){
            TablaGenerica tab_dato =aprogramas.proveedor1(Integer.parseInt(set_proveedor.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                tab_cabecera.setValor("mca_proveedor", tab_dato.getValor("titular")+"");
                utilitario.addUpdate("tab_cabecera");
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
                tab_cabecera.setValor("mca_AUTORIZADO", tab_dato.getValor("nombres")+"");
                utilitario.addUpdate("tab_cabecera");
                set_autorizador.cerrar();
                estado();
            }
        }else{
            utilitario.agregarMensaje("Seleccione un Registro", "");
        }
    }
    
    @Override
    public void insertar() {
        TablaGenerica tab_dato = aCombustible.getSolicitud(tab_vehiculo.getValor("mve_placa"));
        if (!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Solicitud Anterior", "No Terminada");
        }else{
            if (tab_cabecera.isFocus()) {
                tab_cabecera.insertar();
            }
        }
    }

    @Override
    public void guardar() {
        if(tab_cabecera.getValor("mca_estado_tramite").equals("SOLICITUD")){
            if(tab_cabecera.getValor("mca_secuencial")!=null){
                if (tab_cabecera.guardar()) {
                    con_sql.guardarPantalla();
                    aCombustible.updateFecha(tab_cabecera.getValor("mca_fechainman"), Integer.parseInt(tab_vehiculo.getValor("mve_secuencial")),tab_cabecera.getValor("mca_observacion"));
                }
            }else{
                if (tab_cabecera.guardar()) {
                    con_sql.guardarPantalla();
                    aCombustible.updateFecha(tab_cabecera.getValor("mca_fechainman"), Integer.parseInt(tab_vehiculo.getValor("mve_secuencial")),tab_cabecera.getValor("mca_observacion"));
                }
            }
        }else{
            utilitario.agregarMensaje("Resgistro No Puede Ser Modificado", "");
        }
    }

    @Override
    public void eliminar() {
        
    }

//    *CREACION DE REPORTES */
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
                       TablaGenerica tab_dato =aCombustible.ParametrosID(tab_cabecera.getValor("mca_secuencial")+"");
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
                           aCombustible.getNumero(String.valueOf(cantidad), "ORDEN", "ORDEN", tab_cabecera.getValor("mca_secuencial"),utilitario.getVariable("NICK"));
                       }
                   }else {
                       Integer numero = Integer.parseInt(aCombustible.ParametrosMax("ORDEN"));
                       String cadena = anio+"-"+String.valueOf(numero)+"";
                       p_parametros.put("secuencial", cadena);
                   }
                   p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                   p_parametros.put("placa", tab_vehiculo.getValor("mve_placa")+"");
                   p_parametros.put("id", tab_cabecera.getValor("mca_secuencial")+"");
                   p_parametros.put("numero", cmb_impresion.getValue()+"");
                   p_parametros.put("fecha_orden", utilitario.getFechaLarga(utilitario.getFechaActual())+"");
                   p_parametros.put("director", tab_datod.getValor("nombres")+"");
                   p_parametros.put("mantenimiento", tab_cabecera.getValor("msc_secuencial")+"");
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

    public Tabla getTab_vehiculo() {
        return tab_vehiculo;
    }

    public void setTab_vehiculo(Tabla tab_vehiculo) {
        this.tab_vehiculo = tab_vehiculo;
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
