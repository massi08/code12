package metier;

import ViewObjects.Jsons.JsonFormat.ChatFormatter;
import dao.DiscussionDAO;
import dao.MessageDAO;
import ViewObjects.Jsons.JSONdata;
import ViewObjects.Discussiongroup;
import modele.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageManager {

    private DiscussionDAO discussionDAO;
    private MessageDAO messageDAO;
    private UserManager usermanager;
    private EntityManager em;

    /**
     * Constructeur de MessageManager
     * @param discussionDAO
     * @param messageDAO
     * @param usermanager
     * @param em
     */
    public MessageManager(DiscussionDAO discussionDAO, MessageDAO messageDAO,
                          UserManager usermanager, EntityManager em) {
        this.discussionDAO = discussionDAO;
        this.messageDAO = messageDAO;
        this.usermanager = usermanager;
        this.em = em;
    }

    /**
     * Créer un message dans la base de données
     * @param TextM
     * @param idDiscu
     * @param name
     * @return
     */
    public Message createMessage(String TextM, int idDiscu, String name) {
        Discussion discussion=discussionDAO.getDiscussionByIdUser(idDiscu, usermanager.getByPseudo(name));
        if(discussion == null)
            return null ;
        em.getTransaction().begin() ;
        Message message= messageDAO.create(TextM, new Date(), discussion);
        em.getTransaction().commit() ;
        return message;
    }

    /**
     * Récupère un message avec son id
     * @param idMessage
     * @return
     */
    public Message getMessageById(int idMessage){
        Message message=em.find(Message.class, idMessage);
        return message ;
    }

    /**
     * Modifie un message de la base de données
     * @param message
     * @param text
     * @return
     */
    public Message ModifyMessage(Message message, String text){
        if(message==null)
            return null ;
        em.getTransaction().begin();
        message.setText(text);
        message.setDate(new Date());
        em.getTransaction().commit() ;

        return message ;
    }

    /**
     * Supprime un message de la base de données
     * @param message
     * @return
     */
    public boolean DeleteMessage(Message message){
        boolean b=false ;
        em.getTransaction().begin();
        if(message != null) {
            em.remove(message);
            b=true ;
        }
        em.getTransaction().commit();
        return b ;
    }

    /**
     * Récupère un membre d'une discussion
     * @param idDiscussion
     * @param user
     * @return
     */
    public Discussion getOneDiscussion(int idDiscussion ,User user){
        return discussionDAO.getDiscussionByIdUser(idDiscussion, user);
    }

    /**
     * Créer une discussion à partir d'une utilisateur et d'un projet
     * @param UserD
     * @param ProjectD
     * @return
     */
    public Discussion createDiscussion(User UserD, Project ProjectD) {
        em.getTransaction().begin();
        Discussion D = discussionDAO.create(UserD, ProjectD);
        em.getTransaction().commit();
        return D;
    }

    /**
     * Ajoute un membre à une discussion
     * @param discussion
     * @param userD
     * @param projectD
     * @return
     */
    public Discussion addMemberInDiscussion(Discussion discussion, User userD, Project projectD)
    {
        em.getTransaction().begin();
        Discussion D = discussionDAO.addMember(discussion.getIdDiscussion(), userD, projectD);
        em.getTransaction().commit();
        return D;
    }

    /**
     * Supprime un membre de la discussion, renvoi null en cas d'échec.
     * @param discussion
     * @param userD
     */
    public void removeMemberInDiscussion(int discussion, User userD)
    {
        Discussion D = this.getOneDiscussion(discussion, userD);
        if(D != null ) {
            em.getTransaction().begin() ;
            D.setPresent(false);
            em.getTransaction().commit();
        }
    }

    /**
     * Renvoie le format json d'une liste de message avec l'id de la discussion
     * @param iddicussion
     * @return
     */
    public List<Message> getJSONdiscussion(int iddicussion){
        List<Message> listmess=messageDAO.getMessagesFromDiscussion(iddicussion);
        return listmess ;
    }

    /**
     * Renvoie les conversations de manière groupée
     * @param user
     * @param project
     * @return
     */
    public HashMap<Integer, Discussiongroup> getDiscussions(User user, Project project){
        List<Discussion> discussions=discussionDAO.getDiscussionWithUser(user);
        HashMap<Integer, Discussiongroup> discussiongroup=Discussiongroup.GenereDiscussionViewGroup(discussions);
        return discussiongroup;
    }

    /**
     * Renvoie tous les utilisateurs d'une discussion
     * @param discussion
     * @param user
     * @return
     */
    public List<User> getUsersOfDiscussion(Discussion discussion, User user){
        List<User> users=discussionDAO.getSpecificDiscussionUser(discussion);
        return users;
    }

    /**
     * On vérifie qu'il existe une discussion contenant les deux utilisateurs
     * @param user
     * @param user2
     * @return
     */
    public Integer AlreadyExistsDiscussion(User user, User user2){
        return discussionDAO.alreadyExistsDiscussion(user, user2);
    }

    /**
     * Tous les users du projet qui n'appartiennent pas au projet
     * @param idDiscussion
     * @param project
     * @return
     */
    public List<User> getUsersNotInDiscussion(int idDiscussion, Project project){
        List<User> users=discussionDAO.UserNotInDiscussionByProject(idDiscussion, project) ;
        return users;
    }
}
