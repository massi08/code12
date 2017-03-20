package modele;

import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "PROJECT")
@NamedQueries({@NamedQuery(name = "Project.getAll", query = "SELECT p FROM Project p ")
})
public class Project implements Serializable{
    @Column(name = "ID_PROJECT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int idProject;

    @Column(name = "NAME")
    @NotEmpty(message = "Please enter your project name.")
    @Size(min = 4, max = 20, message = "Your project name must between 4 and 20 characters")
    @Pattern(regexp = "[a-zA-Z0-9_.]*", message="Your project name is invalid")
    private String name;

    @Column(name = "PATH")
    // @NotEmpty(message = "Please path your project name.")
    private String path;

    @ManyToOne
    @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE")
    @NotNull(message = "You must choose a language.")
    private Langage language;

    @Column(name = "GIT", columnDefinition = "BIT", length = 1)
    @NotNull(message = "you must make your choice for lodging.")
    private boolean git;

    /**
     * Constructeur vide de Project
     */
    public Project(){
        git=false ;
        language=new Langage("");
    }

    /**
     * Constructeur de Project
     * @param name
     * @param path
     * @param gitParam
     * @param languageParam
     */
    public Project(String name, String path, boolean gitParam, Langage languageParam) {
        this.name = name;
        this.path = path;
        git = gitParam;
        language = languageParam;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param nom
     */
    public void setName(String nom) {
        this.name = nom;
    }

    /**
     *
     * @return idProject
     */
    public int getIdProject() {
        return idProject;
    }

    /**
     * setter
     * @param id
     */
    public void setIdProject(int id) {
        this.idProject = id;
    }

    /**
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * setter
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return language
     */
    public Langage getLanguage() {
        return language;
    }

    /**
     * setter
     * @param l
     */
    public void setLanguage(Langage l) {
        this.language = l;
    }

    /**
     * setter
     * @param git
     */
    public void setGit(boolean git) {
        this.git = git;
    }

    /**
     *
     * @return git
     */
    public boolean getGit() {
        return git;
    }

    /**
     *
     * @return path d'où est enregistré le fichier
     */
    public String ConstructPath() {

        return "/" + this.getIdProject() + "/" + this.getName();

    }



}

