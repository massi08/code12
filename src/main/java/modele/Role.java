package modele;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by audrey on 17/10/16.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Role.getByName", query = "SELECT m FROM Role m WHERE m.name = :name"),
        @NamedQuery(name = "Role.getById", query = "SELECT m FROM Role m WHERE m.id = :id"),


})
@Table(name = "ROLE")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROLE")
    private int idRole;

    @Column(name = "NAME")
    private String name;
    /**
     * Constructeur vide de Role
     */
    public Role() {
    }

    /**
     * Constructeur de Role
     * @param name
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     *
     * @return name
     */
    public String getNom() {
        return name;
    }

    /**
     * setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return idRole
     */
    public int getIdRole() {
        return idRole;
    }
}
