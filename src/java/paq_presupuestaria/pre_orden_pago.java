/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_presupuestaria.ejb.Letras_Numeros;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author p-sistemas
 */
public class pre_orden_pago extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Tabla Normal
    private Tabla tab_orden = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla set_pendientes = new Tabla();
    
    //Dialogo Busca 
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogm = new Dialogo();
    private Grid grid_d = new Grid();
    private Grid grid_m = new Grid();
    private Grid grid = new Grid();
    private Grid grim = new Grid();
    
    //Texto de Ingreso
    Texto txt_motivo = new Texto();
    
    @EJB
    private Programas programas =(Programas) utilitario.instanciarEJB(Programas.class);
    private Letras_Numeros num_letra =(Letras_Numeros) utilitario.instanciarEJB(Letras_Numeros.class);
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    public pre_orden_pago() {
        
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();
        //usuario actual del sistema
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n" +
                "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setExcluirLectura(true);
        bot_anular.setIcon("ui-icon-cancel");
        bot_anular.setMetodo("quitar");
        bar_botones.agregarBoton(bot_anular);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setValue("Ordenes Pendientes");
        bot_limpiar.setExcluirLectura(true);
        bot_limpiar.setIcon("ui-icon-person");
        bot_limpiar.setMetodo("pendientes");
        bar_botones.agregarBoton(bot_limpiar);
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        tab_orden.setId("tab_orden");
        tab_orden.setConexion(con_postgres);
        tab_orden.setHeader("ORDENES DE PAGO");
        tab_orden.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        tab_orden.getColumna("tes_id_proveedor").setCombo("select ide_proveedor,titular from tes_proveedores  where ruc <> '0' order by titular");
        tab_orden.getColumna("tes_id_proveedor").setFiltroContenido();
        tab_orden.getColumna("tes_cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado where estado = 1");
        tab_orden.getColumna("tes_cod_empleado").setFiltroContenido();
        tab_orden.getColumna("tes_comprobante_egreso").setCombo("select comprobante as id, comprobante from tes_comprobante_pago order by comprobante");
        tab_orden.getColumna("tes_id_proveedor").setMetodoChange("proveedor");
        tab_orden.getColumna("tes_cod_empleado").setMetodoChange("empleado");
        tab_orden.getColumna("tes_valor").setMetodoChange("num_letras");
        tab_orden.getColumna("tes_comprobante_egreso").setMetodoChange("comprobante");
        tab_orden.getColumna("tes_letrero").setEtiqueta();
        tab_orden.getColumna("tes_anio").setVisible(false);
        tab_orden.getColumna("tes_mes").setVisible(false);
        tab_orden.getColumna("tes_fecha_ingreso").setVisible(false);
        tab_orden.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_orden.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_orden.getColumna("tes_proveedor").setVisible(false);
        tab_orden.getColumna("tes_empleado").setVisible(false);
        tab_orden.getColumna("tes_estado").setVisible(false);
        tab_orden.getColumna("tes_login_ing").setVisible(false);
        tab_orden.getColumna("tes_login_anu").setVisible(false);
        tab_orden.getColumna("tes_fecha_anu").setVisible(false);
        tab_orden.getColumna("tes_login_actu").setVisible(false);
        tab_orden.getColumna("tes_fecha_actu").setVisible(false);
        tab_orden.getColumna("tes_comentario_anula").setVisible(false);
        tab_orden.getColumna("tes_estado").setValorDefecto("Pendiente");
        tab_orden.getColumna("tes_comprobante_egreso").setMetodoChange("estado");
        tab_orden.getColumna("tes_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tab_orden.getColumna("tes_mes").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tab_orden.getColumna("tes_fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tab_orden.getColumna("tes_login_ing").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_orden.setTipoFormulario(true);
        tab_orden.getGrid().setColumns(4);
        tab_orden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tab_orden);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_postgres);
        tab_detalle.setSql("SELECT tes_ide_orden_pago,tes_numero_orden as numero_orden,tes_comprobante_egreso as numero_comprobante,\n" +
                "(case when tes_proveedor is null then tes_empleado when tes_proveedor is not null then tes_proveedor end) AS beneficiario,\n" +
                "tes_asunto as asunto,tes_valor as valor,tes_concepto as concepto,tes_acuerdo as acuerdo\n" +
                "FROM tes_orden_pago where tes_anio='"+utilitario.getAnio(utilitario.getFechaActual())+"'");
        tab_detalle.getColumna("tes_ide_orden_pago").setVisible(false);
        tab_detalle.getColumna("numero_orden").setFiltroContenido();
        tab_detalle.getColumna("beneficiario").setFiltroContenido();
        tab_detalle.getColumna("acuerdo").setFiltroContenido();
        tab_detalle.getColumna("numero_comprobante").setFiltroContenido();
        tab_detalle.getColumna("acuerdo").setLongitud(50);
        tab_detalle.agregarRelacion(tab_orden);
        tab_detalle.setNumeroTabla(2);
        tab_detalle.setLectura(true);
        tab_detalle.setRows(8);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir2(pto, ptd, "55%", "h");
        agregarComponente(div);
        
        //para poder buscar por apellido el solicitante
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("ORDENES PENDIENTES"); //titulo
        dia_dialogo.setWidth("75%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("80%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getBot_aceptar().setDisabled(true);
        grid_d.setColumns(4);
        agregarComponente(dia_dialogo);
        
        // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        dia_dialogm.setId("dia_dialogm");
        dia_dialogm.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        dia_dialogm.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogm.setHeight("20%");//siempre porcentaje   alto
        dia_dialogm.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogm.getBot_aceptar().setMetodo("anular");
        grid_m.setColumns(4);
        agregarComponente(dia_dialogm);
        
    }

       // METODOS PARA CANCELACION DE PLACA ASIGNADAS 
    public void quitar (){
        dia_dialogm.Limpiar();
        dia_dialogm.setDialogo(grim);
        txt_motivo.setSize(50);
        grim.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN :"));
        grim.getChildren().add(new Etiqueta("_______________________________________________________"));
        grim.getChildren().add(txt_motivo);
        dia_dialogm.setDialogo(grid_m);
        dia_dialogm.dibujar();
    }
    
    public void pendientes(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_d.getChildren().add(set_pendientes);
        set_pendientes.setId("set_pendientes");
        set_pendientes.setConexion(con_postgres);
        set_pendientes.setHeader("PENDIENTES");
        set_pendientes.setSql("SELECT tes_ide_orden_pago,tes_fecha_ingreso,tes_numero_orden,(case when tes_proveedor is null then tes_empleado when tes_proveedor is not null then tes_proveedor end) as beneficiario,tes_concepto,tes_valor,tes_asunto,tes_acuerdo\n" +
                "FROM tes_orden_pago where tes_estado = 'Pendiente'");
        set_pendientes.getColumna("tes_numero_orden").setFiltroContenido();
        set_pendientes.getColumna("beneficiario").setFiltroContenido();
        set_pendientes.getColumna("tes_acuerdo").setFiltroContenido();
        set_pendientes.getColumna("tes_acuerdo").setLongitud(35);
        set_pendientes.getColumna("beneficiario").setLongitud(50);
        set_pendientes.getColumna("tes_concepto").setLongitud(50);
        set_pendientes.setRows(10);
        set_pendientes.setTipoSeleccion(false);
        dia_dialogo.setDialogo(grid_d);
        set_pendientes.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void proveedor(){
        if(tab_orden.getValor("tes_id_proveedor")!=null){
            TablaGenerica tab_dato = programas.getProveedor(Integer.parseInt(tab_orden.getValor("tes_id_proveedor")));
            if (!tab_dato.isEmpty()) {
                tab_orden.setValor("tes_proveedor", tab_dato.getValor("titular"));
                tab_orden.setValor("tes_cod_empleado", null);
                tab_orden.setValor("tes_empleado", null);
                utilitario.addUpdate("tab_orden");//actualiza solo componentes
            }
        }else{
            tab_orden.getColumna("tes_cod_empleado").setLectura(false);
            utilitario.addUpdate("tab_orden");//actualiza solo componentes
        }
    }
    
    public void empleado(){
        if(tab_orden.getValor("tes_cod_empleado")!=null){
            TablaGenerica tab_dato = programas.getEmpleado(Integer.parseInt(tab_orden.getValor("tes_cod_empleado")));
            if (!tab_dato.isEmpty()) {
                tab_orden.setValor("tes_empleado", tab_dato.getValor("nombres"));
                tab_orden.setValor("tes_id_proveedor", null);
                tab_orden.setValor("tes_proveedor", null);
                utilitario.addUpdate("tab_orden");//actualiza solo componentes
            }
        }else{
            tab_orden.getColumna("tes_id_proveedor").setLectura(false);
            utilitario.addUpdate("tab_orden");//actualiza solo componentes
        }
    }
    
    public void num_letras(){
        tab_orden.setValor("tes_valor_letras", num_letra.Convertir(tab_orden.getValor("tes_valor"), true));
        utilitario.addUpdate("tab_orden");//actualiza solo componentes
    }
    
    public void anular(){
//        if(tab_orden.getValor("tes_estado")!="C"){
        String reg = new String();
        programas.actOrden(tab_orden.getValor("tes_numero_orden"), Integer.parseInt(tab_orden.getValor("tes_ide_orden_pago")),tab_consulta.getValor("NICK_USUA"),txt_motivo.getValue()+"");
        utilitario.addUpdate("tab_orden");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        reg = tab_orden.getValorSeleccionado();
        tab_detalle.actualizar();
        tab_detalle.setFilaActual(reg);
        tab_detalle.calcularPaginaActual(); 
        dia_dialogo.cerrar();
//        }else{
//            utilitario.agregarMensajeInfo("Comprobante No Puede Anularse", "Se Encuentra Pagado");
//        }
    }
    
    @Override
    public void insertar() {
        tab_orden.insertar();
        numero();
    }

    @Override
    public void guardar() {
        if(tab_orden.getValor("tes_ide_orden_pago")!=null){
            if(tab_orden.getValor("tes_estado")!="Anulada"){
                if(tab_orden.getValor("tes_cod_empleado")==null && tab_orden.getValor("tes_empleado")==null){
                    String reg = new String();
                    programas.actOrdenTotalPro(tab_orden.getValor("tes_numero_orden"), Integer.parseInt(tab_orden.getValor("tes_ide_orden_pago")), tab_orden.getValor("tes_asunto"), Integer.parseInt(tab_orden.getValor("tes_id_proveedor")),
                            tab_orden.getValor("tes_proveedor"), Double.valueOf(tab_orden.getValor("tes_valor")), tab_orden.getValor("tes_valor_letras")
                            , tab_orden.getValor("tes_concepto"), tab_orden.getValor("tes_acuerdo"),tab_orden.getValor("tes_nota"), tab_orden.getValor("tes_comprobante_egreso"), tab_orden.getValor("tes_fecha_comprobante"), tab_orden.getValor("tes_estado"),tab_consulta.getValor("NICK_USUA"),tab_orden.getValor("tes_fecha_envio"));
                    reg = tab_orden.getValorSeleccionado();
                    tab_detalle.actualizar();
                    tab_detalle.setFilaActual(reg);
                    tab_detalle.calcularPaginaActual();
                    utilitario.agregarMensaje("Registro Actualizado", "");
                }else if(tab_orden.getValor("tes_id_proveedor")==null && tab_orden.getValor("tes_proveedor")==null){
                    String reg = new String();
                    programas.actOrdenTotalEmp(tab_orden.getValor("tes_numero_orden"), Integer.parseInt(tab_orden.getValor("tes_ide_orden_pago")), tab_orden.getValor("tes_asunto"), 
                            Integer.parseInt(tab_orden.getValor("tes_cod_empleado")),tab_orden.getValor("tes_empleado"), Double.valueOf(tab_orden.getValor("tes_valor")), tab_orden.getValor("tes_valor_letras")
                            , tab_orden.getValor("tes_concepto"), tab_orden.getValor("tes_acuerdo"),tab_orden.getValor("tes_nota"), tab_orden.getValor("tes_comprobante_egreso"), tab_orden.getValor("tes_fecha_comprobante"), tab_orden.getValor("tes_estado"),tab_consulta.getValor("NICK_USUA"),tab_orden.getValor("tes_fecha_envio"));
                    reg = tab_orden.getValorSeleccionado();
                    tab_detalle.actualizar();
                    tab_detalle.setFilaActual(reg);
                    tab_detalle.calcularPaginaActual();
                    utilitario.agregarMensaje("Registro Actualizado", "");
                }
            }else{
                utilitario.agregarMensajeInfo("Comprobante No Puede Ser Pagado", "Se Encuentra Anulado");
            }
        }else{
            String reg = new String();
            tab_orden.guardar();
            con_postgres.guardarPantalla();
            reg = tab_orden.getValorSeleccionado();
            tab_detalle.actualizar();
            tab_detalle.setFilaActual(reg);
            tab_detalle.calcularPaginaActual();   
        }
    }
    
    @Override
    public void eliminar() {
    }
    
    public void numero(){
        String numero = programas.maxComprobantes();
        String num;
        Integer cantidad=0;
        cantidad = Integer.parseInt(numero)+1;
        if(numero!=null){
            if(cantidad>=0 && cantidad<=9){
                num = "000000"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            } else if(cantidad>=10 && cantidad<=99){
                num = "00000"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }else if(cantidad>=100 && cantidad<=999){
                num = "0000"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }else if(cantidad>=1000 && cantidad<=9999){
                num = "000"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }else if(cantidad>=10000 && cantidad<=99999){
                num = "00"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }else if(cantidad>=100000 && cantidad<=999999){
                num = "0"+String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }else if(cantidad>=1000000 && cantidad<=9999999){
                num = String.valueOf(cantidad);
                tab_orden.setValor("tes_numero_orden", num);
            }
        }
        utilitario.addUpdate("tab_orden");
    }
    
    public void comprobante(){
        if(tab_orden.getValor("tes_estado")!="Anulada"){
            if(tab_orden.getValor("tes_comprobante_egreso")!=null){
            tab_orden.setValor("tes_estado", "Pagado");
            utilitario.addUpdate("tab_orden");
            }
        }else{
            utilitario.agregarMensajeInfo("Comprobante No Puede Ser Pagado", "Se Encuentra Anulado");
        }
    }
    
    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "IMPRIMIR ORDEN":
                aceptoOrden();
          break;
        }
    } 
    
      public void aceptoOrden(){
        switch (rep_reporte.getNombre()) {
               case "IMPRIMIR ORDEN":
                   p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA")+"");
                    p_parametros.put("id_orden", tab_orden.getValor("tes_numero_orden")+"");
                    p_parametros.put("id_documento", Integer.parseInt(tab_orden.getValor("tes_ide_orden_pago")+""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
        }
    }
    
    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_orden() {
        return tab_orden;
    }

    public void setTab_orden(Tabla tab_orden) {
        this.tab_orden = tab_orden;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
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

    public Tabla getSet_pendientes() {
        return set_pendientes;
    }

    public void setSet_pendientes(Tabla set_pendientes) {
        this.set_pendientes = set_pendientes;
    }
    
}
