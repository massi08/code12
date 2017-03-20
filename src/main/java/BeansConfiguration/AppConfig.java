package BeansConfiguration;

import ViewObjects.Jsons.JsonFormat.ChatFormatter;

import dao.*;

import metier.*;

import metier.UserDetails.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.ApplicationContext;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Scope;

import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.persistence.EntityManager;

import javax.persistence.EntityManagerFactory;

import javax.persistence.Persistence;

@Configuration

public class AppConfig {

    @Autowired
    ApplicationContext ctx;

    @Bean
    @Scope("singleton")
    public EntityManagerFactory EntityManagerfactory(){ return Persistence.createEntityManagerFactory("code12") ;}

    @Bean
    @Qualifier(value = "filemanager")
    @Scope("singleton")
    public FileManager filemanager() {
        return new FileManager(filedao(), entitymanager());
    }

    @Bean
    @Qualifier(value = "ticketmanager")
    @Scope("singleton")
    public TicketManager ticketManager() {

        return new TicketManager(ticketDAO(), commentarydao(), userDAO(), entitymanager());

    }

    @Bean
    @Qualifier(value = "projectmanager")
    @Scope("singleton")
    public ProjectManager projectManager() {

        return new ProjectManager(projectDAO(), langageDAO(), memberDAO(), roleDAO(), entitymanager());
    }

    @Bean
    @Qualifier(value = "membermanager")
    @Scope("singleton")
    public MemberManager memberManager() {
        return new MemberManager(memberDAO(), roleDAO(), entitymanager());
    }

    @Bean(name = "usermanager")
    @Qualifier(value = "usermanager")
    @Scope("singleton")
    public UserManager userManager() {
        return new UserManager(entitymanager(), memberDAO(), userDAO());
    }

    @Bean
    @Qualifier(value = "messagemanager")
    @Scope("singleton")
    public MessageManager chatManager() {
        return new MessageManager(discussiondao(), messageDAO(), userManager(), entitymanager());
    }

    @Bean
    @Qualifier(value = "newsmanager")
    @Scope("singleton")
    public NewsManager newsManager(){
        return new NewsManager(entitymanager());
    }

    @Bean
    @Qualifier(value = "entityManager")
    @Scope("singleton")
    public CompilerManager compilermanager() {
        return new CompilerManager();
    }

    @Bean
    @Qualifier(value = "entityManager")

    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)

    public EntityManager entitymanager() {

        return EntityManagerfactory().createEntityManager();

    }

    @Bean
    @Scope("singleton")

    public BCryptPasswordEncoder encoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);
        return encoder;
    }

    @Bean(name = "MyUserDetailsService")

    @Scope("singleton")
    public MyUserDetailsService MyUserDetailsService() {
        return new MyUserDetailsService(userDAO());
    }

    @Bean(name = "dataSource")
    @Scope("singleton")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/code12");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");
        return driverManagerDataSource;
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler
    savedRequestAwareAuthenticationSuccessHandler() {

        SavedRequestAwareAuthenticationSuccessHandler auth
                = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setDefaultTargetUrl("/Home");
        return auth;
    }


    @Bean
    @Scope("singleton")
    public CommentaryDAO commentarydao() {
        return new CommentaryDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public DiscussionDAO discussiondao() {
        return new DiscussionDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public FileDAO filedao() {
        return new FileDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public LangageDAO langageDAO() {
        return new LangageDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public MemberDAO memberDAO() {
        return new MemberDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public MessageDAO messageDAO() {
        return new MessageDAO(entitymanager());
    }

    @Bean
    @Scope("prototype")
    public ProjectDAO projectDAO() {
        return new ProjectDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public RoleDAO roleDAO() {
        return new RoleDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public TicketDAO ticketDAO() {
        return new TicketDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public UserDAO userDAO() {
        return new UserDAO(entitymanager());
    }

    @Bean
    @Scope("singleton")
    public NewsDAO newsDAO(){return new NewsDAO(entitymanager());}


    @Bean
    @Qualifier(value = "entityManagerMessages")
    @Scope(value = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public EntityManager entitymanagerMessages() {
        return EntityManagerfactory().createEntityManager();
    }

    @Bean(name = "messagemanagerMessages")
    @Qualifier(value = "messagemanagerMessages")
    @Scope("singleton")
    public MessageManager chatManagerMessages() {
        return new MessageManager(discussiondaoMessages(), messageDAOMessages(), userManagerMessages(), entitymanagerMessages());
    }

    @Bean(name = "usermanagerMessages")
    @Qualifier(value = "usermanagerMessages")
    @Scope("singleton")
    public UserManager userManagerMessages() {
        return new UserManager(entitymanagerMessages(), memberDAOMessages(), userDAOMessages());
    }

    @Bean
    @Scope("singleton")
    public UserDAO userDAOMessages() {
        return new UserDAO(entitymanagerMessages());
    }

    @Bean
    @Scope("singleton")
    public MemberDAO memberDAOMessages() {
        return new MemberDAO(entitymanagerMessages());
    }

    @Bean
    @Scope("singleton")
    public MessageDAO messageDAOMessages() {
        return new MessageDAO(entitymanagerMessages());
    }

    @Bean
    @Scope("singleton")
    public DiscussionDAO discussiondaoMessages() {

        return new DiscussionDAO(entitymanagerMessages());

    }

}