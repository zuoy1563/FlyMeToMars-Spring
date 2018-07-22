package flymetomars.data.ormlite;

import com.google.common.collect.Maps;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import flymetomars.data.MissionDAO;
import flymetomars.model.Mission;
import flymetomars.model.Person;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class MisisonDaoImpl extends AbstractEntityDAOImpl<Mission> implements MissionDAO {

    public MisisonDaoImpl(ConnectionSource connectionSource) throws SQLException {
        dao = DaoManager.createDao(connectionSource, Mission.class);
    }

    @Override
    public Set<Mission> getMissionsByCreator(Person person) throws SQLException {
        return new LinkedHashSet<>(dao.queryForEq("captain_id", person.getId()));
    }

    @Override
    public Mission getMissionsByCreatorAndName(Person person, String name) throws SQLException {
        QueryBuilder<Mission, Long> queryBuilder = dao.queryBuilder();
        Where<Mission, Long> where = queryBuilder.where();
        where.eq("captain", person).and().eq("name", name);
        PreparedQuery<Mission> query = queryBuilder.prepare();

        return dao.queryForFirst(query);
    }
}
