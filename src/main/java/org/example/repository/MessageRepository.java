package org.example.repository;

import org.example.domain.Message;
import org.example.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageRepository {

    private final Connection connection;

    public MessageRepository(Connection connection) {
        this.connection = connection;
    }


    public Message save(Message message) throws SQLException {

        String insertMessageSQL = """
        INSERT INTO messages (from_user, message, date, reply_to)
        VALUES (?, ?, ?, ?)
        RETURNING id
    """;

        Integer newId = null;

        try (PreparedStatement stmt = connection.prepareStatement(insertMessageSQL)) {
            stmt.setInt(1, message.getFrom().getId());
            stmt.setString(2, message.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(message.getTimestamp()));

            if (message.getReplyTo() != null)
                stmt.setInt(4, message.getReplyTo().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                newId = rs.getInt("id");
        }

        Message savedMessage = new Message(
                newId,
                message.getFrom(),
                message.getTo(),
                message.getMessage(),
                message.getTimestamp(),
                message.getReplyTo()
        );

        String insertReceiverSQL = """
        INSERT INTO message_receivers (message_id, user_id)
        VALUES (?, ?)
    """;

        try (PreparedStatement stmt = connection.prepareStatement(insertReceiverSQL)) {
            for (User u : message.getTo()) {
                stmt.setInt(1, newId);
                stmt.setInt(2, u.getId());
                stmt.executeUpdate();
            }
        }

        return savedMessage;
    }



    public List<Message> findConversation(int user1Id, int user2Id) throws SQLException {

        Map<Integer, Message> messageMap = new LinkedHashMap<>();

        String sql = """
            SELECT
                m.id AS m_id,
                m.message,
                m.date,
                m.reply_to,

                uf.id AS from_id,
                uf.username AS from_username,
                uf.email AS from_email,
                uf.password AS from_password,

                ut.id AS to_id,
                ut.username AS to_username,
                ut.email AS to_email,
                ut.password AS to_password

            FROM messages m
            JOIN users uf ON m.from_user = uf.id
            JOIN message_receivers mr ON m.id = mr.message_id
            JOIN users ut ON mr.user_id = ut.id

            WHERE (m.from_user = ? AND ut.id = ?)
               OR (m.from_user = ? AND ut.id = ?)

            ORDER BY m.date ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.setInt(3, user2Id);
            stmt.setInt(4, user1Id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                int messageId = rs.getInt("m_id");

                Message message = messageMap.get(messageId);
                if (message == null) {
                    User from = new User(
                            rs.getInt("from_id"),
                            rs.getString("from_username"),
                            rs.getString("from_email"),
                            rs.getString("from_password")
                    );

                    message = new Message(
                            messageId,
                            from,
                            new ArrayList<>(),
                            rs.getString("message"),
                            rs.getTimestamp("date").toLocalDateTime(),
                            null
                    );

                    messageMap.put(messageId, message);
                }

                User to = new User(
                        rs.getInt("to_id"),
                        rs.getString("to_username"),
                        rs.getString("to_email"),
                        rs.getString("to_password")
                );

                message.getTo().add(to);
            }
        }

        // setare reply_to fara query-uri suplimentare
        for (Message m : messageMap.values()) {
            Integer replyId = getReplyId(m.getId());
            if (replyId != null) {
                m.setReplyTo(messageMap.get(replyId));
            }
        }

        return new ArrayList<>(messageMap.values());
    }


    private Integer getReplyId(int messageId) throws SQLException {
        String sql = "SELECT reply_to FROM messages WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getObject("reply_to", Integer.class);
        }
        return null;
    }
}
