package org.example.validation;

import org.example.event.Event;
import org.example.exception.ValidationException;

public class EventValidator implements ValidationStrategy<Event> {

    @Override
    public void validate(Event event) {
        if (event == null)
            throw new ValidationException("Event must not be null");

        if (event.getName() == null || event.getName().isBlank())
            throw new ValidationException("Event name must not be empty");

        if (event.getSubscribers() == null)
            throw new ValidationException("Subscribers list must not be null");
    }
}
