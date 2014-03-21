/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_transportes.ejb.servicioPlaca;
import paq_transportes.ejb.Serviciobusqueda;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud extends Pantalla{
    //DECLARACION OBJETOS TABLA
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_gestor = new Tabla();
    
    //DECLARACION OBJETO DIALOGO
    private Dialogo dia_dialogoEN = new Dialogo();
    
    // DELCARACION OBJETOS PANEL Y GRID
    private Grid grid_en = new Grid();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    private Panel pan_opcion = new Panel();
    private Efecto efecto = new Efecto();
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();
    /*
     DECLARACION DE CADENAS DE CONEXION, DE LLAMADA DE METODOS
     */
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    
    public pre_ingreso_solicitud() {
        /**
         PANTALLA CABECERA DE SOLICITUS
         */
        tab_solicitud.setId("tab_solicitud"); // NOMBRE PANTALLA
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("CEDULA_RUC_EMPRESA").setMetodoChange("cargarEmpresa");
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
        pan_opcion1.setId("pan_opcion1");
	pan_opcion1.setTransient(true);
        pan_opcion1.setHeader("INGRESO DE SOLICITUD PARA PEDIDO - PLACA");
	efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion1.getChildren().add(efecto1);
        tabp1.getChildren().add(pan_opcion1);
        tabp1.setPanelTabla(tab_solicitud);
        /**
         DETALLE SOLICITUD
         */
        tab_detalle.setId("tab_detalle"); // NOMBRE PANTALLA
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
        /**
         * REQUISITOS
         */
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.getColumna("CONFIRMAR_REQUISITO").setNombreVisual("CONFIRMAR");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("REQUISITOS PARA SOLICITUD DE PLACA");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");
        pan_opcion.getChildren().add(efecto);
        tabp3.getChildren().add(pan_opcion);
        tabp3.setPanelTabla(tab_requisito);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tabp1, tabp2, tabp3, "36%", "46%", "H");
        agregarComponente(div_division);
        
        //CONFIGURACION DE DIALOGO SELECCION DE GESTOR
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("GESTORES - SELECCIONE GESTOR DE EMPRESA"); //titulo
        dia_dialogoEN.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoValores");
        
         grid_en.setColumns(2);
         grid_en.getChildren().add(new Etiqueta("SELECCIONE GESTOR"));
        agregarComponente(dia_dialogoEN);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }
    /*
     * CREACION DE METODOS DE BUSQUEDA CON RELACION A VALIDACIONES DE CEDULA O RUC
     */
    public void buscaPersona(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
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
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        }
    }

        /*
         * LLAMADO PARA LA CREACION DE REQUISITOS AUTOMATICOS DEPENDIENTO TIPO Y SOLICITUD
         */
   public void ingresoRequisitos() {
       tab_detalle.guardar();
       guardarPantalla();
       utilitario.addUpdate("tab_detalle");
       ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
       utilitario.addUpdate("tab_requisito");
       tab_requisito.actualizar();
    }
        /*
         * DIBUJO DE DIALOGO PARA LA SELECCION DE GESTORES
         */
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
    /*
     * ACEPTACIÓN DE VALORES SELECCIONADOS DENTRO DEL DIALOGO
     */
        public void aceptoValores() {
        if (set_gestor.getValorSeleccionado()!= null) {
                        tab_solicitud.setValor("ide_gestor", set_gestor.getValorSeleccionado());
                        dia_dialogoEN.cerrar();
                        System.out.println(set_gestor.getValorSeleccionado());
                        utilitario.addUpdate("tab_solicitud");
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
                        tab_solicitud.guardar();
                         guardarPantalla();
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
//                if (tab_requisito.guardar()) {
//                    
////                    guardarPantalla();
//                    }
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

    @Override
    public void actualizar() {
        requisito();
        utilitario.addUpdate("tab_requisito");
    }
    /*
     * FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES
     */
    public void requisito(){
                          for (int i = 0; i < tab_requisito.getTotalFilas(); i++) {
                          tab_requisito.getValor(i, "CONFIRMAR_REQUISITO");
                          tab_requisito.getValor(i, "IDE_TIPO_REQUISITO");
                          tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD");
                          ser_Placa.actulizarRequisito(utilitario.StringToByte(tab_requisito.getValor(i,"CONFIRMAR_REQUISITO")),Integer.parseInt(tab_requisito.getValor(i, "IDE_DETALLE_REQUISITOS_SOLICITUD"))
                        ,Integer.parseInt(tab_requisito.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_requisito.getValor(i, "IDE_TIPO_REQUISITO")));
                          utilitario.addUpdate("tab_requisito");
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
 