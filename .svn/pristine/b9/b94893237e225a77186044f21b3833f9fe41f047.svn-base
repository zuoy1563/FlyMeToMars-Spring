package flymetomars.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EquipmentUnitTest {

    /**
     * From assignment2 code
     */
    private Equipment e, e1, e2;

    @Before
    public void setUp() {
        e1 = new Equipment("Laser", 10, 2, 20);
        e2 = new Equipment("Laser", 10, 2, 20);
        e = new Equipment("iphone", 10, 2, 20);
    }

    @Test
    public void equipmentObjectNotNull() {
        assertNotNull("equipment not null", e);
    }

    @Test
    public void equipmentObjectsAreNotEqual() {
        assertFalse("objects are not equal", e.equals(new Equipment("Laser", 10, 2, 20)));
        assertNotEquals(e.hashCode(), (new Equipment("iphone", 148, 55, 20)).hashCode());
    }

    /**
     * From lecturer
     */
    @Test
    public void sameEquipmentNeedsToHaveSameAttributesAndName() {
        e1 = new Equipment("Laser", 10, 2, 20);
        e2 = new Equipment("Laser", 10, 2, 20);

        assertEquals(e1, e2);
    }

    @Test
    public void differentEquipmentCanHaveDifferentAttributes() {
        e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment("Laser", 10, 2, 20);

        assertNotEquals(e1, e2);
    }

    @Test
    public void differentEquipmentCanHaveDifferentNames() {
        e1 = new Equipment("Laser", 10, 2, 20);
        e2 = new Equipment("laser", 10, 2, 20);

        assertNotEquals(e1, e2);
    }
}