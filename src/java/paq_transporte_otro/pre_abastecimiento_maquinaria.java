/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_abastecimiento_maquinaria extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //Para tabla de
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    private Combo cmb_anio = new Combo();
    private Combo cmb_periodo = new Combo();
    private Combo cmb_placa = new Combo();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_abastecimiento_maquinaria() {
        
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
        
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("SELECT ano_curso, ano_curso as año FROM conc_ano order by actual");
        
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual order by ide_periodo");
        
        cmb_placa.setId("cmb_placa");
        cmb_placa.setConexion(con_sql);
        cmb_placa.setCombo("SELECT MVE_PLACA,MVE_PLACA as placa FROM MVVEHICULO order by MVE_PLACA");
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("lista");
        bar_botones.agregarBoton(bot_busca);
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT IDE_ABASTECIMIENTO_COMBUSTIBLE,NUMERO_ABASTECIMIENTO,PLACA_VEHICULO,DESCRIPCION_VEHICULO,NUMERO_VALE_ABASTECIMIENTO,FECHA_ABASTECIMIENTO\n" +
                "FROM MVABASTECIMIENTO_COMBUSTIBLE where TIPO_INGRESO ='M' order by FECHA_ABASTECIMIENTO");
//        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Abastecimiento:"));
        bar_botones.agregarComponente(aut_busca);
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(pan_opcion);
        
        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("Año: "));
        gru_lis.getChildren().add(cmb_anio);
        gru_lis.getChildren().add(new Etiqueta("Periodo: "));
        gru_lis.getChildren().add(cmb_periodo);
        gru_lis.getChildren().add(new Etiqueta("Placa: "));
        gru_lis.getChildren().add(cmb_placa);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);
        
        set_lista.setId("set_lista");
        set_lista.getTab_seleccion().setConexion(con_sql);//conexion para seleccion con otra base
        set_lista.setSeleccionTabla("SELECT IDE_ABASTECIMIENTO_COMBUSTIBLE,PLACA_VEHICULO,DESCRIPCION_VEHICULO,NUMERO_VALE_ABASTECIMIENTO,GALONES,KILOMETRAJE,TOTAL,FECHA_ABASTECIMIENTO\n" +
                "FROM MVABASTECIMIENTO_COMBUSTIBLE WHERE TIPO_INGRESO ='M' and IDE_ABASTECIMIENTO_COMBUSTIBLE=-1 order by FECHA_ABASTECIMIENTO", "IDE_ABASTECIMIENTO_COMBUSTIBLE");
        set_lista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_lista.getTab_seleccion().setRows(10);
        set_lista.setRadio();
        set_lista.getGri_cuerpo().setHeader(gru_lis);
        set_lista.getBot_aceptar().setMetodo("aceptAprobacion");
        set_lista.setHeader("SELECCIONE LISTADO");
        agregarComponente(set_lista);
        
        dibujaAbastecimiento();
        
    }

    public void dibujaAbastecimiento(){
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvabastecimiento_combustible", "ide_abastecimiento_combustible", 1);
                /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("ide_abastecimiento_combustible=-1");
        } else {
            tab_tabla.setCondicion("ide_abastecimiento_combustible=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_tabla.getColumna("placa_vehiculo").setMetodoChange("busPlaca");
        tab_tabla.getColumna("kilometraje").setMetodoChange("kilometraje");
        tab_tabla.getColumna("galones").setMetodoChange("galones");
        tab_tabla.getColumna("tipo_medicion").setMetodoChange("activarCasilla");
        tab_tabla.getColumna("va_hora").setMetodoChange("cal_hora");
        tab_tabla.getColumna("TIPO_INGRESO").setValorDefecto("M");
         List list = new ArrayList();
        Object fil1[] = {
            "1", "KILOMETROS"
        };
        Object fil2[] = {
            "2", "HORAS"
        };
        list.add(fil1);;
        list.add(fil2);;
        tab_tabla.getColumna("tipo_medicion").setRadio(list, " ");
        tab_tabla.getColumna("ide_tipo_combustible").setLectura(true);
        tab_tabla.getColumna("galones").setLectura(true);
        tab_tabla.getColumna("descripcion_vehiculo").setLectura(true);
        tab_tabla.getColumna("kilometraje").setLectura(true);
        tab_tabla.getColumna("va_hora").setLectura(true);
        tab_tabla.getColumna("numero_abastecimiento").setLectura(true);
        tab_tabla.getColumna("total").setLectura(true);
        tab_tabla.getColumna("titulo").setEtiqueta();
        tab_tabla.getColumna("ci_conductor").setVisible(false);
        tab_tabla.getColumna("fecha_digitacion").setVisible(false);
        tab_tabla.getColumna("hora_digitacion").setVisible(false);
        tab_tabla.getColumna("usu_digitacion").setVisible(false);
        tab_tabla.getColumna("estado").setValorDefecto("1");
        tab_tabla.getColumna("estado").setVisible(false);
        tab_tabla.getColumna("fecha_actualizacion").setVisible(false);
        tab_tabla.getColumna("usu_actualizacion").setVisible(false);
        tab_tabla.getColumna("anio").setVisible(false);
        tab_tabla.getColumna("periodo").setVisible(false);
        tab_tabla.getColumna("TIPO_INGRESO").setVisible(false);
        tab_tabla.getColumna("usu_digitacion").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("fecha_digitacion").setValorDefecto(String.valueOf(utilitario.getFechaActual()));
        tab_tabla.getColumna("hora_digitacion").setValorDefecto(String.valueOf(utilitario.getHoraActual()));
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
    
    public void busPlaca(){
        TablaGenerica tab_dato =aCombustible.getVehiculo(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_datoc = aCombustible.getConductores(tab_dato.getValor("mve_conductor"));
            if (!tab_datoc.isEmpty()) {
                tab_tabla.setValor("descripcion_vehiculo", tab_dato.getValor("descripcion"));
                tab_tabla.setValor("conductor", tab_datoc.getValor("nombres"));
                tab_tabla.setValor("ci_conductor", tab_datoc.getValor("cedula_pass"));
                tab_tabla.setValor("ide_tipo_combustible", tab_dato.getValor("MVE_TIPO_COMBUSTIBLE"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Conductor","No Disponible");
            }
        }else{
            utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
        }
    }
    
    public void kilometraje(){
        TablaGenerica tab_dato =aCombustible.getKilometraje(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_KILOMETRAJE"));
            Double valor2 = Double.valueOf(tab_tabla.getValor("kilometraje"));
            if(valor2>valor1){
                tab_tabla.getColumna("galones").setLectura(false);
                utilitario.addUpdate("tab_calculo");
                carga();
            }else{
                utilitario.agregarMensajeError("Kilometraje","Por Debajo del Anterior");
                tab_tabla.getColumna("galones").setLectura(true);
                utilitario.addUpdate("tab_calculo");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }  
    
    public void cal_hora(){
        String horas =tab_tabla.getValor("va_hora").substring(0,4);
        String minutos =tab_tabla.getValor("va_hora").substring(5,7);
        if(Integer.parseInt(minutos)<60){
            TablaGenerica tab_dato =aCombustible.getKilometraje(tab_tabla.getValor("placa_vehiculo"));
            if (!tab_dato.isEmpty()) {
                String valor1 = tab_dato.getValor("MVE_HOROMETRO").substring(0,4);
                String valor2 = tab_tabla.getValor("va_hora").substring(0,4);
                if(Integer.parseInt(valor2)>Integer.parseInt(valor1)){
                    tab_tabla.getColumna("galones").setLectura(false);
                    utilitario.addUpdate("tab_tabla");
                    carga();
                }else{
                    utilitario.agregarMensajeError("Horas","Por Debajo de la Anterior");
                    tab_tabla.getColumna("galones").setLectura(true);
                    utilitario.addUpdate("tab_tabla");
                }
            }else{
                utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
            }
        }else{
            utilitario.agregarMensaje("Minutos No Deben Ser Mayor", "60");
        }
    }
    
    public void galones(){
        TablaGenerica tab_dato =aCombustible.getKilometraje(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_CAPACIDAD_TANQUE_COMBUSTIBLE"));
            Double valor2 = Double.valueOf(tab_tabla.getValor("galones"));
            if(valor2<valor1){
                utilitario.addUpdate("tab_tabla");
                        valor();
            }else{
                utilitario.agregarMensajeError("Galones","Exceden Capacidad de Vehiculo");
                tab_tabla.setValor("galones", null);
                utilitario.addUpdate("tab_tabla");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void valor(){
        TablaGenerica tab_dato =aCombustible.getCombustible(Integer.parseInt(tab_tabla.getValor("ide_tipo_combustible")));
        if (!tab_dato.isEmpty()) {
            Double valor;
            valor = (Double.parseDouble(tab_dato.getValor("valor_galon"))*Double.parseDouble(tab_tabla.getValor("galones")));
            tab_tabla.setValor("total", String.valueOf(Math.rint(valor*100)/100));
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
        secuencial();
    }
    
    public void secuencial(){
        if(tab_tabla.getValor("numero_abastecimiento")!=null && tab_tabla.getValor("numero_abastecimiento").toString().isEmpty() == false){

        }else{
            Integer numero = Integer.parseInt(aCombustible.listaMax(tab_tabla.getValor("placa_vehiculo"),String.valueOf(utilitario.getAnio(tab_tabla.getValor("fecha_abastecimiento"))),String.valueOf(utilitario.getMes(tab_tabla.getValor("fecha_abastecimiento")))));
            Integer cantidad=0;
            cantidad=numero +1;
            tab_tabla.setValor("numero_abastecimiento", String.valueOf(cantidad));
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void activarCasilla(){
        if(tab_tabla.getValor("TIPO_MEDICION").equals("2")){
            tab_tabla.getColumna("KILOMETRAJE").setLectura(true);
            tab_tabla.getColumna("VA_HORA").setLectura(false);
            utilitario.addUpdate("tab_tabla");
        }else{
            tab_tabla.getColumna("KILOMETRAJE").setLectura(false);
            tab_tabla.getColumna("VA_HORA").setLectura(true);
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void carga(){
       tab_tabla.setValor("anio", String.valueOf(utilitario.getAnio(tab_tabla.getValor("fecha_abastecimiento"))));
       tab_tabla.setValor("periodo", String.valueOf(utilitario.getMes(tab_tabla.getValor("fecha_abastecimiento"))));
       utilitario.addUpdate("tab_tabla");
    }
    
    public void actuKilometrajes(){
        if(tab_tabla.getValor("tipo_medicion").equals("1")){
            if(tab_tabla.getValor("ide_abastecimiento_combustible")!=null && tab_tabla.getValor("ide_abastecimiento_combustible").toString().isEmpty() == false){
                aCombustible.ActKilometraje(tab_tabla.getValor("placa_vehiculo"), Double.parseDouble(tab_tabla.getValor("kilometraje")));
            }
        }else{
            if(tab_tabla.getValor("ide_abastecimiento_combustible")!=null && tab_tabla.getValor("ide_abastecimiento_combustible").toString().isEmpty() == false){
                aCombustible.ActHoras(tab_tabla.getValor("placa_vehiculo"), tab_tabla.getValor("va_hora"));
            }
        }
    }
    
    public void actuRegis(){
       aCombustible.ActRegistro(Integer.parseInt(tab_tabla.getValor("ide_abastecimiento_combustible")), tab_tabla.getValor("numero_abastecimiento"), Integer.parseInt(tab_tabla.getValor("ide_tipo_combustible")), tab_tabla.getValor("fecha_abastecimiento"), 
               tab_tabla.getValor("hora_abastecimiento"), Integer.parseInt(tab_tabla.getValor("kilometraje")), Double.valueOf(tab_tabla.getValor("galones")), Double.valueOf(tab_tabla.getValor("total")), 
               tab_tabla.getValor("placa_vehiculo"), tab_tabla.getValor("descripcion_vehiculo"), tab_tabla.getValor("conductor"), tab_tabla.getValor("ci_conductor"), tab_consulta.getValor("NICK_USUA"));
       utilitario.addUpdate("tab_tabla");
       actuKilometrajes();
       utilitario.agregarMensaje("Registro Actualizado", "");
   }
    
    public void lista(){
        set_lista.dibujar();
    }
    
    public void buscarColumna(){
            set_lista.getTab_seleccion().setSql("SELECT IDE_ABASTECIMIENTO_COMBUSTIBLE,PLACA_VEHICULO,DESCRIPCION_VEHICULO,NUMERO_VALE_ABASTECIMIENTO,GALONES,KILOMETRAJE,TOTAL,FECHA_ABASTECIMIENTO\n" +
                    "FROM MVABASTECIMIENTO_COMBUSTIBLE where ANIO ="+cmb_anio.getValue()+" and PLACA_VEHICULO = '"+cmb_placa.getValue()+"' and PERIODO ="+cmb_periodo.getValue()+" order by FECHA_ABASTECIMIENTO");
            set_lista.getTab_seleccion().ejecutarSql();
    }
    
    public void aceptAprobacion(){
        if (set_lista.getValorSeleccionado() != null) {
            aut_busca.setValor(set_lista.getValorSeleccionado());
            set_lista.cerrar();
            dibujaAbastecimiento();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
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
    
    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_tabla.getValor("ide_abastecimiento_combustible")!=null){
            actuRegis();
        }else{
            if(tab_tabla.guardar()){
                con_sql.guardarPantalla(); 
                actuKilometrajes();
            }
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
