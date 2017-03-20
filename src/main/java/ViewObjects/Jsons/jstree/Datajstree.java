package ViewObjects.Jsons.jstree;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JSONserialize;

public class Datajstree implements JSONserialize {

    private int id;
    private String opened;
    private boolean locking;

    public Datajstree(int id, boolean locking) {
        this.opened = "closed";
        this.id = id;
        this.locking = locking;
    }

    /**
     * json format of object
     * @return
     */
    public String getJSON() {
        String json = "{";
        json += JSONdata.addAttribute("opened", opened, false);
        json += JSONdata.addAttribute("idfile", id, false);
        json += JSONdata.addAttribute("haslock", locking, true);
        json += "}";
        return json;
    }
}
