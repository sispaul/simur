/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import paq_transportes.ejb.servicioPlaca;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud extends Pantalla{
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_gestor = new Tabla();
    private Dialogo dia_dialogoEN = new Dialogo();
    private Grid grid_en = new Grid();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    Tabulador tab_tabulador = new Tabulador();
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    public pre_ingreso_solicitud() {
        tab_tabulador.setId("tab_tabulador");
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("cargarEmpresa");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("NOMBRE GESTOR");
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setVisible(false);
        tab_solicitud.getColumna("NOMBRE_EMPRESA").setNombreVisual("NOMBRE EMPRESA");
        tab_solicitud.getColumna("RUC_EMPRESA").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setNombreVisual("Nro. SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setNombreVisual("TIPO DE SOLICITUD");
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setCombo("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
  
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setMayusculas(true);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setNombreVisual("NOMBRE PROPIETARIO");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC");
        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("buscaPersona");
        tab_detalle.getColumna("NUMERO_FACTURA").setNombreVisual("Nro.FACTURA");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setNombreVisual("TIPO DE VEHICULO");
        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setCombo("SELECT ide_tipo_vehiculo,des_tipo_vehiculo FROM trans_tipo_vehiculo\n" 
                                                                +"WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("TIPO DE SERVICIO");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setMetodoChange("ingresoRequisitos");
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. TRAMITE");
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle); 
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setIdCompleto("tab_tabulador:tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
//        tab_requisito.getColumna("ide_tipo_requisito").setLectura(true);
        tab_requisito.getColumna("CONFIRMAR_REQUISITO").setNombreVisual("CONFIRMAR");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        
        tab_tabulador.agregarTab("REQUISITOS", tabp3);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tabp1, tabp2, tab_tabulador, "30%", "50%", "H");
        agregarComponente(div_division);
        
        //Configurando el dialogo
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("GESTORES - SELECCIONE GESTOR DE EMPRESA"); //titulo
        dia_dialogoEN.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoValores");
        
         grid_en.setColumns(2);
         grid_en.getChildren().add(new Etiqueta("SELECCIONE GESTOR"));
        agregarComponente(dia_dialogoEN);   
    }
    
    public void buscaPersona(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_Placa.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_Placa.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else  {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
        }
    }

        public void cargarEmpresa() {
         if (utilitario.validarCedula(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_Placa.getGestor1(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("NOMBRE_GESTOR"));
                utilitario.addUpdate("tab_solicitud");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_Placa.getGestor(tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("NOMBRE_EMPRESA"));
                utilitario.addUpdate("tab_solicitud");
                aceptoDialogoe();
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else  {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
        }

    }

   public void ingresoRequisitos() {
       
       tab_solicitud.guardar();
       utilitario.addUpdate("tab_solicitud");
       tab_detalle.guardar();
       utilitario.addUpdate("tab_detalle");
       ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
       utilitario.addUpdate("tab_requisito");
       
    }
        
         public void aceptoDialogoe() {
        dia_dialogoEN.Limpiar();
        dia_dialogoEN.setDialogo(gride);
        grid_de.getChildren().add(set_gestor);
        set_gestor.setId("set_gestor");
        set_gestor.setSql("SELECT g.IDE_GESTOR,g.CEDULA_GESTOR,g.NOMBRE_GESTOR,g.ESTADO FROM TRANS_GESTOR g,TRANS_COMERCIAL_AUTOMOTORES c\n" 
                           +"WHERE g.IDE_COMERCIAL_AUTOMOTORES = c.IDE_COMERCIAL_AUTOMOTORES AND c.RUC_EMPRESA ="+tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO"));
        set_gestor.getColumna("CEDULA_GESTOR").setFiltro(true);
        set_gestor.setRows(5);
        set_gestor.setTipoSeleccion(false);
        dia_dialogoEN.setDialogo(grid_de);
        set_gestor.dibujar();
        dia_dialogoEN.dibujar();
    }  
    
        public void aceptoValores() {
        if (set_gestor.getValorSeleccionado()!= null) {
                        tab_solicitud.getColumna("ide_gestor").setValorDefecto(set_gestor.getValorSeleccionado());
                        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setVisible(true);
                        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setCombo("SELECT TRANS_GESTOR.IDE_GESTOR, TRANS_GESTOR.NOMBRE_GESTOR\n" +
                        "FROM TRANS_GESTOR WHERE TRANS_GESTOR.IDE_COMERCIAL_AUTOMOTORES ="+set_gestor.getValorSeleccionado()+"");
                        dia_dialogoEN.cerrar();
                        utilitario.addUpdate("tab_requisito");
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }    
        
    @Override
    public void insertar() {
      if (tab_solicitud.isFocus()) {
         tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.insertar();
                     } else if (tab_requisito.isFocus()) {
                                  tab_requisito.insertar();
                            }  
    }

    @Override
    public void guardar() {
    if (tab_solicitud.guardar()) {
               utilitario.addUpdate("tab_solicitud");
            if (tab_detalle.guardar()) {
                utilitario.addUpdate("tab_detalle");
                 guardarPantalla();
                } 
                }else if(tab_tabulador.equals("REQUISITOS")){
                        if (tab_requisito.guardar()) {
                            guardarPantalla();
                            utilitario.addUpdate("tab_requisito");
                            }
                }
    }

    @Override
    public void eliminar() {
     if (tab_solicitud.isFocus()) {
         tab_solicitud.eliminar();
            } else if (tab_detalle.isFocus()) {
                        tab_detalle.eliminar();
                     } else if (tab_requisito.isFocus()) {
                                  tab_requisito.eliminar();
                            } 
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public Tabla getTab_requisito() {
        return tab_requisito;
    }

    public void setTab_requisito(Tabla tab_requisito) {
        this.tab_requisito = tab_requisito;
    }

    public Tabla getSet_gestor() {
        return set_gestor;
    }

    public void setSet_gestor(Tabla set_gestor) {
        this.set_gestor = set_gestor;
    }
    
}
 