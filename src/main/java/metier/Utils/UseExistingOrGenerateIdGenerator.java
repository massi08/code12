package metier.Utils;

import modele.Discussion;
import modele.DiscussionID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.IncrementGenerator;
import org.hibernate.id.SequenceGenerator;

import java.io.Serializable;

/**
 * Fonctionne seulement avec les discussions. facile à adapter
 */
public class UseExistingOrGenerateIdGenerator extends IncrementGenerator{
    @Override
    public Serializable generate(SessionImplementor session, Object object)
            throws HibernateException {
        Discussion discussion=(Discussion)object;
        int id=discussion.getIdDiscussion();
        return id != 0 ? id : super.generate(session, object);
    }
}