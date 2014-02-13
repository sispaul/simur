/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author Paolo
 */
public class pre_material extends Pantalla {
    private Conexion con_postgres= new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    
    public pre_material() {
        //Persistencia a la postgres.
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        
        List lista = new ArrayList();
        Object fila1[] = {
            "C", "COMPRA"
        };
        Object fila2[] = {
            "B", "BODEGA"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        
        List lst_tipo = new ArrayList();
        Object lsttipo1[] = {
            "A", "ACTIVO"
        };
        Object lsttipo2[] = {
            "M", "MANO DE OBRA"
        };
        lst_tipo.add(lsttipo1);;
        lst_tipo.add(lsttipo2);;
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setTabla("bodc_material", "ide_mat_bodega", 1);
        tab_tabla.getColumna("id_grupo").setCombo("select id_grupo, descripcion from bodc_grupo order by descripcion");
        tab_tabla.getColumna("id_grupo").setPermitirNullCombo(false);
        tab_tabla.getColumna("ide_medida").setCombo("select ide_medida, des_medida from valc_medida order by des_medida");
        tab_tabla.getColumna("ide_medida").setPermitirNullCombo(false);
        tab_tabla.getColumna("ide_medida").setValorDefecto("1");
        tab_tabla.getColumna("tipobodega").setCombo(lista);
        tab_tabla.getColumna("tipobodega").setPermitirNullCombo(false);
        tab_tabla.getColumna("tipobodega").setValorDefecto("B");
        tab_tabla.getColumna("tipo").setCombo(lst_tipo);
        tab_tabla.getColumna("tipo").setPermitirNullCombo(false);
        tab_tabla.getColumna("ide_mat_bodega").setNombreVisual("Codigo");
        tab_tabla.getColumna("des_material").setNombreVisual("Nombre");
        tab_tabla.getColumna("ide_medida").setNombreVisual("Medida");
        tab_tabla.getColumna("cod_material").setNombreVisual("Cuenta");
        tab_tabla.getColumna("cod_material").getMetodoChange();
        tab_tabla.getColumna("cod_material").setMascara("?99.999.999");
        tab_tabla.getColumna("id_grupo").setNombreVisual("Grupo");
        tab_tabla.getColumna("tipo").setNombreVisual("Tipo");
        
        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(con_postgres);
        tab_tabla2.setSql("select ide_mat_bodega, des_material, ide_medida, cod_material, id_grupo, tipo from bodc_material order by des_material");
        tab_tabla2.setCampoPrimaria("ide_mat_bodega");
        tab_tabla2.setNumeroTabla(2);
        tab_tabla2.getColumna("id_grupo").setCombo("select id_grupo, descripcion from bodc_grupo");
        tab_tabla2.getColumna("ide_medida").setCombo("select ide_medida, des_medida from valc_medida");
        tab_tabla2.getColumna("tipo").setCombo(lst_tipo);
        tab_tabla2.getColumna("ide_mat_bodega").setNombreVisual("Codigo");
        tab_tabla2.getColumna("ide_mat_bodega").setFiltro(true);
        tab_tabla2.getColumna("des_material").setNombreVisual("Nombre");
        tab_tabla2.getColumna("des_material").setFiltro(true);
        tab_tabla2.getColumna("des_material").setLongitud(300);
        tab_tabla2.getColumna("ide_medida").setNombreVisual("Medida");
        tab_tabla2.getColumna("cod_material").setNombreVisual("Cuenta");
        tab_tabla2.getColumna("cod_material").setFiltro(true);
        tab_tabla2.getColumna("id_grupo").setNombreVisual("Grupo");
        tab_tabla2.getColumna("id_grupo").setFiltro(true);
        tab_tabla2.getColumna("id_grupo").setLongitud(400);
        tab_tabla2.getColumna("tipo").setNombreVisual("Tipo");
        tab_tabla2.getColumna("tipo").setLongitud(100);
        
        tab_tabla2.setLectura(true);
        tab_tabla2.setRows(15);
   
        tab_tabla2.agregarRelacion(tab_tabla);
        tab_tabla2.dibujar();
        tab_tabla2.setFilaActual(0);
        
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_tabla);


        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tabla2);


        Division div = new Division();
        div.dividir2(tabp, tabp1, "40%", "h");

        agregarComponente(div);
        
        //agregarComponente(pat_panel);
        
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }


    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if(ValidaCodigo()){
        String reg = new String();
        tab_tabla.guardar();
        con_postgres.guardarPantalla();
        reg=tab_tabla.getValorSeleccionado();
        tab_tabla2.actualizar();
        tab_tabla2.setFilaActual(reg);
        tab_tabla2.calcularPaginaActual();
        }
        }

    private boolean ValidaCodigo() {
       Pattern pat = Pattern.compile("[0-9.]+");
        Matcher mat = pat.matcher(tab_tabla.getValor("cod_material")+"");
        
        if (mat.matches()) {
            return true;
        } else {
            utilitario.agregarMensajeError("Codigo", "El codigo no es valido");
            return false;
        }
    }
            
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
    
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}