package org.example.repository;

import java.util.Optional;

/**
 * Generic repository interface used for all persistent entities.
 * Defines basic CRUD operations: find, save, delete, count.
 */
public interface Repository<ID, T> {

    /**
     * Searches an entity by its ID.
     * @return Optional containing the entity if found, or empty otherwise.
     */
    Optional<T> findById(ID id);

    /**
     * Saves an entity into the database.
     * @return The saved entity (with ID populated if generated).
     */
    T save(T entity);

    /**
     * Deletes the entity with the given ID.
     */
    void delete(ID id);

    /**
     * @return Total number of records in the table.
     */
    long count();
}
