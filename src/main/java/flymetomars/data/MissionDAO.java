package flymetomars.data;

import com.google.common.collect.Multiset;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;

import java.sql.SQLException;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
public interface MissionDAO extends DAO<Mission>{
    Set<Mission> getMissionsByCreator(Person person) throws SQLException;

    Mission getMissionsByCreatorAndName(Person person, String name) throws SQLException;

    /**
     * from assignment 2 code, the other group added some extra code for this class, which is not necessary
     */
    //Multiset<Equipment> getEquipmentsForMission(Mission mission);

    //Set<Expertise> getExpertiseForMission(Mission mission);
}
