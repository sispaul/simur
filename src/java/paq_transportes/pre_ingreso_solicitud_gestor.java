/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_solicitud_gestor extends Pantalla{
private Tabla tab_comercial = new Tabla();
private Tabla tab_gestor = new Tabla();
private Tabla set_gestor = new Tabla();
private Tabla set_empresa = new Tabla();
private AutoCompletar aut_busca = new AutoCompletar();
private Conexion con_ciudadania= new Conexion();
private Dialogo dia_dialogo = new Dialogo();
private Grid grid = new Grid();
private Grid grid_de = new Grid();
private Texto cedula = new Texto();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();
private Etiqueta eti_etiqueta= new Etiqueta();
private Tabla tab_solicitud = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_detalle = new Tabla();

private Dialogo dia_dialogo2 = new Dialogo();
private Dialogo dia_dialogo3 = new Dialogo();
private Grid grid2 = new Grid();
private Grid grid_de2 = new Grid();
private Grid grid3 = new Grid();
private Grid grid_de3 = new Grid();
private Tabla set_vehiculo = new Tabla();
private Tabla set_servicio = new Tabla();

@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
public pre_ingreso_solicitud_gestor() {
        
        con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadaniajdbc"));
        con_ciudadania.NOMBRE_MARCA_BASE="sqlserver";
        
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("EMPRESAS - SELECCIONAR EMPRESA"); //titulo
        dia_dialogo.setWidth("75%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("70%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setMetodo("aceptoValores");
        grid_de.setColumns(4);
        agregarComponente(dia_dialogo);
        
        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("GESTOR - EMPRESA A LA QUE PERTENECE"); //titulo
        dia_dialogo1.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("30%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoValores1");
        grid_de1.setColumns(4);
        agregarComponente(dia_dialogo1);
        
        set_empresa.setId("set_empresa");
        set_empresa.setConexion(con_ciudadania);
        set_empresa.setSql("SELECT RUC,RAZON_SOCIAL,DIRECCION,telefono FROM MAESTRO_RUC");
        set_empresa.getColumna("RAZON_SOCIAL").setFiltro(true);
        set_empresa.setRows(18);
        set_empresa.setTipoSeleccion(false);
        set_empresa.dibujar();

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_COMERCIAL_AUTOMOTORES,NOMBRE_EMPRESA,RUC_EMPRESA,DIRECCION_EMPRESA,TELEFONO_EMPRESA\n" 
                                    +"FROM TRANS_COMERCIAL_AUTOMOTORES");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
               
        bar_botones.agregarComponente(new Etiqueta("Buscar Empresa en Sistema:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);       
        
        Boton bot = new Boton();
        bot.setValue("BUSCAR EMPRESA");
        bot.setMetodo("aceptoDialogo");
        bot.setIcon("ui-icon-search");
        bar_botones.agregarBoton(bot);
        
        tab_comercial.setId("tab_comercial");
        tab_comercial.setTabla("trans_comercial_automotores", "ide_comercial_automotores", 1);
        tab_comercial.setHeader("Datos de Empresas");
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("Nombre de Establecimiento");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
        tab_comercial.getColumna("ruc_empresa").setUnico(true);
        tab_comercial.getColumna("direccion_empresa").setNombreVisual("Dirección");
        tab_comercial.getColumna("telefono_empresa").setNombreVisual("Teléfono");
        tab_comercial.getGrid().setColumns(2);
        tab_comercial.setTipoFormulario(true);
        tab_comercial.agregarRelacion(tab_gestor);
        tab_comercial.dibujar();
        PanelTabla pat_comercial = new PanelTabla();
        pat_comercial.setPanelTabla(tab_comercial);
        tab_comercial.setStyle(null);
        pat_comercial.setStyle("width:100%;overflow: auto;");
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setIdCompleto("tab_tabulador:tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.setHeader("Datos de Gestores");
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("Cedula");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("Nombre");
        tab_gestor.agregarRelacion(tab_solicitud);
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        
        Boton bot1 = new Boton();
        bot1.setValue("BUSCAR GESTOR");
        bot1.setMetodo("aceptoDialogo1");
        bot1.setIcon("ui-icon-search");
        eti_etiqueta.setStyle("font-size:12px;color:black;text-align:center;");
        eti_etiqueta.setValue("Buscar Gestor Por Cedula:");
        pat_gestor.getChildren().add(eti_etiqueta);
        pat_gestor.getChildren().add(cedula);
        pat_gestor.getChildren().add(bot1);
        pat_gestor.setPanelTabla(tab_gestor);
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setIdCompleto("tab_tabulador:tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 3);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("Descripción de Solicitud");
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. Automotores");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("Fecha");
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
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("TRANS_DETALLE_SOLICITUD_PLACA", "IDE_DETALLE_SOLICITUD", 3);
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. Tramite");
        tab_detalle.setTipoFormulario(true);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        Boton bot_placa = new Boton();
        bot_placa.setValue("INGRESO NUEVA SOLICITUD");
        bot_placa.setIcon("ui-icon-document");
        bot_placa.setMetodo("aceptoDialogo2");
        tabp2.getChildren().add(bot_placa);
        tabp2.setPanelTabla(tab_detalle); 
        
       tab_tabulador.agregarTab("REPRESENTANTE", pat_gestor);
       tab_tabulador.agregarTab("SOLICITUD DE MATRICULA", tabp1);
       tab_tabulador.agregarTab("DETALLE DE SOLICTUD DE MATRICULA", tabp2);
        
        Division div_division = new Division();
        div_division.dividir2(pat_comercial, tab_tabulador, "40%", "H");
        agregarComponente(div_division);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar(); 
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
        
        dia_dialogo2.setId("dia_dialogo2");
        dia_dialogo2.setTitle("VEHICULO - QUE SE SOLICITA"); //titulo
        dia_dialogo2.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo2.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo2.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo2.getBot_aceptar().setMetodo("aceptoValores2");
        grid_de2.setColumns(4);
        grid_de2.getChildren().add(new Etiqueta("SELECCIONE VEHICULO"));
        agregarComponente(dia_dialogo2);
        
        dia_dialogo3.setId("dia_dialogo3");
        dia_dialogo3.setTitle("SERVICIO - FUNCIONAMIENTO DE VEHICULO"); //titulo
        dia_dialogo3.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo3.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo3.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo3.getBot_aceptar().setMetodo("aceptoValores3");
        grid_de3.setColumns(4);
        grid_de3.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo3);
        
    }
    
     public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_comercial.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_comercial");
        }
    }
        public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        }    

        
     public void aceptoDialogo() {
         limpiar();
         tab_comercial.eliminar();
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_de.getChildren().add(set_empresa);
        dia_dialogo.setDialogo(grid_de);
        set_empresa.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void aceptoValores() {
        if (set_empresa.getValorSeleccionado()!= null) {
                         tab_comercial.getColumna("ruc_empresa").setValorDefecto(set_empresa.getValor("ruc"));
                         tab_comercial.getColumna("nombre_empresa").setValorDefecto(set_empresa.getValor("RAZON_SOCIAL"));
                         tab_comercial.getColumna("direccion_empresa").setValorDefecto(set_empresa.getValor("DIRECCION"));
                         tab_comercial.getColumna("telefono_empresa").setValorDefecto(set_empresa.getValor("telefono"));
                        utilitario.addUpdate(" tab_comercial");
                        dia_dialogo.cerrar();
                        tab_comercial.insertar();
                        }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                            }        
    }
    
      
  public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        set_gestor.setId("set_gestor");
        set_gestor.setConexion(con_ciudadania);
        set_gestor.setSql("SELECT cedula, cedula+digito_verificador as cedula_persona, nombre FROM MAESTRO WHERE (cedula+digito_verificador) LIKE '"+cedula.getValue()+"'");        
        set_gestor.setRows(18);
        set_gestor.setTipoSeleccion(false);
        grid_de1.getChildren().add(set_gestor);
        dia_dialogo1.setDialogo(grid_de1);
        set_gestor.dibujar();
        dia_dialogo1.dibujar();
    }
    
    public void aceptoValores1() {
        if (set_gestor.getValorSeleccionado()!= null) {
                         tab_gestor.getColumna("cedula_gestor").setValorDefecto(set_gestor.getValor("cedula_persona"));
                         tab_gestor.getColumna("nombre_gestor").setValorDefecto(set_gestor.getValor("nombre"));
                        utilitario.addUpdate("tab_gestor");
                        dia_dialogo1.cerrar();
                        tab_gestor.insertar();
                        }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                            }        
    }
    
/*****************************************/
    public void aceptoDialogo2() {
        dia_dialogo2.Limpiar();
        dia_dialogo2.setDialogo(grid2);
        grid_de2.getChildren().add(set_vehiculo);
        dia_dialogo2.setDialogo(grid_de2);
        set_vehiculo.dibujar();
        dia_dialogo2.dibujar();
    }
    
    public void aceptoValores2(){
        if (set_vehiculo.getValorSeleccionado()!= null) {
                        tab_detalle.getColumna("IDE_TIPO_VEHICULO").setValorDefecto(set_vehiculo.getValorSeleccionado());
                        aceptoDialogo3();
                        dia_dialogo2.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    
     public void aceptoDialogo3() {
        dia_dialogo3.Limpiar();
        dia_dialogo3.setDialogo(grid3);
        grid_de3.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo3.setDialogo(grid_de3);
        set_servicio.dibujar();
        dia_dialogo3.dibujar();
    }
    
    public void aceptoValores3() {
            if (set_servicio.getValorSeleccionado()!= null) {
                        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setValorDefecto(set_servicio.getValorSeleccionado());
                        tab_detalle.insertar();
                        dia_dialogo3.cerrar();
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
      if (tab_comercial.guardar()) {
           if (tab_gestor.guardar()) {
                 if (tab_solicitud.guardar()) {
                      if (tab_detalle.guardar()) {
                          ser_Placa.insertarRequisito(Integer.parseInt(tab_detalle.getValor("IDE_DETALLE_SOLICITUD")),Integer.parseInt(tab_detalle.getValor("IDE_TIPO_VEHICULO")), Integer.parseInt(tab_detalle.getValor("IDE_TIPO_SERVICIO")));
                           utilitario.addUpdate("tab_detalle");
                           guardarPantalla();
                            }
                        }
                    }
                }
            }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_comercial() {
        return tab_comercial;
    }

    public void setTab_comercial(Tabla tab_comercial) {
        this.tab_comercial = tab_comercial;
    }

    public Tabla getTab_gestor() {
        return tab_gestor;
    }

    public void setTab_gestor(Tabla tab_gestor) {
        this.tab_gestor = tab_gestor;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getSet_gestor() {
        return set_gestor;
    }

    public void setSet_gestor(Tabla set_gestor) {
        this.set_gestor = set_gestor;
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

    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }

    public Dialogo getDia_dialogo1() {
        return dia_dialogo1;
    }

    public void setDia_dialogo1(Dialogo dia_dialogo1) {
        this.dia_dialogo1 = dia_dialogo1;
    }

    public Dialogo getDia_dialogo2() {
        return dia_dialogo2;
    }

    public void setDia_dialogo2(Dialogo dia_dialogo2) {
        this.dia_dialogo2 = dia_dialogo2;
    }

    public Dialogo getDia_dialogo3() {
        return dia_dialogo3;
    }

    public void setDia_dialogo3(Dialogo dia_dialogo3) {
        this.dia_dialogo3 = dia_dialogo3;
    }

    
}
