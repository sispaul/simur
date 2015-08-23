/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_negocio.varios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import paq_modelo.varios.EncuestaVolcan;
import paq_modelo.varios.Oceubica;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class EncuestaFacade {

    @PersistenceContext(unitName = "SIGAG")
    private EntityManager em;

    public static List<Oceubica> getModelosByCompany(Integer companyId) {
        Query query = Persistence.createEntityManagerFactory("PruebaSIGAG").createEntityManager().createNamedQuery("Oceubica.findByOceUbiCodigo", Oceubica.class);
        query.setParameter("oceUbiCodigo", companyId);

        return (List<Oceubica>) query.getResultList();
    }

    public String guardarEncuesta(EncuestaVolcan encuesta) throws Exception {
        em.persist(encuesta);
        return "Registro Guardado Correctamente";
    }

    public String actualizarEncuesta(EncuestaVolcan encuesta) throws Exception {
        em.merge(encuesta);
        return "Registro Actualizado Correctamente";
    }

    public List<Oceubica> buscarProvincia(Integer pais) throws Exception {
        CriteriaBuilder criBui = em.getCriteriaBuilder();
        CriteriaQuery<Oceubica> criUsu = criBui.createQuery(Oceubica.class);
        Root<Oceubica> utente = criUsu.from(Oceubica.class);
        Predicate predicate = criBui.equal(utente.get("oceUbiCodigo"), pais);
        criUsu.where(predicate);
        Query conTodUsu = em.createQuery(criUsu);
        return conTodUsu.getResultList();
    }

    public List<Oceubica> buscarCanton(Integer provincia) throws Exception {
        CriteriaBuilder criBui = em.getCriteriaBuilder();
        CriteriaQuery<Oceubica> criUsu = criBui.createQuery(Oceubica.class);
        Root<Oceubica> utente = criUsu.from(Oceubica.class);
        Predicate predicate = criBui.equal(utente.get("oceUbiCodigo"), provincia);
        criUsu.where(predicate);
        Query conTodUsu = em.createQuery(criUsu);
        return conTodUsu.getResultList();
    }

    public List<Oceubica> buscarParroquia(Integer canton) throws Exception {
        CriteriaBuilder criBui = em.getCriteriaBuilder();
        CriteriaQuery<Oceubica> criUsu = criBui.createQuery(Oceubica.class);
        Root<Oceubica> utente = criUsu.from(Oceubica.class);
        Predicate predicate = criBui.equal(utente.get("oceUbiCodigo"), canton);
        criUsu.where(predicate);
        Query conTodUsu = em.createQuery(criUsu);
        return conTodUsu.getResultList();
    }

    public Oceubica buscarPorId(Integer idRol) throws Exception {
        Oceubica rolDev = em.find(Oceubica.class, idRol);
        return rolDev;
    }

    public Oceubica buscarPorCedula(String idRol) throws Exception {
        Oceubica rolDev = em.find(Oceubica.class, idRol);
        return rolDev;
    }

    public Oceubica buscarClave(String idRol) throws Exception {
        Oceubica rolDev = em.find(Oceubica.class, idRol);
        return rolDev;
    }
}
