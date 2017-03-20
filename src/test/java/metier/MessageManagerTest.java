package metier;


import BeansConfiguration.AppConfigTest;
import ViewObjects.Discussiongroup;
import modele.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class MessageManagerTest {

    @Autowired
    UserManager userManager;

    @Autowired
    MessageManager messageManager ;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em ;

    @Before
    public void DeleteEverything(){
        String deleteQuery="DELETE FROM NEWS ";
        String deleteQuery2="DELETE FROM MEMBER ";
        String deleteQueryFile="DELETE FROM FILE";
        String deleteQuery21="DELETE FROM MESSAGE ";
        String deleteQuery22="DELETE FROM DISCUSSION ";
        String deleteQueryCommentary="DELETE FROM COMMENTARY ";
        String deleteQueryTicket="DELETE FROM TICKET";
        String deleteQuery3="DELETE FROM USER ";
        String deleteQuery4="DELETE FROM PROJECT ";
        em.getTransaction().begin() ;
        em.createNativeQuery(deleteQuery2).executeUpdate();
        em.createNativeQuery(deleteQueryFile).executeUpdate();
        em.createNativeQuery(deleteQuery).executeUpdate();
        em.createNativeQuery(deleteQuery21).executeUpdate();
        em.createNativeQuery(deleteQuery22).executeUpdate();
        em.createNativeQuery(deleteQueryCommentary).executeUpdate();
        em.createNativeQuery(deleteQueryTicket).executeUpdate();
        em.createNativeQuery(deleteQuery3).executeUpdate();
        em.createNativeQuery(deleteQuery4).executeUpdate();
        em.getTransaction().commit() ;
    }

    @Test
    public void ManageDiscussion(){
        User user= userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        User user1= userManager.createUser("pseudo1", "pseudo1", "pseudo1", "pseudo1", "pseuso1@pseudo1");
        User user2= userManager.createUser("pseudo2", "pseudo2", "pseudo2", "pseudo2", "pseuso2@pseudo2");
        Langage java=projectManager.getLangageByName("Java");
        Project project=projectManager.createProject("MonTestProject", "", false, java, user);
        Discussion discussion= messageManager.createDiscussion(user, project);
        assertTrue( discussion != null );
        assertTrue(messageManager.addMemberInDiscussion(discussion, user1, project ) != null) ;
        assertTrue(messageManager.addMemberInDiscussion(discussion, user2, project ) != null) ;
        Discussion discussion2=messageManager.createDiscussion(user, project);
        messageManager.addMemberInDiscussion(discussion2, user1, project);
        assertTrue(discussion2.getIdDiscussion() != discussion.getIdDiscussion());

        assertTrue(messageManager.AlreadyExistsDiscussion(user1, user) == discussion2.getIdDiscussion() ); // Une discussion qui contient que user et user1
        assertTrue(messageManager.AlreadyExistsDiscussion(user1, user2) == null) ;
        messageManager.removeMemberInDiscussion(discussion2.getIdDiscussion() , user); // On enlève un membre de la discussion
        assertTrue( messageManager.AlreadyExistsDiscussion(user1, user) == null ) ;
    }

    @Test
    public void ManageMessages(){
        User user= userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        User user1= userManager.createUser("pseudo1", "pseudo1", "pseudo1", "pseudo1", "pseuso1@pseudo1");
        User user2= userManager.createUser("pseudo2", "pseudo2", "pseudo2", "pseudo2", "pseuso2@pseudo2");

        Langage java=projectManager.getLangageByName("Java");
        Project project=projectManager.createProject("MonTestProject", "", false, java, user);
        Discussion discussion= messageManager.createDiscussion(user, project);
        assertTrue(messageManager.addMemberInDiscussion(discussion, user2, project ) != null) ;
        messageManager.addMemberInDiscussion(discussion, user1, project);

        Message message1=messageManager.createMessage("Cest un super message", discussion.getIdDiscussion(), user.getPseudo());
        Message message2=messageManager.createMessage("Cest un deuxieme super message", discussion.getIdDiscussion(), user2.getPseudo());
        Message message3= messageManager.createMessage("Cest un troisième super message", discussion.getIdDiscussion(), user1.getPseudo());
        assertTrue(messageManager.getMessageById(message1.getIdMessage()).getText().equals("Cest un super message")); // Il est bien en BDD
        messageManager.ModifyMessage(message1, "nouveau text");
        assertTrue(messageManager.getMessageById(message1.getIdMessage()).getText().equals("nouveau text")); // Il a bien été modifié
        assertTrue(messageManager.getMessageById(message2.getIdMessage()) != null);// Il est en bdd
        messageManager.DeleteMessage(message2);
        assertTrue(messageManager.getMessageById(message2.getIdMessage()) == null);// Il est supprimé de la BDD
    }

    @Test
    public void ViewGeneratorDiscussion(){
        User user= userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        User user1= userManager.createUser("pseudo1", "pseudo1", "pseudo1", "pseudo1", "pseuso1@pseudo1");
        User user2= userManager.createUser("pseudo2", "pseudo2", "pseudo2", "pseudo2", "pseuso2@pseudo2");

        Langage java=projectManager.getLangageByName("Java");
        Project project=projectManager.createProject("MonTestProject", "", false, java, user);
        Discussion discussion= messageManager.createDiscussion(user, project);
        assertTrue(messageManager.addMemberInDiscussion(discussion, user2, project ) != null) ;
        messageManager.addMemberInDiscussion(discussion, user1, project);
        Discussion discussion2= messageManager.createDiscussion(user, project);
        messageManager.addMemberInDiscussion(discussion2, user1, project);
        HashMap<Integer, Discussiongroup> discussions=messageManager.getDiscussions(user, project);
        ArrayList<User> listUser=discussions.get(discussion.getIdDiscussion()).getName();
        assertTrue(listUser.contains(user1));
        assertTrue(listUser.contains(user2));
        assertTrue(!listUser.contains(user)); // Il ne s'ajoute pas lui même
        ArrayList<User> listUser2=discussions.get(discussion2.getIdDiscussion()).getName();
        assertTrue(listUser2.contains(user1)); // Contient user 1
        assertTrue(!listUser2.contains(user2)); // Ne contient pas user2
    }

    @Test
    public void JsonFormatDiscussions(){
        User user= userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        User user1= userManager.createUser("pseudo1", "pseudo1", "pseudo1", "pseudo1", "pseuso1@pseudo1");
        User user2= userManager.createUser("pseudo2", "pseudo2", "pseudo2", "pseudo2", "pseuso2@pseudo2");

        Langage java=projectManager.getLangageByName("Java");
        Project project=projectManager.createProject("MonTestProject", "", false, java, user);
        Discussion discussion= messageManager.createDiscussion(user, project);
        assertTrue(messageManager.addMemberInDiscussion(discussion, user2, project ) != null) ;
        messageManager.addMemberInDiscussion(discussion, user1, project);
        Discussion discussion2= messageManager.createDiscussion(user, project);
        messageManager.addMemberInDiscussion(discussion2, user1, project);
        Message message1=messageManager.createMessage("Cest un super message", discussion.getIdDiscussion(), user.getPseudo());
        Message message2=messageManager.createMessage("Cest un deuxieme super message", discussion.getIdDiscussion(), user2.getPseudo());
        Message message3= messageManager.createMessage("Cest un troisième super message", discussion.getIdDiscussion(), user1.getPseudo());

    }


}
