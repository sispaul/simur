/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_managed.varios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import paq_modelo.varios.EncuestaVolcan;
import paq_modelo.varios.Oceubica;
import paq_negocio.varios.EncuestaFacade;
import paq_sistema.aplicacion.Utilitario;
import paq_utilitario.varios.FacesUtil;

/**
 *
 * @author p-sistemas
 */
@ManagedBean
@RequestScoped
public class EncuestaBean {

    private EncuestaVolcan encuesta;
    private List<EncuestaVolcan> listaEncuesta;
    private List<SelectItem> listaProvincias;
    private List<SelectItem> listaCanton;
    private List<SelectItem> listaParroquias;
    private Integer idProv;
    private Integer idCan;
    private Integer idPar;
    @EJB
    private EncuestaFacade adminEncuesta;
    private Utilitario utilitario;
    private Date currentDate;

    public EncuestaBean() {
        this.encuesta = new EncuestaVolcan();
        this.listaEncuesta = new ArrayList<>();
        this.listaProvincias = new ArrayList<>();
        this.listaCanton = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
    }

    public EncuestaVolcan getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(EncuestaVolcan encuesta) {
        this.encuesta = encuesta;
    }

    public List<EncuestaVolcan> getListaEncuesta() {
        return listaEncuesta;
    }

    public void setListaEncuesta(List<EncuestaVolcan> listaEncuesta) {
        this.listaEncuesta = listaEncuesta;
    }

    public Integer getIdProv() {
        return idProv;
    }

    public void setIdProv(Integer idProv) {
        this.idProv = idProv;
    }

    public Integer getIdCan() {
        return idCan;
    }

    public void setIdCan(Integer idCan) {
        this.idCan = idCan;
    }

    public Integer getIdPar() {
        return idPar;
    }

    public void setIdPar(Integer idPar) {
        this.idPar = idPar;
    }

    public List<SelectItem> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<SelectItem> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List<SelectItem> getListaCanton() {
        return listaCanton;
    }

    public void setListaCanton(List<SelectItem> listaCanton) {
        this.listaCanton = listaCanton;
    }

    public List<SelectItem> getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List<SelectItem> listaParroquias) {
        this.listaParroquias = listaParroquias;
    }

    private void cargarProvincias() {
        try {
            this.listaProvincias.clear();
            for (Oceubica provTMP : adminEncuesta.buscarProvincia(2)) {
                this.listaProvincias.add(new SelectItem(provTMP.getUbiCodigo(), provTMP.getUbiDescri()));
            }
        } catch (Exception ex) {
        }
    }

    public void cargarCantones() {
        try {
            if (idProv != null && !idProv.equals("")) {
                this.listaCanton.clear();
                for (Oceubica provTMP : adminEncuesta.buscarCanton(idProv)) {
                    this.listaCanton.add(new SelectItem(provTMP.getUbiCodigo(), provTMP.getUbiDescri()));
                }
            }
        } catch (Exception ex) {
        }
    }

    public void cargarParroquias() {
        try {
            if (idCan != null && !idCan.equals("")) {
                this.listaParroquias.clear();
                for (Oceubica provTMP : adminEncuesta.buscarParroquia(idCan)) {
                    this.listaParroquias.add(new SelectItem(provTMP.getUbiCodigo(), provTMP.getUbiDescri()));
                }
            }
        } catch (Exception ex) {
        }
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public String resetearFoormulario() {
        this.encuesta = new EncuestaVolcan();
        this.idProv = 0;
        this.idCan = 0;
        this.idPar = 0;
        return null;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String guardarEncuesta() {
        try {
            String mensaje = null;
            if (encuesta.getIdCodigo() != null) {
                mensaje = adminEncuesta.actualizarEncuesta(encuesta);
            } else {
                mensaje = adminEncuesta.guardarEncuesta(encuesta);
            }
            resetearFoormulario();
//            FacesUtil.anadirMensaje(1, mensaje);
        } catch (Exception ex) {
//            FacesUtil.anadirMensaje(3, "Error al guardar" + ex.getMessage());
        }
        return null;
    }

    public String validarCedula() {
        try {
            if (encuesta.getCedulaEncuestado() != null) {
                System.err.println("inhg");
            } else {
                System.err.println("inhg22");
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public String validarClave() {
        try {
            if (encuesta.getCedulaPropietario() != null) {
                System.err.println("inhg");
            } else {
                System.err.println("inhg22");
            }
        } catch (Exception ex) {
        }
        return null;
    }

    @PostConstruct
    public void init() {
        currentDate = new Date();
        cargarProvincias();
    }
}
