package metier.UserDetails;

import dao.UserDAO;
import modele.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {


    private UserDAO userdao;

    public MyUserDetailsService(UserDAO userdao) {
        this.userdao = userdao;
    }

    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        //com.mkyong.users.model.User user = userDao.findByUserName(username);
        User user = userdao.getByName(username);
        MyUserDetails userdetails = new MyUserDetails(user.getPseudo(), user.getPassword(), user.getAuthorities());
        userdetails.setUser(user);
        //System.out.println(user.getPseudo());
        /*List<GrantedAuthority> authorities =
                buildUserAuthority(user.getAccess());*/
        //UserDetails user2=new UserDetails();
        return userdetails;
    }
}
