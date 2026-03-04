package org.example.validation;

import org.example.domain.Card;
import org.example.exception.ValidationException;

public class CardValidator implements ValidationStrategy<Card> {

    @Override
    public void validate(Card card) {
        if (card == null)
            throw new ValidationException("Card cannot be null.");

        if (card.getName() == null || card.getName().isBlank())
            throw new ValidationException("Card name cannot be empty.");

        if (card.getType() == null)
            throw new ValidationException("Card type must be flying or swimming.");
    }
}
