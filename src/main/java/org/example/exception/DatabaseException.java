package org.example.exception;

/**
 * Wraps low-level database errors (SQLExceptions etc.)
 * so that upper layers do not depend on SQL classes directly.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
