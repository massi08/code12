package ViewObjects.Jsons;

import ViewObjects.Jsons.jstree.Packagetree;
import modele.File;
import modele.User;

import java.util.*;

public class JSONdata {

    /**
     * Renvoie un attribut objet javascript sous forme de String. Last permet d'indiquer si c'est le dernier élément ed l'objet
     * Si value est un booleen ou un int, renvoie 'attr' : value,
     * Si value est un objet implémentant JSONserialize, renvoie 'attr' : value.getJSON()
     * Si value est une collection d'objets implémentants JSONserialize, renvoie 'attr' : { obj1.getJSON(), obj2.getJSON() ....}
     *
     * @param attr
     * @param value
     * @param last
     * @return
     */
    public static String addAttribute(String attr, Object value, boolean last) {
        if (value instanceof Collection) {
            Collection<JSONserialize> list = (Collection<JSONserialize>) value;
            return addlist(attr, list, last);
        } else if (value instanceof JSONserialize) {
            String res = "\"" + attr + "\" :" + ((JSONserialize) value).getJSON();
            if (!last)
                res += ",";
            return res;
        } else {
            String res = "\"" + attr + "\" : \"" + value + "\"";
            if (!last)
                res += ",";
            return res;
        }
    }

    /**
     * Fonction privée utilisée par addAttribute pour ajouter les éléments d'une collection.
     *
     * @param attr
     * @param list
     * @param last
     * @return
     */
    private static String addlist(String attr, Collection<JSONserialize> list, boolean last) {

        String res = '\"' + attr + "\" : [";
        Iterator<JSONserialize> it = list.iterator();
        while (it.hasNext()) {
            res += it.next().getJSON();
            if (it.hasNext())
                res += ",";
        }
        res += "]";
        if (!last)
            res += ",";
        return res;
    }



}
