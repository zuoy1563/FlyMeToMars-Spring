package flymetomars.core.check;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class ValidationException extends Exception {
    private Set<ValidationError> validationErrors;

    public ValidationException(Set<ValidationError> validationErrors) {
        super();
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message, Set<ValidationError> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Set<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
