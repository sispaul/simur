/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author KEJA
 */
public class pre_ingreso_placas extends Pantalla{    
private Tabla set_vehiculo = new Tabla();
private Tabla set_servicio = new Tabla();
private Tabla set_tipo = new Tabla();
private Tabla set_estado = new Tabla();
private Tabla tab_ingreso = new Tabla();
private Tabla tab_placa = new Tabla();
private Tabla tab_consulta = new Tabla();
private Dialogo dia_dialogo = new Dialogo();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid = new Grid();
private Grid grid_de = new Grid();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();

    public pre_ingreso_placas() {
       
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
        
        set_servicio.setId("set_servicio");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado()+"");
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        set_servicio.dibujar();
        
        set_estado.setId("set_estado");
        set_estado.setSql("SELECT IDE_TIPO_ESTADO,DESCRIPCION_ESTADO FROM TRANS_TIPO_ESTADO WHERE IDE_TIPO_ESTADO BETWEEN 3 AND 4");
        set_estado.getColumna("DESCRIPCION_ESTADO").setNombreVisual("Estado");
        set_estado.setRows(5);
        set_estado.setTipoSeleccion(false);
        set_estado.dibujar();
   
        set_tipo.setId("set_tipo");
        set_tipo.setSql("SELECT IDE_TIPO_PLACA,DESCRIPCION_TIPO FROM TRANS_TIPO_PLACA");
        set_tipo.getColumna("DESCRIPCION_TIPO").setNombreVisual("Tipo");
        set_tipo.setRows(5);
        set_tipo.setTipoSeleccion(false);
        set_tipo.dibujar();
        
        
        tab_ingreso.setId("tab_ingreso");
        tab_ingreso.setTabla("TRANS_INGRESOS_PLACAS", "IDE_INGRESO_PLACAS", 1);
        tab_ingreso.setHeader("Acta Ingresos de Placas");
        tab_ingreso.getColumna("IDE_INGRESO_PLACAS").setNombreVisual("ID");
        tab_ingreso.getColumna("FECHA_ENVIO_ACTA").setNombreVisual("Fecha de Envio");
        tab_ingreso.getColumna("FECHA_REGISTRO_ACTA").setNombreVisual("Fecha de Registro");
        tab_ingreso.getColumna("ANO").setNombreVisual("Año");
        tab_ingreso.getColumna("ANO").setValorDefecto(utilitario.getAnio(utilitario.getFechaActual())+"");
        tab_ingreso.getColumna("ANO").setLectura(true);
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
        tab_ingreso.setStyle(null);
        pat_panel.setStyle("width:100%;overflow: auto;");
        agregarComponente(pat_panel);
//        tab_placa.setId("tab_placa");
//        tab_placa.setTabla("TRANS_PLACA", "IDE_PLACA", 2);
//        tab_placa.setHeader("Placas");
//        tab_placa.getColumna("cedula_ruc_propietario").setVisible(false);
//        tab_placa.getColumna("nombre_propietario").setVisible(false);
//        tab_placa.getColumna("fecha_entrega_placa").setVisible(false);
//        tab_placa.getColumna("ide_placa").setNombreVisual("ID");
//        tab_placa.getColumna("placa").setNombreVisual("Nro. Placa");
//        tab_placa.getColumna("placa").setMayusculas(true);
//        tab_placa.getColumna("placa").setUnico(true);
//        tab_placa.getColumna("FECHA_REGISTRO_placa").setNombreVisual("Fecha de Registro");
//        tab_placa.getColumna("fecha_registro_placa").setValorDefecto(utilitario.getFechaActual());
//        tab_placa.getColumna("fecha_registro_placa").setLectura(true);
//        tab_placa.getColumna("ide_tipo_vehiculo").setLectura(true);
//        tab_placa.getColumna("ide_tipo_servicio").setLectura(true);
//        tab_placa.getColumna("ide_tipo_placa").setLectura(true);
//        tab_placa.getColumna("ide_tipo_estado").setLectura(true);
//        tab_placa.getColumna("ide_tipo_vehiculo").setCombo("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
//        tab_placa.getColumna("ide_tipo_servicio").setCombo("select ide_tipo_servicio,DESCRIPCION_SERVICIO from trans_tipo_servicio");
//        tab_placa.getColumna("ide_tipo_placa").setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_TIPO FROM TRANS_TIPO_PLACA");
//        tab_placa.getColumna("ide_tipo_estado").setCombo("SELECT IDE_TIPO_ESTADO,DESCRIPCION_ESTADO FROM TRANS_TIPO_ESTADO WHERE IDE_TIPO_ESTADO BETWEEN 3 AND 4");
//        tab_placa.getColumna("ide_tipo_vehiculo").setNombreVisual("Vehiculo");
//        tab_placa.getColumna("ide_tipo_servicio").setNombreVisual("Servicio");
//        tab_placa.getColumna("ide_tipo_placa").setNombreVisual("Tipo Placa");
//        tab_placa.getColumna("ide_tipo_estado").setNombreVisual("Estado");
//        tab_placa.dibujar();
//        PanelTabla pat_panel1=new PanelTabla(); 
//        pat_panel1.setPanelTabla(tab_placa);
//        tab_placa.setStyle(null);
//        pat_panel1.setStyle("width:100%;overflow: auto;");
//        
//        Division div = new Division();
//        div.dividir2(pat_panel, pat_panel1, "30%", "h");
//        agregarComponente(div);

        
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
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

    public Tabla getSet_tipo() {
        return set_tipo;
    }

    public void setSet_tipo(Tabla set_tipo) {
        this.set_tipo = set_tipo;
    }

    public Tabla getSet_estado() {
        return set_estado;
    }

    public void setSet_estado(Tabla set_estado) {
        this.set_estado = set_estado;
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
    
}
