/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.ProvisionCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_vale_consumo extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Extrae datos adicionales, que se necesita, de una clase logica
    @EJB
    private ProvisionCombustible pCombustible = (ProvisionCombustible) utilitario.instanciarEJB(ProvisionCombustible.class);
    
    public pre_vale_consumo() {
        
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
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("trans_vale_consumo", "ide_vale_consumo", 1);
        tab_tabla.setHeader("ORDEN DE PROVISIÓN DE COMBUSTIBLE");
        tab_tabla.getColumna("ide_tipo_combustible").setCombo("SELECT IDE_TIPO_COMBUSTIBLE,(DESCRIPCION_COMBUSTIBLE+'/'+cast(VALOR_GALON as varchar)) as valor FROM TRANS_TIPO_COMBUSTIBLE");
        tab_tabla.getColumna("placa_vehiculo").setMetodoChange("busVehiculo");
        tab_tabla.getColumna("ide_tipo_combustible").setMetodoChange("clean");
        tab_tabla.getColumna("galones").setMetodoChange("valor");
        tab_tabla.getColumna("fecha_vale").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla.getColumna("AUTORIZA").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_tabla.getColumna("IDE_VALE_CONSUMO").setVisible(false);
        tab_tabla.getColumna("CI_CONDUCTOR").setVisible(false);
        tab_tabla.getColumna("AUTORIZA").setVisible(false);
        tab_tabla.getColumna("MODIFICA").setVisible(false);
        tab_tabla.getColumna("FECHA_MODIFICACION").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir2(quinde, ptt, "15%", "h");
        agregarComponente(div);
        
         /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    
    public void busVehiculo(){
        TablaGenerica tab_dato =pCombustible.getVehiculo(tab_tabla.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            TablaGenerica tab_datoc = pCombustible.getConductor(tab_dato.getValor("mve_conductor"));
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
        secuencial();
    }
    
    public void secuencial(){
        if(tab_tabla.getValor("numero_vale")!=null && tab_tabla.getValor("numero_vale").toString().isEmpty() == false){

        }else{
            Integer numero = Integer.parseInt(pCombustible.listaMax());
            Integer cantidad=0;
            cantidad=numero +1;
            tab_tabla.setValor("numero_vale", String.valueOf(cantidad));
            utilitario.addUpdate("tab_tabla");
        }
    }
    
    public void clean(){
        tab_tabla.setValor("galones", "");
        tab_tabla.setValor("total", "");
        utilitario.addUpdate("tab_tabla");
    }
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if(tab_tabla.getValor("ide_vale_consumo")!=null && tab_tabla.getValor("ide_vale_consumo").toString().isEmpty() == false){
            if(Boolean.parseBoolean(tab_consulta.getValor("PERM_UTIL_PERF"))!=false){
                pCombustible.ActualizaAnticipo(Integer.parseInt(tab_tabla.getValor("ide_tipo_combustible")), tab_tabla.getValor("conductor"), Double.valueOf(tab_tabla.getValor("galones")), 
                        Double.valueOf(tab_tabla.getValor("total")), tab_consulta.getValor("NICK_USUA"), Integer.parseInt(tab_tabla.getValor("ide_vale_consumo")), Integer.parseInt(tab_tabla.getValor("numero_vale")));
                utilitario.agregarMensaje("Registro Guardado","");
            }else{
                utilitario.agregarMensajeInfo("Registro No Puede Ser Modificado","No Posee Permisos");
            }
        }else{
            tab_tabla.guardar();
            guardarPantalla();
        }  
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
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
           case "VALE DE CONSUMO":
                aceptoAnticipo();
          break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "VALE DE CONSUMO":
                    p_parametros.put("vale", Integer.parseInt(tab_tabla.getValor("numero_vale")));
                    p_parametros.put("id", Integer.parseInt(tab_tabla.getValor("ide_vale_consumo")));
                    p_parametros.put("autoriza", tab_consulta.getValor("NOM_USUA")+"");
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
    
}
