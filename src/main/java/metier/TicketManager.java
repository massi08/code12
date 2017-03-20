package metier;

import dao.CommentaryDAO;
import dao.TicketDAO;
import dao.UserDAO;
import modele.Commentary;
import modele.Project;
import modele.Ticket;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by audrey on 15/11/16.
 */
public class TicketManager {


    private TicketDAO ticketDAO;
    private CommentaryDAO commentaryDAO;
    private UserDAO userDAO;
    private EntityManager em;


    /**
     * Constructeur de TicketManager
     * @param ticketDAO
     * @param commentaryDAO
     * @param userDAO
     * @param em
     */
    public TicketManager(TicketDAO ticketDAO, CommentaryDAO commentaryDAO, UserDAO userDAO, EntityManager em) {
        this.ticketDAO = ticketDAO;
        this.commentaryDAO = commentaryDAO;
        this.userDAO = userDAO;
        this.em = em;
    }

    /**
     * Crée un ticket dans la BD sans responsable
     * @param ProjectT
     * @param TitleT
     * @param ContentT
     * @param StartDateT
     * @param EtatT
     * @param PriorityT
     * @param AuthorT
     * @param type
     * @return
     */
    public Ticket createTicket(Project ProjectT, String TitleT, String ContentT,
                               Date StartDateT, String EtatT, String PriorityT, User AuthorT, String type)
    {
        em.getTransaction().begin();
        Ticket T = ticketDAO.create(ProjectT, TitleT, ContentT, StartDateT, EtatT, PriorityT, AuthorT, type);
        em.getTransaction().commit();
        return T;
    }

    /**
     * Crée un ticket dans la BD avec un responsable
     * @param ProjectT
     * @param TitleT
     * @param ContentT
     * @param StartDateT
     * @param EtatT
     * @param PriorityT
     * @param AuthorT
     * @param responsable
     * @param type
     * @return
     */
    public Ticket createTicket(Project ProjectT, String TitleT, String ContentT,
                               Date StartDateT, String EtatT, String PriorityT,
                               User AuthorT, User responsable, String type)
    {
        em.getTransaction().begin();
        Ticket T = ticketDAO.create(ProjectT, TitleT, ContentT, StartDateT, EtatT, PriorityT, AuthorT, responsable, type);
        em.getTransaction().commit();
        return T;
    }

    /**
     * Modifie l'etat du ticket t en "etat"
     * @param t
     * @param etat
     */
    public void setEtatTicket(Ticket t, String etat) {
        em.getTransaction().begin();
        ticketDAO.setEtat(t, etat);
        em.getTransaction().commit();
    }

    /**
     * Modifie la priorité du ticket t en "p"
     * @param t
     * @param p
     */
    public void setPriorityTicket(Ticket t, String p)
    {
        em.getTransaction().begin();
        ticketDAO.setPriority(t, p);
        em.getTransaction().commit();
    }

    /**
     * Modifie le type du ticket t en "p"
     * @param t
     * @param p
     */
    public void setTypeTicket(Ticket t, String p)
    {
        em.getTransaction().begin();
        ticketDAO.setType(t, p);
        em.getTransaction().commit();
    }

    /**
     * Modifie le supervisor du ticket t en "u"
     * @param t
     * @param u
     */
    public void setSupervisorTicket(Ticket t, User u)
    {
        em.getTransaction().begin();
        ticketDAO.setSupervisor(t, u);
        em.getTransaction().commit();
    }

    /**
     * Retourne le ticket qui a l'idTicket "idTicket"
     * @param idTicket
     * @return
     */
    public Ticket getTicketById(int idTicket)
    {
        return ticketDAO.getById(idTicket);
    }

    /**
     *
     * @return retourne tous les tickets
     */
    public List<Ticket> getAllTickets()
    {
        return ticketDAO.getAll();
    }

    /**
     *
     * @param idProject
     * @return retourne tous les tickets qui font partie du project "idProject"
     */
    public List<Ticket> getAllTicketsByProject(Project idProject)
    {
        return ticketDAO.getAllByProject(idProject);
    }

    /**
     *
     * @param author
     * @param idProject
     * @return retourne tous les tickets qui ont pour auteur l'user "author"
     */
    public List<Ticket> getAllByAuthor(User author, Project idProject)
    {
        return ticketDAO.getAllByAuthor(author, idProject);
    }

    /**
     *
     * @param supervisor
     * @param idProject
     * @return retourne tous les tickets qui ont pour supervisor l'user "supervisor"
     */
    //
    public List<Ticket> getAllBySupervisor(User supervisor, Project idProject)
    {
        return ticketDAO.getAllBySupervisor(supervisor, idProject);
    }

    /**
     * Crée un commentary dans la BD
     * @param Uc
     * @param t
     * @param ContentC
     * @param DateC
     * @return commentary créé dans la BD
     */
    public Commentary createCommentary(User Uc, Ticket t, String ContentC, Date DateC)
    {
        em.getTransaction().begin();
        Commentary C = commentaryDAO.create(Uc, t, ContentC, DateC);
        em.getTransaction().commit();
        return C;
    }

    /**
     *
     * @param idTicket
     * @return liste des commentaires du ticket "idTicket"
     */
    public List<Commentary> getAllCommentariesByTicket(Ticket idTicket)
    {
        return commentaryDAO.getAllByTicket(idTicket);
    }

    /**
     *
     * @param type
     * @param idProject
     * @return retourne tous les tickets qui ont pour type "type" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByType(String type, Project idProject)
    {
        return ticketDAO.getAllByType(type, idProject);
    }

    /**
     *
     * @param type
     * @param idProject
     * @return retourne tous les tickets qui n'ont pas pour type "type" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByNotType(String type, Project idProject)
    {
        return ticketDAO.getAllByNotType(type, idProject);
    }

    /**
     *
     * @param priority
     * @param idProject
     * @return retourne tous les tickets qui ont pour priority "priority" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByPriority(String priority, Project idProject)
    {
        return ticketDAO.getAllByPriority(priority, idProject);
    }

    /**
     *
     * @param priority
     * @param idProject
     * @return retourne tous les tickets qui n'ont pas pour priority "priority" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByNotPriority(String priority, Project idProject)
    {
        return ticketDAO.getAllByNotPriority(priority, idProject);
    }

    /**
     *
     * @param nameEtat
     * @param idProject
     * @return retourne tous les tickets qui ont pour état "nameEtat" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByEtat(String nameEtat, Project idProject)
    {
        return ticketDAO.getAllByEtat(nameEtat, idProject);
    }

    /**
     *
     * @param etat
     * @param idProject
     * @return tous les tickets qui ont pour état "etat" et qui sont dans le projet "idProject"
     */
    public List<Ticket> getAllTicketsByNotEtat(String etat, Project idProject)
    {
        return ticketDAO.getAllByNotEtat(etat, idProject);
    }

    /**
     * Retourne une liste de tickets selon les critères en paramètre
     * Les tickets avec un état "etat" si boolEtat=true, sinon les tickets avec un état!="etat"
     * Les tickets avec une priority "priority" si boolPriority=true, sinon les tickets avec un priority!="priority"
     * Les tickets avec un type "type" si boolType=true, sinon les tickets avec un type!="type"
     * Les tickets avec un supervisor ayant pour id "sup", si sup="aucun", alors les tickets n'ayant aucun superviseur
     * Les tickets avec un auteur ayant pour id "aucun"
     * Si etat/priority/type/sup/aut ce critère n'est pas utilisé pour la recherche
     * @param etat
     * @param boolEtat
     * @param priority
     * @param boolPriority
     * @param type
     * @param boolType
     * @param sup
     * @param aut
     * @param none
     * @param aucun
     * @param idProject
     * @return Retourne une liste de tickets selon les critères en paramètre
     */
    public List<Ticket> getAllTicketsProps(String etat, boolean boolEtat, String priority, boolean boolPriority,
                                           String type,boolean boolType, String sup, String aut, String none, String aucun,Project idProject)
    {
        User supervisor;
        if(sup.equals(aucun) || sup.equals(none)) supervisor=null;

        else supervisor=userDAO.getUserById(Integer.parseInt(sup));

        User author;
        if(aut.equals(none)) author=null;
        else author=userDAO.getUserById(Integer.parseInt(aut));


        List<Ticket> result;

        if(!etat.equals(none))
        {
            if(boolEtat) result = getAllTicketsByEtat(etat, idProject);
            else result = getAllTicketsByNotEtat(etat, idProject);
        }
        else if(!priority.equals(none))
        {
            if(boolPriority) result = getAllTicketsByPriority(priority, idProject);
            else result = getAllTicketsByNotPriority(priority, idProject);
        }
        else if(!type.equals(none))
        {
            if (boolType) result= getAllTicketsByType(type, idProject);
            else result = getAllTicketsByNotType(type, idProject);
        }
        else if(!sup.equals(none)) result=getAllBySupervisor(supervisor, idProject);
        else if(!aut.equals(none)) return getAllByAuthor(author, idProject);
        else return getAllTicketsByProject(idProject);

        if(!priority.equals(none))
        {
            if(boolPriority) result.retainAll(getAllTicketsByPriority(priority, idProject));
            else result.retainAll(getAllTicketsByNotPriority(priority, idProject));
        }
        if(!type.equals(none))
        {
            if (boolType) result.retainAll(getAllTicketsByType(type, idProject));
            else result.retainAll(getAllTicketsByNotType(type, idProject));
        }
        if(!sup.equals(none)) result.retainAll(getAllBySupervisor(supervisor, idProject));
        if(!aut.equals(none)) result.retainAll(getAllByAuthor(author, idProject));
        return result;

    }
}
