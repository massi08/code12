package modele;

import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by audrey on 17/10/16.
 */


@Entity
@Table(name = "MEMBER")
@NamedQueries({
        @NamedQuery(name="Member.getAllByProject", query="SELECT m FROM Member m WHERE m.idProject = :idProject"),
        @NamedQuery(name="Member.getAllActifByProject", query = "SELECT m FROM Member m WHERE m.idProject =:idProject and m.role != :idRole"),
        @NamedQuery(name="Member.getAllProjectByUser", query="SELECT m.idProject FROM Member m WHERE m.idUser = :idUser and m.role != :idRole"),
        @NamedQuery(name="Member.getAllByRoleProject", query="SELECT m FROM Member m WHERE m.idProject = :idProject and m.role= :role"),
        @NamedQuery(name="Member.getByUserProject", query="SELECT m FROM Member m WHERE m.idProject = :idProject and m.idUser= :idUser"),
        @NamedQuery(name="Member.getAllByProjectExceptOne", query="SELECT m FROM Member m WHERE m.idProject = :idProject and m.idUser!=:user"),
        @NamedQuery(name="Member.getRolebyUserandProject", query="SELECT m.role FROM Member m WHERE m.idProject =:idProject and m.idUser =:idUser"),
        @NamedQuery(name="Member.getChefByProject", query = "SELECT m FROM Member m WHERE m.idProject =:idProject and m.role =:idRole"),
})
@IdClass(MemberID.class)
public class Member implements Serializable{


    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    private User idUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PROJECT", referencedColumnName = "ID_PROJECT")
    private Project idProject;


    @ManyToOne
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE")
    private Role role;

    /**
     * Constructeur vide de Member
     */
    public Member() {
    }

    /**
     * Constructeur de Member
     * @param idUserM
     * @param idProjectM
     * @param roleM
     */
    public Member(User idUserM, Project idProjectM, Role roleM) {
        this.idUser = idUserM;
        this.idProject = idProjectM;
        this.role = roleM;
    }

    /**
     *
     * @return role.getNom()
     */
    public String getRoleName() {
        return role.getNom();
    }

    /**
     *
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * setter
     * @param r
     */
    public void setRole(Role r) {
        role = r;
    }

    /**
     *
     * @return idUser
     */
    public User getIdUser() {
        return idUser;
    }

    /**
     *
     * @return idProject
     */
    public Project getIdProject() {
        return idProject;
    }


}