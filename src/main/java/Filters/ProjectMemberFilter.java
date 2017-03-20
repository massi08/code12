package Filters;

import metier.UserManager;
import modele.Member;
import modele.Project;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * projectMemberFilter class
 */
public class ProjectMemberFilter implements Filter {
    private UserManager usermanager;

    /**
     * Initialisation le bean usermanager
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.usermanager = (UserManager) ctx.getBean("usermanager");
    }

    /**
     *
     * Initialise le projet d'un membre dans sa session. Tri également son accès au projet
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        usermanager.GetUserSession().getPseudo();
        HttpSession session = req.getSession();
        Project sessionproject = (Project) session.getAttribute("project");
        String id = req.getParameter("projectid");
        // On peut accéder à l'url sans le paramètre si le projet est déjà dans la session
        if (id == null && sessionproject == null) {
            ((HttpServletResponse) servletResponse).sendRedirect("/Home");
            return;
        }

        Integer idprojet;
        if (id != null) {
            try {
                idprojet = Integer.parseInt(id);
            }catch(NumberFormatException e ){
                ((HttpServletResponse) servletResponse).sendRedirect("/Home");
                return ;
            }
        } else {
            idprojet = sessionproject.getIdProject();
        }
        if (sessionproject == null || sessionproject.getIdProject() != idprojet) {
            List<Object[]> Result = usermanager.getMembers(idprojet);
            if (Result.isEmpty() || Result.get(0).length == 0) { // S'il fait pas parti du projet il se fait virer
                ((HttpServletResponse) servletResponse).sendRedirect("/Home");
                return;
            }
            Object objets[] = Result.get(0);
            if(((Member)objets[1]).getRoleName().equals("OLDMEMBER")){ // S'il ne fait plus parti du projet
                ((HttpServletResponse) servletResponse).sendRedirect("/Home");
                return;
            }
            session.setAttribute("project", (Project)objets[0]); // economie de recherche en BDD
            session.setAttribute("member", (Member)objets[1]);
        }
        //Ici il a le droit d'accès au projet
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Destruction du filtre, ne fais rien
     */
    @Override
    public void destroy() {
        // Empty
    }
}
