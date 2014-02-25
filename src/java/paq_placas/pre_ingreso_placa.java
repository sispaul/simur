/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso_placa extends Pantalla{

private Tabla tab_placa = new Tabla();
private Tabla tab_ingreso = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla tab_vehiculo = new Tabla();
private Tabla tab_estado = new Tabla();
private Tabla tab_servicio = new Tabla();
private Tabla tab_tipo = new Tabla();


    public pre_ingreso_placa() {
                
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        tab_ingreso.setId("tab_ingreso");
        tab_ingreso.setTabla("TRANS_INGRESOS_PLACAS", "IDE_INGRESO_PLACAS", 1);
        tab_ingreso.setHeader("Acta Ingresos de Placas");
        tab_ingreso.getColumna("IDE_INGRESO_PLACAS").setNombreVisual("ID");
        tab_ingreso.getColumna("FECHA_ENVIO_ACTA").setNombreVisual("Fecha de Envio");
        tab_ingreso.getColumna("FECHA_REGISTRO_ACTA").setNombreVisual("Fecha de Registro");
        tab_ingreso.getColumna("ANO").setNombreVisual("Año");
        tab_ingreso.getColumna("NUMERO_ACTA").setNombreVisual("Nro. Acta");
        tab_ingreso.getColumna("ENTREGADO_ACTA").setNombreVisual("Quien Entrega");
        tab_ingreso.getColumna("RECIBIDO_ACTA").setNombreVisual("Quien Recibe");
        tab_ingreso.getColumna("fecha_envio_acta").setValorDefecto(utilitario.getFechaActual());
        tab_ingreso.getColumna("fecha_registro_acta").setValorDefecto(utilitario.getFechaActual());      
        tab_ingreso.getColumna("fecha_registro_acta").setLectura(true);
        tab_ingreso.getColumna("usu_ingreso").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_ingreso.getColumna("usu_ingreso").setVisible(false);
        tab_ingreso.setTipoFormulario(true);
        tab_ingreso.getGrid().setColumns(4);
        tab_ingreso.dibujar();
        tab_ingreso.agregarRelacion(tab_placa);
        PanelTabla pat_panel=new PanelTabla(); 
        pat_panel.setPanelTabla(tab_ingreso);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
               
        tab_vehiculo.setId("tab_vehiculo");
        tab_vehiculo.setIdCompleto("tab_tabulador:tab_vehiculo");
        tab_vehiculo.setTabla("trans_tipo_vehiculo", "ide_tipo_vehiculo", 2);
        tab_vehiculo.setHeader("Tipo Vehiculo");
        tab_vehiculo.setTipoSeleccion(true);
        tab_vehiculo.dibujar();
        PanelTabla tabv=new PanelTabla(); 
        tabv.setPanelTabla(tab_vehiculo);  
//        
        tab_servicio.setId("tab_servicio");
        tab_servicio.setIdCompleto("tab_tabulador:tab_servicio");
        tab_servicio.setTabla("TRANS_tipo_servicio", "IDE_tipo_servicio", 3);
        tab_servicio.setHeader("Tipo Servicio");
        tab_servicio.setTipoSeleccion(true);
        tab_servicio.dibujar();
        PanelTabla tabs=new PanelTabla(); 
        tabs.setPanelTabla(tab_servicio);
        
        tab_tipo.setId("tab_tipo");
        tab_tipo.setIdCompleto("tab_tabulador:tab_tipo");
        tab_tipo.setTabla("TRANS_tipo_placa", "IDE_tipo_placa", 4);
        tab_tipo.setHeader("Tipo Placa");
        tab_tipo.setTipoSeleccion(true);
        tab_tipo.dibujar();
        PanelTabla tabt=new PanelTabla(); 
        tabt.setPanelTabla(tab_tipo);
        
        tab_estado.setId("tab_estado");
        tab_estado.setIdCompleto("tab_tabulador:tab_estado");
        tab_estado.setTabla("TRANS_tipo_estado", "IDE_tipo_estado", 5);
        tab_estado.setHeader("Tipo Estado");
        tab_estado.setTipoSeleccion(true);
        tab_estado.dibujar();
        PanelTabla tabe=new PanelTabla(); 
        tabe.setPanelTabla(tab_estado);
      
        tab_placa.setId("tab_placa");
        tab_placa.setIdCompleto("tab_tabulador:tab_placa");
        tab_placa.setTabla("TRANS_PLACA", "IDE_PLACA", 6);
        tab_placa.setHeader("Placas");
        tab_placa.getColumna("cedula_ruc_propietario").setVisible(false);
        tab_placa.getColumna("nombre_propietario").setVisible(false);
        tab_placa.getColumna("fecha_entrega_placa").setVisible(false);
        tab_placa.getColumna("ide_placa").setNombreVisual("ID");
        tab_placa.getColumna("placa").setNombreVisual("Nro. Placa");
        tab_placa.getColumna("FECHA_REGISTRO_placa").setNombreVisual("Fecha de Registro");
        tab_placa.getColumna("fecha_registro_placa").setValorDefecto(utilitario.getFechaActual());
        tab_placa.getColumna("fecha_registro_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setValorDefecto(tab_vehiculo.getValorSeleccionado());
        tab_placa.getColumna("ide_tipo_servicio").setValorDefecto(tab_servicio.getValorPadre());
        tab_placa.getColumna("ide_tipo_placa").setValorDefecto(tab_tipo.getValor("IDE_tipo_placa"));
        tab_placa.getColumna("ide_tipo_estado").setValorDefecto(tab_estado.getValor("IDE_tipo_estado"));
//                tab_placa.getColumna("ide_tipo_vehiculo").setVisible(false);
//        tab_placa.getColumna("ide_tipo_servicio").setVisible(false);
//        tab_placa.getColumna("ide_tipo_placa").setVisible(false);
//        tab_placa.getColumna("ide_tipo_estado").setVisible(false);
//        tab_placa.getColumna("ide_tipo_vehiculo").setCombo("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
//        tab_placa.getColumna("ide_tipo_servicio").setCombo("select ide_tipo_servicio,DESCRIPCION_SERVICIO from trans_tipo_servicio");
//        tab_placa.getColumna("ide_tipo_placa").setCombo("select ide_tipo_placa,DESCRIPCION_TIPO from trans_tipo_placa");
//        tab_placa.getColumna("ide_tipo_estado").setCombo("select ide_tipo_estado,DESCRIPCION_ESTADO from trans_tipo_estado"); 
        tab_placa.dibujar();
        PanelTabla pat_panel1=new PanelTabla(); 
        pat_panel1.setPanelTabla(tab_placa);
        
        tab_tabulador.agregarTab("TIPO de VEHICULO", tabv);
        tab_tabulador.agregarTab("TIPO DE ESTADO", tabs);
        tab_tabulador.agregarTab("TIPO DE SERVICIO", tabt);
        tab_tabulador.agregarTab("TIPO DE PLACA", tabe);
        tab_tabulador.agregarTab("ASIGNACIÓN DE PLACAS", pat_panel1);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, tab_tabulador, "25%", "H");
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_ingreso.guardar()) {
            if (tab_placa.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_ingreso() {
        return tab_ingreso;
    }

    public void setTab_ingreso(Tabla tab_ingreso) {
        this.tab_ingreso = tab_ingreso;
    }

    public Tabla getTab_placa() {
        return tab_placa;
    }

    public void setTab_placa(Tabla tab_placa) {
        this.tab_placa = tab_placa;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    public Tabla getTab_vehiculo() {
        return tab_vehiculo;
    }

    public void setTab_vehiculo(Tabla tab_vehiculo) {
        this.tab_vehiculo = tab_vehiculo;
    }

    public Tabla getTab_estado() {
        return tab_estado;
    }

    public void setTab_estado(Tabla tab_estado) {
        this.tab_estado = tab_estado;
    }

    public Tabla getTab_servicio() {
        return tab_servicio;
    }

    public void setTab_servicio(Tabla tab_servicio) {
        this.tab_servicio = tab_servicio;
    }

    public Tabla getTab_tipo() {
        return tab_tipo;
    }

    public void setTab_tipo(Tabla tab_tipo) {
        this.tab_tipo = tab_tipo;
    }

}