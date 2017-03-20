package Controller;

        import ViewObjects.Jsons.JsonFormat.ChatFormatter;
        import ViewObjects.POJOview.DiscussionManage;
        import ViewObjects.POJOview.Message;
        import metier.MessageManager;
        import metier.FileManager;
        import metier.UserManager;
        import modele.Discussion;
        import modele.Project;
        import modele.User;
        import org.apache.log4j.Logger;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.beans.factory.annotation.Qualifier;
        import org.springframework.messaging.handler.annotation.DestinationVariable;
        import org.springframework.messaging.handler.annotation.MessageMapping;
        import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
        import org.springframework.messaging.simp.SimpMessagingTemplate;
        import org.springframework.messaging.simp.annotation.SubscribeMapping;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;
        import javax.servlet.http.HttpSession;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.List;
        import java.util.Map;

@Controller
public class ControllerChat {



    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    @Qualifier(value = "messagemanagerMessages")
    private MessageManager messagemanager;

    @Autowired
    @Qualifier(value = "usermanagerMessages")
    private UserManager usermanager;

    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private Logger logger = Logger.getLogger(ControllerChat.class);

    /**
     * Interception des messages envoyés à la socket pour le chat.
     * @param message
     * @param id
     * @throws Exception
     */
    @MessageMapping(value = "/message/{id}")
    public void sendmessage(
            SimpMessageHeaderAccessor headerAccessor,
            Message message,
            @DestinationVariable("id") String id
    ) {
        try {
            modele.Message modmess;
            if (message.getType().equals("creation")) { // Création de message
                modmess = messagemanager.createMessage(message.getText(), Integer.parseInt(id), headerAccessor.getUser().getName());
                message.setId(String.valueOf(modmess.getIdMessage()));
                message.setName(headerAccessor.getUser().getName());
                String strdate = df.format(modmess.getDate());
                message.setDate(strdate);
            } else if (message.getType().equals("suppression")) { // Suppression de message
                modmess = messagemanager.getMessageById(Integer.parseInt(message.getId()));
                if (!modmess.getIdDiscussion().getUser().getPseudo().equals(message.getName()) || !messagemanager.DeleteMessage(modmess))
                    return;
            } else { // modification de message
                modmess = messagemanager.getMessageById(Integer.parseInt(message.getId()));
                if (modmess == null || !modmess.getIdDiscussion().getUser().getPseudo().equals(message.getName()))
                    return;
                messagemanager.ModifyMessage(modmess, message.getText());
            }

            this.template.convertAndSend("/topic/" + id, message);
        }catch(Exception e){
            logger.error(e);
            return ;
        }
    }


    @SubscribeMapping("/topic/{id}")
    public String getChatInit(
            @DestinationVariable("id") Integer id
    ) {
        List<modele.Message> result=messagemanager.getJSONdiscussion(id);
        return ChatFormatter.JsonConversation(result, id);
    }

    /**
     * Echanges relatifs à l'ensemble d'un projet. Dans notre cas, cela sert à synchroniser les membres d'une conversation.
     * @param headerAccessor
     * @param message
     * @param id
     * @throws Exception
     */
    @MessageMapping(value="/message/project/{idProject}")
    public void sendProjectMessages(
            SimpMessageHeaderAccessor headerAccessor,
            DiscussionManage message,
            @DestinationVariable("idProject") String id
    ){
        try {
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            User user = usermanager.getByPseudo(headerAccessor.getUser().getName());
            User user2 = usermanager.getUserById(Integer.parseInt(message.getIdUser()));
            Project project = (Project) sessionAttributes.get("project");
            Discussion discussion;
            // La discussion existe
            if (message.getType().equals("remove")) { // On enlève un membre de la discussion
                discussion = messagemanager.getOneDiscussion(Integer.parseInt(message.getDiscussion()), user);
                messagemanager.removeMemberInDiscussion(Integer.parseInt(message.getDiscussion()), user);
            } else if (message.getType().equals("ajout")) { // Ajout d'un membre dans une discussion
                discussion = messagemanager.getOneDiscussion(Integer.parseInt(message.getDiscussion()), user);
                messagemanager.addMemberInDiscussion(discussion, user2, project);
            } else if (message.getType().equals("creation")) { // Création d'une discussion
                if (messagemanager.AlreadyExistsDiscussion(user, user2) != null)
                    return;
                discussion = messagemanager.createDiscussion(user, project);
                messagemanager.addMemberInDiscussion(discussion, user2, project);
                message.setDiscussion(Integer.toString(discussion.getIdDiscussion()));
            } else {
                return;
            }
            message.setContent(ChatFormatter.JsonUserList(messagemanager.getUsersOfDiscussion(discussion, user)));
            this.template.convertAndSend("/topic/project/" + id, message);
        }catch(Exception e){
            logger.error(e);
            return ;
        }
    }
}
