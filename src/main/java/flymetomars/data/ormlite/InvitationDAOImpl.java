package flymetomars.data.ormlite;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import flymetomars.data.InvitationDAO;
import flymetomars.model.Invitation;
import flymetomars.model.Person;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Zoe on 28/5/17.
 */
public class InvitationDAOImpl extends AbstractEntityDAOImpl<Invitation> implements InvitationDAO {

    public InvitationDAOImpl(ConnectionSource connectionSource) throws SQLException {
        dao = DaoManager.createDao(connectionSource, Invitation.class);
    }

    @Override
    public Set<Invitation> getInvitationsByCreator(Person person) throws SQLException {
        return new LinkedHashSet<>(dao.queryForEq("creator_id", person.getId()));
    }

    @Override
    public Set<Invitation> getInvitationsByRecipient(Person person) throws SQLException {
        return new LinkedHashSet<>(dao.queryForEq("recipient_id", person.getId()));
    }
}
