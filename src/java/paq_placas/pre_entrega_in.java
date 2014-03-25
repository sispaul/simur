/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_entrega_in extends Pantalla{
Integer consulta,placa,vehiculo,servicio;
String cedula,factura;

private Calendario cal_fechaini = new Calendario();
private Calendario cal_fechafin = new Calendario();

private Tabla tab_entrega = new Tabla ();
private Tabla set_propietario = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla set_servicio = new Tabla();
private Tabla set_vehiculo = new Tabla();

private Panel pan_opcion = new Panel();
private Efecto efecto = new Efecto();

private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();
private Grid grid = new Grid();
private Etiqueta etifec = new Etiqueta();
private Grid grid1 = new Grid();
private Etiqueta etifec1 = new Etiqueta();
private Dialogo dia_dialogoe = new Dialogo();
private Grid grid_de = new Grid();
private Grid gride = new Grid();
private Dialogo dia_dialogoDes = new Dialogo();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid_de1 = new Grid();

    ///REPORTES
private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Map p_parametros = new HashMap();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);

    public pre_entrega_in() {

        etifec.setStyle("font-size:16px;color:blue");
        etifec.setValue("SELECCIONE RANGO DE FECHAS");
        grid.setColumns(4);
        //campos fecha       
        grid.getChildren().add(new Etiqueta("FECHA INICIAL"));
        grid.getChildren().add(cal_fechaini);
        grid.getChildren().add(new Etiqueta("   FECHA FINAL"));
        grid.getChildren().add(cal_fechafin);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setHeader("TIPO DE VEHICULO");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
               
        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        tab_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("aceptarPlaca");
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        tab_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        System.out.println(tab_consulta.getValor("NICK_USUA"));
        tab_entrega.getColumna("USU_ENTREGA").setLectura(true);
        tab_entrega.getColumna("USU_ENTREGA").setNombreVisual("USUARIO DE ENTREGA");
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setNombreVisual("FECHA ENTREGA");
        tab_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC PROPIETARIO");
        tab_entrega.getColumna("CEDULA_PERSONA_RETIRA").setNombreVisual("C.I QUIEN RETIRA");
        tab_entrega.getColumna("CEDULA_PERSONA_RETIRA").setMetodoChange("aceptoretiro");
        tab_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setNombreVisual("NOMBRE QUIEN RETIRA");
        tab_entrega.getGrid().setColumns(2);
        tab_entrega.setTipoFormulario(true); 
        tab_entrega.dibujar();
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("ENTREGA DE PLACAS");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");

        PanelTabla tab_e = new PanelTabla();
        tab_e.setPanelTabla(tab_entrega);
        pan_opcion.getChildren().add(efecto);
        pan_opcion.getChildren().add(tab_e);
        agregarComponente(pan_opcion);
        
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("BUSCAR PROPIETARIO"); //titulo
        dia_dialogoe.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoDialogo1");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
       
                         /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("PLACAS - ASIGNACION DE SERVICIOS"); //titulo
        dia_dialogo1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_de1.setColumns(4);
        grid_de1.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo1);
    }

    public void aceptarPlaca(){
//        tab_aprobacion.actualizar();
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_propietario);
                set_propietario.setId("set_propietario");
        set_propietario.setHeader("PROPIETARIOS PARA ENTREGAS");
        set_propietario.setSql("SELECT DISTINCT d.IDE_DETALLE_SOLICITUD,\n" +
                        "d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,\n" +
                        "p.PLACA,v.des_tipo_vehiculo,d.IDE_SOLICITUD_PLACA,\n" +
                        "e.DESCRIPCION_ESTADO,a.USU_APROBACION\n" +
                        "\n" +
                        "FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA AS d ,\n" +
                        "dbo.TRANS_PLACA AS p ,dbo.TRANS_TIPO_ESTADO AS e ,\n" +
                        "dbo.TRANS_APROBACION_PLACA AS a, dbo.trans_tipo_vehiculo v\n" +
                        "WHERE\n" +
                        "d.IDE_PLACA = p.IDE_PLACA AND\n" +
                        "p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO AND\n" +
                        "d.IDE_APROBACION_PLACA = a.IDE_APROBACION_PLACA AND\n" +
                        "d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND\n" +
                        "e.DESCRIPCION_ESTADO like'asignada'AND\n" +
                        "d.CEDULA_RUC_PROPIETARIO LIKE '"+tab_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"'");
        set_propietario.getColumna("CEDULA_RUC_PROPIETARIO").setFiltro(true);
        set_propietario.setRows(5);
        set_propietario.setTipoSeleccion(false);
//        set_propietario.dibujar();
        dia_dialogoe.setDialogo(grid_de);
        set_propietario.dibujar();
        dia_dialogoe.dibujar();
     }   
    public void aceptoValores(){
        if (set_propietario.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = ser_Placa.getEntrega(Integer.parseInt(set_propietario.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("NOMBRE_PROPIETARIO"));
                consulta = Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
                cedula = tab_dato.getValor("CEDULA_RUC_PROPIETARIO");
                placa = Integer.parseInt(tab_dato.getValor("IDE_PLACA"));
                vehiculo=Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO"));
                servicio=Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO"));
                factura=tab_dato.getValor("NUMERO_FACTURA");
                eti_etiqueta.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta.setValue(tab_dato.getValor("PLACA"));
                eti_etiqueta1.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta1.setValue("PLACA:");
                pan_opcion.getChildren().add(eti_etiqueta1);
                pan_opcion.getChildren().add(eti_etiqueta);
                utilitario.addUpdate("tab_entrega");
                utilitario.addUpdate("pan_opcion");
                dia_dialogoe.cerrar();
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos", "");
            }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
     }
    
        public void aceptoretiro(){
            System.out.println(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
         if (utilitario.validarCedula(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_entrega");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_entrega");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        }
     }
    
    
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
           case "ENTREGA PLACA":
               aceptoDialogo();
               break;
           case "REPORTE DIARIO ENTREGA PLACA":
               aceptoDialogo();
               break;
           case "REPORTE MES ENTREGA PLACA":
                dia_dialogoe.Limpiar();
                dia_dialogoe.setDialogo(etifec);
                dia_dialogoe.setDialogo(grid);
                
                grid_de.getChildren().add(set_vehiculo);
                dia_dialogoe.setDialogo(grid_de);
                set_vehiculo.dibujar();
                dia_dialogoe.dibujar();
               break;
                
        }
    }     
       
        public void aceptoDialogo(){
        if (utilitario.isFechasValidas(cal_fechaini.getFecha(), cal_fechafin.getFecha())){
        switch (rep_reporte.getNombre()) {
               case "ENTREGA PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("cedula", tab_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"");
                      p_parametros.put("vehiculo", vehiculo);
                      p_parametros.put("servicio", servicio);
                      p_parametros.put("factura", factura);
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;
               case "REPORTE DIARIO ENTREGA PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("pide_fechai", utilitario.getFechaActual());
                      p_parametros.put("pide_fechaf", utilitario.getFechaActual());
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;
               case "REPORTE MES ENTREGA PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("pide_fechai", cal_fechaini.getFecha());
                      p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                      p_parametros.put("vehiculo", Integer.parseInt(set_vehiculo.getValorSeleccionado()+""));
                      p_parametros.put("servicio", Integer.parseInt(set_servicio.getValorSeleccionado()+""));
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                       dia_dialogo1.cerrar();
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
                      System.out.println(sef_formato);
               break;                   
        }
        }else{
            utilitario.agregarMensaje("Fechas", "Rango de Fechas no valido");
        }
    }

    public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        grid_de1.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setHeader("TIPO DE SERVICIO");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo1.setDialogo(grid_de1);
        set_servicio.dibujar();
        dia_dialogo1.dibujar();
        dia_dialogoe.cerrar();
    }
    
    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }
    
    @Override
    public void insertar() {
      tab_entrega.insertar();
    }

    @Override
    public void guardar() {
    if (tab_entrega.guardar()) {
            if (guardarPantalla().isEmpty()) {
                ser_Placa.actualizarDS(Integer.parseInt(tab_entrega.getValor("ide_entrega_placa")),consulta);
                tab_entrega.actualizar();
                utilitario.addUpdate("tab_entrega");
                ser_Placa.actualizarDE(consulta, cedula, placa);
            }
        }else {
            utilitario.agregarMensajeInfo("No Puede Guardar Placa Entregada", "");
        }
    }

    @Override
    public void eliminar() {
        tab_entrega.eliminar();
    }

    public Tabla getTab_entrega() {
        return tab_entrega;
    }

    public void setTab_entrega(Tabla tab_entrega) {
        this.tab_entrega = tab_entrega;
    }

    public Tabla getSet_propietario() {
        return set_propietario;
    }

    public void setSet_propietario(Tabla set_propietario) {
        this.set_propietario = set_propietario;
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

    public Tabla getSet_servicio() {
        return set_servicio;
    }

    public void setSet_servicio(Tabla set_servicio) {
        this.set_servicio = set_servicio;
    }

    public Tabla getSet_vehiculo() {
        return set_vehiculo;
    }

    public void setSet_vehiculo(Tabla set_vehiculo) {
        this.set_vehiculo = set_vehiculo;
    }
    
}
