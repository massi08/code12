package modele;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JSONserialize;

import javax.persistence.*;


/**
 * Created by audrey on 17/10/16.
 */
@Entity
@Table(name = "FILE")
@NamedQueries({
        @NamedQuery(name = "File.getByName", query = "SELECT f FROM File f WHERE f.nameFile = :nameF"),
        @NamedQuery(name = "File.getByProject", query = "SELECT f FROM File f WHERE f.idProjet = :projectF"),
        @NamedQuery(name = "File.getByAttributes", query = " SELECT f FROM File f WHERE f.pathFile = :pathF and f.nameFile=:nameF and f.idProjet = :projectF "),
        @NamedQuery(name="File.getFileByUserandProject", query="SELECT f From File f WHERE f.isLocking = :idUser and f.idProjet = :idProject")
})
public class File implements JSONserialize{

    @Id
    @Column(name = "ID_FILE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFile;


    @OneToOne
    @JoinColumn(name = "ISLOCKING", referencedColumnName = "ID_USER")
    private User isLocking;


    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    private Project idProjet;

    @Column(name = "PATH")
    private String pathFile;

    @Column(name = "NAME")
    private String nameFile;

    /**
     * Constructeur vide de File
     */
      public File (){

      }

    /**
     * Constructeur de File
     * @param user
     * @param projectF
     * @param pathFileF
     * @param nameFileF
     */
    public File(User user, Project projectF, String pathFileF, String nameFileF) {

        this.isLocking = user;
        this.idProjet = projectF;
        this.pathFile = pathFileF;
        this.nameFile = nameFileF;
    }

    /**
     *
     * @return idFile
     */
    public int getIdFile() {
        return idFile;
    }

    /**
     * setter
     * @param idFile
     */
    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    /**
     * setter
     * @param isLocking
     */
    public void setIsLocking(User isLocking) {
        this.isLocking = isLocking;
    }

    /**
     * setter
     * @param idProjet
     */
    public void setIdProjet(Project idProjet) {
        this.idProjet = idProjet;
    }

    /**
     *
     * @return isLocking
     */
    public User isLocking() {
        return isLocking;
    }

    /**
     *
     * @return idProjet
     */
    public Project getIdProjet() {
        return idProjet;
    }

    /**
     *
     * @return pathFile
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     *
     * @return nameFile
     */
    public String getNameFile() {
        return nameFile;
    }

    /**
     * Modifie le lock
     * @param locking
     * @param modifylocking
     * @return
     */
    public boolean setLocking(User locking, User modifylocking) {
        if (this.boolocking(locking)) {
            isLocking = modifylocking;
            return true;
        }
        return false;
    }

    /**
     * setter
     * @param pathFile
     */
    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    /**
     * setter
     * @param nameFile
     */
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    /**
     *  construct path of the file from project folder
     * @return
     */
    public String ConstructPath() {
        return "/" + idProjet.getIdProject() + "/" + idProjet.getName() + pathFile + nameFile;
    }

    /**
     * Contruct path of the file from project name folder
     * @return
     */
    public String ConstructPathWithoutProjectid(){
        return idProjet.getName() + pathFile + nameFile;
    }

    /**
     *
     * @return JSON de File
     */
    public String getJSON() {
        String json = "{";
        json += JSONdata.addAttribute("idFile", this.idFile, false);
        json += JSONdata.addAttribute("path", this.pathFile, false);
        if(this.isLocking() != null)
            json += JSONdata.addAttribute("lock", this.isLocking().getPseudo(), false);
        json += JSONdata.addAttribute("name", this.getNameFile(), true);
        json += "}";
        return json;
    }

    /**
     *
     * @param text
     * @param rights
     * @return JSON de File
     */
    public String getJSONtext(String text, boolean rights) {
        String json = "{";
        json += JSONdata.addAttribute("idFile", this.idFile, false);
        json += JSONdata.addAttribute("path", this.pathFile, false);
        json += JSONdata.addAttribute("name", this.getNameFile(), false);
        json += JSONdata.addAttribute("text", text, false);
        json += JSONdata.addAttribute("lock", rights, true);
        json += "}";
        return json;
    }

    /**
     *
     * @param user
     * @return si le fichier est accessible true, false sinon
     */
    public boolean boolocking(User user) {
        if (this.isLocking() == null || user.getIdUser() == this.isLocking().getIdUser()) {
            return true; // fichier est accesible
        }
        return false;
    }



}
