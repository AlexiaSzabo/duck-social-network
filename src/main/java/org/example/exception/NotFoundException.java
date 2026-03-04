package org.example.exception;

/**
 * Thrown when an entity with a given ID cannot be found in the database.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String entityName, Object id) {
        super(entityName + " with id " + id + " was not found");
    }
}
