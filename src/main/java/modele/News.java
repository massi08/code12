package modele;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="NEWS")
@NamedQueries({
        @NamedQuery(name="News.getByIdProject", query="SELECT n FROM News n WHERE n.idProject =:idProject"),
})
public class News {


    @Id
    @Column(name="ID_NEWS")
    private int idNews;

    @Column(name="DATE")
    private Date dateTime;

    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    private Project idProject;

    @ManyToOne
    @JoinColumn(name = "ID_FILE", referencedColumnName = "ID_FILE")
    private File idFile;

    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName ="ID_USER")
    private User idUser;

    @ManyToOne
    @JoinColumn(name="ID_TICKET", referencedColumnName = "ID_TICKET")
    private Ticket idTicket;

    @Column(name="CONTENT")
    private String content;

    /**
     * Constructeur vide de News
     */
    public News(){

    }

    /**
     *
     * @return idNews
     */
    public int getIdNews() {
        return idNews;
    }

    /**
     *
     * @return dateTime
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * setter
     * @param idNews
     */
    public void setIdNews(int idNews) {
        this.idNews = idNews;
    }

    /**
     * setter
     * @param dateTime
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * setter
     * @param idProject
     */
    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    /**
     * setter
     * @param idFile
     */
    public void setIdFile(File idFile) {
        this.idFile = idFile;
    }

    /**
     * setter
     * @param idUser
     */
    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    /**
     * setter
     * @param idTicket
     */
    public void setIdTicket(Ticket idTicket) {
        this.idTicket = idTicket;
    }

    /**
     * setter
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return idProject
     */
    public Project getIdProject() {
        return idProject;
    }

    /**
     *
     * @return idFile
     */
    public File getIdFile() {
        return idFile;
    }

    /**
     *
     * @return idUser
     */
    public User getIdUser() {
        return idUser;
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
}