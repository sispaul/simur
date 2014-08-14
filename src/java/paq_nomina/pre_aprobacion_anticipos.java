/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.SolicAnticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_aprobacion_anticipos extends Pantalla{
    
    private Tabla tab_anticipo = new Tabla();    
    private Tabla tab_consulta = new Tabla();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();

     //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_aprobacion_anticipos() {
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("APROBAR SOLICITUD DE ANTICIPOS DE SUELDOS INGRESADOS");
        agregarComponente(pan_opcion);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setSql("SELECT\n" +
                            "s.ide_solicitud_anticipo,\n" +
                            "s.ide_empleado_solicitante,\n" +
                            "s.ci_solicitante,\n" +
                            "s.solicitante,\n" +
                            "c.valor_anticipo,\n" +
                            "c.numero_cuotas_anticipo,\n" +
                            "c.valor_cuota_mensual,\n" +
                            "c.valor_cuota_adicional,\n" +
                            "p.mes AS mes_descuento,\n" +
                            "s.aprobado_solicitante,\n" +
                            "s.id_distributivo,\n" +
                            "c.ide_periodo_anticipo_inicial,\n" +
                            "c.ide_periodo_anticipo_final\n" +
                            "FROM  \n" +
                            "srh_solicitud_anticipo AS s  \n" +
                            "INNER JOIN srh_calculo_anticipo AS c ON c.ide_solicitud_anticipo = s.ide_solicitud_anticipo   \n" +
                            "INNER JOIN srh_periodo_anticipo AS p ON p.ide_periodo_anticipo = c.ide_periodo_anticipo_inicial\n" +
                            "WHERE c.ide_estado_anticipo = (SELECT ide_estado_tipo FROM srh_estado_anticipo where estado like 'INGRESADO')\n" +
                            "order by s.ide_solicitud_anticipo");
        tab_anticipo.setCampoPrimaria("ide_solicitud_anticipo");
        tab_anticipo.setCampoOrden("ide_solicitud_anticipo");
       List lista = new ArrayList();
        Object fila1[] = {
            "1", "Aprobar"
        };
        Object fila2[] = {
            "0", "Negar"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_anticipo.getColumna("aprobado_solicitante").setRadio(lista, "");
        tab_anticipo.getColumna("id_distributivo").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_inicial").setVisible(false);
        tab_anticipo.getColumna("ide_periodo_anticipo_final").setVisible(false);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_anticipo);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        pan_opcion.getChildren().add(gru);
    }   
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        requisito();
    }

    @Override
    public void eliminar() {
    }

    /* FUNCION QUE PERMITE RECORRER LA TABLA RECUPERANDO EVENTOS ACTUALES     */
    public void seleccionar_tabla1(SelectEvent evt) {
        tab_anticipo.seleccionarFila(evt);
    }
    
    public void requisito(){
         for (int i = 0; i < tab_anticipo.getTotalFilas(); i++) {
              tab_anticipo.getValor(i, "ide_solicitud_anticipo");
              tab_anticipo.getValor(i, "ci_solicitante");
              tab_anticipo.getValor(i, "aprobado_solicitante");
              tab_anticipo.getValor(i, "numero_cuotas_anticipo");
              tab_anticipo.getValor(i, "valor_cuota_adicional");
              tab_anticipo.getValor(i, "id_distributivo");
              
              if(tab_anticipo.getValor(i, "aprobado_solicitante")!=null){
                         if(tab_anticipo.getValor(i, "aprobado_solicitante").equals("1")){
                             if(tab_anticipo.getValor(i, "id_distributivo").equals("1")){//detalle solicitud de empleados
                                 if(tab_anticipo.getValor(i, "valor_cuota_adicional")!= null ){//plazo de anticipo Hasta y Antes de Diciembre                                  
                                        for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            TablaGenerica tab_dato = iAnticipos.periodos1(Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                            if (!tab_dato.isEmpty()) {
                                                   if(tab_dato.getValor("mes").equals("Diciembre")){ 
                                                       iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_adicional")), 
                                                       Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);

                                                   }else{
                                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(j+1), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                                       }
                                            }else {
                                                   utilitario.agregarMensajeInfo("No se encuentra en roles", "");
                                               }
                                        }
                                           Double valorp=0.0,valors=0.0,totall=0.0;
                                           valorp = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-2)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                           valors= Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_adicional"))+valorp ;
                                           totall = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valors ;
                                           iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), Double.parseDouble(String.valueOf(totall)), 
                                           Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));

                                 }else{//plazo de anticipo Despues Diciembre
                                      for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                            iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                     }
                                        Double valor1=0.0,total=0.0;
                                        valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                        total = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valor1 ;
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), total, 
                                        Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));
                                 }
                                 
                             }else{//detalle para solicitud de trabajadores
                                    for (int j = 0; j < (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1); j++){
                                        iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), String.valueOf(1+j), Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual")), 
                                                  Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_inicial"))+j);
                                   } 
                                      Double valor1=0.0,total=0.0;
                                      valor1 = (Integer.parseInt(tab_anticipo.getValor(i, "numero_cuotas_anticipo"))-1)*Double.parseDouble(tab_anticipo.getValor(i,"valor_cuota_mensual"));
                                      total = Double.parseDouble(tab_anticipo.getValor(i,"valor_anticipo"))-valor1 ;
                                      iAnticipos.llenarSolicitud(Integer.parseInt(tab_anticipo.getValor(i,"ide_solicitud_anticipo")), tab_anticipo.getValor(i,"numero_cuotas_anticipo"), total, 
                                                  Integer.parseInt(tab_anticipo.getValor(i,"ide_periodo_anticipo_final")));   
                                   
                             }
                             iAnticipos.actuaSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"), 1,  utilitario.getVariable("NICK"));
                             iAnticipos.actualizSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"));
                         }else if(tab_anticipo.getValor(i, "aprobado_solicitante").equals("0")){//Solicitud Denegada
                                    iAnticipos.negarSolicitud(Integer.parseInt(tab_anticipo.getValor(i, "ide_solicitud_anticipo")), tab_anticipo.getValor(i, "ci_solicitante"));
                         }
                 }
         }
         tab_anticipo.actualizar();
         utilitario.agregarMensaje("Formularios Aprobados", "");
    }
    
    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }
    
}
