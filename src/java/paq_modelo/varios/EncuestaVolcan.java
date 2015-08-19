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
@Table(name = "encuesta_volcan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaVolcan.findAll", query = "SELECT e FROM EncuestaVolcan e"),
    @NamedQuery(name = "EncuestaVolcan.findByIdCodigo", query = "SELECT e FROM EncuestaVolcan e WHERE e.idCodigo = :idCodigo"),
    @NamedQuery(name = "EncuestaVolcan.findByFechaRegistro", query = "SELECT e FROM EncuestaVolcan e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EncuestaVolcan.findByCedulaEncuestado", query = "SELECT e FROM EncuestaVolcan e WHERE e.cedulaEncuestado = :cedulaEncuestado"),
    @NamedQuery(name = "EncuestaVolcan.findByNombreEncuestado", query = "SELECT e FROM EncuestaVolcan e WHERE e.nombreEncuestado = :nombreEncuestado"),
    @NamedQuery(name = "EncuestaVolcan.findByTipoVivienda", query = "SELECT e FROM EncuestaVolcan e WHERE e.tipoVivienda = :tipoVivienda"),
    @NamedQuery(name = "EncuestaVolcan.findByCedulaPropietario", query = "SELECT e FROM EncuestaVolcan e WHERE e.cedulaPropietario = :cedulaPropietario"),
    @NamedQuery(name = "EncuestaVolcan.findByNombrePropietario", query = "SELECT e FROM EncuestaVolcan e WHERE e.nombrePropietario = :nombrePropietario"),
    @NamedQuery(name = "EncuestaVolcan.findByPropietarioDireccion", query = "SELECT e FROM EncuestaVolcan e WHERE e.propietarioDireccion = :propietarioDireccion"),
    @NamedQuery(name = "EncuestaVolcan.findByNumeroIntegrantes", query = "SELECT e FROM EncuestaVolcan e WHERE e.numeroIntegrantes = :numeroIntegrantes"),
    @NamedQuery(name = "EncuestaVolcan.findByNumeroNinos", query = "SELECT e FROM EncuestaVolcan e WHERE e.numeroNinos = :numeroNinos"),
    @NamedQuery(name = "EncuestaVolcan.findByNumeroAdultos", query = "SELECT e FROM EncuestaVolcan e WHERE e.numeroAdultos = :numeroAdultos"),
    @NamedQuery(name = "EncuestaVolcan.findByNumeroDiscapacitados", query = "SELECT e FROM EncuestaVolcan e WHERE e.numeroDiscapacitados = :numeroDiscapacitados"),
    @NamedQuery(name = "EncuestaVolcan.findByLugarRefugiarse", query = "SELECT e FROM EncuestaVolcan e WHERE e.lugarRefugiarse = :lugarRefugiarse"),
    @NamedQuery(name = "EncuestaVolcan.findByTipoRefugio", query = "SELECT e FROM EncuestaVolcan e WHERE e.tipoRefugio = :tipoRefugio"),
    @NamedQuery(name = "EncuestaVolcan.findByParentesco", query = "SELECT e FROM EncuestaVolcan e WHERE e.parentesco = :parentesco"),
    @NamedQuery(name = "EncuestaVolcan.findByProvincia", query = "SELECT e FROM EncuestaVolcan e WHERE e.provincia = :provincia"),
    @NamedQuery(name = "EncuestaVolcan.findByCanton", query = "SELECT e FROM EncuestaVolcan e WHERE e.canton = :canton"),
    @NamedQuery(name = "EncuestaVolcan.findByParroquia", query = "SELECT e FROM EncuestaVolcan e WHERE e.parroquia = :parroquia"),
    @NamedQuery(name = "EncuestaVolcan.findByBarrio", query = "SELECT e FROM EncuestaVolcan e WHERE e.barrio = :barrio")})
public class EncuestaVolcan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_codigo")
    private Long idCodigo;
    @Size(max = 10)
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Size(max = 13)
    @Column(name = "cedula_encuestado")
    private String cedulaEncuestado;
    @Size(max = 150)
    @Column(name = "nombre_encuestado")
    private String nombreEncuestado;
    @Size(max = 35)
    @Column(name = "tipo_vivienda")
    private String tipoVivienda;
    @Size(max = 13)
    @Column(name = "cedula_propietario")
    private String cedulaPropietario;
    @Size(max = 120)
    @Column(name = "nombre_propietario")
    private String nombrePropietario;
    @Size(max = 150)
    @Column(name = "propietario_direccion")
    private String propietarioDireccion;
    @Column(name = "numero_integrantes")
    private Integer numeroIntegrantes;
    @Column(name = "numero_ninos")
    private Integer numeroNinos;
    @Column(name = "numero_adultos")
    private Integer numeroAdultos;
    @Column(name = "numero_discapacitados")
    private Integer numeroDiscapacitados;
    @Size(max = 35)
    @Column(name = "lugar_refugiarse")
    private String lugarRefugiarse;
    @Size(max = 35)
    @Column(name = "tipo_refugio")
    private String tipoRefugio;
    @Size(max = 50)
    @Column(name = "parentesco")
    private String parentesco;
    @Column(name = "provincia")
    private Integer provincia;
    @Column(name = "canton")
    private Integer canton;
    @Column(name = "parroquia")
    private Integer parroquia;
    @Size(max = 120)
    @Column(name = "barrio")
    private String barrio;

    public EncuestaVolcan() {
    }

    public EncuestaVolcan(Long idCodigo) {
        this.idCodigo = idCodigo;
    }

    public Long getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(Long idCodigo) {
        this.idCodigo = idCodigo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCedulaEncuestado() {
        return cedulaEncuestado;
    }

    public void setCedulaEncuestado(String cedulaEncuestado) {
        this.cedulaEncuestado = cedulaEncuestado;
    }

    public String getNombreEncuestado() {
        return nombreEncuestado;
    }

    public void setNombreEncuestado(String nombreEncuestado) {
        this.nombreEncuestado = nombreEncuestado;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public String getCedulaPropietario() {
        return cedulaPropietario;
    }

    public void setCedulaPropietario(String cedulaPropietario) {
        this.cedulaPropietario = cedulaPropietario;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getPropietarioDireccion() {
        return propietarioDireccion;
    }

    public void setPropietarioDireccion(String propietarioDireccion) {
        this.propietarioDireccion = propietarioDireccion;
    }

    public Integer getNumeroIntegrantes() {
        return numeroIntegrantes;
    }

    public void setNumeroIntegrantes(Integer numeroIntegrantes) {
        this.numeroIntegrantes = numeroIntegrantes;
    }

    public Integer getNumeroNinos() {
        return numeroNinos;
    }

    public void setNumeroNinos(Integer numeroNinos) {
        this.numeroNinos = numeroNinos;
    }

    public Integer getNumeroAdultos() {
        return numeroAdultos;
    }

    public void setNumeroAdultos(Integer numeroAdultos) {
        this.numeroAdultos = numeroAdultos;
    }

    public Integer getNumeroDiscapacitados() {
        return numeroDiscapacitados;
    }

    public void setNumeroDiscapacitados(Integer numeroDiscapacitados) {
        this.numeroDiscapacitados = numeroDiscapacitados;
    }

    public String getLugarRefugiarse() {
        return lugarRefugiarse;
    }

    public void setLugarRefugiarse(String lugarRefugiarse) {
        this.lugarRefugiarse = lugarRefugiarse;
    }

    public String getTipoRefugio() {
        return tipoRefugio;
    }

    public void setTipoRefugio(String tipoRefugio) {
        this.tipoRefugio = tipoRefugio;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Integer getProvincia() {
        return provincia;
    }

    public void setProvincia(Integer provincia) {
        this.provincia = provincia;
    }

    public Integer getCanton() {
        return canton;
    }

    public void setCanton(Integer canton) {
        this.canton = canton;
    }

    public Integer getParroquia() {
        return parroquia;
    }

    public void setParroquia(Integer parroquia) {
        this.parroquia = parroquia;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCodigo != null ? idCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaVolcan)) {
            return false;
        }
        EncuestaVolcan other = (EncuestaVolcan) object;
        if ((this.idCodigo == null && other.idCodigo != null) || (this.idCodigo != null && !this.idCodigo.equals(other.idCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paq_modelo.varios.EncuestaVolcan[ idCodigo=" + idCodigo + " ]";
    }
    
}
