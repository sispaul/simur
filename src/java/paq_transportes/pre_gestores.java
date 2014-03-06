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

private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();

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
        dia_dialogo1.setWidth("75%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("70%");//siempre porcentaje   alto
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

        
        tab_comercial.setId("tab_comercial");
        tab_comercial.setTabla("trans_comercial_automotores", "ide_comercial_automotores", 1);
        tab_comercial.setHeader("Datos de Empresas");
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("Nombre de Establecimiento");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
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
        Boton bot = new Boton();
        bot.setValue("BUSCAR EMPRESA");
        bot.setMetodo("aceptoDialogo");
        bot.setIcon("ui-icon-document");
        pat_comercial.getChildren().add(bot);
        
        
        set_gestor.setId("set_gestor");
        set_gestor.setConexion(con_ciudadania);
        set_gestor.setSql("SELECT cedula,cedula+digito_verificador as cedula_gestor,nombre FROM MAESTRO");
        set_gestor.getColumna("cedula_gestor").setFiltro(true);
        set_gestor.getColumna("nombre").setFiltro(true);
        set_gestor.setRows(18);
        set_gestor.setTipoSeleccion(false);
        set_gestor.dibujar();
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.setHeader("Datos de Gestores");
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("Cedula");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("Nombre");
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor);

        Boton bot1 = new Boton();
        bot1.setValue("BUSCAR GESTOR");
        bot1.setMetodo("aceptoDialogo1");
        bot1.setIcon("ui-icon-document");
        pat_comercial.getChildren().add(bot1);
        
        Division div_division = new Division();
        div_division.dividir2(pat_comercial, pat_gestor, "30%", "H");
        agregarComponente(div_division);
 
    }

     public void aceptoDialogo() {
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
                        }else {
                            utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                            }        
    }
    
         public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        grid_de1.getChildren().add(set_gestor);
        dia_dialogo1.setDialogo(grid_de1);
        set_gestor.dibujar();
        dia_dialogo1.dibujar();
    }
    
    public void aceptoValores1() {
        if (set_gestor.getValorSeleccionado()!= null) {
                         tab_gestor.getColumna("ruc_empresa").setValorDefecto(set_gestor.getValor("ruc"));
                         tab_gestor.getColumna("nombre_empresa").setValorDefecto(set_gestor.getValor("RAZON_SOCIAL"));
                         tab_gestor.getColumna("direccion_empresa").setValorDefecto(set_gestor.getValor("DIRECCION"));
                         tab_gestor.getColumna("telefono_empresa").setValorDefecto(set_gestor.getValor("telefono"));
                        utilitario.addUpdate("tab_gestor");
                        dia_dialogo1.cerrar();
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
                guardarPantalla();
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

    
}
