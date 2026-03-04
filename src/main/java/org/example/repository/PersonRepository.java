package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.Person;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.Optional;

/**
 * Repository for accessing and modifying Person entities.
 * A Person extends a User through the same ID (user_id).
 * This repository handles only the "persons" table.
 */
public class PersonRepository implements Repository<Integer, Person> {

    /**
     * Searches for a Person using the user_id (which is the same as the User id).
     * Returns Optional.empty() if no Person exists with the given ID.
     */
    @Override
    public Optional<Person> findById(Integer id) {
        String sql = """
                SELECT nume, prenume, ocupatie, nivel_empatie, data_nasterii
                FROM persons
                WHERE user_id = ?
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return Optional.empty();

            Person p = new Person(
                    id,
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("ocupatie"),
                    rs.getDouble("nivel_empatie"),
                    rs.getDate("data_nasterii").toLocalDate()
            );

            return Optional.of(p);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to query person by id", e);
        }
    }

    /**
     * Inserts a new Person entry.
     * IMPORTANT: user_id must already exist in the users table.
     * This method does NOT generate an ID because the Person uses the User ID.
     */
    @Override
    public Person save(Person person) {
        String sql = """
                INSERT INTO persons(user_id, nume, prenume, ocupatie, nivel_empatie, data_nasterii)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, person.getId());
            stmt.setString(2, person.getNume());
            stmt.setString(3, person.getPrenume());
            stmt.setString(4, person.getOcupatie());
            stmt.setDouble(5, person.getNivelEmpatie());
            stmt.setDate(6, Date.valueOf(person.getDataNasterii()));

            stmt.executeUpdate();
            return person;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save person", e);
        }
    }

    /**
     * Deletes the Person entry from the table.
     * The associated User is NOT deleted here, but usually you delete the user,
     * and CASCADE handles the Person deletion automatically.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM persons WHERE user_id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete person", e);
        }
    }

    /**
     * Counts how many Person entries exist.
     * Uses COUNT(user_id) instead of SELECT * (professor requirement).
     */
    @Override
    public long count() {
        String sql = "SELECT COUNT(user_id) FROM persons";

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to count persons", e);
        }
    }

    public java.util.List<Person> findAll() {
        String sql = """
            SELECT u.id, u.username, u.email, u.password,
                   p.nume, p.prenume, p.ocupatie, p.nivel_empatie, p.data_nasterii
            FROM persons p
            JOIN users u ON u.id = p.user_id
            ORDER BY u.id
            """;

        java.util.List<Person> list = new java.util.ArrayList<>();

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Person p = new Person(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie"),
                        rs.getDate("data_nasterii").toLocalDate()
                );
                list.add(p);
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to load all persons", e);
        }
    }


    public java.util.List<Person> findPage(int limit, int offset) {
        String sql = """
            SELECT u.id, u.username, u.email, u.password,
                   p.nume, p.prenume, p.ocupatie, p.nivel_empatie, p.data_nasterii
            FROM persons p
            JOIN users u ON u.id = p.user_id
            ORDER BY u.id
            LIMIT ? OFFSET ?
            """;

        java.util.List<Person> list = new java.util.ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Person p = new Person(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("ocupatie"),
                        rs.getDouble("nivel_empatie"),
                        rs.getDate("data_nasterii").toLocalDate()
                );
                list.add(p);
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to load person page", e);
        }
    }



}
