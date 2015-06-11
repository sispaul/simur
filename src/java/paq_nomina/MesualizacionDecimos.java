/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

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
public class MesualizacionDecimos extends Pantalla {

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

    public MesualizacionDecimos() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //barra de tareas para busqueda y carga
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
        bot.setIcon("ui-icon-extlink");
        bot.setMetodo("cargarInfo");
        gri_busca.getChildren().add(bot);
        bar_botones.agregarComponente(gri_busca);

        //tabla de registro, formulario que se llena
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

        //para reportes
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

    }

    public void cargaInfo() {
        if (comboAcciones.getValue().equals("1")) {//Llenado de formulario
        } else if (comboAcciones.getValue()
                .equals("2")) {//Subida a Roles
//            setMigraRoles();
        } else if (comboAcciones.getValue()
                .equals("3")) {
//            filtarLista();
        } else {
            utilitario.agregarMensaje("Debe escoger una Acción a realizar", "");
        }
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

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabDecimos() {
        return tabDecimos;
    }

    public void setTabDecimos(Tabla tabDecimos) {
        this.tabDecimos = tabDecimos;
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
