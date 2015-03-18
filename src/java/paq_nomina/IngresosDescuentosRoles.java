/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
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
public class IngresosDescuentosRoles extends Pantalla {
    //atributo para conexion a base de datos

    private Conexion conPostgres = new Conexion();
    //atributos para combo
    private Combo comboParametros = new Combo();
    private Combo comboDistributivo = new Combo();
    private Combo comboAcciones = new Combo();
    private Combo cmbAnio = new Combo();
    private Combo cmbPeriodo = new Combo();
    private Combo cmbDescripcion = new Combo();
    //atributo para pantalla tipo tabla
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setRoles = new SeleccionTabla();
    private SeleccionTabla setRol = new SeleccionTabla();
    //atributo para dialogo
    private Dialogo dialogoMigrar = new Dialogo();
    private Dialogo dialogoRol = new Dialogo();
    private Grid gridDe = new Grid();
    private Grid gridRol = new Grid();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //1.-
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public IngresosDescuentosRoles() {

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
            "3", "BORRAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        list.add(filas3);;
        comboAcciones.setCombo(list);
        bar_botones.agregarComponente(new Etiqueta("Acción : "));
        bar_botones.agregarComponente(comboAcciones);
        bar_botones.agregarSeparador();

        comboParametros.setId("comboParametros");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "INGRESOS"
        };

        Object fila2[] = {
            "2", "DESCUENTOS"
        };

        Object fila3[] = {
            "3", "ANTICIPOS"
        };

        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        comboParametros.setCombo(lista);
        bar_botones.agregarComponente(new Etiqueta("Seleccione Parametro : "));
        bar_botones.agregarComponente(comboParametros);

        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
//        comboDistributivo.setMetodo("mostrarColumna2");
        bar_botones.agregarComponente(new Etiqueta("Distributivo : "));
        bar_botones.agregarComponente(comboDistributivo);

        Boton bot = new Boton();
        bot.setValue("Ejecutar");
        bot.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot.setMetodo("mostrarColumna1");
        bar_botones.agregarBoton(bot);

        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("srh_descuento", "ide_descuento", 1);
        List listas = new ArrayList();
        Object filass1[] = {
            "1", "ROL"
        };

        Object filass2[] = {
            "2", "ACU"
        };

        Object filass3[] = {
            "3", "NO"
        };
        listas.add(filass1);;
        listas.add(filass2);;
        listas.add(filass3);;
        tabTabla.getColumna("fondos_reserva").setRadio(listas, null);
        tabTabla.dibujar();
        PanelTabla panPanel = new PanelTabla();
        panPanel.setPanelTabla(tabTabla);

        Division divDivision = new Division();
        divDivision.setId("divDivision");
        divDivision.dividir1(panPanel);
        agregarComponente(divDivision);

        //
        setRoles.setId("setRoles");
        setRoles.getTab_seleccion().setConexion(conPostgres);//conexion para seleccion con otra base
        setRoles.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        setRoles.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRoles.getTab_seleccion().setRows(11);
        setRoles.setRadio();
        setRoles.getBot_aceptar().setMetodo("setAceptoDescuento");
        setRoles.setHeader("SELECCIONE PARAMETROS PARA DESCUENTO");
        agregarComponente(setRoles);

        /*CONFIGURACIÓN DE COMBOS*/
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);

        gri_busca.getChildren().add(new Etiqueta("AÑO:"));
        cmbAnio.setId("cmbAnio");
        cmbAnio.setConexion(conPostgres);
        cmbAnio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca.getChildren().add(cmbAnio);

        gri_busca.getChildren().add(new Etiqueta("PERIODO:"));
        cmbPeriodo.setId("cmbPeriodo");
        cmbPeriodo.setConexion(conPostgres);
        cmbPeriodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca.getChildren().add(cmbPeriodo);

        gri_busca.getChildren().add(new Etiqueta("DESCRIPCIÓN:"));
        cmbDescripcion.setId("cmbDescripcion");
        cmbDescripcion.setConexion(conPostgres);
        cmbDescripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        cmbDescripcion.setMetodo("buscarColumna");
        gri_busca.getChildren().add(cmbDescripcion);
        setRol.setId("setRol");
        setRol.getTab_seleccion().setConexion(conPostgres);//conexion para seleccion con otra base
        setRol.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        setRol.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRol.getTab_seleccion().setRows(11);
        setRol.setRadio();
        setRol.getGri_cuerpo().setHeader(gri_busca);
        setRol.getBot_aceptar().setMetodo("aceptoDescuentos");
        setRol.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setRol);

        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dialogoMigrar.setId("DialogoMigrar");
        dialogoMigrar.setTitle("CONFIRMAR SUBIDA A ROL"); //titulo
        dialogoMigrar.setWidth("27%"); //siempre en porcentajes  ancho
        dialogoMigrar.setHeight("18%");//siempre porcentaje   alto 
        dialogoMigrar.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoMigrar.getBot_aceptar().setMetodo("setMigraRoles");
//        dialogoMigrar.getBot_cancelar().setMetodo("cancelarValores");
        gridDe.setColumns(4);
        Etiqueta eti = new Etiqueta();
        eti.setValue("ADVERTENCIA - EL SIGUIENTE PROCESO AFECTARA ");
        eti.setStyle("font-size:13px;color:red");
        Etiqueta eti1 = new Etiqueta();
        eti1.setValue("AÑO  :");
        Etiqueta eti4 = new Etiqueta();
        eti4.setStyle("text-align:center;position:absolute;top:28px;left:60px;");
        eti4.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        Etiqueta eti2 = new Etiqueta();
        eti2.setValue("PERIODO  :");
        Etiqueta eti5 = new Etiqueta();
        eti5.setStyle("text-align:center;position:absolute;top:50px;left:84px;");
        eti5.setValue(utilitario.getMes(utilitario.getFechaActual()));
        Etiqueta eti6 = new Etiqueta();
        eti6.setStyle("text-align:center;position:absolute;top:50px;left:104px;");
        eti6.setValue(utilitario.getNombreMes(utilitario.getMes(utilitario.getFechaActual())));
        Etiqueta eti3 = new Etiqueta();
        eti3.setValue("DEL ROL DE PAGOS ");
        eti3.setStyle("font-size:13px;color:red");

        dialogoMigrar.setDialogo(eti);
        dialogoMigrar.setDialogo(eti1);
        dialogoMigrar.setDialogo(eti4);
        dialogoMigrar.setDialogo(eti2);
        dialogoMigrar.setDialogo(eti5);
        dialogoMigrar.setDialogo(eti6);
        dialogoMigrar.setDialogo(eti3);
        utilitario.addUpdate("tab_tabla");
        agregarComponente(dialogoMigrar);

        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dialogoRol.setId("dialogoRol");
        dialogoRol.setTitle("REPORTES DE ROL - EMPLEADOS"); //titulo
        dialogoRol.setWidth("35%"); //siempre en porcentajes  ancho
        dialogoRol.setHeight("20%");//siempre porcentaje   alto 
        dialogoRol.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoRol.getBot_aceptar().setMetodo("aceptoDescuentos");
        gridRol.setColumns(4);
        agregarComponente(dialogoRol);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

    }

    public void abrirDialogo() {
        dialogoMigrar.dibujar();
    }

    public void buscarColumna() {
        if (cmbDescripcion.getValue() != null && cmbDescripcion.getValue().toString().isEmpty() == false) {
            setRol.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE DISTRIBUTIVO=" + cmbDescripcion.getValue());
            setRol.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo", "");
        }
    }

    public void mostrarColumna1() {
        if (comboAcciones.getValue().equals("1")) {//Información que se subira
            if (comboParametros.getValue().equals("1")) {//Ingresos
                buscaColumna();
            } else if (comboParametros.getValue().equals("2")) {//Descuentos
                buscaColumna();
            } else if (comboParametros.getValue().equals("3")) {//Anticipos
                getAnticipo();
            } else if (comboParametros.getValue().equals("4")) {//Fondos Reserva
                setAceptoDescuento();
            } else {
                utilitario.agregarMensaje("Debe elegir un parametro", "");
            }
        } else if (comboAcciones.getValue().equals("2")) {//Subida a Roles
            abrirDialogo();
        } else if (comboAcciones.getValue().equals("3")) {//Elimina Datos
            setBorrarTabla();
        } else {
            utilitario.agregarMensaje("Debe escoger una Acción a realizar", "");
        }
    }

    public void buscaColumna() {
        if (comboDistributivo.getValue() != null && comboDistributivo.getValue().toString().isEmpty() == false) {
            setRoles.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM srh_columnas WHERE ingreso_descuento in( " + comboParametros.getValue() + ",3) and distributivo=" + comboDistributivo.getValue());
            setRoles.getTab_seleccion().ejecutarSql();
            setRoles.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una elemento", "");
        }
    }

    public void getAnticipo() {
        if (comboDistributivo.getValue() != null && comboDistributivo.getValue().toString().isEmpty() == false) {
            mDescuento.InsertarAnticipo(Integer.parseInt(comboDistributivo.getValue() + ""));
            tabTabla.actualizar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una tipo", "");
        }
    }

    public void setMigraRoles() {
        TablaGenerica tabDato = mDescuento.VerificarRol();
        if (!tabDato.isEmpty()) {
            if (comboParametros.getValue().equals("1")) {//ingresos
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    if (tabTabla.getValor(i, "ide_columna").equals("86") || tabTabla.getValor(i, "ide_columna").equals("89")) {
                        TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")),
                                tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")));
                        if (!tabDatos.isEmpty()) {
                            if (tabTabla.getValor(i, "fondos_reserva").compareTo("1") == 0) {
                                mDescuento.setmigrarDescuento(tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")), Integer.parseInt(tabTabla.getValor(i, "ide_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabTabla.getValor(i, "ano")), Double.valueOf(tabTabla.getValor(i, "descuento")));
                            } else if (tabTabla.getValor(i, "fondos_reserva").compareTo("2") == 0) {
                                TablaGenerica tabFondosa = mDescuento.getConfirmaFondos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), tabTabla.getValor(i, "ide_empleado"));
                                if (!tabFondosa.isEmpty()) {
                                    mDescuento.setActuHisFondos(Integer.parseInt(tabTabla.getValor(i, "ano")), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), Integer.parseInt(tabTabla.getValor(i, "ide_empleado")), Double.parseDouble(tabTabla.getValor(i, "descuento")), tabTabla.getValor(i, "fondos_reserva"));
                                } else {
                                    mDescuento.setHistoricoFondos(Integer.parseInt(tabTabla.getValor(i, "ide_empleado")), tabTabla.getValor(i, "fondos_reserva"));
                                }
                            }
                            if (tabTabla.getValor(i, "fondos_reserva").compareTo("3") == 0) {
                                TablaGenerica tabFondosa = mDescuento.getConfirmaFondos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), tabTabla.getValor(i, "ide_empleado"));
                                if (!tabFondosa.isEmpty()) {
                                    mDescuento.setActuHisFondos(Integer.parseInt(tabTabla.getValor(i, "ano")), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), Integer.parseInt(tabTabla.getValor(i, "ide_empleado")), Double.parseDouble(tabTabla.getValor(i, "descuento")), tabTabla.getValor(i, "fondos_reserva"));
                                } else {
                                    mDescuento.setHistoricoFondos(Integer.parseInt(tabTabla.getValor(i, "ide_empleado")), tabTabla.getValor(i, "fondos_reserva"));
                                }
                            }
                            utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                        } else {
                            utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabTabla.getValor(i, "nombres"));
                        }
                    } else {
                        TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")),
                                tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")));
                        if (!tabDatos.isEmpty()) {
                            mDescuento.setmigrarDescuento(tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")), Integer.parseInt(tabTabla.getValor(i, "ide_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabTabla.getValor(i, "ano")), Double.valueOf(tabTabla.getValor(i, "descuento")));
                            utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                        } else {
                            utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabTabla.getValor(i, "nombres"));
                        }
                    }
                }
//                utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
            } else if (comboParametros.getValue().equals("2")) {//descuentos
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")),
                            tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")));
                    if (!tabDatos.isEmpty()) {
                        mDescuento.setmigrarDescuento(tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")), Integer.parseInt(tabTabla.getValor(i, "ide_columna")), tabConsulta.getValor("NICK_USUA"), "valor_egreso", Integer.parseInt(tabTabla.getValor(i, "ano")), Double.valueOf(tabTabla.getValor(i, "descuento")));
                    } else {
                        utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabTabla.getValor(i, "nombres"));
                    }
                }
                utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
            } else {
                utilitario.agregarMensaje("Parametro Debe ser Diferente de Descuento", null);
            }
            dialogoMigrar.cerrar();

        } else {
            utilitario.agregarMensaje("Descuento No Puede Ser Subido a Rol", "Rol Perteneciente a Periodo Aun No Esta Creado");
        }
    }

    public void setBorrarTabla() {
        mDescuento.borrarDescuento();
        tabTabla.actualizar();
    }

    public void setAceptoDescuento() {
        TablaGenerica tabDato = mDescuento.getColumnas(Integer.parseInt(setRoles.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            if (setRoles.getValorSeleccionado().equals("99")) {//subsidio antiguedad
                TablaGenerica tabDato1 = mDescuento.getTrabajadores(Integer.parseInt(comboDistributivo.getValue() + ""));
                if (!tabDato1.isEmpty()) {
                    for (int i = 0; i < tabDato1.getTotalFilas(); i++) {
                        mDescuento.setSubsidioAntiguedad(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")),
                                Integer.parseInt(tabDato1.getValor(i, "cod_empleado")), Integer.parseInt(comboDistributivo.getValue() + ""));
                    }
                }
            } else if (setRoles.getValorSeleccionado().equals("98")) {//subsidio familiar
                int a = 0;
                if (comboDistributivo.getValue().equals("1")) {
                    a = 25;
                } else {
                    a = 70;
                }
                TablaGenerica tabDato1 = mDescuento.getColumnas(a);
                if (!tabDato1.isEmpty()) {
                    mDescuento.setSubsidioFamiliar(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")), Double.valueOf(tabDato1.getValor("porcentaje_subsidio")));
                }
            } else if (setRoles.getValorSeleccionado().equals("86")) {// fondos empleado
                TablaGenerica tabDato1 = mDescuento.getTrabajadores(Integer.parseInt(comboDistributivo.getValue() + ""));
                if (!tabDato1.isEmpty()) {
                    for (int i = 0; i < tabDato1.getTotalFilas(); i++) {//92,93,18,14
                        mDescuento.setFondosReserva(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")), Integer.parseInt(comboDistributivo.getValue() + ""), "92,93", 18, 14, Integer.parseInt(tabDato1.getValor(i, "cod_empleado")));
                    }
                }
            } else if (setRoles.getValorSeleccionado().equals("89")) {// fondos trabajador
                TablaGenerica tabDato1 = mDescuento.getTrabajadores(Integer.parseInt(comboDistributivo.getValue() + ""));
                if (!tabDato1.isEmpty()) {
                    for (int i = 0; i < tabDato1.getTotalFilas(); i++) {//75,76,0,40
                        mDescuento.setFondosReserva(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")), Integer.parseInt(comboDistributivo.getValue() + ""), "75,76", 0, 40, Integer.parseInt(tabDato1.getValor(i, "cod_empleado")));
                    }
                }
            } else if (setRoles.getValorSeleccionado().equals("92")) {// horas ext 100 empleado
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("2"));
                }
            } else if (setRoles.getValorSeleccionado().equals("75")) {// horas ext 100 trabajador
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("2"));
                }
            } else if (setRoles.getValorSeleccionado().equals("93")) {// horas ext 25 empleado
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras25(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")));
                }
            } else if (setRoles.getValorSeleccionado().equals("76")) {// horas ext 50 trabajador
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("1.5"));
                }
                tabTabla.actualizar();
            } else {
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.ActualizaDatos(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""));
                }
            }
        }
        setRoles.cerrar();
        tabTabla.actualizar();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "INCONSISTENCIA EN DESCUENTOS":
                aceptoDescuentos();
                break;
            case "DESCUENTOS ANIO Y PERIODO":
                setRol.dibujar();
                setRol.getTab_seleccion().limpiar();
                break;
            case "ROL DE PAGOS EMPLEADOS - POR MES":
                dialogoRol.Limpiar();
                gridRol.getChildren().add(new Etiqueta("AÑO :"));
                gridRol.getChildren().add(cmbAnio);
                gridRol.getChildren().add(new Etiqueta("PERIODO :"));
                gridRol.getChildren().add(cmbPeriodo);
                gridRol.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                gridRol.getChildren().add(cmbDescripcion);
                dialogoRol.setDialogo(gridRol);
                dialogoRol.dibujar();
                break;
            case "VERIFICAR SUBIDA A ROL":
                aceptoDescuentos();
                break;

        }
    }

    // dibujo de reporte y envio de parametros
    public void aceptoDescuentos() {
        switch (rep_reporte.getNombre()) {
            case "INCONSISTENCIA EN DESCUENTOS":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "DESCUENTOS ANIO Y PERIODO":
                if (setRol.getValorSeleccionado() != null) {
                    TablaGenerica tab_dato = mDescuento.periodo(Integer.parseInt(cmbPeriodo.getValue() + ""));
                    if (!tab_dato.isEmpty()) {
                        TablaGenerica tab_dato1 = mDescuento.distibutivo(Integer.parseInt(cmbDescripcion.getValue() + ""));
                        if (!tab_dato1.isEmpty()) {
                            TablaGenerica tab_dato2 = mDescuento.columnas(Integer.parseInt(setRol.getValorSeleccionado() + ""));
                            if (!tab_dato2.isEmpty()) {
                                p_parametros = new HashMap();
                                p_parametros.put("pide_ano", Integer.parseInt(cmbAnio.getValue() + ""));
                                p_parametros.put("periodo", Integer.parseInt(cmbPeriodo.getValue() + ""));
                                p_parametros.put("p_nombre", tab_dato.getValor("per_descripcion") + "");
                                p_parametros.put("distributivo", Integer.parseInt(cmbDescripcion.getValue() + ""));
                                p_parametros.put("descripcion", tab_dato1.getValor("descripcion") + "");
                                p_parametros.put("columnas", Integer.parseInt(setRol.getValorSeleccionado() + ""));
                                p_parametros.put("descrip", tab_dato2.getValor("descripcion_col") + "");
                                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                                rep_reporte.cerrar();
                                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                sef_formato.dibujar();
                                setRol.cerrar();
                            } else {
                                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                }
                break;
            case "ROL DE PAGOS EMPLEADOS - POR MES":
                TablaGenerica tab_dato1 = mDescuento.distibutivo(Integer.parseInt(cmbDescripcion.getValue() + ""));
                if (!tab_dato1.isEmpty()) {
                    p_parametros = new HashMap();
                    p_parametros.put("anio", Integer.parseInt(cmbAnio.getValue() + ""));
                    p_parametros.put("periodo", Integer.parseInt(cmbPeriodo.getValue() + ""));
                    p_parametros.put("distributivo", Integer.parseInt(cmbDescripcion.getValue() + ""));
                    p_parametros.put("nom_distri", tab_dato1.getValor("descripcion") + "");
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                }
                break;
            case "VERIFICAR SUBIDA A ROL":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public SeleccionTabla getSetRoles() {
        return setRoles;
    }

    public void setSetRoles(SeleccionTabla setRoles) {
        this.setRoles = setRoles;
    }

    public SeleccionTabla getSetRol() {
        return setRol;
    }

    public void setSetRol(SeleccionTabla setRol) {
        this.setRol = setRol;
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
