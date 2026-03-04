package org.example.domain;

/**
 * Represents a card stored in the "cards" table.
 * A card belongs to a specific user (owner_id).
 */
public class Card extends Entity {

    private String name;
    private CardType type;
    private Integer ownerId;

    public Card(Integer id, String name, CardType type, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
    }


    public String getName() {
        return name;
    }
    public CardType getType() {
        return type;
    }
    public Integer getOwnerId() {
        return ownerId;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", ownerId=" + ownerId +
                '}';
    }

}
