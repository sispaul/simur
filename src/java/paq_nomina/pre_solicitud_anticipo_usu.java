/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.SolicAnticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_anticipo_usu extends Pantalla{

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
    
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid = new Grid();
    
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_solicitud_anticipo_usu () {
         
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_solicitud_anticipo,ci_solicitante,solicitante,aprobado_solicitante FROM srh_solicitud_anticipo");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Soliciud");
        bot_anular.setIcon("ui-icon-close");
        bot_anular.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_anular);
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_solicitud_anticipo", "ide_solicitud_anticipo", 1);
     
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
        tpd.setPanelTabla(tab_garante);
        
        tab_parametros.setId("tab_parametros");
        tab_parametros.setConexion(con_postgres);
        tab_parametros.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 3);
        tab_parametros.getColumna("IDE_CALCULO_ANTICIPO").setVisible(false);
        tab_parametros.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());
        tab_parametros.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
//        tab_parametros.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_parametros.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_parametros.getColumna("numero_cuotas_anticipo").setMetodoChange("porcentaje");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setMetodoChange("servidor");
        tab_parametros.setTipoFormulario(true);
        tab_parametros.getGrid().setColumns(4);
        tab_parametros.dibujar();
        
        PanelTabla tpp = new PanelTabla();
        tpp.setPanelTabla(tab_parametros);
            
        Boton bot_save = new Boton();
        bot_save.setValue("GUARDAR");
        bot_save.setIcon("ui-icon-cancel");
        bot_save.setMetodo("guardar");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("ELIMINAR");
        bot_delete.setIcon("ui-icon-cancel");
        bot_delete.setMetodo("eliminar");
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tpa, tpd, tpp, "40%", "40%", "H");
        agregarComponente(div_division);
        
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("BUSCAR COLABORADOR"); //titulo
        dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoColaborador");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
        
    }
    
    //LLENAR DATOS DE SOLICTANTE Y GARANTE     /*POR IDENTIFICACION*/
    public void llenarDatosE(){//SOLICITANTE
       TablaGenerica tab_dato = iAnticipos.Datos(tab_consulta.getValor("NICK_USUA"));
       if (!tab_dato.isEmpty()) {
                    TablaGenerica tab_dato1 = iAnticipos.empleadosNom(tab_dato.getValor("NOM_USUA")+"");//empleados
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
                        tab_anticipo.setValor("ide_estado_anticipo", "1");
                        utilitario.addUpdate("tab_anticipo");
                    }else {
                       TablaGenerica tab_dato2 = iAnticipos.trabajadoresNom(tab_dato.getValor("NOM_USUA")+"");//trabajadores
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
                                tab_anticipo.setValor("ide_estado_anticipo", "1");
                                utilitario.addUpdate("tab_anticipo");
                            }else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                      }
                           } else {
                        utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                    }  
                              tab_garante.insertar(); 
    }

    //BUSQUEDA POR CEDULA DE GARANTE
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
                                   tab_parametros.insertar();
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
          TablaGenerica tab_dato = iAnticipos.GaranteNom(Integer.parseInt(set_colaborador.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                     tab_garante.setValor("ide_empleado_garante", tab_dato.getValor("cod_empleado"));
                     tab_garante.setValor("ci_garante", tab_dato.getValor("cedula_pass"));
                     tab_garante.setValor("garante", tab_dato.getValor("nombres"));
                     tab_garante.setValor("id_distributivo", tab_dato.getValor("id_distributivo"));
                     tab_garante.setValor("cod_tipo", tab_dato.getValor("cod_tipo"));
                      utilitario.addUpdate("tab_garante");
                      dia_dialogo.cerrar();
                      tab_parametros.insertar();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos empleados del municipio", "");
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
                            llenarFecha();
                            cuotas();
                        }
             tab_parametros.getColumna("numero_cuotas_anticipo").setLectura(true);
            }else if((dato1/dato2)>1&&(dato1/dato2)<=3.0){//HASTA 3 REMUNERACIONES 
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
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                    tab_parametros.setValor("valor_cuota_mensual", "NULL");
                    tab_parametros.setValor("valor_cuota_adicional", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                    tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
            utilitario.addUpdate("tab_parametros");
        }
    }
    
    public void porcentaje(){
        Integer porcentaje =0;
           if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){
                       porcentaje = utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO"))-1+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                       if(porcentaje >= 12){
                           utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                           tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                           tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                           tab_parametros.setValor("valor_cuota_mensual", "NULL");
                           tab_parametros.setValor("valor_cuota_adicional", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                           utilitario.addUpdate("tab_parametros");
                       }else{
                           tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                           tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                           tab_parametros.setValor("valor_cuota_mensual", "NULL");
                           tab_parametros.setValor("valor_cuota_adicional", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                           utilitario.addUpdate("tab_parametros");
                           servidor();
                       }
              }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){
                       porcentaje = utilitario.getMes(tab_parametros.getValor("FECHA_ANTICIPO"))+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                       if(porcentaje >= 12){
                           utilitario.agregarMensajeInfo("Seleccione Porcentaje de Descuento", "Para Cuota de Diciembre");
                           tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(false);
                           tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                           tab_parametros.setValor("valor_cuota_mensual", "NULL");
                           tab_parametros.setValor("valor_cuota_adicional", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                           utilitario.addUpdate("tab_parametros");
                       }else{
                           tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
                           tab_parametros.setValor("porcentaje_descuento_diciembre", "NULL");
                           tab_parametros.setValor("valor_cuota_mensual", "NULL");
                           tab_parametros.setValor("valor_cuota_adicional", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                           tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                           utilitario.addUpdate("tab_parametros");
                            servidor();
                       }
                      }
    }
    
    
    /*VALIDACION DE PERSONA QUE SOLICITA ANTICIPO*/    
    public void servidor(){
        
        Integer anos=0, dias=0,meses=0,mesesf=0,aniosf=0,diasf=0,meses_a=0,anios_a=0,dias_a=0;
        TablaGenerica tab_dato = iAnticipos.empleado(tab_anticipo.getValor("ci_solicitante"));
        if (!tab_dato.isEmpty()) {
            anios_a = utilitario.getAnio(utilitario.getFechaActual());
            meses_a = utilitario.getMes(utilitario.getFechaActual());
            dias_a = utilitario.getDia(utilitario.getFechaActual());
            TablaGenerica tab_datof = iAnticipos.FechaContrato(Integer.parseInt(tab_anticipo.getValor("ide_empleado_solicitante")));
            if (!tab_datof.isEmpty()) {
            if(tab_dato.getValor("id_distributivo").equals("1")){//VALIDACION DE EMPLEADOS
                    if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                                            anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                            dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                            meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                if(tab_datof.getValor("cod_tipo").equals("4")||tab_datof.getValor("cod_tipo").equals("10")){
                                        if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
                                        llenarFecha();
                                        cuotas();
                                        }else{
                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                            }
                                                }else{
                                                        mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                        aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                        diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                          if(mesesf!=1){//si el ingreso es diferente de enero
                                                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                        llenarFecha();
                                                                        cuotas();
                                                                        }else{
                                                                            tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                            utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                        }
                                                              }else{
                                                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                        llenarFecha();
                                                                        cuotas();
                                                                    }else{
                                                                        tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                    } 
                                                                   }
                                                    }
                        }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28
                                        if(tab_datof.getValor("cod_tipo").equals("4")||tab_datof.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
                                                        llenarFecha();
                                                        cuotas();
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                            }
                                                        }else{
                                                                mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                  if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                    cuotas();
                                                                                }else{
                                                                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                } 
                                                                      }else{
                                                                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                llenarFecha();
                                                                                cuotas();
                                                                            }else{
                                                                                tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                            }  
                                                                           }
                                                            }
                                }
                }else if(tab_dato.getValor("id_distributivo").equals("2")){//VALIDACION DE TRABAJADORES
                            if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=15){//VALIDACION POR DIA HASTA 10
                                                    anos=utilitario.getAnio(tab_datof.getValor("fecha_contrato"));
                                                    dias=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                    meses=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                        if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
                                                    llenarFecha();
                                                    cuotas();
                                                    }else{
                                                            utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                        }                                              
                                                        }else{
                                                                mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                  if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                 if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                     llenarFecha();
                                                                                     cuotas();
                                                                                    }else{
                                                                                        tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                      }else{
                                                                            if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){ 
                                                                                llenarFecha();
                                                                                cuotas();
                                                                                }else{
                                                                                    tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                    utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,meses_a,dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                }  
                                                                           }
                                                            }
                                }else if(utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))>=16 && utilitario.getDia(tab_parametros.getValor("FECHA_ANTICIPO"))<=31){//VALIDACION POR DIAS DEL 11 AL 28
                                            if(tab_datof.getValor("cod_tipo").equals("7")||tab_datof.getValor("cod_tipo").equals("10")){
                                                    if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>1 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
                                                            llenarFecha();
                                                            cuotas();
                                                        }else{
                                                                utilitario.agregarMensaje("Tiempo Maximo de Pago", "18 MESES");
                                                            }
                                                            }else{
                                                                    mesesf=utilitario.getMes(tab_datof.getValor("fecha_contrato"));
                                                                    aniosf=utilitario.getAnio(tab_datof.getValor("fecha_contrato"))+1;
                                                                    diasf=utilitario.getDia(tab_datof.getValor("fecha_contrato"));
                                                                      if(mesesf!=1){//si el ingreso es diferente de enero
                                                                                if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                    cuotas();
                                                                                    }else{
                                                                                        tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                          }else{
                                                                               if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))){
                                                                                    llenarFecha();
                                                                                    cuotas();
                                                                                    }else{
                                                                                        tab_parametros.setValor("numero_cuotas_anticipo", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));
                                                                                        utilitario.agregarMensajeInfo("Su plazo maximo de meses de pago es: ", String.valueOf(calcularMes(new GregorianCalendar(anios_a,(meses_a+1),dias_a),new GregorianCalendar(aniosf,mesesf,diasf))));  
                                                                                    }
                                                                               }
                                                                }
                                        }
                        }
                        }else {
               utilitario.agregarMensajeError("Solicitante de anticipo", "No posee historia laboral");
               }
            }else {
               utilitario.agregarMensajeInfo("No existen Datos", "");
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
                                        }else {
                                              utilitario.agregarMensajeInfo("No existen Datos", "");
                                              }
                        }else  if(calculo1==0){
                                TablaGenerica tab_dato = iAnticipos.periodos(meses(Integer.parseInt(mes)),String.valueOf(Integer.parseInt(anio)));
                                     if (!tab_dato.isEmpty()) {
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
                                         }else {
                                               utilitario.agregarMensajeInfo("No existen Datos", "");
                                               }
                                }
                    }else if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
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
                                            TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-12-1),String.valueOf(Integer.parseInt(anio)+1));
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
                                                    utilitario.addUpdate("tab_parametros");
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
                            }else if(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))>12 && Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))<=18){
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
                                                    TablaGenerica tab_dato1 = iAnticipos.periodos(meses(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))+mess-12),String.valueOf(Integer.parseInt(anio)+1));
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
//        tramite();
        
        Integer rango;
        double valora=0,valora1=0,valorm=0,media=0,rmu=0,valan=0,valorff=0;
        rmu =Double.parseDouble(tab_anticipo.getValor("rmu"));
        valan= Double.parseDouble(tab_parametros.getValor("valor_anticipo"));
        media = Double.parseDouble(tab_anticipo.getValor("rmu_liquido_anterior"))/2;
        TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_parametros.getValor("ide_periodo_anticipo_inicial")));
        if (!tab_dato.isEmpty()) {
            rango = Integer.parseInt(tab_dato.getValor("periodo"))+Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
            
            if(rango > 12){
                valora= ((rmu*(Integer.parseInt(tab_parametros.getValor("porcentaje_descuento_diciembre"))))/100);
                if(valora>=Double.parseDouble(tab_parametros.getValor("valor_anticipo"))){
                                   utilitario.agregarMensajeError("Cuota Diciembre Excede Sueldo Anterior", "");
                }else{
                        valorm = (valan-valora)/(Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"))-1);
                        if(media>=(Math.rint(valorm*100)/100)){
                            tab_parametros.setValor("valor_cuota_adicional", String.valueOf(valora));
                            tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valorm*100)/100));
                            utilitario.addUpdate("tab_parametros");
                            }else{
                                   tab_parametros.setValor("valor_cuota_mensual", "NULL");
                                   tab_parametros.setValor("valor_cuota_adicional", "NULL");
                                   tab_parametros.setValor("ide_periodo_anticipo_inicial", "NULL");
                                   tab_parametros.setValor("ide_periodo_anticipo_final", "NULL");
                                   utilitario.addUpdate("tab_parametros");
                                   utilitario.agregarMensajeError("Cuota Mensual Excede 50% Sueldo Anterior", "");
                                }
                }
            }else if(rango <= 12){
                    
                    valora1 = Double.parseDouble(tab_parametros.getValor("valor_anticipo")) / Integer.parseInt(tab_parametros.getValor("numero_cuotas_anticipo"));
                    if(media>=(Math.rint(valora1*100)/100)){
                        tab_parametros.setValor("valor_cuota_adicional", "NULL");
                        tab_parametros.setValor("valor_cuota_mensual", String.valueOf(Math.rint(valora1*100)/100));
                        utilitario.addUpdate("tab_parametros");
                    }else{
                           tab_parametros.setValor("valor_cuota_mensual", "NULL");
                           tab_parametros.setValor("valor_cuota_adicional", "NULL");
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
    
    @Override
    public void insertar() {
//       TablaGenerica tab_dato = iAnticipos.Datos(tab_consulta.getValor("NICK_USUA"));
//       if (!tab_dato.isEmpty()) {
//           
//          }else{
//               utilitario.agregarMensajeError("No Encuentra Datos de Solicitante", "");
//               }
        
        
      if (tab_anticipo.isFocus()) {
            tab_anticipo.insertar();
            llenarDatosE();
        }
        
//      TablaGenerica tab_dato = iAnticipos.validar(tab_anticipo.getValor("ci_solicitante"));
//        if (!tab_dato.isEmpty()) {
//            if(tab_anticipo.getValor("ci_solicitante").equals(tab_dato.getValor("ci_solicitante"))){
//                if(tab_dato.getValor("ide_estado_anticipo").equals("4")){
//                    if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
////                                tab_anticipo.guardar();
////                                con_postgres.guardarPantalla();
//                    } else {
//                            utilitario.agregarMensajeError("El Número de Cedula no es válido", "");
//                            }
//                }else{
//                    utilitario.agregarMensajeError("Posee actualmente un Solicitud Ingresada", "En proceso");
//                    }
//           }else{
//                utilitario.agregarMensajeError("Posee actualmente un Solicitud Ingresada", "En proceso");
//                }
//        }else{
//            if (utilitario.validarCedula(tab_anticipo.getValor("ci_solicitante"))) {
////                                                    tab_anticipo.guardar();
////                                                    con_postgres.guardarPantalla();
//                } else {
//                    utilitario.agregarMensajeError("El Número de Cedula no es válido", "");
//                }
//        }
      
    }

    @Override
    public void guardar() {
         tab_anticipo.guardar();
         con_postgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
        
    }

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getTab_garante() {
        return tab_garante;
    }

    public void setTab_garante(Tabla tab_garante) {
        this.tab_garante = tab_garante;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_parametros() {
        return tab_parametros;
    }

    public void setTab_parametros(Tabla tab_parametros) {
        this.tab_parametros = tab_parametros;
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
    
}
