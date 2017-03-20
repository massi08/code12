package metier;

import dao.MemberDAO;
import dao.UserDAO;
import metier.UserDetails.MyUserDetails;
import modele.Member;
import modele.Project;
import modele.User;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.util.List;

public class UserManager {


    private UserDAO userDAO;
    private MemberDAO memberDAO;
    private EntityManager em;

    /**
     * Constructeur de UsermManager
     * @param em
     * @param memberDAO
     * @param userDAO
     */
    public UserManager(EntityManager em, MemberDAO memberDAO, UserDAO userDAO) {
        this.em = em;
        this.memberDAO = memberDAO;
        this.userDAO = userDAO;
    }

    /**
     *
     * @param pseudo
     * @return l'user ayant pour pseudo pseudo, null s''il n'existe pas dans la BD
     */
    public User getByPseudo(String pseudo) {
        return userDAO.getByName(pseudo);
    }


    /**
     *
     * @param id
     * @return l'user en fonction de son ID
     */
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    /**
     *
     * @return
     */
    public User GetUserSession() {
        User user = (
                (MyUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        )
                .getUser();
        em.getTransaction().begin();
        User user2 = em.merge(user); // Il faut reattacher l'user
        em.getTransaction().commit();
        return user2;
    }

    /**
     *
     * @param projectid
     * @return les membres du projet dont l'id est "projectid"
     */
    public List<Object[]> getMembers(int projectid) {
        User user = this.GetUserSession();
        em.getTransaction().begin();
        List<Object[]> objs = memberDAO.getMemberFromProjectAndUser(projectid, user);
        em.getTransaction().commit();
        return objs;
    }

    /**
     *
     * @param project
     * @param user
     * @return tous les membre d'un projet sauf l'user courant
     */
    public List<Member> getMembersOfProject(Project project, User user){
        return memberDAO.getAllMembersByProjectExceptOne(project, user);
    }

    /**
     * Crée un user dans la BD
     * @param pseudo_
     * @param firstName_
     * @param lastName_
     * @param password_
     * @param email_
     * @return l'user créée
     */
    public User createUser(String pseudo_, String firstName_, String lastName_,
                           String password_, String email_)

    {
        em.getTransaction().begin();
        User user = userDAO.create(pseudo_, firstName_, lastName_, password_, email_);
        em.getTransaction().commit();
        return user;
    }

    /**
     * Modifie un user dans la BD
     * @param user
     * @param new_password_
     */
    public void manageAccount(User user,
                              String new_password_)

    {
        em.getTransaction().begin();
        userDAO.manageAccount(user, new_password_);
        em.getTransaction().commit();
    }

    /**
     *
     * @return tous les Users de la base de données.
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

}
