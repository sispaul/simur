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
import framework.componentes.SeleccionTabla;
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
public class IngresosDescuentosRoles extends Pantalla {
    //atributo para conexion a base de datos

    private Conexion conPostgres = new Conexion();
    //atributos para combo
    private Combo comboParametros = new Combo();
    private Combo comboDistributivo = new Combo();
    private Combo comboAcciones = new Combo();
    //atributo para pantalla tipo tabla
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setRoles = new SeleccionTabla();
    //atributo para dialogo
    private Dialogo dialogoMigrar = new Dialogo();
    private Grid gridDe = new Grid();
    //1.-
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public IngresosDescuentosRoles() {

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

        Object fila4[] = {
            "4", "FONDOS RESERVA"
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
        setRoles.getTab_seleccion().setRows(14);
        setRoles.setRadio();
        setRoles.getBot_aceptar().setMetodo("setAceptoDescuento");
        setRoles.setHeader("SELECCIONE PARAMETROS PARA DESCUENTO");
        agregarComponente(setRoles);

        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dialogoMigrar.setId("DialogoMigrar");
        dialogoMigrar.setTitle("CONFIRMAR SUBIDA A ROL"); //titulo
        dialogoMigrar.setWidth("27%"); //siempre en porcentajes  ancho
        dialogoMigrar.setHeight("18%");//siempre porcentaje   alto 
        dialogoMigrar.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoMigrar.getBot_aceptar().setMetodo("migrar");
        dialogoMigrar.getBot_cancelar().setMetodo("cancelarValores");
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
    }

    public void mostrarColumna1() {
        if (comboAcciones.getValue().equals("1")) {//Información que se subira
            if (comboParametros.getValue().equals("1") && comboDistributivo.getValue() != null) {//Ingresos
                buscaColumna();
            } else if (comboParametros.getValue().equals("2") && comboDistributivo.getValue() != null) {//Descuentos
                buscaColumna();
            } else if (comboParametros.getValue().equals("3") && comboDistributivo.getValue() != null) {//Anticipos
                getAnticipo();
            } else if (comboParametros.getValue().equals("4") && comboDistributivo.getValue() != null) {//Fondos Reserva
                buscaColumna();
            }
        } else if (comboAcciones.getValue().equals("2")) {//Subida a Roles
            setMigraRoles();
        } else {//Elimina Datos
            setBorrarTabla();
        }
    }

    public void buscaColumna() {
        if (comboDistributivo.getValue() != null && comboDistributivo.getValue().toString().isEmpty() == false) {
            setRoles.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM srh_columnas WHERE ingreso_descuento = " + comboParametros.getValue() + " and distributivo=" + comboDistributivo.getValue());
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

    public void abrirDialogo() {
        dialogoMigrar.dibujar();
    }

    public void setMigraRoles() {
        TablaGenerica tabDato = mDescuento.VerificarRol();
        if (!tabDato.isEmpty()) {
            for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabTabla.getValor(i, "ano"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")),
                        tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")));
                if (!tabDatos.isEmpty()) {
                    mDescuento.migrarDescuento(tabTabla.getValor(i, "ide_empleado"), Integer.parseInt(tabTabla.getValor(i, "ide_periodo")),
                            Integer.parseInt(tabTabla.getValor(i, "id_distributivo_roles")), Integer.parseInt(tabTabla.getValor(i, "ide_empleado")), tabConsulta.getValor("NICK_USUA") + "");
                    utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                } else {
                    utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabTabla.getValor(i, "nombres"));
                }
            }
            utilitario.agregarMensaje("PROCESO REALIZADO CON EXITO", " ");
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
                tabTabla.actualizar();
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
                    tabTabla.actualizar();
                }
            } else if (setRoles.getValorSeleccionado().equals("86")) {// fondos empleado
                TablaGenerica tabDato1 = mDescuento.getTrabajadores(Integer.parseInt(comboDistributivo.getValue() + ""));
                if (!tabDato1.isEmpty()) {
                    for (int i = 0; i < tabDato1.getTotalFilas(); i++) {//92,93,18,14
                        mDescuento.setFondosReserva(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")), Integer.parseInt(comboDistributivo.getValue() + ""), "92,93", 18, 14, Integer.parseInt(tabDato1.getValor(i, "cod_empleado")));
                    }
                }
                tabTabla.actualizar();
            } else if (setRoles.getValorSeleccionado().equals("89")) {// fondos trabajador
                TablaGenerica tabDato1 = mDescuento.getTrabajadores(Integer.parseInt(comboDistributivo.getValue() + ""));
                if (!tabDato1.isEmpty()) {
                    for (int i = 0; i < tabDato1.getTotalFilas(); i++) {//75,76,0,40
                        mDescuento.setFondosReserva(Integer.parseInt(setRoles.getValorSeleccionado()), Double.valueOf(tabDato.getValor("porcentaje_subsidio")), Integer.parseInt(comboDistributivo.getValue() + ""), "75,76", 0, 40, Integer.parseInt(tabDato1.getValor(i, "cod_empleado")));
                    }
                }
                tabTabla.actualizar();
            } else if (setRoles.getValorSeleccionado().equals("92")) {// horas ext 100 empleado
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("2"));
                }
                tabTabla.actualizar();
            } else if (setRoles.getValorSeleccionado().equals("75")) {// horas ext 100 trabajador
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("2"));
                }
                tabTabla.actualizar();
            } else if (setRoles.getValorSeleccionado().equals("93")) {// horas ext 25 empleado
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras25(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")));
                }
                tabTabla.actualizar();
            } else if (setRoles.getValorSeleccionado().equals("76")) {// horas ext 50 trabajador
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.setHoras100_50(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""), Double.parseDouble(tabTabla.getValor(i, "valor")), Double.parseDouble("1.5"));
                }
                tabTabla.actualizar();
            } else {
                for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
                    mDescuento.ActualizaDatos(tabTabla.getValor(i, "cedula"), Integer.parseInt(setRoles.getValorSeleccionado()), Integer.parseInt(comboDistributivo.getValue() + ""));
                }
                setRoles.cerrar();
                tabTabla.actualizar();
                comboDistributivo.getConexion().desconectar();
            }
        }
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
}
