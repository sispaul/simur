/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 // */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo diaDialogosr = new Dialogo();
    private Grid gridr = new Grid();
    private Combo comboDecimo = new Combo();
    private Combo comboDistributivo = new Combo();

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
        botBuscar.setValue("Filtar Listado");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("actuliLista");
        bar_botones.agregarBoton(botBuscar);
        bar_botones.agregarSeparador();

        tabAcumulacion.setId("tabAcumulacion");
        tabAcumulacion.setConexion(conPostgres);
        tabAcumulacion.setTabla("srh_autorizacion_acumulacion", "autoriza_id", 1);
        tabAcumulacion.getColumna("autoriza_empleado").setFiltro(true);
        List li = new ArrayList();
        Object fi1[] = {
            "1", "SI"
        };
        Object fi2[] = {
            "0", "NO"
        };
        li.add(fi1);;
        li.add(fi2);;
        tabAcumulacion.getColumna("autoriza_decimo_cuarto").setRadio(li, "1");
        List lis = new ArrayList();
        Object fil1[] = {
            "1", "SI"
        };
        Object fil2[] = {
            "0", "NO"
        };
        lis.add(fil1);;
        lis.add(fil2);;
        tabAcumulacion.getColumna("autoriza_decimo_tercero").setRadio(lis, "1");
        List liss = new ArrayList();
        Object fisl1[] = {
            "1", "SI"
        };
        liss.add(fisl1);;
        tabAcumulacion.getColumna("autoriza_quitar").setRadio(liss, "1");
        tabAcumulacion.getColumna("autoriza_login_ingreso").setVisible(false);
        tabAcumulacion.getColumna("autoriza_fecha_creacion").setVisible(false);
        tabAcumulacion.setRows(20);
        tabAcumulacion.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabAcumulacion);
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTADO PARA PAGOS DE SOBRESUELDOS / FONDOS DE RESERVA PARA LOS SERVIDORES DE GADMUR");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);

        //para reportes
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        diaDialogosr.setId("diaDialogosr");
        diaDialogosr.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosr.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosr.setHeight("20%");//siempre porcentaje   alto
        diaDialogosr.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosr.getBot_aceptar().setMetodo("dibujarReporte");
        gridr.setColumns(4);
        agregarComponente(diaDialogosr);

        comboDecimo.setId("comboDecimo");
        comboDecimo.setConexion(conPostgres);
        comboDecimo.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S'");

        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        actuli();
    }

    public void listado() {
        TablaGenerica tabDato = mDescuento.getNumeroFilas();
        if (!tabDato.isEmpty()) {
            for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                TablaGenerica tabDatos = mDescuento.getNumFilas(tabDato.getValor(i, "cod_empleado"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
                if (!tabDatos.isEmpty()) {
                } else {
                    mDescuento.setDatosServidor(utilitario.getVariable("NICK"), tabDato.getValor(i, "cod_empleado"));
                }
            }
        }
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
            str_filtros = "autoriza_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'";

        } else {
            utilitario.agregarMensajeInfo("Información No Disponible  \n" + utilitario.getAnio(utilitario.getFechaActual()) + " ",
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
            str_filtros = "autoriza_id_distributivo ='" + String.valueOf(comboServidor.getValue()) + "'";

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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTADO DECIMOS":
                diaDialogosr.Limpiar();
                Grid griBusca = new Grid();
                griBusca.setColumns(2);
                griBusca.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBusca.getChildren().add(comboDistributivo);
                griBusca.getChildren().add(new Etiqueta("DECIMO :"));
                griBusca.getChildren().add(comboDecimo);
                gridr.getChildren().add(griBusca);
                diaDialogosr.setDialogo(gridr);
                diaDialogosr.dibujar();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTADO DECIMOS":
                TablaGenerica tabDatos = mDescuento.getPeriodos(comboDecimo.getValue() + "");
                if (!tabDatos.isEmpty()) {
                    String columna = "";
                    if (comboDistributivo.getValue().equals("1")) {
                        if (comboDecimo.getValue().equals("D3")) {
                            columna = "2";
                        } else if (comboDecimo.getValue().equals("D4")) {
                            columna = "1";
                        }
                    } else if (comboDistributivo.getValue().equals("2")) {
                        if (comboDecimo.getValue().equals("D3")) {
                            columna = "2";
                        } else if (comboDecimo.getValue().equals("D4")) {
                            columna = "1";
                        }
                    }
                    p_parametros.put("descripcion", tabDatos.getValor("periodo_columna") + "");
                    p_parametros.put("anio", utilitario.getAnio(tabDatos.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("distributivo", comboDistributivo.getValue() + "");
                    p_parametros.put("tipo", Integer.parseInt(columna + ""));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
        }
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
