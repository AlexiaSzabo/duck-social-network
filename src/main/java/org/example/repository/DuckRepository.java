package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.domain.Duck;
import org.example.domain.TipRata;
import org.example.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DuckRepository {

    public Optional<Duck> findById(int id) {
        String sql = """
            SELECT u.username, u.email, u.password,
                   d.tip, d.viteza, d.rezistenta
            FROM ducks d
            JOIN users u ON u.id = d.user_id
            WHERE d.user_id = ?
            """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            return Optional.of(new Duck(
                    id,
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    TipRata.valueOf(rs.getString("tip")),
                    rs.getDouble("viteza"),
                    rs.getDouble("rezistenta")
            ));

        } catch (SQLException e) {
            throw new DatabaseException("Find duck by id failed", e);
        }
    }

    public Duck save(Duck duck) {
        String sql = """
            INSERT INTO ducks(user_id, tip, viteza, rezistenta)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, duck.getId());
            stmt.setString(2, duck.getTip().name()); // ENUM
            stmt.setDouble(3, duck.getViteza());
            stmt.setDouble(4, duck.getRezistenta());

            stmt.executeUpdate();
            return duck;

        } catch (SQLException e) {
            throw new DatabaseException("Save duck failed", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM ducks WHERE user_id = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Delete duck failed", e);
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM ducks";

        try (Statement stmt =
                     DatabaseConnection.getConnection().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new DatabaseException("Count ducks failed", e);
        }
    }

    public long countByType(TipRata type) {
        String sql = "SELECT COUNT(*) FROM ducks WHERE tip = ?";

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new DatabaseException("Count ducks by type failed", e);
        }
    }


    public List<Duck> getDuckByPage(int page, int pageSize, TipRata type) {
        int offset = (page - 1) * pageSize;

        String sql = """
            SELECT u.id, u.username, u.email, u.password,
                   d.tip, d.viteza, d.rezistenta
            FROM ducks d
            JOIN users u ON u.id = d.user_id
            %s
            ORDER BY u.id
            LIMIT ? OFFSET ?
            """;

        String where = (type == null) ? "" : "WHERE d.tip = ?";
        sql = String.format(sql, where);

        List<Duck> list = new ArrayList<>();

        try (PreparedStatement stmt =
                     DatabaseConnection.getConnection().prepareStatement(sql)) {

            int idx = 1;

            if (type != null) {
                stmt.setString(idx++, type.name()); // ENUM
            }

            stmt.setInt(idx++, pageSize);
            stmt.setInt(idx, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Duck(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        TipRata.valueOf(rs.getString("tip")),
                        rs.getDouble("viteza"),
                        rs.getDouble("rezistenta")
                ));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Get duck page failed", e);
        }
    }
}
