package ViewObjects.POJOview;

public class Message {
    private String name;
    private String text;
    private String date ;
    private String discussion;
    private String id ;
    private String type ;

    /**
     * Récupère le nom de l'auteur
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Initialise le nom de l'auteur
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupère le contenu du message
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Initialise le contenu du message
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Récupère la date
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Initialise la date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Récupère la discussion du message
     * @return
     */
    public String getDiscussion() {
        return discussion;
    }

    /**
     * Initialise la discussion du message
     * @param discussion
     */
    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    /**
     * Récupère l'id du message
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Initialise l'id du message
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Récupère le type du message
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Initialise le type du message
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
