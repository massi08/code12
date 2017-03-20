package dao;

import modele.Project;
import modele.Ticket;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class TicketDAO {


    private EntityManager em;

    /**
     * Constructeur de ticketDAO
     * @param em
     */
    public TicketDAO(EntityManager em) {
        this.em = em;
    }


    /**
     * Crée un ticket sans superviseur dans la BD
     * @param ProjectT
     * @param TitleT
     * @param ContentT
     * @param StartDateT
     * @param EtatT
     * @param PriorityT
     * @param AuthorT
     * @param type
     * @return le ticket créé
     */
    public Ticket create(Project ProjectT, String TitleT, String ContentT,
                         Date StartDateT, String EtatT, String PriorityT, User AuthorT, String type) {
        Ticket T = new Ticket(ProjectT, TitleT, ContentT, StartDateT, EtatT, PriorityT, AuthorT, type);
        em.persist(T);
        return T;
    }

    /**
     * Crée un ticket avec superviseur dans la BD
     * @param ProjectT
     * @param TitleT
     * @param ContentT
     * @param StartDateT
     * @param EtatT
     * @param PriorityT
     * @param AuthorT
     * @param supervisor
     * @param type
     * @return le ticket créé
     */
    public Ticket create(Project ProjectT, String TitleT, String ContentT,
                         Date StartDateT, String EtatT, String PriorityT,
                         User AuthorT, User supervisor, String type) {
        Ticket T = new Ticket(ProjectT, TitleT, ContentT, StartDateT,
                EtatT, PriorityT, AuthorT, supervisor, type);
        em.persist(T);
        return T;
    }

    /**
     * change l'état du ticket "t"
     * @param t
     * @param etat
     */
    public void setEtat(Ticket t, String etat) {
        t.setNameEtat(etat);
    }

    /**
     * change la priorité du ticket "t"
     * @param t
     * @param p
     */
    public void setPriority(Ticket t, String p) {
        t.setPriority(p);
    }

    /**
     * change le type du ticket "t"
     * @param t
     * @param p
     */
    public void setType(Ticket t, String p) {
        t.setType(p);
    }

    /**
     * change le superviseur du ticket "t"
     * @param t
     * @param u
     */
    public void setSupervisor(Ticket t, User u) {
        t.setSupervisor(u);
    }

    /**
     *
     * @param idTicket
     * @return le ticket qui a l'idTicket "idTicket"
     */
    public Ticket getById(int idTicket) {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getById", Ticket.class);
        query.setParameter("idTicket", idTicket);
        List<Ticket> results = query.getResultList();
        if (results.size() == 0) return null;
        return results.get(0);
    }

    /**
     *
     * @return tous les tickets
     */
    public List<Ticket> getAll() {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAll", Ticket.class);
        return query.getResultList();
    }

    /**
     *
     * @param idProject
     * @return tous les tickets qui font partie du project "idProject"
     */
    public List<Ticket> getAllByProject(Project idProject) {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByProject", Ticket.class);
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param author
     * @param idProject
     * @return tous les tickets qui ont pour auteur l'user "author", dans le projet "idProject"
     */
    public List<Ticket> getAllByAuthor(User author, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByAuthor", Ticket.class);
        query.setParameter("author",  author);
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param supervisor
     * @param idProject
     * @return tous les tickets qui ont pour supervisor l'user "supervisor", dans le projet "idProject"
     */
    public List<Ticket> getAllBySupervisor(User supervisor, Project idProject)
    {
        TypedQuery<Ticket> query;
        if (supervisor!=null)
        {
            query = em.createNamedQuery("Ticket.getAllBySupervisor", Ticket.class);
            query.setParameter("supervisor",  supervisor);
            query.setParameter("idProject", idProject);
        }
        else
        {
            query = em.createNamedQuery("Ticket.getAllByNoSupervisor", Ticket.class);
            query.setParameter("idProject", idProject);
        }
        return query.getResultList();
    }

    /**
     *
     * @param type
     * @param idProject
     * @return tous les tickets qui ont pour type "type", dans le projet "idProject"
     */
    public List<Ticket> getAllByType(String type, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByType", Ticket.class);
        query.setParameter("type",  Ticket.TYPE.valueOf(type));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param type
     * @param idProject
     * @return tous les tickets qui n'ont pas pour type "type", dans le projet "idProject"
     */
    public List<Ticket> getAllByNotType(String type, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByNotType", Ticket.class);
        query.setParameter("type",  Ticket.TYPE.valueOf(type));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param priority
     * @param idProject
     * @return tous les tickets qui ont pour priority "priority", dans le projet "idProject"
     */
    public List<Ticket> getAllByPriority(String priority, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByPriority", Ticket.class);
        query.setParameter("priority",  Ticket.PRIORITE.valueOf(priority));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param priority
     * @param idProject
     * @return tous les tickets qui n'ont pas pour priority "priority", dans le projet "idProject"
     */
    public List<Ticket> getAllByNotPriority(String priority, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByNotPriority", Ticket.class);
        query.setParameter("priority",  Ticket.PRIORITE.valueOf(priority));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param nameEtat
     * @param idProject
     * @return tous les tickets qui ont pour état "nameEtat", dans le projet "idProject"
     */
    public List<Ticket> getAllByEtat(String nameEtat, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByEtat", Ticket.class);
        query.setParameter("nameEtat",  Ticket.ETAT.valueOf(nameEtat));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }

    /**
     *
     * @param nameEtat
     * @param idProject
     * @return tous les tickets qui n'ont pas pour état "nameEtat", dans le projet "idProject"
     */
    public List<Ticket> getAllByNotEtat(String nameEtat, Project idProject)
    {
        TypedQuery<Ticket> query = em.createNamedQuery("Ticket.getAllByNotEtat", Ticket.class);
        query.setParameter("nameEtat",  Ticket.ETAT.valueOf(nameEtat));
        query.setParameter("idProject", idProject);
        return query.getResultList();
    }
}
