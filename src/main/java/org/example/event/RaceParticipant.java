package org.example.event;

/**
 * Represents the participation of a duck in a race event.
 * Matches the "race_participants" table (event_id, duck_id).
 */
public class RaceParticipant {

    private Integer eventId;
    private Integer duckId;

    public RaceParticipant(Integer eventId, Integer duckId) {
        this.eventId = eventId;
        this.duckId = duckId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public Integer getDuckId() {
        return duckId;
    }
}
