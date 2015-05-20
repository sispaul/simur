/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
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
public class DecimaRemuneracion extends Pantalla {

    private Tabla tabDecimos = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Panel panOpcion = new Panel();
    private Combo comboParametros = new Combo();
    private Combo comboDistributivo = new Combo();
    private Combo comboAcciones = new Combo();
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
    public DecimaRemuneracion() {
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
        bar_botones.agregarSeparador();

        comboParametros.setId("comboParametros");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Empleados"
        };
        Object fila2[] = {
            "2", "Trabajadores"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        comboParametros.setCombo(lista);
        bar_botones.agregarComponente(new Etiqueta("Tipo : "));
        bar_botones.agregarComponente(comboParametros);

        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("select periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S'");
        bar_botones.agregarComponente(new Etiqueta("Remuneración : "));
        bar_botones.agregarComponente(comboDistributivo);
        bar_botones.agregarSeparador();

        Boton bot = new Boton();
        bot.setValue("Ejecutar");
        bot.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot.setMetodo("cargarInfo");
        bar_botones.agregarBoton(bot);

        tabDecimos.setId("tabDecimos");
        tabDecimos.setConexion(conPostgres);
        tabDecimos.setTabla("srh_decimo_cuarto_tercero", "decimo_id", 1);
        tabDecimos.getColumna("decimo_cod_empleado").setLectura(true);
        tabDecimos.getColumna("decimo_columna").setVisible(false);
        tabDecimos.getColumna("decimo_tipo_empleado").setVisible(false);
        tabDecimos.getColumna("decimo_columna").setVisible(false);
        tabDecimos.setRows(20);
        tabDecimos.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDecimos);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTADO DE CALCULO DE DECIMOS ACUMULADOS/PAGO MENSUAL");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);
    }

    public void cargarInfo() {
        if (comboAcciones.getValue().equals("1")) {//Información que se subira
            setMigraRoles();
        } else if (comboAcciones.getValue().equals("2")) {//Subida a Roles
        } else if (comboAcciones.getValue().equals("3")) {//Limpiar Datos
            actualizarLista();
        } else {
            utilitario.agregarMensaje("Debe escoger una Acción a realizar", "");
        }
    }

    public void setMigraRoles() {
        TablaGenerica tabDato = mDescuento.VerificarRol();
        if (!tabDato.isEmpty()) {
            if (comboParametros.getValue().equals("1")) {
                if (comboDistributivo.getValue().equals("D3")) {
                } else if (comboDistributivo.getValue().equals("D4")) {
                } else {
                    utilitario.agregarMensaje("Debe Escoger Remuneración a Calcular", "");
                }
            } else if (comboParametros.getValue().equals("2")) {
                if (comboDistributivo.getValue().equals("D3")) {
                } else if (comboDistributivo.getValue().equals("D4")) {
                } else {
                    utilitario.agregarMensaje("Debe Escoger Remuneración a Calcular", "");
                }
            } else {
                utilitario.agregarMensaje("Debe escoger un Tipo Servidor", "");
            }
        } else {
            utilitario.agregarMensaje("No Puede Ser Subido a Rol", "Periodo de Rol Aun No Creado");
        }
    }

    public void actualizarLista() {
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
}
