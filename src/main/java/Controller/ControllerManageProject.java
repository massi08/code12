package Controller;


import ViewObjects.Discussiongroup;
import ViewObjects.Jsons.JsonFormat.FileFormatter;
import metier.*;
import modele.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : code12
 * Package : Controller
 * Created by adilon on 21/10/16.
 * Copyright (c). All rights reserved.
 */
@Controller
public class ControllerManageProject {

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;
    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager;

    @Autowired
    @Qualifier(value = "newsmanager")
    private NewsManager newsmanager;

    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;

    @Autowired
    @Qualifier(value = "messagemanager")
    private MessageManager messagemanager;
    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;


    private static String localPath = "/static_website/projects/";
    private Logger logger = Logger.getLogger(ControllerManageProject.class);

    /**
     * @param req
     * @param map
     * @return Page des projets de l'utilisateur
     */
    @RequestMapping(value = "/Home", method = RequestMethod.GET)

    public String Manage(HttpServletRequest req, ModelMap map) {
        User user = usermanager.GetUserSession();


        List<Project> projects = membermanager.getProjectsByUser(user);
        Map<Project, Role> roleOfProject = new HashMap<>();
        for (Project p : projects)
            roleOfProject.put(p, projectmanager.getMember(user, p).getRole());
        map.addAttribute("projects", projects);
        map.addAttribute("roleOfProject", roleOfProject);
        return "manage_project";
    }

    /**
     * @param req
     * @param session
     * @param id
     * @return Page de code
     */
    @RequestMapping(value = "Project/code", method = RequestMethod.GET)
    public ModelAndView getfileofprojects(
            HttpServletRequest req,
            HttpSession session,
            @RequestParam(value = "projectid", required = false) String id
    ) {

        User user = usermanager.GetUserSession();
        Project project = (Project) session.getAttribute("project");
        Role roleUser = membermanager.getRoleUserbyProject(user, project);
        if (project != null) {
            String res = FileFormatter.JsonPackageTree(filemanager.getProjectFiles(project, user)); // récupération du jsontree
            HashMap<Integer, Discussiongroup> discussions = messagemanager.getDiscussions(usermanager.GetUserSession(), project);
            List<Member> members = usermanager.getMembersOfProject(project, user);
            ModelAndView model = new ModelAndView("code");

            // GESTION DES NEWS
            List<News> listNews = newsmanager.getAllNewsByProject(project);
            ArrayList<String> listStringNews = new ArrayList<>();
            //On affiche seulement 15 news au maximum !
            if (listNews.size() <= 15) {
                for (News n : listNews) {
                    String news = newsmanager.getStringByNews(n);
                    listStringNews.add(news);
                }
            } else {
                for (int i = 0; i < 15; i++) {
                    String news = newsmanager.getStringByNews(listNews.get(i));
                    listStringNews.add(news);
                }
            }

            model.addObject("jstree", res);
            model.addObject("id", project.getIdProject());
            model.addObject("listNews", listStringNews);
            model.addObject("discussions", discussions);
            model.addObject("members", members);
            model.addObject("userPseudo", user.getPseudo());
            model.addObject("userRole", roleUser);
            model.addObject("userId", user.getIdUser());
            model.addObject("languageId", project.getLanguage().getIdLanguage());
            model.addObject("git", String.valueOf(project.getGit()));
            return model;
        }
        return new ModelAndView("redirect:/Home");
    }


    /**
     * @param model
     * @param map
     * @return Page de création de projet
     */
    @RequestMapping(value = "/createproject", method = RequestMethod.GET)
    public String createProject(Map<String, Object> model, ModelMap map) {

        List<Langage> myLangage = projectmanager.getAllLangages();
        map.addAttribute("monlangage", myLangage);
        Project project = new Project("", "", false, myLangage.get(0));
        model.put("projectForm", project);
        return "create_project";
    }

    /**
     * Crée un projet, à partir du formulaire de création de projet
     *
     * @param projectForm
     * @param model
     * @param map
     * @param req
     * @param result
     * @param file
     * @return Page des projets de l'utilisateur
     */
    @RequestMapping(value = "/createproject-result", method = RequestMethod.POST)
    public String createProjectResult(@Valid @ModelAttribute("projectForm") Project projectForm, Map<String, Object> model,
                                      ModelMap map, HttpServletRequest req, BindingResult result,
                                      @RequestParam("file") MultipartFile file) {

        if (result.hasErrors()) {
            List<Langage> myLangage = projectmanager.getAllLangages();
            map.addAttribute("monlangage", myLangage);
            return "create_project";
        }

        User master = usermanager.GetUserSession();
        Langage language = projectmanager.getLangageByName(projectForm.getLanguage().getName());
        String name = projectForm.getName();
        boolean git = projectForm.getGit();
        String pathlocal = req.getServletContext().getRealPath("/static_website/projects/");
        String path = "/";

        Project project = projectmanager.createProject(name, path, git, language, master);

        try {// Création du  repertoire de Projet
            projectmanager.createLocalProject(project, pathlocal, name);
            if (git)
                filemanager.gitInit(project.getIdProject());
            //Si l'utilisateur a fournit un zip, on ajoute les fichiers qu'il contient au projet
            if (!file.isEmpty() && "application/zip".equals(file.getContentType())) {
                try {
                    byte[] bytes = file.getBytes();
                    String myname = "monzip.zip";
                    // Creating the directory to store file
                    String rootPath = req.getServletContext().getRealPath(localPath) + File.separator + project.getIdProject()
                            + File.separator + project.getName();
                    File dir = new File(rootPath);
                    if (!dir.exists()) dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(rootPath + myname);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    File serverFolfer = new File(rootPath);
                    filemanager.unzip(serverFile, serverFolfer, master, project, rootPath);

                } catch (Exception e) {
                    logger.error("Fichier vide ou pas un metier.ZIP");
                }
            } else {
                logger.error("Fichier vide ou pas un metier.ZIP");
            }


        } catch (Exception e) {
            logger.error(e);
        }

        return "redirect:/Home";
    }

    /**
     * TODO
     *
     * @param req
     * @param name
     * @return
     */
    @RequestMapping(value = "enregistrep", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity enregistrerp(
            HttpServletRequest req,
            @RequestParam(value = "name") String name

    ) {

        String path = req.getServletContext().getRealPath("/static_website/");
        File repertoire = new File(path + "/" + name);
        boolean iscreate = repertoire.mkdirs();
        if (iscreate) return new ResponseEntity<String>(HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * Supprime un projet
     *
     * @param id
     * @return Page des projets de l'utilisateur
     */
    @RequestMapping(value = "/deleteproject-result", method = RequestMethod.GET)
    public String deleteProject(HttpServletRequest req,
                                @RequestParam(value = "project") int id) {

        Project project = projectmanager.getProject(id);

        User user = usermanager.GetUserSession();
        Role role = membermanager.getRoleUserbyProject(user, project);
        List<Member> members = projectmanager.getAllMembersByProject(project);
        List<News> news = newsmanager.getAllNewsByProject(project);
        List<modele.File> files = filemanager.getFileByProject(project);
        String path = req.getServletContext().getRealPath("/static_website/projects/");

        if ("CHEF".equals(role.getNom())) {

            membermanager.deletteProject(members, news, files, project);
            filemanager.deleteLocalFiles(project, files);
            projectmanager.deleteLocal(path, project);

        }
        return "redirect:/Home";
    }

}
