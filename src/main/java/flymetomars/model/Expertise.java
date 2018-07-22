package flymetomars.model;

import com.google.common.base.Objects;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yli on 16/03/15.
 */
@DatabaseTable(tableName = "expertise")
public class Expertise extends SeriablizableEntity {
    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Person holder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expertise expertise = (Expertise) o;
        return Objects.equal(description, expertise.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }

    public Expertise() {
        description = new String();
    }

    public Expertise(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (null == description) {
            throw new IllegalArgumentException("Description cannot be null.");
        }
        // according to website, it can be empty(when register, expertise can be blank)
        this.description = description;
    }

    public Person getHolder() {
        return holder;
    }

    public void setHolder(Person holder) {
        this.holder = holder;
    }
}
