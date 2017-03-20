package modele;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JSONserialize;

import javax.persistence.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by claragnx on 18/10/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Message.getFromDiscussion", query = "SELECT m FROM Message m, Discussion d WHERE " +
                "m.idDiscussion=d and " +
                "d.idDiscussion = :id " +
                "order by m.date"),
})
@Table(name = "MESSAGE")
public class Message implements JSONserialize {

    @Id
    @Column(name = "ID_MESSAGE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMessage;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ID_DISCUSSION", referencedColumnName = "ID_DISCUSSION"),
            @JoinColumn(name = "ID_AUTHOR", referencedColumnName = "ID_USER")

    })
    private Discussion idDiscussion;

    /**
     * Constructeur vide de Message
     */
    public Message() {

    }

    /**
     * Constructeur de Message
     * @param textM
     * @param dateM
     * @param discuM
     */
    public Message(String textM, Date dateM, Discussion discuM)
    {
        this.setText(textM);
        this.setDate(dateM);
        this.setIdDiscussion(discuM);
    }

    /**
     *
     * @return JSON de Message
     */
    public String getJSON() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr = formatter.format(getDate());
        String json="{";
        json+= JSONdata.addAttribute("Date", datestr, false);
        json+= JSONdata.addAttribute("Texte", this.getText(), false);
        json+= JSONdata.addAttribute("id", this.getIdMessage(), false);
        json+= JSONdata.addAttribute("Utilisateur", this.getIdDiscussion().getUser().getPseudo(), true);
        json+="}";
        return json ;
    }

    /**
     *
     * @return idDiscussion
     */
    public Discussion getIdDiscussion() {
        return idDiscussion;
    }

    /**
     * setter
     * @param idDiscussion
     */
    public void setIdDiscussion(Discussion idDiscussion) {
        this.idDiscussion = idDiscussion;
    }

    /**
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * setter
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * setter
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return idMessage
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * setter
     * @param idMessage
     */
    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }
}
