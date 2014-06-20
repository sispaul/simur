/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
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
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_placas_inventario extends Pantalla{

    //Varibales de almacenamiento
    Integer valinicio,valfinal,moto;
    String valor,placas,letra,valor1;
    
    //Dialogos
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialogo1 = new Dialogo();
    private Dialogo dia_vehiculo = new Dialogo();
    private Dialogo dia_servicio = new Dialogo();
    private Dialogo dia_dialogor = new Dialogo();
    private Dialogo dia_dialogot = new Dialogo();
    private Dialogo dia_dialogoc = new Dialogo();
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    private Grid grid_ve = new Grid();
    private Grid grid_se = new Grid();
    private Grid grid1 = new Grid();
    private Grid grid_de1 = new Grid();
    private Grid grid_dr = new Grid();
    private Grid gridr = new Grid();
    private Grid grid_dt = new Grid();
    private Grid grid_dc = new Grid();
    private Grid gridc = new Grid();
    private Grid grid_d = new Grid();
    private Grid gris = new Grid();
    private Grid grive = new Grid();
 
    //Tablas
    private Tabla tab_ingreso = new Tabla();
    private Tabla tab_placa = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_vehiculo = new Tabla();
    private Tabla set_servicio = new Tabla();
    private Tabla set_estado = new Tabla();
    private Tabla set_colaborador = new Tabla();
    private SeleccionTabla set_acta = new SeleccionTabla();
    private SeleccionTabla set_placas = new SeleccionTabla ();

    //Recuadro
    private Panel pan_opcion = new Panel();
    private Efecto efecto = new Efecto();
    
    //Texto de ingreso
    private Texto txt_plaserie= new Texto();
    private Texto txt_numinicio= new Texto();
    private Texto txt_numfinal= new Texto();
    private Texto txt_moto= new Texto();
    private Texto txt_comentario= new Texto();
    private Texto txt_solicita= new Texto();
    
    //conector a clase logica
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Calendario
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    
    //conexion otra base
    private Conexion con_postgres= new Conexion();
    private Conexion conexion= new Conexion();
    private int i;
    
    public pre_placas_inventario() {
        conexion.NOMBRE_MARCA_BASE="sqlserver";
        conexion.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        //Persistencia a la postgres.
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE="postgres";
        
        Boton bot_devolver = new Boton();
        bot_devolver.setValue("DEVOLUCIONES ANT");
        bot_devolver.setIcon("ui-icon-arrowreturnthick-1-w");
        bot_devolver.setMetodo("placas");
//        bar_botones.agregarBoton(bot_devolver);
        
        //muestra el usuario que esta logeado
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //dibujo de tablas
        tab_ingreso.setId("tab_ingreso");//tabla acta de solicitud
        tab_ingreso.setTabla("trans_ingresos_placas", "ide_ingreso_placas", 1);
        tab_ingreso.getColumna("IDE_INGRESO_PLACAS").setNombreVisual("ID");
        tab_ingreso.getColumna("ANO").setValorDefecto(utilitario.getAnio(utilitario.getFechaActual())+"");
        tab_ingreso.getColumna("ANO").setLectura(true);
        tab_ingreso.getColumna("RECIBIDO_ACTA").setMetodoChange("buscaColaborador");
        tab_ingreso.getColumna("fecha_envio_acta").setValorDefecto(utilitario.getFechaActual());
        tab_ingreso.getColumna("fecha_registro_acta").setValorDefecto(utilitario.getFechaActual());      
        tab_ingreso.getColumna("fecha_registro_acta").setLectura(true);
        tab_ingreso.getColumna("usu_ingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_ingreso.getColumna("usu_ingreso").setVisible(false);
        tab_ingreso.getColumna("IDE_TIPO_PLACA").setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");
        tab_ingreso.agregarRelacion(tab_placa);
        tab_ingreso.setTipoFormulario(true);
        tab_ingreso.getGrid().setColumns(4);
        tab_ingreso.dibujar();
        PanelTabla tabi = new PanelTabla();
        tabi.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        tabi.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tabi.setPanelTabla(tab_ingreso);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("INVENTARIO DE PLACAS A INGRESAR");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");
        //boton de servicios
        Boton bot_bus = new Boton();
        bot_bus.setId("bot_bus");
        bot_bus.setValue("SELECCIONAR SERVICIOS PARA PLACAS");
        bot_bus.setIcon("ui-icon-comment");
        bot_bus.setMetodo("buscarServicio");
                
        tab_placa.setId("tab_placa");//tabla ingreso de inventario placas
        tab_placa.setTabla("trans_placas", "ide_placa", 2);
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
        tab_placa.getColumna("IDE_TIPO_ESTADO2").setVisible(false);
        tab_placa.getColumna("ide_tipo_vehiculo").setCombo("SELECT ide_tipo_vehiculo,descripcion_vehiculo FROM trans_vehiculo_tipo");
        tab_placa.getColumna("ide_tipo_servicio").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_placa.getColumna("ide_tipo_placa").setVisible(false);
        tab_placa.getColumna("ide_tipo_placa2").setVisible(false);
        tab_placa.getColumna("fecha_definitiva_placa").setVisible(false);
        tab_placa.getColumna("ide_tipo_estado").setVisible(false);
        tab_placa.getColumna("USU_ENTREGA").setVisible(false);
        tab_placa.getColumna("FECHA_ENTREGA_FINAL").setVisible(false);
        tab_placa.setRows(15);
        tab_placa.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        tabp.getMenuTabla().getItem_insertar().setRendered(false);//nucontextual().setrendered(false);
        tabp.setPanelTabla(tab_placa);
        
        pan_opcion.getChildren().add(tabi);
        pan_opcion.getChildren().add(bot_bus);
        pan_opcion.getChildren().add(tabp);
        agregarComponente(pan_opcion);
   
        //CREACION DE OBJETOS DIALOGOS PARA SELECCION
        
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogoe.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("25%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(4);
        grid_de.getChildren().add(new Etiqueta("SELECCIONE AUTOMOTOR"));
        agregarComponente(dia_dialogoe);
                
        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("PLACAS - ASIGNACION DE SERVICIOS"); //titulo
        dia_dialogo1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoValores1");
        grid_de1.setColumns(4);
        grid_de1.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo1);
        
        dia_vehiculo.setId("dia_vehiculo");
        dia_vehiculo.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_vehiculo.setWidth("30%"); //siempre en porcentajes  ancho
        dia_vehiculo.setHeight("20%");//siempre porcentaje   alto
        dia_vehiculo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_vehiculo.getBot_aceptar().setMetodo("aceptoVehiculo");
        grid_ve.setColumns(4);
        grid_ve.getChildren().add(new Etiqueta("SELECCIONE AUTOMOTOR"));
        agregarComponente(dia_vehiculo);
                
        dia_servicio.setId("dia_servicio");
        dia_servicio.setTitle("PLACAS - ASIGNACION DE SERVICIOS"); //titulo
        dia_servicio.setWidth("30%"); //siempre en porcentajes  ancho
        dia_servicio.setHeight("20%");//siempre porcentaje   alto
        dia_servicio.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_servicio.getBot_aceptar().setMetodo("placasD");
        grid_se.setColumns(4);
        grid_se.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_servicio);
        
        dia_dialogor.setId("dia_dialogor");
        dia_dialogor.setTitle("PLACAS - RANGO DE INGRESO"); //titulo
        dia_dialogor.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogor.setHeight("18%");//siempre porcentaje   alto
        dia_dialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogor.getBot_aceptar().setMetodo("aceptoRango");
        grid_dr.setColumns(4);
        agregarComponente(dia_dialogor);
                
        dia_dialogot.setId("dia_dialogot");
        dia_dialogot.setTitle("PLACAS - ESTADO"); //titulo
        dia_dialogot.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogot.setHeight("20%");//siempre porcentaje   alto
        dia_dialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogot.getBot_aceptar().setMetodo("aceptoInventario");
        grid_dt.setColumns(4);
        agregarComponente(dia_dialogot);
        
        dia_dialogoc.setId("dia_dialogoc");
        dia_dialogoc.setTitle("BUSCAR COLABORADOR"); //titulo
        dia_dialogoc.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoc.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoc.getBot_aceptar().setMetodo("aceptoColaborador");
        grid_dc.setColumns(4);
        agregarComponente(dia_dialogoc);
        
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("SELECCIONE PLACAS PARA DEVOLUCIÒN ANT"); //titulo
        dia_dialogo.setWidth("40%"); //siempre en porcentajes  ancAho
        dia_dialogo.setHeight("55%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("devolverAnt");
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
        
        //para fechas
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        gri_busca.getChildren().add(new Etiqueta("FECHA INICIO"));
        gri_busca.getChildren().add(cal_fechaini);
        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL"));
        gri_busca.getChildren().add(cal_fechafin);
        //boton de buscar
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEmpresa");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca.getChildren().add(bot_buscar);
        
        //tabla de seleccion
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
        
        //objeto/tabla seleccion
        set_acta.setId("set_acta");
        set_acta.setSeleccionTabla("SELECT IDE_INGRESO_PLACAS,FECHA_ENVIO_ACTA,FECHA_REGISTRO_ACTA,NUMERO_ACTA,ENTREGADO_ACTA,RECIBIDO_ACTA,USU_INGRESO,ANO FROM TRANS_INGRESOS_PLACAS where IDE_INGRESO_PLACAS=-1", "IDE_INGRESO_PLACAS");
        set_acta.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_acta.getTab_seleccion().setRows(10);
        set_acta.setRadio();
        set_acta.getGri_cuerpo().setHeader(gri_busca);
        set_acta.getBot_aceptar().setMetodo("aceptoInventario");
        set_acta.setHeader("BUSCAR ACTA DE PLACAS");
        agregarComponente(set_acta);
        
        Grid gri_busca1 = new Grid();
        txt_solicita.setSize(50);
        gri_busca1.getChildren().add(new Etiqueta("QUIEN SOLICITA"));
        gri_busca1.getChildren().add(txt_solicita);
        txt_comentario.setSize(100);
        gri_busca1.getChildren().add(new Etiqueta("COMENTARIO"));
        gri_busca1.getChildren().add(txt_comentario);
        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("buscaVehiculo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1); 
        
        set_placas.setId("set_placas");
        set_placas.setSeleccionTabla("SELECT p.IDE_PLACA,p.PLACA,v.DESCRIPCION_VEHICULO,s.DESCRIPCION_SERVICIO\n" +
                                "FROM TRANS_PLACAS p,TRANS_TIPO_SERVICIO s,TRANS_VEHICULO_TIPO v\n" +
                                "WHERE p.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                                "p.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND s.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO", "IDE_PLACA");
        set_placas.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_placas.getTab_seleccion().setRows(10);
        set_placas.getGri_cuerpo().setHeader(gri_busca1);
        set_placas.getBot_aceptar().setMetodo("devolverAnt");
        set_placas.setHeader("PLACAS PARA DEVOLUCIÒN");
        agregarComponente(set_placas);
        
       /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
    }

public void buscarServicio(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_vehiculo);
        dia_dialogoe.setDialogo(grid_de);
        set_vehiculo.dibujar();
        dia_dialogoe.dibujar();
    }
    
public void aceptoColaborador(){
     if (set_colaborador.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = ser_Placa.Funcionario(set_colaborador.getValorSeleccionado());
                if (!tab_dato.isEmpty()) {
                     tab_ingreso.setValor("RECIBIDO_ACTA", tab_dato.getValor("nombres"));
                      utilitario.addUpdate("tab_ingreso");
                      dia_dialogoc.cerrar();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos empleados del municipio", "");
                               }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
        
 }


        public void aceptoValores() {
        if (set_vehiculo.getValorSeleccionado()!= null) {
                        tab_placa.getColumna("ide_tipo_vehiculo").setValorDefecto(set_vehiculo.getValorSeleccionado());
                        aceptoDialogo1();                        
                        dia_dialogoe.cerrar();
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

public void buscaVehiculo(){
        dia_vehiculo.Limpiar();
        dia_vehiculo.setDialogo(grive);
        grid_ve.getChildren().add(set_vehiculo);
        dia_vehiculo.setDialogo(grid_ve);
        set_vehiculo.dibujar();
        dia_vehiculo.dibujar();
}


public void aceptoVehiculo() {
       dia_vehiculo.cerrar();
        dia_servicio.Limpiar();
        dia_servicio.setDialogo(gris);
        grid_se.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setHeader("TIPO DE SERVICIO");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_vehiculo_tipo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("SERVICIO");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_servicio.setDialogo(grid_se);
        set_servicio.dibujar();
        dia_servicio.dibujar();
    }

public void placas(){
set_placas.dibujar();
set_placas.getTab_seleccion().limpiar();
}

public void aceptoValores1() {
            if (set_servicio.getValorSeleccionado()!= null) {
                        tab_placa.getColumna("ide_tipo_servicio").setValorDefecto(set_servicio.getValorSeleccionado());
                        buscarRango();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }

public void buscarRango(){
     if (set_vehiculo.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
          if (!tab_dato.isEmpty()) {
              if(tab_dato.getValor("DESCRIPCION_VEHICULO").equals("VEHICULO")){
                  tab_placa.insertar();
                  buscarVehiculo();
                  dia_dialogo1.cerrar();
                    } else if (tab_dato.getValor("DESCRIPCION_VEHICULO").equals("MOTO")){
                            tab_placa.insertar();
                            buscarMoto();
                            dia_dialogo1.cerrar();
                            }
     } else {
             utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
            }
       }else {
             utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
}

public void limpiar(){
    txt_moto.limpiar();
    txt_numfinal.limpiar();
    txt_numinicio.limpiar();
    txt_plaserie.limpiar();
    utilitario.addUpdate("dia_dialogor");
}
    public void buscarVehiculo(){
        limpiar();
        dia_dialogor.Limpiar();
        grid_dr.getChildren().clear();
        dia_dialogor.setDialogo(gridr);
        txt_numinicio.setSize(4);
        grid_dr.getChildren().add(new Etiqueta("RANGO INICIO:"));
        grid_dr.getChildren().add(txt_numinicio);
        txt_numfinal.setSize(4);
        grid_dr.getChildren().add(new Etiqueta("RANGO FINAL:"));
        grid_dr.getChildren().add(txt_numfinal);
        txt_plaserie.setSize(3);
        grid_dr.getChildren().add(new Etiqueta("SERIE INGRESO VEHICULO:"));
        grid_dr.getChildren().add(txt_plaserie);
        dia_dialogor.setDialogo(grid_dr);
        dia_dialogor.dibujar();
    } 
    
    public void buscarMoto(){
        limpiar();
        dia_dialogor.Limpiar();
        grid_dr.getChildren().clear();
        dia_dialogor.setDialogo(gridr);
        txt_numinicio.setSize(4);
        grid_dr.getChildren().add(new Etiqueta("RANGO INICIO:"));
        grid_dr.getChildren().add(txt_numinicio);
        txt_numfinal.setSize(4);
        grid_dr.getChildren().add(new Etiqueta("RANGO FINAL:"));
        grid_dr.getChildren().add(txt_numfinal);
        txt_plaserie.setSize(3);
        grid_dr.getChildren().add(new Etiqueta("SERIE INGRESO MOTO:"));
        grid_dr.getChildren().add(txt_plaserie);
        txt_moto.setSize(1);
        grid_dr.getChildren().add(new Etiqueta("ADICIONAL MOTO:"));
        grid_dr.getChildren().add(txt_moto);
        dia_dialogor.setDialogo(grid_dr);
        dia_dialogor.dibujar();
    } 

    
public void aceptoRango(){
if (tab_ingreso.getValor("IDE_TIPO_PLACA")!= null) {
    TablaGenerica tab_dato = ser_Placa.getPlacaActual(Integer.parseInt(tab_ingreso.getValor("IDE_TIPO_PLACA")));
       if(!tab_dato.isEmpty()) {
          if(tab_dato.getValor("DESCRIPCION_PLACA").equals("PAPEL")){
             if (set_vehiculo.getValorSeleccionado()!= null) {
                TablaGenerica tab_dato1 = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
                if (!tab_dato1.isEmpty()) {
                    if(tab_dato1.getValor("DESCRIPCION_VEHICULO").equals("VEHICULO")){
                        valinicio = Integer.parseInt(txt_numinicio.getValue()+"");
                         valfinal = Integer.parseInt(txt_numfinal.getValue()+"");
                         for (int i = valinicio; i <= valfinal; i++) {
//                             if(i>=0 && i<=9){
//                                 valor = "000"+String.valueOf(i);
//                             } else if(i>=10 && i<=99){
//                                 valor = "00"+String.valueOf(i);
//                             }else if(i>=100 && i<=999){
//                                 valor = "0"+String.valueOf(i);
//                             }else if(i>=1000 && i<=9999){
//                                 valor = String.valueOf(i);
//                             }
                             valor = String.valueOf(i);
                             placas = txt_plaserie.getValue() + valor;
                             tab_placa.setValor("placa", placas);
                             tab_placa.insertar();
                             dia_dialogor.cerrar();
                         }
                          } else if (tab_dato1.getValor("DESCRIPCION_VEHICULO").equals("MOTO")){
                                 valinicio = Integer.parseInt(txt_numinicio.getValue()+"");
                                  valfinal = Integer.parseInt(txt_numfinal.getValue()+"");
                                  for (int i = valinicio; i <= valfinal; i++) {
//                                       if(i>=0 && i<=9){
//                                            valor = "00"+String.valueOf(i);
//                                        } else if(i>=10 && i<=99){
//                                            valor = "0"+String.valueOf(i);
//                                        }else if(i>=100 && i<=999){
//                                            valor = String.valueOf(i);
//                                        }
                                      valor = String.valueOf(i);
                                      placas = txt_plaserie.getValue() + valor + txt_moto.getValue();
                                      tab_placa.setValor("placa", placas);
                                      tab_placa.insertar();
                                      dia_dialogor.cerrar();
                                        }
                                  }
                } else {
                        utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                       }
             }else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                   }
                   
           }else if(tab_dato.getValor("DESCRIPCION_PLACA").equals("DEFINITIVA")){
                    aceptarProceso();
                    dia_dialogor.cerrar();
                     } 
          }else {
                 utilitario.agregarMensajeInfo("No Existen Coincidencias ", "");
                }
 }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }                   
    }


public void aceptarProceso(){
for (int i=Integer.parseInt(txt_numinicio.getValue()+"");i<=Integer.parseInt(txt_numfinal.getValue()+"");i++){
//    if(i>=0 && i<=9){
//                                                valor = "000"+String.valueOf(i);
//                                            } else if(i>=10 && i<=99){
//                                                valor = "00"+String.valueOf(i);
//                                            }else if(i>=100 && i<=999){
//                                                valor = "0"+String.valueOf(i);
//                                            }else if(i>=1000 && i<=9999){
//                                                valor = String.valueOf(i);
//                                            }
        String campo = txt_plaserie.getValue() + String.valueOf(i);
        TablaGenerica tab_placav = new TablaGenerica();
//        System.err.println(campo);
        tab_placav.setSql("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS WHERE PLACA LIKE '" +campo+ "'");
        tab_placav.ejecutarSql();
        
//        if(i>=0 && i<=9){
//                                                        valor1 = "00"+String.valueOf(i);
//                                                    } else if(i>=10 && i<=99){
//                                                        valor1 = "0"+String.valueOf(i);
//                                                    }else if(i>=100 && i<=999){
//                                                        valor1 = String.valueOf(i);
//                                                    }
        String campo1 = txt_plaserie.getValue() + String.valueOf(i) + txt_moto.getValue();
        TablaGenerica tab_placam = new TablaGenerica();
//        System.err.println(campo1);
        tab_placam.setSql("SELECT IDE_PLACA,PLACA FROM TRANS_PLACAS WHERE PLACA LIKE '" +campo1+ "'");
        tab_placam.ejecutarSql();
        
  if (set_vehiculo.getValorSeleccionado()!= null) {
         TablaGenerica tab_dato = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
              if (!tab_dato.isEmpty()) {
                      if(tab_dato.getValor("DESCRIPCION_VEHICULO").equals("VEHICULO")){
                          if(tab_placav.getTotalFilas()!=0 ){
                                if (set_vehiculo.getValorSeleccionado()!= null) {
                                    TablaGenerica tab_dato1 = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
                                    if (!tab_dato1.isEmpty()) {
                                        if(tab_dato1.getValor("DESCRIPCION_VEHICULO").equals("VEHICULO")){
//                                            if(i>=0 && i<=9){
//                                                valor = "000"+String.valueOf(i);
//                                            } else if(i>=10 && i<=99){
//                                                valor = "00"+String.valueOf(i);
//                                            }else if(i>=100 && i<=999){
//                                                valor = "0"+String.valueOf(i);
//                                            }else if(i>=1000 && i<=9999){
//                                                valor = String.valueOf(i);
//                                            }
                                           tab_placa.insertar();
                                           placas = txt_plaserie.getValue() + String.valueOf(i);
                                           tab_placa.setValor("placa", placas);
                                           ser_Placa.placaActual(placas);
                                           
                                        }
                                    } else {
                                            utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                                            }
                                }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                            } 
                          } else if (set_vehiculo.getValorSeleccionado()!= null) {
                                    TablaGenerica tab_dato2 = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
                                    if (!tab_dato2.isEmpty()) {
                                        if(tab_dato2.getValor("DESCRIPCION_VEHICULO").equals("VEHICULO")){
//                                            if(i>=0 && i<=9){
//                                                valor = "000"+String.valueOf(i);
//                                            } else if(i>=10 && i<=99){
//                                                valor = "00"+String.valueOf(i);
//                                            }else if(i>=100 && i<=999){
//                                                valor = "0"+String.valueOf(i);
//                                            }else if(i>=1000 && i<=9999){
//                                                valor = String.valueOf(i);
//                                            }
                                            tab_placa.insertar();
                                              placas = txt_plaserie.getValue() + String.valueOf(i);
                                              tab_placa.setValor("placa", placas);
                                              ser_Placa.actualPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()), Integer.parseInt(set_servicio.getValorSeleccionado()),
                                                      Integer.parseInt(tab_ingreso.getValor("IDE_INGRESO_PLACAS")),Integer.parseInt(tab_ingreso.getValor("IDE_TIPO_PLACA")),campo);
//                                              utilitario.agregarMensaje("Placa de Vehiculo no Coincide ", campo);
                                            }
                                    } else {
                                            utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                                           }
           }else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                    }
                         } else if(tab_dato.getValor("DESCRIPCION_VEHICULO").equals("MOTO")){
                             if(tab_placam.getTotalFilas()!=0 ){
                                    if (set_vehiculo.getValorSeleccionado()!= null) {
                                        TablaGenerica tab_dato3 = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
                                            if (!tab_dato3.isEmpty()) {
                                                if (tab_dato3.getValor("DESCRIPCION_VEHICULO").equals("MOTO")){
//                                                    if(i>=0 && i<=9){
//                                                        valor = "00"+String.valueOf(i);
//                                                    } else if(i>=10 && i<=99){
//                                                        valor = "0"+String.valueOf(i);
//                                                    }else if(i>=100 && i<=999){
//                                                        valor = String.valueOf(i);
//                                                    }
                                                    tab_placa.insertar();
                                                    placas = txt_plaserie.getValue() + String.valueOf(i)+ txt_moto.getValue();;
                                                    tab_placa.setValor("placa", placas);
                                                    ser_Placa.placaActual(placas);
                                                    
                                                }
                                            } else {
                                                    utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                                                    }
                                    }else {
                                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                                            }
                             }else if (set_vehiculo.getValorSeleccionado()!= null) {
                                        TablaGenerica tab_dato4 = ser_Placa.getEstadoPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()));
                                            if (!tab_dato4.isEmpty()) {
                                                 if (tab_dato4.getValor("DESCRIPCION_VEHICULO").equals("MOTO")){
//                                                     if(i>=0 && i<=9){
//                                                        valor1 = "00"+String.valueOf(i);
//                                                    } else if(i>=10 && i<=99){
//                                                        valor1 = "0"+String.valueOf(i);
//                                                    }else if(i>=100 && i<=999){
//                                                        valor1 = String.valueOf(i);
//                                                    }
                                                      tab_placa.insertar();
                                                      placas = txt_plaserie.getValue() + String.valueOf(i)+ txt_moto.getValue();;
                                                      tab_placa.setValor("placa", placas);
                                                       ser_Placa.actualPlaca(Integer.parseInt(set_vehiculo.getValorSeleccionado()), Integer.parseInt(set_servicio.getValorSeleccionado()),
                                                      Integer.parseInt(tab_ingreso.getValor("IDE_INGRESO_PLACAS")),Integer.parseInt(tab_ingreso.getValor("IDE_TIPO_PLACA")),campo1);
                                                }
                                            } else {
                                                    utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                                                   }
                                    }else {
                                             utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                                             }

                            }
             } else {
                     utilitario.agregarMensajeInfo("No Existen Coincidencias en la base de datos", "");
                    }
    }else {
          utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
          } 
}
}

//busqueda de colaborado GADMUR
public void buscaColaborador(){
        dia_dialogoc.Limpiar();
        dia_dialogoc.setDialogo(gridc);
        grid_dc.getChildren().add(set_colaborador);
        set_colaborador.setId("set_colaborador");
        set_colaborador.setConexion(con_postgres);
        set_colaborador.setHeader("LISTA DE COLABORADORES");
        set_colaborador.setSql("SELECT cedula_pass,nombres FROM srh_empleado WHERE nombres LIKE '%"+tab_ingreso.getValor("RECIBIDO_ACTA")+"%'");
        set_colaborador.getColumna("nombres").setFiltro(true);
        set_colaborador.setRows(10);
        set_colaborador.setTipoSeleccion(false);
        dia_dialogoc.setDialogo(grid_dc);
        set_colaborador.dibujar();
        dia_dialogoc.dibujar();
}


//Mostrar actas ingresadas por fecha
public void buscarEmpresa() {
        if (cal_fechaini.getValue() != null && cal_fechaini.getValue().toString().isEmpty() == false && cal_fechafin.getValue() != null && cal_fechafin.getValue().toString().isEmpty() == false) {
            set_acta.getTab_seleccion().setSql("SELECT IDE_INGRESO_PLACAS,FECHA_ENVIO_ACTA,FECHA_REGISTRO_ACTA,NUMERO_ACTA,ENTREGADO_ACTA,RECIBIDO_ACTA,USU_INGRESO,ANO FROM TRANS_INGRESOS_PLACAS \n" +
                                                "where FECHA_REGISTRO_ACTA BETWEEN '"+cal_fechaini.getFecha()+"' AND '"+cal_fechafin.getFecha()+"'");
            set_acta.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
}

//Mostrar actas ingresadas por fecha


public void placasD(){
if (set_vehiculo.getValorSeleccionado() != null && set_vehiculo.getValorSeleccionado().isEmpty() == false && set_servicio.getValorSeleccionado() != null && set_servicio.getValorSeleccionado().isEmpty() == false) {
       set_placas.getTab_seleccion().setSql("SELECT p.IDE_PLACA,p.PLACA,v.DESCRIPCION_VEHICULO,s.DESCRIPCION_SERVICIO\n" +
                                    "FROM TRANS_PLACAS p,TRANS_TIPO_SERVICIO s,TRANS_VEHICULO_TIPO v\n" +
                                    "WHERE p.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                                    "p.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND s.IDE_TIPO_VEHICULO = v.IDE_TIPO_VEHICULO AND\n" +
                                    "p.IDE_TIPO_VEHICULO ="+set_vehiculo.getValorSeleccionado()+" AND p.IDE_TIPO_SERVICIO = "+set_servicio.getValorSeleccionado());
            set_placas.getTab_seleccion().ejecutarSql();
            dia_servicio.cerrar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }    
}

public void devolverAnt(){
  if (set_placas.getSeleccionados() != null && set_placas.getSeleccionados().isEmpty() == false) {
      TablaGenerica tab_dato = ser_Placa.getDevolucion(Integer.parseInt(set_placas.getSeleccionados()));
        if (!tab_dato.isEmpty()) {
             utilitario.agregarMensaje("SELECCIONADOS", tab_dato.getValor("PLACA"));
             set_placas.cerrar();
           } else {
                   utilitario.agregarMensajeInfo("Datos no validos", "");
                   } 
         
        } else {
               utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
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
             ser_Placa.placaNew(Integer.parseInt(tab_ingreso.getValor("IDE_TIPO_PLACA")));
             }
        }
    }

    
    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }
    
 //CREACION DE REPORTES
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        cal_fechaini.setFechaActual();
        cal_fechafin.setFechaActual();
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

    public Tabla getSet_estado() {
        return set_estado;
    }

    public void setSet_estado(Tabla set_estado) {
        this.set_estado = set_estado;
    }

    public Tabla getSet_colaborador() {
        return set_colaborador;
    }

    public void setSet_colaborador(Tabla set_colaborador) {
        this.set_colaborador = set_colaborador;
    }

    public SeleccionTabla getSet_acta() {
        return set_acta;
    }

    public void setSet_acta(SeleccionTabla set_acta) {
        this.set_acta = set_acta;
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

    public SeleccionTabla getSet_placas() {
        return set_placas;
    }

    public void setSet_placas(SeleccionTabla set_placas) {
        this.set_placas = set_placas;
    }
    
}