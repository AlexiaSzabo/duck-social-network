package org.example.validation;

import org.example.event.RaceEvent;
import org.example.exception.ValidationException;

public class RaceEventValidator implements ValidationStrategy<RaceEvent> {

    @Override
    public void validate(RaceEvent raceEvent) {
        if (raceEvent == null)
            throw new ValidationException("RaceEvent must not be null");

        if (raceEvent.getName() == null || raceEvent.getName().isBlank())
            throw new ValidationException("RaceEvent name must not be empty");

        if (raceEvent.getCreatorId() == null)
            throw new ValidationException("RaceEvent creator id must not be null");

        if (raceEvent.getLanes() <= 0)
            throw new ValidationException("RaceEvent lanes number must be positive");
    }
}
