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
import paq_bodega.egb.servicioBodega;
import paq_pruebas.ejb.servicioPruebas;
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
        Integer ano = Integer.parseInt(tab_conAño.getValor("ano_curso"));
        Integer material;
        tab_articulos.inicio();
        int i;
        for (i = 0; i < tab_articulos.getTotalFilas(); i++) {
            material = Integer.parseInt(tab_articulos.getValor("ide_bodt_articulo"));
            String consulta = "update bodt_egreso"
                    + " set costo_egreso= valor_calculado,"
                    + " total_egreso= valor_calculado*cant_egreso "
                    + " from ("
                    + "  select ide_bod_egreso,"
                    + " (existencia_inicial + (case when tot_ingreso is null then 0 else tot_ingreso end))-"
                    + " (case when tot_egreso is null then 0 else tot_egreso end) as existencia"
                    + " ,costo_anterioart,cantidad_i,valor,"
                    + " ("
                    + " ("
                    + " ("
                    + " (existencia_inicial + (case when tot_ingreso is null then 0 else tot_ingreso end))-"
                    + " (case when tot_egreso is null then 0 else tot_egreso end) "
                    + " ) * costo_anterioart"
                    + " ) +"
                    + " ("
                    + " cantidad_i*valor"
                    + " )"
                    + " ) /"
                    + " ("
                    + " ("
                    + " (existencia_inicial + (case when tot_ingreso is null then 0 else tot_ingreso end))-"
                    + " (case when tot_egreso is null then 0 else tot_egreso end) "
                    + " )+ cantidad_i"
                    + " ) as valor_calculado"
                    + " from ("
                    + " select ide_bodt_articulo,(select sum(egreso) from ("
                    + " select a.ide_bodt_articulo,sum(cant_egreso) as egreso,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial"
                    + " from bodt_egreso a"
                    + " left join bodt_bodega b on a.ide_bodt_articulo= b.ide_bodt_articulo and fecha_ingreso = fecha_ingresoar"
                    + " left join bodt_articulos c on a.ide_bodt_articulo= c.ide_bodt_articulo"
                    + " where c.ano_curso=" + ano + " and a.ide_bodt_articulo=" + material
                    + " group by a.ide_bodt_articulo,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial"
                    + " ) x where a.fecha_ingresoar > x.fecha_ingresoar) as tot_egreso,"
                    + " (select sum(egreso) from ("
                    + " select c.ide_bodt_articulo,sum(cantidad_i) as egreso,fecha_ingreso"
                    + " from bodt_bodega b,bodt_articulos c"
                    + " where c.ide_bodt_articulo= b.ide_bodt_articulo"
                    + " and c.ano_curso=" + ano + " and c.ide_bodt_articulo=" + material
                    + " group by c.ide_bodt_articulo,fecha_ingreso"
                    + " ) x where a.fecha_ingresoar > x.fecha_ingreso) as tot_ingreso,"
                    + " fecha_ingresoar,costo_anterioart,valor,existencia_inicial,cantidad_i,ide_bod_egreso,costo_egreso"
                    + " from ("
                    + " select a.ide_bodt_articulo,sum(cant_egreso) as egreso,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial,"
                    + " ide_bod_egreso"
                    + " from bodt_egreso a,bodt_bodega b,bodt_articulos c"
                    + " where a.ide_bodt_articulo= b.ide_bodt_articulo"
                    + " and fecha_ingreso = fecha_ingresoar"
                    + " and a.ide_bodt_articulo= c.ide_bodt_articulo"
                    + " and c.ano_curso=" + ano + " and a.ide_bodt_articulo=" + material
                    + " group by a.ide_bodt_articulo,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial,ide_bod_egreso"
                    + " ) a"
                    + " ) a"
                    + " ) a"
                    + " where a.ide_bod_egreso= bodt_egreso.ide_bod_egreso";

//      Ejecutar SQL 
            con_postgres.ejecutarSql(consulta);
//      Registro siguiente
            tab_articulos.siguiente();
        }
        utilitario.agregarMensaje("Valor", tab_articulos.getTotalFilas() + "");
        utilitario.agregarMensaje("Valor", i + "");
    }

    public void recalculaMaterialE() {

        for (int i = 0; i < tab_articulos.getTotalFilas(); i++) {
            ser_bodega.recalcular(tab_articulos.getValor(i, "ide_bodt_articulo"), tab_conAño.getValor(i, "ano_curso"));
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
