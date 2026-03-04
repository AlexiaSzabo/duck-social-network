package org.example.service;

import org.example.domain.Card;
import org.example.exception.NotFoundException;
import org.example.repository.CardRepository;
import org.example.validation.ValidationStrategy;

import java.util.List;

/**
 * Service for card management.
 */
public class CardService {

    final CardRepository repo;
    private final ValidationStrategy<Card> validator;

    public CardService(CardRepository repo, ValidationStrategy<Card> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public Card addCard(Card c) {
        validator.validate(c);
        return repo.save(c);
    }

    public Card getCard(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Card", id));
    }

    public void deleteCard(int id) {
        repo.delete(id);
    }

    public long countCards() {
        return repo.count();
    }

    /**
     * Returns all cards for UI display.
     */
    public List<Card> getAllCards() {
        return repo.findAll();
    }
}
