
package modele;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQueries({
        @NamedQuery(name = "Langage.getByName", query = "SELECT m FROM Langage m WHERE name = :name"),
        @NamedQuery(name = "Langage.getAll", query = "SELECT m FROM Langage m"),
})
@Table(name = "LANGUAGE")
public class Langage implements Serializable {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LANGUAGE")
    @Id
    private int idLanguage;

    private String name;

    /**
     * Constructeur vide de Langage
     */
    public Langage() {

    }

    /**
     * Contructeur par copie de Langage
     * @param l
     */
    public Langage(Langage l) {

        this.idLanguage = l.getId();
        this.name = l.getName();
    }

    /**
     * setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter
     * @param idLanguage
     */
    public void setIdLanguage(int idLanguage) {

        this.idLanguage = idLanguage;
    }

    /**
     *
     * @return idLanguage
     */
    public int getIdLanguage() {

        return idLanguage;
    }

    /**
     * setter
     * @param name
     */
    public Langage(String name) {
        this.name = name;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return idLanguage
     */
    public int getId() {
        return idLanguage;
    }


}
