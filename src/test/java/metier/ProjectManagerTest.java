package metier;

import BeansConfiguration.AppConfigTest;
import modele.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by audrey on 17/11/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class ProjectManagerTest {

    @Autowired
    UserManager userManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    MemberManager memberManager;


    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em;

    private Project project;
    private Project project2;
    private User user;
    private User user2;
    private User user3;
    private User user4;
    private Langage langage;

    @Before
    public void setUp() throws Exception {
        user= userManager.getByPseudo("Martin");
        if(user == null)
        {
            user=userManager.createUser("Martin", "firstname", "lastname", "mdpmdp", "martin@free.fr");
        }

        user2= userManager.getByPseudo("Emilie");
        if(user2 == null)
        {
            user2=userManager.createUser("Emilie", "firstname", "lastname", "mdpmdp", "anatole@free.fr");
        }

        user3= userManager.getByPseudo("Camille");
        if(user3 == null)
        {
            user3=userManager.createUser("Camille", "firstname", "lastname", "mdpmdp", "camille@free.fr");
        }
        user4= userManager.getByPseudo("Maurice");
        if(user4 == null)
        {
            user4=userManager.createUser("Maurice", "firstname", "lastname", "mdpmdp", "maurice@free.fr");
        }
        langage= projectManager.getLangageByName("C++");
        project=projectManager.createProject("MonProjet", "path", false,langage,user);
        project2=projectManager.createProject("MonProjet2", "path", false,langage,user2);
    }

    @Test
    public void createProject() throws Exception {
        Project myproject=projectManager.createProject("MonProjet", "path", false,langage,user);
        Project myproject2=projectManager.createProject("MonProjet2", "path", false,langage,user2);

        List<Project> projects=projectManager.getAllProjects();
        assertTrue("createProject()", projects.contains(myproject) );
        assertTrue("createProject()", projects.contains(myproject2) );

        assertTrue("createProject() - chef projet", memberManager.getRoleUserbyProject(user, myproject).equals(projectManager.getRoleByName("Chef")) );
        assertTrue("createProject() - chef projet", memberManager.getRoleUserbyProject(user2, myproject2).equals(projectManager.getRoleByName("Chef")) );
    }

    @Test
    public void createMember() throws Exception {
        Role r = memberManager.getRoleById(3);
        projectManager.createMember(user2, r, project);
        assertTrue("createMember()", projectManager.getAllMembersByProject(project).contains(projectManager.getMember(user2, project)));
        assertTrue("createMember()", projectManager.getMember(user2, project).getRole().equals(r));
    }

    @Test
    public void getMember() throws Exception {
        Member m=projectManager.getMember(user, project);
        assertTrue("getMember()", m.getIdUser().equals(user));

    }

    @Test
    public void changeRole() throws Exception {
        Role dev = memberManager.getRoleById(2);
        projectManager.createMember(user, dev, project2);
        assertTrue("changeRole()", projectManager.getMember(user, project2).getRole().equals(dev));
        Role rep = memberManager.getRoleById(3);
        projectManager.changeRole(user, rep, project2);
        assertTrue("changeRole()", projectManager.getMember(user, project2).getRole().equals(rep));
    }

    @Test
    public void getRoleByName() throws Exception {
        Role chef= projectManager.getRoleByName("Chef");
        assertTrue("getRoleByName()", projectManager.getMember(user, project).getRole().equals(chef));

    }

    @Test
    public void getLangageByName() throws Exception {
       Langage java=projectManager.getLangageByName("Java");
        assertTrue("getLangageByName()", java!=null);

    }

    @Test
    public void getAllLangages() throws Exception {
        List<Langage> langages=projectManager.getAllLangages();
        assertTrue("getAllLangages()", langages.size()==3);
    }

    @Test
    public void getAllMembersByProject() throws Exception {
        List<Member> membersBefore= projectManager.getAllMembersByProject(project);

        //On vérifie que l'user3 et l'user4 ne font pas déjà partie du projet
        assertTrue("getAllMembersByProject()", !membersBefore.contains(projectManager.getMember(user3, project)));
        assertTrue("getAllMembersByProject()", !membersBefore.contains(projectManager.getMember(user4, project)));
        Role r = memberManager.getRoleById(3);

        //On insert user3 au projet "project"
        projectManager.createMember(user3, r, project);

        //On insert un autre user dans un autre projet pour vérifier qu'il n'y a pas d'interférence entre les projets
        projectManager.createMember(user4, r, project2);

        List<Member> membersAfter= projectManager.getAllMembersByProject(project);

        //On vérifie que la fonction retourne bien une liste contenant user3
        assertTrue("getAllMembersByProject()", membersAfter.contains(projectManager.getMember(user3, project)));
        assertTrue("getAllMembersByProject()", !membersAfter.contains(projectManager.getMember(user4, project)));

    }

    @Test
    public void getAllProjects() throws Exception {
        List<Project> projectsBefore = projectManager.getAllProjects();
        assertTrue("getAllProjects()", projectsBefore.contains(project));
        assertTrue("getAllProjects()", projectsBefore.contains(project2));

        Project myproject=projectManager.createProject("MonProjet", "path", false,langage,user);
        List<Project> projectsAfter = projectManager.getAllProjects();
        assertTrue("getAllProjects()", projectsAfter.contains(myproject));
        assertTrue("getAllProjects()", projectsAfter.size()==projectsBefore.size()+1);

    }


}