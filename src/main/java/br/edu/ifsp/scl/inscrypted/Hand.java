package br.edu.ifsp.scl.inscrypted;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public Hand() {
    }

    public void draw(Card card) {
        cards.add(card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public List<Card> getCards() {
        return List.copyOf(cards);
    }
}
