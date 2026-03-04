package org.example.validation;

import org.example.domain.User;
import org.example.exception.ValidationException;

/**
 * Validates User objects before they are persisted to the database.
 */
public class UserValidator implements ValidationStrategy<User> {

    @Override
    public void validate(User user) {
        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("Username must not be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email must not be empty");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email format is not valid");
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new ValidationException("Password must have at least 4 characters");
        }
    }
}
