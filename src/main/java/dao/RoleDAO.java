package dao;

import modele.Role;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class RoleDAO {

    private EntityManager em;

    /**
     * Constructeur de RoleDAO
     * @param em
     */
    public RoleDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Crée un role dans la BD
     * @param name
     * @return le role créé
     */
    public Role create(String name) {
        Role r = new Role(name);
        em.persist(r);
        return r;
    }

    /**
     *
     * @param name
     * @return le role ayant pour nom "name"
     */
    public Role getByName(String name) {
        TypedQuery<Role> query = em.createNamedQuery("Role.getByName", Role.class);
        query.setParameter("name", name);
        if (query.getResultList().size() == 0) return null;
        return query.getResultList().get(0);
    }

    /**
     *
     * @param idRole
     * @return le rôle ayant l'id "idRole"
     */
    public Role getRoleById(int idRole) {
        TypedQuery<Role> query = em.createNamedQuery("Role.getById", Role.class);
        query.setParameter("id", idRole);
        if (query.getResultList().size() == 0) return null;
        return query.getResultList().get(0);
    }

}
