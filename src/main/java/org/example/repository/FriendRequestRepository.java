package org.example.repository;

import org.example.domain.FriendRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRequestRepository {

    private final Connection connection;

    public FriendRequestRepository(Connection connection) {
        this.connection = connection;
    }

    public FriendRequest save(FriendRequest fr) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO friend_requests (from_user_id, to_user_id, status) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setInt(1, fr.getFromUserId());
            ps.setInt(2, fr.getToUserId());
            ps.setString(3, fr.getStatus().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) fr.setId(rs.getInt(1));

            return fr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<FriendRequest> findAll() {
        List<FriendRequest> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, from_user_id, to_user_id, status FROM friend_requests"
            );
            while (rs.next()) {
                FriendRequest fr = new FriendRequest();
                fr.setId(rs.getInt("id"));
                fr.setFromUserId(rs.getInt("from_user_id"));
                fr.setToUserId(rs.getInt("to_user_id"));
                fr.setStatus(FriendRequest.Status.valueOf(rs.getString("status")));
                list.add(fr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<FriendRequest> findById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, from_user_id, to_user_id, status FROM friend_requests WHERE id = ?"
        )) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FriendRequest fr = new FriendRequest();
                fr.setId(rs.getInt("id"));
                fr.setFromUserId(rs.getInt("from_user_id"));
                fr.setToUserId(rs.getInt("to_user_id"));
                fr.setStatus(FriendRequest.Status.valueOf(rs.getString("status")));
                return Optional.of(fr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void update(FriendRequest fr) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE friend_requests SET status = ? WHERE id = ?"
        )) {
            ps.setString(1, fr.getStatus().name());
            ps.setInt(2, fr.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
