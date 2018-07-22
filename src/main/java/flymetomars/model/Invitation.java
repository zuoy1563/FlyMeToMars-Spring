package flymetomars.model;

import com.google.common.base.Objects;
import com.j256.ormlite.field.DatabaseField;
import flymetomars.core.check.ValidationException;
import flymetomars.core.check.Validator;

import java.util.Date;

/**
 * Created by yli on 10/03/15.
 */
public class Invitation extends SeriablizableEntity {

    public enum InvitationStatus {
        SENT("sent"),
        CREATED("created"),
        ACCEPTED("accepted"),
        DECLINED("declined");

        private String name;

        private InvitationStatus(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Mission mission;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Person creator;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Person recipient;

    @DatabaseField(canBeNull = false)
    private Date lastUpdated;

    @DatabaseField(canBeNull = false)
    private InvitationStatus status;

    public Invitation() {
        // from assignment2 code

        //change 1 starts
        mission = new Mission();
        creator = new Person();
        recipient = new Person();
        lastUpdated = new Date();
        status = InvitationStatus.ACCEPTED;
        //change 1 ends
    }

    public Mission getMission() {
        return mission;
    }


    public void setMission(Mission mission) {
        Validator validator = new Validator();
        validator.missionShouldNotNull(mission);
        this.mission = mission;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        Validator validator = new Validator();
        validator.personShouldNotBeNull(creator);
        this.creator = creator;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        Validator validator = new Validator();
        validator.invitationStatusShouldNotBeNull(status);
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        Validator validator = new Validator();
        validator.dateShouldNotBeNull(lastUpdated);
        this.lastUpdated = lastUpdated;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) throws ValidationException {
        // modified for extra credits
        Validator v = new Validator();
        v.personShouldNotBeNull(recipient);
        v.invitationCreatorAndRecipientCannotBeSame(creator,recipient);
        v.cannnotInviteParticipant(recipient,mission);

        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation that = (Invitation) o;
        return Objects.equal(mission, that.mission) &&
                Objects.equal(creator, that.creator) &&
                Objects.equal(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mission, creator, recipient);
    }

}
