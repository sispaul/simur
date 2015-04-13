/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.decimoCuarto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_acceso extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //Tablas
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
    @EJB
    private decimoCuarto datosEmpledo = (decimoCuarto) utilitario.instanciarEJB(decimoCuarto.class);
    
    public pre_solicitud_acceso() {
        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Auto busqueda para, verificar solicitud
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT s.id_solicitud_acceso,\n" +
                "s.cedula_solicitante,\n" +
                "s.nombre_solicitante,\n" +
                "s.direccion_solicitante,\n" +
                "s.nombre_usuario,\n" +
                "a.nombre_sistema\n" +
                "FROM sca_solicitud_acceso s\n" +
                "INNER JOIN sca_sistemas a ON s.id_sistema = a.id_sistema\n" +
                "WHERE s.estado_solicitud = 'Solicitud'");
        aut_busca.setMetodoChange("filtrarSolicitud");
        aut_busca.setSize(80);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud de Acceso:"));
        bar_botones.agregarComponente(aut_busca);
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("SOLICITUD DE CLAVE DE ACCESO A SISTEMAS");
        agregarComponente(pan_opcion);
        
        dibujarSolicitud();
        
    }

    public void dibujarSolicitud(){
        limpiarPanel();
        tab_solicitud.setId("tab_solicitud");
        tab_solicitud.setConexion(con_postgres);
        tab_solicitud.setTabla("sca_solicitud_acceso", "id_solicitud_acceso", 1);
        /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_solicitud.setCondicion("id_solicitud_acceso=-1");
        } else {
            tab_solicitud.setCondicion("id_solicitud_acceso=" + aut_busca.getValor());
        }
        
        tab_solicitud.getColumna("fechaing_solicitante").setValorDefecto(utilitario.getFechaActual());
        
        tab_solicitud.getColumna("id_sistema").setCombo("SELECT id_sistema,nombre_sistema FROM sca_sistemas order by nombre_sistema");
        tab_solicitud.getColumna("id_modulo").setCombo("SELECT id_modulo,nombre_modulo FROM sca_modulos order by nombre_modulo");
        tab_solicitud.getColumna("id_perfil").setCombo("SELECT id_perfil,nombre_perfil FROM sca_perfiles order by nombre_perfil");
        tab_solicitud.getColumna("codigo_usuario").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_solicitud.getColumna("direccion_solicitante").setCombo("select cod_direccion,nombre_dir from srh_direccion order by nombre_dir");
        tab_solicitud.getColumna("direccion_solicitante").setFiltroContenido();
        tab_solicitud.getColumna("cargo_solicitante").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("cargo_solicitante").setFiltroContenido();
        tab_solicitud.getColumna("cargo_usuario").setCombo("select cod_cargo,nombre_cargo from srh_cargos order by nombre_cargo");
        tab_solicitud.getColumna("cargo_usuario").setFiltroContenido();
        tab_solicitud.getColumna("codigo_solicitante").setCombo("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tab_solicitud.getColumna("codigo_solicitante").setFiltroContenido();

        tab_solicitud.getColumna("ingreso_perfil_usuario").setCheck();
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setCheck();
        tab_solicitud.getColumna("lectura_perfil_usuario").setCheck();
        
        tab_solicitud.getColumna("codigo_solicitante").setMetodoChange("datosSolicitante");
        tab_solicitud.getColumna("codigo_usuario").setMetodoChange("datosUsuario");
        tab_solicitud.getColumna("id_sistema").setMetodoChange("datosModulo");
        tab_solicitud.getColumna("id_modulo").setMetodoChange("datosPerfil");
        tab_solicitud.getColumna("id_perfil").setMetodoChange("activaCasillas");
                
        tab_solicitud.getColumna("bandera_solicitante").setEtiqueta();
        tab_solicitud.getColumna("bandera_usuario").setEtiqueta();
        tab_solicitud.getColumna("bandera_perfil").setEtiqueta();
        
        tab_solicitud.getColumna("ingreso_perfil_usuario").setLectura(true);
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setLectura(true);
        tab_solicitud.getColumna("lectura_perfil_usuario").setLectura(true);
        
        tab_solicitud.getColumna("estado_solicitud").setVisible(false);
        tab_solicitud.getColumna("cedula_solicitante").setVisible(false);
        tab_solicitud.getColumna("nombre_solicitante").setVisible(false);
        tab_solicitud.getColumna("cedula_usuario").setVisible(false);
        tab_solicitud.getColumna("nombre_usuario").setVisible(false);
        tab_solicitud.getColumna("direccion_usuario").setVisible(false);
        tab_solicitud.getColumna("fecha_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("login_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("password_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("cedula_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("codigo_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("nombre_asigna_acceso").setVisible(false);
        tab_solicitud.getColumna("logining_solicitante").setVisible(false);
        tab_solicitud.getColumna("fechact_solicitante").setVisible(false);
        tab_solicitud.getColumna("loginact_solcitante").setVisible(false);
        tab_solicitud.getColumna("logining_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("fechact_acceso_usuario").setVisible(false);
        tab_solicitud.getColumna("loginact_acceso_usuario").setVisible(false);
        
        tab_solicitud.getColumna("logining_solicitante").setValorDefecto(utilitario.getVariable("NICK"));
        
        tab_solicitud.setTipoFormulario(true);
        tab_solicitud.dibujar();
        PanelTabla tps = new PanelTabla();
        tps.setPanelTabla(tab_solicitud);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(tps);
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        pan_opcion.getChildren().add(gru);
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujarSolicitud();
    }
    
    public void datosModulo(){
        tab_solicitud.getColumna("id_modulo").setCombo("SELECT id_modulo,nombre_modulo FROM sca_modulos where id_sistema="+Integer.parseInt(tab_solicitud.getValor("id_sistema"))+" order by nombre_modulo");
        utilitario.addUpdateTabla(tab_solicitud,"id_modulo","");
    }
    
    public void datosPerfil(){
        tab_solicitud.getColumna("id_perfil").setCombo("SELECT id_perfil,nombre_perfil FROM sca_perfiles where id_modulo="+Integer.parseInt(tab_solicitud.getValor("id_modulo"))+" order by nombre_perfil");
        utilitario.addUpdateTabla(tab_solicitud,"id_perfil","");
    }

    public void datosUsuario(){
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleado(tab_solicitud.getValor("codigo_usuario"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_usuario", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_usuario", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("cargo_usuario", tab_dato.getValor("cod_cargo"));
            tab_solicitud.setValor("direccion_usuario", tab_dato.getValor("cod_direccion"));
            utilitario.addUpdate("tab_solicitud");
        }else{
            utilitario.agregarMensaje("Usuario Sin Datos", "");
        }
    }
    public void datosSolicitante(){
        TablaGenerica tab_dato = datosEmpledo.getDatoEmpleado(tab_solicitud.getValor("codigo_solicitante"));
        if (!tab_dato.isEmpty()) {
            tab_solicitud.setValor("cedula_solicitante", tab_dato.getValor("cedula_pass"));
            tab_solicitud.setValor("nombre_solicitante", tab_dato.getValor("nombres"));
            tab_solicitud.setValor("cargo_solicitante", tab_dato.getValor("cod_cargo"));
            tab_solicitud.setValor("direccion_solicitante", tab_dato.getValor("cod_direccion"));
            utilitario.addUpdate("tab_solicitud");
        }else{
            utilitario.agregarMensaje("Solicitante Sin Datos", "");
        }
    }
    
    public void activaCasillas(){
        tab_solicitud.getColumna("ingreso_perfil_usuario").setLectura(false);
        tab_solicitud.getColumna("actualizacion_perfil_usuario").setLectura(false);
        tab_solicitud.getColumna("lectura_perfil_usuario").setLectura(false);
        utilitario.addUpdate("tab_solicitud");
    }
    
    public void estadoAcceso(){
        tab_solicitud.setValor("estado_solicitud", "Solicitud");
        utilitario.addUpdate("tab_solicitud");
    }
    
    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        estadoAcceso();
        if(tab_solicitud.getValor("estado_solicitud").equals("Solicitud")){
            if (tab_solicitud.guardar()) {
                con_postgres.guardarPantalla();
            }
        }
        else{
            utilitario.agregarMensaje("Solicitud Se Encuentra en Ejecución", null);
        }
//        envioMail();
    }
    
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
}
