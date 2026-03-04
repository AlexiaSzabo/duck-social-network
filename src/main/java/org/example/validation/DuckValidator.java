package org.example.validation;

import org.example.domain.Duck;
import org.example.exception.ValidationException;

/**
 * Validates Duck objects before they are persisted to the database.
 */
public class DuckValidator implements ValidationStrategy<Duck> {

    @Override
    public void validate(Duck duck) {
        if (duck == null) {
            throw new ValidationException("Duck must not be null");
        }

        if (duck.getTip() == null) {
            throw new ValidationException("Duck type (TipRata) must not be null");
        }

        if (duck.getViteza() <= 0) {
            throw new ValidationException("Duck speed must be positive");
        }

        if (duck.getRezistenta() <= 0) {
            throw new ValidationException("Duck stamina must be positive");
        }
    }
}
