/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_bodega.ejb.bodt_egreso;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
public class pre_bodt_entrega1 extends Pantalla{
    
    private Conexion con_postgres = new Conexion();
    private Tabla tab_cab_cons = new Tabla();
    private Tabla tab_det_egre = new Tabla();
    private Grid grid = new Grid();
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
        tab_cab_cons.getColumna("ide_destino").setNombreVisual("DESTINO-0");
        tab_cab_cons.getColumna("ide_destino").setCombo("bodc_destinos", "id_destino", "descripcion", "");
        tab_cab_cons.getColumna("ide_destino").setPermitirNullCombo(false);
        tab_cab_cons.getColumna("fec_egreso").setNombreVisual("FECHA EGRESO");
        tab_cab_cons.getColumna("fec_egreso").setValorDefecto(utilitario.getFechaActual());
        tab_cab_cons.getColumna("doc_egreso").setNombreVisual("NUMERO DOCUMENTO");

        System.out.println(egr_bodega.egresoMax());
        //tab_cab_cons.getColumna("doc_egreso").setValorDefecto(egr_bodega.egresoMax());
        tab_cab_cons.getColumna("numero_egreso").setNombreVisual("NUMERO EGRESO");
        tab_cab_cons.getColumna("uso").setNombreVisual("USO");
        tab_cab_cons.agregarRelacion(tab_det_egre); ///relación        
        tab_cab_cons.getGrid().setColumns(4);
        //tab_cab_cons.getGrid().setDir("h");
        tab_cab_cons.dibujar();
        
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_cab_cons);

        tab_det_egre.setId("tab_det_egre");
        tab_det_egre.setConexion(con_postgres);
        tab_det_egre.setTabla("bodt_egreso", "ide_bod_egreso", 2);
        tab_det_egre.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_det_egre);


        Division div = new Division();
        div.dividir2(tabp, tabp1, "60%", "h");

        agregarComponente(div);
        tab_cab_cons.setFocus();
        utilitario.getTablaisFocus().insertar();
        
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
    }

    @Override
    public void guardar() {
        tab_cab_cons.guardar();
        tab_det_egre.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
}
