/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_listado_rechazado extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    private Tabla detalle = new Tabla();
    private SeleccionTabla set_comprobante = new SeleccionTabla();
    
    String num_listado;
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//cabecera
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    
    //Calendario
    private Calendario cal_fecha = new Calendario();
    
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_listado_rechazado() {
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_financiero.png");
        agregarComponente(quinde);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Creación de Botones; Busqueda,Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        //Auto complentar en el formulario
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT DISTINCT on (d.num_documento) t.ide_listado,d.num_documento\n" +
"FROM tes_comprobante_pago_listado t,tes_detalle_comprobante_pago_listado d\n" +
"where d.ide_listado = t.ide_listado and\n" +
"d.ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO')\n" +
"order by d.num_documento");
        aut_busca.setSize(100);
        bar_botones.agregarComponente(new Etiqueta("Busca Listado:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader(" LISTA PARA RECHAZADOS ");
        agregarComponente(pan_opcion);
        
        //Busqueda de comprobantes
        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(2);
        txt_buscar.setSize(20);
        gri_busca1.getChildren().add(new Etiqueta("ELEGIR FECHA :"));
        gri_busca1.getChildren().add(cal_fecha);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("buscarEntrega");
        bar_botones.agregarBoton(bot_buscar);
        gri_busca1.getChildren().add(bot_buscar);
        
        set_comprobante.setId("set_comprobante");
        set_comprobante.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_comprobante.setSeleccionTabla("SELECT DISTINCT on (d.num_documento) t.ide_listado,d.num_documento\n" +
                                            "FROM tes_comprobante_pago_listado t,tes_detalle_comprobante_pago_listado d\n" +
                                            "where d.ide_listado = t.ide_listado and t.IDE_LISTADO=-1 order by d.num_documento", "IDE_LISTADO");
        set_comprobante.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_comprobante.getTab_seleccion().setRows(10);
        set_comprobante.setRadio();
        set_comprobante.setWidth("40%");
        set_comprobante.setHeight("50%");
        set_comprobante.getGri_cuerpo().setHeader(gri_busca1);
        set_comprobante.getBot_aceptar().setMetodo("aceptarBusqueda");
        set_comprobante.setHeader("BUSCAR LISTADO O ITEM  A PAGAR");
        agregarComponente(set_comprobante);
        
    }
    
    public void abrirBusqueda(){
      set_comprobante.dibujar();
      txt_buscar.limpiar();
      set_comprobante.getTab_seleccion().limpiar();
      limpiarPanel();
    }
    
    public void buscarEntrega() {
      if (cal_fecha.getValue() != null && cal_fecha.getValue().toString().isEmpty() == false) {
                 set_comprobante.getTab_seleccion().setSql("SELECT DISTINCT on (d.num_documento) t.ide_listado,d.num_documento\n" +
"FROM tes_comprobante_pago_listado t,tes_detalle_comprobante_pago_listado d\n" +
"where d.ide_listado = t.ide_listado and\n" +
"d.ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') AND d.fecha_transferencia ='"+ cal_fecha.getFecha()+"' order by d.num_documento");
                 set_comprobante.getTab_seleccion().ejecutarSql();
                 limpiar();
          } else {
                 utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
                }
    }
    
    public void aceptarBusqueda() {
        limpiarPanel();
      if (set_comprobante.getValorSeleccionado() != null) {
             aut_busca.setValor(set_comprobante.getValorSeleccionado());
             set_comprobante.cerrar();
             dibujarLista();
             utilitario.addUpdate("aut_busca,pan_opcion");
         } else {
                utilitario.agregarMensajeInfo("Debe seleccionar un listado", "");
                }
    }
    
        //limpieza paneles y abrir busqueda
    public void limpiar() {
      aut_busca.limpiar();
      utilitario.addUpdate("aut_busca");
    }  

    private void limpiarPanel() {
        //borra el contenido de la división central
//      pan_opcion1.getChildren().clear();
      pan_opcion.getChildren().clear();
    }
    
        //Para Pagos de Comprobantes    
    public void dibujarLista(){
        if (aut_busca.getValue() != null) {
         limpiarPanel();
        //comprobante pago listado
        tab_comprobante.setId("tab_comprobante");
        tab_comprobante.setConexion(con_postgres);
        tab_comprobante.setTabla("tes_comprobante_pago_listado", "ide_listado", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_comprobante.setCondicion("ide_listado=-1");
        } else {
            tab_comprobante.setCondicion("ide_listado=" + aut_busca.getValor());
        }
        tab_comprobante.setTipoFormulario(true);
        tab_comprobante.agregarRelacion(detalle);
        tab_comprobante.getGrid().setColumns(6);
        tab_comprobante.dibujar();
        PanelTabla tcp = new PanelTabla();
        tcp.setPanelTabla(tab_comprobante);
        
        //tabla detalle
        detalle.setId("detalle");
        detalle.setConexion(con_postgres);
        detalle.setSql("SELECT  \n" +
                        "d.ide_detalle_listado,  \n" +
                        "d.ide_listado,  \n" +
                        "d.item,  \n" +
                        "d.comprobante,  \n" +
                        "d.num_transferencia,  \n" +
                        "d.cedula_pass_beneficiario,  \n" +
                        "d.nombre_beneficiario,  \n" +
                        "d.valor,  \n" +
                        "d.num_documento,  \n" +
                        "null AS rechazo \n" +
                        "FROM     \n" +
                        "tes_detalle_comprobante_pago_listado AS d  \n" +
                        "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO') and d.num_documento like 'LIST-2014-00004'");
        detalle.getColumna("ide_detalle_listado").setVisible(false);
        List list = new ArrayList();
        Object fil1[] = {
            "1", "SI"
        };
        Object fil2[] = {
            "2", "NO"
        };
        list.add(fil1);;
        list.add(fil2);;
        detalle.getColumna("rechazo").setRadio(list, "");
        detalle.getColumna("item").setVisible(false);
        detalle.getColumna("ide_listado").setVisible(false);
        
        detalle.setRows(5);
        detalle.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(detalle);
        pan_opcion.getChildren().add(tdd);
             } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione Listado en el autocompletar");
            limpiar();
        }
    }
    
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        for (int i = 0; i < detalle.getTotalFilas(); i++) { 
            if(detalle.getValor(i, "rechazo")!=null){
                if(detalle.getValor(i, "rechazo").equals("1")){
                    programas.rechazoComprobante(detalle.getValor(i, "num_transferencia"), detalle.getValor(i, "comprobante"), Integer.parseInt(detalle.getValor(i, "ide_listado")));
                }
            }
        }
         detalle.actualizar();
        utilitario.agregarMensaje("Comprobantes", "Rechazados");
    }

    @Override
    public void eliminar() {
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_comprobante() {
        return tab_comprobante;
    }

    public void setTab_comprobante(Tabla tab_comprobante) {
        this.tab_comprobante = tab_comprobante;
    }

    public SeleccionTabla getSet_comprobante() {
        return set_comprobante;
    }

    public void setSet_comprobante(SeleccionTabla set_comprobante) {
        this.set_comprobante = set_comprobante;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getDetalle() {
        return detalle;
    }

    public void setDetalle(Tabla detalle) {
        this.detalle = detalle;
    }
    
}
