package org.example.event;

public class RaceEvent extends Event {

    private int lanes;

    public RaceEvent(Integer id, String name, Integer creatorId, int lanes) {
        super(id, name, creatorId);
        this.lanes = lanes;
    }

    public int getLanes() {
        return lanes;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
    }

    @Override
    public String toString() {
        return "RaceEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorId=" + creatorId +
                ", lanes=" + lanes +
                ", subscribers=" + subscribers.size() +
                '}';
    }
}
