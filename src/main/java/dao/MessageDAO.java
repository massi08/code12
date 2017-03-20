package dao;

import modele.Discussion;
import modele.Message;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by claragnx on 19/10/16.
 */
public class MessageDAO {

    private EntityManager em;

    /**
     * Constructeur de MessageDAO
     * @param em
     */
    public MessageDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Crée un message dans la BD
     * @param TextM
     * @param DateM
     * @param DiscuM
     * @return le message créé
     */
    public Message create(String TextM, Date DateM, Discussion DiscuM) {
        Message M = new Message(TextM, DateM, DiscuM);
        em.persist(M);
        return M;
    }

    /**
     *
     * @param idDicusssion
     * @return tous les messages d'une discussion
     */
    public List<Message> getMessagesFromDiscussion(int idDicusssion) {
        List<Message> listmess = (List<Message>) em.createNamedQuery("Message.getFromDiscussion")
                .setParameter("id", idDicusssion)
                .getResultList();
        return listmess;
    }

}
