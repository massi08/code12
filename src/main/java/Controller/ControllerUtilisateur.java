package Controller;

import metier.UserManager;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class ControllerUtilisateur {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    @Qualifier(value = "usermanager")
    private UserManager usermanager;

    /**
     * TODO
     * @param req
     * @param logout
     * @return
     */
    @RequestMapping(value = {"/", "/index", ""}, method = RequestMethod.GET)
    public ModelAndView Accueil(
            HttpServletRequest req,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("index");
        return model;
    }

    /**
     *
     * @param model
     * @param map
     * @param b
     * @return Page d'inscription
     */
    @RequestMapping(value = {"/Inscription"}, method = {RequestMethod.GET})
    public String viewLogin( Map<String, Object> model,
            ModelMap map, @RequestParam(value = "pseudo", required = false) String b)
    {
        User user = new User();
        model.put("inscriptionForm", user);
        //Pour traiter le cas où le pseudo rentré n'est pas unique
        if(b == null)
        {
            map.addAttribute("pseudoOk", null);
        }
        else
        {
            map.addAttribute("pseudoOk", b);
        }
        return "inscription";
    }


    /**
     * Traite le formulaire d'inscription
     * @param inscriptionForm
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/Inscription", method = RequestMethod.POST)

    public String doLogin(@Valid @ModelAttribute("inscriptionForm") User inscriptionForm,

                          BindingResult result, Map<String, Object> model) {

        if (result.hasErrors()) {
            return "inscription";
        }

        User us = usermanager.createUser(inscriptionForm.getPseudo(), inscriptionForm.getFirstName(), inscriptionForm.getlastName(), inscriptionForm.getPassword(), inscriptionForm.getEmail());
        //Le pseudo de l'utilisateur existe déjà

        if (us == null) {

            return "redirect:Inscription?pseudo="+inscriptionForm.getPseudo();
            }

        return "redirect:index";
    }


    /**
     *
     * @param req
     * @param map
     * @return Page Mon Compte
     */
    @RequestMapping(value = "/ManageAccount", method = RequestMethod.GET)
    public String viewAccount(HttpServletRequest req, ModelMap map) {
        User user = usermanager.GetUserSession();
        map.addAttribute("user", user);
        return "account";
    }

    /**
     * Modifie le nom d'un projet
     * @param req
     * @param session
     * @param pseudo
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return Page des projets de l'utilisateur
     */
    @RequestMapping(value = "/ManageAccount", method = RequestMethod.POST)
    public String modifyNameProject(HttpServletRequest req,
                                    HttpSession session,
                                    @RequestParam(value = "pseudo") String pseudo,
                                    @RequestParam(value = "old_password") String oldPassword,
                                    @RequestParam(value = "password") String newPassword,
                                    @RequestParam(value = "confirm_password") String confirmPassword) {

        User user = usermanager.GetUserSession();
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        if (!newPassword.equals(confirmPassword) || !encoder.matches(oldPassword, user.getPassword()))
            return "account";

        String password = encoder.encode(newPassword);
        usermanager.manageAccount(user, password);
        return "redirect:Home";
    }

}
