package dao;


import modele.Project;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import modele.*;

/**
 * Created by claragnx on 17/11/16.
 */
public class NewsDAO {


    private EntityManager em;

    /**
     * Constructeur de NewsDAO
     * @param em
     */
    public NewsDAO(EntityManager em)
    {
        this.em=em;
    }

    /**
     *
     * @param p
     * @return les news du projet "p"
     */
    public List<News> getNewsByIdProject(Project p){

        TypedQuery<News> query = em.createNamedQuery("News.getByIdProject", News.class);
        query.setParameter("idProject", p);
        return query.getResultList();

    }

    /**
     *
     * @param n
     * @return
     */
    public String getStringByNews(News n){
        String news = null;

        String content = n.getContent();
        Project p = n.getIdProject();


        int contentInt = Integer.parseInt(content);

        if(n.getIdUser() != null){

            if(n.getIdTicket() != null){
                //NEWS TICKET
                if(contentInt==1){
                    //ADD
                    news = "Le ticket " + n.getIdTicket().getTitle() + " a été crée par " + n.getIdUser().getPseudo() +".";
                }
                else{
                    return null;
                }
            }
            else
            {
                //NEWS MEMBER
                if(contentInt == 1){
                    //ADD
                    news = "Le membre " + n.getIdUser().getPseudo() + " a été ajouté au projet.";
                }else if (contentInt == 2){
                    //DELETE
                    news = "Le membre " + n.getIdUser().getPseudo() + " a été supprimé du projet.";
                }else if(contentInt ==3){
                    //CHANGE ROLE
                    news = "Le membre " + n.getIdUser().getPseudo() + " a changé de role.";
                }else{
                    System.out.println("BIZARRE2");
                    return null;
                }
            }

        }
        else{
            //NEWS FILES
            if(contentInt==1){
                //ADD
               news = "Le fichier " + n.getIdFile().getNameFile() + " a été ajouté au projet. ";
            }else if(contentInt==2){
                //DELETE
                news = "Le fichier " + n.getIdFile().getNameFile() + " a été supprimé du projet. ";
            }else{
                System.out.println("BIZARRE3");
                return null;
            }
        }

        return news;
    }

}