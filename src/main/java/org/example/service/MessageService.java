package org.example.service;

import org.example.domain.Message;
import org.example.domain.User;
import org.example.repository.MessageRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MessageService {

    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public Message sendMessage(User from, List<User> to, String text, Message replyTo) {
        Message message = new Message(null, from, to, text, LocalDateTime.now(), replyTo);
        try {
            return repo.save(message);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getConversation(User user1, User user2) {
        try {
            return repo.findConversation(user1.getId(), user2.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

//    public List<Message> getAllMessages() {
//        try {
//            return repo.findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return List.of();
//        }
//    }
}
