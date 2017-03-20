package modele;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by claragnx on 18/10/16.
 */

public class DiscussionID implements Serializable {

    @Id
    @Column(name="ID_DISCUSSION")
    @GenericGenerator(name="generator", strategy="metier.Utils.UseExistingOrGenerateIdGenerator")
    @GeneratedValue(generator="generator")
    public int idDiscussion;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    public User user;

    /**
     * Constructeur vide de DiscussionID
     */
    public DiscussionID() {

    }

}
