/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.AntiSueldos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_reporte_anticipos extends Pantalla{
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Combos de Selección
    private Combo cmb_anos = new Combo();
    private Combo cmb_mesin = new Combo();
    private Combo cmb_mesfin = new Combo();
    
    //Dialogos
    private Dialogo dia_dialogo= new Dialogo();
    private Dialogo dia_dialogovgl= new Dialogo();
    private Dialogo dia_dialogoinvg = new Dialogo();
    private Grid grid_g = new Grid();
    private Grid grid_vgl = new Grid();
    private Grid grid_invg = new Grid();
    
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    
    
    public pre_reporte_anticipos() {
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("BUSCAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Actualizarlista");
        bar_botones.agregarBoton(bot_busca);
        
        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(con_postgres);
        tab_tabla1.setSql("SELECT DISTINCT s.ide_empleado_solicitante,s.ci_solicitante,s.solicitante\n" +
                "FROM srh_solicitud_anticipo AS s ORDER BY s.solicitante ASC");
        tab_tabla1.getColumna("ide_empleado_solicitante").setVisible(false);
        tab_tabla1.getColumna("ci_solicitante").setFiltro(true);
        tab_tabla1.getColumna("solicitante").setFiltro(true);
        tab_tabla1.setRows(26);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(con_postgres);
        tab_tabla2.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 1);
        tab_tabla2.getColumna("porcentaje_descuento_diciembre").setVisible(false);
        tab_tabla2.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_tabla2.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_tabla2.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        
        tab_tabla2.getColumna("usu_anulacion").setVisible(false);
        tab_tabla2.getColumna("usu_pago_anticipado").setVisible(false);
        tab_tabla2.getColumna("usu_cobra_liquidacion").setVisible(false);
        tab_tabla2.getColumna("ide_empleado").setVisible(false);
        tab_tabla2.getColumna("ide_calculo_anticipo").setVisible(false);
        tab_tabla2.setLectura(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1,pat_panel2, "28%", "V");
        agregarComponente(div_division);
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        
         Grid gri_search = new Grid();
        gri_search.setColumns(2);
               
        gri_search.getChildren().add(new Etiqueta("AÑO: "));
        cmb_anos.setId("cmb_anos");
        cmb_anos.setConexion(con_postgres);
        cmb_anos.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        cmb_anos.setMetodo("comp_anio");
        gri_search.getChildren().add(cmb_anos);
        
        gri_search.getChildren().add(new Etiqueta("Periodo Inicial: "));
        cmb_mesin.setId("cmb_mesin");
        cmb_mesin.setConexion(con_postgres);
        cmb_mesin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_search.getChildren().add(cmb_mesin);
        
        gri_search.getChildren().add(new Etiqueta("Periodo final: "));
        cmb_mesfin.setId("cmb_mesfin");
        cmb_mesfin.setConexion(con_postgres);
        cmb_mesfin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_search.getChildren().add(cmb_mesfin);
        
        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("25%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_search);
        dia_dialogo.getBot_aceptar().setMetodo("aceptoAnticipo");
        grid_g.setColumns(4);
        agregarComponente(dia_dialogo);
    }

    public void comp_anio(){
        if(Integer.parseInt(cmb_anos.getValue()+"")<=2014){
            utilitario.agregarMensaje("Detalle No Disponible Para Año Seleccionado", "Escoga otro ???");
        }
    }
    
    public void Actualizarlista(){
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tabla2.setCondicion(getFiltrosAcceso());
            tab_tabla2.ejecutarSql();
            utilitario.addUpdate("tab_tabla2");
        }
    }
    
    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tab_tabla1.getValorSeleccionado() != null
                ) {

			str_filtros = "ide_empleado = "
					+ String.valueOf(tab_tabla1.getValorSeleccionado());

		} else {
			utilitario.agregarMensajeInfo("Filtros no válidos",
					"Debe ingresar los fitros de vehiculo y servicio");
		}
		return str_filtros;
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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "VER ANTICIPO":
               aceptoAnticipo();
          break;
           case "CONSOLIDADO DE  ANTICIPO":
               dia_dialogo.Limpiar();
               dia_dialogo.dibujar();
               break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "VER ANTICIPO":
                    TablaGenerica tab_dato = iAnticipos.getCedula(Integer.parseInt(tab_tabla2.getValorSeleccionado()));
                    if (!tab_dato.isEmpty()) {
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("identificacion", tab_dato.getValor("ci_solicitante")+"");
                    p_parametros.put("codigo", Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo")+""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    }else{
                        utilitario.agregarMensaje("No puede Mostrar Reporte", "Datos no encontrados");
                    }
               break;
               case "CONSOLIDADO DE  ANTICIPO":
                   TablaGenerica tab_dato1 = iAnticipos.getTotalAnt(String.valueOf(Integer.parseInt(cmb_anos.getValue()+"")-1));
                   if (!tab_dato1.isEmpty()) {
                       p_parametros.put("anio_anterior", String.valueOf(Integer.parseInt(cmb_anos.getValue()+"")-1));
                       p_parametros.put("mes", cmb_mesin.getValue()+"");
                       p_parametros.put("mes1", cmb_mesfin.getValue()+"");
                       p_parametros.put("anio_actual",cmb_anos.getValue()+"");
                       p_parametros.put("anticipo", Double.valueOf(tab_dato1.getValor("anticipo")+""));
                       p_parametros.put("descuento", Double.valueOf(tab_dato1.getValor("descontar")+""));
                       p_parametros.put("saldo", Double.valueOf(tab_dato1.getValor("saldo")+""));
                       p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                       sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                       sef_formato.dibujar();
                   }else{
                       utilitario.agregarMensaje("No puede Mostrar Reporte", "Detalle No encontrado");
                   }
                   break;
        }
    }
    
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
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
