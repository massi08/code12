package metier;

import BeansConfiguration.AppConfigTest;
import modele.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class UserManagerTest {


    @Autowired
    UserManager userManager;

    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em;


    @Test
    public void getByPseudo() throws Exception {
        //Création d'un user (pseudo unique)
        User user= userManager.getByPseudo("Marie");
        if(user == null)
        {
            user=userManager.createUser("Marie", "firstname", "lastname", "mdpmdp",  "alpha@yahoo.fr");
        }
        User user2=userManager.getByPseudo("Marie");
        assertTrue("getByPseudo()", user.equals(user2));
    }

    @Test
    public void getById() throws Exception {

        User user= userManager.getByPseudo("Marie");
        if(user == null)
        {
            user=userManager.createUser("Marie", "firstname", "lastname", "mdpmdp",  "alpha@yahoo.fr");
        }
        int id=user.getIdUser();
        User user2= userManager.getUserById(id);
        assertTrue("getById()", user.equals(user2));
    }


    @Test
    public void createUser() throws Exception {
        User user= null;
        int i=0;
        while(user == null)
        {
            user=userManager.createUser("Melissa"+i, "firstname", "lastname", "mdpmdp",  "alpha@yahoo.fr");
            i++;
        }
        assertTrue("createUser()", userManager.getAllUsers().contains(user));

    }

    @Test
    public void getAllUsers() throws Exception {
        List<User> res1=userManager.getAllUsers();
        User user= null;
        int i=0;
        while(user == null)
        {
            user=userManager.createUser("Pierre"+i, "firstname", "lastname", "mdpmdp", "alpha@yahoo.fr");
            i++;
        }
        assertTrue("getAllUsers()", userManager.getAllUsers().size()==res1.size()+1);

    }

}