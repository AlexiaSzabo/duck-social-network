package org.example.domain;

public class FriendRequest {

    public enum Status { PENDING, APPROVED, REJECTED }

    private Integer id; // schimbat din final
    private Integer fromUserId;
    private Integer toUserId;
    private Status status;

    // Constructor gol - obligatoriu pentru repo/JavaFX
    public FriendRequest() {
        this.status = Status.PENDING; // default
    }

    // constructor fără ID (pentru creare nouă)
    public FriendRequest(Integer fromUserId, Integer toUserId, Status status) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = status;
    }

    // constructor cu ID (pentru citire din DB)
    public FriendRequest(Integer id, Integer fromUserId, Integer toUserId, Status status) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = status;
    }

    // getter + setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFromUserId() { return fromUserId; }
    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() { return toUserId; }
    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
