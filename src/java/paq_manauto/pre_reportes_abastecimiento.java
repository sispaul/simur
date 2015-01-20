/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_manauto.ejb.manauto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_reportes_abastecimiento extends Pantalla{
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    private Panel pan_opcion = new Panel();
    
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Combos de Selección
    private Combo cmb_anio = new Combo();
    private Combo cmb_ano = new Combo();
    private Combo cmb_anos = new Combo();
    private Combo cmb_periodo = new Combo();
    private Combo cmb_peri = new Combo();
    private Combo cmb_periodos = new Combo();
    private Combo cmb_placa = new Combo();
    private Combo cmb_placa1 = new Combo();
    private Combo cmb_general = new Combo();
    
    //Dialogos
    private Dialogo dia_dialogo= new Dialogo();
    private Dialogo dia_dialogovgl= new Dialogo();
    private Dialogo dia_dialogoinvg = new Dialogo();
    private Grid grid_g = new Grid();
    private Grid grid_vgl = new Grid();
    private Grid grid_invg = new Grid();
    
    @EJB
    private manauto aCombustible = (manauto) utilitario.instanciarEJB(manauto.class);
    
    public pre_reportes_abastecimiento() {
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_transporte.png");
        agregarComponente(quinde);
 
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        
        //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        Grid gri_search = new Grid();
        gri_search.setColumns(2);
        gri_search.getChildren().add(new Etiqueta("NIVEL INICIAL: "));
        
        gri_search.getChildren().add(new Etiqueta("AÑO:"));
        cmb_anos.setId("cmb_anos");
        cmb_anos.setConexion(con_postgres);
        cmb_anos.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_search.getChildren().add(cmb_anos);
        
        gri_search.getChildren().add(new Etiqueta("PERIODO:"));
        cmb_periodos.setId("cmb_periodos");
        cmb_periodos.setConexion(con_postgres);
        cmb_periodos.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_search.getChildren().add(cmb_periodos);
        
        List lista = new ArrayList();
        Object filase1[] = {
            "K", "Automotores"
        };
        Object filase2[] = {
            "M", "Maquinaria"
        };
        lista.add(filase1);;
        lista.add(filase2);;
        cmb_general.setCombo(lista);
        cmb_general.eliminarVacio();
        gri_search.getChildren().add(cmb_general);
        
        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("25%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_search);
        dia_dialogo.getBot_aceptar().setMetodo("aceptarDialogo");
        grid_g.setColumns(4);
        agregarComponente(dia_dialogo);
        
        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:270px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("REPORTES COMSUMO DE AUTOMOTORES / MAQUINARIAS");
        grid_pant.getChildren().add(eti_encab);
        Boton bot_lista = new Boton();
        bot_lista.setValue("LISTA DE REPORTES");
        bot_lista.setMetodo("abrirListaReportes");
        grid_pant.getChildren().add(bot_lista);
        agregarComponente(grid_pant);
        pan_opcion.getChildren().add(grid_pant);
        agregarComponente(pan_opcion);
        
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
        
        gri_busca1.getChildren().add(new Etiqueta("PLACA :"));
        cmb_placa1.setId("cmb_placa1");
        cmb_placa1.setConexion(con_postgres);
        cmb_placa1.setCombo("SELECT DISTINCT mve_placa,mve_placa FROM mv_vehiculo ORDER BY mve_placa");
        gri_busca1.getChildren().add(cmb_placa1);
        
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
        cmb_placa.setConexion(con_postgres);
        cmb_placa.setCombo("SELECT DISTINCT mve_placa,mve_placa FROM mv_vehiculo ORDER BY mve_placa");
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
                case "REPORTE GENERAL ABASTECIMIENTO/MANTENIMIENTO":
                    dia_dialogo.Limpiar();
                    dia_dialogo.dibujar();
                    break;
                case "REPORTE INDIVIDUAL ABASTECIMIENTO":
                    dia_dialogovgl.Limpiar();
                    dia_dialogovgl.dibujar();
                    break;
                case "REPORTE INDIVIDUAL MANTENIMIENTO":
                    dia_dialogoinvg.Limpiar();
                    dia_dialogoinvg.dibujar();
                    break;
            }
        }
        
        public void aceptarDialogo() {
            switch (rep_reporte.getNombre()) {
                case "REPORTE GENERAL ABASTECIMIENTO/MANTENIMIENTO":
                    System.err.println(cmb_periodos.getValue());
                    TablaGenerica tab_dato =aCombustible.getMes(Integer.parseInt(cmb_periodos.getValue()+""));
                   if (!tab_dato.isEmpty()) {
                    p_parametros.put("anio", cmb_anos.getValue()+"");
                    p_parametros.put("mes", tab_dato.getValor("per_descripcion")+"");
                    p_parametros.put("periodo", cmb_periodos.getValue()+"");
                    p_parametros.put("tipo", cmb_general.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                       utilitario.agregarMensajeError("Usuario","No Disponible");
                   }
                    break;
                case "REPORTE INDIVIDUAL ABASTECIMIENTO":
                    TablaGenerica tab_dato1 =aCombustible.getMes(Integer.parseInt(cmb_peri.getValue()+""));
                    if (!tab_dato1.isEmpty()) {
                        TablaGenerica tab_datov =aCombustible.getDesResporte(cmb_placa1.getValue()+"");
                        if (!tab_datov.isEmpty()) {
                            p_parametros.put("anio", cmb_ano.getValue()+"");
                            p_parametros.put("mes", tab_dato1.getValor("per_descripcion")+"");
                            p_parametros.put("periodo", cmb_peri.getValue()+"");
                            p_parametros.put("placa", cmb_placa1.getValue()+"");
                            p_parametros.put("descripcion", tab_datov.getValor("mvmarca_descripcion")+"");
                            p_parametros.put("tipo", tab_datov.getValor("mvtipo_descripcion")+"");
                            p_parametros.put("modelo", tab_datov.getValor("mvmodelo_descripcion")+"");
                            p_parametros.put("version", tab_datov.getValor("mvversion_descripcion")+"");
                            rep_reporte.cerrar();
                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                            System.err.println(p_parametros);
                            sef_formato.dibujar();
                        }else{
                            utilitario.agregarMensajeError("Placa","No Disponible");
                        }
                    }else{
                        utilitario.agregarMensajeError("Periodo","No Seleccionado");
                    }
                    break;
                case "REPORTE INDIVIDUAL MANTENIMIENTO":
                    TablaGenerica tab_dato2 =aCombustible.getMes(Integer.parseInt(cmb_periodo.getValue()+""));
                    if (!tab_dato2.isEmpty()) {
                        TablaGenerica tab_datov =aCombustible.getDesResporte(cmb_placa.getValue()+"");
                        if (!tab_datov.isEmpty()) {
                            p_parametros.put("anio", cmb_anio.getValue()+"");
                            p_parametros.put("mes", tab_dato2.getValor("per_descripcion")+"");
                            p_parametros.put("periodo", cmb_periodo.getValue()+"");
                            p_parametros.put("placa", cmb_placa.getValue()+"");
                            p_parametros.put("descripcion", tab_datov.getValor("descripcion")+"");
                            rep_reporte.cerrar();
                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                            System.err.println(p_parametros);
                            sef_formato.dibujar();
                        }else{
                            utilitario.agregarMensajeError("Placa","No Disponible");
                        }
                    }else{
                        utilitario.agregarMensajeError("Periodo","No Seleccionado");
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
