/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Diego
 */
public class pre_arriendo extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Tabla tab_tabla5 = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();

    public pre_arriendo() {

        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT \n"
                + "a.IDE_CMARE, \n"
                + "a.NOMBRE_INHUMADO_CMARE,\n"
                + "(case when a.NICHO_SITIO_CMARE =1  then 'nicho' when a.NICHO_SITIO_CMARE = 2 then 'suelo' end ) AS sitio, \n"
                + "r.NOMBRES_APELLIDOS_CMREP, \n"
                + "r.DOCUMENTO_IDENTIDAD_CMREP \n"
                + "FROM \n"
                + "dbo.CMT_ARRENDAMIENTO a \n"
                + "LEFT JOIN dbo.CMT_REPRESENTANTE r ON r.IDE_CMARE = a.IDE_CMARE");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarComponente(new Etiqueta("Buscador Personas:"));
        bar_botones.agregarComponente(aut_busca);
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);


        sef_reporte.setId("sef_reporte");
        agregarComponente(sef_reporte);

        bar_botones.agregarReporte();

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CMT_ARRENDAMIENTO", "IDE_CMARE", 1);
        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.getColumna("FECHA_DOCUMENTO_CMARE").setValorDefecto(utilitario.getFechaActual());
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "NICHO"
        };
        Object fila2[] = {
            "2", "SITIO (SUELO)"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_tabla1.getColumna("NICHO_SITIO_CMARE").setRadio(lista, "1");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.agregarRelacion(tab_tabla5);

        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setHeader("DATOS DEL REPRESENTANTE");
        tab_tabla2.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tab_tabla2.getColumna("IDE_CMTID").setCombo("select IDE_CMTID,DETALLE_CMTID from CMT_TIPO_DOCUMENTO");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
        tab_tabla3.setTabla("CMT_PAGO", "IDE_CMPAG", 3);
        tab_tabla3.setHeader("DATOS DEL PAGO");
        tab_tabla3.getColumna("IDE_CMESP").setCombo("SELECT IDE_CMESP,DETALLLE_CMESP FROM CMT_ESTADO_PAGO");
        tab_tabla3.getColumna("IDE_CMACC").setCombo("SELECT IDE_CMACC,DETALLE_CMACC FROM CMT_ACCION");
        tab_tabla3.getGrid().setColumns(4);
        tab_tabla3.getColumna("FECHA_PAGO_CMPAG").setValorDefecto(utilitario.getFechaActual());
        tab_tabla3.getColumna("FECHA_PAGO_CMPAG").setLectura(true);
        tab_tabla3.setTipoFormulario(true);
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setIdCompleto("tab_tabulador:tab_tabla4");
        tab_tabla4.setHeader("DETALLES");
        tab_tabla4.setTabla("CMT_DETALLE_ARRENDAMIENTO", "IDE_CMDEA", 4);
        tab_tabla4.getColumna("IDE_CMACC").setCombo("SELECT IDE_CMACC,DETALLE_CMACC FROM CMT_ACCION");
        tab_tabla4.getColumna("IDE_CMACC").setMetodoChange("cambioEstado");
        tab_tabla4.getColumna("FECHA_HORA_ACCION_CMDEA").setCalendarioFechaHora();
        tab_tabla4.getColumna("FECHA_HORA_ACCION_CMDEA").setValorDefecto(utilitario.getFechaHoraActual());
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setPanelTabla(tab_tabla4);

        tab_tabla5.setId("tab_tabla5");
        tab_tabla5.setIdCompleto("tab_tabulador:tab_tabla5");
        tab_tabla5.setTabla("CMT_FOTO", "IDE_CMFOT", 5);
        tab_tabla5.setHeader("FOTOS");
        tab_tabla5.getColumna("PATH_CMFOT").setUpload("cementerio");
        tab_tabla5.getColumna("PATH_CMFOT").setImagen("256", "256");
        tab_tabla5.getGrid().setColumns(4);
        tab_tabla5.setTipoFormulario(true);
        tab_tabla5.dibujar();
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla5);

        tab_tabulador.agregarTab("REPRESENTANTE", pat_panel2);
        tab_tabulador.agregarTab("PAGO", pat_panel3);
        tab_tabulador.agregarTab("DETALLE", pat_panel4);
        tab_tabulador.agregarTab("FOTO", pat_panel5);



        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        Map p_parametros = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("Arrendamiento Cementerio")) {
            if (tab_tabla1.getValorSeleccionado() != null) {
                if (tab_tabla2.getTotalFilas() > 0) {
                    p_parametros.put("titulo", "INFORME PARA ARRENDAMIENTO EN EL CEMENTERIO MUNICIPAL");
                    p_parametros.put("par_para", "JEFE DE RENTAS");
                    p_parametros.put("par_de", "Lic. Nelson Loachamin");
                    p_parametros.put("ide_cmare", Integer.parseInt(tab_tabla1.getValorSeleccionado()));
                    rep_reporte.cerrar();
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarMensaje("No se a puede generar el reporte", "Falta ingresar los datos del representante");
                }

            } else {
                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Control de Cementerio")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sec_rango.dibujar();
            } else if (sec_rango.isVisible()) {
                if (sec_rango.isFechasValidas()) {
                    p_parametros.put("fecha_inicio", sec_rango.getFecha1String());
                    p_parametros.put("fecha_fin", sec_rango.getFecha2String());
                    sec_rango.cerrar();
                    p_parametros.put("titulo", "HOJA DE CONTROL DE INHUMACIONES, EXHUMACIONES,RENOVACIONES Y OTROS CONTROLES DEL CEMENTERIO MUNICIPAL DE SANGOLQUI CANTON RUMIÑAHUI");
                    sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_reporte.dibujar();
                } else {
                    utilitario.agregarNotificacionInfo("Rango de fechas no válidas", "");
                }
            }
        }
    }

    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {

            tab_tabla1.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_tabla1");
            //tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            //tab_tabla3.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            //tab_tabla4.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            //tab_tabla5.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());            
        }
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        if (tab_tabla4.isFocus() && tab_tabla4.isFilaInsertada()) {
            tab_tabla4.setValor("FECHA_HORA_ACCION_CMDEA", utilitario.getFechaHoraActual());
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla3.guardar()) {
                    if (tab_tabla4.isFocus() && tab_tabla4.isFilaInsertada()) {
                        tab_tabla4.setValor("FECHA_HORA_ACCION_CMDEA", utilitario.getFechaHoraActual());
                    }
                    if (tab_tabla4.guardar()) {
                        if (tab_tabla5.guardar()) {
                            if (guardarPantalla().isEmpty()) {
                                aut_busca.actualizar();
                                aut_busca.setSize(70);
                                utilitario.addUpdate("aut_busca");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void cambioEstado() {
        tab_tabla4.setValor("FECHA_HORA_ACCION_CMDEA", utilitario.getFechaHoraActual());
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }

    public Tabla getTab_tabla5() {
        return tab_tabla5;
    }

    public void setTab_tabla5(Tabla tab_tabla5) {
        this.tab_tabla5 = tab_tabla5;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }
}
