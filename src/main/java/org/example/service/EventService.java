package org.example.service;

import org.example.event.Event;
import org.example.exception.NotFoundException;
import org.example.repository.EventRepository;
import org.example.validation.ValidationStrategy;

import java.util.List;

public class EventService {

    private final EventRepository repo;
    private final ValidationStrategy<Event> validator;

    public EventService(EventRepository repo, ValidationStrategy<Event> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    /**
     * Validates and adds a new event.
     */
    public Event addEvent(Event e) {
        validator.validate(e);
        Event saved = repo.save(e);

        // Notificare automată la creare (opțional)
        saved.notifySubscribers("Evenimentul '" + saved.getName() + "' a fost creat.");

        return saved;
    }

    /**
     * Returns an event by ID.
     */
    public Event getEvent(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Event", id));
    }

    public void deleteEvent(int id) {
        repo.delete(id);
    }

    public List<Event> getAllEvents() {
        return repo.findAll();
    }
}
