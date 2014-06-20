/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
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
    
    //PANEL DE BUSQUEDA
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    
    //DIALOGO
    private Dialogo dia_dialogoe = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    
        //PARA CANTIDAD QUE SE RETIRA
    private Texto  txt_numero = new Texto();
    
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    
    public pre_placas_pendientes() {
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("trans_encab_entregas", "codigo", 1);
        List lista1 = new ArrayList();
        Object filaa[] = {
            "1", "PARTIC."
        };
        Object filab[] = {
            "2", "GESTOR"
        };
        lista1.add(filaa);;
        lista1.add(filab);;
        tab_cabecera.getColumna("tipo").setRadio(lista1, "1");
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(2);
        tab_cabecera.agregarRelacion(tab_entrega);
        tab_cabecera.dibujar();
        PanelTabla tpc=new PanelTabla();
        tpc.setPanelTabla(tab_cabecera);
        
        pan_opcion1.getChildren().add(tpc);
        
        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_placas_pendientes", "codigo_entrega", 2);
        tab_entrega.getColumna("cedula_quien_retira").setMetodoChange("quienEs");
        tab_entrega.getColumna("placa").setMetodoChange("revisar");
        tab_entrega.getColumna("fecha_retiro").setValorDefecto(utilitario.getFechaActual());
        tab_entrega.getColumna("fecha_retiro").setLectura(true);        
        tab_entrega.dibujar();
        PanelTabla tpe=new PanelTabla();
        tpe.setPanelTabla(tab_entrega);
        pan_opcion2.getChildren().add(tpe);
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.getChildren().add(pan_opcion1);
        pan_opcion.getChildren().add(pan_opcion2);

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
    }
    
        public void quienEs(){
        if(tab_cabecera.getValor("tipo").equals("1")){ //PARTICULAR
                IDquien();
                tab_entrega.setValor("PARTICULAR_EMPRESA","PARTICULAR");
                utilitario.addUpdate("tab_entrega");
            }else if(tab_cabecera.getValor("tipo").equals("2")){//GESTOR
                        descrip = tab_entrega.getValor("descripcion");
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
                empresa = tab_dato.getValor("NOMBRE_EMPRESA");
                tab_entrega.setValor("nombre_quien_retira", tab_dato.getValor("NOMBRE_GESTOR"));
                tab_entrega.setValor("PARTICULAR_EMPRESA", tab_dato.getValor("NOMBRE_EMPRESA"));
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
                             dia_dialogoe.cerrar();
                         }
    }
    
    public void revisar(){
      TablaGenerica tab_dato = ser_Placa.getPlacaBus(tab_entrega.getValor("placa"));
        if(!tab_dato.isEmpty()) {
            utilitario.agregarMensaje("Placa Entregada", tab_dato.getValor("placa"));
            utilitario.addUpdate("tab_entrega");
            }else {
                    TablaGenerica tab_dato1 = ser_Placa.getPlacaBusc(tab_entrega.getValor("placa"));
                        if(!tab_dato1.isEmpty()) {
                            utilitario.agregarMensaje("Placa Entregada", tab_dato1.getValor("placa"));
                            utilitario.addUpdate("tab_entrega");
                            }else {
                                    utilitario.agregarMensajeInfo("Placa No Entregada", "");
                                    }
                    }
    }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
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
    
}
