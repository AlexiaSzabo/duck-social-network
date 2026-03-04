package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message {

    private Integer id;
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime timestamp;
    private Message replyTo; // poate fi null

    public Message(Integer id, User from, List<User> to, String message, LocalDateTime timestamp, Message replyTo) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.timestamp = timestamp;
        this.replyTo = replyTo;
    }

    public Integer getId() { return id; }
    public User getFrom() { return from; }
    public List<User> getTo() { return to; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Message getReplyTo() { return replyTo; }

    public void setFrom(User from) { this.from = from; }
    public void setTo(List<User> to) { this.to = to; }
    public void setReplyTo(Message replyTo) { this.replyTo = replyTo; }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s: %s",
                timestamp, from.getUsername(),
                to.stream().map(User::getUsername).toList(), message);
    }
}
