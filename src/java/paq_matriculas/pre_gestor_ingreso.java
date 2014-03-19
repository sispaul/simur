/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;
import paq_transportes.ejb.Serviciobusqueda;

/**
 *
 * @author p-sistemas
 */
public class pre_gestor_ingreso extends Pantalla{
    private Panel pan_opcion = new Panel();
    private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
    private PanelMenu pam_menu = new PanelMenu();
    private Tabla tab_comercial = new Tabla();
    private Tabla tab_gestor = new Tabla();
    private Tabla tab_gestor1 = new Tabla();

    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    public pre_gestor_ingreso() {
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);

        contruirMenu();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
        div_division.getDivision1().setCollapsible(true);
        div_division.getDivision1().setHeader("TIPOS DE GESTORES");
        agregarComponente(div_division);
        dibujarEmpresa();
    }
    
        private void contruirMenu() {
        // SUB MENU 1
        Submenu sum_empleado = new Submenu();
        sum_empleado.setLabel("CREACION DE GESTORES");
        pam_menu.getChildren().add(sum_empleado);

        // ITEM 1 : OPCION 0
        ItemMenu itm_datos_empl = new ItemMenu();
        itm_datos_empl.setValue("GESTORES DE EMPRESA");
        itm_datos_empl.setIcon("ui-icon-contact");
        itm_datos_empl.setMetodo("dibujarEmpresa");
        itm_datos_empl.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_datos_empl);

        // ITEM 2 : OPCION 1
        ItemMenu itm_permisos = new ItemMenu();
        itm_permisos.setValue("GESTORES PARTICULARES");
        itm_permisos.setIcon("ui-icon-person");
        itm_permisos.setMetodo("dibujarGestor");
        itm_permisos.setUpdate("pan_opcion");
        sum_empleado.getChildren().add(itm_permisos);
        
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
        // pan_opcion.getChildren().add(efecto);
    }

    public void limpiar() {
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
   

    public void dibujarEmpresa(){
        str_opcion = "0";
        limpiarPanel();
        
        tab_comercial.setId("tab_comercial");
        tab_comercial.setTabla("trans_comercial_automotores", "ide_comercial_automotores", 1);
        tab_comercial.getColumna("ide_comercial_automotores").setNombreVisual("ID");
        tab_comercial.getColumna("nombre_empresa").setNombreVisual("NOMBRE DE EMPRESA");
        tab_comercial.getColumna("ruc_empresa").setNombreVisual("RUC");
        tab_comercial.getColumna("ruc_empresa").setMetodoChange("cargarEmpresa");
//        tab_comercial.getColumna("ruc_empresa").setUnico(true);
        tab_comercial.getColumna("direccion_empresa").setNombreVisual("DIRECCIÓN");
        tab_comercial.getColumna("telefono_empresa").setNombreVisual("TELÉFONO");
        tab_comercial.agregarRelacion(tab_gestor);
        tab_comercial.getGrid().setColumns(2);
        tab_comercial.setTipoFormulario(true);
        tab_comercial.setHeader("DATOS DE EMPRESA");
        tab_comercial.dibujar();
        PanelTabla pat_comercial = new PanelTabla();
        pat_comercial.setPanelTabla(tab_comercial);
        pan_opcion.setTitle(" INGRESO DE DATOS DE GESTORES");
        pan_opcion.getChildren().add(pat_comercial);
        
        tab_gestor.setId("tab_gestor");
        tab_gestor.setTabla("trans_gestor", "ide_gestor", 2);
        tab_gestor.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor.getColumna("cedula_gestor").setNombreVisual("CEDULA");
        tab_gestor.getColumna("cedula_gestor").setMetodoChange("buscaPersona1");
        tab_gestor.getColumna("nombre_gestor").setNombreVisual("NOMBRE");
        tab_gestor.getColumna("direccion_gestor").setNombreVisual("DIRECCIÓN");
        tab_gestor.getColumna("telefono_gestor").setNombreVisual("TELÉFONO");
        tab_gestor.getColumna("estado").setNombreVisual("ESTADO");
        tab_gestor.getColumna("nombre_gestor").setMayusculas(true);
        tab_gestor.setHeader("DATOS DE GESTOR");
        tab_gestor.getColumna("direccion_gestor").setMayusculas(true);
        tab_gestor.getGrid().setColumns(2);
        tab_gestor.setTipoFormulario(true);
        tab_gestor.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor);
        
        Grupo gru1 = new Grupo();
        gru1.getChildren().add(pat_comercial);
        gru1.getChildren().add(pat_gestor);
        pan_opcion.getChildren().add(gru1);

    }
    
    public void dibujarGestor(){
        str_opcion = "1";
        limpiarPanel();
        tab_gestor1.setId("tab_gestor1");
        tab_gestor1.setTabla("trans_gestor", "ide_gestor", 3);
        tab_gestor1.getColumna("ide_gestor").setNombreVisual("ID");
        tab_gestor1.getColumna("cedula_gestor").setNombreVisual("CEDULA");
        tab_gestor1.getColumna("cedula_gestor").setMetodoChange("buscaPersona");
        tab_gestor1.getColumna("nombre_gestor").setNombreVisual("NOMBRE");
        tab_gestor1.getColumna("direccion_gestor").setNombreVisual("DIRECCIÓN");
        tab_gestor1.getColumna("telefono_gestor").setNombreVisual("TELÉFONO");
        tab_gestor1.getColumna("estado").setNombreVisual("ESTADO");
        tab_gestor1.getColumna("ide_comercial_automotores").setVisible(false);
        tab_gestor1.getColumna("nombre_gestor").setMayusculas(true);
        tab_gestor1.getColumna("direccion_gestor").setMayusculas(true);
        tab_gestor1.setHeader("DATOS DE GESTOR PARTICULAR");
        tab_gestor1.getGrid().setColumns(2);
        tab_gestor1.setTipoFormulario(true);
        tab_gestor1.dibujar();
        PanelTabla pat_gestor = new PanelTabla();
        pat_gestor.setPanelTabla(tab_gestor1);
        pan_opcion.getChildren().add(pat_gestor);
        
    }
    
     public void buscaPersona(){
         if (utilitario.validarCedula(tab_gestor1.getValor("cedula_gestor"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_gestor1.getValor("cedula_gestor"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_gestor1.setValor("nombre_gestor", tab_dato.getValor("nombre"));
                tab_gestor1.setValor("direccion_gestor", tab_dato.getValor("calle"));
                tab_gestor1.setValor("telefono_gestor", tab_dato.getValor("telefono"));
                utilitario.addUpdate("tab_gestor1");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_gestor1.getValor("cedula_gestor"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_gestor1.getValor("cedula_gestor"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_gestor1.setValor("nombre_gestor", tab_dato.getValor("razon_social"));
                tab_gestor1.setValor("direccion_gestor", tab_dato.getValor("direccion"));
                tab_gestor1.setValor("telefono_gestor", tab_dato.getValor("telefono"));
                utilitario.addUpdate("tab_gestor1");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else  {
            utilitario.agregarMensajeError("El Número de IDENTIFICACION no es válido", "");
        }
    }

        public void cargarEmpresa() {
         if (utilitario.validarRUC(tab_comercial.getValor("ruc_empresa"))) {
             System.out.println(tab_comercial.getValor("ruc_empresa"));
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_comercial.getValor("ruc_empresa"));
            System.out.println(tab_comercial.getValor("ruc_empresa"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_comercial.setValor("nombre_empresa", tab_dato.getValor("razon_social"));
                tab_comercial.setValor("direccion_empresa", tab_dato.getValor("direccion"));
                tab_comercial.setValor("telefono_empresa", tab_dato.getValor("telefono"));
                utilitario.addUpdate("tab_comercial");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
        }

    }

    public void buscaPersona1(){
         if (utilitario.validarCedula(tab_gestor.getValor("cedula_gestor"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_gestor.getValor("cedula_gestor"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_gestor.setValor("nombre_gestor", tab_dato.getValor("nombre"));
                tab_gestor.setValor("direccion_gestor", tab_dato.getValor("calle"));
                tab_gestor.setValor("telefono_gestor", tab_dato.getValor("telefono"));
                utilitario.addUpdate("tab_gestor");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else  {
            utilitario.agregarMensajeError("El Número de IDENTIFICACION no es válido", "");
        }
    }    
        
    
    @Override
    public void insertar() {
        if (str_opcion.equals("0")) {
            if (tab_comercial.isFocus()) {
                tab_comercial.insertar();
            } else if (tab_gestor.isFocus()) {
                tab_gestor.insertar();
            }  
        }else if (str_opcion.equals("1")) {
            if (tab_gestor1.isFocus()) {
                tab_gestor1.insertar();
            }
        }
    }

    @Override
    public void guardar() {
        if (str_opcion.equals("0")) {
            if (tab_comercial.guardar()) {
                 if (tab_gestor.guardar()) {
                     guardarPantalla();
                    }
                }  

                    }else if (str_opcion.equals("1")) {
                if (tab_gestor1.guardar()) {
                     guardarPantalla();
                    }
             }
    }

    @Override
    public void eliminar() {
                if (str_opcion.equals("0")) {
            if (tab_comercial.isFocus()) {
                tab_comercial.eliminar();
            } else if (tab_gestor.isFocus()) {
                tab_gestor.insertar();
            }  
        }else if (str_opcion.equals("1")) {
            if (tab_gestor1.isFocus()) {
                tab_gestor1.eliminar();
            }
        }
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

    public Tabla getTab_gestor1() {
        return tab_gestor1;
    }

    public void setTab_gestor1(Tabla tab_gestor1) {
        this.tab_gestor1 = tab_gestor1;
    }

    
}
