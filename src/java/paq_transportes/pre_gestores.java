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
import framework.componentes.Texto;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_gestores extends Pantalla{
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
        
    public pre_gestores() {
        
        
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
        tab_comercial.setHeader("DATOS DE EMPRESA");
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("NOMBRE DE EMPRESA");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
        tab_comercial.getColumna("ruc_empresa").setUnico(true);
        tab_comercial.getColumna("direccion_empresa").setNombreVisual("DIRECCIÓN");
        tab_comercial.getColumna("telefono_empresa").setNombreVisual("TELÉFONO");
        tab_comercial.agregarRelacion(tab_gestor);
        tab_comercial.getGrid().setColumns(2);
        tab_comercial.setTipoFormulario(true);
        tab_comercial.dibujar();
        PanelTabla pat_comercial = new PanelTabla();
        pat_comercial.setPanelTabla(tab_comercial);
        pat_comercial.getChildren().clear();
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.setHeader("DATOS DE GESTORES");
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("ide_tipo_gestor").setNombreVisual("TIPO GESTOR");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("CEDULA");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("NOMBRE");
        tab_gestor.getColumna("direccion_gestor").setNombreVisual("DIRECCIÓN");
        tab_gestor.getColumna("telefono_gestor").setNombreVisual("TELÉFONO");
        tab_gestor.getColumna("estado").setNombreVisual("ESTADO");
        tab_gestor.getColumna("nombre_gestor").setMayusculas(true);
        tab_gestor.getColumna("direccion_gestor").setMayusculas(true);
        tab_gestor.getGrid().setColumns(2);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor);
        pat_gestor.getChildren().clear();
//        Boton bot1 = new Boton();
//        bot1.setValue("BUSCAR GESTOR");
//        bot1.setMetodo("aceptoDialogo1");
//        bot1.setIcon("ui-icon-search");
//        eti_etiqueta.setStyle("font-size:12px;color:black;text-align:center;");
//        eti_etiqueta.setValue("Buscar Gestor Por Cedula:");
//        pat_gestor.getChildren().add(eti_etiqueta);
//        pat_gestor.getChildren().add(cedula);
//        pat_gestor.getChildren().add(bot1);


        
        Division div_division = new Division();
        div_division.dividir2(pat_comercial, pat_gestor, "30%", "H");
        agregarComponente(div_division);
 
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
    
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
if (utilitario.validarCedula(tab_gestor.getValor("cedula_gestor"))) { 
    if (tab_comercial.guardar()) {
            if (tab_gestor.guardar()) {
                guardarPantalla();
            }
        }
        }else {
            utilitario.agregarMensajeError("El Número de CEDULA no es válido", "");
            return;
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

    
}
