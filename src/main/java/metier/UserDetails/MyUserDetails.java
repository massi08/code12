package metier.UserDetails;

import modele.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyUserDetails extends org.springframework.security.core.userdetails.User {

    User user;

    public MyUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * Initialise l'user du modèle dans le user de spring security
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Récupère l'user du modèle dans le user de spring security
     * @return
     */
    public User getUser() {
        return this.user;
    }

}