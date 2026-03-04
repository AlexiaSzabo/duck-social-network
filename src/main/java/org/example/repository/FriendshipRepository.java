package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.Friendship;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing friendships between users.
 * This table has a composite primary key (user1_id, user2_id).
 * Used for the social graph (communities), DFS, and sociability analysis.
 */
public class FriendshipRepository {

    /**
     * Saves a new friendship relationship.
     * Users must already exist in the "users" table.
     */
    public void save(Friendship f) {
        String sql = """
                INSERT INTO friendships(user1_id, user2_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            // Ensure consistent ordering (lower ID always first)
            int u1 = Math.min(f.getUser1Id(), f.getUser2Id());
            int u2 = Math.max(f.getUser1Id(), f.getUser2Id());

            stmt.setInt(1, u1);
            stmt.setInt(2, u2);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to add friendship", e);
        }
    }

    /**
     * Deletes a friendship between two users.
     * Also uses sorted IDs to match the stored format.
     */
    public void delete(int user1, int user2) {
        String sql = """
                DELETE FROM friendships
                WHERE user1_id = ? AND user2_id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, Math.min(user1, user2));
            stmt.setInt(2, Math.max(user1, user2));
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete friendship", e);
        }
    }

    /**
     * Returns all friendships.
     * Used for drawing the social graph and building connected components via DFS.
     */
    public List<Friendship> findAll() {
        String sql = """
                SELECT user1_id, user2_id
                FROM friendships
                """;

        List<Friendship> list = new ArrayList<>();

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Friendship(
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id")
                ));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all friendships", e);
        }
    }

    /**
     * Returns the IDs of all direct friends of the given user.
     * Used for DFS traversal to discover communities.
     */
    public List<Integer> findFriendsOf(int userId) {
        String sql = """
                SELECT user1_id, user2_id
                FROM friendships
                WHERE user1_id = ? OR user2_id = ?
                """;

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int u1 = rs.getInt("user1_id");
                int u2 = rs.getInt("user2_id");

                // If userId is u1, friend is u2; otherwise friend is u1
                list.add(u1 == userId ? u2 : u1);
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch friends of user", e);
        }
    }


    public long count() {
        String sql = "SELECT COUNT(*) FROM friendships";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count friendships", e);
        }
    }

    public List<Friendship> findPage(int limit, int offset) {
        String sql = """
            SELECT user1_id, user2_id
            FROM friendships
            ORDER BY user1_id, user2_id
            LIMIT ? OFFSET ?
            """;

        List<Friendship> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Friendship(
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id")
                ));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to load friendship page", e);
        }
    }

}
