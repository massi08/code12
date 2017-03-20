package Controller;

import metier.CompilerManager;
import metier.FileManager;
import metier.UserManager;
import modele.Project;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import org.apache.log4j.Logger;

@Controller
public class ControllerCompiler {
    /**
     * Le contrôleur gère ce qui se rapporte à la compilation
     */
    private CompilerManager compilerManager;

    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager;

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;

    private Logger logger = Logger.getLogger(ControllerCompiler.class);

    /**
     * Récupère l'id du projet, son langage pour le compiler et retourné les logs d'érreurs ou de sorties.
     *
     * @param req
     * @return ResponseEntity contenant l'output de compilation
     */
    @RequestMapping(value = {"/ajax/compiler"}, method = {RequestMethod.GET})
    ResponseEntity compiler(
            HttpServletRequest req,
            HttpSession session
    ) {
        try {
            compilerManager = new CompilerManager();
            User user = usermanager.GetUserSession();
            Project project = (Project) session.getAttribute("project");
            String origpath = req.getServletContext().getRealPath("/static_website/projects/");
            File folder = new File(origpath + "/" + project.getIdProject() + "/" + project.getName() + "/");
            String output = compilerManager.interpreter(filemanager, folder, user, project);
            return new ResponseEntity<String>(output, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<String>("Erreur lors de la compilation. Voir fichiers de log pour plus d'informations.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleIOException(Exception ex) {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}