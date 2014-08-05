/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.SolicAnticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author KEJA
 */
public class pre_anticipos_gadmur extends Pantalla{

        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_garante = new Tabla();
    private Tabla tab_parametros = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_colaborador = new Tabla();
    private Tabla set_solicitante = new Tabla();
    private Tabla set_solicitu = new Tabla();
    private SeleccionTabla set_solicitud = new SeleccionTabla();
    
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogos = new Dialogo();
    private Dialogo dia_dialogoca = new Dialogo();
    private Dialogo dia_dialogoso = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid_ca = new Grid();
    private Grid grid_so = new Grid();
    private Grid grid_ds = new Grid();
    private Grid grid = new Grid();
    private Grid grids = new Grid();
    private Grid gridso = new Grid();
    
    //
    private Panel pan_opcion = new Panel();
    private Texto tex_busqueda = new Texto();
    private Combo cmb_seleccion = new Combo();
    
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_anticipos_gadmur() {
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(pan_opcion);
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
//        bot_busca.setMetodo("abrirBusqueda");
        bot_busca.setMetodo("Busca_tipo");
        bar_botones.agregarBoton(bot_busca);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,aprobado_solicitante FROM srh_solicitud_anticipo");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        Boton bot_abort = new Boton();
        bot_abort.setValue("Anular Solictud");
        bot_abort.setIcon("ui-icon-closethick");
        bot_abort.setMetodo("Anular");
        bar_botones.agregarBoton(bot_abort);
        
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

        dia_dialogos.setId("dia_dialogos");
        dia_dialogos.setTitle("BUSCAR SOLICITANTE"); //titulo
        dia_dialogos.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogos.setHeight("50%");//siempre porcentaje   alto
        dia_dialogos.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogos.getBot_aceptar().setMetodo("aceptoSolicitante");
        grid_ds.setColumns(4);
        agregarComponente(dia_dialogos);
        
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
        lista1.add(fila1);;
        lista1.add(fila2);;
        cmb_seleccion.setCombo(lista1);
        
        dia_dialogoca.setId("dia_dialogoca");
        dia_dialogoca.setTitle("SELECCIONAR TIPO DE BUSQUEDA"); //titulo
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
        
        dibujarSolicitud();
        
         /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
    }

    //Permite Anular la solictud qeu esta ingresada siempre y cuando no este cobrandose.
    public void Anular(){
        iAnticipos.deleteCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")));
        iAnticipos.deleteGarante(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")));
        iAnticipos.deleteSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")));
        utilitario.agregarMensaje("Solicitud Anulada", "Con Exito");
        utilitario.addUpdate("pan_opcion");
    }
    
    //Permite Seleccionar porque tipo de parametro de busqueda se desea hacerlo, cedula o nombres
    public void Busca_tipo(){
        dia_dialogoca.Limpiar();
        grid_ca.getChildren().add(new Etiqueta("ELEGIR PARAMETRO DE BUSQUEDA:"));
        grid_ca.getChildren().add(cmb_seleccion);
        dia_dialogoca.setDialogo(grid_ca);
        dia_dialogoca.dibujar();
    }
    
     public void buscarSolicitud() {
            if (tex_busqueda.getValue() != null && tex_busqueda.getValue().toString().isEmpty() == false) {
                set_solicitud.getTab_seleccion().setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo WHERE ci_solicitante LIKE '" + tex_busqueda.getValue() + "'");
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
          set_solicitu.setSql("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,(case when aprobado_solicitante = 1 then 'SI' ELSE 'NO' end ) AS aprobado FROM srh_solicitud_anticipo");
          set_solicitu.getColumna("solicitante").setFiltro(true);
          set_solicitu.setRows(10);
          set_solicitu.setTipoSeleccion(false);
          dia_dialogoso.setDialogo(grid_so);
          set_solicitu.dibujar();
          dia_dialogoso.dibujar();
     }
     
    public void abrirBusqueda() {
        if(cmb_seleccion.getValue().equals("1")){
            set_solicitud.dibujar();
            tex_busqueda.limpiar();
            set_solicitud.getTab_seleccion().limpiar();
        }else{
                buscarSolicitud1();
        }
    }

    //Dibuja la Pantalla
    public void aceptarBusqueda() {
        if(cmb_seleccion.getValue().equals("1")){
            if (set_solicitud.getValorSeleccionado() != null) {
                aut_busca.setValor(set_solicitud.getValorSeleccionado());
                set_solicitud.cerrar();
                dibujarSolicitud();
                utilitario.addUpdate("aut_busca,pan_opcion");
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una solicitud", "");
            }
        }else{
                if (set_solicitu.getValorSeleccionado() != null) {
                aut_busca.setValor(set_solicitu.getValorSeleccionado());
                dia_dialogoso.cerrar();
                dibujarSolicitud();
                utilitario.addUpdate("aut_busca,pan_opcion");
                    } else {
                        utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
                    }      
                }
    }
    
    //Plantilla para Solicitud
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
        
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("solicitante").setMetodoChange("buscaSolicitante");
        
        tab_anticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_anticipo.getColumna("cod_banco").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_anticipo.getColumna("cod_cuenta").setCombo("SELECT cod_cuenta,nombre FROM ocecuentas");
        tab_anticipo.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tab_anticipo.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_anticipo.getColumna("cod_grupo").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        
        tab_anticipo.getColumna("login_ingre_solicitud").setValorDefecto(utilitario.getVariable("NICK"));
        tab_anticipo.getColumna("ip_ingre_solicitud").setValorDefecto(utilitario.getIp());
        
        tab_anticipo.getColumna("login_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("ip_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("login_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("ip_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("aprobado_solicitante").setVisible(false);
        
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
        tab_garante.getColumna("IDE_GARANTE_ANTICIPO ").setVisible(false);
        tab_garante.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_garante.getColumna("garante").setMetodoChange("buscaColaborador");
        tab_garante.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_garante.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_garante.setTipoFormulario(true);
        tab_garante.getGrid().setColumns(4);
        tab_garante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setMensajeWarn("DATOS DE GARANTE");
        tpd.setPanelTabla(tab_garante);
        
        tab_parametros.setId("tab_parametros");
        tab_parametros.setConexion(con_postgres);
        tab_parametros.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 3);
        tab_parametros.getColumna("IDE_CALCULO_ANTICIPO").setVisible(false);
        tab_parametros.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());
        tab_parametros.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");

        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
        tab_parametros.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_parametros.getColumna("numero_cuotas_anticipo").setMetodoChange("porcentaje");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setMetodoChange("cuotas");
        tab_parametros.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_parametros.setTipoFormulario(true);
        tab_parametros.getGrid().setColumns(6);
        tab_parametros.dibujar();
        
        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("DATOS DE ANTICIPO A SOLICITAR");
        tpp.setPanelTabla(tab_parametros);
        
            Grupo gru = new Grupo();
            gru.getChildren().add(tpa);
            gru.getChildren().add(tpd);
            gru.getChildren().add(tpp);
            pan_opcion.getChildren().add(gru);    
    }
    
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
    
    //BUSCAR SOLICITANTE POR CEDULA
    public void llenarDatosE(){//SOLICITANTE
      TablaGenerica tab_dato = iAnticipos.VerifEmpleid(tab_anticipo.getValor("ci_solicitante"));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Pendiente");
       }else {
              if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) { 
                    TablaGenerica tab_dato1 = iAnticipos.empleadosCed(tab_anticipo.getValor("ci_solicitante"));//empleados
                    if (!tab_dato1.isEmpty()) {
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
                    }else {
                       TablaGenerica tab_dato2 = iAnticipos.trabajadoresCed(tab_anticipo.getValor("ci_solicitante"));//trabajadores
                            if (!tab_dato2.isEmpty()) {
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
     if (set_solicitante.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = iAnticipos.VerifEmpleCod(Integer.parseInt(set_solicitante.getValorSeleccionado()));
                 if (!tab_dato.isEmpty()) {
                        utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Ingresado/Pendiente");
                    }else {
                    TablaGenerica tab_dato1 = iAnticipos.empleados(Integer.parseInt(set_solicitante.getValorSeleccionado()));//empleados
                    if (!tab_dato1.isEmpty()) {
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
                        dia_dialogos.cerrar();
                    }else {
                       TablaGenerica tab_dato2 = iAnticipos.trabajadores(Integer.parseInt(set_solicitante.getValorSeleccionado()));//trabajadores
                            if (!tab_dato2.isEmpty()) {
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
                                dia_dialogos.cerrar();
                            }else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                      }
                            }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    }
    
    // BUSCAR GARANTE POR CEDULA
    public void llenarGarante(){
     TablaGenerica tab_dato = iAnticipos.VerifGaranteid(tab_garante.getValor("ci_garante"));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Garante No Disponible", "");
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
                                      utilitario.agregarMensajeInfo("Garante No Disponible", "");
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
                                "FROM srh_empleado WHERE estado = 1 AND cod_tipo IN (4, 7) and nombres LIKE '%"+tab_garante.getValor("garante")+"%'");
        set_colaborador.getColumna("nombres").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogo.setDialogo(grid_d);
        set_colaborador.dibujar();
        dia_dialogo.dibujar();
    }
    
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
                                             utilitario.agregarMensajeInfo("Garante No Disponible", "");
                                             }
                          }
       
     }else {
            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
    }
    
     //CALCULO DE CUOTAS, VERIFICACION DE MONTOS Y PLAZOS PARA ANTICIPO DE SUELDO
    //VALIDACION DE VALOR A PERCIBIR EN EL ANTICIPO DE ACUERDO A REMUNERACION LIQUIDA ANTERIOR PERCIBIDA
    
    public void remuneracion(){
        double  dato1 = 0,dato2=0,compara=0; 
        dato2 = Double.parseDouble(tab_anticipo.getValor("rmu"));
        dato1 = Double.parseDouble(tab_parametros.getValor("valor_anticipo"));
        compara = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"));
     if(compara>0){
        if((dato1/dato2)<=1){
             tab_parametros.setValor("numero_cuotas_anticipo", "2");
             utilitario.addUpdate("tab_parametros");
             utilitario.agregarMensajeInfo("Anticipo, Hasta una Remuneracion", "Plazo Maximo de Cobro, 2 Meses");
                     if(tab_parametros.getValor("numero_cuotas_anticipo").equals("2")){
//                         llenarFecha();
//                         cuotas();
                        }
            tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(true);
          }else if((dato1/dato2)>1&&(dato1/dato2)<=3){//HASTA 3 REMUNERACIONES 
                    tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(false);
                    tab_parametros.setValor("numero_cuotas_anticipo", "NULL");
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                    utilitario.agregarMensajeInfo("Ingresar Plazo de Cobro", "");
                }else{
                    utilitario.agregarMensajeInfo("Monto Excede Remuneracion Unificada", "");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("numero_cuotas_anticipo", "NULL");
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                    utilitario.addUpdate("tab_parametros");
                }
    }else{
          utilitario.agregarMensajeError("Remuneracion Anterior", "Saldo Negativo");
          tab_parametros.getColumna("valor_anticipo").setLectura(true);
          tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(true);
          tab_parametros.setValor("valor_anticipo", "NULL");
          tab_parametros.setValor("valor_cuota_adicional", "NULL");
          tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
          tab_parametros.setValor("valor_cuota_mensual", "NULL");
          tab_parametros.setValor("valor_cuota_adicional", "NULL");
          tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
          tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
          utilitario.addUpdate("tab_parametros");
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
    }

    @Override
    public void eliminar() {
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
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
    
}
