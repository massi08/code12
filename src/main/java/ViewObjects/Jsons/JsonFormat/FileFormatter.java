package ViewObjects.Jsons.JsonFormat;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.jstree.Packagetree;
import modele.File;

public class FileFormatter {

    /**
     * Transforme un packagetree en json
     * @param root
     * @return
     */
    public static String JsonPackageTree(Packagetree root){
        String jsontree = " [" +
                root.getJSON() +
                " ]";
        return jsontree ;
    }

    /**
     * renvoi un json d'un checkout
     * @param output
     * @param file
     * @return
     */
    public static String GitAndFile(String output, File file, String text, boolean lock){
        String json = "{" ;
        json+= JSONdata.addAttribute("output" , output, false);
        json+= JSONdata.addAttribute("text" , text, false);
        json+= JSONdata.addAttribute("lock" , lock, false);
        json+=JSONdata.addAttribute("file", file, true);
        json+="}";
        return json;
    }

}
