package ViewObjects.Jsons.JsonFormat;

import ViewObjects.Jsons.JSONdata;
import modele.Message;
import modele.User;

import java.util.List;

public class ChatFormatter {

    /**
     * Transforme une discussion et sa liste de message en json
     * @param messages
     * @param idDiscussion
     * @return
     */
    public static String JsonConversation(List<Message> messages, int idDiscussion){
        String json="{";
        json+= JSONdata.addAttribute("Messages", messages, false);
        json+=JSONdata.addAttribute("discussion", idDiscussion,true);
        json+="}";
        return json ;
    }

    /**
     * Transforme une liste d'users en json
     * @param users
     * @return
     */
    public static String JsonUserList(List<User> users){
        String jsonresult="{";
        jsonresult+=JSONdata.addAttribute("users", users, true);
        jsonresult+="}";
        return jsonresult ;
    }

}
