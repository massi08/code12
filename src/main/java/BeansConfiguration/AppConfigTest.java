package BeansConfiguration;

import dao.*;
import metier.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


@Configuration
public class AppConfigTest {


    @Bean
    @Scope("singleton")
    public FileManager filemanagerTest() {
        return new FileManager(filedaoTest(), entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public TicketManager ticketManagerTest() {
        return new TicketManager(ticketDAOTest(), commentarydaoTest(), userDAOTest(), entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public ProjectManager projectManagerTest() {

        return new ProjectManager(projectDAOTest(), langageDAOTest(), memberDAOTest(), roleDAOTest(),entitymanager2());
    }
    @Bean
    @Scope("singleton")
    public MemberManager memberManagerTest() {
        return new MemberManager(memberDAOTest(), roleDAOTest(), entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public UserManager userManagerTest() {
        return new UserManager(entitymanager2(), memberDAOTest(), userDAOTest());

    }


    @Bean
    @Scope("singleton")
    public NewsManager newsManagerTest(){
        return new NewsManager(entitymanager2());
    }


    @Bean
    @Scope("singleton")
    public MessageManager messageManager() {
        return new MessageManager(discussiondaoTest(), messageDAOTest(), userManagerTest(), entitymanager2());
    }


    @Bean
    @Scope("singleton")
    public EntityManagerFactory EntityManagerfactoryTest(){ return Persistence.createEntityManagerFactory("test") ;}

    @Bean
    @Qualifier(value = "entityManagerTest")
    @Scope("singleton")
    public EntityManager entitymanager2() {
        return EntityManagerfactoryTest().createEntityManager();
    }


    @Bean(name = "dataSource2")
    @Scope("singleton")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");
        return driverManagerDataSource;
    }

    @Bean
    @Scope("singleton")
    public CommentaryDAO commentarydaoTest() {
        return new CommentaryDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public DiscussionDAO discussiondaoTest() {
        return new DiscussionDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public FileDAO filedaoTest() {
        return new FileDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public LangageDAO langageDAOTest() {
        return new LangageDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public MemberDAO memberDAOTest() {
        return new MemberDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public MessageDAO messageDAOTest() {
        return new MessageDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public ProjectDAO projectDAOTest() {
        return new ProjectDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public RoleDAO roleDAOTest() {
        return new RoleDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public TicketDAO ticketDAOTest() {
        return new TicketDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public UserDAO userDAOTest() {
        return new UserDAO(entitymanager2());
    }

    @Bean
    @Scope("singleton")
    public NewsDAO newsDAOTest(){return new NewsDAO(entitymanager2());}


}
