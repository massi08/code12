package Controller;

import metier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Created by claragnx on 17/11/16.
 */
@Controller
public class ControllerNews {

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;

    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager;

    @Autowired
    @Qualifier(value = "ticketmanager")
    private TicketManager ticketmanager;

    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;

    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;
    ;


}
