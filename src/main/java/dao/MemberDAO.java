package dao;

import modele.Member;
import modele.Project;
import modele.Role;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class MemberDAO {


    private EntityManager em;

    /**
     * Constructeur de MemberDAO
     * @param em
     */
    public MemberDAO(EntityManager em) {
        this.em = em;
    }

    /**
     *
     * @param p
     * @return la liste des membres du project p
     */
    public List<Member> getAllMembersByProject(Project p) {
        TypedQuery<Member> query = em.createNamedQuery("Member.getAllByProject", Member.class);
        query.setParameter("idProject", p);
        return query.getResultList();
    }

    /**
     *
     * @param p
     * @param user
     * @return la liste des membres du project p
     */
    public List<Member> getAllMembersByProjectExceptOne(Project p, User user) {
        TypedQuery<Member> query = em.createNamedQuery("Member.getAllByProjectExceptOne", Member.class);
        query.setParameter("user", user);
        query.setParameter("idProject", p);
        return query.getResultList();
    }

    /**
     * Ajoute un membre dans la table membre avec role et project
     * @param user
     * @param role
     * @param project
     * @return le membre ajouté
     */
    public Member create(User user, Role role, Project project) {
        Member member = new Member(user, project, role);
        em.persist(member);
        return member;
    }

    /**
     *
     * @param u
     * @param p
     * @return le membre correspondant à l'utilisateur u dans le project p
     */
    public Member getMember(User u, Project p) {
        TypedQuery<Member> query = em.createNamedQuery("Member.getByUserProject", Member.class);
        query.setParameter("idProject", p);
        query.setParameter("idUser", u);
        if (query.getResultList().size() == 0) return null;
        else return query.getResultList().get(0);

    }


    /**
     *
     * @param u
     * @param r
     * @return tous les project d'un utilisateur u
     */
    public List<Project> getProjects(User u, Role r) {
        TypedQuery<Project> query = em.createNamedQuery("Member.getAllProjectByUser", Project.class);
        query.setParameter("idUser", u);
        query.setParameter("idRole", r);
        return query.getResultList();
    }

    /**
     *
     * @param u
     * @param p
     * @return
     */
    public Role getRoleByIdUserandProject(User u, Project p) {
        TypedQuery<Role> query = em.createNamedQuery("Member.getRolebyUserandProject", Role.class);
        query.setParameter("idUser", u);
        query.setParameter("idProject", p);
        if (query.getResultList().size() == 0) return null;
        else return query.getResultList().get(0);
    }

    /**
     *
     * @param projectid
     * @param user
     * @return tous les projets auxquels l'user "user" fait partie
     */

    public List<Object[]> getMemberFromProjectAndUser(int projectid, User user) {
        String query = "SELECT p, m " +
                "FROM Member m, User u, Project p" +
                " WHERE m.idProject=p and m.idUser=u and u=:user and p.idProject=:projectid";

        List<Object[]> res = em.createQuery(query)
                .setParameter("user", user)
                .setParameter("projectid", projectid)
                .getResultList();
        return res;
    }

    /**
     *
     * @param p
     * @param oldRole
     * @return tous les membres actifs du projet p
     */
    public List<Member> getAllActifMemberByProject(Project p, Role oldRole) {
        TypedQuery<Member> query = em.createNamedQuery("Member.getAllActifByProject", Member.class);
        query.setParameter("idProject", p);
        query.setParameter("idRole", oldRole);
        return query.getResultList();

    }

    /**
     *
     * @param p
     * @param r
     * @return le nombre de chef de projet du projet p
     */
    public int getNbChefByProject(Project p, Role r){
        TypedQuery<Member> query = em.createNamedQuery("Member.getChefByProject", Member.class);
        query.setParameter("idProject", p);
        query.setParameter("idRole", r);

        return query.getResultList().size();

    }

}