package modele;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by audrey on 17/10/16.
 */
@Entity
@Table(name = "DISCUSSION")
@IdClass(DiscussionID.class)
@NamedQueries({
        @NamedQuery(name="Discussion.getByID", query="SELECT d FROM Discussion d WHERE " +
                "d.user=:user and " +
                "d.idDiscussion = :idDiscussion and " +
                "d.present=true"),
        @NamedQuery(name="Discussion.getByUserProject", query="SELECT d " +
                "FROM Discussion d, Discussion d2, User u " +
                "WHERE d.user=u and " +
                "d.present=true and " +
                "d2.present=true and " +
                "d2.user=:user and " +
                "u!=:user and " +
                "d.idDiscussion=d2.idDiscussion "
                ),
        @NamedQuery(name="Discussion.getByIDanduser", query="SELECT d.user FROM Discussion d WHERE " +
                "d.present=true and " +
                " d.idDiscussion = :idDiscussion"
        ),
        @NamedQuery(name="Discussion.alreadyExists", query = "SELECT d.idDiscussion FROM Discussion d3 , Discussion d, Discussion d2 WHERE " +
                " d.idDiscussion=d2.idDiscussion and " +
                " d.idDiscussion=d3.idDiscussion and" +
                " d3.user = :user and " +
                " d2.user = :user2 and " +
                " d3.present= true and " +
                " d2.present = true and" +
                " d.present=true " +
                " GROUP BY d.idDiscussion " +
                " HAVING count(distinct d.user)=2 "),
        @NamedQuery(name="Discussion.MemberNotInDiscussion",query ="SELECT u FROM User u, Member m WHERE " +
                " m.idUser=u and" +
                " m.idProject=:project and " +
                " NOT EXISTS( SELECT d.user FROM Discussion d WHERE " +
                    "d.user=u and " +
                    "d.idDiscussion=:idDiscussion " +
                    ")"
                )
})
public class Discussion {

    @Id
    @Column(name="ID_DISCUSSION")
    @GenericGenerator(name="generator", strategy="metier.Utils.UseExistingOrGenerateIdGenerator")
    @GeneratedValue(generator="generator")

    private int idDiscussion;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    private Project project;

    @Column(name = "PRESENT",columnDefinition = "BIT", length = 1)
    private boolean present;

    /**
     * Constructeur vide de Discussion
     */
    public Discussion()
    {
        this.present=true ;
    }

    /**
     * Constructeur de Discussion
     * @param UserD
     * @param ProjectD
     */
    public Discussion(User UserD, Project ProjectD)
    {
        this.user=UserD;
        this.project=ProjectD;
        this.present=true ;
    }

    /**
     *
     * @return idDiscussion
     */
    public int getIdDiscussion() {
        return idDiscussion;
    }

    /**
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @return project
     */
    public Project getProject() {
        return project;
    }

    /**
     * setter
     * @param idDiscussion
     */
    public void setIdDiscussion(int idDiscussion) {
        this.idDiscussion = idDiscussion;
    }

    /**
     * setter
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * setter
     * @param project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     *
     * @return present
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * setter
     * @param present
     */
    public void setPresent(boolean present) {
        this.present = present;
    }
}
