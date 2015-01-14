/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
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
public class pre_abastecimientoauto extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Para tabla de
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_tabla = new Tabla();
    
    //combos de seleccion
    private Combo cmb_anio = new Combo();
    private Combo cmb_periodo = new Combo();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    
    public pre_abastecimientoauto() {
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
        
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("SELECT ano_curso, ano_curso as año FROM conc_ano order by actual");
        
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual order by ide_periodo");
        
        bar_botones.agregarComponente(new Etiqueta("Año :"));
        bar_botones.agregarComponente(cmb_anio);
        bar_botones.agregarSeparador();
        
        bar_botones.agregarComponente(new Etiqueta("Periodo :"));
        bar_botones.agregarComponente(cmb_periodo);
        bar_botones.agregarSeparador();
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(pan_opcion);
        
        dibujaAbastecimiento();
    }

       public void dibujaAbastecimiento(){
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvabactecimiento_combustible", "abastecimiento_id", 1);
        /*Filtro estatico para los datos a mostrar*/
//        if (aut_busca.getValue() == null) {
//            tab_tabla.setCondicion("ide_abastecimiento_combustible=-1");
//        } else {
//            tab_tabla.setCondicion("ide_abastecimiento_combustible=" + aut_busca.getValor());
//        }
        //Metodos para buscar los datos a llenar en el formulario
        tab_tabla.getColumna("tipo_combustible_id").setCombo("SELECT tipo_combustible_id,(tipo_combustible_descripcion||''||tipo_valor_galon) as valor FROM mvtipo_combustible order by tipo_combustible_descripcion");
        tab_tabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial, (v.mve_placa||'/'||m.mvmarca_descripcion ||'/'||o.mvmodelo_descripcion ||'/'||v.mve_ano)as descripcion\n" +"FROM mv_vehiculo v\n" +
                "INNER JOIN mvmarca_vehiculo m ON v.mvmarca_id = m.mvmarca_id\n" +
                "INNER JOIN mvmodelo_vehiculo o ON v.mvmodelo_id = o.mvmodelo_id\n" +
                "WHERE v.mve_tipo_ingreso = 'A'");
        tab_tabla.getColumna("mve_secuencial").setFiltroContenido();
        tab_tabla.getColumna("mve_secuencial").setMetodoChange("busPlaca");
        tab_tabla.getColumna("abastecimiento_kilometraje").setMetodoChange("kilometraje");
        tab_tabla.getColumna("abastecimiento_galones").setMetodoChange("galones");
        tab_tabla.getColumna("abastecimiento_tipo_ingreso").setValorDefecto("K");
        tab_tabla.getColumna("abastecimiento_estado").setValorDefecto("1");
        tab_tabla.getColumna("abastecimiento_tipo_medicion").setValorDefecto("1");
        tab_tabla.getColumna("abastecimiento_logining").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("abastecimiento_fechaing").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("abastecimiento_horaing").setValorDefecto(utilitario.getHoraActual());
        tab_tabla.getColumna("abastecimiento_conductor").setLongitud(70);
        tab_tabla.getColumna("tipo_combustible_id").setLectura(true);
        tab_tabla.getColumna("abastecimiento_numero").setLectura(true);
        tab_tabla.getColumna("abastecimiento_total").setLectura(true);
        tab_tabla.getColumna("abastecimiento_cod_conductor").setVisible(false);
        tab_tabla.getColumna("abastecimiento_fechaing").setVisible(false);
        tab_tabla.getColumna("abastecimiento_titulo").setEtiqueta();
        tab_tabla.getColumna("abastecimiento_fechaing").setVisible(false);
        tab_tabla.getColumna("abastecimiento_logining").setVisible(false);
        tab_tabla.getColumna("abastecimiento_tipo_medicion").setVisible(false);
        tab_tabla.getColumna("abastecimiento_valorhora").setVisible(false);
        tab_tabla.getColumna("abastecimiento_estado").setVisible(false);
        tab_tabla.getColumna("abastecimiento_fechactu").setVisible(false);
        tab_tabla.getColumna("abastecimiento_loginactu").setVisible(false);
        tab_tabla.getColumna("abastecimiento_anio").setVisible(false);
        tab_tabla.getColumna("abastecimiento_tipo_ingreso").setVisible(false);
        tab_tabla.getColumna("abastecimiento_periodo").setVisible(false);
        tab_tabla.getColumna("abastecimiento_horaing").setVisible(false);
        tab_tabla.getColumna("abastecimiento_id").setVisible(false);
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
//        aut_busca.limpiar();
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
    
    public void busPlaca(){
        TablaGenerica tab_dato =aCombustible.getVehiculo(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            if(tab_dato.getValor("MVE_TIPO_INGRESO").equals("K")){
                TablaGenerica tab_datoc = aCombustible.getConductores(tab_dato.getValor("MVE_CONDUCTOR"));
                if (!tab_datoc.isEmpty()) {
                    tab_tabla.setValor("descripcion_vehiculo", tab_dato.getValor("descripcion"));
                    tab_tabla.setValor("conductor", tab_datoc.getValor("nombres"));
                    tab_tabla.setValor("ci_conductor", tab_datoc.getValor("cod_empleado"));
                    tab_tabla.setValor("ide_tipo_combustible", tab_dato.getValor("MVE_TIPO_COMBUSTIBLE"));
                    tab_tabla.setValor("tipo_ingreso", tab_dato.getValor("MVE_TIPO_INGRESO"));
                    utilitario.addUpdate("tab_tabla");
                }else{
                    utilitario.agregarMensajeError("Conductor","No Disponible");
                }
            }else{
                utilitario.agregarMensajeError("Modulo solo para Vehiculos","");
            }
        }else{
            utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
        }
    }
    
    @Override
    public void guardar() {
        if(tab_tabla.guardar()){
            con_postgres.guardarPantalla(); 
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

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}
