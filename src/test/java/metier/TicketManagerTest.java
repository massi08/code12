package metier;

import BeansConfiguration.AppConfigTest;
import modele.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by audrey on 17/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class TicketManagerTest {

    @Autowired
    UserManager userManager;

    @Autowired
    TicketManager ticketManager;

    @Autowired
    ProjectManager projectManager;

    private Project project;
    private Project project2;
    private User user;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user= userManager.getByPseudo("Anais");
        if(user == null)
        {
            user=userManager.createUser("Anais", "firstname", "lastname", "mdpmdp", "anais@free.fr");
        }

        user2= userManager.getByPseudo("Anatole");
        if(user2 == null)
        {
            user2=userManager.createUser("Anatole", "firstname", "lastname", "mdpmdp", "anatole@free.fr");
        }

        Langage langage= projectManager.getLangageByName("C++");
        project=projectManager.createProject("MonProjet", "path", false,langage,user);
        project2=projectManager.createProject("MonProjet2", "path", false,langage,user);
    }


    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em;

    @Test
    //création d'un ticket sans superviseur
    public void createTicket() throws Exception {
        Date date= new Date();
        List<Ticket> ticketsBefore=ticketManager.getAllTickets();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        List<Ticket> ticketsAfter=ticketManager.getAllTickets();
        assertTrue("createTicket()", !ticketsBefore.contains(ticket));
        assertTrue("createTicket()", ticketsAfter.contains(ticket));
        assertTrue("createTicket()", ticketsAfter.size()==ticketsBefore.size()+1);
        assertTrue("createTicket()", ticket.getSupervisor()==null);
    }

   @Test
    //création d'un ticket avec un superviseur
    public void createTicket1() throws Exception {
        Date date= new Date();
        List<Ticket> ticketsBefore=ticketManager.getAllTickets();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user,user2, "Bug");
        List<Ticket> ticketsAfter=ticketManager.getAllTickets();
        assertTrue("createTicket1()", !ticketsBefore.contains(ticket));
        assertTrue("createTicket1()", ticketsAfter.contains(ticket));
        assertTrue("createTicket1()", ticketsAfter.size()==ticketsBefore.size()+1);
        assertTrue("createTicket1()", ticket.getSupervisor().equals(user2));
    }

   @Test
    public void setEtatTicket() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        assertTrue("setEtatTicket()", ticket.getNameEtat().equals("EnCours"));
        ticketManager.setEtatTicket(ticket, "Ferme");
        assertTrue("setEtatTicket()", ticket.getNameEtat().equals("Ferme"));
    }

    @Test
    public void setPriorityTicket() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        assertTrue("setPriorityTicket()", ticket.getPriority().equals("Mineure"));
        ticketManager.setPriorityTicket(ticket, "Majeure");
        assertTrue("setPriorityTicket()", ticket.getPriority().equals("Majeure"));

    }

    @Test
    public void setTypeTicket() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        assertTrue("setTypeTicket()", ticket.getType().equals("Bug"));
        ticketManager.setTypeTicket(ticket, "Tracker");
        assertTrue("setTypeTicket()", ticket.getType().equals("Tracker"));
    }

    @Test
    public void setSupervisorTicket() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user,user, "Bug");
        assertTrue("setSupervisorTicket()", ticket.getSupervisor().equals(user));
        ticketManager.setSupervisorTicket(ticket,user2);
        assertTrue("setSupervisorTicket()", ticket.getSupervisor().equals(user2));
    }

    @Test
    public void getTicketById() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        int id=ticket.getNumber();
        assertTrue("getById()", ticketManager.getTicketById(id).equals(ticket));
    }

    @Test
    public void getAllTickets() throws Exception {
        Date date= new Date();
        List<Ticket> ticketsBefore=ticketManager.getAllTickets();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        Ticket ticket2= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"Ferme", "Majeure", user2, "Tracker");
        List<Ticket> ticketsAfter=ticketManager.getAllTickets();

        //le resultat de getAllTickets doit avoir 2 tickets en plus (on en a ajouté 2)
        assertTrue("getAllTickets()",ticketsAfter.size()==ticketsBefore.size()+2 );

        //Les tickets ajoutés doivent être dans le resultat de getAllTickets
        assertTrue("getAllTickets()", ticketsAfter.contains(ticket));
        assertTrue("getAllTickets()", ticketsAfter.contains(ticket2));
    }

    @Test
    public void getAllTicketsByProject() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore=ticketManager.getAllTicketsByProject(project);

        //ticket du project "project"
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        //ticket du project "project2"
        Ticket ticket2= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"Ferme", "Majeure", user2, "Tracker");
        List<Ticket> ticketsAfter=ticketManager.getAllTicketsByProject(project);

        //On a ajouté un ticket pour le projet "project", getAllTicketsByProject(project) doit renvoyé un ticket de plus
        assertTrue("getAllTickets()",ticketsAfter.size()==ticketsBefore.size()+1 );

        //On vérifie que getAllTicketsByProject(project) contient le bon ticket
        assertTrue("getAllTickets()", ticketsAfter.contains(ticket));
        assertTrue("getAllTickets()", !ticketsAfter.contains(ticket2));
    }

    @Test
    public void getAllByAuthor() throws Exception {
        Date date= new Date();
        List<Ticket> ticketsBefore=ticketManager.getAllByAuthor(user, project);

        //ticket dont l'auteur est "user"
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");

        //ticket dont l'auteur est "user2"
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"Ferme", "Majeure", user2, "Tracker");

        List<Ticket> ticketsAfter=ticketManager.getAllByAuthor(user, project);

        //On a ajouté un ticket ayant pour auteur "user", getAllTicketsByProject(project) doit renvoyé un ticket de plus
        assertTrue("getAllByAuthor()",ticketsAfter.size()==ticketsBefore.size()+1 );

        //On vérifie que getAllByAuthor(user) contient le bon ticket
        assertTrue("getAllByAuthor()", ticketsAfter.contains(ticket));
        assertTrue("getAllByAuthor()", !ticketsAfter.contains(ticket2));
    }

    @Test
    public void getAllBySupervisor() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore=ticketManager.getAllBySupervisor(user,project);

        //ticket ayant pour superviseur "user"
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");
        //ticket ayant pour superviseur "user"
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"Ferme", "Majeure", user2, user,"Tracker");
        List<Ticket> ticketsAfter=ticketManager.getAllBySupervisor(user,project);

        //On a ajouté 2 tickets ayant , getAllTicketsByProject(project) doit renvoyé un ticket de plus
        assertTrue("getAllBySupervisor()",ticketsAfter.size()==ticketsBefore.size()+2 );

        //On vérifie que getAllTicketsByProject(project) contient le bon ticket
        assertTrue("getAllBySupervisor()", ticketsAfter.contains(ticket));
        assertTrue("getAllBySupervisor()", ticketsAfter.contains(ticket2));

    }

    @Test
    public void createCommentary() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");
        List<Commentary> commsBefore= ticketManager.getAllCommentariesByTicket(ticket);
        Commentary comm= ticketManager.createCommentary(user, ticket, "Mon commentaire", date);
        //Commentaire du ticket "ticket"
        List<Commentary> commsAfter= ticketManager.getAllCommentariesByTicket(ticket);
        assertTrue("createCommentary()", !commsBefore.contains(comm));
        assertTrue("createCommentary()", commsAfter.contains(comm));
        assertTrue("createCommentary()", commsAfter.size()==commsBefore.size()+1);
    }

    @Test
    public void getAllCommentariesByTicket() throws Exception {
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");
        List<Commentary> commsBefore= ticketManager.getAllCommentariesByTicket(ticket);

        //Commentaire du ticket "ticket"
        Commentary comm= ticketManager.createCommentary(user, ticket, "Mon commentaire de ticket", date);

        //Commentaire du ticket "ticket2"
        Commentary comm2= ticketManager.createCommentary(user, ticket2, "Mon commentaire de ticket2", date);
        List<Commentary> commsAfter= ticketManager.getAllCommentariesByTicket(ticket);
        assertTrue("createCommentary()", !commsBefore.contains(comm)); //Le commentaire n'était pas là avant
        assertTrue("createCommentary()", commsAfter.contains(comm)); // Le commentaire a bien été ajouté
        assertTrue("createCommentary()", !commsAfter.contains(comm2)); //le ticket n'a pas les commentaires d'un autre ticket
        assertTrue("createCommentary()", commsAfter.size()==commsBefore.size()+1);

    }

    @Test
    public void getAllTicketsByType() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByType("Tracker", project);
        //doivent être retenus car type=Tracker et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Tracker");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Tracker");

        //ne doit pas être retenu car type!=Tracker
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByType("Tracker", project);
        assertTrue("getAllTicketsByType()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByType()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByType()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByType()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByType()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByType()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByType()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByType()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByType()", ticketsAfter.size()==ticketsBefore.size()+2);

    }

    @Test
    public void getAllTicketsByNotType() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByNotType("Tracker", project);
        //doivent être retenus car type!=Tracker et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Build");

        //ne doit pas être retenu car type=Tracker
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Tracker");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByNotType("Tracker", project);
        assertTrue("getAllTicketsByNotType()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByNotType()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByNotType()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByNotType()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByNotType()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByNotType()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByNotType()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByNotType()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByNotType()", ticketsAfter.size()==ticketsBefore.size()+2);
    }

    @Test
    public void getAllTicketsByPriority() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByPriority("Mineure", project);
        //doivent être retenus car priorite=Mineure et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Tracker");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Tracker");

        //ne doit pas être retenu car priorite!=Mineure
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Majeure", user, user,"Bug");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByPriority("Mineure", project);
        assertTrue("getAllTicketsByPriority()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByPriority()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByPriority()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByPriority()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByPriority()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByPriority()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByPriority()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByPriority()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByPriority()", ticketsAfter.size()==ticketsBefore.size()+2);

    }

    @Test
    public void getAllTicketsByNotPriority() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByNotPriority("Mineure", project);
        //doivent être retenus car priorite!=Mineure et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Majeure", user, user,"Tracker");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"NonTraite", "Bloquante", user, user,"Bug");

        //ne doit pas être retenu car priorite=Mineure
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"EnCours", "Bloquante", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByNotPriority("Mineure", project);
        assertTrue("getAllTicketsByNotPriority()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByNotPriority()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByNotPriority()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByNotPriority()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByNotPriority()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByNotPriority()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByNotPriority()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByNotPriority()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByNotPriority()", ticketsAfter.size()==ticketsBefore.size()+2);

    }

    @Test
    public void getAllTicketsByEtat() throws Exception {

        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByEtat("EnCours", project);
        //doivent être retenus car etat=EnCours et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Majeure", user, user,"Tracker");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Bloquante", user, user,"Bug");

        //ne doit pas être retenu car etat!=EnCours
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"NonTraite", "Mineure", user, user,"Bug");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"EnCours", "Bloquante", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByEtat("EnCours", project);
        assertTrue("getAllTicketsByEtat()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByEtat()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByEtat()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByEtat()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByEtat()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByEtat()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByEtat()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByEtat()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByEtat()", ticketsAfter.size()==ticketsBefore.size()+2);

    }

    @Test
    public void getAllTicketsByNotEtat() throws Exception {
        Date date= new Date();

        List<Ticket> ticketsBefore= ticketManager.getAllTicketsByNotEtat("EnCours", project);
        //doivent être retenus car etat!=EnCours et appartiennent à project
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"NonTraite", "Majeure", user, user,"Tracker");
        Ticket ticket2= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"Ferme", "Bloquante", user, user,"Bug");

        //ne doit pas être retenu car etat=EnCours
        Ticket ticket3= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, user,"Bug");

        //ne doit pas être retenu car appartient au project2
        Ticket ticket4= ticketManager.createTicket(project2, "Mon titre", "Mon contenu", date,"NonTraite", "Bloquante", user, user,"Bug");

        List<Ticket> ticketsAfter= ticketManager.getAllTicketsByNotEtat("EnCours", project);
        assertTrue("getAllTicketsByNotEtat()", !ticketsBefore.contains(ticket));
        assertTrue("getAllTicketsByNotEtat()", !ticketsBefore.contains(ticket2));
        assertTrue("getAllTicketsByNotEtat()", !ticketsBefore.contains(ticket3));
        assertTrue("getAllTicketsByNotEtat()", !ticketsBefore.contains(ticket4));

        assertTrue("getAllTicketsByNotEtat()", ticketsAfter.contains(ticket));
        assertTrue("getAllTicketsByNotEtat()", ticketsAfter.contains(ticket2));
        assertTrue("getAllTicketsByNotEtat()", !ticketsAfter.contains(ticket3));
        assertTrue("getAllTicketsByNotEtat()", !ticketsAfter.contains(ticket4));
        assertTrue("getAllTicketsByNotEtat()", ticketsAfter.size()==ticketsBefore.size()+2);

    }

    @Test
    public void getAllTicketsProps() throws Exception {

        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Bloquante", user, user,"Tracker");

        //Les tickets "En Cours" de n'importe quelle priorité, qui ne sont pas des bugs et dont le superviseur et l'auteur sont user
        List<Ticket> ticketsBis = ticketManager.getAllTicketsProps("EnCours", true, "None", true, "Bug", false, String.valueOf(user.getIdUser()),
                String.valueOf(user.getIdUser()), "None", "Aucun", project);
        for(int i=0; i<ticketsBis.size(); i++)
        {
            assertTrue("getAllTicketsProps()", ticketsBis.get(i).getNameEtat().equals("EnCours"));
            assertTrue("getAllTicketsProps()", !ticketsBis.get(i).getType().equals("Bug"));
            assertTrue("getAllTicketsProps()", ticketsBis.get(i).getAuthor().equals(user));
            assertTrue("getAllTicketsProps()", ticketsBis.get(i).getSupervisor().equals(user));
            assertTrue("getAllTicketsProps()", ticketsBis.get(i).getIdProject().equals(project));
            assertTrue("getAllTicketsProps()", ticketsBis.get(i).getIdProject().equals(project));
        }


        //Les tickets "En Cours" de priorité "Mineure", qui ne sont pas des bugs et dont le superviseur et l'auteur sont user
        List<Ticket> tickets = ticketManager.getAllTicketsProps("EnCours", true, "Mineure", true, "Bug", false, String.valueOf(user.getIdUser()),
                                                                    String.valueOf(user.getIdUser()), "None", "Aucun", project);
        for(int i=0; i<tickets.size(); i++)
        {
            assertTrue("getAllTicketsProps()", tickets.get(i).getNameEtat().equals("EnCours"));
            assertTrue("getAllTicketsProps()", tickets.get(i).getPriority().equals("Mineure"));
            assertTrue("getAllTicketsProps()", !tickets.get(i).getType().equals("Bug"));
            assertTrue("getAllTicketsProps()", tickets.get(i).getAuthor().equals(user));
            assertTrue("getAllTicketsProps()", tickets.get(i).getSupervisor().equals(user));
            assertTrue("getAllTicketsProps()", tickets.get(i).getIdProject().equals(project));
            assertTrue("getAllTicketsProps()", ticketsBis.contains(tickets.get(i)));
        }
//ticketBis contient tous les tickets de "tickets", plus ceux qui qui une priorité autre que "Mineure"
        assertTrue("getAllTicketsProps()", ticketsBis.size()>tickets.size());


        //Les tickets non "En Cours" de n'importe quelle priorité, qui ne sont pas des bugs et dont l'auteur est user, et qui n'ont pas de superviseur
        List<Ticket> tickets2 = ticketManager.getAllTicketsProps("EnCours", false, "None", true, "Bug", false,"Aucun",
                                                                    String.valueOf(user.getIdUser()), "None", "Aucun", project);
        for(int i=0; i<tickets.size(); i++)
        {
            assertTrue("getAllTicketsProps()", !tickets.get(i).getNameEtat().equals("EnCours"));
            assertTrue("getAllTicketsProps()", !tickets.get(i).getType().equals("Bug"));
            assertTrue("getAllTicketsProps()", tickets.get(i).getAuthor().equals(user));
            assertTrue("getAllTicketsProps()", tickets.get(i).getSupervisor()==null);
            assertTrue("getAllTicketsProps()", tickets.get(i).getIdProject().equals(project));
        }


    }

}