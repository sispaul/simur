/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_inventario_placa extends  Pantalla{

    Integer codigo;

private Tabla tab_ingreso = new Tabla();
private Tabla tab_placa = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla set_vehiculo = new Tabla();
private Tabla set_servicio = new Tabla();
private Tabla set_tipo = new Tabla();
private Tabla set_estado = new Tabla();
private SeleccionTabla set_acta = new SeleccionTabla();

private Panel pan_opcion = new Panel();
private Efecto efecto = new Efecto();

private Dialogo dia_dialogoe = new Dialogo();
private Dialogo dia_dialogop = new Dialogo();
private Dialogo dia_dialogot = new Dialogo();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid_de = new Grid();
private Grid gride = new Grid();
private Grid grid_dp = new Grid();
private Grid grid_dt = new Grid();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();



    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    private Texto txt_acta= new Texto();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_inventario_placa() {      

        /****CREACION DE OBJETOS TABLAS****/

        tab_ingreso.setId("tab_ingreso");//tabla acta de solicitud
        tab_ingreso.setTabla("trans_ingresos_placas", "ide_ingreso_placas", 1);
        tab_ingreso.setHeader("ACTA DE INGRESO");
        tab_ingreso.getColumna("IDE_INGRESO_PLACAS").setNombreVisual("ID");
        tab_ingreso.getColumna("ANO").setValorDefecto(utilitario.getAnio(utilitario.getFechaActual())+"");
        tab_ingreso.getColumna("ANO").setLectura(true);
        tab_ingreso.getColumna("NUMERO_ACTA").setMascara("999-9999-**-***");
        tab_ingreso.getColumna("fecha_envio_acta").setValorDefecto(utilitario.getFechaActual());
        tab_ingreso.getColumna("fecha_registro_acta").setValorDefecto(utilitario.getFechaActual());      
        tab_ingreso.getColumna("fecha_registro_acta").setLectura(true);
        tab_ingreso.getColumna("usu_ingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_ingreso.getColumna("usu_ingreso").setVisible(false);
        tab_ingreso.agregarRelacion(tab_placa);
        tab_ingreso.setTipoFormulario(true);
        tab_ingreso.getGrid().setColumns(4);
        tab_ingreso.dibujar();
        PanelTabla tabi = new PanelTabla();
        tabi.setPanelTabla(tab_ingreso);

        tab_placa.setId("tab_placa");//tabla ingreso de inventario placas
        tab_placa.setTabla("trans_placa", "ide_placa", 2);
        tab_placa.getColumna("cedula_ruc_propietario").setVisible(false);
        tab_placa.getColumna("nombre_propietario").setVisible(false);
        tab_placa.getColumna("fecha_entrega_placa").setVisible(false);
        tab_placa.getColumna("placa").setMayusculas(true);
        tab_placa.getColumna("placa").setUnico(true);
        tab_placa.getColumna("fecha_registro_placa").setValorDefecto(utilitario.getFechaActual());
        tab_placa.getColumna("fecha_registro_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setLectura(true);
        tab_placa.getColumna("ide_tipo_servicio").setLectura(true);
        tab_placa.getColumna("ide_tipo_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_estado").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setCombo("SELECT ide_tipo_vehiculo,descripcion_vehiculo FROM trans_vehiculo_tipo");
        tab_placa.getColumna("ide_tipo_servicio").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_placa.getColumna("ide_tipo_placa").setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");
        tab_placa.getColumna("ide_tipo_estado").setVisible(false);
        tab_placa.dibujar();
        PanelTabla tabp = new PanelTabla();
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("SERVICIOS DE PLACAS A INGRESAR");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");
        
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("SELECCIONAR SERVICIOS PARA PLACAS");
        bot_bus.setIcon("ui-icon-comment");
        bot_bus.setMetodo("buscarServicio");
        pan_opcion.getChildren().add(bot_bus);
        
	pan_opcion.getChildren().add(efecto);
        tabp.getChildren().add(pan_opcion);
        tabp.setPanelTabla(tab_placa);
        
        Division div = new Division();
        div.dividir2(tabi, tabp, "30%", "h");
        agregarComponente(div);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        /****** CREACION DE OBJETOS DIALOGOS PARA SELECCION ******/
        
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogoe.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("20%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
 //campos fecha       
        gri_busca.getChildren().add(new Etiqueta("FECHA INICIO"));
        gri_busca.getChildren().add(cal_fechaini);
        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL"));
        gri_busca.getChildren().add(cal_fechafin);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);

        
        dia_dialogop.setId("dia_dialogop");
        dia_dialogop.setTitle("SELECIONAR ACTA PARA REPORTE"); //titulo
        dia_dialogop.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogop.setHeight("30%");//siempre porcentaje   alto
        dia_dialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogop.getBot_aceptar().setMetodo("aceptoInventario");
        grid_dp.setColumns(4);
        agregarComponente(dia_dialogop);
        
        dia_dialogot.setId("dia_dialogot");
        dia_dialogot.setTitle("PLACAS - ESTADO"); //titulo
        dia_dialogot.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogot.setHeight("20%");//siempre porcentaje   alto
        dia_dialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogot.getBot_aceptar().setMetodo("aceptoInventario");
        grid_dt.setColumns(4);
        agregarComponente(dia_dialogot);

        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("PLACAS - ASIGNACION DE SERVICIOS"); //titulo
        dia_dialogo1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoValores1");
        grid_de1.setColumns(4);
        grid_de1.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo1);
        
        /****DECLARACION DE OBJETOS TIPO SELECCION*****/
        
        set_tipo.setId("set_tipo");
        set_tipo.setHeader("TIPO DE PLACA");
        set_tipo.setSql("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");
        set_tipo.getColumna("DESCRIPCION_PLACA").setNombreVisual("TIPO");
        set_tipo.setRows(5);
        set_tipo.setTipoSeleccion(false);
        set_tipo.dibujar();
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setHeader("TIPO DE VEHICULO");
        set_vehiculo.setSql("select ide_tipo_vehiculo,descripcion_vehiculo from trans_vehiculo_tipo");
        set_vehiculo.getColumna("descripcion_vehiculo").setNombreVisual("VEHICULO");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
        
        
        set_estado.setId("set_estado");
        set_estado.setHeader("ESTADO PLACA");
        set_estado.setSql("SELECT IDE_TIPO_ESTADO,DESCRIPCION_ESTADO FROM TRANS_TIPO_ESTADO WHERE DESCRIPCION_ESTADO BETWEEN 'asignada' AND 'disponible'");
        set_estado.getColumna("DESCRIPCION_ESTADO").setNombreVisual("ESTADO");
        set_estado.setRows(5);
        set_estado.setTipoSeleccion(false);
        set_estado.dibujar();
                                 /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        set_acta.setId("set_acta");
        set_acta.setSeleccionTabla("SELECT IDE_INGRESO_PLACAS,FECHA_ENVIO_ACTA,FECHA_REGISTRO_ACTA,NUMERO_ACTA,ENTREGADO_ACTA,RECIBIDO_ACTA,USU_INGRESO,ANO FROM TRANS_INGRESOS_PLACAS where IDE_INGRESO_PLACAS=-1", "IDE_INGRESO_PLACAS");
        set_acta.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_acta.getTab_seleccion().setRows(10);
        set_acta.setRadio();
        set_acta.getGri_cuerpo().setHeader(gri_busca);
        set_acta.getBot_aceptar().setMetodo("aceptoInventario");
        set_acta.setHeader("BUSCAR ACTA DE PLACAS");
        agregarComponente(set_acta);
    }

       public void buscarEmpresa() {
        if (cal_fechaini.getValue() != null && cal_fechaini.getValue().toString().isEmpty() == false && cal_fechafin.getValue() != null && cal_fechafin.getValue().toString().isEmpty() == false) {
            set_acta.getTab_seleccion().setSql("SELECT IDE_INGRESO_PLACAS,FECHA_ENVIO_ACTA,FECHA_REGISTRO_ACTA,NUMERO_ACTA,ENTREGADO_ACTA,RECIBIDO_ACTA,USU_INGRESO,ANO FROM TRANS_INGRESOS_PLACAS \n" +
                                                "where FECHA_REGISTRO_ACTA BETWEEN '"+cal_fechaini.getFecha()+"' AND '"+cal_fechafin.getFecha()+"'");
            set_acta.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }
    
    public void buscarServicio(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_vehiculo);
        grid_de.getChildren().add(set_tipo);
        dia_dialogoe.setDialogo(grid_de);
        set_tipo.dibujar();
        set_vehiculo.dibujar();
        dia_dialogoe.dibujar();
    }
    
        public void aceptoValores() {
        if (set_vehiculo.getValorSeleccionado()!= null) {
                if (set_tipo.getValorSeleccionado()!= null) {
                        tab_placa.getColumna("ide_tipo_vehiculo").setValorDefecto(set_vehiculo.getValorSeleccionado());
                        tab_placa.getColumna("ide_tipo_placa").setValorDefecto(set_tipo.getValorSeleccionado());
                        aceptoDialogo1();
                        dia_dialogoe.cerrar();
                 }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    
     public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        grid_de1.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setHeader("TIPO DE SERVICIO");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_vehiculo_tipo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("SERVICIO");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo1.setDialogo(grid_de1);
        set_servicio.dibujar();
        dia_dialogo1.dibujar();
    }
    
        public void aceptoValores1() {
            if (set_servicio.getValorSeleccionado()!= null) {
                        codigo = 1;
                        tab_placa.getColumna("ide_tipo_servicio").setValorDefecto(set_servicio.getValorSeleccionado());
                        tab_placa.insertar();
                        dia_dialogo1.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    } 
     
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
         if (tab_ingreso.guardar()) {
            if (tab_placa.guardar()) {
                guardarPantalla();
                ser_Placa.placaNew();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
/*
 * CREACION DE REPORTES
 */
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fechaini.setFechaActual();
        switch (rep_reporte.getNombre()) {
           case "INGRESO ACTA":
                set_acta.dibujar();
                cal_fechaini.limpiar();
                cal_fechafin.limpiar();
                set_acta.getTab_seleccion().limpiar();
               break;
           case "REPORTE PLACAS":
                dia_dialogot.Limpiar();
                
                grid_dt.getChildren().add(set_estado);
                dia_dialogot.setDialogo(grid_dt);
                set_estado.dibujar();
                dia_dialogot.dibujar();
               break;
                
        }
    }     
       
  public void aceptoInventario(){
        switch (rep_reporte.getNombre()) {
               case "INGRESO ACTA":
                      if (set_acta.getValorSeleccionado()!= null) {
                          TablaGenerica tab_dato = ser_Placa.getIDActa(Integer.parseInt(set_acta.getValorSeleccionado()));
                            if (!tab_dato.isEmpty()) {
                            p_parametros = new HashMap();
                            p_parametros.put("acta", tab_dato.getValor("NUMERO_ACTA"));
                            p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                            rep_reporte.cerrar();
                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                            sef_formato.dibujar();
                            } else {
                                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                            }
                      }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        } 
               break;
               case "REPORTE PLACAS":
                   if (set_estado.getValorSeleccionado()!= null) {
                      p_parametros = new HashMap();
                      p_parametros.put("estado", Integer.parseInt(set_estado.getValorSeleccionado()+""));
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
                      }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
               break;
        }
    }

    
    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }
    
    public Tabla getTab_ingreso() {
        return tab_ingreso;
    }

    public void setTab_ingreso(Tabla tab_ingreso) {
        this.tab_ingreso = tab_ingreso;
    }

    public Tabla getTab_placa() {
        return tab_placa;
    }

    public void setTab_placa(Tabla tab_placa) {
        this.tab_placa = tab_placa;
    }

    public Tabla getSet_vehiculo() {
        return set_vehiculo;
    }

    public void setSet_vehiculo(Tabla set_vehiculo) {
        this.set_vehiculo = set_vehiculo;
    }

    public Tabla getSet_servicio() {
        return set_servicio;
    }

    public void setSet_servicio(Tabla set_servicio) {
        this.set_servicio = set_servicio;
    }

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_estado() {
        return set_estado;
    }

    public void setSet_estado(Tabla set_estado) {
        this.set_estado = set_estado;
    }

    public Calendario getCal_fechaini() {
        return cal_fechaini;
    }

    public void setCal_fechaini(Calendario cal_fechaini) {
        this.cal_fechaini = cal_fechaini;
    }

    public Texto getTxt_acta() {
        return txt_acta;
    }

    public void setTxt_acta(Texto txt_acta) {
        this.txt_acta = txt_acta;
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

    public SeleccionTabla getSet_acta() {
        return set_acta;
    }

    public void setSet_acta(SeleccionTabla set_acta) {
        this.set_acta = set_acta;
    }
    
}
