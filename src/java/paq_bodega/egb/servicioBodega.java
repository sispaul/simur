/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega.egb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author Administrador
 */
@Stateless
public class servicioBodega {

    private Utilitario utilitario = new Utilitario();
    private Conexion con_postgres = new Conexion();

    public void recalcular(String ide_bodt_articulo, String anio) {

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
                + " where c.ano_curso=" + anio + " and a.ide_bodt_articulo=" + ide_bodt_articulo
                + " group by a.ide_bodt_articulo,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial"
                + " ) x where a.fecha_ingresoar > x.fecha_ingresoar) as tot_egreso,"
                + " (select sum(egreso) from ("
                + " select c.ide_bodt_articulo,sum(cantidad_i) as egreso,fecha_ingreso"
                + " from bodt_bodega b,bodt_articulos c"
                + " where c.ide_bodt_articulo= b.ide_bodt_articulo"
                + " and c.ano_curso=" + anio + " and c.ide_bodt_articulo=" + ide_bodt_articulo
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
                + " and c.ano_curso=" + anio + " and a.ide_bodt_articulo=" + ide_bodt_articulo
                + " group by a.ide_bodt_articulo,costo_egreso,costo_anterioart,fecha_ingresoar,valor,cantidad_i,existencia_inicial,ide_bod_egreso"
                + " ) a"
                + " ) a"
                + " ) a"
                + " where a.ide_bod_egreso= bodt_egreso.ide_bod_egreso";


        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        TablaGenerica tab_ejecuta=new TablaGenerica();
        tab_ejecuta.setConexion(con_postgres);
        tab_ejecuta.setSql(consulta);
        tab_ejecuta.ejecutarSql();

    }

    public void productoRecalcular(Integer material, Integer ano) {
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        ///toda la programacion

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
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
