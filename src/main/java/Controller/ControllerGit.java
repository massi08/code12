package Controller;

import ViewObjects.Jsons.JsonFormat.FileFormatter;
import metier.FileManager;
import metier.ProjectManager;
import metier.UserManager;
import modele.File;
import modele.Project;
import modele.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Project : code12
 * Package : Controller
 * Created by adilon on 15/11/16.
 * Copyright (c). All rights reserved.
 */

@Controller
public class ControllerGit {
    @Autowired
    ApplicationContext ctx;
    @Autowired
    @Qualifier(value = "usermanager")
    UserManager usermanager;
    @Autowired
    @Qualifier(value = "filemanager")
    FileManager filemanager;
    @Autowired
    @Qualifier(value = "projectmanager")
    ProjectManager projectManager;

    private Logger logger = Logger.getLogger(ControllerGit.class);
    private static String localPath = "/static_website/projects/";

    /**
     * Effectue le commit à la racine du projet ayant comme identifiant projectId par l'intermédiaire du fileManager
     * @param req La requête
     * @param session La session
     * @param projectId L'identifiant du projet
     * @param commitMessage Le message du commit
     * @return La sortie de la commande "git commit"
     */
    @ResponseBody
    @RequestMapping(value = "/ajax/gitcommit", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity commit(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "project") int projectId,
            @RequestParam(value = "message") String commitMessage
    ) {
        User user=usermanager.GetUserSession() ;
        Project project= (Project) req.getSession().getAttribute("project");
        List<File> files= filemanager.getFileByUserProject(user, project);
        String output=filemanager.gitCommit(project, files, commitMessage);
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    /**
     * Effectue le git log d'un fichier
     * @param req La requête
     * @param projectid L'identifian du projet
     * @param idFile L'identifiant du fichier
     * @return La sortie de la commande "git log" formaté en une liste de "idCommit:messageCommit" par ligne
     */
    @RequestMapping(value="/ajax/gitlog", method= {RequestMethod.POST, RequestMethod.GET} )
    public @ResponseBody ResponseEntity log(
            HttpServletRequest req,
            @RequestParam(value ="project") int projectid,
            @RequestParam(value ="idFile") int idFile
    ) {
        Project project= (Project) req.getSession().getAttribute("project");
        File file = filemanager.getFileById(idFile);
        String output=filemanager.gitLog(project,file);
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    /**
     * Grâce au git log on peut maintenant faire le checkout
     * @param req La requête
     * @param projectid L'identifian du projet
     * @param idFile L'identifiant du fichier
     * @param idCommit L'identifiant du commit récupérer par git log
     * @return Le retour de la commande git checkout
     * @see ControllerGit#log(HttpServletRequest, int, int)
     */
    @RequestMapping(value="/ajax/gitcheckout", method= {RequestMethod.POST, RequestMethod.GET} )
    public @ResponseBody ResponseEntity revert(
            HttpServletRequest req,
            @RequestParam(value ="project") int projectid,
            @RequestParam(value ="idFile") int idFile,
            @RequestParam(value ="idCommit") String idCommit
    ) {
        User user=usermanager.GetUserSession();
        Project project= (Project) req.getSession().getAttribute("project");
        File file=filemanager.getFileById(idFile);
        if(!file.boolocking(user)){
            String output="Vous n'avez pas le verrou sur le fichier";
            return ResponseEntity.status(HttpStatus.OK).body(output);
        }
        String output2=filemanager.gitCheckoutOneFile(project,file,idCommit);
        String path = req.getServletContext().getRealPath(localPath);
        String strrep;
        try {
            strrep = filemanager.readLocalFile(path, file); // Lecture du contenu
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Le fichier est introuvable.");
        }
        String res= FileFormatter.GitAndFile(output2, file, strrep, true);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
