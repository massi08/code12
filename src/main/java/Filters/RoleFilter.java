package Filters;

import modele.Member;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.log4j.Logger;

@WebFilter(filterName = "RoleFilter")
public class RoleFilter implements Filter {

    private static Logger logger = Logger.getLogger("RoleFilter");

    /**
     * Destruction du filtre, on ne fait rien
     */
    public void destroy() {
    }

    /**
     * Vérifie que le membre de la requête qui envoie la requête ajax n'est pas un reporter
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp ;
        try {
            HttpSession session = request.getSession();
            Member member = (Member) session.getAttribute("member");
            if(member.getRoleName().equals("REPORTER"))
                response.sendRedirect("/Project/ajax/error");
        }catch(Exception error){
            logger.error(error);
            response.sendRedirect("/Project/ajax/error");
        }
        chain.doFilter(req, resp);
    }

    /**
     * Initialisation du filtre, on ne fait rien
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {
    }

}
