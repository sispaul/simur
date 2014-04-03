/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.componentes.Boton;
import framework.componentes.Clave;
import framework.componentes.Dialogo;
import framework.componentes.ErrorSQL;
import framework.componentes.Etiqueta;
import framework.componentes.Foco;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.password.Password;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.aplicacion.Utilitario;

/**
 *
 * @author p-sistemas
 */
public class pre_ingreso extends Pantalla{
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
   public pre_ingreso() {
       
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
    
}
