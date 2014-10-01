/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_abastecimiento_combustible extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //Para tabla de
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //Busca de comprobante
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    public pre_abastecimiento_combustible() {
        
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
        
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_sql);
        aut_busca.setAutoCompletar("SELECT ide_abastecimiento_combustible,PLACA_VEHICULO,NUMERO_VALE_ABASTECIMIENTO,FECHA_ABASTECIMIENTO,\n" +
                "NUMERO_ABASTECIMIENTO\n" +
                "FROM MVABASTECIMIENTO_COMBUSTIBLE");
        aut_busca.setMetodoChange("buscarOrden");
        aut_busca.setSize(70);
        
//        bar_botones.agregarComponente(new Etiqueta("Buscar Orden:"));
//        bar_botones.agregarComponente(aut_busca);
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("REGISTRO DE ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(pan_opcion);
        dibujarAbastecimiento();
    }

    public void dibujarAbastecimiento(){
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
        tab_tabla.getColumna("ide_tipo_combustible").setLectura(true);
        tab_tabla.getColumna("descripcion_vehiculo").setLectura(true);
        tab_tabla.getColumna("numero_abastecimiento").setLectura(true);
        tab_tabla.getColumna("total").setLectura(true);
        tab_tabla.getColumna("ide_abastecimiento_combustible").setVisible(false);
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
        tab_tabla.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_tabla.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir1(ptt);
        agregarComponente(div);
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
            }else{
                utilitario.agregarMensajeError("Kilometraje","Por Debajo del Anterior");
                tab_tabla.getColumna("galones").setLectura(true);
                utilitario.addUpdate("tab_calculo");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
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
            Integer numero = Integer.parseInt(aCombustible.listaMax());
            Integer cantidad=0;
            cantidad=numero +1;
            tab_tabla.setValor("numero_abastecimiento", String.valueOf(cantidad));
            utilitario.addUpdate("tab_tabla");
        }
    }
        
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if(tab_tabla.isFocus()){
            tab_tabla.guardar();
            con_sql.guardarPantalla();
        }
        actuKilometrajes();
    }

   public void actuKilometrajes(){
        if(tab_tabla.getValor("ide_abastecimiento_combustible")!=null && tab_tabla.getValor("ide_abastecimiento_combustible").toString().isEmpty() == false){
            aCombustible.ActKilometraje(tab_tabla.getValor("placa_vehiculo"), Double.parseDouble(tab_tabla.getValor("kilometraje")));
        }
    }
   
    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}
