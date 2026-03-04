package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.event.Event;
import org.example.event.RaceEvent;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.Optional;

public class RaceEventRepository {

    private final EventRepository eventRepository;

    public RaceEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Finds a RaceEvent by ID.
     * Reads lanes from race_events and creator_id from events.
     */
    public Optional<RaceEvent> findById(Integer id) {
        String sql = """
                SELECT event_id, lanes
                FROM race_events
                WHERE event_id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return Optional.empty();

            // Load the base event (contains creator_id)
            Optional<Event> baseOpt = eventRepository.findById(id);
            if (baseOpt.isEmpty())
                return Optional.empty();

            Event base = baseOpt.get();

            RaceEvent race = new RaceEvent(
                    base.getId(),
                    base.getName(),
                    base.getCreatorId(),   // <- creatorul se află în tabela events
                    rs.getInt("lanes")
            );

            return Optional.of(race);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to query race event", ex);
        }
    }

    /**
     * Saves the race-specific data into race_events.
     * The base Event must already be saved.
     */
    public RaceEvent save(RaceEvent race) {
        String sql = """
                INSERT INTO race_events(event_id, lanes)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, race.getId());
            stmt.setInt(2, race.getLanes());

            stmt.executeUpdate();
            return race;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to save race event", ex);
        }
    }

    /**
     * Deletes only the race-specific part.
     * Base event delete must be done separately.
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM race_events WHERE event_id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to delete race event", ex);
        }
    }

    /**
     * Counts how many race events exist.
     */
    public long count() {
        String sql = "SELECT COUNT(event_id) FROM race_events";

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to count race events", ex);
        }
    }
}
