package metier;

import dao.*;
import modele.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by audrey on 15/11/16.
 */
public class MemberManager {

    private MemberDAO memberDAO;
    private RoleDAO roleDAO;
    private EntityManager em;

    /**
     * Constructeur de MemberManager
     * @param memberDAO
     * @param roleDAO
     * @param em
     */
    public MemberManager(MemberDAO memberDAO, RoleDAO roleDAO, EntityManager em) {
        this.memberDAO = memberDAO;
        this.roleDAO = roleDAO;
        this.em = em;
    }

    /**
     *
     * @param p
     * @param oldRole
     * @return tous les Membres qui n'ont pas pour Role 4 (supprimé) d'un projet.
     */
    public List<Member> getAllActifMemberByProject(Project p, Role oldRole) {
        return memberDAO.getAllActifMemberByProject(p, oldRole);
    }

    /**
     *
     * @param idRole
     * @return le Role en fonction de son ID
     */
    public Role getRoleById(int idRole) {
        return roleDAO.getRoleById(idRole);
    }

    /**
     *
     * @param u
     * @param p
     * @return le Role d'un User dans un projet
     */
    public Role getRoleUserbyProject(User u, Project p) {
        return memberDAO.getRoleByIdUserandProject(u, p);
    }

    /**
     *
     * @param u
     * @return tous les project d'un utilisateur u
     */
    public List<Project> getProjectsByUser(User u) {
        Role r = getRoleById(4); //Si un utilisateur a été supprimé d'un projet, il a le rôle 4 dans ce ce projet
        return memberDAO.getProjects(u, r);
    }

    /**
     *
     * @param p
     * @return le nombre de chefs de projet du projet "p"
     */
    public int getNbchefByProject(Project p){
        Role r = getRoleById(1);
        return memberDAO.getNbChefByProject(p,r);
    }


    /**
     * Supprime des membres d'un project
     * @param m
     * @param news
     * @param file
     * @param project
     */
    public void deletteProject(List<Member> m,List<News> news,List<File> file,Project project){

        em.getTransaction().begin();
        for(int i=0;i<m.size();i++)  em.remove(m.get(i));
        for(int i=0;i<file.size();i++) em.remove(file.get(i));
        for(int i=0;i<news.size();i++) em.remove(news.get(i));

        em.remove(project);
        em.getTransaction().commit();
    }





}
