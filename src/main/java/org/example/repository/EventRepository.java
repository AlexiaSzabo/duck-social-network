package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.event.Event;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRepository implements Repository<Integer, Event> {

    @Override
    public Optional<Event> findById(Integer id) {
        String sql = """
                SELECT id, name, creator_id
                FROM events
                WHERE id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return Optional.empty();

            Event e = new Event(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("creator_id")
            );

            return Optional.of(e);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to query event by id", ex);
        }
    }

    @Override
    public Event save(Event event) {
        String sql = """
                INSERT INTO events(name, creator_id)
                VALUES (?, ?)
                RETURNING id
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setInt(2, event.getCreatorId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                event.setId(rs.getInt("id"));
            }

            return event;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to save event", ex);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM events WHERE id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to delete event", ex);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(id) FROM events";

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to count events", ex);
        }
    }

    public List<Event> findAll() {
        String sql = """
                SELECT id, name, creator_id
                FROM events
                """;

        List<Event> list = new ArrayList<>();

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("creator_id")
                ));
            }

            return list;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to fetch all events", ex);
        }
    }
}
