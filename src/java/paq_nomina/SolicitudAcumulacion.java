/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 // */
package paq_nomina;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_nomina.ejb.mergeDescuento;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class SolicitudAcumulacion extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabAcumulacion = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private Combo comboServidor = new Combo();
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public SolicitudAcumulacion() {

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Boton botInserta = new Boton();
        botInserta.setValue("Cargar Listado");
        botInserta.setExcluirLectura(true);
        botInserta.setIcon("ui-icon-contact");
        botInserta.setMetodo("listado");
        bar_botones.agregarBoton(botInserta);
        bar_botones.agregarSeparador();

        comboServidor.setId("comboServidor");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Empleados"
        };
        Object fila2[] = {
            "2", "Trabajadores"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        comboServidor.setCombo(lista);
        bar_botones.agregarComponente(new Etiqueta("Tipo : "));
        bar_botones.agregarComponente(comboServidor);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Cargar Listado");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("actuliLista");
        bar_botones.agregarBoton(botBuscar);
        bar_botones.agregarSeparador();

        tabAcumulacion.setId("tabAcumulacion");
        tabAcumulacion.setConexion(conPostgres);
        tabAcumulacion.setTabla("srh_autorizacion_acumulacion", "autoriza_id", 1);
        List li = new ArrayList();
        Object fi1[] = {
            "1", "SI"
        };
        Object fi2[] = {
            "2", "NO"
        };
        li.add(fi1);;
        li.add(fi2);;
        tabAcumulacion.getColumna("autoriza_decimo_cuarto").setRadio(li, "1");
        List lis = new ArrayList();
        Object fil1[] = {
            "1", "SI"
        };
        Object fil2[] = {
            "2", "NO"
        };
        lis.add(fil1);;
        lis.add(fil2);;
        tabAcumulacion.getColumna("autoriza_decimo_tercero").setRadio(lis, "1");
        List list = new ArrayList();
        Object fils1[] = {
            "1", "SI"
        };
        Object fils2[] = {
            "2", "NO"
        };
        Object fils3[] = {
            "3", "NO PAGO"
        };
        list.add(fils1);;
        list.add(fils2);;
        list.add(fils3);;
        tabAcumulacion.getColumna("autoriza_fondos_reserva").setRadio(list, "1");
        tabAcumulacion.getColumna("autoriza_login_ingreso").setVisible(false);
        tabAcumulacion.getColumna("autoriza_fecha_ingreso").setVisible(false);
        tabAcumulacion.setRows(20);
        tabAcumulacion.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabAcumulacion);
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTADO PARA PAGOS DE SOBRESUELDOS / FONDOS DE RESERVA PARA LOS SERVIDORES DE GADMUR");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);

        actuli();
    }

    public void listado() {
        mDescuento.setDatosServidor(utilitario.getVariable("NICK"));
        tabAcumulacion.actualizar();
    }

    public void actuli() {
        if (!getFiltroAcceso().isEmpty()) {
            tabAcumulacion.setCondicion(getFiltroAcceso());
            tabAcumulacion.ejecutarSql();
            utilitario.addUpdate("tabAcumulacion");
        }
    }

    private String getFiltroAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabAcumulacion.getValorSeleccionado() != null) {
            str_filtros = "autoriza_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual()))+"'";

        } else {
            utilitario.agregarMensajeInfo("Información No Disponible  \n"+utilitario.getAnio(utilitario.getFechaActual())+" ",
                    "");
        }
        return str_filtros;
    }

    public void actuliLista() {
        if (!getFiltro().isEmpty()) {
            tabAcumulacion.setCondicion(getFiltro());
            tabAcumulacion.ejecutarSql();
            utilitario.addUpdate("tabAcumulacion");
        }
    }

    private String getFiltro() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabAcumulacion.getValorSeleccionado() != null) {
            str_filtros = "autoriza_id_distributivo ='" + String.valueOf(comboServidor.getValue())+"'";

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "");
        }
        return str_filtros;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        if (tabAcumulacion.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabAcumulacion() {
        return tabAcumulacion;
    }

    public void setTabAcumulacion(Tabla tabAcumulacion) {
        this.tabAcumulacion = tabAcumulacion;
    }
}
