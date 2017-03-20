package dao;

import modele.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class ProjectDAO {

    private EntityManager em;

    /**
     * Constructeur de ProjectDAO
     * @param em
     */
    public ProjectDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Crée un projet dans la BD
     * @param name
     * @param path
     * @param git
     * @param langage
     * @return le projet créé
     */
    public Project create(String name, String path, boolean git, Langage langage) {
        Project p = new Project(name, path, git, langage);
        em.persist(p);
        em.merge(langage);

        return p;
    }

    /**
     *
     * @return tous les projets
     */
    public List<Project> getAllProjects() {
        TypedQuery<Project> query = em.createNamedQuery("Project.getAll", Project.class);
        return query.getResultList();
    }

    /**
     * change le nom du projet "p"
     * @param p
     * @param name
     */
    public void setProjectName(Project p, String name){
        p.setName(name);
    }


    /**
     *
     * @param id
     * @return les projets ayant l'id "id"
     */
    public Project getProjectById(int id) {
        return em.find(Project.class,id);
    }
}
