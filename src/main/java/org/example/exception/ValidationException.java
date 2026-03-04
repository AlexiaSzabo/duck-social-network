package org.example.exception;

/**
 * Thrown when a domain object does not satisfy validation rules.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
