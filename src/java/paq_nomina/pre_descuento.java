/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.mergeDescuento;

import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author m-paucar
 * 
 *
 */
public class pre_descuento extends Pantalla{
    
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_usuario = new Tabla();
    private SeleccionTabla set_rol = new SeleccionTabla();
    private SeleccionTabla set_roles = new SeleccionTabla();

//COMBOS DE SELECICON
    private Combo cmb_anio = new Combo();
    private Combo cmb_periodo = new Combo();
    private Combo cmb_descripcion = new Combo();
    private Combo cmb_distributivo = new Combo();
    private Combo cmb_distributivo1 = new Combo();
    
// DIALOGO DE ACCIÓN
    private Dialogo dia_dialogoe = new Dialogo();
    private Dialogo dia_dialorol = new Dialogo();
    private Dialogo dia_dialogoti = new Dialogo();
    private Grid grid_de = new Grid();
    private Grid grid_rol = new Grid();
    private Grid grid_ti = new Grid();
    
//1.-
 @EJB
private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
 
private Conexion con_postgres= new Conexion();
    public pre_descuento() {
        //2.-
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        //3.-
        con_postgres.NOMBRE_MARCA_BASE = "postgres"; 
        tab_tabla.setId("tab_tabla");
        //4.-
        tab_tabla.setConexion(con_postgres);
        tab_tabla.setNumeroTabla(1);       
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
       
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
     
        Boton bot2 = new Boton();
        bot2.setValue("EXTRAER ANTICIPOS");
        bot2.setIcon("ui-icon-extlink"); //pone icono de jquery temeroller
        bot2.setMetodo("anticipo");
        bar_botones.agregarBoton(bot2);
        
        cmb_distributivo.setId("cmb_distributivo");
        cmb_distributivo.setConexion(con_postgres);
        cmb_distributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        cmb_distributivo.setMetodo("buscaColumna");
        bar_botones.agregarComponente(new Etiqueta("Distributivo : "));
        bar_botones.agregarComponente(cmb_distributivo);
        
        cmb_distributivo1.setId("cmb_distributivo1");
        cmb_distributivo1.setConexion(con_postgres);
        cmb_distributivo1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        
        Boton bot3 = new Boton();
        bot3.setValue("DEPURAR");
        bot3.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot3.setMetodo("completar");
//        bar_botones.agregarBoton(bot3); 
     
        Boton bot4 = new Boton();
        bot4.setValue("MIGRAR ROL");
        bot4.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot4.setMetodo("abrirDialogo");
        bar_botones.agregarBoton(bot4); 
       
        Boton bot5 = new Boton();
        bot5.setValue("BORRAR");
        bot5.setIcon("ui-icon-closethick"); //pone icono de jquery temeroller
        bot5.setMetodo("borrar");
        bar_botones.agregarBoton(bot5);
        
       /*CONFIGURACIÓN DE COMBOS*/
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        
        gri_busca.getChildren().add(new Etiqueta("AÑO:"));
        cmb_anio.setId("cmb_anio");
        cmb_anio.setConexion(con_postgres);
        cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
        gri_busca.getChildren().add(cmb_anio);
        
        gri_busca.getChildren().add(new Etiqueta("PERIODO:"));
        cmb_periodo.setId("cmb_periodo");
        cmb_periodo.setConexion(con_postgres);
        cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
        gri_busca.getChildren().add(cmb_periodo);
        
        gri_busca.getChildren().add(new Etiqueta("DESCRIPCIÓN:"));
        cmb_descripcion.setId("cmb_descripcion");
        cmb_descripcion.setConexion(con_postgres);
        cmb_descripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        cmb_descripcion.setMetodo("buscarColumna");
        gri_busca.getChildren().add(cmb_descripcion);
        
        /*
         * CREACION DE TABLA SELECCION PARA COLUMNAS
         */
        set_rol.setId("set_rol");
        set_rol.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_rol.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        set_rol.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_rol.getTab_seleccion().setRows(12);
        set_rol.setRadio();
        set_rol.getGri_cuerpo().setHeader(gri_busca);
        set_rol.getBot_aceptar().setMetodo("aceptoDescuentos");
        set_rol.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(set_rol);
        
        set_roles.setId("set_roles");
        set_roles.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_roles.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
        set_roles.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_roles.getTab_seleccion().setRows(14);
        set_roles.setRadio();
        set_roles.getBot_aceptar().setMetodo("aceptoDesc");
        set_roles.setHeader("SELECCIONE PARAMETROS PARA DESCUENTO");
        agregarComponente(set_roles);
               
         /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(con_postgres);
        agregarComponente(sef_formato);
        
        tab_usuario.setId("tab_usuario");
        tab_usuario.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_usuario.setCampoPrimaria("IDE_USUA");
        tab_usuario.setLectura(true);
        tab_usuario.dibujar();
        
        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialogoe.setId("dia_dialogoe");
        dia_dialogoe.setTitle("CONFIRMAR SUBIDA A ROL"); //titulo
        dia_dialogoe.setWidth("27%"); //siempre en porcentajes  ancho
        dia_dialogoe.setHeight("18%");//siempre porcentaje   alto 
        dia_dialogoe.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoe.getBot_aceptar().setMetodo("migrar");
        dia_dialogoe.getBot_cancelar().setMetodo("cancelarValores");
        grid_de.setColumns(4);
        
        Etiqueta eti = new Etiqueta();
        eti.setValue("ADVERTENCIA - EL SIGUIENTE PROCESO AFECTARA ");
        eti.setStyle("font-size:13px;color:red");
        Etiqueta eti1 = new Etiqueta();
        eti1.setValue("AÑO  :");
        Etiqueta eti4 = new Etiqueta();
        eti4.setStyle("text-align:center;position:absolute;top:28px;left:60px;");
        eti4.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        Etiqueta eti2 = new Etiqueta();
        eti2.setValue("PERIODO  :");
        Etiqueta eti5 = new Etiqueta();
        eti5.setStyle("text-align:center;position:absolute;top:50px;left:84px;");
        eti5.setValue(utilitario.getMes(utilitario.getFechaActual()));
        Etiqueta eti6 = new Etiqueta();
        eti6.setStyle("text-align:center;position:absolute;top:50px;left:104px;");
        eti6.setValue(utilitario.getNombreMes(utilitario.getMes(utilitario.getFechaActual())));
        Etiqueta eti3 = new Etiqueta();
        eti3.setValue("DEL ROL DE PAGOS ");
        eti3.setStyle("font-size:13px;color:red");
        
        dia_dialogoe.setDialogo(eti);
        dia_dialogoe.setDialogo(eti1);
        dia_dialogoe.setDialogo(eti4);
        dia_dialogoe.setDialogo(eti2);
        dia_dialogoe.setDialogo(eti5);
        dia_dialogoe.setDialogo(eti6);
        dia_dialogoe.setDialogo(eti3);
        utilitario.addUpdate("tab_tabla");
        agregarComponente(dia_dialogoe);
        
        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialorol.setId("dia_dialorol");
        dia_dialorol.setTitle("REPORTES DE ROL - EMPLEADOS"); //titulo
        dia_dialorol.setWidth("35%"); //siempre en porcentajes  ancho
        dia_dialorol.setHeight("20%");//siempre porcentaje   alto 
        dia_dialorol.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialorol.getBot_aceptar().setMetodo("aceptoDescuentos");
        grid_rol.setColumns(4);
        agregarComponente(dia_dialorol);
        
        dia_dialogoti.setId("dia_dialogoti");
        dia_dialogoti.setTitle("SELECICONE SERVIDOR"); //titulo
        dia_dialogoti.setWidth("25%"); //siempre en porcentajes  ancho
        dia_dialogoti.setHeight("15%");//siempre porcentaje   alto 
        dia_dialogoti.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoti.getBot_aceptar().setMetodo("anticipo1");
        grid_ti.setColumns(4);
        agregarComponente(dia_dialogoti);
        
    }
    
    public void anticipo(){
        dia_dialogoti.Limpiar();
        grid_ti.getChildren().add(new Etiqueta("TIPO SERVIDOR :"));
        grid_ti.getChildren().add(cmb_distributivo1);
        dia_dialogoti.setDialogo(grid_ti);
        dia_dialogoti.dibujar();
    }
    
    public void cancelarValores(){
        utilitario.agregarMensajeInfo("NINGUN REGISTRO FUE AFECTADO", "");
        dia_dialogoe.cerrar();
    }
    
    public void buscarColumna() {
        if (cmb_descripcion.getValue() != null && cmb_descripcion.getValue().toString().isEmpty() == false ) {
            set_rol.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE DISTRIBUTIVO="+cmb_descripcion.getValue());
            set_rol.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo", "");
        }
    }
    
    public void buscaColumna() {
            if (cmb_distributivo.getValue() != null && cmb_distributivo.getValue().toString().isEmpty() == false ) {
                set_roles.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE DISTRIBUTIVO="+cmb_distributivo.getValue());
                set_roles.getTab_seleccion().ejecutarSql();
                set_roles.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una elemento", "");
            }
        }
    
    public void aceptoDesc(){
            for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
                
            mDescuento.ActualizaDatos(tab_tabla.getValor(i, "cedula"), Integer.parseInt(set_roles.getValorSeleccionado()),Integer.parseInt(cmb_distributivo.getValue()+""));
            }
            set_roles.cerrar();
            tab_tabla.actualizar();
            cmb_distributivo.getConexion().desconectar();
    }
    
    public void completar() {
  
         Integer ano;
         Integer ide_periodo;
         Integer id_distributivo_roles;
         Integer ide_columna;
         
         tab_consulta.setConexion(con_postgres);
         tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
         tab_consulta.ejecutarSql();
         ano = Integer.parseInt(tab_consulta.getValor("ano"));
         ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_periodo"));
         id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
         ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;
         
        mDescuento.actualizarDescuento(ano, ide_periodo, id_distributivo_roles, ide_columna);
        mDescuento.actualizarDescuento1(ano, ide_periodo, id_distributivo_roles, ide_columna);
        tab_tabla.actualizar();
    }
    
    public void migrar(){
           TablaGenerica tab_dato = mDescuento.VerificarRol();
           if (!tab_dato.isEmpty()) {
               Integer ano;
               Integer ide_periodo;
               Integer id_distributivo_roles;
               Integer ide_columna;
               tab_consulta.setConexion(con_postgres);
               tab_consulta.setSql("select ano,ide_periodo,id_distributivo_roles,ide_columna from srh_descuento");
               tab_consulta.ejecutarSql();
               ano = Integer.parseInt(tab_consulta.getValor("ano"));
               ide_periodo=Integer.parseInt(tab_consulta.getValor("ide_periodo"));
               id_distributivo_roles=Integer.parseInt(tab_consulta.getValor("id_distributivo_roles"));
               ide_columna=Integer.parseInt(tab_consulta.getValor("ide_columna")) ;  
               mDescuento.migrarDescuento(ano,ide_periodo,id_distributivo_roles,ide_columna,tab_usuario.getValor("NICK_USUA")+"");
               utilitario.agregarMensaje("PROCESO REALIZADO CON EXITO", " ");
               dia_dialogoe.cerrar();
           }else{
               utilitario.agregarMensaje("Descuento No Puede Ser Subido a Rol", "Rol Perteneciente a Periodo Aun No Esta Creado");
           }
    }
                    
    public void borrar(){
      mDescuento.borrarDescuento();
      tab_tabla.actualizar();
    }
          //subida de anticcipos de sueldo a rol
   
    public void anticipo1(){

        if (cmb_distributivo1.getValue() != null && cmb_distributivo1.getValue().toString().isEmpty() == false ) {
            mDescuento.InsertarAnticipo(Integer.parseInt(cmb_distributivo1.getValue()+""));
            tab_tabla.actualizar();
            dia_dialogoti.cerrar();
        }else {
            utilitario.agregarMensajeInfo("Debe seleccionar una tipo", "");
        }
    }    
    
/*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
           case "INCONSISTENCIA EN DESCUENTOS":
            aceptoDescuentos();
               break;
           case "DESCUENTOS ANIO Y PERIODO":
                set_rol.dibujar();
                set_rol.getTab_seleccion().limpiar();
               break;
           case "ROL DE PAGOS EMPLEADOS - POR MES":
                dia_dialorol.Limpiar();
                grid_rol.getChildren().add(new Etiqueta("AÑO :"));
                grid_rol.getChildren().add(cmb_anio);
                grid_rol.getChildren().add(new Etiqueta("PERIODO :"));
                grid_rol.getChildren().add(cmb_periodo);
                grid_rol.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                grid_rol.getChildren().add(cmb_descripcion);
                dia_dialorol.setDialogo(grid_rol);
                dia_dialorol.dibujar();
               break;
           case "VERIFICAR SUBIDA A ROL":
                aceptoDescuentos();
               break;
                
        }
    } 
        
       // dibujo de reporte y envio de parametros
  public void aceptoDescuentos(){
        switch (rep_reporte.getNombre()) {
               case "INCONSISTENCIA EN DESCUENTOS":
                    p_parametros.put("nom_resp", tab_usuario.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;
               case "DESCUENTOS ANIO Y PERIODO":
                   if (set_rol.getValorSeleccionado()!= null) {
                        TablaGenerica tab_dato = mDescuento.periodo(Integer.parseInt(cmb_periodo.getValue()+""));
                         if (!tab_dato.isEmpty()) {
                             TablaGenerica tab_dato1 = mDescuento.distibutivo(Integer.parseInt(cmb_descripcion.getValue()+""));
                              if (!tab_dato1.isEmpty()) {
                                 TablaGenerica tab_dato2 = mDescuento.columnas(Integer.parseInt(set_rol.getValorSeleccionado()+""));
                                 if (!tab_dato2.isEmpty()) {
                                    p_parametros = new HashMap();
                                    p_parametros.put("pide_ano",Integer.parseInt(cmb_anio.getValue()+"")); 
                                    p_parametros.put("periodo",Integer.parseInt(cmb_periodo.getValue()+""));
                                    p_parametros.put("p_nombre",tab_dato.getValor("per_descripcion")+"");
                                    p_parametros.put("distributivo",Integer.parseInt(cmb_descripcion.getValue()+""));
                                    p_parametros.put("descripcion",tab_dato1.getValor("descripcion")+"");
                                    p_parametros.put("columnas", Integer.parseInt(set_rol.getValorSeleccionado()+""));
                                    p_parametros.put("descrip",tab_dato2.getValor("descripcion_col")+"");
                                    p_parametros.put("nom_resp", tab_usuario.getValor("NICK_USUA")+"");
                                    rep_reporte.cerrar();
                                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                    sef_formato.dibujar();
                                    set_rol.cerrar();
                                    } else {
                                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                        }
                                } else {
                                  utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                                  }
                            } else {
                              utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                              }
                      }else {
                        utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                        }
               break;
               case "ROL DE PAGOS EMPLEADOS - POR MES":
                   TablaGenerica tab_dato1 = mDescuento.distibutivo(Integer.parseInt(cmb_descripcion.getValue()+""));
                    if (!tab_dato1.isEmpty()) {
                    p_parametros = new HashMap();
                    p_parametros.put("anio",Integer.parseInt(cmb_anio.getValue()+"")); 
                    p_parametros.put("periodo",Integer.parseInt(cmb_periodo.getValue()+""));
                    p_parametros.put("distributivo",Integer.parseInt(cmb_descripcion.getValue()+""));
                    p_parametros.put("nom_distri",tab_dato1.getValor("descripcion")+"");
                    p_parametros.put("nom_resp", tab_usuario.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    } else {
                            utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                            }
               break;
               case "VERIFICAR SUBIDA A ROL":
                   p_parametros.put("nom_resp", tab_usuario.getValor("NICK_USUA")+"");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
               break;     
        }
    }
 
    
    public void abrirDialogo() {
        dia_dialogoe.dibujar();
    }
    @Override
    public void insertar() {
    utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
            con_postgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
    utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
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

    public SeleccionTabla getSet_rol() {
        return set_rol;
    }

    public void setSet_rol(SeleccionTabla set_rol) {
        this.set_rol = set_rol;
    }

    public Dialogo getDia_dialogoe() {
        return dia_dialogoe;
    }

    public void setDia_dialogoe(Dialogo dia_dialogoe) {
        this.dia_dialogoe = dia_dialogoe;
    }

    public SeleccionTabla getSet_roles() {
        return set_roles;
    }

    public void setSet_roles(SeleccionTabla set_roles) {
        this.set_roles = set_roles;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }
    
}
