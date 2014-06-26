/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
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
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author p-sistemas
 */
public class pre_placas_pendientes extends Pantalla{

    String nombre,empresa,descrip,cedula;
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_entrega = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Panel pan_opcion3 = new Panel();
    
    //DIALOGO
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    
        //PARA CANTIDAD QUE SE RETIRA
    private Texto  txt_numero = new Texto();
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    
     ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_placas_pendientes() {
        
        //muestra el usuario que esta logeado
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("trans_encab_entregas", "codigo", 1);
        List lista1 = new ArrayList();
        Object filaa[] = {
            "PARTICULAR", "PARTICULAR"
        };
        Object filab[] = {
            "GESTOR", "GESTOR"
        };
        lista1.add(filaa);;
        lista1.add(filab);;
        tab_cabecera.getColumna("tipo").setRadio(lista1, "PARTICULAR");
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(2);
        tab_cabecera.agregarRelacion(tab_entrega);
        tab_cabecera.dibujar();
        PanelTabla tpc=new PanelTabla();
        tpc.setPanelTabla(tab_cabecera);
        
        pan_opcion1.getChildren().add(tpc);
        
        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_entregar_placa", "IDE_ENTREGA_PLACA", 2);
        tab_entrega.getColumna("cedula_quien_retira").setMetodoChange("quienEs");
        tab_entrega.getColumna("fecha_retiro").setValorDefecto(utilitario.getFechaActual());
        tab_entrega.getColumna("placa").setMetodoChange("revisar");
        tab_entrega.getColumna("fecha_retiro").setLectura(true);
        tab_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_entrega.dibujar();
        PanelTabla tpe=new PanelTabla();
        tpe.setPanelTabla(tab_entrega);
        pan_opcion2.getChildren().add(tpe);
        
        Boton bot_new = new Boton();
        bot_new.setValue("NUEVO");
        bot_new.setIcon("ui-icon-document");
        bot_new.setMetodo("insertar");
        
        Boton bot_save = new Boton();
        bot_save.setValue("GUARDAR");
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("guardar");
        
        Boton bot_delete = new Boton();
        bot_delete.setValue("ELIMINAR");
        bot_delete.setIcon("ui-icon-closethick");
        bot_delete.setMetodo("eliminar");
        
        Boton bot_print = new Boton();
        bot_print.setValue("Imprimir");
        bot_print.setIcon("ui-icon-print");
        bot_print.setMetodo("abrirListaReportes");
        
        pan_opcion3.getChildren().add(bot_new);
        pan_opcion3.getChildren().add(bot_save);
        pan_opcion3.getChildren().add(bot_delete);
        pan_opcion3.getChildren().add(bot_print);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.getChildren().add(pan_opcion1);
        pan_opcion.getChildren().add(pan_opcion2);
//        pan_opcion.getChildren().add(pan_opcion3);

        agregarComponente(pan_opcion);
        
        //CREACION DE OBJETOS DIALOGOS PARA SELECCION
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CANATIDAD A RETIRAR"); //titulo
        dia_dialogoe.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("15%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("GeneraCantida");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);
        
        /**CONFIGURACIÓN DE OBJETO REPORTE*/
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
    }
    
        public void quienEs(){
        if(tab_cabecera.getValor("tipo").equals("PARTICULAR")){ //PARTICULAR
                IDquien();
//                tab_entrega.setValor("PARTICULAR_EMPRESA","PARTICULAR");
                utilitario.addUpdate("tab_entrega");
            }else if(tab_cabecera.getValor("tipo").equals("GESTOR")){//GESTOR
                        QueS();
                    }
    }
    
   public void IDquien(){
       if (utilitario.validarCedula(tab_entrega.getValor("cedula_quien_retira"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_entrega.getValor("cedula_quien_retira"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("nombre_quien_retira", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_entrega");
                } else {
                         utilitario.agregarMensajeInfo("Cédula ingresada no existe en la base de datos del municipio", " Ingrese el nombre");
                        }
        } else if (utilitario.validarRUC(tab_entrega.getValor("cedula_quien_retira"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_entrega.getValor("cedula_quien_retira"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("nombre_quien_retira", tab_dato.getValor("razon_social"));
                utilitario.addUpdate("tab_entrega");
                } else {
                        utilitario.agregarMensajeInfo("RUC ingresado no existe en la base de datos del municipio", "Ingrese el nombre");
                        }
        } else  {
                utilitario.agregarMensajeError("El Número de IDENTIFICACION no es válido", "");
                }
    }
    
    public void QueS(){
        cedula = tab_entrega.getValor("cedula_quien_retira");
         if (utilitario.validarCedula(tab_entrega.getValor("cedula_quien_retira"))) {
            TablaGenerica tab_dato = serviciobusqueda.getGestors(tab_entrega.getValor("cedula_quien_retira"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra 
                nombre = tab_dato.getValor("NOMBRE_GESTOR");
                tab_entrega.setValor("nombre_quien_retira", nombre);
                utilitario.addUpdate("tab_entrega");
                cantidad();
                } else {
                         utilitario.agregarMensajeInfo("Cédula ingresada no existe en la base de datos", " Ingrese el nombre");
                        }
        } else  {
                utilitario.agregarMensajeError("Gestor no corresponde", "");
                }
    }
     
 public void cantidad(){
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        txt_numero.setSize(4);
        grid_de.getChildren().add(new Etiqueta("INGRESE CANTIDAD:"));
        grid_de.getChildren().add(txt_numero);
        dia_dialogoe.setDialogo(grid_de);
        dia_dialogoe.dibujar();
    }
    
    public void GeneraCantida(){
       Integer  valinicio = Integer.parseInt(txt_numero.getValue()+"");
        for (int i = 1; i < valinicio; i++) {
                             tab_entrega.insertar();
                                tab_entrega.setValor("cedula_quien_retira", cedula);
                                tab_entrega.setValor("nombre_quien_retira", nombre);
                             dia_dialogoe.cerrar();
                         }
    }
    
    public void revisar(){
      TablaGenerica tab_dato = ser_Placa.getPlacaBus(tab_entrega.getValor("placa"));
        if(!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Placa Entregada", tab_dato.getValor("placa"));
            utilitario.agregarMensaje("Propietario", tab_dato.getValor("NOMBRE_PROPIETARIO"));
            utilitario.addUpdate("tab_entrega");
            }else {
                    TablaGenerica tab_dato1 = ser_Placa.getPlacaBusc(tab_entrega.getValor("placa"));
                        if(!tab_dato1.isEmpty()) {
                            utilitario.agregarMensaje("Placa Entregada", tab_dato1.getValor("placa"));
                            utilitario.addUpdate("tab_entrega");
                            }else {
                                   TablaGenerica tab_dato2 = ser_Placa.getPlacaEntrega(tab_entrega.getValor("placa"));
                                    if(!tab_dato2.isEmpty()) {
                                        tab_entrega.setValor("cedula_propietario", tab_dato2.getValor("CEDULA_RUC_PROPIETARIO"));
                                        tab_entrega.setValor("nombre_propietario", tab_dato2.getValor("NOMBRE_PROPIETARIO"));
                                        tab_entrega.setValor("ide_detalle_solicitud", tab_dato2.getValor("IDE_DETALLE_SOLICITUD"));
                                        utilitario.addUpdate("tab_entrega");
                                        }else {
                                               utilitario.agregarMensajeInfo("Placa No Asignada", "O NO EXISTE");
                                                }
                                    }
                    }
    }
    
    public void ejeGuardar(){
        TablaGenerica tab_dato = ser_Placa.getIDEntrega(Integer.parseInt(tab_entrega.getValor("IDE_DETALLE_SOLICITUD")));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra  
                System.err.println("Hola");
                ser_Placa.actualizarDS1(Integer.parseInt(tab_dato.getValor("IDE_ENTREGA_PLACA")),Integer.parseInt(tab_entrega.getValor("IDE_DETALLE_SOLICITUD")),tab_entrega.getValor("NOMBRE_quien_RETIRA"), tab_entrega.getValor("CEDULA_quien_RETIRA"));
                actualizarDE();
            } else {
                utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
            }
    }   
         
    public void actualizarDE(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(tab_entrega.getValor("placa"));
         System.err.println("Hola1");
         if (!tab_dato.isEmpty()) {
        ser_Placa.actualizarDE(Integer.parseInt(tab_entrega.getValor("IDE_DETALLE_SOLICITUD")), tab_entrega.getValor("CEDULA_PROPIETARIO"), Integer.parseInt(tab_dato.getValor("IDE_PLACA")));
        actuaMetodo();
        } else {
                utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
            }
    }
    
    public void actuaMetodo(){
         System.err.println("Hola2");
        ser_Placa.actualFinal(Integer.parseInt(tab_entrega.getValor("IDE_DETALLE_SOLICITUD")),tab_entrega.getValor("CEDULA_quien_RETIRA"),tab_entrega.getValor("NOMBRE_quien_RETIRA"));
        actualizaMetodo();
    }
    
    public void actualizaMetodo(){
        TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(tab_entrega.getValor("placa"));
         System.err.println("Hola3");
         if (!tab_dato.isEmpty()) {
        ser_Placa.actualFinalPlaca(Integer.parseInt(tab_dato.getValor("IDE_PLACA")), Integer.parseInt(tab_dato.getValor("IDE_PLACA")), tab_consulta.getValor("NICK_USUA"));
        utilitario.agregarMensajeInfo("Campo Actualizado", "");
         } else {
                utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
            }
         }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_cabecera.guardar()) {
            if (tab_entrega.guardar()) {
                guardarPantalla();
                TablaGenerica tab_dato = ser_Placa.getPlacaActualEli(tab_entrega.getValor("placa"));
                if (!tab_dato.isEmpty()) {
                    System.err.println("Ingreso");
                ejeGuardar();
                } else {
                    //utilitario.agregarMensajeInfo("Proceso no ejcutado no encuentra ide de entrega", "");
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_cabecera.isFocus()) {
                tab_cabecera.eliminar();
                } else if (tab_entrega.isFocus()) {
                            tab_entrega.eliminar();
                            }
    }

//CREACION DE REPORTES
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "IMPRESION COMPROBANTE":
                aceptoInventario();
               break;
        }
    }     
       
  public void aceptoInventario(){
        switch (rep_reporte.getNombre()) {
               case "IMPRESION COMPROBANTE":
                            p_parametros = new HashMap();
                            p_parametros.put("num_tramite", Integer.parseInt(tab_cabecera.getValor("CODIGO")+""));
                            p_parametros.put("cedula", tab_entrega.getValor("CEDULA_QUIEN_RETIRA")+"");
                            p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                            rep_reporte.cerrar();
                            sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                            sef_formato.dibujar();
               break;
        }
    }
  
    public Tabla getTab_entrega() {
        return tab_entrega;
    }

    public void setTab_entrega(Tabla tab_entrega) {
        this.tab_entrega = tab_entrega;
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
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
    
}
