/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_beans;

import framework.componentes.Boton;
import framework.componentes.Clave;
import framework.componentes.Dialogo;
import framework.componentes.ErrorSQL;
import framework.componentes.Etiqueta;
import framework.componentes.Foco;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Texto;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.password.Password;
import paq_sistema.aplicacion.Utilitario;
import paq_sistema.ejb.ServicioSeguridad;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
@ManagedBean
@SessionScoped
public class pre_login {

    private HtmlForm formulario = new HtmlForm();
    private Texto tex_usuario = new Texto();
    private Password pas_clave = new Password();
    private ErrorSQL error_sql = new ErrorSQL();
    private Utilitario utilitario = new Utilitario();
    private HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser            
    private Clave cla_clave_actual = new Clave();
    private Clave cla_nueva = new Clave();
    private Clave cla_confirmar = new Clave();
    private Dialogo dia_cambia = new Dialogo();
    @EJB
    private ServicioSeguridad ser_seguridad;

    /**
     * Creates a new instance of pre_login
     */
    public pre_login() {
        formulario.setTransient(true);
        Grid gri_matriz = new Grid();
        gri_matriz.setColumns(2);
        gri_matriz.setWidth("100%");
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 350px;");
        pan_panel.setHeader("Ingresar al Sistema");

        tex_usuario.setId("tex_usuario");
        tex_usuario.setRequired(true);
        tex_usuario.setRequiredMessage("Debe ingresar el usuario");
        tex_usuario.setStyle("width: 99%;");
        gri_matriz.getChildren().add(new Etiqueta("USUARIO : "));
        gri_matriz.getChildren().add(tex_usuario);
        gri_matriz.getChildren().add(new Etiqueta("CLAVE :"));
        pas_clave.setFeedback(false);
        pas_clave.setRequired(true);
        pas_clave.setRequiredMessage("Debe ingresar la clave");
        pas_clave.setStyle("width: 99%;");
        gri_matriz.getChildren().add(pas_clave);
        Boton bot_login = new Boton();
        bot_login.setId("bot_login");
        bot_login.setValue("Aceptar");
        bot_login.setIcon("ui-icon-locked");
        bot_login.setMetodoRuta("pre_login.ingresar");
        bot_login.setOnclick("dimiensionesNavegador()");
        gri_matriz.setFooter(bot_login);
        Foco foc_foco = new Foco();
        foc_foco.setFor("tex_usuario");
        formulario.getChildren().add(foc_foco);
        Grid gri_login = new Grid();
        gri_login.setColumns(2);
        Imagen ima_llave = new Imagen();
        ima_llave.setValue("imagenes/im_llave.png");
        gri_login.setWidth("100%");
        gri_login.getChildren().add(ima_llave);
        gri_login.getChildren().add(gri_matriz);
        pan_panel.getChildren().add(gri_login);
        pan_panel.setTransient(true);
        BlockUI blo_panel = new BlockUI();
        blo_panel.setBlock("pan_panel");
        blo_panel.setTrigger("bot_login");
        formulario.getChildren().add(blo_panel);
        error_sql.setId("error_sql");
        error_sql.setMetodoAceptar("pre_login.cerrarSql");
        formulario.getChildren().add(error_sql);
        formulario.getChildren().add(pan_panel);
        alto.setId("alto");
        formulario.getChildren().add(alto);
        ancho.setId("ancho");
        formulario.getChildren().add(ancho);

        //Para cambiar contraseña
        dia_cambia.setId("dia_cambia");
        dia_cambia.setTitle("Cambiar la clave");
        dia_cambia.getBot_cancelar().setMetodoRuta("pre_login.cancelarCambiarClave");
        dia_cambia.getBot_aceptar().setMetodoRuta("pre_login.aceptarCambiarClave");
        cla_nueva.setFeedback(true);
        cla_confirmar.setFeedback(true);
        cla_clave_actual.setDisabled(true);
        Grid gri_matriz2 = new Grid();
        gri_matriz2.setColumns(2);
        gri_matriz2.getChildren().add(new Etiqueta("CLAVE ACTUAL :"));
        gri_matriz2.getChildren().add(cla_clave_actual);
        gri_matriz2.getChildren().add(new Etiqueta("CLAVE NUEVA :"));
        gri_matriz2.getChildren().add(cla_nueva);
        gri_matriz2.getChildren().add(new Etiqueta("CONFIRMAR CLAVE NUEVA :"));
        gri_matriz2.getChildren().add(cla_confirmar);
        dia_cambia.setDialogo(gri_matriz2);
        formulario.getChildren().add(dia_cambia);
    }

    public void ingresar() {
        Conexion conexion = utilitario.getConexion();
        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
            utilitario.crearVariable("IDE_EMPR", utilitario.getPropiedad("ide_empr"));
        }
        String str_mensaje = ser_seguridad.ingresar(tex_usuario.getValue().toString(), pas_clave.getValue().toString());
        if (str_mensaje.isEmpty()) {
            //Valida si tiene que cambiar la clave
            if (ser_seguridad.isCambiarClave(utilitario.getVariable("IDE_USUA"))) {
                cambiarClave();
            } else {
                try {
                    utilitario.crearVariable("NICK", tex_usuario.getValue() + "");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pre_index");
                    utilitario.ejecutarJavaScript("location.href='about:blank'");
                    String str_url = utilitario.getURL() + "/index.jsf'";                   
                    utilitario.ejecutarJavaScript("window.open('" + str_url + ",'nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=" + ancho.getValue() + ",height=" + alto.getValue() + ",maximized=yes,resizable=yes')");


                } catch (Exception e) {
                }
            }
        } else {
            utilitario.agregarMensajeError(str_mensaje, "");
            utilitario.addUpdate("pan_panel");
        }
    }

    private void cambiarClave() {
        dia_cambia.setWidth("40%");
        dia_cambia.setHeight("35%");
        cla_clave_actual.setValue(pas_clave.getValue());
        cla_confirmar.setValue("");
        cla_nueva.setValue("");
        dia_cambia.dibujar();
    }

    public void aceptarCambiarClave() {
        if (cla_nueva.getValue() == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la nueva clave");
            return;
        }
        if (cla_confirmar.getValue() == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario confirmar la nueva clave");
            return;
        }

        if (!cla_nueva.getValue().toString().equals(cla_confirmar.getValue().toString())) {
            utilitario.agregarMensajeInfo("Validación", "La clave nueva debe se igual a la clave de confirmación");
            return;
        }
        if (cla_clave_actual.getValue() != null && !cla_clave_actual.getValue().toString().isEmpty()) {
            String str_mensaje = ser_seguridad.getClaveValida(cla_nueva.getValue().toString());
            if (str_mensaje.isEmpty()) {
                if (ser_seguridad.isClaveNueva(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString())) {
                    ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString());
                    dia_cambia.cerrar();
                    try {
                        //Registro el ingreso en auditoria
                        utilitario.getConexion().ejecutarSql(ser_seguridad.crearSQLAuditoriaAcceso(utilitario.getVariable("IDE_USUA"), ser_seguridad.P_SIS_INGRESO_USUARIO, "El sistema le solicitó a cambiar de clave"));
                        utilitario.crearVariable("NICK", tex_usuario.getValue() + ""); //Usuario                                     
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pre_index");
                        utilitario.ejecutarJavaScript("location.href='about:blank'");
                        String str_url = utilitario.getURL() + "/index.jsf'";                       
                        utilitario.ejecutarJavaScript("window.open('" + str_url + ",'nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=" + ancho.getValue() + ",height=" + alto.getValue() + ",maximized=yes,resizable=yes')");

                    } catch (Exception e) {
                    }

                } else {
                    utilitario.agregarMensajeInfo("Clave no válida", "La clave ingresada ya fue utilizada anteriormente, intente con otra clave");
                }
            } else {
                utilitario.agregarMensajeInfo(str_mensaje, "");
            }

        } else {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la clave actual");
        }
    }

    public void cancelarCambiarClave() {
        dia_cambia.cerrar();
    }

    public void caducoSession() {
        try {
            if (utilitario.getConexion() != null) {
                utilitario.ejecutarJavaScript("window.close()");
                ser_seguridad.caduco(utilitario.getVariable("IDE_USUA"));
                utilitario.getConexion().desconectar();
            }
            utilitario.cerrarSesion();
        } catch (Exception ex) {
        }
    }

    public void salir() {
        try {
            if (utilitario.getConexion() != null) {
                ser_seguridad.salir(utilitario.getVariable("IDE_USUA"));
                utilitario.getConexion().desconectar();
            }
            utilitario.cerrarSesion();
            utilitario.ejecutarJavaScript("window.close()");
        } catch (Exception ex) {
        }
    }

    public String getTema() {
        String tema = utilitario.getVariable("TEMA");
        if (tema == null || tema.isEmpty() || tema.equals("null")) {
            if (utilitario.getPropiedad("temaInicial") != null) {
                return utilitario.getPropiedad("temaInicial");
            }
            return "home";
        } else {
            return tema;
        }
    }

    public ErrorSQL getError_sql() {
        return error_sql;
    }

    public void cerrarSql() {
        error_sql.setVisible(false);
    }

    public void setError_sql(ErrorSQL error_sql) {
        this.error_sql = error_sql;
    }

    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }
}
