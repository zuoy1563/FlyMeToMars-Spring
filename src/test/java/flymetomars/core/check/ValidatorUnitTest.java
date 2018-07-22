package flymetomars.core.check;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class ValidatorUnitTest {

    private Validator validator;
    private Mission mission;
    private Person person;
    private Equipment equipment;

    @Before
    public void setUp() throws ValidationException{
        equipment = new Equipment("hat", 10, 10, 10);
        validator = new Validator();
        mission = new Mission();
        person = new Person("A", "Foo", "abc@abc.net.au");

        mission.setName("initial");
        mission.setDuration(5);
        mission.setDescription("initial mission");
        mission.setLocation("Australia");
        mission.setTime(new Date());
        Map<Equipment.Attribute, Integer> attributeIntegerMap = new HashMap<>();
        attributeIntegerMap.put(Equipment.Attribute.weight, 50);
        attributeIntegerMap.put(Equipment.Attribute.cost, 50);
        attributeIntegerMap.put(Equipment.Attribute.volume, 50);
        mission.setMaxAttributes(attributeIntegerMap);

    }

    @Test
    public void missionNeedsToSatisfyRequiredExpertise() {

        Expertise e1 = new Expertise("navigation");
        Expertise e2 = new Expertise("cooking");
        Expertise e3 = new Expertise("IT");
        Expertise e4 = new Expertise("medical");

        Person p1 = new Person("p", "1", "p1@abc.com");
        Person p2 = new Person("p", "2", "p2@abc.com");

        p1.addExpertise(e1);
        p2.addExpertise(e2, e3);

        //setting people to mission
        p1.setMissionRegistered(Sets.newHashSet(mission));
        p2.setMissionRegistered(Sets.newHashSet(mission));
        //adding participant to mission
        mission.setParticipantSet(Sets.newHashSet(p1, p2));
        mission.setExpertiseRequired(Sets.newHashSet(e1, e2, e3, e4));

        try {
            validator.finalise(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains expertiseRequired", validationErrors.iterator().next().getMessage().contains("expertiseRequired"));
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionDoesNotHaveValidName() throws ValidationException{
        try {
            mission.setName("");
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains empty", validationErrors.iterator().next().getMessage().contains("empty"));
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionDoesNotHaveValidTime() {
        try {
            mission.setTime(null);
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains time", validationErrors.iterator().next().getMessage().contains("time"));
            assertFalse(validationErrors.isEmpty());
        }catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("null"));
        }
    }

    @Test
    public void missionDoesNotHaveValidLocation() throws ValidationException{

        try {
            mission.setLocation(null);
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains null", validationErrors.iterator().next().getMessage().contains("null"));
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionDoesNotHaveValidDescription() {
        try {
            mission.setDescription(null);
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains description", validationErrors.iterator().next().getMessage().contains("description"));
            assertFalse(validationErrors.isEmpty());
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("null"));
        }
    }

    @Test
    public void missionDoesNotHaveValidDuration() {
        try {
            mission.setDuration(0);
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains duration", validationErrors.iterator().next().getMessage().contains("duration"));
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionDoesNotHaveValidMaxAttributes() {
        try {
            mission.setMaxAttributes(null);
            validator.validateMissionAttributes(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains maxAttributes", validationErrors.iterator().next().getMessage().contains("maxAttributes"));
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionSatisfiesRequiredExpertise() {

        Expertise e1 = new Expertise("navigation");
        Expertise e2 = new Expertise("cooking");
        Expertise e3 = new Expertise("IT");
        Expertise e4 = new Expertise("medical");

        Person p1 = new Person("p", "1", "p1@abc.com");
        Person p2 = new Person("p", "2", "p2@abc.com");

        p1.addExpertise(e1, e4);
        p2.addExpertise(e2, e3);

        //setting people to mission
        p1.setMissionRegistered(Sets.newHashSet(mission));
        p2.setMissionRegistered(Sets.newHashSet(mission));
        //adding participant to mission
        mission.setParticipantSet(Sets.newHashSet(p1, p2));
        mission.setExpertiseRequired(Sets.newHashSet(e1, e2, e3, e4));

        try {
            Mission missionRet = validator.finalise(mission);
            assertEquals(missionRet.getStatus(), Mission.Status.finalised);
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue(validationErrors.isEmpty());
        }
    }

    @Test
    public void missionExceedsMaximumAllowedEquipmentCost() {
        try {
            Equipment equipment1 = new Equipment("iphone", 5, 5, 30);
            Equipment equipment2 = new Equipment("iphone", 5, 5, 30);
            Multiset<Equipment> equipments = HashMultiset.create();
            equipments.add(equipment1);
            equipments.add(equipment2);
            mission.setEquipmentsRequired(equipments);
            validator.checkAttributesMaxValue(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains cost", validationErrors.iterator().next().getMessage().contains("cost"));
        }
    }

    @Test
    public void missionExceedsMaximumAllowedEquipmentVolume() {
        try {
            Equipment equipment1 = new Equipment("iphone", 5, 30, 10);
            Equipment equipment2 = new Equipment("iphone", 5, 30, 10);
            Multiset<Equipment> equipments = HashMultiset.create();
            equipments.add(equipment1);
            equipments.add(equipment2);
            mission.setEquipmentsRequired(equipments);
            validator.checkAttributesMaxValue(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains volume", validationErrors.iterator().next().getMessage().contains("volume"));
        }
    }

    @Test
    public void missionExceedsMaximumAllowedEquipmentWeight() {
        try {
            Equipment equipment1 = new Equipment("iphone", 50, 30, 10);
            Equipment equipment2 = new Equipment("iphone", 5, 10, 10);
            Multiset<Equipment> equipments = HashMultiset.create();
            equipments.add(equipment1);
            equipments.add(equipment2);
            mission.setEquipmentsRequired(equipments);
            validator.checkAttributesMaxValue(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains weight", validationErrors.iterator().next().getMessage().contains("weight"));
        }
    }

    @Test
    public void missionDoesNotSupportEquipmentRequirements() {
        try {
            Equipment equipment1 = new Equipment("iphone", 10, 30, 10);
            Equipment equipment2 = new Equipment("iphone", 15, 10, 10);
            Multiset<Equipment> equipments = HashMultiset.create();
            equipments.add(equipment1);
            equipments.add(equipment2);
            mission.setEquipmentsRequired(equipments);
            validator.checkEquipmentsForMission(mission);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains equipment", validationErrors.iterator().next().getMessage().contains("equipment"));
        }
    }

    @Test
    public void missionSupportEquipmentRequirements() {
        try {
            Equipment equipment1 = new Equipment("iphone", 10, 30, 10);
            Equipment equipment2 = new Equipment("SLR", 5, 10, 10);
            Multiset<Equipment> equipments = HashMultiset.create();
            equipments.add(equipment1);
            equipments.add(equipment2);
            mission.setEquipmentsRequired(equipments);
            validator.checkEquipmentsForMission(mission);
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertFalse(validationErrors.isEmpty());
        }
    }

    @Test
    public void setInvalidFirstNameToAPerson() throws ValidationException {
        try {
            person.setFirstName(null);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains firstName", validationErrors.iterator().next().getMessage().contains("firstName"));
        }
    }

    @Test
    public void setInvalidLastNameToAPerson() throws ValidationException {
        try {
            person.setLastName(null);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains lastName", validationErrors.iterator().next().getMessage().contains("lastName"));
        }
    }

    @Test
    public void setInvalidEmailToAPerson() throws ValidationException {
        try {
            person.setEmail("aaa.aa");
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains email", validationErrors.iterator().next().getMessage().contains("email"));
        }
    }

    @Test
    public void setInvalidPasswordToAPerson() throws ValidationException {
        try {
            person.setPassword(null);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains password", validationErrors.iterator().next().getMessage().contains("password"));
        }
    }

    @Test
    public void setInvalidNameToAnEquipment() throws ValidationException {
        try {
            equipment.setName(null);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains name", validationErrors.iterator().next().getMessage().contains("name"));
        }
    }

    @Test
    public void setInvalidCostToAnEquipment() throws ValidationException {
        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 10);
        attributes.put(Equipment.Attribute.volume, 10);
        attributes.put(Equipment.Attribute.cost, -1);
        try {
            equipment.setAttributes(attributes);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains cost", validationErrors.iterator().next().getMessage().contains("cost"));
        }
    }

    @Test
    public void setInvalidWeightToAnEquipment() throws ValidationException {
        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, -2);
        attributes.put(Equipment.Attribute.volume, 10);
        attributes.put(Equipment.Attribute.cost, 10);
        try {
            equipment.setAttributes(attributes);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains weight", validationErrors.iterator().next().getMessage().contains("weight"));
        }
    }

    @Test
    public void setInvalidVolumeToAnEquipment() throws ValidationException {
        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 10);
        attributes.put(Equipment.Attribute.volume, 0);
        attributes.put(Equipment.Attribute.cost, 10);
        try {
            equipment.setAttributes(attributes);
            fail("No exception thrown");
        } catch (ValidationException e) {
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("message contains volume", validationErrors.iterator().next().getMessage().contains("volume"));
        }
    }

    /**
     * Added for extra credits
     */
    @Test
    public void stringShouldNotBeEmpty() {
        try {
            validator.stringCannotBeNullOrEmpty("");
            fail("No exception thrown");
        } catch (ValidationException e) {
            assertTrue("Throws VE", e instanceof ValidationException);
            Set<ValidationError> validationErrors = e.getValidationErrors();
            assertTrue("Message contains empty", validationErrors.iterator().next().getMessage().contains("empty"));
        }
    }

    @Test
    public void missionShouldNotNull() {
        try {
            validator.missionShouldNotNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains Mission", e.getMessage().contains("Mission"));
        }
    }
    @Test
    public void personShouldNotBeNull() {
        try {
            validator.personShouldNotBeNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains Person", e.getMessage().contains("Person"));
        }
    }
    @Test
    public void invitationStatusShouldNotBeNull() {
        try {
            validator.invitationStatusShouldNotBeNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains Invitation status", e.getMessage().contains("Invitation status"));
        }
    }
    @Test
    public void dateShouldNotBeNull() {
        try {
            validator.dateShouldNotBeNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains Date", e.getMessage().contains("Date"));
        }
    }

    @Test
    public void personSetShouldNotBeNullOrEmpty() {
        try {
            validator.personSetShouldNotBeNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains PersonSet", e.getMessage().contains("PersonSet"));
        }
    }

    @Test
    public void descriptionShouldNotBeNull() {
        try {
            validator.descriptionShouldNotBeNull(null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
        }
    }


}