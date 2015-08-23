/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import paq_manauto.ejb.SQLManauto;
import paq_presupuestaria.ejb.Programas;
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
    private Tabla setRegistro = new Tabla();
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
    Texto txtMotivo = new Texto();
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    private SQLManauto aCombustible = (SQLManauto) utilitario.instanciarEJB(SQLManauto.class);

    public CertificadoFondos() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBuscar);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("CERTIFICADO DE DISPONIBILIDAD DE FONDOS ");
        agregarComponente(panOpcion);

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("SELECT c.id_codigo,c.numero_certificado,c.memorando,p.des_proyecto FROM cert_fondos c\n"
                + "inner join pre_proyectos p on c.codigo_proyecto = p.pre_ide_proyecto order by c.numero_certificado");
        autBusca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autBusca);

        Boton botAnular = new Boton();
        botAnular.setValue("Anular");
        botAnular.setExcluirLectura(true);
        botAnular.setIcon("ui-icon-cancel");
        botAnular.setMetodo("quitar");
        bar_botones.agregarBoton(botAnular);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("CERTIFICADO DE DISPONIBILIDAD DE FONDOS");
        agregarComponente(panOpcion);

        // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("anular");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("LISTADO DE ORDENES DE PAGO REALIZADAS"); //titulo
        diaDialogot.setWidth("75%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("85%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getBot_aceptar().setMetodo("cargarRegistro");
        gridt.setColumns(4);
        agregarComponente(diaDialogot);


        setRegistro.setId("setRegistro");
        setRegistro.setConexion(conPostgres);
        setRegistro.setSql("SELECT c.id_codigo,c.numero_certificado,c.memorando,c.fecha_memorando,p.des_proyecto,c.monto,e.nombres,c.estado\n"
                + "FROM cert_fondos c\n"
                + "INNER JOIN pre_proyectos p ON c.codigo_proyecto = p.pre_ide_proyecto \n"
                + "inner join srh_empleado e on e.cod_empleado = c.codigo_funcionario");
        setRegistro.getColumna("id_codigo").setVisible(false);
        setRegistro.getColumna("numero_certificado").setLongitud(4);
        setRegistro.getColumna("numero_certificado").setFiltroContenido();
        setRegistro.getColumna("des_proyecto").setFiltroContenido();
        setRegistro.getColumna("des_proyecto").setLongitud(80);
        setRegistro.setLectura(true);
        setRegistro.setRows(13);
        setRegistro.dibujar();


        dibujaCertificado();
    }

    public void dibujaCertificado() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setConexion(conPostgres);
        tabOrden.setTabla("cert_fondos", "id_codigo", 1);
        if (autBusca.getValue() == null) {
            tabOrden.setCondicion("id_codigo=-1");
        } else {
            tabOrden.setCondicion("id_codigo=" + autBusca.getValor());
        }
        tabOrden.getColumna("codigo_funcionario").setCombo("SELECT e.cod_empleado,e.nombres FROM srh_empleado e INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo\n"
                + "WHERE e.estado = 1 and c.nombre_cargo like 'FUNCIONARIO%' ORDER BY e.nombres");
        tabOrden.getColumna("direccion_funcionario").setCombo("select cod_direccion,nombre_dir from srh_direccion order by nombre_dir");
        tabOrden.getColumna("cargo_funcionario").setCombo("select cod_cargo,nombre_cargo from srh_cargos where nombre_cargo like 'FUNCIONARIO%' order by nombre_cargo");
        tabOrden.getColumna("codigo_proyecto").setCombo("select pre_ide_proyecto,des_proyecto from pre_proyectos where des_proyecto like 'EMERGENCIA%'");
        tabOrden.getColumna("director_financiero").setCombo("SELECT e.cod_empleado,e.nombres FROM srh_empleado e INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo where c.cod_cargo = 555");
        tabOrden.getColumna("director_presupuesto").setCombo("SELECT e.cod_empleado,e.nombres FROM srh_empleado e INNER JOIN srh_cargos c ON e.cod_cargo = c.cod_cargo where c.cod_cargo = 66");
        tabOrden.getColumna("codigo_funcionario").setMetodoChange("Datosfuncionario");
        tabOrden.getColumna("fecha_certificado").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("login_ingreso").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("ip_ingreso").setValorDefecto(utilitario.getIp());
        List list = new ArrayList();
        Object fil1[] = {
            "Ingresado", "Ingresado"
        };
        Object fil2[] = {
            "Anulado", "Anulado"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabOrden.getColumna("estado").setCombo(list);
        tabOrden.getColumna("fecha_certificado").setVisible(false);
        tabOrden.getColumna("fecha_ingreso").setVisible(false);
        tabOrden.getColumna("login_ingreso").setVisible(false);
        tabOrden.getColumna("fecha_actualizacion").setVisible(false);
        tabOrden.getColumna("login_actualizacion").setVisible(false);
        tabOrden.getColumna("ip_actualizacion").setVisible(false);
        tabOrden.getColumna("observacion").setVisible(false);
        tabOrden.getColumna("ip_ingreso").setVisible(false);
        tabOrden.getColumna("login_anulacion").setVisible(false);
        tabOrden.getColumna("fecha_anulacion").setVisible(false);
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

    public void buscaRegistro() {
        diaDialogot.Limpiar();
        diaDialogot.setDialogo(gridt);
        gridT.getChildren().add(setRegistro);
        diaDialogot.setDialogo(gridT);
        setRegistro.dibujar();
        diaDialogot.dibujar();
    }

    public void quitar() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        txtMotivo.setSize(50);
        gridm.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN :"));
        grid.getChildren().add(new Etiqueta("_____________________________________________________"));
        grid.getChildren().add(txtMotivo);
        diaDialogo.setDialogo(gridD);
        diaDialogo.dibujar();
    }

    public void anular() {
        programas.actCertificado(tabOrden.getValor("numero_certificado"), Integer.parseInt(tabOrden.getValor("id_codigo")), tabConsulta.getValor("NICK_USUA"), txtMotivo.getValue() + "");
        utilitario.addUpdate("tabOrden");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        diaDialogo.cerrar();
    }

    public void numero() {
        tabOrden.setValor("director_financiero", "764109");
        tabOrden.setValor("director_presupuesto", "407000");
        tabOrden.setValor("estado", "Ingresado");
        String numero = programas.maxCertificados();
        String num;
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "000000" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "00000" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "0000" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "000" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = "00" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 100000 && cantidad <= 999999) {
                num = "0" + String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            } else if (cantidad >= 1000000 && cantidad <= 9999999) {
                num = String.valueOf(cantidad);
                tabOrden.setValor("numero_certificado", num);
            }
        }
        utilitario.addUpdate("tabOrden");
    }

    public void Datosfuncionario() {
        TablaGenerica tabInfo = programas.getCertificado(Integer.parseInt(tabOrden.getValor("codigo_funcionario")));
        if (!tabInfo.isEmpty()) {
            tabOrden.setValor("direccion_funcionario", tabInfo.getValor("cod_direccion"));
            tabOrden.setValor("cargo_funcionario", tabInfo.getValor("cod_cargo"));
            utilitario.addUpdate("tabOrden");
        } else {
            utilitario.agregarMensajeInfo("No se Encuentra Funcionario", null);
        }
    }

    public void comprobante() {
        if (tabOrden.getValor("estado") != "Anulada") {
            tabOrden.setValor("estado", "Pagado");
            utilitario.addUpdate("tabOrden");
        } else {
            utilitario.agregarMensajeInfo("Certificado No Puede Ser Pagado", "Se Encuentra Anulado");
        }
    }

    public void cargarRegistro() {
        if (setRegistro.getValorSeleccionado() != null) {
            autBusca.setValor(setRegistro.getValorSeleccionado());
            dibujaCertificado();
            diaDialogot.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        if (tabOrden.getValor("id_codigo") != null) {
            TablaGenerica tabInfo = aCombustible.getCatalogoDato("*", tabOrden.getTabla(), "id_codigo = " + tabOrden.getValor("id_codigo") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = aCombustible.getNumeroCampos(tabOrden.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = aCombustible.getEstrucTabla(tabOrden.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabOrden.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        aCombustible.setActuaRegis(Integer.parseInt(tabOrden.getValor("id_codigo")), tabOrden.getTabla(), tabInfoColum1.getValor("Column_Name"), tabOrden.getValor(tabInfoColum1.getValor("Column_Name")), "id_codigo");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else {
            if (tabOrden.guardar()) {
                conPostgres.guardarPantalla();
            }
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
            case "CERTIFICADO DE FONDOS":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        switch (rep_reporte.getNombre()) {
            case "CERTIFICADO DE FONDOS":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("certificado", Integer.parseInt(tabOrden.getValor("id_codigo") + ""));
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

    public Tabla getTabOrden() {
        return tabOrden;
    }

    public void setTabOrden(Tabla tabOrden) {
        this.tabOrden = tabOrden;
    }

    public Tabla getSetRegistro() {
        return setRegistro;
    }

    public void setSetRegistro(Tabla setRegistro) {
        this.setRegistro = setRegistro;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }
    
}
