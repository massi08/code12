package Controller;

import metier.FileManager;
import metier.MemberManager;
import metier.ProjectManager;
import metier.UserManager;
import modele.Project;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ControllerModifyProject class
 */
@Controller
public class ControllerModifyProject {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;

    @Autowired
    @Qualifier(value = "projectmanager")
    private ProjectManager projectmanager;

    @Autowired
    @Qualifier(value = "membermanager")
    private MemberManager membermanager;

    @Autowired
    @Qualifier(value = "filemanager")
    private FileManager filemanager;

    private Logger logger = Logger.getLogger(ControllerModifyProject.class);

    /**
     * TODO CLARA
     *
     * @param req
     * @param map
     * @param session
     * @param error
     * @return
     */
    @RequestMapping(value = {"/Project/modifyProject"}, method = {RequestMethod.GET})
    public String getModifyProject(HttpServletRequest req,
                                   ModelMap map,
                                   HttpSession session,
                                   @RequestParam(value = "error", required = false) String error) {
        Project p = (Project) session.getAttribute("project");
        int idProject = p.getIdProject();

        Project myProject = filemanager.getProjectById(idProject);
        if (myProject != null) {
            if (error != null) {
                map.addAttribute("error", "Le nom du projet doit contenir au moins 4 caractères.");
            }
            map.addAttribute("nameProject", myProject.getName());
            map.addAttribute("languageProject", myProject.getLanguage().getName());
            map.addAttribute("gitProject", myProject.getGit());
        }
        return "modify_project";
    }

    /**
     * TODO CLARA
     *
     * @param req
     * @param session
     * @param newName
     * @return
     */
    @RequestMapping(value = "/Project/modifyProjectDone", method = RequestMethod.POST)
    public String modifyNameProject(HttpServletRequest req,
                                    HttpSession session,
                                    @RequestParam(value = "nomProject") String newName
    ) {
        Project p = (Project) session.getAttribute("project");
        int idProject = p.getIdProject();
        Project myProject = filemanager.getProjectById(idProject);
        if (myProject != null) {
            String oldName = myProject.getName();
            boolean succes = false;

            if (oldName != newName && newName.length() >= 4) {
                //Modification en local :
                String path = req.getServletContext().getRealPath("/static_website/projects/");
                try {

                    succes = projectmanager.modifyNameLocalProject(p, path, oldName, newName);
                } catch (Exception e) {
                    logger.error(e);
                }
                if (succes) {
                    //modification dans la base:
                    projectmanager.modifyNameProject(myProject, newName);
                    return "redirect:modifyProject";
                }
            }


        }
        return "redirect:modifyProject?error=1";
    }
}