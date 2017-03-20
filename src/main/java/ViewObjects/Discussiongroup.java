package ViewObjects;

import modele.Discussion;
import modele.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Discussiongroup {

    private ArrayList<User> name ;
    private int id ;


    public Discussiongroup(int id, User name1){
        this.name =new ArrayList<User>();
        this.name.add(name1);
        this.id=id ;
    }

    /**
     * On regroupe les discussions selon leur id pour que la vue les parcourt facilement.
     * On souhaite alors récupérer les utilisateurs d'une discussion
     * @param discussions
     * @return
     */
    public static HashMap<Integer , Discussiongroup> GenereDiscussionViewGroup(List<Discussion> discussions){
        if(discussions.isEmpty())
            return null ;
        Iterator<Discussion> it=discussions.iterator() ;
        HashMap<Integer, Discussiongroup> map=new HashMap<Integer, Discussiongroup>();
        while(it.hasNext()){
            Discussion disc=it.next();
            if(map.containsKey(disc.getIdDiscussion())){ // On ajoute le nom à la liste de noms
                map.get(disc.getIdDiscussion()).addName(disc.getUser());
            }else{ // On ajoute une nouvelle discussion
                map.put(disc.getIdDiscussion(), new Discussiongroup(disc.getIdDiscussion(), disc.getUser()));
            }
        }
        return map ;
    }

    /**
     * Retounr la lise d'utilisateurs
     * @return
     */
    public ArrayList<User> getName() {
        return this.name;
    }

    /**
     * Initialise la liste d'utilisateurs
     * @param name
     */
    public void setName(ArrayList<User> name) {
        this.name = name;
    }

    /**
     * Ajoute un utilisateur dans la liste
     * @param name
     */
    public void addName(User name){
        this.name.add(name);
    }

    /**
     * Retourn l'id de la discussion
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Initilise l'id de la discussion
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
}
