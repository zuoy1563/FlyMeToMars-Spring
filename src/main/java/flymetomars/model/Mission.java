package flymetomars.model;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import flymetomars.core.check.ValidationException;
import flymetomars.core.check.Validator;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
@DatabaseTable(tableName = "missions")
public class Mission extends SeriablizableEntity {
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        preliminary, finalised
    }

    @DatabaseField(canBeNull = false)
    private Date time;

    private int budget;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private int duration;

    @DatabaseField(canBeNull = false)
    private Status status;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Person captain;

    @DatabaseField(canBeNull = false)
    private String location;

    @DatabaseField()
    private String description;

    private Set<Person> participantSet;
    private Set<Invitation> invitationSet;
    private Set<Expertise> expertiseRequired;
    private Multiset<Equipment> equipmentsRequired;

    private Map<Equipment.Attribute, Integer> maxAttributes;

    public Mission() {
        // from assignment 2 code
        time = new Date();
        name = "unknown";
        captain = new Person();
        location = "unknown";
        description = "unknown";
        // end
        status = Status.preliminary;
        invitationSet = new HashSet<>();
        participantSet = new HashSet<>();
        expertiseRequired = new HashSet<>();
        maxAttributes = new HashMap<>();
        equipmentsRequired = HashMultiset.create();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException{
        Validator validator = new Validator();
        validator.stringCannotBeNullOrEmpty(name);
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        Validator validator = new Validator();
        validator.dateShouldNotBeNull(time);
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws ValidationException{
        Validator validator = new Validator();
        validator.stringCannotBeNullOrEmpty(location);
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Validator validator = new Validator();
        validator.descriptionShouldNotBeNull(description);
        this.description = description;
    }

    public Set<Invitation> getInvitationSet() {
        return invitationSet;
    }

    public void setInvitationSet(Set<Invitation> invitationSet) {
        this.invitationSet = invitationSet;
    }

    public Set<Person> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(Set<Person> participantSet) {
        Validator validator = new Validator();
        validator.personSetShouldNotBeNull(participantSet);
        this.participantSet = participantSet;
    }

    public Person getCaptain() {
        return captain;
    }

    public void setCaptain(Person captain) {
        Validator validator = new Validator();
        validator.personShouldNotBeNull(captain);
        this.captain = captain;
    }

    public Multiset<Equipment> getEquipmentsRequired() {
        return equipmentsRequired;
    }

    public void setEquipmentsRequired(Multiset<Equipment> equipmentsRequired) {
        this.equipmentsRequired = equipmentsRequired;
    }

    public Set<Expertise> getExpertiseRequired() {
        return expertiseRequired;
    }

    public void setExpertiseRequired(Set<Expertise> expertiseRequired) {
        this.expertiseRequired = expertiseRequired;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Map<Equipment.Attribute, Integer> getMaxAttributes() {
        return maxAttributes;
    }

    public void setMaxAttributes(Map<Equipment.Attribute, Integer> maxAttributes) {
        this.maxAttributes = maxAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return duration == mission.duration &&
                Objects.equal(time, mission.time) &&
                Objects.equal(name, mission.name) &&
                Objects.equal(location, mission.location) &&
                Objects.equal(captain, mission.getCaptain()) &&
                Objects.equal(description, mission.description) &&
                Objects.equal(maxAttributes, mission.maxAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(time, name, duration, location, captain, description, maxAttributes);
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
