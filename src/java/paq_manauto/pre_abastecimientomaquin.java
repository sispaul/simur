/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_manauto.ejb.manauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author p-sistemas
 */
public class pre_abastecimientomaquin extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Para tabla de
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    
    public pre_abastecimientomaquin() {
        
        //desactiva botones de navegación
        bar_botones.quitarBotonsNavegacion();
        
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
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvabactecimiento_combustible", "abastecimiento_id", 1);
        
        tab_tabla.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||'/'||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tab_tabla.getColumna("abastecimiento_cod_conductor").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where cod_cargo in (SELECT cod_cargo FROM srh_cargos WHERE nombre_cargo like '%CHOFER%') and estado = 1 order by nombres");
        tab_tabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial, ((case when v.mve_placa is NULL then v.mve_codigo when v.mve_placa is not null then v.mve_placa end )||'/'||m.mvmarca_descripcion ||'/'||o.mvmodelo_descripcion ||'/'||v.mve_ano)as descripcion\n" +
                "FROM mv_vehiculo v\n" +
                "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n" +
                "INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id\n" +
                "WHERE v.mve_tipo_ingreso = 'M'");
        tab_tabla.getColumna("mve_secuencial").setFiltroContenido();
        tab_tabla.getColumna("mve_secuencial").setMetodoChange("busPlaca");
        tab_tabla.getColumna("abastecimiento_galones").setMetodoChange("carga");
        tab_tabla.getColumna("abastecimiento_valorhora").setMetodoChange("valor");
         
        tab_tabla.getColumna("abastecimiento_logining").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("abastecimiento_fechaing").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("abastecimiento_horaing").setValorDefecto(utilitario.getHoraActual());
        
        tab_tabla.getColumna("abastecimiento_valorhora").setMascara("99:99");
        tab_tabla.getColumna("abastecimiento_estado").setValorDefecto("1");
        tab_tabla.getColumna("abastecimiento_tipo_ingreso").setValorDefecto("H");
        tab_tabla.getColumna("abastecimiento_tipo_medicion").setValorDefecto("2");
        tab_tabla.getColumna("abastecimiento_ingreso").setValorDefecto("HT");
        
        tab_tabla.getColumna("abastecimiento_loginactu").setVisible(false);
        
        tab_tabla.getColumna("abastecimiento_titulo").setEtiqueta();
        tab_tabla.getColumna("abastecimiento_id").setVisible(false);
        tab_tabla.getColumna("abastecimiento_fechactu").setVisible(false);
        tab_tabla.getColumna("abastecimiento_fechaing").setVisible(false);
        tab_tabla.getColumna("abastecimiento_horaing").setVisible(false);
        tab_tabla.getColumna("abastecimiento_logining").setVisible(false);
        tab_tabla.getColumna("abastecimiento_kilometraje").setVisible(false);
        tab_tabla.getColumna("abastecimiento_estado").setVisible(false);
        tab_tabla.getColumna("abastecimiento_tipo_ingreso").setVisible(false);
        tab_tabla.getColumna("abastecimiento_tipo_medicion").setVisible(false);
        tab_tabla.getColumna("abastecimiento_conductor").setVisible(false);
        tab_tabla.getColumna("abastecimiento_anio").setVisible(false);
        tab_tabla.getColumna("abastecimiento_periodo").setVisible(false);
        tab_tabla.getColumna("abastecimiento_ingreso").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_postgres);
        tab_tabla1.setSql("SELECT\n" +
                "a.abastecimiento_id,\n" +
                "a.abastecimiento_numero_vale,\n" +
                "v.mve_placa,\n" +
                "(m.mvmarca_descripcion||' '||t.mvtipo_descripcion||' '||o.mvmodelo_descripcion) AS descripcion,\n" +
                "a.abastecimiento_total,\n" +
                "a.abastecimiento_numero\n" +
                "FROM mvabactecimiento_combustible AS a\n" +
                "INNER JOIN mv_vehiculo AS v ON a.mve_secuencial = v.mve_secuencial\n" +
                "INNER JOIN mvmarca_vehiculo AS m ON v.mvmarca_id = m.mvmarca_id\n" +
                "INNER JOIN mvmodelo_vehiculo AS o ON v.mvmodelo_id = o.mvmodelo_id\n" +
                "INNER JOIN mvtipo_vehiculo t ON t.mvmarca_id = m.mvmarca_id AND v.mvtipo_id = t.mvtipo_id AND o.mvtipo_id = t.mvtipo_id\n" +
                "WHERE a.abastecimiento_tipo_ingreso = 'H'\n" +
                "ORDER BY a.abastecimiento_id DESC LIMIT 10");
        tab_tabla1.setLectura(true);
        tab_tabla1.getColumna("abastecimiento_numero_vale").setFiltro(true);
        tab_tabla1.getColumna("mve_placa").setFiltro(true);
//        tab_tabla1.agregarRelacion(tab_tabla);
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla ptt1 = new PanelTabla();
        ptt1.setPanelTabla(tab_tabla1);
        Division div = new Division();
        div.dividir2(ptt,ptt1,"40%","H");
        agregarComponente(div);
    }

    //busca datos de vehiculo que se selecciona
    public void busPlaca(){
        TablaGenerica tab_dato =aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_dato.isEmpty()) {
            if(tab_dato.getValor("mve_numimr").equals("H")){
                tab_tabla.setValor("abastecimiento_cod_conductor", tab_dato.getValor("mve_cod_conductor"));
                    tab_tabla.setValor("abastecimiento_conductor", tab_dato.getValor("mve_conductor"));
                    tab_tabla.setValor("tipo_combustible_id", tab_dato.getValor("tipo_combustible_id"));
                    utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Modulo solo para Vehiculos","");
            }
        }else{
            utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
        }
    }
    
    public void carga(){
       tab_tabla.setValor("abastecimiento_anio", String.valueOf(utilitario.getAnio(tab_tabla.getValor("abastecimiento_fecha"))));
       tab_tabla.setValor("abastecimiento_periodo", String.valueOf(utilitario.getMes(tab_tabla.getValor("abastecimiento_fecha"))));
       utilitario.addUpdate("tab_tabla");
       TablaGenerica tab_dato =aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("mve_capacidad_tanque"));
            Double valor2 = Double.valueOf(tab_tabla.getValor("abastecimiento_galones"));
            if(valor2<=valor1){
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Galones","Exceden Capacidad de Vehiculo");
                tab_tabla.setValor("abastecimiento_galones", null);
                utilitario.addUpdate("tab_tabla");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    
    public void valor(){
        String minutos =tab_tabla.getValor("abastecimiento_valorhora").substring(3,5);
        if(Integer.parseInt(minutos)<59){
            TablaGenerica tab_dato =aCombustible.getCombustible(Integer.parseInt(tab_tabla.getValor("tipo_combustible_id")));
            if (!tab_dato.isEmpty()) {
                Double valor;
                valor = (Double.parseDouble(tab_dato.getValor("tipo_valor_galon"))*Double.parseDouble(tab_tabla.getValor("abastecimiento_galones")));
                tab_tabla.setValor("abastecimiento_total", String.valueOf(Math.rint(valor*100)/100));
                utilitario.addUpdate("tab_tabla");
                secuencial();
            }else{
                utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
            }
        }else{
            utilitario.agregarMensaje("Minutos no deben ser menores a 60", "");
        }
    }
    
    public void secuencial(){
        if(tab_tabla.getValor("abastecimiento_fecha")!=null && tab_tabla.getValor("abastecimiento_fecha").toString().isEmpty() == false){
            if(tab_tabla.getValor("abastecimiento_numero")!=null && tab_tabla.getValor("abastecimiento_numero").toString().isEmpty() == false){

            }else{
                Integer numero = Integer.parseInt(aCombustible.listaMax(Integer.parseInt(tab_tabla.getValor("mve_secuencial")),String.valueOf(utilitario.getAnio(tab_tabla.getValor("abastecimiento_fecha"))),String.valueOf(utilitario.getMes(tab_tabla.getValor("abastecimiento_fecha")))));
                Integer cantidad=0;
                cantidad=numero +1;
                tab_tabla.setValor("abastecimiento_numero", String.valueOf(cantidad));
                utilitario.addUpdate("tab_tabla");
            }
        }else{
            tab_tabla.setValor("abastecimiento_numero_vale", null);
            utilitario.addUpdate("tab_tabla");
            utilitario.agregarMensaje("Ingresar Fecha de Abastecimiento", "");
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
        String reg = new String();
        if(tab_tabla.guardar()){
            con_postgres.guardarPantalla(); 
            reg = tab_tabla.getValorSeleccionado();
        }
        tab_tabla1.actualizar();
        tab_tabla1.setFilaActual(reg);
        tab_tabla1.calcularPaginaActual();
        hora_actu();
    }
    
    public void hora_actu(){
        Integer valor=0,horaa;
        TablaGenerica tab_dato =aCombustible.getHorav(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_dato.isEmpty()) {
            String cadena;
            String minutos =tab_tabla.getValor("abastecimiento_valorhora").substring(3,5);
            String horas =tab_dato.getValor("mve_horometro").substring(0,4);
            String minutos1 =tab_dato.getValor("mve_horometro").substring(5,7);
            Integer suma = Integer.parseInt(minutos)+Integer.parseInt(minutos1);
            if(suma>60){
                valor = suma-60;
                horaa=Integer.parseInt(horas)+1;
                cadena = String.valueOf(horaa)+":"+String.valueOf(valor);
                aCombustible.set_ActuaHR(Integer.parseInt(tab_tabla.getValor("mve_secuencial")),cadena, "mve_horometro");
            }else if(suma==60){
                horaa=Integer.parseInt(horas)+1;
                cadena = String.valueOf(horaa)+":"+String.valueOf(suma);
                aCombustible.set_ActuaHR(Integer.parseInt(tab_tabla.getValor("mve_secuencial")),cadena, "mve_horometro");
            }else if(suma<60){
                cadena = String.valueOf(horas)+":"+String.valueOf(suma);
                aCombustible.set_ActuaHR(Integer.parseInt(tab_tabla.getValor("mve_secuencial")),cadena, "mve_horometro");
            }
        }else{
            utilitario.agregarMensaje("no encuentra datos hora", "");
        }
    }
    
    @Override
    public void eliminar() {
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

}