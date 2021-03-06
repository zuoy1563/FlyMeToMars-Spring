package flymetomars.core.check;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import flymetomars.model.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class Validator {

    /**
     * Needs to validate the following.
     * <p>
     * 1. A mission needs to have a time, a name, a duration, a location, a description,
     * as well as a non-empty maxAttributes field variable.
     * 2. A mission needs satisfy its equipment requirements. For this purpose, two pieces
     * of equipments are considered the same if they have the same name.
     * 3. The sum of a particular attribute value of all equipments in a mission cannot exceed
     * the maximally allowed value of that attribute of the mission.
     * 4. A mission needs to satisfy all its required expertise to be viable.
     *
     * @param mission
     * @return
     * @throws ValidationException
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Mission finalise(Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        Set<Person> participants = mission.getParticipantSet();
        Set<Expertise> expertiseOfParticipants = new HashSet<>();
        if (validateMissionAttributes(mission) && checkAttributesMaxValue(mission) && checkEquipmentsForMission(mission)) {
            for (Person p : participants) {
                Collection<Expertise> expertiselist = p.getExpertise();
                expertiseOfParticipants.addAll(expertiselist);
            }

            if (!(expertiseOfParticipants.containsAll(mission.getExpertiseRequired()))) {
                validationErrors.add(new ValidationError("expertiseRequired", "expertiseRequired is not filled"));
            }
        }
        if (validationErrors.isEmpty()) {
            mission.setStatus(Mission.Status.finalised);
            return mission;
        } else {
            throw new ValidationException("Mission cannot be validated", validationErrors);
        }
    }

    public Boolean validateMissionAttributes(Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null == mission.getTime()) {
            validationErrors.add(new ValidationError("time", "time can not be null"));
        } else {
            if (mission.getName() == null || mission.getName().trim().equals("")) {
                validationErrors.add(new ValidationError("name", "name can not be empty"));
            } else {
                if (mission.getDuration() <= 0) {
                    validationErrors.add(new ValidationError("duration", "duration can not be less than 1"));
                } else {
                    if (mission.getLocation() == null || mission.getLocation().trim().equals("")) {
                        validationErrors.add(new ValidationError("location", "location can not be empty"));
                    } else {
                        if (mission.getDescription() == null || mission.getDescription().trim().equals("")) {
                            validationErrors.add(new ValidationError("description", "description can not be empty"));
                        } else {
                            if (!(validateMaxAttribute(mission))) {
                                validationErrors.add(new ValidationError("maxAttributes", "maxAttributes field can not be empty"));
                            }
                        }
                    }
                }
            }
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("Mission cannot be validated", validationErrors);
        }
    }


    public Boolean checkEquipmentsForMission(Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        Multiset<Equipment> equipmentsRequired = mission.getEquipmentsRequired();
        Set<Equipment> equipments = new HashSet<>();
        for (Equipment e : equipmentsRequired) {
            if (equipments.isEmpty()) {
                equipments.add(e);
            } else {
                for (Equipment eq : equipments) {
                    if (!(e.sameEquipments(eq))) {
                        equipments.add(e);
                    } else {
                        validationErrors.add(new ValidationError("equipmentsRequired", "mission does not satisfy equipment requirements"));
                        break;
                    }
                }
            }
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("mission does not satisfy equipment requirements", validationErrors);
        }
    }

    public Boolean checkAttributesMaxValue(Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        Map<Equipment.Attribute, Integer> maxAttributes = mission.getMaxAttributes();
        Multiset<Equipment> equipmentsRequired = mission.getEquipmentsRequired();
        Integer totalCost = 0;
        Integer totalVolume = 0;
        Integer totalWeight = 0;
        for (Equipment e : equipmentsRequired) {
            totalCost += e.getAttributes().get(Equipment.Attribute.cost);
            totalVolume += e.getAttributes().get(Equipment.Attribute.volume);
            totalWeight += e.getAttributes().get(Equipment.Attribute.weight);
        }
        if (totalCost > maxAttributes.get(Equipment.Attribute.cost)) {
            validationErrors.add(new ValidationError("cost", "total cost exceeds maximum allowed cost for the mission"));
        } else if (totalVolume > maxAttributes.get(Equipment.Attribute.volume)) {
            validationErrors.add(new ValidationError("volume", "total volume exceeds maximum allowed volume for the mission"));
        } else if (totalWeight > maxAttributes.get(Equipment.Attribute.weight)) {
            validationErrors.add(new ValidationError("weight", "total weight exceeds maximum allowed weight for the mission"));
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("mission cannot be validated", validationErrors);
        }
    }


    public boolean validatePersonFirstName(String firstName) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null == firstName || firstName.trim().equals("")) {
            validationErrors.add(new ValidationError("firstName", "firstName field can not be empty"));
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("firstName attribute cannot be validated", validationErrors);
        }
    }

    public boolean validatePersonLastName(String lastName) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null == lastName || lastName.trim().equals("")) {
            validationErrors.add(new ValidationError("lastName", "lastName field can not be empty"));
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("lastName attribute cannot be validated", validationErrors);
        }
    }

    public boolean validatePersonEmail(String email) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (email == null || email.trim().equals("")) {
            validationErrors.add(new ValidationError("email", "email cannot be null or empty"));
        }
        else {
            if (!(VALID_EMAIL_ADDRESS_REGEX.matcher(email).find())) {
                validationErrors.add(new ValidationError("email", "email field is not valid pattern "));
            }
        }

        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("email attribute cannot be validated", validationErrors);
        }
    }

    public boolean validatePersonPassword(String password) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null == password || password.trim().equals(""))
            validationErrors.add(new ValidationError("password", "password field can not be null"));

        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("password attribute cannot be validated", validationErrors);
        }
    }

    private boolean validateMaxAttribute(Mission mission) {
        return !(mission.getMaxAttributes() == null
                || mission.getMaxAttributes().isEmpty()
                || mission.getMaxAttributes().get(Equipment.Attribute.cost) <= 0
                || mission.getMaxAttributes().get(Equipment.Attribute.weight) <= 0
                || mission.getMaxAttributes().get(Equipment.Attribute.volume) <= 0);
    }

    public boolean validateEquipmentName(String name) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null == name || name.trim().equals("")) {
            validationErrors.add(new ValidationError("name", "equipment name field can not be empty"));
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("equipment name cannot be validated", validationErrors);
        }
    }

    public boolean validateEquipmentAttributes(Map<Equipment.Attribute, Integer> attributes) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (attributes.get(Equipment.Attribute.cost) <= 0 ) {
            validationErrors.add(new ValidationError("cost", "equipment cost field can not be null or less than 1"));
        } else if (attributes.get(Equipment.Attribute.weight) <= 0 ) {
            validationErrors.add(new ValidationError("weight", "equipment weight field can not be null or less than 1"));
        } else if (attributes.get(Equipment.Attribute.volume) <= 0) {
            validationErrors.add(new ValidationError("volume", "equipment volume field can not be null or less than 1"));
        }
        if (validationErrors.isEmpty()) {
            return true;
        } else {
            throw new ValidationException("equipment attributes cannot be validated", validationErrors);
        }
    }

    public boolean invitationCreatorAndRecipientCannotBeSame(Person creator, Person recipient) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (!creator.equals(recipient))
            return true;
        else {
            validationErrors.add(new ValidationError("invitation","invitation creator and recipient cannot " +
                    "be the same!"));
            throw new ValidationException("Invitation creator and recipient cannot be the same person!", validationErrors);
        }
    }

    public boolean cannnotInviteParticipant(Person recipient, Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (!mission.getParticipantSet().contains(recipient))
            return true;
        else {
            validationErrors.add(new ValidationError("invitation","Cannot invite the person" +
                    "who is already a participant of this mission!"));
            throw new ValidationException("Cannot invite the person who is already a participant of this mission!", validationErrors);
        }
    }

    public boolean stringCannotBeNullOrEmpty(String s) throws ValidationException{
        Set<ValidationError> validationErrors = Sets.newHashSet();
        if (null != s && !"".equals(s.trim()))
            return true;
        else {
            validationErrors.add(new ValidationError("string","String cannot be null or empty"));
            throw new ValidationException("String cannot be null or empty!", validationErrors);
        }
    }

    // added for extra credits

    public boolean descriptionShouldNotBeNull(String description){
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null!");
        else
            return true;
    }

    public boolean missionShouldNotNull(Mission mission){
        if (mission == null)
            throw new IllegalArgumentException("Mission should not be null.");
        else
            return true;
    }

    public boolean personShouldNotBeNull(Person person){
        if (person == null)
            throw new IllegalArgumentException("Person Should Not Be null");
        else
            return true;
    }

    public boolean invitationStatusShouldNotBeNull(Invitation.InvitationStatus status){
        if (status == null)
            throw new IllegalArgumentException("Invitation status cannot be null!");
        else
            return true;
    }

    public boolean dateShouldNotBeNull(Date date){
        if (date == null)
            throw new IllegalArgumentException("Date cannot be null!");
        else
            return true;
    }

    public boolean personSetShouldNotBeNull(Set<Person> personSet){
        if (personSet == null)
            throw new IllegalArgumentException("PersonSet should Not be null.");
        else
            return true;
    }

}

