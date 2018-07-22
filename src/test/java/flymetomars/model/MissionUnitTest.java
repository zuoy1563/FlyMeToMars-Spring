package flymetomars.model;

import flymetomars.core.check.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pokha on 20/03/2017.
 */
public class MissionUnitTest {
    private Mission m;

    @Before
    public void setUp() {
        m = new Mission();
    }

    @Test
    public void missionObjectsAreNotEqual() {
        m.setDescription("desc");
        assertNotEquals(m.hashCode(), (new Mission()).hashCode());
        assertFalse("mission objects are not equal", m.equals(new Mission()));
    }

    @Test
    public void participantSetNotNull() {
        assertNotNull("participantSet not null", m.getParticipantSet());
    }

    @Test
    public void equipmentRequiredNotNull() {
        assertNotNull("equipmentRequired  not null", m.getEquipmentsRequired());
    }

    @Test
    public void invitationSetNotNull() {
        assertNotNull("invitationSet not null", m.getInvitationSet());
    }

    //added for extra credit
    @Test(expected = IllegalArgumentException.class)
    public void timeCantBeNull() {
        m.setTime(null);
    }

    @Test(expected = ValidationException.class)
    public void nameCantBeEmptyOrNull() throws ValidationException{
        m.setName("");
        m.setName(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void captainCannotBeNull(){
        m.setCaptain(null);
    }

    @Test(expected = ValidationException.class)
    public void locationCantBeEmptyOrNull() throws ValidationException {
        m.setLocation("");
        m.setLocation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void descriptionCantBeNull() {
        m.setDescription(null);
    }


}
