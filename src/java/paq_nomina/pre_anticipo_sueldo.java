/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.Parametro;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.AntiSueldos;
import paq_nomina.ejb.SolicAnticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_anticipo_sueldo extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    private List<Parametro> lis_parametros = new ArrayList<Parametro>();
    
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_garante = new Tabla();
    private Tabla tab_parametros = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_colaborador = new Tabla();
    private Tabla set_solicitante = new Tabla();
    private Tabla set_solicitu = new Tabla();
    private Tabla set_garante = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Cuadros para texto, busqueda reportes
    private Texto txt_cedula = new Texto();
    private Texto tex_busqueda = new Texto();
    private Texto tcomenta = new Texto();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogos = new Dialogo();
    private Dialogo dia_dialogoca = new Dialogo();
    private Dialogo dia_dialogoso = new Dialogo();
    private Dialogo dia_dialogosr = new Dialogo();
    private Dialogo dia_dialogoan = new Dialogo();
    private Dialogo dia_dialogoga = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid_ca = new Grid();
    private Grid grid_so = new Grid();
    private Grid grid_ds = new Grid();
    private Grid grid_an = new Grid();
    private Grid grid_ga = new Grid();
    private Grid grid = new Grid();
    private Grid grids = new Grid();
    private Grid gridso = new Grid();
    private Grid gridr = new Grid();
    private Grid gridan = new Grid();
    private Grid gridga= new Grid();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    //para seleciconar opcines para el reporte
    private Combo cmb_seleccion = new Combo();
    
    //Extrae datos adicionales, que se necesita, de una clase logica
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    private SolicAnticipos aAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_anticipo_sueldo() {
    
        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
        agregarComponente(quinde);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(pan_opcion);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setValue("Limpiar");
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-person");
        bot_limpiar.setMetodo("limpia_pa");
        bar_botones.agregarBoton(bot_limpiar);
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,aprobado_solicitante FROM srh_solicitud_anticipo");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_abort = new Boton();
        bot_abort.setValue("Anular Solictud");
        bot_abort.setIcon("ui-icon-closethick");
        bot_abort.setMetodo("Anular1");
        bar_botones.agregarBoton(bot_abort);
        
        Boton bot_des = new Boton();
        bot_des.setValue("Descargo");
        bot_des.setIcon("ui-icon-closethick");
        bot_des.setMetodo("confirma_cuotas");
        bar_botones.agregarBoton(bot_des);
        
        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        tex_busqueda.setSize(45);
        gri_busca.getChildren().add(tex_busqueda);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarSolicitud");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        set_solicitud.setId("set_solicitud");
        set_solicitud.getTab_seleccion().setConexion(con_postgres);
        set_solicitud.setSeleccionTabla("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo where ide_solicitud_anticipo=-1", "ide_solicitud_anticipo");
        set_solicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_solicitud.getTab_seleccion().setRows(10);
        set_solicitud.setRadio();
        set_solicitud.getGri_cuerpo().setHeader(gri_busca);
        set_solicitud.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_solicitud.setHeader("BUSCAR SOLICITUD POR CEDULA");
        agregarComponente(set_solicitud);

        //para poder buscar por apellido el solicitante
        dia_dialogos.setId("dia_dialogos");
        dia_dialogos.setTitle("BUSCAR SOLICITANTE"); //titulo
        dia_dialogos.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogos.setHeight("50%");//siempre porcentaje   alto
        dia_dialogos.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogos.getBot_aceptar().setMetodo("aceptoSolicitante");
        grid_ds.setColumns(4);
        agregarComponente(dia_dialogos);
        
        //para poder busca por apelllido el garante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("BUSCAR SERVIDOR"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("45%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoColaborador");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
        
        cmb_seleccion.setId("cmb_seleccion");
        List lista1 = new ArrayList();
        Object fila1[] = {
            "1", "CEDULA"
        };
        Object fila2[] = {
            "2", "APELLIDO"
        };
        Object fila3[] = {
            "3", "GARANTE"
        };
        lista1.add(fila1);;
        lista1.add(fila2);;
        lista1.add(fila3);;
        cmb_seleccion.setCombo(lista1);
        
        dia_dialogoca.setId("dia_dialogoca");
        dia_dialogoca.setTitle("SELECCIONAR BUSQUEDA PARA SOLICITUD"); //titulo
        dia_dialogoca.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoca.setHeight("15%");//siempre porcentaje   alto
        dia_dialogoca.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoca.getBot_aceptar().setMetodo("abrirBusqueda");
        grid_ca.setColumns(4);
        agregarComponente(dia_dialogoca);
        
        dia_dialogoso.setId("dia_dialogoso");
        dia_dialogoso.setTitle("BUSQUEDA DE SOLICITUD POR APELLIDO"); //titulo
        dia_dialogoso.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoso.setHeight("45%");//siempre porcentaje   alto
        dia_dialogoso.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoso.getBot_aceptar().setMetodo("aceptarBusqueda");
        grid_so.setColumns(4);
        agregarComponente(dia_dialogoso);
        
        dia_dialogosr.setId("dia_dialogosr");
        dia_dialogosr.setTitle("BUSQUEDA DE CEDULA"); //titulo
        dia_dialogosr.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogosr.setHeight("15%");//siempre porcentaje   alto
        dia_dialogosr.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogosr.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridr.setColumns(4);
        agregarComponente(dia_dialogosr);
        
        dia_dialogoan.setId("dia_dialogoan");
        dia_dialogoan.setTitle("MOTIVO DE ANULACIÓN"); //titulo
        dia_dialogoan.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogoan.setHeight("20%");//siempre porcentaje   alto
        dia_dialogoan.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoan.getBot_aceptar().setMetodo("Anular");
        grid_an.setColumns(4);
        agregarComponente(dia_dialogoan);
        
        dia_dialogoga.setId("dia_dialogoga");
        dia_dialogoga.setTitle("VERIFICAR GARANTE"); //titulo
        dia_dialogoga.setWidth("65%"); //siempre en porcentajes  ancho
        dia_dialogoga.setHeight("60%");//siempre porcentaje   alto
        dia_dialogoga.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoga.getBot_aceptar().setDisabled(true);
        grid_ga.setColumns(4);
        agregarComponente(dia_dialogoga);
        
        dibujarSolicitud();
        
         /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
//        crear_periodo();
        
    }

    public void confirma_cuota(){
        if(utilitario.getMes(utilitario.getFechaActual())!=1){
            
        }else{
            
        }
    }
    
    public void confirma_cuotas(){
        if(utilitario.getMes(utilitario.getFechaActual())!=1){
            
            iAnticipos.InsertarAnticipo(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf((utilitario.getMes(utilitario.getFechaActual())-1)));
            
            TablaGenerica tab_dato = iAnticipos.VerificarCuota(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf((utilitario.getMes(utilitario.getFechaActual())-1)));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Cuotas Modificadas Por No Coincidir con el Descuento", "");
                iAnticipos.setCuota(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
                iAnticipos.ProrrogacionCuota(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),  String.valueOf(utilitario.getMes(utilitario.getFechaActual())), String.valueOf((utilitario.getMes(utilitario.getFechaActual())-1)));
                
                if(tab_dato.getValor("his_cuotas_faltantes").endsWith("1")){
                    System.err.println("ing1");
                }else{
                    System.err.println("ing2");
                }
            }
        }else{
            
            iAnticipos.InsertarAnticipo(String.valueOf((utilitario.getAnio(utilitario.getFechaActual())-1)), String.valueOf((utilitario.getMes(utilitario.getFechaActual())+11)));
            
            TablaGenerica tab_dato = iAnticipos.VerificarCuota(String.valueOf((utilitario.getAnio(utilitario.getFechaActual())-1)), String.valueOf((utilitario.getMes(utilitario.getFechaActual())+11)));
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Cuotas Modificadas Por no Coincidir con el Descuento", "");
                iAnticipos.setCuota(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
                iAnticipos.ProrrogacionCuota(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),  String.valueOf(utilitario.getMes(utilitario.getFechaActual())), String.valueOf((utilitario.getMes(utilitario.getFechaActual())+11)));
                if(tab_dato.getValor("his_cuotas_faltantes").endsWith("1")){
                    System.err.println("ing11");
                }else{
                     System.err.println("ing22");
                }
            }
        }
    }

    
    
    //proceso automatico que permita llenar los detalles de anticipos
    public void descontar(){
            if(utilitario.getMes(utilitario.getFechaActual())!=1){
                iAnticipos.ActualizarDetalleAnticipo(utilitario.getAnio(utilitario.getFechaActual()),(utilitario.getMes(utilitario.getFechaActual())-1));
            }else{
                iAnticipos.ActualizarDetalleAnticipo((utilitario.getAnio(utilitario.getFechaActual())-1),(utilitario.getMes(utilitario.getFechaActual())+11));
            }
        actuPago();
    }
    
    public void actuPago(){
        Integer anio=0;
        if(utilitario.getMes(utilitario.getFechaActual())!=1){
            anio = utilitario.getAnio(utilitario.getFechaActual());
            iAnticipos.ActualizaAnticipo(String.valueOf(anio));
            verificaDetalle();
        }else{
            anio = utilitario.getAnio(utilitario.getFechaActual())-1;
            iAnticipos.ActualizaAnticipo(String.valueOf(anio));
            verificaDetalle();
        }
    }
    
    public void verificaDetalle(){
        iAnticipos.CamAnticipoF();
//        verificarSolicitud();
    }
    
    public void verificarSolicitud(){
        iAnticipos.CamAnticipoSo();
        utilitario.agregarMensaje("Cuotas Actualizadas", "");
    }
    //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujarSolicitud();
    }
    
    //permite limpiar el formulario sin guardar o mantener algun dato
    public void limpia_pa(){
        if(tab_anticipo.getValor("ide_solicitud_anticipo")!=null){
            utilitario.agregarMensaje("Limpia Formulario", "No Guardado");
        }else{
            eliminar();
            insertar();
        }
    }
    
    //Platilla para ingreso de datos
    public void dibujarSolicitud(){
        limpiarPanel();
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_solicitud_anticipo", "ide_solicitud_anticipo", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_anticipo.setCondicion("ide_solicitud_anticipo=-1");
        } else {
            tab_anticipo.setCondicion("ide_solicitud_anticipo=" + aut_busca.getValor());
        }
        //Metodos para buscar los datos a llenar en el formulario
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("solicitante").setMetodoChange("buscaSolicitante");
        
        tab_anticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_anticipo.getColumna("cod_banco").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_anticipo.getColumna("cod_cuenta").setCombo("SELECT cod_cuenta,nombre FROM ocecuentas");
        tab_anticipo.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tab_anticipo.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_anticipo.getColumna("cod_grupo").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        tab_anticipo.getColumna("ide_tipo_anticipo").setCombo("SELECT ide_tipo_anticipo,tipo FROM srh_tipo_anticipo");
        
        tab_anticipo.getColumna("login_ingre_solicitud").setValorDefecto(utilitario.getVariable("NICK"));
        tab_anticipo.getColumna("ip_ingre_solicitud").setValorDefecto(utilitario.getIp());
        tab_anticipo.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_anticipo.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        
        tab_anticipo.getColumna("login_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("IDE_EMPLEADO_SOLICITANTE").setVisible(false);
        tab_anticipo.getColumna("ip_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("login_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("ip_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("aprobado_solicitante").setVisible(false);
        tab_anticipo.getColumna("fecha_aprobacion").setVisible(false);
        tab_anticipo.getColumna("ide_listado").setVisible(false);
        tab_anticipo.getColumna("fecha_listado").setVisible(false);
        tab_anticipo.getColumna("anio").setVisible(false);
        tab_anticipo.getColumna("periodo").setVisible(false);
        tab_anticipo.getColumna("ide_tipo_ingreso_anticipo").setVisible(false);
        tab_anticipo.getColumna("usu_anulacion").setVisible(false);
        tab_anticipo.getColumna("comen_anulacion").setVisible(false);
        tab_anticipo.getColumna("fecha_anulacion").setVisible(false);
        
        tab_anticipo.setTipoFormulario(true);
        tab_anticipo.agregarRelacion(tab_garante);
        tab_anticipo.agregarRelacion(tab_parametros);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setMensajeWarn("DATOS DE SOLICITANTE");
        tpa.setPanelTabla(tab_anticipo);
        
        tab_garante.setId("tab_garante");
        tab_garante.setConexion(con_postgres);
        tab_garante.setTabla("srh_garante_anticipo", "ide_garante_anticipo", 2);
        
        //Metodos para buscar los datos a llenar en el formulario del garante
        tab_garante.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_garante.getColumna("garante").setMetodoChange("buscaColaborador");
        
        tab_garante.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_garante.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        
        tab_garante.getColumna("IDE_GARANTE_ANTICIPO ").setVisible(false);
        tab_garante.getColumna("IDE_EMPLEADO_GARANTE").setVisible(false);
        tab_garante.setTipoFormulario(true);
        tab_garante.getGrid().setColumns(8);
        tab_garante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setMensajeWarn("DATOS DE GARANTE");
        tpd.setPanelTabla(tab_garante);
        
        tab_parametros.setId("tab_parametros");
        tab_parametros.setConexion(con_postgres);
        tab_parametros.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 3);
        
        //Metodos para buscar los datos a llenar en el formulario del garante
        tab_parametros.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_parametros.getColumna("numero_cuotas_anticipo").setMetodoChange("porcentaje");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setMetodoChange("cuotas");
        
        tab_parametros.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        
        tab_parametros.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());
        
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLongitud(1);
        tab_parametros.getColumna("val_cuo_adi").setLongitud(1);
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
        tab_parametros.getColumna("val_cuo_adi").setLectura(true);
        tab_parametros.getColumna("IDE_CALCULO_ANTICIPO").setVisible(false);
        tab_parametros.getColumna("numero_cuotas_pagadas").setVisible(false);
        tab_parametros.getColumna("valor_pagado").setVisible(false);
        tab_parametros.getColumna("usu_anulacion").setVisible(false);
        tab_parametros.getColumna("usu_pago_anticipado").setVisible(false);
        tab_parametros.getColumna("fecha_pago_anticipado").setVisible(false);
        tab_parametros.getColumna("numero_documento_pago").setVisible(false);
        tab_parametros.getColumna("usu_cobra_liquidacion").setVisible(false);
        tab_parametros.getColumna("fecha_cobro_liquidacion").setVisible(false);
        tab_parametros.getColumna("comentario_cobro").setVisible(false);
        tab_parametros.getColumna("ide_empleado").setVisible(false);
        
        tab_parametros.setTipoFormulario(true);
        tab_parametros.getGrid().setColumns(6);
        tab_parametros.dibujar();
        
        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("DATOS DE ANTICIPO A SOLICITAR");
        tpp.setPanelTabla(tab_parametros);
        
        PanelTabla t = new PanelTabla();
        Division div= new Division();
        div.setId("div");
        div.dividir2(tpp,t, "63%", "H");
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tpa, tpd, div, "36%", "49%", "H");
        agregarComponente(div_division);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        pan_opcion.getChildren().add(gru);    
    }
    
    //BUSCAR SOLICITANTE POR CEDULA
    public void llenarDatosE(){//SOLICITANTE
        Integer anio=0,mes=0;
        TablaGenerica tab_dato = iAnticipos.VerifEmpleid(tab_anticipo.getValor("ci_solicitante"),Integer.parseInt(tab_anticipo.getValor("ide_tipo_anticipo")));
        if (!tab_dato.isEmpty()) {
            if(tab_dato.getValor("ide_tipo_anticipo").equals("1")){
                utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Ordinario Pendiente");
            }else{
                utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo ExtraOrdinario Pendiente");
            }
        }else {
            if(utilitario.getMes(utilitario.getFechaActual())!=1){
                anio = utilitario.getAnio(utilitario.getFechaActual());
                mes = utilitario.getMes(utilitario.getFechaActual())-1;
            }else{
                anio = utilitario.getAnio(utilitario.getFechaActual())-1;
                mes = 12;
            }
            if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
                TablaGenerica tab_dato1 = iAnticipos.empleadosCed(tab_anticipo.getValor("ci_solicitante"),anio,mes);//empleados
                if (!tab_dato1.isEmpty()) {
                    Double rmu =Double.valueOf(tab_dato1.getValor("liquido_recibir"));
                    if(rmu>1){
                        tab_anticipo.setValor("ide_empleado_solicitante", tab_dato1.getValor("COD_EMPLEADO"));
                        tab_anticipo.setValor("ci_solicitante", tab_dato1.getValor("cedula_pass"));
                        tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                        tab_anticipo.setValor("rmu", tab_dato1.getValor("ru"));
                        tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                        tab_anticipo.setValor("id_distributivo", tab_dato1.getValor("id_distributivo_roles"));
                        tab_anticipo.setValor("cod_cargo", tab_dato1.getValor("cod_cargo"));
                        tab_anticipo.setValor("cod_grupo", tab_dato1.getValor("cod_grupo"));
                        tab_anticipo.setValor("cod_tipo", tab_dato1.getValor("cod_tipo"));
                        tab_anticipo.setValor("cod_banco", tab_dato1.getValor("cod_banco"));
                        tab_anticipo.setValor("cod_cuenta", tab_dato1.getValor("cod_cuenta"));
                        tab_anticipo.setValor("numero_cuenta", tab_dato1.getValor("numero_cuenta"));
                        utilitario.addUpdate("tab_anticipo");
                        tab_parametros.setValor("ide_empleado", tab_dato1.getValor("COD_EMPLEADO"));
                        utilitario.addUpdate("tab_parametros");
                    }else{
                        utilitario.agregarNotificacionInfo("SU REMUNERACION ANTERIOR NO LE PERMITE REALIZAR ANTICIPO",tab_dato1.getValor("liquido_recibir"));
                    }
                }else {
                    TablaGenerica tab_dato2 = iAnticipos.trabajadoresCed(tab_anticipo.getValor("ci_solicitante"),anio,mes);//trabajadores
                    if (!tab_dato2.isEmpty()) {
                        Double rmut =Double.valueOf(tab_dato2.getValor("liquido_recibir"));
                        if(rmut>1){
                            tab_anticipo.setValor("ide_empleado_solicitante", tab_dato2.getValor("COD_EMPLEADO"));
                            tab_anticipo.setValor("ci_solicitante", tab_dato2.getValor("cedula_pass"));
                            tab_anticipo.setValor("solicitante", tab_dato2.getValor("nombres"));
                            tab_anticipo.setValor("rmu", tab_dato2.getValor("su"));
                            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato2.getValor("liquido_recibir"));
                            tab_anticipo.setValor("id_distributivo", tab_dato2.getValor("id_distributivo_roles"));
                            tab_anticipo.setValor("cod_cargo", tab_dato2.getValor("cod_cargo"));
                            tab_anticipo.setValor("cod_grupo", tab_dato2.getValor("cod_grupo"));
                            tab_anticipo.setValor("cod_tipo", tab_dato2.getValor("cod_tipo"));
                            tab_anticipo.setValor("cod_banco", tab_dato2.getValor("cod_banco"));
                            tab_anticipo.setValor("cod_cuenta", tab_dato2.getValor("cod_cuenta"));
                            tab_anticipo.setValor("numero_cuenta", tab_dato2.getValor("numero_cuenta"));
                            utilitario.addUpdate("tab_anticipo");
                            tab_parametros.setValor("ide_empleado", tab_dato1.getValor("COD_EMPLEADO"));
                            utilitario.addUpdate("tab_parametros");
                        }else{
                            utilitario.agregarNotificacionInfo("SU REMUNERACION ANTERIOR NO LE PERMITE REALIZAR ANTICIPO",tab_dato2.getValor("liquido_recibir"));
                        }
                    }else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            } else {
                utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
            }
            
        }
    }
 
    //BUSCAR SOLICITANTE POR APELLIDO Y NOMBRES
    public void buscaSolicitante(){
        dia_dialogos.Limpiar();
        dia_dialogos.setDialogo(grids);
        grid_ds.getChildren().add(set_solicitante);
        set_solicitante.setId("set_solicitante");
        set_solicitante.setConexion(con_postgres);
        set_solicitante.setHeader("LISTA DE SOLICITANTES");
        set_solicitante.setSql("SELECT cod_empleado,cedula_pass,nombres,id_distributivo,cod_tipo\n" +
                                "FROM srh_empleado WHERE estado = 1 and nombres LIKE '%"+tab_anticipo.getValor("solicitante")+"%'");
        set_solicitante.getColumna("nombres").setFiltro(true);
        set_solicitante.setRows(10);
        set_solicitante.setTipoSeleccion(false);
        dia_dialogos.setDialogo(grid_ds);
        set_solicitante.dibujar();
        dia_dialogos.dibujar();
    }
    
    public void aceptoSolicitante(){
        Integer anio=0,mes=0;
        if (set_solicitante.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = iAnticipos.VerifEmpleCod(Integer.parseInt(set_solicitante.getValorSeleccionado()),Integer.parseInt(tab_anticipo.getValor("ide_tipo_anticipo")));
            if (!tab_dato.isEmpty()) {
                if(tab_dato.getValor("ide_tipo_anticipo").equals("1")){
                    utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Ordinario Pendiente");
                }else{
                    utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo ExtraOrdinario Pendiente");
                }
            }else {
                if(utilitario.getMes(utilitario.getFechaActual())!=1){
                    anio = utilitario.getAnio(utilitario.getFechaActual());
                    mes = utilitario.getMes(utilitario.getFechaActual())-1;
                }else{
                    anio = utilitario.getAnio(utilitario.getFechaActual())-1;
                    mes = 12;
                }
                TablaGenerica tab_dato1 = iAnticipos.empleados(Integer.parseInt(set_solicitante.getValorSeleccionado()),anio,mes);//empleados
                if (!tab_dato1.isEmpty()) {
                    if (!tab_dato1.isEmpty()) {
                        double rmu =0;
                        rmu=Double.valueOf(tab_dato1.getValor("liquido_recibir"));
                        if(rmu>1){
                        tab_anticipo.setValor("ide_empleado_solicitante", tab_dato1.getValor("COD_EMPLEADO"));
                        tab_anticipo.setValor("ci_solicitante", tab_dato1.getValor("cedula_pass"));
                        tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                        tab_anticipo.setValor("rmu", tab_dato1.getValor("ru"));
                        tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                        tab_anticipo.setValor("id_distributivo", tab_dato1.getValor("id_distributivo_roles"));
                        tab_anticipo.setValor("cod_cargo", tab_dato1.getValor("cod_cargo"));
                        tab_anticipo.setValor("cod_grupo", tab_dato1.getValor("cod_grupo"));
                        tab_anticipo.setValor("cod_tipo", tab_dato1.getValor("cod_tipo"));
                        tab_anticipo.setValor("cod_banco", tab_dato1.getValor("cod_banco"));
                        tab_anticipo.setValor("cod_cuenta", tab_dato1.getValor("cod_cuenta"));
                        tab_anticipo.setValor("numero_cuenta", tab_dato1.getValor("numero_cuenta"));
                        utilitario.addUpdate("tab_anticipo");
                        tab_parametros.setValor("ide_empleado", tab_dato1.getValor("COD_EMPLEADO"));
                        utilitario.addUpdate("tab_parametros");
                        dia_dialogos.cerrar();
                        }else{
                            utilitario.agregarNotificacionInfo("SU REMUNERACION ANTERIOR NO LE PERMITE REALIZAR ANTICIPO",tab_dato1.getValor("liquido_recibir"));
                        }
                                }
                }else {
                    TablaGenerica tab_dato2 = iAnticipos.trabajadores(Integer.parseInt(set_solicitante.getValorSeleccionado()),anio,mes);//trabajadores
                    if (!tab_dato2.isEmpty()) {
                        double rmut = 0;
                        rmut=Double.valueOf(tab_dato2.getValor("liquido_recibir"));
                        if(rmut>1){
                            tab_anticipo.setValor("ide_empleado_solicitante", tab_dato2.getValor("COD_EMPLEADO"));
                            tab_anticipo.setValor("ci_solicitante", tab_dato2.getValor("cedula_pass"));
                            tab_anticipo.setValor("solicitante", tab_dato2.getValor("nombres"));
                            tab_anticipo.setValor("rmu", tab_dato2.getValor("su"));
                            tab_anticipo.setValor("rmu_liquido_anterior", tab_dato2.getValor("liquido_recibir"));
                            tab_anticipo.setValor("id_distributivo", tab_dato2.getValor("id_distributivo_roles"));
                            tab_anticipo.setValor("cod_cargo", tab_dato2.getValor("cod_cargo"));
                            tab_anticipo.setValor("cod_grupo", tab_dato2.getValor("cod_grupo"));
                            tab_anticipo.setValor("cod_tipo", tab_dato2.getValor("cod_tipo"));
                            tab_anticipo.setValor("cod_banco", tab_dato2.getValor("cod_banco"));
                            tab_anticipo.setValor("cod_cuenta", tab_dato2.getValor("cod_cuenta"));
                            tab_anticipo.setValor("numero_cuenta", tab_dato2.getValor("numero_cuenta"));
                            utilitario.addUpdate("tab_anticipo");
                            tab_parametros.setValor("ide_empleado", tab_dato1.getValor("COD_EMPLEADO"));
                            utilitario.addUpdate("tab_parametros");
                            dia_dialogos.cerrar();
                        }else{
                            utilitario.agregarNotificacionInfo("SU REMUNERACION ANTERIOR NO LE PERMITE REALIZAR ANTICIPO",tab_dato2.getValor("liquido_recibir"));}
                    }else {
                        utilitario.agregarMensajeInfo("No existen Datos", "");
                    }
                }
            }
        }else {
            utilitario.agregarMensajeInfo("Seleccionar registro ", "");
        }
    }
    
    // BUSCAR GARANTE POR CEDULA
    public void llenarGarante(){
        TablaGenerica tab_dato = iAnticipos.VerifGaranteid(tab_garante.getValor("ci_garante"));
        if (!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Garante No Disponible", "Se Encuentra En Otra Solicitud...");
        }else {
            if (utilitario.validarCedula(tab_garante.getValor("ci_garante"))) {
                TablaGenerica tab_dato1 = iAnticipos.Garantemple(tab_garante.getValor("ci_garante"));
                if (!tab_dato1.isEmpty()) {
                    tab_garante.setValor("garante", tab_dato1.getValor("nombres"));
                    tab_garante.setValor("ide_empleado_garante", tab_dato1.getValor("cod_empleado"));
                    tab_garante.setValor("cod_tipo", tab_dato1.getValor("cod_tipo"));
                    tab_garante.setValor("id_distributivo", tab_dato1.getValor("id_distributivo"));
                    utilitario.addUpdate("tab_garante");
                }else {
                    TablaGenerica tab_dato2 = iAnticipos.GarNumConat(Integer.parseInt(tab_dato1.getValor("cod_empleado")));
                    if (!tab_dato2.isEmpty()) {
                    tab_garante.setValor("garante", tab_dato1.getValor("nombres"));
                    tab_garante.setValor("ide_empleado_garante", tab_dato2.getValor("cod_empleado"));
                    tab_garante.setValor("cod_tipo", tab_dato1.getValor("cod_tipo"));
                    tab_garante.setValor("id_distributivo", tab_dato1.getValor("id_distributivo"));
                    utilitario.addUpdate("tab_garante");
                    }else {
                    utilitario.agregarMensajeInfo("Garante No Cumple Requisitos","");
                    }
                }    
            } else {
                utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
            }
        }
    }
    
     //BUSQUEDA POR NOMBRE DE GARANTE
    public void buscaColaborador(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_d.getChildren().add(set_colaborador);
        set_colaborador.setId("set_colaborador");
        set_colaborador.setConexion(con_postgres);
        set_colaborador.setHeader("LISTA DE COLABORADORES");
        set_colaborador.setSql("SELECT cod_empleado,cedula_pass,nombres,id_distributivo,cod_tipo\n" +
                                "FROM srh_empleado WHERE estado = 1 AND cod_tipo IN (4,7,8) and nombres LIKE '%"+tab_garante.getValor("garante")+"%'");
        set_colaborador.getColumna("nombres").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogo.setDialogo(grid_d);
        set_colaborador.dibujar();
        dia_dialogo.dibujar();
    }
    //Busquedad de garante por apellido
    public void aceptoColaborador(){
        if (set_colaborador.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato1 = iAnticipos.VerifGaranteCod(Integer.parseInt(set_colaborador.getValorSeleccionado()));
            if (!tab_dato1.isEmpty()) {
                utilitario.agregarMensajeInfo("Garante, No Se Encuentra Disponible", "");
            }else {
                TablaGenerica tab_dato = iAnticipos.GaranteNom(Integer.parseInt(set_colaborador.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                    tab_garante.setValor("ide_empleado_garante", tab_dato.getValor("cod_empleado"));
                    tab_garante.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
                    tab_garante.setValor("garante", tab_dato.getValor("nombres"));
                    tab_garante.setValor("id_distributivo", tab_dato.getValor("id_distributivo"));
                    tab_garante.setValor("cod_tipo", tab_dato.getValor("cod_tipo"));
                    utilitario.addUpdate("tab_garante");
                    dia_dialogo.cerrar();
                } else {
                    TablaGenerica tab_dato2 = iAnticipos.GarNumConat(Integer.parseInt(tab_dato.getValor("cod_empleado")));
                    if (!tab_dato2.isEmpty()) {
                    tab_garante.setValor("garante", tab_dato.getValor("nombres"));
                    tab_garante.setValor("ide_empleado_garante", tab_dato2.getValor("cod_empleado"));
                    tab_garante.setValor("cod_tipo", tab_dato.getValor("cod_tipo"));
                    tab_garante.setValor("id_distributivo", tab_dato.getValor("id_distributivo"));
                    utilitario.addUpdate("tab_garante");
                    }else {
                    utilitario.agregarMensajeInfo("Garante No Cumple Requisitos","");
                    }
                }
            }
        }else {
            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
        }
    }
    
    //CALCULO DE CUOTAS, VERIFICACION DE MONTOS Y PLAZOS PARA ANTICIPO DE SUELDO
    //VALIDACION DE VALOR A PERCIBIR EN EL ANTICIPO DE ACUERDO A REMUNERACION LIQUIDA ANTERIOR PERCIBIDA
    public void remuneracion(){
        double  dato1 = 0,dato2=0,dato3=0; 
        dato2 = Double.parseDouble(tab_anticipo.getValor("rmu"));
        dato1 = Double.parseDouble(tab_parametros.getValor("valor_anticipo"));
        dato3 = (Double.parseDouble(tab_anticipo.getValor("rmu"))/2);
        if(Integer.parseInt(tab_anticipo.getValor("ide_tipo_anticipo"))!=1){
            if((dato1/dato2)<=1){
                tab_parametros.setValor("numero_cuotas_anticipo", "2");
                utilitario.addUpdate("tab_parametros");
                utilitario.agregarMensajeInfo("Anticipo, Hasta una Remuneracion", "Plazo Maximo de Cobro, 2 Meses");
                if(tab_parametros.getValor("numero_cuotas_anticipo").equals("2")){
                    llenarFecha();
                    cuotas();
                }
                tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(true);
            }else if((dato1/dato2)>1&&(dato1/dato2)<=3){//HASTA 3 REMUNERACIONES 
                tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(false);
                tab_parametros.setValor("numero_cuotas_anticipo", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tab_parametros.setValor("valor_cuota_mensual", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tab_parametros");
                utilitario.agregarMensaje("Ingrese Cuotas Para Cobro", "Por Mes");
            }else{
                utilitario.agregarMensajeInfo("Monto Excede Remuneración", "");
                tab_parametros.setValor("valor_cuota_mensual", "NULL");
                tab_parametros.setValor("numero_cuotas_anticipo", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tab_parametros.setValor("valor_cuota_mensual", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tab_parametros");
            }
        }else{
            if(Double.parseDouble(tab_parametros.getValor("valor_anticipo"))<=dato3){
                tab_parametros.setValor("numero_cuotas_anticipo", "1");
                utilitario.addUpdate("tab_parametros");
                utilitario.agregarMensajeInfo("Anticipo Ordinario", "Plazo Maximo de Cobro, 1 Meses");
                llenarFecha();
                cuotas();
                tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(true);
            }else{
                utilitario.agregarMensaje("Monto Maximo en Anticipo Ordinario", "50% De la Remuneración");
                tab_parametros.setValor("numero_cuotas_anticipo", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tab_parametros");
            }
        }
    }
    
    //Verifica si se debe ingresar, el 70% para cobro
    public void porcentaje(){
        Integer porcentaje =0;
        if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
            if(tab_anticipo.getValor("id_distributivo").equals("1")){
                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                if(porcentaje == 12){
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }else if(porcentaje > 12){
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }else{
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }
            }else{
                tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tab_parametros.setValor("valor_cuota_mensual", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tab_parametros");
                servidor();
            }
        }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28
            if(tab_anticipo.getValor("id_distributivo").equals("1")){
                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                if(porcentaje == 12){
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }else if(porcentaje > 12){
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }else {
                    tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("val_cuo_adi", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    servidor();
                }
            }else{
                tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                tab_parametros.setValor("valor_cuota_mensual", "NULL");
                tab_parametros.setValor("val_cuo_adi", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                utilitario.addUpdate("tab_parametros");
                servidor();
            }
        }
    }
  
    /*VALIDACION DE TIMEPO DE COBRO QUE SE SOLICITA PARA EL ANTICIPO*/    
    public void servidor(){
        Integer anos=0, dias=0,meses=0,mesesf=0,aniosf=0,diasf=0,meses_a=0,anios_a=0,dias_a=0,anof=0,diaf=0,mesf=0;
        anios_a = utilitario.getAnio(utilitario.getFechaActual());
        meses_a = utilitario.getMes(utilitario.getFechaActual());
        dias_a = utilitario.getDia(utilitario.getFechaActual());
        if(tab_anticipo.getValor("id_distributivo").equals("1")){
            if(tab_anticipo.getValor("cod_tipo").equals("4") ||tab_anticipo.getValor("cod_tipo").equals("8")||tab_anticipo.getValor("cod_tipo").equals("3")){
                if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                        Integer porcentaje =0;  
                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                        if(porcentaje == 12){
                            llenarFecha();
                            cuotas();
                        }else if(porcentaje > 12){
                            llenarFecha();
                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                        }else{
                            llenarFecha();
                            cuotas();
                        }
                    }else{
                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                    }
                }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28  
                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                        Integer porcentaje =0;  
                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                        if(porcentaje == 12){
                            llenarFecha();
                            cuotas();
                        }else if(porcentaje > 12){
                            llenarFecha();
                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                        }else{
                            llenarFecha();
                            cuotas();
                        }
                    }else{
                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                    }
                }
            }else{
                if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                    TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                    if (!tab_datof.isEmpty()) {
                        anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                        dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                        meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                        if(tab_datof.getValor("fecha_fin")!=null){
                            anof=utilitario.getAnio(tab_datof.getValor("fecha_fin"));
                        diaf=utilitario.getDia(tab_datof.getValor("fecha_fin"));
                        mesf=utilitario.getMes(tab_datof.getValor("fecha_fin"));
                        
                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                if(porcentaje == 12){
                                    llenarFecha();
//                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                        }else{//
                            if(mesesf!=1){//si el ingreso es diferente de enero
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        Integer porcentaje =0;  
                                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                        if(porcentaje == 12){
                                            llenarFecha();
                                            cuotas();
                                        }else if(porcentaje > 12){
                                            llenarFecha();
                                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                        }else{
                                            llenarFecha();
                                            cuotas();
                                        }
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));  //-1
                                }
                            }else{
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        Integer porcentaje =0;  
                                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                        if(porcentaje == 12){
                                            llenarFecha();
                                            cuotas();
                                        }else if(porcentaje > 12){
                                            llenarFecha();
                                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                        }else{
                                            llenarFecha();
                                            cuotas();
                                        }
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf)))); //-1 
                                } 
                            }
                        }
                        }else{
                            if(tab_anticipo.getValor("cod_tipo").equals("4") ||tab_anticipo.getValor("cod_tipo").equals("8")||tab_anticipo.getValor("cod_tipo").equals("3")){
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                if(porcentaje == 12){
                                    llenarFecha();
                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                            }else{
                                utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                            }
                        }
                    }else {
                        utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                    }
                }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28  
                    TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                    if (!tab_datof.isEmpty()) {
                        anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                        dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                        meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                        if(tab_datof.getValor("fecha_fin")!=null){
                        anof=utilitario.getAnio(tab_datof.getValor("fecha_fin"));
                        diaf=utilitario.getDia(tab_datof.getValor("fecha_fin"));
                        mesf=utilitario.getMes(tab_datof.getValor("fecha_fin"));
                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                if(porcentaje == 12){
                                    llenarFecha();
                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                        }else{
                            mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                            aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                            diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                            if(mesesf!=1){//si el ingreso es diferente de enero
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        Integer porcentaje =0;  
                                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                        if(porcentaje == 12){
                                            llenarFecha();
                                            cuotas();
                                        }else if(porcentaje > 12){
                                            llenarFecha();
                                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                        }else{
                                            llenarFecha();
                                            cuotas();
                                        }
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(anof,mesf,diaf))));  //-1
                                }
                            }else{
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        Integer porcentaje =0;  
                                        porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                        if(porcentaje == 12){
                                            llenarFecha();
                                            cuotas();
                                        }else if(porcentaje > 12){
                                            llenarFecha();
                                            utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                        }else{
                                            llenarFecha();
                                            cuotas();
                                        }
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(anof,mesf,diaf))));  //1
                                } 
                            }
                        }
                        }else{
                                if(tab_anticipo.getValor("cod_tipo").equals("4") ||tab_anticipo.getValor("cod_tipo").equals("8")||tab_anticipo.getValor("cod_tipo").equals("3")){
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                if(porcentaje == 12){
                                    llenarFecha();
                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                            }else{
                                utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                            }
                        }
                    }else {
                        utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                    }
                }
            }
        }else if(tab_anticipo.getValor("id_distributivo").equals("2")){
            if(tab_anticipo.getValor("cod_tipo").equals("7")||tab_anticipo.getValor("cod_tipo").equals("10")){
                if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                        llenarFecha();
                        cuotas();
                    }else{
                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                    }
                }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28  
                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                        llenarFecha();
                        cuotas();
                    }else{
                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                    }
                }
            }else{
                if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                    TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                    if (!tab_datof.isEmpty()) {
                        anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                        dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                        meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                       if(tab_datof.getValor("fecha_fin")!=null){
                        anof=utilitario.getAnio(tab_datof.getValor("fecha_fin"));
                        diaf=utilitario.getDia(tab_datof.getValor("fecha_fin"));
                        mesf=utilitario.getMes(tab_datof.getValor("fecha_fin"));
                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                llenarFecha();
                                cuotas();
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                        }else{//
                            mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                            aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                            diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"))-1;
                            
                            if(mesesf!=1){//si el ingreso es diferente de enero
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        llenarFecha();
                                        cuotas();
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));  //-1
                                }
                            }else{
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){//-1
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        llenarFecha();
                                        cuotas();
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));//-1
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));  //-1
                                } 
                            }
                        }
                    }else{
                                if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                if(porcentaje == 12){
                                    llenarFecha();
                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    cuotas();
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                            }else{
                                utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                            }
                        }
                    }else {
                        utilitario.agregarMensajeError("Falta de Ingreso","Movimientos de Empleadoas");
                    }
                }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28  
                    TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
                    if (!tab_datof.isEmpty()) {
                        anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                        dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                        meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                        if(tab_datof.getValor("fecha_fin")!=null){
                        anof=utilitario.getAnio(tab_datof.getValor("fecha_fin"));
                        diaf=utilitario.getDia(tab_datof.getValor("fecha_fin"));
                        mesf=utilitario.getMes(tab_datof.getValor("fecha_fin"));
                        if(calcularAnios(new GregorianCalendar(anos,meses,dias))>=1){
                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                llenarFecha();
                                cuotas();
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                        }else{//
                            mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                            aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                            diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                            if(mesesf!=1){//si el ingreso es diferente de enero
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        llenarFecha();
                                        cuotas();
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));  
                                }
                            }else{
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))){
                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                        llenarFecha();
                                        cuotas();
                                    }else{
                                        utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                                    }
                                }else{
//                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));
                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(anof,mesf,diaf))));  
                                } 
                            }
                        }
                    }else{
                            if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>=1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=12){
                                Integer porcentaje =0;  
                                porcentaje= utilitario.getMes(utilitario.getFechaActual())+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                                if(porcentaje == 12){
                                    llenarFecha();
                                    cuotas();
                                }else if(porcentaje > 12){
                                    llenarFecha();
                                    utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                                }else{
                                    llenarFecha();
                                    cuotas();
                                }
                            }else{
                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "12 MESES");
                            }
                            }else{
                                utilitario.agregarMensajeError("Falta de Información","Movimientos de Empleadoas");
                            }
                        }
                    }else {
                        utilitario.agregarMensajeError("Falta de Ingreso","Movimientos de Empleadoas");
                    }
                }
            }
        }
    }
   
    //VALIDACION DE FECHAS
    public void llenarFecha(){

        Integer calculo=0,calculo1=0,calculo2=0,calculo3=0,mess=0;
        String mes,anio,dia,fecha,busca;
        
        anio = String.valueOf(utilitario.getAnio(tab_parametros.getValor("FECHA_ANTICIPO")));
        mes = String.valueOf(utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO")));
        dia = String.valueOf(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO")));
        mess = utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO"));
        if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){
             if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))==12){
                calculo = 12 - Integer.parseInt(mes);
                calculo1 = calculo - Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                if(calculo1<0){
                    TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                         if (!tab_dato.isEmpty()) {
                                       //fecha inicial
                                       fecha= tab_dato.getValor("ide_periodo_anticipo");
                                       tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_parametros");
                                        //fecha final
                                        if(mes.equals("1")){//mes de enero
                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                        }else{//otros meses
                                              TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes)-1),String.valueOf(Integer.parseInt(anio)+1));
                                                if (!tab_dato2.isEmpty()) {
                                                    fecha= tab_dato2.getValor("ide_periodo_anticipo");
                                                    tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                    utilitario.addUpdate("tab_parametros");
                                                } else {
                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                        }
                                        }
                             }else {
                                   utilitario.agregarMensajeInfo("No existen Datos", "");
                                   }
                }
            }else if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<12){
                        Integer cuota =0; 
                        calculo = 12 - Integer.parseInt(mes);
                        calculo1 = calculo - Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                        if(calculo1<0){
                              TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                if (!tab_dato.isEmpty()) {
                                        //fecha inicial
                                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                                        tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_parametros");
                                        //fecha final
                                        calculo2 = calculo1*-1;
                                        cuota = Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                        
                                        if(mes.equals("2")){//para febrero
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                        }else if(mes.equals("3")){
                                            if(cuota == 11){
                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                    if (!tab_datoa.isEmpty()) {
                                                        fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                            }else{
                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                            }
                                            }else if(mes.equals("4")){
                                                if(cuota == 11||cuota == 10){
                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                    if (!tab_datoa.isEmpty()) {
                                                        fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                                }else{
                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                    if (!tab_dato1.isEmpty()) {
                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                        utilitario.addUpdate("tab_parametros");
                                                    } else {
                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                            }
                                                }
                                                }else if(mes.equals("5")){
                                                    if(cuota == 11||cuota == 10||cuota == 9){
                                                        TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                        if (!tab_datoa.isEmpty()) {
                                                            fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                            tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                            utilitario.addUpdate("tab_parametros");
                                                        } else {
                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                }
                                                    }else{
                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                        if (!tab_dato1.isEmpty()) {
                                                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                            tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                            utilitario.addUpdate("tab_parametros");
                                                        } else {
                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                }
                                                    }
                                                    }else if(mes.equals("6")){
                                                        if(cuota == 11||cuota == 10||cuota == 9||cuota == 8){
                                                            TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                            if (!tab_datoa.isEmpty()) {
                                                                fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                utilitario.addUpdate("tab_parametros");
                                                            } else {
                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                    }
                                                        }else{
                                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                            if (!tab_dato1.isEmpty()) {
                                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                utilitario.addUpdate("tab_parametros");
                                                            } else {
                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                    }
                                                        }
                                                        }else if(mes.equals("7")){
                                                            if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7){
                                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                          if (!tab_datoa.isEmpty()) {
                                                                              fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                              tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                              utilitario.addUpdate("tab_parametros");
                                                                          } else {
                                                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                  }
                                                            }else{
                                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                if (!tab_dato1.isEmpty()) {
                                                                    fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                    tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                    utilitario.addUpdate("tab_parametros");
                                                                } else {
                                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                        }
                                                            }
                                                            }else if(mes.equals("8")){
                                                                if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6){
                                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                              if (!tab_datoa.isEmpty()) {
                                                                                  fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                  utilitario.addUpdate("tab_parametros");
                                                                              } else {
                                                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                      }
                                                                }else{
                                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                    if (!tab_dato1.isEmpty()) {
                                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                        utilitario.addUpdate("tab_parametros");
                                                                    } else {
                                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                            }
                                                                }
                                                                }else if(mes.equals("9")){
                                                                    if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5){
                                                                        TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                  if (!tab_datoa.isEmpty()) {
                                                                                      fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                      tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                      utilitario.addUpdate("tab_parametros");
                                                                                  } else {
                                                                                          utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                          }
                                                                    }else{
                                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                        if (!tab_dato1.isEmpty()) {
                                                                            fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                            tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                            utilitario.addUpdate("tab_parametros");
                                                                        } else {
                                                                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                }
                                                                    }
                                                                    }else if(mes.equals("10")){
                                                                        if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4){
                                                                            TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                      if (!tab_datoa.isEmpty()) {
                                                                                          fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                          tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                          utilitario.addUpdate("tab_parametros");
                                                                                      } else {
                                                                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                              }
                                                                        }else{
                                                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                            if (!tab_dato1.isEmpty()) {
                                                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                utilitario.addUpdate("tab_parametros");
                                                                            } else {
                                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                    }
                                                                        }
                                                                        }else if(mes.equals("11")){
                                                                            if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4||cuota == 3){
                                                                                TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                          if (!tab_datoa.isEmpty()) {
                                                                                              fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                              tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                              utilitario.addUpdate("tab_parametros");
                                                                                          } else {
                                                                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                                  }
                                                                            }else{
                                                                                TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                                if (!tab_dato1.isEmpty()) {
                                                                                    fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                    tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                    utilitario.addUpdate("tab_parametros");
                                                                                } else {
                                                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                        }
                                                                            }
                                                                            }else if(mes.equals("12")){
                                                                                if(cuota == 11||cuota == 10||cuota == 9||cuota == 8||cuota == 7||cuota == 6||cuota == 5||cuota == 4||cuota == 1){
                                                                                    TablaGenerica tab_datoa = iAnticipos.periodos(meses(calculo2-1),String.valueOf(Integer.parseInt(anio)+1));
                                                                                              if (!tab_datoa.isEmpty()) {
                                                                                                  fecha= tab_datoa.getValor("ide_periodo_anticipo");
                                                                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                                  utilitario.addUpdate("tab_parametros");
                                                                                              } else {
                                                                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                                      }
                                                                                }else{
                                                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                                                                    if (!tab_dato1.isEmpty()) {
                                                                                        fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                                        tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                                        utilitario.addUpdate("tab_parametros");
                                                                                    } else {
                                                                                            utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                                            }
                                                                                }
                                                                            }
                                    }else {
                                          utilitario.agregarMensajeInfo("No existen Datos", "");
                                          }
                        }else if(calculo1>0){
                                 TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                    if (!tab_dato.isEmpty()) {
                                        if(Integer.parseInt(tab_anticipo.getValor("ide_tipo_anticipo"))!=1){
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato1.isEmpty()) {
                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                  utilitario.addUpdate("tab_parametros");
                                                } else {
                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                       }
                                    }else{
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato1.isEmpty()) {
                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                  utilitario.addUpdate("tab_parametros");
                                                } else {
                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                       }
                                        }
                                        }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }
                        }else  if(calculo1==0){
                                TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                     if (!tab_dato.isEmpty()) {
                                         if(Integer.parseInt(tab_anticipo.getValor("ide_tipo_anticipo"))!=1){
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_parametros");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato1.isEmpty()) {
                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                  utilitario.addUpdate("tab_parametros");
                                                } else {
                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                       }
                                     }else{
                                            //fecha inicial
                                            fecha= tab_dato.getValor("ide_periodo_anticipo");
                                            tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                            utilitario.addUpdate("tab_anticipo");
                                            //fecha final
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-1),String.valueOf(Integer.parseInt(anio)));
                                             if (!tab_dato1.isEmpty()) {
                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                  utilitario.addUpdate("tab_parametros");
                                                } else {
                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                       }
                                        }
                                         }else {
                                               utilitario.agregarMensajeInfo("No existen Datos", "");
                                               }
                                }
                    }
            }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31 ){
                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))==12){
                        calculo = 12 - Integer.parseInt(mes);
                        calculo1 = calculo - Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                        if(calculo1<0){
                            TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                 if (!tab_dato.isEmpty()) {
                                      //fecha inicial
                                        fecha= tab_dato.getValor("ide_periodo_anticipo");
                                        tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                        utilitario.addUpdate("tab_parametros");
                                        //fecha final
                                              TablaGenerica tab_dato2 = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)+1));
                                                if (!tab_dato2.isEmpty()) {
                                                    fecha= tab_dato2.getValor("ide_periodo_anticipo");
                                                    tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                    utilitario.addUpdate("tab_parametros");
                                                } else {
                                                        utilitario.agregarMensajeInfo("No existen Datos", "");
                                                        }
                                     }else {
                                           utilitario.agregarMensajeInfo("No existen Datos", "");
                                           }
                        }
                    }else if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<12){
                                Integer cuota =0;
                                calculo = 12 - Integer.parseInt(mes);
                                calculo1 = calculo - Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                if(calculo1<0){
                                      TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                        if (!tab_dato.isEmpty()) {
                                             //fecha inicial
                                                fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                                utilitario.addUpdate("tab_parametros");
                                                //fecha final
                                                calculo2 = calculo1*-1;
                                                cuota = Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                                                       TablaGenerica tab_dato1 = iAnticipos.periodos(meses(calculo2),String.valueOf(Integer.parseInt(anio)+1));
                                                            if (!tab_dato1.isEmpty()) {
                                                                fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                utilitario.addUpdate("tab_parametros");
                                                            } else {
                                                                    utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                    }
                                            }else {
                                                  utilitario.agregarMensajeInfo("No existen Datos", "");
                                                  }
                                }else if(calculo1>0){
                                         TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                            if (!tab_dato.isEmpty()) {
                                                 //fecha inicial
                                                    fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                    tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                    //fecha final
                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess),String.valueOf(Integer.parseInt(anio)));
                                                             if (!tab_dato1.isEmpty()) {
                                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                  utilitario.addUpdate("tab_parametros");
                                                                } else {
                                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                       }
                                                }else {
                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                      }
                                }else  if(calculo1==0){
                                         TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)+1),String.valueOf(Integer.parseInt(anio)));
                                            if (!tab_dato.isEmpty()) {
                                                 //fecha inicial
                                                    fecha= tab_dato.getValor("ide_periodo_anticipo");
                                                    tab_parametros.setValor("ide_periodo_anticipo_inicial", fecha);
                                                    utilitario.addUpdate("tab_anticipo");
                                                    //fecha final
                                                        TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess),String.valueOf(Integer.parseInt(anio)));
                                                             if (!tab_dato1.isEmpty()) {
                                                                  fecha= tab_dato1.getValor("ide_periodo_anticipo");
                                                                  tab_parametros.setValor("ide_periodo_anticipo_final", fecha);
                                                                  utilitario.addUpdate("tab_parametros");
                                                                } else {
                                                                       utilitario.agregarMensajeInfo("No existen Datos", "");
                                                                       }
                                                }else {
                                                      utilitario.agregarMensajeInfo("No existen Datos", "");
                                                      }
                                        }
                            }
                    }
    }
    
    //CALCULO DE CUOTAS Y CUOTA ESPECIAL
    public void cuotas(){
        
      Integer periodo =0;
      if(tab_anticipo.getValor("id_distributivo").equals("1")){
           if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){
                  periodo = utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO"))-1+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                       if(periodo > 12){
                             if(Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))>=70 && Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))<=100){
                                 calculo_valor();
                                }else{
                                      utilitario.agregarMensaje("Al menos el 70% de Sueldo", "Para Cuota de Diciembre");
                                }
                       }else{
                           calculo_valor();
                       }
              }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){
                       periodo = utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO"))+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                       if(periodo > 12){
                            if(Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))>=70 && Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))<=100){
                                    calculo_valor();
                            }else{
                                  utilitario.agregarMensaje("Al menos el 70% de Sueldo", "Maximo el 100%, Para Cuota de Diciembre");
                            }
                       }else{
                           calculo_valor();
                       }
                      }
      }else{
            calculo_valor();
      }     
    }
    
    public void calculo_valor(){
        Integer rango;
        double valora=0,valora1=0,valorm=0,media=0,rmu=0,valan=0,valorff=0;
        rmu =Double.parseDouble(tab_anticipo.getValor("rmu"));
        valan= Double.parseDouble(tab_parametros.getValor("valor_anticipo"));
        media = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"))/2;
        if(tab_anticipo.getValor("ide_tipo_anticipo").equals("1")){

        }else{
            if(tab_anticipo.getValor("id_distributivo").equals("1")){
            if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){
            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_parametros.getValor("ide_periodo_anticipo_inicial")));
            if (!tab_dato.isEmpty()) {
                rango = Integer.parseInt(tab_dato.getValor("periodo"))+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1;
                if(rango > 12){
                    valora= ((rmu*(Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))))/100);
                    if(valora>=Double.parseDouble(tab_parametros.getValor("valor_anticipo"))){
                                       utilitario.agregarMensajeError("Cuota Diciembre Excede Sueldo Anterior", "");
                    }else{
                            valorm = (valan-valora)/(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1);
                            if(media>=(Math.rint(valorm*100)/100)){
                                tab_parametros.setValor("val_cuo_adi", String.valueOf(valora));
                                tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                                utilitario.addUpdate("tab_parametros");
                                }else{
                                       tab_parametros.setValor("valor_cuota_mensual", "NULL");
                                       tab_parametros.setValor("val_cuo_adi", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                                       utilitario.addUpdate("tab_parametros");
                                       utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                    }
                    }
                }else if(rango <= 12){
                        valora1 = Double.parseDouble(tab_parametros.getValor("valor_anticipo")) / Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                        if(media>=(Math.rint(valora1*100)/100)){
                            tab_parametros.setValor("val_cuo_adi", "NULL");
                            tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1*100)/100));
                            utilitario.addUpdate("tab_parametros");
                        }else{
                               tab_parametros.setValor("valor_cuota_mensual", "NULL");
                               tab_parametros.setValor("val_cuo_adi", "NULL");
                               tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                               tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                               utilitario.addUpdate("tab_parametros");
                               utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                        }
                    }
            }else {
                   utilitario.agregarMensajeInfo("No existen Datos", "");
                   }
            }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){
                TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_parametros.getValor("ide_periodo_anticipo_inicial")));
            if (!tab_dato.isEmpty()) {
                rango = (Integer.parseInt(tab_dato.getValor("periodo"))-1+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo")));
                if(rango > 12){
                    valora= ((rmu*(Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))))/100);
                    if(valora>=Double.parseDouble(tab_parametros.getValor("valor_anticipo"))){
                    utilitario.agregarMensajeError("Cuota Diciembre Excede Sueldo Anterior", "");
                    }else{
                            valorm = (valan-valora)/(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1);
                            if(media>=(Math.rint(valorm*100)/100)){
                                tab_parametros.setValor("val_cuo_adi", String.valueOf(valora));
                                tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                                utilitario.addUpdate("tab_parametros");
                                }else{
                                       tab_parametros.setValor("valor_cuota_mensual", "NULL");
                                       tab_parametros.setValor("val_cuo_adi", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                                       utilitario.addUpdate("tab_parametros");
                                       utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                    }
                    }
                }else if(rango <= 12){
                        valora1 = Double.parseDouble(tab_parametros.getValor("valor_anticipo")) / Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                        if(media>=(Math.rint(valora1*100)/100)){
                            tab_parametros.setValor("val_cuo_adi", "NULL");
                            tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1*100)/100));
                            utilitario.addUpdate("tab_parametros");
                        }else{
                               tab_parametros.setValor("valor_cuota_mensual", "NULL");
                               tab_parametros.setValor("val_cuo_adi", "NULL");
                               tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                               tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                               utilitario.addUpdate("tab_parametros");
                               utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                        }
                    }
            }else {
                   utilitario.agregarMensajeInfo("No existen Datos", "");
                   }
            }
        }else{
            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_parametros.getValor("ide_periodo_anticipo_inicial")));
            if (!tab_dato.isEmpty()) {
                            valorm = (valan)/(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo")));
                            if(media>=(Math.rint(valorm*100)/100)){
                                tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                                utilitario.addUpdate("tab_parametros");
                                }else{
                                       tab_parametros.setValor("valor_cuota_mensual", "NULL");
                                       tab_parametros.setValor("val_cuo_adi", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                                       tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                                       utilitario.addUpdate("tab_parametros");
                                       utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                    }
            }else {
                   utilitario.agregarMensajeInfo("No existen Datos", "");
                   }
        }
        }
    }
    
    //Permite Buscar solicitud que se encuentra Ingresada o Pendiente
        public void Busca_tipo(){
        dia_dialogoca.Limpiar();
        grid_ca.getChildren().add(new Etiqueta("ELEGIR PARAMETRO DE BUSQUEDA:"));
        grid_ca.getChildren().add(cmb_seleccion);
        dia_dialogoca.setDialogo(grid_ca);
        dia_dialogoca.dibujar();
    }
    
     public void buscarSolicitud() {
            if (tex_busqueda.getValue() != null && tex_busqueda.getValue().toString().isEmpty() == false) {
                set_solicitud.getTab_seleccion().setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo WHERE aprobado_solicitante = 1 and ci_solicitante LIKE '" + tex_busqueda.getValue() + "'");
                set_solicitud.getTab_seleccion().ejecutarSql();
            } else {
                utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
            }
    }
    
    public void buscarSolicitud1(){
          dia_dialogoso.Limpiar();
          dia_dialogoso.setDialogo(gridso);
          grid_so.getChildren().add(set_solicitu);
          set_solicitu.setId("set_solicitu");
          set_solicitu.setConexion(con_postgres);
          set_solicitu.setHeader("LISTADO DE SERVIDORES");
          set_solicitu.setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo where ci_solicitante is not null and aprobado_solicitante =1");
          set_solicitu.getColumna("solicitante").setFiltro(true);
          set_solicitu.setRows(10);
          set_solicitu.setTipoSeleccion(false);
          dia_dialogoso.setDialogo(grid_so);
          set_solicitu.dibujar();
          dia_dialogoso.dibujar();
     }
     
    public void buscarGarantesoli(){
        dia_dialogoga.Limpiar();
        dia_dialogoga.setDialogo(gridga);
        grid_ga.getChildren().add(set_garante);
        set_garante.setId("set_garante");
        set_garante.setConexion(con_postgres);
        set_garante.setHeader("LISTADO DE GARANTES NO DISPONIBLES");
        set_garante.setSql("SELECT\n" +
                "ide_garante_anticipo,\n" +
                "g.ci_garante as cedula,\n" +
                "g.garante,\n" +
                "s.ci_solicitante,\n" +
                "s.solicitante,\n" +
                "c.valor_anticipo,\n" +
                "c.numero_cuotas_anticipo as plazo,\n" +
                "c.fecha_anticipo,\n" +
                "e.estado\n" +
                "FROM \"public\".srh_solicitud_anticipo AS s\n" +
                "INNER JOIN \"public\".srh_garante_anticipo AS \"g\" ON \"g\".ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "INNER JOIN \"public\".srh_calculo_anticipo AS \"c\" ON \"c\".ide_solicitud_anticipo = s.ide_solicitud_anticipo\n" +
                "INNER JOIN \"public\".srh_estado_anticipo AS e ON \"e\".ide_estado_tipo = c.ide_estado_anticipo\n" +
                "where c.ide_estado_anticipo in (1,3,2)");
        set_garante.getColumna("cedula").setFiltro(true);
        set_garante.getColumna("garante").setFiltro(true);
        set_garante.setRows(10);
        set_garante.setTipoSeleccion(false);
        dia_dialogoga.setDialogo(grid_ga);
        set_garante.dibujar();
        dia_dialogoga.dibujar();
    }
    
    public void abrirBusqueda() {
        if(cmb_seleccion.getValue().equals("1")){
            set_solicitud.dibujar();
            tex_busqueda.limpiar();
            set_solicitud.getTab_seleccion().limpiar();
        }else if(cmb_seleccion.getValue().equals("2")){
                buscarSolicitud1();
        }else {
            buscarGarantesoli();
        }
    }

    //Dibuja la Pantalla
    public void aceptarBusqueda() {
        if(cmb_seleccion.getValue().equals("1")){
            if (set_solicitud.getValorSeleccionado() != null) {
                aut_busca.setValor(set_solicitud.getValorSeleccionado());
                set_solicitud.cerrar();
                dibujarSolicitud();
                dia_dialogoca.cerrar();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
            }
        }else{
                if (set_solicitu.getValorSeleccionado() != null) {
                aut_busca.setValor(set_solicitu.getValorSeleccionado());
                dia_dialogoso.cerrar();
                dibujarSolicitud();
                dia_dialogoca.cerrar();
                utilitario.addUpdate("aut_busca,pan_opcion");
                    } else {
                        utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
                    }      
                }
    }
    
        //Permite Anular la solictud qeu esta ingresada siempre y cuando no este cobrandose.
    public void Anular1(){
        dia_dialogoan.Limpiar();
        dia_dialogoan.setDialogo(gridan);
        tcomenta.setSize(60);
        gridan.getChildren().add(new Etiqueta(" INGRESE COMENTARIO :"));
        gridan.getChildren().add(tcomenta);
        dia_dialogoan.setDialogo(grid_an);
        dia_dialogoan.dibujar();;
    }
    
    public void Anular(){
        if(tab_parametros.getValor("ide_estado_anticipo").equals("3")){//se encuentra pangandose
              utilitario.agregarMensajeError("Actualmente se Encuentra Descontandose", "");
        }else{
            iAnticipos.deleteDetalle(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")));
            iAnticipos.deleteCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")),Integer.parseInt(tab_parametros.getValor("ide_calculo_anticipo")),utilitario.getVariable("NICK"));
            iAnticipos.deleteSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")),tab_anticipo.getValor("ci_solicitante"),utilitario.getVariable("NICK"),tcomenta.getValue()+"",utilitario.getFechaActual());
            utilitario.agregarMensaje("Solicitud Anulada", "Con Exito");
            limpiarPanel();
        }
    }
    
    @Override
    public void insertar() {
        if (tab_anticipo.isFocus()) {
            aut_busca.limpiar();
            utilitario.addUpdate("aut_busca");
            tab_anticipo.limpiar();
            tab_anticipo.insertar();
            tab_garante.limpiar();
            tab_garante.insertar();
            tab_parametros.limpiar();
            tab_parametros.insertar();
        }
    }

    @Override
    public void guardar() {
        double dato3 =0;
        if(tab_anticipo.getValor("ide_solicitud_anticipo")!=null){
                utilitario.agregarMensaje("Registro No Puede Ser Guardado", "");
        }else {
            dato3 = (Double.parseDouble(tab_anticipo.getValor("rmu"))/2);
            if(tab_anticipo.getValor("ide_tipo_anticipo").equals("1")){
                if(Double.parseDouble(tab_parametros.getValor("valor_anticipo"))<=dato3){
                    if (tab_anticipo.guardar()) {
                        if (tab_garante.guardar()) {
                            tab_parametros.setValor("ide_estado_anticipo","1");
                            if (tab_parametros.guardar()) {
                                con_postgres.guardarPantalla();
                            }
                        }
                    }
                }else{
                    utilitario.agregarMensaje("Registro No Puede Ser Guardado", "Anticipo Ordinario No Cumple Condición");
                }
            }else{
                if (tab_anticipo.guardar()) {
                    if (tab_garante.guardar()) {
                        tab_parametros.setValor("ide_estado_anticipo","1");
                        if (tab_parametros.guardar()) {
                            con_postgres.guardarPantalla();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_parametros.isFocus()) {
            if(tab_parametros.eliminar()){
                if(tab_garante.eliminar()){
                    tab_anticipo.eliminar();
                }
            }
        }
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "DETALLE DE ANTICIPO":
                dia_dialogosr.Limpiar();
                gridr.getChildren().add(new Etiqueta("Ingrese Cedula :"));
                gridr.getChildren().add(txt_cedula);
                dia_dialogosr.setDialogo(gridr);
                dia_dialogosr.dibujar();
          break;
        }
    } 
    
      public void aceptoAnticipo(){
        switch (rep_reporte.getNombre()) {
               case "DETALLE DE ANTICIPO":
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("identificacion", txt_cedula.getValue()+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }
    }
    
    
    
    //CALCULAR MESES
    public static int calcularMes(Calendar cal1,Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60 ; // calcular la diferencia en horas a
        long diffDays = diffHours / 24 ; // calcular la diferencia en dias
        long diffWeek = diffDays / 7 ; // calcular la diferencia en semanas
        long diffMounth = diffWeek / 4 ; // calcular la diferencia en meses
        return Integer.parseInt(String.valueOf(diffMounth));
    }
    
    //CALCULAR ANIOS
    public static int calcularAnios(Calendar AnioLab) {
    Calendar fechaActual = Calendar.getInstance();
    // Cálculo de las diferencias.
    int anios = fechaActual.get(Calendar.YEAR) - AnioLab.get(Calendar.YEAR);
    int meses = fechaActual.get(Calendar.MONTH) - AnioLab.get(Calendar.MONTH);
    int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - AnioLab.get(Calendar.DAY_OF_MONTH);
    if(meses < 0 // Aún no es el mes 
            || (meses==0 && dias < 0)) { // o es el mes pero no ha llegado el día.
        anios--;
    }
    return anios;
    }
    
    //BUSQUEDA DE MES PARA LA AISGNACION DEL PERIODO
    public String meses(Integer numero){
        switch (numero){
            case 12:
                selec_mes = "Diciembre";
                break;
            case 11:
                selec_mes = "Noviembre";
                break;
            case 10:
                selec_mes = "Octubre";
                break;
            case 9:
                selec_mes = "Septiembre";
                break;
            case 8:
                selec_mes = "Agosto";
                break;
            case 7:
                selec_mes = "Julio";
                break;
            case 6:
                selec_mes = "Junio";
                break;
            case 5:
                selec_mes = "Mayo";
                break;
            case 4:
                selec_mes = "Abril";
                break;
            case 3:
                selec_mes = "Marzo";
                break;
            case 2:
                selec_mes = "Febrero";
                break;
            case 1:
                selec_mes = "Enero";
                break;
        }
        return selec_mes;
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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

    public Tabla getSet_colaborador() {
        return set_colaborador;
    }

    public void setSet_colaborador(Tabla set_colaborador) {
        this.set_colaborador = set_colaborador;
    }

    public Tabla getSet_solicitante() {
        return set_solicitante;
    }

    public void setSet_solicitante(Tabla set_solicitante) {
        this.set_solicitante = set_solicitante;
    }

    public Tabla getSet_solicitu() {
        return set_solicitu;
    }

    public void setSet_solicitu(Tabla set_solicitu) {
        this.set_solicitu = set_solicitu;
    }

    public SeleccionTabla getSet_solicitud() {
        return set_solicitud;
    }

    public void setSet_solicitud(SeleccionTabla set_solicitud) {
        this.set_solicitud = set_solicitud;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Tabla getTab_garante() {
        return tab_garante;
    }

    public void setTab_garante(Tabla tab_garante) {
        this.tab_garante = tab_garante;
    }

    public Tabla getTab_parametros() {
        return tab_parametros;
    }

    public void setTab_parametros(Tabla tab_parametros) {
        this.tab_parametros = tab_parametros;
    }

    public Tabla getSet_garante() {
        return set_garante;
    }

    public void setSet_garante(Tabla set_garante) {
        this.set_garante = set_garante;
    }
    
}
