/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud extends Pantalla{
//Tablas para Seleccion de atributos
    private Tabla set_tipo = new Tabla();
    private Tabla set_gestor = new Tabla();
    private Tabla set_gestores = new Tabla();
    private Tabla set_empresa = new Tabla();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_persona = new Tabla();
//Dialogos para seleccion de atributos
    private Dialogo dia_dialogoG = new Dialogo();
    private Dialogo dia_dialogoGP = new Dialogo();
    private Dialogo dia_dialogoGE = new Dialogo();
    private Dialogo dia_dialogoGG = new Dialogo();
    private Grid gridG = new Grid();
    private Grid gridG1 = new Grid();
    private Grid gridGP = new Grid();
    private Grid gridGP1 = new Grid();
    private Grid gridGE = new Grid();
    private Grid gridGE1 = new Grid();
    private Grid gridGG = new Grid();
    private Grid gridGG1 = new Grid();
//Autocompletar datos en pantalla    
    private AutoCompletar aut_gestor = new AutoCompletar();
    
//Dibujar Paneles y Menú
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    public pre_ingreso_solicitud() {
        //BOTON DE SELECCION DE OPCIONES
        Boton bot_ingreso = new Boton();
        bot_ingreso.setValue("SELECCIONAR GESTOR");
        bot_ingreso.setIcon("ui-icon-comment");
        bot_ingreso.setMetodo("aceptoDialogo");
        bar_botones.agregarBoton(bot_ingreso);
        
        //BUSQUEDA Y LLENO AUTOMATICO EN CAMPOS
        
        aut_gestor.setId("aut_empresas");
        aut_gestor.setAutoCompletar("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO FROM TRANS_GESTOR");
        aut_gestor.setMetodoChange("filtrarGestor");
        aut_gestor.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscar Gestor:"));
        bar_botones.agregarComponente(aut_gestor);
        
//        //BOTON DE LIMPIEZA
//        Boton bot_limpiar = new Boton();
//        bot_limpiar.setIcon("ui-icon-cancel");
//        bot_limpiar.setMetodo("limpiar");
//        bar_botones.agregarBoton(bot_limpiar);
//        bar_botones.agregarReporte();
        
        ////Configurar Seleccion Tipo de Gestor
        set_tipo.setId("set_tipo");
        set_tipo.setSql("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR ORDER BY DESCRIPCION_GESTOR");
        set_tipo.setRows(5);
        set_tipo.setTipoSeleccion(false);
        set_tipo.dibujar();
        
        set_empresa.setId("set_empresa");
        set_empresa.setSql("SELECT IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA,RUC_EMPRESA FROM TRANS_COMERCIAL_AUTOMOTORES");
        set_empresa.getColumna("RUC_EMPRESA").setFiltro(true);
        set_empresa.getColumna("NOMBRE_EMPRESA").setFiltro(true);
        set_empresa.setRows(10);
        set_empresa.setTipoSeleccion(false);
        set_empresa.dibujar();
        
        //Creacion de Objetos Empresa
        dia_dialogoG.setId("dia_dialogoG");
        dia_dialogoG.setTitle("SOLICITUD - SELECCIONE TIPO"); //titulo
        dia_dialogoG.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogoG.setHeight("25%");//siempre porcentaje   alto
        dia_dialogoG.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoG.getBot_aceptar().setMetodo("seleccionDialogo");
        
         gridG.setColumns(2);
         gridG.getChildren().add(new Etiqueta("SELECCIONE TIPO DE SOLICITUD"));
         agregarComponente(dia_dialogoG);
        
        dia_dialogoGE.setId("dia_dialogoGE");
        dia_dialogoGE.setTitle("EMPRESA - SELECCIONE SU EMPRESA"); //titulo
        dia_dialogoGE.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoGE.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoGE.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoGE.getBot_aceptar().setMetodo("aceptoGestors");
        
         gridGE.setColumns(2);
         gridGE.getChildren().add(new Etiqueta("SELECCIONE EMPRESA"));
         agregarComponente(dia_dialogoGE);         

        dia_dialogoGG.setId("dia_dialogoGG");
        dia_dialogoGG.setTitle("GESTORES - SELECCIONE GESTOR"); //titulo
        dia_dialogoGG.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoGG.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoGG.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoGG.getBot_aceptar().setMetodo("aceptoGestor");
        
        gridGG.setColumns(2);
        gridGG.getChildren().add(new Etiqueta("GESTOR EMPRESA"));
        agregarComponente(dia_dialogoGG);

        //Creacion de Objetos Particulares
         dia_dialogoGP.setId("dia_dialogoGP");
        dia_dialogoGP.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogoGP.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoGP.setHeight("50%");//siempre porcentaje   alto
        dia_dialogoGP.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoGP.getBot_aceptar().setMetodo("aceptoPersona1");
        
        gridGP.setColumns(2);
        gridGP.getChildren().add(new Etiqueta("TIPO DE PLACA"));
        agregarComponente(dia_dialogoGP);

        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("SOLICITUD MATRICULA");
        agregarComponente(div_division);
        dibujarSolicitud();
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
    }

     private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("INGRESO SOLICITUD");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("PEDIDO DE PLACA");
        itm_datos_empl.setIcon("ui-icon-note");
        itm_datos_empl.setMetodo("dibujarSolicitud");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("REQUISITOS DE PEDIDO");
        itm_permisos.setIcon("ui-icon-bookmark");
        itm_permisos.setMetodo("dibujarRequisitos");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);
        
    }
    
     public void aceptoDialogo() {
        dia_dialogoG.Limpiar();
        dia_dialogoG.setDialogo(gridG);
        gridG1.getChildren().add(set_tipo);
        dia_dialogoG.setDialogo(gridG1);
        set_tipo.dibujar();
        dia_dialogoG.dibujar();
    }
    public void seleccionDialogo() {
        if(set_tipo.getValorSeleccionado().equals("5")) {
             if (set_tipo.getValorSeleccionado()!= null) {            
                    aceptoEmpresa();
                    dia_dialogoG.cerrar();
                }else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                    }  
        } else if(set_tipo.getValorSeleccionado().equals("6")){
            aceptoPersona();
            dia_dialogoG.cerrar();
        }else if(set_tipo.getValorSeleccionado().equals("7")){
            aceptoPersona();
            dia_dialogoG.cerrar();
        }else if(set_tipo.getValorSeleccionado().equals("8")){
            aceptoPersona();
            dia_dialogoG.cerrar();
        }
    }
    
    /******SELECCION DE GESTORES POR EMPRESA******/
   public void  aceptoEmpresa(){
        dia_dialogoGE.Limpiar();
        dia_dialogoGE.setDialogo(gridGE);
        gridGE1.getChildren().add(set_empresa);
        dia_dialogoGE.setDialogo(gridGE1);
        set_empresa.dibujar();
        dia_dialogoGE.dibujar();
   }
   
      public void  aceptoGestores(){
        dia_dialogoGG.Limpiar();
        dia_dialogoGG.setDialogo(gridGG);
        gridGG1.getChildren().add(set_gestores);
        set_gestores.setId("set_gestores");
        set_gestores.setSql("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO\n" 
                            +"FROM TRANS_GESTOR WHERE IDE_TIPO_GESTOR = "+set_tipo.getValorSeleccionado()+" and IDE_COMERCIAL_AUTOMOTORES ="+set_empresa.getValorSeleccionado());
        set_gestores.getColumna("CEDULA_GESTOR").setFiltro(true);
        set_gestores.setRows(5);
        set_gestores.setTipoSeleccion(false);
        dia_dialogoGG.setDialogo(gridGG1);
        set_gestores.dibujar();
        dia_dialogoGG.dibujar();
   }
    
       public void aceptoGestors() {
            if (set_empresa.getValorSeleccionado()!= null) {
                        aceptoGestores();
                        dia_dialogoGE.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    
       public void aceptoGestor() {
            if (set_gestores.getValorSeleccionado()!= null) {
                        dia_dialogoGG.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }

    /******SELECCION DE GESTORES POR EMPRESA******/
       public void aceptoPersona(){
        dia_dialogoGP.Limpiar();
        dia_dialogoGP.setDialogo(gridGP);
        gridGP1.getChildren().add(set_gestor);
        set_gestor.setId("set_gestor");
        set_gestor.setSql("SELECT IDE_GESTOR,CEDULA_GESTOR,NOMBRE_GESTOR,ESTADO\n" 
                                    +"FROM TRANS_GESTOR WHERE IDE_TIPO_GESTOR ="+set_tipo.getValorSeleccionado());
        set_gestor.getColumna("CEDULA_GESTOR").setFiltro(true);
        set_gestor.setRows(5);
        set_gestor.setTipoSeleccion(false);

        dia_dialogoGP.setDialogo(gridGP1);
        set_gestor.dibujar();
        dia_dialogoGP.dibujar();
       }
       
       public void aceptoPersona1() {
            if (set_gestores.getValorSeleccionado()!= null) {
                System.out.println("HOLA");
                        dia_dialogoGP.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    /******DIBUJAR SOLICITUDA Y REQUERIMIENTOS******/
       
     private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
//        aut_gestor.limpiar();
//        utilitario.addUpdate("aut_gestor");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void dibujarSolicitud(){
        str_opcion = "0";
        limpiarPanel();
        
//        tab_persona.setId("tab_persona");
//        tab_persona.setTabla("trans_gestor", "ide_gestor", 1);
//        tab_persona.agregarRelacion(tab_solicitud);
//        tab_persona.setTipoFormulario(true);
//        tab_persona.dibujar();
//        PanelTabla tabp = new PanelTabla();
//        tabp.setPanelTabla(tab_persona);
//        pan_opcion.setTitle("SOLICITUD DE PLACAS");
//        pan_opcion.getChildren().add(tab_persona);
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_solicitud);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 2);
        tab_detalle.getColumna("NOMBRE_PROPIETARIO").setMayusculas(true);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. TRAMITE");
        tab_detalle.setTipoFormulario(true);
        agregarComponente(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle); 
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
//        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
//                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
//                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        
        Grupo gru = new Grupo();
//        gru.getChildren().add(tabp);
        gru.getChildren().add(tabp1);
        gru.getChildren().add(tabp2);
        gru.getChildren().add(tabp3);
        pan_opcion.getChildren().add(gru);
    }
    
    public void dibujarRequisitos(){
        str_opcion = "1";
        limpiarPanel(); 
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("TRANS_DETALLE_REQUISITOS_SOLICITUD", "IDE_DETALLE_REQUISITOS_SOLICITUD", 3);
//        tab_requisito.getColumna("ide_tipo_requisito").setCombo("SELECT r.IDE_TIPO_REQUISITO,r.DECRIPCION_REQUISITO FROM TRANS_TIPO_REQUISITO r\n" 
//                                                                +"INNER JOIN TRANS_TIPO_SERVICIO s ON r.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO\n" 
//                                                                +"INNER JOIN trans_tipo_vehiculo v ON s.ide_tipo_vehiculo = v.ide_tipo_vehiculo\n");
        tab_requisito.setHeader("REQUISITOS DE PEDIDO DE PLACA");
        tab_requisito.dibujar();
        PanelTabla tabp3=new PanelTabla();
        tabp3.setPanelTabla(tab_requisito);
        pan_opcion.getChildren().add(tabp3);
//        Grupo gru1 = new Grupo();
//        gru1.getChildren().add(tabp3);
//        pan_opcion.getChildren().add(gru1);
         System.out.println("Dibuja");
    }
    
//     public void filtrarGestor(SelectEvent evt) {
//        //Filtra el cliente seleccionado en el autocompletar
//        aut_gestor.onSelect(evt);
//        dibujarPanel();
//    }
//
//    private void dibujarPanel() {
//        if (str_opcion.equals("0") || str_opcion.isEmpty()) {
//            dibujarSolicitud();
//        } else if (str_opcion.equals("1")) {
//            dibujarRequisitos();
//        } 
//        utilitario.addUpdate("pan_opcion");
//    }
    
    @Override
    public void insertar() {
    if (str_opcion.equals("0")) {
        if (tab_solicitud.isFocus()) {
                tab_solicitud.insertar();
            } else if (tab_detalle.isFocus()) {
                tab_detalle.insertar();
            }  
        }else if (str_opcion.equals("1")) {
            if (tab_requisito.isFocus()) {
                tab_requisito.insertar();
            }
        }
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_gestor() {
        return set_gestor;
    }

    public void setSet_gestor(Tabla set_gestor) {
        this.set_gestor = set_gestor;
    }

    public Tabla getSet_gestores() {
        return set_gestores;
    }

    public void setSet_gestores(Tabla set_gestores) {
        this.set_gestores = set_gestores;
    }

    public Tabla getSet_empresa() {
        return set_empresa;
    }

    public void setSet_empresa(Tabla set_empresa) {
        this.set_empresa = set_empresa;
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

    public AutoCompletar getAut_gestor() {
        return aut_gestor;
    }

    public void setAut_gestor(AutoCompletar aut_gestor) {
        this.aut_gestor = aut_gestor;
    }
    
}
