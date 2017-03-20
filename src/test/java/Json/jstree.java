package Json;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JsonFormat.FileFormatter;
import ViewObjects.Jsons.jstree.Packagetree;
import modele.File;
import modele.Langage;
import modele.Project;
import modele.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class jstree{

    private String projectName ;
    private Project project ;
    private User user ;

    public jstree(){
        this.projectName="NomProjet";
        this.project=new Project(projectName,"",false, new Langage("java"));
        user=new User("arthur","arthur", "arthur", "arthur", "arthur");
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testJSONtree(){
        ArrayList<File> list=new ArrayList<File>();
        list.add(new File(user, project, "/package1/pack2", "fichier1.java" ));
        String res= FileFormatter.JsonPackageTree(Packagetree.ParseJSONpathfiles(projectName,list, user));
        System.out.println(res);
        assert(res.equals(" [{\"text\" : \"NomProjet\",\"type\" : \"package\",\"children\" : [{\"text\" : \"package1\",\"type\" : \"package\",\"children\" : [{\"text\" : \"pack2\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier1.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}} ]"));    }

    @Test
    public void multiplePackage(){
        ArrayList<File> list=new ArrayList<File>();
        list.add(new File(user ,project, "/package1/pack2", "fichier1.java" ));
        list.add(new File(user ,project, "/package/pack2", "fichier2.java" ));
        list.add(new File(user ,project, "/package1/pack3", "fichier3.java" ));
        list.add(new File(user ,project, "/package/package1", "fichier4.java" ));
        String res=FileFormatter.JsonPackageTree(Packagetree.ParseJSONpathfiles(projectName,list, user));
        System.out.println(res);
        assert(res.equals(" [{\"text\" : \"NomProjet\",\"type\" : \"package\",\"children\" : [{\"text\" : \"package1\",\"type\" : \"package\",\"children\" : [{\"text\" : \"pack3\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier3.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"text\" : \"pack2\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier1.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"text\" : \"package\",\"type\" : \"package\",\"children\" : [{\"text\" : \"pack2\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier2.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"text\" : \"package1\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier4.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}} ]")) ;
    }

    @Test
    public void diffentsizepackages(){
        ArrayList<File> list=new ArrayList<File>();
        list.add(new File(user ,project, "/package1/pack2/plop", "fichier1.java" ));
        list.add(new File(user ,project, "/package1/pack2/plop", "fichier11.java" ));
        list.add(new File(user ,project, "/package", "fichier2.java" ));
        list.add(new File(user ,project, "/package1/tagada/plop/plop", "fichier3.java" ));
        list.add(new File(user ,project, "/", "fichier4.java" ));
        String res=FileFormatter.JsonPackageTree(Packagetree.ParseJSONpathfiles(projectName,list, user));
        System.out.println(res);
        assert(res.equals(" [{\"text\" : \"NomProjet\",\"type\" : \"package\",\"children\" : [{\"text\" : \"package1\",\"type\" : \"package\",\"children\" : [{\"text\" : \"tagada\",\"type\" : \"package\",\"children\" : [{\"text\" : \"plop\",\"type\" : \"package\",\"children\" : [{\"text\" : \"plop\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier3.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"text\" : \"pack2\",\"type\" : \"package\",\"children\" : [{\"text\" : \"plop\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier1.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}},{\"id\" : \"file-0\",\"text\" : \"fichier11.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"text\" : \"package\",\"type\" : \"package\",\"children\" : [{\"id\" : \"file-0\",\"text\" : \"fichier2.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}},{\"id\" : \"file-0\",\"text\" : \"fichier4.java\",\"type\" : \"file\",\"children\" : [],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"0\",\"haslock\" : \"true\"}}],\"data\" :{\"opened\" : \"closed\",\"idfile\" : \"-1\",\"haslock\" : \"true\"}} ]"));
    }
}
