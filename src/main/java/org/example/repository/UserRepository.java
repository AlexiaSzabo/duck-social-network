package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.User;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<Integer, User> {

    @Override
    public Optional<User> findById(Integer id) {
        String sql = """
                SELECT id, username, email, password
                FROM users
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password") // deja hash
            );
            return Optional.of(user);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to query user by id", e);
        }
    }

    @Override
    public User save(User user) {
        String sql = """
                INSERT INTO users(username, email, password)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // hash deja pregatit

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) user.setId(rs.getInt("id"));

            return user;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save user", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete user", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(id) FROM users";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count users", e);
        }
    }

    public List<User> findAll() {
        String sql = """
                SELECT id, username, email, password
                FROM users
                """;

        List<User> list = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch all users", e);
        }
    }

    public void updatePassword(User user) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update password", e);
        }
    }

    public void update(User user) {
        String sql = """
        UPDATE users
        SET username = ?, email = ?, password = ?
        WHERE id = ?
        """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update user", e);
        }
    }



}
