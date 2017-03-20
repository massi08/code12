package Controller;

import ViewObjects.Jsons.JsonFormat.FileFormatter;
import metier.FileManager;
import metier.MemberManager;
import metier.ProjectManager;
import metier.UserManager;
import modele.File;
import modele.Member;
import modele.Project;
import modele.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * ControllerFiles class
 */
@Controller
public class ControllerFiles {
    /**
     * Le contrôleur gère ce qui se rapporte aux fichiers
     */
    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager;
    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;
    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;

    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;

    private static String localPath = "/static_website/projects/";
    private Logger logger = Logger.getLogger(ControllerFiles.class);

    /**
     * Appelée lorsqu'un utilisateur souhaite fermer un fichier qu'il a lock
     *
     * @param req
     * @param idfile
     * @return
     */
    @RequestMapping(value = "/Project/ajax/dev/closeFile", method = RequestMethod.GET)
    public ResponseEntity close(
            HttpServletRequest req,
            @RequestParam(value = "idfile") int idfile
    ) {
        try {
            modele.File file = filemanager.getFileById(idfile);
            User user = usermanager.GetUserSession();
            if (!filemanager.unsetlocking(file, user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas propriétaire du fichier.");//S'il est pas prioritaire, renvoie une erreur
            }
            return ResponseEntity.status(HttpStatus.OK).body("Le fichier a été fermé.");
        }catch(Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur dans la fermeture du fichier");
        }
    }

    /**
     * Requête pour supprimer un fichier
     *
     * @param req
     * @param session
     * @param idfile
     * @return
     */
    @RequestMapping(value = "/Project/ajax/dev/deleteFile", method = RequestMethod.GET)
    public ResponseEntity delete(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "idfile") int idfile
    ) {
        try {
            modele.File file = filemanager.getFileById(idfile);
            User user = usermanager.GetUserSession();
            if (!filemanager.unsetlocking(file, user)) {// Vérification de la propriété
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas le droit de détruire le fichier.");
            }
            if (filemanager.deleteLocalFile(req.getServletContext().getRealPath(localPath), file)) {
                filemanager.deleteFile(file); // Suppression dans la hiérarchie et en BDD
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossible de détruire le fichier local.");
            }
            return new ResponseEntity<String>("Le fichier a été détruit.", HttpStatus.OK);
        }catch(Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur dans la destruction du fichier.");
        }
    }

    /**
     * Ouverture d'un fichier
     *
     * @param req
     * @param idfile
     * @return
     */
    @RequestMapping(value = "/Project/ajax/getfile", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity getfile(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "idfile") int idfile,
            @RequestParam(value = "read") String read
    ) {
        try {
            modele.File fichier = filemanager.getFileById(idfile);
            User user = usermanager.GetUserSession();
            if (fichier == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Le fichier n'existe plus.");
            }

            int projectid = fichier.getIdProjet().getIdProject();
            String path = req.getServletContext().getRealPath(localPath);
            String strrep;
            try {
                strrep = filemanager.readLocalFile(path, fichier); // Lecture du contenu
            } catch (FileNotFoundException e) {
                logger.error(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Le fichier est introuvable.");
            } catch (IOException e) {
                logger.error(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur à la lecture du fichier.");
            }
            String role = ((Member) session.getAttribute("member")).getRoleName();
            boolean right = role.equals("REPORTER") || role.equals("OLDMEMBER");
            boolean lock=false ;
            if(read.equals("write")) { // On gère les droits s'il veut l'accès en écriture
                if (fichier.boolocking(user) && !right) {
                    filemanager.setlocking(fichier, user); // L'utilisateur lock le fichier
                    lock = true; //On lui dit qu'il a le droit
                }else{
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Impossible d'ouvrir le fichier en écriture.");
                }
            }else {
                filemanager.unsetlocking(fichier, user);
            }
            strrep= FileFormatter.GitAndFile("", fichier, strrep, lock);
            return new ResponseEntity<String>(strrep, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossible d'accéder au fichier.");
        }
    }


    /**
     * Enregistrement des meta données (que la BDD) d'un fichier, utile lorsqu'on bouge un fichier ou qu'on le renomme
     *
     * @param req
     * @param session
     * @param idfile
     * @param filename
     * @param path
     * @return
     */
    @RequestMapping(value = "/Project/ajax/dev/EntregistreMetas", method = RequestMethod.GET)
    public ResponseEntity saveMetas(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "idfile") int idfile,
            @RequestParam(value = "filename") String filename,
            @RequestParam(value = "path") String path
    ) {
        try {
            modele.File fichier = filemanager.getFileById(idfile);
            User user = usermanager.GetUserSession();
            if (fichier == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Le fichier n'existe plus, rafraichissez le projet.");
            }
            String origpath = req.getServletContext().getRealPath(localPath);
            if (fichier.boolocking(user)) {             // Vérifie qu'il peut toucher au fichier
                if (filemanager.ModifyFile(fichier, path, filename, origpath)) { //Modification du fichier et gestion d'un eventuel conflit
                    return ResponseEntity.status(HttpStatus.OK).body("Le fichier a été renommé.");
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Vous ne pouvez pas renommer le fichier, il y a probablement un conflit.");
        }catch(Exception e){
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossible de renommer ce fichier");
        }
    }


    /**
     * Enregistrement d'un fichier, le fichier peut être nouveau donc on n'enregistre pas avec l'id
     *
     * @param req
     * @param texte
     * @param arborescence
     * @param projectid
     * @param filename
     * @return
     */
    @RequestMapping(value = "/Project/ajax/dev/enregistre", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    ResponseEntity enregistrer(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "texte", defaultValue = "") String texte,
            @RequestParam(value = "arborescence") String arborescence,
            @RequestParam(value = "project") int projectid,
            @RequestParam(value = "filename") String filename
    ) {
        try {
            User user = usermanager.GetUserSession(); // récupération du User courant
            modele.File databasefile = filemanager.getFileByAttributes(projectid, arborescence, filename); //Recupère le fichier et le creer en BSS s'il faut
            Project project = (Project) session.getAttribute("project");
            if ((databasefile != null && user != databasefile.isLocking())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Vous n'avez pas accès au fichier.");
            } // On vérifie qu'il a le droit d'enregistrer
            //Il faut le chemin total
            String path = req.getServletContext().getRealPath(localPath);
            try {// Création du fichier localement
                filemanager.createLocalFile(project, path, arborescence, filename, texte);
            } catch (IOException e) {
                logger.error(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création du fichier.");
            }
            if (databasefile == null)// Sil est créé localement, alors on le rajoute à la bdd s'il est nouveau
                databasefile = filemanager.createFile(user, project, arborescence, filename);
            return new ResponseEntity<>(databasefile.getJSON(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e);
            return new ResponseEntity<>("Impossible d'enregistrer le fichier.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Renvoie d'une erreur ajax lorsqu'un utilisateur n'a pas les droits requis.
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/Project/ajax/error", method = {RequestMethod.POST, RequestMethod.GET})
    ResponseEntity error(
        HttpServletRequest req,
        HttpSession session
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas les droits requis.");
    }

    /**
     * Crée un zip du projet
     * @param req
     * @param session
     * @param response
     * @return le zip du projet créé
     */
    @ResponseBody
    @RequestMapping(value = "/Project/createZIP", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FileSystemResource Telechargement(
            HttpServletRequest req, HttpSession session, HttpServletResponse response
    ) {
        Project project = (Project) session.getAttribute("project");
        List<File> myfiles = filemanager.getFileByProject(project);
        //Il faut le chemin total
        String pathArchive = req.getServletContext().getRealPath(localPath) + "/" + project.getIdProject();
        String pathFichiers = req.getServletContext().getRealPath(localPath);
        String nomArchive = "archive.zip";
        filemanager.ConstructZip(myfiles, pathArchive, pathFichiers, nomArchive);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=archive.zip");
        FileSystemResource myfile = new FileSystemResource(pathArchive + "/" + nomArchive);
        return myfile;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity handleIOException(Exception ex) {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
