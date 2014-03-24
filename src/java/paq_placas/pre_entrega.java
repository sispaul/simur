/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_placas;

import framework.componentes.Dialogo;
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
public class pre_entrega extends Pantalla{
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Consulta
    private Tabla tab_consulta = new Tabla();
    private Tabla set_entrega = new Tabla();

    private Dialogo dia_dialogoEN = new Dialogo();
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_en = new Grid();
    private Etiqueta etifec = new Etiqueta();
    public pre_entrega() {
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();      


        set_entrega.setId("set_entrega");
        set_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        set_entrega.setHeader("ENTREGA DE PLACAS");
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        set_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        set_entrega.getColumna("USU_ENTREGA").setLectura(true);
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setLectura(true);
        set_entrega.getColumna("IDE_ENTREGA_PLACA").setVisible(false);
        set_entrega.getColumna("FECHA_ENTREGA_PLACA ").setNombreVisual("Fecha Entrega");
        set_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setNombreVisual("C.I./RUC Propietario");
        set_entrega.getColumna("CEDULA_PERSONA_RETIRA ").setNombreVisual("C.I de Quien Retira");
        set_entrega.getColumna("NOMBRE_PERSONA_RETIRA").setNombreVisual("Nombre de Quien Retira");
        set_entrega.getColumna("USU_ENTREGA").setVisible(false);
        set_entrega.getColumna("DESCRIPCION_PERSONA_RETIRA").setNombreVisual("A Notaciones");
        set_entrega.getColumna("ENTREGA_PLACA ").setNombreVisual("Se Entrega Placa");
        set_entrega.getGrid().setColumns(2);
        set_entrega.setTipoFormulario(true);
        set_entrega.dibujar();
        PanelTabla pat_panel=new PanelTabla();  //Para el menu contextual
        pat_panel.setPanelTabla(set_entrega);
        set_entrega.setStyle(null);
        pat_panel.setStyle("width:100%;overflow: auto;");
        agregarComponente(pat_panel);
        
        //Configurando el dialogo
        dia_dialogoEN.setId("dia_dialogoEN");
        dia_dialogoEN.setTitle("PLACAS - PLACAS ENTREGADAS"); //titulo
        dia_dialogoEN.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogoEN.setHeight("40%");//siempre porcentaje   alto
        dia_dialogoEN.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoEN.getBot_aceptar().setMetodo("aceptoDialogo");
        
//         grid_en.setColumns(2);
//         grid_en.getChildren().add(new Etiqueta("SELECCIONE Vehiculo"));
//         grid_en.getChildren().add(new Etiqueta("SELECCIONE Servicio"));
//        agregarComponente(dia_dialogoEN);
         /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }
@Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "ENTREGA DE PLACA":
//               System.out.println("Acceso");
//                dia_dialogoEN.Limpiar();
//                //Configura grid
//                dia_dialogoEN.setDialogo(etifec);
//                dia_dialogoEN.setDialogo(grid_en);
//                dia_dialogoEN.dibujar();
//                System.out.println("Dibuja");
               aceptoDialogo();
               break;      
                
        }
    }
     
    public void aceptoDialogo() {
        switch (rep_reporte.getNombre()) {
            
               case "ENTREGA DE PLACA":
                   System.err.println("Ingreso");
                    if ((set_entrega.getValorSeleccionado() != null)) {  
                    //los parametros de este reporte
                    p_parametros = new HashMap();
                        p_parametros.put("cedula", set_entrega.getValor("CEDULA_RUC_PROPIETARIO"));
                        p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                    dia_dialogoEN.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                        System.out.println(p_parametros);
                    sef_formato.dibujar();
                    } else {
                        utilitario.agregarMensaje("No se a seleccionado ningun registro ", "");
                    }
               break;                   
        }

    }

    public void abrirDialogo() {
        dia_dialogo.dibujar();
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

    public Tabla getSet_entrega() {
        return set_entrega;
    }

    public void setSet_entrega(Tabla set_entrega) {
        this.set_entrega = set_entrega;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }
    
}
