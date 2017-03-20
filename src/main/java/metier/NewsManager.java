package metier;

import dao.*;
import modele.News;
import modele.Project;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by claragnx on 17/11/16.
 */
public class NewsManager {

    @Autowired
    NewsDAO newsDAO;

    EntityManager em;

    /**
     * Constructeur de NewsManager
     * @param em
     */
    public NewsManager(EntityManager em){
        this.em=em;
    }

    /**
     *
     * @param p
     * @return la liste de toutes les news en rapport avec un projet p
     */
    public List<News> getAllNewsByProject (Project p){return newsDAO.getNewsByIdProject(p);}

    /**
     *
     * @param n
     * @return la phrase d'affichage des news en fonction du type de la news (Ajout d'un membre, Changement de role d'un membre,Suppression d'un membre, Ajout d'un ticket, Ajout d'un file)
     */
    public String getStringByNews(News n){return newsDAO.getStringByNews(n);}
}

