/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.ProvisionCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_orden_consumo_masivo extends Pantalla{

    //Conexion a base
    private Conexion con_sql = new Conexion();
    
    private Tabla tab_formulario = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    
    @EJB
    private ProvisionCombustible pCombustible = (ProvisionCombustible) utilitario.instanciarEJB(ProvisionCombustible.class);
    
    public pre_orden_consumo_masivo() {
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
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("DATOS DE PROVISIÓN DE COMBUSTIBLE");
        
        tab_formulario.setId("tab_formulario");
        tab_formulario.setConexion(con_sql);
        tab_formulario.setSql("SELECT\n" +
"o.IDE_ORDEN_CONSUMO,\n" +
"o.NUMERO_ORDEN,\n" +
"o.PLACA_VEHICULO,\n" +
"o.DESCRIPCION_VEHICULO,\n" +
"o.CONDUCTOR,\n" +
"c.IDE_CALCULO_CONSUMO,\n" +
"c.FECHA_ABASTECIMIENTO,\n" +
"c.HORA_ABASTECIMIENTO,\n" +
"c.KILOMETRAJE,\n" +
"c.GALONES,\n" +
"c.TOTAL,\n" +
"c.FECHA_DIGITACION,\n" +
"c.HORA_DIGITACION,\n" +
"c.USU_DIGITACION,\n" +
"(t.DESCRIPCION_COMBUSTIBLE+'/'+cast(t.VALOR_GALON as varchar)) AS tipo,\n" +
"t.IDE_TIPO_COMBUSTIBLE\n" +
"FROM dbo.MVORDEN_CONSUMO AS o\n" +
"LEFT JOIN dbo.MVCALCULO_CONSUMO AS c ON c.IDE_ORDEN_CONSUMO = o.IDE_ORDEN_CONSUMO\n" +
"LEFT JOIN dbo.MVVEHICULO AS v ON o.PLACA_VEHICULO = v.MVE_PLACA\n" +
"LEFT JOIN dbo.MVTIPO_COMBUSTIBLE t ON v.MVE_TIPO_COMBUSTIBLE = t.IDE_TIPO_COMBUSTIBLE\n" +
"WHERE c.IDE_CALCULO_CONSUMO IS NULL");
        tab_formulario.getColumna("kilometraje").setMetodoChange("kilometraje");
        tab_formulario.getColumna("galones").setMetodoChange("galones");
        tab_formulario.getColumna("usu_digitacion").setVisible(false);
        tab_formulario.getColumna("ide_orden_consumo").setVisible(false);
        tab_formulario.getColumna("ide_tipo_combustible").setVisible(false);
        tab_formulario.getColumna("hora_digitacion").setVisible(false);
        tab_formulario.getColumna("fecha_digitacion").setVisible(false);
        tab_formulario.getColumna("ide_calculo_consumo").setVisible(false);
        tab_formulario.getColumna("conductor").setLongitud(50);
        tab_formulario.getColumna("tipo").setLongitud(2);
        tab_formulario.getColumna("fecha_abastecimiento").setLongitud(2);
        tab_formulario.getColumna("hora_abastecimiento").setLongitud(2);
        tab_formulario.setRows(10);
        tab_formulario.dibujar();
        PanelTabla ptt = new PanelTabla();
        ptt.setPanelTabla(tab_formulario);
        
         Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(ptt);
        agregarComponente(div_division);
    }
    
    public void kilometraje(){
        TablaGenerica tab_dato =pCombustible.getKilometraje(tab_formulario.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_KILOMETRAJE"));
            Double valor2 = Double.valueOf(tab_formulario.getValor("kilometraje"));
            if(valor2>valor1){
                tab_formulario.getColumna("galones").setLectura(false);
                tab_formulario.getColumna("total").setLectura(false);
                utilitario.addUpdate("tab_formulario");
            }else{
                utilitario.agregarMensajeError("Kilometraje","Por Debajo del Anterior");
                tab_formulario.getColumna("galones").setLectura(true);
                tab_formulario.getColumna("total").setLectura(true);
                tab_formulario.getColumna("tipo").setLectura(true);
                utilitario.addUpdate("tab_formulario");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void galones(){
        TablaGenerica tab_dato =pCombustible.getKilometraje(tab_formulario.getValor("placa_vehiculo"));
        if (!tab_dato.isEmpty()) {
            Double valor1 = Double.valueOf(tab_dato.getValor("MVE_CAPACIDAD_TANQUE_COMBUSTIBLE"));
            Double valor2 = Double.valueOf(tab_formulario.getValor("galones"));
            if(valor2<valor1){
                tab_formulario.getColumna("total").setLectura(false);
                utilitario.addUpdate("tab_formulario");
                        valor();
            }else{
                utilitario.agregarMensajeError("Galones","Exceden Capacidad de Vehiculo");
                tab_formulario.setValor("galones", null);
                tab_formulario.getColumna("total").setLectura(true);
                utilitario.addUpdate("tab_formulario");
            }
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    public void valor(){
        TablaGenerica tab_dato =pCombustible.getCombustible(Integer.parseInt(tab_formulario.getValor("ide_tipo_combustible")));
        if (!tab_dato.isEmpty()) {
            Double valor;
            valor = (Double.parseDouble(tab_dato.getValor("valor_galon"))*Double.parseDouble(tab_formulario.getValor("galones")));
            tab_formulario.setValor("total", String.valueOf(Math.rint(valor*100)/100));
            utilitario.addUpdate("tab_formulario");
        }else{
            utilitario.agregarMensajeError("Valor","No Se Encuentra Registrado");
        }
    }
    
    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {
        if(tab_formulario.getValor("fecha_abastecimiento")!=null && tab_formulario.getValor("fecha_abastecimiento").toString().isEmpty() == false){
            if(tab_formulario.getValor("hora_abastecimiento")!=null && tab_formulario.getValor("hora_abastecimiento").toString().isEmpty() == false){
                if(tab_formulario.getValor("kilometraje")!=null && tab_formulario.getValor("kilometraje").toString().isEmpty() == false){
                    if(tab_formulario.getValor("total")!=null && tab_formulario.getValor("total").toString().isEmpty() == false){
                        pCombustible.getConsumo(Integer.parseInt(tab_formulario.getValor("ide_orden_consumo")), tab_formulario.getValor("fecha_abastecimiento"), tab_formulario.getValor("hora_abastecimiento"), Double.valueOf(tab_formulario.getValor("kilometraje")),
                                Double.valueOf(tab_formulario.getValor("galones")), Double.valueOf(tab_formulario.getValor("total")), Integer.parseInt(tab_formulario.getValor("ide_tipo_combustible")), tab_formulario.getValor("placa_vehiculo"),tab_consulta.getValor("NICK_USUA"));
                        utilitario.agregarMensaje("Registros Guardados","");
                        tab_formulario.actualizar();
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_formulario() {
        return tab_formulario;
    }

    public void setTab_formulario(Tabla tab_formulario) {
        this.tab_formulario = tab_formulario;
    }
    
}
