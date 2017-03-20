package metier;

import BeansConfiguration.AppConfigTest;
import junit.framework.TestCase;
import modele.*;
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



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class NewsManagerTest{

    @Autowired
    NewsManager newsManager;

    @Autowired
    TicketManager ticketManager;

    @Autowired
    UserManager userManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    MemberManager memberManager;

    @Autowired
    FileManager fileManager;

    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em;


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

    @Test
    public void testGetAllNewsByProject() throws Exception {
        List<News> newsBefore= newsManager.getAllNewsByProject(project);

        //Creation d'une news : on ajoute un ticket !
        Date date= new Date();
        Ticket ticket= ticketManager.createTicket(project, "Mon titre", "Mon contenu", date,"EnCours", "Mineure", user, "Bug");

        List<News> newsAfter = newsManager.getAllNewsByProject(project);

        assertTrue("getAllNews()",newsAfter.size()==newsBefore.size()+1 );


    }

    @Test
    public void testGetStringByNews() throws Exception {
        List<News> newsBefore =null;
        Member m=null;
        String strString=null;
        //TEST AJOUT D'UN MEMBRE
        Role r = memberManager.getRoleById(2);
        m = projectManager.getMember(user2, project);
        if(m==null){
            m = projectManager.createMember(user2,r, project);
        }
        newsBefore= newsManager.getAllNewsByProject(project);
        News newsAddMember= newsBefore.get(newsBefore.size()-1);
        String strAddMember = newsManager.getStringByNews(newsAddMember);
        strString="Le membre " + user2.getPseudo() + " a été ajouté au projet.";
        assertTrue("getStringByNewsAddMember()",strAddMember.equals(strString));

        //TEST MODIFICATION ROLE MEMBRE
        Role newR = memberManager.getRoleById(3);
        m = projectManager.getMember(user2, project);
        if(m==null){
            m = projectManager.createMember(user2,r, project);
        }
        if(m.getRole().getIdRole()!=3){
            projectManager.changeRole(user2, newR, project);
        }
        else
        {
            newR= memberManager.getRoleById(2);
            projectManager.changeRole(user2, newR, project);
        }

        newsBefore= newsManager.getAllNewsByProject(project);
        News newsModifMember= newsBefore.get(newsBefore.size()-1);
        String strModifMember = newsManager.getStringByNews(newsModifMember);
        strString="Le membre " + user2.getPseudo() + " a changé de role.";


        assertTrue("getStringByNewsModifMember()",strModifMember.equals(strString));

        //TEST SUPPRESSION MEMBRE

        Role deleteR = memberManager.getRoleById(4);
        projectManager.changeRole(user2, deleteR, project);
        newsBefore= newsManager.getAllNewsByProject(project);
        News newsGetOldMember= newsBefore.get(newsBefore.size()-1);
        String strGetOldMember = newsManager.getStringByNews(newsGetOldMember);
        strString="Le membre " + user2.getPseudo() + " a été supprimé du projet.";

        assertTrue("getStringByNewsSupprMember()",strGetOldMember.equals(strString));

        //TEST AJOUT FICHIER

        String nameFile="nameFile";
        File file = fileManager.createFile(user, project,"path", nameFile);
        newsBefore= newsManager.getAllNewsByProject(project);
        News newsAddFile= newsBefore.get(newsBefore.size()-1);
        String strAddFile = newsManager.getStringByNews(newsAddFile);
        strString="Le fichier " + nameFile + " a été ajouté au projet. ";
        
        assertTrue("getStringByNewsAddFile()",strAddFile.equals(strString));


        //TEST AJOUT TICKET
        Date date= new Date();
        String titreTicket = "Mon Titre";
        Ticket ticket= ticketManager.createTicket(project, titreTicket, "Mon contenu", date,"EnCours", "Mineure", user, "Bug");
        newsBefore= newsManager.getAllNewsByProject(project);
        News newsTicket= newsBefore.get(newsBefore.size()-1);
        String strTicket = newsManager.getStringByNews(newsTicket);
        strString="Le ticket " + titreTicket + " a été crée par " + user.getPseudo() +".";

        assertTrue("getStringByNews()",strTicket.equals(strString));




    }

}