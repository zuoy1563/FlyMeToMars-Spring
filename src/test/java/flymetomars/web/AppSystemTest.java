package flymetomars.web;

import com.google.common.collect.Sets;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sun.tools.internal.ws.processor.generator.JwsImplGenerator;
import flymetomars.core.check.ValidationException;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.data.ormlite.InvitationDAOImpl;
import flymetomars.data.ormlite.MisisonDaoImpl;
import flymetomars.data.ormlite.PersonDAOImpl;
import flymetomars.model.Invitation;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import flymetomars.model.PersonMission;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import spark.Spark;
import spark.resource.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import static org.h2.util.IOUtils.closeSilently;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class AppSystemTest {

    private static String dbURL;
    private static PersonDAO personDAO;
    private static MissionDAO missionDAO;
    private static InvitationDAO invitationDAO;
    private static Dao<PersonMission, Long> personMissionDao;

    @BeforeClass
    public static void setUp() throws Exception {
        ClassPathResource resource = new ClassPathResource( "app.properties" );

        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            properties.load(stream);
            int port = Integer.parseInt(properties.getProperty("spark.port"));
            JWebUnit.setBaseUrl("http://localhost:" + port);

            dbURL = "jdbc:h2:./" + properties.getProperty("h2.dir");
            setupDB(dbURL);
        } finally {
            closeSilently(stream);
        }

        App.main(null);

        try {
            Thread.sleep(500);
        } catch (Exception ignored) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Spark.stop();
        cleanDataBase(dbURL);
    }

    private static void setupDB(String dbURL) throws SQLException, IOException, ValidationException {
        ConnectionSource connectionSource = new JdbcConnectionSource(dbURL);

        personDAO = new PersonDAOImpl(connectionSource);
        TableUtils.createTableIfNotExists(connectionSource, Person.class);

        missionDAO = new MisisonDaoImpl(connectionSource);
        TableUtils.createTableIfNotExists(connectionSource, Mission.class);

        invitationDAO = new InvitationDAOImpl(connectionSource);
        TableUtils.createTableIfNotExists(connectionSource, Invitation.class);

        personMissionDao = DaoManager.createDao(connectionSource, PersonMission.class);
        TableUtils.createTableIfNotExists(connectionSource, PersonMission.class);


        addDummyUsers();
    }

    /**
     * Adds dummy users to the system. These users are used to display the information on the userpage.
     *
     * @throws SQLException
     */
    private static void addDummyUsers() throws SQLException, ValidationException {
        Person newUser = new Person();
        String userInfostr = "demo";
        for (int index = 0; index < 3; index++) {
            newUser.setFirstName(userInfostr);
            newUser.setLastName("Name" + index);
            newUser.setEmail(userInfostr + index + "@" + userInfostr + ".com");
            newUser.setPassword(userInfostr + index);
            personDAO.save(newUser);
        }
    }

    /**
     * Removes all the tables from the database once the test is implemented. This method should not be called form
     * any other location.
     *
     * @throws SQLException
     */
    private static void cleanDataBase(String dbURL) throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(dbURL);

        TableUtils.dropTable(connectionSource, Person.class, true);
        TableUtils.dropTable(connectionSource, Mission.class, true);
        TableUtils.dropTable(connectionSource, Invitation.class, true);
        TableUtils.dropTable(connectionSource, PersonMission.class, true);
    }

    @Test
    public void basePageShouldContainWelcome() {
        String path = "/";
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("Welcome");
    }

    @Test
    public void loginPageShouldContainUsernameField() {
        String path = "/login";
        JWebUnit.beginAt(path);
        JWebUnit.assertTitleEquals("Fly me to Mars: a mission registration system.");
        JWebUnit.assertFormPresent("user_login");
        JWebUnit.assertElementPresent("user_name");
        JWebUnit.assertTextInElement("user_name", "");
        JWebUnit.assertSubmitButtonPresent();
    }

    /**
     * For extra credit
     */

    @Test
    public void registerPageShouldContainUserRegistrationText() {
        String path = "/register";
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("User Registration");
    }

    @Test
    public void NameCanAppearOnHomePageAndCanHaveOnceRegistered() throws ValidationException, SQLException{
        Person newUser = new Person();
        newUser.setFirstName("Small");
        newUser.setLastName("Fish");
        newUser.setEmail("smallfish@sm.com");
        newUser.setPassword("smallfish");
        personDAO.save(newUser);

        String path = "/persons";
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("Small Fish");

        String path1 = "/person/" + newUser.getId();
        JWebUnit.beginAt(path1);
        JWebUnit.assertTextPresent("Small Fish");
    }

    @Test
    public void missionsListedShouldContainMissionsListingText() {
        String path = "/missions";
        JWebUnit.beginAt(path);
        JWebUnit.assertTitleEquals("Fly me to Mars: a mission registration system.");
        JWebUnit.assertTextPresent("Mission Listing");
    }

    @Test
    public void createAMissionThenThatCanBeListedAndSeeDetails() throws SQLException, ValidationException {
        Mission mission = createTestMission();
        String path1 = "/missions";
        JWebUnit.beginAt(path1);
        JWebUnit.assertTextPresent("defeat boss");

        String path = "/mission/" + mission.getId();
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("defeat boss");
    }

    @Test
    public void missionDetailsShouldContainMissionDetailsText() throws SQLException {
        String path = "/mission/" + missionDAO.getAll().get(0).getId();
        JWebUnit.beginAt(path);
        JWebUnit.assertTitleEquals("Fly me to Mars: a mission registration system.");
        JWebUnit.assertTextPresent("Mission details");
        JWebUnit.assertTextPresent("Name");
        JWebUnit.assertTextPresent("Location");
    }

    @Test
    public void invitationDetailsCanBeViewedOnceInvitationHasBeenCreated() throws SQLException, ValidationException {
        Mission mission = createTestMission();

        Invitation invitation = new Invitation();
        invitation.setMission(mission);
        invitation.setCreator(mission.getCaptain());
        invitation.setLastUpdated(new Date());
        invitation.setStatus(Invitation.InvitationStatus.SENT);
        invitation.setRecipient(personDAO.getAll().get(1));
        invitationDAO.save(invitation);

        invitation.getRecipient().getInvitationsReceived().add(invitation);
        personDAO.update(invitation.getRecipient());

        // can see invitation details
        String path = "/invitation/" + invitation.getId();
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("defeat boss");
        JWebUnit.assertTextPresent("Creator: " + invitation.getCreator().getFirstName() +
                " " + invitation.getCreator().getLastName());

        // recipient can see invitation at his/ her home page
        String path1 = "/person/" + invitation.getRecipient().getId();
        JWebUnit.beginAt(path1);
        JWebUnit.assertTextPresent("defeat boss");

        // creator can also see invitation at his/ her home page
        String path2 = "/person/" + invitation.getCreator().getId();
        JWebUnit.beginAt(path2);
        JWebUnit.assertTextPresent("defeat boss");
    }

    @Test
    public void invitationCreationPageShouldContainForm() throws SQLException, ValidationException{
        Mission mission = createTestMission();

        String path = "/mission/" + mission.getId() + "/create_invitation";
        JWebUnit.beginAt(path);
        JWebUnit.assertTitleEquals("Fly me to Mars - a mission registration system.");
        JWebUnit.assertFormPresent();
        JWebUnit.assertElementPresent("recipient");
        JWebUnit.assertTextPresent("Invitation creation");
    }

    @Test
    public void missionUpdatePageShouldContainForm() throws SQLException,ValidationException {
        Mission mission = createTestMission();

        String path = "/mission/" + mission.getId() + "/update";
        JWebUnit.beginAt(path);
        JWebUnit.assertTitleEquals("Fly me to Mars: a mission registration system.");
        JWebUnit.assertFormPresent();
        JWebUnit.assertElementPresent("time");
        JWebUnit.assertTextPresent("Mission Update");
    }

    @Test
    public void updatedMissionDetailsCanBeCorrectlyShow() throws SQLException,ValidationException {
        Mission mission = createTestMission();
        mission.setName("defeat boss stage 2");
        mission.setDescription("999 warriors");
        missionDAO.update(mission);

        // can see invitation details
        String path = "/mission/" + mission.getId();
        JWebUnit.beginAt(path);
        JWebUnit.assertTextPresent("defeat boss stage 2");
        JWebUnit.assertTextPresent("999 warriors");

        // can see new mission in mission list
        String path1 = "/missions";
        JWebUnit.beginAt(path1);
        JWebUnit.assertTextPresent("defeat boss stage 2");
    }

    public Mission createTestMission() throws SQLException, ValidationException {
        Mission mission = new Mission();
        mission.setName("defeat boss");
        mission.setBudget(10000);
        Person p = personDAO.getAll().get(0);
        mission.setCaptain(p);
        mission.setDescription("let's defeat boss!");
        mission.setLocation("home");
        mission.setTime(new Date());
        mission.setParticipantSet(Sets.newHashSet(p));

        PersonMission personMission = new PersonMission();
        personMission.setMission(mission);
        personMission.setPerson(p);
        missionDAO.save(mission);
        personMissionDao.create(personMission);

        return mission;
    }

}
