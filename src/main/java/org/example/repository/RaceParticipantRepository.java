package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.Duck;
import org.example.event.RaceParticipant;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository that manages participants of race events.
 *
 * race_participants table:
 * - event_id (FK to race_events)
 * - duck_id  (FK to ducks)
 *
 * Primary key: (event_id, duck_id)
 */
public class RaceParticipantRepository {

    /**
     * Adds a duck as a participant in a race event.
     * Both IDs must already exist in database.
     */
    public void save(RaceParticipant p) {
        String sql = """
                INSERT INTO race_participants(event_id, duck_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, p.getEventId());
            stmt.setInt(2, p.getDuckId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to add participant to race event", ex);
        }
    }

    /**
     * Removes a duck from a race event.
     */
    public void delete(int eventId, int duckId) {
        String sql = """
                DELETE FROM race_participants
                WHERE event_id = ? AND duck_id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, duckId);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to remove participant from race event", ex);
        }
    }

    /**
     * Returns the list of duck IDs participating in a race event.
     * Used in UI to show participants or validate if a duck is already in the event.
     */
    public List<Integer> findParticipants(int eventId) {
        String sql = """
                SELECT duck_id
                FROM race_participants
                WHERE event_id = ?
                """;

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("duck_id"));
            }

            return list;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to fetch race participants", ex);
        }
    }

    /**
     * Returns all events in which a specific duck participates.
     */
    public List<Integer> findEventsOfDuck(int duckId) {
        String sql = """
                SELECT event_id
                FROM race_participants
                WHERE duck_id = ?
                """;

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, duckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("event_id"));
            }

            return list;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to fetch events of duck", ex);
        }
    }
}
