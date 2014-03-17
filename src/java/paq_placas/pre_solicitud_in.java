/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_registros.ejb.ServicioRegistros;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_in extends Pantalla{
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_requisito = new Tabla();
    private Conexion con_ciudadania= new Conexion();
    
    @EJB
    private ServicioRegistros ser_registros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);
    
    public pre_solicitud_in() {
//        pan_opcion.setHeader("CATASTRO DE EMPRESAS DE TRANSPORTE PÚBLICO");
        con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadaniajdbc"));
        con_ciudadania.NOMBRE_MARCA_BASE="sqlserver";
        
        tab_solicitud.setMostrarNumeroRegistros(false);       
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setTabla("TRANS_SOLICITUD_PLACA", "IDE_SOLICITUD_PLACA", 1);
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setNombreVisual("DESCRIPCIÓN DE SOLICITUD");
        tab_solicitud.getColumna("DESCRIPCION_SOLICITUD").setMayusculas(true);
        tab_solicitud.getColumna("NUMERO_AUTOMOTORES").setNombreVisual("Nro. AUTOMOTORES");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setNombreVisual("FECHA");
        tab_solicitud.getColumna("FECHA_SOLICITUD").setValorDefecto(utilitario.getFechaActual());
        tab_solicitud.getColumna("RUC_EMPRESA").setMetodoChange("cargarEmpresa");
        tab_solicitud.getColumna("USU_SOLICITUD").setVisible(false);
        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setCombo("select ide_tipo_gestor,descripcion_gestor from trans_tipo_gestor");
////        tab_solicitud.getColumna("IDE_TIPO_GESTOR").setValorDefecto(cmb_tipos.getValue()+"");
        tab_solicitud.getColumna("IDE_GESTOR").setVisible(false);
//        tab_solicitud.getColumna("USU_SOLICITUD").setValorDefecto(tab_consulta.getValor("NICK_USUA")); 
//        tab_solicitud.agregarRelacion(tab_detalle);
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
//        tab_detalle.getColumna("CEDULA_RUC_PROPIETARIO").
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
        
        tab_detalle.setTipoFormulario(true);
        agregarComponente(tab_requisito);
        tab_detalle.dibujar();
        PanelTabla tabp2 = new PanelTabla();
        tabp2.setPanelTabla(tab_detalle); 
        
         Division div = new Division();
        div.dividir2(tabp1, tabp2, "30%", "H");
        agregarComponente(div);
//        agregarComponente(tabp1);
        
    }

       public void cargarEmpresa() {
        if (utilitario.validarRUC(tab_solicitud.getValor("RUC_EMPRESA"))) {
            System.err.println("RUC");
            TablaGenerica tab_dato = ser_registros.getEmpresa(tab_solicitud.getValor("RUC_EMPRESA"));
            System.out.println(ser_registros.getEmpresa(tab_solicitud.getValor("RUC_EMPRESA")));
            if (!tab_dato.isEmpty()) {
                //Cargo la información de la base de datos maestra 
                System.out.println("RUC");
                tab_solicitud.setValor("NOMBRE_EMPRESA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_solicitud");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeError("El Número de RUC no es válido", "");
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
   
    
}
