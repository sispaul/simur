/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;


import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import paq_nomina.ejb.anticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author p-sistemas
 */
public class pre_autorizacion_anticipos extends Pantalla{

private Tabla tab_anticipo = new Tabla();    
private Tabla tab_consulta = new Tabla();
//Conexion a base
    private Conexion con_postgres= new Conexion();

     //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Efecto efecto1 = new Efecto();
    
    //dialogos
    private Dialogo dia_autoriza = new Dialogo();
    private Grid grid_au = new Grid();
    private Grid grida = new Grid();
    
    //texto de ingreso
    private Texto txt_comentario = new Texto();
        //clase logica
    @EJB
    private anticipos iAnticipos = (anticipos) utilitario.instanciarEJB(anticipos.class);

    
    public pre_autorizacion_anticipos() {
        
        //agregar usuario actual   
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
         //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("AUTORIZAR ANTICIPOS DE SUELDO");
        efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion.getChildren().add(efecto1);
        agregarComponente(pan_opcion);
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setSql("SELECT  \n" +
                            "a.ide_anticipo,a.fecha_anticipo,  \n" +
                            "a.ide_empleado_solicitante,a.ci_solicitante,  \n" +
                            "a.solicitante,a.rmu,  \n" +
                            "a.rmu_liquido_anterior,a.valor_anticipo,  \n" +
                            "a.numero_cuotas_anticipo,  \n" +
                            "(select (periodo || '/' || anio) As periodos from srh_periodo_anticipo where ide_periodo_anticipo = \"a\".ide_periodo_anticipo_inicial) as periodo_inicial,\n" +
                            "(select (periodo || '/' || anio) As periodos from srh_periodo_anticipo where ide_periodo_anticipo = \"a\".ide_periodo_anticipo_final) as periodo_final,\n" +
                            "a.valor_cuota_mensual,a.valor_cuota_adicional,  \n" +
                            "a.ide_empleado_garante,a.ci_garante,  \n" +
                            "a.garante,a.observacion_solicitante,  \n" +
                            "a.aprobado_solicitante,e.estado  \n" +
                            "FROM  \n" +
                            "srh_anticipo a ,  \n" +
                            "srh_estado_anticipo e,  \n" +
                            "srh_periodo_anticipo p  \n" +
                            "WHERE  \n" +
                            "a.ide_estado_anticipo = 1 AND  \n" +
                            "a.ide_estado_anticipo = e.ide_estado_tipo AND  \n" +
                            "a.ide_periodo_anticipo_inicial = p.ide_periodo_anticipo");

        tab_anticipo.setCampoPrimaria("ide_anticipo");
        tab_anticipo.setTipoFormulario(true);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.setLectura(true);
        tab_anticipo.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_anticipo);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru);
              
        //Botones para autorizar o quitar autorizacion
        Boton bt=new Boton();
        bt.setValue("Autorizar Solicitud");
        bt.setMetodo("autorizar");
        bar_botones.agregarBoton(bt);
        
        //Dialogo de autorizacion
        dia_autoriza.setId("dia_dialogoe");
        dia_autoriza.setTitle("CONFIRMAR AUTORIZACION DE ANTICIPO"); //titulo
        dia_autoriza.setWidth("25%"); //siempre en porcentajes  ancho
        dia_autoriza.setHeight("18%");//siempre porcentaje   alto 
        dia_autoriza.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_autoriza.getBot_aceptar().setMetodo("Autoriza");
        grid_au.setColumns(4);
        agregarComponente(dia_autoriza);
    }

    public void autorizar(){
        dia_autoriza.Limpiar();
        dia_autoriza.setDialogo(grida);
        txt_comentario.setSize(50);
        grida.getChildren().add(new Etiqueta("OBSERVACIÓN:"));
        grida.getChildren().add(txt_comentario);
        dia_autoriza.setDialogo(grid_au);
        dia_autoriza.dibujar();
    }
        
    public void Autoriza(){
      TablaGenerica tab_dato = iAnticipos.director();
        if (!tab_dato.isEmpty()) {
            iAnticipos.actuaAutorizador(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), Integer.parseInt(tab_dato.getValor("cod_empleado")),
                    tab_dato.getValor("cedula_pass"), tab_dato.getValor("nombres"), utilitario.getVariable("NICK"), txt_comentario.getValue()+"");
            utilitario.addUpdate("tab_anticipo");
            utilitario.agregarMensaje("Solicitud Autorizada # ", tab_anticipo.getValor("ide_anticipo"));
            utilitario.agregarMensaje("Empleado / Trabajador", tab_anticipo.getValor("solicitante"));
            utilitario.agregarMensaje("Monto de Anticipo", tab_anticipo.getValor("valor_anticipo"));
            dia_autoriza.cerrar();
            cuotas();
          } else {
                 utilitario.agregarMensajeInfo("No existe en la base de datos", "");
                 }
    }
        
    public void cuotas(){
        Integer conta =Integer.parseInt(tab_anticipo.getValor("numero_cuotas anticipo"));
        if(tab_anticipo.getValor("valor_cuota_adicional")!= null && tab_anticipo.getValor("valor_cuota_adicional").isEmpty()){
            
            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), Integer.parseInt("1"), Integer.parseInt(tab_anticipo.getValor("valor_cuota_adicional")), 
                             Integer.parseInt("ide_periodo_descuento"),txt_comentario.getValue()+"");
            
                for (int i = 0; i < (conta-2); i++){
                     iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), 1+i, Integer.parseInt(tab_anticipo.getValor("valor_cuota_mensual")), 
                             Integer.parseInt("ide_periodo_descuento"),txt_comentario.getValue()+"");
                    }
                     Double valorp,valors,totall;
                     valorp = conta*Double.parseDouble(tab_anticipo.getValor("valor_cuota_mensual"));
                     valors= Double.parseDouble(tab_anticipo.getValor("valor_cuota_adicional"))+valorp ;
                     totall = Double.parseDouble(tab_anticipo.getValor("valor_anticipo"))-valors ;
                     iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), Integer.parseInt(tab_anticipo.getValor("numero_cuotas anticipo")), Integer.parseInt(String.valueOf(totall)), 
                             Integer.parseInt("ide_periodo_descuento"),txt_comentario.getValue()+"");
        }else{
                
                for (int i = 0; i < Integer.parseInt(tab_anticipo.getValor("numero_cuotas anticipo"))-1; i++){
                    iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), 0+i, Integer.parseInt(tab_anticipo.getValor("valor_cuota_mensual")), 
                             Integer.parseInt("ide_periodo_descuento"),txt_comentario.getValue()+"");
                    }
                    Double valor1,total;
                    valor1 = conta*Double.parseDouble(tab_anticipo.getValor("valor_cuota_mensual"));
                    total = Double.parseDouble(tab_anticipo.getValor("valor_anticipo"))-valor1 ;
                    iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor("ide_anticipo")), Integer.parseInt(tab_anticipo.getValor("numero_cuotas anticipo")), Integer.parseInt(String.valueOf(total)), 
                             Integer.parseInt("ide_periodo_descuento"),txt_comentario.getValue()+"");
             }
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

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }
 
}