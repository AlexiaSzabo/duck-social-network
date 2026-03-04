package org.example.service;

import org.example.event.Event;
import org.example.event.RaceEvent;
import org.example.exception.NotFoundException;
import org.example.repository.EventRepository;
import org.example.repository.RaceEventRepository;
import org.example.validation.ValidationStrategy;

public class RaceEventService {

    private final RaceEventRepository raceRepo;
    private final EventRepository baseRepo;
    private final ValidationStrategy<RaceEvent> validator;

    public RaceEventService(
            RaceEventRepository raceRepo,
            EventRepository baseRepo,
            ValidationStrategy<RaceEvent> validator) {
        this.raceRepo = raceRepo;
        this.baseRepo = baseRepo;
        this.validator = validator;
    }

    /**
     * Adds a new RaceEvent:
     * 1. Validate
     * 2. Save base event in `events` (includes creator_id)
     * 3. Save race-specific data in `race_events` (lanes)
     */
    public RaceEvent addRaceEvent(RaceEvent raceEvent) {
        validator.validate(raceEvent);

        // Save base information (id, name, creatorId)
        Event savedBase = baseRepo.save(raceEvent);

        // Save race-specific info (lanes)
        RaceEvent savedRace = raceRepo.save(raceEvent);

        // Notify observers (subscribers)
        savedRace.notifySubscribers(
                "Race event '" + savedRace.getName() + "' has been created!"
        );

        return savedRace;
    }

    /**
     * Get a RaceEvent by ID.
     */
    public RaceEvent getRaceEvent(int id) {
        return raceRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("RaceEvent", id));
    }

    /**
     * Deletes both race data and base event.
     */
    public void deleteRaceEvent(int id) {
        raceRepo.delete(id);
        baseRepo.delete(id);
    }
}
