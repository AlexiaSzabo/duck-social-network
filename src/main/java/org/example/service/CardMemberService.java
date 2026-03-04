package org.example.service;

import org.example.domain.CardMember;
import org.example.repository.CardMemberRepository;

import java.util.List;

/**
 * Handles many-to-many membership between ducks and cards.
 */
public class CardMemberService {

    private final CardMemberRepository repo;

    public CardMemberService(CardMemberRepository repo) {
        this.repo = repo;
    }

    /**
     * Adds a duck to a card.
     */
    public void addDuckToCard(int cardId, int duckId) {
        repo.save(new CardMember(cardId, duckId));
    }

    /**
     * Removes a duck from a card.
     */
    public void removeDuckFromCard(int cardId, int duckId) {
        repo.delete(cardId, duckId);
    }

    /**
     * Returns all ducks inside a card.
     */
    public List<Integer> getDucksInCard(int cardId) {
        return repo.findDucksByCard(cardId);
    }

    /**
     * Returns all cards a duck belongs to.
     */
    public List<Integer> getCardsOfDuck(int duckId) {
        return repo.findCardsOfDuck(duckId);
    }
}
