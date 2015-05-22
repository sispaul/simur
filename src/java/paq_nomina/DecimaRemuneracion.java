/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.mergeDescuento;
import static paq_nomina.pre_calculo_decimo_cuarto.calcularDias;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class DecimaRemuneracion extends Pantalla {

    private Tabla tabDecimos = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Panel panOpcion = new Panel();
    private Combo comboDistributivo = new Combo();
    private Combo comboAcciones = new Combo();
    private Combo comboEmpleados = new Combo();
    private Combo comboEmpleados1 = new Combo();
    private Combo comboAnio = new Combo();
    private Combo comboPeriodo = new Combo();
    private Combo comboAccion = new Combo();
    private Combo comboDecimo = new Combo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo diaDialogosr = new Dialogo();
    private Grid gridr = new Grid();
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public DecimaRemuneracion() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        comboAcciones.setId("comboAcciones");
        List list = new ArrayList();
        Object filas1[] = {
            "1", "INFORMACIÓN"
        };
        Object filas2[] = {
            "2", "MIGRAR A ROL"
        };
        Object filas3[] = {
            "3", "LIMPIAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        list.add(filas3);;
        comboAcciones.setCombo(list);
        bar_botones.agregarComponente(new Etiqueta("Acción : "));
        bar_botones.agregarComponente(comboAcciones);

        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("select periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S'");
        bar_botones.agregarComponente(new Etiqueta("Remuneración : "));
        bar_botones.agregarComponente(comboDistributivo);

        Boton bot = new Boton();
        bot.setValue("Ejecutar");
        bot.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot.setMetodo("cargarInfo");
        bar_botones.agregarBoton(bot);
        bar_botones.agregarSeparador();

        tabDecimos.setId("tabDecimos");
        tabDecimos.setConexion(conPostgres);
        tabDecimos.setTabla("srh_decimo_cuarto_tercero", "decimo_id", 1);
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "0", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabDecimos.getColumna("decimo_estado").setRadio(lista, "1");
        tabDecimos.getColumna("decimo_cod_empleado").setLectura(true);
        tabDecimos.getColumna("decimo_id_distributivo").setVisible(false);
        tabDecimos.getColumna("decimo_empleado").setFiltro(true);
        tabDecimos.setRows(20);
        tabDecimos.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDecimos);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTADO DE CALCULO DE DECIMOS ACUMULADOS/PAGO MENSUAL");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);


        diaDialogosr.setId("diaDialogosr");
        diaDialogosr.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosr.setWidth("35%"); //siempre en porcentajes  ancho
        diaDialogosr.setHeight("35%");//siempre porcentaje   alto
        diaDialogosr.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosr.getBot_aceptar().setMetodo("aceptoDecimo");
        gridr.setColumns(4);
        agregarComponente(diaDialogosr);

        comboEmpleados.setId("comboEmpleados");
        comboEmpleados.setConexion(conPostgres);
        comboEmpleados.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados1.setId("comboEmpleados1");
        comboEmpleados1.setConexion(conPostgres);
        comboEmpleados1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        bar_botones.agregarComponente(comboEmpleados1);

        Boton botb = new Boton();
        botb.setValue("Filtra");
        botb.setIcon("ui-icon-search"); //pone icono de jquery temeroller
        botb.setMetodo("filtarLista");
        bar_botones.agregarBoton(botb);
        bar_botones.agregarSeparador();

        comboAnio.setId("comboAnio");
        comboAnio.setConexion(conPostgres);
        comboAnio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");

        comboPeriodo.setId("comboPeriodo");
        comboPeriodo.setConexion(conPostgres);
        comboPeriodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");

        comboAccion.setId("comboAccion");
        List lis = new ArrayList();
        Object fi1[] = {
            "1", "Acumula"
        };
        Object fi2[] = {
            "0", "Pago Mes"
        };
        lis.add(fi1);;
        lis.add(fi2);;
        comboAccion.setCombo(lis);

        comboDecimo.setId("comboDecimo");
        List liss = new ArrayList();
        Object fis1[] = {
            "1", "Decimo Tercero"
        };
        Object fis2[] = {
            "2", "Decimo Cuarto"
        };
        liss.add(fis1);;
        liss.add(fis2);;
        comboDecimo.setCombo(liss);
        actuliLista();
    }

    public void cargarInfo() {
        if (comboAcciones.getValue().equals("1")) {//Información que Subira
            if (comboDistributivo.getValue().equals("D3")) {
                TablaGenerica tabDato3 = mDescuento.getCalculoD3T(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getAnio(utilitario.getFechaActual()));
                if (!tabDato3.isEmpty()) {
                    for (int i = 0; i < tabDato3.getTotalFilas(); i++) {

                        if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato3.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato3.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato3.getValor(i, "autoriza_fecha_ingreso"))),
                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                            BigDecimal bd;
                            double val = 0.0;
                            bd = new BigDecimal((Double.valueOf(tabDato3.getValor(i, "hxe"))
                                    + Double.valueOf(tabDato3.getValor(i, "sbr"))
                                    + Double.valueOf(tabDato3.getValor(i, "remuneracion"))) / 12);
                            val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            if (tabDato3.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                mDescuento.setDatosCalculo(tabDato3.getValor(i, "autoriza_cod_empleado"), tabDato3.getValor(i, "autoriza_empleado"), "15", val, tabDato3.getValor(i, "autoriza_decimo_tercero"), Double.valueOf(tabDato3.getValor(i, "remuneracion")), Double.valueOf(tabDato3.getValor(i, "hxe")), Double.valueOf(tabDato3.getValor(i, "sbr")), tabDato3.getValor(i, "autoriza_id_distributivo"));
                            } else if (tabDato3.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                mDescuento.setDatosCalculo(tabDato3.getValor(i, "autoriza_cod_empleado"), tabDato3.getValor(i, "autoriza_empleado"), "42", val, tabDato3.getValor(i, "autoriza_decimo_tercero"), Double.valueOf(tabDato3.getValor(i, "remuneracion")), Double.valueOf(tabDato3.getValor(i, "hxe")), Double.valueOf(tabDato3.getValor(i, "sbr")), tabDato3.getValor(i, "autoriza_id_distributivo"));
                            }
                        } else {
                            Integer valor = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato3.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato3.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato3.getValor(i, "autoriza_fecha_ingreso"))),
                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                            BigDecimal bd;
                            double val = 0.0;
                            bd = new BigDecimal(((Double.valueOf(tabDato3.getValor(i, "hxe"))
                                    + Double.valueOf(tabDato3.getValor(i, "sbr"))
                                    + Double.valueOf(tabDato3.getValor(i, "remuneracion"))) / 360) * valor);
                            val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            if (tabDato3.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                mDescuento.setDatosCalculo(tabDato3.getValor(i, "autoriza_cod_empleado"), tabDato3.getValor(i, "autoriza_empleado"), "15", val, tabDato3.getValor(i, "autoriza_decimo_tercero"), Double.valueOf(tabDato3.getValor(i, "remuneracion")), Double.valueOf(tabDato3.getValor(i, "hxe")), Double.valueOf(tabDato3.getValor(i, "sbr")), tabDato3.getValor(i, "autoriza_id_distributivo"));
                            } else if (tabDato3.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                mDescuento.setDatosCalculo(tabDato3.getValor(i, "autoriza_cod_empleado"), tabDato3.getValor(i, "autoriza_empleado"), "42", val, tabDato3.getValor(i, "autoriza_decimo_tercero"), Double.valueOf(tabDato3.getValor(i, "remuneracion")), Double.valueOf(tabDato3.getValor(i, "hxe")), Double.valueOf(tabDato3.getValor(i, "sbr")), tabDato3.getValor(i, "autoriza_id_distributivo"));
                            }
                        }
                    }
                }
                tabDecimos.actualizar();
            } else if (comboDistributivo.getValue().equals("D4")) {
                TablaGenerica tabDato4 = mDescuento.getCalculoD4T();
                if (!tabDato4.isEmpty()) {
                    for (int i = 0; i < tabDato4.getTotalFilas(); i++) {

                        if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato4.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato4.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato4.getValor(i, "autoriza_fecha_ingreso"))),
                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                            BigDecimal bd;
                            double val = 0.0;
                            bd = new BigDecimal(Double.valueOf(tabDato4.getValor(i, "valor")) / 12);
                            val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            if (tabDato4.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                mDescuento.setDatosCalculo(tabDato4.getValor(i, "autoriza_cod_empleado"), tabDato4.getValor(i, "autoriza_empleado"), "16", val, tabDato4.getValor(i, "autoriza_decimo_cuarto"), Double.valueOf(tabDato4.getValor(i, "remuneracion")), null, null, tabDato4.getValor(i, "autoriza_id_distributivo"));
                            } else if (tabDato4.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                mDescuento.setDatosCalculo(tabDato4.getValor(i, "autoriza_cod_empleado"), tabDato4.getValor(i, "autoriza_empleado"), "43", val, tabDato4.getValor(i, "autoriza_decimo_cuarto"), Double.valueOf(tabDato4.getValor(i, "remuneracion")), null, null, tabDato4.getValor(i, "autoriza_id_distributivo"));
                            }
                        } else {
                            Integer valor = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato4.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato4.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato4.getValor(i, "autoriza_fecha_ingreso"))),
                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                            BigDecimal bd;
                            double val = 0.0;
                            bd = new BigDecimal((Double.valueOf(tabDato4.getValor(i, "valor")) / 360) * valor);
                            val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                            if (tabDato4.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                mDescuento.setDatosCalculo(tabDato4.getValor(i, "autoriza_cod_empleado"), tabDato4.getValor(i, "autoriza_empleado"), "16", val, tabDato4.getValor(i, "autoriza_decimo_cuarto"), Double.valueOf(tabDato4.getValor(i, "remuneracion")), null, null, tabDato4.getValor(i, "autoriza_id_distributivo"));
                            } else if (tabDato4.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                mDescuento.setDatosCalculo(tabDato4.getValor(i, "autoriza_cod_empleado"), tabDato4.getValor(i, "autoriza_empleado"), "43", val, tabDato4.getValor(i, "autoriza_decimo_cuarto"), Double.valueOf(tabDato4.getValor(i, "remuneracion")), null, null, tabDato4.getValor(i, "autoriza_id_distributivo"));
                            }
                        }
                    }
                }
                tabDecimos.actualizar();
            }
        } else if (comboAcciones.getValue().equals("2")) {//Subida a Roles
            setMigraRoles();
        } else if (comboAcciones.getValue().equals("3")) {//Limpiar Datos
        } else {
            utilitario.agregarMensaje("Debe escoger una Acción a realizar", "");
        }
    }

    public void setMigraRoles() {
        TablaGenerica tabDato = mDescuento.VerificarRol();
        if (!tabDato.isEmpty()) {
            for (int i = 0; i < tabDecimos.getTotalFilas(); i++) {
                if (tabDecimos.getValor(i, "decimo_estado").equals("0")) {
                    TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabDecimos.getValor(i, "decimo_anio"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")),
                            tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")));
                    if (!tabDatos.isEmpty()) {
                        mDescuento.setmigrarDescuento(tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabDecimos.getValor(i, "decimo_anio")), Double.valueOf(tabDecimos.getValor(i, "decimo_valor")));
                        utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                    } else {
                        utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabDecimos.getValor(i, "decimo_empleado"));
                    }
                }
            }
        } else {
            utilitario.agregarNotificacionInfo("Descuento No Puede Ser Subido a Rol", "Periodo de Rol Aun No Esta Creado");
        }
    }

    public static int calcularDias(Calendar cal1, Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60; // calcular la diferencia en horas a
        long diffDays = diffHours / 24; // calcular la diferencia en dias
//        long diffWeek = diffDays / 7; // calcular la diferencia en semanas
//        long diffMounth = diffWeek / 4; // calcular la diferencia en meses
        return Integer.parseInt(String.valueOf(diffDays));
    }

    public void actuliLista() {
        if (!getFiltro().isEmpty()) {
            tabDecimos.setCondicion(getFiltro());
            tabDecimos.ejecutarSql();
            utilitario.addUpdate("tabDecimos");
        }
    }

    private String getFiltro() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabDecimos.getValorSeleccionado() != null) {
            str_filtros = "decimo_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'"
                    + "and decimo_periodo = '" + String.valueOf(utilitario.getMes(utilitario.getFechaActual())) + "'";

        } else {
            utilitario.agregarMensajeInfo("No ahi informacion disponible",
                    "");
        }
        return str_filtros;
    }

    public void filtarLista() {
        if (!getFiltroLista().isEmpty()) {
            tabDecimos.setCondicion(getFiltroLista());
            tabDecimos.ejecutarSql();
            utilitario.addUpdate("tabDecimos");
        }
    }

    private String getFiltroLista() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "",valor ="";
        if (tabDecimos.getValorSeleccionado() != null) {
            if (comboDecimo.getValue().equals("1") && comboEmpleados1.getValue().equals("1")) {
                valor ="15";
            } else if (comboDecimo.getValue().equals("2") && comboEmpleados1.getValue().equals("1")) {
                valor = "16";
            } else if (comboDecimo.getValue().equals("1") && comboEmpleados1.getValue().equals("2")) {
                valor = "42";
            } else if (comboDecimo.getValue().equals("2") && comboEmpleados1.getValue().equals("2")) {
               valor ="43";
            }
            str_filtros = "decimo_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'"
                    + "and decimo_periodo = '" + String.valueOf(utilitario.getMes(utilitario.getFechaActual())) + "' and "
                    + "decimo_id_distributivo = '" + comboEmpleados1.getValue() + "'"
                    + "and decimo_columna = '"+valor+"'";

        } else {
            utilitario.agregarMensajeInfo("No ahi informacion disponible",
                    "");
        }
        return str_filtros;
    }

    @Override
    public void insertar() {
        tabDecimos.insertar();
    }

    @Override
    public void guardar() {
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
            case "DETALLE DE DECIMOS":
                diaDialogosr.Limpiar();
                Grid griVersions = new Grid();
                griVersions.setColumns(2);
                griVersions.getChildren().add(new Etiqueta("Seleccione Año :"));
                griVersions.getChildren().add(comboAnio);
                griVersions.getChildren().add(new Etiqueta("Seleccione Periodo :"));
                griVersions.getChildren().add(comboPeriodo);
                griVersions.getChildren().add(new Etiqueta("Tipo de Servidor :"));
                griVersions.getChildren().add(comboEmpleados);
                griVersions.getChildren().add(new Etiqueta("Tipo Parametro :"));
                griVersions.getChildren().add(comboDecimo);
                griVersions.getChildren().add(new Etiqueta("Parametro :"));
                griVersions.getChildren().add(comboAccion);
                gridr.getChildren().add(griVersions);
                diaDialogosr.setDialogo(gridr);
                diaDialogosr.dibujar();
                break;
        }
    }

    public void aceptoDecimo() {
        switch (rep_reporte.getNombre()) {
            case "DETALLE DE DECIMOS":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("anio", comboAnio.getValue() + "");
                p_parametros.put("periodo", comboPeriodo.getValue() + "");
                p_parametros.put("servidor", comboEmpleados.getValue() + "");
                if (comboDecimo.getValue().equals("1") && comboEmpleados.getValue().equals("1")) {
                    p_parametros.put("parametro", "15");
                } else if (comboDecimo.getValue().equals("2") && comboEmpleados.getValue().equals("1")) {
                    p_parametros.put("parametro", "16");
                } else if (comboDecimo.getValue().equals("1") && comboEmpleados.getValue().equals("2")) {
                    p_parametros.put("parametro", "42");
                } else if (comboDecimo.getValue().equals("2") && comboEmpleados.getValue().equals("2")) {
                    p_parametros.put("parametro", "43");
                }
                p_parametros.put("acumula", comboAccion.getValue() + "");//si/no acumula
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabDecimos() {
        return tabDecimos;
    }

    public void setTabDecimos(Tabla tabDecimos) {
        this.tabDecimos = tabDecimos;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
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
}
