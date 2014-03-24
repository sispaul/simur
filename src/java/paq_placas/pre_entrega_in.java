/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
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
Integer consulta,placa;
String cedula;
private Tabla tab_entrega = new Tabla ();
private Tabla set_propietario = new Tabla();
private Tabla tab_consulta = new Tabla();
private Panel pan_opcion = new Panel();
private Efecto efecto = new Efecto();
private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();

private Dialogo dia_dialogoe = new Dialogo();
private Grid grid_de = new Grid();
private Grid gride = new Grid();

    ///REPORTES
private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Map p_parametros = new HashMap();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);

    public pre_entrega_in() {

        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        tab_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("aceptarPlaca");
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        tab_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
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
        dia_dialogoe.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar(); 
       
                         /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
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
                System.err.println(tab_dato.getValor("nombre"));
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
        switch (rep_reporte.getNombre()) {
           case "ENTREGA DE PLACA":
               aceptoDialogo();
               break;      
                
        }
    }     
       
        public void aceptoDialogo(){
        switch (rep_reporte.getNombre()) {
               case "ENTREGA DE PLACA":
//                    if ((set_entrega.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                      p_parametros = new HashMap();
                      p_parametros.put("cedula", tab_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"");
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
//                    dia_dialogoEN.cerrar();
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
//                    } else {
//                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
//                    }
               break;                   
        }

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
    
}
