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
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.mergeDescuento;

import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author m-paucar
 * 
 *
 */
public class pre_descuento extends Pantalla{
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
private Tabla tab_tabla = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_usuario = new Tabla();

//COMBOS DE SELECICON

private Combo cmb_anio = new Combo();
private Combo cmb_periodo = new Combo();
private Combo cmb_rol = new Combo();
private Combo cmb_descripcion = new Combo();

private Dialogo dia_dialogoDes = new Dialogo();
private Grid grid_dt = new Grid();
private Grid gri_busca = new Grid();
//1.-
 @EJB
private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
 
private Conexion con_postgres= new Conexion();
    public pre_descuento() {
        //2.-
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        //3.-
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        tab_tabla.setId("tab_tabla");
        //4.-
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setNumeroTabla(1);       
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
       
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        
        agregarComponente(div_division);
        
        Boton bot1 = new Boton();
        bot1.setValue("DEPURAR DESCUENTO");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("completar");
       bar_botones.agregarBoton(bot1); 
     
    
        Boton bot2 = new Boton();
        bot2.setValue("MIGRAR DESCUENTO");
        bot2.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot2.setMetodo("migrar");
       bar_botones.agregarBoton(bot2); 
       
        Boton bot3 = new Boton();
        bot3.setValue("BORRAR DESCUENTO");
        bot3.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot3.setMetodo("borrar");
       bar_botones.agregarBoton(bot3);  
       
       /**
         * CONFIGURACIÓN DE COMBOS
         */
       
        dia_dialogoDes.setId("dia_dialogoDes");
        dia_dialogoDes.setTitle("PARAMETROS - DECUENTOS"); //titulo
        dia_dialogoDes.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoDes.setHeight("25%");//siempre porcentaje   alto
        dia_dialogoDes.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoDes.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_dt.setColumns(4);
        agregarComponente(dia_dialogoDes);
        ///configurar tabla Destino
         
//        gri_busca.setColumns(2);
//        gri_busca.getChildren().add(new Etiqueta("AÑO  :"));
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        
//        gri_busca.getChildren().add(new Etiqueta("PERIODO  :"));
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        
        cmb_descripcion.setId("cmb_descripcion");
        cmb_descripcion.setConexion(con_postgres);
        cmb_descripcion.getChildren().add(new Etiqueta("DESCRIPCIÓN  :"));
        cmb_descripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");


        
         /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        tab_usuario.setId("tab_usuario");
        tab_usuario.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_usuario.setCampoPrimaria("IDE_USUA");
        tab_usuario.setLectura(true);
        tab_usuario.dibujar();
    }
    
    
    
      public void completar() {
  
         Integer ano;
         Integer ide_periodo;
         Integer id_distributivo_roles;
         Integer ide_columna;
         
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
         tab_consulta.ejecutarSql();
         ano = Integer.parseInt(tab_consulta.getValor("ano"));
         ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_columna"));
         id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
         ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;
         
        mDescuento.actualizarDescuento(ano, ide_periodo, id_distributivo_roles, ide_columna);
        mDescuento.actualizarDescuento1(ano, ide_periodo, id_distributivo_roles, ide_columna);
        tab_tabla.actualizar();
        
        }
      
       public void migrar(){                     
         Integer ano;
         Integer ide_periodo;
         Integer id_distributivo_roles;
         Integer ide_columna;
                 
         
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
         tab_consulta.ejecutarSql();
         ano = Integer.parseInt(tab_consulta.getValor("ano"));
         ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_columna"));
         id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
         ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;  
         mDescuento.migrarDescuento(ano,ide_periodo,id_distributivo_roles,ide_columna);
         }
                    
         public void borrar()
         {
         mDescuento.borrarDescuento();
         tab_tabla.actualizar();
         }
                                  
/*
 * CREACION DE REPORTES
 */
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
//        cal_fechaini.setFechaActual();
//        cal_fechafin.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "INCONSISTENCIA EN DESCUENTOS":
            aceptoDescuentos();
               break;
           case "DESCUENTOS ANIO Y PERIODO":
                dia_dialogoDes.Limpiar();
                grid_dt.getChildren().add(cmb_anio);
                grid_dt.getChildren().add(cmb_periodo);
                grid_dt.getChildren().add(cmb_descripcion);
                dia_dialogoDes.setDialogo(grid_dt);
                dia_dialogoDes.dibujar();
               break;
                
        }
    }     
       
  public void aceptoDescuentos(){
        switch (rep_reporte.getNombre()) {
               case "INCONSISTENCIA EN DESCUENTOS":
                    p_parametros.put("nom_resp", tab_usuario.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
               case "DESCUENTOS ANIO Y PERIODO":
//                   if (set_estado.getValorSeleccionado()!= null) {
//                      p_parametros = new HashMap();
//                      p_parametros.put("estado", Integer.parseInt(set_estado.getValorSeleccionado()+""));
//                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
//                      rep_reporte.cerrar();
//                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
//                      sef_formato.dibujar();
//                      }else {
//                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
//                        }
               break;
        }
    }


 
    
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
            guardarPantalla();

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
