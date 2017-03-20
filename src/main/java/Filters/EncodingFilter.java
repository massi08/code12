package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {
    private static String encoding = "utf-8";

    /**
     * Destruction du filtre
     */
    public void destroy() {
    }

    /**
     * encode les caractères en utf8
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(encoding);
        chain.doFilter(req, resp);
    }

    /**
     * Initialisation du filtre
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {

    }

}
