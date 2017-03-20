package modele;

import ViewObjects.Jsons.JSONdata;
import ViewObjects.Jsons.JSONserialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * Created by audrey on 17/10/16.
 */

@Entity
@Table(name = "USER")
@NamedQueries({
        @NamedQuery(name = "User.getByPseudo", query = "SELECT u FROM User u WHERE u.pseudo = :pseudo"),
        @NamedQuery(name = "User.getById", query = "SELECT u FROM User u WHERE u.idUser = :id"),
        @NamedQuery(name = "User.getAll", query = "SELECT c FROM User c"),
})
public class User implements UserDetails, JSONserialize {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID_USER")
    private int idUser;


    @Column(name = "PSEUDO")
    @NotEmpty(message = "Please enter your pseudo.")
    @Size(min = 2, max = 30, message = "Your pseudo must between 2 and 30 characters")
    @Pattern(regexp = "[a-zA-Z0-9_.]*", message="Your pseudo is invalid")
    private String pseudo;


    @Column(name = "FIRSTNAME")
    @NotEmpty(message = "Please enter your fist name.")
    @Size(min = 2, max = 30, message = "Your firstname must between 2 and 30 characters")
    @Pattern(regexp = "[a-zA-Z0-9_.]*", message="Your firstname is invalid")
    private String firstName;


    @Column(name = "LASTNAME")
    @NotEmpty(message = "Please enter your lastName.")
    @Size(min = 2, max = 15, message = "Your lastname must between 2 and 15 characters")
    @Pattern(regexp = "[a-zA-Z0-9_.]*", message="Your lastname is invalid")
    private String lastName;


    @Column(name = "PASSWORD")
    @NotEmpty(message = "Please enter your password.")
    @Size(min = 4, message = "Your password must between 6 and 15 characters")
    private String password;

    @Transient
    private String confirmPassword ;

    @Column(name = "EMAIL")
    @NotEmpty(message = "Please enter your email.")
    @Size(min = 6, max = 30, message = "Your email must between 6 and 30 characters")
    @Email
    private String email;

    @Column(name = "ACCESS")
    private String access;

    /**
     * Constructeur vide de User
     */
    public User(){
    }

    /**
     * Constructeur de User
     * @param pseudoParam
     * @param firstNameParam
     * @param lastNameParam
     * @param passwordParam
     * @param emailParam
     */
    public User(String pseudoParam, String firstNameParam, String lastNameParam, String passwordParam, String emailParam) {
        password = encoder.encode(passwordParam);
        pseudo = pseudoParam;
        firstName=firstNameParam;
        lastName=lastNameParam;
        email=emailParam;
        this.setAccess("User");

    }

    /**
     *
     * @return pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     *
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
        auths.add(new SimpleGrantedAuthority(getAccess()));
        return auths;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter
     * @param pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * setter
     * @param password
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     *
     * @return pseudo
     */
    public String getUsername() {
        return pseudo;
    }

    /**
     *
     * @return
     */
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isEnabled() {
        return false;
    }

    /**
     *
     * @return idUser
     */
    public int getIdUser() {
        return idUser;
    }


    /**
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return lastName
     */
    public String getlastName() {
        return lastName;
    }

    /**
     *
     * @return access
     */
    public String getAccess() {
        return access;
    }

    /**
     * setter
     * @param access
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     *
     * @return JSON de user
     */
    public String getJSON() {
        String json = "{";
        json += JSONdata.addAttribute("pseudo", this.getPseudo(), false);
        json += JSONdata.addAttribute("firstName", this.getFirstName(), false);
        json += JSONdata.addAttribute("lastName", this.getLastName(), false);
        json += JSONdata.addAttribute("email", this.getEmail(), false);
        json += JSONdata.addAttribute("id", this.getIdUser(), true);
        json += "}";
        return json;
    }

    /**
     * setter
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * setter
     * @param confirmPassword
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * setter
     * @param idUser
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
