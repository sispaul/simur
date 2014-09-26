/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.ProvisionCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_orden_consumo extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_calculo = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_colaborador = new Tabla();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid = new Grid();
    
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Busca de comprobante
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private ProvisionCombustible pCombustible = (ProvisionCombustible) utilitario.instanciarEJB(ProvisionCombustible.class);
    
    public pre_orden_consumo() {
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT IDE_ORDEN_CONSUMO,NUMERO_ORDEN,FECHA_ORDEN,PLACA_VEHICULO\n" +
                "FROM MVORDEN_CONSUMO");
        aut_busca.setMetodoChange("buscarOrden");
        aut_busca.setSize(70);
        
        bar_botones.agregarComponente(new Etiqueta("Buscador Orden:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_sql);
        tab_tabla.setTabla("mvorden_consumo", "ide_orden_consumo", 1);
        tab_tabla.setHeader("ORDEN DE PROVISIÓN DE COMBUSTIBLE");
        tab_tabla.getColumna("placa_vehiculo").setMetodoChange("busVehiculo");
        tab_tabla.getColumna("conductor").setMetodoChange("buscaConductor");
        tab_tabla.getColumna("fecha_orden").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla.getColumna("AUTORIZA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("ci_conductor").setVisible(false);
        tab_tabla.getColumna("autoriza").setVisible(false);
        tab_tabla.getColumna("ide_orden_consumo").setVisible(false);
        tab_tabla.agregarRelacion(tab_calculo);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        tab_calculo.setId("tab_calculo");
        tab_calculo.setConexion(con_sql);
        tab_calculo.setTabla("mvcalculo_consumo", "ide_calculo_consumo", 2);
        tab_calculo.setHeader("DATOS DE PROVISIÓN DE COMBUSTIBLE");
        tab_calculo.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM mvTIPO_COMBUSTIBLE");
        tab_calculo.getColumna("fecha_digitacion").setVisible(false);
        tab_calculo.getColumna("hora_digitacion").setVisible(false);
        tab_calculo.getColumna("usu_digitacion").setVisible(false);
        tab_calculo.getColumna("ide_calculo_consumo").setVisible(false);
        tab_calculo.getColumna("ide_tipo_combustible").setMetodoChange("clean");
        tab_calculo.getColumna("galones").setMetodoChange("valor");
        tab_calculo.getColumna("kilometraje").setMetodoChange("kilometraje");
        tab_calculo.setTipoFormulario(true);
        tab_calculo.getGrid().setColumns(4);
        tab_calculo.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_calculo);
        
        Division div = new Division();
        div.dividir3(quinde,ptt,ptc, "17%","55%", "h");
        agregarComponente(div);
        
        pan_opcion.getChildren().add(div);
        agregarComponente(pan_opcion);
        //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);
        
        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("BUSCAR CONDUCTOR"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("45%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoConductor");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
    }
    
         //BUSQUEDA POR NOMBRE DE GARANTE
    public void buscaConductor(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_d.getChildren().add(set_colaborador);
        set_colaborador.setId("set_colaborador");
        set_colaborador.setConexion(con_sql);
        set_colaborador.setHeader("LISTA DE CONDUCTORES");
        set_colaborador.setSql("SELECT MVE_SECUENCIAL,MVE_CONDUCTOR\n" +
                "FROM MVVEHICULO\n" +
                "WHERE MVE_CONDUCTOR LIKE '%"+tab_tabla.getValor("conductor")+"%'");
        set_colaborador.getColumna("MVE_CONDUCTOR").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogo.setDialogo(grid_d);
        set_colaborador.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void buscarOrden(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_tabla.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }
    
        //Busquedad de garante por apellido
    public void aceptoConductor(){
        if (set_colaborador.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = pCombustible.getConductor(Integer.parseInt(set_colaborador.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                TablaGenerica tab_dato1 =pCombustible.getConductores(tab_dato.getValor("MVE_CONDUCTOR"));
                if (!tab_dato1.isEmpty()) {
                    tab_tabla.setValor("conductor", tab_dato1.getValor("nombres"));
                    tab_tabla.setValor("ci_conductor", tab_dato1.getValor("cedula_pass"));
                    utilitario.addUpdate("tab_tabla");
                }else{
                utilitario.agregarMensajeInfo("Conductor, Disponible", "");
            }
            }else{
                utilitario.agregarMensajeInfo("Conductor, No Seleccionado", "");
            }
        }else {
            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
        }
        dia_dialogo.cerrar();
    }
    
    public void busVehiculo(){
        TablaGenerica tab_dato =pCombustible.getVehiculo(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_datoc = pCombustible.getConductores(tab_dato.getValor("mve_conductor"));
            if (!tab_datoc.isEmpty()) {
                tab_tabla.setValor("descripcion_vehiculo", tab_dato.getValor("descripcion"));
                tab_tabla.setValor("conductor", tab_datoc.getValor("nombres"));
                tab_tabla.setValor("ci_conductor", tab_datoc.getValor("cedula_pass"));
                utilitario.addUpdate("tab_tabla");
            }else{
                utilitario.agregarMensajeError("Conductor","No Disponible");
            }
        }else{
            utilitario.agregarMensajeError("Vehiculo","No Se Encuentra Registrado");
        }
                secuencial();
    }

    public void secuencial(){
        if(tab_tabla.getValor("numero_orden")!=null && tab_tabla.getValor("numero_orden").toString().isEmpty() == false){

        }else{
            Integer numero = Integer.parseInt(pCombustible.listaMax());
            Integer cantidad=0;
            cantidad=numero +1;
            tab_tabla.setValor("numero_orden", String.valueOf(cantidad));
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void clean(){
        tab_tabla.setValor("galones", "");
        tab_tabla.setValor("total", "");
        utilitario.addUpdate("tab_tabla");
    }
    
    public void kilometraje(){
        TablaGenerica tab_dato =pCombustible.getKilometraje(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_KILOMETRAJE"));
            Double valor2 = Double.valueOf(tab_calculo.getValor("kilometraje"));
            if(valor2>valor1){
                tab_calculo.getColumna("galones").setLectura(false);
                tab_calculo.getColumna("total").setLectura(false);
                tab_calculo.getColumna("ide_tipo_combustible").setLectura(false);
                utilitario.addUpdate("tab_calculo");
            }else{
                utilitario.agregarMensajeError("Kilometraje","Por Debajo del Anterior");
                tab_calculo.getColumna("galones").setLectura(true);
                tab_calculo.getColumna("total").setLectura(true);
                tab_calculo.getColumna("ide_tipo_combustible").setLectura(true);
                utilitario.addUpdate("tab_calculo");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void valor(){
        TablaGenerica tab_dato =pCombustible.getCombustible(Integer.parseInt(tab_tabla.getValor("ide_tipo_combustible")));
        if (!tab_dato.isEmpty()) {
            Double valor;
            valor = (Double.parseDouble(tab_dato.getValor("valor_galon"))*Double.parseDouble(tab_tabla.getValor("galones")));
            tab_tabla.setValor("total", String.valueOf(Math.rint(valor*100)/100));
            utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        con_sql.guardarPantalla();
        
    }

    @Override
    public void eliminar() {
         utilitario.getTablaisFocus().eliminar();
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
        @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ORDEN DE CONSUMO":
                aceptoOrden();
          break;
        }
    } 
    
      public void aceptoOrden(){
        switch (rep_reporte.getNombre()) {
               case "ORDEN DE CONSUMO":
                   TablaGenerica tab_dato =pCombustible.getUsuario(tab_tabla.getValor("autoriza"));
                   if (!tab_dato.isEmpty()) {
                    p_parametros.put("autoriza", tab_dato.getValor("NOM_USUA")+"");
                    p_parametros.put("id", Integer.parseInt(tab_tabla.getValor("ide_orden_consumo")+""));
                    p_parametros.put("vale", Integer.parseInt(tab_tabla.getValor("numero_orden")+""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                   }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
               break;
        }
    }
    
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_calculo() {
        return tab_calculo;
    }

    public void setTab_calculo(Tabla tab_calculo) {
        this.tab_calculo = tab_calculo;
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

    public Tabla getSet_colaborador() {
        return set_colaborador;
    }

    public void setSet_colaborador(Tabla set_colaborador) {
        this.set_colaborador = set_colaborador;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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
    
}
