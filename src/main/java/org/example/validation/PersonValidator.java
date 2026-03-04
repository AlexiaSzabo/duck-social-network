package org.example.validation;

import org.example.domain.Person;
import org.example.exception.ValidationException;

/**
 * Validates Person objects before they are persisted in the database.
 */
public class PersonValidator implements ValidationStrategy<Person> {

    @Override
    public void validate(Person person) {

        if (person == null) {
            throw new ValidationException("Person must not be null");
        }

        if (person.getId() != null && person.getId() <= 0) {
            throw new ValidationException("Person ID must be a positive number");
        }

        if (person.getNume() == null || person.getNume().isBlank()) {
            throw new ValidationException("Person last name (nume) must not be empty");
        }

        if (person.getPrenume() == null || person.getPrenume().isBlank()) {
            throw new ValidationException("Person first name (prenume) must not be empty");
        }

        if (person.getOcupatie() == null || person.getOcupatie().isBlank()) {
            throw new ValidationException("Person occupation must not be empty");
        }

        if (person.getNivelEmpatie() < 0 || person.getNivelEmpatie() > 10) {
            throw new ValidationException("Empathy level must be between 0 and 10");
        }

        if (person.getDataNasterii() == null) {
            throw new ValidationException("Birth date must not be null");
        }
    }
}
