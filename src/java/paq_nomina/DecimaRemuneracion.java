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

        Grid gri_busca = new Grid();
        gri_busca.setColumns(8);

        gri_busca.getChildren().add(new Etiqueta("Acciones :"));
        comboAcciones.setId("comboAcciones");
        List list = new ArrayList();
        Object filas1[] = {
            "1", "INFORMACIÓN"
        };
        Object filas2[] = {
            "2", "MIGRAR A ROL"
        };
        Object filas3[] = {
            "3", "CONSULTAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        list.add(filas3);;
        comboAcciones.setCombo(list);
        gri_busca.getChildren().add(comboAcciones);

        gri_busca.getChildren().add(new Etiqueta("Tipo Servidor :"));
        comboEmpleados1.setId("comboEmpleados1");
        comboEmpleados1.setConexion(conPostgres);
        comboEmpleados1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        gri_busca.getChildren().add(comboEmpleados1);

        gri_busca.getChildren().add(new Etiqueta("Tipo Columna :"));
        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("select periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S'");
        gri_busca.getChildren().add(comboDistributivo);

        Boton bot = new Boton();
        bot.setValue("Ejecutar");
        bot.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot.setMetodo("cargarInfo");
        gri_busca.getChildren().add(bot);
        bar_botones.agregarComponente(gri_busca);


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
        tabDecimos.setRows(18);
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
        Integer fecha = 0, valo = 0, valo1 = 0;
        String columna = "", fechaIni = "", fechaFin = "";
        fecha = utilitario.getMes(utilitario.getFechaActual());
//        double sbu = mDescuento.getSbu();
        if (comboAcciones.getValue().equals("1")) {//Información que Subira
            TablaGenerica tabDato = mDescuento.getInfoAcumulacion(comboEmpleados1.getValue() + "");
            if (!tabDato.isEmpty()) {
                TablaGenerica tabFecha = mDescuento.getPeriodos(comboDistributivo.getValue() + "");
                if (!tabFecha.isEmpty()) {
                    fechaIni = tabFecha.getValor("periodo_fecha_inicial");
                    if (comboDistributivo.getValue().equals("D3")) {
                        if (comboEmpleados1.getValue().equals("1")) {
                            columna = "15";
                        } else if (comboEmpleados1.getValue().equals("2")) {
                            columna = "42";
                        }
                        for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                            TablaGenerica tabDatos = mDescuento.getInfoListaPago(tabDato.getValor(i, "autoriza_cod_empleado"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                                    String.valueOf(utilitario.getMes(utilitario.getFechaActual())), columna);
                            if (!tabDatos.isEmpty()) {
                            } else {
                                TablaGenerica tabD3T = mDescuento.getCalculoD3T(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), tabDato.getValor(i, "autoriza_cod_empleado"));
                                if (!tabD3T.isEmpty()) {
                                    double rmu = 0.0, hxe = 0.0, sbr = 0.0, total = 0.0, val = 0.0;
                                    BigDecimal bd;
                                    rmu = Double.parseDouble(tabD3T.getValor("remuneracion"));
                                    hxe = Double.parseDouble(tabD3T.getValor("hxe"));
                                    sbr = Double.parseDouble(tabD3T.getValor("sbr"));
                                    total = rmu + hxe + sbr;
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(total / 12);
                                        val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valo = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((total / 360) * valo);
                                        val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                    if (tabDato.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                        if (fecha.compareTo(11) == 0) {
                                        } else {
                                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, val, tabDato.getValor(i, "autoriza_decimo_tercero"), rmu, hxe, sbr, tabDato.getValor(i, "autoriza_id_distributivo"));
                                            if (tabDato.getValor(i, "autoriza_acumulado_cuarto") != null) {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
                                            } else {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + 0.0);
                                            }
                                        }
                                    } else if (tabDato.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                        if (fecha.compareTo(11) == 0) {
                                        } else {
                                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, val, tabDato.getValor(i, "autoriza_decimo_tercero"), rmu, hxe, sbr, tabDato.getValor(i, "autoriza_id_distributivo"));
//                                            if (tabDato.getValor(i, "autoriza_acumulado_cuarto") != null) {
//                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
//                                            } else {
//                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + 0.0);
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (comboDistributivo.getValue().equals("D4")) {
                        if (comboEmpleados1.getValue().equals("1")) {
                            columna = "16";
                        } else if (comboEmpleados1.getValue().equals("2")) {
                            columna = "43";
                        }
                        for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                            TablaGenerica tabDatos = mDescuento.getInfoListaPago(tabDato.getValor(i, "autoriza_cod_empleado"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                                    String.valueOf(utilitario.getMes(utilitario.getFechaActual())), columna);
                            if (!tabDatos.isEmpty()) {
                            } else {
                                TablaGenerica tabD4T = mDescuento.getCalculoD4T(tabDato.getValor(i, "autoriza_cod_empleado"));
                                if (!tabD4T.isEmpty()) {
                                    double d4t = 0.0, val = 0.0, rmu = 0.0, valac = 0.0;
                                    BigDecimal bd, acu;
                                    rmu = Double.parseDouble(tabDato.getValor("remuneracion"));
                                    d4t = Double.parseDouble(tabD4T.getValor("sbu"));
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(d4t / 12);
                                        val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valo = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((d4t / 360) * valo);
                                        val = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                    if (tabDato.getValor(i, "autoriza_id_distributivo").equals("1")) {
                                        if (fecha.compareTo(7) == 0) {
                                            if (calcularMeses(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 12) {
                                                if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                                                    acu = new BigDecimal(d4t);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                } else {
                                                    acu = new BigDecimal(d4t - Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                }
                                            } else {
                                                valo1 = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                        new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                                if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                                                    acu = new BigDecimal((d4t / 360) * valo1);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                } else {
                                                    acu = new BigDecimal(Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")) + val);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                }
                                            }

                                        } else {
                                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, val, tabDato.getValor(i, "autoriza_decimo_cuarto"), rmu, 0.0, 0.0, tabDato.getValor(i, "autoriza_id_distributivo"));
                                            if (tabDato.getValor(i, "autoriza_acumulado_cuarto") != null) {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
                                            } else {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + 0.0);
                                            }
                                        }
                                    } else if (tabDato.getValor(i, "autoriza_id_distributivo").equals("2")) {
                                        if (fecha.compareTo(7) == 0) {
                                            if (calcularMeses(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 12) {
                                                if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                                                    acu = new BigDecimal(d4t);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                } else {
                                                    acu = new BigDecimal(d4t - Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                }
                                            } else {
                                                valo1 = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                        new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                                if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                                                    acu = new BigDecimal((d4t / 360) * valo1);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                } else {
                                                    acu = new BigDecimal(Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")) + val);
                                                    valac = acu.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                                }
                                            }
                                        } else {
                                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, val, tabDato.getValor(i, "autoriza_decimo_cuarto"), rmu, 0.0, 0.0, tabDato.getValor(i, "autoriza_id_distributivo"));
                                            if (tabDato.getValor(i, "autoriza_acumulado_cuarto") != null) {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + Double.parseDouble(tabDato.getValor(i, "autoriza_acumulado_cuarto")));
                                            } else {
                                                mDescuento.setAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), val + 0.0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    utilitario.agregarMensajeInfo("No existe Periodo Activo", null);
                }
            }
            tabDecimos.actualizar();
        } else if (comboAcciones.getValue()
                .equals("2")) {//Subida a Roles
            setMigraRoles();
        } else if (comboAcciones.getValue()
                .equals("3")) {
            filtarLista();
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
                } else {
                    TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabDecimos.getValor(i, "decimo_anio"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")),
                            tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")));
                    if (!tabDatos.isEmpty()) {
                        mDescuento.setmigrarDescuento(tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabDecimos.getValor(i, "decimo_anio")), Double.valueOf(0.0));
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

    public static int calcularMeses(Calendar cal1, Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60; // calcular la diferencia en horas a
        long diffDays = diffHours / 24; // calcular la diferencia en dias
        long diffWeek = diffDays / 7; // calcular la diferencia en semanas
        long diffMounth = diffWeek / 4; // calcular la diferencia en meses
        return Integer.parseInt(String.valueOf(diffMounth));
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
        String str_filtros = "", valor = "";
        if (tabDecimos.getValorSeleccionado() != null) {
            if (comboEmpleados1.getValue().equals("1")) {
                if (comboDistributivo.getValue().equals("D3")) {
                    valor = "15";
                } else if (comboDistributivo.getValue().equals("D4")) {
                    valor = "16";
                }
            } else if (comboEmpleados1.getValue().equals("2")) {
                if (comboDistributivo.getValue().equals("D3")) {
                    valor = "42";
                } else if (comboDistributivo.getValue().equals("D4")) {
                    valor = "43";
                }
            }
            str_filtros = "decimo_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'"
                    + "and decimo_periodo = '" + String.valueOf(utilitario.getMes(utilitario.getFechaActual())) + "' and "
                    + "decimo_id_distributivo = '" + comboEmpleados1.getValue() + "' "
                    + "and decimo_columna ='" + valor + "'";

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
