package flymetomars.core.mining;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import com.j256.ormlite.stmt.query.In;
import flymetomars.core.check.ValidationException;
import flymetomars.data.*;

import flymetomars.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EntityMinerTest {
    private EntityMiner miner;
    private PersonDAO pDao;
    private MissionDAO mDao;
    private ExpertiseDAO eDao;
    private InvitationDAO iDao;
    private EquipmentDAO eqDao;

    private Person p1;
    private Person p2;
    private Person p3;
    private Person p4;
    private Mission m1;
    private Mission m2;
    private Mission m3;
    private Expertise e1;
    private Expertise e2;
    private Equipment eq1;
    private Equipment eq2;

    @Before
    public void setUp() throws ValidationException{
        pDao = mock(PersonDAO.class);
        mDao = mock(MissionDAO.class);
        eDao = mock(ExpertiseDAO.class);
        iDao = mock(InvitationDAO.class);

        p1 = new Person("p", "1", "p1@example.com");
        p2 = new Person("p", "2", "p2@example.com");
        p3 = new Person("p", "3", "p3@example.com");
        p4 = new Person("p", "4", "p4@example.com");
        m1 = new Mission();
        m1.setName("initial");
        m2 = new Mission();
        m2.setName("intermediate");
        m3 = new Mission();
        m3.setName("final");
        e1 = new Expertise();
        e1.setDescription("Levitation");
        e2 = new Expertise();
        e2.setDescription("IT");
        eq1 = new Equipment("Laser", 10, 1, 20);
        eq2 = new Equipment("camera", 10, 2, 40);

        eqDao = mock(EquipmentDAO.class);

        miner = new EntityMiner(pDao, mDao, eDao, iDao);
    }

    @Test
    public void onePersonIsAlsoMostPopularPerson() throws SQLException, ValidationException {
        // create a single person
        Person p = new Person();
        p.setEmail("abc@abc.net.au");
        ArrayList<Person> list = new ArrayList<>();
        list.add(p);

        // mock the behaviour of pDao
        when(pDao.getAll()).thenReturn(list);

        Person mostPopularPerson = miner.getMostPopularPerson();
        assertEquals("One person is also most popular", p, mostPopularPerson);
    }

    /*    please uncomment if need to implement
    @Test
    public void onePersonIsAlsoCelebrarity() throws SQLException, ValidationException {
        // create a single person
        Person p = new Person();
        p.setEmail("abc@abc.net.au");
        ArrayList<Person> list = new ArrayList<>();
        list.add(p);

        // mock the behaviour of pDao
        when(pDao.getAll()).thenReturn(list);

        List<Person> celebrarities = miner.getCelebrarity(2);
        assertEquals("One person is also celebrarity", p, celebrarities.get(0));
    }

    @Test
    public void getTop3Celebrarities() throws SQLException, ValidationException {
        Invitation invitation1 = new Invitation();
        invitation1.setCreator(p2);
        invitation1.setRecipient(p1);

        Invitation invitation2 = new Invitation();
        invitation2.setCreator(p1);
        invitation2.setRecipient(p2);

        Invitation invitation3 = new Invitation();
        invitation3.setCreator(p3);
        invitation3.setRecipient(p2);

        Invitation invitation4 = new Invitation();
        invitation4.setCreator(p1);
        invitation4.setRecipient(p3);

        Invitation invitation5 = new Invitation();
        invitation5.setCreator(p2);
        invitation5.setRecipient(p3);

        Invitation invitation6 = new Invitation();
        invitation6.setCreator(p4);
        invitation6.setRecipient(p3);

        p1.setInvitationsReceived(Sets.newHashSet(invitation1));
        p2.setInvitationsReceived(Sets.newHashSet(invitation2,invitation3));
        p3.setInvitationsReceived(Sets.newHashSet(invitation4,invitation5,invitation6));

        // mock the behaviour of pDao
        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1,p2,p3,p4));

        List<Person> celebrarities = miner.getCelebrarity(3);
        assertEquals("Can get top 3 celebrarities", 3, celebrarities.size());
        assertEquals("Can get top 3 celebrarities", p3, celebrarities.get(0));
        assertEquals("Can get top 3 celebrarities", p2, celebrarities.get(1));
        assertEquals("Can get top 3 celebrarities", p1, celebrarities.get(2));
    }

    @Test
    public void oneBuddyIsAlsoBusiest() throws SQLException {
        p1.setFriends(Sets.newHashSet(p2));

        try {
            List<Person> buddies = miner.getBuddies(p1, 3);

            assertEquals("One buddy is also busiest", 1, buddies.size());
            assertEquals("One buddy is also busiest", p2, buddies.get(0));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getTop2BusiestBuddies() throws SQLException {
        p1.setFriends(Sets.newHashSet(p2,p3,p4));

        p2.getMissionRegistered().addAll(Sets.newHashSet(m1,m2,m3));
        p3.getMissionRegistered().addAll(Sets.newHashSet(m2,m3));
        p4.getMissionRegistered().addAll(Sets.newHashSet(m1));

        try {
            List<Person> buddies = miner.getBuddies(p1, 2);

            assertEquals("Can get top 2 busiest buddies", 2, buddies.size());
            assertEquals("Can get top 2 busiest buddies", p2, buddies.get(0));
            assertEquals("Can get top 2 busiest buddies", p3, buddies.get(1));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSocialCircleWith4Person() throws SQLException{

        p1.getMissionRegistered().addAll(Sets.newHashSet(m1,m3));
        p2.getMissionRegistered().addAll(Sets.newHashSet(m1,m2));
        p3.getMissionRegistered().addAll(Sets.newHashSet(m1,m2));
        p4.getMissionRegistered().addAll(Sets.newHashSet(m2,m3));

        m1.getParticipantSet().addAll(Sets.newHashSet(p1,p2,p3));
        m2.getParticipantSet().addAll(Sets.newHashSet(p2,p3,p4));
        m3.getParticipantSet().addAll(Sets.newHashSet(p1,p4));

        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1,p2,p3,p4));

        try {
            Set<Person> socialCircle = miner.getSocialCircle(p2);
            assertEquals(4,socialCircle.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aloneCanAlsoHaveSocialCircle() throws SQLException{

        p1.getMissionRegistered().addAll(Sets.newHashSet(m1));
        p2.getMissionRegistered().addAll(Sets.newHashSet(m2));
        p3.getMissionRegistered().addAll(Sets.newHashSet(m2,m3));
        p4.getMissionRegistered().addAll(Sets.newHashSet(m3));

        m1.getParticipantSet().addAll(Sets.newHashSet(p1));
        m2.getParticipantSet().addAll(Sets.newHashSet(p2,p3));
        m3.getParticipantSet().addAll(Sets.newHashSet(p3,p4));

        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1,p2,p3,p4));

        try {
            Set<Person> socialCircle = miner.getSocialCircle(p1);
            assertEquals(1,socialCircle.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void emptySourGrape() throws SQLException {
        p1.setFriends(Sets.newHashSet(p2,p3,p4));

        Invitation invitation1 = new Invitation();
        invitation1.setCreator(p2);
        invitation1.setRecipient(p1);

        // m1 will not be count in, because p1 is invited in this mission
        m1.setInvitationSet(Sets.newHashSet(invitation1));

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1,m2,m3));

        try {
            List<Mission> sourGrapes = miner.getSourGrapes(p1,3);
            assertEquals(0,sourGrapes.size());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTop3SourGrapes() throws SQLException {
        p1.setFriends(Sets.newHashSet(p2,p3,p4));

        Invitation invitation1 = new Invitation();
        invitation1.setCreator(p2);
        invitation1.setRecipient(p1);

        Invitation invitation2 = new Invitation();
        invitation2.setCreator(p1);
        invitation2.setRecipient(p2);

        Invitation invitation4 = new Invitation();
        invitation4.setCreator(p1);
        invitation4.setRecipient(p3);

        Invitation invitation6 = new Invitation();
        invitation6.setCreator(p3);
        invitation6.setRecipient(p4);

        // m1 will not be count in, because p1 is invited in this mission
        m1.setInvitationSet(Sets.newHashSet(invitation1,invitation2));  //p1,p2
        m2.setInvitationSet(Sets.newHashSet(invitation2,invitation4));  //p2,p3
        m3.setInvitationSet(Sets.newHashSet(invitation2,invitation4,invitation6)); // p2,p3,p4

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1,m2,m3));

        try {
            List<Mission> sourGrapes = miner.getSourGrapes(p1,3);
            assertEquals(2,sourGrapes.size());
            assertEquals(m3,sourGrapes.get(0));
            assertEquals(m2,sourGrapes.get(1));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * From assignment2 code
     */

    @Test
    public void oneSingleMissionIsExorbitance() throws SQLException {
        Multiset<Equipment> equipmentsRequired = HashMultiset.create();
        equipmentsRequired.add(eq1);
        equipmentsRequired.add(eq2);
        m1.getEquipmentsRequired().addAll(equipmentsRequired);
        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1));
        when(eqDao.getAll()).thenReturn(Lists.newArrayList(eq1, eq2));


        List<Mission> exorbitance = miner.getExorbitance(2);
        assertEquals(1, exorbitance.size());
        assertEquals(m1, exorbitance.get(0));
    }

    @Test
    public void oneExorbitanceMissionAmong3missions() throws SQLException {
        Multiset<Equipment> equipmentsRequired = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired1 = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired2 = HashMultiset.create();
        equipmentsRequired.add(eq1);
        equipmentsRequired.add(eq2);
        equipmentsRequired1.add(eq1);
        equipmentsRequired2.add(eq2);
        m1.getEquipmentsRequired().addAll(equipmentsRequired);
        m2.getEquipmentsRequired().add(eq1);
        m3.getEquipmentsRequired().add(eq2);

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1));
        when(eqDao.getAll()).thenReturn(Lists.newArrayList(eq1, eq2));
        //when(mDao.getEquipmentsForMission(m1)).thenReturn(HashMultiset.create(equipmentsRequired));
        //when(mDao.getEquipmentsForMission(m2)).thenReturn(HashMultiset.create(equipmentsRequired1));
        //when(mDao.getEquipmentsForMission(m3)).thenReturn(HashMultiset.create(equipmentsRequired2));

        List<Mission> exorbitance = miner.getExorbitance(1);
        assertEquals(1, exorbitance.size());
        assertEquals(m1, exorbitance.get(0));
    }

    @Test
    public void topTwoExorbitanceMissionAmong3missions() throws SQLException {
        Multiset<Equipment> equipmentsRequired = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired1 = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired2 = HashMultiset.create();
        equipmentsRequired.add(eq1);
        equipmentsRequired.add(eq2);
        equipmentsRequired1.add(eq1);
        equipmentsRequired2.add(eq2);
        m1.getEquipmentsRequired().addAll(equipmentsRequired);
        m2.getEquipmentsRequired().add(eq1);
        m3.getEquipmentsRequired().add(eq2);

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1, m2, m3));
        when(eqDao.getAll()).thenReturn(Lists.newArrayList(eq1, eq2));
        //when(mDao.getEquipmentsForMission(m1)).thenReturn(HashMultiset.create(equipmentsRequired));
        //when(mDao.getEquipmentsForMission(m2)).thenReturn(HashMultiset.create(equipmentsRequired1));
        //when(mDao.getEquipmentsForMission(m3)).thenReturn(HashMultiset.create(equipmentsRequired2));

        List<Mission> exorbitance = miner.getExorbitance(2);
        assertEquals(2, exorbitance.size());
        assertEquals(Lists.newArrayList(m1, m3), exorbitance);
    }

    @Test
    public void kLargerThanAllMissions() throws SQLException {
        Multiset<Equipment> equipmentsRequired = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired1 = HashMultiset.create();
        Multiset<Equipment> equipmentsRequired2 = HashMultiset.create();
        equipmentsRequired.add(eq1);
        equipmentsRequired.add(eq2);
        equipmentsRequired1.add(eq1);
        equipmentsRequired2.add(eq2);
        m1.getEquipmentsRequired().addAll(equipmentsRequired);
        m2.getEquipmentsRequired().add(eq1);
        m3.getEquipmentsRequired().add(eq2);

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1, m2, m3));
        when(eqDao.getAll()).thenReturn(Lists.newArrayList(eq1, eq2));
        //when(mDao.getEquipmentsForMission(m1)).thenReturn(HashMultiset.create(equipmentsRequired));
        //when(mDao.getEquipmentsForMission(m2)).thenReturn(HashMultiset.create(equipmentsRequired1));
        //when(mDao.getEquipmentsForMission(m3)).thenReturn(HashMultiset.create(equipmentsRequired2));

        List<Mission> exorbitance = miner.getExorbitance(5);
        assertEquals(3, exorbitance.size());
        assertEquals(Lists.newArrayList(m1, m3, m2), exorbitance);
    }

    @Test
    public void nullMissionHaveNoExorbitance() throws SQLException {
        when(mDao.getAll()).thenReturn(null);
        try {
            miner.getExorbitance(1);
            fail(" Should have thrown an exception .");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains(" No Mission found "));
        }
    }


    @Test
    public void oneSinglePersonIsAHotshot() throws SQLException {
        p1.addExpertise(e1);
        m1.getParticipantSet().add(p1);
        m1.getExpertiseRequired().add(e1);

        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1));
        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1));
        List<Person> hotshots = miner.getHotshots(2);
        assertEquals(1, hotshots.size());
        assertEquals(p1, hotshots.get(0));
    }

    @Test
    public void getAHotshotAmong3People() throws SQLException {
        p1.addExpertise(e1, e2);
        m1.getParticipantSet().add(p1);
        m1.getExpertiseRequired().add(e1);

        p2.addExpertise(e2);
        p3.addExpertise(e2);
        m2.getParticipantSet().add(p2);
        m2.getExpertiseRequired().add(e1);
        m3.getExpertiseRequired().add(e2);

        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1, p2, p3));
        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1, m2, m3));
        //when(pDao.getExpertiseByPerson(p1)).thenReturn(Sets.newHashSet(e1, e2));
        //when(pDao.getExpertiseByPerson(p2)).thenReturn(Sets.newHashSet(e2));
        //when(pDao.getExpertiseByPerson(p3)).thenReturn(Sets.newHashSet(e2));
        //when(mDao.getExpertiseForMission(m1)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m2)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m3)).thenReturn(Sets.newHashSet(e2));

        List<Person> hotshots = miner.getHotshots(1);
        assertEquals(1, hotshots.size());
        assertEquals(p1, hotshots.get(0));
    }

    @Test
    public void get2Hotshots() throws SQLException {
        p1.addExpertise(e1, e2);
        m1.getParticipantSet().add(p1);
        m1.getExpertiseRequired().add(e1);
        p2.addExpertise(e2);
        p3.addExpertise(e2);
        p4.addExpertise(e1);
        m2.getParticipantSet().add(p2);
        m2.getExpertiseRequired().add(e1);
        m3.getExpertiseRequired().add(e2);

        when(pDao.getAll()).thenReturn(Lists.newArrayList(p1, p2, p3, p4));
        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1, m2, m3));
        //when(pDao.getExpertiseByPerson(p1)).thenReturn(Sets.newHashSet(e1, e2));
        //when(pDao.getExpertiseByPerson(p2)).thenReturn(Sets.newHashSet(e2));
        //when(pDao.getExpertiseByPerson(p3)).thenReturn(Sets.newHashSet(e2));
        //when(pDao.getExpertiseByPerson(p4)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m1)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m2)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m3)).thenReturn(Sets.newHashSet(e2));

        List<Person> hotshots = miner.getHotshots(2);
        assertEquals(2, hotshots.size());
        assertEquals(Lists.newArrayList(p1, p4), hotshots);
    }

    @Test
    public void kLargerThanPeopleReturnsAllPeople() throws SQLException {
        p1.addExpertise(e1, e2);
        m1.getParticipantSet().add(p1);
        m1.getExpertiseRequired().add(e1);
        p2.addExpertise(e2);
        p3.addExpertise(e2);
        p4.addExpertise(e1);
        m2.getParticipantSet().add(p2);
        m2.getExpertiseRequired().add(e1);
        m3.getExpertiseRequired().add(e2);
        List<Person> listp = Lists.newArrayList(p1, p2, p3, p4);
        List<Mission> listm = Lists.newArrayList(m1, m2, m3);

        when(pDao.getAll()).thenReturn(listp);
        when(mDao.getAll()).thenReturn(listm);
        //when(pDao.getExpertiseByPerson(p1)).thenReturn(Sets.newHashSet(e1, e2));
        //when(pDao.getExpertiseByPerson(p2)).thenReturn(Sets.newHashSet(e2));
        //when(pDao.getExpertiseByPerson(p3)).thenReturn(Sets.newHashSet(e2));
        //when(pDao.getExpertiseByPerson(p4)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m1)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m2)).thenReturn(Sets.newHashSet(e1));
        //when(mDao.getExpertiseForMission(m3)).thenReturn(Sets.newHashSet(e2));

        List<Person> hotshots = miner.getHotshots(10);
        assertEquals(4, hotshots.size());
        assertEquals(listp, hotshots);
    }

    @Test
    public void nullMissionsHaveNoHotShots() throws SQLException {
        when(mDao.getAll()).thenReturn(null);
        try {
            miner.getHotshots(1);
            fail(" Should have thrown an exception .");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains(" No Mission found "));
        }
    }

    @Test
    public void nullPersonHaveNoHotShots() throws SQLException {
        when(pDao.getAll()).thenReturn(null);
        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1, m2, m3));
        try {
            miner.getHotshots(1);
            fail(" Should have thrown an exception .");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains(" No Person found "));
        }
    }

}