package ViewObjects.Jsons.jstree;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JSONserialize;
import modele.File;
import modele.User;

import java.util.*;

public class Packagetree implements JSONserialize {
    private HashSet<Packagetree> children;
    private String name;
    private String type;
    private boolean locking;
    private int id;

    /**
     * constructor
     * @param name
     */
    public Packagetree(String name) {
        children = new HashSet<Packagetree>();
        this.name = name;
        this.id = -1;
        this.type = "package";
        this.setLocking(true);
    }

    /**
     * JSON format of object
     * @return
     */
    public String getJSON() {
        Datajstree datatree = new Datajstree(this.id, this.isLocking());
        String json = "{";
        if (this.type.equals("file"))
            json += JSONdata.addAttribute("id", "file-" + this.getId(), false);
        json += JSONdata.addAttribute("text", this.getName(), false);
        json += JSONdata.addAttribute("type", this.type, false);
        json += JSONdata.addAttribute("children", children, false);
        json += JSONdata.addAttribute("data", datatree, true);
        json += "}";
        return json;
    }

    /**
     * add child
     * @param child
     */
    public void AddChildren(Packagetree child) {
        children.add(child);
    }

    /**
     * getter
     * @return
     */
    public HashSet<Packagetree> getChildren() {
        return children;
    }

    /**
     * setter
     * @param children
     */
    public void setChildren(HashSet<Packagetree> children) {
        this.children = children;
    }

    /**
     * getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * setter
     * @param id
     */
    public void setId(int id) {
        this.type = "file";
        this.id = id;
    }

    /**
     * La fonction transforme les string de path de fichiers en un arbre. Les packages sont des noeuds et les fichiers sont des feuilles.
     * Tous implémentent JSONserialize().
     *
     * @param projectName
     * @param files
     * @param user
     * @return
     */
    public static Packagetree ParseJSONpathfiles(String projectName, List<File> files, User user) {
        HashMap<String, Packagetree> temp = new HashMap<String, Packagetree>();
        String path, path2, bigpath, pack, last;
        Packagetree currentpackage = null;
        Packagetree project = new Packagetree(projectName); // Création de la racine
        temp.put(projectName, project);                 // Ajout de la racine
        for (int i = 0; i < files.size(); i++) {
            last = project.getName();
            File f = files.get(i);
            path = f.getPathFile();
            if (path.length() > 1) {
                path2 = path.substring(1);
                int found = path2.indexOf("/");
                int additionnalfound = 1;
                while (found+1 < path2.length()) {     // A chaque / trouvé, on récupère la string le précédent
                    pack = path2.substring(0, found);
                    additionnalfound += found;            //On veut l'index depuis le départ pour avoir un package unique
                    bigpath = path.substring(0, additionnalfound+1);
                    currentpackage = new Packagetree(pack); //On créé un package pour la string
                    if (!temp.containsKey(bigpath)) {        //On l'ajoute à la map si elle y est pas
                        temp.put(bigpath, currentpackage);
                        //System.out.println("DOSSIER 1 : in "+last+" with key :"+bigpath);
                        temp.get(last).AddChildren(currentpackage);
                    }
                    path2 = path2.substring(found + 1); // On enlève le / étudié
                    found = path2.indexOf("/"); // Et on recommence la boucle
                    last = bigpath;
                }
                path2 = path2.substring(0,found);
                currentpackage = new Packagetree(path2); // String finale
                //System.out.println("DOSSIER : in "+last+" with key :"+path);
                if (!temp.containsKey(path)) {        //On l'ajoute à la map si elle y est pas
                    temp.put(path, currentpackage);
                    temp.get(last).AddChildren(currentpackage);
                }
                last = path;
            }
            //System.out.println("FICHIER : in "+last);
            currentpackage = new Packagetree(f.getNameFile());// On termine avec l'ajout du fichier
            currentpackage.setId(f.getIdFile());
            currentpackage.setLocking(f.boolocking(user));
            temp.put(path + "/" + f.getNameFile(), currentpackage);
            temp.get(last).AddChildren(currentpackage);
        }

        return project;

    }



    public boolean isLocking() {
        return locking;
    }

    public void setLocking(boolean locking) {
        this.locking = locking;
    }
}
