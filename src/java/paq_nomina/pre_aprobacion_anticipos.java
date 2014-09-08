/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
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
public class pre_aprobacion_anticipos extends Pantalla{
    
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_listado = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_lista = new SeleccionTabla();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();

     //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion2 = new Panel();
    
    private Texto txt_num_listado = new Texto();
    
    private Calendario cal_fecha = new Calendario();
    
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_aprobacion_anticipos() {
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
        agregarComponente(quinde);
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS INGRESADOS PARA APROBAR");
        agregarComponente(pan_opcion);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setSql("SELECT\n" +
                            "s.ide_solicitud_anticipo,\n" +
                            "c.fecha_anticipo,\n" +
                            "s.ide_empleado_solicitante,\n" +
                            "s.ci_solicitante,\n" +
                            "s.solicitante,\n" +
                            "c.valor_anticipo,\n" +
                            "c.numero_cuotas_anticipo,\n" +
                            "c.valor_cuota_mensual,\n" +
                            "c.val_cuo_adi,\n" +
                            "p.mes AS mes_descuento,\n" +
                            "s.aprobado_solicitante,\n" +
                            "s.id_distributivo,\n" +
                            "c.ide_periodo_anticipo_inicial,\n" +
                            "c.ide_periodo_anticipo_final\n" +
                            "FROM   \n" +
                            "srh_solicitud_anticipo AS s   \n" +
                            "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo    \n" +
                            "INNER JOIN srh_periodo_anticipo AS p ON p.ide_periodo_anticipo = c.ide_periodo_anticipo_inicial\n" +
                            "WHERE c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')\n" +
                            "order by s.ide_solicitud_anticipo");
        tab_anticipo.setCampoPrimaria("ide_solicitud_anticipo");
        tab_anticipo.setCampoOrden("ide_solicitud_anticipo");
       List lista = new ArrayList();
        Object fila1[] = {
            "1", "Aprobar"
        };
        Object fila2[] = {
            "0", "Negar"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_anticipo.getColumna("aprobado_solicitante").setRadio(lista, "");
        tab_anticipo.getColumna("id_distributivo").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_inicial").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setVisible(false);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.setRows(8);
        tab_anticipo.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_anticipo);
        
        tab_listado.setId("tab_listado");
        tab_listado.setConexion(con_postgres);
        tab_listado.setSql("SELECT \n" +
                            " s.ide_solicitud_anticipo, \n" +
                            " s.ide_empleado_solicitante, \n" +
                            " s.ci_solicitante, \n" +
                            " s.solicitante, \n" +
                            " c.valor_anticipo, \n" +
                            " c.numero_cuotas_anticipo, \n" +
                            " c.valor_cuota_mensual, \n" +
                            " c.val_cuo_adi, \n" +
                            " p.mes AS mes_descuento, \n" +
                            " s.aprobado_solicitante, \n" +
                            " null as quitar_lista, \n" +
                            " s.id_distributivo, \n" +
                            " c.ide_periodo_anticipo_inicial, \n" +
                            " c.ide_periodo_anticipo_final \n" +
                            " FROM   \n" +
                            " srh_solicitud_anticipo AS s   \n" +
                            " INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo    \n" +
                            " INNER JOIN srh_periodo_anticipo AS p ON p.ide_periodo_anticipo = c.ide_periodo_anticipo_inicial \n" +
                            " WHERE c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'APROBADO') and s.ide_listado is null\n" +
                            " order by s.ide_solicitud_anticipo");
        tab_listado.setCampoPrimaria("ide_solicitud_anticipo");
        tab_listado.setCampoOrden("ide_solicitud_anticipo");
        tab_listado.getColumna("id_distributivo").setVisible(false);
        tab_listado.getColumna("ide_periodo_anticipo_inicial").setVisible(false);
        tab_listado.getColumna("ide_periodo_anticipo_final").setVisible(false);
        tab_listado.getColumna("aprobado_solicitante").setVisible(false);
        List list = new ArrayList();
        Object fila[] = {
            "1", "Devolver"
        };
        list.add(fila);;
        tab_listado.getColumna("quitar_lista").setRadio(list, "");
        tab_listado.getColumna("quitar_lista").setLongitud(1);
        tab_listado.getGrid().setColumns(4);
        tab_listado.setRows(7);
        tab_listado.dibujar();

        PanelTabla pat_lista = new PanelTabla();
        pat_lista.setPanelTabla(tab_listado);
        pan_opcion2.getChildren().add(pat_lista);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru);
        txt_num_listado.setId("txt_num_listado");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(6);
        gri_busca.getChildren().add(new Etiqueta("# Listado: "));    
        gri_busca.getChildren().add(txt_num_listado);
        
        Boton bot_save = new Boton();
        bot_save.setValue("Guardar Listado");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("save_lista");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("Quitar de Listado");
        bot_delete.setExcluirLectura(true);
        bot_delete.setIcon("ui-icon-extlink");
        bot_delete.setMetodo("devolver");
        
        gri_busca.getChildren().add(bot_save);
        gri_busca.getChildren().add(bot_delete);
        agregarComponente(gri_busca);
        
        pan_opcion2.setId("pan_opcion2");
        pan_opcion2.setTransient(true);
        pan_opcion2.setHeader("LISTADO DE SOLICITUD DE ANTICIPOS APROBADOS");
        agregarComponente(pan_opcion2);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pan_opcion, gri_busca, pan_opcion2, "44%", "50%", "H");
        agregarComponente(div_division);
        
        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        ////Para reportes
        
        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("FECHA: "));
        gru_lis.getChildren().add(cal_fecha);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);
        
        set_lista.setId("set_lista");
        set_lista.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_lista.setSeleccionTabla("SELECT ide_solicitud_anticipo,ide_listado FROM srh_solicitud_anticipo WHERE ide_solicitud_anticipo=-1", "ide_solicitud_anticipo");
        set_lista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_lista.getTab_seleccion().setRows(10);
        set_lista.setRadio();
        set_lista.setWidth("20%");
        set_lista.setHeight("40%");
        set_lista.getGri_cuerpo().setHeader(gru_lis);
        set_lista.getBot_aceptar().setMetodo("aceptoAprobacion");
        set_lista.setHeader("SELECCIONE LISTADO");
        agregarComponente(set_lista);
        
    }   
    
    public void buscarColumna() {
        if (cal_fecha.getValue() != null && cal_fecha.getValue().toString().isEmpty() == false ) {
            set_lista.getTab_seleccion().setSql("SELECT DISTINCT on (ide_listado ) ide_solicitud_anticipo,ide_listado FROM srh_solicitud_anticipo where fecha_listado='"+cal_fecha.getFecha()+"' ORDER BY ide_listado ");
            set_lista.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    public void devolver(){
        for (int i = 0; i < tab_listado.getTotalFilas(); i++) {
            tab_listado.getValor(i, "quitar_lista");
            tab_listado.getValor(i, "ide_solicitud_anticipo");
            if(tab_listado.getValor(i, "quitar_lista")!=null){
                iAnticipos.deleteDevolver(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
                iAnticipos.actuaDevolucion(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
                iAnticipos.actuaSoliDevolucion(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")));
            }
        }
         tab_anticipo.actualizar();
         utilitario.agregarMensaje("Formularios Devueltos", "");
         tab_listado.actualizar();
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        requisito();
    }

    @Override
    public void eliminar() {
    }

    /* FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES     */
    public void seleccionar_tabla1(SelectEvent evt) {
        tab_anticipo.seleccionarFila(evt);
    }
    
    public void requisito(){
         for (int i = 0; i < tab_anticipo.getTotalFilas(); i++) {
              tab_anticipo.getValor(i, "ide_solicitud_anticipo");
              tab_anticipo.getValor(i, "ci_solicitante");
              tab_anticipo.getValor(i, "aprobado_solicitante");
              tab_anticipo.getValor(i, "numero_cuotas_anticipo");
              tab_anticipo.getValor(i, "val_cuo_adi");
              tab_anticipo.getValor(i, "id_distributivo");
              
              if(tab_anticipo.getValor(i, "aprobado_solicitante")!=null){
                         if(tab_anticipo.getValor(i, "aprobado_solicitante").equals("1")){
                             if(tab_anticipo.getValor(i, "id_distributivo").equals("1")){//detalle solicitud de empleados
                                 if((utilitario.getDia(tab_anticipo.getValor(i, "fecha_anticipo")))>10){
                                     if((Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))+(utilitario.getMes(tab_anticipo.getValor(i, "fecha_anticipo"))))>12){
                                            for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                            if (!tab_dato.isEmpty()) {
                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"val_cuo_adi")), 
                                                       Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);

                                                   }else{
                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                                       }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                               }
                                        }
                                           Double valorp=0.0,valors=0.0,totall=0.0;
                                           valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                           valors= Double.parseDouble(tab_anticipo.getValor(i,"val_cuo_adi"))+valorp ;
                                           totall = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valors ;
                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));
                                     }else{
                                      for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                     }
                                        Double valor1=0.0,total=0.0;
                                        valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                        total = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valor1 ;
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), total, 
                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));
                                          }//
                                 }else {
                                      if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1){
                                       if((Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))+(utilitario.getMes(tab_anticipo.getValor(i, "fecha_anticipo"))))-1>12){
                                           for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                            if (!tab_dato.isEmpty()) {
                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"val_cuo_adi")), 
                                                       Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);

                                                   }else{
                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                                       }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                               }
                                        }
                                           Double valorp=0.0,valors=0.0,totall=0.0;
                                           valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                           valors= Double.parseDouble(tab_anticipo.getValor(i,"val_cuo_adi"))+valorp ;
                                           totall = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valors ;
                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));
                                         }else{
                                      for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                     }
                                        Double valor1=0.0,total=0.0;
                                        valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                        total = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valor1 ;
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), total, 
                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));
                                              }
                                                                            }else{
                                         iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), String.valueOf(1), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")), 
                                                  Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
                                     iAnticipos.actuaCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")));
                                     }
                                 }//
                             }else{//detalle para solicitud de trabajadores
                                 if(Integer.parseInt(tab_anticipo.getValor("numero_cuotas_anticipo"))>1){
                                    for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                  Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                   } 
                                      Double valor1=0.0,total=0.0;
                                      valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                      total = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valor1 ;
                                      iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), total, 
                                                  Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));   
                                   
                             }else{
                                     iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), String.valueOf(1), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")), 
                                                  Integer.parseInt(tab_anticipo.getValor("ide_periodo_anticipo_inicial")));
                                     iAnticipos.actuaCalculo(Integer.parseInt(tab_anticipo.getValor("ide_solicitud_anticipo")), Double.parseDouble(tab_anticipo.getValor("valor_anticipo")));
                             }
                             
                         }
                             iAnticipos.actuaSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"), 1,  utilitario.getVariable("NICK"));
                             iAnticipos.actualizSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"));//actualiza cuota de cobro
                         }else if(tab_anticipo.getValor(i, "aprobado_solicitante").equals("0")){//Solicitud Denegada
                                    iAnticipos.negarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"));
                         }
                 }
         }
         tab_anticipo.actualizar();
         utilitario.agregarMensaje("Formularios Aprobados", "");
         txt_num_listado.limpiar();
         utilitario.addUpdate("txt_num_listado");
         tab_listado.actualizar();
    }
    
    public void save_lista(){
        txt_num_listado.setDisabled(true); //Desactiva el cuadro de texto
        String numero = iAnticipos.listaMax();
        String valor,anio,num;
        Integer cantidad=0;
        anio=String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
        valor=numero.substring(10,15);
        cantidad = Integer.parseInt(valor)+1;
        if(numero!=null){
            if(cantidad>=0 && cantidad<=9){
                num = "0000"+String.valueOf(cantidad);
                String cadena = "LIST"+"-"+anio+"-"+num;
                txt_num_listado.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
               } else if(cantidad>=10 && cantidad<=99){
                          num = "000"+String.valueOf(cantidad);
                          String cadena = "LIST"+"-"+anio+"-"+num;
                        txt_num_listado.setValue(cadena + "");
                        utilitario.addUpdate("txt_num_listado");
                         }else if(cantidad>=100 && cantidad<=999){
                                   num = "00"+String.valueOf(cantidad);
                                   String cadena = "LIST"+"-"+anio+"-"+num;
                                    txt_num_listado.setValue(cadena + "");
                                    utilitario.addUpdate("txt_num_listado");
                                  }else if(cantidad>=1000 && cantidad<=9999){
                                            num = "0"+String.valueOf(cantidad);
                                            String cadena = "LIST"+"-"+anio+"-"+num;
                                            txt_num_listado.setValue(cadena + "");
                                            utilitario.addUpdate("txt_num_listado");
                                           }else if(cantidad>=10000 && cantidad<=99999){
                                                     num = String.valueOf(cantidad);
                                                     String cadena = "LIST"+"-"+anio+"-"+num;
                                                    txt_num_listado.setValue(cadena + "");
                                                    utilitario.addUpdate("txt_num_listado");
                                                    }
        }
        save_listado();
    }
    
    public void save_listado(){
         for (int i = 0; i < tab_listado.getTotalFilas(); i++) {
             
              tab_listado.getValor(i, "ide_solicitud_anticipo");
              tab_listado.getValor(i, "ide_empleado_solicitante");
              tab_listado.getValor(i, "ci_solicitante");
              
              iAnticipos.llenarListado(Integer.parseInt(tab_listado.getValor(i, "ide_solicitud_anticipo")), Integer.parseInt(tab_listado.getValor(i, "ide_empleado_solicitante")),tab_listado.getValor(i, "ci_solicitante"), txt_num_listado.getValue()+"");
         }
         utilitario.agregarMensaje("Listado Guardado", "");
         utilitario.addUpdate("txt_num_listado");
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
        cal_fecha.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "LISTA DE PAGO":
                set_lista.dibujar();
                set_lista.getTab_seleccion().limpiar();
           break;
        }
    }
    
    public void aceptoAprobacion() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTA DE PAGO":
                TablaGenerica tab_dato = iAnticipos.ide_listado(Integer.parseInt(set_lista.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                    TablaGenerica tab_dato1 = iAnticipos.login_solicitud(tab_dato.getValor("ide_listado"));
                    if (!tab_dato1.isEmpty()) {
                        TablaGenerica tab_dato2 = iAnticipos.director();
                        if (!tab_dato2.isEmpty()) {
                            TablaGenerica tab_dato3 = iAnticipos.getUsuario(tab_dato1.getValor("login_ingre_solicitud"));
                            if (!tab_dato3.isEmpty()) {
                                TablaGenerica tab_dato4 = iAnticipos.getUsuario(tab_dato1.getValor("login_aprob_solicitud"));
                                if (!tab_dato4.isEmpty()) {
                                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                                    p_parametros.put("listado", tab_dato.getValor("ide_listado")+"");
                                    p_parametros.put("quien_elabora", tab_dato3.getValor("NOM_USUA")+"");
                                    p_parametros.put("quien_aprueba", tab_dato4.getValor("NOM_USUA")+"");
                                    p_parametros.put("director", tab_dato2.getValor("nombres")+"");
                                    rep_reporte.cerrar();
                                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                    sef_formato.dibujar();
                                } else {
                                    utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                                }
                            } else {
                                utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                            }
                        } else {
                            utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                        } 
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
                } else {
                    utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                }
                break;
        }
    }
    
    
    
    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_listado() {
        return tab_listado;
    }

    public void setTab_listado(Tabla tab_listado) {
        this.tab_listado = tab_listado;
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

    public SeleccionTabla getSet_lista() {
        return set_lista;
    }

    public void setSet_lista(SeleccionTabla set_lista) {
        this.set_lista = set_lista;
    }
    
}
