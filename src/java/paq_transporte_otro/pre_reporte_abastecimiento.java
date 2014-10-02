/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panelmenu.PanelMenu;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_reporte_abastecimiento extends Pantalla{

    //Conexion a base
    private Conexion con_sql = new Conexion();
    private Conexion con_postgres= new Conexion();
    
    private Panel pan_opcion = new Panel();
    
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Dialogos
    private Dialogo dia_dialogovgl= new Dialogo();
    private Dialogo dia_dialogoinvg = new Dialogo();
    private Grid grid_vgl = new Grid();
    private Grid grid_invg = new Grid();
    
    //Combos de Selección
    private Combo cmb_anio = new Combo();
    private Combo cmb_ano = new Combo();
    private Combo cmb_periodo = new Combo();
    private Combo cmb_peri = new Combo();
    private Combo cmb_placa = new Combo();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_reporte_abastecimiento() {
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
        
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        
        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:270px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("REPORTES ABASTECIMIENTO DE COMBUSTIBLE");
        grid_pant.getChildren().add(eti_encab);
        Boton bot_lista = new Boton();
        bot_lista.setValue("LISTA DE REPORTES");
        bot_lista.setMetodo("abrirListaReportes");
        grid_pant.getChildren().add(bot_lista);
        agregarComponente(grid_pant);
        pan_opcion.getChildren().add(grid_pant);
        agregarComponente(pan_opcion);
                //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_sql);
        agregarComponente(sef_formato);
        
        //Dialogos para ingreso d eparametros pra reportes
        /*CONFIGURACIÓN DE COMBOS*/
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        
        gri_busca1.getChildren().add(new Etiqueta("AÑO:"));
        cmb_ano.setId("cmb_ano");
        cmb_ano.setConexion(con_postgres);
        cmb_ano.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca1.getChildren().add(cmb_ano);
        
        gri_busca1.getChildren().add(new Etiqueta("PERIODO:"));
        cmb_peri.setId("cmb_peri");
        cmb_peri.setConexion(con_postgres);
        cmb_peri.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca1.getChildren().add(cmb_peri);
        
         //para poder busca por apelllido el garante
        dia_dialogovgl.setId("dia_dialogovgl");
        dia_dialogovgl.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        dia_dialogovgl.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogovgl.setHeight("25%");//siempre porcentaje   alto
        dia_dialogovgl.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogovgl.getGri_cuerpo().setHeader(gri_busca1);
        dia_dialogovgl.getBot_aceptar().setMetodo("aceptarDialogo");
        grid_vgl.setColumns(4);
        agregarComponente(dia_dialogovgl);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        
        gri_busca.getChildren().add(new Etiqueta("AÑO:"));
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca.getChildren().add(cmb_anio);
        
        gri_busca.getChildren().add(new Etiqueta("PERIODO:"));
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca.getChildren().add(cmb_periodo);
        
        gri_busca.getChildren().add(new Etiqueta("PLACA :"));
        cmb_placa.setId("cmb_placa");
        cmb_placa.setConexion(con_sql);
        cmb_placa.setCombo("SELECT DISTINCT PLACA_VEHICULO,PLACA_VEHICULO FROM MVABASTECIMIENTO_COMBUSTIBLE ORDER BY PLACA_VEHICULO");
        gri_busca.getChildren().add(cmb_placa);
        
         //para poder busca por apelllido el garante
        dia_dialogoinvg.setId("dia_dialogoinvg");
        dia_dialogoinvg.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        dia_dialogoinvg.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoinvg.setHeight("25%");//siempre porcentaje   alto
        dia_dialogoinvg.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoinvg.getGri_cuerpo().setHeader(gri_busca);
        dia_dialogoinvg.getBot_aceptar().setMetodo("aceptarDialogo");
        grid_invg.setColumns(4);
        agregarComponente(dia_dialogoinvg);
        
    }
        @Override
        public void abrirListaReportes() {
            rep_reporte.dibujar();
        } 
        
        @Override
        public void aceptarReporte() {
            rep_reporte.cerrar();
            switch (rep_reporte.getNombre()) {
                case "ABASTECIMIENTO GENERAL POR MES GALONES/CONSUMO":
                    dia_dialogovgl.Limpiar();
                    dia_dialogovgl.dibujar();
                    break;
                case "ABASTECIMIENTO GENERAL POR kILOMETROS":
                    dia_dialogovgl.Limpiar();
                    dia_dialogovgl.dibujar();
                    break;
                case "ABASTECIMIENTO POR VEHICULO GALONES/CONSUMO":
                    dia_dialogoinvg.Limpiar();
                    dia_dialogoinvg.dibujar();
                    break;
                case "ABASTECIMIENTO POR VEHICULO KILOMETROS":
                    dia_dialogoinvg.Limpiar();
                    dia_dialogoinvg.dibujar();
                    break;
            }
        }
        
        public void aceptarDialogo() {
            switch (rep_reporte.getNombre()) {
                case "ABASTECIMIENTO GENERAL POR MES GALONES/CONSUMO":
                    TablaGenerica tab_dato =aCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                    break;
                case "ABASTECIMIENTO GENERAL POR kILOMETROS":
                    TablaGenerica tab_dato1 =aCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato1.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato1.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                    break;
                case "ABASTECIMIENTO POR VEHICULO GALONES/CONSUMO":
                    TablaGenerica tab_dato2 =aCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato2.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato2.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    p_parametros.put("placa", cmb_placa.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                    break;
                case "ABASTECIMIENTO POR VEHICULO KILOMETROS":
                    TablaGenerica tab_dato3 =aCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                   if (!tab_dato3.isEmpty()) {
                    p_parametros.put("anio", cmb_anio.getValue()+"");
                    p_parametros.put("mes", tab_dato3.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodo.getValue()+"");
                    p_parametros.put("placa", cmb_placa.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                    break;
            }
        }
        
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
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
