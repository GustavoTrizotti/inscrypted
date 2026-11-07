package br.edu.ifsp.scl.core;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    Hand() {
    }

    void draw(Card card) {
        cards.add(card);
    }

    void remove(Card card) {
        cards.remove(card);
    }

    List<Card> getCards() {
        return List.copyOf(cards);
    }
}
