package flymetomars.model;

import flymetomars.core.check.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by pokha on 20/03/2017.
 */
public class InvitationUnitTest {
    private Invitation i;

    @Before
    public void setUp() {
        i = new Invitation();
    }

    @Test
    public void testGetAndSetStatus() {
        i.setStatus(Invitation.InvitationStatus.SENT);
        Invitation.InvitationStatus status = i.getStatus();
        assertEquals(status, Invitation.InvitationStatus.SENT);
    }

    @Test
    public void invitationObjectNotNull() {
        assertNotNull("Invitation not null", i);
    }

    @Test
    public void objectsAreNotEqual() {
        assertFalse("objects are not equal", i.equals(new Person()));
    }

    @Test
    public void invitationObjectsAreEqual() throws ValidationException {
        Person c = new Person();
        c.setEmail("ccc@ccc.com");
        Person r = new Person();
        r.setEmail("rrr@rrr.com");
        Mission m = new Mission();
        Invitation iv = new Invitation();
        iv.setCreator(c);
        i.setCreator(c);
        iv.setRecipient(r);
        i.setRecipient(r);
        iv.setMission(m);
        i.setMission(m);
        assertTrue("invitations are equal", iv.equals(i));
    }
}
