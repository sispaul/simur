/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_bodega.ejb.bodt_egreso;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
public final class pre_bodt_entrega1 extends Pantalla{
    
    private Conexion con_postgres = new Conexion();
    private Tabla tab_cab_cons = new Tabla();
    private Tabla tab_det_egre = new Tabla();
    private Tabla tab_material = new Tabla();
    private Grid grid = new Grid();
    //Dialogos
    private Dialogo dia_dialogo= new Dialogo();
    //private ListaSeleccion lista_seleccio = new ListaSeleccion();
     @EJB
    private bodt_egreso egr_bodega = (bodt_egreso) utilitario.instanciarEJB(bodt_egreso.class);
    
    public pre_bodt_entrega1() {
         //Persistencia a la postgres.
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
                /**
         * PERSONA AÑO ACTUAL
         */
        TablaGenerica tab_consulta_ano=new TablaGenerica();
        tab_consulta_ano.setConexion(con_postgres);
        tab_consulta_ano.setSql("select ano_curso,ano_curso from conc_ano where actual='A'");
        tab_consulta_ano.ejecutarSql();
        
        grid.setColumns(4);
        tab_cab_cons.setId("tab_cab_cons");
        tab_cab_cons.setConexion(con_postgres);
        //tab_cab_cons.setTabla("bodt_concepto_egreso", "ide_egreso", 1);
        tab_cab_cons.setSql("select bodt_concepto_egreso.ide_egreso, solicita, fec_egreso,  visto_bueno, doc_egreso, recibe, numero_egreso, ide_destino, uso  from bodt_concepto_egreso, bodt_egreso where bodt_concepto_egreso.ide_egreso=bodt_egreso.ide_egreso and bodt_concepto_egreso.ide_egreso=-1");
        tab_cab_cons.setCampoPrimaria("ide_egreso");
        tab_cab_cons.setHeader("Requisicion de Materiales CONSUMO INTERNO");
        tab_cab_cons.setTipoFormulario(true);
        tab_cab_cons.getColumna("ide_egreso").setVisible(false);
        tab_cab_cons.getColumna("solicita").setNombreVisual("SOLICITA");
        tab_cab_cons.getColumna("solicita").setCombo("srh_empleado", "cod_empleado", "nombres", "");
        tab_cab_cons.getColumna("solicita").setPermitirNullCombo(false);
        tab_cab_cons.getColumna("recibe").setNombreVisual("RECIBE");
        tab_cab_cons.getColumna("recibe").setCombo("srh_empleado", "cod_empleado", "nombres", "");
        tab_cab_cons.getColumna("recibe").setPermitirNullCombo(false);
        tab_cab_cons.getColumna("visto_bueno").setNombreVisual("RESPONSABLE");
        tab_cab_cons.getColumna("visto_bueno").setCombo("srh_empleado", "cod_empleado", "nombres", "");
        tab_cab_cons.getColumna("visto_bueno").setPermitirNullCombo(false);
        tab_cab_cons.getColumna("ide_destino").setNombreVisual("DESTINO");
        tab_cab_cons.getColumna("ide_destino").setCombo("bodc_destinos", "id_destino", "descripcion", "");
        tab_cab_cons.getColumna("ide_destino").setPermitirNullCombo(false);
        tab_cab_cons.getColumna("fec_egreso").setNombreVisual("FECHA EGRESO");
        tab_cab_cons.getColumna("fec_egreso").setValorDefecto(utilitario.getFechaActual());
        tab_cab_cons.getColumna("doc_egreso").setNombreVisual("NUMERO DOCUMENTO");
        tab_cab_cons.getColumna("numero_egreso").setNombreVisual("NUMERO EGRESO");
        tab_cab_cons.getColumna("uso").setNombreVisual("USO");
        //tab_cab_cons.agregarRelacion(tab_det_egre); ///relación        
        tab_cab_cons.getGrid().setColumns(4);
        tab_cab_cons.dibujar();
        
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cab_cons);
        
        //Configurando el dialogo
//        dia_dialogo.setId("dia_dialogo");
//        dia_dialogo.setTitle("BODEGA - GRUPOS"); //titulo
//        dia_dialogo.setWidth("50%"); //siempre en porcentajes  ancho
//        dia_dialogo.setHeight("60%");//siempre porcentaje   alto
//        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
//        dia_dialogo.getBot_aceptar().setMetodo("aceptoDialogo");
//
//        tab_material.setId("tab_material");
//        tab_det_egre.setConexion(con_postgres);
//        //tab_det_egre.setTabla("bodt_egreso", "ide_bod_egreso", 2);
//        tab_material.setSql("select ide_bodt_articulo, bodc_material.ide_mat_bodega, (cod_material||' '||des_material) as material, ((existencia_inicial + ingreso_material) - egreso_material) as existencia, costo_actual, 0 as cantidad from bodc_material INNER JOIN bodt_articulos on (bodc_material.ide_mat_bodega=bodt_articulos.ide_mat_bodega)");
//        tab_material.getColumna("ide_bodt_articulo").setVisible(false);
//        tab_material.getColumna("material").setLectura(true);
//        tab_material.getColumna("material").setMetodoChange("buscaMaterial");
//        tab_material.getColumna("material").setNombreVisual("Materia");
//        tab_material.getColumna("material").setFiltro(true);
//        tab_material.getColumna("ide_mat_bodega").setLectura(true);
//        tab_material.getColumna("ide_mat_bodega").setNombreVisual("Codigo");
//        tab_material.getColumna("existencia").setLectura(true);
//        tab_material.getColumna("existencia").setNombreVisual("Existencia");
//        tab_material.getColumna("costo_actual").setLectura(true);
//        tab_material.getColumna("costo_actual").setNombreVisual("Valor U");
//        tab_material.getColumna("cantidad").setNombreVisual("Cantidad");
//               
//        dia_dialogo.setDialogo(tab_material);
//        tab_material.dibujar();
//        //dia_dialogo.dibujar();
//        agregarComponente(dia_dialogo);
        
        tab_det_egre.setId("tab_det_egre");
        tab_det_egre.setConexion(con_postgres);
        //tab_det_egre.setTabla("bodt_egreso", "ide_bod_egreso", 2);
        tab_det_egre.setSql("select ide_bodt_articulo, bodc_material.ide_mat_bodega, bodc_material.ide_mat_bodega as material, ((existencia_inicial + ingreso_material) - egreso_material) as existencia, costo_actual, existencia_inicial from bodc_material INNER JOIN bodt_articulos on (bodc_material.ide_mat_bodega='-1' and bodc_material.ide_mat_bodega=bodt_articulos.ide_mat_bodega)");
        tab_det_egre.getColumna("ide_bodt_articulo").setVisible(false);
        tab_det_egre.getColumna("material").setCombo("select a.ide_mat_bodega, cod_material, des_material, ((existencia_inicial + ingreso_material) - egreso_material) as existencia, costo_actual from bodt_articulos a, bodc_material b, conc_ano c where a.ide_mat_bodega = b.ide_mat_bodega and a.ano_curso = c.ano_curso and c.actual like 'A' and  ((existencia_inicial + ingreso_material) - egreso_material) > 0 order by des_material");
        tab_det_egre.getColumna("material").setAutoCompletar();
        tab_det_egre.getColumna("material").setMetodoChange("buscaMaterial");
        tab_det_egre.getColumna("material").setNombreVisual("Materia");
        tab_det_egre.getColumna("ide_mat_bodega").setLectura(true);
        tab_det_egre.getColumna("ide_mat_bodega").setNombreVisual("Codigo");
        tab_det_egre.getColumna("existencia").setLectura(true);
        tab_det_egre.getColumna("existencia").setNombreVisual("Existencia");
        tab_det_egre.getColumna("costo_actual").setLectura(true);
        tab_det_egre.getColumna("costo_actual").setNombreVisual("Valor U");
        tab_det_egre.getColumna("existencia_inicial").setNombreVisual("Cantidad");
        tab_det_egre.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_det_egre);


        Division div = new Division();
        div.dividir2(tabp, tabp1, "50%", "h");

        agregarComponente(div);
        tab_cab_cons.setFocus();
        insertar();
      
    }

    public void buscaMaterial(SelectEvent evt) {
        //aut_busca.onSelect(evt);
        if (tab_det_egre.getValor("material")!= null) {
            TablaGenerica tab_consulta = new TablaGenerica();
            tab_consulta.setConexion(con_postgres);
            tab_consulta.setSql("select ide_bodt_articulo, bodc_material.ide_mat_bodega, bodc_material.ide_mat_bodega as material, ((existencia_inicial + ingreso_material) - egreso_material) as existencia, costo_actual from bodc_material INNER JOIN bodt_articulos on (bodc_material.ide_mat_bodega='"+ tab_det_egre.getValor("material") +"' and bodc_material.ide_mat_bodega=bodt_articulos.ide_mat_bodega)");
            tab_consulta.ejecutarSql();
            tab_det_egre.setValor("ide_mat_bodega", tab_consulta.getValor("ide_mat_bodega"));
            tab_det_egre.setValor("existencia", tab_consulta.getValor("existencia"));
            tab_det_egre.setValor("costo_actual", tab_consulta.getValor("costo_actual"));
            //tab_establecimiento.setFilaActual(aut_busca.getValor());
        }else{
            tab_det_egre.setValor("existencia", "");
        }
        utilitario.addUpdate("tab_det_egre");
    }
    
    public Tabla getTab_cab_cons() {
        return tab_cab_cons;
    }

    public void setTab_cab_cons(Tabla tab_cab_cons) {
        this.tab_cab_cons = tab_cab_cons;
    }

    public Tabla getTab_det_egre() {
        return tab_det_egre;
    }

    public void setTab_det_egre(Tabla tab_det_egre) {
        this.tab_det_egre = tab_det_egre;
    }
    
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        tab_cab_cons.setValor("doc_egreso",egr_bodega.egresoMax());
        
//        if (tab_cab_cons.isFocus()){
//            tab_cab_cons.insertar();
//            tab_cab_cons.setValor("doc_egreso",egr_bodega.egresoMax());
//        }else{
//            dia_dialogo.dibujar();
//        }
    }

    public Tabla getTab_material() {
        return tab_material;
    }

    public void setTab_material(Tabla tab_material) {
        this.tab_material = tab_material;
    }

    @Override
    public void guardar() {
        
        //tab_cab_cons.guardar();
        //tab_det_egre.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
}
