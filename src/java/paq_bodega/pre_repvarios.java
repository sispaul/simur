/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
public class pre_repvarios extends Pantalla {
    private Conexion con_postgres= new Conexion();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Consulta
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_consulta_ano = new Tabla();
    //Tablas
    private Tabla set_tablaMat = new Tabla();
    //combo año
    private Combo cmb_anio = new Combo();
    //dialogos
    private Dialogo dia_repmat= new Dialogo();
    private Dialogo dia_karart= new Dialogo();
    private Dialogo dia_iimat= new Dialogo();
    //Grids
    private Grid grid_anio = new Grid();
    

    public pre_repvarios() {
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        bar_botones.limpiar(); /// deja en blanco la barra de botones
        
        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:270px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("REPORTES VARIOS");
        grid_pant.getChildren().add(eti_encab);
        Boton bot_lista = new Boton();
        bot_lista.setValue("LISTA DE REPORTES");
        bot_lista.setMetodo("abrirListaReportes");
        grid_pant.getChildren().add(bot_lista);
        agregarComponente(grid_pant);
        
        //Combo año
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        cmb_anio.setLabel("Año Curso");
        grid_anio.setColumns(2);
        grid_anio.setWidth("300px");
        grid_anio.getChildren().add(new Etiqueta("Año Curso"));
        grid_anio.getChildren().add(cmb_anio);
        
        ///configurar tabla Materiales
        set_tablaMat.setId("set_tablaMat");
        set_tablaMat.setConexion(con_postgres);
        set_tablaMat.setSql("select ide_mat_bodega, cod_material, des_material from bodc_material order by des_material");
        set_tablaMat.getColumna("des_material").setFiltro(true);
        set_tablaMat.getColumna("cod_material").setFiltro(true);
        set_tablaMat.setRows(10);
        set_tablaMat.setTipoSeleccion(false);
        set_tablaMat.dibujar();
        
        //Configurando el dialogo REPORTE DE MATERIALES
        dia_repmat.setId("dia_repmat");
        dia_repmat.setTitle("BODEGA - REPORTE DE MATERIALES"); //titulo
        dia_repmat.setWidth("30%"); //siempre en porcentajes  ancho
        dia_repmat.setHeight("25%");//siempre porcentaje   alto
        dia_repmat.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_repmat.getBot_aceptar().setMetodo("aceptoDialogo");
        
        agregarComponente(dia_repmat);
        
        
         //Configurando el dialogo KARDEX DE ARTICULOS
        dia_karart.setId("dia_karart");
        dia_karart.setTitle("BODEGA - KARDEX DE ARTICULOS"); //titulo
        dia_karart.setWidth("70%"); //siempre en porcentajes  ancho
        dia_karart.setHeight("60%");//siempre porcentaje   alto
        dia_karart.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_karart.getBot_aceptar().setMetodo("aceptoDialogo");
        
        agregarComponente(dia_karart);
        
        
        //Configurando el dialogo KARDEX DE ARTICULOS
        dia_karart.setId("dia_karart");
        dia_karart.setTitle("BODEGA - KARDEX DE ARTICULOS"); //titulo
        dia_karart.setWidth("70%"); //siempre en porcentajes  ancho
        dia_karart.setHeight("60%");//siempre porcentaje   alto
        dia_karart.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_karart.getBot_aceptar().setMetodo("aceptoDialogo");
        
        agregarComponente(dia_karart);
        
        
         //Configurando el dialogo KARDEX DE ARTICULOS
        dia_iimat.setId("dia_iimat");
        dia_iimat.setTitle("BODEGA - KARDEX DE ARTICULOS"); //titulo
        dia_iimat.setWidth("30%"); //siempre en porcentajes  ancho
        dia_iimat.setHeight("25%");//siempre porcentaje   alto
        dia_iimat.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_iimat.getBot_aceptar().setMetodo("aceptoDialogo");
        
        agregarComponente(dia_iimat);
        
        
          /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        
        
        /**
         * PERSONA RESPONSABLE
         */
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        /**
         * PERSONA RESPONSABLE
         */
        tab_consulta_ano.setId("tab_consulta_ano");
        tab_consulta_ano.setSql("select ano_curso,ano_curso from conc_ano where actual='A'");
        tab_consulta_ano.setCampoPrimaria("ano_curso");
        tab_consulta_ano.setLectura(true);
        tab_consulta_ano.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Reporte de Materiales":
                dia_repmat.Limpiar();
                Etiqueta eti0 = new Etiqueta();
                eti0.setStyle("font-size:18px;color:blue");
                eti0.setValue("SELECCIONE AÑO");
                cmb_anio.setValue(tab_consulta_ano.getValor("ano_curso")+"");
                dia_repmat.setDialogo(eti0);
                dia_repmat.setDialogo(grid_anio);
                dia_repmat.dibujar();
                break;
            case "Kardex de artículo":
                dia_karart.Limpiar();
                Etiqueta eti1 = new Etiqueta();
                eti1.setStyle("font-size:18px;color:blue");
                eti1.setValue("SELECCIONE AÑO");
                cmb_anio.setValue(tab_consulta_ano.getValor("ano_curso")+"");
                dia_karart.setDialogo(eti1);
                dia_karart.setDialogo(grid_anio);
                Etiqueta eti2 = new Etiqueta();
                eti2.setStyle("font-size:18px;color:blue");
                eti2.setValue("SELECCIONE MATERIAL");
                dia_karart.setDialogo(eti2);
                dia_karart.setDialogo(set_tablaMat);
                set_tablaMat.dibujar();
                dia_karart.dibujar();
                break;
            case "Inventario inicial de materiales":
                dia_iimat.Limpiar();
                Etiqueta eti3 = new Etiqueta();
                eti3.setStyle("font-size:18px;color:blue");
                eti3.setValue("SELECCIONE AÑO");
                cmb_anio.setValue(tab_consulta_ano.getValor("ano_curso")+"");
                dia_iimat.setDialogo(eti3);
                dia_iimat.setDialogo(grid_anio);
                dia_iimat.dibujar();
                break;
            case "KARDEX GENERAL":
                aceptoDialogo();
                break;
            case "DETALLE DE INGRESO POR CUENTAS":
                aceptoDialogo();
                break;
            case "DETALLE DE INGRESOS":
                aceptoDialogo();
                break;
            case "DETALLE DE EGRESOS":
                aceptoDialogo();
                break;
            case "DETALLE GRUPOS DE MATERIALES":
                aceptoDialogo();
                break;
            case "TOTAL GRUPOS DE MATERIAL":
                aceptoDialogo();
                break;
        }
    }
    
    
    public void aceptoDialogo() {
        
        switch (rep_reporte.getNombre()) {
            case "Reporte de Materiales":
                  //los parametros de este reporte
                    if (cmb_anio.getValue() != null) {     
                        p_parametros = new HashMap();
                        //p_parametros.put("p_grupo", set_tabla.getSeleccionados());
                        p_parametros.put("pide_ano", Integer.parseInt(cmb_anio.getValue()+""));
                        dia_repmat.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                
                break;
            case "Kardex de artículo":
//                    /los parametros de este reporte
                    if ((cmb_anio.getValue() != null) && (set_tablaMat.getValorSeleccionado() != null)) {     
                        p_parametros = new HashMap();
                        //p_parametros.put("p_grupo", set_tabla.getSeleccionados());
                        p_parametros.put("pide_ano", Integer.parseInt(cmb_anio.getValue()+""));
                        p_parametros.put("pide_mat_articulo", Integer.parseInt(set_tablaMat.getValorSeleccionado()+""));
                        dia_karart.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro", "");
                    }
                break;
            case "Inventario inicial de materiales":
                    //los parametros de este reporte
                    if (cmb_anio.getValue() != null) {     
                        p_parametros = new HashMap();
                        //p_parametros.put("p_grupo", set_tabla.getSeleccionados());
                        p_parametros.put("pide_ano", Integer.parseInt(cmb_anio.getValue()+""));
                        dia_iimat.cerrar();
                        sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                
                break;
             case "KARDEX GENERAL":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("pide_ano", Integer.parseInt(tab_consulta_ano.getValor("ano_curso"+"")));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
            case "DETALLE DE INGRESO POR CUENTAS":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
            case "DETALLE DE INGRESOS":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
            case "DETALLE DE EGRESOS":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
            case "DETALLE GRUPOS DE MATERIALES":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("p_ano", Integer.parseInt(tab_consulta_ano.getValor("ano_curso"+"")));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
            case "TOTAL GRUPOS DE MATERIAL":
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("p_ano", Integer.parseInt(tab_consulta_ano.getValor("ano_curso"+"")));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                break;
             
        }
        
    }
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

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

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
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

    public Tabla getSet_tablaMat() {
        return set_tablaMat;
    }

    public void setSet_tablaMat(Tabla set_tablaMat) {
        this.set_tablaMat = set_tablaMat;
    }

    public Combo getCmb_anio() {
        return cmb_anio;
    }

    public void setCmb_anio(Combo cmb_anio) {
        this.cmb_anio = cmb_anio;
    }
    
}
