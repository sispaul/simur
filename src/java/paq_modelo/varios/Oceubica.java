/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_modelo.varios;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author p-sistemas
 */
@Entity
@Table(name = "oceubica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oceubica.findAll", query = "SELECT o FROM Oceubica o"),
    @NamedQuery(name = "Oceubica.findByUbiCodigo", query = "SELECT o FROM Oceubica o WHERE o.ubiCodigo = :ubiCodigo"),
    @NamedQuery(name = "Oceubica.findByOceUbiCodigo", query = "SELECT o FROM Oceubica o WHERE o.oceUbiCodigo = :oceUbiCodigo"),
    @NamedQuery(name = "Oceubica.findByUbiDescri", query = "SELECT o FROM Oceubica o WHERE o.ubiDescri = :ubiDescri"),
    @NamedQuery(name = "Oceubica.findByUbiNivel", query = "SELECT o FROM Oceubica o WHERE o.ubiNivel = :ubiNivel"),
    @NamedQuery(name = "Oceubica.findByUbiUsuari", query = "SELECT o FROM Oceubica o WHERE o.ubiUsuari = :ubiUsuari"),
    @NamedQuery(name = "Oceubica.findByUbiMaquina", query = "SELECT o FROM Oceubica o WHERE o.ubiMaquina = :ubiMaquina"),
    @NamedQuery(name = "Oceubica.findByUbiFecaud", query = "SELECT o FROM Oceubica o WHERE o.ubiFecaud = :ubiFecaud"),
    @NamedQuery(name = "Oceubica.findByUbiEstado", query = "SELECT o FROM Oceubica o WHERE o.ubiEstado = :ubiEstado"),
    @NamedQuery(name = "Oceubica.findByUbiAcceso", query = "SELECT o FROM Oceubica o WHERE o.ubiAcceso = :ubiAcceso"),
    @NamedQuery(name = "Oceubica.findByCodigoDivision", query = "SELECT o FROM Oceubica o WHERE o.codigoDivision = :codigoDivision"),
    @NamedQuery(name = "Oceubica.findByUsuarioIngre", query = "SELECT o FROM Oceubica o WHERE o.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "Oceubica.findByFechaIngre", query = "SELECT o FROM Oceubica o WHERE o.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "Oceubica.findByHoraIngre", query = "SELECT o FROM Oceubica o WHERE o.horaIngre = :horaIngre"),
    @NamedQuery(name = "Oceubica.findByUsuarioActua", query = "SELECT o FROM Oceubica o WHERE o.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "Oceubica.findByFechaActua", query = "SELECT o FROM Oceubica o WHERE o.fechaActua = :fechaActua"),
    @NamedQuery(name = "Oceubica.findByHoraActua", query = "SELECT o FROM Oceubica o WHERE o.horaActua = :horaActua")})
public class Oceubica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ubi_codigo")
    private Integer ubiCodigo;
    @Column(name = "oce_ubi_codigo")
    private Integer oceUbiCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ubi_descri")
    private String ubiDescri;
    @Column(name = "ubi_nivel")
    private Integer ubiNivel;
    @Size(max = 50)
    @Column(name = "ubi_usuari")
    private String ubiUsuari;
    @Size(max = 25)
    @Column(name = "ubi_maquina")
    private String ubiMaquina;
    @Size(max = 10)
    @Column(name = "ubi_fecaud")
    private String ubiFecaud;
    @Size(max = 1)
    @Column(name = "ubi_estado")
    private String ubiEstado;
    @Size(max = 1)
    @Column(name = "ubi_acceso")
    private String ubiAcceso;
    @Size(max = 10)
    @Column(name = "codigo_division")
    private String codigoDivision;
    @Size(max = 50)
    @Column(name = "USUARIO_INGRE")
    private String usuarioIngre;
    @Size(max = 10)
    @Column(name = "FECHA_INGRE")
    private String fechaIngre;
    @Size(max = 16)
    @Column(name = "HORA_INGRE")
    private String horaIngre;
    @Size(max = 50)
    @Column(name = "USUARIO_ACTUA")
    private String usuarioActua;
    @Size(max = 10)
    @Column(name = "FECHA_ACTUA")
    private String fechaActua;
    @Size(max = 16)
    @Column(name = "HORA_ACTUA")
    private String horaActua;

    public Oceubica() {
    }

    public Oceubica(Integer ubiCodigo) {
        this.ubiCodigo = ubiCodigo;
    }

    public Oceubica(Integer ubiCodigo, String ubiDescri) {
        this.ubiCodigo = ubiCodigo;
        this.ubiDescri = ubiDescri;
    }

    public Integer getUbiCodigo() {
        return ubiCodigo;
    }

    public void setUbiCodigo(Integer ubiCodigo) {
        this.ubiCodigo = ubiCodigo;
    }

    public Integer getOceUbiCodigo() {
        return oceUbiCodigo;
    }

    public void setOceUbiCodigo(Integer oceUbiCodigo) {
        this.oceUbiCodigo = oceUbiCodigo;
    }

    public String getUbiDescri() {
        return ubiDescri;
    }

    public void setUbiDescri(String ubiDescri) {
        this.ubiDescri = ubiDescri;
    }

    public Integer getUbiNivel() {
        return ubiNivel;
    }

    public void setUbiNivel(Integer ubiNivel) {
        this.ubiNivel = ubiNivel;
    }

    public String getUbiUsuari() {
        return ubiUsuari;
    }

    public void setUbiUsuari(String ubiUsuari) {
        this.ubiUsuari = ubiUsuari;
    }

    public String getUbiMaquina() {
        return ubiMaquina;
    }

    public void setUbiMaquina(String ubiMaquina) {
        this.ubiMaquina = ubiMaquina;
    }

    public String getUbiFecaud() {
        return ubiFecaud;
    }

    public void setUbiFecaud(String ubiFecaud) {
        this.ubiFecaud = ubiFecaud;
    }

    public String getUbiEstado() {
        return ubiEstado;
    }

    public void setUbiEstado(String ubiEstado) {
        this.ubiEstado = ubiEstado;
    }

    public String getUbiAcceso() {
        return ubiAcceso;
    }

    public void setUbiAcceso(String ubiAcceso) {
        this.ubiAcceso = ubiAcceso;
    }

    public String getCodigoDivision() {
        return codigoDivision;
    }

    public void setCodigoDivision(String codigoDivision) {
        this.codigoDivision = codigoDivision;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public String getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(String fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(String horaIngre) {
        this.horaIngre = horaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public String getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(String fechaActua) {
        this.fechaActua = fechaActua;
    }

    public String getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(String horaActua) {
        this.horaActua = horaActua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ubiCodigo != null ? ubiCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oceubica)) {
            return false;
        }
        Oceubica other = (Oceubica) object;
        if ((this.ubiCodigo == null && other.ubiCodigo != null) || (this.ubiCodigo != null && !this.ubiCodigo.equals(other.ubiCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paq_modelo.varios.Oceubica[ ubiCodigo=" + ubiCodigo + " ]";
    }
    
}
