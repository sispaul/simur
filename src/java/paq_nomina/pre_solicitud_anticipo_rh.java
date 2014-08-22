/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_solicitud_anticipo_rh extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Dibuja contorno de pantalla
    private Panel pan_opcion = new Panel();
    
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_garante = new Tabla();
    private Tabla tab_parametros = new Tabla();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    public pre_solicitud_anticipo_rh() {
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
        agregarComponente(quinde);
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(pan_opcion);
        
        dibujarSolicitud();
        
    }

        //Plantilla para Solicitud
    public void dibujarSolicitud(){
//        limpiarPanel();
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_solicitud_anticipo", "ide_solicitud_anticipo", 1);
     
                /*Filtro estatico para los datos a mostrar*/
            if (aut_busca.getValue() == null) {
                tab_anticipo.setCondicion("ide_solicitud_anticipo=-1");
            } else {
                tab_anticipo.setCondicion("ide_solicitud_anticipo=" + aut_busca.getValor());
            }
        
        tab_anticipo.getColumna("ci_solicitante").setMetodoChange("llenarDatosE");
        tab_anticipo.getColumna("solicitante").setMetodoChange("buscaSolicitante");
        
        tab_anticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_anticipo.getColumna("cod_banco").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_anticipo.getColumna("cod_cuenta").setCombo("SELECT cod_cuenta,nombre FROM ocecuentas");
        tab_anticipo.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tab_anticipo.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_anticipo.getColumna("cod_grupo").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        
        tab_anticipo.getColumna("login_ingre_solicitud").setValorDefecto(utilitario.getVariable("NICK"));
        tab_anticipo.getColumna("ip_ingre_solicitud").setValorDefecto(utilitario.getIp());
        tab_anticipo.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_anticipo.getColumna("periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        
        tab_anticipo.getColumna("login_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("IDE_EMPLEADO_SOLICITANTE").setVisible(false);
        tab_anticipo.getColumna("ip_ingre_solicitud").setVisible(false);
        tab_anticipo.getColumna("login_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("ip_aprob_solicitud").setVisible(false);
        tab_anticipo.getColumna("aprobado_solicitante").setVisible(false);
        tab_anticipo.getColumna("fecha_aprobacion").setVisible(false);
        tab_anticipo.getColumna("ide_listado").setVisible(false);
        tab_anticipo.getColumna("fecha_listado").setVisible(false);
        tab_anticipo.getColumna("anio").setVisible(false);
        tab_anticipo.getColumna("periodo").setVisible(false);
        
        tab_anticipo.setTipoFormulario(true);
        tab_anticipo.agregarRelacion(tab_garante);
        tab_anticipo.agregarRelacion(tab_parametros);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setMensajeWarn("DATOS DE SOLICITANTE");
        tpa.setPanelTabla(tab_anticipo);
        
        tab_garante.setId("tab_garante");
        tab_garante.setConexion(con_postgres);
        tab_garante.setTabla("srh_garante_anticipo", "ide_garante_anticipo", 2);
        tab_garante.getColumna("IDE_GARANTE_ANTICIPO ").setVisible(false);
        tab_garante.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_garante.getColumna("garante").setMetodoChange("buscaColaborador");
        tab_garante.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_garante.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_garante.getColumna("IDE_EMPLEADO_GARANTE").setVisible(false);
        tab_garante.setTipoFormulario(true);
        tab_garante.getGrid().setColumns(6);
        tab_garante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setMensajeWarn("DATOS DE GARANTE");
        tpd.setPanelTabla(tab_garante);
        
        tab_parametros.setId("tab_parametros");
        tab_parametros.setConexion(con_postgres);
        tab_parametros.setTabla("srh_calculo_anticipo", "ide_calculo_anticipo", 3);
        tab_parametros.getColumna("IDE_CALCULO_ANTICIPO").setVisible(false);
        tab_parametros.getColumna("fecha_anticipo").setValorDefecto(utilitario.getFechaActual());
        tab_parametros.getColumna("ide_periodo_anticipo_inicial").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Cliente from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("ide_periodo_anticipo_final").setCombo("select ide_periodo_anticipo, (mes || '/' || anio) As Clientes from srh_periodo_anticipo order by ide_periodo_anticipo");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLongitud(1);
        tab_parametros.getColumna("val_cuo_adi").setLongitud(1);
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setLectura(true);
        tab_parametros.getColumna("val_cuo_adi").setLectura(true);
        tab_parametros.getColumna("numero_cuotas_pagadas").setVisible(false);
        tab_parametros.getColumna("valor_pagado").setVisible(false);
        tab_parametros.getColumna("valor_anticipo").setMetodoChange("remuneracion");
        tab_parametros.getColumna("numero_cuotas_anticipo").setMetodoChange("porcentaje");
        tab_parametros.getColumna("porcentaje_descuento_diciembre").setMetodoChange("cuotas");
        tab_parametros.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_parametros.setTipoFormulario(true);
        tab_parametros.getGrid().setColumns(8);
        tab_parametros.dibujar();
        
        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("DATOS DE ANTICIPO A SOLICITAR");
        tpp.setPanelTabla(tab_parametros);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tpa, tpd, tpp, "36%", "45%", "H");
        agregarComponente(div_division);
        
            Grupo gru = new Grupo();
            gru.getChildren().add(div_division);
            pan_opcion.getChildren().add(gru);    
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }
}
