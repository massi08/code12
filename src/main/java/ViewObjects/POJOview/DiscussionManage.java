package ViewObjects.POJOview;

public class DiscussionManage {
    private String idUser ;
    private String type ;
    private String content ;
    private String discussion ;

    /**
     * Id de l'user
     * @return
     */
    public String getIdUser() {
        return idUser;
    }

    /**
     * Initialise id User
     * @param idUser
     */
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    /**
     * Retourne le type de message, création, ajout ou suppression
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Initialise le type de message
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retourne le contenu du message
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Intialise le contenu du message
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retourne l'id de la discussion du message
     * @return
     */
    public String getDiscussion() {
        return discussion;
    }

    /**
     * Initialise l'id de la discussion du message
     * @param discussion
     */
    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }
}
