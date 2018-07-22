package flymetomars.model;

import com.google.common.base.Objects;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import flymetomars.core.check.ValidationException;
import flymetomars.core.check.Validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
@DatabaseTable(tableName = "persons")
public class Person extends SeriablizableEntity {
    @DatabaseField
    private String firstName;

    @DatabaseField(canBeNull = false)
    private String lastName;

    @DatabaseField(canBeNull = false, unique = true)
    private String email;

    @DatabaseField(canBeNull = false)
    private String password;

    @ForeignCollectionField
    private Collection<Expertise> expertise;

    private Set<Equipment> equipmentOwned;

    private Set<Mission> missionRegistered;

    private Set<Invitation> invitationsReceived;

    // private Set<Person> friends; // for assignment3, please uncomment if need to implement

    private Validator validator;   // from assignment2 code

    public Person() {
        // from assignment2 code
        //change 1 starts
        firstName = "unknown";
        lastName = "unknown";
        email = "unknown@unknown.un";
        password = "password";
        //change 1 ends

        expertise = new HashSet<>();
        missionRegistered = new HashSet<>();
        invitationsReceived = new HashSet<>();
        equipmentOwned = new HashSet<>();
        //friends = new HashSet<>();   // for assignment3, please uncomment if need to implement

        validator = new Validator();
    }

    /**
     * Assignment3
     */

    /* please uncomment if need to implement

    public Set<Person> getFriends() {
        return friends;
    }

    public void setFriends(Set<Person> friends) {
        this.friends = friends;
    }

    */
    /**
     * From assignment code base or assignment2
     */

    // from assignment2 code
    public Person(String firstname, String lastname, String email) {
        this();
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
    }
    // end

    public Person(String email) {
        this();
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    // from assignment2 code
    public void setFirstName(String firstName) throws ValidationException {
        if (validator.validatePersonFirstName(firstName)) {
            this.firstName = firstName;
        }
    }
    // end

    public String getLastName() {
        return lastName;
    }

    // from assignment2 code
    public void setLastName(String lastName) throws ValidationException {
        if (validator.validatePersonLastName(lastName)) {
            this.lastName = lastName;
        }
    }
    // end

    public String getPassword() {
        return password;
    }

    // from assignment2 code
    public void setPassword(String password) throws ValidationException {
        validator.validatePersonPassword(password);
        this.password = password;
    }
    // end

    public String getEmail() {
        return email;
    }

    // from assignment2 code
    public void setEmail(String email) throws ValidationException {
        if (validator.validatePersonEmail(email)) {
            this.email = email;
        }
    }
    // end

    public Set<Mission> getMissionRegistered() {
        return missionRegistered;
    }

    public void setMissionRegistered(Set<Mission> missionRegistered) {
        this.missionRegistered = missionRegistered;
    }

    public Set<Invitation> getInvitationsReceived() {
        return invitationsReceived;
    }

    public void setInvitationsReceived(Set<Invitation> invitationsReceived) {
        this.invitationsReceived = invitationsReceived;
    }

    public Collection<Expertise> getExpertise() {
        return expertise;
    }

    public void setExpertise(Set<Expertise> expertise) {
        this.expertise = expertise;
    }

    public void addExpertise(Expertise... experties) {
        if (null == experties) {
            throw new IllegalArgumentException("Expertise cannot be null.");
        }
        for (Expertise exp : experties) {
            if (null == exp.getDescription()) {
                throw new IllegalArgumentException("Expertise cannot have null description.");
            } else if (exp.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Expertise cannot have empty description.");
            }
            expertise.add(exp);
        }
    }

    public Set<Equipment> getEquipmentOwned() {
        return equipmentOwned;
    }

    public void setEquipmentOwned(Set<Equipment> equipmentOwned) {
        this.equipmentOwned = equipmentOwned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equal(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
