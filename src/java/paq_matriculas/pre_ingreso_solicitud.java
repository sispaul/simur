/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_registros.ejb.ServicioRegistros;
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
    
    @EJB
    private ServicioRegistros ser_registros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);
    
    public pre_ingreso_solicitud() {
            
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("FECHA_SOLICITUD").setLectura(true);
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_SOLICITUD_PLACA").setVisible(false);
//        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setVisible(false);
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
        tab_detalle.getColumna("IDE_TIPO_SERVICIO").setNombreVisual("TIPO DE SERVICIO");
        tab_detalle.getColumna("IDE_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_APROBACION_PLACA").setVisible(false);
        tab_detalle.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_detalle.getColumna("APROBADO_SOLICITUD").setVisible(false);
        tab_detalle.getColumna("ENTREGADA_PLACA").setVisible(false);
        tab_detalle.getColumna("IDE_DETALLE_SOLICITUD").setNombreVisual("Nro. TRAMITE");
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.setTipoFormulario(true);
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
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tabp1, tabp2, tabp3, "30%", "40%", "H");
        agregarComponente(div_division);
    }
    
    public void buscaPersona(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_registros.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_registros.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
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

    public void buscaEmpresa(){
         if (utilitario.validarCedula(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_registros.getPersona(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_detalle.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_detalle");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"))) {
            TablaGenerica tab_dato = ser_registros.getEmpresa(tab_detalle.getValor("CEDULA_RUC_PROPIETARIO"));
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
    
}
