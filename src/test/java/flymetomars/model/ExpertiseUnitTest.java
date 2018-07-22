package flymetomars.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rashmi on 20/03/2017.
 */
public class ExpertiseUnitTest {
    private Expertise ex;
    private Expertise ex1;

    @Before
    public void setUp() {
        ex = new Expertise();
        ex1 = new Expertise("theoretical physics");
    }

    @Test
    public void expertiseObjectsAreEqual() {
        assertTrue("objects are equal", ex.equals(new Expertise()));

        ex.setDescription("theoretical physics");
        assertEquals(ex.hashCode(), ex1.hashCode());
    }

    @Test
    public void expertiseObjectsAreNotEqual() {
        assertFalse("objects are not equal", ex.equals(ex1));
        assertNotEquals(ex.hashCode(), new Expertise("cooking"));
    }

    @Test
    public void descriptionNotNullOrEmpty() {
        try {
            ex.setDescription(null);
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
        }

        try {
            ex.setDescription("");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains empty", e.getMessage().contains("empty"));
        }
    }


}
