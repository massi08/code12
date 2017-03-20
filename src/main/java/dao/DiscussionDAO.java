package dao;

import modele.Discussion;
import modele.Project;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */

public class DiscussionDAO {


    private EntityManager em;

    /**
     * Constructeur de DiscussionDAO
     * @param em
     */
    public DiscussionDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Crée une discussion dans la BD
     * @param UserD
     * @param ProjectD
     * @return discussion créée
     */
    public Discussion create(User UserD, Project ProjectD) {
        Discussion D = new Discussion(UserD, ProjectD);
        em.persist(D);
        return D;
    }

    /**
     * Ajoute un membre dans une discussion
     * @param idDiscussion
     * @param user
     * @param projectD
     * @return la discussion concernée
     */
    public Discussion addMember(int idDiscussion, User user, Project projectD) {
        Discussion d = new Discussion();
        d.setIdDiscussion(idDiscussion);
        d.setProject(projectD);
        d.setUser(user);
        em.persist(d) ;

        return d;
    }

    /**
     *
     * @param idDiscussion
     * @param user
     * @return
     */
    public Discussion getDiscussionByIdUser(int idDiscussion, User user) {
        List<Discussion> discussions = (List<Discussion>) em.createNamedQuery("Discussion.getByID")
                .setParameter("idDiscussion", idDiscussion)
                .setParameter("user", user)
                .setMaxResults(1)
                .getResultList();
        if(discussions.isEmpty())
            return null ;
        return discussions.get(0);
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Discussion> getDiscussionWithUser(User user){
        List<Discussion> discussions=(List<Discussion>)em.createNamedQuery("Discussion.getByUserProject")
                .setParameter("user", user)
                .getResultList();
        return discussions ;
    }

    /**
     *
     * @param discussion
     * @return
     */
    public List<User> getSpecificDiscussionUser(Discussion discussion){
        List<User> users=(List<User>)em.createNamedQuery("Discussion.getByIDanduser")
                .setParameter("idDiscussion", discussion.getIdDiscussion())
                .getResultList() ;
        return users ;
    }

    /**
     *
     * @param user
     * @param user2
     * @return
     */
    public Integer alreadyExistsDiscussion(User user, User user2){
        List<Integer> discussions=(List<Integer>)em.createNamedQuery("Discussion.alreadyExists")
                .setParameter("user", user)
                .setParameter("user2", user2)
                .getResultList() ;
        if(discussions.size() == 1)
            return discussions.get(0);
        return null ;
    }

    /**
     *
     * @param idDiscussion
     * @param project
     * @return
     */
    public List<User> UserNotInDiscussionByProject(int idDiscussion, Project project){
        List<User> users=(List<User>)em.createNamedQuery("Discussion.MemberNotInDiscussion")
                .setParameter("idDiscussion", idDiscussion)
                .setParameter("project", project)
                .getResultList() ;
        return users ;
    }

}
