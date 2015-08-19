/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_utilitario.varios;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Clases para administrar las utilidades de JSF
 *
 * @author cfabara
 */
public class FacesUtil {

    public static void anadirMensaje(int tipoerror, String mensaje) {
        FacesMessage mg = new FacesMessage();
        mg.setSummary(mensaje);

        if (tipoerror == 1) {
            mg.setSeverity(FacesMessage.SEVERITY_INFO);
        } else if (tipoerror == 2) {
            mg.setSeverity(FacesMessage.SEVERITY_ERROR);
        } else {
            mg.setSeverity(FacesMessage.SEVERITY_ERROR);
        }
        FacesContext.getCurrentInstance().addMessage("frmEncues", mg);
    }

    public static Object recuperarParametro(String parametro) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext conServlet = fc.getExternalContext();
        return conServlet.getRequestParameterMap().get(parametro);
    }
}
