/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_bodega.ejb.servicioBodega;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Paolo B.
 */
public class pre_recalcular extends Pantalla {

    private Conexion con_postgres = new Conexion();
    //Consulta Año en CUrso
    private Tabla tab_conAño = new Tabla();
    private Tabla tab_articulos = new Tabla();
    @EJB
    private servicioBodega ser_bodega = (servicioBodega) utilitario.instanciarEJB(servicioBodega.class);

    public pre_recalcular() {
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        bar_botones.limpiar(); /// deja en blanco la barra de botones

        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:250px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("RECALCULAR LOS MATERIALES");
        grid_pant.getChildren().add(eti_encab);

        Boton bot_recPA = new Boton();
        bot_recPA.setValue("Recalcular Valor Ponderado/Año");
        bot_recPA.setMetodo("recalculaMaterialV");
        grid_pant.getChildren().add(bot_recPA);

        Boton bot_recEA = new Boton();
        bot_recEA.setValue("Recalcular Existencias/Año");
        bot_recEA.setMetodo("recalculaMaterialE");
        grid_pant.getChildren().add(bot_recEA);

        agregarComponente(grid_pant);

        /**
         * ARTICULOS
         */
        tab_articulos.setId("tab_articulos");
        tab_articulos.setConexion(con_postgres);
        tab_articulos.setSql("select ide_bodt_articulo,ide_bodt_articulo from bodt_articulos");
        tab_articulos.setCampoPrimaria("ide_bodt_articulo");
        tab_articulos.setLectura(true);
        tab_articulos.dibujar();

        /**
         * AÑO EN CURSO
         */
        tab_conAño.setId("tab_conAño");
        tab_conAño.setConexion(con_postgres);
        tab_conAño.setSql("select ano_curso,ano_curso from conc_ano where actual='A' order by ano_curso desc");
        tab_conAño.setCampoPrimaria("ano_curso");
        tab_conAño.setLectura(true);
        tab_conAño.dibujar();
    }

    public void recalculaMaterialV() {
        for (int i = 0; i < tab_articulos.getTotalFilas(); i++) {
            ser_bodega.recalcularV(tab_articulos.getValor(i, "ide_bodt_articulo"), tab_conAño.getValor("ano_curso"));
        }
        utilitario.agregarMensaje("se recalcularon los Valores de", tab_articulos.getTotalFilas() + " articulos");
    }

    public void recalculaMaterialE() {

        for (int i = 0; i < tab_articulos.getTotalFilas(); i++) {
            ser_bodega.recalcularE(tab_articulos.getValor(i, "ide_bodt_articulo"), tab_conAño.getValor("ano_curso"));
//            System.out.println(i);
        }
        utilitario.agregarMensaje("se recalcularon las Existencias de", tab_articulos.getTotalFilas() + " articulos");

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
}
