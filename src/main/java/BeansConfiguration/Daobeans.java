/*package BeansConfiguration;

import dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages={"dao"})
public class Daobeans {

    @Bean
    @Scope("singleton")
    public CommentaryDAO commentarydao(){
        return new CommentaryDAO();
    }

    @Bean
    @Scope("singleton")
    public DiscussionDAO discussiondao(){
        return new DiscussionDAO();
    }

    @Bean
    @Scope("singleton")
    public FileDAO filedao(){
        return new FileDAO();
    }

    @Bean
    @Scope("singleton")
    public LangageDAO langageDAO(){
        return new LangageDAO();
    }

    @Bean
    @Scope("singleton")
    public MemberDAO memberDAO(){
        return new MemberDAO();
    }

    @Bean
    @Scope("singleton")
    public MessageDAO messageDAO(){
        return new MessageDAO();
    }

    @Bean
    @Scope("singleton")
    public ProjectDAO projectDAO(){
        return new ProjectDAO();
    }

    @Bean
    @Scope("singleton")
    public RoleDAO roleDAO(){
        return new RoleDAO();
    }

    @Bean
    @Scope("singleton")
    public TicketDAO ticketDAO(){
        return new TicketDAO();
    }

    @Bean
    @Scope("singleton")
    public UserDAO userDAO(){
        return new UserDAO();
    }

    @Bean
    @Scope("singleton")
    public NewsDAO newsDAO(){return new NewsDAO();}

}
*/