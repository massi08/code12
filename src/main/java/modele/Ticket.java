package modele;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by audrey on 17/10/16.
 */


@Entity
@NamedQueries({
        @NamedQuery(name="Ticket.getById", query="SELECT c FROM Ticket c WHERE idTicket = :idTicket"),
        @NamedQuery(name="Ticket.getAll", query="SELECT c FROM Ticket c"),
        @NamedQuery(name="Ticket.getAllByProject", query="SELECT c FROM Ticket c WHERE idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByAuthor", query="SELECT c FROM Ticket c WHERE author = :author AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllBySupervisor", query="SELECT c FROM Ticket c WHERE supervisor = :supervisor AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByNoSupervisor", query="SELECT c FROM Ticket c WHERE supervisor is NULL AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByType", query="SELECT c FROM Ticket c WHERE type = :type AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByPriority", query="SELECT c FROM Ticket c WHERE priority = :priority AND idProject = :idProject "),
        @NamedQuery(name="Ticket.getAllByEtat", query="SELECT c FROM Ticket c WHERE nameEtat = :nameEtat AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByNotEtat", query="SELECT c FROM Ticket c WHERE nameEtat != :nameEtat AND idProject = :idProject "),
        @NamedQuery(name="Ticket.getAllByNotPriority", query="SELECT c FROM Ticket c WHERE priority != :priority AND idProject = :idProject"),
        @NamedQuery(name="Ticket.getAllByNotType", query="SELECT c FROM Ticket c WHERE type != :type AND idProject = :idProject"),

})
@Table(name = "TICKET")
public class Ticket {

    //  @EmbeddedId TicketID id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TICKET")
    private int idTicket;

    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    private Project idProject;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    public enum TYPE {Bug, Feature, Tracker, Build};
    @Column(name = "TYPE")
    private TYPE type;

    public enum ETAT {NonTraite, EnCours, Ferme};
    @Column(name = "ETAT")
    private ETAT nameEtat;

    public enum PRIORITE {Mineure, Majeure, Bloquante};
    @Column(name = "PRIORITY")
    private PRIORITE priority;

    @ManyToOne
    @JoinColumn(name = "ID_AUTHOR", referencedColumnName = "ID_USER")
    private User author;


    @ManyToOne
    @JoinColumn(name = "ID_SUPERVISOR", referencedColumnName = "ID_USER")
    private User supervisor;

    /**
     * Constructeur vide de Ticket
     */
    public Ticket() {

    }

    /**
     *
     * @param idProjectT
     * @param TitleT
     * @param ContentT
     * @param StartDateT
     * @param EtatT
     * @param PriorityT
     * @param AuthorT
     * @param type
     */
    public Ticket(Project idProjectT, String TitleT, String ContentT,
                  Date StartDateT, String EtatT, String PriorityT, User AuthorT, String type) {
        this.idProject = idProjectT;
        this.title = TitleT;
        this.content = ContentT;
        this.startDate = StartDateT;
        this.endDate = null;
        this.nameEtat = ETAT.valueOf(EtatT);
        this.priority = PRIORITE.valueOf(PriorityT);
        this.author = AuthorT;
        this.supervisor = null;
        this.type = TYPE.valueOf(type);
    }

    /**
     * Constructeur de Ticket
     * @param idProject
     * @param title
     * @param content
     * @param startDate
     * @param nameEtat
     * @param priority
     * @param author
     * @param supervisor
     * @param type
     */
    public Ticket(Project idProject, String title, String content,
                  Date startDate, String nameEtat, String priority,
                  User author, User supervisor, String type) {
        this.idProject = idProject;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.nameEtat = ETAT.valueOf(nameEtat);
        this.priority = PRIORITE.valueOf(priority);
        this.author = author;
        this.supervisor = supervisor;
        this.type = TYPE.valueOf(type);
    }

    /**
     * setter
     * @param title
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * setter
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * setter
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * setter
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * setter
     * @param nameEtat
     */
    public void setNameEtat(String nameEtat) {
        this.nameEtat = ETAT.valueOf(nameEtat);
    }

    /**
     * setter
     * @param p
     */
    public void setPriority(String p) {
        this.priority = PRIORITE.valueOf(p);
    }

    /**
     * setter
     * @param p
     */
    public void setType(String p) {
        this.type = TYPE.valueOf(p);
    }

    /**
     * setter
     * @param author
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * setter
     * @param supervisor
     */
    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    /**
     *
     * @return idTicket
     */
    public int getNumber() {
        return idTicket;
    }

    /**
     *
     * @return title
     */

    public String getTitle() {
        return title;
    }

    /**
     *
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @return startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     *
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     *
     * @return nameEtat
     */
    public String getNameEtat() {
        return nameEtat.toString();
    }

    /**
     *
     * @return priority
     */
    public String getPriority() {
        return priority.toString();
    }

    /**
     *
     * @return type
     */
    public String getType() {
        return type.toString();
    }

    /**
     *
     * @return author
     */
    public User getAuthor() {
        return author;
    }

    /**
     *
     * @return supervisor
     */
    public User getSupervisor() {
        return supervisor;
    }



    /**
     *
     * @return idProject
     */
    public Project getIdProject() {
        return idProject;
    }
}
