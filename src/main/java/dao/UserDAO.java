package dao;


import modele.User;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by audrey on 17/10/16.
 */
public class UserDAO {


    private EntityManager em;


    /**
     * Constructeur de UserDAO
     * @param em
     */
    public UserDAO(EntityManager em) {
        this.em = em;
    }


    /**
     * Insert un user dans la base de données un user si il y en a pas déjà un qui a le même pseudo
     * Si c'est le cas ne l'insert pas et retourne null
     *
     * @param
     * @return user inséré dans la base de données
     */
    public User create(String pseudo, String firstName, String lastName,

                       String password, String email) {
        if (IsUserExiste(pseudo)) return null;
        User u = new User(pseudo, firstName, lastName, password, email);

        em.persist(u);
        return u;
    }

    /**
     * Modifie un user dans la base de données
     *
     * @param
     * @return user inséré dans la base de données
     */
    public void manageAccount(User user,String new_password) {
        user.setPassword(new_password);
    }


    /**
     * @param
     * @return vrai si un user avec "pseudo" comme pseudo existe déjà dans la BD
     */
    public boolean IsUserExiste(String pseudo) {
        TypedQuery<User> query = em.createNamedQuery("User.getByPseudo", User.class);
        query.setParameter("pseudo", pseudo);
        List<User> results = query.getResultList();
        if (results.size() > 0) return true;
        return false;
    }

    /**
     *
     * @return tous les users
     */
    public List<User> getAllUsers() {
        TypedQuery<User> query = em.createNamedQuery("User.getAll", User.class);
        return query.getResultList();
    }


    /**
     *
     * @param s
     * @return l'user dont le pseudo est s
     */
    public User getByName(String s) {
        TypedQuery<User> query = em.createNamedQuery("User.getByPseudo", User.class);
        query.setParameter("pseudo", s);
        List<User> results = query.getResultList();
        if (results.size() == 0) return null;
        return results.get(0);
    }

    /**
     *
     * @param idUser
     * @return l'user dont l'id est "idUser"
     */
    public User getUserById(int idUser) {

        TypedQuery<User> query = em.createNamedQuery("User.getById", User.class);
        query.setParameter("id", idUser);
        List<User> results = query.getResultList();
        if (results.size() == 0) return null;
        return results.get(0);

    }

}
