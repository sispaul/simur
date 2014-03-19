/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transportes;


import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
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
private Dialogo dia_dialogoe = new Dialogo();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid gride = new Grid();
private Grid grid_de = new Grid();
private Grid grid1 = new Grid();
private Grid grid_de1 = new Grid();
        ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo dia_dialogoEN = new Dialogo();
    private Grid grid_en = new Grid();
    private Calendario cal_fechaini = new Calendario();
    private Calendario cal_fechafin = new Calendario();
    private Etiqueta etifec = new Etiqueta();
    private Dialogo dia_dialogoDP = new Dialogo();
    private Dialogo dia_dialogod = new Dialogo();
    private Grid gridd = new Grid();
    private Grid grid_dp = new Grid();
    private Tabla set_vehiculo1 = new Tabla();
    private Tabla set_servicio1 = new Tabla ();
    
    
    public pre_ingreso_placas() {
        /*** CONFIGURACIÓN DE OBJETO REPORTE ***/
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
                
        set_vehiculo1.setId("set_vehiculo1");
        set_vehiculo1.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo1.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo1.setRows(5);
        set_vehiculo1.setTipoSeleccion(false);
        set_vehiculo1.dibujar();
        
        set_servicio1.setId("set_servicio1");
        set_servicio1.setSql("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");
        set_servicio1.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio1.setRows(10);
        set_servicio1.setTipoSeleccion(false);
        set_servicio1.dibujar();
        
         ///configurar la tabla Seleccion MOVIMIENTOS POR GRUPO Y ENCARGADO
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("PLACAS - PLACAS ENTREGADAS"); //titulo
        dia_dialogoEN.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoDialogo");
        
         grid_en.setColumns(2);
         grid_en.getChildren().add(new Etiqueta("SELECCIONE Vehiculo"));
         grid_en.getChildren().add(new Etiqueta("SELECCIONE Servicio"));
        agregarComponente(dia_dialogoEN);
        
        /***CREACION DE OBJETOS TABLA***/
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogoe.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores()");
        grid_de.setColumns(4);
        grid_de.getChildren().add(new Etiqueta("TIPO DE PLACA"));
        grid_de.getChildren().add(new Etiqueta("TIPO DE ESTADO"));
        grid_de.getChildren().add(new Etiqueta("SELECCIONE VEHICULO"));
        agregarComponente(dia_dialogoe);
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
        
       
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
                     
        Boton bot_placa = new Boton();
        bot_placa.setValue("ASIGNAR ESTADOS");
        bot_placa.setIcon("ui-icon-document");
        bot_placa.setMetodo("aceptoDialogoe");
        pat_panel.getChildren().add(bot_placa);
        
        tab_placa.setId("tab_placa");
        tab_placa.setTabla("TRANS_PLACA", "IDE_PLACA", 2);
//        tab_placa.setHeader("Placas");
        tab_placa.getColumna("cedula_ruc_propietario").setVisible(false);
        tab_placa.getColumna("nombre_propietario").setVisible(false);
        tab_placa.getColumna("fecha_entrega_placa").setVisible(false);
        tab_placa.getColumna("ide_placa").setNombreVisual("ID");
        tab_placa.getColumna("placa").setNombreVisual("Nro. Placa");
        tab_placa.getColumna("placa").setMayusculas(true);
        tab_placa.getColumna("placa").setUnico(true);
        tab_placa.getColumna("FECHA_REGISTRO_placa").setNombreVisual("Fecha de Registro");
        tab_placa.getColumna("fecha_registro_placa").setValorDefecto(utilitario.getFechaActual());
        tab_placa.getColumna("fecha_registro_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setLectura(true);
        tab_placa.getColumna("ide_tipo_servicio").setLectura(true);
        tab_placa.getColumna("ide_tipo_placa").setLectura(true);
        tab_placa.getColumna("ide_tipo_estado").setLectura(true);
        tab_placa.getColumna("ide_tipo_vehiculo").setNombreVisual("Vehiculo");
        tab_placa.getColumna("ide_tipo_servicio").setNombreVisual("Servicio");
        tab_placa.getColumna("ide_tipo_placa").setVisible(false);
        tab_placa.getColumna("ide_tipo_estado").setVisible(false);
        tab_placa.dibujar();
        PanelTabla pat_panel1=new PanelTabla(); 
        pat_panel1.setPanelTabla(tab_placa);
        
        Division div = new Division();
        div.dividir2(pat_panel, pat_panel1, "30%", "h");
        agregarComponente(div);
        
        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("PLACAS - ASIGNACION DE TIPOS"); //titulo
        dia_dialogo1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoValores1()");
        grid_de1.setColumns(4);
        grid_de1.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo1);
                
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        
    }     
    
     public void aceptoDialogoe() {
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_vehiculo);
        grid_de.getChildren().add(set_tipo);
        grid_de.getChildren().add(set_estado);
        dia_dialogoe.setDialogo(grid_de);
        set_estado.dibujar();
        set_tipo.dibujar();
        set_vehiculo.dibujar();
        dia_dialogoe.dibujar();
    }
    
    public void aceptoValores() {
        if (set_vehiculo.getValorSeleccionado()!= null) {
                if (set_tipo.getValorSeleccionado()!= null) {
                    if (set_estado.getValorSeleccionado()!= null) {
                        tab_placa.getColumna("ide_tipo_vehiculo").setValorDefecto(set_vehiculo.getValorSeleccionado());
                        tab_placa.getColumna("ide_tipo_placa").setValorDefecto(set_tipo.getValorSeleccionado());
                        tab_placa.getColumna("ide_tipo_estado").setValorDefecto(set_estado.getValorSeleccionado());
                        aceptoDialogo1();
                        dia_dialogoe.cerrar();
                        }
                 }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }
    
     public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        grid_de1.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo1.setDialogo(grid_de1);
        set_servicio.dibujar();
        dia_dialogo1.dibujar();
    }
    
    public void aceptoValores1() {
            if (set_servicio.getValorSeleccionado()!= null) {
                        tab_placa.getColumna("ide_tipo_servicio").setValorDefecto(set_servicio.getValorSeleccionado());
                        tab_placa.insertar();
                        dia_dialogo1.cerrar();
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }        
    }

@Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
        @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();            
        cal_fechaini.setFechaActual();
        cal_fechafin.setFechaActual();
        switch (rep_reporte.getNombre()) {
            case "PLACAS ENTREGADAS":
                dia_dialogoEN.Limpiar();
                //Agrega Fechas
                dia_dialogoEN.setDialogo(etifec);
                dia_dialogoEN.setDialogo(gridd);
                //Configura grid
                grid_en.getChildren().add(set_vehiculo1);
                grid_en.getChildren().add(set_servicio1);
                dia_dialogoEN.setDialogo(grid_en);
                set_vehiculo1.dibujar();
                set_servicio1.dibujar();
                dia_dialogoEN.dibujar();
               break;
           case "PLACAS DISPONIBLES":
                dia_dialogoDP.Limpiar();
                //Configura grid
                grid_dp.getChildren().add(set_vehiculo1);
                grid_dp.getChildren().add(set_servicio1);
                dia_dialogoDP.setDialogo(grid_dp);
                set_vehiculo1.dibujar();
                set_servicio1.dibujar();
                dia_dialogoDP.dibujar();
               break;      
                
        }
    }
     
    public void aceptoDialogo() {
        if (utilitario.isFechasValidas(cal_fechaini.getFecha(), cal_fechafin.getFecha())){
        switch (rep_reporte.getNombre()) {
               case "PLACAS ENTREGADAS":
                    if ((set_vehiculo1.getValorSeleccionado() != null) && (set_servicio1.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("pide_veh", Integer.parseInt(set_vehiculo1.getValorSeleccionado()));
                    p_parametros.put("pide_serv", Integer.parseInt(set_servicio1.getValorSeleccionado()));
                    p_parametros.put("pide_fechai", cal_fechaini.getFecha());
                    p_parametros.put("pide_fechaf", cal_fechafin.getFecha());
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    dia_dialogoEN.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                     System.out.println(p_parametros);
                        System.err.println(rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;
               case "PLACAS DISPONIBLES":
                    if ((set_vehiculo1.getValorSeleccionado() != null) && (set_servicio1.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                    p_parametros.put("vehiculo", Integer.parseInt(set_vehiculo1.getValorSeleccionado()));
                    p_parametros.put("servicio", Integer.parseInt(set_servicio1.getValorSeleccionado()));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA"));
                    dia_dialogoDP.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;                   
        }
        }else{
            utilitario.agregarMensaje("Fechas", "Rango de Fechas no valido");
        }
    }
    public void abrirDialogo() {
        dia_dialogod.dibujar();
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
        return dia_dialogoe;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogoe = dia_dialogo;
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Calendario getCal_fechaini() {
        return cal_fechaini;
    }

    public void setCal_fechaini(Calendario cal_fechaini) {
        this.cal_fechaini = cal_fechaini;
    }

    public Calendario getCal_fechafin() {
        return cal_fechafin;
    }

    public void setCal_fechafin(Calendario cal_fechafin) {
        this.cal_fechafin = cal_fechafin;
    }

    public Tabla getSet_vehiculo1() {
        return set_vehiculo1;
    }

    public void setSet_vehiculo1(Tabla set_vehiculo1) {
        this.set_vehiculo1 = set_vehiculo1;
    }

    public Tabla getSet_servicio1() {
        return set_servicio1;
    }

    public void setSet_servicio1(Tabla set_servicio1) {
        this.set_servicio1 = set_servicio1;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

}
