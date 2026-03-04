package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.CardMember;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing the many-to-many relationship between cards and ducks.
 * Each row represents one duck belonging to one card.
 * Primary key: (card_id, duck_id)
 */
public class CardMemberRepository {

    /**
     * Adds a duck into a card.
     * Both IDs must already exist in database (foreign keys).
     */
    public void save(CardMember m) {
        String sql = """
                INSERT INTO card_members(card_id, duck_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, m.getCardId());
            stmt.setInt(2, m.getDuckId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to add duck to card", e);
        }
    }

    /**
     * Removes one duck from one card.
     */
    public void delete(int cardId, int duckId) {
        String sql = """
                DELETE FROM card_members
                WHERE card_id = ? AND duck_id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, cardId);
            stmt.setInt(2, duckId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to remove duck from card", e);
        }
    }

    /**
     * Finds all ducks that belong to a specific card.
     * Used when showing card members in UI.
     */
    public List<Integer> findDucksByCard(int cardId) {
        String sql = """
                SELECT duck_id
                FROM card_members
                WHERE card_id = ?
                """;

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, cardId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("duck_id"));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get ducks from card", e);
        }
    }

    /**
     * Finds all cards that a duck is a member of.
     * Useful if you want to show duck participation.
     */
    public List<Integer> findCardsOfDuck(int duckId) {
        String sql = """
                SELECT card_id
                FROM card_members
                WHERE duck_id = ?
                """;

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, duckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("card_id"));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get cards for duck", e);
        }
    }
}
