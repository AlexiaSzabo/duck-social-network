package org.example.domain;

/**
 * Represents a duck that belongs to a card.
 * Matches the card_members table in database.
 */
public class CardMember {

    private final Integer cardId;
    private final Integer duckId;

    public CardMember(Integer cardId, Integer duckId) {
        this.cardId = cardId;
        this.duckId = duckId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public Integer getDuckId() {
        return duckId;
    }

    @Override
    public String toString() {
        return String.format(
                "CardMember{cardId=%d, duckId=%d}",
                cardId, duckId
        );
    }

}
