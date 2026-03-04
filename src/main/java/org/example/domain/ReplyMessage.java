package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    public ReplyMessage(Integer id, User from, List<User> to, String message, LocalDateTime date, Message reply) {
        super(id, from, to, message, date, reply);
    }
}
