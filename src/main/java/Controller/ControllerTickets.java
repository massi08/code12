package Controller;


import metier.*;
import modele.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by audrey on 21/10/16.
 */

@Controller
public class ControllerTickets {

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager ;

    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager ;

    @Autowired
    @Qualifier(value = "ticketmanager")
    private TicketManager ticketmanager ;

    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;

    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;

    /**
     *
     * @param req
     * @param map
     * @return Page générale des tickets
     */
    @RequestMapping(value={"/Project/ManageTickets"}, method= { RequestMethod.GET})
    public String ManageTickets(
            HttpServletRequest req, ModelMap map
    ) {
        User user = usermanager.GetUserSession();
        Project p= (Project) req.getSession().getAttribute("project");
        //Les tickets dont l'utilisateur en cours est l'auteur
        List<Ticket> myTickets= ticketmanager.getAllTicketsByProject(p);
        map.addAttribute("myTickets", myTickets);

        //Les membres du projets pour la recherche
        List<Member> members=projectmanager.getAllMembersByProject(p);
        map.addAttribute("members",members);
        return "manage_tickets";
    }


    /**
     *
     * @param req
     * @param id
     * @param map
     * @return Page du ticket en paramètre
     */
    @RequestMapping(value={"/Project/ManageTicket"}, method= { RequestMethod.GET})
    public String ManageTicket(
            HttpServletRequest req,
            @RequestParam(value = "ticket", required = true) Integer id,
            ModelMap map
    ) {
        Ticket ticket=ticketmanager.getTicketById(id);
        map.addAttribute("ticket", ticket);

        Project p= (Project) req.getSession().getAttribute("project");

        //Membres du projet
        Role r = membermanager.getRoleById(4);
        List<Member> members=membermanager.getAllActifMemberByProject(p, r);
        map.addAttribute("members",members);

        List<Commentary> commentaries= ticketmanager.getAllCommentariesByTicket(ticket);
        map.addAttribute("commentaries",commentaries);
        return "manage_ticket";

    }

    /**
     *
     * @param req
     * @param map
     * @return Page de création d'un ticket
     */
    @RequestMapping(value={"/Project/CreateTicket"}, method= { RequestMethod.GET})
    public String createTicket(
            HttpServletRequest req, ModelMap map
    ) {
        User user = usermanager.GetUserSession();

        //Projet de la session
        // TODO mettre un filtre pour gérer l'erreur si il n'y a pas de variable project dans la session
        Project p= (Project) req.getSession().getAttribute("project");

        List<Member> members=projectmanager.getAllMembersByProject(p);
        map.addAttribute("members",members);
        return "create_ticket";
    }

    /**
     * Modifie le ticket avec les informations en paramètres
     * @param req
     * @param priority
     * @param etat
     * @param type
     * @param supervisor
     * @param id
     * @return Page générale des tickets
     */
    @RequestMapping(value="/Project/ManageTicket-result", method= RequestMethod.POST)
    public String ManageTicket(
            HttpServletRequest req,
            @RequestParam(value="choose_priority") String priority,
            @RequestParam(value="choose_statut") String etat ,
            @RequestParam(value="choose_type") String type ,
            @RequestParam(value="choose_supervisor") String supervisor,
            @RequestParam(value = "ticket", required = true) Integer id
    ) {
        Ticket ticket=ticketmanager.getTicketById(id);
        ticketmanager.setPriorityTicket(ticket,priority);
        ticketmanager.setEtatTicket(ticket,etat);
        ticketmanager.setTypeTicket(ticket,type);
        if(supervisor.equals("Nobody")) ticketmanager.setSupervisorTicket(ticket,null);
        else ticketmanager.setSupervisorTicket(ticket,usermanager.getUserById(Integer.parseInt(supervisor)));
        return "redirect:/Project/ManageTickets";
    }

    /**
     * Crée un commentaire dans le ticket en paramètre
     * @param req
     * @param content
     * @param id
     * @return Page du ticket en paramètre
     */
    @RequestMapping(value="/Project/ManageTicket-commentaire", method= RequestMethod.POST)
    public String ManageTicketCommentaire(
            HttpServletRequest req,
            @RequestParam(value="commentaire") String content,
            @RequestParam(value = "ticket", required = true) Integer id
    ) {
        Ticket ticket=ticketmanager.getTicketById(id);
        User user = usermanager.GetUserSession();
        Date date= new Date();
        ticketmanager.createCommentary(user,ticket,content, date);

        return "redirect:/Project/ManageTicket?ticket="+id.toString();
    }

    /**
     * Crée un ticket en fonction du formulaire fournit
     * @param req
     * @param title
     * @param priority
     * @param etat
     * @param type
     * @param start_date
     * @param content
     * @param supervisor
     * @return Page générale des tickets
     */
    @RequestMapping(value="/Project/CreateTicket-result", method= RequestMethod.POST)
    public String CreateTicket(
            HttpServletRequest req,
            @RequestParam(value="title") String title,
            @RequestParam(value="choose_priority") String priority,
            @RequestParam(value="choose_statut") String etat ,
            @RequestParam(value="choose_type") String type ,
            @RequestParam(value="date") Date start_date,
            @RequestParam(value="description") String content,
            @RequestParam(value="choose_supervisor") String supervisor
    ) {
        User a = usermanager.GetUserSession();
        Project p= (Project) req.getSession().getAttribute("project");
        //Le ticket n'a pas de supervisor
        if(supervisor.equals("Nobody"))
            ticketmanager.createTicket(p,title,content,start_date, etat, priority, a, type);
        else //Le ticket a un supervisor
        {
            User sup=usermanager.getUserById(Integer.parseInt(supervisor));
            ticketmanager.createTicket(p,title,content,start_date, etat, priority, a,sup, type);
        }
        return "redirect:/Project/ManageTickets"; //TODO à changer vers la page des droits des utilisateurs
    }

    /**
     * Met dans la map les tickets correspondant à la recherche demandée
     * @param req
     * @param map
     * @param priority
     * @param etat
     * @param type
     * @param supervisor
     * @param author
     * @param boolPriority
     * @param boolEtat
     * @param boolType
     * @return Page générale des tickets
     */
    @RequestMapping(value={"/RechercheTickets"}, method= { RequestMethod.POST})
    public String RechercheTickets(
            HttpServletRequest req, ModelMap map,
            @RequestParam(value="choose_priority") String priority,
            @RequestParam(value="choose_etat") String etat ,
            @RequestParam(value="choose_type") String type,
            @RequestParam(value="choose_supervisor") String supervisor,
            @RequestParam(value="choose_author") String author,
            @RequestParam(value="choose_priority_eg") String boolPriority,
            @RequestParam(value="choose_etat_eg") String boolEtat,
            @RequestParam(value="choose_type_eg") String boolType
    ) {
        //Si la priority doit être différente ou égale de celle selectionnée
        boolean boolPriority2 =false;
        if(boolPriority.equals("Eg")) boolPriority2= true;

        //Si l'état doit être différent ou égal de celui selectionné
        boolean boolEtat2 =false;
        if(boolEtat.equals("Eg")) boolEtat2= true;

        //Si le type doit être différent ou égal de celui selectionné
        boolean boolType2 =false;
        if(boolType.equals("Eg")) boolType2= true;

        Project project= (Project) req.getSession().getAttribute("project");
        List<Ticket> myTickets=ticketmanager.getAllTicketsProps(etat,boolEtat2,priority, boolPriority2,
                type, boolType2, supervisor, author, "None", "Aucun", project);
        map.addAttribute("myTickets", myTickets);

        //Les membres du projets pour la recherche
        Project p= (Project) req.getSession().getAttribute("project");
        List<Member> members=projectmanager.getAllMembersByProject(p);
        map.addAttribute("members",members);

        return "manage_tickets";
    }
}
