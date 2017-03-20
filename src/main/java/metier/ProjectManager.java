package metier;

import dao.LangageDAO;
import dao.MemberDAO;
import dao.ProjectDAO;
import dao.RoleDAO;
import modele.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

/**
 * Created by audrey on 15/11/16.
 */
public class ProjectManager {

    private ProjectDAO projectDAO;

    private LangageDAO langageDAO;

    private MemberDAO memberDAO;

    private RoleDAO roleDAO;

    private EntityManager em;


    /**
     * Constructeur de ProjectManager
     *
     * @param projectDAO
     * @param langageDAO
     * @param memberDAO
     * @param roleDAO
     * @param em
     */
    public ProjectManager(ProjectDAO projectDAO, LangageDAO langageDAO, MemberDAO memberDAO, RoleDAO roleDAO, EntityManager em) {
        this.projectDAO = projectDAO;
        this.langageDAO = langageDAO;
        this.memberDAO = memberDAO;
        this.roleDAO = roleDAO;
        this.em = em;
    }


    /**
     * Crée un projet dans la BD
     *
     * @param name
     * @param path
     * @param git
     * @param langage
     * @param id_master
     * @return le project créé dans la BD
     */
    public Project createProject(String name, String path, boolean git, Langage langage, User id_master) {
        em.getTransaction().begin();
        Project p = projectDAO.create(name, path, git, langage);
        Role r = roleDAO.getByName("CHEF");
        Member m = memberDAO.create(id_master, r, p);
        em.getTransaction().commit();
        return p;
    }

    /**
     * Crée un projet en local
     *
     * @param project
     * @param path
     * @param projectName
     * @throws IOException
     */
    public void createLocalProject(Project project, String path, String projectName) throws IOException {
        String strprojectid = Integer.toString(project.getIdProject());
        java.io.File repertoire = new java.io.File(path + "/" + strprojectid + "/" + projectName);

        if (!repertoire.exists()) {
            if (!repertoire.mkdirs())
                throw new IOException();
        }

    }

    /**
     * Modifie le nom d'un projet local
     *
     * @param p
     * @param path
     * @param oldNameProject
     * @param newNameProject
     * @return
     * @throws IOException
     */
    public boolean modifyNameLocalProject(Project p, String path, String oldNameProject, String newNameProject) throws IOException {
        String strProjectid = Integer.toString(p.getIdProject());
        java.io.File rep = new java.io.File(path + "/" + strProjectid + "/" + oldNameProject);
        boolean success = rep.renameTo(new java.io.File(path + "/" + strProjectid + "/" + newNameProject));
        return success;

    }


    /**
     * Crée un member dans la table member avec son project et son role
     *
     * @param user
     * @param role
     * @param project
     * @return le member créé
     */
    public Member createMember(User user, Role role, Project project) {
        em.getTransaction().begin();
        Project p = em.merge(project);
        Member D = memberDAO.create(user, role, p);
        em.getTransaction().commit();
        return D;
    }

    /**
     * @param u
     * @param p
     * @return le membre correspondant à l'utilisateur u dans le project p
     */
    public Member getMember(User u, Project p) {
        return memberDAO.getMember(u, p);
    }

    /**
     * L'user user prend comme role "role" dans le project
     *
     * @param user
     * @param role
     * @param project
     */
    public void changeRole(User user, Role role, Project project) {
        Member m = getMember(user, project);
        if (m != null) {
            em.getTransaction().begin();
            m.setRole(role);
            em.getTransaction().commit();
        }

    }

    /**
     * Modifie le nom du projet p
     *
     * @param p
     * @param name
     */
    public void modifyNameProject(Project p, String name) {
        em.getTransaction().begin();
        projectDAO.setProjectName(p, name);
        em.getTransaction().commit();
    }

    /**
     * @param name
     * @return le role ayant pour nom name, null s''il n'existe pas dans la BD
     */
    public Role getRoleByName(String name) {
        return roleDAO.getByName(name);
    }


    /**
     * @param name
     * @return le langage dans le name est name, null si il n'existe pas dans la BD
     */
    public Langage getLangageByName(String name) {
        return langageDAO.getByName(name);
    }

    /**
     * @return tous les langages dans la BD
     */
    public List<Langage> getAllLangages() {
        return langageDAO.getAll();
    }

    /**
     * @param p
     * @return tous les membres du projet p
     */
    public List<Member> getAllMembersByProject(Project p) {
        return memberDAO.getAllMembersByProject(p);
    }

    /**
     * @param idproject
     * @return le projet dont l'id est "idproject"
     */
    public Project getProject(int idproject) {

        em.getTransaction().begin();
        Project proj = em.find(Project.class, idproject);
        em.getTransaction().commit();
        return proj;

    }

    /**
     * @return tous les projets dans la BD
     */
    public List<Project> getAllProjects() {
        return projectDAO.getAllProjects();
    }

    /**
     * @param id
     * @return le projet dont l'id est "id"
     */
    public Project getProjectById(int id) {
        return projectDAO.getProjectById(id);
    }


    /**
     * Supprime le projet p de la hierarchie
     *
     * @param path
     * @param p
     * @return
     */
    public boolean deleteLocal(String path, Project p) {

        java.io.File repertoire = new java.io.File(path + p.ConstructPath());

        return repertoire.delete();
    }


}
