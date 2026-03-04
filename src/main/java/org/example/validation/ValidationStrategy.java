package org.example.validation;

import org.example.exception.ValidationException;

/**
 * Generic validation interface.
 * Implementations validate a given object and throw ValidationException
 * if the object does not satisfy specific rules.
 */
public interface ValidationStrategy<T> {

    void validate(T entity) throws ValidationException;
}
