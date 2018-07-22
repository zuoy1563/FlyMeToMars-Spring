package flymetomars.model;

import com.google.common.base.Objects;
import flymetomars.core.check.ValidationException;
import flymetomars.core.check.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class Equipment {
    public enum Attribute {
        weight, volume, cost
    }

    private String name;

    public Map<Attribute, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Attribute, Integer> attributes) throws ValidationException {
        if (validator.validateEquipmentAttributes(attributes)) {
            this.attributes = attributes;
        }
    }

    private Map<Attribute, Integer> attributes;
    private Validator validator;   // from assignment2 code

    public Equipment(String name, int weight, int volume, int cost) {
        this.name = name;
        this.attributes = new HashMap<>();
        attributes.put(Attribute.weight, weight);
        attributes.put(Attribute.volume, volume);
        attributes.put(Attribute.cost, cost);
        validator = new Validator();   // from assignment2 code
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equal(name, equipment.name) &&
                Objects.equal(attributes, equipment.attributes);
    }

    // from assignment2 code
    public boolean sameEquipments(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equal(name, equipment.name);
    }
    // end

    @Override
    public int hashCode() {
        return Objects.hashCode(name, attributes);
    }

    // from assignment2 code
    public void setName(String name) throws ValidationException {
        if (validator.validateEquipmentName(name)) {
            this.name = name;
        }
    }
    // end

}
