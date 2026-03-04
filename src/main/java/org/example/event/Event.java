package org.example.event;

import org.example.domain.Entity;
import org.example.domain.User;

import java.util.ArrayList;
import java.util.List;

public class Event extends Entity {

    protected String name;
    protected Integer creatorId;
    protected List<User> subscribers;

    public Event(Integer id, String name, Integer creatorId) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.subscribers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    // OBSERVER PATTERN
    public void subscribe(User user) {
        if (user != null && !subscribers.contains(user))
            subscribers.add(user);
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    public void notifySubscribers(String message) {
        for (User u : subscribers) {
            System.out.println("Notify " + u.getUsername() + ": " + message);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorId=" + creatorId +
                ", subscribers=" + subscribers.size() +
                '}';
    }
}
