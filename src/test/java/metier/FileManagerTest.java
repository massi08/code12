package metier;

import BeansConfiguration.AppConfigTest;
import modele.File;
import modele.Langage;
import modele.Project;
import modele.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class FileManagerTest {
    @Autowired
    UserManager userManager;

    @Autowired
    FileManager fileManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    @Qualifier(value = "entityManagerTest")
    EntityManager em;

    private static String path=(new java.io.File("").getAbsoluteFile())+"/src/main/webapp/static_website/projects";

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
    public void CreateAndGetFile(){
        User user=userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        Langage java=projectManager.getLangageByName("Java");
        Project project = projectManager.createProject("project", "/", true, java, user);
        File file = fileManager.createFile(user, project, "/", "FileTest");
        try {
            fileManager.createLocalFile(project, path, "/", "FileTest", "Voici le texte");
        }catch(Exception e){
            assertTrue(false);
        }

        try {
            String text = fileManager.readLocalFile(path, file);
            assertTrue(text.equals("Voici le texte\\n") );
        }catch(Exception e ){
            assertTrue(false);
        }
        fileManager.deleteFile(file);
        fileManager.deleteLocalFile(path, file);
    }


    @Test
    public void ChangeLockingFile(){
        User user=userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        Langage java=projectManager.getLangageByName("Java");
        Project project = projectManager.createProject("project", "/", true, java, user);
        File file = fileManager.createFile(user, project, "/", "FileTest");
        try {
            fileManager.createLocalFile(project, path, "/", "FileTest", "Voici le texte");
        }catch(Exception e){
            assertTrue(false);
        }
        assertTrue(fileManager.unsetlocking(file, user)); // le fichier a été dévérouiller
        assertTrue(fileManager.setlocking(file, user)); // Le fichier est revérouillé
        assertTrue(file.boolocking(user)); // ON vérifie que user peut accéder à file

        fileManager.deleteFile(file);
        fileManager.deleteLocalFile(path, file);
    }

    @Test
    public void MetasChanges(){
        User user=userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        Langage java=projectManager.getLangageByName("Java");
        Project project = projectManager.createProject("project", "/", true, java, user);
        File file = fileManager.createFile(user, project, "/", "FileTest");

        try {
            fileManager.createLocalFile(project, path, "/", "FileTest", "Voici le texte");
        }catch(Exception e){
            assertTrue(false);
        }
        File file2=fileManager.getFileById(file.getIdFile());
        assertTrue(file2.getNameFile().equals(file.getNameFile()));
        assertTrue(fileManager.ModifyFile(file, "/packs/", "NouveauNom", path));

        file=fileManager.getFileById(file2.getIdFile());
        assertTrue(file.getNameFile().equals("NouveauNom"));
        assertTrue(file.getPathFile().equals("/packs/"));
        try {
            String text = fileManager.readLocalFile(path, file);
            assertTrue(text.equals("Voici le texte\\n") );
        }catch(Exception e ){
            assertTrue(false);
        }
        File file3 = fileManager.createFile(user, project, "/", "FileTest");
        assertTrue(fileManager.ModifyFile(file, "/packs/pack2/pack/", "NouveauNom3", path));
        assertTrue(!fileManager.ModifyFile(file, "/", "FileTest", path)); // Il ne peut pas changer en un fichier déjà existant

        assertTrue(fileManager.deleteLocalFile(path, file));
        fileManager.deleteFile(file);
    }

 @Test
    public void deleteFile(){
        User user=userManager.createUser("pseudo", "pseudo", "pseudo", "pseudo", "pseuso@pseudo");
        Langage java=projectManager.getLangageByName("Java");
        Project project = projectManager.createProject("project", "/", true, java, user);
        File file = fileManager.createFile(user, project, "/", "FileTest");
        File file2 = fileManager.createFile(user, project, "/pack1/pack2/pack3/", "FileTest");
        try {
            fileManager.createLocalFile(project, path, "/", "FileTest", "Voici le texte");
            fileManager.createLocalFile(project, path, "/pack1/pack2/pack3/", "FileTest", "Voici le texte");
            fileManager.createLocalFile(project, path, "/pack1/", "FileTest", "Voici le texte");
            fileManager.createLocalFile(project, path, "/", "FileTest2", "Voici le texte");
            assertTrue(fileManager.deleteLocalFile(path,file2));
            assertTrue( !(new java.io.File(path+"/"+project.getIdProject()+"/"+project.getName()+"/pack1/pack2")).exists()); // On vérifie que le répertoire vide est supprimé
            assertTrue( (new java.io.File(path+"/"+project.getIdProject()+"/"+project.getName()+"/pack1/")).exists()); // On vérifie que le répertoire vide est toujours là car un autre fichier est dedans
        }catch(Exception e){
            assertTrue(false);
        }

        assertTrue(fileManager.deleteLocalFile(path, file));
        fileManager.deleteFile(file);
    }

}
