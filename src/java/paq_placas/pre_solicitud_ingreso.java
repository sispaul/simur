/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_solicitud_ingreso extends Pantalla{
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_gestor = new Tabla();
    
    private Dialogo dia_dialogoEN = new Dialogo();
    private Grid grid_en = new Grid();
    private Grid grid_de = new Grid();
    private Grid gride = new Grid();
    Integer valor;
    @EJB
    private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
        
    public pre_solicitud_ingreso() {
        
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("trans_solicitud_placa", "ide_solicitud_placa", 1);
        tab_solicitud.getColumna("RUC_EMPRESA").setVisible(false);
        tab_solicitud.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("cargarEmpresa");
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setCombo("SELECT IDE_TIPO_GESTOR,DESCRIPCION_GESTOR FROM TRANS_TIPO_GESTOR");
        tab_solicitud.getColumna("NOMBRE_PROPIETARIO").setVisible(false);
        tab_solicitud.getColumna("IDE_GESTOR").setCombo("SELECT g.IDE_GESTOR,g.CEDULA_GESTOR,g.NOMBRE_GESTOR FROM TRANS_COMERCIAL_AUTOMOTORES t,TRANS_GESTOR g\n" +
                                                        "WHERE g.IDE_COMERCIAL_AUTOMOTORES = t.IDE_COMERCIAL_AUTOMOTORES and t.RUC_EMPRESA ="+tab_solicitud.getValor("CEDULA_RUC_PROPIETARIO")+"");
        tab_solicitud.agregarRelacion(tab_detalle);
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.getGrid().setColumns(4);
        tab_solicitud.dibujar();
        PanelTabla taps = new PanelTabla();
        taps.setPanelTabla(tab_solicitud);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("trans_detalle_solicitud_placa", "ide_detalle_solicitud", 2);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.agregarRelacion(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tapd = new PanelTabla();
        tapd.setPanelTabla(tab_detalle);
        
        tab_requisito.setId("tab_requisito");
        tab_requisito.setTabla("trans_detalle_requisitos_solicitud", "ide_detalle_requisitos_solicitud", 3);
        tab_requisito.dibujar();
        PanelTabla tapr = new PanelTabla();
        tapr.setPanelTabla(tab_requisito);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(taps,tapd, tapr, "35%", "47%", "H");
        agregarComponente(div_division);
        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
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
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
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
    }

    @Override
    public void eliminar() {
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
