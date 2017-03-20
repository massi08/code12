package dao;

import modele.Commentary;
import modele.Project;
import modele.Ticket;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by claragnx on 18/10/16.
 */
public class CommentaryDAO {


    private EntityManager em;

    /**
     * Constructeur de CommentaryDAO
     * @param em
     */
    public CommentaryDAO(EntityManager em) {
        this.em = em;
    }

    public Commentary create(User UserC, Ticket TicketC, String ContenuC, Date DateC) {
        Commentary C = new Commentary(UserC, TicketC, ContenuC, DateC);
        em.persist(C);
        return C;
    }

    /**
     *
     * @param idTicket
     * @return tous les commentaires du ticket idTicket
     */
    public List<Commentary> getAllByTicket(Ticket idTicket) {
        TypedQuery<Commentary> query = em.createNamedQuery("Commentary.getByTicket", Commentary.class);
        query.setParameter("idTicket", idTicket);
        return query.getResultList();
    }

}
