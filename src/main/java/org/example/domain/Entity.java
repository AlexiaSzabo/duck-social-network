package org.example.domain;

/**
 * Base class for all persistent entities.
 * Contains the database-generated ID value.
 */
public abstract class Entity {
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
