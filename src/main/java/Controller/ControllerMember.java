package Controller;

import ViewObjects.Jsons.JsonFormat.ChatFormatter;
import metier.MemberManager;
import metier.MessageManager;
import metier.ProjectManager;
import metier.UserManager;
import modele.Member;
import modele.Project;
import modele.Role;
import modele.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ControllerMember class
 */
@Controller
public class ControllerMember {

    @Autowired
    ApplicationContext ctx;
    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;
    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;
    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;
    @Autowired
    @Qualifier(value = "messagemanager")
    private MessageManager messagemanager;


    public static final String PROJECT_ATTRIBUTE = "project";
    public static final String PROJECT_REDIRECT = "redirect:/Project/manageProject";
    private Logger logger = Logger.getLogger(ControllerMember.class);

    /**
     * TODO CLARA
     *
     * @param req
     * @param map
     * @param session
     * @param error
     * @return Page de gestion des membres du projet
     */
    @RequestMapping(value = {"/Project/manageProject"}, method = {RequestMethod.GET})
    public String getManageUser(HttpServletRequest req,
                                ModelMap map,
                                HttpSession session,
                                @RequestParam(value = "error", required = false) String error
    ) {
        Project p = (Project) session.getAttribute(PROJECT_ATTRIBUTE);
        usermanager.GetUserSession();
        Role oldRole = membermanager.getRoleById(4);
        if (p != null) {
            if (error != null) {
                map.addAttribute("error", "Vous ne pouvez pas modifier le chef de projet : il doit toujours en rester un.");
            }
            List<Member> members = membermanager.getAllActifMemberByProject(p, oldRole);
            List<User> users = usermanager.getAllUsers();

            for (Member m : members) {
                User u = m.getIdUser();
                Iterator<User> it = users.iterator();
                while (it.hasNext()) {
                    if (it.next() == u) {
                        it.remove();
                    }
                }
            }
            map.addAttribute("Members", members);
            map.addAttribute("Users", users);
        }
        return "manage_users";
    }

    /**
     * Modifie le role d'un membre du projet dans la session
     *
     * @param req
     * @param session
     * @param newRoleId
     * @param idUser
     * @return Page de gestion des membres du projet
     */
    @RequestMapping(value = "/Project/ModifyRole", method = RequestMethod.POST)
    public String modifyRole(HttpServletRequest req,
                             HttpSession session,
                             @RequestParam(value = "radio_modify") String newRoleId,
                             @RequestParam(value = "idMember") String idUser
    ) {

        Project p = (Project) session.getAttribute(PROJECT_ATTRIBUTE);
        if (p != null) {
            Role chef = membermanager.getRoleById(1);
            User userChange = usermanager.getUserById(Integer.parseInt(idUser));
            Role newRole = membermanager.getRoleById(Integer.parseInt(newRoleId));
            Role oldRole = membermanager.getRoleUserbyProject(userChange, p);
            if (newRole != oldRole && oldRole == chef) {
                int nbChef = membermanager.getNbchefByProject(p);
                if (nbChef == 1) {
                    return "redirect:/Project/manageProject?error=1";
                } else {
                    projectmanager.changeRole(userChange, newRole, p);
                }
            } else {
                projectmanager.changeRole(userChange, newRole, p);
            }
            return PROJECT_REDIRECT;
        }
        return PROJECT_REDIRECT;
    }

    /**
     * TODO CLARA
     *
     * @param req
     * @param session
     * @param idUser
     * @return Page de gestion des membres du projet
     */
    @RequestMapping(value = "/Project/GetOldMember", method = RequestMethod.POST)
    public String getOldMember(HttpServletRequest req,
                               HttpSession session,
                               @RequestParam(value = "idMember") String idUser

    ) {
        Project p = (Project) session.getAttribute(PROJECT_ATTRIBUTE);

        int idUserInt = Integer.parseInt(idUser);
        User user = usermanager.getUserById(idUserInt);
        Role oldRole = membermanager.getRoleUserbyProject(user, p);
        Role role = membermanager.getRoleById(4);
        Role chef = membermanager.getRoleById(1);
        if (oldRole == chef) {
            int nbChef = membermanager.getNbchefByProject(p);
            if (nbChef <= 1) {
                return "redirect:/Project/manageProject?error=1";
            }
        }
        projectmanager.changeRole(user, role, p);
        return PROJECT_REDIRECT;
    }

    /**
     * Ajoute un membre au projet dans la session
     *
     * @param req
     * @param session
     * @param role
     * @return Page de gestion des membres du projet
     */
    @RequestMapping(value = "/Project/AddMember", method = RequestMethod.POST)
    public String addMember(HttpServletRequest req,
                            HttpSession session,
                            @RequestParam(value = "radio_function") String role
    ) {
        Project p = (Project) session.getAttribute(PROJECT_ATTRIBUTE);

        List<Integer> listUserAdd = new ArrayList<>();
        int idRole = Integer.parseInt(role);
        
        /* Creation liste de tous les utilisateurs - ceux deja dans le projet */
        List<Member> members = projectmanager.getAllMembersByProject(p);
        List<User> listUsers = usermanager.getAllUsers();

        for (Member m : members) {
            User u = m.getIdUser();
            Iterator<User> it = listUsers.iterator();
            while (it.hasNext()) {
                if (it.next() == u && m.getRole().getIdRole() != 4) {
                    it.remove();
                }
            }
        }

        // Fin de la création de la liste*/
        // Recuperation value des check Box pour chaque utilisateur
        for (int j = 0; j < listUsers.size(); j++) {
            User user = listUsers.get(j);
            String idUserString = Integer.toString(user.getIdUser());
            String userCheckBox = req.getParameter(idUserString);
                /* Si la checkBox est coché, on l'ajoute l'ID dans ListUserAdd */
            if (userCheckBox != null) {
                listUserAdd.add(user.getIdUser());
            }
        }

        // Pour chaque User dans ListUserAdd, on l'ajoute en tant que membre avec le Role
        for (int i = 0; i < listUserAdd.size(); i++) {
            User u = usermanager.getUserById(listUserAdd.get(i));
            Role r = membermanager.getRoleById(idRole);
            Member m = projectmanager.getMember(u, p);
            if (m == null) {
                projectmanager.createMember(u, r, p);
            } else {
                projectmanager.changeRole(u, r, p);
            }
        }
        return PROJECT_REDIRECT;
    }


    /**
     * Récupère les membres d'un projet qui ne sont pas membres d'une discussion.
     * @param idDiscussion
     * @param session
     * @return TODO CLARA
     */
    @RequestMapping(value="/Project/ajax/DiscussionNotMembers", method=RequestMethod.GET)
    public ResponseEntity DiscussionNotMembers(
            @RequestParam(value ="discussion") int idDiscussion,
            HttpSession session
    )
    {
        try {
            Project project = (Project) session.getAttribute("project");
            String res = ChatFormatter.JsonUserList((messagemanager.getUsersNotInDiscussion(idDiscussion, project)));
            return new ResponseEntity<String>(res, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossible de récupérer les membres du projet.");
        }
    }


}