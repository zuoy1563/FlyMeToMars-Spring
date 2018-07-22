package flymetomars.model;

import flymetomars.core.check.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yli on 16/03/15.
 */
public class PersonUnitTest {
    private Person p;

    @Before
    public void setUp() {
        p = new Person();
    }

    @Test
    public void expertiseNotNull() {
        assertNotNull("expertise not null", p.getExpertise());
    }

    @Test
    public void passwordNotNullOrEmpty() {
        try {
            p.setPassword(null);
            fail("No exception thrown for null password");
        } catch (Exception e) {
            assertTrue("Throws VE", e instanceof ValidationException);
            //assertTrue("Message contains null", e.getMessage().contains("null"));
        }
    }

    @Test
    public void differentPersonNotEqual() throws ValidationException {
        p.setEmail("abc@abc.net.au");
        Object o = "abc@abc.net.au";
        assertNotEquals("String not Person", p, o);

        Person q = new Person();
        assertNotEquals("q doesn't have an email", p, q);

        q.setEmail("abc@abc.net");
        assertNotEquals("Different emails", p, q);
    }

    // other group changed sameEmailSamePerson(), see assignment2 code part

    @Test
    public void newNullExpertiseThrowsIAE() {
        try {
            p.addExpertise(null);
            fail("No exception thrown for null expertise");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
        }
    }

    @Test
    public void newExpertiseNullDescriptionThrowsIAE() {
        // mock an Expertise object with expected behaviour
        Expertise exp = mock(Expertise.class);
        when(exp.getDescription()).thenReturn(null);

        try {
            p.addExpertise(exp);
            fail("No exception thrown for null expertise description");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains description", e.getMessage().contains("description"));
        }
    }

    @Test
    public void newExpertiseWithNonemptyDescription() {
        // mock an Expertise object with expected behaviour
        Expertise exp = mock(Expertise.class);
        when(exp.getDescription()).thenReturn("fishing");

        assertEquals("Empty collection", 0, p.getExpertise().size());
        p.addExpertise(exp);
        assertEquals("1 expertise", 1, p.getExpertise().size());
    }

    /**
     * From assignment2 code
     */

    @Test
    public void personObjectNotNull() {
        assertNotNull("Person not null", p);
    }

    @Test
    public void sameEmailSamePerson() {
        Person q = new Person("B", "Bar", "unknown@unknown.un");
        assertEquals("Names don't matter", p, q);
    }

    @Test
    public void peopleWithDifferentEmailsAreNotEqual() {
        Person q = new Person("B", "Bar", "abc@def.net.au");
        assertNotEquals("Different emails", p, q);
    }


    @Test
    public void equipmentOwnedNotNull() {
        assertNotNull("equipmentOwned  not null", p.getEquipmentOwned());
    }

    @Test
    public void missionRegisteredNotNull() {
        assertNotNull("missionRegistered not null", p.getMissionRegistered());
    }

    @Test
    public void invitationReceivedNotNull() {
        assertNotNull("invitationReceived not null", p.getInvitationsReceived());
    }

}