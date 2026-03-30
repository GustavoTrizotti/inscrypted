package br.edu.ifsp.scl.inscrypted;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Card> cards = new ArrayList<>();

    public void drawInitialHand() {
        for (int i = 0; i < 5; i++) {
            cards.add(new Card());
        }
    }

    public List<Card> getHand() {
        return List.copyOf(cards);
    }
}
