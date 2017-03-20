package dao;



/**
 * Created by audrey on 20/10/16.
 */
//=======

//>>>>>>> other
public class TicketDAOTest {

    /*@Autowired
    EntityManager em;
    @Autowired
    TicketDAO ticketDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    LangageDAO langageDAO;
    @Autowired
    UserDAO userDAO;


    public TicketDAOTest() {
        //em = Persistence.createEntityManagerFactory("testAudrey").createEntityManager();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {

    }

    @Test
    public void create1() throws Exception {

    }

    @Test
    public void setEtat() throws Exception {

    }

    @Test
    public void setPriority() throws Exception {

    }

    @Test
    public void getById() throws Exception {
        //Création d'un langage (name unique)
        Langage langage= langageDAO.getByName("C++");
        if(langage == null)
        {
            em.getTransaction().begin();
            langageDAO.create("C#");
            em.getTransaction().commit();
        }


        //Création d'un project
        em.getTransaction().begin();
        Project project = projectDAO.create("MonProjet", "SonPath", langage);
        em.getTransaction().commit();

        //Création d'une date
        Date date = new Date();
        date.getTime();

        //Création d'un user (pseudo unique)
        User user= userDAO.getByName("pseudo");
        if(user == null)
        {
            em.getTransaction().begin();
            user=userDAO.create("pseudo", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un ticket avec le langage, le project, la date et le user
        em.getTransaction().begin();
        Ticket ticket1= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        em.getTransaction().commit();
        int id=ticket1.getNumber();
        Ticket ticket2=ticketDAO.getById(id);

        //getById(idticket de ticket1) renvoie ticket1
        assertTrue("getById(), ticket1(idTicket) != ticketDAO.getById(idTicket)", ticket1.equals(ticket2));

    }

    @Test
    public void getAll() throws Exception {
        //Création d'un langage (name unique)
        Langage langage= langageDAO.getByName("C++");
        if(langage == null)
        {
            em.getTransaction().begin();
            langageDAO.create("C#");
            em.getTransaction().commit();
        }


        //Création d'un project
        em.getTransaction().begin();
        Project project = projectDAO.create("MonProjet", "SonPath", langage);
        Project project2 = projectDAO.create("MonProjet2", "SonPath", langage);
        em.getTransaction().commit();

        //Création d'une date
        Date date = new Date();
        date.getTime();

        //Création d'un user (pseudo unique)
        User user= userDAO.getByName("pseudo");
        if(user == null)
        {
            em.getTransaction().begin();
            user=userDAO.create("pseudo", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un user2 (pseudo unique)
        User user2= userDAO.getByName("pseudo2");
        if(user2 == null)
        {
            em.getTransaction().begin();
            user2=userDAO.create("pseudo2", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un ticket avec le langage, le project, la date et le user
        em.getTransaction().begin();
        Ticket ticket1= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        Ticket ticket2= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, "Bug");
        Ticket ticket3= ticketDAO.create(project2, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        em.getTransaction().commit();

        List<Ticket> myList = ticketDAO.getAll();

        assertTrue("Echec getAll TicketDAO list contents", myList.contains(ticket1));
        assertTrue("Echec getAll TicketDAOlist contents", myList.contains(ticket2));
        assertTrue("Echec getAll TicketDAO list contents", myList.contains(ticket3));

    }

    @Test
    public void getAllByProject() throws Exception {
        //Création d'un langage (name unique)
        Langage langage= langageDAO.getByName("C++");
        if(langage == null)
        {
            em.getTransaction().begin();
            langageDAO.create("C#");
            em.getTransaction().commit();
        }


        //Création d'un project
        em.getTransaction().begin();
        Project project = projectDAO.create("MonProjet", "SonPath", langage);
        Project project2 = projectDAO.create("MonProjet2", "SonPath", langage);
        em.getTransaction().commit();

        //Création d'une date
        Date date = new Date();
        date.getTime();

        //Création d'un user (pseudo unique)
        User user= userDAO.getByName("pseudo");
        if(user == null)
        {
            em.getTransaction().begin();
            user=userDAO.create("pseudo", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un user2 (pseudo unique)
        User user2= userDAO.getByName("pseudo2");
        if(user2 == null)
        {
            em.getTransaction().begin();
            user2=userDAO.create("pseudo2", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un ticket avec le langage, le project, la date et le user
        em.getTransaction().begin();
        //tickets du project "project"
        Ticket ticket1= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        Ticket ticket2= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, "Bug");
        Ticket ticket3= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, "Bug");

        //ticket du project "project2" (pour tester qu'il ne se retrouve dans myList
        Ticket ticket4= ticketDAO.create(project2, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        em.getTransaction().commit();
        List<Ticket> myList = ticketDAO.getAllByProject(project);

        assertTrue("getAllByProject(project), retourne une liste de bonne taille", myList.size()==3);
        assertTrue("getAllByProject(project), contient les bons éléments", myList.contains(ticket1));
        assertTrue("getAllByProject(project), contient les bons éléments", myList.contains(ticket2));
        assertTrue("getAllByProject(project), contient les bons éléments", myList.contains(ticket3));
    }

    @Test
    public void getAllByAuthor() throws Exception {
        //Création d'un langage (name unique)
        Langage langage= langageDAO.getByName("C++");
        if(langage == null)
        {
            em.getTransaction().begin();
            langageDAO.create("C#");
            em.getTransaction().commit();
        }


        //Création d'un project
        em.getTransaction().begin();
        Project project = projectDAO.create("MonProjet", "SonPath", langage);
        Project project2 = projectDAO.create("MonProjet2", "SonPath", langage);
        em.getTransaction().commit();

        //Création d'une date
        Date date = new Date();
        date.getTime();

        //Création d'un user (pseudo unique)
        User user= userDAO.getByName("pseudo");
        if(user == null)
        {
            em.getTransaction().begin();
            user=userDAO.create("pseudo", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un user2 (pseudo unique)
        User user2= userDAO.getByName("pseudo2");
        if(user2 == null)
        {
            em.getTransaction().begin();
            user2=userDAO.create("pseudo2", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un ticket avec le langage, le project, la date et le user
        em.getTransaction().begin();
        //tickets dont l'auteur est "user"
        Ticket ticket1= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");
        Ticket ticket2= ticketDAO.create(project2, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, "Bug");

        //tickets dont l'auteur est "user2"
        Ticket ticket3= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, "Bug");
        Ticket ticket4= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, "Bug");

        em.getTransaction().commit();
        List<Ticket> myList = ticketDAO.getAllByAuthor(user);

        assertTrue("getAllByAuthor(user), retourne une liste de bonne taille", myList.size()>=2);
        assertTrue("getAllByAuthor(user), contient les bons éléments", myList.contains(ticket1));
        assertTrue("getAllByAuthor(user), contient les bons éléments", myList.contains(ticket2));
        assertTrue("getAllByAuthor(user), contient les bons éléments", !myList.contains(ticket3));
        assertTrue("getAllByAuthor(user), contient les bons éléments", !myList.contains(ticket4));

    }

    @Test
    public void getAllBySupervisor() throws Exception {

        //Création d'un langage (name unique)
        Langage langage= langageDAO.getByName("C++");
        if(langage == null)
        {
            em.getTransaction().begin();
            langageDAO.create("C#");
            em.getTransaction().commit();
        }


        //Création d'un project
        em.getTransaction().begin();
        Project project = projectDAO.create("MonProjet", "SonPath", langage);
        Project project2 = projectDAO.create("MonProjet2", "SonPath", langage);
        em.getTransaction().commit();

        //Création d'une date
        Date date = new Date();
        date.getTime();

        //Création d'un user (pseudo unique)
        User user= userDAO.getByName("pseudo");
        if(user == null)
        {
            em.getTransaction().begin();
            user=userDAO.create("pseudo", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un user2 (pseudo unique)
        User user2= userDAO.getByName("pseudo2");
        if(user2 == null)
        {
            em.getTransaction().begin();
            user2=userDAO.create("pseudo2", "firstname ", "lastname", "mdp", "e-mail");
            em.getTransaction().commit();
        }

        //Création d'un ticket avec le langage, le project, la date et le user
        em.getTransaction().begin();
        //tickets du user "user"
        Ticket ticket1= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, user,"Bug");
        Ticket ticket2= ticketDAO.create(project2, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, user,"Bug");

        //tickets du user "user2"
        Ticket ticket3= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user2, user2,"Bug");
        Ticket ticket4= ticketDAO.create(project, "Mon titre", "Mon contenu", date, "NonTraite", "Mineure", user, user2,"Bug");
        em.getTransaction().commit();

        List<Ticket> myList = ticketDAO.getAllBySupervisor(user);

        assertTrue("getAllByAuthor(user), retourne une liste de bonne taille", myList.size()>=2);
        assertTrue("getAllByAuthor(user), contient les bons éléments", myList.contains(ticket1));
        assertTrue("getAllByAuthor(user), contient les bons éléments", myList.contains(ticket2));
        assertTrue("getAllByAuthor(user), contient les bons éléments", !myList.contains(ticket3));
        assertTrue("getAllByAuthor(user), contient les bons éléments", !myList.contains(ticket4));

    }*/


}