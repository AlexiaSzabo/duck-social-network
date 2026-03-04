package org.example.service;

import org.example.event.RaceParticipant;
import org.example.repository.RaceParticipantRepository;

import java.util.List;

/**
 * Manages participants of a race event.
 */
public class RaceParticipantService {

    private final RaceParticipantRepository repo;

    public RaceParticipantService(RaceParticipantRepository repo) {
        this.repo = repo;
    }

    /**
     * Adds a duck to a race event.
     */
    public void addParticipant(int eventId, int duckId) {
        repo.save(new RaceParticipant(eventId, duckId));
    }

    /**
     * Removes a duck from a race event.
     */
    public void removeParticipant(int eventId, int duckId) {
        repo.delete(eventId, duckId);
    }

    /**
     * Returns all ducks participating in an event.
     */
    public List<Integer> getParticipants(int eventId) {
        return repo.findParticipants(eventId);
    }
}
