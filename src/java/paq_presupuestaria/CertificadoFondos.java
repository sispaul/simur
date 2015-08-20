/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Dialogo;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class CertificadoFondos extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabOrden = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridm = new Grid();
    private Grid gridt = new Grid();
    private Grid gridD = new Grid();
    private Grid gridT = new Grid();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    Texto txt_motivo = new Texto();

    public CertificadoFondos() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ORDENES DE PAGO");
        agregarComponente(panOpcion);

        // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        dibujaCertificado();
    }

    public void dibujaCertificado() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setConexion(conPostgres);
        tabOrden.setTabla("cert_fondos", "id_codigo", 1);
//        if (autBusca.getValue() == null) {
//            tabOrden.setCondicion("tes_ide_orden_pago=-1");
//        } else {
//            tabOrden.setCondicion("tes_ide_orden_pago=" + autBusca.getValor());
//        }
        tabOrden.getColumna("codigo_funcionario").setCombo("select cod_empleado,nombres from srh_empleado where estado =1 order by nombres");
        tabOrden.getColumna("direccion_funcionario").setCombo("select cod_direccion,nombre_dir from srh_direccion order by nombre_dir");
        tabOrden.getColumna("cargo_funcionario").setCombo("select cod_cargo,nombre_cargo from srh_cargos where nombre_cargo like 'FUNCIONARIO%' order by nombre_cargo");
        tabOrden.getColumna("codigo_proyecto").setCombo("select pre_ide_proyecto,des_proyecto from pre_proyectos where des_proyecto like 'EMERGENCIA%'");
        tabOrden.getColumna("director_financiero").setCombo("");
        tabOrden.getColumna("director_presupuesto").setCombo("");
        tabOrden.getColumna("fecha_certificado").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("login_ingreso").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("ip_ingreso").setValorDefecto(utilitario.getIp());

        tabOrden.getColumna("fecha_certificado").setVisible(false);
        tabOrden.getColumna("fecha_ingreso").setVisible(false);
        tabOrden.getColumna("login_ingreso").setVisible(false);
        tabOrden.getColumna("fecha_actualizacion").setVisible(false);
        tabOrden.getColumna("login_actualizacion").setVisible(false);
        tabOrden.getColumna("ip_actualizacion").setVisible(false);
        tabOrden.getColumna("observacion").setVisible(false);
        tabOrden.setTipoFormulario(true);
        tabOrden.getGrid().setColumns(2);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
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
