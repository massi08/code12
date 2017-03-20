package modele;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * MemberID class
 */
public class MemberID implements Serializable {

    /**
     * TODO
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    public User idUser;

    /**
     * TODO
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    public Project idProject;

    /**
     * Constructeur vide de Discussion
     */
    public MemberID() {
        // Empty
    }


}