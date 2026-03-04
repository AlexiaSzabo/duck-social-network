package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.Card;
import org.example.domain.CardType;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Integer, Card> {

    @Override
    public Optional<Card> findById(Integer id) {
        String sql = """
                SELECT id, name, type, owner_id
                FROM cards
                WHERE id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return Optional.empty();

            Card card = new Card(
                    rs.getInt("id"),
                    rs.getString("name"),
                    CardType.valueOf(rs.getString("type")),
                    rs.getInt("owner_id")
            );

            return Optional.of(card);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to query card by id", ex);
        }
    }

    @Override
    public Card save(Card c) {
        String sql = """
                INSERT INTO cards(name, type, owner_id)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getType().name());
            stmt.setInt(3, c.getOwnerId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                c.setId(rs.getInt("id"));
            }

            return c;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to save card", ex);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM cards WHERE id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to delete card", ex);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(id) FROM cards";

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to count cards", ex);
        }
    }

    public List<Card> findAll() {
        String sql = """
                SELECT id, name, type, owner_id
                FROM cards
                """;

        List<Card> cards = new ArrayList<>();

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cards.add(new Card(
                        rs.getInt("id"),
                        rs.getString("name"),
                        CardType.valueOf(rs.getString("type")),
                        rs.getInt("owner_id")
                ));
            }

            return cards;

        } catch (SQLException ex) {
            throw new DatabaseException("Failed to fetch cards", ex);
        }
    }
}
