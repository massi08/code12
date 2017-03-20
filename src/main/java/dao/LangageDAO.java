package dao;

import modele.Langage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class LangageDAO {

    private EntityManager em;

    /**
     * Constructeur de LangageDAO
     * @param em
     */
    public LangageDAO(EntityManager em) {
        this.em = em;
    }


    /**
     * Crée un langage dans la BD
     * @param name
     * @return langage créé, null si un langage du même nom existe déjà
     */
    public Langage create(String name) {
        if (getByName(name) != null) return null;
        Langage u = new Langage(name);
        em.persist(u);
        return u;
    }

    /**
     *
     * @param name
     * @return le langage dans le name est name, null si il n'existe pas dans la BD
     */
    public Langage getByName(String name) {
        TypedQuery<Langage> query = em.createNamedQuery("Langage.getByName", Langage.class);
        query.setParameter("name", name);
        if (query.getResultList().size() == 0) return null;
        return query.getResultList().get(0);
    }

    /**
     *
     * @return tous les langages dans la BD
     */
    public List<Langage> getAll() {
        TypedQuery<Langage> query = em.createNamedQuery("Langage.getAll", Langage.class);
        return query.getResultList();
    }


}


