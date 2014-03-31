/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_matriculas;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_transportes.ejb.servicioPlaca;

/**
 *
 * @author KEJA
 */
public class pre_entrega_placa extends Pantalla{
Integer consulta,placa,vehiculo,servicio;
String cedula,factura;

private Combo cmb_servicio = new Combo();
private Combo cmb_vehiculo = new Combo();

private Calendario cal_fechaini = new Calendario();
private Calendario cal_fechafin = new Calendario();

private Tabla tab_entrega = new Tabla ();
private Tabla set_propietario = new Tabla();
private Tabla set_propietario1 = new Tabla();
private Tabla tab_consulta = new Tabla();
private Tabla set_servicio = new Tabla();
private Tabla set_vehiculo = new Tabla();

private Panel pan_opcion = new Panel();
private Efecto efecto = new Efecto();

private Etiqueta eti_etiqueta= new Etiqueta();
private Etiqueta eti_etiqueta1= new Etiqueta();
private Grid grid = new Grid();
private Etiqueta etifec = new Etiqueta();
private Grid grid1 = new Grid();
private Dialogo dia_dialogoe = new Dialogo();
private Grid grid_de = new Grid();
private Dialogo dia_dialogop = new Dialogo();
private Grid grid_dp = new Grid();
private Grid gride = new Grid();
private Grid gridp = new Grid();
private Dialogo dia_dialogo1 = new Dialogo();
private Grid grid_de1 = new Grid();

    private Panel pan_opcion1 = new Panel();
    private Panel pan_opcion2 = new Panel();
    private Panel pan_opcion4 = new Panel();

    private Texto tex_fecha = new Texto();
    private Texto tex_num_sol = new Texto();
    private Texto tex_empresa = new Texto();
    private Texto tex_gestor = new Texto();
    private Texto tex_usu_in = new Texto();
    private Texto tex_tip_sol = new Texto();
    private Texto tex_automotor = new Texto();
    private Texto tex_servicio = new Texto();
    private Texto tex_fech_apro = new Texto();
    private Texto tex_placa = new Texto();
    private Texto tex_usu_ap = new Texto();
    

    ///REPORTES
private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
private Map p_parametros = new HashMap();
@EJB
private servicioPlaca ser_Placa =(servicioPlaca) utilitario.instanciarEJB(servicioPlaca.class);
    private Serviciobusqueda serviciobusqueda =(Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);

    public pre_entrega_placa() {
        /*
         * CREACION DE BOTON BUSQUEDA POR NOMBRE
         */
        
        Boton bot_req = new Boton();
        bot_req.setValue("BUSQUEDA PROPIETARIO - NOMBRE");
        bot_req.setIcon("ui-icon-search");
        bot_req.setMetodo("buscaNombrep");
        bar_botones.agregarBoton(bot_req);
        /*
         * CREACION DE DE CAMPOS QUE MOSTRARAN LOS DATOS EN GRID DENTRO DE UN PANEL
         */
        pan_opcion2.setId("pan_opcion2");
        pan_opcion2.setHeader("REFERENCIAS DE SOLICITUD");
	pan_opcion2.setTransient(true);
        
        Grid gri_fechai = new Grid();
        gri_fechai.setColumns(2);
        gri_fechai.getChildren().add(new Etiqueta("FECHA SOLICITUD: "));
        tex_fecha.setId("tex_fecha");
        gri_fechai.getChildren().add(tex_fecha);

        Grid gri_usin = new Grid();
        gri_usin.setColumns(2);
        gri_usin.getChildren().add(new Etiqueta("USUARIO - INGRESO: "));
        tex_usu_in.setId("tex_usu_in");
        gri_usin.getChildren().add(tex_usu_in);
        
        Grid gri_fechaa = new Grid();
        gri_fechaa.setColumns(2);
        tex_num_sol.setId("tex_num_sol");
        tex_num_sol.setStyle("width: 89%;");
        gri_fechaa.getChildren().add(new Etiqueta("NRO SOLICITUD: "));
        gri_fechaa.getChildren().add(tex_num_sol);

        Grid gri_placa = new Grid();
        gri_placa.setColumns(2);
        tex_empresa.setId("tex_empresa");
        tex_empresa.setStyle("width: 220%;");
        gri_placa.getChildren().add(new Etiqueta("EMPRESA GESTIONA: "));
        gri_placa.getChildren().add(tex_empresa);
        
        Grid gri_usap = new Grid();
        gri_usap.setColumns(2);
        tex_gestor.setId("tex_gestor");
        tex_gestor.setStyle("width: 199%;");
        gri_usap.getChildren().add(new Etiqueta("GESTOR: "));
        gri_usap.getChildren().add(tex_gestor);
        
        pan_opcion4.setId("pan_opcion4");
        pan_opcion4.setHeader("REQUISITO SOLICITADO");
	pan_opcion4.setTransient(true);
        
        Grid gri_soli = new Grid();
        gri_soli.setColumns(2);
        gri_soli.getChildren().add(new Etiqueta("TIPO SOLICITUD: "));
        tex_tip_sol.setId("tex_tip_sol");
        gri_soli.getChildren().add(tex_tip_sol);
         
        Grid gri_usp = new Grid();
        gri_usp.setColumns(2);
        gri_usp.getChildren().add(new Etiqueta("AUTOMOTOR: "));
        tex_automotor.setId("tex_automotor");
        gri_usp.getChildren().add(tex_automotor);
//        
        Grid gri_u = new Grid();
        gri_u .setColumns(2);
        gri_u .getChildren().add(new Etiqueta("SERVICIO: "));
        tex_servicio.setId("tex_servicio");
        gri_u .getChildren().add(tex_servicio);
        
        pan_opcion2.getChildren().add(gri_fechai);
        pan_opcion2.getChildren().add(gri_fechaa);
        pan_opcion2.getChildren().add(gri_placa);
        pan_opcion2.getChildren().add(gri_usap);
        pan_opcion2.getChildren().add(gri_usin);
        pan_opcion4.getChildren().add(gri_soli);
        pan_opcion4.getChildren().add(gri_usp);
        pan_opcion4.getChildren().add(gri_u);
        
        pan_opcion1.setId("pan_opcion1");
        pan_opcion1.setHeader("APROBACIÓN");
	pan_opcion1.setTransient(true);
        
        Grid gri_ti = new Grid();
        gri_ti.setColumns(2);
        gri_ti.getChildren().add(new Etiqueta("FECHA DE APROBACIÒN: "));
        tex_fech_apro.setId("tex_fech_apro");
        gri_ti.getChildren().add(tex_fech_apro);
        
        Grid gri_pl = new Grid();
        gri_pl.setColumns(2);
        gri_pl.getChildren().add(new Etiqueta("NRO. PLACA: "));
        tex_placa.setId("tex_placa9");
        gri_pl.getChildren().add(tex_placa);
        
        Grid gri_pr = new Grid();
        gri_pr .setColumns(2);
        gri_pr.getChildren().add(new Etiqueta("USUARIO - APROBACIÒN : "));
        gri_pr.getChildren().add(tex_usu_ap);
        
        pan_opcion1.getChildren().add(gri_ti);
        pan_opcion1.getChildren().add(gri_pl);
        pan_opcion1.getChildren().add(gri_pr);        
        
//        etifec.setStyle("font-size:16px;color:blue");
//        etifec.setValue("SELECCIONE RANGO DE FECHAS");
//        grid.setColumns(4);
//        //campos fecha       
//        grid.getChildren().add(new Etiqueta("FECHA INICIAL"));
//        grid.getChildren().add(cal_fechaini);
//        grid.getChildren().add(new Etiqueta("   FECHA FINAL"));
//        grid.getChildren().add(cal_fechafin);
        
        Division div = new Division();
        div.dividir2( pan_opcion4,  pan_opcion1, "50%", "v");
        Division div1 = new Division();
        div1.dividir2(div, null, "20%", "h");
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        set_vehiculo.setId("set_vehiculo");
        set_vehiculo.setHeader("TIPO DE VEHICULO");
        set_vehiculo.setSql("select ide_tipo_vehiculo,des_tipo_vehiculo from trans_tipo_vehiculo WHERE ide_tipo_vehiculo BETWEEN 4 AND 5");
        set_vehiculo.getColumna("des_tipo_vehiculo").setNombreVisual("Vehiculo");
        set_vehiculo.setRows(5);
        set_vehiculo.setTipoSeleccion(false);
        set_vehiculo.dibujar();
               
        tab_entrega.setId("tab_entrega");
        tab_entrega.setTabla("trans_entrega_placa", "ide_entrega_placa", 1);
        tab_entrega.getColumna("CEDULA_RUC_PROPIETARIO").setMetodoChange("aceptarPlaca");
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setValorDefecto(utilitario.getFechaActual());
        tab_entrega.getColumna("FECHA_ENTREGA_PLACA ").setLectura(true);
        tab_entrega.getColumna("USU_ENTREGA").setValorDefecto(tab_consulta.getValor("NICK_USUA"));
        tab_entrega.getColumna("USU_ENTREGA").setLectura(true);
        tab_entrega.getColumna("CEDULA_PERSONA_RETIRA").setMetodoChange("aceptoretiro");
        tab_entrega.getGrid().setColumns(4);
        tab_entrega.setTipoFormulario(true); 
        tab_entrega.dibujar();
        
        pan_opcion.setId("pan_opcion");
	pan_opcion.setTransient(true);
        pan_opcion.setHeader("ENTREGA DE PLACAS");
	efecto.setType("drop");
	efecto.setSpeed(150);
	efecto.setPropiedad("mode", "'show'");
	efecto.setEvent("load");

        PanelTabla tab_e = new PanelTabla();
        tab_e.setPanelTabla(tab_entrega);
        pan_opcion.getChildren().add(efecto);
        pan_opcion.getChildren().add(tab_e);
        pan_opcion.getChildren().add(pan_opcion2);
        pan_opcion.getChildren().add(div1);
//        pan_opcion.getChildren().add(pan_opcion1);
        agregarComponente(pan_opcion);
        
        /*
         * DIALOGOS PARA BUSQUEDA DE PROPIETARIO
         */
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("BUSCAR PROPIETARIO"); //titulo
        dia_dialogoe.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("30%");//siempre porcentaje   alto
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("aceptoValores");
//        Grid gri_busca = new Grid();
//        gri_busca.setColumns(2);
//        gri_busca.getChildren().add(new Etiqueta("FECHA INICIO"));
//        gri_busca.getChildren().add(cal_fechaini);
//        gri_busca.getChildren().add(new Etiqueta("FECHA FINAL"));
//        gri_busca.getChildren().add(cal_fechafin);
//        dia_dialogoe.getGri_cuerpo().setHeader(gri_busca);
        grid_de.setColumns(4);
        agregarComponente(dia_dialogoe);

        dia_dialogop.setId("dia_dialogop");
        dia_dialogop.setTitle("BUSCAR PROPIETARIO"); //titulo
        dia_dialogop.setWidth("80%"); //siempre en porcentajes  ancho
        dia_dialogop.setHeight("50%");//siempre porcentaje   alto
        dia_dialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogop.getBot_aceptar().setMetodo("aceptoPersona");
        grid_dp.setColumns(4);
        agregarComponente(dia_dialogop);
        
         /**
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        dia_dialogo1.setId("dia_dialogo1");
        dia_dialogo1.setTitle("PLACAS - ASIGNACION DE SERVICIOS"); //titulo
        dia_dialogo1.setWidth("30%"); //siempre en porcentajes  ancho
        dia_dialogo1.setHeight("20%");//siempre porcentaje   alto
        dia_dialogo1.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo1.getBot_aceptar().setMetodo("aceptoDialogo");
        grid_de1.setColumns(4);
        grid_de1.getChildren().add(new Etiqueta("SELECCIONE SERVICIO"));
        agregarComponente(dia_dialogo1);
        
    }

    
    public void aceptarPlaca(){
//        tab_aprobacion.actualizar();
        dia_dialogoe.Limpiar();
        dia_dialogoe.setDialogo(gride);
        grid_de.getChildren().add(set_propietario);
        set_propietario.setId("set_propietario");
        set_propietario.setHeader("PROPIETARIOS PARA ENTREGAS");
        set_propietario.setSql("SELECT DISTINCT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,p.PLACA,v.des_tipo_vehiculo,s.DESCRIPCION_SERVICIO,\n" +
                                "d.IDE_SOLICITUD_PLACA,e.DESCRIPCION_ESTADO,a.USU_APROBACION\n" +
                                "FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA AS d ,dbo.TRANS_PLACA AS p ,dbo.TRANS_TIPO_ESTADO AS e ,dbo.TRANS_APROBACION_PLACA AS a ,\n" +
                                "dbo.trans_tipo_vehiculo AS v ,dbo.TRANS_TIPO_SERVICIO s\n" +
                                "WHERE d.IDE_PLACA = p.IDE_PLACA AND p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO AND d.IDE_APROBACION_PLACA = a.IDE_APROBACION_PLACA AND\n" +
                                "d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND d.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                                "e.DESCRIPCION_ESTADO LIKE 'asignada' AND d.CEDULA_RUC_PROPIETARIO LIKE '"+tab_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"'");
        set_propietario.getColumna("CEDULA_RUC_PROPIETARIO").setFiltro(true);
        set_propietario.setRows(5);
        set_propietario.setTipoSeleccion(false);
        dia_dialogoe.setDialogo(grid_de);
        set_propietario.dibujar();
        dia_dialogoe.dibujar();
     }   
    public void aceptoValores(){
        if (set_propietario.getValorSeleccionado()!= null) {
            
            TablaGenerica tab_dato = ser_Placa.getEntrega(Integer.parseInt(set_propietario.getValorSeleccionado()));
            System.out.println(tab_dato);
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("NOMBRE_PROPIETARIO"));
                tex_fecha.setValue(tab_dato.getValor("FECHA_SOLICITUD"));
                tex_num_sol.setValue(tab_dato.getValor("IDE_SOLICITUD_PLACA"));
                tex_empresa.setValue(tab_dato.getValor("NOMBRE_EMPRESA"));
                tex_gestor.setValue(tab_dato.getValor("NOMBRE_GESTOR"));
                tex_usu_in.setValue(tab_dato.getValor("USU_SOLICITUD"));
                
                tex_tip_sol.setValue(tab_dato.getValor("DESCRIPCION_GESTOR"));
                tex_automotor.setValue(tab_dato.getValor("des_tipo_vehiculo"));
                tex_servicio.setValue(tab_dato.getValor("DESCRIPCION_SERVICIO"));
                
                tex_fech_apro.setValue(tab_dato.getValor("FECHA_APROBACION"));
                tex_placa.setValue(tab_dato.getValor("PLACA"));
                tex_usu_ap.setValue(tab_dato.getValor("USU_APROBACION"));
                
                eti_etiqueta.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta.setValue(tab_dato.getValor("PLACA"));
                eti_etiqueta1.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta1.setValue("PLACA:");
                pan_opcion2.getChildren().add(eti_etiqueta1);
                pan_opcion2.getChildren().add(eti_etiqueta);
                
                consulta = Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
                cedula = tab_dato.getValor("CEDULA_RUC_PROPIETARIO");
                placa = Integer.parseInt(tab_dato.getValor("IDE_PLACA"));
                vehiculo=Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO"));
                servicio=Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO"));
                factura=tab_dato.getValor("NUMERO_FACTURA");
                
                utilitario.addUpdate("tab_entrega");
                utilitario.addUpdate("pan_opcion");
                dia_dialogoe.cerrar();
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos", "");
            }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
     }
    
        public void aceptoretiro(){
            System.out.println(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
         if (utilitario.validarCedula(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getPersona(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("nombre"));
                utilitario.addUpdate("tab_entrega");
                
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else if (utilitario.validarRUC(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"))) {
            TablaGenerica tab_dato = serviciobusqueda.getEmpresa(tab_entrega.getValor("CEDULA_PERSONA_RETIRA"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PERSONA_RETIRA", tab_dato.getValor("RAZON_SOCIAL"));
                utilitario.addUpdate("tab_entrega");
            } else {
                utilitario.agregarMensajeInfo("El Número de RUC ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        }
     }
    
    
     public void buscaNombrep(){
        dia_dialogop.Limpiar();
        dia_dialogop.setDialogo(gridp);
        grid_dp.getChildren().add(set_propietario1);
        set_propietario1.setId("set_propietario1");
        set_propietario1.setHeader("PROPIETARIOS PARA ENTREGAS");
        set_propietario1.setSql("SELECT DISTINCT d.IDE_DETALLE_SOLICITUD,d.CEDULA_RUC_PROPIETARIO,d.NOMBRE_PROPIETARIO,p.PLACA,v.des_tipo_vehiculo,s.DESCRIPCION_SERVICIO,\n" +
                                "d.IDE_SOLICITUD_PLACA,e.DESCRIPCION_ESTADO,a.USU_APROBACION\n" +
                                "FROM dbo.TRANS_DETALLE_SOLICITUD_PLACA AS d ,dbo.TRANS_PLACA AS p ,dbo.TRANS_TIPO_ESTADO AS e ,dbo.TRANS_APROBACION_PLACA AS a ,\n" +
                                "dbo.trans_tipo_vehiculo AS v ,dbo.TRANS_TIPO_SERVICIO s\n" +
                                "WHERE d.IDE_PLACA = p.IDE_PLACA AND p.IDE_TIPO_ESTADO = e.IDE_TIPO_ESTADO AND d.IDE_APROBACION_PLACA = a.IDE_APROBACION_PLACA AND\n" +
                                "d.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND d.IDE_TIPO_SERVICIO = s.IDE_TIPO_SERVICIO AND\n" +
                                "e.DESCRIPCION_ESTADO LIKE 'asignada'");
        set_propietario1.getColumna("NOMBRE_PROPIETARIO").setFiltro(true);
        set_propietario1.setRows(10);
        set_propietario1.setTipoSeleccion(false);
        dia_dialogop.setDialogo(grid_dp);
        set_propietario1.dibujar();
        dia_dialogop.dibujar();
     }
     
     public void aceptoPersona(){
         if (set_propietario1.getValorSeleccionado()!= null) {
            TablaGenerica tab_dato = ser_Placa.getEntrega(Integer.parseInt(set_propietario1.getValorSeleccionado()));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_entrega.setValor("NOMBRE_PROPIETARIO", tab_dato.getValor("NOMBRE_PROPIETARIO"));
                tab_entrega.setValor("CEDULA_RUC_PROPIETARIO", tab_dato.getValor("CEDULA_RUC_PROPIETARIO"));
                tex_fecha.setValue(tab_dato.getValor("FECHA_SOLICITUD"));
                tex_num_sol.setValue(tab_dato.getValor("IDE_SOLICITUD_PLACA"));
                tex_empresa.setValue(tab_dato.getValor("NOMBRE_EMPRESA"));
                tex_gestor.setValue(tab_dato.getValor("NOMBRE_GESTOR"));
                tex_usu_in.setValue(tab_dato.getValor("USU_SOLICITUD"));
                
                tex_tip_sol.setValue(tab_dato.getValor("DESCRIPCION_GESTOR"));
                tex_automotor.setValue(tab_dato.getValor("des_tipo_vehiculo"));
                tex_servicio.setValue(tab_dato.getValor("DESCRIPCION_SERVICIO"));
                
                tex_fech_apro.setValue(tab_dato.getValor("FECHA_APROBACION"));
                tex_placa.setValue(tab_dato.getValor("PLACA"));
                tex_usu_ap.setValue(tab_dato.getValor("USU_APROBACION"));
                
                eti_etiqueta.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta.setValue(tab_dato.getValor("PLACA"));
                eti_etiqueta1.setStyle("font-size:25px;color:black;text-align:center;");
                eti_etiqueta1.setValue("PLACA:");
                pan_opcion2.getChildren().add(eti_etiqueta1);
                pan_opcion2.getChildren().add(eti_etiqueta);
                
                consulta = Integer.parseInt(tab_dato.getValor("IDE_DETALLE_SOLICITUD"));
                cedula = tab_dato.getValor("CEDULA_RUC_PROPIETARIO");
                placa = Integer.parseInt(tab_dato.getValor("IDE_PLACA"));
                vehiculo=Integer.parseInt(tab_dato.getValor("IDE_TIPO_VEHICULO"));
                servicio=Integer.parseInt(tab_dato.getValor("IDE_TIPO_SERVICIO"));
                factura=tab_dato.getValor("NUMERO_FACTURA");
                
                utilitario.addUpdate("tab_entrega");
                utilitario.addUpdate("pan_opcion");
                dia_dialogop.cerrar();
            } else {
                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
            }
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
           case "ENTREGA PLACA":
               aceptoDialogo();
               break;       
           case "REPORTE DIARIO ENTREGA PLACA":
               aceptoDialogo();
               break;
           case "REPORTE MES ENTREGA PLACA": 
                dia_dialogoe.Limpiar();

                dia_dialogoe.setDialogo(etifec);
                dia_dialogoe.setDialogo(grid);
                
                grid_de.getChildren().add(set_vehiculo);
                dia_dialogoe.setDialogo(grid_de);
                set_vehiculo.dibujar();
                dia_dialogoe.dibujar();

               break;
                
        }
    }     
       
        public void aceptoDialogo(){
        if (utilitario.isFechasValidas(cal_fechaini.getFecha(), cal_fechafin.getFecha())){
        switch (rep_reporte.getNombre()) {
               case "ENTREGA PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("cedula", tab_entrega.getValor("CEDULA_RUC_PROPIETARIO")+"");
                      p_parametros.put("vehiculo", vehiculo);
                      p_parametros.put("servicio", servicio);
                      p_parametros.put("factura", factura);
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;
               case "REPORTE DIARIO ENTREGA PLACA":
                      p_parametros = new HashMap();
                      p_parametros.put("pide_fechai", utilitario.getFechaActual());
                      p_parametros.put("pide_fechaf", utilitario.getFechaActual());
                      p_parametros.put("nomp_res", tab_consulta.getValor("NICK_USUA")+"");
                      rep_reporte.cerrar();
                      sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                      sef_formato.dibujar();
               break;                   
        }
        }else{
            utilitario.agregarMensaje("Fechas", "Rango de Fechas no valido");
        }
    }

    public void aceptoDialogo1() {
        dia_dialogo1.Limpiar();
        dia_dialogo1.setDialogo(grid1);
        grid_de1.getChildren().add(set_servicio);
        set_servicio.setId("set_servicio");
        set_servicio.setHeader("TIPO DE SERVICIO");
        set_servicio.setSql("SELECT s.IDE_TIPO_SERVICIO,s.DESCRIPCION_SERVICIO FROM trans_tipo_vehiculo v,TRANS_TIPO_SERVICIO s\n" 
                            +"WHERE s.IDE_TIPO_VEHICULO = v.ide_tipo_vehiculo AND v.ide_tipo_vehiculo ="+set_vehiculo.getValorSeleccionado());
        set_servicio.getColumna("DESCRIPCION_SERVICIO").setNombreVisual("Servicio");
        set_servicio.setRows(10);
        set_servicio.setTipoSeleccion(false);
        dia_dialogo1.setDialogo(grid_de1);
        set_servicio.dibujar();
        dia_dialogo1.dibujar();
        dia_dialogoe.cerrar();
    }
    
    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }
    
    @Override
    public void insertar() {
      tab_entrega.insertar();
    }

    @Override
    public void guardar() {
    if (tab_entrega.guardar()) {
            if (guardarPantalla().isEmpty()) {
                tab_entrega.actualizar();
                ser_Placa.actualizarDS(Integer.parseInt(tab_entrega.getValor("ide_entrega_placa")),consulta);
                utilitario.addUpdate("tab_entrega");
                actualizarDE();
            }
        }else {
            utilitario.agregarMensajeInfo("No Puede Guardar Placa Entregada", "");
        }
    }

    public void actualizarDE(){
                        System.err.println("Actualizado");
        ser_Placa.actualizarDE(consulta, cedula, placa);
    }
    @Override
    public void eliminar() {
        tab_entrega.eliminar();
    }

    public Tabla getTab_entrega() {
        return tab_entrega;
    }

    public void setTab_entrega(Tabla tab_entrega) {
        this.tab_entrega = tab_entrega;
    }

    public Tabla getSet_propietario() {
        return set_propietario;
    }

    public void setSet_propietario(Tabla set_propietario) {
        this.set_propietario = set_propietario;
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

    public Tabla getSet_servicio() {
        return set_servicio;
    }

    public void setSet_servicio(Tabla set_servicio) {
        this.set_servicio = set_servicio;
    }

    public Tabla getSet_vehiculo() {
        return set_vehiculo;
    }

    public void setSet_vehiculo(Tabla set_vehiculo) {
        this.set_vehiculo = set_vehiculo;
    }

    public Tabla getSet_propietario1() {
        return set_propietario1;
    }

    public void setSet_propietario1(Tabla set_propietario1) {
        this.set_propietario1 = set_propietario1;
    }
    
}
