package modele;

import org.w3c.dom.Comment;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by claragnx on 18/10/16.
 */

@Entity
@Table(name = "COMMENTARY")
@NamedQueries({
        @NamedQuery(name = "Commentary.getByTicket", query = "SELECT c FROM Commentary c WHERE idTicket = :idTicket"),
})
public class Commentary {

    @Id
    @Column(name = "ID_COMMENTARY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCommentary;

    @ManyToOne
    @JoinColumn(name = "ID_AUTHOR", referencedColumnName = "ID_USER")
    private User idAuthor;

    @ManyToOne
    @JoinColumn(name = "ID_TICKET", referencedColumnName = "ID_TICKET")
    private Ticket idTicket;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DATE")
    private Date date;

    /**
     * constructeur vide de Commentary
     */
    public Commentary() {

    }

    /**
     * Constructeur de Commentary
     * @param UserC
     * @param TicketC
     * @param ContentC
     * @param DateC
     */
    public Commentary(User UserC, Ticket TicketC, String ContentC, Date DateC) {
        this.idAuthor = UserC;
        this.idTicket = TicketC;
        this.content = ContentC;
        this.date= DateC;
    }

    /**
     *
     * @return idCommentary
     */
    public int getIdCommentary() {
        return idCommentary;
    }

    /**
     *
     * @return idAuthor
     */
    public User getIdAuthor() {
        return idAuthor;
    }

    /**
     *
     * @return idTicket
     */
    public Ticket getIdTicket() {
        return idTicket;
    }

    /**
     *
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * Modifie le contenu du commentaire
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return date
     */

    public Date getDate() {
        return date;
    }
}
