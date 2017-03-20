package dao;

import modele.File;
import modele.Project;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class FileDAO {


    private EntityManager em;

    /**
     * Constructeur de FileDAO
     * @param em
     */
    public FileDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Crée un file dans la BD
     * @param user
     * @param projectF
     * @param pathFileF
     * @param nameFileF
     * @return le file créé
     */
    public File create(User user, Project projectF, String pathFileF, String nameFileF) {
        File f = new File(user, projectF, pathFileF, nameFileF);
        em.persist(f);
        return f;
    }


    /**
     *
     * @param id
     * @return le file dont l'id est "id"
     */
    public File getbyId(int id) {
        return em.find(File.class, id);
    }

    /**
     *
     * @param p
     * @return la liste des fichiers du projet "p"
     */
    public List<File> getFileByProject(Project p) {
        Query query = em.createNamedQuery("File.getByProject", File.class).setParameter("projectF", p);
        List<File> listFile = query.getResultList();
        return listFile;
    }

    /**
     *
     * @param project
     * @param path
     * @param name
     * @return
     */
    public File getFileByAttributes(Project project, String path, String name) {
        em.getTransaction().begin();
        Query query = em.createNamedQuery("File.getByAttributes", File.class)
                .setParameter("projectF", project)
                .setParameter("pathF", path)
                .setParameter("nameF", name);
        List<File> listFile = (List<File>) query.getResultList();
        em.getTransaction().commit();
        if (listFile.isEmpty())
            return null;
        return listFile.get(0);
    }

    /**
     *
     * @param u
     * @param p
     * @return
     */
    public List<File> getFileByUserandProject(User u, Project p){
        TypedQuery<File> query=em.createNamedQuery("File.getFileByUserandProject", File.class);
        query.setParameter("idUser", u);
        query.setParameter("idProject", p);
        return query.getResultList();
    }

}
