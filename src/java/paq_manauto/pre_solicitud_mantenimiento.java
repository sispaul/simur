/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_manauto.ejb.manauto;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_mantenimiento extends Pantalla{

    //conexion a base de datos
    private Conexion con_postgres = new Conexion();
    
    //declaracion de tablas
    private Tabla tab_tabla = new Tabla();
    
    @EJB
    private Programas programas =(Programas) utilitario.instanciarEJB(Programas.class);
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    public pre_solicitud_mantenimiento() {
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("mvcab_mantenimiento", "mca_codigo", 1);
        
        tab_tabla.getColumna("mve_secuencial").setCombo("SELECT v.mve_secuencial,\n" +
                "(v.mve_placa||' / '||m.mvmarca_descripcion ||' / '||t.mvtipo_descripcion||' / '||o.mvmodelo_descripcion ||' / '||v.mve_ano) AS descripcion\n" +
                "FROM mv_vehiculo AS v\n" +
                "INNER JOIN mvmarca_vehiculo AS m ON v.mvmarca_id = m.mvmarca_id\n" +
                "INNER JOIN mvmodelo_vehiculo AS o ON v.mvmodelo_id = o.mvmodelo_id\n" +
                "INNER JOIN mvtipo_vehiculo t ON t.mvmarca_id = m.mvmarca_id\n" +
                "ORDER BY v.mve_secuencial ASC");
        tab_tabla.getColumna("mca_cod_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores  where ruc <> '0' order by titular");
        tab_tabla.getColumna("mca_cod_autoriza").setCombo("select cod_empleado,nombres from srh_empleado where estado = 1 or cod_empleado = 100 order by nombres");
        tab_tabla.getColumna("mca_cod_responsable").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where cod_cargo in (SELECT cod_cargo FROM srh_cargos WHERE nombre_cargo like '%CHOFER%') and estado = 1 order by nombres");
                
        tab_tabla.getColumna("mve_secuencial").setFiltroContenido();
        tab_tabla.getColumna("mca_cod_proveedor").setFiltroContenido();
        tab_tabla.getColumna("mca_cod_autoriza").setFiltroContenido();
        tab_tabla.getColumna("mca_cod_responsable").setFiltroContenido();
        
        tab_tabla.getColumna("mca_fechainman").setValorDefecto(utilitario.getFechaActual());
        
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
        tab_tabla.getColumna("mca_tipo_mantenimiento").setCombo(lista);
        
        tab_tabla.getColumna("mve_secuencial").setMetodoChange("caracteristicas");
        tab_tabla.getColumna("mca_cod_proveedor").setMetodoChange("proveedor");
        tab_tabla.getColumna("mca_cod_autoriza").setMetodoChange("empleado");
        tab_tabla.getColumna("mca_cod_responsable").setMetodoChange("responsable");
        
        tab_tabla.getColumna("mca_horasaman").setVisible(false);
        tab_tabla.getColumna("mca_horainman").setVisible(false);
        tab_tabla.getColumna("mca_fechasaman").setVisible(false);
        tab_tabla.getColumna("mca_kmanterior_hora").setVisible(false);
        tab_tabla.getColumna("mca_kmactual_hora").setVisible(false);
        tab_tabla.getColumna("mca_acotaciones").setVisible(false);
        tab_tabla.getColumna("mca_tipomedicion").setVisible(false);
        tab_tabla.getColumna("mca_loginingreso").setVisible(false);
        tab_tabla.getColumna("mca_fechaingreso").setVisible(false);
        tab_tabla.getColumna("mca_loginactuali").setVisible(false);
        tab_tabla.getColumna("mca_fechactuali").setVisible(false);
        tab_tabla.getColumna("mca_loginborrado").setVisible(false);
        tab_tabla.getColumna("mca_fechaborrado").setVisible(false);
        tab_tabla.getColumna("mca_estado_tramite").setVisible(false);
        tab_tabla.getColumna("mca_tiposol").setVisible(false);
        tab_tabla.getColumna("mca_monto").setVisible(false);
        tab_tabla.getColumna("mca_anio").setVisible(false);
        tab_tabla.getColumna("mca_periodo").setVisible(false);
        tab_tabla.getColumna("mca_codigo").setVisible(false);
        tab_tabla.getColumna("mca_autorizado").setVisible(false);
        tab_tabla.getColumna("mca_proveedor").setVisible(false);
        tab_tabla.getColumna("mca_responsable").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(2);
        tab_tabla.dibujar();
        
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_tabla);
        
        Division div = new Division();
        div.dividir1(ptt);
        agregarComponente(div);
    }

    public void proveedor(){
            TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mve_secuencial", tab_dato.getValor("titular"));
                utilitario.addUpdate("tab_tabla");//actualiza solo componentes
            }
    }
    
    //busca datos de vehiculo que se selecciona
    public void caracteristicas(){
        TablaGenerica tab_dato =aCombustible.getVehiculo(Integer.parseInt(tab_tabla.getValor("mve_secuencial")));
        if (!tab_dato.isEmpty()) {
                    tab_tabla.setValor("mca_responsable", tab_dato.getValor("mve_conductor"));
                    tab_tabla.setValor("mca_cod_responsable", tab_dato.getValor("mve_cod_conductor"));
                    utilitario.addUpdate("tab_tabla");
        }else{
            utilitario.agregarMensajeError("No Se Encuentra Responsable","");
        }
    }
    
    public void empleado(){
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tab_tabla.getValor("mca_cod_autoriza")));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mca_autorizado", tab_dato.getValor("nombres"));
                utilitario.addUpdate("tab_tabla");//actualiza solo componentes
            }
    }
    
    public void responsable(){
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tab_tabla.getValor("mca_cod_responsable")));
            if (!tab_dato.isEmpty()) {
                tab_tabla.setValor("mca_responsable", tab_dato.getValor("nombres"));
                utilitario.addUpdate("tab_tabla");//actualiza solo componentes
            }
    }
    
    public void secuencial(){
        
    }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        secuencial();
    }

    @Override
    public void guardar() {
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

}
